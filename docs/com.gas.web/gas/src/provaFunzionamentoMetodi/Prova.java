package provaFunzionamentoMetodi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;

import business.DistributoreManager;
import model.Prezzo;
import model.Valutazione;
import utils.JPAUtil;

public class Prova {
	public static void main(String[] args) {

		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		String query = "SELECT d.idImpianto, AVG(c.giudizio) FROM Distributore d JOIN d.valutaziones c GROUP BY c.distributore";
		Map<Integer,Float> risposta = new HashMap<>();
		List<Object[]> v = em.createQuery(query).getResultList();
		Iterator it = v.iterator();
		while(it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			int id = (int) obj[0];
			double media = (double) obj[1];
			float c = (float)media;
			risposta.put(id, c);
		}
		for(Entry<Integer, Float> entry: risposta.entrySet()){
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
		
	}

}
