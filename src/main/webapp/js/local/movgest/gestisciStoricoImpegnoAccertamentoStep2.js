/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){
	
	var numero = $('#numeroAccertamento').val();
	var numeroSub = $('#numeroSubAccertamento').val();
	
	if(numero){
		 $('#numeroAccertamento').val(importoToFloat(numero));
	}
	
	if(numeroSub){
		$('#numeroSubAccertamento').val(importoToFloat(numeroSub));
	}
	
	if($('#idStorico').val() && $('#idStorico').val() != 0){
		$('#aggiornamentoStoricoSubmit').show();
		$('#inserimentoStoricoSubmit').hide();
	}else{
		$('#inserimentoStoricoSubmit').show();
	}
	
	$('.aggiornaStorico').off('click').on('click',  function(e){
		var $target = $(e.target);
		e && e.preventDefault && e.stopPropagation;
		$('input[name^="storicoImpegnoAccertamentoInModifica"]').val("");
		$('#annoAccertamento').val($target.data("anno"));
		$('#numeroAccertamento').val($target.data("numero"));
		$('#numeroSubAccertamento').val($target.data("numero-sub"));
		$('#idStorico').val($target.data("uid"));
		$('#aggiornamentoStoricoSubmit').show();
		$('#inserimentoStoricoSubmit').hide();
	});
	
	$(".linkElimina").click(function() {
		var supportId = $(this).attr("id").split("_");
		$('#btnEliminazione').hide();
		$('#btnEliminazioneSubmit').show();
		$('#idStorico').val(supportId[1]);
		
		if (supportId != null && supportId.length > 0) {
			
			if (supportId != null && supportId.length > 0) {
				$("#numeroQuotaDaPassare").val(supportId[1]);
			}
			
		}
	});
	
});