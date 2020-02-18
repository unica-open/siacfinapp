/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var ModificaMovGestEntrata = function($){
	'use strict';
	var exports = {};
	exports.gestisciChangeTipoMotivoAggiornamento = gestisciChangeTipoMotivoAggiornamento;
	exports.gestisciProvvedimento = gestisciProvvedimento;
	
	function gestisciChangeTipoMotivoAggiornamento(){
		var descrizioneObbligatoria = $('#modificaDescrizioneObbligatoria').hide();
		var optionSelected =  $('#listaTipoMotivo').children(":selected");
		var stringaTipoMotivo = optionSelected && optionSelected[0] && optionSelected[0].innerHTML;
		
        if(stringaTipoMotivo && /^ROR/.test(stringaTipoMotivo)){
        	descrizioneObbligatoria.show();
        }
        
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
  	  } else {
  		  $('#sub').show();
      	  $('#anche').hide();
      	  $('#infosImp').hide();
  	  }
    }
	
	
	return exports;
}(jQuery);

$(document).ready(function() {    
    	 
	var radioReimputatoNo = $("#radioReimputatoNo");
		var radioReimputatoSi = $("#radioReimputatoSi");
		var bloccoReimputato = $("#bloccoReimputato");
		var radioReimputato = $("#radioReimputato");
	
	
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
      
      
    
	  $("#linkCompilazioneGuidataProvvedimento").click(ModificaMovGestEntrata.gestisciProvvedimento);
	    
	 $('#listaTipoMotivo').off('change').on('change', ModificaMovGestEntrata.gestisciChangeTipoMotivoAggiornamento).trigger('change');
 
 
 });