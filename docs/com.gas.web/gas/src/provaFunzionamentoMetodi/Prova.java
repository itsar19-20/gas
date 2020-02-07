package provaFunzionamentoMetodi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import javax.persistence.EntityManager;

import model.Distributore;
import utils.JPAUtil;

public class Prova {
	static final Logger LOG = LoggerFactory.getLogger(Prova.class);
	public static void main(String[] args) {

		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		
		
		 
		  List<Integer> l=(em.createQuery("Select c.idImpianto FROM Distributore c", Integer.class).getResultList());
		  int i=0;
//		 for (i=0;i<l.size();i++) {
//			 System.out.println(l.get(i).toString());
//		 }
		 int a = 446145;
		 System.out.println(l.contains(a));
		 LOG.trace("Hello World!");
		 LOG.debug("How are you today?");
		 LOG.info("I am fine.");
		 LOG.warn("I love programming.");
		 LOG.error("I am programming.");
		// System.out.println(u.getValutaziones().toString());

		
	
	}
}
