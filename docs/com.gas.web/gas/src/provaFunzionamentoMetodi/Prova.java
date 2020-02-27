package provaFunzionamentoMetodi;

import java.util.List;

import business.DistributoreManager;
import model.Prezzo;

public class Prova {
	public static void main(String[] args) {
		
		DistributoreManager dm = new DistributoreManager();
		List<Prezzo> lista = dm.cercaPiuEconomici("Blue Diesel");
		for (int i = 0; i < lista.size(); i++) {
			System.out.println(lista.get(i));
		}
	}

}
