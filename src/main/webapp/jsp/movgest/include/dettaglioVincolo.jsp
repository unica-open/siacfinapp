<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4>Aggiorna vincolo</h4>
	</div>
	
	<div class="modal-body"> 		
		<fieldset class="form-horizontal">
		
			<div class="control-group">
				<label class="control-label">Importo</label>
				<div class="controls">    
					
					<s:textfield id="impVincolo" cssClass="lbTextSmall span3 soloNumeri decimale" 
			               name="step1Model.dettaglioVincolo.importoDaAggiornareFormattato" maxlength="14" />
					
					
					<label class="radio inline"><b>Utilizzabile: <s:property value="step1Model.dettaglioVincolo.importoUtilizzabile"/> </b></label>					
				</div>
			</div>
			
		</fieldset>	
		
		<s:hidden id="ac" name="step1Model.dettaglioVincolo.annoAccertamento"/>
		<s:hidden id="nc" name="step1Model.dettaglioVincolo.numeroAccertamento"/>
		<!-- task-202 -->
		<s:set var="operazione">${param.aggiornaVincoloOperazione}</s:set>
	</div>
	
	<div class="modal-footer">
		<!-- task-131 <s:submit id="btnAggVincolo" name="btnAggVincolo" aria-hidden="true"  value="conferma" cssClass="btn btn-primary" method="aggiornaVincolo"/> -->
		<s:submit name="btnAggVincolo" id="btnAggVincolo" aria-hidden="true" value="conferma" action="%{#operazione + '_aggiornaVincolo'}"  cssClass="btn btn-primary" />
	</div>
	
<script type="text/javascript">
	$(document).ready(function() {
		$(".soloNumeri").allowedChars({numeric: true});
		 /* Formattazione corretta dei campi numerici */
    	$(".decimale").gestioneDeiDecimali();
	});
</script>