<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<s:if test="soggettoSelezionatoDue">
	<h4>
	Titolare Cessione di incasso: 
	 <s:property value="soggettoDue.codCreditore" /> -	<s:property value="soggettoDue.denominazione" /> - <s:property value="soggettoDue.codfisc" />
	</h4>
</s:if>
<s:else>
	<h4>Titolare Cessione di incasso </h4>
</s:else>
