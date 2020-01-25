//Verifica che Admin sia loggato altrimenti manda in Login
$(() => {
    if (localStorage.getItem('user')) {
        var utente = JSON.parse(localStorage.getItem('user'));
        $('#nomeAdmin').text(`Benvenuto ${utente.nome}`);
    } else {
        location.href = './login.html';
    }
});
//bottone logout per tutti i html che usano il dashboard
$('#logout').click(() => {
    localStorage.removeItem('user');
    location.href = './index.html';
});