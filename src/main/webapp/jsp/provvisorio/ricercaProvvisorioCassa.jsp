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


<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 contentPage">
			<%-- SIAC-7952 rimuovo .do dalla action --%>
			<s:form method="post" action="ricercaProvvisorioCassa" id="ricercaProvvisorioCassa">
				<s:include value="/jsp/include/actionMessagesErrors.jsp" />
			
				<h3>Ricerca provvisori cassa</h3>

        		<div class="step-content">
		   		<div class="step-pane active" id="step1">
          			<p class="margin-medium">
          			<!-- task-131 <s:submit name="cerca" value="cerca" method="ricercaProvvisorioCassa" cssClass="btn btn-primary pull-right" /> --> 
					<s:submit name="cerca" value="cerca" action="ricercaProvvisorioCassa_ricercaProvvisorioCassa" cssClass="btn btn-primary pull-right" />
					<br>
					<br>
					<h4>Dati principali</h4>
         		  
					<fieldset class="form-horizontal margin-large">
					  
						<div class="control-group">
							<span class="control-label">Documento</span>
							<div class="controls">														 
							 	<div class="radio inline">
                       				<s:radio id="documento" cssClass="flagDocumento" name="tipoDocumentoProv" list="#{ 'Entrata': 'Entrata', 'Spesa' : 'Spesa' }"/>
								</div>		       								                     			
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="Nquiet">Numero</label>
							<div class="controls">
								<s:textfield id="numeroProvvisorio" onkeyup="return checkItNumbersOnly(event)" name="model.numeroProvvisorio" cssClass="lbTextSmall span2"/>
							</div>
						</div>												
						
						<div class="control-group">
							<label class="control-label" for="distinta">Conto evidenza</label>
							<div class="controls">
								<s:select list="model.listaContoTesoriere" id="listacontoTesoreria" headerKey=""  
									headerValue="" name="model.contoTesoriere" cssClass="span4" 
									listKey="codice" listValue="codice + ' - ' + descrizione" />
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="DescCaus">Descrizione causale</label>
							<div class="controls">
								<s:textfield rows="2" cols="15" id="DescCaus" name="model.descCausale" cssClass="span9" ></s:textfield>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="CodSottoCaus">Sotto causale</label>
							<div class="controls">
								<s:textfield id="subCausale" name="model.subCausale" cssClass="span9"></s:textfield>
							</div>
						</div>
		
						<div class="control-group">
							<label class="control-label" for="DescSogg">Descrizione soggetto</label>
							<div class="controls">
								<s:textfield rows="2" cols="15" id="DescSogg" name="model.denominazioneSoggetto" cssClass="span9" ></s:textfield>
							</div>
						</div>	
						
					    <div class="control-group">      
				          <label class="control-label">Struttura Amministrativa</label>
				          <div class="controls">   
				            <s:hidden name="strutturaSelezionataSuPagina" id="strutturaSelezionataSuPagina" />
				                           
				            <div class="accordion span9" class="struttAmmRicercaProvvisorio">
				              <div class="accordion-group">
				                <div class="accordion-heading">    
				                  <a class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmmRicercaProvvisorio" href="#4b">
				                 Seleziona la Struttura amministrativa
				                  <i class="icon-spin icon-refresh spinner" id="spinnerStruttAmmRicercaProvvisorio"></i></a>
				                </div>
				                <div id="4b" class="accordion-body collapse">
				                  <div class="accordion-inner" id="strutturaAmministrativaRicercaProvvisorioDiv">
				                    <ul id="strutturaAmministrativaRicercaProvvisorio" class="ztree treeStruttAmm"></ul>
				                  </div>
				                  <div class="accordion-inner" id="strutturaAmministrativaRicercaProvvisorioWait">
				                    Attendere prego..
				                  </div>
				                  
				                </div>
				              </div>
				            </div>
				          </div>
				        </div>
						
						
						<div class="control-group">
							<label class="control-label" for="DescSogg">Data emissione</label>
							<div class="controls">
								<span class="al">
										<label class="radio inline" for="dataInizioEmissione">Inizio</label>
								</span>								
								<s:textfield id="dataInizioEmissione" title="gg/mm/aaaa" name="model.dataInizioEmissione" cssClass="lbTextSmall span2 datepicker"></s:textfield>
								<span class="al">
										<label class="radio inline" for="dataFineEmissione">Fine</label>
								</span>
								<s:textfield id="dataFineEmissione" title="gg/mm/aaaa" name="model.dataFineEmissione" cssClass="lbTextSmall span2 datepicker"></s:textfield>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="DescSogg">Data trasmissione</label>
							<div class="controls">
								<span class="al">
										<label class="radio inline" for="dataInizioTrasmissione">Inizio</label>
								</span>								
								<s:textfield id="dataInizioTrasmissione" title="gg/mm/aaaa" name="model.dataInizioTrasmissione" cssClass="lbTextSmall span2 datepicker"></s:textfield>
								<span class="al">
										<label class="radio inline" for="dataFineTrasmissione">Fine</label>
								</span>
								<s:textfield id="dataFineTrasmissione" title="gg/mm/aaaa" name="model.dataFineTrasmissione" cssClass="lbTextSmall span2 datepicker"></s:textfield>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="DescSogg">Importo</label>
							<div class="controls">
								<span class="al">
										<label class="radio inline" for="importoDa">Da</label>
								</span>								
								<s:textfield id="importoDa"  name="model.importoDa" cssClass="lbTextSmall span2 formatDecimal"></s:textfield>
								<span class="al">
										<label class="radio inline" for="importoA">A</label>
								</span>
								<s:textfield id="importoA"  name="model.importoA" cssClass="lbTextSmall span2 formatDecimal"/>
							</div>
						</div>	
						
						<div class="control-group">
							<span class="control-label">Annullato</span>
							<div class="controls">
								<div class="checkbox inline">
									<%-- <s:checkboxlist id="annullatoProv" name="flagAnnullatoProv" list="flagAnnullatoProvList"/> --%>
									<s:checkboxlist id="annullatoProv" name="flagAnnullatoProv" list="#{ 'si': 'Si', 'no' : 'No' }" value="defaultValueAnnulla"/>									
								</div>								
							</div>
						</div>
						
						<div class="control-group">					
							<label class="control-label" for="DescSogg">Da regolarizzare</label>							
							<div class="controls">	
								<div class="radio inline">																					
									<%-- <s:checkboxlist id="regolarizzatoProv" name="flagDaRegolarizzare" list="flagDaRegolarizzareList"/> --%>
									<s:radio id="regolarizzatoProv" name="flagDaRegolarizzare" list="#{ 'si': 'Si', 'no' : 'No' }"/>
								</div>																																			
							</div>					
						</div>	
							<!-- <div class="controls">
								<label class="radio inline">
									<input type="checkbox" name="Nozero" value="">
								</label>
								
							</div> -->
						
							
						<div class="control-group" id="invioServizioCtrlGrp">
							<label class="control-label" for="DescSogg">Data invio al Servizio (SAC)</label>
							<div class="controls">
								<span class="al">
										<label class="radio inline" for="dataInizioInvioServizio">Inizio</label>
								</span>								
								<s:textfield id="dataInizioInvioServizio" title="gg/mm/aaaa" name="model.dataInizioInvioServizio" cssClass="lbTextSmall span2 datepicker"></s:textfield>
								<span class="al">
										<label class="radio inline" for="dataFineInvioServizio">Fine</label>
								</span>
								<s:textfield id="dataFineInvioServizio" title="gg/mm/aaaa" name="model.dataFineInvioServizio" cssClass="lbTextSmall span2 datepicker"></s:textfield>
							</div>
						</div>
						


						<div class="control-group">					
							<label class="control-label" for="flag-accettato">Accettato</label>							
							<div class="controls">	
								<div class="radio inline">																					
									<s:radio id="flag-accettato" name="flagAccettato" list="#{ 'si': 'Si', 'no' : 'No' }"/>
								</div>																																			
							</div>					
						</div>	

						<div class="control-group" id="presaInCaricoServizioCtrlGrp">
							<label class="control-label" for="DescSogg">Data di accettazione</label>
							<div class="controls">
								<span class="al">
										<label class="radio inline" for="dataInizioPresaInCaricoServizio">Inizio</label>
								</span>								
								<s:textfield id="dataInizioPresaInCaricoServizio" title="gg/mm/aaaa" name="model.dataInizioPresaInCaricoServizio" cssClass="lbTextSmall span2 datepicker"></s:textfield>
								<span class="al">
										<label class="radio inline" for="dataFinePresaInCaricoServizio">Fine</label>
								</span>
								<s:textfield id="dataFinePresaInCaricoServizio" title="gg/mm/aaaa" name="model.dataFinePresaInCaricoServizio" cssClass="lbTextSmall span2 datepicker"></s:textfield>
							</div>
						</div>
						
						<div class="control-group" id="rifiutoErrataAttribuzioneCtrlGrp">
							<label class="control-label" for="DescSogg">Data di rifiuto per errata attribuzione</label>
							<div class="controls">
								<span class="al">
										<label class="radio inline" for="dataInizioRifiutoErrataAttribuzione">Inizio</label>
								</span>								
								<s:textfield id="dataInizioRifiutoErrataAttribuzione" title="gg/mm/aaaa" name="model.dataInizioRifiutoErrataAttribuzione" cssClass="lbTextSmall span2 datepicker"></s:textfield>
								<span class="al">
										<label class="radio inline" for="dataFineRifiutoErrataAttribuzione">Fine</label>
								</span>
								<s:textfield id="dataFineRifiutoErrataAttribuzione" title="gg/mm/aaaa" name="model.dataFineRifiutoErrataAttribuzione" cssClass="lbTextSmall span2 datepicker"></s:textfield>
							</div>
						</div>
						

							
											
						</fieldset>         		            
  
					</div>
				</div>
				<br/> <br/> 
				<p>           
	            <s:include value="/jsp/include/indietro.jsp" /> 
				<!-- task-131 <s:submit name="annulla" value="annulla" method="annullaRicercaProvvisori" cssClass="btn btn-secondary" /> -->
				<!-- task-131 <s:submit id="pulsanteCercaProvvisorio" name="cerca" value="cerca" method="ricercaProvvisorioCassa" cssClass="btn btn-primary pull-right" /> --> 
				<s:submit name="annulla" value="annulla" action="ricercaProvvisorioCassa_annullaRicercaProvvisori" cssClass="btn btn-secondary" />
				<s:submit id="pulsanteCercaProvvisorio" name="cerca" value="cerca" action="ricercaProvvisorioCassa_ricercaProvvisorioCassa" cssClass="btn btn-primary pull-right" /> 
				</p>       				
  	    	</s:form>
    	</div>
	</div>	 
</div>	


<script type="text/javascript">
// task-131 var url_primaDiRicercaProvvisorioDiCassa = '<s:url method="primaDiRicercaProvvisorioDiCassa"/>';	
var url_primaDiRicercaProvvisorioDiCassa = '<s:url action="ricercaProvvisorioCassa_primaDiRicercaProvvisorioDiCassa"/>';
</script>

<script type="text/javascript" src="${jspath}provvisorio/ricercaProvvisorioCassa.js" charset="utf-8"></script>


<s:include value="/jsp/include/footer.jsp" />

