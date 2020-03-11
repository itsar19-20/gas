package business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.Distributore;
import model.Prezzo;
import model.User;
import utils.JPAUtil;

public class DistributoreManager implements Comparator<Prezzo> {
	@Override
	public int compare(Prezzo o1, Prezzo o2) {
		return 0;
	}

	Comparator ordinaDataDesc = Collections.reverseOrder(new Comparator<Prezzo>() {
		@Override
		public int compare(Prezzo a, Prezzo b) {
			return a.getDataComunicazione().compareTo(b.getDataComunicazione());
		}
	});
	Comparator ordinaConPrezzo = new Comparator<Prezzo>() {
		public int compare(Prezzo a, Prezzo b) {
			return a.getPrezzo().compareTo(b.getPrezzo());
		}
	};

	public List<Prezzo> cercaPiuEconomici(String tipoCarburante) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		TypedQuery<Distributore> query = em.createQuery(
				"SELECT DISTINCT d FROM Prezzo e JOIN e.distributore d WHERE d.provincia LIKE 'MI'",
				Distributore.class);
		List<Distributore> listaDistributore = query.getResultList();
		// Abbiamo la lista con tutti i Distributori di Milano
		List<Prezzo> prezziPiuRecenti = new ArrayList<Prezzo>();
		for (int i = 0; i < listaDistributore.size(); i++) {
			List<Prezzo> prezziOgniDistributore = listaDistributore.get(i).getPrezzos();
			//Abbiamo la lista con i tutti i prezzi disponibili per ciascun Distributore
			Collections.sort(prezziOgniDistributore, ordinaDataDesc);
			List<Prezzo> temporaria = new ArrayList<Prezzo>();
			for (int j = 0; j < prezziOgniDistributore.size(); j++) {
				if (prezziOgniDistributore.get(j).getDescCarburante().contentEquals(tipoCarburante)) {
					temporaria.add(prezziOgniDistributore.get(j));
				}
				if (temporaria.size() >= 2)
					break;
			}
			//Mettiamo in una lista temporaria i 2 prezzi piu recenti e, se sono uguali in isSelf
			//aggiungiamo nella lista finale il piu recente, altrimenti il piu economico
			if (temporaria.size() >= 2) {
				if (temporaria.get(0).getIsSelf() == temporaria.get(1).getIsSelf()) {
					prezziPiuRecenti.add(temporaria.get(0));
				} else if (temporaria.get(0).getPrezzo() < temporaria.get(1).getPrezzo()) {
					prezziPiuRecenti.add(temporaria.get(0));
				} else {
					prezziPiuRecenti.add(temporaria.get(1));
				}
			}
			//in questo punto per ogni Distributore abbiamo il prezzo piu recente
		}
		return prezziPiuRecenti;
	}

	public Distributore getDistributore(int idImpianto) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		Distributore d =(Distributore) em.createQuery("Select c FROM Distributore c WHERE c.idImpianto LIKE :name")
				.setParameter("name", idImpianto).getSingleResult();
		em.close();
		return d;
	}
	
	
	
	
	
	
}
