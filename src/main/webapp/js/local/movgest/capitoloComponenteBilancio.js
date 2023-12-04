/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(document).ready(function() {

	//SE SIAMO DA RICERCA CARICHIAMO IN AUTOMATICO TUTTE LE COMPONENTI 
	if($("#capitolo").val()=='' || $("#articolo").val()==''){
		//cercaComponentiCapitoloTotali();
		}else{
			if(($("#capitolo").val()!='' &&  $("#articolo").val()!='')){
				if($('#listaComponenteBilancio').val() == ''){
					cercaComponentiCapitolo();
				}
				
			}
		}
	

	$("#capitolo").blur(function(){
		  if($("#articolo").val()!='' && $("#capitolo").val()!=''){
			  cercaComponentiCapitolo();
			  }
		});

	$("#articolo").blur(function(){
		  if($("#articolo").val()!='' && $("#capitolo").val()!=''){
			  cercaComponentiCapitolo();
			  }
	});
	
	
//	cercaComponentiCapitolo();
	
	$("#listaComponenteBilancio").change(function(){
		$("#componenteBilancioUidReturn").val($("#listaComponenteBilancio").val());
	});
	
	
	


	function cercaComponentiCapitolo(){
		//Disabilitiamo il tasto prosegui
		$('#prosegui').attr('disabled', true);
	
		var oggettoDaInviare = {};
		oggettoDaInviare.anno =$("#anno").val();
		oggettoDaInviare.numCapitolo =$("#capitolo").val();
		oggettoDaInviare.articolo =$("#articolo").val();
		oggettoDaInviare.azione = 'INSERISCI';
		var overlay = $('#listaComponenteBilancio').overlay({usePosition: true});
		overlay.overlay('show');
		$('#listaComponenteBilancio').prop('disabled',false);
		$.ajax({
			url: 'ajax/getComponentiBilancioDaCapitoloAjax.do',   
			type: 'GET',
			data: oggettoDaInviare,
			success: function(data) {
				//var listaComponentiCapitoloAjaxObj = {};
				$("#uidCapitoloFromAjax").val(data.uidCapitolo);
				//$('#listaComponenteBilancio').attr('placeholder', 'seleziona componente');
				
				//$('#listaComponenteBilancio').html('<option></option>');
				//DISABILITARE SE UN SOLO ELEMENTO
				$('#listaComponenteBilancio').html('');
				if(data.listaComponentiBilancio.length != 1){
					$('#listaComponenteBilancio').html('<option></option>');
				}
				
				$.each(data.listaComponentiBilancio, function (i, item) {
				    $('#listaComponenteBilancio').append($('<option>', { 
				        value: item.uidComponente,
				        text : item.tipoComponenteImportiCapitolo.descrizione 
				    }));
				   
				    //listaComponentiCapitoloAjaxObj[item.uidComponente] = item.tipoComponenteImportiCapitolo.descrizione;
					});
				
				    //$('#listaComponentiCapitoloAjax').val(JSON.stringify(listaComponentiCapitoloAjaxObj));
					$('#listaComponenteBilancio').val($("#componenteBilancioUidReturn").val());
					//DISABILITIAMO LA SELECT SE ELEMENTO E' UNICO
					if(data.listaComponentiBilancio.length == 1){
						$('#listaComponenteBilancio').attr('readonly',true);
						$('#listaComponenteBilancio').attr("style", "pointer-events: none;");
					}else{
						if($("#capitolo").prop('disabled')){
							$('#listaComponenteBilancio').attr('readonly',true);
							$('#listaComponenteBilancio').attr("style", "pointer-events: none;");
						}else{
							$('#listaComponenteBilancio').attr('readonly',false);
							$('#listaComponenteBilancio').attr("style", "pointer-events: display;");
						}
					}
					
					$('#prosegui').attr('disabled', false);
					
				}
			}).always(overlay.overlay.bind(overlay, 'hide'));
		}


//	function cercaComponentiCapitoloTotali(){
//		var oggettoDaInviare = {}; 
//		var overlay = $('#listaComponenteBilancio').overlay({usePosition: true});
//		overlay.overlay('show');
//		$('#listaComponenteBilancio').attr('readonly',false);
//		$('#listaComponenteBilancio').attr("style", "pointer-events: display;");
//		$.ajax({
//			url: 'ajax/getComponentiBilancioTotaliAjax.do',   
//			type: 'GET',
//			data: oggettoDaInviare,
//			success: function(data) {
//				//$('#listaComponenteBilancio').attr('placeholder', 'seleziona componente');
//				$('#listaComponenteBilancio').html('<option></option>');
//				$.each(data.listaComponentiBilancio, function (i, item) {
//				    $('#listaComponenteBilancio').append($('<option>', { 
//				        value: item.uidComponente,
//				        text : item.tipoComponenteImportiCapitolo.descrizione 
//				    }));
//					});
//				}
//			}).always(overlay.overlay.bind(overlay, 'hide'));
//		}
	
	
})