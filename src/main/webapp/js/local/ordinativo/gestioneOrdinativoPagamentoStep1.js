/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){ 

	if ($('#listacontoTesoreria option').length <= 2) {
		$('#listacontoTesoreria option[value=""]').remove();
	}

	$('#listacontoTesoreria option').each(function() {
		$(this).data('vincolato', /:true/.test($(this).val()));
		$(this).val($(this).val().split(':')[0]);
	});	

	$('#listacontoTesoreria')
		.change(function() {
			$('#controllaDisponibilitaSottocontoVincolato').toggle($(this).find('option:selected').data('vincolato')); 
			var currSel = $(this).find('option:selected');
			$('#controllaDisponibilitaSottocontoVincolato').toggle(currSel.data('vincolato'));
			$('#controllaDisponibilitaSottocontoVincolatoStr').toggle(currSel.hasClass('orig-sel'));
		})
		
	$('#capitolo').keypress(function(){
		$('#controllaDisponibilitaSottocontoVincolatoStr').html('');
	});
	
	$('#listacontoTesoreria').val($("#conto-tesoriere-selected").text());	
	$('#listacontoTesoreria option:selected').addClass('orig-sel');
	$('#listacontoTesoreria').trigger('change');
	
	$("#listaNoteTesoriere").change(function() {
		$("#Note").val($("#listaNoteTesoriere option:selected").text());
	});
	
	$("#flagDaTrasmettere").click(function() {
		if (! $(this).attr('readonly')) {
			if (this.checked) {
				$("#HIDDEN_flagDaTrasmettere").remove();
			} else {
				$(this).after('<input type="hidden" name="gestioneOrdinativoStep1Model.ordinativo.flagDaTrasmettere" id="HIDDEN_flagDaTrasmettere" value="false" />');
			}
		}
	});

	
	$("#linkCompilazioneGuidataCapitolo").click(function(){
		initRicercaGuidataCapitolo(
				$("#capitolo").val(), 
				$("#articolo").val(),
				$("#ueb").val()
			
		);
	});
	
	
	$("#cercaCapitoloSubmit").click(function(){
		$("#capitolo").attr("disabled", true);
		$("#articolo").attr("disabled", true);
		$("#ueb").attr("disabled", true);
	});
	
	
	$("#linkCompilazioneGuidataSoggetto").click(function(){
		initRicercaGuidataSoggetto(
			$("#codCreditoreLiquidazione").val(),
			null
		);
	});
	
	
	  
    $('#Note').keyup(function(){ 
    			    	
    	var text = $(this).val();
    	
        var size = text.length;  
    			  
        if(size > 500){  

            var newText = text.substr(0, 500);  
   
            $(this).val(newText);  
        }  		        
   
    });  
    
	$("#listaTipiCausale").change(function() {
		var selezionata = $("#listaTipiCausale").val();
		
		$.ajax({
			url: tipoCausaleEntrataChangedUrl,
			type: "GET",
			data: $(".hiddenGestoreToggle").serialize() + "&selezionata=" + selezionata
		}).then(function(data)  {
	    	$("#refreshTendinoCausali").html(data);
		});	
		
	});	
	
	
});