<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
	       
 <div id="refreshDescrizioneProgetto" style="display: inline">
	  
	  <s:if test="step1Model.progettoImpegno!=null && step1Model.progettoImpegno.descrizione != null">
	  	<s:property value="step1Model.progettoImpegno.descrizione" />
	  </s:if>
	  <s:elseif test=" step1Model.progetto!=null && step1Model.progetto != ''">
	   	 <%-- <s:property value="step1Model.progetto +  ' non corrisponde a nessun progetto'" /> --%>
	   	 <%-- <s:property value="step1Model.progetto" /> --%>
	  </s:elseif>
 </div>
