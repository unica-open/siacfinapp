<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:if test="radioCodiceCapitolo != null">
	<div id="visDett" class="collapse margin-large">
		<div class="accordion_info">	
			<h4>
				Capitolo - <s:property value="datoPerVisualizza.numCapitolo" />/<s:property value="datoPerVisualizza.articolo" />/<s:property value="datoPerVisualizza.ueb" />
				<strong>Tipo finanziamento:</strong> <s:property value="datoPerVisualizza.tipoFinanziamento" />
			</h4>
			<table summary="riepilogo incarichi" class="table table-hover tab_centered">
				<thead>
					<tr>
						<th>Stanziamenti</th>
						<s:iterator value="datoPerVisualizza.importiCapitolo">
							<th><s:property value="annoCompetenza" /></th>
						</s:iterator>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th>Competenza</th>
						<s:iterator value="datoPerVisualizza.importiCapitolo">
							<td><s:property value="getText('struts.money.format', {competenza})" /></th>
						</s:iterator>
					</tr>
					<tr>
						<th>Residuo</th>
						<s:iterator value="datoPerVisualizza.importiCapitolo">
							<td><s:property value="getText('struts.money.format', {residuo})" /></th>
						</s:iterator>
					</tr>
					<tr>
						<th>Cassa</th>
						<s:iterator value="datoPerVisualizza.importiCapitolo">
							<td><s:property value="getText('struts.money.format', {cassa})" /></th>
						</s:iterator>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</s:if>