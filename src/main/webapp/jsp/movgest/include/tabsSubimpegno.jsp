<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<ul class="nav nav-tabs">
	<li>
		<a id="gotoAggiornaImpegnoStep1_false" class="confermaFinApp"><s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/></a>
	</li>
	<li class="active">
		<a href="#"><s:property value="%{labels.OGGETTO_GENERICO}"/></a>
	</li>

	<s:if test="!disabilitaTabModificheAggiornamento()">
		<s:if test="abilitaTabModificheImpAcc()">
			<li>
				<a id="gotoElencoMovimentoSpesa_false" class="confermaFinApp">Modifiche</a>
			</li>
		</s:if>	
	</s:if>	
</ul>