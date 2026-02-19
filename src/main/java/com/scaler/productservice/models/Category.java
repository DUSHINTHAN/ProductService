package com.scaler.productservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Category extends BaseModel {


        private String name;

        @OneToMany(mappedBy = "category", cascade = {CascadeType.REMOVE}) // This cascadeType will ensure that if a category is deleted, all the products associated with that category will also be deleted from the database.
        @JsonIgnore
        private List<Product> products;
}


