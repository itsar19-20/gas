package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
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

import org.hibernate.FlushMode;

import model.Distributore;
import model.Prezzo;
import utils.JPAUtil;

/**
 * Servlet implementation class AggiornaPrezzi
 */
@WebServlet("/aggiornaPrezzi2")
@MultipartConfig(location = "/tmp", fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024
		* 7, maxRequestSize = 1024 * 1024 * 7 * 2)
public class AggiornaPrezzi2 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AggiornaPrezzi2() {
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
			EntityManager em = JPAUtil.getInstance().getEmf().createEntityManager();
			em.setFlushMode(FlushModeType.COMMIT);
			int batchsize = 1000;
			int i = 0;
			em.getTransaction().begin();
			while (s.hasNext() == true) {
				i = i + 1;
				// Devo creare distributore e prezzo nuovo da inserire, fare i parse da string
				try {
					
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
					em.persist(p);
					if(i == 999) {
						em.flush();
						em.clear();
					}
					System.out.println("persisted");
				} catch (Exception e) {
					System.out.println("exception");
				}
			}
			request.setAttribute("messageSuccesfulPrice", "File: " + fileName + ", dati inseriti correttamente.");
			em.getTransaction().commit();
			s.close();
		} catch (

		Exception e) {
			request.setAttribute("messageErrorPrice", "Errore, caricamento non riuscito.");
		}

		RequestDispatcher rd = request.getRequestDispatcher("flusso.jsp");
		rd.include(request, response);
	}
}
