/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(document).ready(function() {

	//SE SIAMO DA RICERCA CARICHIAMO IN AUTOMATICO TUTTE LE COMPONENTI 
	if($("#capitolo").val()=='' || $("#articolo").val()==''){
		cercaComponentiCapitoloTotali();
		}
	
//	else{
//			if(($("#capitolo").val()!='' &&  $("#articolo").val()!='')){
//				if($('#listaComponenteBilancio').val() == ''){
//					cercaComponentiCapitolo();
//				}
//				
//			}
//		}
	
	
	


//	$("#capitolo").blur(function(){
//		  if($("#articolo").val()!='' && $("#capitolo").val()!=''){
//			  cercaComponentiCapitolo();
//			  }else{
//				//SE SIAMO SOLO DA RICERCA
//				  cercaComponentiCapitoloTotali();
//				  }
//		});
//
//	$("#articolo").blur(function(){
//		  if($("#articolo").val()!='' && $("#capitolo").val()!=''){
//			  cercaComponentiCapitolo();
//			  }else{
//				//SE SIAMO SOLO DA RICERCA
//				  cercaComponentiCapitoloTotali();
//
//				  }
//	});
	
	
	$("#listaComponenteBilancio").change(function(){
		
		$("#componenteBilancioUidReturn").val($("#listaComponenteBilancio").val());
	});
	
	
	


	function cercaComponentiCapitolo(){
		var oggettoDaInviare = {};
		oggettoDaInviare.anno =$("#anno").val();
		oggettoDaInviare.numCapitolo =$("#capitolo").val();
		oggettoDaInviare.articolo =$("#articolo").val(); 
		oggettoDaInviare.azione = 'RICERCA';
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
				
				$('#listaComponenteBilancio').html('<option></option>');
				
				
				$.each(data.listaComponentiBilancio, function (i, item) {
				    $('#listaComponenteBilancio').append($('<option>', { 
				        value: item.uidComponente,
				        text : item.tipoComponenteImportiCapitolo.descrizione 
				    }));
				   
				    //listaComponentiCapitoloAjaxObj[item.uidComponente] = item.tipoComponenteImportiCapitolo.descrizione;
					});
				
				    //$('#listaComponentiCapitoloAjax').val(JSON.stringify(listaComponentiCapitoloAjaxObj));
					$('#listaComponenteBilancio').val($("#componenteBilancioUidReturn").val());
					
					
				
				}
			}).always(overlay.overlay.bind(overlay, 'hide'));
		}


	function cercaComponentiCapitoloTotali(){
		var oggettoDaInviare = {}; 
		var overlay = $('#listaComponenteBilancio').overlay({usePosition: true});
		overlay.overlay('show');
		$('#listaComponenteBilancio').attr('readonly',false);
		$('#listaComponenteBilancio').attr("style", "pointer-events: display;");
		$.ajax({
			url: 'ajax/getComponentiBilancioTotaliAjax.do',   
			type: 'GET',
			data: oggettoDaInviare,
			success: function(data) {
				//$('#listaComponenteBilancio').attr('placeholder', 'seleziona componente');
				$('#listaComponenteBilancio').html('<option></option>');
				$.each(data.listaComponentiBilancio, function (i, item) {
				    $('#listaComponenteBilancio').append($('<option>', { 
				        value: item.uidComponente,
				        text : item.tipoComponenteImportiCapitolo.descrizione 
				    }));
					});
				}
			}).always(overlay.overlay.bind(overlay, 'hide'));
		}
	
	
})