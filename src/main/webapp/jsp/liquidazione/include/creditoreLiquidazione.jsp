<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
	<h4>Creditore : <span class="datiPagamento">000000/X/XX - .....</span></h4>
	
	<fieldset class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="Codice">Codice</label>
			<div class="controls">
			  <input id="Codice" name="Codice" class="span2" type="text"/>
			  <span class="alRight">
				<label class="radio inline" for="classe">Classe</label>
			  </span>
			   <input id="classe" name="classe" class="span4" type="text" disabled="disabled"/>
			  <span class="radio guidata"><a href="#guidaCreditore" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
			</div>
		</div>
	</fieldset>

