<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

<%-- Inclusione head e CSS NUOVO --%>
<s:include value="/jsp/include/head.jsp" />

<%-- Inclusione JavaScript NUOVO --%>
<s:include value="/jsp/include/javascript.jsp" />
</head>

<body>   
<s:include value="/jsp/include/header.jsp" />                    
  <p class="nascosto"><a title="A-sommario"></a></p>     
  <ul id="sommario" class="nascosto">
    <li><a href="#A-contenuti">Salta ai contenuti</a></li>
  </ul>
  <hr />
<div class="container-fluid-banner">

  <a title="A-contenuti"></a>
</div>

<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">			
		<s:form cssClass="form-horizontal" id="inserisciLiquidazioneStep2" action="inserisciLiquidazioneStep3Aggiorna" method="post">     
		<h3>Liquidazione: <s:property value="annoLiquidazioneScheda"/> / <s:property value="numeroLiquidazioneScheda.intValue()"/><s:if test="%{descrizioneLiquidazioneScheda != null && descrizioneLiquidazioneScheda != ''}"> - </s:if><s:property value="descrizioneLiquidazioneScheda"/><s:if test="%{importoLiquidazioneScheda != null && importoLiquidazioneScheda != ''}"> - </s:if><s:property value="importoLiquidazioneScheda"/></h3>	
			
			<s:include value="/jsp/include/actionMessagesErrors.jsp" />
			
			<dl class="dl-horizontal">				
				<dt>Capitolo <a class="tooltip-test" title="Visualizza dettagli" href="#capitoloTab" data-toggle="modal"><i class="icon-info-sign">&nbsp;<span class="nascosto">Visualizza dettagli</span></i></a></dt>
				 <dd><s:property value="capitolo.annoCapitolo"/>/<s:property value="capitolo.numeroCapitolo"/>/<s:property value="capitolo.numeroArticolo"/>/<s:property value="capitolo.numeroUEB"/>  - <s:property value="capitolo.descrizione"/>  -  <s:property value="capitolo.strutturaAmministrativoContabile.codice"/> - tipo Finanziamento: <s:property value="capitolo.tipoFinanziamento.descrizione"/></dd>
						<%-- <dd><s:property value="capitolo.annoCapitolo"/>/<s:property value="capitolo.numeroCapitolo"/>/<s:property value="capitolo.numeroUEB"/>  - <s:property value="capitolo.descrizione"/>  -  <s:property value="capitolo.strutturaAmministrativoContabile.codice"/> - tipo Finanziamento: <s:property value="capitolo.tipoFinanziamento.codice"/></dd> --%>
				
				    <div id="capitoloTab" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="capitoloTabLabel" aria-hidden="true">
              		<div class="modal-header">
		              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
		              <h3 id="myModalLabel2">Dettagli del capitolo</h3>
		              </div>
		              <div class="modal-body">
		                <%-- <dl class="dl-horizontal">
		                  <dt>Numero</dt>
		                  <dd >cap <s:property value="capitolo.numeroCapitolo" default=" - "></s:property> - art <s:property value="capitolo.numeroArticolo" default="-"></s:property> - ueb <s:property value="capitolo.numeroUEB" default="-"></s:property></dd>
		                  <!-- <dd>cap - art - ueb</dd> -->
		                  <dt>Tipo finanziamento</dt>
		                  <dd><s:property value="capitolo.tipoFinanziamento.codice"/> - <s:property value="capitolo.tipoFinanziamento.descrizione"/></dd>
		                  <dt>Piano dei conti finanziario</dt>
		                  <dd><s:property value="capitolo.elementoPianoDeiConti.codice"/> - <s:property value="capitolo.elementoPianoDeiConti.descrizione"/></dd>
		                </dl>
		                
		                <display:table name="capitolo.listaImportiCapitolo" class="table table-hover tab_left" summary="riepilogo importi capitolo" uid="capitoloImportiSupport">
					       
						  	<display:column title="" property="annoCompetenza"/>
						  	<display:column title="Stanziamento" property="stanziamento" />
						  	<display:column title="Disponibile" property="disponibilitaImpegnare" />
					   </display:table> --%>
					   
					   <dl class="dl-horizontal">
					     <dt>Numero</dt>
					      <dd><s:property value="capitolo.annoCapitolo"/> / <s:property value="capitolo.numeroCapitolo"/> / <s:property value="capitolo.numeroArticolo"/> / <s:property value="capitolo.numeroUEB"/> - <s:property value="capitolo.descrizione" /> - <s:property value="capitolo.strutturaAmministrativoContabile.descrizione" />&nbsp;</dd>
					      <dt>Tipo finanziamento</dt>
					      <dd><s:property value="capitolo.tipoFinanziamento.descrizione" />&nbsp;</dd>
					      <dt>Piano dei conti finanziario</dt>
					      <dd><s:property value="capitolo.elementoPianoDeiConti.codice" default=" "/> - <s:property value="capitolo.elementoPianoDeiConti.descrizione" default=" "/>&nbsp;</dd>
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
<%-- 					        <s:iterator value="datoPerVisualizza.importiCapitolo"> --%>
<%-- 								<td><s:property value="getText('struts.money.format', {disponibile})" /></td> --%>
<%-- 							</s:iterator>  --%>
								<td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloUG[0].disponibilitaImpegnareAnno1})" /></td>
								<td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloUG[0].disponibilitaImpegnareAnno2})" /></td>
							    <td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloUG[0].disponibilitaImpegnareAnno3})" /></td>
					      </tr>
					    </table>
							                
		                
		<!--                 <table class="table table-hover table-bordered"> -->
		<!--                   <tr> -->
		<!--                     <th>&nbsp;</th> -->
		<!--                     <th scope="col" class="text-center">2013</th> -->
		<!--                     <th scope="col" class="text-center">2014</th> -->
		<!--                     <th scope="col" class="text-center">2015</th> -->
		<!--                   </tr> -->
		<!--                   <tr> -->
		<!--                     <th>Stanziamento</th> -->
		<!--                     <td>&nbsp;</td> -->
		<!--                     <td>&nbsp;</td> -->
		<!--                     <td>&nbsp;</td>        -->
		<!--                   </tr> -->
		<!--                   <tr> -->
		<!--                     <th>Disponibile </th> -->
		<!--                     <td scope="row" >&nbsp;</td> -->
		<!--                     <td>&nbsp;</td> -->
		<!--                     <td>&nbsp;</td>         -->
		<!--                   </tr> -->
		<!--                 </table> -->
		              </div>
		            </div>  
				
				
				
				<%-- <dt>Provvedimento</dt>
				  <dd><s:property value="provvedimento.annoProvvedimento"/>/<s:property value="provvedimento.numeroProvvedimento"/> - <s:property value="provvedimento.tipoProvvedimento"/> - <s:property value="provvedimento.oggetto"/> - <s:property value="provvedimento.strutturaAmministrativa"/> -  Stato: <s:property value="provvedimento.stato"/></dd>
				<dt>Soggetto</dt>
				  <s:if test="soggettoSelezionato">
			    	<dd><s:property value="soggetto.codCreditore"/> - <s:property value="soggetto.denominazione"/> - CF: <s:property value="soggetto.codfisc"/> - PIVA : <s:property value="soggetto.piva"/>  - Classe <s:property value="soggetto.classe"/></dd>
				  </s:if>
				  <s:else>
					<dd> ... </dd>				  
			      </s:else>			 --%>	          																
				<dt>Impegno</dt>
					<dd><s:property value="annoImpegno"/>/<s:property value="numeroImpegno"/><s:if test="%{numeroSub != null && numeroSub != ''}">/</s:if><s:property value="numeroSub"/><s:if test="%{impegno.descrizione != null && impegno.descrizione != ''}"> - </s:if><s:property value="impegno.descrizione"/><s:if test="%{impegno.tipoImpegno.descrizione != null && impegno.tipoImpegno.descrizione != ''}"> - </s:if><s:property value="impegno.tipoImpegno.descrizione"/></dd>

			</dl>

		 <div id="MyWizard" class="wizard">
			<ul class="steps">
				<li data-target="#step1" class="complete" ><span class="badge badge-success">1</span>dati liquidazione<span class="chevron"></span></li>
			</ul>
		</div>
		
        <div class="step-content">	
            <div class="step-pane active" id="step1">	        	
	        	
	        	<s:set var="resedeAction" value="%{'inserisciLiquidazioneStep3Aggiorna_resede'}" />	
	        	<s:set var="remodpagamentoAction" value="%{'inserisciLiquidazioneStep3Aggiorna_remodpagamento'}" />	    
	        	    
	        	
	        	<div id="refreshHeaderSoggetto">	       	
			       	<s:include value="/jsp/liquidazione/include/headerCreditoreLiquidazioneAggiorna.jsp" /> 		        	
		        	<div id="refreshSediSecondarie">
		        		<s:include value="/jsp/liquidazione/include/sediSecondarie.jsp" />	   
		        	</div>		        	
		        	<div id="refreshModPagamento">  	
	        			<s:include value="/jsp/liquidazione/include/modalitaPagamento.jsp" />
		        	</div>
	        	</div> 
	        	
	        	<s:if test="!provvedimentoModificabile">
	        		<s:include value="/jsp/liquidazione/include/provvedimentoLiquidazioneLock.jsp" />
	        	</s:if>
	        	<s:else>
	        		<s:include value="/jsp/liquidazione/include/provvedimentoLiquidazione.jsp" />
	        	</s:else>
	        	
	        	<s:set var="selezionaProvvedimentoAction" value="%{'inserisciLiquidazioneStep3Aggiorna_selezionaProvvedimento'}" />
		        <s:set var="clearRicercaProvvedimentoAction" value="%{'inserisciLiquidazioneStep3Aggiorna_clearRicercaProvvedimento'}" />	          
		        <s:set var="ricercaProvvedimentoAction" value="%{'inserisciLiquidazioneStep3Aggiorna_ricercaProvvedimento'}" />	              
				<s:include value="/jsp/include/modalProvvedimenti.jsp" />			
				
				<s:include value="/jsp/liquidazione/include/liquidazione.jsp" />        	
	        	
	        	<s:set var="codiceSiopeChangedAction" value="%{'inserisciLiquidazioneStep3Aggiorna_codiceSiopeChanged'}" />
		        <s:set var="confermaPdcAction" value="%{'inserisciLiquidazioneStep3Aggiorna_confermaPdc'}" />
		        
	        	<s:include value="/jsp/include/transazioneElementare.jsp" />
	        		            	        
	        </div>            
        </div>
		<p class="margin-medium">
			
			<s:if test="%{!flagIndietro}">
				<s:include value="/jsp/include/indietro.jsp" />
			</s:if>
			<s:else>
				<!-- task-131 <s:submit name="indietro" value="indietro" method="indietro" cssClass="btn btn-secondary" /> -->
				<s:submit name="indietro" value="indietro" action="inserisciLiquidazioneStep3Aggiorna_indietro" cssClass="btn btn-secondary" />
			</s:else>
			
			
			<s:submit name="annulla" value="annulla" id="annulla" cssClass="btn btn-secondary"/>				
			<!-- task-131 <s:submit name="salva" value="salva" method="salva" cssClass="btn btn-primary pull-right freezePagina" /> -->
			<s:submit name="salva" value="salva" action="inserisciLiquidazioneStep3Aggiorna_salva" cssClass="btn btn-primary pull-right freezePagina" /> 
		</p>          
          
      </s:form>
     </div>
  </div>	 
</div>	
  <script type="text/javascript">

	$(document).ready(function() {
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
		
		$("#annulla").click(function(){
			window.location.reload();
		});	
		
		$("#codCreditoreLiquidazione").change(function(){
			var cod = $("#codCreditoreLiquidazione").val();
			//Carico i dati in tabella "Modalita' di pagamento"		
			$.ajax({
				//task-131 url: '<s:url method="modpagamento"></s:url>',
				url: '<s:url action="inserisciLiquidazioneStep3Aggiorna_modpagamento"/>',
				type: "GET",
				data: { id: cod },
			    success: function(data)  {
			    	$("#refreshModPagamento").html(data);
			    	//Carico i dati in tabella "Sedi secondarie"
					$.ajax({
						//task-131 url: '<s:url method="sedisecondarie"></s:url>',
						url: '<s:url action="inserisciLiquidazioneStep3Aggiorna_sedisecondarie"/>',
						type: "GET",
						data: { id: cod },
					    success: function(data)  {
					    	$("#refreshSediSecondarie").html(data);
						}
					});
				}
			});			
		});	
		
		/*
		$("#cup").change(function(){
			var cup = $("#cup").val();		
			$.ajax({
				url: '<s:url method="setcup"></s:url>',
				type: "GET",
				data: { cup: cup },
			    success: function(data)  {
			    	$("#refreshTRX").html(data);
				}
			});			
		});	
		*/
		
		$("#linkCompilazioneGuidataSoggetto").click(function(){
			initRicercaGuidataSoggetto(
				$("#codCreditoreLiquidazione").val(),
				null
			);
		});
		
		
		var tipoDebitoSiopeVar = $("input[name='tipoDebitoSiope']");
		<s:include value="/jsp/include/toggleAssenzaCig.jsp" />	
		
	});
	
</script> 
<s:include value="/jsp/include/footer.jsp" />
