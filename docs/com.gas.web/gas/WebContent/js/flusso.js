//cerco se il file caricato ha estensione .CSV e metto l'attributo action
function getFile(filePath) {
    return filePath.substr(filePath.lastIndexOf('\\') + 1).split('.')[0];
}
$('#file').change(() => { 
    let nomeFile = getFile(file.value);
    let nomeEstensione = file.value.split('.')[1];
    let estensioneCorreta = "csv"
    let risultato = nomeEstensione.localeCompare(estensioneCorreta);
    if(risultato == 0) {
        $('#formUpdatePrice').attr('action', '/gas/aggiornaPrezzi');
    } else {
        $('#formUpdatePrice').attr('action', '/gas/flusso.html');
    }
});