package com.simpad.pathaknotebook.models;

public class ProductData {
    String Name;
    String category;
    String imageUrl;
    String tags;
    String specialDesp1;
    String specialDesp2;
    String specialDesp3;
    String description;
    String price;
    String quantity;
    String Serialnumber;

    public ProductData() {
    }

    public ProductData(String name, String category, String imageUrl, String specialDesp1, String specialDesp2, String specialDesp3, String description, String price, String quantity, String serialnumber) {
        Name = name;
        this.category = category;
        this.imageUrl = imageUrl;
        this.specialDesp1 = specialDesp1;
        this.specialDesp2 = specialDesp2;
        this.specialDesp3 = specialDesp3;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        Serialnumber = serialnumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSpecialDesp1() {
        return specialDesp1;
    }

    public void setSpecialDesp1(String specialDesp1) {
        this.specialDesp1 = specialDesp1;
    }

    public String getSpecialDesp2() {
        return specialDesp2;
    }

    public void setSpecialDesp2(String specialDesp2) {
        this.specialDesp2 = specialDesp2;
    }

    public String getSpecialDesp3() {
        return specialDesp3;
    }

    public void setSpecialDesp3(String specialDesp3) {
        this.specialDesp3 = specialDesp3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSerialnumber() {
        return Serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        Serialnumber = serialnumber;
    }
}
