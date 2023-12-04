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
				<td class="span4 sBold textRight bottomGreen" data-disponibilita-motivazione-trigger="subimpegnare">
					<s:if test="statoImpegnoDefinitivo()">
						Non subimpegnabile
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
					<span class="hide" data-disponibilita-motivazione-data="subimpegnare">
						<s:property value="movimento.motivazioneDisponibilitaSubImpegnare" />
					</span>
					
				</td>
				<td class="span4"></td>
			</tr>
			<%-- CARTE_NON_REG --%>
			<tr>
				<td class="span4 sBold textLeft">Tot Carte non regolarizzate</td>
				<td class="span3"></td>
				<td class="span1"><s:property value="dettaglioImporti.carteNonReg.numero" /></td>
				<td class="span4 sBold textRight bottomGreen"><s:property value="getText('struts.money.format', {dettaglioImporti.carteNonReg.importo})" /></td>
			</tr>
			<%-- IMP PRE DOC --%>
			<tr>
				<td class="span4 sBold textLeft">Tot predoc non liquidati</td>
				<td class="span3"></td>
				<td class="span1"><s:property value="dettaglioImporti.impPredoc.numero" /></td>
				<td class="span4 sBold textRight bottomGreen"><s:property value="getText('struts.money.format', {dettaglioImporti.impPredoc.importo})" /></td>
			</tr>
			<%-- DOCUMENTI NON LIQUIDATI --%>
			<tr>
				<td class="span4 sBold textLeft">Documenti non liquidati</td>
				<td class="span3"></td>
				<td class="span1"><s:property value="dettaglioImporti.docNonLiq.numero" /></td>
				<td class="span4 sBold textRight bottomGreen"><s:property value="getText('struts.money.format', {dettaglioImporti.docNonLiq.importo})" /></td>
			</tr>
			<%-- PAGAMENTI ECONOMALI --%>
			<tr>
				<td class="span4 sBold textLeft">Tot pagamenti economali</td>
				<td class="span3"></td>
				<td class="span1"></td>
				<td class="span4 sBold textRight bottomGreen"><s:property value="getText('struts.money.format', {dettaglioImporti.totCec})" /></td>
			</tr>
			<%-- TOTALE LIQUIDAZIONI --%>
			<tr>
				<td class="span4 sBold textLeft">Totale liquidazioni</td>
				<td class="span3"></td>
				<td class="span1"><s:property value="dettaglioImporti.impLiq.numero" /></td>
				<td class="span4 sBold textRight bottomGreen"><s:property value="getText('struts.money.format', {dettaglioImporti.impLiq.importo})" /></td>
			</tr>
			<%-- TOTALE MOVIMENTI --%>
			<tr>
				<td class="span4 sBold textRight bottomGreen">Totale movimenti</td>
				<td class="span3"></td>
				<td class="span1"></td>
				<td class="span4 sBold textRight bottomGreen"><s:property value="getText('struts.money.format', {dettaglioImporti.totaleMovimenti})" /></td>
			</tr>
			
			<tr>
				<td class="span4 sBold textRight bottomGreen" data-disponibilita-motivazione-trigger="liquidare">Disponibile a liquidare</td>
				<td class="span3">
					<s:property value="getText('struts.money.format', {movimento.disponibilitaLiquidare})" />
					<span class="hide" data-disponibilita-motivazione-data="liquidare">
						<s:property value="movimento.motivazioneDisponibilitaLiquidare" />
					</span>
				</td>
				<td class="span1"></td>
				<td class="span4"></td>
			</tr>
			<tr>
				<td class="span4 sBold textRight bottomGreen" data-disponibilita-motivazione-trigger="pagare">Disponibile a pagare</td>
				<td class="span3">
					<s:property value="getText('struts.money.format', {movimento.disponibilitaPagare})" />
					<span class="hide" data-disponibilita-motivazione-data="pagare">
						<s:property value="movimento.motivazioneDisponibilitaPagare" />
					</span>
				</td>
				<td class="span1"></td>
				<td class="span4"></td>
			</tr>
			<tr>
				<td class="span4 sBold textRight bottomGreen" data-disponibilita-motivazione-trigger="vincolare">Disponibile a vincolare</td>
				<td class="span3">
					<s:property value="getText('struts.money.format', {movimento.disponibilitaVincolare})" />
					<span class="hide" data-disponibilita-motivazione-data="vincolare">
						<s:property value="movimento.motivazioneDisponibilitaVincolare" />
					</span>
				</td>
				<td class="span1"></td>
				<td class="span4"></td>
			</tr>

		</tbody>
	</table>
</div>
