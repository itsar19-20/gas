package provaFunzionamentoMetodi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import business.DistributoreManager;
import model.Distributore;
import model.Prezzo;
import model.User;
import model.Valutazione;
import utils.JPAUtil;

public class Prova {
	public static void main(String[] args) throws ParseException {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		TypedQuery<Prezzo> query = em.createQuery(
				"SELECT DISTINCT d FROM Distributore e JOIN e.prezzos d WHERE e.provincia LIKE 'MI'",
				Prezzo.class);
		List<Prezzo> lista = query.getResultList();
		for (int i = 0; i < lista.size(); i++) {
			System.out.println(lista.get(i).getDistributore().getIdImpianto());
		}
	}

}
