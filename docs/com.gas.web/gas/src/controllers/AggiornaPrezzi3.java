package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	public static Logger log = LoggerFactory.getLogger(AggiornaPrezzi3.class);

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
			Scanner s = new Scanner(fileContent).useDelimiter("\\n");
			s.next();
			s.next(); // skip prime 2 righe
			System.out.println("arrivato");
			log.debug("starting loop");
			while (s.hasNext()) {
				// Devo creare distributore e prezzo nuovo da inserire, fare i parse da string
				try {
					EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
					String row = s.next();
					String[] column = row.split(";");
					Prezzo p = new Prezzo();
					int id = Integer.parseInt(column[0]);
					float prezzo = Float.parseFloat(column[2]);
					int isSelf = Integer.parseInt(column[3]);
					Distributore d = (Distributore) em
							.createQuery("Select c FROM Distributore c WHERE c.idImpianto LIKE :name")
							.setParameter("name", id).getSingleResult();
					System.out.println("creato nuovo");
					p.setDistributore(d);
					p.setDescCarburante(column[1]);
					p.setPrezzo(prezzo);
					p.setIsSelf(isSelf);
					p.setDtComu(column[4]);
					em.getTransaction().begin();
					em.persist(p);
					System.out.println("persisted");
					em.getTransaction().commit();
					System.out.println("commited");
				} catch (Exception e) {
					System.out.println("exception");
				}
			}
			request.setAttribute("messageSuccesfulPrice", "File: " + fileName + ", dati inseriti correttamente.");
			s.close();
		} catch (

		Exception e) {
			request.setAttribute("messageErrorPrice", "Errore, caricamento non riuscito.");
		}

		RequestDispatcher rd = request.getRequestDispatcher("flusso.jsp");
		rd.include(request, response);
	}
}
