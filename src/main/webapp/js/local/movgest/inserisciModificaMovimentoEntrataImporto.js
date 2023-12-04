/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var ModificaMovGestEntrata = function($){
	'use strict';
	var exports = {};
	exports.gestisciChangeTipoMotivo = gestisciChangeTipoMotivo;
	exports.annullaPluri = annullaPluri;
	exports.gestisciCompetenze = gestisciCompetenze;
	exports.gestisciAbbina = gestisciAbbina;
	exports.gestisciProvvedimento = gestisciProvvedimento;
	//SIAC-7349 Inizio SR180 CM 23/04/2020 aggiunto per gestione tabella di modifiche reimputazioni di spesa
	exports.gestisciListaModificheReimputazioniSpesa = gestisciListaModificheReimputazioniSpesa;
	//SIAC-7349 Fine SR180 CM 23/04/2020
	//SIAC-7349 Inizio SR180 CM 27/04/2020 aggiunto per disattivazione tabella di modifiche reimputazioni di spesa
	exports.disattivaListaModificheReimputazioniSpesa = disattivaListaModificheReimputazioniSpesa;
	//SIAC-7349 Fine SR180 CM 27/04/2020
	
	 function annullaPluri(){
    	 $('#listaTipoMotivo').val("ECON");
     } 
	
	function gestisciChangeTipoMotivo(){
		var selectTipoMotivo = $('#listaTipoMotivo');
		var descrizioneObbligatoria = $('#modificaDescrizioneObbligatoria').hide();
		var campoImporto = $('#importoImpegnoModImp').removeAttr('disabled');
		var optionSelected =  $('#listaTipoMotivo').children(":selected");
		var stringaTipoMotivo = optionSelected && optionSelected[0] && optionSelected[0].innerHTML;
		var tipoMotivo=$('#listaTipoMotivo').val();
		var $importoRORM = $('#HIDDEN_importoRORM').html('');
		var stringaHiddenInput;
		
		if(tipoMotivo=='RIAC'){     
	        $('.buttonPluriennale').show();
	        $('.anniPluriennale').show();
	        $('.singleButton').hide();
	        return;
		}
		
		//SIAC-8819 		
		var flagTipoMotivo = tipoMotivo=='REIMP' || tipoMotivo=='REANNO';

		$("#radioReimputatoSi").prop('checked', flagTipoMotivo);
		$("#radioReimputatoNo").prop('checked', ! flagTipoMotivo);
		$("#bloccoReimputato").toggle(flagTipoMotivo);
		// SIAC-8826
		if(flagTipoMotivo) {
			ModificaMovGestEntrata.gestisciListaModificheReimputazioniSpesa();
		}
		
		
        $('.buttonPluriennale').hide();
        $('.anniPluriennale').hide();
        $('.singleButton').show();
        
        if(tipoMotivo == 'RORM'){
        	stringaHiddenInput = '<input id="HIDDEN_campo_importo" type="hidden" name="'+ campoImporto.attr('name') + '"value="0,00"/>';
        	campoImporto.val(0).trigger('blur').attr('disabled', 'disabled');
        	$importoRORM.html(stringaHiddenInput);
        }
        if(stringaTipoMotivo && /^ROR/.test(stringaTipoMotivo)){
        	descrizioneObbligatoria.show();
        }
        
		//SIAC-7349 Inizio SR180 CM 27/04/2020 aggiunto controllo di visualizzazione della tabella di Associazioni e filtro sulle righe della tabella Associazioni alla valorizzazione del motivo
   		var competenzeSubAccertamento = $("#competenzeSubAccertamento");
 		var motivoVal = optionSelected.val();
		var abbcheckd = $('#abbina-1').is(':checked');
     	
 		
        if(motivoVal === null || motivoVal === ""){
			$('#accordion2').hide(); 
        }else{
			//SIAC-8826
    		if (competenzeSubAccertamento.is(':checked') && (!abbcheckd)) {
    			$('#accordion2').hide(); 
     		}else{     			
    	 		$("tr.motivo_all").each(function() {
    	 			$(this).hide();
    	 		});
    	 		
    	 		 
    	 		$("tr.motivo_"+motivoVal).each(function() {
    	 			$(this).show();
    	 		});
    	 		
    	 		
    	 		
     			ModificaMovGestEntrata.gestisciListaModificheReimputazioniSpesa();
     		}
        }
 		//SIAC-7349 Fine SR180 CM 27/04/2020
	}
	
	 function gestisciCompetenze(){
		  var radioSec = $('.flagCompetenze:checked').val();
	   	  if(radioSec == 'Accertamento'){
	   		  $('#numeroSubImpegnoList').hide();
	   		  $('#subDiv').hide();
	   		  $('#abbinas').hide();
	   		  $('#subInfo').hide();
	       	  $('#imp').show();
	       	  $('#infosImp').show();
	       	  $('#sub').hide();
	       	  $('#anche').hide();
	   	  }
	   	  if(radioSec == 'SubAccertamento'){
	   		  $('#numeroSubImpegnoList').show();
	   		  $('#subDiv').show();
	   		  $('#abbinas').show();
	   		  $('#subInfo').show();
	       	  $('#imp').hide();
	       	  $('#sub').show();
	       	  $('#anche').hide();
	       	  $('#infosImp').hide();
	   	  }
     }
	
	 
	function gestisciProvvedimento(){
	    	
		var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaOrdinativoIncasso");
		var strutturaAmministrativaParam = "";
		if (treeObj != null) {
			var selectedNode = treeObj.getCheckedNodes(true);
			selectedNode.forEach(function(currentNode) {
				strutturaAmministrativaParam = currentNode;
			});
		}
	
	
		initRicercaGuidataProvvedimentoConStruttura(
			$("#annoProvvedimento").val(), 
			$("#numeroProvvedimento").val(),
			$("#listaTipiProvvedimenti").val(),
			strutturaAmministrativaParam
		);
	}
	
	
	function gestisciAbbina(){
  	  var opt = $('#abbina-1').is(':checked');
  	  if(opt){
  		  $('#anche').show();    		
  		  $('#infosImp').show();
  		  $('#sub').hide();
		  // SIAC-8826
		  ModificaMovGestEntrata.gestisciListaModificheReimputazioniSpesa();
  	  } else {
  		  $('#sub').show();
      	  $('#anche').hide();
      	  $('#infosImp').hide();
		  // SIAC-8826
		  $('#accordion2').hide();
  	  }
    }
	
	
	function checkNullToEmptyString(e) {
	        
	    var obj = e.srcElement || e.target;
	    if(obj.value.match(/^\s*$/g)){
	        obj.value = obj.value.replace(/^\s*$/g, "0,00");
	    }
	}
	
	//SIAC-7349 Inizio SR180 CM 23/04/2020 aggiunto per gestione tabella di modifiche reimputazioni di spesa
	function gestisciListaModificheReimputazioniSpesa(){
		
    	var radioReimputatoNo = $("#radioReimputatoNo");
  		var radioReimputatoSi = $("#radioReimputatoSi");
		
   		$('[data-toggle="tooltip"]').tooltip(); 

   		if (radioReimputatoNo.is(':checked')) {
			$('#accordion2').hide(); 
 		}
 		if (radioReimputatoSi.is(':checked')) {
 			var optionSelectedTipoMotivoVal =  $('#listaTipoMotivo').children(":selected").val();
 			if(optionSelectedTipoMotivoVal != null && optionSelectedTipoMotivoVal != ""){ 				
 				$('#accordion2').show();
 				$("#ModalAssociazioni").attr("href","#collapseOne"); 
 				var valAnnoReimp1 = $("#annoReimp").val();
 				
 				$("td.anno_"+valAnnoReimp1).each(function() {
 		 			$(this).children('span').hide();
 		 			$(this).children('input').show();
 		 		});	
 			}		
 		}
		
 		radioReimputatoNo.change(function(){
			$('#accordion2').hide(); 
		});
	    
	    radioReimputatoSi.change(function(){ 
			var optionSelectedTipoMotivoVal =  $('#listaTipoMotivo').children(":selected").val();
	    	if(optionSelectedTipoMotivoVal != null && optionSelectedTipoMotivoVal != ""){
				$('#accordion2').show();
				$("#ModalAssociazioni").attr("href","#collapseOne"); 
	    	}
		});

	}
	//SIAC-7349 Fine SR180 CM 23/04/2020
	
	//SIAC-7349 Inizio SR180 CM 27/04/2020 aggiunto per nascondere tabella di modifiche reimputazioni di spesa
	function disattivaListaModificheReimputazioniSpesa(){
		
    	var radioReimputatoNo = $("#radioReimputatoNo");
  		var radioReimputatoSi = $("#radioReimputatoSi");
		
   		if (radioReimputatoNo.is(':checked')) {
   			$('#accordion2').hide(); 
 		}
 		if (radioReimputatoSi.is(':checked')) {
 			$('#accordion2').hide(); 
 		}
 		radioReimputatoNo.change(function(){
 			$('#accordion2').hide(); 
		});
	    radioReimputatoSi.change(function(){
	    	$('#accordion2').hide(); 
		});
	}
	//SIAC-7349 Fine SR180 CM 27/04/2020
	
	return exports;
}(jQuery);

$(document).ready(function() {    
    	 
    	var radioReimputatoNo = $("#radioReimputatoNo");
  		var radioReimputatoSi = $("#radioReimputatoSi");
   		var bloccoReimputato = $("#bloccoReimputato");
   		var radioReimputato = $("#radioReimputato");
   		//SIAC-7349 Inizio SR180 CM 22/04/2020 aggiunto per gestione tabella di reimputazioni di spesa rispetto a radio button accertamento/subaccertamento
   		var competenzeAccertamento = $("#competenzeAccertamento");
   		var competenzeSubAccertamento = $("#competenzeSubAccertamento");
   		//SIAC-7349 Fine SR180 CM 22/04/2020
   		

   		if (radioReimputatoNo.is(':checked')) {
   			bloccoReimputato.hide();

 		}
 		if (radioReimputatoSi.is(':checked')) {
 			bloccoReimputato.show();

 		}
 		
 		radioReimputatoNo.change(function(){
			$("#annoReimp").val("");
			bloccoReimputato.hide();

		});
	    
	    radioReimputatoSi.change(function(){
			bloccoReimputato.show();

		});
    	 
    	 
    	 $('#noPluri').click(function(){
    		 annullaPluri();
    	 });
    	 
      $('.buttonPluriennale').hide();
      $('.anniPluriennale').hide();  
      
	  //SIAC-8826 aggiunta gestione lista reimputazione anche se viene checkato il subaccertamento con modifica contestuale dell'accertamento	
	  var abbcheckd = $('#abbina-1').is(':checked');
     	
 		//SIAC-7349 Inizio SR180 CM 22/04/2020 aggiunto per gestione tabella di reimputazioni di spesa rispetto a radio button accertamento/subaccertamento
		//if (competenzeAccertamento.is(':checked')) {
		if (competenzeAccertamento.is(':checked') || (competenzeSubAccertamento.is(':checked') && abbcheckd))  {	
			ModificaMovGestEntrata.gestisciListaModificheReimputazioniSpesa();
 		} else {
		//if (competenzeSubAccertamento.is(':checked')) {
			//SIAC-7349 Inizio SR180 CM 27/04/2020 Richiamato metodo creato per non visualizzare l'accordion
			ModificaMovGestEntrata.disattivaListaModificheReimputazioniSpesa();
			//SIAC-7349 Fine SR180 CM 27/04/2020
 		}

 		competenzeAccertamento.change(ModificaMovGestEntrata.gestisciListaModificheReimputazioniSpesa);
 		if(abbcheckd){
			competenzeSubAccertamento.change(ModificaMovGestEntrata.gestisciListaModificheReimputazioniSpesa);
 		} else {
			//SIAC-7349 Inizio SR180 CM 27/04/2020 Richiamato metodo creato per non visualizzare l'accordion
 			competenzeSubAccertamento.change(ModificaMovGestEntrata.disattivaListaModificheReimputazioniSpesa);
 			//SIAC-7349 Fine SR180 CM 27/04/2020
 		}
		//SIAC-7349 Fine SR180 CM 22/04/2020
	//SIAC-8826 Fine
	  
	    
 	      
 	  var competenza = $('.flagCompetenze:checked').val();
       
      if(competenza == 'Accertamento'){
    	  $('#numeroSubImpegnoList').hide();
    	  $('#subDiv').hide();
    	  $('#abbinas').hide();
    	  $('#subInfo').hide();
    	  $('#imp').show();
    	  $('#sub').hide();
    	  $('#anche').hide();
    	  $('#infosImp').show();
      }
      if(competenza == 'SubAccertamento'){
    	  $('#numeroSubImpegnoList').show();
    	  $('#subDiv').show();
    	  $('#abbinas').show();
    	  $('#subInfo').show();
    	  if(abbcheckd){
        	  $('#infosImp').show();
        	  $('#anche').show();
        	  $('#sub').hide();
		  }else{
    	  	  $('#infosImp').hide();
        	  $('#anche').hide();
        	  $('#sub').show();
    	  }
    	  $('#imp').hide();
      }
      $('.flagCompetenze').change(ModificaMovGestEntrata.gestisciCompetenze);
      
      $('#abbina-1').click(ModificaMovGestEntrata.gestisciAbbina);

	  $("#linkCompilazioneGuidataProvvedimento").click(ModificaMovGestEntrata.gestisciProvvedimento);
	    
	  $('#listaTipoMotivo').off('change').on('change', ModificaMovGestEntrata.gestisciChangeTipoMotivo).trigger('change');
	  
	  //SIAC-7349 Inizio SR180 CM 01/04/2020 aggiunto controllo sui campi della tabella Associazioni alla valorizzazione dell'anno
	  $("#annoReimp").change(function(){
	 		
		  var valAnnoReimp = $("#annoReimp").val();
	 		
		  $("td.anno_all").each(function() {
			  $(this).children('span').show();
			  $(this).children('input').hide();
		  });

		  $("td.anno_"+valAnnoReimp).each(function() {
			  $(this).children('span').hide();
			  $(this).children('input').show();
		  });
	 		
		});
	 	//SIAC-7349 Fine SR180 CM 01/04/2020 
 });