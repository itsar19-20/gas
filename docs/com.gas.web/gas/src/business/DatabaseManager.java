package business;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import model.Distributore;
import model.Prezzo;
import utils.JPAUtil;

public class DatabaseManager {

	public void aggiornaPrezzi(String s, String desc, String p, String i, String dtComu) {
		int idImpianto = Integer.parseInt(s);
		Double prezzo = Double.parseDouble(p);
		int isSelf = Integer.parseInt(i);
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();

		Distributore d = (Distributore) em.createQuery("Select c FROM Distributore c WHERE c.idImpianto LIKE :name")
				.setParameter("name", idImpianto).getSingleResult();
		Prezzo pr = new Prezzo();
		em.getTransaction().begin();
		pr.setDistributore(d);
		pr.setDescCarburante(desc);
		pr.setPrezzo(prezzo);
		pr.setIsSelf(isSelf);
		pr.setDtComu(dtComu);
		em.persist(pr);
		em.getTransaction().commit();
	}

	public Distributore aggiornaDistributori(String id, String gestore, String bandiera, String tipoImpianto, String nomeImpianto,
			String indirizzo, String comune, String provincia, String lat, String lon) {
		int idImpianto = Integer.parseInt(id);
		Double latitudine = Double.parseDouble(lat);
		Double longitudine = Double.parseDouble(lon);
		Distributore d = new Distributore();
		d.setIdImpianto(idImpianto);
		d.setGestore(gestore);
		d.setBandiera(bandiera);
		d.setTipoImpianto(tipoImpianto);
		d.setNomeImpianto(nomeImpianto);
		d.setIndirizzo(indirizzo);
		d.setComune(comune);
		d.setProvincia(provincia);
		d.setLatitudine(latitudine);
		d.setLongitudine(longitudine);
		return d;
	}
	
	
	
	
	
	
}
