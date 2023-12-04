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
				<s:form id="mainForm" method="post" action="elencoDocumentiPerRegolazioneCarta">
				
				    
				
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
						
						<h4 class="step-pane">Collega a documento</h4>	
						
						<fieldset class="form-horizontal">
<!-- 							<input id="selectall" type="checkbox">&nbsp;<b>Seleziona tutto</b> -->
		
		                    <display:table name="ricercaDocumenti.elencoSubdocumentoSpesa" 
											class="table table-hover tab_left" 
											summary="riepilogo carte contabili" 
											pagesize="10" requestURI="elencoDocumentiPerRegolazioneCarta.do" 
											partialList="true" size="ricercaDocumenti.resultSize"
											uid="ricercaSubDocPerNuovaRigaCartaID" keepStatus="true" clearStatus="${clearPagina}">
											
								   <display:column class="ckRigaClass">
<%-- 									    <s:checkboxlist id="ckRiga" list="%{#attr.ricercaSubDocPerNuovaRigaCartaID.uid}"  name="ckRigaSelezionata" theme="displaytag" /> --%>
						       	  		<s:radio id="checkDocUid" list="%{#attr.ricercaSubDocPerNuovaRigaCartaID.uid}" name="ricercaDocumenti.radioUidDocSelezionato" theme="displaytag"></s:radio>
						       	   </display:column>
									
									
									<display:column title="Documento">
										<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.documento.anno}"/>/<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.documento.tipoDocumento.codice}"/>
										/<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.documento.numero}"/>
									</display:column>	 
									
									<display:column title="Data" property="documento.dataEmissione" format="{0,date,dd/MM/yyyy}"/>	 
									
									<display:column title="Stato">
										<a href="#" data-trigger="hover" rel="popover" title="Stato" data-content="<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.documento.statoOperativoDocumento.descrizione}"/>">
											<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.documento.statoOperativoDocumento.codice}"/>
										</a>
									</display:column>
									
									
									
									<display:column title="Soggetto"><s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.documento.soggetto.codiceSoggetto}"/>-<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.documento.soggetto.denominazione}"/></display:column>
									
									<display:column title="Quota" property="numero"></display:column>
									
									<display:column title="Impegno" >
										 <s:if test="%{#attr.ricercaSubDocPerNuovaRigaCartaID.subImpegno == null}">
			 	     						<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.impegno.annoMovimento}"/>/<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.impegno.numero.intValue()}"/>
			  							 </s:if>
										  <s:else>
										 	<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.impegno.annoMovimento}"/>/<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.impegno.numero.intValue()}"/>/<s:property value="%{#attr.ricercaSubDocPerNuovaRigaCartaID.subImpegno.numero.intValue()}"/>
										 </s:else>
									</display:column>
									
									<display:column title="Importo" property="importo"  		
											decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
								 	
										
							</display:table>				
						</fieldset>
						
		
					
						<div class="Border_line"></div>
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
<!-- 							<a id="consolidaReg" data-toggle="modal" href="#consolidaRegModal"  class="btn btn-primary pull-right" >collega</a> -->
					        <!--task-131 <s:submit id="eseguiCollega" cssClass="btn btn-primary pull-right freezePagina" method="preCheckCollegaDocumento" value="collega" name="collega" />-->
					        <s:submit id="eseguiCollega" cssClass="btn btn-primary pull-right freezePagina" action="elencoDocumentiPerRegolazioneCarta_preCheckCollegaDocumento" value="collega" name="collega" />
						</p>  
					
					
				        <a id="linkMsgCheckConferma" href="#msgConfermaCollega" style="display: none;" data-toggle="modal"></a>
       					<a id="linkMsgCheckProvvisorioDiCassa" href="#msgCheckProvvisorioDiCassa" style="display: none;" data-toggle="modal"></a>
					
					
					
						<div id="msgConfermaCollega" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgPrimaNota" aria-hidden="true">
					      <div class="modal-body">
					          
					        <div style="padding: 0px 20px 0px 20px">
								<s:include value="/jsp/include/actionMessagesErrors.jsp" />
							</div>
					        
					        <p>Verra' collegata la quota documento selezionata alla riga della carta, vuoi procedere ?</p>
					        
					      </div>
					      <div class="modal-footer">
					        <button class="btn" data-dismiss="modal" aria-hidden="true">no</button>
					        <!-- task-131 <s:submit id="eseguiCollega1" cssClass="btn btn-primary pull-right freezePagina" method="preCheckControlliDiMerito" value="si" name="si" /> -->
					        <s:submit id="eseguiCollega1" cssClass="btn btn-primary pull-right freezePagina" action="elencoDocumentiPerRegolazioneCarta_preCheckControlliDiMerito" value="si" name="si" />				 
					      </div>
						</div>   
		
						<div id="msgCheckProvvisorioDiCassa" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgPrimaNota" aria-hidden="true">
						      <div class="modal-body">
						          
						        <div style="padding: 0px 20px 0px 20px">
									<s:include value="/jsp/include/actionMessagesErrors.jsp" />
								</div>
						        
						        <p>Documento senza provvisorio di cassa. Attenzione, prima di pagare il documento collegare il provvisorio di cassa.
						           Vuoi salvare comunque ?</p>
						        
						      </div>
						      <div class="modal-footer">
						        <button class="btn" data-dismiss="modal" aria-hidden="true">no</button>
						        <!-- task-131 <s:submit id="eseguiCollega2" cssClass="btn btn-primary pull-right freezePagina" method="collega" value="si" name="si" /> -->
						        <s:submit id="eseguiCollega2" cssClass="btn btn-primary pull-right freezePagina" action="elencoDocumentiPerRegolazioneCarta_collega" value="si" name="si" />
						      </div>
						</div>  
					
				</s:form>
				
		</div>	  
	</div>	 
</div>	
<script type="text/javascript">


	<s:if test="ricercaDocumenti.checkConfermaCollega">
		$("#linkMsgCheckConferma").click();
	</s:if>
	
	<s:if test="ricercaDocumenti.checkWarningSenzaProvvisorioDiCassa">
		$("#linkMsgCheckProvvisorioDiCassa").click();
	</s:if>

$(document).ready(function() {
	
	
	
});	
</script>		

<s:include value="/jsp/include/footer.jsp" />
