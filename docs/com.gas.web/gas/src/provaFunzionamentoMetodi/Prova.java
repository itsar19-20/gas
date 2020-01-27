package provaFunzionamentoMetodi;

import javax.persistence.EntityManager;

import business.ValutazioneManager;
import model.Distributore;
import model.User;
import utils.JPAUtil;

public class Prova {

	public static void main(String[] args) {
		
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User u = (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name")
				.setParameter("name", "admin").getSingleResult();
		/*Distributore d = (Distributore) em.createQuery("Select c FROM Distributore c WHERE c.id LIKE :name").setParameter("name", 3464).getSingleResult();
		System.out.println("arrivato");
		ValutazioneManager vm = new ValutazioneManager();
		vm.addValutazione(u, d, 4, "tutto ok");*/
		System.out.println(u.getValutaziones().toString());
		
	}

}
