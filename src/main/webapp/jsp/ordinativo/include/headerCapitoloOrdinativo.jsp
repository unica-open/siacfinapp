<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:if test="gestioneOrdinativoStep1Model.capitoloSelezionato">
	<h4>
		Capitolo: <a class="tooltip-test" title="Visualizza dettagli" href="#capitoloTabOrd" data-toggle="modal"><i class="icon-info-sign">&nbsp;<span class="nascosto">Visualizza dettagli</span></i></a> <s:property value="gestioneOrdinativoStep1Model.capitolo.anno"/> / <s:property value="gestioneOrdinativoStep1Model.capitolo.numCapitolo"/> / <s:property value="gestioneOrdinativoStep1Model.capitolo.articolo"/> / <s:property value="gestioneOrdinativoStep1Model.capitolo.ueb"/>
		- <s:property value="gestioneOrdinativoStep1Model.capitolo.descrizione"/> - <s:property value="gestioneOrdinativoStep1Model.capitolo.codiceStrutturaAmministrativa"/> - <s:property value="gestioneOrdinativoStep1Model.capitolo.descrizioneStrutturaAmministrativa"/>  
		- Tipo finanziamento: <s:property value="gestioneOrdinativoStep1Model.capitolo.tipoFinanziamento"/>
	</h4>
</s:if>
<s:else>
	<h4>Capitolo:</h4>
</s:else>
