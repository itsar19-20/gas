package business;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	public static List<Valutazione> getValutazioni() {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		Query q = em.createQuery("SELECT e FROM Valutazione e", Valutazione.class);
		List<Valutazione> lista = q.getResultList();
		em.close();
		return lista;
	}

	public Valutazione addValutazione(User user, Distributore distributore, int giudizio, String descrizione) {
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

	public Map<Integer, Float> getMediaValutazioni() {
		// metodo risponde con idImpianto e la media
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		String query = "SELECT d.idImpianto, AVG(c.giudizio) FROM Distributore d JOIN d.valutaziones c GROUP BY c.distributore";
		Map<Integer, Float> _return = new HashMap<>();
		List<Object[]> rispostaServer = em.createQuery(query).getResultList();
		Iterator iterator = rispostaServer.iterator();
		while (iterator.hasNext()) {
			Object[] object = (Object[]) iterator.next();
			int idImpianto = (int) object[0];
			double d = (double) object[1];
			float mediaValutazioni = (float) d;
			_return.put(idImpianto, mediaValutazioni);
		}
		em.close();
		return _return;
	}

}
