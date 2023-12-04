<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

	  <%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />
    
    
    <%-- Inclusione JavaScript NUOVO --%>
   <s:include value="/jsp/include/javascript.jsp" />	
	
  </head>

  <body>
  
  
  <s:include value="/jsp/include/header.jsp" />
  
  
    <div class="container-fluid">
<div class="row-fluid">


    <div class="span12 contentPage">
    	<%-- SIAC-7952 rimuovo .do dalla action --%>
        <s:form id="salvaSoggetto" action="salvaSoggetto" method="post">
        
        <h3>Inserisci un nuovo soggetto</h3>
        <!-- 
        <p> Segui i passi indicati per inserire i dati dell'ente. 
        <br/>Non &egrave; necessario compilare tutti i campi, ma solo quelli obbligatori: xxx, xxxx e xxxxx.</p>
        -->
        <div id="MyWizard" class="wizard">
					<ul class="steps">
						<li data-target="#step1" class="complete"><span class="badge badge-success">1</span>Soggetto<span class="chevron"></span></li>
						<li data-target="#step2" class="complete"><span class="badge badge-success">2</span>Recapiti<span class="chevron"></span></li>
						<li data-target="#step3" class="active"><span class="badge">3</span>Salva<span class="chevron"></span></li>
                        	
<!--						<li data-target="#step4"><span class="badge">4</span>Salva<span class="chevron"></span></li> -->
					</ul>
				</div>
				<div class="step-content">
					<div class="step-pane active" id="step3">

       <h4>Salva</h4>
                <s:if test="hasActionErrors()">
						<%-- Messaggio di ERROR --%>
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
				<!-- <div class="alert alert-warning"> -->
				   <div class="alert alert-success">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<strong>Attenzione!!</strong><br>
					<ul>
					    <s:actionmessage/>
					</ul>
				</div>
			</s:if>					 										 
                                          
             <p>
                 
                 <!-- task-131 <s:submit name="indietroStep2" value="indietro" method="indietroStep2" cssClass="btn" disabled="true" /> -->
                 <s:submit name="indietroStep2" value="indietro" action="salvaSoggetto_indietroStep2" cssClass="btn" disabled="true" />   
                <!-- <a class="btn btn-link" href="">annulla</a>
                -->
                <s:a cssClass="btn btn-primary pull-right" href="${redirect.cruscotto}">chiudi</s:a>
               <!--  <a href="#" class="btn btn-primary pull-right">chiudi</a> --> 
             </p>
             
             </div>
             
             </div>
				</div>
        </s:form>
    </div>
</div>	 
</div>	

<s:include value="/jsp/include/javascript.jsp" />


<s:include value="/jsp/include/footer.jsp" />