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
    	<%-- SIAC-7952 rimuovo .do dalla action --%>
        <s:form id="nuovaLiquidazioneOrdinativo" action="nuovaLiquidazioneOrdinativo" method="post">
		<!--#include virtual="include/alertErrorSuccess.html" --> 
		<s:include value="/jsp/include/actionMessagesErrors.jsp" />
		<h3>Nuova liquidazione</h3> 
		<div class="step-content"> 
			<div class="step-pane active" id="insLiq">
				<!--#include virtual="include/impegno.html" --> 
				<s:include value="/jsp/ordinativo/include/headerDettaglioOrdinativo.jsp" />
				
				<s:set var="pulisciRicercaImpegnoAction" value="%{'nuovaLiquidazioneOrdinativo_pulisciRicercaImpegno'}" />	  						        	        							
				<s:include value="/jsp/ordinativo/include/impegnoNuovaLiquidazione.jsp" />
				 
				 
				<!--#include virtual="include/liquidazione.html" --> 
				<s:include value="/jsp/ordinativo/include/liquidazioneOrdinativo.jsp" />
				
				<s:set var="confermaCompGuidataAction" value="%{'nuovaLiquidazioneOrdinativo_confermaCompGuidata'}" />	  						        	        							
				<s:set var="ricercaGuidataImpegnoAction" value="%{'nuovaLiquidazioneOrdinativo_ricercaGuidataImpegno'}" />	          		
				<s:include value="/jsp/include/modalImpegno.jsp" />
				
				<!-- per modalOrdinativo.jsp -->
				<s:set var="eliminaQuotaOrdinativoAction" value="%{'nuovaLiquidazioneOrdinativo_eliminaQuotaOrdinativo'}" />
				<s:set var="eliminaProvvisorioAction" value="%{'nuovaLiquidazioneOrdinativo_eliminaProvvisorio'}" />
				<s:set var="forzaInserisciQuotaAccertamentoAction" value="%{'nuovaLiquidazioneOrdinativo_forzaInserisciQuotaAccertamento'}" />
				<s:set var="forzaAggiornaQuotaAccertamentoAction" value="%{'nuovaLiquidazioneOrdinativo_forzaAggiornaQuotaAccertamento'}" />
				
				<!-- per transazioneElementare.jsp -->
				<s:set var="codiceSiopeChangedAction" value="%{'nuovaLiquidazioneOrdinativo_codiceSiopeChanged'}" />
		    	<s:set var="confermaPdcAction" value="%{'nuovaLiquidazioneOrdinativo_confermaPdc'}" />	
				
			  	<s:hidden id="idPianoDeiContiCapitolo" name="teSupport.pianoDeiConti.uid"/>
				<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
				
				
				<s:if test="!presenzaQuote()">
					<s:include value="/jsp/include/transazioneElementare.jsp" />
				</s:if>
				
				<s:include value="/jsp/ordinativo/include/modalOrdinativo.jsp" />
				
				
			</div>
		</div>
		
		
		<p class="margin-medium">
			
<%-- 		<s:include value="/jsp/include/indietro.jsp" /> --%>
			<!-- task-131 <s:submit cssClass="btn" method="indietroDaLiquidazione" value="indietro" name="indietro" /> -->
			<s:submit cssClass="btn" action="nuovaLiquidazioneOrdinativo_indietroDaLiquidazione" value="indietro" name="indietro" />
			<!-- task-131 <s:submit cssClass="btn btn-secondary" method="annullaInserimentoNuovaLiquidazione" value="annulla" name="annulla" /> -->
			<s:submit cssClass="btn btn-secondary" action="nuovaLiquidazioneOrdinativo_annullaInserimentoNuovaLiquidazione" value="annulla" name="annulla" />
<!-- 		<a class="btn btn-secondary" href="">annulla</a>     -->

           <s:if test="attivaTastoPreCompila()">
           		<!-- task-131 <s:submit cssClass="btn btn-primary pull-right" method="salva" value="precompila" name="precompila" /> -->
           		<s:submit cssClass="btn btn-primary pull-right" action="nuovaLiquidazioneOrdinativo_salva" value="precompila" name="precompila" />
           </s:if>
           <s:else>
           	    <!-- task-131 <s:submit cssClass="btn btn-primary pull-right" method="salva" value="salva" name="salva" /> -->
           	    <s:submit cssClass="btn btn-primary pull-right" action="nuovaLiquidazioneOrdinativo_salva" value="salva" name="salva" />         
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