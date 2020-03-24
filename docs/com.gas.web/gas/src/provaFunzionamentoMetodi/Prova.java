package provaFunzionamentoMetodi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import business.DistributoreManager;
import controllers.CercaPiuEconomici;
import model.Distributore;
import model.Prezzo;
import model.User;
import model.Valutazione;
import utils.JPAUtil;

public class Prova {
	public static void main(String[] args) throws ParseException {

		Map<Double, Prezzo> map = cercaPiuVicini("Gasolio", 40, 45.451167, 9.229806);
		for (double d:map.keySet()) {
			Prezzo p = map.get(d);
			System.out.println(p);
		}

	}

	public static Map<Double, Prezzo> cercaPiuVicini(String carburante, int raggio, double latitudine,
			double longitudine) {
		DistributoreManager distributoreManager = new DistributoreManager();
		List<Prezzo> _return = new ArrayList<Prezzo>();
		Map<Double, Prezzo> map = new TreeMap<>();
		List<Prezzo> temp = distributoreManager.cercaPiuEconomici(carburante);
		int raggioMetri = raggio * 1000;
		for (int i = 0; i < temp.size(); i++) {
			double distanza = distFrom(latitudine, longitudine, temp.get(i).getDistributore().getLatitudine(),
					temp.get(i).getDistributore().getLongitudine());
			if (distanza < raggioMetri) {
				map.put(distanza, temp.get(i));
			}
		}

		return map;
	}

	public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);

		return dist;
	}

}
