<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<s:if test="hasActionErrors()">
	<%-- Messaggio di ERROR --%>
	<div class="alert alert-error">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		<strong>Attenzione!!</strong><br>
		<ul>
			<s:actionerror />
		</ul>
	</div>
</s:if>
<s:if test="hasActionMessages()">
	<%-- Messaggio di INFO --%>
	<div class="alert alert-success">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		<%-- Rimosso Attenzione!! per JIRA SIAC-5248 --%>
		<strong></strong><br>
		<ul>
			<s:actionmessage />
		</ul>
	</div>
</s:if>
<s:if test="hasActionWarnings()">
	<%-- Messaggio di WARNING --%>
	<div class="alert alert-warning">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		<strong>Attenzione!!</strong><br>
		<ul>
		   <s:iterator value="actionWarnings">
		       <s:property escapeHtml="false"/><br>
		   </s:iterator>
			
		</ul>
	</div>
</s:if>