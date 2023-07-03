package com.example.graduationpartyinvitation;

public class SuggestionActivity {

    private String name, description;

    public SuggestionActivity() {
        this.name = "";
        this.description = "";
    }

    public SuggestionActivity(String name, String description) {
        this.name = name;
        this.description = description;

    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

}
