package provaFunzionamentoMetodi;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.persistence.EntityManager;

import model.Prezzo;
import utils.JPAUtil;

public class Prova {
	// static final Logger LOG = LoggerFactory.getLogger(Prova.class);
	public static void main(String[] args) {
		Logger logger = Logger.getLogger("MyLog");
		FileHandler fh;
		try {
			fh = new FileHandler("./logs/aggiornaDistributori.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		logger.setUseParentHandlers(false);

		
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
//		 int a = 446145;
//		 System.out.println(l.contains(a));
//		 LOG.trace("Hello World!");
//		 LOG.debug("How are you today?");
//		 LOG.info("I am fine.");
//		 LOG.warn("I love programming.");
//		 LOG.error("I am programming.");
		// System.out.println(u.getValutaziones().toString());

	}
}
