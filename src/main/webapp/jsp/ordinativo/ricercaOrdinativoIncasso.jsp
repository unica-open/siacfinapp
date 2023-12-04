<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

   <%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />
    
    <%-- Inclusione JavaScript NUOVO --%>
    <s:include value="/jsp/include/javascript.jsp" />	
   	<s:include value="/jsp/include/javascriptTree.jsp" />
</head>
<body>
  	
  <s:include value="/jsp/include/header.jsp" />
  
<div class="container-fluid-banner">
<a name="A-contenuti" title="A-contenuti"></a>
</div>
 
 <div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 contentPage">
    		<%-- SIAC-7952 rimuovo .do dalla action --%>
			<s:form method="post" action="ricercaOrdinativoIncasso" id="ricercaOrdinativoIncasso">
				<s:hidden name="uidOrdCollegaReversali" />
			
				<s:include value="/jsp/include/actionMessagesErrors.jsp" />
			
				<h3>Ricerca Ordinativi di Incasso</h3>
				<p>E' necessario inserire almeno un criterio di ricerca.</p>					
				<div class="step-content">	
					<div class="step-pane active" id="step1"> 
					<p class="margin-medium">
						<!-- task-131 <s:submit id="cerca2" name="cerca2" value="cerca" method="ricercaOrdinativoIncasso" cssClass="btn btn-primary pull-right" /> -->
						<s:submit id="cerca2" name="cerca2" value="cerca" action="ricercaOrdinativoIncasso_ricercaOrdinativoIncasso" cssClass="btn btn-primary pull-right" /> 
					</p>
					<br>

					<h4 class="step-pane">Ordinativo</h4>
					<fieldset class="form-horizontal">

						<div class="control-group">
							<label class="control-label"> Numero</label>
							<div class="controls">
								<span class="al">
									<label class="radio inline" for="perDA">Da</label>
								</span>
								<!-- <input type="text" name="perDA" value="" class="lbTextSmall span2" id="perDA" /> -->
								<s:textfield id="numeroOrdinativoDa" onkeyup="return checkItNumbersOnly(event)" name="model.numeroOrdinativoDa" cssClass="lbTextSmall span2"/>
								<span class="al">
									<label for="perA" class="radio inline">A</label>
								</span>
								<!-- <input type="text" name="perA" value="" class="lbTextSmall span2" id="perA" /> -->
								<s:textfield id="numeroOrdinativoA" onkeyup="return checkItNumbersOnly(event)" name="model.numeroOrdinativoA" cssClass="lbTextSmall span2"/>
							</div>
						</div>

						<div class="control-group">
							<label class="control-label" for="">Data emissione </label>
							<div class="controls">
								<span class="al">
									<label for="dataDa" class="radio inline">Da</label>
								</span>
								<!-- <input type="text" name="dataDa" value="" class="lbTextSmall span2" id="dataDa" /> -->
								<s:textfield id="dataEmissioneDa" title="gg/mm/aaaa" name="model.dataEmissioneDa" cssClass="lbTextSmall span2 datepicker"></s:textfield>
								<span class="al">
									<label for="dataA" class="radio inline">A</label>
								</span>
								<!-- <input type="text" name="dataA" value="" class="lbTextSmall span2" id="dataA" /> -->
								<s:textfield id="dataEmissioneA" title="gg/mm/aaaa" name="model.dataEmissioneA" cssClass="lbTextSmall span2 datepicker"></s:textfield>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="distinta">Distinta</label>
							<div class="controls">
								<s:if test="null!=model.listaDistinta">
									<s:select list="model.listaDistinta" id="listaDistinta" headerKey="" 
										headerValue="" name="model.distinta" cssClass="span3" 
										listKey="codice" listValue="codice+' - '+descrizione" />
								</s:if>    
								
								
								<span class="alRight">
									<label class="radio inline" for="ctTesoriere">Conto del tesoriere </label>
								</span>
								<s:if test="null!=model.listaContoTesoriere">
									<s:select list="model.listaContoTesoriere" id="listacontoTesoreria" headerKey=""  
										headerValue="" name="model.contoTesoriere" cssClass="span4" 
										listKey="codice" listValue="codice+' - '+descrizione" />
								</s:if> 
								  		   
							</div>
						</div>
	
						<div class="control-group">
							<div class="control-label">Stato operativo</div>
							<div class="controls">
								<s:if test="null!=model.listaStatoOperativo">
									<s:select list="model.listaStatoOperativo" id="listaStatoOperativo" headerKey="" 
										headerValue="" name="model.statoOperativo" cssClass="span3" 
										listKey="codice" listValue="codice+' - '+descrizione" />
								</s:if>      		   
								<%-- <select name="statoOperativoEN" id="statoOperativoEN" class="span4">
									<option>Incompleto</option>
									<option>xxxxx</option>
								</select> --%>
							</div>
						</div>
						
						<div class="control-group">
							<div class="control-label">Escludi annullati</div>
							<div class="controls">
	     						<s:checkbox id="escludiAnnullatiCheckBox" name="model.escludiAnnullati" onclick="impostaValoreEscludiAnnullati()"/>  		   
							</div>
						</div>
						
						<div class="control-group">
							<div class="control-label">Data trasmissione OIL</div>
							<div class="controls">
								<!-- <input type="text" name="dataOil" value="" class="lbTextSmall span2" id="dataOil" /> -->
								<s:textfield id="dataEmissioneDa" title="gg/mm/aaaa" name="model.dataTrasmissioneOIL" cssClass="lbTextSmall span2 datepicker"></s:textfield>
							</div>
						</div>
						
						
						<div class="control-group">
							<label class="control-label" for="m">Descrizione</label>
							<div class="controls">
								<s:textfield id="descrizione" name="model.descrizioneOrdinativo" cssClass="lbTextSmall span2"/>
							</div>
						</div>	
						
						
				<div class="control-group">
					<span class="control-label">Da trasmettere</span>
					<div class="controls">

						<label class="radio inline" for="daTrasmettereTrue">
							<input type="radio" name="model.daTrasmettere" id="daTrasmettereTrue" value="1"> S&igrave;
						</label>
							
						<label class="radio inline" for="daTrasmettereFalse">
							<input type="radio" name="model.daTrasmettere" id="daTrasmettereFalse" value="0"> No
						</label>
							
						<label class="radio inline" for="daTrasmettereNull">
							<input checked type="radio" name="model.daTrasmettere" id="daTrasmettereNull" value=""> Non applicare
						</label>
					</div>
				</div>						
						
					<h4>Accertamento</h4>
						<fieldset class="form-horizontal">
							<div class="control-group">
								<label class="control-label" for="anno">Anno </label>
								<div class="controls">    
									<!-- <input id="anno" class="span1" type="text" value="" name="anno"> -->
									<s:textfield id="annoAccertamento" maxlength="4" onkeyup="return checkItNumbersOnly(event)" name="model.annoAccertamento" cssClass="lbTextSmall span1" />
									<span class="al">
										<label class="radio inline" for="numero">Numero </label>
									</span>
									<!-- <input id="numero" class="span2" type="text" value="" name="numero"> -->     
									<s:textfield id="numeroAccertamento" onkeyup="return checkItNumbersOnly(event)" name="model.numeroAccertamento" cssClass="lbTextSmall span2"/>
									<span class="al">
										<label class="radio inline" for="Subaccertamento">Sub</label>
									</span>
									<!-- <input id="Subaccertamento" class="span2" type="text" value="" name="Subaccertamento"> -->
									<s:textfield id="numeroSubAccertamento" onkeyup="return checkItNumbersOnly(event)" name="model.numeroSubAccertamento" cssClass="lbTextSmall span2"/>
								</div>
							</div>
						</fieldset>				

					<div id="refreshHeaderCapitolo">
		            	<s:include value="/jsp/ordinativo/include/headerCapitoloOrdinativoRicerca.jsp"/>		            	
		            </div>
		            <s:include value="/jsp/ordinativo/include/capitoloOrdinativoRicerca.jsp" />
		            					 	
		            <div id="refreshHeaderSoggetto">
		            	<s:include value="/jsp/ordinativo/include/headerSoggettoOrdinativoRicerca.jsp"/>
		            </div>
		            <s:include value="/jsp/ordinativo/include/soggettoOrdinativoRicerca.jsp" /> 

		            <s:include value="/jsp/ordinativo/include/provvedimentoOrdinativoRicerca.jsp" />
		            
		            
		            <h4>Causale</h4>
						<fieldset class="form-horizontal">
							<div class="control-group">
								<label class="control-label" for="tipocaus">Tipo causale
								</label>
								<div class="controls">
								  <s:if test="null!=causaleEntrataTendino.listaTipiCausale">
							 		  <s:select list="causaleEntrataTendino.listaTipiCausale" id="listaTipiCausale" headerKey="" headerValue=""   
							           		   name="causaleEntrataTendino.codiceTipoCausale" cssClass="span9" 
							           		   listKey="codice" listValue="codice+' - '+descrizione" disabled="false" />
						          </s:if> 	
						         </div>
							</div>
							<div id="refreshTendinoCausali" style="display: inline">
								<s:include value="/jsp/ordinativo/include/tendinoCausaliPerRicerca.jsp" />
							</div>
						</fieldset>	
		            
		            <s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden>
		            
		            <s:hidden id="hiddenPerEscludiAnnullati" name="model.hiddenPerEscludiAnnullati" />

					<!-- Modal -->
					
					<s:set var="ricercaCapitoloAction" value="%{'ricercaOrdinativoIncasso_ricercaCapitolo'}" />
	        		<s:set var="pulisciRicercaCapitoloAction" value="%{'ricercaOrdinativoIncasso_pulisciRicercaCapitolo'}" />
	        		<s:set var="selezionaCapitoloAction" value="%{'ricercaOrdinativoIncasso_selezionaCapitolo'}" />
	        		<s:set var="visualizzaDettaglioCapitoloAction" value="%{'ricercaOrdinativoIncasso_visualizzaDettaglioCapitolo'}" />
	        
					<s:include value="/jsp/include/modalCapitolo.jsp" />
					
					<s:set var="selezionaProvvedimentoAction" value="%{'ricercaOrdinativoIncasso_selezionaProvvedimento'}" />
		    		<s:set var="clearRicercaProvvedimentoAction" value="%{'ricercaOrdinativoIncasso_clearRicercaProvvedimento'}" />	          
		    		<s:set var="ricercaProvvedimentoAction" value="%{'ricercaOrdinativoIncasso_ricercaProvvedimento'}" />	                          	
            
		            <s:include value="/jsp/include/modalProvvedimenti.jsp" />
		            <s:include value="/jsp/include/modalSoggetto.jsp" />
		            <!-- Fine Modal -->
		            
		            <div id="capitoloTab" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="capitoloTabLabel" aria-hidden="true">
              		<div class="modal-header">
		              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
		              <h3 id="myModalLabel2">Dettagli del capitolo</h3>
		              </div>
		              <div class="modal-body">
		               
					   <dl class="dl-horizontal">
					     <dt>Numero</dt>
					      <dd><s:property value="capitolo.numCapitolo"/> / <s:property value="capitolo.articolo"/> / <s:property value="capitolo.ueb"/> - <s:property value="capitolo.descrizione" /> - <s:property value="capitolo.codiceStrutturaAmministrativa"/> - <s:property value="capitolo.descrizioneStrutturaAmministrativa" />&nbsp;</dd>
					      <dt>Tipo finanziamento</dt>
					      <dd><s:property value="capitolo.tipoFinanziamento" />&nbsp;</dd>
					      <dt>Piano dei conti finanziario</dt>
					      <dd><s:property value="capitolo.descrizionePdcFinanziario" default=" "/>&nbsp;</dd>
					    </dl>
						<table class="table table-hover table-bordered">
					      <tr>
					        <th>&nbsp;</th>
					        <s:iterator value="datoPerVisualizza.importiCapitolo">
								<th scope="col" class="text-center"><s:property value="annoCompetenza" /></th>
							</s:iterator>
					      </tr>
					      <tr>
					        <th>Stanziamento</th>
					        <s:iterator value="datoPerVisualizza.importiCapitolo">
								<td><s:property value="getText('struts.money.format', {competenza})" /></td>
							</s:iterator>       
					      </tr>
					      <tr>
					        <th>Disponibile</th>

								<td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloEG[0].disponibilitaAccertareAnno1})" /></td>
								<td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloEG[0].disponibilitaAccertareAnno2})" /></td>
							    <td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloEG[0].disponibilitaAccertareAnno3})" /></td>
					      </tr>
					    </table>
							                
		                
		
		              </div>
		            </div>  
      
					</div>
				</div>
					
				<p class="margin-medium">
	            <s:include value="/jsp/include/indietro.jsp" /> 
				<!-- task-131 <s:submit name="annulla" value="annulla" method="annullaRicercaOrdinativi" cssClass="btn btn-secondary" /> -->
				<!-- task-131 <s:submit name="cerca" value="cerca" method="ricercaOrdinativoIncasso" cssClass="btn btn-primary pull-right" /> --> 
				<s:submit name="annulla" value="annulla" action="ricercaOrdinativoIncasso_annullaRicercaOrdinativi" cssClass="btn btn-secondary" />
				<s:submit name="cerca" value="cerca" action="ricercaOrdinativoIncasso_ricercaOrdinativoIncasso" cssClass="btn btn-primary pull-right" /> 
				
				</p>     
  
      </s:form>
    </div>
</div>	 
</div>	
<script type="text/javascript">

$(document).ready(function() {
	
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
	
	
    //aggiunta RM xche non funziona la selezione del radio della sac (provvedimento)
    $("#cerca").click(function() {
 	   //funcion definita in genericCustom.js
 	   //alert("passo di qui....");
 	   preselezionaStrutturaPaginaPrincipale();

	});	
    
  	//aggiunta RM xche non funziona la selezione del radio della sac (provvedimento)
    $("#cerca2").click(function() {
 	   //funcion definita in genericCustom.js
 	   //alert("passo di qui....");
 	   preselezionaStrutturaPaginaPrincipale();

	});	
  	
    $("#listaTipiCausale").change(function() {
		var selezionata = $("#listaTipiCausale").val();
		
		$.ajax({
			// task-131 url: '<s:url method="tipoCausaleEntrataChanged"></s:url>',
			url: '<s:url action="ricercaOrdinativoIncasso_tipoCausaleEntrataChanged"></s:url>',		
			type: "GET",
			data: $(".hiddenGestoreToggle").serialize() + "&selezionata=" + selezionata
		}).then(function(data)  {
	    	$("#refreshTendinoCausali").html(data);
		});	
		
	});	

	
});	

function impostaValoreEscludiAnnullati(){
	cbObj = document.getElementById("escludiAnnullatiCheckBox");
    var valore = cbObj.checked;
    $("#hiddenPerEscludiAnnullati").val(valore);
}

</script>  


<s:include value="/jsp/include/footer.jsp" />

