package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import business.DatabaseManager;
import model.Distributore;
import utils.JPAUtil;

/**
 * Servlet implementation class AggiornaDistributori
 */
@WebServlet("/aggiornaDistributori")
@MultipartConfig(location = "/tmp", fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024
		* 7, maxRequestSize = 1024 * 1024 * 7 * 2)
public class AggiornaDistributori extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AggiornaDistributori() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DatabaseManager dm = new DatabaseManager();
		try {
			Part part = request.getPart("fileA"); // Per ricevere <input type="file" name="file">
			String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString(); // nome del file caricato
			InputStream fileContent = part.getInputStream();// fileContent è il file caricato
			Scanner s = new Scanner(fileContent).useDelimiter("\\n");
			s.next();
			s.next(); // skip prime 2 righe
			EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
			List<Integer> listaID = (em.createQuery("Select c.idImpianto FROM Distributore c", Integer.class)
					.getResultList());
			em.setFlushMode(FlushModeType.COMMIT);
			int batchsize = 500;
			int i = 0;
			em.getTransaction().begin();
			long startTime = System.currentTimeMillis();

			while (s.hasNext()) {

				try {
					String row = s.next();
					String[] column = row.split(";");
					int idImpianto = Integer.parseInt(column[0]);
					if (listaID.contains(idImpianto)) {
						continue;
					} else {
//
//						Double latitudine = Double.parseDouble(column[8]);
//						Double longitudine = Double.parseDouble(column[9]);
//						em.createNativeQuery(
//								"INSERT INTO distributore (idImpianto, gestore, bandiera, tipoImpianto, nomeImpianto, indirizzo, comune, provincia, latitudine, longitudine) "
//										+ "VALUES (?,?,?,?,?,?,?,?,?,?)")
//								.setParameter(1, column[0]).setParameter(2, column[1]).setParameter(3, column[2])
//								.setParameter(4, column[3]).setParameter(5, column[4]).setParameter(6, column[5])
//								.setParameter(7, column[6]).setParameter(8, column[7]).setParameter(9, latitudine)
//								.setParameter(10, longitudine).executeUpdate();
//				
					Distributore d = dm.aggiornaDistributori(column[0], column[1], column[2], column[3], column[4], column[5], column[6],
							column[7], column[8], column[9]);
					em.persist(d);
					if (++i % batchsize == 0) {
						em.flush();
						em.clear();
						System.out.println("entitymanager Flushed");
					}
					}
				} catch (Exception e) {
					System.out.println("eccezione  " + e.toString());
				}

			}
			request.setAttribute("messageSuccesfulStation", "File: " + fileName + ", dati inseriti correttamente.");
			System.out.println("commited");
//			em.getTransaction().commit();
//			em.close();
			s.close();
			long endTime = System.currentTimeMillis();
			System.out.println("Tempo di esecuzione: " + (endTime - startTime));
		} catch (Exception e) {
			request.setAttribute("messageErrorStation", "Errore, caricamento non riuscito.");
		}

		RequestDispatcher rd = request.getRequestDispatcher("flusso.jsp");
		rd.include(request, response);
	}
}
