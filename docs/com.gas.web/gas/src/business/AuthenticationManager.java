package business;

import java.util.Date;

import javax.persistence.EntityManager;

import model.User;
import utils.JPAUtil;

public class AuthenticationManager {

	public User login(String username, String password) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User _return = null;
		// cerco l'utente nel DB
		_return = (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name").setParameter("name", username).getSingleResult();
		if (_return == null)
			return null;
		em.getTransaction().begin();
		_return.setDataUltimaLogin(new Date());
		em.getTransaction().commit();
		if (password.contentEquals(_return.getPassword())) {
			return _return;
		}
		return null;
	}

	public User signup(String email, String nome, String cognome, String username, String password) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User _return = new User();
		_return.setEmail(email);
		_return.setNome(nome);
		_return.setCognome(cognome);
		_return.setUsername(username);
		_return.setPassword(password);
		em.getTransaction().begin();
		em.persist(_return);
		em.getTransaction().commit();
		em.close();
		return _return;
	}

}