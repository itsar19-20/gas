$(document).ready(() => {
    /**Inizio creazione tabella */
    $.ajax({
        url: '/gas/cercaValutazioni',
        method: 'get'
    })
        .done((valutazioni) => {
            /* Formatting function for row details - modify as you need */
            function format(valutazioni) {
                // `d` is the original data object for the row
                return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">' +
                    '<tr>' +
                    '<td>Commento:</td>' +
                    '<td>' + valutazioni.descrizione + '</td>' +
                    '</tr>' +
                    '</table>';
            }

            $('#tblRecensioni').DataTable({
                data: valutazioni,
                columns: [
                    {
                        className: 'details-control',
                        orderable: false,
                        data: null,
                        defaultContent: ''
                    },
                    {
                        data: null,
                        render: function (data, type, row) {
                            return '<button class="btnDelete btn btn-danger btn-sm">Cancella</button>';
                        }
                    },
                    { title: 'ID', data: 'id' },
                    { title: 'Commento', data: 'descrizione' },
                    { title: 'Giudizio', data: 'giudizio' },
                    { title: 'Distributore', data: 'distributore.nomeImpianto' },
                    {
                        title: 'Data', data: 'data', render: function (data) {
                            var date = new Date(data);
                            var dt = date.getDate();
                            var month = date.getMonth() + 1;
                            return (dt.toString().length > 1 ? dt : "0" + dt) + "/" + (month.toString().length > 1 ? month : "0" + month) + "/" +
                                date.getFullYear();
                        }
                    },
                    { title: 'User', data: 'user.username' },
                ],
                order: [1, 'asc'],
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
            /**Serve perche la tabella da errore quando viene chiamata */
            table = $('#tblRecensioni').DataTable({
                retrieve: true,
                paging: false
            });

            /** Onclick bottone espandi commento */
            $('#tblRecensioni tbody').on('click', 'td.details-control', function () {
                var tr = $(this).closest('tr');
                var row = table.row(tr);

                if (row.child.isShown()) {
                    // This row is already open - close it
                    row.child.hide();
                    tr.removeClass('shown');
                }
                else {
                    // Open this row
                    row.child(format(row.data())).show();
                    tr.addClass('shown');
                }
            });

            /** Funzione onClick per bottone Cancella, attiva e mette parametri in modal */
            $('#tblRecensioni tbody').on('click', '.btnDelete', function () {
                let data_row = table.row($(this).closest('tr')).data();
                $('#modalDelete').modal();
                $('#deleteIdValutazione').text(data_row.id);
                $('#deleteUser').text(data_row.user.username);
                $('#deleteDistributore').text(data_row.distributore.nomeImpianto);
                $('#deleteGiudizio').text(data_row.giudizio);
                $('#deleteDescrizione').text(data_row.descrizione);
            })

            /** Funzione onClick per bottone Cancella dentro il modal Delete */
            $('#btnDeleteValutazione').click(() => {
                $.ajax({
                    url: '/gas/cancellaValutazione',
                    method: 'get',
                    data: {
                        idValutazione: $('#deleteIdValutazione').text()
                    },
                }).done((risposta) => {
                    if (risposta) {
                        location.href = './recensioni.html';
                    }
                }).fail(() => {
                    alert("Qualcosa e' andato storto!")
                })
            });

        })
        /**Fail del caricamento tabella */
        .fail(() => {
            alert('OOPS, Valutazioni non caricate!');
        })
});
