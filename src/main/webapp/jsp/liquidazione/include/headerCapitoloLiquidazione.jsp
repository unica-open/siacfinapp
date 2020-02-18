<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:if test="capitoloSelezionato">

<h4>Capitolo <a class="tooltip-test" title="Visualizza dettagli" href="#capitoloTab" data-toggle="modal"><i class="icon-info-sign">&nbsp;<span class="nascosto">Visualizza dettagli</span></i></a> - ${capitolo.anno} / ${capitolo.numCapitolo} / ${capitolo.articolo}/ ${capitolo.ueb} - ${capitolo.descrizione} - ${capitolo.codiceStrutturaAmministrativa} - ${capitolo.descrizioneStrutturaAmministrativa} - tipo Finanziamento: ${capitolo.tipoFinanziamento}</h4> 


	<%-- <h4>
		Capitolo: <s:property value="step1Model.capitolo.anno"/> / <s:property value="step1Model.capitolo.numCapitolo"/> / <s:property value="step1Model.capitolo.articolo"/> / <s:property value="step1Model.capitolo.ueb"/>
		- <s:property value="step1Model.capitolo.descrizione"/> - <s:property value="step1Model.capitolo.codiceStrutturaAmministrativa"/>  
		- Tipo finanziamento: <s:property value="step1Model.capitolo.tipoFinanziamentoSelezionato"/>
	</h4> --%>
</s:if>
<s:else>
	<h4>Capitolo: </h4>
</s:else>




















<%-- <%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:if test="capitoloSelezionato">
	<h4>
		Capitolo <s:property value="capitolo.anno"/> / <s:property value="capitolo.numCapitolo"/> / <s:property value="capitolo.articolo"/> / <s:property value="capitolo.ueb"/>
		- <s:property value="capitolo.descrizione"/> - <s:property value="capitolo.codiceStrutturaAmministrativa"/>  
		- Tipo finanziamento: <s:property value="capitolo.tipoFinanziamentoSelezionato"/>
	</h4>
</s:if>
<s:else>
	<h4>Capitolo </h4>
</s:else> --%>
