<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4>Aggiorna importo</h4>
</div>
	
<div class="modal-body"> 		
	<fieldset class="form-horizontal">
	
		<div class="control-group">
			<label class="control-label">Importo</label>
			<div class="controls">    
				
				<s:textfield id="impVincolo" cssClass="lbTextSmall span3 soloNumeri decimale" 
		               name="step1Model.importoInModificaFormattato" maxlength="10" />
							
			</div>
		</div>
		
	</fieldset>	
</div>

<div class="modal-footer">
	<s:submit id="btnAggImportoVincolo" name="btnAggImportoVincolo" data-dismiss="modal" aria-hidden="true"  
	          value="conferma" cssClass="btn btn-primary" method="aggiornaImportoConVincolo"/>
</div>
	
<script type="text/javascript">
	$(document).ready(function() {
		$(".soloNumeri").allowedChars({numeric: true});
		 /* Formattazione corretta dei campi numerici */
    	$(".decimale").gestioneDeiDecimali();
	});
</script>	