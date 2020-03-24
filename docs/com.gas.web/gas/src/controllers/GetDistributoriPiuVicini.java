package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import business.DistributoreManager;
import model.Prezzo;

/**
 * Servlet implementation class GetDistributoriPiuVicini
 */
@WebServlet("/GetDistributoriPiuVicini")
public class GetDistributoriPiuVicini extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetDistributoriPiuVicini() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String carburante = request.getParameter("carburante");
		int distanza = Integer.parseInt(request.getParameter("distanza"));
		double latitudine = Double.parseDouble(request.getParameter("latitudine"));
		double longitudine = Double.parseDouble(request.getParameter("longitudine"));
		DistributoreManager distributoreManager = new DistributoreManager();
		List<Prezzo> risposta = new ArrayList<>();
		try {
			risposta = distributoreManager.cercaPiuVicini(carburante, distanza, latitudine, longitudine);
		} catch (Exception e) {
			response.sendError(403);
		}
		if (risposta != null) {
			ObjectMapper om = new ObjectMapper();
			response.setContentType("application/json");
			response.getWriter().append(om.writeValueAsString(risposta));
		} else {
			response.sendError(500);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
