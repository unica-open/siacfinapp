<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>	
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<fmt:setBundle basename="globalMessages"/>
	<s:include value="/jsp/include/header.jsp" />

	<div id="contentPanel">
		<div id="centerWrapper">
			<div id="centerPanel">
				<div >

	<h3><fmt:message key="error.unauthorized.title" /></h3>
	
	<div id="applicationError">
	<fmt:message key="error.unauthorized.warning" />
		<br />
	<fmt:message key="error.unauthorized.message" />	
	
	
	<s:property value="exception"/>
	
	
		</div>
	<div id="applicationError_links">
		<div>
			<a href="HomePage.do" class="buttonWidget"><fmt:message key="error.unauthorized.btnmsg" /></a>
		</div>
	</div>
				</div>
			</div>
		</div>
	</div>
	

<s:include value="/jsp/include/javascript.jsp" />
<s:include value="/jsp/include/footer.jsp" />