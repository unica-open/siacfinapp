<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

<div class="accordion" id="accordion2" style="display:none" >
	<div class="accordion-group">
		<div class="accordion-heading">
			<a class="accordion-toggle collapsed" data-toggle="collapse" id="ModalAssociazioni" data-parent="#accordion2">
				Associazioni<span class="icon">&nbsp;</span>
			</a>
		</div>
		<div id="collapseOne" class="accordion-body collapse">
			<div class="accordion-inner">
				<fieldset class="form-horizontal">
					<span class="control-label" id="titoloTabAssociazioni" disabled>Reimputazioni di Spesa:</span>
					<br/>		
					<br/>
					<s:if test="%{(listaModificheSpeseCollegata != null) && (listaModificheSpeseCollegata.size > 0) }">
					
						<table class="table table-hover table-condensed table-bordered" id="tabellaAssociazioni">
							<tr class="componentiRowOther">
								<th class="text-center">Numero impegno</th>
								<th class="text-center">Numero modifica</th>
								<th class="text-center">Descrizione</th>
								<th class="text-center">Anno Reimp.</th>
								<th class="text-center">Importo modifica</th>
								<th class="text-center">Residuo collegare</th>
								<th class="text-center">Importo collegamento</th>
								<th class="text-center">Importo max. collegabile</th>
							</tr>
							<s:iterator value="listaModificheSpeseCollegata" var="modColl" status="statusModColl">
								<tr class="componentiRowOther motivo_<s:property value="#modColl.modificaMovimentoGestioneSpesa.tipoModificaMovimentoGestione"/> motivo_all">
									<s:if test="#modColl.vincoloEsplicito">
										<s:set var="classAttr" value="%{'componentiRowFirst'}"/>
									</s:if>
									<s:else>					
										<s:set var="classAttr" value="%{'componentiRowOther'}"/>
									</s:else>	
									<td class="text-left <s:property value="#classAttr"/>" ><s:property value="#modColl.modificaMovimentoGestioneSpesa.impegno.numero.intValue()"/></td>
									<td class="text-left <s:property value="#classAttr"/>"><s:property value="#modColl.modificaMovimentoGestioneSpesa.numeroModificaMovimentoGestione"/></td>
									<td class="text-left <s:property value="#classAttr"/>"> 
										<div data-toggle="tooltip" data-placement="right" title="<s:property value="%{ (#modColl.modificaMovimentoGestioneSpesa.descrizioneModificaMovimentoGestione.length())>=10 ? #modColl.modificaMovimentoGestioneSpesa.descrizioneModificaMovimentoGestione : '' }"/>">
 											<s:property value="%{ (#modColl.modificaMovimentoGestioneSpesa.descrizioneModificaMovimentoGestione.length())>=10 ? #modColl.modificaMovimentoGestioneSpesa.descrizioneModificaMovimentoGestione.substring(0,10)+'...' : #modColl.modificaMovimentoGestioneSpesa.descrizioneModificaMovimentoGestione }"/>
 										</div> 
									</td>
									<td class="text-left <s:property value="#classAttr"/>"><s:property value="#modColl.modificaMovimentoGestioneSpesa.annoReimputazione"/></td>
									<td class="text-right componentiRowLight"><s:property value="#modColl.modificaMovimentoGestioneSpesa.importoOld"/></td>
									<%-- rimuovo il calcolo %{#modColl.importoResiduoCollegare - #modColl.importoCollegamento}, i dati sono gia' popolati dal servizio --%>
									<td class="text-right componentiRowLight"><s:property value="#modColl.importoResiduoCollegare"/></td>
									<td class="text-right componentiRowLight anno_<s:property value="#modColl.modificaMovimentoGestioneSpesa.annoReimputazione"/> anno_all" >
											<span>
												<s:property value="#modColl.importoCollegamento"/>
											</span>
											<s:textfield style="display:none" type="text" maxlength="30"  name="listaModificheSpeseCollegata[%{#statusModColl.index}].importoCollegamento" value="%{importoCollegamento}"  id="inputReimputazione%{#statusModColl.index}" onblur="checkNullToEmptyString(event)" placeholder="0.00" class="soloNumeri decimale"/>
									</td>
									<td class="text-right componentiRowLight"><s:property value="#modColl.importoMaxCollegabile"  /></td>
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