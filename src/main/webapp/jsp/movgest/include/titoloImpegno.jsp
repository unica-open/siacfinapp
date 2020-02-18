<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

<s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/> ${model.step1Model.annoImpegno}/${model.step1Model.numeroImpegno} - 
<s:if test="step1Model.oggettoImpegno!=null	">
	<s:if test="step1Model.oggettoImpegno!=''"> 
		<s:property value="step1Model.oggettoImpegno"/> -
	</s:if>	 
</s:if> 
<s:property value="getText('struts.money.format', {step1Model.importoImpegno})"/> (${model.step1Model.descrizioneStatoOperativoMovimento} dal 
<s:property value="%{step1Model.dataStatoOperativoMovimento}" />)
<s:if test="isImpegnoSdf()">   
	 - SENZA DISPONIBILITA' DI FONDI
</s:if>