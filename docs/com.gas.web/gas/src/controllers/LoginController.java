package controllers;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import business.AuthenticationManager;
import model.User;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/dashboard")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AuthenticationManager am = new AuthenticationManager();
		User u = am.login(request.getParameter("username"), request.getParameter("password"));
		if (u == null) {
			PrintWriter out = response.getWriter();
			out.println("sbagliato");
			//request.getRequestDispatcher("/").forward(request, response);
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("nome", u.getNome());
			request.getRequestDispatcher("/login/admin/dashboard.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
