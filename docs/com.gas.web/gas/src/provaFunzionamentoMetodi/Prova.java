package provaFunzionamentoMetodi;

import java.util.List;

import javax.persistence.EntityManager;

import business.DistributoreManager;
import model.Prezzo;
import model.Valutazione;
import utils.JPAUtil;

public class Prova {
	public static void main(String[] args) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		List<Valutazione> list = em.createQuery("select d from Valutazione d where d.user.nome like:name and d.distributore.idImpianto like:idImpianto", Valutazione.class)
				.setParameter("name", "simone").setParameter("idImpianto", 12629).getResultList();
		
//		System.out.println(list);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getDescrizione());
		}
	}

}
