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

import model.Distributore;
import utils.JPAUtil;

/**
 * Servlet implementation class AggiornaPrezzi
 */
@WebServlet("/aggiornaDistributori2")
@MultipartConfig(location = "/tmp", fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024
		* 7, maxRequestSize = 1024 * 1024 * 7 * 2)
public class AggiornaDistributori2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AggiornaDistributori2() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			Part part = request.getPart("fileA"); // Per ricevere <input type="file" name="file">
			String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString(); // nome del file caricato
			InputStream fileContent = part.getInputStream();// fileContent è il file caricato
			Scanner s = new Scanner(fileContent).useDelimiter("\\n");
			s.next();
			s.next(); // skip prime 2 righe
			System.out.println("arrivato");

			while (s.hasNext()) {
				// Devo creare distributore e prezzo nuovo da inserire, fare i parse da string
				try {
					EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
					String row = s.next();
					String[] column = row.split(";");
					Distributore d = new Distributore();
					int idImpianto = Integer.parseInt(column[0]);
					Double latitudine = Double.parseDouble(column[8]);
					Double longitudine = Double.parseDouble(column[9]);
					System.out.println("creato nuovo");
					d.setIdImpianto(idImpianto);
					d.setGestore(column[1]);
					d.setBandiera(column[2]);
					d.setTipoImpianto(column[3]);
					d.setNomeImpianto(column[4]);
					d.setIndirizzo(column[5]);
					d.setComune(column[6]);
					d.setProvincia(column[7]);
					d.setLatitudine(latitudine);
					d.setLongitudine(longitudine);
					em.getTransaction().begin();
					em.persist(d);
					System.out.println("persisted");
					em.getTransaction().commit();
					System.out.println("commited");
				} catch (Exception e) {
					System.out.println("exception");
				}
			}
			request.setAttribute("messageSuccesfulStation", "File: " + fileName + ", dati inseriti correttamente.");
			s.close();
		} catch (

		Exception e) {
			request.setAttribute("messageErrorStation", "Errore, caricamento non riuscito.");
		}

		RequestDispatcher rd = request.getRequestDispatcher("flusso.jsp");
		rd.include(request, response);
	}
}
