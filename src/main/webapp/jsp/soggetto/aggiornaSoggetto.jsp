<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
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



  
  
  
<div class="container-fluid-banner">

    


<a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->


    <div class="container-fluid">
<div class="row-fluid">


    <div class="span12 contentPage">
     <%-- SIAC-7952 rimuovo .do dalla action --%>
     <s:form id="aggiornaSoggetto" action="aggiornaSoggetto" method="post">
     
     <s:hidden name="soggetto" />
     
     <s:include value="/jsp/include/actionMessagesErrors.jsp" /> 
 
             
 
         <ul class="nav nav-tabs">
    <li class="active"><a href="#">Soggetto</a></li>
<%--     <li><s:a action="aggiornaSediSecondarie">Sedi Secondarie</s:a></li> --%>
    <li><a href="#" id="vaiAggiornaSediSecondarie" >Sedi Secondarie</a></li>
    
    <li><a href="#" id="vaiModPagam">Modalit&agrave; pagamento</a></li>
<%--     <li><s:a action="modalitaPagamentoSoggetto">Modalit&agrave; pagamento</s:a></li> --%>
    </ul>
     
              <div id="MyWizard" class="wizard">
					<ul class="steps">
						<li data-target="#step1" class="active"><span class="badge  badge-success">1</span>Anagrafica<span class="chevron"></span></li>
						<li data-target="#step2" class="complete"><span class="badge  badge-success">2</span>Recapiti<span class="chevron"></span></li>
						<!--<li data-target="#step3"><span class="badge">3</span>Salva<span class="chevron"></span></li> -->
						<!--<li data-target="#step4"><span class="badge">4</span>Salva<span class="chevron"></span></li> -->
					</ul>
				</div>
				<div class="step-content">
					<div class="step-pane active" id="step1">

		<h3>Codice Soggetto: <s:property value="dettaglioSoggetto.codiceSoggetto" /> 
		-  <s:property value="dettaglioSoggetto.denominazione" /> 
		 (<s:property value="dettaglioSoggetto.statoOperativo" /> dal <s:property value="%{dettaglioSoggetto.dataValidita}" />) </h3>     
		
		 
        <s:include value="/jsp/soggetto/include/soggAnagrafica.jsp"/>
        
        <s:include value="/jsp/soggetto/include/soggAnagrafica2.jsp" />
        <s:include value="/jsp/soggetto/include/soggAnagrafica3.jsp" />
           	                                 
             <p>
              <s:include value="/jsp/include/indietro.jsp" />
           
                 <!-- task-131 <s:submit name="annulla" id="annulla" value="annulla" method="annullaAggiornaSoggetto" cssClass="btn" /> -->
                 <!-- task-131 <s:submit name="prosegui" id="prosegui" value="prosegui" method="prosegui" cssClass="btn btn-primary pull-right" /> -->
                 <s:submit name="annulla" id="annulla" value="annulla" action="aggiornaSoggetto_annullaAggiornaSoggetto" cssClass="btn" />
                 <s:submit name="prosegui" id="prosegui" value="prosegui" action="aggiornaSoggetto_prosegui" cssClass="btn btn-primary pull-right" />                  
              </p>
             </div>
				</div>
      </s:form>
    </div>
</div>	 
</div>	

<script src="${jspath}soggetto/step1Soggetto.js" type="text/javascript"></script>
 
<s:include value="/jsp/include/footer.jsp" />


<script type="text/javascript">
var elencoStruttureAmministrativoContabiliJson = 
	<s:property escapeHtml="false" value="elencoStruttureAmministrativoContabiliJson" />;
</script>	
	


<script type="text/javascript">
// $(document).ready(function() {
// 	$("#prosegui").click(function() {
// 		var f = $("form");
// 		f.attr("action", "<s:url method="prosegui" />");
// 		f.submit();
// 	});
// });

	
	$(document).ready(function() {

		$("#vaiAggiornaSediSecondarie").click(function() {
			// task-131 $("#aggiornaSoggetto").attr('action','<s:url method="redirectSedi"/>').submit();
			$("#aggiornaSoggetto").attr('action','<s:url action="aggiornaSoggetto_redirectSedi"/>').submit();
		});
		
		$("#vaiModPagam").click(function() {
			
			// task-131 $("#aggiornaSoggetto").attr('action','<s:url method="redirectMdp"/>').submit();
			$("#aggiornaSoggetto").attr('action','<s:url action="aggiornaSoggetto_redirectMdp"/>').submit();
		});
		
		

			
		
		$("#idNazione").change(function() {
			$("#comune").val($("option:selected", this).text()).trigger({type: 'keydown', keyCode: 1, which: 1, charCode: 1});
		});
		
		
	});
         
</script>
