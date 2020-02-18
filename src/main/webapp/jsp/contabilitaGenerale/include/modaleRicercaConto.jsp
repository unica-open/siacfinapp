<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="comp-CodConto" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3 id="myModalLabel">Codice Conto</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleRicercaConto">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset class="form-horizontal" id="fieldsetModaleRicercaConto">
			<div id="campiRicercaConto" class="accordion-body collapse in">
			
				<s:hidden id="HIDDEN_ambitoConto" data-maintain="" value="%{ambito}" name="ambito"/>
				<s:hidden id="HIDDEN_ambitoContoFIN" data-maintain="" value="%{ambitoFIN}" name="ambitoFIN"/>
			
				<div class="control-group">
					<label class="control-label" for="classePianoDeiConti_modale">Classe *</label>
					<div class="controls">
					<s:select list="listaClassi" name="conto.pianoDeiConti.classePiano.uid"
						id="classePianoDeiConti_modale" cssClass="span6" headerKey="0" headerValue=""
						listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label"  for="codicePianoDeiContiRicerca_modale">Codice Conto</label>
					<div class="controls">
						<s:textfield id="codicePianoDeiContiRicerca_modale" name="conto.codice" cssClass="span6" data-noclassepdc=""/>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="livelloPianoDeiContiRicerca_modale">Livello</label>
					<div class="controls">
						<s:textfield id="livelloPianoDeiContiRicerca_modale" name="conto.livello" cssClass="span6 soloNumeri"  data-noclassepdc=""/>
					</div>
				</div>
				
				<div id="divContoFIN">
					<div class="control-group">
						<div class="controls">
							<label class="radio inline">
								<input type="radio" name="optionConto" value="entrata" id="optionsRadios1" data-noclassepdc="" data-entrata=""> Entrata
							</label>
							<label class="radio inline">
								<input type="radio" name="optionConto" value="spesa" id="optionsRadios2" data-noclassepdc="" data-spesa=""> Spesa
							</label>
						</div>
					</div>
					
					<div class="campiEntrata" id="campiEntrata">
						<div class="control-group">
							<label class="control-label" for="titoloEntrata">Titolo</label>
							<div class="controls">
								<s:select list="listaTitoloEntrata" id="titoloEntrata" cssClass="span10"
									name="titoloEntrata.uid" headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" data-noclassepdc="" data-campoentrata="" />
							</div>
						</div>
	
						<div class="control-group">
							<label class="control-label" for="tipologiaTitolo">Tipologia &nbsp;<a class="tooltip-test" data-original-title="Selezionare prima il Titolo"><i class="icon-info-sign">&nbsp;<span class="nascosto">Selezionare prima il Titolo</span></i></a></label>
							<div class="controls">
								<select id="tipologiaTitolo" class="span10" data-noclassepdc="" data-campoentrata="" ></select>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="categoriaTipologiaTitolo">Categoria &nbsp;<a class="tooltip-test" data-original-title="Selezionare prima la Tipologia"><i class="icon-info-sign">&nbsp;<span class="nascosto">Selezionare prima la Tipologia</span></i></a></label>
							<div class="controls">
								<select id="categoriaTipologiaTitolo" name="categoriaTipologiaTitolo.uid" class="span10" data-noclassepdc="" data-campoentrata="" ></select>
							</div>
						</div>
					</div>
					<%-- Attenzione nella jsp originale i class="campiSpesa" e class="campiEntrata" sono scambiati--%>
					<div class="campiSpesa" id="campiSpesa">
						<div class="control-group">
							
							<label class="control-label" for="titoloSpesa">Titolo</label>
							<div class="controls">
								<s:select list="listaTitoloSpesa" id="titoloSpesa" cssClass="span10"
										name="titoloSpesa.uid" headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" data-noclassepdc="" data-campospesa="" />
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="macroaggregato">Macroaggregato &nbsp;<a class="tooltip-test" data-original-title="Selezionare prima il Titolo"><i class="icon-info-sign">&nbsp;<span class="nascosto">Selezionare prima il Titolo</span></i></a></label>
							<div class="controls">
								<select id="macroaggregato" name="macroaggregato.uid" class="span12" data-noclassepdc=""  data-campospesa="" ></select>
							</div>
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label" for="bottonePdC">V Livello P.d.C. finanziario</label>
						<input type="hidden" id="HIDDEN_ElementoPianoDeiContiUid" name="conto.elementoPianoDeiConti.uid" value="${elementoPianoDeiConti.uid}" />
						<s:hidden id="HIDDEN_ElementoPianoDeiContiStringa" name="pdcFinanziario" />
						<div class="controls">
							<div class="accordion span12" class="PDCfinanziario" >
								<div class="accordion-group">
									<div class="accordion-heading">
										<a data-noclassepdc="" id="bottonePdC" class="accordion-toggle" data-toggle="collapse" data-parent="#PDCfinanziario" href="#PDCfin" >
											<span id="SPAN_ElementoPianoDeiConti">
												<s:if test='%{pdcFinanziario != null && pdcFinanziario neq ""}'>
													<s:property value="pdcFinanziario"/>
												</s:if><s:else>
													Seleziona il conto finanziario
												</s:else>
											</span>
											<i class="icon-spin icon-refresh spinner" id="SPINNER_ElementoPianoDeiConti"></i>&nbsp;
										</a>
									</div>
									<div id="PDCfin" class="accordion-body collapse">
										<div class="accordion-inner">
											<ul id="treePDC" class="ztree"></ul>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>	
				
				<s:hidden id="HIDDEN_Ammortamento" data-maintain="" name="conto.ammortamento" />
			</div>
			<button type="button" class="btn btn-primary pull-right" id="bottoneCercaModaleRicercaPDC">
				<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="spinnerModaleRicercaPDC"></i>
			</button>
		</fieldset>
		<div id="risultatiRicercaConto" class="hide">
			<h4>Elenco Conti</h4>
			<table class="table table-hover tab_left" id="tabellaRisultatiRicercaConto">
				<thead>
					<tr>
						<th></th>
						<th>Livello</th>
						<th>Codice</th>
						<th>Descrizione</th>
						<th>Conto di legge</th>
						<th>Conto foglia</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>

	<div class="modal-footer">
		<button class="btn btn-primary"  aria-hidden="true" id="pulsanteConfermaModaleRicercaConto">conferma</button>
	</div>
 
</div>