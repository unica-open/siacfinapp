<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="r" uri="http://www.csi.it/taglibs/remincl-1.0"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
				<s:form id="consultaCartaContabile" method="post" cssClass="form-horizontal">

<!-- ************************************************************************************* -->
					<h3>Carta <s:property value="cartaContabile.bilancio.anno"/>/<s:property value="cartaContabile.numero"/> - <s:property value="cartaContabile.oggetto"/></h3>
					
					<ul class="nav nav-tabs">
					<s:if test="activeTab == 'cartaContabile'">
						<li class="active"><a href="#cartaContabile" data-toggle="tab" id="linkTabCartaContabile" >Carta contabile</a></li>
						<li><a href="#righe" data-toggle="tab" id="linkTabRighe" >Righe</a></li>
					</s:if>
					<s:elseif test="activeTab == 'righe'">
						<li><a href="#cartaContabile" data-toggle="tab" id="linkTabCartaContabile">Carta contabile</a></li>
						<li class="active"><a href="#righe" data-toggle="tab" id="linkTabRighe" >Righe</a></li>
					</s:elseif>
					</ul>
					
					<div class="tab-content">
<!-- ************************************************************************************* -->
					<s:if test="activeTab == 'cartaContabile'">
						<div class="tab-pane active" id="cartaContabile">
					</s:if>
					<s:else>
						<div class="tab-pane" id="cartaContabile">
					</s:else>
							<h4>Inserito il <s:property value="%{cartaContabile.dataCreazioneSupport}"/> da <s:property value="cartaContabile.utenteCreazione"/> - <span class="alLeft">Aggiornato il <s:property value="%{cartaContabile.dataModifica}"/>  da <s:property value="cartaContabile.utenteModifica"/></span></h4>
							<div class="boxOrSpan2">
								<div class="boxOrInLeft">
									<p>Dati carta</p>
									<ul class="htmlelt">
										<li>
											<%--
											<dfn>Firma 1&nbsp;<a class="tooltip-test" title="<s:property value="intestazioneFirma1"/>" href="#" data-toggle="modal">
													<i class="icon-info-sign">&nbsp;<span class="nascosto"><s:property value="intestazioneFirma1"/></span></i>
												</a>
											</dfn>--%>
											<dfn>Firma 1&nbsp;<a href="#" data-toggle="tooltip" title="<s:property value="intestazioneFirma1" />" data-container="body"><i class="icon-info-sign"></i></a></dfn>
											<dl><s:property value="cartaContabile.firma1"/></dl>
										</li>
										<li>
											<%--
											<dfn>Firma 2&nbsp;<a class="tooltip-test" title="<s:property value="intestazioneFirma2"/>" href="#" data-toggle="modal">
													<i class="icon-info-sign">&nbsp;<span class="nascosto"><s:property value="intestazioneFirma2"/></span></i>
												</a>
											</dfn>--%>
											<dfn>Firma 2&nbsp;<a href="#" data-toggle="tooltip" title="<s:property value="intestazioneFirma2" />" data-container="body"><i class="icon-info-sign"></i></a></dfn>
											<dl><s:property value="cartaContabile.firma2"/></dl>
										</li>
										<li>
											<dfn>Numero</dfn> 
											<dl><s:property value="cartaContabile.numero"/></dl>
										</li>
										<li>
											<dfn>Importo Totale</dfn> 
											<dl><s:property value="getText('struts.money.format', {cartaContabile.importo})"/></dl>
										</li>
										<li>
											<dfn>Stato:</dfn> 
											<dl><s:property value="cartaContabile.statoOperativoCartaContabile"/> dal <s:property value="%{cartaContabile.dataStatoOperativo}"/></dl>
										</li>
										<li>
											<dfn>Descrizione</dfn> 
											<dl><s:property value="cartaContabile.oggetto"/></dl>
										</li>
										<li>
											<dfn>Causale Ordinativo</dfn> 
											<dl><s:property value="cartaContabile.causale"/></dl>
										</li>
										<li>
											<dfn>Note</dfn> 
											<dl><s:property value="cartaContabile.note"/></dl>
										</li>
										<li>
											<dfn>Motivo Urgenza</dfn> 
											<dl><s:property value="cartaContabile.motivoUrgenza"/></dl>
										</li>
									</ul>
								</div>
								<div class="boxOrInRight">
									<p>Provvedimento</p>
									<ul class="htmlelt">
										<li>
											<dfn>Tipo</dfn> 
											<dl><s:property value="cartaContabile.attoAmministrativo.tipoAtto.codice"/></dl>
										</li>
										<li>
											<dfn>Anno</dfn> 
											<dl><s:property value="cartaContabile.attoAmministrativo.anno"/></dl>
										</li>
										<li>
											<dfn>Numero</dfn> 
											<dl><s:property value="cartaContabile.attoAmministrativo.numero"/></dl>
										</li>
										<li>
											<dfn>Struttura</dfn> 
											<dl><s:property value="cartaContabile.attoAmministrativo.StrutturaAmmContabile.codice"/> - <s:property value="cartaContabile.attoAmministrativo.StrutturaAmmContabile.descrizione"/></dl>
										</li>
										<li>
											<dfn>Oggetto</dfn> 
											<dl><s:property value="cartaContabile.attoAmministrativo.oggetto"/></dl>
										</li>
									</ul>
								</div>
							</div>
							<div class="boxOrSpan2">
								<div class="boxOrInLeft">
									<p>Dati Valuta Estera</p>
									<ul class="htmlelt">
										<li>
											<dfn>Importo</dfn> 
											<s:if test="cartaContabile.listaCarteEstere[0] != null">
												<dl><s:property value="getText('struts.money.format', {cartaContabile.listaCarteEstere[0].totaleValutaEstera})"/> <s:property value="cartaContabile.listaCarteEstere[0].valuta.codice"/></dl>
											</s:if>
										</li>
										<li>
											<dfn>Da regolarizzare</dfn> 
											<s:if test="cartaContabile.listaCarteEstere[0] != null">
												<dl><s:property value="getText('struts.money.format', {cartaContabile.importoDaRegolarizzare})"/></dl>
											</s:if>
										</li>
										<li>
											<dfn>Causale Pagamento</dfn> 
											<s:if test="cartaContabile.listaCarteEstere[0] != null">
												<dl><s:property value="cartaContabile.listaCarteEstere[0].causalePagamento"/></dl>
											</s:if>
										</li>
										<li>
											<dfn>Spese e Commissioni</dfn> 
											<s:if test="cartaContabile.listaCarteEstere[0] != null">
												<dl><s:property value="cartaContabile.listaCarteEstere[0].commissioniEstero.descrizione"/></dl>
											</s:if>
										</li>
										<li>
											<dfn>Tipo Pagamento</dfn> 
											<dl>
												<s:if test="cartaContabile.listaCarteEstere[0] != null">
													<s:property value="cartaContabile.listaCarteEstere[0].tipologiaPagamento"/> 
													<s:if test="cartaContabile.listaCarteEstere[0].tipologiaPagamento.equalsIgnoreCase('Assegno') ">
														- <s:property value="cartaContabile.listaCarteEstere[0].metodoConsegna"/> 
													</s:if>
												</s:if>
											</dl>
										</li>
										<li>
											<dfn>Istruzioni Particolari</dfn> 
											<s:if test="cartaContabile.listaCarteEstere[0] != null">
												<dl><s:property value="cartaContabile.listaCarteEstere[0].istruzioni"/></dl>
											</s:if>
										</li>
										<li>
											<dfn>Esecutore diverso da Titolare</dfn> 
											<s:if test="cartaContabile.listaCarteEstere[0] != null">
												<dl><s:property value="cartaContabile.listaCarteEstere[0].diversoTitolare"/></dl>
											</s:if>
										</li>
									</ul>
								</div>
								<div class="boxOrInRight">
									<p>Date transazione</p>
									<ul class="htmlelt">
										<li>
											<dfn>Data emissione</dfn> 
											<dl><s:property value="%{cartaContabile.dataCreazione}"/></dl>
										</li>
										<li>
											<dfn>Data scadenza </dfn> 
											<dl><s:property value="%{cartaContabile.dataScadenza}"/></dl>
										</li>
										<%-- 
										<li>
											<dfn>Data esecuzione pagamento</dfn> 
											<dl><s:property value="%{cartaContabile.dataEsecuzionePagamento}"/></dl>
										</li>--%>
										<li>
											<dfn>Trasmissione</dfn> 
											<dl>Trasmessa il <s:property value="%{cartaContabile.dataTrasmissione}"/>. Con Registrazione n. <s:property value="cartaContabile.numRegistrazione"/>.</dl>
										</li>
									</ul>
								</div>
							</div>
						</div>
<!-- ************************************************************************************* -->
					<s:if test="activeTab == 'righe'">
						<div class="tab-pane active" id="righe">
					</s:if>
					<s:else>
						<div class="tab-pane" id="righe">
					</s:else>
							<s:if test="%{(cartaContabile.listaPreDocumentiCarta != null) && (cartaContabile.listaPreDocumentiCarta.size > 0) }">
								<h4>Righe: <s:property value="cartaContabile.listaPreDocumentiCarta.size"/></h4>
								
								<display:table htmlId="listaRighe" name="cartaContabile.listaPreDocumentiCarta" class="table tab_centered" summary="riepilogo" uid="rigaID" pagesize="5"
								 	requestURI="consultaCartaContabile.do"
								>
									<display:setProperty name="basic.show.header" value="false" />
									<display:column>
										<a id="openRiga<s:property value="%{#attr.rigaID.numero}"/>" data-toggle="modal" data-target="#refreshRiga<s:property value="%{#attr.rigaID.numero}"/>"></a>	        	
							        	<div id="refreshModPagamento<s:property value="%{#attr.rigaID.numero}"/>">     	
						        			<div class="step-pane active" id="riga<s:property value="%{#attr.rigaID.numero}"/>">
												<div class="accordion" >
													<div class="accordion-group">
														<div class="accordion-heading" style="text-align: left;">
															<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#riga<s:property value="%{#attr.rigaID.numero}"/>" href="#riga<s:property value="%{#attr.rigaID.numero}"/>TAB">
																Numero: <s:property value="%{#attr.rigaID.numero}"/> - Impegno: <s:property value="%{#attr.rigaID.impegno.annoMovimento}" /> / <s:property value="%{#attr.rigaID.impegno.numero.intValue()}" />
																<s:if test="%{(#attr.rigaID.impegno.elencoSubImpegni != null) && (#attr.rigaID.impegno.elencoSubImpegni.size > 0) }">
																	/ <s:property value="%{#attr.rigaID.impegno.elencoSubImpegni[0].numero.intValue()}" />
																</s:if>	
<%--                                                                     <s:if test="%{#attr.rigaID.subImpegno != null}"> --%>
<%--                                                                          / <s:property value="%{#attr.rigaID.subImpegno.numero.intValue()}" /> --%>
<%--                                                                     </s:if> --%>
																 - Soggetto: <s:property value="%{#attr.rigaID.soggetto.codiceSoggetto}" /> - <s:property value="%{#attr.rigaID.soggetto.denominazione}" />
																 - Importo: <s:property value="getText('struts.money.format', {#attr.rigaID.importo})" default="" />
																<span class="datiPagamento"></span><span class="icon">&nbsp;</span></a>
															</a>
														</div>
													
														<div id="riga<s:property value="%{#attr.rigaID.numero}"/>TAB" class="accordion-body collapse">
															<div class="accordion-inner">
																<fieldset class="form-horizontal">
																	<h4 style="text-align: left;">Inserito il <s:property value="%{#attr.rigaID.dataCreazioneSupport}"/> da <s:property value="#attr.rigaID.utenteCreazione"/> - <span class="alLeft">Aggiornato il <s:property value="%{#attr.rigaID.dataModifica}"/>  da <s:property value="#attr.rigaID.utenteModifica"/></span></h4>
																	<div class="boxOrSpan2">
																		<div class="boxOrInLeft">
																			<p>Dati riga</p>
																			<ul class="htmlelt">
																				<li>
																					<dfn>Numero</dfn> 
																					<dl><s:property value="%{#attr.rigaID.numero}"/></dl>
																				</li>
																				<li>
																					<dfn>Conto Tesoreria</dfn> 
																					<dl><s:property value="%{#attr.rigaID.contoTesoreria.descrizione}"/></dl>
																				</li>
																				<li>
																					<dfn>Descrizione</dfn> 
																					<dl><s:property value="%{#attr.rigaID.descrizione}"/></dl>
																				</li>
																				<li>
																					<dfn>Note</dfn> 
																					<dl><s:property value="%{#attr.rigaID.note}"/></dl>
																				</li>
																				<li>
																					<dfn>Importo Euro</dfn> 
																					<dl><s:property value="%{getText('struts.money.format', {#attr.rigaID.importo})}"/></dl>
																				</li>
																				<li>
																					<dfn>Importo <s:property value="cartaContabile.listaCarteEstere[0].valuta.codice"/></dfn> 
																					<dl><s:property value="%{getText('struts.money.format', {#attr.rigaID.importoValutaEstera})}"/></dl>
																				</li>
																				<li>
																					<dfn>Da Regolarizzare</dfn> 
																					<dl><s:property value="%{getText('struts.money.format', {#attr.rigaID.importoDaRegolarizzare})}"/></dl>
																				</li>
																			</ul>
																		</div>
																		<div class="boxOrInRight">
																			<p>Dati Impegno</p>
																			<ul class="htmlelt">
																				<li>
																					<dfn>Anno</dfn> 
																					<dl><s:property value="%{#attr.rigaID.impegno.annoMovimento}"/></dl>
																				</li>
																				<li>
																					<dfn>Impegno</dfn> 
																					<dl><s:property value="%{#attr.rigaID.impegno.numero.intValue()}"/></dl>
																				</li>
																				
																				
																				<li>
																					<dfn>Subimpegno</dfn> 
																					<dl><s:property value="%{#attr.rigaID.impegno.elencoSubImpegni[0].numero.intValue()}" /></dl>
<!-- 																						<dl>  -->
<%-- 																							<s:if test="%{#attr.rigaID.subImpegno != null}"> --%>
<%-- 																								<s:property value="%{#attr.rigaID.subImpegno.numero.intValue()}" /> --%>
<%-- 																					    	 </s:if> --%>
<%-- 																					    	 <s:else>&nbsp;</s:else> --%>
<!-- 																					    </dl>  -->
																				</li>
																			</ul>
																		</div>
																	</div>
																	<div class="boxOrSpan2">
																		<div class="boxOrInLeft">
																			<p>Capitolo</p>
																			<ul class="htmlelt">
																				<li>
																					<dfn>Anno</dfn> 
																					<dl><s:property value="%{#attr.rigaID.impegno.capitoloUscitaGestione.annoCapitolo}"/></dl>
																				</li>
																				<li>
																					<dfn>Capitolo</dfn> 
																					<dl><s:property value="%{#attr.rigaID.impegno.capitoloUscitaGestione.numeroCapitolo}"/></dl>
																				</li>
																				<li>
																					<dfn>Articolo</dfn> 
																					<dl><s:property value="%{#attr.rigaID.impegno.capitoloUscitaGestione.numeroArticolo}"/></dl>
																				</li>
																				<li>
																					<dfn>UEB</dfn> 
																					<dl><s:property value="%{#attr.rigaID.impegno.capitoloUscitaGestione.numeroUEB}"/></dl>
																				</li>
																				<li>
																					<dfn>Descrizione</dfn> 
																					<dl><s:property value="%{#attr.rigaID.impegno.capitoloUscitaGestione.descrizione}"/></dl>
																				</li>
																				<li>
																					<dfn>Struttura</dfn> 
																					<dl><s:property value="%{#attr.rigaID.impegno.capitoloUscitaGestione.strutturaAmministrativoContabile.codice}"/> - <s:property value="%{#attr.rigaID.impegno.capitoloUscitaGestione.strutturaAmministrativoContabile.descrizione}"/></dl>
																				</li>
																				<li>
																					<dfn>Tipo fin.</dfn> 
																					<dl><s:property value="%{#attr.rigaID.impegno.capitoloUscitaGestione.tipoFinanziamento.codice}"/> - <s:property value="%{#attr.rigaID.impegno.capitoloUscitaGestione.tipoFinanziamento.descrizione}"/></dl>
																				</li>
																			</ul>
																		</div>
																		<div class="boxOrInRight">
																			<p>Creditore</p>
																			<ul class="htmlelt">
																				<li>
																					<dfn>Codice</dfn> 
																					<dl><s:property value="%{#attr.rigaID.soggetto.codiceSoggetto}"/></dl>
																				</li>
																				<li>
																					<dfn>Ragione Sociale</dfn> 
																					<dl><s:property value="%{#attr.rigaID.soggetto.denominazione}"/></dl>
																				</li>
																				<li>
																					<dfn>Partita IVA</dfn> 
																					<dl><s:property value="%{#attr.rigaID.soggetto.partitaIva}"/></dl>
																				</li>
																				<li>
																					<dfn>Cod. Fiscale</dfn> 
																					<dl><s:property value="%{#attr.rigaID.soggetto.codiceFiscale}"/></dl>
																				</li>
																				<li>
																					<dfn>Modalit&agrave; di pagamento</dfn> 
																					<s:if test="%{#attr.rigaID.soggetto.sediSecondarie[0] != null}">
																						<dl><s:property value="%{#attr.rigaID.soggetto.sediSecondarie[0].modalitaPagamentoSoggettos[0].descrizioneInfo.descrizioneArricchita}"/></dl>
																					</s:if>
																					<s:else>
																						<dl><s:property value="%{#attr.rigaID.soggetto.modalitaPagamentoList[0].descrizioneInfo.descrizioneArricchita}"/></dl>
																					</s:else>
																				</li>
																			</ul>
																		</div>
																	</div>
																	<div class="boxOrSpan2">
																		<h4>Elenco Pagamenti</h4>
																		<s:if test="%{(#attr.rigaID.listaSubDocumentiSpesaCollegati != null) && (#attr.rigaID.listaSubDocumentiSpesaCollegati.size > 0) }">
																		<c:set var="parentRow" value="${rigaID_rowNum -1}" />
																			<display:table htmlId="listaPagamenti_%{#attr.rigaID.numero}" name="${cartaContabile.listaPreDocumentiCarta[parentRow].listaSubDocumentiSpesaCollegati}" class="table table-hover tab_centered" summary="riepilogo" uid="pagamentoID" pagesize="5"
																			 	requestURI="consultaCartaContabile.do"
																			>
												
																				<display:column title="Documento" headerClass="tabRow sBold" class="tabRow tab_Right">
																					<s:property value="%{#attr.pagamentoID.documento.anno}"/> / <s:property value="%{#attr.pagamentoID.documento.tipoDocumento.codice}"/> / <s:property value="%{#attr.pagamentoID.documento.numero}"/> del <s:property value="%{#attr.pagamentoID.documento.dataEmissione}"/>
																				</display:column>
																				<display:column title="Descrizione" headerClass="tabRow sBold" class="tabRow tab_Right">
																					<s:property value="%{#attr.pagamentoID.descrizione}" />
																				</display:column>
																				<display:column title="Provvedimento" headerClass="tabRow sBold" class="tabRow tab_Right">
																					<s:property value="%{#attr.pagamentoID.attoAmministrativo.anno}"/> / <s:property value="%{#attr.pagamentoID.attoAmministrativo.numero}"/> / <s:property value="%{#attr.pagamentoID.attoAmministrativo.tipoAtto.codice}"/> / <s:property value="%{#attr.pagamentoID.attoAmministrativo.strutturaAmmContabile.codice}"/>
																				</display:column>
																				<display:column title="Importo" headerClass="tabRow sBold borderRight" class="borderRight">
																					<s:property value="getText('struts.money.format', {#attr.pagamentoID.importo})" default="-" />
																				</display:column>
																												
																			</display:table>
																			
																		</s:if>
																		<s:else>
																			<div id="ncsdf">Non ci sono record da visualizzare.</div>
																		</s:else>
																	</div>
																</fieldset >						
															</div>
														</div>
													</div>
												</div>
											</div>
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
					<s:if test="%{fromInserisci}"> 										
						<p><a class="btn" href="gestioneCartaStep1_annulla.do">indietro</a></p>
					</s:if>	
					<s:else>
						<p><s:include value="/jsp/include/indietro.jsp" /></p>
					</s:else>	
					
				</s:form>
			</div>
		</div>
	</div>
	
<script type="text/javascript">

	$(document).ready(function() {
		$('*[data-toggle="tooltip"]').tooltip();
	});
 
	$("#linkTabCartaContabile").click(function() {
		$.ajax({
			//task-131 url: '<s:url method="cambiaTabFolder"/>',
			url: '<s:url action="consultaCartaContabile_cambiaTabFolder"/>',
			type: 'POST',
			data: 'tabFolder=tabCartaContabile',
		    success: function(data)  {
			}
		});
	});	
	
	$("#linkTabRighe").click(function() {
		$.ajax({
			//task-131 url: '<s:url method="cambiaTabFolder"/>',
			url: '<s:url action="consultaCartaContabile_cambiaTabFolder"/>',
			type: 'POST',
			data: 'tabFolder=tabRighe',
		    success: function(data)  {
			}
		});
	});	
	
	

</script>	
	
	
<s:include value="/jsp/include/footer.jsp" />