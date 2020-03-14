package model;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String cognome;

	@Temporal(TemporalType.DATE)
	private Date dataRegistrazione;

	private Date dataUltimaLogin; 

	private String email;

	private Boolean isAdmin;

	private String nome;
	
	@JsonIgnore
	private String password;
	
	@Column(unique=true)
	private String username;

	//bi-directional many-to-one association to Valutazione
	@OneToMany(mappedBy="user")
	@JsonIgnore
	private List<Valutazione> valutaziones;

	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Date getDataRegistrazione() {
		return this.dataRegistrazione;
	}

	public void setDataRegistrazione(Date dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}


	public Date getDataUltimaLogin() {
		return dataUltimaLogin;
	}

	public void setDataUltimaLogin(Date date) {
		this.dataUltimaLogin = date;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Valutazione> getValutaziones() {
		return this.valutaziones;
	}

	public void setValutaziones(List<Valutazione> valutaziones) {
		this.valutaziones = valutaziones;
	}

	public Valutazione addValutazione(Valutazione valutazione) {
		getValutaziones().add(valutazione);
		valutazione.setUser(this);

		return valutazione;
	}

	public Valutazione removeValutazione(Valutazione valutazione) {
		getValutaziones().remove(valutazione);
		valutazione.setUser(null);

		return valutazione;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return username;
	}

}