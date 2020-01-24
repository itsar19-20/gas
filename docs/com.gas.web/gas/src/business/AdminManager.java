package business;

import java.util.List;

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
		em.close();
	}

	public void addAdmin(String username) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User u = (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name")
				.setParameter("name", username).getSingleResult();
		//u.setIsAdmin((byte)1);
		em.getTransaction().begin();
		em.getTransaction().commit();
		em.close();
	}
	
	public void removeAdmin(String username) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User u = (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name")
				.setParameter("name", username).getSingleResult();
		//u.setIsAdmin((byte)0);
		em.getTransaction().begin();
		em.getTransaction().commit();
		em.close();
	}
	
	public static List<User> getUsers() {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		List<User> lista = em.createQuery("Select c FROM User c", User.class).getResultList();
		em.close();
		return lista;
	}
	
	public User searchUserByEmail (String email) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User u = (User) em.createQuery("Select c FROM User c WHERE c.email LIKE :mail")
				.setParameter("mail", email).getSingleResult();
		em.close();
		return u;
	}
	
	public void editUser(String nome, String cognome, String email, String username) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User u = (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name")
				.setParameter("name", username).getSingleResult();
		u.setNome(nome);
		u.setCognome(cognome);
		u.setEmail(email);
		em.getTransaction().begin();
		em.getTransaction().commit();
		em.close();
	}

}
