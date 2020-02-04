package business;

import javax.persistence.EntityManager;

import model.Distributore;
import model.Prezzo;
import utils.JPAUtil;

public class DatabaseManager {

	public void aggiornaPrezzi(String s, String desc, String p, String i, String dtComu) {
		int idImpianto = Integer.parseInt(s);
		float prezzo = Float.parseFloat(p);
		int isSelf = Integer.parseInt(i);
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		Distributore d = (Distributore) em.createQuery("Select c FROM Distributore c WHERE c.idImpianto LIKE :name")
				.setParameter("name", idImpianto).getSingleResult();
		Prezzo pr = new Prezzo();
		em.getTransaction().begin();
		pr.setDistributore(d);
		pr.setDescCarburante(desc);
		pr.setPrezzo(prezzo);
		pr.setIsSelf(isSelf);
		pr.setDtComu(dtComu);
		em.persist(pr);
		em.getTransaction().commit();
	}
	
	public void aggiornaDistributori() {
		
	}
	
	
	
	
	
	
}
