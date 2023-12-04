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
	
	<s:set var="cambiaTabFolderAction" value="%{'consultaOrdinativoPagamento_cambiaTabFolder'}" />
					
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:include value="/jsp/include/actionMessagesErrors.jsp" />
				<s:form id="consultaOrdinativoPagamento" method="post" cssClass="form-horizontal">
					<!-- per modalOrdinativo.jsp -->
					<s:set var="eliminaQuotaOrdinativoAction" value="%{'consultaOrdinativoPagamento_eliminaQuotaOrdinativo'}" />
					<s:set var="eliminaProvvisorioAction" value="%{'consultaOrdinativoPagamento_eliminaProvvisorio'}" />
					<s:set var="forzaInserisciQuotaAccertamentoAction" value="%{'consultaOrdinativoPagamento_forzaInserisciQuotaAccertamento'}" />
					<s:set var="forzaAggiornaQuotaAccertamentoAction" value="%{'consultaOrdinativoPagamento_forzaAggiornaQuotaAccertamento'}" />
	
					
<!-- ************************************************************************************* -->
					<h3>Consulta ordinativo <s:property value="ordinativoPagamento.numero"/> del <s:property value="%{ordinativoPagamento.dataEmissione}"/> - <s:property value="ordinativoPagamento.descrizione"/></h3>
					
					<ul class="nav nav-tabs">
								
						<li <s:if test="activeTab == 'ordinativoPagamento'">class="active"</s:if>>
							<a href="#ordinativoPagamento" data-toggle="tab" id="linkTabOrdinativoPagamento">Ordinativo di spesa</a>
						</li>
						<li <s:if test="activeTab == 'quote'">class="active"</s:if>>
							<a href="#quote" data-toggle="tab" id="linkTabQuote">Quote</a>
						</li>
						<li <s:if test="activeTab == 'provvisori'">class="active"</s:if>>
							<a href="#provvisori" data-toggle="tab" id="linkTabProvvisori">Provvisori di cassa</a>
						</li>
						<li <s:if test="activeTab == 'ordinativiCollegati'">class="active"</s:if>>
							<a href="#ordinativiCollegati" data-toggle="tab" id="linkTabOrdinativiCollegati">Ordinativi collegati</a>
						</li>
					<s:if test="%{(ordinativoPagamento.elencoOrdinativiCollegati != null) && (ordinativoPagamento.elencoOrdinativiCollegati.size > 0) }">
						<li class="hide <s:if test="activeTab == 'dettaglioOnere'">active</s:if>">
							<a href="#dettaglioOnere" data-toggle="tab" id="linkTabDettaglioOnere">Dettaglio onere</a>
						</li>
					</s:if>

					</ul>
					
					<div class="tab-content">
<!-- ************************************************************************************* -->
					<s:if test="activeTab == 'ordinativoPagamento'">
						<div class="tab-pane active" id="ordinativoPagamento">
					</s:if>
					<s:else>
						<div class="tab-pane" id="ordinativoPagamento">
					</s:else>
							<h4>Inserimento: <s:property value="%{ordinativoPagamento.dataEmissione}"/> (<s:property value="ordinativoPagamento.loginCreazione"/>) - <span class="alLeft">Ultima modifica: <s:property value="%{ordinativoPagamento.dataModifica}"/>  (<s:property value="ordinativoPagamento.loginModifica"/>)</span></h4>
							<div class="boxOrSpan2">
								<div class="boxOrInLeft">
									<p>Dati ordinativo</p>
									<ul class="htmlelt">
										<li>
											<dfn>Numero</dfn> 
											<dl><s:property value="ordinativoPagamento.numero"/></dl>
										</li>
										<li>
											<dfn>Importo</dfn> 
											<dl><s:property value="getText('struts.money.format', {ordinativoPagamento.importoOrdinativo})"/></dl>
										</li>
										<li>
											<dfn>Stato:</dfn> 
											<dl><s:property value="ordinativoPagamento.statoOperativoOrdinativo"/> dal <s:property value="%{ordinativoPagamento.dataInizioValidita}"/></dl>
										</li>
										<li>
											<dfn>Descrizione</dfn> 
											<dl><s:property value="ordinativoPagamento.descrizione"/></dl>
										</li>
										<li>
											<dfn>Bollo</dfn> 
											<dl><s:property value="ordinativoPagamento.codiceBollo.codice"/> - <s:property value="ordinativoPagamento.codiceBollo.descrizione"/></dl>
										</li>
										<li>
											<dfn>Commissioni</dfn> 
											<dl><s:property value="ordinativoPagamento.commissioneDocumento.codice"/> - <s:property value="ordinativoPagamento.commissioneDocumento.descrizione"/></dl>
										</li>
										<%-- <li>
											<dfn>Note al tesoriere</dfn> 
											<dl><s:property value="ordinativoPagamento.noteTesoriere.codice"/> - <s:property value="ordinativoPagamento.noteTesoriere.descrizione"/></dl>
										</li> --%>
										<li>
											<dfn>Note al tesoriere</dfn> 
											<dl><s:property value="ordinativoPagamento.note"/></dl>
										</li>
										<li>
											<dfn>Distinta</dfn> 
											<dl><s:property value="ordinativoPagamento.distinta.codice"/> - <s:property value="ordinativoPagamento.distinta.descrizione"/></dl>
										</li>
										<li>
											<dfn>Conto tesoriere</dfn> 
											<dl><s:property value="ordinativoPagamento.contoTesoreria.codice"/> - <s:property value="ordinativoPagamento.contoTesoreria.descrizione"/></dl>
										</li>
										<s:if test="null != ordinativoPagamento.contoTesoreriaSenzaCapienza">
											<li>
												<dfn>Conto tesoriere di pertinenza</dfn> 
												<dl><s:property value="ordinativoPagamento.contoTesoreriaSenzaCapienza.codice"/> - <s:property value="ordinativoPagamento.contoTesoreriaSenzaCapienza.descrizione"/></dl>
											</li>
										</s:if>
										<li>
											<dfn>Allegato cartaceo</dfn> 
											<dl>
												<s:if test="ordinativoPagamento.flagAllegatoCartaceo">
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
												<s:if test="ordinativoPagamento.flagBeneficiMultiplo">
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
												<s:if test="ordinativoPagamento.flagCopertura">
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
												<s:if test="ordinativoPagamento.flagDaTrasmettere">
													si
												</s:if>
												<s:else>
													no
												</s:else>
											</dl>
										</li>
										<li>
											<dfn>Avviso</dfn> 
											<dl><s:property value="ordinativoPagamento.tipoAvviso.codice"/> - <s:property value="ordinativoPagamento.tipoAvviso.descrizione"/></dl>
										</li>
										
										<li>
											<dfn>Tipo debito siope</dfn>
											<dl><s:property value="tipoDebitoSiope" />&nbsp;</dl>
										</li>
										
										
									</ul>
								</div>
								<div class="boxOrInRight">
									<p>Capitolo</p>
									<ul class="htmlelt">
										<li>
											<dfn>Anno</dfn> 
											<dl><s:property value="ordinativoPagamento.capitoloUscitaGestione.annoCapitolo"/></dl>
										</li>
										<li>
											<dfn>Capitolo</dfn> 
											<dl><s:property value="ordinativoPagamento.capitoloUscitaGestione.numeroCapitolo"/></dl>
										</li>
										<li>
											<dfn>Articolo</dfn> 
											<dl><s:property value="ordinativoPagamento.capitoloUscitaGestione.numeroArticolo"/></dl>
										</li>
										<li>
											<dfn>UEB</dfn> 
											<dl><s:property value="ordinativoPagamento.capitoloUscitaGestione.numeroUEB"/></dl>
										</li>
										<li>
											<dfn>Descrizione</dfn> 
											<dl><s:property value="ordinativoPagamento.capitoloUscitaGestione.descrizione"/></dl>
										</li>
										<li>
											<dfn>Struttura</dfn> 
											<dl><s:property value="ordinativoPagamento.capitoloUscitaGestione.strutturaAmministrativoContabile.codice"/> - <s:property value="ordinativoPagamento.capitoloUscitaGestione.strutturaAmministrativoContabile.descrizione"/></dl>
										</li>
										<li>
											<dfn>Tipo fin.</dfn> 
											<dl><s:property value="ordinativoPagamento.capitoloUscitaGestione.tipoFinanziamento.codice"/> - <s:property value="ordinativoPagamento.capitoloUscitaGestione.tipoFinanziamento.descrizione"/></dl>
										</li>
									</ul>
									<p>Soggetto</p>
									<ul class="htmlelt">
										<li>
											<dfn>Codice</dfn> 
											<dl><s:property value="ordinativoPagamento.soggetto.codiceSoggetto"/></dl>
										</li>
										<li>
											<dfn>Ragione Sociale</dfn> 
											<dl><s:property value="ordinativoPagamento.soggetto.denominazione"/></dl>
										</li>
										<li>
											<dfn>Partita IVA</dfn> 
											<dl><s:property value="ordinativoPagamento.soggetto.partitaIva"/></dl>
										</li>
										<li>
											<dfn>Cod. Fiscale</dfn> 
											<dl><s:property value="ordinativoPagamento.soggetto.codiceFiscale"/></dl>
										</li>
										<li>
											<dfn>Modalit&agrave; di pagamento</dfn> 
											<s:if test="ordinativoPagamento.soggetto.sediSecondarie[0] != null">
												<dl><s:property value="ordinativoPagamento.soggetto.sediSecondarie[0].modalitaPagamentoSoggettos[0].descrizione"/></dl>
											</s:if>
											<s:else>
												<dl><s:property value="ordinativoPagamento.soggetto.modalitaPagamentoList[0].descrizione"/></dl>
											</s:else>
										</li>
									</ul>
								</div>
							</div>
							<div class="boxOrSpan2">
								<div class="boxOrInLeft">
									<p>Transazione elementare</p>
									<ul class="htmlelt">
									
										<li>
											<dfn>Missione</dfn> 
											<dl><s:property value="codMissione"/>
											<s:if test="descMissione != null">
												 - <s:property value="descMissione"/>
											</s:if>
											</dl>
										</li>
										<li>
											<dfn>Programma</dfn> 
											<dl><s:property value="codProgramma"/>
											<s:if test="descProgramma != null">
												 - <s:property value="descProgramma"/>
											</s:if>
											</dl>
										</li>
										<li>
											<dfn>P.dC. Finanziario</dfn> 
											<dl><s:property value="ordinativoPagamento.codPdc"/>
											<s:if test="ordinativoPagamento.descPdc != null">
												 - <s:property value="ordinativoPagamento.descPdc"/>
											</s:if>
											</dl>
										</li>
										<li>
											<dfn>Cofog</dfn> 
											<dl><s:property value="ordinativoPagamento.codCofog"/>
											<s:if test="ordinativoPagamento.descCofog != null">
												 - <s:property value="ordinativoPagamento.descCofog"/>
											</s:if>
											</dl>
										</li>
										<li>
											<dfn>Codice transazione europea</dfn> 
											<dl><s:property value="ordinativoPagamento.codTransazioneEuropeaSpesa"/>
											<s:if test="ordinativoPagamento.descTransazioneEuropeaSpesa != null">
												 - <s:property value="ordinativoPagamento.descTransazioneEuropeaSpesa"/>
											</s:if>
											</dl>
										</li>
										<li>
											<dfn>SIOPE</dfn> 
											<dl><s:property value="ordinativoPagamento.codSiope"/>
											<s:if test="ordinativoPagamento.descCodSiope != null">
												 - <s:property value="ordinativoPagamento.descCodSiope"/>
											</s:if>
											</dl>
										</li>
										<li>
											<dfn>Ricorrente</dfn> 
											<dl><s:property value="ordinativoPagamento.codRicorrenteSpesa"/></dl>
										</li>
										<li>
											<dfn>Capitolo perimetro sanitario</dfn> 
											<dl><s:property value="ordinativoPagamento.codCapitoloSanitarioSpesa"/></dl>
										</li>
										<li>
											<dfn>Programma Pol. Reg. Unitaria</dfn> 
											<dl><s:property value="ordinativoPagamento.codPrgPolReg"/></dl>
										</li>
										
									</ul>
								</div>
								<div class="boxOrInRight">
									<p>Date transazione</p>
									<ul class="htmlelt">
										<li>
											<dfn>Data emissione</dfn> 
											<dl><s:property value="%{ordinativoPagamento.dataEmissione}"/></dl>
										</li>
										<li>
											<dfn>Data variazione </dfn> 
											<dl><s:property value="%{ordinativoPagamento.dataVariazione}"/></dl>
										</li>
										<li>
											<dfn>Data trasmissione </dfn> 
											<dl><s:property value="%{ordinativoPagamento.datiOrdinativoTrasmesso.dataTrasmissione}"/></dl>
										</li>
										<li>
											<dfn>Data spostamento</dfn> 
											<dl><s:property value="%{ordinativoPagamento.dataSpostamento}"/></dl>
										</li>
										<li>
											<dfn>Data quietanza</dfn> 
											<dl><s:property value="%{ordinativoPagamento.datiOrdinativoTrasmesso.dataQuietanza}"/></dl>
										</li>
										<li>
											<dfn>Numero quietanza</dfn> 
											<dl><s:property value="ordinativoPagamento.datiOrdinativoTrasmesso.numeroQuietanza"/></dl>
											<%-- <dfn>Cro</dfn> 
											<dl><s:property value="ordinativoPagamento.datiOrdinativoTrasmesso.cro"/></dl> --%>
										</li>
										<li>
											<dfn>Data firma</dfn> 
											<dl><s:property value="%{ordinativoPagamento.datiOrdinativoTrasmesso.dataFirma}"/></dl>
										</li>
										<li>
											<dfn>Firmatario</dfn> 
											<dl><s:property value="ordinativoPagamento.datiOrdinativoTrasmesso.firma"/></dl>
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
											<dl><s:property value="ordinativoPagamento.attoAmministrativo.tipoAtto.codice"/></dl>
										</li>
										<li>
											<dfn>Anno</dfn> 
											<dl><s:property value="ordinativoPagamento.attoAmministrativo.anno"/></dl>
										</li>
										<li>
											<dfn>Numero</dfn> 
											<dl><s:property value="ordinativoPagamento.attoAmministrativo.numero"/></dl>
										</li>
										<li>
											<dfn>Struttura</dfn> 
											<dl><s:property value="ordinativoPagamento.attoAmministrativo.StrutturaAmmContabile.codice"/> - <s:property value="ordinativoPagamento.attoAmministrativo.StrutturaAmmContabile.descrizione"/></dl>
										</li>
										<li>
											<dfn>Oggetto</dfn> 
											<dl><s:property value="ordinativoPagamento.attoAmministrativo.oggetto"/></dl>
										</li>
										<li>
											<dfn>Stato</dfn> 
											<dl><s:property value="ordinativoPagamento.attoAmministrativo.statoOperativo"/></dl>
										</li>
										
									</ul>
								</div>
								<div class="boxOrInRight">
									<p>Classificatori</p>
									<ul class="htmlelt">
										<s:include value="/jsp/ordinativo/include/classifGenericiConsultaOrdPag.jsp" />
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
							<h4>Inserimento: <s:property value="%{ordinativoPagamento.dataCreazioneSupport}"/> (<s:property value="ordinativoPagamento.loginCreazione"/>) - <span class="alLeft">Ultima modifica: <s:property value="%{ordinativoPagamento.dataModifica}"/>  (<s:property value="ordinativoPagamento.loginModifica"/>)</span></h4>

							<s:if test="%{(ordinativoPagamento.elencoSubOrdinativiDiPagamento != null) && (ordinativoPagamento.elencoSubOrdinativiDiPagamento.size > 0) }">
								<display:table htmlId="listaQuote" name="ordinativoPagamento.elencoSubOrdinativiDiPagamento" class="table table-hover tab_centered" summary="riepilogo" uid="quotaID" pagesize="5"
								 	requestURI="consultaOrdinativoPagamento.do"
								>
	
									<display:column title="Numero" headerClass="tabRow sBold" class="tabRow tab_Right">
										<s:property value="%{#attr.quotaID.numero}"/>
									</display:column>
									<display:column title="Liquidazione" headerClass="tabRow sBold" class="tabRow tab_Right">
										<s:property value="%{#attr.quotaID.liquidazione.annoLiquidazione}" /> - <s:property value="%{#attr.quotaID.liquidazione.numeroLiquidazione.intValue()}" />
									</display:column>
									<display:column title="Impegno" headerClass="tabRow sBold" class="tabRow tab_Right">
										<s:property value="%{#attr.quotaID.liquidazione.impegno.annoMovimento}" /> / <s:property value="%{#attr.quotaID.liquidazione.impegno.numero.intValue()}" />
										<s:if test="%{(#attr.quotaID.liquidazione.impegno.elencoSubImpegni != null) && (#attr.quotaID.liquidazione.impegno.elencoSubImpegni.size > 0) }">
											/ <s:property value="%{#attr.quotaID.liquidazione.impegno.elencoSubImpegni[0].numero.intValue()}" />
										</s:if>	
									</display:column>
									
									<display:column title="Documento" headerClass="tabRow sBold" class="tabRow tab_Right" >
										
										<s:property value="%{#attr.quotaID.subDocumentoSpesa.documento.tipoDocumento.codice}" /> / 
										<s:property value="%{#attr.quotaID.subDocumentoSpesa.documento.anno}" /> / 
										<s:property value="%{#attr.quotaID.subDocumentoSpesa.documento.numero}" /> / 
										<s:property value="%{#attr.quotaID.subDocumentoSpesa.documento.descrizione}" /> / 
										emesso il <s:property value="%{#attr.quotaID.subDocumentoSpesa.documento.dataEmissione}" /> - <s:property value="%{#attr.quotaID.subDocumentoSpesa.numero}" />
										
									</display:column>
									
									<display:column title="Descrizione quota" headerClass="tabRow sBold borderRight" class="borderRight">
										<s:property value="%{#attr.quotaID.descrizione}"/>
									</display:column>
									
									<s:if test="%{#attr.quotaID.liquidazione.cig != null && #attr.quotaID.liquidazione.cig != ''}">
										<display:column title="Cig" headerClass="tabRow sBold borderRight" class="borderRight">
											<s:property value="%{#attr.quotaID.liquidazione.cig}"/>
											<s:if test="!isCigValido(#attr.quotaID.liquidazione.cig)">
												*
											</s:if>
										</display:column>
									</s:if>
									<s:else>
										<display:column title="Motivazione assenza cig" headerClass="tabRow sBold borderRight" class="borderRight">
											<s:property value="%{#attr.quotaID.liquidazione.siopeAssenzaMotivazione.descrizione}"/>
										</display:column>
									</s:else>
									
									
									<display:column title="Cup" headerClass="tabRow sBold borderRight" class="borderRight">
										<s:property value="%{#attr.quotaID.liquidazione.cup}"/>
										<s:if test="!isCupValido(#attr.quotaID.liquidazione.cup)">
											*
										</s:if>
									</display:column>
									
									<display:column title="Data scadenza pagamento" headerClass="tabRow sBold borderRight" class="borderRight">
										<s:property value="%{#attr.quotaID.dataEsecuzionePagamento}"/>
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
											<th>&nbsp;</th>
											<th>&nbsp;</th>
											<th>&nbsp;</th>
											<th class="tab_Right" style="text-align:center"><s:property value="getText('struts.money.format', {ordinativoPagamento.importoOrdinativo})" default="-" /></th>
										<tr> 
									</display:footer>								
								</display:table>
								</div>
								
								
								<div id="ncsdf">* Se a fianco dei codici CIG e CUP appare un asterisco il formalismo del codice e' errato.</div>
								<br/>
								
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
							<h4>Inserimento: <s:property value="%{ordinativoPagamento.dataCreazioneSupport}"/> (<s:property value="ordinativoPagamento.loginCreazione"/>) - <span class="alLeft">Ultima modifica: <s:property value="%{ordinativoPagamento.dataModifica}"/>  (<s:property value="ordinativoPagamento.loginModifica"/>)</span></h4>

							<s:if test="%{(ordinativoPagamento.elencoRegolarizzazioneProvvisori != null) && (ordinativoPagamento.elencoRegolarizzazioneProvvisori.size > 0) }">
								<display:table htmlId="listaProvvisori" name="ordinativoPagamento.elencoRegolarizzazioneProvvisori" class="table table-hover tab_centered" summary="riepilogo" uid="provvisorioID" pagesize="5"
								 	requestURI="consultaOrdinativoPagamento.do"
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
											<th class="tab_Right" style="text-align:center"> <s:property value="getText('struts.money.format', {ordinativoPagamento.totImportiProvvisori})" default="-" /> </th>
											<th class="tab_Right" style="text-align:center"> <s:property value="getText('struts.money.format', {ordinativoPagamento.totImportiRegolarizzati})" default="-" /> </th>
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
					
							<s:if test="%{(ordinativoPagamento.elencoOrdinativiCollegati != null) && (ordinativoPagamento.elencoOrdinativiCollegati.size > 0) }">
								<display:table htmlId="listaOrdinativiCollegati" 
									name="ordinativoPagamento.elencoOrdinativiCollegati"
									partialList="true"
									sort="external"
									size="totaleOrdinativiCollegati" 
									class="table table-hover tab_centered" 
									summary="riepilogo" uid="ordCollID" pagesize="5"
								 	requestURI="consultaOrdinativoPagamento.do"
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
													    		<a id="linkQuotePag_<s:property value="%{#attr.ordCollID.numero}"/>" href="#ModQuote" class="linkQuotePagInc" data-toggle="modal">dettaglio quote</a>
															</li>
														</s:if>	
														<s:elseif test='%{(#attr.ordCollID.tipoOrdinativo.equals("S")) }'>
															<li>
													    		<a id="linkQuotePag_<s:property value="%{#attr.ordCollID.numero}"/>" href="#ModQuote" class="linkQuotePagPag" data-toggle="modal">dettaglio quote</a>
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
<!-- ************************************************************************************* -->
				<s:if test="%{(ordinativoPagamento.elencoOrdinativiCollegati != null) && (ordinativoPagamento.elencoOrdinativiCollegati.size > 0) }">
					<!-- CR 1909 se ci sono ordinativiCollegati e il dettaglio onere, abilito il tab Dettaglio Onere  -->
					<s:if test="activeTab == 'ordinativiCollegati'">
						<div class="hide tab-pane active" id="dettaglioOnere">
					</s:if>
					<s:else>
							<div class="hide tab-pane" id="dettaglioOnere">
					</s:else>
					
								<display:table 
								htmlId="listaOrdinativiCollegati" 
									partialList="true"
									sort="external"
									size="totaleOrdinativiCollegati" 
								name="ordinativoPagamento.elencoOrdinativiCollegati" 
								class="table table-hover tab_centered" 
								summary="riepilogo" uid="ordCollID" pagesize="5"
								 	requestURI="consultaOrdinativoPagamento.do">
	
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
											<s:property value="%{#attr.ordCollID.subordinativo.subDocumentoSpesa.documento.tipoDocumento.codice}" /> / 
											<s:property value="%{#attr.ordCollID.subordinativo.subDocumentoSpesa.documento.anno}" /> / 
											<s:property value="%{#attr.ordCollID.subordinativo.subDocumentoSpesa.documento.numero}" /> / 
											<s:property value="%{#attr.ordCollID.subordinativo.subDocumentoSpesa.documento.descrizione}" /> / 
											emesso il <s:property value="%{#attr.ordCollID.subordinativo.subDocumentoSpesa.documento.dataEmissione}" /> - <s:property value="%{#attr.ordCollID.subordinativo.subDocumentoSpesa.numero}" />
										</display:column>
																			
									</s:else>
									<display:column title="Importo" headerClass="tabRow sBold" class="tabRow tab_Right">
											<s:property value="%{#attr.ordCollID.subordinativo.importoAttuale}"/>
									</display:column>
									
								</display:table>
								</div>
								
							<s:else>
								<div id="ncsdf">Non ci sono record da visualizzare.</div>	
							</s:else>
						
					</div>
					
				</s:if>
					
<!-- ************************************************************************************* -->
					<s:include value="/jsp/ordinativo/include/modalOrdinativo.jsp" />
					<p><s:include value="/jsp/include/indietro.jsp" /></p>
					
				</s:form>
			</div>
		</div>
	</div>
	
	
<s:include value="/jsp/include/javascriptConsultaOrdinativo.jsp" />
	
<script type="text/javascript">
	
	
	$(".linkQuotePagInc").click(function() {
		var supportId = $(this).attr("id").split("_");
		$.ajax({
			//task-131 --> url: '<s:url method="dettaglioQuoteOrdinativoPagamentoInc"/>',
			url: '<s:url action="consultaOrdinativoPagamento_dettaglioQuoteOrdinativoPagamentoInc"/>',
			type: 'POST',
			data: 'numeroOrdinativoSelezionato=' + supportId[1],
		    success: function(data)  {
			    $("#divModQuote").html(data);
			}
		});
	});	
	
	$(".linkQuotePagPag").click(function() {
		var supportId = $(this).attr("id").split("_");
		$.ajax({
			//task-131 --> url: '<s:url method="dettaglioQuoteOrdinativoPagamentoPag"/>',
			url: '<s:url action="consultaOrdinativoPagamento_dettaglioQuoteOrdinativoPagamentoPag"/>',
			type: 'POST',
			data: 'numeroOrdinativoSelezionato=' + supportId[1],
		    success: function(data)  {
			    $("#divModQuote").html(data);
			}
		});
	});	
		
</script>
<s:include value="/jsp/include/footer.jsp" />