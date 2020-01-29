<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Administrator Page</title>
<link rel="shortcut icon" type="image/x-icon" href="favicon.ico?" />
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
<link href="css/dashboard.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="css/flusso.css">
</head>

<body>

	<nav
		class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
		<a class="navbar-brand col-sm-3 col-md-2 mr-0" href="/gas/index.html">GasAdvisor</a>
		<input class="form-control form-control-dark w-100" type="text"
			placeholder="Search" aria-label="Search">
		<ul class="navbar-nav px-3">
			<li class="nav-item text-nowrap"><a class="nav-link" id="logout">Uscita</a></li>
		</ul>
	</nav>
	<!--Inizia il sidebar a sinistra-->
	<div class="container-fluid" id="container_main">
		<div class="row" id="container_main_two">
			<nav class="col-md-2 d-none d-md-block bg-light sidebar"
				id="containerAdmin">
				<div class="sidebar-sticky">
					<h5 id="nomeAdmin">Ciao</h5>
					<!--Inizia la lista delle scelte per l'admin-->
					<ul class="nav flex-column border-left pl-3">
						<li class="nav-item"><a class="nav-link navListUtenti"
							href="/gas/utenti.html"> <span data-feather="home"></span>
								Utenti <span class="sr-only"></span>
						</a></li>
						<li class="nav-item"><a class="nav-link navListFlusso"
							href="/gas/flusso.html"> <span data-feather="file"></span>
								Flusso Giornaliero
						</a></li>
						<li class="nav-item"><a class="nav-link navListNotizie"
							href="/gas/mappa.html"> <span data-feather="shopping-cart"></span>
								Mappa
						</a></li>
						<li class="nav-item"><a class="nav-link navListRecensioni"
							href="/gas/recensioni.html"> <span data-feather="users"></span>
								Recensioni
						</a></li>
						<li class="nav-item"><a class="nav-link navListStatistiche"
							href="/gas/statistiche.html"> <span data-feather="layers"></span>
								Statistiche
						</a></li>
					</ul>
				</div>
			</nav>

			<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4"
				id="containerDatiApp">
				<div
					class="d-flex justify-content-center flex-wrap flex-md-nowrap align-items-center pt-4 mt-2 pb-2 mb-3 border-bottom">
					<h1 class="h2" id="datiApp">Dati Applicazione</h1>
				</div>
				<!--tutto il container con dati utenti a destra-->
				<div class="border d-flex" id="adminUtils">
					<!--Container a sinistra-->
					<div class="border d-flex flex-column justify-content-center"
						id="containerStations">
						<div class="d-flex flex-column-reverse text-center"
							id="textContainerStations">
							<small>Scegli file CSV da inserire</small>
							<h3 class="pb-5">Aggiorna Distributori!</h3>
						</div>
						<br>
						<form id="formUpdateStation" class="pt-3"
							action="/gas/aggiornaDistributori" method="POST"
							enctype="multipart/form-data">
							<div class="form-group d-flex flex-column">
								<div class="input-group d-flex justify-content-center pb-5">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<input type="file" name="file"> <span><a
												href="https://www.mise.gov.it/index.php/it/open-data/elenco-dataset/2032336-
                                carburanti-prezzi-praticati-e-anagrafica-degli-impianti"
												target="_blank">Scarica CSV!</a></span>
										</div>
									</div>
								</div>
								<br>
								<div class="d-flex justify-content-center">
									<button type="" class="btn btn-primary">Carica</button>
								</div>
								<br>
							</div>
						</form>
					</div>
					<!--Container a destra-->
					<div class="border d-flex flex-column" id="containerPrice">
						<div class="d-flex flex-column-reverse text-center"
							id="textContainerPrice">
							<small>Scegli file CSV da inserire</small>
							<h3 class="pb-5">Aggiorna Prezzi!</h3>
						</div>
						<br>
						<form id="formUpdatePrice" class="pt-3"
							action="/gas/aggiornaPrezzi" method="POST"
							enctype="multipart/form-data">
							<div class="form-group d-flex flex-column">
								<div class="input-group d-flex justify-content-center pb-5">
									<div class="input-group-prepend">
										<div class="input-group-text">
											<input type="file" name="file"> <span><a
												href="https://www.mise.gov.it/index.php/it/open-data/elenco-dataset/2032336-
                                carburanti-prezzi-praticati-e-anagrafica-degli-impianti"
												target="_blank">Scarica CSV!</a></span>
										</div>
									</div>
								</div>
								<br>
								<div class="d-flex justify-content-center">
									<button type="" class="btn btn-primary">Carica</button>
								</div>
								<br>
								<div class="d-flex justify-content-center">
									<p id="msgUploadSuccesful"> ${ messageSuccesful } </p>
								</div>
							</div>
						</form>
					</div>
				</div>
			</main>
		</div>
	</div>
	<script src="https://code.jquery.com/jquery-3.4.1.min.js"
		integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
		crossorigin="anonymous"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/feather-icons/4.9.0/feather.min.js"></script>
	<script src="js/dashboard.js"></script>
	<script src="js/flusso.js"></script>
</body>

</html>