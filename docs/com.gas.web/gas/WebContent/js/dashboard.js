//Verifica che Admin sia loggato altrimenti manda in Login
$(() => {
	var remember = localStorage.getItem('remember');
	var utente = JSON.parse(localStorage.getItem('user'));
	//alert(remember);
	if (localStorage.getItem('user')) {
		$('#nomeAdmin').text(`Benvenuto ${ utente.nome }`);
		// Se la funzione "Remember me" non è attiva, alla chiusura della pagina cancella lo user
		if (remember == 0) {
			window.onbeforeunload = function() {
				localStorage.removeItem('user');
				  return '';
				};
		}
	} else {
		location.href = './login.html';
	}
});
// bottone logout per tutti i html che usano il dashboard
$('#logout').click(() => {
	localStorage.removeItem('user');
	location.href = './index.html';
});