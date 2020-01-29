package controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class AggiornaPrezzi
 */
@WebServlet("/aggiornaPrezzi")
@MultipartConfig
public class AggiornaPrezzi extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AggiornaPrezzi() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Part part = request.getPart("file"); // Per ricevere <input type="file" name="file">
		String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
		// MSIE fix, riceve il nome del file
		File uploads = new File("/home/diego/Desktop/");
		File file = new File(uploads, "nomeacaso.csv");
		try (InputStream fileContent = part.getInputStream()) {
			Files.copy(fileContent, file.toPath());
			request.setAttribute("messageSuccesful", "Dati inseriti correttamente!");
		}
		RequestDispatcher rd = request.getRequestDispatcher("flusso.jsp");
		rd.include(request, response);
	}
}
