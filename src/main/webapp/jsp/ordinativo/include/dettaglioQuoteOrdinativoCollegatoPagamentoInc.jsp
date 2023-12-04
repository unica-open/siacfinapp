<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<!-- div modale dettaglio quote oordinativi INCASSO-->
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	<h4>Ordinativo - <s:property value="ordinativoCollegatoSelezionato.numero"/></h4>
</div>
<div class="modal-body">
	<display:table name="ordinativoCollegatoSelezionato.elencoSubOrdinativiDiIncasso" class="table table-hover tab_centered" summary="riepilogo" uid="quotaOrdCollIncID" pagesize="10"
	 	requestURI="consultaOrdinativoPagamento.do"
	>

		<display:column title="Numero" headerClass="tabRow sBold" class="tabRow tab_Right">
			<s:property value="%{#attr.quotaOrdCollIncID.numero}"/>
		</display:column>
		<s:if test="%{false}">
			<display:column title="Natura" headerClass="tabRow sBold" class="tabRow tab_Right" >
				
			</display:column>
			<display:column title="Tipo onere" headerClass="tabRow sBold" class="tabRow tab_Right" >
				
			</display:column>
		</s:if>
		<s:else>
			<display:column title="Descrizione" headerClass="tabRow sBold borderRight" class="borderRight">
				<s:property value="%{#attr.quotaOrdCollIncID.descrizione}"/>
			</display:column>
			<display:column title="Documento" headerClass="tabRow sBold" class="tabRow tab_Right" >
				
			</display:column>
		</s:else>
		<display:column title="Importo" headerClass="tabRow sBold borderRight" class="borderRight">
			<s:property value="getText('struts.money.format', {#attr.quotaOrdCollIncID.importoAttuale})" default="-" />
		</display:column>						
	</display:table>
</div>
<div class="modal-footer">
		<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
</div>

<!-- /div modale dettaglio quote oordinativi INCASSO-->