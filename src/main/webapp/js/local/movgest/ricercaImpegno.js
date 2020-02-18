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
				url: '/siacfinapp/ricercaImpegno!codiceProgettoChanged.do',
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
				url: '/siacfinapp/ricercaImpegno!listaClasseSoggettoChanged.do',
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
		
	});
	
});