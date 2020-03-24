package business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location;

import model.Distributore;
import model.Prezzo;
import model.User;
import utils.JPAUtil;

public class DistributoreManager implements Comparator<Prezzo> {

	@Override
	public int compare(Prezzo o1, Prezzo o2) {
		return 0;
	}

	Comparator ordinaDataDesc = Collections.reverseOrder(new Comparator<Prezzo>() {
		@Override
		public int compare(Prezzo a, Prezzo b) {
			return a.getDataComunicazione().compareTo(b.getDataComunicazione());
		}
	});
	Comparator ordinaConPrezzo = new Comparator<Prezzo>() {
		public int compare(Prezzo a, Prezzo b) {
			return a.getPrezzo().compareTo(b.getPrezzo());
		}
	};

	public List<Prezzo> cercaPiuEconomici(String tipoCarburante) {
		List<Prezzo> _return = new ArrayList<Prezzo>();
		switch (tipoCarburante) {
		case "Benzina":
			_return = cercaPiuEconomiciBenzina();
			break;
		case "Gasolio":
			_return = cercaPiuEconomiciGasolio();
			break;
		case "GPL":
			_return = cercaPiuEconomiciGpl();
			break;
		case "Metano":
			_return = cercaPiuEconomiciMetano();
			break;
		}
		return _return;
	}

	public List<Prezzo> cercaPiuEconomiciBenzina() {
		String benzina = "Benzina";
		String benzinaWR100 = "Benzina WR 100";
		String benzinaPlus98 = "Benzina Plus 98";
		String benzinaspeciale = "Benzina speciale";
		String benzinaShellVPower = "Benzina Shell V Power";
		String benzina100ottani = "Benzina 100 ottani";
		String blueSuper = "Blue Super";
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		TypedQuery<Distributore> query = em.createQuery(
				"SELECT DISTINCT d FROM Prezzo e JOIN e.distributore d WHERE d.provincia LIKE 'MI'",
				Distributore.class);
		List<Distributore> listaDistributore = query.getResultList();
		// Abbiamo la lista con tutti i Distributori di Milano
		List<Prezzo> prezziPiuRecenti = new ArrayList<Prezzo>();
		for (int i = 0; i < listaDistributore.size(); i++) {
			List<Prezzo> prezziOgniDistributore = listaDistributore.get(i).getPrezzos();
			// Abbiamo la lista con i tutti i prezzi disponibili per ciascun Distributore
			Collections.sort(prezziOgniDistributore, ordinaDataDesc);
			List<Prezzo> temporaria = new ArrayList<Prezzo>();
			for (int j = 0; j < prezziOgniDistributore.size(); j++) {
				if (prezziOgniDistributore.get(j).getDescCarburante().contentEquals(benzina)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(benzinaWR100)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(benzinaPlus98)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(benzinaspeciale)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(benzinaShellVPower)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(benzina100ottani)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(blueSuper)) {
					temporaria.add(prezziOgniDistributore.get(j));
				}
				if (temporaria.size() >= 2)
					break;
			}
			// Mettiamo in una lista temporaria i 2 prezzi piu recenti e, se sono uguali in
			// isSelf
			// aggiungiamo nella lista finale il piu recente, altrimenti il piu economico
			if (temporaria.size() >= 2) {
				if (temporaria.get(0).getIsSelf() == temporaria.get(1).getIsSelf()) {
					prezziPiuRecenti.add(temporaria.get(0));
				} else if (temporaria.get(0).getPrezzo() < temporaria.get(1).getPrezzo()) {
					prezziPiuRecenti.add(temporaria.get(0));
				} else {
					prezziPiuRecenti.add(temporaria.get(1));
				}
			}
			// in questo punto per ogni Distributore abbiamo il prezzo piu recente
		}
		return prezziPiuRecenti;
	}

	public List<Prezzo> cercaPiuEconomiciGasolio() {
		String gasolio = "Gasolio";
		String excelliumDiesel = "Excellium Diesel";
		String blueDiesel = "Blue Diesel";
		String gasolioAlpino = "Gasolio Alpino";
		String gasolioartico = "Gasolio artico";
		String gasolioPremium = "Gasolio Premium";
		String dieselHiQ = "Hi-Q Diesel";
		String gasolioOroDiesel = "Gasolio Oro Diesel";
		String gasolioSpeciale = "Gasolio Speciale";
		String gasolioEcoplus = "Gasolio Ecoplus";
		String dieselMax = "DieselMax";
		String bluDieselAlpino = "Blu Diesel Alpino";
		String dieselE10 = "Diesel e+10";
		String gasolioGelo = "Gasolio Gelo";
		String gpDIESEL = "GP DIESEL";
		String gasolioEnergyD = "Supreme Diesel";
		String eDIESEL = "E-DIESEL";
		String dieselShellVPower = "Diesel Shell V Power";
		String vPowerDiesel = "V-Power Diesel";
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		TypedQuery<Distributore> query = em.createQuery(
				"SELECT DISTINCT d FROM Prezzo e JOIN e.distributore d WHERE d.provincia LIKE 'MI'",
				Distributore.class);
		List<Distributore> listaDistributore = query.getResultList();
		// Abbiamo la lista con tutti i Distributori di Milano
		List<Prezzo> prezziPiuRecenti = new ArrayList<Prezzo>();
		for (int i = 0; i < listaDistributore.size(); i++) {
			List<Prezzo> prezziOgniDistributore = listaDistributore.get(i).getPrezzos();
			// Abbiamo la lista con i tutti i prezzi disponibili per ciascun Distributore
			Collections.sort(prezziOgniDistributore, ordinaDataDesc);
			List<Prezzo> temporaria = new ArrayList<Prezzo>();
			for (int j = 0; j < prezziOgniDistributore.size(); j++) {
				if (prezziOgniDistributore.get(j).getDescCarburante().contentEquals(gasolio)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(excelliumDiesel)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(blueDiesel)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(gasolioAlpino)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(gasolioartico)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(gasolioPremium)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(dieselHiQ)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(gasolioOroDiesel)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(gasolioSpeciale)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(gasolioEcoplus)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(dieselMax)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(bluDieselAlpino)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(dieselE10)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(gasolioGelo)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(gpDIESEL)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(gasolioEnergyD)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(eDIESEL)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(dieselShellVPower)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(vPowerDiesel)) {
					temporaria.add(prezziOgniDistributore.get(j));
				}
				if (temporaria.size() >= 2)
					break;
			}
			// Mettiamo in una lista temporaria i 2 prezzi piu recenti e, se sono uguali in
			// isSelf
			// aggiungiamo nella lista finale il piu recente, altrimenti il piu economico
			if (temporaria.size() >= 2) {
				if (temporaria.get(0).getIsSelf() == temporaria.get(1).getIsSelf()) {
					prezziPiuRecenti.add(temporaria.get(0));
				} else if (temporaria.get(0).getPrezzo() < temporaria.get(1).getPrezzo()) {
					prezziPiuRecenti.add(temporaria.get(0));
				} else {
					prezziPiuRecenti.add(temporaria.get(1));
				}
			}
			// in questo punto per ogni Distributore abbiamo il prezzo piu recente
		}
		return prezziPiuRecenti;
	}

	public List<Prezzo> cercaPiuEconomiciGpl() {
		String GPL = "GPL";
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		TypedQuery<Distributore> query = em.createQuery(
				"SELECT DISTINCT d FROM Prezzo e JOIN e.distributore d WHERE d.provincia LIKE 'MI'",
				Distributore.class);
		List<Distributore> listaDistributore = query.getResultList();
		// Abbiamo la lista con tutti i Distributori di Milano
		List<Prezzo> prezziPiuRecenti = new ArrayList<Prezzo>();
		for (int i = 0; i < listaDistributore.size(); i++) {
			List<Prezzo> prezziOgniDistributore = listaDistributore.get(i).getPrezzos();
			// Abbiamo la lista con i tutti i prezzi disponibili per ciascun Distributore
			Collections.sort(prezziOgniDistributore, ordinaDataDesc);
			List<Prezzo> temporaria = new ArrayList<Prezzo>();
			for (int j = 0; j < prezziOgniDistributore.size(); j++) {
				if (prezziOgniDistributore.get(j).getDescCarburante().contentEquals(GPL)) {
					temporaria.add(prezziOgniDistributore.get(j));
				}
				if (temporaria.size() >= 2)
					break;
			}
			// Mettiamo in una lista temporaria i 2 prezzi piu recenti e, se sono uguali in
			// isSelf
			// aggiungiamo nella lista finale il piu recente, altrimenti il piu economico
			if (temporaria.size() >= 2) {
				if (temporaria.get(0).getIsSelf() == temporaria.get(1).getIsSelf()) {
					prezziPiuRecenti.add(temporaria.get(0));
				} else if (temporaria.get(0).getPrezzo() < temporaria.get(1).getPrezzo()) {
					prezziPiuRecenti.add(temporaria.get(0));
				} else {
					prezziPiuRecenti.add(temporaria.get(1));
				}
			}
			// in questo punto per ogni Distributore abbiamo il prezzo piu recente
		}
		return prezziPiuRecenti;
	}

	public List<Prezzo> cercaPiuEconomiciMetano() {
		String metano = "Metano";
		String GNL = "GNL";
		String lGNC = "L-GNC";
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		TypedQuery<Distributore> query = em.createQuery(
				"SELECT DISTINCT d FROM Prezzo e JOIN e.distributore d WHERE d.provincia LIKE 'MI'",
				Distributore.class);
		List<Distributore> listaDistributore = query.getResultList();
		// Abbiamo la lista con tutti i Distributori di Milano
		List<Prezzo> prezziPiuRecenti = new ArrayList<Prezzo>();
		for (int i = 0; i < listaDistributore.size(); i++) {
			List<Prezzo> prezziOgniDistributore = listaDistributore.get(i).getPrezzos();
			// Abbiamo la lista con i tutti i prezzi disponibili per ciascun Distributore
			Collections.sort(prezziOgniDistributore, ordinaDataDesc);
			List<Prezzo> temporaria = new ArrayList<Prezzo>();
			for (int j = 0; j < prezziOgniDistributore.size(); j++) {
				if (prezziOgniDistributore.get(j).getDescCarburante().contentEquals(metano)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(GNL)
						|| prezziOgniDistributore.get(j).getDescCarburante().contentEquals(lGNC)) {
					temporaria.add(prezziOgniDistributore.get(j));
				}
				if (temporaria.size() >= 2)
					break;
			}
			// Mettiamo in una lista temporaria i 2 prezzi piu recenti e, se sono uguali in
			// isSelf
			// aggiungiamo nella lista finale il piu recente, altrimenti il piu economico
			if (temporaria.size() >= 2) {
				if (temporaria.get(0).getIsSelf() == temporaria.get(1).getIsSelf()) {
					prezziPiuRecenti.add(temporaria.get(0));
				} else if (temporaria.get(0).getPrezzo() < temporaria.get(1).getPrezzo()) {
					prezziPiuRecenti.add(temporaria.get(0));
				} else {
					prezziPiuRecenti.add(temporaria.get(1));
				}
			}
			// in questo punto per ogni Distributore abbiamo il prezzo piu recente
		}
		return prezziPiuRecenti;
	}

	public Distributore getDistributore(int idImpianto) {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		Distributore d = (Distributore) em.createQuery("Select c FROM Distributore c WHERE c.idImpianto LIKE :name")
				.setParameter("name", idImpianto).getSingleResult();
		em.close();
		return d;
	}

	public List<Prezzo> cercaPiuVicini(String carburante, int raggio, double latitudine, double longitudine) {
		List<Prezzo> _return = new ArrayList<Prezzo>();
		Map<Double, Prezzo> map = new TreeMap<>();
		List<Prezzo> temp = cercaPiuEconomici(carburante);
		int raggioMetri = raggio * 1000;
		for (int i = 0; i < temp.size(); i++) {
			double distanza = distFrom(latitudine, longitudine,
					temp.get(i).getDistributore().getLatitudine(), temp.get(i).getDistributore().getLongitudine());
			if (distanza < raggioMetri) {
				map.put(distanza, temp.get(i));
			}
		}
		for (double d:map.keySet()) {
			_return.add(map.get(d));
		}
		return _return;
	}

	// metodo che calcola distanza tra 2 punti
	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = (double) (earthRadius * c);

		return dist;
	}

}
