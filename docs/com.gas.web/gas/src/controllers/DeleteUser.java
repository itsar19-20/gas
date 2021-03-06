package controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import business.AdminManager;

/**
 * Servlet implementation class cancellaUtente
 */
@WebServlet("/deleteUser")
public class DeleteUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeleteUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		AdminManager am = new AdminManager();
		int risposta = am.deleteUser(request.getParameter("deleteUsername"));
		if (risposta == 0) {
			response.sendError(404);
		} else {
			response.getWriter().append("Utente cancellato");
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
