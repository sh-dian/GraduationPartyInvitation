package com.example.graduationpartyinvitation;

public class Feedback {
    private String type;
    private String feedback;

    public Feedback() {
        this.type = "";
        this.feedback = "";
    }

    public Feedback(String type, String feedback) {
        this.type = type;
        this.feedback = feedback;
    }

    public String getType() {
        return type;
    }

    public String getFeedback() {
        return feedback;
    }
}
