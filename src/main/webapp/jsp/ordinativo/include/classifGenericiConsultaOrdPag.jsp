<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<!-- ALTRI CLASSIFICATORI -->

	<s:if
		test="ordinativoPagamento.codClassGen11 !=null && listaClassificatoriGen21!=null && listaClassificatoriGen21.size()>0">
			
			<li> 
			
			<s:iterator value="listaClassificatoriGen21" status="statGen11">
				<s:if test="#statGen11.first == true">
					 <dfn><s:property value="tipoClassificatore.descrizione"/></dfn>
				</s:if>
			</s:iterator>
					
			<s:iterator value="listaClassificatoriGen21" status="statGen11">
				<s:if test="codice == ordinativoPagamento.codClassGen11">
					 <dl><s:property value="descrizione"/></dl>
				</s:if>
			</s:iterator>
			
			</li>
			
	</s:if>
	
	<s:if
		test="ordinativoPagamento.codClassGen12 !=null && listaClassificatoriGen22!=null && listaClassificatoriGen22.size()>0">
			
			<li> 
			
			<s:iterator value="listaClassificatoriGen22" status="statGen12">
				<s:if test="#statGen12.first == true">
					 <dfn><s:property value="tipoClassificatore.descrizione"/></dfn>
				</s:if>
			</s:iterator>
					
			<s:iterator value="listaClassificatoriGen22" status="statGen12">
				<s:if test="codice == ordinativoPagamento.codClassGen12">
					 <dl><s:property value="descrizione"/></dl>
				</s:if>
			</s:iterator>
			
			</li>
			
	</s:if>
	
	
	<s:if
		test="ordinativoPagamento.codClassGen13 !=null && listaClassificatoriGen23!=null && listaClassificatoriGen23.size()>0">
			
			<li> 
			
			<s:iterator value="listaClassificatoriGen23" status="statGen13">
				<s:if test="#statGen13.first == true">
					 <dfn><s:property value="tipoClassificatore.descrizione"/></dfn>
				</s:if>
			</s:iterator>
				
			<s:iterator value="listaClassificatoriGen23" status="statGen13">
				<s:if test="codice == ordinativoPagamento.codClassGen13">
					 <dl><s:property value="descrizione"/></dl>
				</s:if>
			</s:iterator>
			
			</li>
			
	</s:if>
	
	<s:if
		test="ordinativoPagamento.codClassGen14 !=null && listaClassificatoriGen24!=null && listaClassificatoriGen24.size()>0">
			
			<li> 
			
			<s:iterator value="listaClassificatoriGen24" status="statGen14">
				<s:if test="#statGen14.first == true">
					 <dfn><s:property value="tipoClassificatore.descrizione"/></dfn>
				</s:if>
			</s:iterator>
					
			<s:iterator value="listaClassificatoriGen24" status="statGen14">
				<s:if test="codice == ordinativoPagamento.codClassGen14">
					 <dl><s:property value="descrizione"/></dl>
				</s:if>
			</s:iterator>
				
			
			</li>
			
	</s:if>
	
	<s:if
		test="ordinativoPagamento.codClassGen15 !=null && listaClassificatoriGen25!=null && listaClassificatoriGen25.size()>0">
			
			<li> 
			
			<s:iterator value="listaClassificatoriGen25" status="statGen15">
				<s:if test="#statGen15.first == true">
					 <dfn><s:property value="tipoClassificatore.descrizione"/></dfn>
				</s:if>
			</s:iterator>
					
			<s:iterator value="listaClassificatoriGen25" status="statGen15">
				<s:if test="codice == ordinativoPagamento.codClassGen15">
					 <dl><s:property value="descrizione"/></dl>
				</s:if>
			</s:iterator>
				
			
			</li>
			
	</s:if>
	
	<s:if test="ordinativoPagamento.classificatoreStipendi !=null && listaClassificatoriStipendi!=null && listaClassificatoriStipendi.size()>0">
		<li>
			<s:iterator value="listaClassificatoriStipendi" status="statGenSti">
				<s:if test="#statGenSti.first == true">
					 <dfn><s:property value="tipoClassificatore.descrizione"/></dfn>
				</s:if>
			</s:iterator>
			<s:property value="ordinativoPagamento.classificatoreStipendi.descrizione" />
		</li>
	</s:if>
