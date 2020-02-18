<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<!-- ERRORI -->
<s:include value="/jsp/include/actionMessagesErrors.jsp" />


<s:if test="step1Model.trovatiAccPerVincolo" >
<h4>Elenco elementi trovati</h4>   



	 <display:table name="step1Model.listaAccPerVincoli"  
	                 class="table table-hover tab_left dataTable" 
	                 summary="riepilogo accertamenti"
					 uid="ricercaAccID">
					 
		<display:column>			 
	 		<s:radio list="%{#attr.ricercaAccID.uid}" name="radioCodiceAccPerVincoli" theme="displaytag" cssClass="radioCodiceAccPerVincoli" />
	 	</display:column>
	 	<display:column title="Numero">
	 	   	<s:property value="%{#attr.ricercaAccID.numero.intValue()}"/>
	 	</display:column>
	 	<display:column title="Descrizione" property="descrizione" />
	 	<display:column title="Soggetto">
	 			<s:property value="%{#attr.ricercaAccID.soggetto.denominazione}"/>
	 	</display:column>
	 	
	 	<display:column title="Importo" property="importoAttuale" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
	 	<display:column title="Utilizzabile" property="disponibilitaUtilizzare" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
	 	
	</display:table> 
</s:if>	