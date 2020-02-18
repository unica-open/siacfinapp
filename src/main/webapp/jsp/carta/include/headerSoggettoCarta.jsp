<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<s:if test="soggettoSelezionato">
	<h4>

		Soggetto: <s:property value="soggetto.codCreditore" /> -	<s:property value="soggetto.denominazione" /> - <s:property value="soggetto.codfisc" />
	</h4>
</s:if>
<s:else>
    	<h4>Soggetto </h4>
</s:else>
