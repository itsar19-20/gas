package business;

import javax.persistence.EntityManager;

import model.User;
import utils.JPAUtil;

public class AuthenticationManager {

	EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();

	public User findWithName(String name) {
		return (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name").setParameter("name", name)
				.getSingleResult();
	}

	public User login(String username, String password) {
		User _return = null;
		// cerco l'utente nel DB

		_return = findWithName(username);
		if (_return != null) {
			// utente trovato; controllo la password
			if (!password.contentEquals(_return.getPassword())) {
				_return = null;
			}
		}
		em.close();
		return _return;
	}

}