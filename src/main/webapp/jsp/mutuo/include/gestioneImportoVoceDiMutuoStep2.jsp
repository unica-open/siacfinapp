<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<div class="accordion_info" id="prova">
	<div class="step-pane active" id="step1">
		<h4>Voce Mutuo</h4>
		<fieldset class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="impVoce">Importo *</label>
				<div class="controls">
					<s:textfield id="importoVoceMutuo" name="importoNuovaVoceDiMutuo" disabled="inserimentoStornoVoceMutuo" cssClass="lbTextSmall span2 paramVoceDiMutuoPerSalva soloNumeri decimale" />
				</div>
			</div>
		</fieldset>
	</div>
	<p>
		<a class="btn btn-secondary" name="btnInvisible" data-toggle="collapse" data-target="#insVoce">annulla inserimento</a>
		<span class="pull-right">
			<s:submit name="salvaVoce" value="salva voce" method="salvaVoce" cssClass="btn btn-primary" />
		</span>
	</p>
</div>
<script type="text/javascript">

	$(document).ready(function() {
		
		
		$("#importoVoceMutuo").allowedChars({numeric: true});
		
		 /* Formattazione corretta dei campi numerici */
	    $("#importoVoceMutuo").gestioneDeiDecimali();
		
	});
	
</script> 