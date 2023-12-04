<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
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
	<%--corpo pagina--%>
	<%--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> --%>


	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
		<%-- 	<h3>
					<s:property value="%{labels.OGGETTO_GENERICO_PADRE}" />
					${model.step1Model.annoImpegno}/ ${model.step1Model.numeroImpegno}
					- ${model.step1Model.oggettoImpegno} -
					<s:property
						value="getText('struts.money.format', {step1Model.importoImpegno})" />
				</h3>	--%>
				
				<h3><s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
	  			<%-- SIAC-7952 rimuovo .do dalla action --%>    				
				<s:form id="mainForm" method="post" action="aggiornaMovSpesaAcc"
					cssClass="form-horizontal">
					<s:include value="/jsp/movgest/include/tabModificaSpesa.jsp" />
					
					<s:set var="gestisciForwardAction" value="%{'aggiornaMovSpesaAcc_gestisciForward'}" />	                     		 
					<s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />

					<%-- SIAC-7630 --%>
					<s:include value="/jsp/include/actionMessagesErrors.jsp" />
<%-- 					<s:if test="hasActionErrors()">
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
					</s:if> --%>
					<h4>Aggiornamento modifica</h4>
					<div class="step-content">
						<div class="step-pane active" id="step1">

		                   <h4>Modifica ${model.movimentoSpesaModel.modificaAggiornamentoAcc.numeroModificaMovimentoGestione}
		                   <s:if test="%{model.movimentoSpesaModel.modificaAggiornamentoAcc.numeroSubAccertamento != null}">
		                    	- SubAccertamento - ${model.movimentoSpesaModel.modificaAggiornamentoAcc.numeroSubAccertamento}
		                    </s:if>
		                    <s:else>
		                    	- Accertamento
		                    </s:else>
		                    </h4>
							<fieldset class="form-horizontal ">
								<div class="control-group">
									<div class="controls">
										<dl class="dl-horizontal">
											
											<s:if test="%{movimentoSpesaModel.modificaAggiornamentoAcc.importoOld == null}">
					                       		<s:if test="%{model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoNewMovimentoGestione != null}">
					                       			<dt>Soggetto attuale</dt>
					                          		<dd>
					                          		${model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoNewMovimentoGestione.codiceSoggetto} - 
					                          		${model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoNewMovimentoGestione.denominazione} - 
					                          		CF: ${model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoNewMovimentoGestione.codiceFiscale} - 
					                          		PIVA: ${model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoNewMovimentoGestione.partitaIva}
					                          		</dd> 
					                       		</s:if>
					                       		
					                       		<s:if test="%{model.movimentoSpesaModel.modificaAggiornamentoAcc.classeSoggettoNewMovimentoGestione != null}">
					                       			<dt>Classe attuale</dt>
					                          		<dd>
					                          		${model.movimentoSpesaModel.modificaAggiornamentoAcc.classeSoggettoNewMovimentoGestione.codice} - 
					                          		${model.movimentoSpesaModel.modificaAggiornamentoAcc.classeSoggettoNewMovimentoGestione.descrizione}
					                          		</dd> 
					                       		</s:if>
					                        				                        	
					                       		<s:if test="%{model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoOldMovimentoGestione != null && model.movimentoSpesaModel.modificaAggiornamentoAcc.classeSoggettoOldMovimentoGestione != null}">
					                       			<%-- casistica C->S --%>
					                       			<s:if test="%{model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoNewMovimentoGestione == null}">
						                       			<dt>Soggetto precedente</dt>
						                          		<dd>
						                          		${model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoOldMovimentoGestione.codiceSoggetto} - 
						                          		${model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoOldMovimentoGestione.denominazione} - 
						                          		CF: ${model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoOldMovimentoGestione.codiceFiscale} - 
						                          		PIVA: ${model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoOldMovimentoGestione.partitaIva}
						                          		</dd>
						                          	</s:if>
						                          	<%-- casistica S->C --%>
						                          	<s:if test="%{model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoNewMovimentoGestione != null}">
						                          		<dt>Classe precedente</dt>
						                          		<dd>
						                          		${model.movimentoSpesaModel.modificaAggiornamentoAcc.classeSoggettoOldMovimentoGestione.codice} - 
						                          		${model.movimentoSpesaModel.modificaAggiornamentoAcc.classeSoggettoOldMovimentoGestione.descrizione}
						                          		</dd> 
						                          	</s:if>	 
					                       		</s:if>
					                       		<s:else>
					                       			<%-- casistica S->S --%>
					                       			<s:if test="%{model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoOldMovimentoGestione != null}">
					                       				<dt>Soggetto precedente</dt>
						                          		<dd>
						                          		${model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoOldMovimentoGestione.codiceSoggetto} - 
						                          		${model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoOldMovimentoGestione.denominazione} - 
						                          		CF: ${model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoOldMovimentoGestione.codiceFiscale} - 
						                          		PIVA: ${model.movimentoSpesaModel.modificaAggiornamentoAcc.soggettoOldMovimentoGestione.partitaIva}
						                          		</dd>
					                       			</s:if>
					                       			<s:else>
					                       				<dt>Classe precedente</dt>
						                          		<dd>
						                          		${model.movimentoSpesaModel.modificaAggiornamentoAcc.classeSoggettoOldMovimentoGestione.codice} - 
						                          		${model.movimentoSpesaModel.modificaAggiornamentoAcc.classeSoggettoOldMovimentoGestione.descrizione}
						                          		</dd> 
					                       			</s:else>
					                       		</s:else>				                        				                       						                        				                       		
											</s:if>
											<%-- SIAC-7579 --%>
											<s:hidden id="hidden_movimentoSpesaId" name="movimentoSpesaId" />
											 
											 <s:if test="%{importoNewFormatted != null && importoNewFormatted.trim() != ''}">
												<dt>Nuovo Importo</dt>
	                         						<dd><s:property value="getText('struts.money.format', {movimentoSpesaModel.modificaAggiornamentoAcc.importoOld})" /></dd>
												<s:if test="%{movimentoSpesaModel.modificaAggiornamentoAcc.importoOld lt 0}">
					                          		<div class="control-group">    
									                     <label class="control-label" for="reimputazione">Reimputazione </label>
										                 <div class="controls">
										                 	<%-- SIAC-6997: [...radioReimputato...] Non può essere deselezionato se Elaborato ROR REANNO è true --%>
										                 	<s:if test="%{model.movimentoSpesaModel.elaboratoRor == 'Si'}">
														      	<s:radio id="radioReimputato" disabled="true" name="model.movimentoSpesaModel.reimputazione" cssClass="flagResidenza" list="model.movimentoSpesaModel.daReimputazione"></s:radio>
															</s:if>
															<s:else>
															<!-- CONTABILIA-260 - Bug fix per test non segnalato nel documento ma da test in corso - SIAC-7349 Inizio CM 08/07/2020 -->
														      	<s:radio id="radioReimputato" disabled="true" name="model.movimentoSpesaModel.reimputazione" cssClass="flagResidenza" list="model.movimentoSpesaModel.daReimputazione"></s:radio>
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
										        </s:if>     
											</s:if>
										</dl>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="Motivo">Modifica motivo *</label>
									<div class="controls">
										<s:select list="model.movimentoSpesaModel.listaTipoMotivo"
											id="listaTipoMotivo" headerKey="" 
          		   							headerValue="" name="motivo" listKey="codice"
											listValue="descrizione" cssClass="span9" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="Descrizione">Modifica descrizione <span class="hide" id="modificaDescrizioneObbligatoria"> * </span></label>
									<div class="controls">
										<%-- SIAC-8506 --%>
										<s:textfield name="descrizione" id="descrizione" cssClass="span9" maxlength="500" />
									</div>
								</div>
							</fieldset>
							<s:set var="consultaModificheProvvedimentoAction" value="%{'aggiornaMovSpesaAcc_consultaModificheProvvedimento'}" />
				  			<s:set var="consultaModificheProvvedimentoSubAction" value="%{'aggiornaMovSpesaAcc_consultaModificheProvvedimentoSub'}" />
	
							<%--p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p--%>
<%-- 							<s:include value="/jsp/movgest/include/provvedimentoAggiorna.jsp" /> --%>
							<s:include value="/jsp/movgest/include/provvedimento.jsp" />

							<s:set var="selezionaProvvedimentoAction" value="%{'aggiornaMovSpesaAcc_selezionaProvvedimento'}" />
							<s:set var="clearRicercaProvvedimentoAction" value="%{'aggiornaMovSpesaAcc_clearRicercaProvvedimento'}" />	          
            	  			<s:set var="ricercaProvvedimentoAction" value="%{'aggiornaMovSpesaAcc_ricercaProvvedimento'}" />	          
            				<s:set var="eliminaAction" value="%{'aggiornaMovSpesaAcc_elimina'}" />	  
            				<s:set var="selezionaProvvedimentoInseritoAction" value="%{'aggiornaMovSpesaAcc_selezionaProvvedimentoInserito'}" />	
							<s:set var="inserisciProvvedimentoAction" value="%{'aggiornaMovSpesaAcc_inserisciProvvedimento'}" />	
							<s:set var="clearInserimentoProvvedimentoAction" value="%{'aggiornaMovSpesaAcc_clearInserimentoProvvedimento'}" />
	          
				 			
				 			<s:set var="siSalvaAction" value="%{'aggiornaMovSpesaAcc_siSalva'}" />	 
					  		<s:set var="siProseguiAction" value="%{'aggiornaMovSpesaAcc_siProsegui'}" />	
							<s:set var="annullaSubImpegnoAction" value="%{'aggiornaMovSpesaAcc_annullaSubImpegno'}" />	 
							<s:set var="annullaSubAccertamentoAction" value="%{'aggiornaMovSpesaAcc_annullaSubAccertamento'}" />	 
							<s:set var="annullaMovGestSpesaAction" value="%{'aggiornaMovSpesaAcc_annullaMovGestSpesa'}" />	 
							<s:set var="eliminaSubImpegnoAction" value="%{'aggiornaMovSpesaAcc_eliminaSubImpegno'}" />	 
							<s:set var="eliminaSubAccertamentoAction" value="%{'aggiornaMovSpesaAcc_eliminaSubAccertamento'}" />
							<s:set var="forzaProseguiAction" value="%{'aggiornaMovSpesaAcc_forzaProsegui'}" />	          
							<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'aggiornaMovSpesaAcc_forzaSalvaPluriennaleAccertamento'}" />	          
						 	<s:set var="salvaConByPassDodicesimiAction" value="%{'aggiornaMovSpesaAcc_salvaConByPassDodicesimi'}" />	          
				
							<s:include value="/jsp/movgest/include/modal.jsp" />

							<br /> <br />
							<p>
								<s:include value="/jsp/include/indietro.jsp" />
								<!-- task-131 <s:submit method="annulla" value="annulla" cssClass="btn btn-secondary" id="annullaBtn"></s:submit> -->
								<s:submit action="aggiornaMovSpesaAcc_annulla" value="annulla" cssClass="btn btn-secondary" id="annullaBtn"></s:submit>
								<span class="pull-right">
								<!-- task-131 <s:submit method="salva" value="Salva"	cssClass="btn btn-primary"></s:submit></span> -->
								<s:submit action="aggiornaMovSpesaAcc_salva" value="Salva"	cssClass="btn btn-primary"></s:submit></span>
							</p>
						</div>
					</div>
				</s:form>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="${jspath}movgest/aggiornaModifiche.js"></script>
	
	<s:include value="/jsp/include/footer.jsp" />