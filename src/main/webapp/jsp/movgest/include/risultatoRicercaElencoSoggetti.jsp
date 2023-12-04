<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:include value="/jsp/include/actionMessagesErrors.jsp" />
<s:if test="soggettoTrovato">
	<h4>Elenco soggetti</h4>     
	    <!--     TABELLE       RIEPILOGO   con azioni -->
	<display:table name="listaRicercaSoggetto"  
	                 class="table table-striped table-bordered table-hover" 
	                 summary="riepilogo soggetti"
					 uid="ricercaSoggettoID">
	        <display:column>
	       		<s:radio list="%{#attr.ricercaSoggettoID.codiceSoggetto}" name="radioCodiceSoggetto" theme="displaytag"></s:radio>
	        </display:column>
	        
	        <!-- LEVO MOMENTANEAMENTE GLI ORDINAMENTI -->
	        
	        <display:column title="Codice" property="codiceSoggetto" />
	        <display:column title="Codice Fiscale" property="codiceFiscale" />
	        <display:column title="Partita IVA" property="partitaIva" />	       
	        <display:column title="Denominazione" property="denominazione"/>
	        <display:column title="Stato" property="statoOperativo" />	      
	</display:table>
</s:if>