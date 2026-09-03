package com.scaler.productservice.services;

import com.scaler.productservice.exceptions.PaymentProcessingException;
import com.stripe.Stripe;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentGateway implements PaymentGateway{

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Override
    public String generatePaymentLink(String orderId, Long amount, String returnUrl, String idempotencyKey){
        // Logic to generate payment link using Stripe API

        try{
            Stripe.apiKey = stripeApiKey;

            PriceCreateParams priceParams  = PriceCreateParams.builder()
                    .setUnitAmount(amount)
                    .setCurrency("INR")
                    .setProductData(
                            PriceCreateParams.ProductData.builder().setName("Order " + orderId).build()
                    )
                    .build();
            RequestOptions requestOptions = RequestOptions.builder().setIdempotencyKey(idempotencyKey).build();

            Price price = Price.create(priceParams, requestOptions);

            PaymentLinkCreateParams linkCreateParams = PaymentLinkCreateParams.builder()
                    .addLineItem(
                            PaymentLinkCreateParams.LineItem.builder()
                                    .setPrice(price.getId())
                                    .setQuantity(1L)
                                    .build()
                    )
                    .setAfterCompletion(
                            PaymentLinkCreateParams.AfterCompletion.builder()
                                    .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                    .setRedirect(
                                            PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                    .setUrl(returnUrl)
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();
            PaymentLink paymentLink = PaymentLink.create(linkCreateParams, requestOptions);

            return paymentLink.getUrl();
        }
        catch (Exception e){

            throw new PaymentProcessingException("Failed to generate payment link using Stripe API:" + e.getMessage());
        }

    }
}
