/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
var setting = {
	check: {
		enable: true,
		chkStyle: "radio",
		radioType: "level"
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		// anche se seleziona la descrizione
		// deve ceccare il radio button
		onClick: zTreeClick
	}
};


var settingProvvisoriCassa = {
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "level"
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			// anche se seleziona la descrizione
			// deve ceccare il radio button
			onClick: zTreeClick,
			onCheck: onCheckProvvisoriCassa
		}
	};


var settingCompetente = {
		check: {
			enable: true,
			chkStyle: "radio",
			radioType: "all"
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			// anche se seleziona la descrizione
			// deve ceccare il radio button
			onClick: zTreeClickComp,
			onCheck: onCheckCompetente
		}
	};


function zTreeClickComp(event ,treeId, treeNode) {
	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	if (treeNode!=null) {
		$('#strutturaSelezionataCompetente').val(treeNode.uid);
		treeObj.checkNode(treeNode, true, true);

	}
    return (treeNode.id !== 1);
};

function zTreeClick(event ,treeId, treeNode) {
	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	if (treeNode!=null) {
		treeObj.checkNode(treeNode, true, true);
	}
    return (treeNode.id !== 1);
};

// eventuale utilizzo che al click riporta il
// nome della struttura amministrativa
//var settingStrutturaAmministrativaIncasso = {
//		check: {
//			enable: true,
//			chkStyle: "radio",
//			radioType: "all" // altrimenti si perde i check
//		},
//		data: {
//			simpleData: {
//				enable: true
//			}
//		},
//		callback: {
//			
//			onCheck: onCheckStrutturaAmministrativaIncasso
//		}
//	};
//
//function onCheckStrutturaAmministrativaIncasso(e, treeId, treeNode) {
//	console.log("treeNode", treeNode);
//	if(treeNode.checked){
//		
//		if(treeNode.pId!=null){
//			// se e' nodo figlio stampa padre + descrizione
//			$("#prova").text(treeNode.pId + " - "+treeNode.name);
//			
//			
//		}else{
//			// se e' nodo padre stampa solo il nome
//			$("#prova").text(treeNode.name);
//		}
//		
//	}else{
//		
//		$("#prova").text("Seleziona una struttura amministrativa");
//	}
//}


var settingWithCallbackElementoPdcTransazioneElementare = {
	check: {
		enable: true,
		chkStyle: "radio",
		radioType: "level"
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		// anche se seleziona la descrizione
		// deve ceccare il radio button
		onClick: zTreeBeforeClickAlbero,
		// click su radio
		onCheck: onCheckElementoPdcTransazioneElementare
	}
};

/**
 * permette di selezionare il radio anche selezionando solamente
 * la descrizione
 * @param event
 * @param treeId
 * @param treeNode
 * @returns {Boolean}
 */
function zTreeBeforeClickAlbero(event, treeId, treeNode) {
	
	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	if (treeNode!=null) {
		treeObj.checkNode(treeNode, true, true);
	}
	// chiamo la fuction che valorizza i radio
	if(treeId=='elementiPdcTransazioneElementare'){
		// PDC
		onCheckElementoPdcTransazioneElementare(event,treeId, treeNode);
	}else if(treeId=='contoEconomicoTransazioneElementare'){
		    // Conto Economico
			onCheckContoEconomicoTransazioneElementare(event,treeId, treeNode);
	}else if(treeId=='siopeTransazioneElementare'){
		    // SIOPE
			onCheckSiopeSpesaTransazioneElementare(event,treeId, treeNode);
	}
	// ritorno true/false
    return (treeNode.id !== 1);
};



var settingWithCallbackContoEconomicoTransazioneElementare = {
	check: {
		enable: true,
		chkStyle: "radio",
		radioType: "level"
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		// anche se seleziona la descrizione
		// deve ceccare il radio button
		onClick: zTreeBeforeClickAlbero,
		// click su radio button
		onCheck: onCheckContoEconomicoTransazioneElementare
	}
};

var settingWithCallbackSiopeSpesaTransazioneElementare = {
	check: {
		enable: true,
		chkStyle: "radio",
		radioType: "level"
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		// anche se seleziona la descrizione
		// deve ceccare il radio button
		onClick: zTreeBeforeClickAlbero,
		// click su radio button
		onCheck: onCheckSiopeSpesaTransazioneElementare
	}
};

var code, lastTreeNode, lastTreeNodeSuffix, lastTreeId, lastTreeChecked = false;
var supportTreeMap = {
		"elementiPdcTransazioneElementare": "PianoDeiContiCapitolo",
		"contoEconomicoTransazioneElementare": "ContoEconomicoTransazioneEconomica",
		"siopeTransazioneElementare": "SiopeSpesaTransazioneEconomica"
};

function setCheck() {
	var type = $("#level").attr("checked")? "level":"all";
	//alert('setCheck');
	//alert('setCheck - type: '+type);
	var supportComponents = {};
	type="all";
	setting.check.radioType = type;
	showCode('setting.check.radioType = "' + type + '";');
	settingWithCallbackElementoPdcTransazioneElementare.check.radioType = type;
	showCode('settingWithCallbackElementoPdcTransazioneElementare.check.radioType = "' + type + '";');
	var dataPianoDeiConti = "";
	var dataContoEconomicoSiopeSpesa = "";
	var dataContoEconomico = "";
	var trovatoParametro = false;
	var trovatoParametroDtCESSpesa = false;
	
	if (typeof $("#idMacroaggregatoCapitolo") !== 'undefined' && $("#idMacroaggregatoCapitolo").val() != null) {
		dataPianoDeiConti = "idMacroaggregato=" + $("#idMacroaggregatoCapitolo").val(); 
		trovatoParametro = true;
	}
	if (typeof $("#idPianoDeiContiCapitolo") !== 'undefined' && $("#idPianoDeiContiCapitolo").val() != null) {
		if (trovatoParametro) {
			dataPianoDeiConti += "&";
		}
		dataPianoDeiConti += "idPianoDeiConti=" + $("#idPianoDeiContiCapitolo").val(); 
		dataContoEconomicoSiopeSpesa = "idPianoDeiConti=" + $("#idPianoDeiContiCapitolo").val();
		trovatoParametro = true;
		trovatoParametroDtCESSpesa = true;
	}
	
	// classificatore pdc
	if (typeof $("#codiceClassificatorePdc") !== 'undefined' && $("#codiceClassificatorePdc").val() != null) {
		if (trovatoParametro) {
			dataPianoDeiConti += "&";
		}
		dataPianoDeiConti += "codiceClassificatorePdc" + $("#codiceClassificatorePdc").val();
	}	
	
	
	if (typeof $("#idContoEconomicoTransazioneEconomica") !== 'undefined' && $("#idContoEconomicoTransazioneEconomica").val() != null) {
		if (trovatoParametro) {
			dataPianoDeiConti += "&";
		}
		dataPianoDeiConti += "idContoEconomico=" + $("#idContoEconomicoTransazioneEconomica").val(); 
		trovatoParametro = true;
	}
	if (typeof $("#ricaricaAlberoPianoDeiConti") !== 'undefined' && $("#ricaricaAlberoPianoDeiConti").val() != null) {
		if (trovatoParametro) {
			dataPianoDeiConti += "&";
		}
		dataPianoDeiConti += "ricaricaAlberoPianoDeiConti=" + $("#ricaricaAlberoPianoDeiConti").val(); 
		trovatoParametro = true;
	}
	if (typeof $("#daRicerca") !== 'undefined' && $("#daRicerca").val() != null) {
		if (trovatoParametro) {
			dataPianoDeiConti += "&";
		}
		dataPianoDeiConti += "daRicerca=" + $("#daRicerca").val(); 
		trovatoParametro = true;
	}
	
	//alert("ricaricaAlberoContoEconomico: "+ $("#ricaricaAlberoContoEconomico").val());
	
	if (typeof $("#ricaricaAlberoContoEconomico") !== 'undefined' && $("#ricaricaAlberoContoEconomico").val() != null) {
		if (trovatoParametro) {
			dataPianoDeiConti += "&";
		}
		dataPianoDeiConti += "ricaricaAlberoContoEconomico=" + $("#ricaricaAlberoContoEconomico").val(); 
		trovatoParametro = true;
		
		//alert("dopo dataPianoDeiConti : "+dataPianoDeiConti);
	}
	if (typeof $("#ricaricaSiopeSpesa") !== 'undefined' && $("#ricaricaSiopeSpesa").val() != null) {
		if (trovatoParametro) {
			dataPianoDeiConti += "&";
		}
		dataPianoDeiConti += "ricaricaSiopeSpesa=" + $("#ricaricaSiopeSpesa").val(); 
		trovatoParametro = true;
	}
	if (typeof $("#ricaricaStrutturaAmministrativa") !== 'undefined' && $("#ricaricaStrutturaAmministrativa").val() != null) {
		if (trovatoParametro) {
			dataPianoDeiConti += "&";
		}
		dataPianoDeiConti += "ricaricaStrutturaAmministrativa=" + $("#ricaricaStrutturaAmministrativa").val(); 
		trovatoParametro = true;
	}
	if (typeof $("#struttAmmOriginale") !== 'undefined' && $("#struttAmmOriginale").val() != null) {
		
		if (trovatoParametro) {
			dataPianoDeiConti += "&";
		}
		dataPianoDeiConti += "struttAmmOriginale=" + $("#struttAmmOriginale").val();
		trovatoParametro = true;
		
		if (trovatoParametroDtCESSpesa) {
			dataContoEconomicoSiopeSpesa += "&";
		}
		dataContoEconomicoSiopeSpesa += "struttAmmOriginale=" + $("#struttAmmOriginale").val(); 
		trovatoParametroDtCESSpesa = true;
	} 
	if (typeof $("#struttAmmOriginaleCompetente") !== 'undefined' && $("#struttAmmOriginaleCompetente").val() != null) {
		
		if (trovatoParametro) {
			dataPianoDeiConti += "&";
		}
		dataPianoDeiConti += "struttAmmOriginaleCompetente=" + $("#struttAmmOriginaleCompetente").val();
		trovatoParametro = true;
		
		if (trovatoParametroDtCESSpesa) {
			dataContoEconomicoSiopeSpesa += "&";
		}
		dataContoEconomicoSiopeSpesa += "struttAmmOriginaleCompetente=" + $("#struttAmmOriginaleCompetente").val(); 
		trovatoParametroDtCESSpesa = true;
	} 
	if (typeof $("#idPianoDeiContiCapitoloPadrePerAggiorna") !== 'undefined' && $("#idPianoDeiContiCapitoloPadrePerAggiorna").val() != null) {
		if (trovatoParametro) {
			dataPianoDeiConti += "&";
		}
		dataPianoDeiConti += "idPianoDeiContiCapitoloPadrePerAggiorna=" + $("#idPianoDeiContiCapitoloPadrePerAggiorna").val(); 
		trovatoParametro = true;
	}
	if (typeof $("#hiddenDatiUscitaImpegno") !== 'undefined' && $("#hiddenDatiUscitaImpegno").val() != null) {
		if (trovatoParametro) {
			dataPianoDeiConti += "&";
		}
		dataPianoDeiConti += "datiUscitaImpegno=" + $("#hiddenDatiUscitaImpegno").val(); 
		trovatoParametro = true;
	}
	
	//Commentato per ricerca PDC su modalCapitolo troppo lenta
//	supportComponents = {"#elementiPdcInserisciImpegnoStep2": settingWithCallbackElementoPdcTransazioneElementare, "#elementoPdcRicercaImpegno": setting,
//			"#elementiPdcAggiornaSubimpegnoStep2": settingWithCallbackElementoPdcTransazioneElementare, "#elementiPdcModalCapitolo": setting};
	supportComponents = {"#elementiPdcTransazioneElementare": settingWithCallbackElementoPdcTransazioneElementare,
			"#elementiPdcAggiornaSubimpegnoStep2": settingWithCallbackElementoPdcTransazioneElementare};
	chiamataAjaxPerPopolaTree('ajax/pianoDeiContiAjax.do', dataPianoDeiConti, 'listaPianoDeiConti', supportComponents);
	supportComponents = {"#elementoPdcRicercaImpegno": settingWithCallbackElementoPdcTransazioneElementare, 
			"#elementoPdcRicercaAccertamento": settingWithCallbackElementoPdcTransazioneElementare};
	
	// commento la chiamata inutile se il piano dei conti completo non c'e' piu'
	//chiamataAjaxPerPopolaTree('ajax/pianoDeiContiCompletoAjax.do', dataPianoDeiConti, 'listaPianoDeiContiCompleto', supportComponents);
	
	supportComponents = {
			"#strutturaAmministrativaRicercaCapitolo": setting, 
			"#strutturaAmministrativaRicercaProvvedimento": setting,
			"#treeStrutturaAmministrativoContabileProgetto": setting,
			"#strutturaAmministrativaOrdinativoIncasso" :setting, 
			"#strutturaAmministrativaInserimentoProvvedimento" :setting, 
			"#strutturaAmministrativaAggiornamentoProvvisorio" : settingProvvisoriCassa, 
			"#strutturaAmministrativaRicercaProvvisorio" : settingProvvisoriCassa};

	chiamataAjaxPerPopolaTree('ajax/strutturaAmministrativeAjax.do', dataContoEconomicoSiopeSpesa, 'listaStrutturaAmministrative', supportComponents);
	
	supportComponents = {"#contoEconomicoTransazioneElementare": settingWithCallbackContoEconomicoTransazioneElementare};
	//alert('ajax/contoEconomicoAjax.do');
	//chiamataAjaxPerPopolaTree('ajax/contoEconomicoAjax.do', dataPianoDeiConti, 'listaContiEconomici', supportComponents);
	
	supportComponents = {"#siopeTransazioneElementare": settingWithCallbackSiopeSpesaTransazioneElementare};
	chiamataAjaxPerPopolaTree('ajax/siopeSpesaAjax.do', dataPianoDeiConti, 'listaSiopeSpesa', supportComponents);
	
	//SIAC-7477
	supportComponents = {
			"#strutturaAmministrativaCompetente" :settingCompetente};
	
	chiamataAjaxPerPopolaTree('ajax/strutturaAmministrativeCompetenteAjax.do', dataContoEconomicoSiopeSpesa, 'listaStrutturaAmministrativeCompetente', supportComponents);


}

function showCode(str) {
	if (!code) code = $("#code");
	code.empty();
	code.append("<li>"+str+"</li>");
}

$(document).ready(function(){
	setCheck();		
	//alert('post check : ' + setCheck);
	
	$("#level").bind("change", setCheck);
	$("#all").bind("change", setCheck);
	
	
});

function gestisciChangeTreeNode(boolTree, suffix, treeId, treeNode) {
	/*lastTreeNode = treeNode;
	lastTreeNodeSuffix = suffix;
	lastTreeId = treeId;*/
	if(boolTree){
		$("#id" + suffix).val(treeNode.uid);
		$("#codice" + suffix).val(treeNode.codice);
		$("#descrizione" + suffix).val(treeNode.name);
		if(suffix=='PianoDeiContiCapitolo'){
			$("#codiceClassificatorePdc").val(treeNode.tipoClassificatore.codice);
		}
		
	}else{
		$("#id" + suffix).val(0);
		$("#codice" + suffix).val(null);
		$("#descrizione" + suffix).val(null);
		if(suffix=='PianoDeiContiCapitolo'){
			$("#codiceClassificatorePdc").val(null);
		}
	}}

function onCheckProvvisoriCassa(e, treeId, treeNode) {
	$('#strutturaSelezionataSuPagina').val(treeNode.uid);
}

function onCheckCompetente(e, treeId, treeNode) {
	$('#strutturaSelezionataCompetente').val(treeNode.uid);
}

function onCheckElementoPdcTransazioneElementare(e, treeId, treeNode) {
	lastTreeChecked = true;
	gestisciChangeTreeNode(treeNode.checked, "PianoDeiContiCapitolo", "elementiPdcTransazioneElementare", treeNode);
	/*lastTreeNode = treeNode;
	lastTreeNodeSuffix = "PianoDeiContiCapitolo";
	if(treeNode.checked){
		$("#idPianoDeiContiCapitolo").val(treeNode.uid);
		$("#codicePianoDeiContiCapitolo").val(treeNode.codice);
		$("#descrizionePianoDeiContiCapitolo").val(treeNode.name);
	}else{ 
		$("#idPianoDeiContiCapitolo").val(0);
		$("#codicePianoDeiContiCapitolo").val(null);
		$("#descrizionePianoDeiContiCapitolo").val(null);
	}*/
}

function onCheckContoEconomicoTransazioneElementare(e, treeId, treeNode) {
	lastTreeChecked = true;
	gestisciChangeTreeNode(treeNode.checked, "ContoEconomicoTransazioneEconomica", "contoEconomicoTransazioneElementare", treeNode);
	/*lastTreeNode = treeNode;
	lastTreeNodeSuffix = "ContoEconomicoTransazioneEconomica";
	// se e' ceccato allora metto i valori in hidden 
	if(treeNode.checked){
		$("#idContoEconomicoTransazioneEconomica").val(treeNode.uid);
		$("#codiceContoEconomicoTransazioneEconomica").val(treeNode.codice);
		$("#descrizioneContoEconomicoTransazioneEconomica").val(treeNode.name);
	}else{
		$("#idContoEconomicoTransazioneEconomica").val(0);
		$("#codiceContoEconomicoTransazioneEconomica").val(null);
		$("#descrizioneContoEconomicoTransazioneEconomica").val(null);
	}*/

}


function onCheckSiopeSpesaTransazioneElementare(e, treeId, treeNode) {
	lastTreeChecked = true;
	gestisciChangeTreeNode(treeNode.checked, "SiopeSpesaTransazioneEconomica", "siopeTransazioneElementare", treeNode);
	/*lastTreeNode = treeNode;
	lastTreeNodeSuffix = "SiopeSpesaTransazioneEconomica";
	if(treeNode.checked){
		$("#idSiopeSpesaTransazioneEconomica").val(treeNode.uid);
		$("#codiceSiopeSpesaTransazioneEconomica").val(treeNode.codice);
		$("#descrizioneSiopeSpesaTransazioneEconomica").val(treeNode.name);
	}else{
		$("#idSiopeSpesaTransazioneEconomica").val(0);
		$("#codiceSiopeSpesaTransazioneEconomica").val(null);
		$("#descrizioneSiopeSpesaTransazioneEconomica").val(null);
	}*/
}

function resetLastTreeNode() {
	if (lastTreeChecked) {
		if (lastTreeNode) {
			gestisciChangeTreeNode(!lastTreeNode.checked, lastTreeNodeSuffix, lastTreeId, lastTreeNode);
			/*if(!lastTreeNode.checked){
				$("#id" + lastTreeNodeSuffix).val(lastTreeNode.uid);
				$("#codice" + lastTreeNodeSuffix).val(lastTreeNode.codice);
				$("#descrizione" + lastTreeNodeSuffix).val(lastTreeNode.name);
			}else{
				$("#id" + lastTreeNodeSuffix).val(0);
				$("#codice" + lastTreeNodeSuffix).val(null);
				$("#descrizione" + lastTreeNodeSuffix).val(null);
			}*/
			$.fn.zTree.getZTreeObj(lastTreeId).checkNode(lastTreeNode, !lastTreeNode.checked, true, false);
		} else if (lastTreeId) {
			var treeObj = $.fn.zTree.getZTreeObj(lastTreeId);
			var nodes = treeObj.getCheckedNodes(true);
			for (var i = 0; i < nodes.length; i++) {
				lastTreeNode = nodes[i];
				break;
			}
			if (lastTreeNode) {
				lastTreeNodeSuffix = supportTreeMap[lastTreeId];
				resetLastTreeNode();
			}
		}
		lastTreeChecked = false;
	}
	lastTreeNode = null;
	lastTreeNodeSuffix = null;
	lastTreeId = null;
}

//var mapTreeHidden = {
//		"contoEconomicoTransazioneElementare" : "codiceContoEconomicoTransazioneEconomica",
//		"" : "",
//		"" : ""
//}

/**
 * Metodo che fa la chiamata Ajax per la creazionde dell'albero di navigazione
 * 
 * @param url - metodo mappato su struts che si interfaccia con il servizio relativo per la creazione dell'albero
 * @param data - parametri (opzionali) che il metodo richiamato utilizza
 * @param listaDiRiferimento - nome del model che contiene il risultato del servizio
 * @param supportComponents - mappa composta da id (idComponenteAlbero) e setting (propertyAlbero)
 */
function chiamataAjaxPerPopolaTree(url, data, listaDiRiferimento, supportComponents) {
	/*alert("chiamataAjaxPerPopolaTree, url : "+url 
			+ " , data: " +data +", listaDiRiferimento:  " +listaDiRiferimento+  " , supportComponents:  " + supportComponents);
	*/
	$.ajax({
		url: url,
        dataType: 'json',
        data: data,
        contentType: "application/json",
        success: function(data, status, xhr) {
        	var supportJson = JSON.stringify(data[listaDiRiferimento]);
        	var supportString = JSON.parse(supportJson);
        	$.each(supportComponents, function(k, v) {
        		$(k+"Div").hide();
        		$(k+"Wait").show();
        		
        		var ztree = $.fn.zTree.init($(k), v, supportString);
       			$(document).trigger("ztree:init", [ ztree.setting.treeId ]);

//        		var treeObj = $.fn.zTree.init($(k), v, supportString);
//        		var nodes = treeObj.getSelectedNodes();
//        		alert("nodi "+nodes);
//        		var hiddenFieldCode = $("#" + mapTreeHidden[k]).val();
//        		for (var i=0, l=nodes.length; i < l; i++) {
//        			if (hiddenFieldCode == treeNode.codice)
//        				treeObj.checkNode(nodes[i], true, true);
//        		}
        		$(k+"Div").show();
        		$(k+"Wait").hide()
        		// alert(" ecco "+k);
        		// il k arriva con il cancelletto davanti
        		// TE - PDC
        		if(k=="#elementiPdcTransazioneElementare"){
        			
        			$("#spinnerElementoPianoDeiContiTE").addClass("hideContent");
        		}
        		
        		// TE - CONTO ECONOMICO
        		if(k=="#contoEconomicoTransazioneElementare"){

        			$("#spinnerElementoContoEconomicoTE").addClass("hideContent");
        			
        		}
        		
        		// TE - SIOPE
        		if(k=="#siopeTransazioneElementare"){
        			
        			$("#spinnerElementoSiopeTE").addClass("hideContent");
        		}
        		
        		
        		// Struttura amministrativa Ricerca Capitolo
        		if(k=="#strutturaAmministrativaRicercaCapitolo"){
        			
        			$("#spinnerStruttAmmRicercaCapitolo").addClass("hideContent");
        		}
        	   
        		// Struttura amministrativa Ricerca Provvedimento
        		if(k=="#strutturaAmministrativaRicercaProvvedimento"){
        			
        			$("#spinnerStruttAmmRicercaProvvedimento").addClass("hideContent");
        		}
        		
        		if(k=="#strutturaAmministrativaInserimentoProvvedimento"){
        			
        			$("#spinnerStruttAmmInserimentoProvvedimento").addClass("hideContent");
        		}
        		
        		if(k=="#strutturaAmministrativaAggiornamentoProvvisorio"){
        			
        			$("#spinnerStruttAmmAggiornamentoProvvisorio").addClass("hideContent");
        		}
        		
        		if(k=="#strutturaAmministrativaRicercaProvvisorio"){
        			
        			$("#spinnerStruttAmmRicercaProvvisorio").addClass("hideContent");
        		}
        		
        		if(k=="#strutturaAmministrativaOrdinativoIncasso"){
        			
        			$("#spinnerStruttAmmOrdinativoIncasso").addClass("hideContent");
        		}
        		
        		//SIAC-6997
        		if(k=="#strutturaAmministrativaCompetente"){
        			
        			$("#spinnerStrutturaAmministrativaCompetente").addClass("hideContent");
        		}
        		
        		
        		
//        		spinnerStruttAmmRicercaCapitolo strutturaAmministrativaRicercaCapitolo *
//        		spinnerElementoSiopeTE siopeTransazioneElementare *
//        		spinnerElementoContoEconomicoTE  contoEconomicoTransazioneElementare *
//        		spinnerElementoPianoDeiContiTE elementiPdcTransazioneElementare *
//				spinnerStruttAmmRicercaProvvedimento strutturaAmministrativaRicercaProvvedimento
        		
        		
//        		<i class="icon-spin icon-refresh spinner" id="spinnerStruttAmmRicercaCapitolo"></i>
        		//                 elementiPdcAggiornaSubimpegnoStep2
        		
        	});
        	
        	
        	
        	
        },
        error: function(xhr, status, error) {}
	});
}

$(document).ready(function() {
	$('.modal')
		.on('hidden', resetLastTreeNode)
		.on('show', function() {
			var zt = $(this).find(".ztree");
			var ztId = zt.attr("id");
			var treeObj = $.fn.zTree.getZTreeObj(ztId);
			if(!treeObj){
				return;
			}
			var nodes = treeObj.getCheckedNodes(true);
			lastTreeId = ztId;
			for (var i = 0; i < nodes.length; i++) {
				lastTreeNode = nodes[i];
				break;
			}
			lastTreeNodeSuffix = supportTreeMap[lastTreeId];
		});
	
	
    $(this).on('ztree:init', function(evt, ztreeId) {
 	   if (ztreeId === undefined) return;
 	   
 	   var $ztreeId = $('#' + ztreeId);
 	   var ztree = $.fn.zTree.getZTreeObj(ztreeId);
 	   
	   	var updateAccordion = function(text) {
			//SIAC-7583
			//non sostuisco il valore in caso l'accordion sia della transazione elementare
			var accordion = $ztreeId.closest('.accordion').find('.accordion-toggle');
			accordion.text().indexOf('Transazione elementare') === -1 ? accordion.text(text) : $.noop();
			//
	   	};

 	   var fnClick = ztree.setting.callback.onClick || function(){};
 	   var fnCheck = ztree.setting.callback.onCheck || function(){};
 	   
 	   ztree.setting.callback.onClick = function(srcEvent, treeId, node, clickFlag) {
 		   fnClick(srcEvent, treeId, node, clickFlag);
 		   updateAccordion(node.name);
 	   }
 	   
 	   ztree.setting.callback.onCheck = function(srcEvent, treeId, node, clickFlag) {
 		   fnCheck(srcEvent, treeId, node, clickFlag);
 		   updateAccordion(node.name);
 	   }
 	});
	
});