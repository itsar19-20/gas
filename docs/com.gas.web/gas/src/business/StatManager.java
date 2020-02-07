package business;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import model.Prezzo;
import utils.JPAUtil;

public class StatManager {
	
	
	public static List<Prezzo> getStat(String scelta) {
		
		String q="Select c FROM Prezzo c WHERE descCarburante=";
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		
		List<Prezzo> lista = em.createQuery(q.concat(scelta), Prezzo.class).getResultList();
		Comparator<Prezzo> compareById = (Prezzo o1, Prezzo o2) -> o1.getDataComunicazione()
				.compareTo(o2.getDataComunicazione());
		lista.sort(compareById);
		int i = 0;
		int k = 0;
		int counter = 1;
		float dep = lista.get(i).getPrezzo();
		float a;
		for (i = 0; i < lista.size(); i++) {
			
			for (k = i + 1; k < lista.size(); k++) {
				a = (lista.get(i).getDataComunicazione().compareTo(lista.get(k).getDataComunicazione()));
				if (a == 0) {

					dep += lista.get(k).getPrezzo();
					counter++;
					lista.remove(k);
					lista.get(i).setPrezzo(dep / counter);
					counter = 1;
					dep = lista.get(i).getPrezzo();
				}
			}
		}
		em.close();
		return lista;
	}
}
