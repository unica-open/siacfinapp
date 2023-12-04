/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
$(function(){

	$(document).ready(function() {
		

	    $('li[treenode]').click(function() { 
			$('#lineaStruttura').text($(this).find('a').attr('title'))                   
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
				
		$("#linkCompilazioneGuidataSoggetto").click(function(){
			initRicercaGuidataSoggetto(
				$("#codCreditoreLiquidazione").val(),
				null
			);
		});
		
		$("#cercaCapitoloSubmit").click(function(){
			$("#capitolo").attr("disabled", true);
			$("#articolo").attr("disabled", true);
			$("#ueb").attr("disabled", true);
		});
		
		
		//RM aggiunta perchè non funziona la selezione del radio della sac (provvedimento)
	    $("#cerca").click(function() {
	 	   //funcion definita in genericCustom.js
	 	   //alert("passo di qui....");
	 	   preselezionaStrutturaPaginaPrincipale();

		});	
		
		
	  	//RM aggiunta perchè non funziona la selezione del radio della sac (provvedimento)
	    $("#cerca2").click(function() {
	 	   //funcion definita in genericCustom.js
	 	   //alert("passo di qui....");
	 	   preselezionaStrutturaPaginaPrincipale();

		});	
		
	});	

	function impostaValoreEscludiAnnullati(){
		cbObj = document.getElementById("escludiAnnullatiCheckBox");
	    var valore = cbObj.checked;
	    $("#hiddenPerEscludiAnnullati").val(valore);
	}

});