<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:if test="step1Model.soggettoSelezionato">
	<h4>
		<s:if test="%{subImpegnoSelected}">
       		Soggetto: <s:property value="soggettoSubAttuale.codiceSoggetto" /> -	<s:property value="soggettoSubAttuale.denominazione" /> - <s:property value="soggettoSubAttuale.codiceFiscale" />
      	</s:if>
      	<s:else>Soggetto: <s:property value="soggettoImpegnoAttuale.codiceSoggetto" /> -	<s:property value="soggettoImpegnoAttuale.denominazione" /> - <s:property value="soggettoImpegnoAttuale.codiceFiscale" /></s:else>
		
	</h4>
</s:if>
<s:else>
	<h4>Soggetto: </h4>
</s:else>
