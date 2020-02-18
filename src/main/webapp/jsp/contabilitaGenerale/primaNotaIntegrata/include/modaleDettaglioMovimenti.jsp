<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="modaleDettaglioMovimentiLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleDettaglioMovimenti">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h3 id="modaleDettaglioMovimentiLabel">Prima Nota Integrata <span id="modaleDettaglioMovimentiSpan"></span></h3>
	</div>
	<div class="modal-body">
		<h4 id="modaleDettaglioMovimentiDescrizione"></h4>
		<table class="table table-hover tab_left" id="modaleDettaglioMovimentiTabella">
			<thead>
				<tr>
					<th>Conto</th>
					<th>Descrizione</th>
					<th class="tab_Right">Dare</th>
					<th class="tab_Right">Avere</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
			<tfoot>
				<tr><th colspan="2">Totale</th>
				<th class="tab_Right" id="modaleDettaglioMovimentiTotaleDare"></th>
				<th class="tab_Right" id="modaleDettaglioMovimentiTotaleAvere"></th>
				</tr>
			</tfoot>
		</table>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn btn-secondary">annulla</button>
	</div>
</div>