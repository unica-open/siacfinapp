/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){
	
	if ($("#riaccertatoNo").is(':checked') || $("#reannoNo").is(':checked')) {
		$("#bloccoRiaccertato").hide();
	}
	if ($("#riaccertatoSi").is(':checked') || $("#reannoSi").is(':checked')) {	
		$("#bloccoRiaccertato").show();
	}
	
	//inizio SIAC-6997
	function gestioneRiaccertatoFlagSi(){
		$("#bloccoRiaccertato").show();
	}
	
	function gestioneRiaccertatoFlagNo(){
		$("#annoImpRiacc").val("");
		$("#numImpRiacc").val("");
		$("#bloccoRiaccertato").hide();
	}
	
	$("#reannoNo").change(function(){
		if($("#riaccertatoNo").is(':checked')){
			gestioneRiaccertatoFlagNo();
		}
	});
	
	$("#riaccertatoNo").change(function(){
		if($("#reannoNo").is(':checked')){
			gestioneRiaccertatoFlagNo();
		}
	});
	
	$("#riaccertatoSi").change(function(){
		gestioneRiaccertatoFlagSi();
		if($("#reannoSi").is(':checked')){
			$("#reannoNo").prop('checked', true);
			$("#reannoSi").prop('checked', false);
		}
	});
	
	$("#reannoSi").change(function(){		  
		gestioneRiaccertatoFlagSi();
		if($("#riaccertatoSi").is(':checked')){
			$("#riaccertatoNo").prop('checked', true);
			$("#riaccertatoSi").prop('checked', false);
		}
	});
	
	//fine SIAC-6997
	
	$("#guidaProgCronop").on('shown', function(){
		$.ajax({
			//url: '<s:url method="ricercaCronop"/>',
			
			url: '/siacfinapp/aggiornaImpegnoStep1_ricercaCronop.do',
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
	
	
	$('#conferma-modal-cronop').click(function() {
		var spesaSel = $('.speseProgetti:checked');
		
		if (spesaSel.length > 0) {
			$('#linkCompilazioneGuidataProgetto').attr('disabled', true).attr('href', '#');
			$('#cup').attr('readonly', true);
			$('#descrImpegno').attr('readonly', true);
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
		}
	});

	
	
	$('#radioPrevistaFatturaSi').click(function() {
		$('#radioPrevistoCorrispettivoNo').trigger('click');
	});
	
	$('#radioPrevistoCorrispettivoSi').click(function() {
		$('#radioPrevistaFatturaNo').trigger('click');
	});
	

	$("#linkConsultaModificheStrutturaCompetente").click(function() {
		
		//$('div').remove('.nome_classe');
		
		var url = $('#consultaModificheStrutturaCompetente').val();
		
		$.ajax({
			url: url,
			type: 'POST',
			success: function(data)  {
				$("#modConsultaModificheStrutturaCompetente").html(data);
			}
		});
	});	
	
	
	
});