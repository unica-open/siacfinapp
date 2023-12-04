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


</head>
  
   
    
    
  <body>     
  
  
    <s:include value="/jsp/include/header.jsp" />
    
    
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
 		<s:set var="selezionaProvvedimentoAction" value="%{'inserisceImpegnoStep3_selezionaProvvedimento'}" />
	    <s:set var="clearRicercaProvvedimentoAction" value="%{'inserisceImpegnoStep3_clearRicercaProvvedimento'}" />	          
	    <s:set var="ricercaProvvedimentoAction" value="%{'inserisceImpegnoStep3_ricercaProvvedimento'}" />	          
	    <s:set var="eliminaAction" value="%{'inserisceImpegnoStep3_elimina'}" />	
	           
	    <s:set var="gestisciForwardAction" value="%{'inserisceImpegnoStep3_gestisciForward'}" />
		<s:set var="siSalvaAction" value="%{'inserisceImpegnoStep3_siSalva'}" />	 
		<s:set var="siProseguiAction" value="%{'inserisceImpegnoStep3_siProsegui'}" />	
		<s:set var="annullaSubImpegnoAction" value="%{'inserisceImpegnoStep3_annullaSubImpegno'}" />	 
		<s:set var="annullaSubAccertamentoAction" value="%{'inserisceImpegnoStep3_annullaSubAccertamento'}" />	 
		<s:set var="annullaMovGestSpesaAction" value="%{'inserisceImpegnoStep3_annullaMovGestSpesa'}" />	 
		<s:set var="eliminaSubImpegnoAction" value="%{'inserisceImpegnoStep3_eliminaSubImpegno'}" />	 
		<s:set var="eliminaSubAccertamentoAction" value="%{'inserisceImpegnoStep3_eliminaSubAccertamento'}" />
		<s:set var="forzaProseguiAction" value="%{'inserisceImpegnoStep3_forzaProsegui'}" />	          
		<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'inserisceImpegnoStep3_forzaSalvaPluriennaleAccertamento'}" />	          
		<s:set var="salvaConByPassDodicesimiAction" value="%{'inserisceImpegnoStep3_salvaConByPassDodicesimi'}" />
		
		<s:set var="salvaPluriennalePrimeNoteEsercizioInCorsoAction" value="%{'inserisceImpegnoStep3_salvaPluriennalePrimeNoteEsercizioInCorso'}" />
		<s:set var="salvaPluriennalePrimeNoteEserciziFuturiAction" value="%{'inserisceImpegnoStep3_salvaPluriennalePrimeNoteEserciziFuturi'}" />
			          
	</s:if>
    <s:else>
    	<s:set var="selezionaProvvedimentoAction" value="%{'inserisceAccertamentoStep3_selezionaProvvedimento'}" />
	    <s:set var="clearRicercaProvvedimentoAction" value="%{'inserisceAccertamentoStep3_clearRicercaProvvedimento'}" />	          
	    <s:set var="ricercaProvvedimentoAction" value="%{'inserisceAccertamentoStep3_ricercaProvvedimento'}" />	          
	    <s:set var="eliminaAction" value="%{'inserisceAccertamentoStep3_elimina'}" />	  
	    
	    <s:set var="gestisciForwardAction" value="%{'inserisceAccertamentoStep3_gestisciForward'}" />
		<s:set var="siSalvaAction" value="%{'inserisceAccertamentoStep3_siSalva'}" />	 
		<s:set var="siProseguiAction" value="%{'inserisceAccertamentoStep3_siProsegui'}" />	
		<s:set var="annullaSubImpegnoAction" value="%{'inserisceAccertamentoStep3_annullaSubImpegno'}" />	 
		<s:set var="annullaSubAccertamentoAction" value="%{'inserisceAccertamentoStep3_annullaSubAccertamento'}" />	 
		<s:set var="annullaMovGestSpesaAction" value="%{'inserisceAccertamentoStep3_annullaMovGestSpesa'}" />	 
		<s:set var="eliminaSubImpegnoAction" value="%{'inserisceAccertamentoStep3_eliminaSubImpegno'}" />	 
		<s:set var="eliminaSubAccertamentoAction" value="%{'inserisceAccertamentoStep3_eliminaSubAccertamento'}" />
		<s:set var="forzaProseguiAction" value="%{'inserisceAccertamentoStep3_forzaProsegui'}" />	          
		<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'inserisceAccertamentoStep3_forzaSalvaPluriennaleAccertamento'}" />	          
		<s:set var="salvaConByPassDodicesimiAction" value="%{'inserisceAccertamentoStep3_salvaConByPassDodicesimi'}" />	
    	
    	<s:set var="salvaPluriennalePrimeNoteEsercizioInCorsoAction" value="%{'inserisceAccertamentoStep3_salvaPluriennalePrimeNoteEsercizioInCorso'}" />
		<s:set var="salvaPluriennalePrimeNoteEserciziFuturiAction" value="%{'inserisceAccertamentoStep3_salvaPluriennalePrimeNoteEserciziFuturi'}" />
		
    </s:else>
            
  <div class="container-fluid">
    <div class="row-fluid">
      <div class="span12 contentPage">    
        <s:form id="%{labels.FORM}" action="%{labels.FORM}" method="post">
        	 <s:if test="oggettoDaPopolareImpegno()">
		    	<s:set var="oggetto" value="%{'Impegno'}" />	          
			 </s:if>
			 <s:else>
				<s:set var="oggetto" value="%{'Accertamento'}" />	          
			 </s:else> 
	       
	       	<h3><s:property value="%{labels.TITOLO}"/></h3>
	       	<div id="MyWizard" class="wizard">
	          <ul class="steps">
	            <li data-target="#step1" class="complete"><span class="badge badge-success">1</span><s:property value="%{labels.STEP1}"/><span class="chevron"></span></li>
	            <li data-target="#step2" class="complete"><span class="badge badge-success">2</span>Classificazioni<span class="chevron"></span></li>
	            <li data-target="#step3" class="active"><span class="badge">3</span><s:property value="%{labels.STEP3}"/><span class="chevron"></span></li>
	          </ul>
	        </div>
          
             <s:include value="/jsp/include/actionMessagesErrors.jsp" />
			<div class="step-content"> 
           	 <div class="step-pane active" id="step2">
              <h4>Anni di pluriennale: <s:property value="step1Model.numeroPluriennali"/></h4>
                  
                  <!-- dettaglio con capitolo/provvedimento/soggetto -->
                  <s:include value="/jsp/movgest/include/headerDettaglioMovGest.jsp" />
                  
                  <display:table name="step3Model.listaImpegniPluriennali"  
                                 class="table tab_left table-hover" 
                                 summary="riepilogo soggetti"
                                 requestURI="inserisceImpegnoStep3.do"
				                 uid="ricercaImplegnoPlur" >
					 <display:column title="Anno"><s:textfield id="annoImpPluriennale" cssClass="input-small" onkeyup="return checkItNumbersOnly(event)" name="step3Model.listaImpegniPluriennali[%{#attr.ricercaImplegnoPlur_rowNum - 1}].annoImpPluriennale" maxlength="4"></s:textfield> </display:column>
		             <display:column title="Importo" ><s:textfield id="importoImpPluriennale" cssClass="input-small soloNumeri decimale" name="step3Model.listaImpegniPluriennali[%{#attr.ricercaImplegnoPlur_rowNum - 1}].importoImpPluriennaleString"  maxlength="10"></s:textfield></display:column>
		             
		             
		             <!-- nuova  visualizzazione data  -->      
		             <display:column title="Data Scadenza" ><s:textfield maxlength="10" id="dataScadenzaImpPluriennale" cssClass="input-small datepicker" name="step3Model.listaImpegniPluriennali[%{#attr.ricercaImplegnoPlur_rowNum - 1}].dataScadenzaImpPluriennaleString"></s:textfield></display:column>
		             
		             <!-- Componente bilancio SIAC-7349 -->
		              <s:if test="componenteBilancioCapitoloAttivo">
		              <display:column title="Componente" >
		              	<s:select list="step3Model.tipoComponenteImportiCapitolo"
							id="componenteImpPluriennale%{#attr.ricercaImplegnoPlur_rowNum}"
							cssClass="parametroRicercaCapitolo"
							headerKey="" headerValue=""
							name="step3Model.listaImpegniPluriennali[%{#attr.ricercaImplegnoPlur_rowNum - 1}].componenteImpPluriennale"
						    disabled="false" 
							listKey="uid" listValue="descrizione" />
		              	
		              </display:column>
		             </s:if>
		             
		             <display:column title="" class="tab_Right">
			             <div class="btn-group">
				             <%-- task-131 <s:url id="pulisci" method="pulisciCampoPluriennale"> --%>
						     <s:url var="pulisci" action="inserisce%{#oggetto}Step3_pulisciCampoPluriennale">
						     
						     	<s:param name="campoPerPulire" value="%{#attr.ricercaImplegnoPlur.annoImpPluriennale}" />		        	
				             </s:url>
						     <a class="btn dropdown-toggle" href="<s:property value="%{pulisci}"/>">Pulisci</a>
			             </div>
		             </display:column>				 
				 </display:table>
              </div>
            </div>
            <p class="margin-large">
            
             <s:include value="/jsp/movgest/include/modal.jsp" />
             
             <s:include value="/jsp/movgest/include/modalSalvaPrimeNote.jsp"/>
            
            
            <!-- CON LA NUOVA GESTIONE DI SALVA E CONFERMA E PROSEGUI
                 IL TASTO INDIETRO E' FUNZIONALMENTE ERRATO E NON 
                 GESTITO A LIVELLO DI ANALISI            
             -->
<%--             	<s:include value="/jsp/include/indietro.jsp" /> --%>
            	<!-- task-131 <s:submit method="annulla" value="annulla" cssClass="btn"/> -->
            	<s:submit action="inserisce%{#oggetto}Step3_annulla" value="annulla" cssClass="btn"/>
						   
            	
            	
            	<s:if test="salvaConConfermaPrimaNota()"> 
					<div class="btn btn-primary">
						<a id="linkSalvaConConfermaPrimaNota" href="#msgConfermaPrimeNote" data-toggle="modal" class="linkConfermaPrimeNote">
						salva</a> 
					</div>
				</s:if>
				<s:else>
					<!-- task-131 <s:submit method="salvaPluriennale" value="salva" id="salvaPluriennareId" cssClass="btn btn-primary pull-right freezePagina"/> -->
					<s:submit action="inserisce%{#oggetto}Step3_salvaPluriennale" value="salva" id="salvaPluriennareId" cssClass="btn btn-primary pull-right freezePagina"/>
				</s:else>
            	
            	
            	<a id="linkMsgDatipersi" href="#msgControlloProseguiPlurAcc" data-toggle="modal" style="display:none;"></a>
            </p>  
          </s:form>
        </div>
      </div>	 
    </div>	


<script type="text/javascript">

	<s:if test="step3Model.checkproseguiPlurAcc">
		$("#linkMsgDatipersi").click();
	
	</s:if>
</script>	

<s:include value="/jsp/include/footer.jsp" />
 