package business;

import model.User;

public class Prova {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AuthenticationManager a = new AuthenticationManager();
		User b = a.login("admin", "admin");
		System.out.println(b.toString());
		
		
	}

}
