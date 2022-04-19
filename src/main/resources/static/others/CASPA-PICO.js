function show_appareil(button){
	var actif = document.getElementsByClassName("un_appareil_actif");
	for(let i = 0 ; i < actif.length ; i++){
		actif[i].className = "un_appareil";
	}
	document.getElementById("default_appareil_INFO").className = "un_appareil_info_cache";
	button.className = "un_appareil_actif"
}

function swap_overlay_appareils(self, target){
	document.getElementById(self.id).style.filter = "grayscale(0%)";
	document.getElementById('object_' + self.id).style.display = "flex";
	document.getElementById(target).style.filter = "grayscale(75%)";
	document.getElementById('object_' + target).style.display = "none";
}

function appareillage_off(){
	document.getElementById("overlay_appareillage").style.display = "none";
	document.getElementById("overlay_appareillage").innerHTML = "";
}

function appareillage_on(){
	document.getElementById("overlay_appareillage").style.display = "block";
}

function back(){
	window.history.back();
}

function open_recherche(){
	document.getElementById("form_barre_recherche").style.minHeight = "250px";
	document.getElementById("open_barre_overlay").style.height = "0px";
	document.getElementById("form_barre_recherche").style.borderTop = "3px black solid";
}

function close_recherche(){
	document.getElementById("form_barre_recherche").style.minHeight = "0px";
	document.getElementById("open_barre_overlay").style.height = "40px";
	document.getElementById("form_barre_recherche").style.borderTop = "none";
}

