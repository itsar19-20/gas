$(() => {
    if (localStorage.getItem('user')) {
        var utente = JSON.parse(localStorage.getItem('user'));
        $('#nomeAdmin').text(`${utente.nome}`);
    } else {
        location.href = './login.html';
    }
});