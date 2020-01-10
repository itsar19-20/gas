package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the notizia database table.
 * 
 */
@Entity
@Table(name="notizia")
@NamedQuery(name="Notizia.findAll", query="SELECT n FROM Notizia n")
public class Notizia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Temporal(TemporalType.DATE)
	private Date data;

	private String localita;

	private String testo;

	public Notizia() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getData() {
		return this.data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getLocalita() {
		return this.localita;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public String getTesto() {
		return this.testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

}