<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<s:if test="gestioneOrdinativoStep1Model.soggettoSelezionato">
	<h4>

		Soggetto: <s:property value="gestioneOrdinativoStep1Model.soggetto.codCreditore" /> -	<s:property value="gestioneOrdinativoStep1Model.soggetto.denominazione" /> - <s:property value="gestioneOrdinativoStep1Model.soggetto.codfisc" />
	</h4>
</s:if>
<s:elseif test="gestioneOrdinativoStep2Model.listaAccertamento[0].classeSoggetto!=null">
	<h4>
		<s:property value="%{labels.SOGGETTO}"/>: 
		<s:property value="gestioneOrdinativoStep2Model.listaAccertamento[0].classeSoggetto.codice" /> - <s:property value="gestioneOrdinativoStep2Model.listaAccertamento[0].classeSoggetto.descrizione" />
	</h4>	
</s:elseif>
<s:else>
    <!-- creditore o debitore -->
	<h4><s:property value="%{labels.SOGGETTO}"/></h4>
</s:else>
