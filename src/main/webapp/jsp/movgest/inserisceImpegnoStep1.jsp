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
<%--corpo pagina --%>
<%--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> --%>

<s:if test="oggettoDaPopolareImpegno()">
	<s:set var="gestisciForwardAction" value="%{'inserisceImpegno_gestisciForward'}" />
	<s:set var="siSalvaAction" value="%{'inserisceImpegno_siSalva'}" />	 
	<s:set var="siProseguiAction" value="%{'inserisceImpegno_siProsegui'}" />	
	<s:set var="annullaSubImpegnoAction" value="%{'inserisceImpegno_annullaSubImpegno'}" />	 
	<s:set var="annullaSubAccertamentoAction" value="%{'inserisceImpegno_annullaSubAccertamento'}" />	 
	<s:set var="annullaMovGestSpesaAction" value="%{'inserisceImpegno_annullaMovGestSpesa'}" />	 
	<s:set var="eliminaSubImpegnoAction" value="%{'inserisceImpegno_eliminaSubImpegno'}" />	 
	<s:set var="eliminaSubAccertamentoAction" value="%{'inserisceImpegno_eliminaSubAccertamento'}" />
	<s:set var="forzaProseguiAction" value="%{'inserisceImpegno_forzaProsegui'}" />	          
	<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'inserisceImpegno_forzaSalvaPluriennaleAccertamento'}" />	          
	<s:set var="salvaConByPassDodicesimiAction" value="%{'inserisceImpegno_salvaConByPassDodicesimi'}" />	          
	
	<!--per modale provvedimento e elimina (incluse in modal.jsp) -->
	<s:set var="selezionaProvvedimentoAction" value="%{'inserisceImpegno_selezionaProvvedimento'}" />
	<s:set var="clearRicercaProvvedimentoAction" value="%{'inserisceImpegno_clearRicercaProvvedimento'}" />	          
	<s:set var="ricercaProvvedimentoAction" value="%{'inserisceImpegno_ricercaProvvedimento'}" />	          
	<s:set var="eliminaAction" value="%{'inserisceImpegno_elimina'}" />	  
	<s:set var="selezionaProvvedimentoInseritoAction" value="%{'inserisceImpegno_selezionaProvvedimentoInserito'}" />	
	<s:set var="inserisciProvvedimentoAction" value="%{'inserisceImpegno_inserisciProvvedimento'}" />	
	<s:set var="clearInserimentoProvvedimentoAction" value="%{'inserisceImpegno_clearInserimentoProvvedimento'}" />
		           
	<s:set var="consultaModificheProvvedimentoAction" value="%{'inserisceImpegno_consultaModificheProvvedimento'}" />
	<s:set var="consultaModificheProvvedimentoSubAction" value="%{'inserisceImpegno_consultaModificheProvvedimentoSub'}" />
	            
	<%--modale progetto --%>
	<s:set var="selezionaProgettoCronopAction" value="%{'inserisceImpegno_selezionaProgettoCronop'}" />	          
	<s:set var="selezionaProgettoAction" value="%{'inserisceImpegno_selezionaProgetto'}" />			
	<s:set var="pulisciRicercaProgettoAction" value="%{'inserisceImpegno_pulisciRicercaProgetto'}" />	          
	<s:set var="ricercaProgettoAction" value="%{'inserisceImpegno_ricercaProgetto'}" />	          
	<s:set var="codiceProgettoChangedAction" value="%{'inserisceImpegno_codiceProgettoChanged'}" /> 
		            
	<s:set var="ricercaCapitoloAction" value="%{'inserisceImpegno_ricercaCapitolo'}" />
	<s:set var="pulisciRicercaCapitoloAction" value="%{'inserisceImpegno_pulisciRicercaCapitolo'}" />
	<s:set var="selezionaCapitoloAction" value="%{'inserisceImpegno_selezionaCapitolo'}" />
	<s:set var="visualizzaDettaglioCapitoloAction" value="%{'inserisceImpegno_visualizzaDettaglioCapitolo'}" />
	        
	            
	<!--  per soggetto -->
	<s:set var="selezionaSoggettoAction" value="%{'inserisceImpegno_selezionaSoggetto'}" />
	<s:set var="pulisciRicercaSoggettoAction" value="%{'inserisceImpegno_pulisciRicercaSoggetto'}" />	          
	<s:set var="ricercaSoggettoAction" value="%{'inserisceImpegno_ricercaSoggetto'}" />	    
	<s:set var="listaClasseSoggettoChangedAction" value="%{'inserisceImpegno_listaClasseSoggettoChanged'}" />
	
	<!-- vincoli -->
	<!-- task-202 -->
	<!--<s:set var="aggiornaImportoConVincoloAction" value="%{'inserisceImpegno_aggiornaImportoConVincolo'}" /> -->
	<!--<s:set var="aggiornaAvanzoVincoloAction" value="%{'inserisceImpegno_aggiornaAvanzoVincolo'}" /> -->
	<!--<s:set var="aggiornaVincoloAction" value="%{'inserisceImpegno_aggiornaVincolo'}" /> -->
	<s:set var="aggiornaVincoloOperazione" value="%{'inserisceImpegno'}" />
		
	<s:set var="selezionaAccPerVincoloAction" value="%{'inserisceImpegno_selezionaAccPerVincolo'}" />
	
</s:if>
<s:else>
	<s:set var="gestisciForwardAction" value="%{'inserisceAccertamento_gestisciForward'}" />
	<s:set var="siSalvaAction" value="%{'inserisceAccertamento_siSalva'}" />	 
	<s:set var="siProseguiAction" value="%{'inserisceAccertamento_siProsegui'}" />	
	<s:set var="annullaSubImpegnoAction" value="%{'inserisceAccertamento_annullaSubImpegno'}" />	 
	<s:set var="annullaSubAccertamentoAction" value="%{'inserisceAccertamento_annullaSubAccertamento'}" />	 
	<s:set var="annullaMovGestSpesaAction" value="%{'inserisceAccertamento_annullaMovGestSpesa'}" />	 
	<s:set var="eliminaSubImpegnoAction" value="%{'inserisceAccertamento_eliminaSubImpegno'}" />	 
	<s:set var="eliminaSubAccertamentoAction" value="%{'inserisceAccertamento_eliminaSubAccertamento'}" />
	<s:set var="forzaProseguiAction" value="%{'inserisceAccertamento_forzaProsegui'}" />	          
	<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'inserisceAccertamento_forzaSalvaPluriennaleAccertamento'}" />	          
	<s:set var="salvaConByPassDodicesimiAction" value="%{'inserisceAccertamento_salvaConByPassDodicesimi'}" />	          
	
	<!--per modale provvedimento e elimina (incluse in modal.jsp) -->
	<s:set var="selezionaProvvedimentoAction" value="%{'inserisceAccertamento_selezionaProvvedimento'}" />
	<s:set var="clearRicercaProvvedimentoAction" value="%{'inserisceAccertamento_clearRicercaProvvedimento'}" />	          
	<s:set var="ricercaProvvedimentoAction" value="%{'inserisceAccertamento_ricercaProvvedimento'}" />	          
	<s:set var="eliminaAction" value="%{'inserisceAccertamento_elimina'}" />	  
	<s:set var="selezionaProvvedimentoInseritoAction" value="%{'inserisceAccertamento_selezionaProvvedimentoInserito'}" />	
	<s:set var="inserisciProvvedimentoAction" value="%{'inserisceAccertamento_inserisciProvvedimento'}" />	
	<s:set var="clearInserimentoProvvedimentoAction" value="%{'inserisceAccertamento_clearInserimentoProvvedimento'}" />
		           
	<s:set var="consultaModificheProvvedimentoAction" value="%{'inserisceAccertamento_consultaModificheProvvedimento'}" />
	<s:set var="consultaModificheProvvedimentoSubAction" value="%{'inserisceAccertamento_consultaModificheProvvedimentoSub'}" />
	            
	<%--modale progetto --%>
	<s:set var="selezionaProgettoCronopAction" value="%{'inserisceAccertamento_selezionaProgettoCronop'}" />	          
	<s:set var="selezionaProgettoAction" value="%{'inserisceAccertamento_selezionaProgetto'}" />			
	<s:set var="pulisciRicercaProgettoAction" value="%{'inserisceAccertamento_pulisciRicercaProgetto'}" />	          
	<s:set var="ricercaProgettoAction" value="%{'inserisceAccertamento_ricercaProgetto'}" />	          
	<s:set var="codiceProgettoChangedAction" value="%{'inserisceAccertamento_codiceProgettoChanged'}" /> 
		            
	<s:set var="ricercaCapitoloAction" value="%{'inserisceAccertamento_ricercaCapitolo'}" />
	<s:set var="pulisciRicercaCapitoloAction" value="%{'inserisceAccertamento_pulisciRicercaCapitolo'}" />
	<s:set var="selezionaCapitoloAction" value="%{'inserisceAccertamento_selezionaCapitolo'}" />
	 <s:set var="visualizzaDettaglioCapitoloAction" value="%{'inserisceAccertamento_visualizzaDettaglioCapitolo'}" />
	        
	            
	<!--  per soggetto -->
	<s:set var="selezionaSoggettoAction" value="%{'inserisceAccertamento_selezionaSoggetto'}" />
	<s:set var="pulisciRicercaSoggettoAction" value="%{'inserisceAccertamento_pulisciRicercaSoggetto'}" />	          
	<s:set var="ricercaSoggettoAction" value="%{'inserisceAccertamento_ricercaSoggetto'}" />	    
	<s:set var="listaClasseSoggettoChangedAction" value="%{'inserisceAccertamento_listaClasseSoggettoChanged'}" />
	
	<s:set var="selezionaAccPerVincoloAction" value="%{'inserisceAccertamento_selezionaAccPerVincolo'}" />		    
</s:else>  
   
<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">    
      <s:form id="%{labels.FORM}" action="%{labels.FORM}" method="post" class="form-horizontal">
          
          <s:if test="oggettoDaPopolareImpegno()">
	    	<s:set var="oggetto" value="%{'Impegno'}" />	          
	      </s:if>
	      <s:else>
	        <s:set var="oggetto" value="%{'Accertamento'}" />	          
	      </s:else> 
	       
       	<h3><s:property value="%{labels.TITOLO}"/></h3>
       	<div id="MyWizard" class="wizard">
          <ul class="steps">
            <li data-target="#step1" class="active"><span class="badge">1</span><s:property value="%{labels.STEP1}"/><span class="chevron"></span></li>
            <li data-target="#step2"><span class="badge">2</span>Classificazioni<span class="chevron"></span></li>
            <li data-target="#step3"><span class="badge">3</span><s:property value="%{labels.STEP3}"/><span class="chevron"></span></li>
          </ul>
        </div>
       
        <s:include value="/jsp/include/actionMessagesErrors.jsp" />
       
        <div class="step-content">
          <div class="step-pane active" id="step1">    
            
            <div id="refreshHeaderCapitolo">
            	<s:include value="/jsp/movgest/include/headerCapitolo.jsp"/>
            </div>
            <!-- SIAC 7349 -->
<%--             <s:include value="/jsp/movgest/include/capitolo.jsp" />  --%>

				 <s:if test="componenteBilancioCapitoloAttivo">
				 	<s:include value="/jsp/movgest/include/capitoloComponentiBilancio.jsp" /> 
				 </s:if>
				 <s:else>
				 	<s:include value="/jsp/movgest/include/capitolo.jsp" />
				 </s:else>
				 
                      
                                          
            <s:include value="/jsp/movgest/include/provvedimento.jsp" />
            <div id="refreshHeaderSoggetto">
            	<s:include value="/jsp/movgest/include/headerSoggetto.jsp"/>
            </div>
            <s:include value="/jsp/movgest/include/soggetto.jsp" />              
                <%--     TABELLE       RIEPILOGO   con azioni --%>              
            <s:include value="/jsp/movgest/include/datiEntita.jsp" />
            
            <%-- Vincolo --%>
            <s:if test="oggettoDaPopolareImpegno()"> 
             	<a id="ancoraVincoli"></a>
             	
             	<!--  per tabVincolo.jsp -->
				<s:set var="dettaglioAggiornaVincoloAction" value="%{'inserisceImpegno_dettaglioAggiornaVincolo'}" />
				<s:set var="dettaglioAvanzoAggiornaVincoloAction" value="%{'inserisceImpegno_dettaglioAvanzoAggiornaVincolo'}" />
				<s:set var="dettaglioAggiornaImportoConVincoliAction" value="%{'inserisceImpegno_dettaglioAggiornaImportoConVincoli'}" />
				<s:set var="pulisciRicercaAccPerVincoliAction" value="%{'inserisceImpegno_pulisciRicercaAccPerVincoli'}" />
				<s:set var="ricercaAccertamentoPerVincoliAction" value="%{'inserisceImpegno_ricercaAccertamentoPerVincoli'}" />
				<s:set var="dettaglioImportoResiduoAvanzoSelezionatoAction" value="%{'inserisceImpegno_dettaglioImportoResiduoAvanzoSelezionato'}" />
				<s:set var="eliminaAvanzoVincoloAction" value="%{'inserisceImpegno_eliminaAvanzoVincolo'}" />
				<s:set var="eliminaVincoloAction" value="%{'inserisceImpegno_eliminaVincolo'}" />
				<s:set var="annullaValoriVincoloAction" value="%{'inserisceImpegno_annullaValoriVincolo'}" />
				<s:set var="aggiungiVincoloAction" value="%{'inserisceImpegno_aggiungiVincolo'}" />
				
             	<s:include value="/jsp/movgest/include/tabVincolo.jsp" /> 
            </s:if> 
            <%-- fine Vincolo --%>
             
            <%-- qua c'era l'accertamento collegato --%>
            <%-- Modal --%> 
            
                   
            
             <s:include value="/jsp/movgest/include/modal.jsp" /> 
           
            <s:include value="/jsp/movgest/include/modalAccVincoli.jsp" /> 
          
			<s:include value="/jsp/movgest/include/modalProgettoCronop.jsp"/>	
			<%--/modale progetto --%>
            
			<s:include value="/jsp/include/modalCronop.jsp"/>	
            
            
            <%-- Fine Modal --%>
            <br/> <br/>    
            <%-- questi pulsanti servono se siamo in pluriennale --%>           
           
           
            <p>           
            
           	 <s:include value="/jsp/include/indietro.jsp" /> 
           
           
            <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
           			<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
					
					<%-- CR-2023 da eliminare 
					<s:hidden id="ricaricaAlberoContoEconomico" name="teSupport.ricaricaAlberoContoEconomico"/> 
					--%>
					
					<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
					<s:hidden id="ricaricaSiopeSpesa" name="teSupport.ricaricaSiopeSpesa"/>
		   <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->

             
            <s:hidden id="impegnoRiaccGiaRicercata" name="impegnoRiaccGiaRicercata"></s:hidden>
         
             <s:hidden id="doveMiTrovo" name="doveMiTrovo" value="Inserimento "></s:hidden>
             <s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden>
             
             
           <%-- ANNO BILANCIO CORRENTE --%>
           <s:hidden id="annoEsercizio" name="sessionHandler.annoEsercizio"/>
           
           <s:hidden id="bilPrecInPredispConsuntivo" name="bilancioPrecedenteInPredisposizioneConsuntivo"/>
           <s:hidden id="bilAttualeInPredispConsuntivo" name="bilancioAttualeInPredisposizioneConsuntivo"/>
           
           <s:hidden id="hiddenPerPrenotazioneLiquidabile" name="step1Model.hiddenPerPrenotazioneLiquidabile" />
           
            <!-- task-131 <s:submit method="annulla" value="annulla" cssClass="btn btn-secondary" id="annullaBtn"></s:submit> -->
            <s:submit action="inserisce%{#oggetto}_annulla" value="annulla" cssClass="btn btn-secondary" id="annullaBtn"></s:submit>
                    
            <!-- task-131 <s:submit name="prosegui" id="prosegui"  value="prosegui" method="prosegui" cssClass="btn btn-primary pull-right" /> -->
            <s:submit name="prosegui" id="prosegui"  value="prosegui" action="inserisce%{#oggetto}_prosegui" cssClass="btn btn-primary pull-right" />
            
             	
           <%--  <s:hidden id="checkProsegui" name="step1Model.capitolo.numCapitolo"></s:hidden>	 --%>
          
        	<a id="linkMsgDatipersi" href="#msgControlloProsegui" data-toggle="modal" style="display:none;"></a>
             
             </p> 
                   
          </div>
        </div>
        
      </s:form>
    </div>
  </div>	 
</div>	


<script type="text/javascript">

		var tipoDebitoSiopeVar = $("input[name='step1Model.tipoDebitoSiope']");
		<s:include value="/jsp/include/toggleAssenzaCig.jsp" />


	<s:if test="step1Model.checkproseguiRiacc || step1Model.checkproseguiOrigin">
		$("#linkMsgDatipersi").click();
	</s:if>
	
	<%-- apre il tab vincoli --%>
	<s:if test="step1Model.apriTabVincoli">
		$("#hrefTabVincolo").click();
	</s:if>
	<%-- apre il form di inserimento vincolo --%>
	<s:if test="step1Model.inserisciVincoloBtn">
		$("#inserisciVincoloBtn").click();
	</s:if>
	
	<s:if test="step1Model.portaAdAltezzaVincoli">
		spostaLAncora('ancoraVincoli');
	</s:if>
	
		
</script>
 
<script src="/siacfinapp/js/local/movgest/inserisceImpegno.js" type="text/javascript"></script>
<script src="/siacfinapp/js/local/include/modalRicercaCronop.js" type="text/javascript"></script>

   
<s:include value="/jsp/include/footer.jsp" />
 