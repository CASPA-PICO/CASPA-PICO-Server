function swap_overlay_appareils(self, target){
    document.getElementById(self.id).style.filter = "grayscale(0%)";
    document.getElementById('object_' + self.id).style.display = "flex";
    document.getElementById(target).style.filter = "grayscale(75%)";
    document.getElementById('object_' + target).style.display = "none";
}

function swap_appareil(self){
    var doc = document.getElementsByClassName("un_appareil_info_actif")[0];
    if(doc != null){
        doc.className = "un_appareil_info_cache";
    }

    doc = document.getElementsByClassName("un_appareil_actif")[0];
    if(doc != null){
        doc.className = "un_appareil";
    }

    document.getElementById(self.id + "_INFO").className = "un_appareil_info_actif";
    document.getElementById(self.id).className = "un_appareil_actif";
}

function ajout_appareil(self){
    document.getElementsByClassName("un_appareil_info_actif")[0].className = "un_appareil_info_cache";

    document.getElementById(self.id + "_INFO").className = "un_appareil_info_actif";
}

