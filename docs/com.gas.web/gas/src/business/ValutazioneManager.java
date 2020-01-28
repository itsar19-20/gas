package business;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Distributore;
import model.User;
import model.Valutazione;
import utils.JPAUtil;

public class ValutazioneManager {
	
	public void deleteValutazione(String idS) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		int id = Integer.parseInt(idS);
		Valutazione v = (Valutazione) em.createQuery("Select c FROM Valutazione c WHERE c.id LIKE :id")
				.setParameter("id", id).getSingleResult();
		em.getTransaction().begin();
		em.remove(v);
		em.getTransaction().commit();
		em.close();
	}
	
	public static List <Valutazione> getValutazioni() {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		Query q = em.createQuery("SELECT e FROM Valutazione e", Valutazione.class);
		List<Valutazione> lista = q.getResultList();
		em.close();
		return lista;
	}
	
	public Valutazione addValutazione(User user, Distributore distributore, 
			int giudizio, String descrizione) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		Valutazione _return = new Valutazione();
		_return.setData(new Date());
		_return.setUser(user);
		_return.setGiudizio(giudizio);
		_return.setDistributore(distributore);
		_return.setDescrizione(descrizione);
		em.getTransaction().begin();
		em.persist(_return);
		em.getTransaction().commit();
		em.close();		
		return _return;
	}
	
	
	
	
	
}
