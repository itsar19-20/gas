package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import business.DatabaseManager;

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
		Part part = request.getPart("fileA"); // Per ricevere <input type="file" name="file">
		String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString(); // nome del file caricato
		InputStream fileContent = part.getInputStream();// fileContent è il file caricato
		Scanner s = new Scanner(fileContent).useDelimiter("\\n");
		try {
			System.out.println("arrivato");
			while (s.hasNext()) {
				String row = s.next();
				String[] column = row.split(";");
				dm.aggiornaPrezzi(column[0], column[1], column[2], column[3], column[4]);
				s.close();
			}
			request.setAttribute("messageSuccesfulStation", "File: " + fileName + " Dati inseriti correttamente.");
		} catch (Exception e) {
			s.close();
			request.setAttribute("messageErrorStation", "Errore, caricamento non riuscito.");
		}
		RequestDispatcher rd = request.getRequestDispatcher("flusso.jsp");
		rd.include(request, response);

	}

}
