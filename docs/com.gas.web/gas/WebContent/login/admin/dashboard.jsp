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
							<form class="d-flex flex-wrap" id="userFound">
								<div class="form-group adminUtilsData px-3 mx-3 py-3">
									<label for="nameUser">Nome</label> <input type="text"
										class="form-control" id="nameUser">
								</div>
								<div class="form-group adminUtilsData px-3 mx-3 py-3">
									<label for="lastNameUser">Cognome</label> <input type="text"
										class="form-control" id="lastNameUser">
								</div>
								<div class="form-group adminUtilsData px-3 mx-3 py-3">
									<label for="emailUser">E-mail</label> <input type="text"
										class="form-control" id="emailUser">
								</div>
								<div class="form-group adminUtilsData px-3 mx-3 py-3">
									<label for="usernameUser">Nome Utente</label> <input
										type="text" class="form-control" id="usernameUser">
								</div>
								<div class="form-group adminUtilsData px-3 mx-3 py-3">
									<label for="isAdminUser">Admin</label> <input disabled
										type="text" class="form-control" id="isAdminUser">
								</div>
								<div class="form-group adminUtilsData px-3 mx-3 py-3">
									<label for="reviewsUser">Numero Valutazioni</label> <input
										disabled type="text" class="form-control" id="reviewsUser">
								</div>
							</form>
						</div>
						<div class="adminUtilsBtn d-flex" style="height: 10rem;">
							<button form="userFound" type="submit" 
								class="btn btn-primary mx-3 btnDeleteUser">Cancella
								Utente</button>
							<button form="userFound" type="submit" 
								class="btn btn-primary mx-3 btnModifyUser">Modifica
								Utente</button>
							<button form="userFound" type="submit" 
								class="btn btn-primary mx-3 btnAddAdmin">Aggiungi Admin</button>
							<button form="userFound" type="submit" 
								class="btn btn-primary mx-3 btnRemoveAdmin">Rimuovi
								Admin</button>
						</div>
					</div>
					<!--Sezione Trova Utente a destra-->
					<div class="dataSearch border d-flex flex-column">
						<h5 class="text-center mt-4">Trova Utente</h5>
						<div class="formContainer d-flex flex-column">
							<div class="imageBook">
								<img src="/login/admin/db.png" width="80px" height="80px" alt="imagine_libro">
							</div>
							<form class="d-flex flex-column formData" action="dashboard_cercaUtente">
								<div class="custom-control custom-radio">
									<input checked type="radio" id="byUsername" name="trovaUtente"
										class="custom-control-input"> <label
										class="custom-control-label" for="byUsername">Cerca
										con Nome Utente</label>
								</div>
								<br>
								<div class="custom-control custom-radio">
									<input type="radio" id="byEmail" name="trovaUtente"
										class="custom-control-input"> <label
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
								<button type="submit" class="btn btn-primary">Avvia
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
</body>
</html>