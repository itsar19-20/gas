package business;

import java.util.List;

import javax.persistence.EntityManager;

import model.Distributore;
import utils.JPAUtil;

public class MapManager {
	
	public static List<Distributore> getDistributori() {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		List<Distributore> lista = em.createQuery("Select c FROM Distributore c", Distributore.class).getResultList();
		em.close();
		return lista;
	}
	
	
}
