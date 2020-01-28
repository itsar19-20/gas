package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * The persistent class for the distributore database table.
 * 
 */
@Entity
@Table(name="distributore")
@NamedQuery(name="Distributore.findAll", query="SELECT d FROM Distributore d")
public class Distributore implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idImpianto;

	private String bandiera;

	private String comune;

	private String gestore;

	private String indirizzo;

	private BigDecimal latitudine;

	private BigDecimal longitudine;

	private String nomeImpianto;

	private String provincia;

	private String tipoImpianto;

	//bi-directional many-to-one association to Prezzo
	@JsonIgnore
	@OneToMany(mappedBy="distributore")
	private List<Prezzo> prezzos;

	//bi-directional many-to-one association to Statistiche
	@JsonIgnore
	@OneToMany(mappedBy="distributore")
	private List<Statistiche> statistiches;

	//bi-directional many-to-one association to Valutazione
	@JsonIgnore
	@OneToMany(mappedBy="distributore")
	private List<Valutazione> valutaziones;

	public Distributore() {
	}

	public int getIdImpianto() {
		return this.idImpianto;
	}

	public void setIdImpianto(int idImpianto) {
		this.idImpianto = idImpianto;
	}

	public String getBandiera() {
		return this.bandiera;
	}

	public void setBandiera(String bandiera) {
		this.bandiera = bandiera;
	}

	public String getComune() {
		return this.comune;
	}

	public void setComune(String comune) {
		this.comune = comune;
	}

	public String getGestore() {
		return this.gestore;
	}

	public void setGestore(String gestore) {
		this.gestore = gestore;
	}

	public String getIndirizzo() {
		return this.indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public BigDecimal getLatitudine() {
		return this.latitudine;
	}

	public void setLatitudine(BigDecimal latitudine) {
		this.latitudine = latitudine;
	}

	public BigDecimal getLongitudine() {
		return this.longitudine;
	}

	public void setLongitudine(BigDecimal longitudine) {
		this.longitudine = longitudine;
	}

	public String getNomeImpianto() {
		return this.nomeImpianto;
	}

	public void setNomeImpianto(String nomeImpianto) {
		this.nomeImpianto = nomeImpianto;
	}

	public String getProvincia() {
		return this.provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getTipoImpianto() {
		return this.tipoImpianto;
	}

	public void setTipoImpianto(String tipoImpianto) {
		this.tipoImpianto = tipoImpianto;
	}

	public List<Prezzo> getPrezzos() {
		return this.prezzos;
	}

	public void setPrezzos(List<Prezzo> prezzos) {
		this.prezzos = prezzos;
	}

	public Prezzo addPrezzo(Prezzo prezzo) {
		getPrezzos().add(prezzo);
		prezzo.setDistributore(this);

		return prezzo;
	}

	public Prezzo removePrezzo(Prezzo prezzo) {
		getPrezzos().remove(prezzo);
		prezzo.setDistributore(null);

		return prezzo;
	}

	public List<Statistiche> getStatistiches() {
		return this.statistiches;
	}

	public void setStatistiches(List<Statistiche> statistiches) {
		this.statistiches = statistiches;
	}

	public Statistiche addStatistich(Statistiche statistich) {
		getStatistiches().add(statistich);
		statistich.setDistributore(this);

		return statistich;
	}

	public Statistiche removeStatistich(Statistiche statistich) {
		getStatistiches().remove(statistich);
		statistich.setDistributore(null);

		return statistich;
	}

	public List<Valutazione> getValutaziones() {
		return this.valutaziones;
	}

	public void setValutaziones(List<Valutazione> valutaziones) {
		this.valutaziones = valutaziones;
	}

	public Valutazione addValutazione(Valutazione valutazione) {
		getValutaziones().add(valutazione);
		valutazione.setDistributore(this);

		return valutazione;
	}

	public Valutazione removeValutazione(Valutazione valutazione) {
		getValutaziones().remove(valutazione);
		valutazione.setDistributore(null);

		return valutazione;
	}

	@Override
	public String toString() {
		return nomeImpianto;
	}

}