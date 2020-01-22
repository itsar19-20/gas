<%@page import="com.mysql.cj.Session"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Administrator Page</title>
<link rel="shortcut icon" type="image/x-icon"
	href="login/admin/favicon.ico?" />
<!-- Bootstrap core CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
	integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh"
	crossorigin="anonymous">

<style>
.bd-placeholder-img {
	font-size: 1.125rem;
	text-anchor: middle;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}

@media ( min-width : 768px) {
	.bd-placeholder-img-lg {
		font-size: 3.5rem;
	}
}
</style>

<!-- Stili di questa pagina -->
<link href="login/admin/admin.css" rel="stylesheet" type="text/css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
</head>
<body>
	<%
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "0");
		
		if(session.getAttribute("nome")==null) {
			response.sendRedirect("../../index.html");
		}
		String name = (String)session.getAttribute("nome");
		//String nomeUtente = (String)session.getAttribute("nomeUtente");
		String cognomeUtente = (String)session.getAttribute("cognomeUtente");
		String emailUtente = (String)session.getAttribute("emailUtente");
		String usernameUtente = (String)session.getAttribute("usernameUtente");
		byte isAdminUtente = (byte)session.getAttribute("isAdminUtente");
	%>
	<nav
		class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
		<a class="navbar-brand col-sm-3 col-md-2 mr-0" href="#">GasAdvisor</a>
		<input class="form-control form-control-dark w-100" type="text"
			placeholder="Search" aria-label="Search">
		<ul class="navbar-nav px-3">
			<li class="nav-item text-nowrap"><a class="nav-link" href="/gas/LogoutController">Uscita</a>
			</li>
		</ul>
	</nav>
	<!--Inizia il sidebar a sinistra-->
	<div class="container-fluid">
		<div class="row">
			<nav class="col-md-2 d-none d-md-block bg-light sidebar">
				<div class="sidebar-sticky">
					<h4 id="nomeAdmin">Ciao <%= name %></h4>
					<!--Inizia la lista delle scelte per l'admin-->
					<ul class="nav flex-column border-left">
						<li class="nav-item"><a class="nav-link" href="#"> <span
								data-feather="home"></span> Utenti <span class="sr-only"></span>
						</a></li>
						<li class="nav-item"><a class="nav-link" href="#"> <span
								data-feather="file"></span> Flusso Giornaliero
						</a></li>
						<li class="nav-item"><a class="nav-link" href="#"> <span
								data-feather="shopping-cart"></span> Notizie
						</a></li>
						<li class="nav-item"><a class="nav-link" href="#"> <span
								data-feather="users"></span> Recensioni
						</a></li>
						<li class="nav-item"><a class="nav-link" href="#"> <span
								data-feather="bar-chart-2"></span> Distributori
						</a></li>
						<li class="nav-item"><a class="nav-link" href="#"> <span
								data-feather="layers"></span> Statistiche
						</a></li>
					</ul>
				</div>
			</nav>

			<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
				<div
					class="d-flex justify-content-center flex-wrap flex-md-nowrap align-items-center pt-4 mt-3 pb-2 mb-3 border-bottom">
					<h1 class="h2" id="datiApp">Dati Applicazione</h1>
				</div>
				<!--tutto il container a destra-->
				<div class="d-flex flex-row" id="dati">
					<!--sezione untente trovato a sinistra-->
					<div class="adminUtils border d-flex flex-column">
						<div
							class="flex-grow-1 d-flex  justify-content-center align-items-center">
							<form class="d-flex flex-wrap" id="userFound" action="">
								<div class="form-group adminUtilsData px-3 mx-3 py-3">
									<label for="nameUser">Nome</label> <input type="text" name="nameUser"
										class="form-control" id="nameUser" value="${ nomeUtente }">
								</div>
								<div class="form-group adminUtilsData px-3 mx-3 py-3">
									<label for="lastNameUser">Cognome</label> <input type="text" name="lastnameUser"
										class="form-control" id="lastNameUser" value="<%= cognomeUtente %>">
								</div>
								<div class="form-group adminUtilsData px-3 mx-3 py-3">
									<label for="emailUser">E-mail</label> <input type="text" name="emailUser"
										class="form-control" id="emailUser" value="<%= emailUtente %>">
								</div>
								<div class="form-group adminUtilsData px-3 mx-3 py-3">
									<label for="usernameUser">Nome Utente</label> <input 
										type="text" class="form-control" id="usernameUser" name="usernameUser" value="<%= usernameUtente %>">
								</div>
								<div class="form-group adminUtilsData px-3 mx-3 py-3">
									<label for="isAdminUser">Admin</label> <input disabled
										type="text" class="form-control" id="isAdminUser" value="<%= isAdminUtente %>">
								</div>
								<div class="form-group adminUtilsData px-3 mx-3 py-3">
									<label for="reviewsUser">Numero Valutazioni</label> <input
										disabled type="text" class="form-control" id="reviewsUser">
								</div>
							</form>
						</div>
						<div class="adminUtilsBtn d-flex" style="height: 10rem;">
							<button form="userFound" type="submit" id="deleteUser"
								class="btn btn-primary mx-3 btnDeleteUser">Cancella
								Utente</button>
							<button form="userFound" type="submit" id="editUser"
								class="btn btn-primary mx-3 btnModifyUser">Modifica
								Utente</button>
							<button form="userFound" type="submit" id="addAdmin"
								class="btn btn-primary mx-3 btnAddAdmin">Aggiungi Admin</button>
							<button form="userFound" type="submit" id="removeAdmin"
								class="btn btn-primary mx-3 btnRemoveAdmin">Rimuovi
								Admin</button>
						</div>
					</div>
					<!--Sezione Trova Utente a destra-->
					<div class="dataSearch border d-flex flex-column">
						<h5 class="text-center mt-4">Trova Utente</h5>
						<div class="formContainer d-flex flex-column">
							<div class="imageBook">
								<svg class="bi bi-book" width="80px" height="80px"
									viewBox="0 0 20 20" fill="currentColor"
									xmlns="http://www.w3.org/2000/svg">
  									<path fill-rule="evenodd"
										d="M5.214 3.072c1.599-.32 3.702-.363 5.14 1.074a.5.5 0 01.146.354v11a.5.5 0 01-.854.354c-.843-.844-2.115-1.059-3.47-.92-1.344.14-2.66.617-3.452 1.013A.5.5 0 012 15.5v-11a.5.5 0 01.276-.447L2.5 4.5l-.224-.447.002-.001.004-.002.013-.006a5.116 5.116 0 01.22-.103 12.958 12.958 0 012.7-.869zM3 4.82v9.908c.846-.343 1.944-.672 3.074-.788 1.143-.118 2.387-.023 3.426.56V4.718c-1.063-.929-2.631-.956-4.09-.664A11.958 11.958 0 003 4.82z"
										clip-rule="evenodd" />
  									<path fill-rule="evenodd"
										d="M14.786 3.072c-1.598-.32-3.702-.363-5.14 1.074A.5.5 0 009.5 4.5v11a.5.5 0 00.854.354c.844-.844 2.115-1.059 3.47-.92 1.344.14 2.66.617 3.452 1.013A.5.5 0 0018 15.5v-11a.5.5 0 00-.276-.447L17.5 4.5l.224-.447-.002-.001-.004-.002-.013-.006-.047-.023a12.582 12.582 0 00-.799-.34 12.96 12.96 0 00-2.073-.609zM17 4.82v9.908c-.846-.343-1.944-.672-3.074-.788-1.143-.118-2.386-.023-3.426.56V4.718c1.063-.929 2.631-.956 4.09-.664A11.956 11.956 0 0117 4.82z"
										clip-rule="evenodd" />
								</svg>
							</div>
							<form class="d-flex flex-column formData" action="dashboard_cercaUtente" id="searchForUser">
								<div class="custom-control custom-radio">
									<input checked type="radio" id="byUsername" name="trovaUtente"
										class="custom-control-input" value="byUsername"> <label
										class="custom-control-label" for="byUsername">Cerca
										con Nome Utente</label>
								</div>
								<br>
								<div class="custom-control custom-radio">
									<input type="radio" id="byEmail" name="trovaUtente"
										class="custom-control-input" value="byEmail"> <label
										class="custom-control-label" for="byEmail">Cerca con
										E-mail</label>
								</div>
								<br>
								<div class="form-group">
									<label for="datiUtente"></label> <input type="text"
										class="form-control" id="datiUtente" placeholder="Cerca"
										name="cerca">
								</div>
								<br>
								<button type="submit" id="searchForUserBtn" class="btn btn-primary">Avvia
									Ricerca</button>
							</form>
						</div>
					</div>
				</div>


			</main>
		</div>
	</div>
	<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
		integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
		crossorigin="anonymous"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.9.0/feather.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.3/Chart.min.js"></script>
	<script src="login/admin/dashboard.js"></script>
</body>
</html>