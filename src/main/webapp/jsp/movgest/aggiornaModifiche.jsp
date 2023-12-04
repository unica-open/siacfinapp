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


<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">
 <!--  <h3>Impegno ${model.movimentoSpesaModel.impegno.annoMovimento}/${model.movimentoSpesaModel.impegno.numero} - ${model.movimentoSpesaModel.impegno.descrizione} - ${model.movimentoSpesaModel.impegno.importoAttuale}</h3>	-->

		<h3><s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
	  <%-- SIAC-7952 rimuovo .do dalla action --%>    
      <s:form id="mainForm" method="post" action="aggiornaMovSpesa" cssClass="form-horizontal">
      	<s:include value="/jsp/movgest/include/tabModificaSpesa.jsp" />		
		<s:set var="gestisciForwardAction" value="%{'aggiornaMovSpesa_gestisciForward'}" />	                     		 
		<s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
		
		<%-- SIAC-7630 --%>
		<s:include value="/jsp/include/actionMessagesErrors.jsp" />
<%--         <s:if test="hasActionErrors()">
		Messaggio di ERROR
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
					<ul>
						<s:actionerror />
					</ul>
			</div>
		</s:if>
		<s:if test="hasActionMessages()">
		Messaggio di WARNING
			<div class="alert alert-success">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<ul>
					<s:actionmessage />
				</ul>
			</div>
		</s:if>  
		
		<s:if test="hasActionWarnings()">
			Messaggio di WARNING
			<div class="alert alert-warning">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
				   <s:iterator value="actionWarnings">
				       <s:property escapeHtml="false"/><br>
				   </s:iterator>
					
				</ul>
			</div>
		</s:if>  --%>  
        <h4>Aggiornamento modifica</h4> 
        <div class="step-content">
          <div class="step-pane active" id="step1">
           
                   <h4>Modifica ${model.movimentoSpesaModel.modificaAggiornamento.numeroModificaMovimentoGestione}
                   <s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.numeroSubImpegno != null}">
                    	- SubImpegno - ${model.movimentoSpesaModel.modificaAggiornamento.numeroSubImpegno}
                    </s:if>
                    <s:else>
                    	- Impegno
                    </s:else>
                    </h4>
                  <fieldset class="form-horizontal ">                    
                    <div class="control-group">              
                      <div class="controls">              
                        <dl class="dl-horizontal">
										
							<s:if test="%{movimentoSpesaModel.modificaAggiornamento.importoOld == null}">
                				<s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.soggettoNewMovimentoGestione != null}">
	                       			<dt>Soggetto attuale</dt>
	                          		<dd>
	                          		${model.movimentoSpesaModel.modificaAggiornamento.soggettoNewMovimentoGestione.codiceSoggetto} - 
	                          		${model.movimentoSpesaModel.modificaAggiornamento.soggettoNewMovimentoGestione.denominazione} - 
	                          		CF: ${model.movimentoSpesaModel.modificaAggiornamento.soggettoNewMovimentoGestione.codiceFiscale} - 
	                          		PIVA: ${model.movimentoSpesaModel.modificaAggiornamento.soggettoNewMovimentoGestione.partitaIva}
	                          		</dd> 
	                       		</s:if>
	                       		
	                       		<s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoNewMovimentoGestione != null}">
	                       			<dt>Classe attuale</dt>
	                          		<dd>
	                          		${model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoNewMovimentoGestione.codice} - 
	                          		${model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoNewMovimentoGestione.descrizione}
	                          		</dd> 
	                       		</s:if>
	                        				                        	
	                       		<s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione != null && model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoOldMovimentoGestione != null}">
	                       			<%-- casistica C->S --%>
	                       			<s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.soggettoNewMovimentoGestione == null}">
		                       			<dt>Soggetto precedente</dt>
		                          		<dd>
		                          		${model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione.codiceSoggetto} - 
		                          		${model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione.denominazione} 
		                          		</dd>
		                          	</s:if>
		                          	<%-- casistica S->C --%>
		                          	<s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.soggettoNewMovimentoGestione != null}">
		                          		<dt>Classe precedente</dt>
		                          		<dd>
		                          		${model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoOldMovimentoGestione.codice} - 
		                          		${model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoOldMovimentoGestione.descrizione}
		                          		</dd> 
		                          	</s:if>	 
	                       		</s:if>
	                       		<s:else>
	                       			<%-- casistica S->S --%>
	                       			<s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione != null}">
	                       				<dt>Soggetto precedente</dt>
		                          		<dd>
		                          		${model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione.codiceSoggetto} - 
		                          		${model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione.denominazione} - 
		                          		CF: ${model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione.codiceFiscale} - 
		                          		PIVA: ${model.movimentoSpesaModel.modificaAggiornamento.soggettoOldMovimentoGestione.partitaIva}
		                          		</dd>
	                       			</s:if>
	                       			<s:else>
	                       				<dt>Classe precedente</dt>
		                          		<dd>
		                          		${model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoOldMovimentoGestione.codice} - 
		                          		${model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoOldMovimentoGestione.descrizione}
		                          		</dd> 
	                       			</s:else>
	                       		</s:else>
	            			</s:if>
				                       						                        		
                       		<s:if test="%{movimentoSpesaModel.modificaAggiornamento.importoOld != null}">
                       		    <dt>Importo</dt>
                          		<dd><s:property value="getText('struts.money.format', {movimentoSpesaModel.modificaAggiornamento.importoOld})" /></dd>
                          		
                          		<dt>Riduzione e contestuale impegno: </dt>
                          		<dd>
	                          		<s:if test="%{movimentoSpesaModel.modificaAggiornamento.flagAggiudicazione}">
	                          		S&igrave;
	                          		</s:if><s:else>
	                          			No
	                          		</s:else>
                          		</dd>
                          		<s:if test="%{movimentoSpesaModel.modificaAggiornamento.flagAggiudicazione}">
	                          		<dt>Soggetto riduzione</dt>
		                          		<dd>
		                          		<s:if test="%{movimentoSpesaModel.modificaAggiornamento.soggettoAggiudicazione != null}">
			                          		${model.movimentoSpesaModel.modificaAggiornamento.soggettoAggiudicazione.codiceSoggetto} - 
			                          		${model.movimentoSpesaModel.modificaAggiornamento.soggettoAggiudicazione.denominazione}
		                          		</s:if><s:else>&nbsp;</s:else>
		                          		</dd> 
	                          		<dt>Classe riduzione</dt>
	                          			<s:if test="%{movimentoSpesaModel.modificaAggiornamento.classeSoggettoAggiudicazione != null}">
		                          		<dd>
		                          		${model.movimentoSpesaModel.modificaAggiornamento.classeSoggettoAggiudicazione.descrizione}
		                          		</dd>
		                          		</s:if><s:else>&nbsp;</s:else>
		                          	<dt>Senza soggetto</dt>
		                          		<dd>
		                          		<s:if test="%{movimentoSpesaModel.modificaAggiornamento.flagAggiudicazioneSenzaSoggetto}"> 
		                          			S&igrave;
		                          			</s:if><s:else>No</s:else>
		                          		</dd> 
                          		</s:if>
                          		<s:if test="%{movimentoSpesaModel.modificaAggiornamento.importoOld lt 0}">
<%-- 	                          	<s:if test="%{model.movimentoSpesaModel.modificaAggiornamento.numeroSubImpegno == null}"> --%>
                          			<div class="control-group">    
				                      <label class="control-label" for="reimputazione">Reimputazione </label>
				                      <div class="controls">
				                      <%-- SIAC-6997: [...radioReimputato...] Non può essere deselezionato se Elaborato ROR REANNO è true --%>
				                      	<s:if test="%{model.movimentoSpesaModel.elaboratoRor == 'Si'}">
									      <s:radio id="radioReimputato" disabled="true" name="model.movimentoSpesaModel.reimputazione" cssClass="flagResidenza" disabled="%{movimentoSpesaModel.modificaAggiornamento.flagAggiudicazione}" list="model.movimentoSpesaModel.daReimputazione"></s:radio>
										</s:if>
										<s:else>
										<!-- CONTABILIA-260 - Bug fix per test non segnalato nel documento ma da test in corso - SIAC-7349 Inizio CM 08/07/2020 -->
									      <s:radio id="radioReimputato" disabled="true" name="model.movimentoSpesaModel.reimputazione" cssClass="flagResidenza" disabled="%{movimentoSpesaModel.modificaAggiornamento.flagAggiudicazione}" list="model.movimentoSpesaModel.daReimputazione"></s:radio>
										<!-- CONTABILIA-260 - Bug fix per test non segnalato nel documento ma da test in corso - SIAC-7349 Fine CM 08/07/2020 -->
										</s:else>
										  <span class="riaccVisible" id="bloccoReimputato">
									      &nbsp; Anno reimputazione:
									      &nbsp; <s:textfield id="annoReimp" name="model.movimentoSpesaModel.annoReimputazione" onkeyup="return checkItNumbersOnly(event)" cssClass="span1 soloNumeri" />&nbsp;
									      &nbsp; Elaborato ROR - Reimp. in corso d&lsquo;anno
									      &nbsp; <s:radio id="radioElaboratoRor" disabled="true" name="model.movimentoSpesaModel.elaboratoRor" cssClass="flagResidenza" list="model.movimentoSpesaModel.daElaboratoRor"></s:radio>
								         </span>
				                      </div>
				                   	</div> 
<%-- 	                          	</s:if> --%>
                          		</s:if>
                          		
                          		
                       		</s:if>
                       		
                       		
                       		
                       		
                       		
                        </dl>             
                      </div>
                    </div>       
                    <div class="control-group">
                    	<label class="control-label" for="Motivo">Modifica motivo *</label>
                    	<s:if test="%{movimentoSpesaModel.modificaAggiornamento.flagAggiudicazione}">
	                      <div class="controls">
	                         <s:select list="model.movimentoSpesaModel.listaTipoMotivoAggiudicazione" id="listaTipoMotivo" name="motivo" headerKey="" 
	          		   				headerValue="" listKey="codice" listValue="descrizione"  disabled="true" cssClass="span9"/>
	                      </div>
                    	
                    	</s:if><s:else>
                    		 <s:select list="model.movimentoSpesaModel.listaTipoMotivo" id="listaTipoMotivoAgg" name="motivo" headerKey="" 
	          		   				headerValue="" listKey="codice" listValue="descrizione" cssClass="span9"/>
                    	</s:else>
                      
                    </div>
                    <div class="control-group">
                      <label class="control-label" for="Descrizione">Modifica  descrizione <span class="hide" id="modificaDescrizioneObbligatoria"> * </span></label>
                      <div class="controls">
                      	<%-- SIAC-8506 --%>
                        <s:textfield name="descrizione" id="descrizione" cssClass="span9" maxlength="500" />  
                      </div>
                    </div>  
                  </fieldset>
                  
                  <s:set var="consultaModificheProvvedimentoAction" value="%{'aggiornaMovSpesa_consultaModificheProvvedimento'}" />
				  <s:set var="consultaModificheProvvedimentoSubAction" value="%{'aggiornaMovSpesa_consultaModificheProvvedimentoSub'}" />
	
                  <%--p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p--%>			          
<%--                   <s:include value="/jsp/movgest/include/provvedimentoAggiorna.jsp" />  --%>
					<s:if test="%{movimentoSpesaModel.modificaAggiornamento.flagAggiudicazione}">
						<h4>
							Provvedimento: <s:property value="step1Model.provvedimento.annoProvvedimento"/>  / <s:property value="step1Model.provvedimento.numeroProvvedimento"/> 
							- <s:property value="step1Model.provvedimento.tipoProvvedimento"/> - <s:property value="step1Model.provvedimento.oggetto"/>
							- <s:property value="step1Model.provvedimento.CodiceStrutturaAmministrativa"/> - <s:property value="step1Model.provvedimento.strutturaAmministrativa"/> - Stato: <s:property value="step1Model.provvedimento.stato"/>
						</h4>
					</s:if><s:else>
						<s:include value="/jsp/movgest/include/provvedimento.jsp" />
					</s:else>
                  
                  <!--  per provvedimento  -->
                  <s:set var="selezionaProvvedimentoAction" value="%{'aggiornaMovSpesa_selezionaProvvedimento'}" />       
                  <s:set var="clearRicercaProvvedimentoAction" value="%{'aggiornaMovSpesa_clearRicercaProvvedimento'}" />	          
            	  <s:set var="ricercaProvvedimentoAction" value="%{'aggiornaMovSpesa_ricercaProvvedimento'}" />	                      		
            	  <s:set var="eliminaAction" value="%{'aggiornaMovSpesa_elimina'}" />	              
                  <s:set var="selezionaProvvedimentoInseritoAction" value="%{'aggiornaMovSpesa_selezionaProvvedimentoInserito'}" />	
				  <s:set var="inserisciProvvedimentoAction" value="%{'aggiornaMovSpesa_inserisciProvvedimento'}" />	
				  <s:set var="clearInserimentoProvvedimentoAction" value="%{'aggiornaMovSpesa_clearInserimentoProvvedimento'}" />
	          
				 <s:set var="gestisciForwardAction" value="%{'aggiornaMovSpesa_gestisciForward'}" />	 
		 		 <s:set var="siSalvaAction" value="%{'aggiornaMovSpesa_siSalva'}" />	 
		  		 <s:set var="siProseguiAction" value="%{'aggiornaMovSpesa_siProsegui'}" />	
				 <s:set var="annullaSubImpegnoAction" value="%{'aggiornaMovSpesa_annullaSubImpegno'}" />	 
				 <s:set var="annullaSubAccertamentoAction" value="%{'aggiornaMovSpesa_annullaSubAccertamento'}" />	 
				 <s:set var="annullaMovGestSpesaAction" value="%{'aggiornaMovSpesa_annullaMovGestSpesa'}" />	 
				 <s:set var="eliminaSubImpegnoAction" value="%{'aggiornaMovSpesa_eliminaSubImpegno'}" />	 
				 <s:set var="eliminaSubAccertamentoAction" value="%{'aggiornaMovSpesa_eliminaSubAccertamento'}" />
				 <s:set var="forzaProseguiAction" value="%{'aggiornaMovSpesa_forzaProsegui'}" />	          
				 <s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'aggiornaMovSpesa_forzaSalvaPluriennaleAccertamento'}" />	          
			 	 <s:set var="salvaConByPassDodicesimiAction" value="%{'aggiornaMovSpesa_salvaConByPassDodicesimi'}" />	          
	
                  <s:include value="/jsp/movgest/include/modal.jsp" /> 
                                   
            <br/> <br/>                                         
            <p>
            	<s:include value="/jsp/include/indietro.jsp" /> 
               <!-- task-131 <s:submit method="annulla" value="annulla" cssClass="btn btn-secondary" id="annullaBtn"></s:submit> -->
               <s:submit action="%{'aggiornaMovSpesa_annulla'}" value="annulla" cssClass="btn btn-secondary" id="annullaBtn"></s:submit>
               <span class="pull-right">
               <!-- task-131 <s:submit method="salva" value="Salva" cssClass="btn btn-primary" ></s:submit> -->
               <s:submit action="%{'aggiornaMovSpesa_salva'}" value="Salva" cssClass="btn btn-primary" id="salvaBtn"></s:submit> 
              
            </span> </p>
          </div>
        </div>
        
      </s:form>
    </div>
  </div>	 
</div>	

	<script type="text/javascript" src="${jspath}movgest/aggiornaModifiche.js"></script>

<s:include value="/jsp/include/footer.jsp" />