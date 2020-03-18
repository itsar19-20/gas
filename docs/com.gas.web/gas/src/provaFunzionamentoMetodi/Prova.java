package provaFunzionamentoMetodi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.persistence.EntityManager;

import business.DistributoreManager;
import model.Prezzo;
import model.User;
import model.Valutazione;
import utils.JPAUtil;

public class Prova {
	public static void main(String[] args) throws ParseException {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		User u = (User) em.createQuery("Select c FROM User c WHERE c.username LIKE :name")
				.setParameter("name", "samu").getSingleResult();
		List<Valutazione> lista = u.getValutaziones();
		em.getTransaction().begin();
		for (int i = 0; i < lista.size(); i++) {
			Valutazione v = lista.get(i);
			em.remove(v);
		}
		em.remove(u);
		em.getTransaction().commit();
	
	}

}
