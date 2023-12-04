<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	<h4>Modifica <s:property value="modificaDettaglio.numero" /> -<s:if test="modificaDettaglio.descSub == null">Associato all'<s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/></s:if><s:else>Associato al Sub <s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/></s:else>  - <s:property value="modificaDettaglio.descrizione" /></h4>
</div>
<div class="modal-body">
	<fieldset class="form-horizontal">
		<p>Data inserimento: <s:property value="%{modificaDettaglio.dataInserimento}" /> - Data ultima modifica: <s:property value="%{modificaDettaglio.dataModifica}" /></p>
		<dl class="dl-horizontal">
			<dt>Inserito</dt>
			<dd>il <s:property value="%{modificaDettaglio.dataInserimento}" /> da <s:property value="modificaDettaglio.utenteCreazione" />&nbsp;</dd>
			<dt>Aggiornato</dt>
			<dd>il <s:property value="%{modificaDettaglio.dataModifica}" /> da <s:property value="modificaDettaglio.utenteModifica" />&nbsp;</dd>
			<dt>Stato</dt>
			<dd><s:property value="modificaDettaglio.statoOperativo" /> dal <s:property value="%{modificaDettaglio.dataStatoOperativo}" />&nbsp;</dd>
			<dt>Dati <s:property value="modificaDettaglio.descMovimentoLower" />o</dt>
			<dd><s:property value="modificaDettaglio.descMain" />&nbsp;</dd>
		<s:if test="modificaDettaglio.descSub != null">
			<dt>Dati sub<s:property value="modificaDettaglio.descMovimentoLower" />o</dt>
			<dd><s:property value="modificaDettaglio.descSub" />&nbsp;</dd>
		</s:if>
			<dt>Provvedimento</dt>
			<dd>
			<s:if test="modificaDettaglio.provvedimento.tipo != null">
				<s:property value="modificaDettaglio.provvedimento.anno" /> / <s:property value="modificaDettaglio.provvedimento.numero" /> - <s:property value="modificaDettaglio.provvedimento.tipo" /> - <s:property value="modificaDettaglio.provvedimento.oggetto" /> - <s:property value="modificaDettaglio.provvedimento.struttura" /> - Stato: <s:property value="modificaDettaglio.provvedimento.stato" /> 
				- Blocco: di operativit&agrave; contabile del provvedimento:&nbsp;
				<!--  - <s:if test="modificaDettaglio.provvedimento.bloccoRagioneria=='true'">Si</s:if>
							<s:if test="modificaDettaglio.provvedimento.bloccoRagioneria=='false'">No</s:if>
							<s:if test="modificaDettaglio.provvedimento.bloccoRagioneria=='null'">N/A</s:if> &nbsp; -->
			</s:if>
			&nbsp;
			</dd>
			<dt>Motivo</dt>
			<dd><s:property value="modificaDettaglio.motivo" />&nbsp;</dd>
			<!-- SIAC-8834 -->
			<s:if test="modificaDettaglio.impegnoAssociato != null">
				<dt>Impegno contestuale</dt>
					<dd><s:property value="modificaDettaglio.impegnoAssociato.annoMovimento" /> / <s:property value="modificaDettaglio.impegnoAssociato.numero.intValue()" />&nbsp;</dd>
			</s:if>
		<s:if test="modificaDettaglio.importo != null">
			<dt>Importo modifica</dt>
			<dd><s:property value="getText('struts.money.format', {modificaDettaglio.importo})" /></dd>
		</s:if>	
		<s:else>	
		<s:if test="modificaDettaglio.soggettoAttuale.codice != null">
			<dt>Soggetto attuale</dt>
			<dd>
				<s:property value="modificaDettaglio.soggettoAttuale.codice" /> - <s:property value="modificaDettaglio.soggettoAttuale.denominazione" />
			<s:if test="modificaDettaglio.soggettoAttuale.codiceFiscale != null"> - CF: <s:property value="modificaDettaglio.soggettoAttuale.codiceFiscale" /> </s:if>
			<s:if test="modificaDettaglio.soggettoAttuale.partitaIva != null"> - PIVA: <s:property value="modificaDettaglio.soggettoAttuale.partitaIva" /> </s:if> 
			&nbsp;
			</dd>
		</s:if>
		<s:elseif test="modificaDettaglio.soggettoAttuale.classeSoggettoCodice != null">
			<dt>Classe soggetto attuale</dt>
			<dd><s:property value="modificaDettaglio.soggettoAttuale.classeSoggettoCodice" /> - <s:property value="modificaDettaglio.soggettoAttuale.classeSoggettoDescrizione" />&nbsp;</dd>
		</s:elseif> 
		<s:if test="modificaDettaglio.soggettoPrec.codice != null">
			<dt>Soggetto precedente</dt>
			<dd>
				<s:property value="modificaDettaglio.soggettoPrec.codice" /> - <s:property value="modificaDettaglio.soggettoPrec.denominazione" />
			<s:if test="modificaDettaglio.soggettoPrec.codiceFiscale != null"> - CF: <s:property value="modificaDettaglio.soggettoPrec.codiceFiscale" /> </s:if>
			<s:if test="modificaDettaglio.soggettoPrec.partitaIva != null"> - PIVA: <s:property value="modificaDettaglio.soggettoPrec.partitaIva" /> </s:if> 
			&nbsp;
			</dd>
		</s:if>
		<s:elseif test="modificaDettaglio.soggettoPrec.classeSoggettoCodice != null">
			<dt>Classe soggetto precedente</dt>
			<dd><s:property value="modificaDettaglio.soggettoPrec.classeSoggettoCodice" /> - <s:property value="modificaDettaglio.soggettoPrec.classeSoggettoDescrizione" />&nbsp;</dd>
		</s:elseif>
		<s:else>
			<dt>Soggetto precedente</dt>
			<dd>&nbsp;</dd>
		</s:else>
		</s:else>
		
		<!-- SOLO PER MODIFICHE DI IMPORTO: -->
		<s:if test="modificaDettaglio.importo != null">
			<dt>Reimputazione</dt>
			<dd><s:property value="modificaDettaglio.reimputazione" /></dd>
			<s:if test="modificaDettaglio.annoReimputazione != null">
				<dt>Anno reimputazione</dt>
				<dd><s:property value="modificaDettaglio.annoReimputazione" /></dd>
			</s:if>
			<s:if test="modificaDettaglio.reanno != null">
				<dt>Elaborato ROR - Reimp. in corso d&lsquo;anno</dt>
				<dd><s:property value="modificaDettaglio.reanno" /></dd>
			</s:if>
			
			<%-- SIAC-7349 Inzio SR180 CM 08/04/2020 Introduzione della tabella delle associazioni nella popup di consulta modifiche di accertamento --%>
			<br/>
			
			<s:if test="modificaDettaglio.reimputazione == 'Si'">
				<div class="accordion" id="accordion2">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle collapsed" data-toggle="collapse" id="ModalAssociazioni" data-parent="#accordion2" href="#collapseOne">
								Associazioni<span class="icon">&nbsp;</span>
							</a>
						</div>
						<div id="collapseOne" class="accordion-body collapse">
							<div class="accordion-inner">
								<fieldset class="form-horizontal">
					<!-- 											qui dentro si inserisce la tabella 								-->
									<span class="control-label" id="titoloTabAssociazioni">Reimputazioni di Spesa:</span>
									<br/>		
									<br/>
									
									<s:if test="%{(modificaDettaglio.listaModificheMovimentoGestioneSpesaCollegata != null) && (modificaDettaglio.listaModificheMovimentoGestioneSpesaCollegata.size > 0) }">
										<table class="table table-hover table-condensed table-bordered" id="tabellaAssociazioni">
											<tr class="componentiRowOther">
												<s:if test="modificaDettaglio.tipoMovimento == 0">
													<th class="text-center">Numero Accertamento</th>
												</s:if>
												<s:else>				
													<th class="text-center">Numero impegno</th>	
												</s:else>	
												<th class="text-center">Numero modifica</th>
												<th class="text-center">Descrizione</th>
												<th class="text-center">Anno Reimp.</th>
												<th class="text-center">Importo modifica</th>
												<th class="text-center">Residuo collegare</th>
												<th class="text-center">Importo collegamento</th>
											</tr>
											<s:iterator value="modificaDettaglio.listaModificheMovimentoGestioneSpesaCollegata" var="modColl" status="statusModColl">
											 		<tr>
											 			<s:if test="modificaDettaglio.tipoMovimento == 0">
											 				<td class="text-left componentiRowFirst" ><s:property value="#modColl.modificaMovimentoGestioneEntrata.accertamento.numero.intValue()"/></td>
															<td class="text-left componentiRowFirst"><s:property value="#modColl.modificaMovimentoGestioneEntrata.numeroModificaMovimentoGestione"/></td>
															<!-- SIAC-7349 Inizio SR180 CM 22/04/2020 Aggiunto tooltip nella descrizione -->
															<td class="text-left componentiRowFirst"> 
																<div data-toggle="tooltip" data-placement="right" title="<s:property value="%{ (#modColl.modificaMovimentoGestioneEntrata.descrizioneModificaMovimentoGestione.length())>=10 ? #modColl.modificaMovimentoGestioneEntrata.descrizioneModificaMovimentoGestione : '' }"/>">
						 											<s:property value="%{ (#modColl.modificaMovimentoGestioneEntrata.descrizioneModificaMovimentoGestione.length())>=10 ? #modColl.modificaMovimentoGestioneEntrata.descrizioneModificaMovimentoGestione.substring(0,10)+'...' : #modColl.modificaMovimentoGestioneEntrata.descrizioneModificaMovimentoGestione }"/>
						 										</div> 
															</td>
															<!-- SIAC-7349 Fine SR180 CM 23/04/2020 -->
															<td class="text-left componentiRowFirst"><s:property value="#modColl.modificaMovimentoGestioneEntrata.annoReimputazione"/></td>
															<td class="text-right componentiRowLight"><s:property value="#modColl.modificaMovimentoGestioneEntrata.importoOld"/></td>
														</s:if>
														<s:else>					
															<td class="text-left componentiRowFirst" ><s:property value="#modColl.modificaMovimentoGestioneSpesa.impegno.numero.intValue()"/></td>
															<td class="text-left componentiRowFirst"><s:property value="#modColl.modificaMovimentoGestioneSpesa.numeroModificaMovimentoGestione"/></td>
															<!-- SIAC-7349 Inizio SR180 CM 22/04/2020 Aggiunto tooltip nella descrizione -->
															<td class="text-left componentiRowFirst"> 
																<div data-toggle="tooltip" data-placement="right" title="<s:property value="%{ (#modColl.modificaMovimentoGestioneSpesa.descrizioneModificaMovimentoGestione.length())>=10 ? #modColl.modificaMovimentoGestioneSpesa.descrizioneModificaMovimentoGestione : '' }"/>">
						 											<s:property value="%{ (#modColl.modificaMovimentoGestioneSpesa.descrizioneModificaMovimentoGestione.length())>=10 ? #modColl.modificaMovimentoGestioneSpesa.descrizioneModificaMovimentoGestione.substring(0,10)+'...' : #modColl.modificaMovimentoGestioneSpesa.descrizioneModificaMovimentoGestione }"/>
						 										</div> 
															</td>
															<!-- SIAC-7349 Fine SR180 CM 22/04/2020 Aggiunto tooltip nella descrizione -->
															<td class="text-left componentiRowFirst"><s:property value="#modColl.modificaMovimentoGestioneSpesa.annoReimputazione"/></td>
															<td class="text-right componentiRowLight"><s:property value="#modColl.modificaMovimentoGestioneSpesa.importoOld"/></td>
														</s:else>
														<td class="text-right componentiRowLight"><s:property value="#modColl.importoResiduoCollegare"/></td>
														<td class="text-right componentiRowLight"><s:property value="#modColl.importoCollegamento"/></td>
													</tr>
											</s:iterator>   
					
										</table>
										
										</s:if>
									<s:else>
									<div id="ncsdf">Non ci sono record da visualizzare.</div>
								</s:else>
								</fieldset>
							</div>
						</div>
					</div>
				</div>     
		      </s:if>
			<%-- SIAC-7349 Fine SR180 CM 08/04/2020 --%>     
			
		</s:if>	
	
		</dl>
	</fieldset>
</div>
<div class="modal-footer">
	<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
</div>

<!-- SIAC-7349 Inizio SR180 CM 08/04/2020 aggiunto per visualizzazione tooltip su descrizione -->
<script type="text/javascript">
	$(document).ready(function() {
		
   		$('[data-toggle="tooltip"]').tooltip(); 
   		
	});
</script>
<!-- SIAC-7349 Fine SR180 CM 08/04/2020 -->
