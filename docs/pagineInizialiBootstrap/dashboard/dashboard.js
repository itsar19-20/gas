// cambio attributo "action" per la forma a sinistra
$('#deleteUser').click(function () {
   $('#userFound').attr('action', 'cancellaUtente');
});
$('#editUser').click(function () {
   $('#userFound').attr('action', 'editUser');
});
$('#addAdmin').click(function () {
   $('#userFound').attr('action', 'addAdmin');
});
$('#removeAdmin').click(function () {
   $('#userFound').attr('action', 'removeAdmin');
});
