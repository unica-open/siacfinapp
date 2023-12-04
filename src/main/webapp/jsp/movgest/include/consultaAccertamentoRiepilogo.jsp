<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<div class="boxOrInLeft">
	<p>Riepilogo</p>
	<table class="span12">
		<tbody class="span12">
			<%-- TOTALE SUB --%>
			<tr>
				<td class="span4 sBold textLeft">Totale sub.</td>
				<td class="span3"></td>
				<td class="span1"></td>
				<td class="span4 sBold textRight bottomGreen"><s:property value="getText('struts.money.format', {totImportiAttualiSubValidi})" /></td>
			</tr>
			<%-- DISPONIBILE SUB IMPEGNARE --%>
			<tr>
				<td class="span4 sBold textRight bottomGreen" data-disponibilita-motivazione-trigger="subaccertare">
					<s:if test="statoImpegnoDefinitivo()">
						Non subaccertabile
					</s:if><s:else>
						Disponibile a sub<s:property value="movimento.descDispMovimento" />are
					</s:else>
				</td>
				<td class="span3"></td>
				<td class="span1">
					<s:if test="statoImpegnoDefinitivo()">
						-
					</s:if><s:else>
						<s:property value="getText('struts.money.format', {movimento.disponibilitaSub})" />
					</s:else>
					<span class="hide" data-disponibilita-motivazione-data="subaccertare">
						<s:property value="movimento.motivazioneDisponibilitaSubAccertare" />
					</span>
				</td>
				<td class="span4"></td>
			</tr>
			<%-- IMP PRE DOC --%>
			<tr>
				<td class="span4 sBold textLeft">Tot predoc non inc.</td>
				<td class="span3"></td>
				<td class="span1"><s:property value="dettaglioImporti.impPredoc.numero" /></td>
				<td class="span4 sBold textRight bottomGreen"><s:property value="getText('struts.money.format', {dettaglioImportiAcc.impPredoc.importo})" /></td>
			</tr>
			<%-- DOCUMENTI NON INCASSATI --%>
			<tr>
				<td class="span4 sBold textLeft">Totale Documenti non inc.</td>
				<td class="span3"></td>
				<td class="span1"><s:property value="dettaglioImporti.docNonLiq.numero" /></td>
				<td class="span4 sBold textRight bottomGreen"><s:property value="getText('struts.money.format', {dettaglioImportiAcc.docNonInc.importo})" /></td>
			</tr>
			<%-- TOTALE LIQUIDAZIONI --%>
			<tr>
				<td class="span4 sBold textLeft">Totale Ordinativi (Reversali)</td>
				<td class="span3"></td>
				<td class="span1"><s:property value="dettaglioImporti.impLiq.numero" /></td>
				<td class="span4 sBold textRight bottomGreen"><s:property value="getText('struts.money.format', {dettaglioImportiAcc.impOrd.importo})" /></td>
			</tr>
			<%-- TOTALE MOVIMENTI --%>
			<tr>
				<td class="span4 sBold textRight bottomGreen">Totale movimenti</td>
				<td class="span3"></td>
				<td class="span1"></td>
				<td class="span4 sBold textRight bottomGreen"><s:property value="getText('struts.money.format', {dettaglioImportiAcc.totaleMovimenti})" /></td>
			</tr>
			
			<tr>
				<td class="span4 sBold textRight bottomGreen">Disponibile a incassare (solo Ord)</td>
				<%-- SIAC-8491 --%>
				<td class="span3"><s:property value="getText('struts.money.format', {dettaglioImportiAcc.disponibileIncassareSoloOrd})" /></td>
				<td class="span1"></td>
				<td class="span4"></td>
			</tr>
			<tr>
				<td class="span4 sBold textRight bottomGreen" data-disponibilita-motivazione-trigger="incassare">Disponibilita' Accertamento (con doc)</td>
				<td class="span3">
					<s:property value="getText('struts.money.format', {movimento.disponibilitaIncassare})" />
					<span class="hide" data-disponibilita-motivazione-data="incassare">
						<s:property value="movimento.motivazioneDisponibilitaIncassare" />
					</span>
				</td>
				<td class="span1"></td>
				<td class="span4"></td>
			</tr>
			<tr>
				<td class="span4 sBold textRight bottomGreen" data-disponibilita-motivazione-trigger="utilizzare">Disponibile per vincoli</td>
				<td class="span3">
					<s:property value="getText('struts.money.format', {movimento.disponibilitaUtilizzare})" />
					<span class="hide" data-disponibilita-motivazione-data="utilizzare">
						<s:property value="movimento.motivazioneDisponibilitaUtilizzare" />
					</span>
				</td>
				<td class="span1"></td>
				<td class="span4"></td>
			</tr>
		</tbody>
	</table>
</div>
