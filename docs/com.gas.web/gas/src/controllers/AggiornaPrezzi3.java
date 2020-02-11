package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.Distributore;
import model.Prezzo;
import utils.JPAUtil;

/**
 * Servlet implementation class AggiornaPrezzi
 */
@WebServlet("/aggiornaPrezzi2")
@MultipartConfig(location = "/tmp", fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024
		* 7, maxRequestSize = 1024 * 1024 * 7 * 2)
public class AggiornaPrezzi3 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AggiornaPrezzi3() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		

		try {
			Part part = request.getPart("file"); // Per ricevere <input type="file" name="file">
			String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString(); // nome del file caricato
			InputStream fileContent = part.getInputStream();// fileContent è il file caricato
			BufferedReader br = new BufferedReader(new InputStreamReader(fileContent));
			br.readLine();
			long startTime = System.currentTimeMillis();
			EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
			String row = br.readLine();
			while (row != null) {
				// Devo creare distributore e prezzo nuovo da inserire, fare i parse da string
				try {
					row = br.readLine();
					String[] column = row.split(";");
					Prezzo u = new Prezzo();
					int id = Integer.parseInt(column[0]);
					Double prezzo = Double.parseDouble(column[2]);
					int isSelf = Integer.parseInt(column[3]);

					try {
						u = (Prezzo) em.createQuery(
								"Select c FROM Prezzo c WHERE c.descCarburante LIKE :descCarburante AND c.isSelf LIKE : isSelf AND c.dtComu LIKE : dtComu")
								.setParameter("descCarburante", column[1]).setParameter("isSelf", isSelf)
								.setParameter("dtComu", column[4]).getSingleResult();
						System.out.println("duplicato");
						continue;
					} catch (Exception e) {
						System.out.println("continue a creare nuovo");
					}
					Distributore d = (Distributore) em
							.createQuery("Select c FROM Distributore c WHERE c.idImpianto LIKE :name")
							.setParameter("name", id).getSingleResult();
					System.out.println("creato nuovo");
					u.setDistributore(d);
					u.setDescCarburante(column[1]);
					u.setPrezzo(prezzo);
					u.setIsSelf(isSelf);
					u.setDtComu(column[4]);
					em.getTransaction().begin();
					em.persist(u);
					em.getTransaction().commit();
					System.out.println("commited");
				} catch (Exception e) {
					System.out.println("exception" + e.toString());
				}
			}
			request.setAttribute("messageSuccesfulPrice", "File: " + fileName + ", dati inseriti correttamente.");
			long endTime = System.currentTimeMillis();
			System.out.println("Tempo di esecuzione: " + (endTime - startTime));
		} catch (

		Exception e) {
			request.setAttribute("messageErrorPrice", "Errore, caricamento non riuscito.");
		}

		RequestDispatcher rd = request.getRequestDispatcher("flusso.jsp");
		rd.include(request, response);
	}
}
