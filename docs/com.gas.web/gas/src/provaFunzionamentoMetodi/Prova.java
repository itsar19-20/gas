package provaFunzionamentoMetodi;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Prezzo;
import utils.JPAUtil;

public class Prova {
	// static final Logger LOG = LoggerFactory.getLogger(Prova.class);
	private static final Logger logger = LoggerFactory.getLogger(Prova.class);

	public static void main(String[] args) {

		
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		Prezzo u = null;
		u = (Prezzo) em.createQuery(
				"Select c FROM Prezzo c WHERE c.descCarburante LIKE :descCarburante AND c.isSelf LIKE : isSelf AND c.dtComu LIKE : dtComu")
				.setParameter("descCarburante", "Benzina").setParameter("isSelf", 1)
				.setParameter("dtComu", "01/01/2018 10:20:52").getSingleResult();
		logger.info("logger logger arrivato");
		System.out.println(u);
		logger.info("logger logger prova");
//		  List<Integer> l=(em.createQuery("Select c.idImpianto FROM Distributore c", Integer.class).getResultList());
//		  int i=0;
//		 for (i=0;i<l.size();i++) {
//			 System.out.println(l.get(i).toString());
//		 }
		 int a = 446145;
		
		 
		// System.out.println(u.getValutaziones().toString());

	}
}
