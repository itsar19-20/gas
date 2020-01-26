$('#areaRiservata').click(() => {
    if (localStorage.getItem('user')) {
        location.href = './utenti.html';
    } else {
        location.href = './login.html';
    }
});
$('#scaricaApp').click(() => {
    location.href = 'https://play.google.com/store/apps?hl=it';
});
