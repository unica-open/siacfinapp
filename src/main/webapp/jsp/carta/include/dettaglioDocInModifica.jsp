<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


<!--modale aggRegolaCarta-->
	
	     <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="nostep-pane">Aggiorna</h4>
		</div>
		<div class="modal-body">
			<div class="control-group">
				<label class="control-label">Importo</label>
				<div class="controls">
				 <s:textfield cssClass="lbTextSmall span3 soloNumeri decimale" id="importoPopupFormattato" name="importoPopupFormattato" maxlength="8" />
<!-- 									<input id="ImpRegolarizza" name="ImpRegolarizza" class="lbTextSmall span3" type="text" value=""  /> -->
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<s:submit id="confermaAggiornamentoRigaDoc" name="conferma" value="conferma" cssClass="btn btn-primary" data-dismiss="modal" method="aggiornaRigaDoc"/>
<!-- 			<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">conferma</button> -->
		</div>
		
		 <!-- hidden utilizzato per l'aggiorna del documento non ancora regolarizzato -->
		<s:hidden id="uidSelezPerModifica" name="uidSelezPerModifica" value="%{documentoInModifica.uid}" />  
		
<!--/modale -->



<script type="text/javascript">
	$(document).ready(function() {
		$(".soloNumeri").allowedChars({numeric: true});
		 /* Formattazione corretta dei campi numerici */
    	$(".decimale").gestioneDeiDecimali();
	});
</script>