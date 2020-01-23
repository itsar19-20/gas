$(() => {
    if (localStorage.getItem('user')) {
        var utente = JSON.parse(localStorage.getItem('user'));
        $('#nomeAdmin').text(`Benvenuto ${utente.nome}`);
    } else {
        location.href = './login.html';
    }
});
$('#logout').click(() => {
    localStorage.removeItem('user');
    location.href = './index.html';
});