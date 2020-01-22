package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the statistiche database table.
 * 
 */
@Entity
@Table(name="statistiche")
@NamedQuery(name="Statistiche.findAll", query="SELECT s FROM Statistiche s")
public class Statistiche implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.DATE)
	private Date data;

	//bi-directional many-to-one association to Distributore
	@ManyToOne
	@JoinColumn(name="idImpianto")
	private Distributore distributore;

	//bi-directional many-to-one association to Prezzo
	@ManyToOne
	@JoinColumn(name="idPrezzo")
	private Prezzo prezzo;

	public Statistiche() {
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

	public Distributore getDistributore() {
		return this.distributore;
	}

	public void setDistributore(Distributore distributore) {
		this.distributore = distributore;
	}

	public Prezzo getPrezzo() {
		return this.prezzo;
	}

	public void setPrezzo(Prezzo prezzo) {
		this.prezzo = prezzo;
	}

}