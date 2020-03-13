//cerco se il file caricato ha estensione .CSV e metto l'attributo action nel Form
//forma a destra
function getFile(filePath) {
    return filePath.substr(filePath.lastIndexOf('\\') + 1).split('.')[0];
}
$('#file').change(() => {
    let nomeFile = getFile(file.value);
    let nomeEstensione = file.value.split('.')[1];
    let estensioneCorreta = "csv"
    let risultato = nomeEstensione.localeCompare(estensioneCorreta);
    if (risultato == 0) {
        $('#formUpdatePrice').attr('action', '/gas/aggiornaPrezzi');
    } else {
        $('#formUpdatePrice').attr('action', '/gas/flusso.jsp');
    }
});

//forma a sinistra
$('#fileA').change(() => {
    let nomeFileA = getFile(fileA.value);
    let nomeEstensioneA = fileA.value.split('.')[1];
    let estensioneCorretaA = "csv"
    let risultatoA = nomeEstensioneA.localeCompare(estensioneCorretaA);
    if (risultatoA == 0) {
        $('#formUpdateStation').attr('action', '/gas/aggiornaDistributori');
    } else {
        $('#formUpdateStation').attr('action', '/gas/flusso.jsp');
    }
});

//loading screen
function hideLoader() {
    $('#loading').hide();
}
function showLoader() {
    $('#loading').show();
}
$(document).ready(hideLoader);
$('#btnCaricaPrezzo').click(() => {
    showLoader();
});
$('#btnCaricaDistributori').click(() => {
    showLoader();
});