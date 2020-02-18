<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

	<h4>Dettagli disposizione</h4>
	<fieldset class="form-horizontal">
		
		<div class="control-group">
			<label class="control-label">Valuta estera *</label>
			<div class="controls">
				<s:select list="listaValuta" id="listaValuta" name="codiceDivisa" cssClass="span4" 
					listKey="codice" listValue="codice+' - '+descrizione" headerKey="" headerValue="" disabled="disableStep1()"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">Causale pagamento *</label>
				<div class="controls">
					<s:textfield id="causalePagamentoEstero" name="causalePagamentoEstero" cssClass="lbTextSmall span9" disabled="disableStep1()"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label" for="SpeseEcommissioni">Spese e commissioni *</label>
				<div class="controls">
				<s:select list="listaCommissioniEstero" id="listaCommissioniEstero" name="speseECommissioni" cssClass="span4" 
					listKey="codice" listValue="codice+' - '+descrizione" headerKey="" headerValue="" disabled="disableStep1()" />
			</div>
		</div>
		
	</fieldset>
	
	<h4>Modalit&agrave; di pagamento</h4>
	<fieldset class="form-horizontal">
	
	<div class="control-group">
		<label class="control-label">Tipologia</label>
		<div class="controls">
		  <%-- <label class="radio inline"><input type="radio" name="radioTipologia" id="radioBonifico" value="bonifico" checked="checked">Bonifico</label>
		  <label class="radio inline"><input type="radio" name="radioTipologia" id="radioAssegno" value="assegno">Assegno</label>
		  <label class="radio inline"><input type="radio" name="radioTipologia" id="radioAltro" value="altro">Altro</label>
		  <span id="ModAssegno" class="alRight marginLeft2">
				<select name="modConsegna" value="" id="modConsegna" style="display:none">
					<option value="">Da consegnare</option>
					<option value="">Da spedire</option>
				</select>
			</span> --%>
				<s:select list="listaModalita" name="modalitaPagamento" cssClass="span4" disabled="disableStep1()" />
			
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" >Istruzioni particolari</label>
		<div class="controls">
			<s:textarea id="istrPartPagamentoEstero" name="istrPartPagamentoEstero" rows="2" cols="15" cssClass="span9" disabled="disableStep1()"  />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label">Esecutore diverso da Titolare</label>
			<div class="controls">
				<s:checkbox id="checkEsecutoreTitolare" name="checkEsecutoreTitolare" disabled="disableStep1()"/>
		</div>
	</div>

	</fieldset>  

