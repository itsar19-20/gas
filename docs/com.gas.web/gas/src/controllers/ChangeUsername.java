package controllers;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import business.AdminManager;
import business.AuthenticationManager;
import model.User;
import utils.JPAUtil;

/**
 * Servlet implementation class ChangeUsername
 */
@WebServlet("/ChangeUsername")
public class ChangeUsername extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangeUsername() {
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
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String newUsername = request.getParameter("newUsername");
		AuthenticationManager am = new AuthenticationManager();
		AdminManager adminManager = new AdminManager();
		User checkLogin = am.loginUsers(username, password);
		if (checkLogin == null) {
			response.sendError(404);
		} else if (am.verifyUser(newUsername) != null) {
			response.sendError(403);
		} else {
			User _return = adminManager.changeUsername(username, newUsername);
			ObjectMapper om = new ObjectMapper();
			response.setContentType("application/json");
			response.getWriter().append(om.writeValueAsString(_return));
		}
	}

}
