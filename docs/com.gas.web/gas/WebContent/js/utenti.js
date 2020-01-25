$(document).ready(() => {
    /**Inizio creazione tabella */
    $.ajax({
        url: '/gas/cercaUtenti',
        method: 'get'
    })
        .done((users) => {

            $('#tblUsers').DataTable({
                data: users,
                columns: [
                    {
                        data: null,
                        render: function (data, type, row) {
                            return '<button class="btnEdit" data-id="' + row.id + '">Modifica</button>';
                        }
                    },
                    { title: 'ID Utente', data: 'id' },
                    { title: 'Nome', data: 'nome' },
                    { title: 'Cognome', data: 'cognome' },
                    { title: 'Data di Registrazione', data: 'dataRegistrazione' },
                    { title: 'Ultimo Login', data: 'dataUltimaLogin' },
                    { title: 'Username', data: 'username' },
                    { title: 'Admin', data: 'isAdmin' },
                    { title: 'E-Mail', data: 'email' },
                ]
            });
            /**Serve perche la tabella da errore quando viene chiamata */
            table = $('#tblUsers').DataTable({
                retrieve: true,
                paging: false
            });
            /** Funzione onClick per bottone Modifica, attiva e mette parametri in modal */
            $('#tblUsers tbody').on('click', '.btnEdit', function () { 
                let data_row = table.row($(this).closest('tr')).data();
                $('#modalEdit').modal();
                $('#editUsername').val(data_row.username);
                $('#editName').val(data_row.nome);
                $('#editLastname').val(data_row.cognome);
                $('#editEmail').val(data_row.email);
                //$('#div.editForm select').val(data_row.isAdmin);
             })

             /** Funzione onClick per bottone salva modifiche dentro il modal */
             $('#btnSaveEdit').click(() => { 
                $.ajax({
                    url: '/gas/editUser',
                    method: 'get',
                    contentType: 'application/json; charset=utf-8',
                    data: {
                        editName: $('#editName').val(),
                        editLastname: $('#editLastname').val(),
                        editEmail: $('#editEmail').val(),
                        editAdmin: $('select[name=editAdmin]').val(),
                        editUsername: $('#editUsername').val()
                    },
                    dataType: 'text json'
                }).done((user) => {
                    if (user) {
                        location.href = './utenti.html';
                    }
                })
             });
        })
        /**Fail del caricamento tabella */
        .fail(() => {
            alert('OOPS, Utenti non sono caricati!');
        })
});
