<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

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
					<s:if test="%{(listaModificheRor != null) && (listaModificheRor.size > 0) }">
						<s:iterator value="listaModificheRor" status="incr">
<%-- 						<s:if test='%{listaModificheRor[#incr.index].tipoModificaMovimentoGestione.equals("REIMP")}'> --%>
							<s:if test="%{(listaModificheSpeseCollegata[#incr.index].listaModificheMovimentoGestioneSpesaCollegata != null) && (listaModificheSpeseCollegata[#incr.index].listaModificheMovimentoGestioneSpesaCollegata.size > 0)}">
								<table class="table table-hover table-condensed table-bordered impegnoReimp_<s:property value="listaModificheSpeseCollegata[#incr.index].uid"/> impegnoReimp_all" id="tabellaAssociazioni">
									<tr class="componentiRowOther">
										<th class="text-center">Numero Accertamento</th>
										<th class="text-center">Numero modifica</th>
										<th class="text-center">Descrizione</th>
										<th class="text-center">Anno Reimp.</th>
										<th class="text-center">Importo modifica</th>
										<th class="text-center">Residuo collegare</th>
										<th class="text-center">Importo collegamento</th>
									</tr>
									<s:iterator value="listaModificheSpeseCollegata[#incr.index].listaModificheMovimentoGestioneSpesaCollegata" var="modColl" status="statusModColl">
									 		<tr>
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
												<td class="text-right componentiRowLight"><s:property value="#modColl.importoResiduoCollegare"/></td>
												<td class="text-right componentiRowLight"><s:property value="#modColl.importoCollegamento"/></td>
											</tr>
									</s:iterator>   
								</table>
							</s:if>
						</s:iterator> 
					</s:if>
					<s:else>
						<div id="ncsdf">Non ci sono record da visualizzare.</div>
					</s:else>
					
					<div id="ncsdfImpegno">Non ci sono record da visualizzare.</div>
					
				</fieldset>
			</div>
		</div>
	</div>
</div>  