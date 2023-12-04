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

<%-- SIAC-7952 rimuovo .do dalla action --%>
<s:form id="gestioneClassiSoggetto" action="gestioneClassiSoggetto" method="post" focusElement="cerca" >
	<!--  per soggetto -->
	<s:set var="ricercaSoggettoAction" value="%{'gestioneClassiSoggetto_ricercaSoggetto'}"/> 
	<s:set var="selezionaSoggettoAction" value ="%{'gestioneClassiSoggetto_selezionaSoggetto'}"/>
	<s:set var="pulisciRicercaSoggettoAction" value="%{'gestioneClassiSoggetto_pulisciRicercaSoggetto'}" />	          
	<s:set var="listaClasseSoggettoChangedAction" value="%{'gestioneClassiSoggetto_listaClasseSoggettoChanged'}" />	
										

  <!--modale soggetto -->
	<s:include value="/jsp/include/modalSoggetto.jsp"/>	
  <!--/modale soggetto -->

<div class="contentPage">  
	
	

	<h3>Gestione Classi Soggetto</h3>
	<div class="step-content">
	
	
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

		<fieldset class="form-horizontal">
		
	       <s:hidden name="idNaturaGiuridica" value="-1" /> 
	       <s:hidden name="idStato" value="-1" /> 
	       <s:hidden name="idNazione" value="-1" /> 
	       
	       <br/>
	       
	       <div class="control-group">
	          <label class="control-label" for="classif">Classificatore</label>
			  <div class="controls">
			   <s:if test="null!=listaClasseSoggetto">
	          	   <s:select list="listaClasseSoggetto" id="listaClasseSoggetto" name="idClasse" cssClass="span9" title="Scegli il tipo di classificazione" 
	          	             listKey="id" listValue="codice+' - '+descrizione" headerKey="-1" headerValue="Scegli il tipo di classificazione"/>
	           </s:if>	             
	 		  </div>
	       </div>
	       
	       
		</fieldset>
	
		
	

	</div>	
	
		<br/>
		<p>
		   <s:a cssClass="btn btn-secondary" href="${redirect.cruscotto}">indietro</s:a>
		   <!-- task-131 <s:submit name="gestisciSelezionata" id="gestisciSelezionata" value="gestisci selezionata" method="gestisciSelezionata" cssClass="btn btn-primary pull-right" /> -->
		   <s:submit name="gestisciSelezionata" id="gestisciSelezionata" value="gestisci selezionata" action="gestioneClassiSoggetto_gestisciSelezionata" cssClass="btn btn-primary pull-right" />  
		</p>
	 
	 
	 

</div>	

<s:if test="ricercaSoggettiEffettuata()"> 
	<s:include value="/jsp/soggetto/include/elencoSoggettiGestioneClasse.jsp" />
</s:if>



<!--  AGGIUNGERE UN SOGGETTO: -->
<s:if test="ricercaSoggettiEffettuata()"> 
	<div class="container-fluid">
		<div class="row-fluid">
					<div class="span12 ">
	
							<div class="contentPage">  
							   
							   <h3>Aggiungere un soggetto alla classe</h3>
							   <p>Da qui puoi aggiungere soggetti dalla classe:</p>  
													
								<div id="refreshHeaderSoggetto">
					            	<s:include value="/jsp/soggetto/include/headerSoggettoPerGestioneClassi.jsp"/>
					            </div>
					            <s:include value="/jsp/soggetto/include/soggettoPerGestioneClassi.jsp" />  
							    
							 <div class="Border_line"></div> 
							 
							 <p>
						  		 <!-- task-131 <s:submit name="aggiungiSoggetto" id="aggiungiSoggetto" value="aggiungi soggetto" method="aggiungiSoggettoAllaClasse" cssClass="btn btn-primary pull-left" /> -->  
								 <s:submit name="aggiungiSoggetto" id="aggiungiSoggetto" value="aggiungi soggetto" action="gestioneClassiSoggetto_aggiungiSoggettoAllaClasse" cssClass="btn btn-primary pull-left" />
							 </p>
							 
							 <br/>
							 
							  	                                 
							</div>	
							
							 
							
					</div>	
		</div>	 
	</div>
</s:if>

<!--  END AGGIUNGERE UN SOGGETTO -->



</s:form>

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
