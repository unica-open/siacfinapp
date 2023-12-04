<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


    <%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />
    
    
    <%-- Inclusione JavaScript NUOVO --%>
   <s:include value="/jsp/include/javascript.jsp" />	


</head>

<s:include value="/jsp/include/header.jsp" />

  <body>
	<div class="container-fluid-banner">
<!-- 
=======================================================================
											*****************
											inclusione Banner portale
											*****************
=======================================================================
-->

<!--
=======================================================================
											*****************
											fine ////////inclusione Banner portale
											*****************
=======================================================================
-->
<!--
=======================================================================
											*****************
											inclusione Banner applicativo
											*****************
=======================================================================
-->
<!--
=======================================================================
											*****************
											fine //////inclusione Banner applicativo
											*****************
=======================================================================
-->
<!--
=======================================================================
											*****************
											inclusione login
											*****************
=======================================================================
-->
<!--
=======================================================================
											*****************
											fine //////inclusione login
											*****************
=======================================================================
-->
<a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->

<!--   TABELLE       RIEPILOGO    -->

 
<div class="container-fluid">
<div class="row-fluid">
<div class="span12 ">

<div class="contentPage">  
	<%-- SIAC-7952 rimuovo .do dalla action --%>
	<s:form method="post" action="collegaSoggetti">
	<s:include value="/jsp/include/actionMessagesErrors.jsp" /> 

<h3>Soggetti concatenati</h3>

<h4> Successivi </h4>
<display:table name="soggettiSuccessivi" class="table table-hover" summary="riepilogo soggetti successivi" pagesize="10" 
requestURI="collegaSoggetti.do" uid="soggettoSuccessivoID">
        <display:column title="Codice" property="codiceSoggetto" style="width:82px; height:36px;"/>
        <display:column title="Codice Fiscale" property="codiceFiscale" style="width:231px; height:36px;"/>
        <display:column title="Partita IVA" property="partitaIva"style="width:120px; height:36px;"/>
        <display:column title="Denominazione" property="denominazione"style="width:167px; height:36px;"/>
        <display:column title="Stato" property="statoOperativo" style="width:139px; height:36px;"/>
        <display:column title="Tipo Collegamento" property="tipoLegame"style="width:199px; height:36px;"/>
        <display:column title="" style="width:155px; height:36px;"/>
        <display:column title="Tipo Collegamento" property="tipoLegame"/>
        <display:column title="" style="width:133px; height:35px;"/>
</display:table>                         
<h4> Precedenti </h4>
<s:hidden name="azioneDaEseguire" id="azioneDaEseguire"/>

<display:table name="soggettiPrecedenti" class="table table-hover" summary="riepilogo soggetti precedenti" 
pagesize="10" requestURI="collegaSoggetti.do" uid="soggettoPrecedenteID">
        <display:column title="Codice" property="codiceSoggetto"/>
        <display:column title="Codice Fiscale" property="codiceFiscale" />
        <display:column title="Partita IVA" property="partitaIva"/>
        <display:column title="Denominazione" property="denominazione"/>
        <display:column title="Stato" property="statoOperativo"/>
        <display:column title="Tipo Collegamento" property="tipoLegame"/>
        <display:column title="">
        	<%-- task-131 <s:url id="deleteUrl" action="collegaSoggetti.do"> --%> 
  		    <s:url var="deleteUrl" action="collegaSoggetti"> 
  		    	<s:param name="soggettoPrecedenteDaAnnullare" value="%{#attr.soggettoPrecedenteID.codiceSoggetto}" />
   			</s:url>
            <button class="btn" onclick="document.getElementById('azioneDaEseguire').value='${deleteUrl}'"  >
                <s:a href="%{deleteUrl}"/>
				<i class="icon-trash"></i> elimina
		    </button>	 	
        </display:column>
</display:table>                         
<h3>Ricerca Soggetto</h3>
<p>&Egrave; necessario inserire almeno un criterio di ricerca.</p>
	<fieldset class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="Codice">Codice</label>
			<div class="controls">
				<s:textfield id="Codice" name="codice" cssClass="span4 required"/>
				<label for="den" class="radio inline">Denominazione</label>
				<s:textfield id="den" name="denominazione" cssClass="span6 required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="iva">Partita IVA</label>
			<div class="controls">
				<s:textfield id="iva" name="iva" cssClass="span4 required"/>
				<label class="radio inline" for="codfisc">Codice Fiscale</label>
				<s:textfield id="codfisc" name="codiceFiscale" cssClass="span6 required"/>
			</div>
		</div>
	</fieldset>
	<p>
		<!-- task-131 <s:submit name="annulla" value="annulla" method="annullaRicerca" cssClass="btn" /> -->
		<!-- task-131 <s:submit name="cerca" value="cerca" method="cerca" cssClass="btn" /> -->
		<s:submit name="annulla" value="annulla" action="collegaSoggetti_annullaRicerca" cssClass="btn" />
		<s:submit name="cerca" value="cerca" action="collegaSoggetti_cerca" cssClass="btn" />
	</p>
<s:if test="soggettiRicerca != null && soggettiRicerca.size() > 0">
	<h5>Risultati della ricerca</h5>
</s:if>     
<display:table name="soggettiRicerca" class="table table-hover" summary="riepilogo soggetti ricercati" pagesize="10" requestURI="collegaSoggetti.do" uid="soggettoRicercaID">
      <display:column>
      	<s:radio list="%{#attr.soggettoRicercaID.codiceSoggetto}" name="radioCodiceSoggetto" theme="displaytag"></s:radio>
      </display:column>
      <display:column title="Codice" property="codiceSoggetto"/>
      <display:column title="Codice Fiscale" property="codiceFiscale" />
      <display:column title="Partita IVA" property="partitaIva"/>
      <display:column title="Denominazione" property="denominazione"/>
      <display:column title="Stato" property="statoOperativo" />
</display:table>           
<s:if test="soggettiRicerca != null && soggettiRicerca.size() > 0">
	<p>    
		<input class="btn" href="#sceltaLegame" data-toggle="modal" type="button" value="aggiungi legame"/>  
	</p>
</s:if>       		
<div id="sceltaLegame" class="modal hide fade" tabindex="-1" data-role="dialog" data-aria-labelledby="sceltaLegame" data-aria-hidden="true">
	<div class="modal-body">
		<div class="alert alert-success">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
          	<p>Si &egrave; deciso di creare una catena di legami. Proseguire?</p>
		</div>
        <div class="control-group">
            <label class="control-label" for="tipoleg">Tipo legame</label>
            <div class="controls">
           		<s:select list="listaTipoLegame" id="listaTipoLegame" name="tipoLegame" listKey="codice" listValue="descrizione"/>
           	</div>
       </div> 
	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" data-aria-hidden="true">no, indietro</button>
		<!-- task-131 <s:submit id="btnCreaLegame" name="btnCreaLegame" value="si, prosegui" cssClass="btn btn-primary" method="creaLegame"/> -->
		<s:submit id="btnCreaLegame" name="btnCreaLegame" value="si, prosegui" cssClass="btn btn-primary" action="collegaSoggetti_creaLegame"/>
	</div>
</div>   
<p class="marginLarge"> 
  	<s:include value="/jsp/include/indietro.jsp" />
</p>  	                                 
</s:form>
</div>	
</div>	
<s:include value="/jsp/include/footer.jsp" />