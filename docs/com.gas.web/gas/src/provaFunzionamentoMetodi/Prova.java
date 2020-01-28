package provaFunzionamentoMetodi;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.fasterxml.jackson.databind.ObjectMapper;

import business.AdminManager;
import model.User;
import model.Valutazione;
import utils.JPAUtil;

public class Prova {

	public static void main(String[] args) {

		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User u = (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name").setParameter("name", "admin")
				.getSingleResult();
		/*
		 * Distributore d = (Distributore)
		 * em.createQuery("Select c FROM Distributore c WHERE c.id LIKE :name").
		 * setParameter("name", 3464).getSingleResult(); System.out.println("arrivato");
		 * ValutazioneManager vm = new ValutazioneManager(); vm.addValutazione(u, d, 4,
		 * "tutto ok");
		 */
		// System.out.println(u.getValutaziones().toString());

		Query q = em.createQuery("SELECT e FROM Valutazione e", Valutazione.class);
		List<Valutazione> lista1 = q.getResultList();
		System.out.println(lista1);
		
		List<User> lista = AdminManager.getUsers();
		ObjectMapper om = new ObjectMapper();
		System.out.println(lista);
		
	}

}
