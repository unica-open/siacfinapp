<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:if test="!isAbilitatoAggiornamentoGenerico()">
  	 	<ul class="nav nav-tabs">
		<li class="active">
			<a href="#"><s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/></a>
		</li>
</ul>
</s:if>
<s:else>
	 	<s:include value="/jsp/movgest/include/tabAggImpegno.jsp" />
</s:else>