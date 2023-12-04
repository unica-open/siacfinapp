/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){

	$(document).ready(function() {
		var riaccertatoNo = $("#riaccertatoNo");
		var riaccertatoSi = $("#riaccertatoSi");
		var riaccVisible = $('.riaccVisible');
		var annoImpRiacc = $("#annoImpRiacc");
		var numImpRiacc = $("#numImpRiacc");

		if (riaccertatoNo.is(':checked')) {
			riaccVisible.hide();
			annoImpRiacc.hide();
			numImpRiacc.hide();
		}
		if (riaccertatoSi.is(':checked')) {
			riaccVisible.show();
			annoImpRiacc.show();
			numImpRiacc.show();
		}
		
		$("#linkCompilazioneGuidataProgetto").click(function(){
	    	initRicercaGuidataProgetto($("#progetto").val());
		});
	
		$("#progetto").change(function(){
			var cod = $("#progetto").val();
			//Carico i dati in tabella "Modalità di pagamento"		
			$.ajax({
				url: '/siacfinapp/ricercaImpegno_codiceProgettoChanged.do',
				type: "GET",
				data: $(".hiddenGestoreToggle").serialize() + "&id=" + cod, 
			    success: function(data)  {
			    	$("#refreshDescrizioneProgetto").html(data);
				}
			});			
		});

		$("#linkCompilazioneGuidataCapitolo").click(function(){
			initRicercaGuidataCapitolo(
					$("#capitolo").val(), 
					$("#articolo").val(),
					$("#ueb").val()
			);
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

       
       //aggiunta RM xche non funziona la selezione del radio della sac (provvedimento)
       $("#cerca").click(function() {
    	   //funcion definita in genericCustom.js
    	   preselezionaStrutturaPaginaPrincipale();

		});	
		

				
		$("#linkCompilazioneGuidataSoggetto").click(function(){
			initRicercaGuidataSoggetto(
				$("#codCreditore").val(),
				$("#listaClasseSoggetto").html()
			);
		});
		
		$("#codCreditore").blur(function(){
			$("#listaClasseSoggetto").val(-1);
		});
		
		$("#listaClasseSoggetto").change(function(){
			$("#codCreditore").val("");
			$.ajax({
				url: '/siacfinapp/ricercaImpegno_listaClasseSoggettoChanged.do',
				success: function(data)  {
				    $("#refreshHeaderSoggetto").html(data);
				}
			});
		});
		
		$("#cercaCapitoloSubmit").click(function(){
			$("#capitolo").attr("disabled", true);
			$("#articolo").attr("disabled", true);
			$("#ueb").attr("disabled", true);
		});
		
	/*	radioPluriennaleNo.change(function(){
			numeroPluriennaliLabel.hide();
			numeroPluriennali.hide();
		});
		
		radioPluriennaleSi.change(function(){
			numeroPluriennaliLabel.show();
			numeroPluriennali.show();
		});
	*/	
		riaccertatoNo.change(function(){
			annoImpRiacc.hide();
			numImpRiacc.hide();
		});
		
		riaccertatoSi.change(function(){
			annoImpRiacc.show();
			numImpRiacc.show();
		});
		
		
//		function changeRiacc(){
//			var riaccertatoNo = $("#riaccertatoNo");
//			var riaccertatoSi = $("#riaccertatoSi");
//			var annoImpRiacc = $("#annoImpRiacc");
//			var numImpRiacc = $("#numImpRiacc");
//			var riaccVisible = $('.riaccVisible');
//			if (riaccertatoNo.is(':checked')) {
//				riaccVisible.hide();
//				annoImpRiacc.hide();
//				numImpRiacc.hide();
//			}
//			if (riaccertatoSi.is(':checked')) {
//				riaccVisible.show();
//				annoImpRiacc.show();
//				numImpRiacc.show();
//			}
//		}
//		
		
		function impostaValoreEscludiAnnullati(){
			cbObj = document.getElementById("escludiAnnullatiCheckBox");
		    var valore = cbObj.checked;
		    $("#hiddenPerEscludiAnnullati").val(valore);
		}
		
		//SIAC-8157 controllo direttamente con una funzione anonima il valore
		$('#escludiAnnullatiCheckBox').change(function(){
			$('#escludiAnnullatiCheckBox').is(':checked') ? 
				$('#hiddenPerEscludiAnnullati').val('true') : $('#hiddenPerEscludiAnnullati').val('false');
		});
		
		//inizio SIAC-6997
		if ($("#riaccertatoNo").is(':checked') || $("#reannoNo").is(':checked')) {
			$("#bloccoRiaccertato").hide();
			
		}
		if ($("#riaccertatoSi").is(':checked') || $("#reannoSi").is(':checked')) {	
			$("#bloccoRiaccertato").show();
			
		}
		
		
		function gestioneRiaccertatoFlagSi(){
			$("#bloccoRiaccertato").show();
			riaccVisible.show();
			annoImpRiacc.show();
			numImpRiacc.show();
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
	});
});