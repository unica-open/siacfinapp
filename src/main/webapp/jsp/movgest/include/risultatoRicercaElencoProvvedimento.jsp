<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

	 <s:include value="/jsp/include/actionMessagesErrors.jsp" /> 

      <s:if test="provvedimentoTrovato">
        <h4>Elenco provvedimenti trovati</h4> 
        
        <display:table name="listaRicercaProvvedimento"  
	                 class="table table-striped table-bordered table-hover" 
	                 summary="elenco provvedimenti"
					 uid="ricercaProvvedimentoID">
	      <display:column>
	      	<s:radio list="%{#attr.ricercaProvvedimentoID.uid}" name="radioCodiceProvvedimento" theme="displaytag" cssClass="radioCodiceProvvedimento"></s:radio>
	      </display:column>
	      <display:column title="Anno">${attr.ricercaProvvedimentoID.annoProvvedimento}</display:column>
	      <display:column title="Numero">${attr.ricercaProvvedimentoID.numeroProvvedimento}</display:column>
	      <display:column title="Tipo">
	      	<s:property value="%{#attr.ricercaProvvedimentoID.CodiceTipoProvvedimento}" /> - <s:property value="%{#attr.ricercaProvvedimentoID.tipoProvvedimento}" />
	      </display:column>
	      <display:column title="Oggetto">${attr.ricercaProvvedimentoID.oggetto}</display:column>
<%-- 	      <display:column title="ciao"> --%>
<%-- 	      	<a href="#" data-trigger="hover" data-placement="left" rel="popover" title="Descrizione" data-content="${attr.ricercaProvvedimentoID.strutturaAmministrativa}"> --%>
<%-- 	      			${attr.ricercaProvvedimentoID.CodiceStrutturaAmministrativa} --%>
<%-- 				<s:property value="%{#attr.ricercaProvvedimentoID.CodiceStrutturaAmministrativa}"/> --%>
<!-- 			</a> -->
<%-- 	      </display:column> --%>

		  <display:column title="<abbr title='Struttura Amministrativa Responsabile'>Strutt Amm Resp</abbr>">
	      	<a href="#" data-trigger="hover" data-placement="left" rel="popover" title="Descrizione" data-content="${attr.ricercaProvvedimentoID.strutturaAmministrativa}">
				<s:property value="%{#attr.ricercaProvvedimentoID.CodiceStrutturaAmministrativa}"/>
			</a>
	      </display:column>
	      <display:column title="Stato">${attr.ricercaProvvedimentoID.stato}</display:column>
	    </display:table>
      </s:if>      
<script type="text/javascript">

	$(document).ready(function() {

		$("a[rel=popover]").popover();
	});
	
</script> 