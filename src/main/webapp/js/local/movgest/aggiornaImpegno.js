/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){
	
	
	$("#guidaProgCronop").on('shown', function(){
		$.ajax({
			//url: '<s:url method="ricercaCronop"/>',
			
			url: '/siacfinapp/aggiornaImpegnoStep1!ricercaCronop.do',
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
	
});