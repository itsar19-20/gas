$(document).ready(() => {
    /**Inizio creazione tabella */
    $.ajax({
        url: '/gas/cercaValutazioni',
        method: 'get'
    })
        .done((valutazioni) => {

            $('#tblRecensioni').DataTable({
                data: valutazioni,
                columns: [
                    {
                        data: null,
                        render: function (data, type, row) {
                            return '<button class="btnEdit btn btn-primary btn-sm">Modifica</button>';
                        }
                    },
                    {
                        data: null,
                        render: function (data, type, row) {
                            return '<button class="btnDelete btn btn-danger btn-sm">Cancella</button>';
                        }
                    },
                    { title: 'ID', data: 'id' },
                    { title: 'Descrizione', data: 'descrizione' },
                    { title: 'Giudizio', data: 'giudizio' },
                    { title: 'Distributore', data: 'distributore' },
                    { title: 'Data', data: 'data' },
                    { title: 'User', data: 'user.username' },
                ],
                language: {
                    lengthMenu: "Mostra _MENU_ Valutazioni per pagina",
                    zeroRecords: "Nessuna Valutazione trovata - ci dispiace!",
                    info: "Mostrando pagina _PAGE_ di _PAGES_",
                    infoEmpty: "Niente dati disponibili!",
                    infoFiltered: "(Filtrato da _MAX_ Valutazioni totali)",
                    search: "Cerca",
                    paginate: {
                        first: "Inizio",
                        last: "Fine",
                        next: "Prossimo",
                        previous: "Precedente"
                    }
                }
            });
        })
        /**Fail del caricamento tabella */
        .fail(() => {
            alert('OOPS, Valutazioni non caricati!');
        })
});
