package utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
	private EntityManagerFactory emf;

	private static JPAUtil instance;
	private JPAUtil() {
		this.emf = Persistence.createEntityManagerFactory("gas");
	}

	public static JPAUtil getInstance() {
		if (instance == null)
			instance = new JPAUtil();
		return instance;
	}

	public EntityManagerFactory getEmf() {
		return emf;
	}

}


