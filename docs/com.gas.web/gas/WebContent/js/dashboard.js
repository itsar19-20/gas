//Verifica che Admin sia loggato altrimenti manda in Login
$(() => {
	var remember = localStorage.getItem('remember');
	var utente = JSON.parse(localStorage.getItem('user'));
	var temputente = JSON.parse(sessionStorage.getItem('user'));
	if (localStorage.getItem('user')) {
		$('#nomeAdmin').text(`Benvenuto ${ utente.nome }`);	// Se la funzione "Remember me" non è attiva, alla chiusura della pagina cancella lo user
	} else if (sessionStorage.getItem('user')) {
		$('#nomeAdmin').text(`Benvenuto ${ temputente.nome }`);
	} else {
		location.href = './login.html';
	}
});
// bottone logout per tutti i html che usano il dashboard
$('#logout').click(() => {
	sessionStorage.removeItem('user');
	localStorage.removeItem('user');
	location.href = './index.html';
});