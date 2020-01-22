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

$('#searchForUserBtn').click(function() {
	let selected = $("input[name=trovaUtente]:checked").val();
	if (selected === "byUsername") {
		$('#searchForUser').attr('action', 'dashboard_cercaUtente');
	} else {
		$('#searchForUser').attr('action', 'searchUserByEmail');
	}
});