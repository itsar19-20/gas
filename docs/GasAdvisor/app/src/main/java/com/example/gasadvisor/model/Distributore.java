package com.example.gasadvisor.model;

public class Distributore {
    //annotation @serializedName("name") se vogliamo
    //usare un nome diverso su android da quello che
    //ci da il json

    public Distributore(Double prezzo, String bandiera, String comune, Double latitudine, Double longitudine) {
        this.prezzo = prezzo;
        this.bandiera = bandiera;
        this.comune = comune;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    private Integer _id;

    private Double prezzo;

    public Double getPrezzo() {
        return prezzo;
    }

    private int idImpianto;

    private String bandiera;

    private String comune;

    public Integer get_id() {
        return _id;
    }

    public int getIdImpianto() {
        return idImpianto;
    }

    public String getBandiera() {
        return bandiera;
    }

    public String getComune() {
        return comune;
    }

    public String getGestore() {
        return gestore;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public Double getLatitudine() {
        return latitudine;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public String getNomeImpianto() {
        return nomeImpianto;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getTipoImpianto() {
        return tipoImpianto;
    }

    private String gestore;

    private String indirizzo;

    private Double latitudine;

    private Double longitudine;

    private String nomeImpianto;

    private String provincia;

    private String tipoImpianto;

}
