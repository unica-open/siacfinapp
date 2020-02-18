<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:include value="/jsp/include/actionMessagesErrors.jsp" />
<s:if test="progettoTrovato">
	<h4>Elenco progetti</h4>     
	    <!--     TABELLE       RIEPILOGO   con azioni -->
	    
	    
  <s:if test="oggettoDaPopolareImpegno()"> 
	<display:table name="listaRicercaProgettoCronoprogrammi"  
	                 class="table table-striped table-bordered table-hover" 
	                 summary="riepilogo progetti"
					 uid="ricercaProgettoCronoprogrammaID">
	        <display:column>
	       		<s:radio list="%{#attr.ricercaProgettoCronoprogrammaID.uid}" name="radioUidCronoprogramma" theme="displaytag"></s:radio>
	        </display:column>
	        
	        <display:column title="Progetto" property="progetto.codice" />
	        <display:column title="Ambito">
	        	<s:property value="%{#attr.ricercaProgettoCronoprogrammaID.progetto.tipoAmbito.codice}"/> - <s:property value="%{#attr.ricercaProgettoCronoprogrammaID.progetto.tipoAmbito.descrizione}"/> 
	        </display:column>
	        <display:column title="Valore complessivo" property="progetto.valoreComplessivo" />
	        <display:column title="Cronoprogramma">
	        	<s:property value="%{#attr.ricercaProgettoCronoprogrammaID.codice}"/> - <s:property value="%{#attr.ricercaProgettoCronoprogrammaID.descrizione}"/> 
	        </display:column>
	</display:table>
  </s:if>
  <s:else>
	<display:table name="listaRicercaProgetto"  
	                 class="table table-striped table-bordered table-hover" 
	                 summary="riepilogo progetti"
					 uid="ricercaProgettoID">
	        <display:column>
	       		<s:radio list="%{#attr.ricercaProgettoID.codice}" name="radioCodiceProgetto" theme="displaytag"></s:radio>
	        </display:column>
	        
	        <display:column title="Progetto" property="codice" />
	        <display:column title="Ambito">
	        	<s:property value="%{#attr.ricercaProgettoID.tipoAmbito.codice}"/> - <s:property value="%{#attr.ricercaProgettoID.tipoAmbito.descrizione}"/> 
	        </display:column>
	        <display:column title="Valore complessivo" property="valoreComplessivo" />
	        <display:column title="Cronoprogramma">-</display:column>
	</display:table>
  </s:else>
</s:if>