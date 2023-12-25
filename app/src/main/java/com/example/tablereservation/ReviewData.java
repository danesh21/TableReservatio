package com.example.tablereservation;

public class ReviewData {
    private String description;
    private String foodRating;
    private String serviceRating;

    public ReviewData(String description, String foodRating, String serviceRating) {
        this.description = description;
        this.foodRating = foodRating;
        this.serviceRating = serviceRating;
    }

    // Getters and setters for each field...

    // You might want to generate these using your IDE or manually create them.
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFoodRating() {
        return foodRating;
    }

    public void setFoodRating(String foodRating) {
        this.foodRating = foodRating;
    }

    public String getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(String serviceRating) {
        this.serviceRating = serviceRating;
    }
}

