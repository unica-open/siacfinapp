/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){
	
	var actionForm = $('form').attr('action');
	
	$(document).ready(function() {

		var radioPluriennaleNo = $("#radioPluriennaleNo");
		var radioPluriennaleSi = $("#radioPluriennaleSi");
 		var bloccoPluriennali = $("#bloccoPluriennali");
 		var bloccoRiaccertato = $("#bloccoRiaccertato");
		var riaccertatoNo = $("#riaccertatoNo");
		var riaccertatoSi = $("#riaccertatoSi");
		
		var reannoNo = $("#reannoNo");
		var reannoSi = $("#reannoSi");
		
		var bloccoNumero = $("#bloccoNumero");
		
		var bloccoPrenotatoLiquidabile = $("#bloccoPrenotatoLiquidabile");
		var prenotazioneNo = $("#prenotazioneNo");
		var prenotazioneSi = $("#prenotazioneSi");
		
		var annoDigitato = $("#annoImpegno").val();
		var annoDiEsercizio = $("#annoEsercizio").val();
		var bilPrecInPredispConsuntivo = $("#bilPrecInPredispConsuntivo").val();
		var bilAttualeInPredispConsuntivo = $("#bilAttualeInPredispConsuntivo").val();
		
		if(annoDigitato<annoDiEsercizio && bilPrecInPredispConsuntivo == 'true'){
			bloccoNumero.show();
		} else {
			bloccoNumero.hide();
		}
		
		if (radioPluriennaleNo.is(':checked')) {
			bloccoPluriennali.hide();
		}
		if (radioPluriennaleSi.is(':checked')) {
			bloccoPluriennali.show();
		}
		if (riaccertatoNo.is(':checked') || reannoNo.is(':checked')) {
			bloccoRiaccertato.hide();
		}
		if (riaccertatoSi.is(':checked') || reannoSi.is(':checked')) {	
			bloccoRiaccertato.show();
		}
		
	    if (prenotazioneNo.is(':checked')) {
			bloccoPrenotatoLiquidabile.hide();
		}
		if (prenotazioneSi.is(':checked')) {	
			bloccoPrenotatoLiquidabile.show();
		} 
		
		if( (annoDigitato<annoDiEsercizio && bilPrecInPredispConsuntivo == 'true') || (annoDigitato>=annoDiEsercizio && bilAttualeInPredispConsuntivo == 'true') ){
			bloccoPluriennali.hide();
			radioPluriennaleNo.prop('checked', true);
			radioPluriennaleSi.prop('checked', false);
		}
		
		$("#linkCompilazioneGuidataCapitolo").click(function(){
			initRicercaGuidataCapitolo(
					$("#capitolo").val(), 
					$("#articolo").val(),
					$("#ueb").val()
			);
		});
		

	$("#prosegui").click(function(){
		// vado a verificare di aver scelto o meno il provvedimento con 
		// compilazione guidata, funcion definita in genericCustom.js
		preselezionaStrutturaPaginaPrincipale();
	});

    $("#linkCompilazioneGuidataProvvedimento").click(function(){

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


	});
    
    
    $("#linkInserisciProvvedimento").click(function(){

		var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaInserimentoProvvedimento");
			var strutturaAmministrativaParam = "";
			var splitted = actionForm && actionForm.split('.do');
			var urlToSend;
			if(!splitted || splitted.length === 0){
				return;
			}
			if (treeObj != null) {
				var selectedNode = treeObj.getCheckedNodes(true);
				selectedNode.forEach(function(currentNode) {
					strutturaAmministrativaParam = currentNode;
				});
			}
			
			
			urlToSend = splitted[0] + "_clearInserimentoProvvedimento.do";
			
			$.ajax({
				url: urlToSend,
				success: function(data)  {
					//$("#gestioneEsitoInserimentoProvvedimento").html(data);
					$("#gestioneRisultatoRicercaProgCronop").html(
				    		data.trim().length  > 0 ?
				    		data :
				    		"Non esistono Progetti/cronoprogrammi per il provvedimento digitato"
				    );
				}
			});

	});
		
		$("#linkCompilazioneGuidataSoggetto").click(function(){
			initRicercaGuidataSoggetto(
				$("#codCreditore").val(),
				$("#listaClasseSoggetto").val()
			);
		});
		
		$("#codCreditore").blur(function(){
			$("#listaClasseSoggetto").val(-1);
		});
		
		$("#listaClasseSoggetto").change(function(){
			var splitted = actionForm && actionForm.split('.do');
			if(!splitted || splitted.legth === 0){
				return;
			}
			var urlToSend = splitted[0] + "_listaClasseSoggettoChanged.do"
			$("#codCreditore").val("");
			$.ajax({
				url: urlToSend,
				success: function(data)  {
				    $("#refreshHeaderSoggetto").html(data);
				}
			});
		});
	
	 $("#linkCompilazioneGuidataProgetto").click(function(){
			var treeObj = $.fn.zTree.getZTreeObj("treeStrutturaAmministrativoContabileProgetto");
			var strutturaAmministrativaParam = "";
			if (treeObj != null) {
				var selectedNode = treeObj.getCheckedNodes(true);
				selectedNode.forEach(function(currentNode) {
					strutturaAmministrativaParam = currentNode;
				});
			}
		 
		 
		 
	    	initRicercaGuidataProgetto($("#progetto").val());
		});
		
		
	
	
		$("#progetto").change(function(){
			var cod = $("#progetto").val();
			//Carico i dati in tabella "Modalità di pagamento"		
			$.ajax({
				url: '/siacfinapp/inserisceImpegno_codiceProgettoChanged.do',
				type: "GET",
				data: $(".hiddenGestoreToggle").serialize() + "&id=" + cod, 
			    success: function(data)  {
			    	$("#refreshDescrizioneProgetto").html(data);
				}
			});			
		});
		
		$("#annoImpegno").change(function(){
			var annoDigitato = $("#annoImpegno").val();
			var annoDiEsercizio = $("#annoEsercizio").val();
			var bilPrecInPredispConsuntivo = $("#bilPrecInPredispConsuntivo").val();
			
// 			alert(bilPrecInPredispConsuntivo);
			
// 			alert(annoDigitato);
// 			alert(annoDiEsercizio);
			
			if(annoDigitato<annoDiEsercizio && bilPrecInPredispConsuntivo == 'true'){
// 				alert('annoDigitato minore');
				bloccoNumero.show();
			} else {
// 				alert('annoDigitato maggiore o uguale');
				bloccoNumero.hide();
			}
			
			
			if( (annoDigitato<annoDiEsercizio && bilPrecInPredispConsuntivo == 'true') || (annoDigitato>=annoDiEsercizio && bilAttualeInPredispConsuntivo == 'true') ){
				bloccoPluriennali.hide();
				radioPluriennaleNo.prop('checked', true);
				radioPluriennaleSi.prop('checked', false);
				$("#numeroPluriennali").val("");
			}
			
		});
		
		
		$("#cercaCapitoloSubmit").click(function(){
			$("#capitolo").attr("disabled", true);
			$("#articolo").attr("disabled", true);
			$("#ueb").attr("disabled", true);
			
		});
		
		radioPluriennaleNo.change(function(){
			$("#numeroPluriennali").val("");
			bloccoPluriennali.hide();
			
		});
		
		radioPluriennaleSi.change(function(){
			
			var annoDigitato = $("#annoImpegno").val();
			var annoDiEsercizio = $("#annoEsercizio").val();
			var bilPrecInPredispConsuntivo = $("#bilPrecInPredispConsuntivo").val();
			
			if( (annoDigitato<annoDiEsercizio && bilPrecInPredispConsuntivo == 'true') || (annoDigitato>=annoDiEsercizio && bilAttualeInPredispConsuntivo == 'true') ){
				radioPluriennaleNo.prop('checked', true);
				radioPluriennaleSi.prop('checked', false);
			} else {
				riaccertatoNo.prop('checked', true);
				reannoNo.prop('checked', true);
				$("#annoImpRiacc").val("");
				$("#numImpRiacc").val("");
				bloccoPluriennali.show();
				bloccoRiaccertato.hide();
			}
			
		});
		
		//SIAC-6997
		function gestioneRiaccertatoFlagSi(){
			radioPluriennaleNo.prop('checked', true);
			$("#numeroPluriennali").val("");
			bloccoRiaccertato.show();
			bloccoPluriennali.hide();
		}
		
		function gestioneRiaccertatoFlagNo(){
			$("#annoImpRiacc").val("");
			$("#numImpRiacc").val("");
			bloccoRiaccertato.hide();
		}
		
		reannoNo.change(function(){
			if(riaccertatoNo.is(':checked')){
				gestioneRiaccertatoFlagNo();
			}
		});
		
		riaccertatoNo.change(function(){
			if(reannoNo.is(':checked')){
				gestioneRiaccertatoFlagNo();
			}
		});
		
		riaccertatoSi.change(function(){
			gestioneRiaccertatoFlagSi();
			if(reannoSi.is(':checked')){
				reannoNo.prop('checked', true);
				reannoSi.prop('checked', false);
			}
		});
		
		reannoSi.change(function(){
			gestioneRiaccertatoFlagSi();
			if(riaccertatoSi.is(':checked')){
				riaccertatoNo.prop('checked', true);
				riaccertatoSi.prop('checked', false);
			}
		});
		//FINE SIAC-6997
		
		prenotazioneNo.change(function(){
			bloccoPrenotatoLiquidabile.hide();
		});
		
		prenotazioneSi.change(function(){
			bloccoPrenotatoLiquidabile.show();
		});
		
		
		$("#annoImpRiacc").blur(function() {
			$("#impegnoRiaccGiaRicercata").val("false")
		});
		
		$("#numImpRiacc").blur(function() {
			$("#impegnoRiaccGiaRicercata").val("false")
		});
	});
	

	function impostaValorePrenotatoLiquidabile(){
		cbObj = document.getElementById("prenotatoLiquidabileCheckBox");
        var valore = cbObj.checked;
        $("#hiddenPerPrenotazioneLiquidabile").val(valore);
	}
	//SIAC-7653
	$('#prenotatoLiquidabileCheckBox').off('click').on('click', impostaValorePrenotatoLiquidabile)
	
	$("#guidaProgCronop").on('shown', function(){
		$.ajax({
			//url: '<s:url method="ricercaCronop"/>',
			
			url: '/siacfinapp/inserisceImpegno_ricercaCronop.do',
			type: 'POST',
		    success: function(data)  {
			    $("#gestioneRisultatoRicercaProgCronop").html(
			    		data.trim().length  > 0 ?
			    		data :
			    		"Non esistono Progetti/cronoprogrammi per il provvedimento digitato"
			    );
			    
			    
				$('.lista-cronop div.accordion-toggle').on('click', function() {
					$(this).toggleClass('collapsed');
					$($(this).data('href')).css('height', 'auto').toggle();
				});
			}
		});
	});
	
	var hasCronop = $('#cronoprogramma').val() === undefined ? false : $('#cronoprogramma').val().length > 0;
	$('#cup').attr('readonly', hasCronop);

// SIAC-7817	
//	$('#descrImpegno').attr('readonly', hasCronop);
	$('#descrImpegno').attr('readonly', false);
	
	$('#conferma-modal-cronop').click(function() {
		var spesaSel = $('.speseProgetti:checked');
		
		if (spesaSel.length > 0) {
			$('#linkCompilazioneGuidataProgetto').attr('disabled', true).attr('href', '#');
			$('#cup').attr('readonly', true);
			//$('#descrImpegno').attr('readonly', true); // SIAC-7817
			if (spesaSel.data('capitolo').length > 0) $('#capitolo').attr('readonly', true);
			if (spesaSel.data('articolo').length > 0) $('#articolo').attr('readonly', true);
			
			$('#oggettoImpegno').val();
			$('#progetto').val(spesaSel.data('progetto'));
			$('#cronoprogramma').val(spesaSel.data('cronoprogramma'));
			$('#idCronoprogramma').val(spesaSel.data('id-cronoprogramma'));
			$('#idSpesaCronoprogramma').val(spesaSel.data('id-spesa-cronoprogramma'));
			$('#importoImpegno').val(spesaSel.data('importo'));
			$('#descrImpegno').val(spesaSel.data('attivita-prevista'));
			$('#cup').val(spesaSel.data('cup'));
			$('#capitolo').val(spesaSel.data('capitolo'));
			$('#articolo').val(spesaSel.data('articolo'));
			//SIAC-6936
			$('#annoImpegno').val(spesaSel.data('annocompetenza'));
			
		}
	});

	
	$('#radioPrevistaFatturaSi').click(function() {
		$('#radioPrevistoCorrispettivoNo').trigger('click');
	});
	
	$('#radioPrevistoCorrispettivoSi').click(function() {
		$('#radioPrevistaFatturaNo').trigger('click');
	});

	//SIAC-8075-CMTO
	$('#annoImpegno').on('blur', function(){
		if(new BigNumber($('#annoImpegno').val()).comparedTo(new BigNumber($('#anno').val())) < 0){
			$('#vincoliImpegno').slideUp();	
		} else {
			$('#vincoliImpegno').slideDown();
		}
	});
  
	
});