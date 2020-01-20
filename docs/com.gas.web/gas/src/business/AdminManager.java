package business;

import javax.persistence.EntityManager;

import model.User;
import utils.JPAUtil;

public class AdminManager {

	public void deleteUser(String username) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User u = (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name")
				.setParameter("name", username).getSingleResult();
		em.getTransaction().begin();
		em.remove(u);
		em.getTransaction().commit();
	}

	public void addAdmin(String username) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User u = (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name")
				.setParameter("name", username).getSingleResult();
		u.setIsAdmin((byte)1);
		em.getTransaction().begin();
		em.getTransaction().commit();
	}
	
	public User searchUser(String username) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User u = (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name")
				.setParameter("name", username).getSingleResult();
		return u;
	}

}
