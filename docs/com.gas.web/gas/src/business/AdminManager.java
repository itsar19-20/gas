package business;

import java.util.List;

import javax.persistence.EntityManager;

import model.User;
import utils.JPAUtil;
import utils.MailUtils;

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

	public static List<User> getUsers() {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		List<User> lista = em.createQuery("Select c FROM User c", User.class).getResultList();
		em.close();
		return lista;
	}

	public User editUser(String nome, String cognome, String email, Boolean isAdmin, String username) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User u = (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name")
				.setParameter("name", username).getSingleResult();
		u.setNome(nome);
		u.setCognome(cognome);
		u.setEmail(email);
		u.setIsAdmin(isAdmin);
		em.getTransaction().begin();
		em.getTransaction().commit();
		em.close();
		return u;
	}

	public User forgotPassword(String email) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		MailUtils mu = new MailUtils();
		User u = new User();
		try {
			u = (User) em.createQuery("Select c FROM User c WHERE c.email LIKE :name").setParameter("name", email)
					.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		String newPass = mu.sendNewPass(email);
		if (newPass == null) {
			return null;
		} else {
			u.setPassword(newPass);
			em.getTransaction().begin();
			em.getTransaction().commit();
			em.close();
			return u;
		}
	}
	
	

}
