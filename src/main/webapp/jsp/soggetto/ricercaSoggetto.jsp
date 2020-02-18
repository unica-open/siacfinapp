<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

    <%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />
    
    
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
	<h3>Ricerca Soggetto</h3>
	<p>&Egrave; necessario inserire almeno un criterio di ricerca.</p>
	<div class="step-content">
	<s:form id="ricercaSoggetto" action="ricercaSoggetto.do" method="post" focusElement="cerca" >
		<p></p>
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
			<%-- Messaggio di INFO --%>
			<div class="alert alert-success">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
					<s:actionmessage />
				</ul>
			</div>
		</s:if>

		<fieldset class="form-horizontal">
		
	    	<div class="control-group">
	        	<label class="control-label" for="codice">Codice</label>
	            <div class="controls">
	            	<s:textfield id="codice" name="codice" onkeyup="return checkItNumbersOnly(event)" cssClass="span2"/>
					<span class="al">
						<label class="radio inline" for="codiceFiscale">Codice fiscale</label>
						<s:textfield id="codiceFiscale" name="codiceFiscale" cssClass="span2 capitalize" maxlength="16" 
							pattern="^([A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]|[0-9]{11})$"/>
					</span>
					<span class="al">
						<label class="radio inline" for="partitaIva">Partita IVA</label>
						<s:textfield id="partitaIva" name="partitaIva" cssClass="span2" maxlength="11"/>
					</span>
					
			   </div>
	       </div>
	       
	       <div class="control-group"> 
		   		<label class="control-label" for="codiceFiscaleEstero">Codice fiscale estero</label>
  				<div class="controls">
	          		<s:textfield id="codiceFiscaleEstero" name="codiceFiscaleEstero" cssClass="span2" maxlength="11"/>
	          		<span class="al">
						<label class="radio inline" for="codDestinatario">Codice destinatario / IPA</label>
						<s:textfield id="codDestinatario" name="codDescrizione" cssClass="span2" maxlength="7"/>
					</span>
					<span class="al">
						<label class="radio inline" for="emailPec">email PEC</label>
						<s:textfield id="emailPec" name="emailPec" cssClass="span2" maxlength="256"/>
					</span>
	    		</div>
	       </div>
	       
		   <div class="control-group">
	       	   <label class="control-label" for="denominazione">Denominazione</label>
	           <div class="controls">
	           		<s:textfield id="denominazione" name="denominazione" cssClass="span9"/>
	           </div>
	       </div>
	       
	       <s:hidden name="idNaturaGiuridica" value="-1" /> 
	       <s:hidden name="idStato" value="-1" /> 
	       <s:hidden name="idNazione" value="-1" /> 
	       
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
		<p></p><p>
		   
		   <s:a cssClass="btn btn-secondary" href="${redirect.cruscotto}">indietro</s:a>
		  
		   <s:submit name="annulla" value="annulla" method="annulla" cssClass="btn btn-link" />  
		   <s:submit name="cerca" id="cerca" value="cerca" method="cerca" cssClass="btn btn-primary pull-right" />  
		   
		</p><p></p>
	
	</s:form>

		
	
	

</div>	
</div>	
</div>	 
</div>	


 <s:include value="/jsp/include/javascript.jsp" />

 <script src="${jspath}soggetto/ricercaSoggetto.js" type="text/javascript"></script>

<s:include value="/jsp/include/footer.jsp" />
