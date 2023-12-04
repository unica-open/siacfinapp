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
	<hr />
	<div class="container-fluid-banner">
		<a name="A-contenuti" title="A-contenuti"></a>
	</div>
    <div class="container-fluid">
		  <div class="row-fluid">
			  <div class="span12 contentPage">
            <h4>Gestione ROR Impegno <s:property value="anno"/>/<s:property value="numero"/></h4>
            <s:include value="/jsp/include/actionMessagesErrors.jsp" />
            
                  
              <fieldset class="step-content">
                <div class="step-pane active" id="step1">
                  
                  <s:if test="isCruscottoDisabled()">
                    <h4>Non puoi utilizzare il cruscotto</h4>
                    <s:include value="/jsp/include/indietro.jsp" />
                  </s:if> 
                  <s:else>
                    <%-- <s:form class="form-horizontal" id="selezionaProvvedimento" action="gestisciImpegnoRor.do" method="post" > --%>
                      	<s:set var="selezionaProvvedimentoAction" value="%{'aggiornaModificaMovimentoRor_selezionaProvvedimento'}" />	        
	                    <s:set var="clearRicercaProvvedimentoAction" value="%{'aggiornaModificaMovimentoRor_clearRicercaProvvedimento'}" />	          
	            	  	<s:set var="ricercaProvvedimentoAction" value="%{'aggiornaModificaMovimentoRor_ricercaProvvedimento'}" />	          
	            	  	<s:set var="eliminaAction" value="%{'aggiornaModificaMovimentoRor_elimina'}" />	  
	            		<s:set var="selezionaProvvedimentoInseritoAction" value="%{'aggiornaModificaMovimentoRor_selezionaProvvedimentoInserito'}" />	
						<s:set var="inserisciProvvedimentoAction" value="%{'aggiornaModificaMovimentoRor_inserisciProvvedimento'}" />	
			
						<s:set var="gestisciForwardAction" value="%{'aggiornaModificaMovimentoRor_gestisciForward'}" />
					    <s:set var="siSalvaAction" value="%{'aggiornaModificaMovimentoRor_siSalva'}" />	 
					    <s:set var="siProseguiAction" value="%{'aggiornaModificaMovimentoRor_siProsegui'}" />	
						<s:set var="annullaSubImpegnoAction" value="%{'aggiornaModificaMovimentoRor_annullaSubImpegno'}" />	 
						<s:set var="annullaSubAccertamentoAction" value="%{'aggiornaModificaMovimentoRor_annullaSubAccertamento'}" />	 
						<s:set var="annullaMovGestSpesaAction" value="%{'aggiornaModificaMovimentoRor_annullaMovGestSpesa'}" />	 
						<s:set var="eliminaSubImpegnoAction" value="%{'aggiornaModificaMovimentoRor_eliminaSubImpegno'}" />	 
						<s:set var="eliminaSubAccertamentoAction" value="%{'aggiornaModificaMovimentoRor_eliminaSubAccertamento'}" />
						<s:set var="forzaProseguiAction" value="%{'aggiornaModificaMovimentoRor_forzaProsegui'}" />	          
						<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'aggiornaModificaMovimentoRor_forzaSalvaPluriennaleAccertamento'}" />	          
						<s:set var="salvaConByPassDodicesimiAction" value="%{'aggiornaModificaMovimentoRor_salvaConByPassDodicesimi'}" />	          
										  	
                      <s:include value="/jsp/movgest/include/modal.jsp" />
                      
                      <s:set var="consultaModificheProvvedimentoAction" value="%{'aggiornaModificaMovimentoRor_consultaModificheProvvedimento'}" />
				  	  <s:set var="consultaModificheProvvedimentoSubAction" value="%{'aggiornaModificaMovimentoRor_consultaModificheProvvedimentoSub'}" />
	
                      <s:include value="/jsp/movgest/include/provvedimento.jsp"/>
                      <%-- <s:include value="/jsp/movgest/include/cruscottoGestioneDatiContabili.jsp"/> --%>
                    <%-- </s:form> --%>
                  </s:else>
                </div>
              </fieldset>
            

          </div>
                   
      </div>
    </div>

</body>

<script type="text/javascript" src="${jspath}movgest/gestisciImpegnoRor.js"></script>