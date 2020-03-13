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
                	if ($("input[type=checkbox]").is( 
                    ":checked")) {
                    localStorage.setItem('user', JSON.stringify(utente));
                    localStorage.setItem('remember',1);
                    location.href = './utenti.html';
                    
                } else {
                	localStorage.setItem('user', JSON.stringify(utente));
                	localStorage.setItem('remember',0);
                	location.href = './utenti.html';
                    
            	  } } else {
            		  localStorage.removeItem('user');
                      alert("Questo Utente non puo' procedere!")
            	  }
            	}).fail(() => {
                alert("OOPS...Qualcosa e' andato storto!")
         
    }); })           
});