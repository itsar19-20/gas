$(() => {
    $('#btnLogin').click(() => {
        $.ajax({
            url: '/gas/login',
            method: 'post',
            data: {
                username: $('#inputUsername').val(),
                password: $('#inputPassword').val()
            }
        })
            .done((utente) => {
                if (utente) {
                    localStorage.setItem('user', JSON.stringify(utente));
                    location.href = './utenti.html';
                } else {
                    localStorage.removeItem('user');
                }
            }).fail(() => {
                alert("OOPS...Qualcosa e' andato storto!")
            })
    });
});