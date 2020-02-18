<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<div class="control-group">
	<label for="nomeautore" class="control-label">Missione</label>
	<div class="controls">

		<s:if test="listaMissione!=null">
			<s:select list="listaMissione" id="listaMissione" disabled="true"
				name="trxModel.missioneSelezionata" cssClass="span10"
				listKey="codice" listValue="descrizione" />
			<s:hidden value="trxModel.missioneSelezionata" />
		</s:if>
	</div>
</div>

<div class="control-group">
	<label for="soggetto5" class="control-label">Programma
	</label>
	<div class="controls">
		<s:select list="listaProgramma" id="listaProgramma" disabled="true"
			name="trxModel.programmaSelezionato" cssClass="span10"
			listKey="codice" listValue="descrizione" />
		<s:hidden value="trxModel.programmaSelezionato" />
	</div>
</div>

<div class="control-group">
	<label for="Titolo" class="control-label">Cofog</label>
	<div class="controls">
		<s:select list="listaCofog" id="listaCofog" headerKey=""
			headerValue="" name="trxModel.cofogSelezionato" cssClass="span10"
			listKey="codice" listValue="descrizione" />
	</div>
</div>

<div class="control-group">
	<label for="Macroaggregato" class="control-label">Codifica
		Transazione Europea</label>
	<div class="controls">
		<s:select list="listaTransazioneEuropeaSpesa" headerKey=""
			headerValue="" id="listaTransEuropea"
			name="trxModel.transazioneEuropeaSelezionato" cssClass="span10"
			listKey="codice" listValue="%{codice + ' - ' + descrizione}" />



	</div>
</div>


<div class="control-group">
	<label for="CUP" class="control-label">CUP</label>
	<div class="controls">
		<s:textfield name="trxModel.cup" id="cup" readonly="true" />
	</div>
</div>
<div class="control-group">
	<label for="Ricorrente" class="control-label">Ricorrente</label>
	<div class="controls">

		<s:select list="listaRicorrenteSpesa" id="listaRicorrenteSpesa"
			headerKey="" headerValue=""
			name="trxModel.ricorrenteSpesaSelezionato" cssClass="span10"
			listKey="codice" listValue="%{codice + ' - ' + descrizione}" />
	</div>
</div>

<div class="control-group">
	<label for="ASL" class="control-label">Capitoli perimetro
		sanitario</label>
	<div class="controls">
		<s:select list="listaPerimetroSanitarioSpesa"
			id="listaPerimetroSanitarioSpesa"
			name="trxModel.perimetroSanitarioSpesaSelezionato" cssClass="span10"
			headerKey="" headerValue="" listKey="codice"
			listValue="%{codice + ' - ' + descrizione}" />
	</div>
</div>
<div class="control-group">
	<label for="Unitaria" class="control-label">Programma Pol. Reg.
		Unitaria</label>
	<div class="controls">
		<s:select list="listaPoliticheRegionaliUnitarie"
			id="listaPoliticheRegionaliUnitarie"
			name="trxModel.politicaRegionaleSelezionato" cssClass="span10"
			headerKey="" headerValue="" listKey="codice"
			listValue="%{codice + ' - ' + descrizione}" />
	</div>
</div>
