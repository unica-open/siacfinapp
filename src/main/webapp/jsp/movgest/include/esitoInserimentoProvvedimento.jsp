<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

	 <s:include value="/jsp/include/actionMessagesErrors.jsp" /> 

<%--       <s:if test="provvedimentoInserito"> --%>
<!--         <h4>Provvedimento Inserito:</h4>  -->
<%--       </s:if>       --%>
      
      
<script type="text/javascript">

	$(document).ready(function() {

		$("a[rel=popover]").popover();
	});
	
</script> 