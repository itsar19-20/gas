package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.List;

import javax.persistence.EntityManager;
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
 * Servlet implementation class AggiornaPrezzi
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
		// inizializzo i variabili per usarli dentro il try/catch e anche fuori
		Part part;
		String fileName = null;
		InputStream fileContent;
		BufferedReader br = null;
		List<Integer> listaID = null;
		EntityManager em = null;
		String row;
		try {
			// Per ricevere <input type="file" name="file">
			part = request.getPart("fileA");
			// nome del file caricato
			fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
			// fileContent è il file caricato
			fileContent = part.getInputStream();
			br = new BufferedReader(new InputStreamReader(fileContent));
			br.readLine(); // prime 2 righe sono senza dati
		} catch (Exception e) {
			request.setAttribute("messageErrorStation", "Errore, caricamento non riuscito.");
			RequestDispatcher rd = request.getRequestDispatcher("flusso.jsp");
			rd.include(request, response);
			System.out.println("errore: " + e.toString());
		}
		try {
			em = JPAUtil.getInstance().getEmf().createEntityManager();
			listaID = em
					.createQuery("Select c.idImpianto FROM Distributore c WHERE c.provincia LIKE 'MI'", Integer.class)
					.getResultList();
			row = br.readLine();
		} catch (Exception e) {
			request.setAttribute("messageErrorStation", "Errore Generico");
			RequestDispatcher rd = request.getRequestDispatcher("flusso.jsp");
			rd.include(request, response);
			System.out.println("EntityManager non creato, errore: " + e.toString());
		}
		long startTime = System.currentTimeMillis();
		
		while ((row = br.readLine())!= null) {
			try {
				String[] column = row.split(";");
				int idImpianto = Integer.parseInt(column[0]);
				if (listaID.contains(idImpianto)) {
					System.out.println("Duplicato");
					continue; // impianto duplicato
				} else if (column[7].equals("MI")) { // il caso che ci interessa
					DatabaseManager dm = new DatabaseManager();
					Distributore nuovo = dm.aggiornaDistributori(idImpianto, column[1], column[2], column[3], column[4],
							column[5], column[6], column[7], column[8], column[9]);
					System.out.println("nuovo");
					em.getTransaction().begin();
					System.out.println("passed3");
					em.persist(nuovo);
					em.getTransaction().commit();
				} else {
					continue;
				}
			} catch (Exception e) {
				System.out.println(e.getStackTrace());
				System.out.println("exception" + e.toString());
			}
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("Tempo di esecuzione: " + ((endTime - startTime) / 1000) + " secondi.");
		request.setAttribute("messageSuccesfulStation", "File: " + fileName + ", dati inseriti correttamente.");
		RequestDispatcher rd = request.getRequestDispatcher("flusso.jsp");
		rd.include(request, response);
	}
}
