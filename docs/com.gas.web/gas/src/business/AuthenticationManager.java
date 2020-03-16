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
		try {
			_return = (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name")
					.setParameter("name", username).getSingleResult();
		} catch (Exception e) {
			return null;
		}
		em.getTransaction().begin();
		_return.setDataUltimaLogin(new Date());
		em.getTransaction().commit();
		if (password.contentEquals(_return.getPassword()) && _return.getIsAdmin()) {
			return _return;
		} else {
			return null;
		}
	}
	
	public User loginUsers(String username, String password) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User _return = null;
		// cerco l'utente nel DB
		try {
			_return = (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name")
					.setParameter("name", username).getSingleResult();
		} catch (Exception e) {
			em.close();
			return null;
		}
		em.getTransaction().begin();
		_return.setDataUltimaLogin(new Date());
		em.getTransaction().commit();
		if (password.contentEquals(_return.getPassword())) {
			em.close();
			return _return;
		} else {
			em.close();
			return null;
		}
	}
	

	public User verifyUser(String username) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User _return = null;
		// cerco l'utente nel DB
		try {
			_return = (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name")
					.setParameter("name", username).getSingleResult();
		} catch (Exception e) {
			System.out.println(e.getCause());
		}
		if (_return == null) {
			em.close();
			return null;
		} else {
			em.close();
			return _return;
		}
	}

	public User signup(String email, String nome, String cognome, String username, String password) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User _return = new User();
		_return.setEmail(email);
		_return.setNome(nome);
		_return.setCognome(cognome);
		_return.setUsername(username);
		_return.setPassword(password);
		_return.setIsAdmin(false);
		_return.setDataRegistrazione(new Date());
		_return.setDataUltimaLogin(new Date());
		em.getTransaction().begin();
		try {
			em.persist(_return);
		} catch (Exception e) {
			em.close();
			return null;
		}
		em.getTransaction().commit();
		em.close();
		return _return;
	}

}