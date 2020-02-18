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
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:include value="/jsp/include/actionMessagesErrors.jsp" />
				<s:form id="consultaOrdinativoPagamento" method="post" cssClass="form-horizontal">

					<!-- ************************************************************************************* -->
					<h3>
						Consulta provvisorio
						<s:property value="provvisorioCassa.anno" />
						/
						<s:property value="provvisorioCassa.numero" />
						del
						<s:property value="%{provvisorioCassa.dataEmissione}" />
						- <s:property value="tipoDocumentoProv" /> -
						
						<s:property value="provvisorioCassa.causale" />  
						
					</h3>

					<ul class="nav nav-tabs">
						<li <s:if test="activeTab == 'provvisorioCassa'">class="active"</s:if>>
							<a href="#provvisorioCassa" data-toggle="tab" id="linkTabProvvisorioCassa">Provvisorio di cassa</a>
						</li>
						<li <s:if test="activeTab == 'documenti'">class="active"</s:if>>
							<a href="#documenti" data-toggle="tab" id="linkTabDocumenti">Documenti</a>
						</li>
						<li <s:if test="activeTab == 'ordinativi'">class="active"</s:if>>
							<a href="#ordinativi" data-toggle="tab" id="linkTabOrdinativi">Ordinativi</a>
						</li>
					</ul>

					<div class="tab-content">
						<%-- ************************************************************************************* --%>

						<div class="tab-pane <s:if test="activeTab == 'provvisorioCassa'">active</s:if>" id="provvisorioCassa">
							<h4>Dati principali</h4>
							<div class="boxOrSpan2">
								<div class="boxOrInLeft">
									<p>Provvisorio</p>
									<ul class="htmlelt">
										<li>
											<dfn>Inserito il</dfn>
											<dl><s:property value="%{provvisorioCassa.dataCreazioneProvv}" /></dl>
										</li>
										<li>
											<dfn>Aggiornato il</dfn>
											<dl><s:property value="%{provvisorioCassa.dataModificaProvv}" /></dl>
										</li>
										<li>
											<dfn>da</dfn>
											<dl>
												<s:property value="provvisorioCassa.loginOperazione" />
											</dl>
										</li>
										<li>
											<dfn>Conto evidenza</dfn>
											<dl><s:property value="provvisorioCassa.codiceContoEvidenza" /> - <s:property value="provvisorioCassa.descrizioneContoEvidenza" /></dl>
										</li>
										<li>
											<dfn>Data Emissione</dfn>
											<dl><s:property value="%{provvisorioCassa.dataEmissione}" /></dl>
										</li>
										<li>
											<dfn>Data Trasmissione</dfn>
											<dl><s:property value="%{provvisorioCassa.dataTrasmissione}" /></dl>
										</li>
										
										<s:if test="tipoDocumentoProv eq 'Entrata'">										
										<li>
											<dfn>Data invio al Servizio (SAC)</dfn>
											<dl><s:property value="%{provvisorioCassa.dataInvioServizio}" /></dl>
										</li>

										<li>
											<dfn>Data presa in carico dal Servizio</dfn>
											<dl><s:property value="%{provvisorioCassa.dataPresaInCaricoServizio}" /></dl>
										</li>
										<li>
											<dfn>Data rifiuto per errata attribuzione</dfn>
											<dl><s:property value="%{provvisorioCassa.dataRifiutoErrataAttribuzione}" /></dl>
										</li>
										</s:if>

										<li>
											<dfn>Data Regolarizzazione</dfn>
											<dl><s:property value="%{provvisorioCassa.dataRegolarizzazione}" /></dl>
										</li>
										<li>
											<dfn>Importo</dfn>
											<dl><s:property value="getText('struts.money.format', {provvisorioCassa.importo})" /></dl>
										</li>
										<li>
											<dfn>Soggetto</dfn>
											<dl><s:property value="provvisorioCassa.denominazioneSoggetto" /></dl>
										</li>
										<li>
											<dfn>SAC</dfn>
											<dl><s:property value="provvisorioCassa.strutturaAmministrativoContabile.codice" /> - <s:property value="provvisorioCassa.strutturaAmministrativoContabile.descrizione" /></dl>
										</li>
										<li>
											<dfn>Note</dfn>
											<dl><s:property value="provvisorioCassa.note" />&nbsp;</dl>
										</li>
									</ul>
								</div>
								<div class="boxOrInRight">
									<p>Riepilogo</p>
									<ul class="htmlelt">
										<li>
											<dfn>Numero quote documento collegate</dfn>
											<dl>
												<s:if test="isDiSpesa()">
													<s:property value="resultSizeSubDocSpesa" />
												</s:if>
												<s:else>
													<s:property value="resultSizeSubDocEntrata" />
												</s:else>
											</dl>
										</li>
										<li>
											<dfn>Importo documenti collegati</dfn>
											<dl>
												<s:property value="getText('struts.money.format', {totImportiSubDoc})" />
											</dl>
										</li>
										<li>
											<dfn>Numero ordinativi collegati</dfn>
											<dl>
												<s:if test="isDiSpesa()">
														<s:property value="resultSizeSubOrdPag" />
													</s:if>
													<s:else>
														<s:property value="resultSizeSubOrdInc" />
												</s:else>
											</dl>
										</li>
										<li>
											<dfn>Importo ordinativi collegati</dfn>
											<dl>
												<s:property value="getText('struts.money.format', {totImportiOrdinativi})" />
											</dl>
										</li>
									</ul>
								</div>
							</div>
							
						</div>
						<%-- ************************************************************************************* --%>
						<div class="tab-pane <s:if test="activeTab == 'documenti'">active</s:if>" id="documenti">

							<s:if test="isDiSpesa()">
								<h4>Sub Documenti di Spesa</h4>

								<s:if test="%{(listaSubDocSpesa != null) && (listaSubDocSpesa.size > 0) }">

									<display:table
										name="sessionScope.RISULTATI_RICERCA_QUOTE_SPESA"
										class="table table-hover tab_left"
										summary="riepilogo quote spesa"
										pagesize="10"
										partialList="true"
										size="resultSizeSubDocSpesa"
										keepStatus="${status}"
										clearStatus="${status}"
										requestURI="consultaProvvisorioCassa.do"
										uid="ricercaQuoteSpesaID">

										<display:column title="Documento">
											<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="<s:property value="%{#attr.ricercaQuoteSpesaID.descrizione}"/>">
												<s:property value="%{#attr.ricercaQuoteSpesaID.documento.anno}"/>/<s:property value="%{#attr.ricercaQuoteSpesaID.documento.tipoDocumento.codice}"/>
												/<s:property value="%{#attr.ricercaQuoteSpesaID.documento.numero}"/>
											</a>
										</display:column>	 
										
										<display:column title="Quota" property="numero"></display:column>
										
										<display:column title="Data" property="documento.dataEmissione" format="{0,date,dd/MM/yyyy}"/>
										
										<display:column title="Importo">
											<a href="#" data-trigger="hover" rel="popover" title="Importo documento" data-content="<s:property value="%{converterEuro(#attr.ricercaQuoteSpesaID.documento.importo)}"/>">
												<s:property value="%{converterEuro(#attr.ricercaQuoteSpesaID.importo)}" />
											</a>
										</display:column>
										
										<display:column title="Soggetto"><s:property value="%{#attr.ricercaQuoteSpesaID.documento.soggetto.codiceSoggetto}"/>-<s:property value="%{#attr.ricercaQuoteSpesaID.documento.soggetto.denominazione}"/></display:column>
										
										<display:column title="Stato">
											<a href="#" data-trigger="hover" rel="popover" title="Stato" data-content="<s:property value="%{#attr.ricercaQuoteSpesaID.documento.statoOperativoDocumento.descrizione}"/>">
												<s:property value="%{#attr.ricercaQuoteSpesaID.documento.statoOperativoDocumento.codice}"/>
											</a>
										</display:column>
										

									    <display:column title="Pagato CEC">
										   <s:if test="%{#attr.ricercaQuoteSpesaID.pagatoInCEC == true}">
								              	SI
								              </s:if>                   
								              <s:else>
								             	NO
								              </s:else>
										</display:column>

									</display:table>

								</s:if>
								<s:else>
									<div id="ncsdf">Non ci sono record da visualizzare.</div>
								</s:else>
							</s:if>
							<s:else>
								<h4>Sub Documenti di Entrata</h4>

								<s:if test="%{(listaSubDocEntrata != null) && (listaSubDocEntrata.size > 0) }">
									<display:table
										name="sessionScope.RISULTATI_RICERCA_QUOTE_ENTRATA"
										class="table table-hover tab_left"
										summary="riepilogo quote spesa"
										pagesize="10"
										partialList="true"
										size="resultSizeSubDocEntrata"
										keepStatus="${status}"
										clearStatus="${status}"
										requestURI="consultaProvvisorioCassa.do"
										uid="ricercaQuoteEntrataID">

										<display:column title="Documento">
											<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="<s:property value="%{#attr.ricercaQuoteEntrataID.descrizione}"/>">
												<s:property value="%{#attr.ricercaQuoteEntrataID.documento.anno}"/>/<s:property value="%{#attr.ricercaQuoteEntrataID.documento.tipoDocumento.codice}"/>
												/<s:property value="%{#attr.ricercaQuoteEntrataID.documento.numero}"/>
											</a>
										</display:column>
										
										<display:column title="Quota" property="numero"></display:column>
										
										<display:column title="Data" property="documento.dataEmissione" format="{0,date,dd/MM/yyyy}"/>
										
										<display:column title="Importo">
											<a href="#" data-trigger="hover" rel="popover" title="Importo documento" data-content="<s:property value="%{converterEuro(#attr.ricercaQuoteEntrataID.documento.importo)}"/>">
												<s:property value="%{converterEuro(#attr.ricercaQuoteEntrataID.importo)}" />
											</a>
										</display:column>

										
										<display:column title="Soggetto"><s:property value="%{#attr.ricercaQuoteEntrataID.documento.soggetto.codiceSoggetto}"/>-<s:property value="%{#attr.ricercaQuoteEntrataID.documento.soggetto.denominazione}"/></display:column>
										
										<display:column title="Stato">
											<a href="#" data-trigger="hover" rel="popover" title="Stato" data-content="<s:property value="%{#attr.ricercaQuoteEntrataID.documento.statoOperativoDocumento.descrizione}"/>">
												<s:property value="%{#attr.ricercaQuoteEntrataID.documento.statoOperativoDocumento.codice}"/>
											</a>
										</display:column>

									</display:table>

								</s:if>
								<s:else>
									<div id="ncsdf">Non ci sono record da visualizzare.</div>
								</s:else>
							</s:else>
						
						<%-- Martellata: vedere come fare in base alla SIAC-6199 --%>
						<s:if test="%{((listaSubDocEntrata != null) && (listaSubDocEntrata.size > 0)) || ((listaSubDocSpesa != null) && (listaSubDocSpesa.size > 0))}">
							</div>
						</s:if>						
							
						
						</div>
						<%-- ************************************************************************************* --%>
						<div class="tab-pane <s:if test="activeTab == 'ordinativi'">active</s:if>" id="ordinativi">
							
						<s:if test="isDiSpesa()">
							<h4>Ordinativi di Spesa</h4>

							<s:if test="%{(listaOrdPag != null) && (listaOrdPag.size > 0) }">
								
								<display:table name="sessionScope.RISULTATI_RICERCA_ORD_PAG"  
					                class="table table-hover tab_left" 
					                summary="riepilogo quote spesa"
					                pagesize="10" 
					                
					                partialList="true"
					                size="resultSizeSubOrdPag"
					                keepStatus="${status}" clearStatus="${status}"
					                
					                requestURI="consultaProvvisorioCassa.do"
					                
									uid="ricercaOrdPagID">
	
									<display:column title="Mandato"  >
										<s:property value="%{#attr.ricercaOrdPagID.numero}" />
									</display:column>
									
									<display:column title="Data emissione">
								 		<s:property value="%{#attr.ricercaOrdPagID.dataEmissione}" />
									 </display:column>
									 
									<display:column title="Creditore">	
										<s:property value="%{#attr.ricercaOrdPagID.soggetto.codiceSoggetto}" /> - <s:property value="%{#attr.ricercaOrdPagID.soggetto.denominazione}" /> 
									</display:column>
									
									<display:column title="Modalit&agrave; pagamento">
									
										<s:if test="%{#attr.ricercaOrdPagID.soggetto.sediSecondarie[0] != null}">
											<a href="#" data-trigger="hover" rel="popover" data-container="#modTAB" 
									    	title="${attr.ricercaOrdPagID.soggetto.sediSecondarie[0].modalitaPagamentoSoggettos[0].modalitaAccreditoSoggetto.codice}&nbsp;-&nbsp;${attr.ricercaOrdPagID.soggetto.sediSecondarie[0].modalitaPagamentoSoggettos[0].modalitaAccreditoSoggetto.descrizione}" 
									    	data-content="${attr.ricercaOrdPagID.soggetto.sediSecondarie[0].modalitaPagamentoSoggettos[0].descrizioneForPopOver}">
									    	<s:property value="%{#attr.ricercaOrdPagID.soggetto.sediSecondarie[0].modalitaPagamentoSoggettos[0].descrizioneInfo.descrizioneArricchita}" /></a>
										</s:if>
										<s:else>
											<a href="#" data-trigger="hover" rel="popover" data-container="#modTAB" 
									    	title="${attr.ricercaOrdPagID.soggetto.modalitaPagamentoList[0].modalitaAccreditoSoggetto.codice}&nbsp;-&nbsp;${attr.ricercaOrdPagID.soggetto.modalitaPagamentoList[0].modalitaAccreditoSoggetto.descrizione}" 
									    	data-content="${attr.ricercaOrdPagID.soggetto.modalitaPagamentoList[0].descrizioneForPopOver}">
									    	<s:property value="%{#attr.ricercaOrdPagID.soggetto.modalitaPagamentoList[0].descrizioneInfo.descrizioneArricchita}" /></a>
										</s:else>
								    	
							    	</display:column>
							    	
							    	<display:column title="Stato">
<%-- 							    		<s:property value="%{#attr.ricercaOrdPagID.statoOperativoOrdinativo}" /> --%>
							    		<s:property value="%{#attr.ricercaOrdPagID.ordinaordinativoPagamentotivoIncasso.codStatoOperativoOrdinativo}" />
							    	</display:column>
							    	
				    				<display:column title="Data quietanza">
								 		<s:property value="%{#attr.ricercaOrdPagID.dataSpostamento}" />
								 	</display:column>
								 	
								 	<display:column title="Provvedimento" >
										<a href="#" data-trigger="hover" rel="popover" title="Oggetto" data-content="<s:property value="%{#attr.ricercaOrdPagID.attoAmministrativo.oggetto}"/>">
											<s:property value="%{#attr.ricercaOrdPagID.attoAmministrativo.anno}"/> / 
											<s:property value="%{#attr.ricercaOrdPagID.attoAmministrativo.numero}"/>/
											<s:property value="%{#attr.ricercaOrdPagID.attoAmministrativo.strutturaAmmContabile.codice}"/>/
											<s:property value="%{#attr.ricercaOrdPagID.attoAmministrativo.tipoAtto.codice}"/>
										</a>
									</display:column>
								 	
								 	<display:column title="Importo">
										<s:property value="%{converterEuro(#attr.ricercaOrdPagID.importoRegolarizzato)}" />
									</display:column>	 
									
								</display:table>
								
							</s:if>
							<s:else>
								<div id="ncsdf">Non ci sono record da visualizzare.</div>
							</s:else>
						</s:if>
						<s:else>
							<h4>Ordinativi di Entrata</h4>

							<s:if test="%{(listaOrdInc != null) && (listaOrdInc.size > 0) }">
								
								<display:table name="sessionScope.RISULTATI_RICERCA_ORD_INC"  
					                class="table table-hover tab_left" 
					                summary="riepilogo quote spesa"
					                pagesize="10" 
					                
					                partialList="true"
					                size="resultSizeSubOrdInc"
					                keepStatus="${status}" clearStatus="${status}"
					                
					                requestURI="consultaProvvisorioCassa.do"
					                
									uid="ricercaOrdIncID">
	
									<display:column title="Reversale" >
										<s:property value="%{#attr.ricercaOrdIncID.numero}" />
									</display:column>
									
									<display:column title="Data emissione">
								 		<s:property value="%{#attr.ricercaOrdIncID.dataEmissione}" />
									 </display:column>
									 
									<display:column title="Creditore">	
										<s:property value="%{#attr.ricercaOrdIncID.soggetto.codiceSoggetto}" /> - <s:property value="%{#attr.ricercaOrdIncID.soggetto.denominazione}" /> 
									</display:column>
									
									<display:column title="Stato">
<%-- 							    		<s:property value="%{#attr.ricercaOrdIncID.statoOperativoOrdinativo}" /> --%>
							    		<s:property value="%{#attr.ricercaOrdIncID.codStatoOperativoOrdinativo}" />
							    	</display:column>
							    	
							    	<display:column title="Data quietanza">
								 		<s:property value="%{#attr.ricercaOrdIncID.dataSpostamento}" />
								 	</display:column>	
								 	
									<display:column title="Provvedimento" >
										<a href="#" data-trigger="hover" rel="popover" title="Oggetto" data-content="<s:property value="%{#attr.ricercaOrdIncID.attoAmministrativo.oggetto}"/>">
											<s:property value="%{#attr.ricercaOrdIncID.attoAmministrativo.anno}"/> / 
											<s:property value="%{#attr.ricercaOrdIncID.attoAmministrativo.numero}"/>/
											<s:property value="%{#attr.ricercaOrdIncID.attoAmministrativo.strutturaAmmContabile.codice}"/>/
											<s:property value="%{#attr.ricercaOrdIncID.attoAmministrativo.tipoAtto.codice}"/>
											
										</a>
									</display:column>
									
									 <display:column title="Importo">
										<s:property value="%{converterEuro(#attr.ricercaOrdIncID.importoRegolarizzato)}" />
									</display:column>
									
								</display:table>
								
							</s:if>
							<s:else>
								<div id="ncsdf">Non ci sono record da visualizzare.</div>
							</s:else>
						</s:else>
							
							
						</div>
						<%-- ************************************************************************************* --%>



						<s:include value="/jsp/ordinativo/include/modalOrdinativo.jsp" />

					</div>
						<p>						  
							<s:include value="/jsp/include/indietro.jsp" />
						</p>

				</s:form>
			</div>
		</div>
	</div>


	<s:include value="/jsp/include/javascriptConsultaOrdinativo.jsp" />

	<script type="text/javascript">
		$("#linkTabProvvisorioCassa").click(function() {
			$.ajax({
				url : '<s:url method="cambiaTabFolder"/>',
				type : 'POST',
				data : 'tabFolder=tabProvvisorioCassa',
				success : function(data) {
				}
			});
		});

		$("#linkTabDocumenti").click(function() {
			$.ajax({
				url : '<s:url method="cambiaTabFolder"/>',
				type : 'POST',
				data : 'tabFolder=tabDocumenti',
				success : function(data) {
				}
			});
		});

		$("#linkTabOrdinativi").click(function() {
			$.ajax({
				url : '<s:url method="cambiaTabFolder"/>',
				type : 'POST',
				data : 'tabFolder=tabOrdinativi',
				success : function(data) {
				}
			});
		});
	</script>
	<s:include value="/jsp/include/footer.jsp" />