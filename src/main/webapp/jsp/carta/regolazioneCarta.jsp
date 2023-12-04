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
				<s:form id="mainForm" method="post" action="regolazioneCarta">
				
				    
				
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
						
						<h4 class="step-pane">Documenti collegati</h4>	
						
						 <display:table name="dettaglioRiga.listaSubDocumentiSpesaCollegati" 
								class="table table-hover tab_left" 
								summary="riepilogo regolazione" 
								pagesize="10" requestURI="regolazioneCarta.do" 
								uid="righeRegoCartaID" >
								
							<display:column title="Documento" >
								<s:if test="%{#attr.righeRegoCartaID.documento!=null}">
									<s:property value="%{#attr.righeRegoCartaID.documento.anno}" /> / <s:property value="%{#attr.righeRegoCartaID.documento.tipoDocumento.codice}" /> / <s:property value="%{#attr.righeRegoCartaID.documento.numero}" />
								</s:if>
								<s:else> - </s:else>
							</display:column>
							
							<display:column title="Soggetto">
								<s:if test="%{#attr.righeRegoCartaID.documento!=null}">
									<s:property value="%{#attr.righeRegoCartaID.documento.soggetto.codiceSoggetto}" /> / <s:property value="%{#attr.righeRegoCartaID.documento.soggetto.denominazione}" />
								</s:if>	
								<s:else> - </s:else>
							</display:column>
							
							<display:column title="Data"  property="documento.dataEmissione" format="{0,date,dd/MM/yyyy}"/>
							<display:column title="Stato" property="documento.statoOperativoDocumento.codice" />
							<display:column title="Quota" property="numero" />
							
							<display:column title="Impegno">
								<s:if test="%{#attr.righeRegoCartaID.subImpegno == null ||  #attr.righeRegoCartaID.subImpegno.numero == null}">
									<s:property value="%{#attr.righeRegoCartaID.impegno.capitoloUscitaGestione.annoCapitolo}" /> / <s:property value="%{#attr.righeRegoCartaID.impegno.annoMovimento}" /> / <s:property value="%{#attr.righeRegoCartaID.impegno.numero.intValue()}" /> 
								</s:if>
								<s:else>
									<s:property value="%{#attr.righeRegoCartaID.impegno.capitoloUscitaGestione.annoCapitolo}" /> / <s:property value="%{#attr.righeRegoCartaID.impegno.annoMovimento}" /> / <s:property value="%{#attr.righeRegoCartaID.impegno.numero.intValue()}" /> - <s:property value="%{#attr.righeRegoCartaID.subImpegno.numero.intValue()}" /> 
								
								</s:else>
							</display:column>
							<display:column title="Importo quota" property="importo" class="tab_Right" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"  />
							
							
							<display:column>
		    	                <s:if test="%{#attr.righeRegoCartaID.documento==null}">
			    	               <div class="btn-group">
										<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
										<ul class="dropdown-menu pull-right">
<!-- 											<li><a href="#aggRegolaCarta" data-toggle="modal">aggiorna</a></li> -->
											
											
											
												<s:if test="%{#attr.righeRegoCartaID.subImpegno == null ||  #attr.righeRegoCartaID.subImpegno.numero == null}">
												
<%-- 													<li><a id="linkAggiorna_<s:property value="%{#attr.righeRegoCartaID.impegno.capitoloUscitaGestione.annoCapitolo}" />_<s:property value="%{#attr.righeRegoCartaID.impegno.annoMovimento}" />_<s:property value="%{#attr.righeRegoCartaID.impegno.numero.intValue()}" />_null" --%>
													<li><a id="linkAggiorna_<s:property value="%{#attr.righeRegoCartaID.uid}" />"
													    	href="#aggRegolaCarta" data-toggle="modal" class="linkAggiornaRiga">aggiorna</a></li>
													    	
													   	
													    	
													<li><a id="linkAnnulla_<s:property value="%{#attr.righeRegoCartaID.impegno.capitoloUscitaGestione.annoCapitolo}" />_<s:property value="%{#attr.righeRegoCartaID.impegno.annoMovimento}" />_<s:property value="%{#attr.righeRegoCartaID.impegno.numero.intValue()}" />_null_<s:property value="%{#attr.righeRegoCartaID.uid}" />"
													       href="#msgElimina" data-toggle="modal" class="linkEliminaRiga" >elimina</a>
													</li>
												 
												 </s:if>
												 <s:else>
												 
<%-- 													<li><a id="linkAggiorna_<s:property value="%{#attr.righeRegoCartaID.impegno.capitoloUscitaGestione.annoCapitolo}" />_<s:property value="%{#attr.righeRegoCartaID.impegno.annoMovimento}" />_<s:property value="%{#attr.righeRegoCartaID.impegno.numero.intValue()}" />_<s:property value="%{#attr.righeRegoCartaID.subImpegno.numero.intValue()}" />" --%>
														<li><a id="linkAggiorna_<s:property value="%{#attr.righeRegoCartaID.uid}" />"
													    	href="#aggRegolaCarta" data-toggle="modal" class="linkAggiornaRiga">aggiorna</a></li>
													    	
													   	
													    	
													<li><a id="linkAnnulla_<s:property value="%{#attr.righeRegoCartaID.impegno.capitoloUscitaGestione.annoCapitolo}" />_<s:property value="%{#attr.righeRegoCartaID.impegno.annoMovimento}" />_<s:property value="%{#attr.righeRegoCartaID.impegno.numero.intValue()}" />_<s:property value="%{#attr.righeRegoCartaID.subImpegno.numero.intValue()}" />_<s:property value="%{#attr.righeRegoCartaID.uid}" />" 
													       href="#msgElimina" data-toggle="modal" class="linkEliminaRiga" >elimina</a>
													</li>
												 
												 </s:else>
											 
										</ul>
									</div>      
		    	                </s:if>
								                         
							</display:column>
							
							
							<display:footer>
					        		<tr> 
										<th>Totale documenti collegati</th>
					                    <th>&nbsp;</th>
					                    <th>&nbsp;</th>
					                    <th>&nbsp;</th>
					                    <th>&nbsp;</th>
					                    <th>&nbsp;</th>
					                    <th class="tab_Right"><s:property value="getText('struts.money.format', {sommaDocCollegatiRegolazione})" /></th>
					                    <th>&nbsp;</th>
					                </tr>  
					        </display:footer>	
								
						  </display:table>		
			
						<p>
						
							<a class="btn btn-primary" id="collImpegnoARef" data-toggle="collapse" data-target="#collImpegno">Collega impegno</a>
							<!-- task-131 <s:submit name="Collega documento" value="Collega documento" method="collegaDocumento" cssClass="btn btn-primary" /> -->
							<s:submit name="Collega documento" value="Collega documento" action="regolazioneCarta_collegaDocumento" cssClass="btn btn-primary" />	
						</p>
						
						<div class="clear"></div>
						
						<!-- BLOCCO A SCOMPARSA PER COLLEGARE L'IMPEGNO: -->
						<s:include value="/jsp/carta/include/collegaImpegnoPerRegolarizzaCarta.jsp" />
			
					
					<div class="Border_line"></div>
					<p>
						<s:include value="/jsp/include/indietro.jsp" />
						<a id="consolidaReg" data-toggle="modal" href="#consolidaRegModal"  class="btn btn-primary pull-right" >consolida regolarizzazione</a>
					</p>  
					
					<s:set var="confermaImpegnoCartaAction" value="%{'regolazioneCarta_confermaImpegnoCarta'}" />			
            		<s:set var="pulisciRicercaImpegnoAction" value="%{'regolazioneCarta_pulisciRicercaImpegno'}" />	          
            		<s:set var="ricercaGuidataImpegnoAction" value="%{'regolazioneCarta_ricercaGuidataImpegno'}" />	          
         
					<s:include value="/jsp/carta/include/modalImpegnoCarta.jsp" />
					
					<!-- Modal elimina -->
					<div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
						<div class="modal-body">
							<div class="alert alert-error">
								<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
								<p><strong>Attenzione!</strong></p>
								<p><strong>Elemento selezionato: <s:textfield cssStyle="width:30px;" id="numeroAnnoCapitolo" name="numeroAnnoCapitolo" disabled="true"/> / 
								                                 <s:textfield cssStyle="width:30px;" id="numeroAnnoMovimento" name="numeroAnnoMovimento" disabled="true"/> /
								                                 <s:textfield cssStyle="width:50px;" id="numeroImpegnoDaPassare" name="numeroImpegnoDaPassare" disabled="true"/>   -
								                                 <s:textfield cssStyle="width:30px;" cssClass="prova" id="numeroSubImpegnoDaPassare" name="numeroSubImpegnoDaPassare" disabled="true"/>  </strong></p>
								<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
							</div>
						</div>
						<div class="modal-footer">
							<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
							<!-- task-131 <s:submit id="submitBtn" name="btnEliminaDocumentoCollegato" value="si, prosegui" cssClass="btn btn-primary" method="eliminaDocumentoCollegato"/> -->
							<s:submit id="submitBtn" name="btnEliminaDocumentoCollegato" value="si, prosegui" cssClass="btn btn-primary" action="regolazioneCarta_eliminaDocumentoCollegato"/>							
						</div>
					</div>  
					<!--/modale elimina -->
					
					
<!-- 					Modal Consolida Regolarizzazione -->
					<div id="consolidaRegModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgConsolidaLabel" aria-hidden="true">
						<div class="modal-body">
							<div class="alert alert-error">
								<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
								<p><strong>Attenzione!</strong></p>
								<p>Sei sicuro di voler regolarizzare ?</p>
							</div>
						</div>
						<div class="modal-footer">
							<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
							<!-- task-131 <s:submit cssClass="btn btn-primary" method="consolidaRegolarizzazione" value="consolida regolarizzazione" name="consolida" /> -->
							<s:submit cssClass="btn btn-primary" action="regolazioneCarta_consolidaRegolarizzazione" value="consolida regolarizzazione" name="consolida" />
							
						</div>
					</div>  
<!-- 					/modale Consolida Regolarizzazione -->
					
					
					<!-- Modale Aggiorna -->
					<div id="aggRegolaCarta" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="aggRegolaCartaLabel" aria-hidden="true">
						<div id="dettaglioDocInModificaDiv"></div>
	<%-- 						<s:include value="/jsp/carta/include/dettaglioDocInModifica.jsp" /> --%>
						 
					</div>
					<!--modale aggiorna -->
					
					<s:hidden id="numeroAnnoCapitolo" name="numeroAnnoCapitolo"/>
       				<s:hidden id="numeroAnnoMovimento" name="numeroAnnoMovimento"/>
       				<s:hidden id="numeroImpegnoDaPassare" name="numeroImpegnoDaPassare"/>
       				<s:hidden id="numeroSubImpegnoDaPassare" name="numeroSubImpegnoDaPassare"/>
       				
       				
       				<s:hidden id="numeroAnnoCapitoloPerElimina" name="numeroAnnoCapitoloPerElimina"/>
       				<s:hidden id="numeroAnnoMovimentoPerElimina" name="numeroAnnoMovimentoPerElimina"/>
       				<s:hidden id="numeroImpegnoDaPassarePerElimina" name="numeroImpegnoDaPassarePerElimina"/>
       				<s:hidden id="numeroSubImpegnoDaPassarePerElimina" name="numeroSubImpegnoDaPassarePerElimina"/>
       				
       				<s:hidden id="uidTemporaneo" name="uidTemporaneo"/>
       				
					<s:hidden name="toggleCollegaImpegnoAperto" id="toggleCollegaImpegnoAperto" />
					
				</s:form>
				
		</div>	  
	</div>	 
</div>	
<script type="text/javascript">
$(document).ready(function() {
	
	
	var toggleCollegaImpegnoAperto = $("#toggleCollegaImpegnoAperto").val();
	if(toggleCollegaImpegnoAperto=="true"){
		
		$("#collImpegnoARef").click();
	}
	
	
	$("#linkCompilazioneGuidataImpegno").click(function(){
			initRicercaGuidataImpegno(
					$("#annoImpegno").val(), 
					$("#numeroImpegno").val()
			);
		});
	
	$("#annullaCollega").click(function(){
		
		$("#annoImpegno").val("");
		$("#numeroImpegno").val("");
		$("#numeroSub").val("");
		$("#importoImpegno").val("");
		
		
	});
	
	
	
	$(".linkAggiornaRiga").click(function() {
// 		var supportId = $(this).attr("id").split("_");
		$.ajax({
			//task-131 url: '<s:url method="dettaglioAggiornaImportoRegolarizzazione"/>',
			url: '<s:url action="regolazioneCarta_dettaglioAggiornaImportoRegolarizzazione"/>',
			type: 'POST',
			data: $(".linkAggiornaRiga").serialize() + "&tripletta=" + $(this).attr("id"),
		    success: function(data)  {
			    $("#dettaglioDocInModificaDiv").html(data);
			}
		});
	});	
	
	
	
	
	$(".linkEliminaRiga").click(function() {
		var supportId = $(this).attr("id").split("_");
		
		if (supportId != null && supportId.length > 0) {
			$("#numeroAnnoCapitolo").val(supportId[1]);
			$("#numeroAnnoMovimento").val(supportId[2]);
			$("#numeroImpegnoDaPassare").val(supportId[3]);
			$("#numeroSubImpegnoDaPassare").val(supportId[4]);
			
			$("#numeroAnnoCapitoloPerElimina").val(supportId[1]);
			$("#numeroAnnoMovimentoPerElimina").val(supportId[2]);
			$("#numeroImpegnoDaPassarePerElimina").val(supportId[3]);
			$("#numeroSubImpegnoDaPassarePerElimina").val(supportId[4]);
			$("#uidTemporaneo").val(supportId[5]);
			
		}
		
		if($("#numeroSubImpegnoDaPassare").val() == 'null'){
			$(".prova").css('display', 'none');
		}else{
			
			$(".prova").css('visibility', 'visible');
// 			$(".prova").css('display', 'block');
		}
	});
	
});	
</script>		

<s:include value="/jsp/include/footer.jsp" />
