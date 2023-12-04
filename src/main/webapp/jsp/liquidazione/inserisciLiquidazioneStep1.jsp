<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

<s:include value="/jsp/include/head.jsp" />
<s:include value="/jsp/include/javascript.jsp" />
</head>

<body>
	<s:include value="/jsp/include/header.jsp" />
	  <p class="nascosto"><a title="A-sommario"></a></p>     
	  <ul id="sommario" class="nascosto">
	    <li><a href="#A-contenuti">Salta ai contenuti</a></li>
	  </ul>
	  <hr />
	<div class="container-fluid-banner">
	  <a title="A-contenuti"></a>
	</div>
	
	<div class="container-fluid">
	  <div class="row-fluid">
	    <div class="span12 contentPage">
	      <s:form cssClass="form-horizontal" id="inserisciLiquidazioneStep1" action="inserisciLiquidazioneStep1" method="post">
			<h3>Nuova liquidazione</h3> 
			
			<s:include value="/jsp/include/actionMessagesErrors.jsp" />
			
			<div id="MyWizard" class="wizard">
				<ul class="steps">
					<li data-target="#step1" class="active"><span class="badge">1</span>ricerca impegno<span class="chevron"></span></li>
					<li data-target="#step2" ><span class="badge">2</span>dati liquidazione<span class="chevron"></span></li>
				</ul>
			</div>
	        <div class="step-content">	
	          <div class="step-pane active" id="step1"> 
				<s:include value="/jsp/liquidazione/include/impegno.jsp" />		
				
				<s:set var="confermaCompGuidataAction" value="%{'inserisciLiquidazioneStep1_confermaCompGuidata'}" />
				<s:set var="ricercaGuidataImpegnoAction" value="%{'inserisciLiquidazioneStep1_ricercaGuidataImpegno'}" />
				<s:set var="ricercaImpegnoCompilazioneGuidataAction" value="%{'inserisciLiquidazioneStep1_ricercaGuidataImpegno'}" />	          
				<s:set var="selezionaImpegnoCompilazioneGuidataAction" value="%{'inserisciLiquidazioneStep1_confermaCompGuidata'}" />	          
			
				<s:include value="/jsp/liquidazione/include/modalImpegno.jsp" />      
	          </div>
	        </div>          
	        <br> 
	         
	        <p>
				<s:include value="/jsp/include/indietro.jsp" /> 
		        <!-- task-131 <s:submit name="annulla" value="annulla" method="annulla" cssClass="btn btn-secondary"/> -->
		        <s:submit name="annulla" value="annulla" action="inserisciLiquidazioneStep1_annulla" cssClass="btn btn-secondary"/>
		        <!-- task-131 <s:submit name="prosegui" value="prosegui" method="prosegui" cssClass="btn btn-primary pull-right" /> -->
		        <s:submit name="prosegui" value="prosegui" action="inserisciLiquidazioneStep1_prosegui" cssClass="btn btn-primary pull-right" /> 
			</p>   
			        
	      </s:form>
	     </div>
	  </div>	 
	</div>
	<s:include value="/jsp/include/footer.jsp" />

