package business;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

import model.Prezzo;
import utils.JPAUtil;

public class StatManager {

	public static List<Prezzo> getStat(String scelta) {	
		
		try {
			EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
			LocalDate localDate= java.time.LocalDate.now();
			Date value = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			
			List<Prezzo> lista = em.createQuery("Select c FROM Prezzo c WHERE descCarburante LIKE :scelta AND YEAR(DataComunicazione) = YEAR(:today) AND MONTH(DataComunicazione)= MONTH(:today)",
					 Prezzo.class)
					.setParameter("scelta", "%" + scelta + "%")
					.setParameter("today", value, TemporalType.TIMESTAMP)
					.getResultList();			
			Comparator<Prezzo> compareById = (Prezzo o1, Prezzo o2) -> o1.getDataComunicazione()
					.compareTo(o2.getDataComunicazione());
			lista.sort(compareById);
			int i = 0;
			int k = 0;
			int counter = 1;
			System.out.println(lista);
			Double dep = lista.get(i).getPrezzo();
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
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
