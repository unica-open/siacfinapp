<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="modaleAggiornamentoContoLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleAggiornamentoConto">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3 id="modaleAggiornamentoContoLabel">Aggiorna Prima Nota</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modale">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br/>
			<ul>
			</ul>
		</div>
		<fieldset id="fieldsetModaleAggiornamentoConto" class="form-horizontal">
			<s:hidden id="indiceContoModale" name="modale.indiceConto" />
			<s:hidden id="segnoModale" name="modale.segno" />
			
			<div class="control-group">
				<label class="control-label" >Segno Conto *</label>
				<div class="controls" id="#bloccoSegno">
					<label class="radio inline"> 
						Dare <input type="radio" value="DARE" name="modale.operazioneSegnoConto" data-DARE="" disabled />
					</label>
					<label class="radio inline">  
						Avere <input type="radio" value="AVERE" name="modale.operazioneSegnoConto" data-AVERE="" disabled />
					</label>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="importoModale">Importo *</label>
				<div class="controls">
					<s:textfield id="importoModale" name="modale.importo" cssClass="span4 soloNumeri decimale required"/>
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn btn-secondary">annulla</button>
		<button type="button" class="btn btn-primary" id="pulsanteSalvaAggiornamentoConto">
			conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteSalvaAggiornamentoConto"></i>
		</button>
	</div>
</div>