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
					<h3><s:property value="movimento.descMovimentoCapital" />o <s:property value="movimento.anno" />/<s:property value="movimento.numero" /> - <s:property value="movimento.descrizione" /> - <s:property value="getText('struts.money.format', {movimento.importo})" /> ( <s:property value="movimento.statoOperativo" /> dal <s:property value="%{movimento.dataStatoOperativo}" /> ) 
					<!-- <s:if test="movimento.impegno"><s:if test="isImpegnoSdf()"> - SENZA DISPONIBILITA' DI FONDI</s:if></s:if> -->
					</h3>
					<s:hidden id="annoMovimentoInConsultazione" name="movimento.anno"/>
					<s:hidden id="numeroMovimentoInConsultazione" name="movimento.numero"/>
					<ul class="nav nav-tabs">
						<li class="active"><a href="#accertamento" data-toggle="tab"><s:property value="movimento.descMovimentoCapital" />o</a></li>
						<li><a href="#sub" data-toggle="tab">Sub<s:property value="movimento.descMovimentoLower" />i</a></li>
						<li><a href="#modifiche" data-toggle="tab">Modifiche</a></li>
						<s:if test="movimento.impegno">
							<li><a href="#mutui" data-toggle="tab">Mutui</a></li>
							<li><a href="#accertamenti" id="linkAccertamenti" data-toggle="tab">Accertamenti</a></li>
						</s:if>
						<s:if test="!movimento.impegno">
							<li><a id="linkImpegni" href="#impegni" data-toggle="tab">Impegni</a></li>
						</s:if>
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
											<dl><s:property value="movimento.codificaTransazioneElementare" escape="false" />&nbsp;</dl>
		                                </li> 
		                                
		                                <!-- SIAC-6247 & SIAC-6268 -->
		                                <li>
											<dfn>Titolo giuridico</dfn>
											<dl><s:property value="movimento.titoloGiuridico" escape="false" />&nbsp;</dl>
		                                </li>
		                                <s:if test="movimento.tipoMovimento == 0">
			                                <li>
												<dfn>Tipo Tracciabilita</dfn>
												<dl><s:property value="movimento.tipoTracciabilita" escape="false" />&nbsp;</dl>
			                                </li>
			                                <li>
												<dfn>Voce Tracciabilita</dfn>
												<dl><s:property value="movimento.voceTracciabilita" escape="false" />&nbsp;</dl>
			                                </li>
			                                <li>
												<dfn>Classificazione FSC</dfn>
												<dl><s:property value="movimento.classificazioneFSC" escape="false" />&nbsp;</dl>
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
										
										<li>	
											<dfn>Da riaccertamento</dfn>
											<dl><s:property value="movimento.daRiaccertamento" />&nbsp;</dl>
										</li>
										
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
									
									<!-- PER ACCERTAMENTO Rilevante Co.Ge. GSA va qui -->
									<s:if test="!movimento.impegno">
										<s:if test="isEnteAbilitatoGestioneGsa()">
											<li>
												<dfn>Rilevante Co.Ge. GSA</dfn>
												<dl><s:property value="movimento.flagAttivaGsa" />&nbsp;</dl>
											</li>
										</s:if>
									</s:if>
										
									<!-- il  cig e cup visibili solo se impegno  -->	
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
										
										<!-- PER IMPEGNO Rilevante Co.Ge. GSA va qui -->
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
									
								</div>	
	                             
	                             
	                             <div class="boxOrSpan2">
								    
								    <s:if test="movimento.impegno">
								    	<s:include value="/jsp/movgest/include/consultaImpegnoRiepilogo.jsp" />
								    </s:if>
								    <s:if test="!movimento.impegno">
								    	<s:include value="/jsp/movgest/include/consultaAccertamentoRiepilogo.jsp" />
								    </s:if>
								    
									<!-- PROVVEDIMENTO -->
										<!--modale consulta modifiche provvedimento-->
										<%-- <div id="modConsultaModificheProvvedimento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaProvLabel" aria-hidden="true">
											<div class="modal-body">
											    <s:include value="/jsp/movgest/include/consultaModificheProvvedimento.jsp" />
										    </div>   
									 	 </div>  --%>
									   <!--/modale modifiche -->
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
							
							
								<!-- SIAC-6247 -->
								<div class="boxOrSpan2">
									<div class="boxOrInLeft">
										
								  	</div>
									
									
									<div class="boxOrInRight"></div>
									
								</div>
								<!-- /SIAC-6247 -->
								
	                             
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
								<!-- SIAC-6929 -->
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
								
								<!--  gia' inserito il pop up sul click del numero di sub -->
<%-- 								<display:column> --%>
<!-- 				       				<div class="btn-group"> -->
<%-- 										<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span> --%>
<!-- 										</button> -->
<!-- 										<ul class="dropdown-menu pull-right"> -->
<%-- 											<li><a id="linkConsulta_<s:property value="%{#attr.subID.numero}"/>" --%>
<!-- 											       href="#consultazioneSub" class="consultaSubPopUp"  data-toggle="modal">consulta</a></li> -->
<!-- 										</ul> -->
<!-- 									</div> -->
<%-- 								</display:column>		 --%>
							</display:table>
						</div>
						<!-- fine SUBIMPEGNI -->	

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
								<display:column title="Stato" property="statoOperativo" />
								<!-- SIAC-6929 --> 
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
						
						<!-- MUTUI -->
						
						<div class="tab-pane" id="mutui">
							<h4 class="step-pane">Mutui</h4>
							
							
							<display:table name="listaMutui" class="table tab_left table-hover" summary="riepilogo mutui" uid="mutuoID" >
								
								<display:column title="Subimp." property="numeroMovimentoGestione" />
								<display:column title="Nr. mutuo" property="codiceMutuo" />
								<display:column title="Descrizione mutuo" property="descrizioneMutuo" />
								<display:column title="Istituto mutuante" property="istitutoMutuante" />
	                            <display:column title="Tipo mutuo" property="descrizioneTipoMutuo" />
								<display:column title="Data inizio" property="dataInizioMutuo" format="{0,date,dd/MM/yyyy}"/>
								<display:column title="Importo mutuo" property="importoAttualeMutuo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"  />
								<display:column title="Importo voce" property="importoAttualeVoceMutuo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
							</display:table>
							
							
							<div class="Border_line"></div>
						</div>
						<!-- fine MUTUI -->
						
						<!-- ACCERTAMENTI -->
						<div class="tab-pane" id="accertamenti">
						  <h4 class="step-pane">Accertamenti</h4>
						  
						    <display:table name="listaVincoliImpegno" class="table tab_left table-hover" summary="riepilogo mutui" uid="accertamentiID">
						  
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
						    </display:table>
										
							<div class="Border_line"></div>
							
							
							<h4 class="step-pane">Accertamenti da legame storicizzato</h4>
							<div id="divLegameStoricoDaImpegno">
							   Caricamento in corso..
							</div>
							
							<div class="Border_line"></div>
						</div>
						<!-- fine ACCERTAMENTI -->
						
						<!-- IMPEGNI -->
						<div class="tab-pane" id="impegni">
							<h4 class="step-pane">Impegni</h4>
							  
						    <display:table name="listaVincoliAccertamento" class="table tab_left table-hover" summary="riepilogo mutui" uid="impegniID">
						  
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
					
					<!--modale consulta modifiche provvedimento-->
					<div id="modConsultaModificheProvvedimento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaProvLabel" aria-hidden="true">
					    <div class="modal-body">
						    <s:include value="/jsp/movgest/include/consultaModificheProvvedimento.jsp" />
					        </div>   
				    </div>


					<%-- <div id="divCapitoloPopup" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="capitoloTabLabel" aria-hidden="true">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							<h3>Capitolo <s:property value="movimento.capitolo.anno" /> / <s:property value="movimento.capitolo.numero" /> / <s:property value="movimento.capitolo.ueb" /> - <s:property value="movimento.capitolo.descrizione" /> - <s:property value="movimento.capitolo.struttura" /> - Tipo finanziamento: <s:property value="movimento.capitolo.tipoFinanziamento" /></h3>
						</div>
						<div class="modal-body">
							<display:table name="movimento.capitolo.importi" class="table table-hover table-bordered" summary="riepilogo capitoli" uid="capitoloID">
								<display:column title="Anno competenza" property="annoCompetenza" />
								<display:column title="Disponibilit&agrave; da impegnare" property="disponibilitaImpegnare" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
								<display:column title="Disponibilit&agrave; da pagare" property="disponibilitaPagare" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
								<display:column title="Disponibilit&agrave; da variare" property="disponibilitaVariare" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
							</display:table>
						</div>
					</div> --%>


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
							<%-- <s:iterator value="datoPerVisualizza.importiCapitoloUG">
								<td><s:property value="getText('struts.money.format', {importiCapitoloUG})" /></td>
							</s:iterator>      --%>   
						</tr>
						</table>
					</div>
					</div>  

					<!--  -->

					<p><s:include value="/jsp/include/indietro.jsp" /></p>

				</s:form>
			</div>
		</div>
	</div>

	<s:url method="dettaglioSubPopup" var="dettaglioSubPopup" />
	<s:url method="dettaglioModPopup" var="dettaglioModPopup" />
	<s:url method="consultaModificheProvvedimento" var="consultaModificheProvvedimento" />
	<s:url method="consultaModificheProvvedimentoSub" var="consultaModificheProvvedimentoSub" />
	<s:url method="loadLegameMovimentoStoricizzato" var="loadLegameMovimentoStoricizzato" />
	
	<s:hidden id="dettaglioSubPopup" value="%{dettaglioSubPopup}" />
	<s:hidden id="dettaglioModPopup" value="%{dettaglioModPopup}" />
	<s:hidden id="consultaModificheProvvedimento" value="%{consultaModificheProvvedimento}" />
	<s:hidden id="consultaModificheProvvedimentoSub" value="%{consultaModificheProvvedimentoSub}" />
	<s:hidden id="loadLegameMovimentoStoricizzato" value="%{loadLegameMovimentoStoricizzato}" />
	
	<script type="text/javascript" src="${jspath}movgest/consultaMovimento.js"></script>
	<script type="text/javascript" src="${jspath}movgest/provvedimento.js"></script>

	<s:include value="/jsp/include/footer.jsp" />