package controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import business.AdminManager;
import model.User;

/**
 * Servlet implementation class SearchUserByEmail
 */
@WebServlet("/searchUserByEmail")
public class SearchUserByEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchUserByEmail() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AdminManager am = new AdminManager();
		User u = am.searchUserByEmail(request.getParameter("cerca"));
		HttpSession session = request.getSession();
		session.setAttribute("nomeUtente", u.getNome());
		session.setAttribute("cognomeUtente", u.getCognome());
		session.setAttribute("emailUtente", u.getEmail());
		session.setAttribute("usernameUtente", u.getUsername());
		session.setAttribute("isAdminUtente", u.getIsAdmin());
		
		request.getRequestDispatcher("/login/admin/dashboard_cercaUtente.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
