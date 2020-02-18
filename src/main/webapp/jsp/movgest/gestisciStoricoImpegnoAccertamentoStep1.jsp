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
	      <s:form cssClass="form-horizontal" id="gestioneStoricoImpegnoAccertamentoStep1" action="gestisciStoricoImpegnoAccertamentoStep1" method="post">
			<h3>Storico impegno accertamento</h3> 
			
			<s:include value="/jsp/include/actionMessagesErrors.jsp" />
			
			<div id="MyWizard" class="wizard">
				<ul class="steps">
					<li data-target="#step1" class="active"><span class="badge">1</span>ricerca impegno<span class="chevron"></span></li>
					<li data-target="#step2" ><span class="badge">2</span>associa accertamento<span class="chevron"></span></li>
				</ul>
			</div>
	        <div class="step-content">	
	          <div class="step-pane active" id="step1"> 
				<h4>Impegno:</h4>
				<fieldset class="form-horizontal">
					<div class="control-group">
						<label class="control-label" for="anno1">Anno *</label>
						<div class="controls">					  
						  <s:textfield id="annoImpegno" maxlength="4" cssClass="lbTextSmall span1 required" name="annoImpegno" onkeyup="return checkItNumbersOnly(event)"></s:textfield>			   
						  <span class="al">
							<label class="radio inline" for="numero">Numero *</label>
						  </span>
						  <s:textfield id="numeroImpegno" cssClass="lbTextSmall span2 required" name="numeroImpegno" onkeyup="return checkItNumbersOnly(event)"></s:textfield>   
						  <span class="al">
							<label class="radio inline" for="sub">Sub </label>
						  </span>
						  <s:textfield id="numeroSub" cssClass="lbTextSmall span2 required" name="numeroSub" onkeyup="return checkItNumbersOnly(event)"></s:textfield>
						  <s:if test="ricercaImpegnoStep1Abilitata">
						  	<span class="radio guidata"><a id="compilazioneGuidataImpegnoStep1" href="#guidaImpegno" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
						  </s:if>
						  <s:else>
						  	<span class="radio guidata"><a id="compilazioneGuidataImpegnoStep1" href="#" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
						  </s:else>
						</div>
					</div>
				</fieldset>
							
				<s:include value="/jsp/include/modalImpegno.jsp" />      
	          </div>
	        </div>          
	        <br> 
	         
	        <p>
				<s:include value="/jsp/include/indietro.jsp" /> 
		        <s:submit name="annulla" value="annulla" method="annulla" cssClass="btn btn-secondary"/>
				<s:submit name="prosegui" value="associa accertamento" method="prosegui" cssClass="btn btn-primary pull-right freezePagina" /> 
			</p>   
			        
	      </s:form>
	     </div>
	  </div>	 
	</div>
	<script type="text/javascript" src="${jspath}movgest/gestisciStoricoImpegnoAccertamentoStep1.js"></script>
	<s:include value="/jsp/include/footer.jsp" />

