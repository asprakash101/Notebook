package com.simpad.pathaknotebook.models;

public class MoreProducts {
    String productName;
    String productLink;

    public MoreProducts(String productName, String productLink) {
        this.productName = productName;
        this.productLink = productLink;
    }

    public MoreProducts() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }
}
