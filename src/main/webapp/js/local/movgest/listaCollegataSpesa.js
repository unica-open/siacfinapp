/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/

/**
 * FL CM
 * 30/04/2020
 * Gestione Cruscotto Accertamento Lista Reimputazione Spesa Collegata 
 */


    function initAssociazioneSpesa (anno, importo , descrizioneValoreSintesi, descrizione,valIndice) {

    	$("#annoAssocia").val(anno);
		$("#importoAssocia").val(importo);
		$("#indexReimputazioneAnnualita").val(valIndice);
		
		if (descrizioneValoreSintesi != null && descrizioneValoreSintesi.length > 0) {            
			$('#sintesiAssocia option[value="' + descrizioneValoreSintesi +'"]').attr("selected", "selected");
            $("#descrzioneAssocia").attr("readonly", true);
		}else{
	    	$('#sintesiAssocia option[value="' + descrizioneValoreSintesi +'"]').attr("selected", "false");
//				$('#sintesiAssocia option').attr('selected', false);
            $("#descrzioneAssocia").attr("readonly", false);
		}
		$("#descrzioneAssocia").val(descrizione);
		
		var motivoVal = 'REIMP';

		var annoDiEsercizio = $("#annoEsercizio").val();
		var inserimento=$("#inInserimentoValore").val();

		var annoDiEsercizioReimp = parseInt(annoDiEsercizio)+ parseInt(3);
		

		$("tr.motivo_all").each(function() {
 			$(this).hide();
 		});
 		$("tr.motivo_"+motivoVal).each(function() {
 			$(this).show();
 		});

 		$("td.anno_all").each(function() {
 			
 			$(this).children('span').show();
 			$(this).children('input').hide(); 
 		});
 		
 		
 		$("td.anno_"+anno).each(function() {
 			$(this).children('span').hide();
 			$(this).children('input').show();
 		});
	 		
		
	}
	
    
    function initAssociazioneSpesaListaCollegataImpegno (anno, importo , descrizioneValoreSintesi, descrizione,valIndice, uidImpegno) {
    	
		var inserimento=$("#inInserimentoValore").val();
		var impegno=$("#inImpegnoValore").val();
		
		if(inserimento=="false" && impegno=="true"){
			
	    	$("#annoAssocia").val(anno);
			$("#annoAssocia").attr("readonly", true);
			$("#importoAssocia").val(importo);
			$("#importoAssocia").attr("readonly", true);
			$("#indexReimputazioneAnnualita").val(valIndice);
			
			$("#uidImpegno").val(uidImpegno);
			
			if (descrizioneValoreSintesi != null && descrizioneValoreSintesi.length > 0) {            
				$('#sintesiAssocia option[value="' + descrizioneValoreSintesi +'"]').attr("selected", "selected");
				$("#sintesiAssocia").attr("readonly", true);
			}else{
		    	$('#sintesiAssocia option[value="' + descrizioneValoreSintesi +'"]').attr("selected", "false");
				$("#sintesiAssocia").attr("readonly", true);
			}
			$("#descrzioneAssocia").val(descrizione);
			$("#descrzioneAssocia").attr("readonly", true);

			$("table.impegnoReimp_all").each(function() {
				$(this).hide();
				$("#ncsdfImpegno").show();
			});
				
			$("table.impegnoReimp_"+uidImpegno).each(function() {
				$(this).show();
				$("#ncsdfImpegno").hide();
			});
		}
	}
    
    //non viene mai utilizzata
	function linkAssociazioneSpesa2(itemselezione){
		overlayForWait();
		initAssociazioneSpesa(
			$("#annoReimp"+itemselezione).val(), 
			$("#inputReimp"+itemselezione).val(),
			$("#listaSintesiMotiviReimputazione"+itemselezione).children(":selected").val(),
			$("#descrizioneMotivoReimputazione"+itemselezione).val() 
		);
	}

	 function getDescrizioneFromSintesi(type, optSel){
	        var desc = DescrizioniMotiviRor.getDescByCodeAndType(type, optSel);
	        return desc;
	    }
	
	$(document).ready(function() {   
		
		 $("#sintesiAssocia").on('change', function(event){

			 	var idTag = event.target.id;
	            var idTagLength = idTag.length;
	            var index = idTag.charAt(idTagLength-1); //indice
	            var idName = idTag.split(index)[0]; //nome id senza indice
	            var optSel = ($("option:selected", this).val());
	            
	            var descrizione = getDescrizioneFromSintesi("descrizioniReimp", optSel)
	     
	               var idDescrizioneMotivo = "#descrzioneAssocia";            
	               if(descrizione){
	                   $(idDescrizioneMotivo).val(descrizione)                 
	                    $(idDescrizioneMotivo).attr("readonly", true);
	                   
	               }else{
	                   $(idDescrizioneMotivo).val("");
	                   $(idDescrizioneMotivo).attr("readonly", false);

	               }
		  });


		
	
		 $("#annoAssocia").change(function(){
		 		
			  var valAnnoReimp = $("#annoAssocia").val();
		 		
			  $("td.anno_all").each(function() {
				  $(this).children('span').show();
				  $(this).children('input').hide();
			  });

			  $("td.anno_"+valAnnoReimp).each(function() {
				  $(this).children('span').hide();
				  $(this).children('input').show();
			  });
		 		
		});
		
	
		$('#accordion2').show();
		$("#ModalAssociazioni").attr("href","#collapseOne"); 
	
		$(".linkAssociazioneSpesa").click(function(){		

			var supportId = $(this).attr("id").split("_");
			if (supportId != null && supportId.length > 0) {
				$("#uidModDaAnnullare").val(supportId[1]);
				var anno = $("#annoReimp"+supportId[1]).val();
				var annoDiEsercizio = $("#annoEsercizio").val();
				var inserimento=$("#inInserimentoValore").val();
				var annoDiEsercizioReimp = parseInt(annoDiEsercizio)+ parseInt(3);
	
				
				if(anno != null && anno != ""){
					if(inserimento=="true"){
				 		if (supportId[1] > 2){
				 			$("#annoAssocia").attr("readonly", false);
					 	}else{
							$("#annoAssocia").attr("readonly", true);
					 	}
					}else{
						var nameUidHiddenValueModifica = "listaModificheRor["+supportId[1]+"].uid";
						var uidModificaValue = document.getElementsByName(nameUidHiddenValueModifica)[0].value;
						//aggiornamento
						if(uidModificaValue != null && uidModificaValue != "" && uidModificaValue != 0){
							$("#annoAssocia").attr("readonly", true);
						}else{
							$("#annoAssocia").attr("readonly", false);
						}
					}
				} else{
					$("#annoAssocia").attr("readonly", false);
				}
				
				initAssociazioneSpesa(
					$("#annoReimp"+supportId[1]).val(), 
					$("#inputReimp"+supportId[1]).val(), 
					$("#listaSintesiMotiviReimputazione"+supportId[1]).children(":selected").val(),
					$("#descrizioneMotivoReimputazione"+supportId[1]).val(),
					supportId[1]
				);
			}
			
		});		
		
		$(".linkAssociazioneSpesaListaCollegataImpegno").click(function(){

			var inserimento=$("#inInserimentoValore").val();
			var impegno=$("#inImpegnoValore").val();
			
			if(inserimento=="false" && impegno=="true"){

				var supportId = $(this).attr("id").split("_");
				if (supportId != null && supportId.length > 0) {
						
					initAssociazioneSpesaListaCollegataImpegno(
						$("#annoReimp"+supportId[1]).val(), 
						$("#inputReimp"+supportId[1]).val(), 
						$("#listaSintesiMotiviReimputazione"+supportId[1]).children(":selected").val(),
						$("#descrizioneMotivoReimputazione"+supportId[1]).val(),
						supportId[1],
						supportId[2]
					);
				}
			}
		});
		
		
	});

	