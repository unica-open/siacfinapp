/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){ 
	
	 // lista soggetto 
	 // abilita/disabilita i campi a seconda del tipo
	
	var func_bloccaCampi = function(){
		var testoCombo = $("#listaTipoSoggetto").val();
		
		
		if(testoCombo=='PF' || testoCombo=='PFI'){
			
			$("#denominazione").attr("disabled", true);
			$("#denominazione").val("");
			
			// riaccendo i campi
			$("#cognome").attr("disabled", false);
			$("#nome").attr("disabled", false);
			$("#matricola").attr("disabled", false);
			$("#dataNascita").attr("disabled", false);
			$("#idNazione").attr("disabled", false);
			$("#comune").attr("disabled", false);
			// risetto il css in modo che non lo dia con lo sfondo grigio
			//$("#dataNascita").css("background-color","white");
			//$('#dataNascita').css('cursor', 'pointer');
			$("#Sesso").attr("disabled", false);
			$(".flagSesso").attr("disabled", false);
			
		}else if(testoCombo=='PG' || testoCombo=='PGI'){
			
			$("#cognome").attr("disabled", true);
			$("#cognome").val("");
			$("#nome").attr("disabled", true);
			$("#nome").val("");
			$("#dataNascita").attr("disabled", true);
			$("#dataNascita").val("");
			$("#matricola").attr("disabled", true);
			$("#matricola").val("");
			$("#idNazione").attr("disabled", true);
			$("#comune").attr("disabled", true);
			$("#comune").val("");
			// mette il colore del disabilitato
//			$("#dataNascita").css("background-color","rgb(241,241,241)");
			$("#Sesso").attr("disabled", true);
			$(".flagSesso").attr("disabled", true);
			
			// riaccendo i campi
			$("#denominazione").attr("disabled", false);
			
		}
	}
	
	 $("#listaTipoSoggetto").change(func_bloccaCampi);
	 
	// richiamo la funzione appena entro nella pagina
	 func_bloccaCampi();
	 
	
	 // blocca cod fiscale estero se check == no 
	 var func_flagResidenza = function(){
		 
		 $("#codiceFiscaleEstero").attr("disabled", $("#flagResidenzano").is(":checked"));
		 // se seleziono no pulisco la casella di testo
		 if($("#flagResidenzano").is(":checked")){
			$("#codiceFiscaleEstero").val("");
		 }
		
	 };         
	 
	 $(".flagResidenza").click(func_flagResidenza);
	 
	 
	 // richiamo la funzione appena entro nella pagina
	 func_flagResidenza();
	
	 
	$("#idNazione").change(function() {
		$("#comune").val($("option:selected", this).text()).trigger({type: 'keydown', keyCode: 1, which: 1, charCode: 1});
	});
	

	
	
	
	function initZTree(id) {
		return $.fn.zTree.init($(id), {
	        check : {
	            radioType : "all",
	            enable : true,
	            chkStyle : "radio"
	        },
			data: {
				simpleData: {
					enable: true
				}
			},
	    }, elencoStruttureAmministrativoContabiliJson);
	}
	
    function handleZTreeClick(zt, suff = '') {
        $('#confermaStrutturaAmministrativoContabile' + suff).click(function(e) {
            var checkedNodes = zt.getCheckedNodes(true);
            
            if (checkedNodes.length > 0) {
                $('#HIDDEN_StrutturaAmministrativoContabileUid' + suff).val(checkedNodes[0].uid);
                $('#SPAN_StrutturaAmministrativoContabile' + suff).text(checkedNodes[0].name);
            } else {
            	$('#SPAN_StrutturaAmministrativoContabile' + suff).text("Seleziona la Struttura amministrativa");
            }
        });

        $('#deselezionaStrutturaAmministrativoContabile' + suff).click(function(e) {
            var checkedNodes = zt.getCheckedNodes(true);
            if (checkedNodes.length > 0) {
                zt.checkNode(checkedNodes[0], false, true, false);
            }
            e.stopPropagation();
            return false;
        });
    }

    var zTreeObj = initZTree("#treeStruttAmm");
    handleZTreeClick(zTreeObj);	
    
    var sacUid = $('#HIDDEN_StrutturaAmministrativoContabileUid').val();
    
    if (sacUid.length > 0) {
    	var node = zTreeObj.getNodeByParam("uid", sacUid);
    	zTreeObj.refresh();
    	zTreeObj.checkNode(node, true, true, true);
        $('#SPAN_StrutturaAmministrativoContabile').text(node.name);
    }


	// DURC
	$('#tipoFonteDurc').data('orig-val', $('#tipoFonteDurc').val());
	
	var listaTipoSoggetto_change = function() {
		var sd = showDurc();
	
		$("#dati-durc").toggle(sd);
		$('#tipoFonteDurc').val(sd ? 
				$('#tipoFonteDurc').data('orig-val') === '' ? 'M' : $('#tipoFonteDurc').data('orig-val') 
				: '');
	};
	
	//SIAC-6565
    $("#listaCanalePA").change(function(){
    	var canale=$(this).val();
    	if (canale=='PA'){
    		$("#emailPec").val("");
    		$("#emailPec").prop("readonly",true);
    		$("#codDestinatario").prop("maxlength","6");
    	}else
    	{
    		$("#emailPec").prop("readonly",false);
    		$("#codDestinatario").prop("maxlength","7");
    	}
    }).trigger("change");

	$("#listaTipoSoggetto")
		.each(listaTipoSoggetto_change)
		.change(listaTipoSoggetto_change);	
	
	$("#dataFineValiditaDurc, #noteDurc").attr('readonly', $('#tipoFonteDurc').val() === 'A');
	$("#accordionStrutturaAmministrativaContabileAttoAmministrativo").toggle($('#tipoFonteDurc').val() !== 'A');
	$("#descrizioneFonteDurc").toggle($('#tipoFonteDurc').val() === 'A');
	
	var dataFineValiditaDurc_change = function() {
		$('#btnStrutturaAmministrativoContabile, #fonteDurcClassifIdDiv, #noteDurcDiv').toggle($('#dataFineValiditaDurc').val().length > 0);
	} 
	
	$("#dataFineValiditaDurc")
		.each(dataFineValiditaDurc_change)
		.change(dataFineValiditaDurc_change);

	$('form').submit(function() {
		if ($('#dataFineValiditaDurc').val().length === 0) {
			$('#HIDDEN_StrutturaAmministrativoContabileUid, #noteDurc').val("");
		}
		
		if (!showDurc()) {
			$('#tipoFonteDurc, #dataFineValiditaDurc').val("");
		}
	});

	function showDurc() {
		var val = $('#listaTipoSoggetto').val();
		return val !== 'PF' && val !== '';
	}
});
