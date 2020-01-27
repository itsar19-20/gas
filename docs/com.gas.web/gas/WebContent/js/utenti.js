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
                            return '<button class="btnEdit btn btn-primary btn-sm" data-id="' + row.id + '">Modifica</button>';
                        }
                    },
                    {
                        data: null,
                        render: function (data, type, row) {
                            return '<button class="btnDelete btn btn-danger btn-sm" data-id="' + row.id + '">Cancella</button>';
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
                ],
                language: {
                    lengthMenu: "Mostra _MENU_ Utenti per pagina",
                    zeroRecords: "Nessun Utente trovato - ci dispiace!",
                    info: "Mostrando pagina _PAGE_ di _PAGES_",
                    infoEmpty: "Niente dati disponibili!",
                    infoFiltered: "(Filtrato da _MAX_ Utenti totali)",
                    search: "Cerca",
                    paginate: {
                        first: "Inizio",
                        last: "Fine",
                        next: "Prossimo",
                        previous: "Precedente"
                    }
                }
            });
            /**Serve perche la tabella da errore quando viene chiamata */
            table = $('#tblUsers').DataTable({
                retrieve: true,
                paging: false
            });

            /** Funzione onClick per bottone Cancella, attiva e mette parametri in modal */
            $('#tblUsers tbody').on('click', '.btnDelete', function () {
                let data_row = table.row($(this).closest('tr')).data();
                $('#modalDelete').modal();
                $('#deleteUsername').text(data_row.username);
                $('#deleteName').text(data_row.nome);
                $('#deleteLastname').text(data_row.cognome);
                $('#deleteEmail').text(data_row.email);
            })

            /** Funzione onClick per bottone Cancella dentro il modal Delete */
            $('#btnDeleteUser').click(() => {
                $.ajax({
                    url: '/gas/deleteUser',
                    method: 'get',
                    data: {
                        deleteUsername: $('#deleteUsername').text()
                    },
                }).done((risposta) => {
                    if (risposta) {
                        location.href = './utenti.html';
                    }
                }).fail(() => {
                    alert("Qualcosa e' andato storto!")
                })
            });

            /** Funzione onClick per bottone Modifica, attiva e mette parametri in modal */
            $('#tblUsers tbody').on('click', '.btnEdit', function () {
                let data_row = table.row($(this).closest('tr')).data();
                $('#modalEdit').modal();
                $('#editUsername').text(data_row.username);
                $('#editName').val(data_row.nome);
                $('#editLastname').val(data_row.cognome);
                $('#editEmail').val(data_row.email);
                //$('#div.editForm select').val(data_row.isAdmin);
            })

            /** Funzione onClick per bottone Salva Modifiche dentro il modal Modifica */
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
                        editUsername: $('#editUsername').text()
                    },
                    dataType: 'text json'
                }).done((user) => {
                    if (user) {
                        location.href = './utenti.html';
                    }
                }).fail(() => {
                    alert("Qualcosa e' andato storto!")
                })
            });
        })
        /**Fail del caricamento tabella */
        .fail(() => {
            alert('OOPS, Utenti non sono caricati!');
        })
});
