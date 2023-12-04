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
  
  
                
	<!-- NAVIGAZIONE -->
	<p class="nascosto"><a name="A-sommario" title="A-sommario"></a></p>     
	<ul id="sommario" class="nascosto">
		<li><a href="#A-contenuti">Salta ai contenuti</a></li>
	</ul>
	<!-- /NAVIGAZIONE -->
	<hr />
<div class="container-fluid-banner">



	<a name="A-contenuti" title="A-contenuti"></a>
</div>

	
</div>


<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 contentPage"> 
				
				<%-- SIAC-7952 rimuovo .do dalla action --%>
				<s:form id="mainForm" method="post" action="ricercaDocumentiPerRegolazioneCarta">
				
				    
				
					<s:include value="/jsp/include/actionMessagesErrors.jsp" />
					<!--#include virtual="include/alertErrorSuccess.html" -->
					<h3>Carta <s:property value="cartaContabileDaRicerca.numero"/> (stato <s:property value="cartaContabileDaRicerca.statoOperativoCartaContabile" /> del <s:property value="%{cartaContabileDaRicerca.dataCreazione}" />)</h3>
				
					<h4>Riga numero: <s:property value="numeroRigaSelezionata"/> - <s:property value="dettaglioRiga.soggetto.codiceSoggetto"/> - <s:property value="dettaglioRiga.soggetto.denominazione"/> </h4>
			
						<div class="accordion-group">
							<div class="accordion-heading">
								<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#DatiPrincipali" href="#DatiPrincipaliTAB">Riepilogo riga<span class="icon">&nbsp;</span></a>		
							</div>
						
							<div id="DatiPrincipaliTAB" class="accordion-body collapse">
								<div class="accordion-inner">

									<fieldset class="form-horizontal">	
										<div class="boxOrSpan2">
											<div class="boxOrInLeft">
<!-- 												<p>Quota</p> -->
												<ul class="htmlelt">	
													<li>
														<dfn>Codice creditore</dfn> 
														<dl><s:property value="dettaglioRiga.soggetto.codiceSoggetto"/></dl>
													</li>
													<li>
														<dfn>Denominazione</dfn> 
														<dl><s:property value="dettaglioRiga.soggetto.denominazione"/></dl>
													</li>
													<li>
														<dfn>Modalit&agrave; di pagamento</dfn> 
														<dl><s:property value="dettaglioRiga.modalitaPagamentoSoggetto.modalitaAccreditoSoggetto.descrizione"/></dl>
													</li>
													<li>
														<dfn>Anno</dfn> 
														<dl><s:property value="dettaglioRiga.impegno.annoMovimento"/></dl>
													</li>
													<li>
														<dfn>Impegno</dfn> 
														<dl><s:property value="dettaglioRiga.impegno.numero.intValue()"/></dl>
													</li>
													<li>
														<dfn>Subimpegno</dfn> 
														<dl><s:property value="dettaglioRiga.subImpegno.numero.intValue()"/></dl>
													</li>
													<li>
														<dfn>Importo</dfn> 
														<dl><s:property value="getText('struts.money.format', {dettaglioRiga.importo})" /></dl>
													</li>
													<li>
														<dfn>Da regolarizzare</dfn> 
														<dl><s:property value="getText('struts.money.format', {dettaglioRiga.importoDaRegolarizzare})" /></dl>
													</li>
													
												</ul>
											</div>
															

										
										</div>
										

									</fieldset>
									
								</div>
							</div>
						</div>
						
						<h4 class="step-pane">Collega a documento - Ricerca</h4>	
						
						
						<div class="step-content">
							<div class="step-pane active" id="step1">    
							 <h4>Documenti</h4>
							 
								<fieldset class="form-horizontal">
									
									<div class="control-group">
										<label class="control-label">Tipo documento</label>
											<div class="controls">
								  				<s:if test="ricercaDocumenti.listaDocTipoSpesa!=null">
													<s:select list="ricercaDocumenti.listaDocTipoSpesa"
														id="listaDocTipoSpesa"
														cssClass="parametroRicercaCapitolo"
														headerKey="" headerValue=""
														name="ricercaDocumenti.codiceTipoDoc"
														listKey="id" listValue="codice+' - '+descrizione" />
			 									</s:if>
								
											</div>
									</div>
									
									
									
									<div class="control-group">
										<label class="control-label">Documento</label>
										<div class="controls">   
											<input id="datiDocumentoAnno" name="ricercaDocumenti.datiDocumentoAnno" class="lbTextSmall span1" type="text" value="" maxlength="4" placeholder="Anno"/>
											<input id="datiDocumentoNumero"  name="ricercaDocumenti.datiDocumentoNumero" class="lbTextSmall span1" type="text" value="" placeholder="numero" />  
											<input id="datiDocumentoQuota" name="ricercaDocumenti.datiDocumentoQuota" class="lbTextSmall span2" type="text" value="" placeholder="quota" />      
											<span class="alRight">
												<label class="radio inline">Data documento</label>
											</span>
											<input id="dataDocumento" name="ricercaDocumenti.dataDocumento" class="span2 datepicker" type="text" value="" placeholder="11/05/2013" />                  
										</div>
									</div>
									
									
									<!--#include virtual="include/soggetto.html" -->
									<div id="refreshHeaderSoggetto">
							          	<s:include value="/jsp/carta/include/headerSoggettoCarta.jsp"/>		
							          	<!-- Abbiamo il soggetto fisso a quello della carta, commento: -->       	
<%-- 								       	<s:include value="/jsp/carta/include/soggettoCarta.jsp" />  --%>
<%-- 								       	<s:include value="/jsp/include/modalSoggetto.jsp" />									       	 --%>
						        	</div>
						        	
        							<h4>Elenco</h4>
					
									<div class="control-group">
										<label class="control-label">Anno </label>
										<div class="controls">   
											<input id="elencoAnno" name="ricercaDocumenti.elencoAnno" class="lbTextSmall span1" type="text" value="" maxlength="4" />    
											<span class="alRight">
												<label class="radio inline">Numero </label>
											</span>
											<input id="elencoNumero" name="ricercaDocumenti.elencoNumero" class="lbTextSmall span2" type="text" value="" />                  
										</div>
									</div>
								
								
								<!-- provvedimento -->
									<s:include value="/jsp/carta/include/provvedimentoRicercaRigaDoc.jsp" />									
								<!-- provvedimento -->
								
						
										
								</fieldset>    
							</div>
						</div>
					
					
					<p class="margin-medium">
						<!-- task-131 <s:submit name="annulla" value="annulla" method="annullaRicercaDoc" cssClass="btn btn-secondary" /> -->
						<s:submit name="annulla" value="annulla" action="ricercaDocumentiPerRegolazioneCarta_annullaRicercaDoc" cssClass="btn btn-secondary" /> -->
<%-- 						<s:include value="/jsp/include/indietro.jsp" /> --%>
						<!-- task-131 <s:submit name="indietro" value="indietro" method="indietro" cssClass="btn" /> -->
						<s:submit name="indietro" value="indietro" action="ricercaDocumentiPerRegolazioneCarta_indietro" cssClass="btn" />
						<span class="pull-right">
							<!-- task-131 <s:submit name="cerca" value="cerca" method="cercaDocPerCollega" cssClass="btn btn-primary" /> -->
							<s:submit name="cerca" value="cerca" action="ricercaDocumentiPerRegolazioneCarta_cercaDocPerCollega" cssClass="btn btn-primary" />
						</span>
					</p>  
					
					<s:set var="selezionaProvvedimentoAction" value="%{'ricercaDocumentiPerRegolazioneCarta_selezionaProvvedimento'}" />
		            <s:set var="clearRicercaProvvedimentoAction" value="%{'ricercaDocumentiPerRegolazioneCarta_clearRicercaProvvedimento'}" />	          
		        	<s:set var="ricercaProvvedimentoAction" value="%{'ricercaDocumentiPerRegolazioneCarta_ricercaProvvedimento'}" />	          
		            <s:include value="/jsp/include/modalProvvedimenti.jsp" />
					
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
	
});	
</script>		

<s:include value="/jsp/include/footer.jsp" />
