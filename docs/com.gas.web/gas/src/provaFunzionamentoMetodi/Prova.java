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
import model.Valutazione;
import utils.JPAUtil;

public class Prova {
	public static void main(String[] args) throws ParseException {
		EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
		Date date = new Date();
		String data = date.getDate()+"/"+date.getMonth()+"/"+date.getYear();
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
		Date vecchio = simpleDateFormat.parse(data);
		Date nuovo = new Date();
		System.out.println(vecchio.compareTo(nuovo) +" "+ vecchio);
		
	
	}

}
