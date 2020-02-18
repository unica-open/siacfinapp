<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<!-- ALTRI CLASSIFICATORI -->

	<s:if
		test="ordinativoIncasso.codClassGen16 !=null && listaClassificatoriGen26!=null && listaClassificatoriGen26.size()>0">
			
			<li> 
			
			<s:iterator value="listaClassificatoriGen26" status="statGen16">
				<s:if test="#statGen16.first == true">
					 <dfn><s:property value="tipoClassificatore.descrizione"/></dfn>
				</s:if>
			</s:iterator>
				
			<s:iterator value="listaClassificatoriGen26" status="statGen16">
				<s:if test="codice == ordinativoIncasso.codClassGen16">
					 <dl><s:property value="descrizione"/></dl>
				</s:if>
			</s:iterator>
				
			
			</li>
			
	</s:if>
	
	<s:if
		test="ordinativoIncasso.codClassGen17 !=null && listaClassificatoriGen27!=null && listaClassificatoriGen27.size()>0">
			
			<li> 
			
			<s:iterator value="listaClassificatoriGen27" status="statGen17">
				<s:if test="#statGen17.first == true">
					 <dfn><s:property value="tipoClassificatore.descrizione"/></dfn>
				</s:if>
			</s:iterator>
				
			<s:iterator value="listaClassificatoriGen27" status="statGen17">
				<s:if test="codice == ordinativoIncasso.codClassGen17">
					 <dl><s:property value="descrizione"/></dl>
				</s:if>
			</s:iterator>
				
			
			</li>
			
	</s:if>
	
	
	<s:if
		test="ordinativoIncasso.codClassGen18 !=null && listaClassificatoriGen28!=null && listaClassificatoriGen28.size()>0">
			
			<li> 
			
			<s:iterator value="listaClassificatoriGen28" status="statGen18">
				<s:if test="#statGen18.first == true">
					 <dfn><s:property value="tipoClassificatore.descrizione"/></dfn>
				</s:if>
			</s:iterator>
				
			<s:iterator value="listaClassificatoriGen28" status="statGen18">
				<s:if test="codice == ordinativoIncasso.codClassGen18">
					 <dl><s:property value="descrizione"/></dl>
				</s:if>
			</s:iterator>
			
			</li>
			
	</s:if>
	
	<s:if
		test="ordinativoIncasso.codClassGen19 !=null && listaClassificatoriGen29!=null && listaClassificatoriGen29.size()>0">
			
			<li> 
			
			<s:iterator value="listaClassificatoriGen29" status="statGen19">
				<s:if test="#statGen19.first == true">
					 <dfn><s:property value="tipoClassificatore.descrizione"/></dfn>
				</s:if>
			</s:iterator>
					
			<s:iterator value="listaClassificatoriGen29" status="statGen19">
				<s:if test="codice == ordinativoIncasso.codClassGen19">
					 <dl><s:property value="descrizione"/></dl>
				</s:if>
			</s:iterator>
				
			
			</li>
			
	</s:if>
	
	<s:if
		test="ordinativoIncasso.codClassGen20 !=null && listaClassificatoriGen30!=null && listaClassificatoriGen30.size()>0">
			
			<li> 
			
			<s:iterator value="listaClassificatoriGen30" status="statGen20">
				<s:if test="#statGen20.first == true">
					 <dfn><s:property value="tipoClassificatore.descrizione"/></dfn>
				</s:if>
			</s:iterator>
					
			<s:iterator value="listaClassificatoriGen30" status="statGen20">
				<s:if test="codice == ordinativoIncasso.codClassGen20">
					 <dl><s:property value="descrizione"/></dl>
				</s:if>
			</s:iterator>
				
			
			</li>
			
	</s:if>
	<s:if test="ordinativoIncasso.classificatoreStipendi !=null && listaClassificatoriStipendi!=null && listaClassificatoriStipendi.size()>0">
		<li>
			<s:iterator value="listaClassificatoriStipendi" status="statGenSti">
				<s:if test="#statGenSti.first == true">
					 <dfn><s:property value="tipoClassificatore.descrizione"/></dfn>
				</s:if>
			</s:iterator>
			<s:property value="ordinativoIncasso.classificatoreStipendi.descrizione" />
		</li>
	</s:if>
	<li>
	
		<dfn>Causale</dfn> 
		<dl><s:property value="ordinativoIncasso.causale.codice"/> - <s:property value="ordinativoIncasso.causale.descrizione"/></dl>
	</li>
