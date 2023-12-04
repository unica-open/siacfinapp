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

<body id="cruscottoBody">
    <s:include value="/jsp/include/header.jsp" />
	<hr />
	<div class="container-fluid-banner">
		<a name="A-contenuti" title="A-contenuti"></a>
	</div>
    <div class="container-fluid">
      
		  <div class="row-fluid">
			  <div class="span12 contentPage">
          <s:include value="/jsp/include/actionMessagesErrors.jsp" />
           
          <s:if test = "isInInserimento()">
            <h3>Gestione Inserimento ROR <s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
          </s:if>
          <s:else>
            <h3>Gestione Aggiornamento ROR <s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
          </s:else>
                 
            <fieldset class="step-content">
              <div class="step-pane active" id="step1">
                <%-- SIAC-7952 rimuovo .do dalla action --%>
                <s:form class="form-horizontal" id="selezionaProvvedimento" action="aggiornaModificaMovimentoRor" method="post" > 
                  <s:set var="consultaModificheProvvedimentoAction" value="%{'aggiornaModificaMovimentoRor_consultaModificheProvvedimento'}" />
				  <s:set var="consultaModificheProvvedimentoSubAction" value="%{'aggiornaModificaMovimentoRor_consultaModificheProvvedimentoSub'}" />
                  
                  <s:include value="/jsp/movgest/include/provvedimento.jsp"/> 

				  <s:set var="selezionaProvvedimentoAction" value="%{'aggiornaModificaMovimentoRor_selezionaProvvedimento'}" />   
				  <s:set var="clearRicercaProvvedimentoAction" value="%{'aggiornaModificaMovimentoRor_clearRicercaProvvedimento'}" />	          
       			  <s:set var="ricercaProvvedimentoAction" value="%{'aggiornaModificaMovimentoRor_ricercaProvvedimento'}" />	                
                  <s:set var="eliminaAction" value="%{'aggiornaModificaMovimentoRor_elimina'}" />	  
                  <s:set var="selezionaProvvedimentoInseritoAction" value="%{'aggiornaModificaMovimentoRor_selezionaProvvedimentoInserito'}" />	
				  <s:set var="inserisciProvvedimentoAction" value="%{'aggiornaModificaMovimentoRor_inserisciProvvedimento'}" />	
	  
                  <s:include value="/jsp/movgest/include/modal.jsp" />
          
          		  <s:set var="confermaSpeseCollegateAction" value="%{'aggiornaModificaMovimentoRor_confermaSpeseCollegate'}" />	
	              <s:include value="/jsp/movgest/include/cruscottoGestioneDatiContabili.jsp" />
                
                
                
                </s:form>
                
              </div>
            </fieldset>

          </div>
                   
      </div>
    </div>
    <div id="overlay"></div>
    <style>
      #overlay {
        position: fixed;
        display: none;
        width: 100%;
        height: 100%;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: rgba(0,0,0,0.5);
        z-index: 2;
        cursor: pointer;
      }
    </style>

</body>
<script type="text/javascript" src="${jspath}movgest/descrizioniMotivoRor.js"></script>
<script type="text/javascript" src="${jspath}movgest/gestisciImpegnoRor.js"></script>
