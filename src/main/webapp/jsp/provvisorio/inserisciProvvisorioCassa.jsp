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


<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 contentPage">
    		<%-- SIAC-7952 rimuovo .do dalla action --%>
			<s:form method="post" action="inserisciProvvisorioCassa" id="inserisciProvvisorioCassa">
				<s:include value="/jsp/include/actionMessagesErrors.jsp" />
			
			
				<h3>Inserisci provvisorio di cassa</h3>
        		<div class="step-content">
		   
          		<div class="step-pane active" id="step1">
          		
					<h4>Dati principali</h4>
				
				
         		  
					<fieldset class="form-horizontal margin-large">
					  
						<div class="control-group">
							<span class="control-label">Tipologia:</span>
							<div class="controls">														 
							 	<div class="radio inline">
                       				<s:radio id="documento" cssClass="flagDocumento" name="tipoDocumentoProv" list="tipoDocumentoProvList"/>
								</div>		       								                     			
							</div>
						</div>
						
		      
						<div class="control-group">
							<label class="control-label" for="Nquiet">Numero*</label>
							<div class="controls">
								<s:textfield id="numeroProvvisorio" onkeyup="return checkItNumbersOnly(event)" name="model.numeroProvvisorio"  maxlength="8"  cssClass="lbTextSmall span2"/>
							</div>
						</div>												
						
						<div class="control-group">
							<label class="control-label" for="DescCaus">Descrizione causale</label>
							<div class="controls">
								<s:textfield rows="2" cols="15" id="DescCaus" name="model.descCausale" cssClass="span9" ></s:textfield>
							</div>
						</div>
						
		
						<div class="control-group">
							<label class="control-label" for="DescSogg">Denominazione soggetto</label>
							<div class="controls">
								<s:textfield rows="2" cols="15" id="DescSogg" name="model.denominazioneSoggetto" cssClass="span9" ></s:textfield>
							</div>
						</div>	
						
						
						<div class="control-group">
							<label class="control-label" for="DescSogg">Data emissione</label>
							<div class="controls">
								<s:textfield id="dataEmissione" title="gg/mm/aaaa" name="model.dataEmissione" cssClass="lbTextSmall span2 datepicker"></s:textfield>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="DescSogg">Importo*</label>
							<div class="controls">
								<s:textfield id="importo" name="model.importoFormattato" cssClass="lbTextSmall span2 soloNumeri decimale"></s:textfield>
							</div>
						</div>	
						
											
						</fieldset>         		            
  
					</div>
				</div>
				<br/> <br/> 
				<p>           
	            <s:include value="/jsp/include/indietro.jsp" /> 
				<!-- task-131 <s:submit name="annulla" value="annulla" method="annullaInserisciProvvisorio" cssClass="btn btn-secondary" /> -->
				<!-- task-131 <s:submit name="cerca" value="Salva" method="insericiProvvisorioDiCassa" cssClass="btn btn-primary pull-right" /> -->
				<s:submit name="annulla" value="annulla" action="inserisciProvvisorioCassa_annullaInserisciProvvisorio" cssClass="btn btn-secondary" />
				<s:submit name="cerca" value="Salva" action="inserisciProvvisorioCassa_insericiProvvisorioDiCassa" cssClass="btn btn-primary pull-right" />
				</p>       				
  	    	</s:form>
    	</div>
	</div>	 
</div>	

<s:include value="/jsp/include/footer.jsp" />

