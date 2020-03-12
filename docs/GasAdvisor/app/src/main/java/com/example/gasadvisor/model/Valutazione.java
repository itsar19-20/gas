package com.example.gasadvisor.model;

import java.util.Date;

public class Valutazione {
    private int id;

    public int getId() {
        return id;
    }

    public Date getData() {
        return data;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getGiudizio() {
        return giudizio;
    }

    private Date data;

    private String descrizione;

    private int giudizio;

    private Distributore distributore;

    private User user;
}
