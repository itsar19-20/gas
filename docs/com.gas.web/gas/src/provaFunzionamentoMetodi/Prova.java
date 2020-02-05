package provaFunzionamentoMetodi;

import java.util.List;

import javax.persistence.EntityManager;

import model.Distributore;
import utils.JPAUtil;

public class Prova {

	public static void main(String[] args) {

		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		
		
		 
		  List<Integer> l=(em.createQuery("Select c.idImpianto FROM Distributore c", Integer.class).getResultList());
		  int i=0;
		 for (i=0;i<l.size();i++) {
			 System.out.println(l.get(i).toString());
			 
		 }
		// System.out.println(u.getValutaziones().toString());

		
	}

}
