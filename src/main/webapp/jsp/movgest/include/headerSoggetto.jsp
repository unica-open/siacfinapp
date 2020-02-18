<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:if test="step1Model.soggettoSelezionato">
	<s:if test="step1Model.soggetto.codCreditore!=null">
		<h4>
			Soggetto: <s:property value="step1Model.soggetto.codCreditore" /> -	<s:property value="step1Model.soggetto.denominazione" /> - <s:property value="step1Model.soggetto.codfisc" />
		</h4>
	</s:if>
	<s:else>
		<h4>Soggetto:</h4>
	</s:else>
</s:if>
<s:else>
	<h4>Soggetto:</h4>
</s:else>
