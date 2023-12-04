<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="bc" uri="/struts-arianna-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Gestione della navigazione --%>
<p class="nascosto">
	<a name="A-sommario" title="A-sommario"></a>
</p>

<ul id="sommario" class="nascosto">
	<li><a href="#A-contenuti">Salta ai contenuti</a></li>
</ul>
<%-- Termine navigazione --%>

<hr>

<%-- Banner --%>
<div class="container-fluid-banner">
	<%-- Inclusione Banner del portale --%>
	<r:include url="/ris/servizi/siac/include/portalheader.html" resourceProvider="rp"/>

	<%-- Inclusione informazioni Utente loggato --%>
	<s:include value="/jsp/include/infoUtenteLogin.jsp" />
	
	<%-- Inclusione Banner dell'applicativo --%>
	<r:include url="/ris/servizi/siac/include/applicationHeader.html" resourceProvider="rp"/>

	<a name="A-contenuti" title="A-contenuti"></a>

	<%-- BreadBrumb --%>
	<div class="row-fluid">
		<div class="span12">
			<ul class="breadcrumb">
				<%-- Il primo crumb è sempre la redirezione al Cruscotto --%>
				<s:url action="redirectToCruscotto" var="homeURL" />
				<li><a href="${homeURL}">Home</a></li>
				
				<bc:breadcrumbs var='c' status='s'>
		    		<%-- Divisore --%>
		    		&nbsp;<span class="divider">&gt;</span>&nbsp;
				    
				    <s:if test="#s.last">
				    	<%-- Se è l'ultimo crumb, allora renderizzalo come semplice label --%>
						<li class="active"><s:property value='name'/></li>
					</s:if> <s:else>
						<%-- cfr. http://code.google.com/p/struts2-arianna-plugin/wiki/TagLibrary --%>
						<%-- Tag per la costruzione dell'URL --%>
		    			<s:url var="surl" action="%{action}" namespace="%{namespace}" includeContext="false"/>
		    			
		    			<%-- Costruzione dei parametri per l'URL --%>
					    <c:url var="url" value="${surl}">
					        <%-- Iterazione sui parametri --%>             
					        <c:forEach var="p" items="${c.params}">
					            <%-- Iterazione sul valore dei parametri --%>             
					            <c:forEach var="v" items="${p.value}">${v}
					                <c:param name="${p.key}" value="${v}"/>
					            </c:forEach>
					            <c:param name="forceReload" value="false"/>
					        </c:forEach>
					    </c:url>
					
					    <%-- Utilizzo dell'URL costruito --%>
					    <li><s:a href="%{#attr['url']}">${c.name}</s:a></li>
					    <%-- a href='<s:property value="%{#attr['url']}"/>'>${c.name}</a--%>
					</s:else>
				</bc:breadcrumbs>
			</ul>
		</div>	
	</div>	
</div>