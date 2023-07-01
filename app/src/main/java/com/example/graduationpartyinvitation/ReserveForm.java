package com.example.graduationpartyinvitation;

public class ReserveForm {
    private String name, phone, numberAdult, numberKids;

    public ReserveForm() {
        this.name = "";
        this.phone = "";
        this.numberAdult = "";
        this.numberKids="";
    }

    public ReserveForm(String name, String phone, String numberAdult, String numberKids) {
        this.name = name;
        this.phone = phone;
        this.numberAdult = numberAdult;
        this.numberKids= numberKids;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getNumberAdult() {
        return numberAdult;
    }

    public String getNumberKids() {
        return numberKids;
    }
}

