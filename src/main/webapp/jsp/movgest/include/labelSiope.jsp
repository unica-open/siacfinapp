<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
	       
 <div id="refreshDescrizioneSiope" style="display: inline">
	  
	  <s:if test="teSupport.siopeSpesa!=null && teSupport.siopeSpesa.descrizione != null">
	  	<s:property value="teSupport.siopeSpesa.descrizione" />
	  </s:if>
	   <s:elseif test=" teSupport.siopeSpesaCod!=null && teSupport.siopeSpesaCod != ''">
	   	 <s:property value="teSupport.siopeSpesaCod +  ' non corrisponde a nessun siope'" />
	   </s:elseif>
 </div>
