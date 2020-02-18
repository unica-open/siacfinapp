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
  	<s:include value="/jsp/include/javascriptTree.jsp" />
  </head>

  <body>
  
   <s:include value="/jsp/include/header.jsp" />
  <!-- NAVIGAZIONE -->
  
  <hr />
<div class="container-fluid-banner">

<a name="A-contenuti" title="A-contenuti"></a>
</div>


<div class="container-fluid">
<div class="row-fluid">

    <div class="span12 contentPage">
    
        <s:form id="nuovaLiquidazioneOrdinativo" action="nuovaLiquidazioneOrdinativo.do" method="post">
		<!--#include virtual="include/alertErrorSuccess.html" --> 
		<s:include value="/jsp/include/actionMessagesErrors.jsp" />
		<h3>Nuova liquidazione</h3> 
		<div class="step-content"> 
			<div class="step-pane active" id="insLiq">
				<!--#include virtual="include/impegno.html" --> 
				<s:include value="/jsp/ordinativo/include/headerDettaglioOrdinativo.jsp" />
				
				<s:include value="/jsp/ordinativo/include/impegnoNuovaLiquidazione.jsp" />
				 
				 
				<!--#include virtual="include/liquidazione.html" --> 
				<s:include value="/jsp/ordinativo/include/liquidazioneOrdinativo.jsp" />
				
				
				<s:include value="/jsp/include/modalImpegno.jsp" />
			  	<s:hidden id="idPianoDeiContiCapitolo" name="teSupport.pianoDeiConti.uid"/>
				<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
				
				<s:if test="!presenzaQuote()">
					<s:include value="/jsp/include/transazioneElementare.jsp" />
				</s:if>
				
				<s:include value="/jsp/ordinativo/include/modalOrdinativo.jsp" />
				
				
			</div>
		</div>
		
		
		<p class="margin-medium">
			
<%-- 			<s:include value="/jsp/include/indietro.jsp" /> --%>
			<s:submit cssClass="btn" method="indietroDaLiquidazione" value="indietro" name="indietro" />
			<s:submit cssClass="btn btn-secondary" method="annullaInserimentoNuovaLiquidazione" value="annulla" name="annulla" />
<!-- 			<a class="btn btn-secondary" href="">annulla</a>     -->

           <s:if test="attivaTastoPreCompila()">
           		<s:submit cssClass="btn btn-primary pull-right" method="salva" value="precompila" name="precompila" />
           </s:if>
           <s:else>
           	    <s:submit cssClass="btn btn-primary pull-right" method="salva" value="salva" name="salva" />
           </s:else>

			
			
		</p>     
		<!--#include virtual="include/modal.html" --> 
      </s:form>
    </div>
</div>	 
</div>	
<script type="text/javascript">

	$(document).ready(function() {
		
	});
	
</script> 
	
<s:include value="/jsp/include/footer.jsp" />