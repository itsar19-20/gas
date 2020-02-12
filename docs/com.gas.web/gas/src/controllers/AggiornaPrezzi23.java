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
import javax.transaction.Transactional;

import model.Distributore;
import model.Prezzo;
import utils.JPAUtil;

/**
 * Servlet implementation class AggiornaPrezzi
 */
@WebServlet("/aggiornaPrezzi23")
@MultipartConfig(location = "/tmp", fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024
		* 7, maxRequestSize = 1024 * 1024 * 7 * 2)
public class AggiornaPrezzi23 extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AggiornaPrezzi23() {
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
			int batchsize = 500;
			int i = 0;
			em.getTransaction().begin();
			long startTime = System.currentTimeMillis();
			while (s.hasNext()) {

				try {
					String row = s.next();
					String[] column = row.split(";");
					Prezzo p = new Prezzo();
					int id = Integer.parseInt(column[0]);
					Double prezzo = Double.parseDouble(column[2]);
					int isSelf = Integer.parseInt(column[3]);
//					em.createNativeQuery("INSERT INTO prezzo (idImpianto, descCarburante, prezzo, isSelf, dtComu) "
//							+ "VALUES (?,?,?,?,?)")
//				      .setParameter(1, id)
//				      .setParameter(2, column[1])
//				      .setParameter(3, prezzo)
//				      .setParameter(4, isSelf)
//				      .setParameter(5, column[4])
//				      .executeUpdate();
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
					if (++i % batchsize == 0) {
						em.flush();
						em.clear();
						System.out.println("entitymanager Flushed");
					}
					System.out.println("persisted");

				} catch (Exception e) {
					System.out.println("exception " + e.toString());
				}
			}
			request.setAttribute("messageSuccesfulPrice", "File: " + fileName + ", dati inseriti correttamente.");
			em.getTransaction().commit();
			s.close();
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
