package provaFunzionamentoMetodi;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.persistence.EntityManager;

import model.Prezzo;
import utils.JPAUtil;

public class Prova {
	// static final Logger LOG = LoggerFactory.getLogger(Prova.class);
	public static void main(String[] args) {

		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		String scelta="Benzina";
		List<Prezzo> lista = em.createQuery("Select c FROM Prezzo c WHERE descCarburante= :scelta", Prezzo.class)
				.setParameter("scelta", scelta)
				.getResultList();

		
		System.out.println(lista);
//		  List<Integer> l=(em.createQuery("Select c.idImpianto FROM Distributore c", Integer.class).getResultList());
//		  int i=0;
//		 for (i=0;i<l.size();i++) {
//			 System.out.println(l.get(i).toString());
//		 }
		int a = 446145;

		// System.out.println(u.getValutaziones().toString());

	}
}
