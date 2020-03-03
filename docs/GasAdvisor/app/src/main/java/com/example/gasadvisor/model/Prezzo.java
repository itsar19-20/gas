package com.example.gasadvisor.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Prezzo {

    private Integer _id;

    private String descCarburante;

    private Date dataComunicazione;

    private String dtComu;

    private int isSelf;

    private Double prezzo;

    private String id_impianto;
    private Distributore distributore;

    public Integer get_id() {
        return _id;
    }

    public String getDescCarburante() {
        return descCarburante;
    }

    public Distributore getDistributore() {
        return distributore;
    }

    public Date getDataComunicazione() {
        return dataComunicazione;
    }

    public String getDtComu() {
        return dtComu;
    }

    public int getIsSelf() {
        return isSelf;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public String getId_impianto() {
        return id_impianto;
    }
}
