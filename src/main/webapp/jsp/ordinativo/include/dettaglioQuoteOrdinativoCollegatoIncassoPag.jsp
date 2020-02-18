<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<!-- div modale dettaglio quote oordinativi PAGAMENTO-->
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	<h4>Ordinativo - <s:property value="ordinativoCollegatoSelezionato.numero"/></h4>
</div>
<div class="modal-body">
	<display:table name="ordinativoCollegatoSelezionato.elencoSubOrdinativiDiPagamento" class="table table-hover tab_centered" summary="riepilogo" uid="quotaOrdCollPagID" pagesize="10"
	 	requestURI="consultaOrdinativoIncasso.do"
	>

		<display:column title="Numero" headerClass="tabRow sBold" class="tabRow tab_Right">
			<s:property value="%{#attr.quotaOrdCollPagID.numero}"/>
		</display:column>
		<display:column title="Descrizione" headerClass="tabRow sBold borderRight" class="borderRight">
			<s:property value="%{#attr.quotaOrdCollPagID.descrizione}"/>
		</display:column>
		<display:column title="Liquidazione" headerClass="tabRow sBold" class="tabRow tab_Right">
			<s:property value="%{#attr.quotaOrdCollPagID.liquidazione.annoLiquidazione}" /> - <s:property value="%{#attr.quotaOrdCollPagID.liquidazione.numeroLiquidazione.intValue()}" />
		</display:column>
		<display:column title="Importo" headerClass="tabRow sBold borderRight" class="borderRight">
			<s:property value="getText('struts.money.format', {#attr.quotaOrdCollPagID.importoAttuale})" default="-" />
		</display:column>						
	</display:table>
</div>
<div class="modal-footer">
		<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
</div>

<!-- /div modale dettaglio quote oordinativi PAGAMENTO-->