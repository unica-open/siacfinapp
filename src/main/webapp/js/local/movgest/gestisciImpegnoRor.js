/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

/**
 * Ricevuto Manuel L.
 * 23/01/2020
 * Gestione Cruscotto Impegno Ror
 */


			
			
function checkNullToEmptyString(e) {
        
    var obj = e.srcElement || e.target;
    if(obj.value.match(/^\s*$/g)){
        obj.value = obj.value.replace(/^\s*$/g, "0,00");
    }
}

function overlayForWait(){
    document.getElementById("overlay").style.display = "block";
}

function checkFirstLoadSintesi(checkSpalma, selectsReimp, textreaReimputazione, idSintesiMotivo0, idDescrizioneMotivo0){
    if($(idSintesiMotivo0).val()!=""){
        $(idDescrizioneMotivo0).attr("readonly", true);
    }
    if($(checkSpalma).prop("checked") == true){
        var primaSelect = $(idSintesiMotivo0).val();
        if(primaSelect != ""){
            for (var i = 1; i < selectsReimp.length; i++) {
                var element = "#"+selectsReimp[i];
                var textArea = "#"+textreaReimputazione[i];               
                $(element).prop("readonly", true);
                $(textArea).attr("readonly", true);
                
            }
        }
            
    }else{
        
        for (var i = 1; i < selectsReimp.length; i++) {
            var element = "#"+selectsReimp[i];
            if($(element).val()!=""){
                var textArea = "#"+textreaReimputazione[i];
                
                $(textArea).attr("readonly", true);
            }
        }
    
    }

}

function checkSintesiNoReimp(idSintesiInes, idSintesiIns, idSintesiMan, idDescInes, idDescIns, idDescMan){
    if($(idSintesiInes).val() != ""){
        $(idDescInes).attr('readonly', true);
    }
    if($(idSintesiIns).val() != ""){
        $(idDescIns).attr('readonly', true);
    }
    if($(idSintesiMan).val() != ""){
        $(idDescMan).attr('readonly', true);
    }
}




function disabilitaPrimeAnnualita(){
    var inserimento=$("#inInserimentoValore").val();
    if(inserimento=="true"){
        var anno1 = "#annoReimp0"
        var anno2 = "#annoReimp1"
        var anno3 = "#annoReimp2"
    
        if($(anno1).val()!=""){
            $(anno1).prop("readonly", true);
        }
        if($(anno2).val()!=""){
            $(anno2).prop("readonly", true);
        }
        if($(anno3).val()!=""){
            $(anno3).prop("readonly", true);
        }
    } 

}

function show_divAssociazioni() {
 	var abbcheckd = $('#propagaSelected').is(':checked');
	if(abbcheckd) {
		for (var i = 0; i <=2; i++ ) {
			$('#divAssociazioni_'+i).show();	
		}
		
	} else {
		for (var i = 0; i <=2; i++ ) {
			$('#divAssociazioni_'+i).hide();
		}
	}
	
}
		


$(function(){
	 
	show_divAssociazioni();
	
	// $("#annoProvvedimento").attr("disabled", false);
	// $("#numeroProvvedimento").attr("disabled", false);
	// $("#listaTipiProvvedimenti").attr("disabled", false);
	// $("#lineaStruttura").attr("href", "#3h");
	// $("#lineaStruttura").attr("data-toggle", "collapse");

    var inserimento=$("#inInserimentoValore").val();

    
    var checkSpalma = "#spalmaDescrizione";
    var idDescrizioneMotivo0 = "#descrizioneMotivoReimputazione0";
    var idSintesiMotivo0 = "#listaSintesiMotiviReimputazione0";
    var selectReimputazione = getSelectReimputazione();
    var textreaReimputazione = getTextareaReimputazione();
    //SIAC-7349 Inizio SR190 CM 12/05/2020 aggiunto per gestire l'editabilità del campo descrizione textarea che dipende dal campo descrizione select
    if(inserimento=="true"){
        getReimputazioneDescrizioneEditabile();
    }
    //SIAC-7349 Inizio SR190 CM 12/05/2020
    
    var idSintesiInes =  "#listaSintesiMotiviCancellazioneInesegibilita";
    var idSintesiIns =  "#listaSintesiMotiviCancellazioneInsussistenza";
    var idSintesiMan =  "#listaSintesiMotiviDaMantenere";
    
    var idDescInes = "#descrizioneMotivoCancellazioneInesegibilita";
    var idDescIns =  "#descrizioneMotivoCancellazioneInsussistenza";
    var idDescMan =  "#descrizioneMotivoRorDaMantenere";

    // if($("#daMantenereValue").prop("checked") != true){
    //     $("#descrizioneMotivoRorDaMantenere").val("");
    //     $("#listaSintesiMotiviDaMantenere").val("");
    // }
    
    disabilitaPrimeAnnualita();
    if(inserimento){
        checkFirstLoadSintesi(checkSpalma, selectReimputazione, textreaReimputazione, idSintesiMotivo0, idDescrizioneMotivo0);
        checkSintesiNoReimp(idSintesiInes, idSintesiIns, idSintesiMan, idDescInes, idDescIns, idDescMan);
    }

    $('select').on('change', function(event){

            var idTag = event.target.id;
            var idTagLength = idTag.length;
            var index = idTag.charAt(idTagLength-1); //indice
            var idName = idTag.split(index)[0]; //nome id senza indice
            var optSel = ($("option:selected", this).val());

            var descrizione = getDescrizioneFromSintesi("descrizioniReimp", optSel)
            if(idName == "listaSintesiMotiviReimputazione"){
                var idDescrizioneMotivo = "#descrizioneMotivoReimputazione"+index;
                               
               if(descrizione){
                   $(idDescrizioneMotivo).val(descrizione)                 
                    $(idDescrizioneMotivo).attr("readonly", true);
                   
               }else{
                   $(idDescrizioneMotivo).val("");
                   $(idDescrizioneMotivo).attr("readonly", false);

               }
               
                if($(checkSpalma).prop("checked")==true){
                     spalmaDescrizioni(true);
                }else{
                    spalmaDescrizioni(false);
                }
      
        }
        // if(inserimento != "true"){
        //     for (var j = 0; j < textreaReimputazione.length; j++) {
        //         var element = textreaReimputazione[j];
        //         var jqEl = "#"+element;
        //         $(jqEl).attr("readOnly", false);                  
        //     }
        //     var idDescrizioneMotivo = "#descrizioneMotivoReimputazione"+index;
                               
        //        if(descrizione){
        //            $(idDescrizioneMotivo).val(descrizione)                 
        //             $(idDescrizioneMotivo).attr("readonly", true);
                   
        //        }else{
        //            $(idDescrizioneMotivo).val("");
        //            $(idDescrizioneMotivo).attr("readonly", false);

        //        }


        // }
        


        
    });

    $(idSintesiInes).on('change', function(event){
        var optSel = ($("option:selected", this).val());
        var descrizione = getDescrizioneFromSintesi("descrizioniCancellazione", optSel)
        var idDescrizioneMotivo = "#descrizioneMotivoCancellazioneInesegibilita";
        if(descrizione){
            $(idDescrizioneMotivo).val(descrizione)
        }else{
            $(idDescrizioneMotivo).val("");           
        }
        if(optSel=='' || !optSel || optSel==null){

            $(idDescrizioneMotivo).prop("readonly", false);

        }else{

            $(idDescrizioneMotivo).prop("readonly", true);

        }

        
    });

    $(idSintesiIns).on('change', function(event){
        var optSel = ($("option:selected", this).val());
        var descrizione = getDescrizioneFromSintesi("descrizioniCancellazione", optSel)       
        var idDescrizioneMotivo = "#descrizioneMotivoCancellazioneInsussistenza";
        if(descrizione){
            $(idDescrizioneMotivo).val(descrizione)
        }else{
            $(idDescrizioneMotivo).val("");

        }
        if(optSel=='' || !optSel || optSel==null){

            $(idDescrizioneMotivo).prop("readonly", false);

            //forse va settata la descrizione in un hidden nella jsp
        }else{

            $(idDescrizioneMotivo).prop("readonly", true);

        }


        
    });

    $(idSintesiMan).on('change', function(event){
        var optSel = ($("option:selected", this).val());
        var descrizione = getDescrizioneFromSintesi("descrizioniMantenere", optSel)       
        var idDescrizioneMotivo = "#descrizioneMotivoRorDaMantenere";
        if(descrizione){
            $(idDescrizioneMotivo).val(descrizione)
        }else{
            $(idDescrizioneMotivo).val("");

        }
        if(optSel=='' || !optSel || optSel==null){

            $(idDescrizioneMotivo).prop("readonly", false);
        }else{

            $(idDescrizioneMotivo).prop("readonly", true);

        }
        
    });

    function getDescrizioneFromSintesi(type, optSel){
        var desc = DescrizioniMotiviRor.getDescByCodeAndType(type, optSel);
        return desc;
    }

    


    //spalma descrizione su tutti gli anni
    $(checkSpalma).click(function(){
        
        if($(this).prop("checked") == true){
            spalmaDescrizioni(true);

        }else{
            spalmaDescrizioni(false);

        }
    });



    function spalmaDescrizioni(suTutte, optSel){
        for (var index = 1; index < selectReimputazione.length; index++) {
            var element = selectReimputazione[index];
            var idForJq = "#"+element;
            if(suTutte===true){
                $(idForJq).val($(idSintesiMotivo0).val());
                if($(idSintesiMotivo0).val() !=""){
                   $(idForJq).attr("readonly", true);
                }else{
                    $(idForJq).removeAttr("readonly"); 
                }


            }else{
                
                     $(idForJq).removeAttr("readonly"); 
                 
            }
        }
        for (var index = 1; index < textreaReimputazione.length; index++) {
            var element = textreaReimputazione[index];
            var idForJq = "#"+element;
            if(suTutte===true){
                $(idForJq).val($(idDescrizioneMotivo0).val())
                if($(idDescrizioneMotivo0).val() !=""){
                    $(idForJq).attr("readonly", true);
                 }else{
                     $(idForJq).removeAttr("readonly"); 
                 }
            }else{
                var element = selectReimputazione[index];
                var idForSel = "#"+element;
                if($(idForSel).val() != ""){
                    $(idForJq).attr("readonly", true); 
                }else{
                    $(idForJq).removeAttr("readonly"); 
                }
                
                
            }
        }
        
        
    }

    function getSelectReimputazione(){
        var selects = $('select');
        var selectsToReturn = [];
        for (var index = 0; index < selects.length; index++) {
            
            var isReimp = selects[index].id.startsWith("listaSintesiMotiviReimputazione")
            if(isReimp === true){
                selectsToReturn.push(selects[index].id);
            }
        }
        return selectsToReturn
    }

    function getTextareaReimputazione(){
        var textareas = $('textarea');
        var textareasToReturn = [];
        for (var index = 0; index < textareas.length; index++) {
            var isReimp = textareas[index].id.startsWith("descrizioneMotivoReimputazione")
            if(isReimp === true){
                textareasToReturn.push(textareas[index].id);
            }
   
        }
        return textareasToReturn
    }
    
  //SIAC-7349 Inizio SR190 CM 12/05/2020 aggiunto per gestire l'editabilità del campo descrizione textarea che dipende dal campo descrizione select
    function getReimputazioneDescrizioneEditabile(){

    	var selectsToReturn = getSelectReimputazione();
    	var textareasToReturn = getTextareaReimputazione();
    	
    	for(var i=0; i<selectsToReturn.length; i++){
      	var selectReimp = $("#"+selectsToReturn[i]).children(":selected").val();
      	var idDescrizioneMotivo = "#"+textareasToReturn[i];
    		if(selectReimp != null && selectReimp != ""){
    			$(idDescrizioneMotivo).attr("readonly", true);
    		}else{
    			$(idDescrizioneMotivo).attr("readonly", false);
    		}
    	}
    	
    }
    //SIAC-7349 Fine SR190 CM 12/05/2020
    
    $("#daMantenereValue").click(function(){
        
        if($("#daMantenereValue").prop("checked") != true){
            $("#descrizioneMotivoRorDaMantenere").val("");
            $("#listaSintesiMotiviDaMantenere").val("");
        }
    });

    


    
    
    

    
 
});


