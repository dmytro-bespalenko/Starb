package com.hfad.starbuzz;

import androidx.annotation.NonNull;

public class Food {

    private String name;
    private String description;
    private int imageResourceId;

    public static final Food [] foods = {
//            new Food("Hamburger", "The tastiest hamburger you have ever eaten", R.drawable.hamburger),
//            new Food("Okroshka", "Best summer soup ever", R.drawable.okroshka),
//            new Food("Salad", "Just salad", R.drawable.salad)
    };

    public Food(String name, String description, int imageResourceId) {
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
