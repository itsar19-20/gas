package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the prezzo database table.
 * 
 */
@Entity
@Table(name="prezzo")
@NamedQuery(name="Prezzo.findAll", query="SELECT p FROM Prezzo p")
public class Prezzo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String descCarburante;

	private String dtComu;

	private int isSelf;

	private float prezzo;

	//bi-directional many-to-one association to Distributore
	@ManyToOne
	@JoinColumn(name="idImpianto")
	private Distributore distributore;

	public Prezzo() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescCarburante() {
		return this.descCarburante;
	}

	public void setDescCarburante(String descCarburante) {
		this.descCarburante = descCarburante;
	}

	public String getDtComu() {
		return this.dtComu;
	}

	public void setDtComu(String dtComu) {
		this.dtComu = dtComu;
	}

	public int getIsSelf() {
		return this.isSelf;
	}

	public void setIsSelf(int isSelf) {
		this.isSelf = isSelf;
	}

	public float getPrezzo() {
		return this.prezzo;
	}

	public void setPrezzo(float prezzo) {
		this.prezzo = prezzo;
	}

	public Distributore getDistributore() {
		return this.distributore;
	}

	public void setDistributore(Distributore distributore) {
		this.distributore = distributore;
	}

}