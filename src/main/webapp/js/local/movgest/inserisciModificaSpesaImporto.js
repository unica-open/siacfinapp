/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var ModificaMovGestSpesa = function($){
	'use strict';
	var exports = {};
	exports.gestisciChangeTipoMotivo = gestisciChangeTipoMotivo;
	exports.annullaPluri = annullaPluri;
	exports.gestisciCompetenze = gestisciCompetenze;
	exports.gestisciAbbina = gestisciAbbina;
	exports.gestisciProvvedimento = gestisciProvvedimento;
	exports.controlloSDF = controlloSDF;
	exports.gestisciChangeFlagAggiudicazione = gestisciChangeFlagAggiudicazione;
	
	 function annullaPluri(){
		 $('#listaTipoMotivo').val("ECON");
		 $('.anniPluriennale').hide();
		 //SIAC-6990
		 //reimposto il valore di default
		 $('#anniPluriennali').val("1");
	 }
	
	function gestisciChangeTipoMotivo(){
		var selectTipoMotivo = $('#listaTipoMotivo');
		var descrizioneObbligatoria = $('#modificaDescrizioneObbligatoria').hide();
		var campoImporto = $('#importoImpegnoModImp').removeAttr('disabled');
		var optionSelected =  $('#listaTipoMotivo').children(":selected");
		var stringaTipoMotivo = optionSelected && optionSelected[0] && optionSelected[0].innerHTML;
		var tipoMotivo=$('#listaTipoMotivo').children(":selected").val();
		var $importoRORM = $('#HIDDEN_importoRORM').html('');
		var stringaHiddenInput;
		 
		impostaPulsanteSingoloPluriennale(tipoMotivo);

		if(tipoMotivo === 'RORM'){
			stringaHiddenInput = '<input id="HIDDEN_campo_importo" type="hidden" name="'+ campoImporto.attr('name') + '"value="0,00"/>';
        	campoImporto.val(0).trigger('blur').attr('disabled', 'disabled');
        	$importoRORM.html(stringaHiddenInput);
        }
        if(stringaTipoMotivo && /^ROR/.test(stringaTipoMotivo)){
        	descrizioneObbligatoria.show();
        }

		//SIAC-8819 		
		var flagTipoMotivo = tipoMotivo=='REIMP' || tipoMotivo=='REANNO';

		$("#radioReimputatoSi").prop('checked', flagTipoMotivo);
		$("#radioReimputatoNo").prop('checked', ! flagTipoMotivo);
		$("#bloccoReimputato").toggle(flagTipoMotivo);
        
	}
	
	 function gestisciCompetenze(){
		 var radioSec = $('.flagCompetenze:checked').val();
		  if(radioSec == 'SubImpegno'){
			  $('#numeroSubImpegnoList').show();
			  $('#subDiv').show();
			  $('#abbinas').show();
			  $('#subInfo').show();
	    	  $('#imp').hide();
	    	  $('#sub').show();
	    	  $('#anche').hide();
	    	  $('#infosImp').hide();
			  nascondiSelectMotivo($('#modificaMotivoAggiudicazione'));
			  abilitaSelectMotivo($('#modificaMotivoGenerico'));

		  } 
			if(radioSec == 'Impegno') {

				$('#numeroSubImpegnoList').hide();
			    $('#subDiv').hide();
			    $('#abbinas').hide();
			    $('#subInfo').hide();			    
			    $('#sub').hide();
			    $('#anche').hide();
			    
			    $('#infosImp').show();
			    $('#imp').show();
		  }
			
		   controlloSDF();
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
			  $('#sub').hide();
			  $('#infosImp').show();
		  } else {
			  $('#sub').show();
	    	  $('#anche').hide();
	    	  $('#infosImp').hide();
		  }
		  
		  controlloSDF();
    }
	
	function impostaPulsanteSingoloPluriennale(selectOpt){
		 if(selectOpt=='RIAC'){   
	         $('.buttonPluriennale').show();
	         $('.anniPluriennale').show();
	         $('.singleButton').hide();
	       }else{
			 $('.buttonPluriennale').hide();			 
	         $('.anniPluriennale').hide();
		     //SIAC-6990
		     //reimposto il valore di default
		     $('#anniPluriennali').val("1");
	         $('.singleButton').show();
	       }
	}
	
	function controlloSDF(e){
		 var valore = $("#importoImpegnoModImp").val();
			
		 var urlSDF = $('#HIDDEN_urlImporto').val();
			var competenza = $('.flagCompetenze:checked').val();
		    var abbcheckd = $('#abbina-1').is(':checked');
			
			var pluriennaleVar = $('.buttonPluriennale').is(":visible");
			var singleVar =  $('.singleButton').is(":visible");
			
			var selectOpt=$('#listaTipoMotivo').children(":selected").val();
			
			$.ajax({
				url: urlSDF,
				type: "GET",
				data: $(".hiddenGestoreToggle").serialize() + "&impMod=" + valore + "&cmptnz=" + competenza +"&abbchckd="+abbcheckd, 
			    success: function(data)  {
//			    	funzioneRefreshPulsantiSalva(pluriennaleVar,singleVar,selectOpt);
			    	$("#refreshPulsantiSalva").html(data);
			    	funzioneRefreshPulsantiSalva(pluriennaleVar,singleVar,selectOpt);
			    	
				}
			});		
	  }
	
	function funzioneRefreshPulsantiSalva(pluriennaleVar,singleVar,selectOpt){
		 if(pluriennaleVar==singleVar){
			 impostaPulsanteSingoloPluriennale(selectOpt);
		 } else {
			//MANTENGO I SETTING
			if(pluriennaleVar == true){
		       	   $('.buttonPluriennale').show();
		       	} else {
		       	   $('.buttonPluriennale').hide();
		       	}
		   		if(singleVar == true){
		   		   $('.singleButton').show();
		       	} else {
		       	   $('.singleButton').hide();
		       	}
		 }
	 }

	function nascondiSelectMotivo($enclosingDiv){
		var enclosedSelect = $enclosingDiv.find('select');
		$enclosingDiv.hide();
		enclosedSelect.attr('disabled', 'true');
		enclosedSelect.val("");
	}
	
	function abilitaSelectMotivo($enclosingDiv){
		var enclosedSelect = $enclosingDiv.find('select');
		$enclosingDiv.show();
		enclosedSelect.removeAttr('disabled');
	}

	function gestisciChangeFlagAggiudicazione(){
		var isAggiudicazione = $('#flagAggiudicazione').is(':checked');
		var campiAggiudicazione = $('#campiAggiudicazione');
		var divMotivoAgg=$('#modificaMotivoAggiudicazione');
		var divMotivo=$('#modificaMotivoGenerico');
		var reimputazioneRadios = $('input[name="model.movimentoSpesaModel.reimputazione"]');
		/*var hiddenInput;
		var spanHiddenInput=$("#hidden_motivo_agg");*/
		if(!isAggiudicazione){
			campiAggiudicazione.find('input[type="text"]').val('');
			campiAggiudicazione.find('select').val("");
			campiAggiudicazione.slideUp();
			reimputazioneRadios.removeAttr('disabled');
			nascondiSelectMotivo(divMotivoAgg);
			abilitaSelectMotivo(divMotivo);
//			spanHiddenInput.html("");
			return;
		}
		/*hiddenInput ='<input type="hidden" name="idTipoMotivo" value="' + codiceMotivoAggiudicazione+'"/>';
		spanHiddenInput.html(hiddenInput);*/
		nascondiSelectMotivo(divMotivo);
		abilitaSelectMotivo(divMotivoAgg);
		
		$('#radioReimputatoNo').attr('checked', true).trigger('change');
		reimputazioneRadios.attr('disabled', true);
		campiAggiudicazione.slideDown();
		
		//SIAC-8096
		$('#subDiv').slideUp();
		$('#competenzeImpegno').attr('checked', true).trigger('change');
		//
	}
	
	
	
	
	return exports;
}(jQuery);


$(document).ready(function() {
	 
	var radioReimputatoNo = $("#radioReimputatoNo");
	var radioReimputatoSi = $("#radioReimputatoSi");
	var bloccoReimputato = $("#bloccoReimputato");
	var radioReimputato = $("#radioReimputato");
		
	if (radioReimputatoNo.is(':checked')){
		bloccoReimputato.hide();
	}
	
	if (radioReimputatoSi.is(':checked')){
		bloccoReimputato.show();
	}
	
	$('.buttonPluriennale').hide();
	$('.anniPluriennale').hide();
	
    radioReimputatoNo.change(function(){
		$("#annoReimp").val("");
		bloccoReimputato.hide();
	});
//    
    radioReimputatoSi.change(function(){
		bloccoReimputato.show();
	});
    
    var competenza = $('.flagCompetenze:checked').val();
    var abbcheckd = $('#abbina-1').is(':checked');
	
	if(competenza == 'Impegno'){
		$('#numeroSubImpegnoList').hide();
	    $('#subDiv').hide();
	    $('#abbinas').hide();
	    $('#subInfo').hide();
	    $('#imp').show();
	    $('#sub').hide();
	    $('#anche').hide();
	    $('#infosImp').show();
		//ModificaMovGestSpesa.nascondiMotivoAggiudicazione();
    }
//  
	if(competenza == 'SubImpegno'){
		$('#numeroSubImpegnoList').show();
		$('#subDiv').show();
		$('#abbinas').show();
		$('#subInfo').show();
		/*ModificaMovGestSpesa.nascondiMotivoAggiudicazione();*/
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
  
	$('#listaTipoMotivo').change(ModificaMovGestSpesa.gestisciChangeTipoMotivo).trigger('change');
	$('#abbina-1').click(ModificaMovGestSpesa.gestisciAbbina);
    $("#linkCompilazioneGuidataProvvedimento").click(ModificaMovGestSpesa.gestisciProvvedimento);
    $("#importoImpegnoModImp").change(ModificaMovGestSpesa.controlloSDF);
    
    $('.flagCompetenze').change(ModificaMovGestSpesa.gestisciCompetenze);
    
	//SIAC-8096 permetto l'inserimento delle aggiudicazioni per gli impegni con sub
	//ma ne inbisco la gestione se si prova a modificare i sub
    $('#competenzeSubImpegno').on('click', function(){
    	$('#competenzeSubImpegno').attr('checked', true).trigger('change');
    	$('#flagAggiudicazione').removeAttr('checked').trigger('change');		
    });
    
    //SIAC-6865
	if($('#flagAggiudicazione').length > 0){
		$('#flagAggiudicazione').off('change').on('change', ModificaMovGestSpesa.gestisciChangeFlagAggiudicazione).trigger('change');
	}    
	
 
 });