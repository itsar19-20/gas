package controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import business.AuthenticationManager;
import business.DistributoreManager;
import business.ValutazioneManager;
import model.Distributore;
import model.User;
import model.Valutazione;

/**
 * Servlet implementation class AggiungiValutazione
 */
@WebServlet("/AggiungiValutazione")
public class AggiungiValutazione extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AggiungiValutazione() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = null;
		String username, password, nome, cognome, email, descrizione;
		int giudizio, idImpianto;
		username = request.getParameter("username");
		AuthenticationManager am = new AuthenticationManager();
		user = am.verifyUser(username);
		if (user == null) {
			email = request.getParameter("email");
			nome = request.getParameter("nome");
			cognome = request.getParameter("cognome");
			password = request.getParameter("password");
			user = am.signup(email, nome, cognome, username, password);
		}
		DistributoreManager dm = new DistributoreManager();
		idImpianto = Integer.parseInt(request.getParameter("idImpianto"));
		Distributore distributore = dm.getDistributore(idImpianto);
		ValutazioneManager vm = new ValutazioneManager();
		giudizio = Integer.parseInt(request.getParameter("giudizio"));
		descrizione = request.getParameter("descrizione");
		Valutazione valutazione = vm.addValutazione(user, distributore, giudizio, descrizione);
		//ObjectMapper om = new ObjectMapper();
		//response.setContentType("application/json");
		response.getWriter().append("Operazione andata a buon fine");
	}
}
