package business;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Distributore;
import model.Prezzo;
import utils.JPAUtil;

public class DatabaseManager {
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public Prezzo aggiornaPrezzi(String desc, String price, String selfS, String dtComu) {
		Double prezzo = Double.parseDouble(price);
		int isSelf = Integer.parseInt(selfS);
		Prezzo pr = new Prezzo();
		pr.setDescCarburante(desc);
		pr.setPrezzo(prezzo);
		try {
			pr.setDataComunicazione(formatter.parse(dtComu));
		} catch (ParseException e) {
			pr.setDataComunicazione(new Date());
		}
		pr.setIsSelf(isSelf);
		pr.setDtComu(dtComu);
		return pr;
	}

	public Distributore aggiornaDistributori(int id, String gestore, String bandiera, String tipoImpianto,
			String nomeImpianto, String indirizzo, String comune, String provincia, String lat, String lon) {
		Double latitudine = Double.parseDouble(lat);
		Double longitudine = Double.parseDouble(lon);
		Distributore d = new Distributore();
		d.setIdImpianto(id);
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
