package business;

import java.util.Comparator;
import java.util.Iterator;
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
		

		Comparator<Prezzo> compareById = (Prezzo o1, Prezzo o2) -> o1.getDataComunicazione()
				.compareTo(o2.getDataComunicazione());

		lista.sort(compareById);
		for (Prezzo element : lista) {
			System.out.println(element.getId()+"  "+element.getDataComunicazione()+" "+element.getPrezzo());
		}
		int i = 0;
		int k = 0;
		int counter = 1;
		float dep = lista.get(i).getPrezzo();
		for (i = 0; i < lista.size(); i++) {

			
			System.out.println("Elemento "+i+" "+lista.get(i).getPrezzo());
			for (k = i; k < lista.size() ; k++) {
				System.out.println("k vale "+k);
				//if (i >=lista.size()) break;
				System.out.println("Elemento "+i+" "+lista.get(i).getPrezzo()+ " "+lista.get(i).getDataComunicazione()+
						" confrontato con elemento "+
						k+" "+lista.get(k).getPrezzo()+ " "+lista.get(k).getDataComunicazione()
						);
				if ((lista.get(i).getDataComunicazione().compareTo(lista.get(k).getDataComunicazione()))==0) {
					dep += lista.get(k).getPrezzo();
					counter++;
					System.out.println("Ora dep vale "+ dep+" e counter vale "+counter);
					System.out.println("è stato rimosso l'elemento alla posizione "+k);
					lista.remove(k);
					lista.get(i).setPrezzo(dep / counter);
					System.out.println("Prezzo medio di "+i+": "+(lista.get(i).getPrezzo()));
					System.out.println("La lista ora è lunga "+ lista.size());
					for (Prezzo element : lista) {
						System.out.println(element.getId()+"  "+element.getDataComunicazione()+" "+element.getPrezzo());
						}
					counter=1;
					dep = lista.get(i).getPrezzo();
					//break;
				} 
				
			}
			
			
			
		}
		em.close();
		for (i=0;i<lista.size();i++) {
			System.out.println(lista.get(i).getPrezzo()+"        "+lista.get(i).getDataComunicazione());
		}
		System.out.println(lista.toString());
		return lista;
	}
}
