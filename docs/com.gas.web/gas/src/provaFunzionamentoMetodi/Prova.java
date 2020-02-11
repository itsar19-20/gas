package provaFunzionamentoMetodi;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import model.Prezzo;
import utils.JPAUtil;

public class Prova {
	public static void main(String[] args) {

		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		Prezzo u = null;
		u = (Prezzo) em.createQuery(
				"Select c FROM Prezzo c WHERE c.descCarburante LIKE :descCarburante AND c.isSelf LIKE : isSelf AND c.dtComu LIKE : dtComu")
				.setParameter("descCarburante", "Benzina").setParameter("isSelf", 1)
				.setParameter("dtComu", "01/01/2018 10:20:52").getSingleResult();
		System.out.println(u);
//		  List<Integer> l=(em.createQuery("Select c.idImpianto FROM Distributore c", Integer.class).getResultList());
//		  int i=0;
//		 for (i=0;i<l.size();i++) {
//			 System.out.println(l.get(i).toString());
//		 }
		int a = 446145;

		// System.out.println(u.getValutaziones().toString());

	}
}
