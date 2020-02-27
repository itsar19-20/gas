package com.example.gasadvisor.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Prezzo implements Parcelable {

    private int id;

    private Date dataComunicazione;

    private String descCarburante;

    private String dtComu;

    private int isSelf;

    private Double prezzo;

    private Distributore distributore;

    protected Prezzo(Parcel in) {
        id = in.readInt();
        descCarburante = in.readString();
        dtComu = in.readString();
        isSelf = in.readInt();
        if (in.readByte() == 0) {
            prezzo = null;
        } else {
            prezzo = in.readDouble();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(descCarburante);
        parcel.writeString(dtComu);
        parcel.writeInt(isSelf);
        if (prezzo == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(prezzo);
        }
    }

    public static final Creator<Prezzo> CREATOR = new Creator<Prezzo>() {
        @Override
        public Prezzo createFromParcel(Parcel in) {
            return new Prezzo(in);
        }

        @Override
        public Prezzo[] newArray(int size) {
            return new Prezzo[size];
        }
    };

    public int getId() {
        return id;
    }

    public Date getDataComunicazione() {
        return dataComunicazione;
    }

    public String getDescCarburante() {
        return descCarburante;
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

    public Distributore getDistributore() {
        return distributore;
    }


}
