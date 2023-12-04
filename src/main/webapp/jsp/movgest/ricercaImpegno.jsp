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
  
<div class="container-fluid-banner">

<a name="A-contenuti" title="A-contenuti"></a>
</div>

<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->

<s:set var="gestisciForwardAction" value="%{'ricercaImpegno_gestisciForward'}" />
<s:set var="siSalvaAction" value="%{'ricercaImpegno_siSalva'}" />	 
<s:set var="siProseguiAction" value="%{'ricercaImpegno_siProsegui'}" />	
<s:set var="annullaSubImpegnoAction" value="%{'ricercaImpegno_annullaSubImpegno'}" />	 
<s:set var="annullaSubAccertamentoAction" value="%{'ricercaImpegno_annullaSubAccertamento'}" />	 
<s:set var="annullaMovGestSpesaAction" value="%{'ricercaImpegno_annullaMovGestSpesa'}" />	 
<s:set var="eliminaSubImpegnoAction" value="%{'ricercaImpegno_eliminaSubImpegno'}" />	 
<s:set var="eliminaSubAccertamentoAction" value="%{'ricercaImpegno_eliminaSubAccertamento'}" />
<s:set var="forzaProseguiAction" value="%{'ricercaImpegno_forzaProsegui'}" />	          
<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'ricercaImpegno_forzaSalvaPluriennaleAccertamento'}" />	          
<s:set var="salvaConByPassDodicesimiAction" value="%{'ricercaImpegno_salvaConByPassDodicesimi'}" />	          
	
 

<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 ">  
      <div class="contentPage"> 
        <s:form method="post" action="ricercaImpegno" id="ricercaImpegno">
            <s:include value="/jsp/include/actionMessagesErrors.jsp" />
          
          <h3>Ricerca Impegno
          <s:if test="%{ricercaTipoROR == true}">
              	ROR
          </s:if> 
          </h3>
          <p>&Egrave; necessario inserire almeno un criterio di ricerca.</p>  
         
         <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
         
					<s:hidden id="idPianoDeiContiCapitolo" name="teSupport.pianoDeiConti.uid"/>
					<s:hidden id="codicePianoDeiContiCapitolo" name="teSupport.pianoDeiConti.codice"/>
					<s:hidden id="descrizionePianoDeiContiCapitolo" name="teSupport.pianoDeiConti.descrizione"/>
					<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
					<%-- CR-2023 da eliminare 
					<s:hidden id="ricaricaAlberoContoEconomico" name="teSupport.ricaricaAlberoContoEconomico"/> 
					--%>
					<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
					<s:hidden id="ricaricaSiopeSpesa" name="teSupport.ricaricaSiopeSpesa"/>

					<s:hidden id="daRicerca"
						name="step1Model.daRicerca" />				
         <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
          
          <div class="step-content">
         <div class="step-pane active" id="step1"> <br>
          <p>
          	<!-- task-131 <s:submit name="cerca" value="cerca" method="ricercaImpegni" cssClass="btn btn-primary pull-right" /> -->
          	<s:submit name="cerca" value="cerca" action="ricercaImpegno_ricercaImpegni" cssClass="btn btn-primary pull-right" />         	
          </p><br>
           <h4>Impegno</h4>
          
          <fieldset class="form-horizontal">  
            <div class="control-group">
              <label class="control-label" for="annoEsercizio">Anno</label>
              <div class="controls">  
                <s:textfield id="annoMovimento" onkeyup="return checkItNumbersOnly(event)" name="model.ricercaModel.annoMovimento" cssClass="span1" />
                <span class="al">
                  <label class="radio inline" onkeyup="return checkItNumbersOnly(event)" for="model.ricercaModel.numeroImpegno">Numero </label>
                </span>
                <s:textfield id="numeroImpegno" onkeyup="return checkItNumbersOnly(event)" name="model.ricercaModel.numeroImpegno" cssClass="lbTextSmall span1"/> 
                <span class="al">
                  <label class="radio inline" for="stato">Stato</label>
                </span>
                <s:select list="listaStatoOperativoMovgest" id="listaStatoOperativoMovgest"  headerKey="" 
          		   headerValue="" name="model.ricercaModel.idStatoOperativoMovgest" cssClass="span2"  
       	 	       listKey="codice" listValue="descrizione" /> 
              </div>
            </div>
            
            <div class="control-group">
				<div class="control-label">Escludi annullati</div>
				<div class="controls">
						<!-- SIAC-8157 rimosso onclick="impostaValoreEscludiAnnullati()" -->
   						<s:checkbox id="escludiAnnullatiCheckBox" name="model.ricercaModel.escludiAnnullati" />  		   
				</div>
			</div>
            
	      <%-- <div class="control-group">
	      <label for="pdc" class="control-label"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario </label>
          <div class="controls">
            
           <s:property value="teSupport.pianoDeiConti.descrizione"/>&nbsp;&nbsp;&nbsp;<a href="#myModal" role="button" class="btn btn-primary" data-toggle="modal">seleziona il Piano dei Conti</a>
            
            <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                <h3 id="myModalLabel">Piano dei Conti</h3>
              </div> --%>
<!--               <div class="modal-body"> -->
<!--                 <ul id="elementoPdcRicercaImpegno" class="ztree"></ul> -->
<!--               </div> -->
              
              
               <!-- ALBERO VISUALIZZATO -->
				<!-- <div class="modal-body" id="elementoPdcRicercaImpegnoDiv">
					<ul id="elementoPdcRicercaImpegno" class="ztree"></ul>
				</div> -->
				
				 <!-- ALBERO IN ATTESA -->
				<%-- <div class="modal-body" id="elementoPdcRicercaImpegnoWait">
					
					    Attendere prego..
	
					
				</div>
              
              
              <div class="modal-footer">
              		<s:submit value="conferma" name="conferma" method="confermaPdc" cssClass="btn btn-primary" data-dismiss="modal" aria-hidden="true"></s:submit>
              </div>              
            </div>
            
          </div>
        </div> --%>
            <div class="control-group">
             <label class="control-label" for="piano">Competenza</label>
             	<div class="controls">					        
             	
                	<div class="radio inline">
			    		<s:radio id="competenze" cssClass="flagSesso" name="model.ricercaModel.competenze" list="competenzeList"/>
                	</div>
             				                                     
             </div>
            </div>
            <div class="control-group">
              <label class="control-label" for="cup">CUP</label>
              <div class="controls">					
              	<s:textfield name="model.ricercaModel.cup" id="cup" cssClass="lbTextSmall span2" />        
                <span class="cig">
                <label class="radio inline" for="cig">CIG</label>  
                </span>
                <s:textfield name="model.ricercaModel.cig" id="cig" cssClass="lbTextSmall span2" />      
                
                <!--  
                <span class="cig">
                	<label class="radio inline" for="progetto">Progetto</label>  
                </span>
                <s:textfield name="model.ricercaModel.progetto" id="progetto" cssClass="lbTextSmall span2" />    
                -->
                
                                  
              </div>
            </div>
            
            <!-- SIAC-7501 -->
           	<div class="control-group">
		  		<table class="span10 ">
		  			<tr>
		  				<td class="span4 ">
		  					<span class="control-label">Da riaccertamento</span>
		  					<div class="controls">    
					      		<s:radio id="riaccertato" name="step1Model.riaccertato" cssClass="flagResidenza" list="step1Model.daRiaccertamento" onclick="check(this.value)"></s:radio> 
					    	</div>
		  				</td>
		  				<td rowspan="3" valign="middle" width="90%" class="span8 ">
		  					<span class="riaccVisible" id="bloccoRiaccertato">
	      						&nbsp; 
	      						<s:textfield onkeyup="return checkItNumbersOnly(event)" id="annoImpRiacc" name="model.ricercaModel.annoImpRiacc" cssClass="span2 " title="Anno" />&nbsp;
	      						<s:textfield onkeyup="return checkItNumbersOnly(event)" id="numImpRiacc" cssClass="lbTextSmall span4 " title="Numero" name="model.ricercaModel.numeroImpRiacc"/>
	     			 		</span>
		  				</td>			
		  			</tr>
		  			<tr><td>&nbsp;</td></tr>
		  			<!-- inizio SIAC-6997 -->
		  			<tr>
		  				<td class="span4 ">
		  					<span class="control-label">Da reimputazione in corso d&rsquo;anno</span>
		  					<div class="controls">
					      		<s:radio id="reanno" name="step1Model.reanno" cssClass="flagResidenza" list="step1Model.daReanno" onclick="check(this.value)"></s:radio>
					    	</div>
		  				</td>
		  			</tr>
		  		</table>
		  	</div>
           	<!-- div class="control-group">
   				<span class="control-label">Da riaccertamento</span>
   				<div class="controls">    
 						<s:radio id="riaccertato" name="step1Model.riaccertato" cssClass="flagResidenza" list="step1Model.daRiaccertamento" onclick="check(this.value)"></s:radio> 
      				<span class="riaccVisible" id="bloccoRiaccertato">
     						&nbsp; 
     						<s:textfield onkeyup="return checkItNumbersOnly(event)" id="annoImpRiacc" name="model.ricercaModel.annoImpRiacc" cssClass="span1 " title="Anno" />&nbsp;
     						<s:textfield onkeyup="return checkItNumbersOnly(event)" id="numImpRiacc" cssClass="lbTextSmall span2 " title="Numero" name="model.ricercaModel.numeroImpRiacc"/>
    			 		</span>
  			 		</div>
 				</div>
 				<div class="control-group">
	    		<span class="control-label">Da reimputazione in corso d&rsquo;anno</span>
	    		<div class="controls">    
	        		<s:radio id="reanno" name="step1Model.reanno" cssClass="flagResidenza" list="step1Model.daReanno" onclick="check(this.value)"></s:radio>
	    		</div>
	  		</div-->
  			
  			
  			
  				<div class="control-group">      
				          <label class="control-label">Struttura Compentente</label>
				          <div class="controls">   
				            <s:hidden name="step1Model.strutturaSelezionataCompetente" id="strutturaSelezionataCompetente" />
				                           
				            <div class="accordion span9" class="struttAmmCompetente">
				              <div class="accordion-group">
				                <div class="accordion-heading">    
				                  <a class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmmCompetente" href="#4n">
				                 Seleziona la Struttura competente
				                  <i class="icon-spin icon-refresh spinner" id="spinnerStrutturaAmministrativaCompetente"></i></a>
				                </div>
				                <div id="4n" class="accordion-body collapse">
				                  <div class="accordion-inner" id="strutturaAmministrativaCompetenteDiv">
				                    <ul id="strutturaAmministrativaCompetente" class="ztree treeStruttAmm"></ul>
				                  </div>
				                  <div class="accordion-inner" id="strutturaAmministrativaCompetenteWait">
				                    Attendere prego..
				                  </div>
				                  
				                </div>
				              </div>
				            </div>
				          </div>
				        </div>
  			
  			
            <div class="control-group">
            	<label class="control-label" >Impegno di origine</label>
              	<div class="controls">	
              	<s:textfield id="annoImpOrigine" onkeyup="return checkItNumbersOnly(event)" name="model.ricercaModel.annoImpOrigine" maxlength="4" cssClass="span1" title="Anno"/>	
              	<s:textfield id="numeroImpOrigine" onkeyup="return checkItNumbersOnly(event)" name="model.ricercaModel.numeroImpOrigine" cssClass="lbTextSmall span2" title="Numero"/>	
              				        
              	</div>
            </div>
            
            <!-- PROGETTO -->
   			<s:include value="/jsp/movgest/include/progetto.jsp" />
            
            
            <div id="refreshHeaderCapitolo">
            	<s:include value="/jsp/movgest/include/headerCapitolo.jsp"/>
            </div>
            <!-- SIAC-7349 -->
<%--             <s:include value="/jsp/movgest/include/capitolo.jsp" /> --%>
            <s:if test="componenteBilancioCapitoloAttivo">
				 <s:include value="/jsp/movgest/include/capitoloComponentiBilancio.jsp" /> 
			</s:if>
			<s:else>
				 <s:include value="/jsp/movgest/include/capitolo.jsp" />
			</s:else>
            
            <s:set var="consultaModificheProvvedimentoAction" value="%{'ricercaImpegno_consultaModificheProvvedimento'}" />
			<s:set var="consultaModificheProvvedimentoSubAction" value="%{'ricercaImpegno_consultaModificheProvvedimentoSub'}" />
           
            <s:include value="/jsp/movgest/include/provvedimento.jsp" />
              
            <s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden> 
            
            <s:hidden id="hiddenPerEscludiAnnullati" name="model.ricercaModel.hiddenPerEscludiAnnullati" value="false" />
            
            <div id="refreshHeaderSoggetto">
            	<s:include value="/jsp/movgest/include/headerSoggetto.jsp"/>
            </div>
            <s:include value="/jsp/movgest/include/soggetto.jsp" />  
			
			<!--per modale provvedimento e elimina (incluse in modal.jsp) -->
		    <s:set var="selezionaProvvedimentoAction" value="%{'ricercaImpegno_selezionaProvvedimento'}" />
			<s:set var="clearRicercaProvvedimentoAction" value="%{'ricercaImpegno_clearRicercaProvvedimento'}" />	          
       		<s:set var="ricercaProvvedimentoAction" value="%{'ricercaImpegno_ricercaProvvedimento'}" />	          
       		<s:set var="eliminaAction" value="%{'ricercaImpegno_elimina'}" />	  
                  
              
            <!--modale progetto -->
            <s:set var="selezionaProgettoCronopAction" value="%{'ricercaImpegno_selezionaProgettoCronop'}" />	          
            <s:set var="selezionaProgettoAction" value="%{'ricercaImpegno_selezionaProgetto'}" />		
            <s:set var="pulisciRicercaProgettoAction" value="%{'ricercaImpegno_pulisciRicercaProgetto'}" />	          
            <s:set var="ricercaProgettoAction" value="%{'ricercaImpegno_ricercaProgetto'}" />	          
            <s:set var="codiceProgettoChangedAction" value="%{'ricercaImpegno_codiceProgettoChanged'}" /> 
	                
	        <s:set var="ricercaCapitoloAction" value="%{'ricercaImpegno_ricercaCapitolo'}" />
	        <s:set var="pulisciRicercaCapitoloAction" value="%{'ricercaImpegno_pulisciRicercaCapitolo'}" />
	        <s:set var="selezionaCapitoloAction" value="%{'ricercaImpegno_selezionaCapitolo'}" />
	        <s:set var="visualizzaDettaglioCapitoloAction" value="%{'ricercaImpegno_visualizzaDettaglioCapitolo'}" />
	        
	       
	        <!--  per soggetto -->
			<s:set var="selezionaSoggettoAction" value="%{'ricercaImpegno_selezionaSoggetto'}" />
			<s:set var="pulisciRicercaSoggettoAction" value="%{'ricercaImpegno_pulisciRicercaSoggetto'}" />	          
			<s:set var="ricercaSoggettoAction" value="%{'ricercaImpegno_ricercaSoggetto'}" />	    
			<s:set var="listaClasseSoggettoChangedAction" value="%{'ricercaImpegno_listaClasseSoggettoChanged'}" />
	                    
            <s:include value="/jsp/movgest/include/modal.jsp" /> 
          
			<s:include value="/jsp/movgest/include/modalProgettoCronop.jsp"/>	
			<!--/modale progetto -->
           
				
            <!-- Fine Modal -->
            
            
            
            <br/> <br/> 
            
            <p>           
            
            <s:include value="/jsp/include/indietro.jsp" /> 

            <!-- task-131  <s:submit name="annulla" value="annulla" method="annulla" cssClass="btn btn-secondary" /> -->
             <s:submit name="annulla" value="annulla" action="ricercaImpegno_annulla" cssClass="btn btn-secondary" />          
            <!-- task-131 <s:submit id="cerca" name="cerca" value="cerca" method="ricercaImpegni" cssClass="btn btn-primary pull-right" /> -->
          	<s:submit id="cerca" name="cerca" value="cerca" action="ricercaImpegno_ricercaImpegni" cssClass="btn btn-primary pull-right" />         	 
             </p> 
            
                  
          </fieldset>  
       	  </div>
       	  </div>
           
        </s:form>
      </div>	
    </div>	
  </div>	 
</div>	


<script src="/siacfinapp/js/local/movgest/ricercaImpegno.js" type="text/javascript"></script>


<s:include value="/jsp/include/footer.jsp" />