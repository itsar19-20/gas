package business;

import java.util.List;

import javax.persistence.EntityManager;

import model.Prezzo;
import utils.JPAUtil;

public class StatManager {
	public static List<Prezzo> getStat() {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
//		List<Float> lista = em.createQuery("SELECT "
//				//+ "AVG("
//				+ "c.prezzo "
//				//+ ") " 
//				+ 
//				"FROM Prezzo c" 
//				//+ 
//				//"WHERE c.descCarburante='Benzina'" + 
//				//" GROUP BY c.dtComu"
//				, 
//				Float.class).getResultList();
		List<Prezzo> lista = em.createQuery("Select c FROM Prezzo c", Prezzo.class).getResultList();
		em.close();
		System.out.println(lista.toString());
		return lista;
	}
}
