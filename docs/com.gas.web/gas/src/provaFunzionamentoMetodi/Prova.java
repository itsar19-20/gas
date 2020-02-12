package provaFunzionamentoMetodi;

import java.util.List;

import javax.persistence.EntityManager;

import model.Distributore;
import utils.JPAUtil;

public class Prova {
	public static void main(String[] args) {

		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		List<Integer> listaID = (em.createQuery("Select c.idImpianto FROM Distributore c WHERE c.provincia LIKE 'MI'", Integer.class)
				.getResultList());
		System.out.println(listaID.toString());
		Distributore d = (Distributore)em.createQuery("Select c from Distributore c where c.idImpianto LIKE '5202'").getSingleResult();
		System.out.println(d);
	}
}
