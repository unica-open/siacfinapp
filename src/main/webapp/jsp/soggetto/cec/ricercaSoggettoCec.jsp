<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

    <%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />
    
    
    <%-- Inclusione JavaScript NUOVO --%>
   <s:include value="/jsp/include/javascript.jsp" />	
   
  </head>

  <body>
  	
  <s:include value="/jsp/include/header.jsp" />
  
<div class="container-fluid-banner">


<a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->


 
    <div class="container-fluid">
<div class="row-fluid">
<div class="span12 ">

<div class="contentPage">  


<%-- SIAC-7952 rimuovo .do dalla action --%>
<s:form id="ricercaSoggetto" action="ricercaSoggettoCec" method="post" focusElement="cerca" >
	<!-- task-224 -->
	<h3>Ricerca Soggetto di Cassa Economale</h3>
	<%-- Messaggio di ERROR --%>
	<s:if test="hasActionErrors()">
		<div class="alert alert-error">
			<button type="button" class="close" data-dismiss="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
				<s:actionerror/>
			</ul>
		</div>
	</s:if>
	
	
		<s:if test="hasActionMessages()">
			<%-- Messaggio di WARNING --%>
			<div class="alert alert-warning">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
					<s:actionmessage/>
				</ul>
			</div>
		</s:if>
	
	
<%--     <s:submit name="cerca" value="cerca" method="cerca" cssClass="btn btn-primary pull-right" />  --%>
<!--     <br/> -->
<!--     <br/> -->
<!--     <br/> -->
	<fieldset class="form-horizontal">
	
 	    	<div class="control-group">
	        	<label class="control-label" for="codice">Codice</label>
	            <div class="controls">
	            	<s:textfield id="codice" name="codice" onkeyup="return checkItNumbersOnly(event)" cssClass="span2"/>
					<span class="al">
						<label class="radio inline" for="codiceFiscale">Codice fiscale</label>
						<s:textfield id="codiceFiscale" name="codiceFiscale" cssClass="span2" maxlength="16" pattern="[A-Z]"/>
					</span>
					<span class="al">
						<label class="radio inline" for="partitaIva">Partita IVA</label>
						<s:textfield id="partitaIva" name="partitaIva" cssClass="span2" maxlength="11"/>
					</span>
					<span class="al">
						<label class="radio inline" for="codiceFiscaleEstero">Codice fiscale estero</label>
						<s:textfield id="codiceFiscaleEstero" name="codiceFiscaleEstero" cssClass="span2" maxlength="11"/>
					</span>
			   </div>
	       </div>
	   <div class="control-group">
       	   <label class="control-label" for="denominazione">Denominazione</label>
           <div class="controls">
           		<s:textfield id="denominazione" name="denominazione" cssClass="span9"/>
           </div>
       </div>
       <div class="control-group">
           <label class="control-label" for="ente">Natura giuridica</label>
		   <div class="controls">
<%-- 		        <s:if test="null!=listaStatoOperativoSoggetto">  --%>
<%-- 		   			<s:select list="listaStatoOperativoSoggetto" id="listaFormaGiuridicaTipo" name="idTipoNatura" cssClass="span2" listKey="id" listValue="descrizione" disabled="true"/> --%>
<%-- 		   		</s:if> --%>
		   		<s:if test="null!=listaNaturaGiuridica">
					<span class="al">
<!-- 						<label class="radio inline" for="den">Natura giuridica</label> -->
	                	<s:select list="listaNaturaGiuridica" id="listaNaturaGiuridica" name="idNaturaGiuridica" cssClass="span9" listKey="id" listValue="descrizione" headerKey="-1" headerValue="Scegli la natura giuridica" title="Scegli la natura giuridica"/>
					</span>
				</s:if>
		  </div>
       </div>
	   <div class="control-group">
          <label class="control-label" for="ente">Stato soggetto</label>
		  <div class="controls">
		  		<s:if test="null!=listaStatoOperativoSoggetto">
          			<s:select list="listaStatoOperativoSoggetto" id="listaStatoOperativoSoggetto" name="idStato" cssClass="span4" listKey="id" headerKey="-1" headerValue="Seleziona lo stato del soggetto" title="Seleziona lo stato del soggetto" listValue="descrizione"/>
          		</s:if>
                <span class="al">
					<label class="radio inline">  Sesso </label>
                    <div class="radio inline">
                    	<s:radio id="sesso" cssClass="flagSesso" name="sesso" list="listaSesso"/>
                    </div>
               </span>
		  </div>
       </div>
       <div class="control-group">
          <label class="control-label" for="Nazione">Nazione</label>
          <div class="controls">
                    <s:if test="isNazioniPresenti()">
          	  			<s:select list="nazioni" id="idNazione" name="idNazione" cssClass="span4" listKey="codice" listValue="descrizione" headerKey="-1" headerValue="Seleziona la nazione" title="Seleziona la nazione" />
          	   		</s:if> 
          	   		
               <span class="al">
               		<label class="radio inline" for="comune">Comune di Nascita</label>
     			    <s:textfield id="comune" name="comune" cssClass="span4"/>
     			    <s:hidden id="comuneId" name="idComune"></s:hidden>    
               </span>
          </div>
       </div>
       

	</fieldset>
	
	<div class="Border_line"></div>
	<p>
<%-- 	   <s:include value="/jsp/include/indietro.jsp" /> --%>
	   
	   <s:a cssClass="btn btn-secondary" href="${redirect.cruscotto}">indietro</s:a>
	  
	   <!-- task-131 <s:submit name="annulla" value="annulla" method="annulla" cssClass="btn btn-link" /> -->
	   <s:submit name="annulla" value="annulla" action="ricercaSoggettoCec_annulla" cssClass="btn btn-link" />  
	   <!-- task-131 <s:submit name="cerca" id="cerca" value="cerca" method="cerca" cssClass="btn btn-primary pull-right" />  -->  
	   <s:submit name="cerca" id="cerca" value="cerca" action="ricercaSoggettoCec_cerca" cssClass="btn btn-primary pull-right" />
	</p>
</s:form>
</div>	
</div>	
</div>	 
</div>	
<script type="text/javascript">
$(document).ready(function() {
    // sul click enter scatta la ricerca	
	$(document).keypress(function( e) {
	    if(e.which == 13) {
	        $("#cerca").click();
	    }
	});
    
});

</script>
<s:include value="/jsp/include/footer.jsp" />
