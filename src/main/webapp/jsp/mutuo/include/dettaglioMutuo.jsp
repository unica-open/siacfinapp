<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<h4>Dati mutuo</h4>
<fieldset class="form-horizontal">
	<div class="control-group">
		<label class="control-label" for="listaTipiMutuo">Tipo *</label>
		<div class="controls">
		 <s:if test="null!=listaTipiMutuo">
			<s:select list="listaTipiMutuo" id="listaTipiMutuo"  headerKey="" 
       		   headerValue="" name="tipoMutuo" cssClass="span2"  
   	 	       listKey="codice" listValue="descrizione" />
   	 	 </s:if>
			<span class="al">
				<label class="radio inline" for="stato">Stato</label>
			</span>
			<s:textfield id="stato" name="stato" cssClass="lbTextSmall span2" disabled="true"/> 
			<span class="al">
				<label class="radio inline" for="numeroRegistrazione">Num. Registrazione</label>
			</span> 
			<s:textfield cssClass="lbTextSmall span2" maxlength="15" id="numeroRegistrazione" name="numeroRegistrazione" disabled="checkImportoEStatoMutuo()"/>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="descrizioneMutuo">Descrizione</label>
		<div class="controls">
			<s:textfield maxlength="500" id="descrizioneMutuo" name="descrizione" cssClass="span9"/>  
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="importoImpegno">Importo *</label>
		<div class="controls">
			<s:textfield id="importoImpegno" name="importoFormattato" onkeypress="return checkItNumbersCommaAndDotOnly(event)" cssClass="lbTextSmall span2 soloNumeri decimale" disabled="checkImportoEStatoMutuo()"></s:textfield> 
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="durata">Durata (anni) *</label>
		<div class="controls">
			<s:textfield cssClass="span1 gestioneAnnoDinamico" id="durata" onkeyup="return checkItNumbersOnly(event)" name="durata" maxlength="2"/>
			<span class="al">
				<label class="radio inline" for="dataInizio">Data inizio *</label>
			</span>
			<s:textfield id="dataInizio" title="gg/mm/aaaa" name="dataInizio" cssClass="lbTextSmall span2 datepicker gestioneAnnoDinamico"/>
			<span id="aggiornaDataFineMutuo">
				<s:include value="/jsp/mutuo/include/dataFineMutuo.jsp" />
			</span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="noteMutuo">Note</label>
		<div class="controls">
			<s:textarea id="noteMutuo" name="note" rows="2" cols="15" cssClass="input-medium span9"/>
		</div>
	</div>
</fieldset>
<script type="text/javascript">
	$(document).ready(function() {
		$(".gestioneAnnoDinamico").blur(function() {
			aggiornaDataFine();
		});
		$("#dataInizio").on('changeDate', function(ev){
			aggiornaDataFine();
		});
	});
	
	function aggiornaDataFine() {
		$.ajax({
			url: '<s:url method="calcolaDataFineMutuo"/>',
			type: 'POST',
			data: $(".gestioneAnnoDinamico").serialize(),
		    success: function(data)  {
			    $("#aggiornaDataFineMutuo").html(data);
			}
		});
	}
</script>
