package com.simpad.pathaknotebook.models;

import java.util.ArrayList;

public class Banner {
    private String bannerImageUrl;
    private String bannerDescription;

    public Banner(String bannerImageUrl, String bannerDescription) {
        this.bannerImageUrl = bannerImageUrl;
        this.bannerDescription = bannerDescription;
    }

    public Banner() {

    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public String getBannerDescription() {
        return bannerDescription;
    }

    public void setBannerDescription(String bannerDescription) {
        this.bannerDescription = bannerDescription;
    }
}