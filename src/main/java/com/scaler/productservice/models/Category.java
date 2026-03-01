package com.scaler.productservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.awt.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Category extends BaseModel {

        @Column(nullable = false)
        private String name;

        @OneToMany(mappedBy = "category", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY) // This cascadeType will ensure that if a category is deleted, all the products associated with that category will also be deleted from the database.
        @JsonIgnore
        @Fetch(FetchMode.JOIN)// this will ensure that when we fetch a category, the products associated with that category will also be fetched from the database in a single query, instead of fetching the category first and then fetching the products separately.
        private List<Product> products;
}


