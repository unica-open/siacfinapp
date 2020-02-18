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
    	 
    	 
    	 $('#noPluri').click(function(){
    		 annullaPluri();
    	 });
    	 
      $('.buttonPluriennale').hide();
      $('.anniPluriennale').hide();      
      
      
      var competenza = $('.flagCompetenze:checked').val();
      var abbcheckd = $('#abbina-1').is(':checked');
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
 
 
 });