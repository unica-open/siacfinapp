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

<s:set var="gestisciForwardAction" value="%{'aggiornaModificaAccRor_gestisciForward'}" />	 
<s:set var="siSalvaAction" value="%{'aggiornaModificaAccRor_siSalva'}" />	 
<s:set var="siProseguiAction" value="%{'aggiornaModificaAccRor_siProsegui'}" />	
<s:set var="annullaSubImpegnoAction" value="%{'aggiornaModificaAccRor_annullaSubImpegno'}" />	 
<s:set var="annullaSubAccertamentoAction" value="%{'aggiornaModificaAccRor_annullaSubAccertamento'}" />	 
<s:set var="annullaMovGestSpesaAction" value="%{'aggiornaModificaAccRor_annullaMovGestSpesa'}" />	 
<s:set var="eliminaSubImpegnoAction" value="%{'aggiornaModificaAccRor_eliminaSubImpegno'}" />	 
<s:set var="eliminaSubAccertamentoAction" value="%{'aggiornaModificaAccRor_eliminaSubAccertamento'}" />
<s:set var="forzaProseguiAction" value="%{'aggiornaModificaAccRor_forzaProsegui'}" />	          
<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'aggiornaModificaAccRor_forzaSalvaPluriennaleAccertamento'}" />	          
<s:set var="salvaConByPassDodicesimiAction" value="%{'aggiornaModificaAccRor_salvaConByPassDodicesimi'}" />	          
		
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
                <s:form class="form-horizontal" id="selezionaProvvedimento" action="aggiornaModificaAccRor" method="post" > 
                  <s:set var="consultaModificheProvvedimentoAction" value="%{'aggiornaModificaAccRor_consultaModificheProvvedimento'}" />
				  <s:set var="consultaModificheProvvedimentoSubAction" value="%{'aggiornaModificaAccRor_consultaModificheProvvedimentoSub'}" />
                  
                  <s:include value="/jsp/movgest/include/provvedimento.jsp"/> 
				  
				  <s:set var="selezionaProvvedimentoAction" value="%{'aggiornaModificaAccRor_selezionaProvvedimento'}" />
                  <s:set var="clearRicercaProvvedimentoAction" value="%{'aggiornaModificaAccRor_clearRicercaProvvedimento'}" />	          
       			  <s:set var="ricercaProvvedimentoAction" value="%{'aggiornaModificaAccRor_ricercaProvvedimento'}" />	                
                  <s:set var="eliminaAction" value="%{'aggiornaModificaAccRor_elimina'}" />	  
            	  <s:set var="selezionaProvvedimentoInseritoAction" value="%{'aggiornaModificaAccRor_selezionaProvvedimentoInserito'}" />	
				  <s:set var="inserisciProvvedimentoAction" value="%{'aggiornaModificaAccRor_inserisciProvvedimento'}" />	
	
            	  <s:include value="/jsp/movgest/include/modal.jsp" />
                
                  <s:set var="confermaSpeseCollegateAction" value="%{'aggiornaModificaAccRor_confermaSpeseCollegate'}" />	
	              <s:include value="/jsp/movgest/include/cruscottoGestioneDatiContabiliAcc.jsp" /> 
                
                
                
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
<script type="text/javascript" src="${jspath}movgest/descrizioniMotivoAccRor.js"></script><%-- Modifica per siac-7968 carica le motivazione degli accertamenti --%>
<script type="text/javascript" src="${jspath}movgest/gestisciImpegnoRor.js"></script>
