<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<%-- Inclusione head e CSS NUOVO --%>
<s:include value="/jsp/include/head.jsp" />

<%-- Inclusione JavaScript NUOVO --%>
<s:include value="/jsp/include/javascript.jsp" />

</head>

<body>
<s:if test="oggettoDaPopolareImpegno()">
	<s:set var="oggetto" value="%{'Impegno'}" /> 
</s:if>
<s:else>
	<s:set var="oggetto" value="%{'Accertamento'}" /> 
</s:else>
	<s:include value="/jsp/include/header.jsp" />
	<hr />

	<div class="container-fluid-banner">
		<a name="A-contenuti" title="A-contenuti"></a>
	</div>

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:include value="/jsp/include/actionMessagesErrors.jsp" />
				<s:form id="consultaMovimento" method="post">
					<h3><s:property value="movimento.descMovimentoCapital" />o <s:property value="movimento.anno" />/<s:property value="movimento.numero" /> 
					- <s:property value="movimento.descrizione" /> 
					<%--SIAC-7349 07/07/2020 GM - rif. TC Conulta Impegno 15.2 --%>
					- <s:property value="%{movimento.descrizioneComponente}" />
					<%--END SIAC-7349--%>
					- <s:property value="getText('struts.money.format', {movimento.importo})" /> ( <s:property value="movimento.statoOperativo" /> dal <s:property value="%{movimento.dataStatoOperativo}" /> ) 
					<%-- <s:if test="movimento.impegno"><s:if test="isImpegnoSdf()"> - SENZA DISPONIBILITA' DI FONDI</s:if></s:if> --%>
					</h3>
					<s:hidden id="annoMovimentoInConsultazione" name="movimento.anno"/>
					<s:hidden id="numeroMovimentoInConsultazione" name="movimento.numero"/>
					<ul class="nav nav-tabs">
						<li class="active"><a href="#accertamento" data-toggle="tab"><s:property value="movimento.descMovimentoCapital" />o</a></li>
						<li><a href="#sub" data-toggle="tab">Sub<s:property value="movimento.descMovimentoLower" />i</a></li>
						<li><a href="#modifiche" data-toggle="tab">Modifiche</a></li>
						<s:if test="movimento.impegno">
							<li><a href="#accertamenti" id="linkAccertamenti" data-toggle="tab">Accertamenti</a></li>
						</s:if>
						<s:if test="!movimento.impegno">
							<li><a id="linkImpegni" href="#impegni" data-toggle="tab">Impegni</a></li>
						</s:if>
					<!-- task-93 -->
						<li><a href="#mutui" data-toggle="tab">Mutui</a></li>
					</ul>

					<div class="tab-content">
						<div class="tab-pane active" id="accertamento">

							<h4 class="step-pane">Data inserimento: <s:property value="%{movimento.dataInserimento}" /> - Data ultima modifica: <s:property value="%{movimento.dataModifica}" />
							</h4>

							<dl class="dl-horizontal">
                              <div class="boxOrSpan2">
	                              <div class="boxOrInLeft">
	                              <p><s:property value="movimento.descMovimentoCapital" />o</p>
		                              <ul  class="htmlelt">
		                              
		                              <li>
										<dfn>Inserito</dfn>
										<dl>il <s:property value="%{movimento.dataInserimento}" /> da <s:property value="movimento.utenteCreazione" />&nbsp;</dl>
									  </li>
									  
									  <li>
										<dfn>Aggiornato</dfn>
										<dl>il <s:property value="%{movimento.dataModifica}" /> da <s:property value="movimento.utenteModifica" />&nbsp;</dl>
									  </li>	
		
		                              <li> 
										<dfn>Stato</dfn>
										<dl><s:property value="movimento.statoOperativo" /> dal <s:property value="%{movimento.dataStatoOperativo}" />&nbsp;
										<span class="guidata"><s:property value="movimento.prenotatoString" /></span>
										</dl>
		                              </li>
		                              
		                              <s:if test="movimento.impegno"> 
		                              	
		                              	<s:if test="isEnteAbilitatoParereFinanziario()">
			                              	<li> 
				                              	<dfn>Vistato</dfn>
												<dl><s:property value="movimento.parereFinanziario" />&nbsp; 
      											
      											<s:if test="movimento.parereFinanziarioDataModifica != null">
							        					il:  <s:property value="%{movimento.parereFinanziarioDataModifica}"/>
							        					-
							        					da:  <s:property value="movimento.parereFinanziarioLoginOperazione"/> )
      											</s:if>
      											
      											</dl>
												
											</li>	
										</s:if>	
									 
										 <li>
											<dfn>Tipo impegno</dfn>
											<dl><s:property value="movimento.tipo" />&nbsp;
											<s:if test="movimento.isFrazionabileValorizzato()">
											 	<span class="guidata"><s:property value="movimento.frazionabileString" /></span>
											</s:if>
											</dl>
										 </li>
										 
									 </s:if>
		
		
		                                <li>
											<dfn>Transazione elementare</dfn>
											<dl><s:property value="movimento.codificaTransazioneElementare" escapeHtml="false" />&nbsp;</dl>
		                                </li> 
		                                
		                                <%-- SIAC-6247 & SIAC-6268 --%>
		                                <li>
											<dfn>Titolo giuridico</dfn>
											<dl><s:property value="movimento.titoloGiuridico" escapeHtml="false" />&nbsp;</dl>
		                                </li>
		                                <s:if test="movimento.tipoMovimento == 0">
			                                <li>
												<dfn>Tipo Tracciabilita</dfn>
												<dl><s:property value="movimento.tipoTracciabilita" escapeHtml="false" />&nbsp;</dl>
			                                </li>
			                                <li>
												<dfn>Voce Tracciabilita</dfn>
												<dl><s:property value="movimento.voceTracciabilita" escapeHtml="false" />&nbsp;</dl>
			                                </li>
			                                <li>
												<dfn>Classificazione FSC</dfn>
												<dl><s:property value="movimento.classificazioneFSC" escapeHtml="false" />&nbsp;</dl>
			                                </li>
		                                </s:if>
		                                
		                                <li>
											<dfn>Importo</dfn>
											<dl><s:property value="getText('struts.money.format', {movimento.importo})" />&nbsp; (iniziale: <s:property value="getText('struts.money.format', {movimento.importoIniziale})" />)
											<s:if test="%{dettaglioImporti.totModProv != null && dettaglioImporti.totModProv.longValue() > 0 }">
											di cui mod. provv <s:property value="getText('struts.money.format', {dettaglioImporti.totModProv})" />
											</s:if>
											</dl>
										</li>	
										
		
		                               	<li> 
											<dfn>Scadenza</dfn>
											<dl><s:property value="%{movimento.dataScadenza}" />&nbsp;</dl>
										</li>
										
										<%-- SIAC-7349 gestione componenti su impegni --%>

										<li>	
											<dfn>Codice verbale</dfn>
											<dl><s:property value="movimento.codiceVerbale" />&nbsp;</dl>
										</li>
										<li>	
											<dfn>Da riaccertamento</dfn>
											<dl><s:property value="movimento.daRiaccertamento" />&nbsp;</dl>
										</li>
										
										<%-- da verificare se queste informazioni si devono vedere solo per l'utente con azioni ROR e ROR decentrato oppure per tutti --%>
										<%-- inizio SIAC-6997 --%>
										<li>	
											<dfn> Da reimputazione in corso d&rsquo;anno</dfn>
											<dl><s:property value="movimento.daReanno" />&nbsp;</dl>
										</li>
										
										<li>
											
											<dfn><a id="linkConsultaModificheStrutturaCompetente" class="tooltip-test" title="Visualizza storico strutture" href="#modConsultaModificheStrutturaCompetente" data-toggle="modal"><i class="icon-info-sign"><span class="nascosto">Visualizza storico strutture</span></i></a> Struttura Competente</dfn>
											<dl><s:property value="movimento.strutturaCompetente" />&nbsp;</dl>

										</li>
										
										<%-- fine SIAC-6997 --%>
										
									<s:if test="movimento.impegno">
										<li>	
											<dfn>Soggetto a DURC</dfn>
											<dl><s:property value="movimento.soggettoDurc" />&nbsp;</dl>
										</li>
									</s:if>
										
										
										<s:if test="!movimento.impegno">
											<li>
												<dfn>&Egrave; prevista fattura</dfn>
												<dl><s:property value="movimento.flagFattura" />&nbsp;</dl>
											</li>

											<li>
												<dfn>&Egrave; previsto corrispettivo</dfn>
												<dl><s:property value="movimento.flagCorrispettivo" />&nbsp;</dl>
											</li>
										</s:if>
										
									<!-- PROGETTO -->	
									<li>
										<dfn>Progetto / Iniziativa</dfn>
										<dl><s:property value="movimento.progetto" />&nbsp;</dl>
									</li>
									
									<%-- PER ACCERTAMENTO Rilevante Co.Ge. GSA va qui --%>
									<s:if test="!movimento.impegno">
										<s:if test="isEnteAbilitatoGestioneGsa()">
											<li>
												<dfn>Rilevante Co.Ge. GSA</dfn>
												<dl><s:property value="movimento.flagAttivaGsa" />&nbsp;</dl>
											</li>
										</s:if>
									</s:if>
										
									<%-- il  cig e cup visibili solo se impegno  --%>	
									<s:if test="movimento.impegno">
									
										<li>
											<dfn>Tipo debito siope</dfn>
											<dl><s:property value="movimento.tipoDebitoSiope" />&nbsp;</dl>
										</li>
									
										<s:if test="%{movimento.cig != null && movimento.cig != ''}">
											 <li>
												<dfn>CIG</dfn>
												<dl><s:property value="movimento.cig" />&nbsp;</dl>
											</li>
										</s:if>
									    <s:else>
									    	<li>
											<dfn>Motivazione assenza CIG</dfn>
											<dl><s:property value="movimento.motivazioneAssenzaCig" />&nbsp;
											<s:if test="%{movimento.motivazioneAssenzaCig != null && movimento.motivazioneAssenzaCig == 'CIG in corso di definizione'}">
												<font color="red">(non liquidabile per assenza CIG)</font>&nbsp;
											</s:if>
											</dl>
											</li>
									    </s:else>
										
										<li>
											<dfn>CUP</dfn>
											<dl><s:property value="movimento.cup" />&nbsp;</dl>
										</li>
										
										<%-- PER IMPEGNO Rilevante Co.Ge. GSA va qui --%>
										<s:if test="isEnteAbilitatoGestioneGsa()">
											<li>
												<dfn>Rilevante Co.Ge. GSA</dfn>
												<dl><s:property value="movimento.flagAttivaGsa" />&nbsp;</dl>
											</li>
										</s:if>
										
									</s:if>
									
									<s:if test="%{movimento.annoScritturaEconomicoPatrimoniale != null && movimento.annoScritturaEconomicoPatrimoniale != ''}">
										<li>
											<dfn>Compet. econ. patr. nell'anno: </dfn>
											<dl><s:property value="movimento.annoScritturaEconomicoPatrimoniale" />&nbsp;</dl>
										</li>
									</s:if>
									
									</ul>
									</div>
									
									
										<div class="boxOrInRight">
			                              
			                              <s:include value="/jsp/movgest/include/consultaMovimentoCapitolo.jsp" />
			                            
			                              <p>Soggetto</p>
										  <ul class="htmlelt">
			                                     
			                              <s:if test="%{movimento.soggetto.codice != null && movimento.soggetto.codice != ''}">
				                              <li> 
												<dfn>Codice</dfn>
												<dl><s:property value="movimento.soggetto.codice" />&nbsp;</dl>
											  </li>
				                              
				                              <li> 
												<dfn>Codice Fiscale</dfn>
												<dl><s:property value="movimento.soggetto.codiceFiscale" />&nbsp;</dl>
											  </li>
											  
											  <li> 
												<dfn>P.IVA</dfn>
												<dl><s:property value="movimento.soggetto.partitaIva" />&nbsp;</dl>
											  </li>
											  
											  <li> 
												<dfn>Ragione Sociale</dfn>
												<dl><s:property value="movimento.soggetto.denominazione" />&nbsp;</dl>
											  </li>
										  </s:if>
										  <s:else>
										   	  <li> 
												<dfn>Classe soggetto</dfn>
												<dl><s:property value="movimento.soggetto.classeSoggettoCodice" /> - <s:property value="movimento.soggetto.classeSoggettoDescrizione" />&nbsp;</dl>
											  </li>
										  </s:else> 
			                              
			                              </ul>
			                            
			                            </div>
			                            <%-- SIAC-6865 --%>
			                            <s:if test="movimento.impegno && movimento.aggiudicazioneDaPrenotazione">
											<div class="boxOrInRight">
												<p> Riduzione e contestuale impegno </p>	
												<ul  class="htmlelt">
													<li>	
														<%-- SIAC-7808 --%>
														<dfn>Provvedimento impegno origine</dfn>
														<dl><a href="#" data-trigger="hover" rel="popover" title="Oggetto" data-content="<s:property value="%{movimento.provvedimentoAggiudicazione.oggetto}"/>">
															<s:property value="%{movimento.provvedimentoAggiudicazione.anno}"/>/
															<s:property value="%{movimento.provvedimentoAggiudicazione.numero}"/>/
															<s:property value="%{movimento.provvedimentoAggiudicazione.strutturaAmmContabile.codice}"/>/
															<s:property value="%{movimento.provvedimentoAggiudicazione.tipoAtto.codice}"/>
														</a>
													</li>
													
													<li>	
														<dfn>Impegno prenotazione origine</dfn>
														<dl><a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="<s:property value="%{movimento.impegnoPrenotazioneOrigine.descrizione}"/>">
															<s:property value="%{movimento.impegnoPrenotazioneOrigine.annoMovimento}"/>/
															<s:property value="%{movimento.numeroPrenotazioneOrigine}"/>
														</a>
													</li>
													
												</ul>
											</div>
										</s:if>
									
								</div>	
								
                             	<%-- inizio SIAC-6997 - modale consulta modifiche provvedimento--%>
								<div id="modConsultaModificheStrutturaCompetente" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaProvLabel" aria-hidden="true">
								    <div class="modal-body">
							 		<s:include value="/jsp/movgest/include/consultaModificheStrutturaCompetente.jsp" />
								    </div>   
								</div>
							   <%-- fine SIAC-6997 - modale modifiche --%>
							   
							   
	                             <div class="boxOrSpan2">
								    
								    <s:if test="movimento.impegno">
								    	<s:include value="/jsp/movgest/include/consultaImpegnoRiepilogo.jsp" />
								    </s:if>
								    <s:if test="!movimento.impegno">
								    	<s:include value="/jsp/movgest/include/consultaAccertamentoRiepilogo.jsp" />
								    </s:if>
									   
									<div class="boxOrInRight">
			                              <p>
			                              	Provvedimento <a id="linkConsultaModificheProvvedimento" class="tooltip-test" title="Visualizza storico provvedimenti" href="#modConsultaModificheProvvedimento" data-toggle="modal">
			                              	<i class="icon-info-sign">&nbsp;<span class="nascosto">Visualizza storico provvedimenti</span></i></a> 
			                              </p>
			                              <ul  class="htmlelt">
			                              
			                                 <s:if test="movimento.provvedimento.tipo != null">
			                                 	<li> 
													<dfn>Tipo</dfn>
													<dl><s:property value="movimento.provvedimento.tipo" />&nbsp;</dl>
												</li>	
												<li> 
												<dfn>Anno</dfn>
												<dl><s:property value="movimento.provvedimento.anno" />&nbsp;</dl>
												</li>
												<li> 
													<dfn>Numero</dfn>
													<dl><s:property value="movimento.provvedimento.numero" />&nbsp;</dl>
												</li>	
												<li> 
													<dfn>Oggetto</dfn>
													<dl><s:property value="movimento.provvedimento.oggetto" />&nbsp;</dl>
												</li>	
												<li> 
													<dfn>Stato</dfn>
													<dl><s:property value="movimento.provvedimento.stato" />&nbsp;</dl>
												</li>
												<li> 
													<dfn>Blocco Rag.</dfn>
													<dl><s:property value="movimento.provvedimento.bloccoRagioneria" />&nbsp;</dl>
												</li>
													
												<s:if test="movimento.impegno">
													<li>
														<dfn>Struttura</dfn>
														<dl><s:property value="movimento.provvedimento.strutturaCompleta" />&nbsp;</dl>
													</li>
												</s:if>
											 </s:if>
			                              </ul>
			                        </div>   
			                         
			                        
							     </div>		 
							
							
								<%-- SIAC-6247 --%>
								<div class="boxOrSpan2">
									<div class="boxOrInLeft">
										
								  	</div>
									
									
									<div class="boxOrInRight"></div>
									
								</div>
								<%-- /SIAC-6247 --%>
								
	                             
								</dl>
							</div>
							
									
						<!-- SUBIMPEGNI -->			
						<div class="tab-pane" id="sub">
							<h4 class="step-pane">Sub<s:property value="movimento.descMovimentoLower" />i - Totale: <s:property value="getText('struts.money.format', {movimento.totaleSub})" /> - Disponibile a sub<s:property value="movimento.descDispMovimento" />are: <s:property value="getText('struts.money.format', {movimento.disponibilitaSub})" /></h4>

							<display:table name="listaSub" class="table table-hover tab_centered" summary="riepilogo sub" uid="subID">
								<display:column title="Numero">
									<a id="linkConsulta_<s:property value="%{#attr.subID_rowNum-1}"/>" href="#consultazioneSub" class="consultaSubPopup" data-toggle="modal">
										<s:property value="%{#attr.subID.numero}" />
									</a>
								</display:column>
								<display:column title="Creditore">
									<s:property value="%{#attr.subID.soggetto.codice}" />&nbsp;<s:property value="%{#attr.subID.soggetto.denominazione}" />
								</display:column>
								<display:column title="Stato" property="statoOperativo" />
								<display:column title="Provvedimento" >
								<s:if test="%{#attr.subID.provvedimento.tipo != null}">
									<a href="#" data-trigger="hover" rel="popover" title="OGGETTO" data-content="${attr.subID.provvedimento.oggetto}">
										<s:property value="%{#attr.subID.provvedimento.anno}" />/<s:property value="%{#attr.subID.provvedimento.numero}" />
										/<s:property value="%{#attr.subID.provvedimento.struttura}" />
										/<s:property value="%{#attr.subID.provvedimento.tipo}" />
									</a>
									
									<a id="linkConsultaModificheProvvedimentoSub_<s:property value="%{#attr.subID.idMovimento}"/>" class="tooltip-test" title="Visualizza storico provvedimenti" href="#modConsultaModificheProvvedimento" data-toggle="modal">
			                              	<i class="icon-info-sign">&nbsp;<span class="nascosto">Visualizza storico provvedimenti</span></i>
			                       	</a>
								</s:if>
								</display:column>
								<%-- SIAC-6929 --%>
								<display:column title="Blocco Ragioneria" property="bloccoRagioneria" />
								
								<display:column title="Tipo debito siope" property="tipoDebitoSiope" />
								
								<display:column title="CIG">
									<s:if test="%{#attr.subID.cig != null && #attr.subID.cig != ''}">
										 <s:property value ="%{#attr.subID.cig}"/>
									</s:if>
									
									<s:else>
										<s:if test="%{#attr.subID.motivazioneAssenzaCig != null && #attr.subID.motivazioneAssenzaCig != ''}">
											<a href="#" data-trigger="hover" rel="popover" title="Motivazione assenza CIG"  data-content="<s:property value="%{#attr.subID.motivazioneAssenzaCig}"/>">
								 	    		 Motivazione assenza
								 	 		</a>
								 		</s:if> 	
									</s:else>
									
								</display:column>
								
								<display:column title="Importo" property="importo"   decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
								
							</display:table>
						</div>
						<%-- fine SUBIMPEGNI --%>	

                        <!-- MODIFICHE -->
						<div class="tab-pane" id="modifiche">
							<h4 class="step-pane" >Modifiche</h4>

							<display:table name="listaModifiche" class="table tab_left table-hover" summary="riepilogo modifiche" uid="modMovimentoID">
								<display:column title="Numero">
									<a id="linkConsulta_<s:property value="%{#attr.modMovimentoID_rowNum}"/>" href="#consultazioneMod" class="consultaModPopup" data-toggle="modal">
										<s:property value="%{#attr.modMovimentoID.numero}" />
									</a>
								</display:column>
								<display:column title="Sub" property="numSub" />   
								<display:column title="Descrizione" property="descrizione"/> 
								<display:column title="Motivo" property="motivo" />  
								
								<!-- SIAC-8834 -->
								<display:column title="Impegno contestuale" >
									<s:if test="%{#attr.modMovimentoID.impegnoAssociato != null}">
										<s:property value="%{#attr.modMovimentoID.impegnoAssociato.annoMovimento}" />/
 										<s:property value="%{#attr.modMovimentoID.impegnoAssociato.numero.intValue()}" />
 									</s:if>
								</display:column>
								
								<display:column title="Stato" property="statoOperativo" />
								<%-- SIAC-6929 --%> 
								<display:column title="Blocco Ragioneria" property="bloccoRagioneria" />  
 								
 								<display:column title="Provvedimento" >
 									<s:if test="%{#attr.modMovimentoID.provvedimento.tipo != null}">
										<a href="#" data-trigger="hover" rel="popover" title="OGGETTO" data-content="${attr.modMovimentoID.provvedimento.oggetto}"> 
											<s:property value="%{#attr.modMovimentoID.provvedimento.anno}" />/
											<s:property value="%{#attr.modMovimentoID.provvedimento.numero}" />/
											<s:property value="%{#attr.modMovimentoID.provvedimento.tipo}"/>/
											<s:property value="%{#attr.modMovimentoID.provvedimento.struttura}"/>(
											<s:property value="%{#attr.modMovimentoID.provvedimento.stato.charAt(0)}"/>)
										</a>
										
										
									</s:if>
								</display:column>
								<display:column class="tab_Right" title="Importo" property="importo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
							</display:table>
						</div>
						<!-- fine MODIFICHE -->
						
						<!-- task-93 Inizio Mutui  -->
						<div class="tab-pane" id="mutui">
							<h4 class="step-pane" >Mutui</h4>

							<display:table name="elencoMutuiAssociati" class="table tab_left table-hover" summary="riepilogo mutui" uid="modMovimentoID">
								
								<display:column title="Numero"> 
									<s:property value="%{#attr.modMovimentoID.mutuo.numero}"/>
								</display:column>
								<display:column title="Tipo Tasso" >   
									<s:property value="%{#attr.modMovimentoID.mutuo.tipoTasso}"/>
								</display:column>
								<display:column title="Stato" > 
									<s:property value="%{#attr.modMovimentoID.mutuo.statoMutuo}"/>
								</display:column>
								<display:column title="Periodo" >
									<s:property value="%{#attr.modMovimentoID.mutuo.periodoRimborso.descrizione}"/>
								</display:column>  
								<display:column title="Tasso Interesse"  decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"> 
									<s:property value="%{#attr.modMovimentoID.mutuo.tassoInteresse}" />
								</display:column>
								<display:column title="Euribor"  decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro">
									<s:property value="%{#attr.modMovimentoID.mutuo.tassoInteresseEuribor}" />
								</display:column>
								<display:column title="Spread"  decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"> 
									<s:property value="%{#attr.modMovimentoID.mutuo.tassoInteresseSpread}"/>
								</display:column>
								<display:column title="Istituto di credito">
									<s:property   value="%{#attr.modMovimentoID.mutuo.soggetto.codice}"/> - <s:property   value="%{#attr.modMovimentoID.mutuo.soggetto.denominazione}"/>
								</display:column> 		
								<display:column title="Importo Mutuo"  decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" >
									<s:property   value="%{#attr.modMovimentoID.mutuo.sommaMutuataIniziale}"/>
								</display:column>
							</display:table>
						</div>
						<!-- fine task-93 Fine Mutui -->
						
						
					
						
						<!-- ACCERTAMENTI -->
						<div class="tab-pane" id="accertamenti">
						  <h4 class="step-pane">Accertamenti</h4>
						  
						    <display:table name="listaVincoliImpegno" class="table tab_left table-hover" summary="riepilogo " uid="accertamentiID">
						  
								 <display:column title="Anno" property="accertamento.annoMovimento" />
						         <display:column title="Numero" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterNumeroIntero">
						         	<s:if test="%{#attr.accertamentiID.accertamento != null}">
										<s:property  value="%{#attr.accertamentiID.accertamento.numero.intValue()}" />		
									</s:if>
									<s:elseif test="%{#attr.accertamentiID.avanzoVincolo != null}">
										<s:property  value="%{#attr.accertamentiID.avanzoVincolo.tipoAvanzovincolo.codice}" />
									</s:elseif>
						         </display:column>
						         <display:column title="Descrizione" >
							         <s:if test="%{#attr.accertamentiID.accertamento != null}">
										<s:property  value="%{#attr.accertamentiID.accertamento.descrizione}" />		       
									</s:if>
									<s:elseif test="%{#attr.accertamentiID.avanzoVincolo != null}">
										<s:property  value="%{#attr.accertamentiID.avanzoVincolo.tipoAvanzovincolo.descrizione}" />
									</s:elseif>
								</display:column>
						         <display:column title="Capitolo">
						          <s:if test="%{#attr.accertamentiID.accertamento != null}">
							           <s:property value="%{#attr.accertamentiID.accertamento.capitoloEntrataGestione.annoCapitolo}" />/
							           <s:property value="%{#attr.accertamentiID.accertamento.capitoloEntrataGestione.numeroCapitolo}" />/
							           <s:property value="%{#attr.accertamentiID.accertamento.capitoloEntrataGestione.numeroArticolo}" />/
							           <s:property value="%{#attr.accertamentiID.accertamento.capitoloEntrataGestione.numeroUEB}" />
						           </s:if>
						         </display:column>
						         <display:column title="Importo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" >
						          	<s:if test="%{#attr.accertamentiID.accertamento != null}">
										<s:property  value="%{#attr.accertamentiID.accertamento.importoAttuale}" />		       
									</s:if>
									<s:elseif test="%{#attr.accertamentiID.avanzoVincolo != null}">
										<s:property  value="%{#attr.accertamentiID.avanzoVincolo.avavImportoMassimale}" />
									</s:elseif>
						         </display:column>
						         <display:column title="Importo Vincolo" property="importo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"  />
						         <display:column title="-di cui pending per reimp. provv." property="diCuiPending" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"  />
						    </display:table>
										
							<div class="Border_line"></div>
							
							
							<h4 class="step-pane">Accertamenti da legame storicizzato</h4>
							<div id="divLegameStoricoDaImpegno">
							   Caricamento in corso..
							</div>
							
							<div class="Border_line"></div>
							
							<%-- SIAC-8650 --%>
							<h4>Vincoli originari</h4>
							<div class="Border_line"></div>
							<div id="vincoliOriginariAccertamento">
								<display:table name="listaVincoliImpegnoOriginari" class="table tab_left table-hover" summary="riepilogo vincoli originari" uid="impegniID">
							  
									<display:column title="Anno">
							         	<s:if test="%{#attr.impegniID.accertamento != null}">
											<s:property  value="%{#attr.impegniID.accertamento.annoMovimento}" />		
										</s:if>
									</display:column>
							        <display:column title="Numero" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterNumeroIntero">
							         	<s:if test="%{#attr.impegniID.accertamento != null}">
											<s:property  value="%{#attr.impegniID.accertamento.numero.intValue()}" />		
										</s:if>
										<s:elseif test="%{#attr.impegniID.avanzoVincolo != null}">
											<s:property  value="%{#attr.impegniID.avanzoVincolo.tipoAvanzovincolo.codice}" />
										</s:elseif>
							        </display:column>
							        <display:column title="Descrizione" >
								        <s:if test="%{#attr.impegniID.accertamento != null}">
											<s:property  value="%{#attr.impegniID.accertamento.descrizione}" />		       
										</s:if>
										<s:elseif test="%{#attr.impegniID.avanzoVincolo != null}">
											<s:property  value="%{#attr.impegniID.avanzoVincolo.tipoAvanzovincolo.descrizione}" />
										</s:elseif>
									</display:column>
									
									<display:column title="Importo Vincolo" property="importo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"  />

									<display:column title="Bilancio" property="accertamento.bilancio.anno" />

									<display:column title="Impegno">
										<s:if test="%{primoImpCatena != null}">
											<s:property value="%{primoImpCatena.annoMovimento}"/> /<s:property value="%{primoImpCatena.numero.intValue()}"/>
										</s:if>
									</display:column>
									
									<display:column title="Descrizione orig." >
										<s:if test="%{primoImpCatena != null}">
											<s:property  value="%{primoImpCatena.descrizione}" />		       
										</s:if>
									</display:column>

									
						        	<display:column title="Sommatoria modifiche reimputazione e reanno" property="sommaImportiModReimpReanno" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"  />

							   
							    </display:table>		
							</div>
							<div class="Border_line"></div>
							
						</div>
						<!-- fine ACCERTAMENTI -->
						
						<!-- IMPEGNI -->
						<div class="tab-pane" id="impegni">
							<h4 class="step-pane">Impegni</h4>
							  
						    <display:table name="listaVincoliAccertamento" class="table tab_left table-hover" summary="riepilogo " uid="impegniID">
						  
								 <display:column title="Anno" property="impegno.annoMovimento" />
						         <display:column title="Numero" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterNumeroIntero">
									<s:property  value="%{#attr.impegniID.impegno.numero.intValue()}" />		
						         </display:column>
						         
				         		<display:column title="Soggetto">
						              <s:if test="%{#attr.impegniID.impegno.classeSoggetto == null}">
						              	<s:property value="%{#attr.impegniID.impegno.soggetto.codiceSoggetto}"/>-<s:property value="%{#attr.impegniID.impegno.soggetto.denominazione}"/>
						              </s:if>                   
						              <s:else>
						             	 <s:property value="%{#attr.impegniID.impegno.classeSoggetto.codice}"/>-<s:property value="%{#attr.impegniID.impegno.classeSoggetto.descrizione}"/>
						              </s:else>
								</display:column>
						         
						        <display:column title="Stato">
						        	 <s:property value="%{#attr.impegniID.impegno.descrizioneStatoOperativoMovimentoGestioneSpesa}"/>
						        </display:column>
						         
						         <display:column title="Provvedimento">
									<a href="#" data-trigger="hover" rel="popover" title="Oggetto" data-content="<s:property value="%{#attr.impegniID.impegno.attoAmministrativo.oggetto}"/>">
										<s:property value="%{#attr.impegniID.impegno.attoAmministrativo.anno}"/>/
										<s:property value="%{#attr.impegniID.impegno.attoAmministrativo.numero}"/>/
										<s:property value="%{#attr.impegniID.impegno.attoAmministrativo.strutturaAmmContabile.codice}"/>/
										<s:property value="%{#attr.impegniID.impegno.attoAmministrativo.tipoAtto.codice}"/>
									</a>
								</display:column>
						         
						         <display:column title="Importo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" >
									<s:property  value="%{#attr.impegniID.impegno.importoAttuale}" />		       
						         </display:column>
						         <display:column title="Importo Vincolo" property="importo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"  />
						   
				   		 	     <display:footer>
									<tr> 
										<th>Totale vincoli</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th class="tab_Left"><s:property value="getText('struts.money.format', {totVincoliAccertamento})" /></th>
									</tr>  
									<tr> 
										<th>Quota non ancora vincolata</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th class="tab_Left"><s:property value="getText('struts.money.format', {quotaNonVincolataAccertamento})" /></th>
									</tr>  
							  </display:footer>
						   
						   
						    </display:table>
							
						
							<div class="Border_line"></div>
							
							<h4 class="step-pane">Impegni da legame storicizzato</h4>
							<div id="divLegameStoricoDaAccertamento">
							   Caricamento in corso..
							</div>
							<div class="Border_line"></div>
							
							<%-- SIAC-8650 --%>
							<h4>Vincoli originari</h4>
							<div class="Border_line"></div>
							<display:table name="listaVincoliAccertamentoOriginari" class="table tab_left table-hover" summary="riepilogo vincoli originari" uid="accertamentiID">
						  
								<display:column title="Anno">
						         	<s:if test="%{#attr.accertamentiID.impegno != null}">
										<s:property  value="%{#attr.accertamentiID.impegno.annoMovimento}" />		
									</s:if>
								</display:column>
						        <display:column title="Numero" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterNumeroIntero">
									<s:property value="%{#attr.accertamentiID.impegno.numero.intValue()}" />		
						        </display:column>
						         
					         	<display:column title="Descrizione" >
									<s:property  value="%{#attr.accertamentiID.impegno.descrizione}" />		       
								</display:column>
						         
						        <display:column title="Importo Vincolo" property="importo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"  />
						         	
					 			<display:column title="Bilancio" property="impegno.bilancio.anno" />
								 						         
								<display:column title="Accertamento orig.">
									<s:if test="%{primoAccCatena != null}">
										<s:property value="%{primoAccCatena.annoMovimento}"/> /<s:property value="%{primoAccCatena.numero.intValue()}"/>
									</s:if>
									<s:else>
										-						
									</s:else>
								</display:column>
								
								<display:column title="Descrizione orig." >
									<s:if test="%{primoAccCatena != null}">
										<s:property  value="%{primoAccCatena.descrizione}" />		       
									</s:if>
									<s:else>
										-						
									</s:else>
								</display:column>

								<display:column title="Sommatoria modifiche reimputazione e reanno" property="sommaImportiModReimpReanno" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"  />
						   
								
						    </display:table>
							<div class="Border_line"></div>
						</div>
						<!-- fine IMPEGNI -->
	
					</div>

				    <!--/modale modifiche -->
					<div id="consultazioneSub" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="consulta" aria-hidden="true">
						<div id="divDettaglioSubPopup"></div>
					</div>
					<div id="consultazioneMod" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="consulta" aria-hidden="true">
						<div id="divDettaglioModPopup"></div>
					</div>
					
					<%--modale consulta modifiche provvedimento--%>
					<div id="modConsultaModificheProvvedimento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaProvLabel" aria-hidden="true">
					    <div class="modal-body">
						    <s:include value="/jsp/movgest/include/consultaModificheProvvedimento.jsp" />
					        </div>   
				    </div>

					<!--   PROVA COLOMBO  -->
					<div id="divCapitoloPopup" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="capitoloTabLabel" aria-hidden="true">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
						<h3 id="myModalLabel2">Dettagli del capitolo</h3>
					</div>
					<div class="modal-body">
						<dl class="dl-horizontal">
						<dt>Numero</dt>
						<dd><s:property value="movimento.capitolo.anno"/> / <s:property value="movimento.capitolo.numCapitolo"/> / <s:property value="movimento.capitolo.articolo"/> / <s:property value="movimento.capitolo.ueb"/> - <s:property value="movimento.capitolo.descrizione" /> - <s:property value="movimento.capitolo.descrizioneStrutturaAmministrativa" />&nbsp;</dd>
						<dt>Tipo finanziamento</dt>
						<dd><s:property value="movimento.capitolo.tipoFinanziamento" />&nbsp;</dd>
						<dt>Piano dei conti finanziario</dt>
						<dd><s:property value="movimento.capitolo.codicePdcFinanziario" default=" "/> - <s:property value="movimento.capitolo.descrizionePdcFinanziario" default=" "/>&nbsp;</dd>
						</dl>
						<table class="table table-hover table-bordered">
						<tr>
							<th>&nbsp;</th>
							<s:iterator value="movimento.capitolo.importiCapitolo">
								<th scope="col" class="text-center"><s:property value="annoCompetenza" /></th>
							</s:iterator>
						</tr>
						<tr>
							<th>Stanziamento</th>
							<s:iterator value="movimento.capitolo.importiCapitolo">
								<td><s:property value="getText('struts.money.format', {competenza})" /></td>
							</s:iterator>       
						</tr>
						<tr>
							<th>Disponibile</th>
							<s:if test="oggettoDaPopolareImpegno()">
								<td><s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno1})" /></td>
								<td><s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno2})" /></td>
								<td><s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno3})" /></td>
							</s:if>
							<s:else>
								<td><s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno1})" /></td>
								<td><s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno2})" /></td>
								<td><s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno3})" /></td>
							</s:else>
						</tr>
						</table>
					</div>
					</div>  

					<p><s:include value="/jsp/include/indietro.jsp" /></p>

				</s:form>
			</div>
		</div>
	</div>
	
	
<script type="text/javascript">

$(function(){
		$("#linkConsultaModificheStrutturaCompetente").click(function() {

		var url = $('#consultaModificheStrutturaCompetente').val();
		
		$.ajax({
			url: url,
			type: 'POST',
			success: function(data)  {
				$("#modConsultaModificheStrutturaCompetente").html(data);
			}
		});
	});	
});	
</script>

	<!-- task-131 <s:url method="dettaglioSubPopup" var="dettaglioSubPopup" /> -->
	<!-- task-131 <s:url method="dettaglioModPopup" var="dettaglioModPopup" /> -->
	<!-- task-131 <s:url method="consultaModificheProvvedimento" var="consultaModificheProvvedimento" /> -->
	<!-- task-131 <s:url method="consultaModificheProvvedimentoSub" var="consultaModificheProvvedimentoSub" /> -->
	<!-- task-131 <s:url method="loadLegameMovimentoStoricizzato" var="loadLegameMovimentoStoricizzato" /> -->
	
	<s:url action="consulta%{#oggetto}_dettaglioSubPopup" var="dettaglioSubPopup" />
	<s:url action="consulta%{#oggetto}_dettaglioModPopupPopup" var="dettaglioModPopup" />
	<s:url action="consulta%{#oggetto}_consultaModificheProvvedimento" var="consultaModificheProvvedimento" />
	<s:url action="consulta%{#oggetto}_consultaModificheProvvedimentoSub" var="consultaModificheProvvedimentoSub" />
	<s:url action="consulta%{#oggetto}_loadLegameMovimentoStoricizzato" var="loadLegameMovimentoStoricizzato" />
	
	<s:hidden id="dettaglioSubPopup" value="%{dettaglioSubPopup}" />
	<s:hidden id="dettaglioModPopup" value="%{dettaglioModPopup}" />
	<s:hidden id="consultaModificheProvvedimento" value="%{consultaModificheProvvedimento}" />
	<s:hidden id="consultaModificheProvvedimentoSub" value="%{consultaModificheProvvedimentoSub}" />
	<s:hidden id="loadLegameMovimentoStoricizzato" value="%{loadLegameMovimentoStoricizzato}" />
	
    <%-- SIAC-6997 --%>
	<!-- task-131 <s:url method="consultaModificheStrutturaCompetente" var="consultaModificheStrutturaCompetente" /> -->
	<s:url action="consulta%{#oggetto}_consultaModificheStrutturaCompetente" var="consultaModificheStrutturaCompetente" />

<%-- 	<s:url method="consultaModificheStrutturaCompetenteSub" var="consultaModificheStrutturaCompetenteSub" /> --%>

	<s:hidden id="consultaModificheStrutturaCompetente"	value="%{consultaModificheStrutturaCompetente}" />
<%-- 	<s:hidden id="consultaModificheStrutturaCompetenteSub" value="%{consultaModificheStrutturaCompetenteSub}" /> --%>
    <%-- SIAC-6997 --%>
	
	<script type="text/javascript" src="${jspath}movgest/consultaMovimento.js"></script>
	<script type="text/javascript" src="${jspath}movgest/provvedimento.js"></script>

	<s:include value="/jsp/include/footer.jsp" />