package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the valutazione database table.
 * 
 */
@Entity
@Table(name="valutazione")
@NamedQuery(name="Valutazione.findAll", query="SELECT v FROM Valutazione v")
public class Valutazione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.DATE)
	private Date data;

	private String descrizione;

	private int giudizio;

	//bi-directional many-to-one association to Distributore
	@ManyToOne
	@JoinColumn(name="idImpianto")
	private Distributore distributore;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="username")
	private User user;

	public Valutazione() {
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

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getGiudizio() {
		return this.giudizio;
	}

	public void setGiudizio(int giudizio) {
		this.giudizio = giudizio;
	}

	public Distributore getDistributore() {
		return this.distributore;
	}

	public void setDistributore(Distributore distributore) {
		this.distributore = distributore;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}