<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="modaleEliminazioneLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleEliminazione">
	<div class="modal-body">
		<div class="alert alert-error alert-persistent">
			<p><strong>Attenzione!</strong></p>
			<p><strong>Elemento selezionato:<span id="modaleEliminazioneElementoSelezionato"></span></strong></p>
			<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn" id="btnIndietro">no, indietro</button>
		<button type="button" class="btn btn-primary" id="modaleEliminazionePulsanteSalvataggio">
			si, prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_modaleEliminazionePulsanteSalvataggio"></i>
		</button> 
	</div>
</div>