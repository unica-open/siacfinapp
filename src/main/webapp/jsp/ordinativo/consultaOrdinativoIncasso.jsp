<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="r" uri="http://www.csi.it/taglibs/remincl-1.0"%>
<%@taglib prefix="display" uri="/display-tags"%>

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
	
	<s:set var="cambiaTabFolderAction" value="%{'consultaOrdinativoIncasso_cambiaTabFolder'}" />
						
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:include value="/jsp/include/actionMessagesErrors.jsp" />
				<s:form id="consultaOrdinativoIncasso" method="post" cssClass="form-horizontal">
					<!-- per modalOrdinativo.jsp -->
					<s:set var="eliminaQuotaOrdinativoAction" value="%{'consultaOrdinativoIncasso_eliminaQuotaOrdinativo'}" />
					<s:set var="eliminaProvvisorioAction" value="%{'consultaOrdinativoIncasso_eliminaProvvisorio'}" />
					<s:set var="forzaInserisciQuotaAccertamentoAction" value="%{'consultaOrdinativoIncasso_forzaInserisciQuotaAccertamento'}" />
					<s:set var="forzaAggiornaQuotaAccertamentoAction" value="%{'consultaOrdinativoIncasso_forzaAggiornaQuotaAccertamento'}" />
					
				
<!-- ************************************************************************************* -->
					<h3>Consulta ordinativo <s:property value="ordinativoIncasso.numero"/> del <s:property value="%{ordinativoIncasso.dataEmissione}"/> - <s:property value="ordinativoIncasso.descrizione"/></h3>
					
					<ul class="nav nav-tabs">
					
						<li <s:if test="activeTab == 'ordinativoIncasso'">class="active"</s:if>><a href="#ordinativoIncasso" data-toggle="tab" id="linkTabOrdinativoIncasso">Ordinativo di incasso</a></li>
						<li <s:if test="activeTab == 'quote'">class="active"</s:if>><a href="#quote" data-toggle="tab">Quote</a></li>
						<li <s:if test="activeTab == 'provvisori'">class="active"</s:if>><a href="#provvisori" data-toggle="tab">Provvisori di cassa</a></li>
						<li <s:if test="activeTab == 'ordinativiCollegati'">class="active"</s:if>><a href="#ordinativiCollegati" data-toggle="tab">Ordinativi collegati</a></li>
					
					<s:if test="%{(ordinativoIncasso.elencoOrdinativiCollegati != null) && (ordinativoIncasso.elencoOrdinativiCollegati.size > 0) }">
						<li class="hide <s:if test="activeTab == 'dettaglioOnere'">active</s:if>">
							<a href="#dettaglioOnere" data-toggle="tab" id="linkTabDettaglioOnere">Dettaglio onere</a>
						</li>
					</s:if>
					
					</ul>
					
					<div class="tab-content">
<!-- ************************************************************************************* -->
					<s:if test="activeTab == 'ordinativoIncasso'">
						<div class="tab-pane active" id="ordinativoIncasso">
					</s:if>
					<s:else>
						<div class="tab-pane" id="ordinativoIncasso">
					</s:else>
							<h4>Inserimento: <s:property value="%{ordinativoIncasso.dataCreazioneSupport}"/> (<s:property value="ordinativoIncasso.loginCreazione"/>) - <span class="alLeft">Ultima modifica: <s:property value="%{ordinativoIncasso.dataModifica}"/>  (<s:property value="ordinativoIncasso.loginModifica"/>)</span></h4>
							<div class="boxOrSpan2">
								<div class="boxOrInLeft">
									<p>Dati ordinativo</p>
									<ul class="htmlelt">
										<li>
											<dfn>Numero</dfn> 
											<dl><s:property value="ordinativoIncasso.numero"/></dl>
										</li>
										<li>
											<dfn>Importo</dfn> 
											<dl><s:property value="getText('struts.money.format', {ordinativoIncasso.importoOrdinativo})"/></dl>
										</li>
										<li>
											<dfn>Stato:</dfn> 
											<dl><s:property value="ordinativoIncasso.statoOperativoOrdinativo"/> dal <s:property value="%{ordinativoIncasso.dataInizioValidita}"/></dl>
										</li>
										<li>
											<dfn>Descrizione</dfn> 
											<dl><s:property value="ordinativoIncasso.descrizione"/></dl>
										</li>
										
										<%-- <li> jira 2642
											<dfn>Note al tesoriere</dfn> 
											<dl><s:property value="ordinativoIncasso.noteTesoriere.codice"/> - <s:property value="ordinativoIncasso.noteTesoriere.descrizione"/></dl>
										</li> --%>
										<li>
											<dfn>Note</dfn> 
											<dl><s:property value="ordinativoIncasso.note"/></dl>
										</li>
										<li>
											<dfn>Distinta</dfn> 
											<dl><s:property value="ordinativoIncasso.distinta.codice"/> - <s:property value="ordinativoIncasso.distinta.descrizione"/></dl>
										</li>
										<li>
											<dfn>Conto tesoriere</dfn> 
											<dl><s:property value="ordinativoIncasso.contoTesoreria.codice"/> - <s:property value="ordinativoIncasso.contoTesoreria.descrizione"/></dl>
										</li>
										<s:if test="null != ordinativoIncasso.contoTesoreriaSenzaCapienza">
											<li>
												<dfn>Conto tesoriere di pertinenza</dfn> 
												<dl><s:property value="ordinativoIncasso.contoTesoreriaSenzaCapienza.codice"/> - <s:property value="ordinativoIncasso.contoTesoreriaSenzaCapienza.descrizione"/></dl>
											</li>
										</s:if>
										<li>
											<dfn>Allegato cartaceo</dfn> 
											<dl>
												<s:if test="ordinativoIncasso.flagAllegatoCartaceo">
													si
												</s:if>
												<s:else>
													no
												</s:else>
											</dl>
										</li>
										<li>
											<dfn>Creditori multipli</dfn> 
											<dl>
												<s:if test="ordinativoIncasso.flagBeneficiMultiplo">
													si
												</s:if>
												<s:else>
													no
												</s:else>
											</dl>
										</li>
										<li>
											<dfn>A copertura</dfn> 
											<dl>
												<s:if test="ordinativoIncasso.flagCopertura">
													si
												</s:if>
												<s:else>
													no
												</s:else>
											</dl>
										</li>
										<li>
											<dfn>Da trasmettere</dfn> 
											<dl>
												<s:if test="ordinativoIncasso.flagDaTrasmettere">
													si
												</s:if>
												<s:else>
													no
												</s:else>
											</dl>
										</li>
										<li>
											<dfn>Avviso</dfn> 
											<dl><s:property value="ordinativoIncasso.tipoAvviso.codice"/> - <s:property value="ordinativoIncasso.tipoAvviso.descrizione"/></dl>
										</li>
									</ul>
								</div>
								<div class="boxOrInRight">
									<p>Capitolo</p>
									<ul class="htmlelt">
										<li>
											<dfn>Anno</dfn> 
											<dl><s:property value="ordinativoIncasso.capitoloEntrataGestione.annoCapitolo"/></dl>
										</li>
										<li>
											<dfn>Capitolo</dfn> 
											<dl><s:property value="ordinativoIncasso.capitoloEntrataGestione.numeroCapitolo"/></dl>
										</li>
										<li>
											<dfn>Articolo</dfn> 
											<dl><s:property value="ordinativoIncasso.capitoloEntrataGestione.numeroArticolo"/></dl>
										</li>
										<li>
											<dfn>UEB</dfn> 
											<dl><s:property value="ordinativoIncasso.capitoloEntrataGestione.numeroUEB"/></dl>
										</li>
										<li>
											<dfn>Descrizione</dfn> 
											<dl><s:property value="ordinativoIncasso.capitoloEntrataGestione.descrizione"/></dl>
										</li>
										<li>
											<dfn>Struttura</dfn> 
											<dl><s:property value="ordinativoIncasso.capitoloEntrataGestione.strutturaAmministrativoContabile.codice"/> - <s:property value="ordinativoIncasso.capitoloEntrataGestione.strutturaAmministrativoContabile.descrizione"/></dl>
										</li>
									</ul>
									<p>Soggetto</p>
									<ul class="htmlelt">
										<li>
											<dfn>Codice</dfn> 
											<dl><s:property value="ordinativoIncasso.soggetto.codiceSoggetto"/></dl>
										</li>
										<li>
											<dfn>Denominazione</dfn> 
											<dl><s:property value="ordinativoIncasso.soggetto.denominazione"/></dl>
										</li>
										<li>
											<dfn>Partita IVA</dfn> 
											<dl><s:property value="ordinativoIncasso.soggetto.partitaIva"/></dl>
										</li>
										<li>
											<dfn>Cod. Fiscale</dfn> 
											<dl><s:property value="ordinativoIncasso.soggetto.codiceFiscale"/></dl>
										</li>
									</ul>
								</div>
							</div>
							
							<div class="boxOrSpan2">
								<div class="boxOrInLeft">
									<p>Transazione elementare</p>
									<ul class="htmlelt">
										
										<li>
											<dfn>P.dC. Finanziario</dfn> 
											<dl><s:property value="ordinativoIncasso.codPdc"/>
											<s:if test="ordinativoIncasso.descPdc != null">
												 - <s:property value="ordinativoIncasso.descPdc"/>
											</s:if>
											</dl>
										</li>
										<li>
											<dfn>Codice transazione europea</dfn> 
											<dl><s:property value="ordinativoIncasso.codTransazioneEuropeaSpesa"/>
											<s:if test="ordinativoIncasso.descTransazioneEuropeaSpesa != null">
												 - <s:property value="ordinativoIncasso.descTransazioneEuropeaSpesa"/>
											</s:if>
											</dl>
										</li>
										<li>
											<dfn>SIOPE</dfn> 
											<dl><s:property value="ordinativoIncasso.codSiope"/>
											<s:if test="ordinativoIncasso.descCodSiope != null">
												 - <s:property value="ordinativoIncasso.descCodSiope"/>
											</s:if>
											</dl>
										</li>
										<li>
											<dfn>Ricorrente</dfn> 
											<dl><s:property value="ordinativoIncasso.codRicorrenteSpesa"/></dl>
										</li>
										<li>
											<dfn>Capitolo perimetro sanitario</dfn> 
											<dl><s:property value="ordinativoIncasso.codCapitoloSanitarioSpesa"/></dl>
										</li>
										
									</ul>
								</div>
								<div class="boxOrInRight">
									<p>Date transazione</p>
									<ul class="htmlelt">
										<li>
											<dfn>Data emissione</dfn> 
											<dl><s:property value="%{ordinativoIncasso.dataEmissione}"/></dl>
										</li>
										<li>
											<dfn>Data variazione </dfn> 
											<dl><s:property value="%{ordinativoIncasso.dataVariazione}"/></dl>
										</li>
										<li>
											<dfn>Data trasmissione </dfn> 
											<dl><s:property value="%{ordinativoIncasso.datiOrdinativoTrasmesso.dataTrasmissione}"/></dl>
										</li>
										<li>
											<dfn>Data spostamento</dfn> 
											<dl><s:property value="%{ordinativoIncasso.dataSpostamento}"/></dl>
										</li>
										<li>
											<dfn>Data quietanza</dfn> 
											<dl><s:property value="%{ordinativoIncasso.datiOrdinativoTrasmesso.dataQuietanza}"/></dl>
										</li>
										<li>
											<dfn>Numero quietanza</dfn> 
											<dl><s:property value="ordinativoIncasso.datiOrdinativoTrasmesso.numeroQuietanza"/></dl>
											<%-- <dfn>Cro</dfn> 
											<dl><s:property value="ordinativoIncasso.datiOrdinativoTrasmesso.cro"/></dl> --%>
										</li>
										<li>
											<dfn>Data firma</dfn> 
											<dl><s:property value="%{ordinativoIncasso.datiOrdinativoTrasmesso.dataFirma}"/></dl>
										</li>
										<li>
											<dfn>Firmatario</dfn> 
											<dl><s:property value="ordinativoIncasso.datiOrdinativoTrasmesso.firma"/></dl>
										</li>
									</ul>
								</div>
							</div>
							
							
							<div class="boxOrSpan2">
								<div class="boxOrInLeft">
									<p>Provvedimento</p>
									<ul class="htmlelt">
										<li>
											<dfn>Tipo</dfn> 
											<dl><s:property value="ordinativoIncasso.attoAmministrativo.tipoAtto.codice"/></dl>
										</li>
										<li>
											<dfn>Anno</dfn> 
											<dl><s:property value="ordinativoIncasso.attoAmministrativo.anno"/></dl>
										</li>
										<li>
											<dfn>Numero</dfn> 
											<dl><s:property value="ordinativoIncasso.attoAmministrativo.numero"/></dl>
										</li>
										<li>
											<dfn>Struttura</dfn> 
											<dl><s:property value="ordinativoIncasso.attoAmministrativo.StrutturaAmmContabile.codice"/> - <s:property value="ordinativoIncasso.attoAmministrativo.StrutturaAmmContabile.descrizione"/></dl>
										</li>
										<li>
											<dfn>Oggetto</dfn> 
											<dl><s:property value="ordinativoIncasso.attoAmministrativo.oggetto"/></dl>
										</li>
										<li>
											<dfn>Stato</dfn> 
											<dl><s:property value="ordinativoIncasso.attoAmministrativo.statoOperativo"/></dl>
										</li>
										
									</ul>
								</div>
								<div class="boxOrInRight">
									<p>Classificatori</p>
									<ul class="htmlelt">
										<s:include value="/jsp/ordinativo/include/classifGenericiConsultaOrdInc.jsp" />
									</ul>
								</div>
							</div>
							
							
							
						</div>
<!-- ************************************************************************************* -->
					<s:if test="activeTab == 'quote'">
						<div class="tab-pane active" id="quote">
					</s:if>
					<s:else>
						<div class="tab-pane" id="quote">
					</s:else>
							<h4>Inserimento: <s:property value="%{ordinativoIncasso.dataCreazioneSupport}"/> (<s:property value="ordinativoIncasso.loginCreazione"/>) - <span class="alLeft">Ultima modifica: <s:property value="%{ordinativoIncasso.dataModifica}"/>  (<s:property value="ordinativoIncasso.loginModifica"/>)</span></h4>

							<s:if test="%{(ordinativoIncasso.elencoSubOrdinativiDiIncasso != null) && (ordinativoIncasso.elencoSubOrdinativiDiIncasso.size > 0) }">
								<display:table htmlId="listaQuote" 
									name="ordinativoIncasso.elencoSubOrdinativiDiIncasso" 
									class="table table-hover tab_centered" 
									summary="riepilogo" 
									uid="quotaID" 
									pagesize="5"
								 	requestURI="consultaOrdinativoIncasso.do" 
								>
	
									<display:column title="Numero" headerClass="tabRow sBold" class="tabRow tab_Right">
										<s:property value="%{#attr.quotaID.numero}"/>
									</display:column>
									<display:column title="Accertamento" headerClass="tabRow sBold" class="tabRow tab_Right">
										<s:property value="%{#attr.quotaID.accertamento.annoMovimento}" /> / <s:property value="%{#attr.quotaID.accertamento.numero.intValue()}" />
										<s:if test="%{(#attr.quotaID.accertamento.elencoSubAccertamenti != null) && (#attr.quotaID.accertamento.elencoSubAccertamenti.size > 0) }">
											/ <s:property value="%{#attr.quotaID.accertamento.elencoSubAccertamenti[0].numero.intValue()}" />
										</s:if>	
									</display:column>
									<display:column title="Documento" headerClass="tabRow sBold" class="tabRow tab_Right" >
										<!-- CR 1910 -->
										<s:property value="%{#attr.quotaID.subDocumentoEntrata.documento.tipoDocumento.codice}" /> / 
										<s:property value="%{#attr.quotaID.subDocumentoEntrata.documento.anno}" /> / 
										<s:property value="%{#attr.quotaID.subDocumentoEntrata.documento.numero}" /> / 
										<s:property value="%{#attr.quotaID.subDocumentoEntrata.documento.descrizione}" /> / 
										emesso il <s:property value="%{#attr.quotaID.subDocumentoEntrata.documento.dataEmissione}" /> - <s:property value="%{#attr.quotaID.subDocumentoEntrata.numero}" />
										
									</display:column>
										
									
									<display:column title="Descrizione quota" headerClass="tabRow sBold borderRight" class="borderRight">
										<s:property value="%{#attr.quotaID.descrizione}"/>
									</display:column>
									<display:column title="Data scadenza" headerClass="tabRow sBold borderRight" class="borderRight">
										<s:property value="%{#attr.quotaID.dataScadenza}"/>
									</display:column>
									<display:column title="Importo quota" headerClass="tabRow sBold borderRight" class="borderRight">
										<s:property value="getText('struts.money.format', {#attr.quotaID.importoAttuale})" default="-" />
									</display:column>
									
									<display:footer> 
										<tr> 
											<th>Totali</th>
											<th>&nbsp;</th>
											<th>&nbsp;</th>
											<th>&nbsp;</th>
											<th>&nbsp;</th>
											<th class="tab_Right" style="text-align:center"><s:property value="getText('struts.money.format', {ordinativoIncasso.importoOrdinativo})" default="-" /></th>
										<tr> 
									</display:footer>								
								</display:table>
								</div>
							</s:if>
							<s:else>
								<div id="ncsdf">Non ci sono record da visualizzare.</div>
							</s:else>
							

						</div>
<!-- ************************************************************************************* -->
					<s:if test="activeTab == 'provvisori'">
						<div class="tab-pane active" id="provvisori">
					</s:if>
					<s:else>
						<div class="tab-pane" id="provvisori">
					</s:else>
							<h4>Inserimento: <s:property value="%{ordinativoIncasso.dataCreazioneSupport}"/> (<s:property value="ordinativoIncasso.loginCreazione"/>) - <span class="alLeft">Ultima modifica: <s:property value="%{ordinativoIncasso.dataModifica}"/>  (<s:property value="ordinativoIncasso.loginModifica"/>)</span></h4>

							<s:if test="%{(ordinativoIncasso.elencoRegolarizzazioneProvvisori != null) && (ordinativoIncasso.elencoRegolarizzazioneProvvisori.size > 0) }">
								<display:table 
									htmlId="listaProvvisori" 
									name="ordinativoIncasso.elencoRegolarizzazioneProvvisori" 
									class="table table-hover tab_centered" 
									summary="riepilogo" 
									uid="provvisorioID" 
									pagesize="5"
								 	requestURI="consultaOrdinativoIncasso.do"
								>
	
									<display:column title="Numero" headerClass="tabRow sBold" class="tabRow tab_Right">
										<s:property value="%{#attr.provvisorioID.provvisorioDiCassa.anno}"/> - <s:property value="%{#attr.provvisorioID.provvisorioDiCassa.numero}"/>
									</display:column>
									<display:column title="Causale" headerClass="tabRow sBold" class="tabRow tab_Right">
										<s:property value="%{#attr.provvisorioID.provvisorioDiCassa.causale}" />
									</display:column>
									<display:column title="Importo provvisorio" headerClass="tabRow sBold borderRight" class="borderRight">
										<s:property value="getText('struts.money.format', {#attr.provvisorioID.provvisorioDiCassa.importo})" default="-" />
									</display:column>
									<display:column title="Importo regolarizzato" headerClass="tabRow sBold borderRight" class="borderRight">
										<s:property value="getText('struts.money.format', {#attr.provvisorioID.importo})" default="-" />
									</display:column>
									
									<display:footer> 
										<tr> 
											<th>Totali</th>
											<th>&nbsp;</th>
											<th class="tab_Right" style="text-align:center"> <s:property value="getText('struts.money.format', {ordinativoIncasso.totImportiProvvisori})" default="-" /> </th>
											<th class="tab_Right" style="text-align:center"> <s:property value="getText('struts.money.format', {ordinativoIncasso.totImportiRegolarizzati})" default="-" /> </th>
										<tr> 
									</display:footer>								
								</display:table>
								</div>
							</s:if>
							<s:else>
								<div id="ncsdf">Non ci sono record da visualizzare.</div>
							</s:else>
							

						</div>
<!-- ************************************************************************************* -->
					<s:if test="activeTab == 'ordinativiCollegati'">
						<div class="tab-pane active" id="ordinativiCollegati">
					</s:if>
					<s:else>
						<div class="tab-pane" id="ordinativiCollegati">
					</s:else>
					
							<s:if test="%{(ordinativoIncasso.elencoOrdinativiCollegati != null) && (ordinativoIncasso.elencoOrdinativiCollegati.size > 0) }">
								<display:table 
								htmlId="listaOrdinativiCollegati" 
								name="ordinativoIncasso.elencoOrdinativiCollegati" 
								class="table table-hover tab_centered" 
									partialList="true"
									sort="external"
									size="totaleOrdinativiCollegati" 
								summary="riepilogo" 
								uid="ordCollID" 
								pagesize="5"
								 	requestURI="consultaOrdinativoIncasso.do"
								>
	
									<display:column title="Tipo" headerClass="tabRow sBold" class="tabRow tab_Right">
										<s:property value="%{#attr.ordCollID.tipoOrdinativo}"/>
									</display:column>
									<display:column title="Associazione" headerClass="tabRow sBold" class="tabRow tab_Right">
										<s:property value="%{#attr.ordCollID.tipoAssociazioneEmissione}"/>
									</display:column>
									<display:column title="Numero" headerClass="tabRow sBold" class="tabRow tab_Right">
										<s:property value="%{#attr.ordCollID.numero}"/>
									</display:column>
									<display:column title="Soggetto" headerClass="tabRow sBold" class="tabRow tab_Right">
										<s:property value="%{#attr.ordCollID.soggetto.codiceSoggetto}"/> - <s:property value="%{#attr.ordCollID.soggetto.denominazione}"/>
									</display:column>
									<display:column title="Descrizione" headerClass="tabRow sBold borderRight" class="borderRight">
										<s:property value="%{#attr.ordCollID.descrizione}"/>
									</display:column>
									<display:column title="Stato" headerClass="tabRow sBold borderRight" class="borderRight">
										<s:property value="%{#attr.ordCollID.statoOperativoOrdinativo}"/>
										<%-- <s:if test='%{#attr.ordCollID.statoOperativoOrdinativo == "INSERITO" }'>
											I
										</s:if>
										<s:elseif test='%{#attr.ordCollID.statoOperativoOrdinativo == "TRASMESSO" }'>
											T
										</s:elseif>
										<s:elseif test='%{#attr.ordCollID.statoOperativoOrdinativo == "FIRMATO" }'>
											F
										</s:elseif>
										<s:elseif test='%{#attr.ordCollID.statoOperativoOrdinativo == "QUIETANZATO" }'>
											Q
										</s:elseif>
										<s:elseif test='%{#attr.ordCollID.statoOperativoOrdinativo == "ANNULLATO" }'>
											A
										</s:elseif> --%>
									</display:column>
									<display:column title="Capitolo" headerClass="tabRow sBold borderRight" class="borderRight">
										<s:if test='%{(#attr.ordCollID.tipoOrdinativo.equals("E")) }'>
											<s:property value="%{#attr.ordCollID.capitoloEntrataGestione.numeroCapitolo}"/> / <s:property value="%{#attr.ordCollID.capitoloEntrataGestione.numeroArticolo}"/> / <s:property value="%{#attr.ordCollID.capitoloEntrataGestione.numeroUEB}"/>
										</s:if>	
										<s:elseif test='%{(#attr.ordCollID.tipoOrdinativo.equals("S")) }'>
											<s:property value="%{#attr.ordCollID.capitoloUscitaGestione.numeroCapitolo}"/> / <s:property value="%{#attr.ordCollID.capitoloUscitaGestione.numeroArticolo}"/> / <s:property value="%{#attr.ordCollID.capitoloUscitaGestione.numeroUEB}"/>
										</s:elseif>
									</display:column>
									<display:column title="Importo" headerClass="tabRow sBold borderRight" class="borderRight">
										<s:property value="getText('struts.money.format', {#attr.ordCollID.importoOrdinativo})" default="-" />
									</display:column>
									<display:column title="" headerClass="tabRow sBold" class="tabRow tab_Right" >
										<div class="btn-group">
													<button class="btn dropdown-toggle" id="act_btn2" data-toggle="dropdown">Azioni <span class="caret"></span>
													</button>
													<ul class="dropdown-menu pull-right">
													
														<s:if test='%{(#attr.ordCollID.tipoOrdinativo.equals("E")) }'>
															<li>
													    		<a id="linkQuoteInc_<s:property value="%{#attr.ordCollID.numero}"/>" href="#ModQuote" class="linkQuoteIncInc" data-toggle="modal">dettaglio quote</a>
															</li>
														</s:if>	
														<s:elseif test='%{(#attr.ordCollID.tipoOrdinativo.equals("S")) }'>
															<li>
													    		<a id="linkQuoteInc_<s:property value="%{#attr.ordCollID.numero}"/>" href="#ModQuote" class="linkQuoteIncPag" data-toggle="modal">dettaglio quote</a>
															</li>
														</s:elseif>
													    
													</ul>
												</div>
									</display:column>
								</display:table>
	
							</div>
							</s:if>
							<s:else>
								<div id="ncsdf">Non ci sono record da visualizzare.</div>
							</s:else>
						
					</div>
					
					
					<!-- CR 1910 se ci sono ordinativi Collegati e il dettaglio onere, abilito il tab Dettaglio Onere  -->
					<s:if test="activeTab == 'ordinativiCollegati'">
						<div class="tab-pane active" id="dettaglioOnere">
					</s:if>
					<s:else>
							<div class="tab-pane" id="dettaglioOnere">
					</s:else>
					
							<s:if test="%{(ordinativoIncasso.elencoOrdinativiCollegati != null) && (ordinativoIncasso.elencoOrdinativiCollegati.size > 0) }">
								<display:table 
								htmlId="listaOrdinativiCollegati" 
								name="ordinativoPagamento.elencoOrdinativiCollegati" 
									partialList="true"
									sort="external"
									size="totaleOrdinativiCollegati" 
								class="table table-hover tab_centered" 
								summary="riepilogo" 
								uid="ordCollID" pagesize="5"
								 	requestURI="consultaOrdinativoIncasso.do">
	
									<display:column title="Numero" headerClass="tabRow sBold" class="tabRow tab_Right">
											<s:property value="%{#attr.ordCollID.subordinativo.numero}"/>
									</display:column>
										
									<s:if test="ordCollID.subordinativo.tipoOnere!=null">
										
										<display:column title="Natura" headerClass="tabRow sBold" class="tabRow tab_Right">
											<s:property value="%{#attr.ordCollID.subordinativo.tipoOnere.naturaOnere.codice}"/>
										</display:column>
										<display:column title="TipoOnere" headerClass="tabRow sBold" class="tabRow tab_Right">
											<s:property value="%{#attr.ordCollID.subordinativo.tipoOnere.codice}"/>- <s:property value="%{#attr.ordCollID.subordinativo.tipoOnere.descrizione}"/>
										</display:column>
										
									</s:if>
									<s:else>
										<display:column title="Descrizione" headerClass="tabRow sBold" class="tabRow tab_Right">
											<s:property value="%{#attr.ordCollID.subordinativo.descrizione}"/>
										</display:column>
										<display:column title="Documento" headerClass="tabRow sBold" class="tabRow tab_Right">
											<s:property value="%{#attr.ordCollID.subordinativo.subDocumentoEntrata.documento.tipoDocumento.codice}" /> / 
											<s:property value="%{#attr.ordCollID.subordinativo.subDocumentoEntrata.documento.anno}" /> / 
											<s:property value="%{#attr.ordCollID.subordinativo.subDocumentoEntrata.documento.numero}" /> / 
											<s:property value="%{#attr.ordCollID.subordinativo.subDocumentoEntrata.documento.descrizione}" /> / 
											emesso il <s:property value="%{#attr.ordCollID.subordinativo.subDocumentoEntrata.documento.dataEmissione}" /> - <s:property value="%{#attr.ordCollID.subordinativo.subDocumentoEntrata.numero}" />
										</display:column>
																			
									</s:else>
									<display:column title="Importo" headerClass="tabRow sBold" class="tabRow tab_Right">
											<s:property value="%{#attr.ordCollID.subordinativo.importoAttuale}"/>
									</display:column>
									
								</display:table>
								</div>
								
							</s:if>
							<s:else>
								<div id="ncsdf">Non ci sono record da visualizzare.</div>	
							</s:else>
						
					</div>
					
<!-- ************************************************************************************* -->
					<s:include value="/jsp/ordinativo/include/modalOrdinativo.jsp" />
					<p>
						<s:include value="/jsp/include/indietro.jsp" />
						
						<!-- TASTO RIPETI ORDINATIVO -->
						<s:if test="abilitatoRipetiOrdinativoIncasso()">
							<!-- task-131 <s:submit name="ripeti" id="ripeti" value="ripeti" method="ripeti" cssClass="btn btn-primary pull-right" /> -->
							<s:submit name="ripeti" id="ripeti" value="ripeti" action="consultaOrdinativoIncasso_ripeti" cssClass="btn btn-primary pull-right" />
						</s:if>
					</p>
					
				</s:form>
			</div>
		</div>
	</div>

<s:include value="/jsp/include/javascriptConsultaOrdinativo.jsp" />	

<script type="text/javascript">
	
		
	$(".linkQuoteIncInc").click(function() {
		var supportId = $(this).attr("id").split("_");
		$.ajax({
			// task-131 url: '<s:url method="dettaglioQuoteOrdinativoIncassoInc"/>',
			url: '<s:url action="consultaOrdinativoIncasso_dettaglioQuoteOrdinativoIncassoInc"/>',
			type: 'POST',
			data: 'numeroOrdinativoSelezionato=' + supportId[1],
		    success: function(data)  {
			    $("#divModQuote").html(data);
			}
		});
	});	
	
	$(".linkQuoteIncPag").click(function() {
		var supportId = $(this).attr("id").split("_");
		$.ajax({
			// task-131 url: '<s:url method="dettaglioQuoteOrdinativoIncassoPag"/>',
			url: '<s:url action="consultaOrdinativoIncasso_dettaglioQuoteOrdinativoIncassoPag"/>',
			type: 'POST',
			data: 'numeroOrdinativoSelezionato=' + supportId[1],
		    success: function(data)  {
			    $("#divModQuote").html(data);
			}
		});
	});	
		
</script>
<s:include value="/jsp/include/footer.jsp" />