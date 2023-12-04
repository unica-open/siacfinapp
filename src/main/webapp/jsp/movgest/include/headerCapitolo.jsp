<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:if test="step1Model.capitoloSelezionato">

<h4>Capitolo <a class="tooltip-test" title="Visualizza dettagli" href="#capitoloTab" data-toggle="modal"><i class="icon-info-sign">&nbsp;<span class="nascosto">Visualizza dettagli</span></i></a> - ${model.step1Model.capitolo.anno} / ${model.step1Model.capitolo.numCapitolo} / ${model.step1Model.capitolo.articolo}/ ${model.step1Model.capitolo.ueb} - ${model.step1Model.capitolo.descrizione} - ${model.step1Model.capitolo.descrizioneStrutturaAmministrativa} - tipo Finanziamento: ${model.step1Model.capitolo.tipoFinanziamento}</h4> 


	<%-- <h4>
		Capitolo: <s:property value="step1Model.capitolo.anno"/> / <s:property value="step1Model.capitolo.numCapitolo"/> / <s:property value="step1Model.capitolo.articolo"/> / <s:property value="step1Model.capitolo.ueb"/>
		- <s:property value="step1Model.capitolo.descrizione"/> - <s:property value="step1Model.capitolo.codiceStrutturaAmministrativa"/>  
		- Tipo finanziamento: <s:property value="step1Model.capitolo.tipoFinanziamentoSelezionato"/>
	</h4> --%>
</s:if>
<s:else>
	<h4>Capitolo: </h4>
</s:else>
