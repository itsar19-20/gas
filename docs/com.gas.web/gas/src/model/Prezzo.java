package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.DATE)
	private Date dataComunicazione;

	private String descCarburante;

	private String dtComu;

	private int isSelf;

	private float prezzo;

	//bi-directional many-to-one association to Distributore
	@ManyToOne
	@JoinColumn(name="idImpianto")
	private Distributore distributore;

	//bi-directional many-to-one association to Statistiche
	@OneToMany(mappedBy="prezzo")
	private List<Statistiche> statistiches;

	public Prezzo() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDataComunicazione() {
		return this.dataComunicazione;
	}

	public void setDataComunicazione(Date dataComunicazione) {
		this.dataComunicazione = dataComunicazione;
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

	public List<Statistiche> getStatistiches() {
		return this.statistiches;
	}

	public void setStatistiches(List<Statistiche> statistiches) {
		this.statistiches = statistiches;
	}

	public Statistiche addStatistich(Statistiche statistich) {
		getStatistiches().add(statistich);
		statistich.setPrezzo(this);

		return statistich;
	}

	public Statistiche removeStatistich(Statistiche statistich) {
		getStatistiches().remove(statistich);
		statistich.setPrezzo(null);

		return statistich;
	}

}