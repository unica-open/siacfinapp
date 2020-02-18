<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<!-- Indietro gestito da Crumb -->
<s:if test="%{previousCrumb == null}">
	<s:url action="redirectToCruscotto" var="indietroURL" />
	<s:a cssClass="btn" href="%{indietroURL}"> indietro </s:a>
	<%--<a class="btn" href='<s:property value="#indietroURL"/>'>Indietro</a> --%>
	<!-- deve stampare questo -->
</s:if>
<s:else>
<%-- 	<s:url var="surl" action="%{previousCrumb.action}" namespace="%{previousCrumb.namespace}"  --%>
<%-- 	       includeContext="false"/> --%>
<%-- 	<c:url var="url" value="${surl}"> --%>
<%-- 		<c:forEach var="p" items="${previousCrumb.params}"> --%>
<%-- 			<c:forEach var="v" items="${p.value}">${v} --%>
<%-- 				<c:param name="${p.key}" value="${v}"/> --%>
<%-- 			</c:forEach> --%>
<%-- 		</c:forEach> --%>
<%-- 		<c:param name="forceReload" value="false"/> --%>
<%-- 	</c:url> --%>
	<input type="hidden" name="forceReload" value="true" id="forceReloadPerIndietro"/>		
	<s:submit action="%{previousCrumb.action}" id="backbutton" value="indietro" cssClass="btn" onclick="return setForceReload(false);"></s:submit>
<%-- 	<s:submit action="%{#attr['url']}" value="indietro" cssClass="btn" onclick="return setForceReload(false);"></s:submit> --%>
	
	
	<script type="text/javascript">
		function setForceReload(flag) {
			$("#forceReloadPerIndietro").val(flag);
			return true;
		}
	</script>
	
</s:else> 
