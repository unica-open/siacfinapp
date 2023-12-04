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
<s:include value="/jsp/include/javascriptTree.jsp" />
</head>
<body>
	<s:include value="/jsp/include/header.jsp" />
	<hr />
	<div class="container-fluid-banner">
		<a name="A-contenuti" title="A-contenuti"></a>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form id="%{labels.FORM}" action="%{labels.FORM}" method="post">
			<%-- 	<h3><s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/> <s:property value="step1Model.annoImpegno"/>/<s:property value="step1Model.numeroImpegno"/> - <s:property value="step1Model.oggettoImpegno"/> -
					<s:property value="step1Model.importoFormattato"/></h3>	--%>
					
					
					<h3><s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
					
					<s:include value="/jsp/include/actionMessagesErrors.jsp" />
					<s:include value="/jsp/movgest/include/tabsSubimpegno.jsp"/>
					
					<s:if test="oggettoDaPopolareSubimpegno()">
						<s:set var="gestisciForwardAction" value="%{'aggiornaSubimpegno_gestisciForward'}" />
						<s:set var="dettaglioSubPopUpAction" value="%{'aggiornaSubimpegno_dettaglioSubPopUp'}" />
						
						<s:set var="siSalvaAction" value="%{'aggiornaSubimpegno_siSalva'}" />	 
						<s:set var="siProseguiAction" value="%{'aggiornaSubimpegno_siProsegui'}" />
						<s:set var="annullaSubImpegnoAction" value="%{'aggiornaSubimpegno_annullaSubImpegno'}" />	 
						<s:set var="annullaSubAccertamentoAction" value="%{'aggiornaSubimpegno_annullaSubAccertamento'}" />	 
						<s:set var="annullaMovGestSpesaAction" value="%{'aggiornaSubimpegno_annullaMovGestSpesa'}" />	
						<s:set var="eliminaSubImpegnoAction" value="%{'aggiornaSubimpegno_eliminaSubImpegno'}" />	 
						<s:set var="eliminaSubAccertamentoAction" value="%{'aggiornaSubimpegno_eliminaSubAccertamento'}" />
						<s:set var="forzaProseguiAction" value="%{'aggiornaSubimpegno_forzaProsegui'}" />	          
						<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'aggiornaSubimpegno_forzaSalvaPluriennaleAccertamento'}" />	          
						<s:set var="salvaConByPassDodicesimiAction" value="%{'aggiornaSubimpegno_salvaConByPassDodicesimi'}" />	          
					</s:if>
					<s:else>
						<s:set var="gestisciForwardAction" value="%{'aggiornaSubaccertamento_gestisciForward'}" />
						<s:set var="dettaglioSubPopUpAction" value="%{'aggiornaSubaccertamento_dettaglioSubPopUp'}" />	
						
						<s:set var="siSalvaAction" value="%{'aggiornaSubaccertamento_siSalva'}" />	 
						<s:set var="siProseguiAction" value="%{'aggiornaSubaccertamento_siProsegui'}" />
						<s:set var="annullaSubImpegnoAction" value="%{'aggiornaSubaccertamento_annullaSubImpegno'}" />	 
						<s:set var="annullaSubAccertamentoAction" value="%{'aggiornaSubaccertamento_annullaSubAccertamento'}" />	 
						<s:set var="annullaMovGestSpesaAction" value="%{'aggiornaSubaccertamento_annullaMovGestSpesa'}" />	
						<s:set var="eliminaSubImpegnoAction" value="%{'aggiornaSubaccertamento_eliminaSubImpegno'}" />	 
						<s:set var="eliminaSubAccertamentoAction" value="%{'aggiornaSubaccertamento_eliminaSubAccertamento'}" />
						<s:set var="forzaProseguiAction" value="%{'aggiornaSubaccertamento_forzaProsegui'}" />	          
						<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'aggiornaSubaccertamento_forzaSalvaPluriennaleAccertamento'}" />	          
						<s:set var="salvaConByPassDodicesimiAction" value="%{'aggiornaSubaccertamento_salvaConByPassDodicesimi'}" />				
					</s:else>
						                     		 
					<s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
					<h4><s:property value="%{labels.OGGETTO_GENERICO}"/> - Totale: <s:property value="getText('struts.money.format', {totaleSubImpegno})"/> - Disponibile a
						<s:property value="%{labels.OGGETTO_GENERICO_VERBO}"/>: <s:property value="getText('struts.money.format', {disponibilitaSubImpegnare})"/>
					</h4>
					<s:if test="oggettoDaPopolareSubimpegno()">
						<display:table name="sessionScope.RISULTATI_RICERCA_SUBIMPEGNI"  
					                class="table table-hover tab_centered" 
					                summary="riepilogo subimpegni"
					                pagesize="10" 
					               
					                partialList="true"
					                size="resultSize"
					                keepStatus="${status}" clearStatus="${status}"
					                
					                requestURI="aggiornaSubimpegno.do"
					                
									uid="ricercaSubImpegniID">
									
									
<%-- 									<display:table name="sessionScope.RISULTATI_RICERCA_SUBIMPEGNI"  --%>
<%-- 	class="table tab_left table-hover"  --%>
<%-- 	summary="riepilogo impegni"  --%>
<%-- 	pagesize="10" partialList="true" size="resultSize"  --%>
<%-- 	requestURI="elencoImpegni.do" uid="ricercaImpegnoID"  --%>
<%-- 	keepStatus="${status}" clearStatus="${status}"> --%>
									
									
					 <display:column title="Numero" property="numero"/>
					 	<display:column title="Creditore">
					 	 <s:property value ="%{#attr.ricercaSubImpegniID.soggetto.codiceSoggetto}"/> - <s:property value ="%{#attr.ricercaSubImpegniID.soggetto.denominazione}"/>
					 </display:column>
					 <display:column title="Stato" property="statoOperativoMovimentoGestioneSpesa"/>
					 
					 
					<display:column title="Tipo debito SIOPE">
						<s:if test="%{#attr.ricercaSubImpegniID.siopeTipoDebito != null && #attr.ricercaSubImpegniID.siopeTipoDebito.codice != ''}">
							<s:if test="%{#attr.ricercaSubImpegniID.siopeTipoDebito.codice == 'CO'}">
								Commerciale (Con fatture)
							</s:if>
							<s:else>
								Non Commerciale
							</s:else>
						</s:if>
					</display:column>
					
					
					 
					<display:column title="CIG">
					
						<s:if test="%{#attr.ricercaSubImpegniID.cig != null && #attr.ricercaSubImpegniID.cig != ''}">
							 <s:property value ="%{#attr.ricercaSubImpegniID.cig}"/>
						</s:if>
						
						<s:else>
							
							<s:if test="%{#attr.ricercaSubImpegniID.siopeAssenzaMotivazione.descrizione != null && #attr.ricercaSubImpegniID.siopeAssenzaMotivazione.descrizione != ''}">
								<a href="#" data-trigger="hover" rel="popover" title="Motivazione assenza CIG" data-content="<s:property value="%{#attr.ricercaSubImpegniID.siopeAssenzaMotivazione.descrizione}"/>">
					 	    		Motivazione assenza
					 	 		</a>
							</s:if>
						</s:else>
					 	
						 
					</display:column>
					
					
					
				 	<display:column title="CUP" property="cup"/>
					 <display:column title="Provvedimento">
					 <%-- attr.ricercaSubImpegniID.attoAmministrativo.oggetto --%>
					 	 <a href="#" data-trigger="hover" rel="popover" title="OGGETTO" 
					 	    
					 	    data-content="<s:property value="%{#attr.ricercaSubImpegniID.attoAmministrativo.oggetto}"/>">
					 	    <s:property value ="%{#attr.ricercaSubImpegniID.attoAmministrativo.anno}"/>
					 	    /<s:property value ="%{#attr.ricercaSubImpegniID.attoAmministrativo.numero}"/>
					 	    /<s:property value="%{#attr.ricercaSubImpegniID.attoAmministrativo.strutturaAmmContabile.codice}"/>
							/<s:property value="%{#attr.ricercaSubImpegniID.attoAmministrativo.tipoAtto.codice}"/>
					 	    
					 	 </a>
					 </display:column>
					 
					 <%-- SIAC-6929 --%>
					 <display:column title="Blocco Rag.">
					   <s:if test="%{#attr.ricercaSubImpegniID.attoAmministrativo.bloccoRagioneria == null }">
			              	N/A
			              </s:if>  
			              <s:if test="%{#attr.ricercaSubImpegniID.attoAmministrativo.bloccoRagioneria == false}">
			              	NO
			              </s:if> 
			              <s:if test="%{#attr.ricercaSubImpegniID.attoAmministrativo.bloccoRagioneria == true}">
			              	SI
			              </s:if>                        
					</display:column>
					 
					 <display:column title="Importo" property="importoAttuale" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
					 <display:column>
				       <div class="btn-group">
				       		<%-- SIAC-7952 rimuovo .do dalla action --%>
				       		<%-- task-131 <s:url id="aggiornaSubimpegnoUrl" action="gestioneSubimpegno" escapeAmp="false"> --%>
					        <s:url var="aggiornaSubimpegnoUrl" action="gestioneSubimpegno" escapeAmp="false">
					        	<s:param name="idSubImpegno" value="%{#attr.ricercaSubImpegniID.uid}" />
					        	<s:param name="inserimentoSubimpegno" value="false" />
			                </s:url>
							<button class="btn dropdown-toggle" data-toggle="dropdown">
								Azioni <span class="caret"></span>
							</button>
							<ul class="dropdown-menu pull-right">
								<li><a id="linkConsulta_<s:property value="%{#attr.ricercaSubImpegniID_rowNum-1}"/>" href="#consultazioneSub" class="consultaSubPopUp"  data-toggle="modal">consulta</a></li>

								<%-- SIAC-6929 --%>
								<%-- SIAC-7800 soluzione workaround - non riesco a reperire indicazioni del controllo per la SIAC-6929 - ripropongo il controllo degli accertamenti --%>
								<s:if test="isAggiornaAbilitatoImpegno(#attr.ricercaSubImpegniID.statoOperativoMovimentoGestioneSpesa) && ((#attr.ricercaSubImpegniID.attoAmministrativo == null || #attr.ricercaSubImpegniID.attoAmministrativo.bloccoRagioneria != true))">
			                         <%-- 
			                        && !isAbilitatoGestisciImpegnoDecentratoP())
			                        || isAggiornaAbilitatoImpegno(#attr.ricercaSubImpegniID.statoOperativoMovimentoGestioneSpesa)
			                         --%>
										<li><a href="<s:property value="aggiornaSubimpegnoUrl"/>">aggiorna</a></li>
								<%-- </s:if> --%>
									
								</s:if>		
								
								<%-- SIAC-6929 --%>
								<%-- SIAC-7800 soluzione workaround - non riesco a reperire indicazioni del controllo per la SIAC-6929 - ripropongo il controllo degli accertamenti --%>
								<s:if test="isAnnullaAbilitatoImpegno(#attr.ricercaSubImpegniID.statoOperativoMovimentoGestioneSpesa,#attr.ricercaSubImpegniID.disponibilitaImpegnoModifica,#attr.ricercaSubImpegniID.importoAttuale) && ((#attr.ricercaSubImpegniID.attoAmministrativo == null || #attr.ricercaSubImpegniID.attoAmministrativo.bloccoRagioneria != true))">
			                        <%-- 
		                           && !isAbilitatoGestisciImpegnoDecentratoP())
			                        || isAnnullaAbilitatoImpegno(#attr.ricercaSubImpegniID.statoOperativoMovimentoGestioneSpesa)
			                        --%>
			                           <li>
			                         	  <a id="linkAnnulla_<s:property value="%{#attr.ricercaSubImpegniID.uid}"/>_<s:property value="%{#attr.ricercaSubImpegniID.numero.intValue()}"/>_<s:property value="%{#attr.ricercaSubImpegniID.annoMovimento}"/>" href="#msgAnnullaSubImp" data-toggle="modal" class="linkAnnullaSub"> 
			                               annulla
			                              </a>
			                           </li>
	                           <%-- </s:if> --%>

								</s:if>	

								<%-- SIAC-6929 --%>
								<%-- SIAC-7800 soluzione workaround - non riesco a reperire indicazioni del controllo per la SIAC-6929 - ripropongo il controllo degli accertamenti --%>
								<s:if test="isEliminaAbilitato(#attr.ricercaSubImpegniID.statoOperativoMovimentoGestioneSpesa) && ((#attr.ricercaSubImpegniID.attoAmministrativo == null || #attr.ricercaSubImpegniID.attoAmministrativo.bloccoRagioneria != true))">
			                         <%-- 
		                           && !isAbilitatoGestisciImpegnoDecentratoP())
			                        || isEliminaAbilitato(#attr.ricercaSubImpegniID.statoOperativoMovimentoGestioneSpesa)
			                         --%>
			                           <li>
<%-- 			                           	<a id="linkElimina_<s:property value="%{#attr.ricercaSubImpegniID.uid}"/>_<s:property value="%{#attr.ricercaSubImpegniID.numero.intValue()}"/>_<s:property value="%{#attr.ricercaSubImpegniID.numero.intValue()}"/>_<s:property value="%{#attr.ricercaSubImpegniID.annoMovimento}"/>_<s:property value="%{#attr.ricercaSubImpegniID.annoMovimento}"/>" href="#msgEliminaSubImp" data-toggle="modal" class="linkEliminaSub">  --%>
<%-- 			                               elimina --%>
<%-- 			                            </a> --%>
			                            	<a id="linkElimina_<s:property value="%{#attr.ricercaSubImpegniID.uid}"/>_<s:property value="%{#attr.ricercaSubImpegniID.numero.intValue()}"/>_<s:property value="%{#attr.ricercaSubImpegniID.annoMovimento}"/>" href="#msgEliminaSubImp" data-toggle="modal" class="linkEliminaSub">elimina</a>
			                           </li>
	                           <%-- </s:if> --%>

								</s:if>					
							</ul>
						</div>
					 </display:column>             	 
					 </display:table>
					</s:if>
					<s:else>
						<display:table name="listaSubaccertamenti"  
					                class="table table-hover tab_centered" 
					                summary="riepilogo subaccertamenti"
					                pagesize="10" 
					                requestURI=""
									uid="ricercaSubAccertamentiID">
					 <display:column title="Numero" property="numero"/>
					 	<display:column title="Debitore">
					 	 <s:property value ="%{#attr.ricercaSubAccertamentiID.soggetto.codiceSoggetto}"/> - <s:property value ="%{#attr.ricercaSubAccertamentiID.soggetto.denominazione}"/>
					 </display:column>
					 <display:column title="Stato" property="statoOperativoMovimentoGestioneEntrata"/>
					 <display:column title="Provvedimento">
					 	 <a href="#" data-trigger="hover" rel="popover" title="OGGETTO" 
					 	    data-content="<s:property value="%{#attr.ricercaSubAccertamentiID.attoAmministrativo.oggetto}"/>">
					 	    <s:property value ="%{#attr.ricercaSubAccertamentiID.attoAmministrativo.anno}"/>
					 	    /<s:property value ="%{#attr.ricercaSubAccertamentiID.attoAmministrativo.numero}"/>
					 	    /<s:property value="%{#attr.ricercaSubAccertamentiID.attoAmministrativo.strutturaAmmContabile.codice}"/>
							/<s:property value="%{#attr.ricercaSubAccertamentiID.attoAmministrativo.tipoAtto.codice}"/>
					 	 </a>
					 </display:column>
					 <display:column title="Importo" property="importoAttuale" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
					 <%-- SIAC-6929 --%>
					 <display:column title="Blocco Rag.">
					   <s:if test="%{#attr.ricercaSubAccertamentiID.attoAmministrativo.bloccoRagioneria == null }">
			              	N/A
			              </s:if>  
			              <s:if test="%{#attr.ricercaSubAccertamentiID.attoAmministrativo.bloccoRagioneria == false}">
			              	NO
			              </s:if> 
			              <s:if test="%{#attr.ricercaSubAccertamentiID.attoAmministrativo.bloccoRagioneria == true}">
			              	SI
			              </s:if>                        
					</display:column>
				
					 <display:column>					 
				       <div class="btn-group">
				       		<s:url var="aggiornaSubAccUrl" action="gestioneSubaccertamento.do" escapeAmp="false">
					        	<s:param name="idSubImpegno" value="%{#attr.ricercaSubAccertamentiID.uid}" />
					        	<s:param name="inserimentoSubimpegno" value="false" />
			                </s:url>
							<button class="btn dropdown-toggle" data-toggle="dropdown">
								Azioni <span class="caret"></span>
							</button>
							<ul class="dropdown-menu pull-right">
								<li><a id="linkConsulta_<s:property value="%{#attr.ricercaSubAccertamentiID_rowNum-1}"/>" href="#consultazioneSub" class="consultaSubPopUp"  data-toggle="modal">consulta</a></li>
								<s:if test="isAggiornaAbilitatoAccertamento(#attr.ricercaSubAccertamentiID.statoOperativoMovimentoGestioneEntrata) && ((#attr.ricercaSubAccertamentiID.attoAmministrativo.bloccoRagioneria != true) || (#attr.ricercaSubAccertamentiID.attoAmministrativo == null))">
										<li>
											<a href="<s:property value="aggiornaSubAccUrl"/>">aggiorna</a>
										</li>
								</s:if>		
								<s:if test="isAnnullaAbilitatoAccertamento(#attr.ricercaSubAccertamentiID.statoOperativoMovimentoGestioneEntrata) && ((#attr.ricercaSubAccertamentiID.attoAmministrativo.bloccoRagioneria != true) || (#attr.ricercaSubAccertamentiID.attoAmministrativo == null))">		
										<li>		
										   <a id="linkAnnulla_<s:property value="%{#attr.ricercaSubAccertamentiID.uid}"/>_<s:property value="%{#attr.ricercaSubAccertamentiID.numero.intValue()}"/>" href="#msgAnnullaSubAcc" data-toggle="modal" class="linkAnnullaSub">annulla</a>
										</li>
								</s:if>	
								<s:if test="isEliminaAbilitatoAccertamento(#attr.ricercaSubAccertamentiID.statoOperativoMovimentoGestioneEntrata)  &&  ((#attr.ricercaSubAccertamentiID.attoAmministrativo.bloccoRagioneria != true) || (#attr.ricercaSubAccertamentiID.attoAmministrativo == null))">											
										<li>							
											<a id="linkElimina_<s:property value="%{#attr.ricercaSubAccertamentiID.uid}"/>_<s:property value="%{#attr.ricercaSubAccertamentiID.numero.intValue()}"/>" href="#msgEliminaSubAcc" data-toggle="modal" class="linkEliminaSub">elimina</a>
										</li>	
								</s:if>					
							</ul>
						</div>
					 </display:column>             	 
					 </display:table>
					</s:else>
					
					<s:hidden id="uidSubDaEliminare" name="uidSubDaEliminare"/>
					<s:hidden id="uidSubDaAnnullare" name="uidSubDaAnnullare"/>
					<s:hidden id="uidSubDaAggiornare" name="uidSubDaAggiornare"/>
					
					
				    <s:if test="oggettoDaPopolareSubimpegno()">					        
						<s:set var="selezionaProvvedimentoAction" value="%{'aggiornaSubimpegno_selezionaProvvedimento'}" />
						<s:set var="clearRicercaProvvedimentoAction" value="%{'aggiornaSubimpegno_clearRicercaProvvedimento'}" />	          
	            	  	<s:set var="ricercaProvvedimentoAction" value="%{'aggiornaSubimpegno_ricercaProvvedimento'}" />	          
	            	  	<s:set var="eliminaAction" value="%{'aggiornaSubimpegno_elimina'}" />	  
	            		<s:set var="selezionaProvvedimentoInseritoAction" value="%{'aggiornaSubimpegno_selezionaProvvedimentoInserito'}" />	
						<s:set var="inserisciProvvedimentoAction" value="%{'aggiornaSubimpegno_inserisciProvvedimento'}" />		
						<s:set var="clearInserimentoProvvedimentoAction" value="%{'aggiornaSubimpegno_clearInserimentoProvvedimento'}" />	            
					</s:if>
					<s:else>
						<s:set var="selezionaProvvedimentoAction" value="%{'aggiornaSubaccertamento_selezionaProvvedimento'}" />
					    <s:set var="clearRicercaProvvedimentoAction" value="%{'aggiornaSubaccertamento_clearRicercaProvvedimento'}" />	          
	        			<s:set var="ricercaProvvedimentoAction" value="%{'aggiornaSubaccertamento_ricercaProvvedimento'}" />	          
	         			<s:set var="eliminaAction" value="%{'aggiornaSubaccertamento_elimina'}" />	  
	            		<s:set var="selezionaProvvedimentoInseritoAction" value="%{'aggiornaSubaccertamento_selezionaProvvedimentoInserito'}" />	
						<s:set var="inserisciProvvedimentoAction" value="%{'aggiornaSubaccertamento_inserisciProvvedimento'}" />
						<s:set var="clearInserimentoProvvedimentoAction" value="%{'aggiornaSubaccertamento_clearInserimentoProvvedimento'}" />	
					</s:else>
								
					<s:include value="/jsp/movgest/include/modal.jsp" /> 
					
					<%-- ACCERTAMENTO IN STATO D NON DEVE VISUALIZZARE IL BTN --%>
					<s:if test="statoSubDefinitivo()">
					  		
					</s:if> 
					<s:else>
						<%-- <s:if test="!isAbilitatoGestisciImpegnoDecentratoP()" > --%>
						<s:if test="oggettoDaPopolareSubimpegno()">
							<s:if test="!isAbilitatoImpegnoRORdecentratoEFaseBilancioPredisposizioneConsuntivo()">
						   		<p>
									<!-- task-131 <s:submit name="idInserisciSubimpegno" value="%{labels.INSERISCI}" method="inserisciSubimpegno" cssClass="btn" /> -->
									<s:submit name="idInserisciSubimpegno" value="%{labels.INSERISCI}" action="aggiornaSubimpegno_inserisciSubimpegno" cssClass="btn" />
										
								</p>
							</s:if>
						</s:if>
						<s:else>
							<s:if test="!isAbilitatoAccertamentoRORdecentratoEFaseBilancioPredisposizioneConsuntivo()">
						   		<p>
									<!-- task-131 <s:submit name="idInserisciSubimpegno" value="%{labels.INSERISCI}" method="inserisciSubimpegno" cssClass="btn" /> -->
									<s:submit name="idInserisciSubimpegno" value="%{labels.INSERISCI}" action="aggiornaSubaccertamento_inserisciSubimpegno" cssClass="btn" />	
								</p>
							</s:if>
						</s:else>
					    <%-- </s:if>   --%>
					</s:else>
					
					
					<%-- Jira-1584 levare il tasto indietro --%>
<%-- 					<p class="margin-large"> --%>
<%-- 						<s:include value="/jsp/include/indietro.jsp" />  --%>
<%-- 					</p> --%>
				</s:form>
			</div>
		</div>
	</div>
 <script type="text/javascript">
	$(document).ready(function() {
		$(".consultaSubPopUp").click(function() {
			var supportId = $(this).attr("id").split("_");
			$.ajax({
				// task-131 url: '<s:url method="dettaglioSubPopUp"/>',
				url: '<s:url action="%{#dettaglioSubPopUpAction}"/>',
				type: 'POST',
				data: 'uidPerDettaglioSub=' + supportId[1],
			    success: function(data)  {
				    $("#divDettaglioSubPopUp").html(data);
				}
			});
		});	
		//annulla
		$(".linkAnnullaSub").click(function() {
			var supportId = $(this).attr("id").split("_");
			if (supportId != null && supportId.length > 0) {
				$("#uidSubDaAnnullare").val(supportId[1]);
				$("#numeroSubDaAnnullare").val(supportId[2]);
				$("#numeroSubAccDaAnnullare").val(supportId[2]);
			}
		});
		//Elimina
		$(".linkEliminaSub").click(function() {
			var supportId = $(this).attr("id").split("_");
			if (supportId != null && supportId.length > 0) {
				$("#uidSubDaEliminare").val(supportId[1]);
				$("#numeroSubDaEliminare").val(supportId[2]);
				$("#numeroSubAccDaEliminare").val(supportId[2]);
				
			}
		});
	});	
</script>
	