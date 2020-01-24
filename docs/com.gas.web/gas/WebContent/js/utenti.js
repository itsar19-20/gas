$(document).ready(() => {
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
                            return '<button class="btnUser" data-id="' + row.id + '">Modifica</button>';
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
            table = $('#tblUsers').DataTable({
                retrieve: true,
                paging: false
            });
            $('#tblUsers tbody').on('click', '.btnUser', function () { 
                let data_row = table.row($(this).closest('tr')).data();
                console.log(data_row.nome);
             })
        })
        .fail(() => {
            alert('OOPS, Utenti non sono caricati!');
        })
});
