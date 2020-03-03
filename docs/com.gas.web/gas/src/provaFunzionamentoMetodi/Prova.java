package provaFunzionamentoMetodi;

import java.util.List;

import business.DistributoreManager;
import model.Prezzo;

public class Prova {
	public static void main(String[] args) {
		
		DistributoreManager dm = new DistributoreManager();
		List<Prezzo> lista = dm.cercaPiuEconomici("Benzina");
		System.out.println(lista.size());
	}

}
