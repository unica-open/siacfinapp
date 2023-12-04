<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>

<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>


<%-- Inclusione head e CSS NUOVO --%>
<s:include value="/jsp/include/head.jsp" />
  

</head>

<body>
  
  <s:include value="/jsp/include/header.jsp" />
  
     <%-- Inclusione JavaScript NUOVO --%>
    <s:include value="/jsp/include/javascript.jsp" />	
  	<s:include value="/jsp/include/javascriptTree.jsp" />
  
    
  <!-- NAVIGAZIONE
  <p class="nascosto"><a name="A-sommario" title="A-sommario"></a></p>     
  <ul id="sommario" class="nascosto">
    <li><a href="#A-contenuti">Salta ai contenuti</a></li>
  </ul>
   /NAVIGAZIONE -->
  <hr />
	<div class="container-fluid-banner">
	  <a name="A-contenuti" title="A-contenuti"></a>
	</div>

	<s:if test="oggettoDaPopolareImpegno()">
		<s:set var="oggetto" value="%{'Impegno'}" /> 
		<s:set var="proseguiSalvaAction" value="%{'aggiornaImpegnoStep1_proseguiSalva'}" />
		
		<s:set var="gestisciForwardAction" value="%{'aggiornaImpegnoStep1_gestisciForward'}" />	 
		<s:set var="siSalvaAction" value="%{'aggiornaImpegnoStep1_siSalva'}" />	 
		<s:set var="siProseguiAction" value="%{'aggiornaImpegnoStep1_siProsegui'}" />	
		<s:set var="annullaSubImpegnoAction" value="%{'aggiornaImpegnoStep1_annullaSubImpegno'}" />	 
		<s:set var="annullaSubAccertamentoAction" value="%{'aggiornaImpegnoStep1_annullaSubAccertamento'}" />	 
		<s:set var="annullaMovGestSpesaAction" value="%{'aggiornaImpegnoStep1_annullaMovGestSpesa'}" />	 
		<s:set var="eliminaSubImpegnoAction" value="%{'aggiornaImpegnoStep1_eliminaSubImpegno'}" />	 
		<s:set var="eliminaSubAccertamentoAction" value="%{'aggiornaImpegnoStep1_eliminaSubAccertamento'}" />
		<s:set var="forzaProseguiAction" value="%{'aggiornaImpegnoStep1_forzaProsegui'}" />	          
		<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'aggiornaImpegnoStep1_forzaSalvaPluriennaleAccertamento'}" />	          
		<s:set var="salvaConByPassDodicesimiAction" value="%{'aggiornaImpegnoStep1_salvaConByPassDodicesimi'}" />	          
		
		<s:set var="salvaDaModaleConfermaSalvaVincoliAction" value="%{'aggiornaImpegnoStep1_salvaDaModaleConfermaSalvaVincoli'}" />
		<s:set var="proseguiDaModaleConfermaSalvaVincoliAction" value="%{'aggiornaImpegnoStep1_proseguiDaModaleConfermaSalvaVincoli'}" />	
			
		<!--per modale provvedimento e elimina (incluse in modal.jsp) -->
		<s:set var="selezionaProvvedimentoAction" value="%{'aggiornaImpegnoStep1_selezionaProvvedimento'}" />	          
		<s:set var="clearRicercaProvvedimentoAction" value="%{'aggiornaImpegnoStep1_clearRicercaProvvedimento'}" />	          
		<s:set var="ricercaProvvedimentoAction" value="%{'aggiornaImpegnoStep1_ricercaProvvedimento'}" />	          
		<s:set var="eliminaAction" value="%{'aggiornaImpegnoStep1_elimina'}" />	  
		<s:set var="selezionaProvvedimentoInseritoAction" value="%{'aggiornaImpegnoStep1_selezionaProvvedimentoInserito'}" />	
		<s:set var="inserisciProvvedimentoAction" value="%{'aggiornaImpegnoStep1_inserisciProvvedimento'}" />
		<s:set var="clearInserimentoProvvedimentoAction" value="%{'aggiornaImpegnoStep1_clearInserimentoProvvedimento'}" />
		
		<s:set var="consultaModificheProvvedimentoAction" value="%{'aggiornaImpegnoStep1_consultaModificheProvvedimento'}" />
		<s:set var="consultaModificheProvvedimentoSubAction" value="%{'aggiornaImpegnoStep1_consultaModificheProvvedimentoSub'}" />
		   
		<!--per modale progetto -->
	    <s:set var="selezionaProgettoCronopAction" value="%{'aggiornaImpegnoStep1_selezionaProgettoCronop'}" />	          
		<s:set var="selezionaProgettoAction" value="%{'aggiornaImpegnoStep1_selezionaProgetto'}" />			          
		<s:set var="pulisciRicercaProgettoAction" value="%{'aggiornaImpegnoStep1_pulisciRicercaProgetto'}" />	          
		<s:set var="ricercaProgettoAction" value="%{'aggiornaImpegnoStep1_ricercaProgetto'}" />	 
		<s:set var="codiceProgettoChangedAction" value="%{'aggiornaImpegnoStep1_codiceProgettoChanged'}" /> 
		            
		<s:set var="ricercaCapitoloAction" value="%{'aggiornaImpegnoStep1_ricercaCapitolo'}" />
		<s:set var="pulisciRicercaCapitoloAction" value="%{'aggiornaImpegnoStep1_pulisciRicercaCapitolo'}" />
		<s:set var="selezionaCapitoloAction" value="%{'aggiornaImpegnoStep1_selezionaCapitolo'}" />
		<s:set var="visualizzaDettaglioCapitoloAction" value="%{'aggiornaImpegnoStep1_visualizzaDettaglioCapitolo'}" />
		        
				
		<!--  per soggetto -->
		<s:set var="selezionaSoggettoAction" value="%{'aggiornaImpegnoStep1_selezionaSoggetto'}" />
		<s:set var="pulisciRicercaSoggettoAction" value="%{'aggiornaImpegnoStep1_pulisciRicercaSoggetto'}" />	          
		<s:set var="ricercaSoggettoAction" value="%{'aggiornaImpegnoStep1_ricercaSoggetto'}" />	    
		<s:set var="listaClasseSoggettoChangedAction" value="%{'aggiornaImpegnoStep1_listaClasseSoggettoChanged'}" />	
		
		<!-- vincoli -->
		<!-- task-202 -->
		<!--<s:set var="aggiornaImportoConVincoloAction" value="%{'aggiornaImpegnoStep1_aggiornaImportoConVincolo'}" /> -->
		<!--<s:set var="aggiornaAvanzoVincoloAction" value="%{'aggiornaImpegnoStep1_aggiornaAvanzoVincolo'}" /> -->
		<!--<s:set var="aggiornaVincoloAction" value="%{'aggiornaImpegnoStep1_aggiornaVincolo'}" /> -->
		<s:set var="aggiornaVincoloOperazione" value="%{'aggiornaImpegnoStep1'}" />
		
		<s:set var="selezionaAccPerVincoloAction" value="%{'aggiornaImpegnoStep1_selezionaAccPerVincolo'}" />
			
			          
	</s:if>
	<s:else>
		<s:set var="oggetto" value="%{'Accertamento'}" />	      
		<s:set var="proseguiSalvaAction" value="%{'aggiornaAccertamentoStep1_proseguiSalva'}" />
		    
		<s:set var="gestisciForwardAction" value="%{'aggiornaAccertamentoStep1_gestisciForward'}" />
		<s:set var="siSalvaAction" value="%{'aggiornaAccertamentoStep1_siSalva'}" />	 
		<s:set var="siProseguiAction" value="%{'aggiornaAccertamentoStep1_siProsegui'}" />	
		<s:set var="annullaSubImpegnoAction" value="%{'aggiornaAccertamentoStep1_annullaSubImpegno'}" />	 
		<s:set var="annullaSubAccertamentoAction" value="%{'aggiornaAccertamentoStep1_annullaSubAccertamento'}" />	 
		<s:set var="annullaMovGestSpesaAction" value="%{'aggiornaAccertamentoStep1_annullaMovGestSpesa'}" />	 
		<s:set var="eliminaSubImpegnoAction" value="%{'aggiornaAccertamentoStep1_eliminaSubImpegno'}" />	 
		<s:set var="eliminaSubAccertamentoAction" value="%{'aggiornaAccertamentoStep1_eliminaSubAccertamento'}" />
		<s:set var="forzaProseguiAction" value="%{'aggiornaAccertamentoStep1_forzaProsegui'}" />	          
		<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'aggiornaAccertamentoStep1_forzaSalvaPluriennaleAccertamento'}" />	          
		<s:set var="salvaConByPassDodicesimiAction" value="%{'aggiornaAccertamentoStep1_salvaConByPassDodicesimi'}" />	          
			    
		<!--per modale provvedimento -->
		<s:set var="selezionaProvvedimentoAction" value="%{'aggiornaAccertamentoStep1_selezionaProvvedimento'}" />	          
		<s:set var="clearRicercaProvvedimentoAction" value="%{'aggiornaAccertamentoStep1_clearRicercaProvvedimento'}" />	          
		<s:set var="ricercaProvvedimentoAction" value="%{'aggiornaAccertamentoStep1_ricercaProvvedimento'}" />	          
		<s:set var="eliminaAction" value="%{'aggiornaAccertamentoStep1_elimina'}" />	  
		<s:set var="selezionaProvvedimentoInseritoAction" value="%{'aggiornaAccertamentoStep1_selezionaProvvedimentoInserito'}" />	
		<s:set var="inserisciProvvedimentoAction" value="%{'aggiornaAccertamentoStep1_inserisciProvvedimento'}" />	
		<s:set var="clearInserimentoProvvedimentoAction" value="%{'aggiornaAccertamentoStep1_clearInserimentoProvvedimento'}" />
		 
		<s:set var="consultaModificheProvvedimentoAction" value="%{'aggiornaAccertamentoStep1_consultaModificheProvvedimento'}" />
		<s:set var="consultaModificheProvvedimentoSubAction" value="%{'aggiornaAccertamentoStep1_consultaModificheProvvedimentoSub'}" />
		           
		<!--per modale progetto -->
	    <s:set var="selezionaProgettoCronopAction" value="%{'aggiornaAccertamentoStep1_selezionaProgettoCronop'}" />	          
		<s:set var="selezionaProgettoAction" value="%{'aggiornaAccertamentoStep1_selezionaProgetto'}" />			          
		<s:set var="pulisciRicercaProgettoAction" value="%{'aggiornaAccertamentoStep1_pulisciRicercaProgetto'}" />	          
		<s:set var="ricercaProgettoAction" value="%{'aggiornaAccertamentoStep1_ricercaProgetto'}" />	         
		<s:set var="codiceProgettoChangedAction" value="%{'aggiornaAccertamentoStep1_codiceProgettoChanged'}" />   
					
		<s:set var="ricercaCapitoloAction" value="%{'aggiornaAccertamentoStep1_ricercaCapitolo'}" />
		<s:set var="pulisciRicercaCapitoloAction" value="%{'aggiornaAccertamentoStep1_pulisciRicercaCapitolo'}" />
		<s:set var="selezionaCapitoloAction" value="%{'aggiornaAccertamentoStep1_selezionaCapitolo'}" />
		<s:set var="visualizzaDettaglioCapitoloAction" value="%{'aggiornaAccertamentoStep1_visualizzaDettaglioCapitolo'}" />
		                
		<!--  per soggetto -->
		<s:set var="selezionaSoggettoAction" value="%{'aggiornaAccertamentoStep1_selezionaSoggetto'}" />
		<s:set var="pulisciRicercaSoggettoAction" value="%{'aggiornaAccertamentoStep1_pulisciRicercaSoggetto'}" />	          
		<s:set var="ricercaSoggettoAction" value="%{'aggiornaAccertamentoStep1_ricercaSoggetto'}" />
		<s:set var="listaClasseSoggettoChangedAction" value="%{'aggiornaAccertamentoStep1_listaClasseSoggettoChanged'}" />
		
		<s:set var="selezionaAccPerVincoloAction" value="%{'aggiornaAccertamentoStep1_selezionaAccPerVincolo'}" />				
	</s:else>

<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">
    	<s:form class="form-horizontal" id="%{labels.FORM}" action="%{labels.FORM}" method="post" >
    	  <div id="msgControlloMovColl" class="modal hide fade" tabindex="-1" role="dialog"  aria-hidden="true">
			<div class="modal-body">
				<div class="alert alert-warning">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
    				<p><strong>Attenzione!</strong></p>
                   <s:iterator value="actionWarnings">
		     	  		<s:property escapeHtml="false"/><br>
		   		   </s:iterator>
				</div>
			</div>
			<div class="modal-footer">
                <!-- <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button> -->
				<!-- tak-131 <s:submit id="annullaMovCollBtn" name="btnAnnullaMovColl" method="annulla" value="no, annulla" cssClass="btn btn-secondary" /> -->
				<s:submit id="annullaMovCollBtn" name="btnAnnullaMovColl" action="aggiorna%{#oggetto}Step1_annulla" value="no, annulla" cssClass="btn btn-secondary" />
				
				
				<s:if test="isProseguiStep1()">
                	<!-- tak-131 <s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, prosegui" cssClass="btn btn-primary" method="siProsegui"/>-->
                	<s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, prosegui" cssClass="btn btn-primary" action="%{#siProseguiAction}"/>
                </s:if>
                <s:else>
                	<!-- tak-131 <s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, salva" cssClass="btn btn-primary" method="siSalva"/> -->
                	<s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, salva" cssClass="btn btn-primary" action="%{#siSalvaAction}"/>             	              	
                </s:else>
			</div>
		</div>
      	 
  
  
  
      	 <h3>Aggiorna <s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
      	 
      	 
      	<%--  <s:if test="%{successStep1}">
				<div class="alert alert-success margin-medium">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					L'operazione &egrave; stata completata con successo
				</div>   
			</s:if>  --%>
      	   
      	 <s:include value="/jsp/include/actionMessagesErrors.jsp" />
		 <s:include value="/jsp/movgest/include/tabAggImpegnoPerStep.jsp" />
		 
		 <s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
			
        <div id="MyWizard" class="wizard">
          <ul class="steps">
            <li data-target="#step1" class="active"><span class="badge ">1</span><s:property value="%{labels.STEP1}"/><span class="chevron"></span></li>
            <li data-target="#step2" ><span class="badge">2</span>Classificazioni<span class="chevron"></span></li>
            <!--li data-target="#step3"><span class="badge">3</span>Impegni pluriennali<span class="chevron"></span></li-->
          </ul>
        </div>
        <div class="step-content">
          <div class="step-pane active" id="step1">
			<h4>
				 Capitolo 
				 <a class="tooltip-test" title="Visualizza dettagli" href="#capitoloTab" data-toggle="modal">
				 	<i class="icon-info-sign">&nbsp;<span class="nascosto">Visualizza dettagli</span></i>
				 </a>
				 </dt>
				 <s:property value="step1Model.capitolo.anno"/>/<s:property value="step1Model.capitolo.numCapitolo"/>/<s:property value="step1Model.capitolo.articolo"/>/<s:property value="step1Model.capitolo.ueb"/> 
				  - <s:property value="step1Model.capitolo.descrizione"/>  -  <s:property value="step1Model.capitolo.codiceStrutturaAmministrativa"/> 
				  - tipo Finanziamento: <s:property value="step1Model.capitolo.tipoFinanziamento"/> -
				  <!--SIAC-7349 Nome componente e tipo-->
				  <s:if test="%{model.nomeComponente !=  null}">
				  	&nbsp;COMPONENTE: <s:property value="%{model.nomeComponente}"/>
				  </s:if>
				  <!--END SIAC-7349-->
				 </dd>              				
			</h4>	
	        
	        
			<s:include value="/jsp/movgest/include/provvedimento.jsp" />
               
            <s:if test="oggettoDaPopolareImpegno()">    
	            <!-- CR 1965 Parere finanziario -->   
	            <h4>Parere finanziario</h4>
				<div class="control-group">
						<label class="control-label">Parere finanziario</label>
						<div class="controls">
							<s:checkbox id="parereFinanziario" name="step1Model.parereFinanziario" disabled="!abilitaModificaParereFinanziario()" />
						</div>
						
						<s:if test="step1Model.parereFinanziarioDataModifica != null">
							 <span class="al">
	        					Data modifica:  <s:property value="%{step1Model.parereFinanziarioDataModifica}"/>
	        					-
	        					Login modifica:  <s:property value="step1Model.parereFinanziarioLoginOperazione"/>
	      					</span>
      					</s:if>
      					
      					<!-- 
      					<s:if test="isImpegnoSdf()">   
	      					<span class="guidata">
	      						<label class="control-label" style="text-decoration: underline;">ATTENZIONE Impegno Senza Disponibilita' Fondi !</label>
	      					</span>
      					</s:if>
      					 -->
						
			     </div>  
            </s:if>
               
            <div id="refreshHeaderSoggetto">
            	<s:include value="/jsp/movgest/include/headerSoggetto.jsp"/>
            </div>
<%--             <s:include value="/jsp/movgest/include/soggettoAggiorna.jsp" />  --%>
				 <s:include value="/jsp/movgest/include/soggetto.jsp" />
				 
            <s:include value="/jsp/movgest/include/datiEntita_aggimp.jsp" />
            
             <!-- Vincolo -->
            <s:if test="oggettoDaPopolareImpegno() && !isMovimentoResiduo()"> 
             	<a id="ancoraVincoli"></a>
             	
             	<!--  per tabVincolo.jsp -->
				<s:set var="dettaglioAggiornaVincoloAction" value="%{'aggiornaImpegnoStep1_dettaglioAggiornaVincolo'}" />
				<s:set var="dettaglioAvanzoAggiornaVincoloAction" value="%{'aggiornaImpegnoStep1_dettaglioAvanzoAggiornaVincolo'}" />
				<s:set var="dettaglioAggiornaImportoConVincoliAction" value="%{'aggiornaImpegnoStep1_dettaglioAggiornaImportoConVincoli'}" />
				<s:set var="pulisciRicercaAccPerVincoliAction" value="%{'aggiornaImpegnoStep1_pulisciRicercaAccPerVincoli'}" />
				<s:set var="ricercaAccertamentoPerVincoliAction" value="%{'aggiornaImpegnoStep1_ricercaAccertamentoPerVincoli'}" />
				<s:set var="dettaglioImportoResiduoAvanzoSelezionatoAction" value="%{'aggiornaImpegnoStep1_dettaglioImportoResiduoAvanzoSelezionato'}" />
				<s:set var="eliminaAvanzoVincoloAction" value="%{'aggiornaImpegnoStep1_eliminaAvanzoVincolo'}" />
				<s:set var="eliminaVincoloAction" value="%{'aggiornaImpegnoStep1_eliminaVincolo'}" />
				<s:set var="annullaValoriVincoloAction" value="%{'aggiornaImpegnoStep1_annullaValoriVincolo'}" />
				<s:set var="aggiungiVincoloAction" value="%{'aggiornaImpegnoStep1_aggiungiVincolo'}" />
				
		
			 	<s:include value="/jsp/movgest/include/tabVincolo.jsp" /> 
            </s:if> 
            <!-- fine Vincolo -->
            
            
          
            <s:include value="/jsp/movgest/include/modal.jsp" />
            
            <s:include value="/jsp/movgest/include/modalSalvaSdf.jsp"/>
                                   	                       
            <s:include value="/jsp/movgest/include/modalAccVincoli.jsp" />  
            
            <!--modale progetto -->   
			<s:include value="/jsp/movgest/include/modalProgettoCronop.jsp"/>	
			<!--/modale progetto -->
                        
                        
			<s:include value="/jsp/include/modalCronop.jsp"/>	
                                   
            <br/> <br/>                                         
            <p>

			<s:if test="%{!flagIndietro}">
				<s:include value="/jsp/include/indietro.jsp" />
			</s:if>
			<s:else>
				<!--task-131 <s:submit name="indietro" value="indietro" method="indietro" cssClass="btn btn-secondary" /> -->
				<s:submit name="indietro" value="indietro" action="aggiorna%{#oggetto}Step1_indietro" cssClass="btn btn-secondary" />
			</s:else>

			<s:hidden id="doveMiTrovo" name="doveMiTrovo" value="Aggiornamento "></s:hidden>
	        
	        <s:hidden id="hiddenPerPrenotazioneLiquidabile" name="step1Model.hiddenPerPrenotazioneLiquidabile" />
	               
	        <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
	        <s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
			
			<%-- CR-2023 da eliminare 
			<s:hidden id="ricaricaAlberoContoEconomico" name="teSupport.ricaricaAlberoContoEconomico"/> 
			--%>
			
			<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
			<s:hidden id="ricaricaSiopeSpesa" name="teSupport.ricaricaSiopeSpesa"/>
			<!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->

			<s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden>

			<%-- <s:if test="!isAbilitatoGestisciImpegnoDecentratoP()"> --%>
				<!-- task-131 <s:submit method="annulla" value="annulla" cssClass="btn btn-secondary" ></s:submit> -->
				 <s:submit action="aggiorna%{#oggetto}Step1_annulla" value="annulla" cssClass="btn btn-secondary" ></s:submit>
			<%-- </s:if> --%>
			
            <span class="pull-right">
            
            	<%-- <s:if test="!isAbilitatoGestisciImpegnoDecentratoP()"> --%>
	            	<!--task-131 <s:submit name="ripeti" value="ripeti" method="ripeti" cssClass="btn btn-primary"  />-->
	            	<!--task-131 <s:submit name="prosegui" value="prosegui" method="prosegui" cssClass="btn btn-primary"  />-->
           		    <s:submit name="ripeti" value="ripeti" action="aggiorna%{#oggetto}Step1_ripeti" cssClass="btn btn-primary"  />
	            	<s:submit name="prosegui" value="prosegui" action="aggiorna%{#oggetto}Step1_prosegui" cssClass="btn btn-primary pull-right"  />
            	    
					<%-- con disabilitaTabModificheAggiornamento == true &&  abilitaBottneSalvaDecentrato == false il bottone non si abilita! --%>
					
					<%-- SIAC-7320 viene disaccoppiata la gestione delle modifiche dall'azione decentrata
					<s:if test="disabilitaTabModificheAggiornamento()">
							
						<s:if test="isAbilitaBottneSalvaDecentrato()"> 
							<s:submit name="salva"  value="salva" id="salvaId" method="salva" cssClass="btn btn-primary freezePagina" />
						</s:if>	
						
					</s:if> --%>
					<s:if test="salvaDaNormaleASDF()"> 
						<div class="btn btn-primary pull-right">
							<a id="linkSalvaConConferma" href="#msgConfermaSDF" data-toggle="modal" class="pull-right">
							salva</a> 
						</div>
					</s:if>
					<s:elseif test="salvaDaSDFANormale()">
						<div class="btn btn-primary pull-right">
							<a id="linkSalvaConConfermaDaSdfANormale" href="#msgConfermaSDFDiNuovoDisp" data-toggle="modal" class="pull-right">
							salva</a> 
						</div>
					</s:elseif>
					<s:elseif test="isAbilitaBottneSalvaDecentrato()">
							<!-- task-131 <s:submit name="salva"  value="salva" id="salvaId" method="proseguiSalva" cssClass="btn btn-primary freezePagina" />-->
							<s:submit name="salva"  value="salva" id="salvaId" action="%{#proseguiSalvaAction}" cssClass="btn btn-primary freezePagina" />								
					</s:elseif>
	            
            </span>
			<a id="linkMsgControlloMovColl" href="#msgControlloMovColl" data-toggle="modal" style="display:none;"></a>
			<a id="linkVisualizzaModaleConfermaModificaProvvedimento" href="#modalSalvaModificaProvvedimento" data-toggle="modal" style="display:none;"></a>
			<a id="linkVisualizzaModaleConfermaProseguiModificaProvvedimento" href="#modalProseguiModificaProvvedimento" data-toggle="modal" style="display:none;"></a>
			
			<!-- RM 29/09/2017-->
            <!-- MODALE CONFERMA PROSEGUI MODIFICA VINCOLI  -->
			<a id="linkVisualizzaModaleConfermaSalvaModificaVincoli" href="#modalConfermaSalvaModificaVincoli" data-toggle="modal" style="display:none;"></a>
			<a id="linkVisualizzaModaleConfermaProseguiModificaVincoli" href="#modalConfermaProseguiModificaVincoli" data-toggle="modal" style="display:none;"></a>
			<!-- FINE -->
			
			<!-- DODICESIMI: -->
			<a id="linkVisualizzaModaleConfermaSalvaConBypassDodicesimi" href="#modalSalvaConBypassDodicesimi" data-toggle="modal" style="display:none;"></a>
            
            <a id="linkmsgPrimaNota" href="#msgPrimaNota" data-toggle="modal" style="display:none;"></a>
            
            </p>
            
          </div>
        </div>
        
        
		<!-- SIAC-5333 -->
		<s:include value="/jsp/movgest/include/modalValidaPrimaNota.jsp"/>
        <!-- SIAC-5333 -->
        
        
        <!-- SIAC-6993 -->
        			<!--  TODO aggiugere s:if -->
					<!-- task-131 <s:url method="consultaModificheProvvedimento"	var="consultaModificheProvvedimento" /> -->
					<!-- task-131 <s:url method="consultaModificheProvvedimentoSub"	var="consultaModificheProvvedimentoSub" /> -->
					<s:url action="aggiorna%{#oggetto}Step1_consultaModificheProvvedimento"	var="consultaModificheProvvedimento" /> 
					<s:url method="aggiorna%{#oggetto}Step1_consultaModificheProvvedimentoSub"	var="consultaModificheProvvedimentoSub" />
					
					
					<s:hidden id="consultaModificheProvvedimento" value="%{consultaModificheProvvedimento}" />
					<s:hidden id="consultaModificheProvvedimentoSub" value="%{consultaModificheProvvedimentoSub}" />
        <!-- SIAC-6993 -->
        
         <!-- SIAC-6997 -->
					<!-- task-131 <s:url method="consultaModificheStrutturaCompetente" var="consultaModificheStrutturaCompetente" /> -->
					<!-- task-131 <s:url method="consultaModificheStrutturaCompetenteSub" var="consultaModificheStrutturaCompetenteSub" /> -->
					<s:url method="aggiorna%{#oggetto}Step1_consultaModificheStrutturaCompetente" var="consultaModificheStrutturaCompetente" />
					<s:url method="aggiorna%{#oggetto}Step1_consultaModificheStrutturaCompetenteSub" var="consultaModificheStrutturaCompetenteSub" />						
						
					<s:hidden id="consultaModificheStrutturaCompetente"	value="%{consultaModificheStrutturaCompetente}" />
					<s:hidden id="consultaModificheStrutturaCompetenteSub" value="%{consultaModificheStrutturaCompetenteSub}" />
        <!-- SIAC-6997 -->

	  </s:form>
    </div>
  </div>	 
</div>	
   
  <script type="text/javascript">

	$(document).ready(function() {
		
		// SIAC-6103
		if ( $("#parereFinanziario").is(':checked') ) {
			$("#parereFinanziario").attr('readonly',true);
			$("#parereFinanziario").on( "click", function() {
				return false;
			});
		}
		
		var bloccoPrenotatoLiquidabile = $("#bloccoPrenotatoLiquidabile");
		var prenotazioneNo = $("#prenotazioneNo");
		var prenotazioneSi = $("#prenotazioneSi");

		/*	var radioPluriennaleNo = $("#radioPluriennaleNo");
		var radioPluriennaleSi = $("#radioPluriennaleSi");
		var numeroPluriennaliLabel = $("#numeroPluriennaliLabel");
		var numeroPluriennali = $("#numeroPluriennali");
	*/	var bloccoRiaccertato = $("#bloccoRiaccertato");
	    var riaccertatoNo = $("#riaccertatoNo");
		var riaccertatoSi = $("#riaccertatoSi");
		var annoImpRiacc = $("#annoImpRiacc");
		var numImpRiacc = $("#numImpRiacc");
		
	/*	if (radioPluriennaleNo.is(':checked')) {
			numeroPluriennaliLabel.hide();
			numeroPluriennali.hide();
		}
		if (radioPluriennaleSi.is(':checked')) {
			numeroPluriennaliLabel.show();
			numeroPluriennali.show();
		}
	*/	if (riaccertatoNo.is(':checked')) {
// 			annoImpRiacc.hide();
// 			numImpRiacc.hide();
			bloccoRiaccertato.hide();
		}
		if (riaccertatoSi.is(':checked')) {
// 			annoImpRiacc.show();
// 			numImpRiacc.show();
			bloccoRiaccertato.show();
		}
		
		if (prenotazioneNo.is(':checked')) {
			bloccoPrenotatoLiquidabile.hide();
		}
		if (prenotazioneSi.is(':checked')) {	
			bloccoPrenotatoLiquidabile.show();
		} 
		
		$("#ricercaGuidataCapitolo").click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaRicercaCapitolo");
			var selectedNode = treeObj.getCheckedNodes(true);
			var strutturaAmministrativaSelezionata = {};
			strutturaAmministrativaSelezionata.codice = "";
			if (selectedNode != null) {
				selectedNode.forEach(function(currentNode) {
				    strutturaAmministrativaSelezionata = currentNode;
				});
			}
			$.ajax({
				// task-131 url: '<s:url method="ricercaCapitolo"/>',
				url: '<s:url action="%{#ricercaCapitoloAction}"/>',
				type: 'POST',
				data: $(".parametroRicercaCapitolo").serialize() + "&strutturaAmministrativaSelezionata=" + strutturaAmministrativaSelezionata.codice,
				success: function(data)  {
					$("#gestioneRisultatoRicercaCapitoli").html(data);
				}
			});
		});	
		
		$("#ricercaGuidataSoggetto").click(function() {
			$.ajax({
				//task-131 url: '<s:url method="ricercaSoggetto"/>',
				url: '<s:url action="%{#ricercaSoggettoAction}"/>',
				type: 'POST',
				data: $(".parametroRicercaSoggetto").serialize(),
			    success: function(data)  {
				    $("#gestioneRisultatoRicercaSoggetti").html(data);
				}
			});
		});	
		
		$("#linkCompilazioneGuidataCapitolo").click(function(){
			$("#capitoloRicerca").val($("#capitolo").val());
			$("#articoloRicerca").val($("#articolo").val());
			$("#uebRicerca").val($("#ueb").val());
		});
		
       $("#linkCompilazioneGuidataProvvedimento").click(function(){

      		var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaOrdinativoIncasso");
      			var strutturaAmministrativaParam = "";
      			if (treeObj != null) {
      				var selectedNode = treeObj.getCheckedNodes(true);
      				selectedNode.forEach(function(currentNode) {
      					strutturaAmministrativaParam = currentNode;
      				});
      			}
      		
      		
      			initRicercaGuidataProvvedimentoConStruttura(
      				$("#annoProvvedimento").val(), 
      				$("#numeroProvvedimento").val(),
      				$("#listaTipiProvvedimenti").val(),
      				strutturaAmministrativaParam
      			);
      	});
       
       
       $("#linkInserisciProvvedimento").click(function(){

   		var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaInserimentoProvvedimento");
   			var strutturaAmministrativaParam = "";
   			if (treeObj != null) {
   				var selectedNode = treeObj.getCheckedNodes(true);
   				selectedNode.forEach(function(currentNode) {
   					strutturaAmministrativaParam = currentNode;
   				});
   			}
   			
   			$.ajax({
   				//task-131 url: '<s:url method="clearInserimentoProvvedimento"/>',
   				url: '<s:url action="%{#clearInserimentoProvvedimentoAction}"/>',
   				success: function(data)  {
   					$("#gestioneEsitoInserimentoProvvedimento").html(data);
   				}
   			});

   		});
				
		$("#linkCompilazioneGuidataSoggetto").click(function(){
			initRicercaGuidataSoggetto(
				$("#codCreditore").val(),
				$("#listaClasseSoggetto").val()
			);
		});
		
		 $("#linkCompilazioneGuidataProgetto").click(function(){
		    	initRicercaGuidataProgetto($("#progetto").val());
			});
		
		$("#progetto").change(function(){
			var cod = $("#progetto").val();
			//Carico i dati in tabella "Modalita' di pagamento"		
			$.ajax({
				//task-131 url: '<s:url method="codiceProgettoChanged"></s:url>',
				url: '<s:url action="%{#codiceProgettoChangedAction}"/>',
				type: "GET",
				data: $(".hiddenGestoreToggle").serialize() + "&id=" + cod, 
			    success: function(data)  {
			    	$("#refreshDescrizioneProgetto").html(data);
				}
			});			
		});
		
		$("#codCreditore").blur(function(){
			$("#listaClasseSoggetto").val(-1);
		});
		
		$("#listaClasseSoggetto").change(function(){
			$("#codCreditore").val("");
			$.ajax({
				//url: '<s:url method="listaClasseSoggettoChanged"/>',
				url: '<s:url action="%{#listaClasseSoggettoChangedAction}"/>',
   				success: function(data)  {
				    $("#refreshHeaderSoggetto").html(data);
				}
			});
		});
		
		$("#cercaCapitoloSubmit").click(function(){
			$("#capitolo").attr("disabled", true);
			$("#articolo").attr("disabled", true);
			$("#ueb").attr("disabled", true);
		});
		
	/*	radioPluriennaleNo.change(function(){
			numeroPluriennaliLabel.hide();
			numeroPluriennali.hide();
		});
		
		radioPluriennaleSi.change(function(){
			numeroPluriennaliLabel.show();
			numeroPluriennali.show();
		});
	*/	
		riaccertatoNo.change(function(){
// 			annoImpRiacc.hide();
// 			numImpRiacc.hide();
			bloccoRiaccertato.hide();
		});
		
		riaccertatoSi.change(function(){
// 			annoImpRiacc.show();
// 			numImpRiacc.show();
			bloccoRiaccertato.show();
		});
		
		prenotazioneNo.change(function(){
			bloccoPrenotatoLiquidabile.hide();
		});
		
		prenotazioneSi.change(function(){
			bloccoPrenotatoLiquidabile.show();
		});
		
		
		var tipoDebitoSiopeVar = $("input[name='step1Model.tipoDebitoSiope']");
		<s:include value="/jsp/include/toggleAssenzaCig.jsp" />	
		
	});

	<s:if test="%{!richiediConfermaRedirezioneContabilitaGenerale}">
		
		<s:if test="isShowPopUpMovColl()">
			$("#linkMsgControlloMovColl").click();
		</s:if>
		
		<s:if test="isShowModaleConfermaModificaProvvedimento()">
			$("#linkVisualizzaModaleConfermaModificaProvvedimento").click();
		</s:if>
		
		<s:if test="isShowModaleConfermaProseguiModificaProvvedimento()">
			$("#linkVisualizzaModaleConfermaProseguiModificaProvvedimento").click();
		</s:if>
		
		/* **********************************************************************  */
		/* CR vitelli su aggiorna vincoli anche senza disponibilita' sul capitolo */
		<s:if test="isShowModaleConfermaSalvaModificaVincoli()">
			$("#linkVisualizzaModaleConfermaSalvaModificaVincoli").click();
		</s:if>
		<s:if test="isShowModaleConfermaProseguiModificaVincoli()">
			$("#linkVisualizzaModaleConfermaProseguiModificaVincoli").click();
		</s:if>
		/*  ************** fine *************************************************** */
		
		<s:if test="isShowModaleConfermaSalvaConBypassDodicesimi()">
			$("#linkVisualizzaModaleConfermaSalvaConBypassDodicesimi").click();
		</s:if>
		
		<!-- apre il form di inserimento vincolo -->
		<!-- apre il tab vincoli -->
		<s:if test="step1Model.apriTabVincoli">
			$("#hrefTabVincolo").click();
		</s:if>
		<!-- apre il form di inserimento vincolo -->
		<s:if test="step1Model.inserisciVincoloBtn">
			$("#inserisciVincoloBtn").click();
		</s:if>
	
	</s:if>
	<s:else>
		<s:if test="oggettoDaPopolareImpegno()">
			$('#linkmsgPrimaNota').click();
		 	$('#inserisciPrimaNotaProvvisoria').on('click', function(){
				$('#HIDDEN_saltaInserimentoPrimaNota').val(false);
				$('#HIDDEN_richiediConfermaUtente').val(true);
				
				$('form')
				//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
				.append('<input type="hidden" name="action:aggiornaImpegnoStep1_salva" value="" class="btn" >')
				action="%{#salvaAction}"
				.submit();
			});
			$('#validaPrimaNota').on('click', function(){
				$('#HIDDEN_saltaInserimentoPrimaNota').val(true);
				$('#HIDDEN_richiediConfermaUtente').val(true);
				$('form')
				//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
				.append('<input type="hidden" name="action:aggiornaImpegnoStep1_salva" value="" class="btn" >')	
				.submit(); 
				});
		</s:if>
		<s:else>
			$('#linkmsgPrimaNota').click();
		 	$('#inserisciPrimaNotaProvvisoria').on('click', function(){
				$('#HIDDEN_saltaInserimentoPrimaNota').val(false);
				$('#HIDDEN_richiediConfermaUtente').val(true);
				
				$('form')
				//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
				.append('<input type="hidden" name="action:aggiornaAccertamentoStep1_salva" value="" class="btn" >')
				action="%{#salvaAction}"
				.submit();
			});
			$('#validaPrimaNota').on('click', function(){
				$('#HIDDEN_saltaInserimentoPrimaNota').val(true);
				$('#HIDDEN_richiediConfermaUtente').val(true);
				$('form')
				//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
				.append('<input type="hidden" name="action:aggiornaAccertamentoStep1_salva" value="" class="btn" >')	
				.submit(); 
				});
		</s:else>	
	</s:else>
		
	
	<s:if test="step1Model.portaAdAltezzaVincoli">
		spostaLAncora('ancoraVincoli');
	</s:if>
	
	function impostaValorePrenotatoLiquidabile(){
		cbObj = document.getElementById("prenotatoLiquidabileCheckBox");
        var valore = cbObj.checked;
        $("#hiddenPerPrenotazioneLiquidabile").val(valore);
	}
	
	
</script>  
 
<script src="/siacfinapp/js/local/movgest/aggiornaImpegno.js" type="text/javascript"></script>
<script src="/siacfinapp/js/local/include/modalRicercaCronop.js" type="text/javascript"></script>

 
   
<s:include value="/jsp/include/footer.jsp" />