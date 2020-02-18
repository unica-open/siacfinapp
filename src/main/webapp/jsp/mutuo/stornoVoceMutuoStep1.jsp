<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
  <%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

<%-- Inclusione head e CSS NUOVO --%>
<s:include value="/jsp/include/head.jsp" />

<%-- Inclusione JavaScript NUOVO --%>
<s:include value="/jsp/include/javascript.jsp" />
</head>
<body>                  
  <s:include value="/jsp/include/header.jsp" />
	<hr />
  <div class="container-fluid-banner">



  
  <div class="container-fluid">
    <div class="row-fluid">
      <div class="span12 contentPage">
			  
        <s:form id="stornoVoceMutuoStep1" action="stornoVoceMutuoStep1.do" method="post">
         
		    <s:include value="/jsp/include/actionMessagesErrors.jsp" />
			<h3>Inserimento storno voce mutuo</h3>
			 
			 <h4>
				Numero Mutuo: <s:property value="mutuoSelezionato.codiceMutuo" />  - Codice Istituto <s:property value="mutuoSelezionato.soggettoMutuo.codiceSoggetto" /> - <s:property value="mutuoSelezionato.soggettoMutuo.denominazione" />
				
				 <span class="alLeft">Disponibile mutuo:
					<s:property value="getText('struts.money.format', {mutuoSelezionato.disponibileMutuo})" />
				  </span>
			</h4>
			<div id="MyWizard" class="wizard">
					<ul class="steps">
						<li data-target="#step1" class="active"><span class="badge">1</span>Inserisci importo storno<span class="chevron"></span></li>
						<li data-target="#step2"><span class="badge">2</span>Seleziona Provvedimento<span class="chevron"></span></li>
						<li data-target="#step3"><span class="badge">3</span>Inserimento voce<span class="chevron"></span></li>
						
					</ul>
			</div>	
			<div class="step-content">
				<div class="step-pane active" id="step1">
					
					<h4>Storno - Importo attuale <s:property value="getText('struts.money.format', {mutuoSelezionato.importoAttualeMutuo})"/></h4>                           
					<div class="control-group">
						<label class="control-label" for="importo">Riduzione per storno *</label>
						<div class="controls">  
						<s:textfield id="importoStorno" name="importoStornoStringa" cssClass="lbTextSmall span3 soloNumeri decimale" /> 
						 
						</div>
					</div>
				
      			
				</div>
			</div>
		  	<!--#include virtual="include/modal.html" -->   
			<p class="margin-medium">
				 <s:include value="/jsp/include/indietro.jsp" /> 
				<span class="pull-right">
				<s:submit name="inserisciStornoVoceMutuo" value="prosegui" method="prosegui" cssClass="btn btn-primary" />
				
				</span> 
			</p>       
            
        </s:form>
      </div>
    </div>	 
  </div>	
<s:include value="/jsp/include/footer.jsp" />