function loadClient() {
	var selectedValue = $("$listClient").val();
	alert(selectedValue);
}

$(document).ready(function() {
	document.getElementById("clientId").addEventListener('change', function() {
		$(document.getElementById("buildingId")).load(value);
		alert("Cargado");
	}, false);
});
