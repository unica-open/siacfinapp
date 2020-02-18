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


<div id="aggiornaProvvisorioDivPrincipale" class="container-fluid">
	<div class="row-fluid">
		<div class="span12 contentPage">
    
			<s:form method="post" action="aggiornaProvvisorioCassa.do"  id="aggiornaProvvisorioCassa"  >
				<s:include value="/jsp/include/actionMessagesErrors.jsp" />
		
			
				<h3>Aggiorna provvisorio di cassa <s:include value="/jsp/provvisorio/include/titoloProvvisorio.jsp" /></h3>
				
				<s:if test="%{successSalva}">
				<div class="alert alert-success margin-medium">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					L'operazione &egrave; stata completata con successo
				</div>   
				</s:if> 
				
        		<div class="step-content">
		   
          		<div class="step-pane active" id="step1">
          		
					<h4>Dati principali</h4>
				
				
         		  
					<fieldset class="form-horizontal margin-large">
					  
						<div class="control-group">
							<span class="control-label">Tipologia:</span>
							<div class="controls">														 
							 	<div class="radio inline">
							 		<s:if test="not utenteAmministratore">
							 			<s:hidden name="tipoDocumentoProv" />
							 		</s:if>
                       				<s:radio disabled="%{not utenteAmministratore}" id="documento" cssClass="flagDocumento" name="tipoDocumentoProv" list="tipoDocumentoProvList"/>
								</div>		       								                     			
							</div>
						</div>
						
		      
						<div class="control-group">
							<label class="control-label" for="Nquiet">Numero*</label>
							<div class="controls">
								<s:textfield readonly="%{not utenteAmministratore}"  id="numeroProvvisorio" onkeyup="return checkItNumbersOnly(event)"  maxlength="8" name="model.numeroProvvisorio" cssClass="lbTextSmall span2"/>
							</div>
						</div>												
						
						<div class="control-group">
							<label class="control-label" for="Nquiet">Conto evidenza</label>
							<div class="controls">
								<s:textfield readonly="true" name="model.codiceContoEvidenza" cssClass="lbTextSmall span2"/>
								<s:textfield readonly="true" name="model.descrizioneContoEvidenza" cssClass="lbTextSmall span7"/>
							</div>
						</div>												
						
						<div class="control-group">
							<label class="control-label" for="DescCaus">Descrizione causale</label>
							<div class="controls">
								<s:textfield  readonly="%{not utenteAmministratore}" rows="2" cols="15" id="DescCaus" name="model.descCausale" cssClass="span9" ></s:textfield>
							</div>
						</div>
						
		
						<div class="control-group">
							<label class="control-label" for="DescSogg">Denominazione soggetto</label>
							<div class="controls">
								<s:textfield  readonly="%{not utenteAmministratore}" rows="2" cols="15" id="DescSogg" name="model.denominazioneSoggetto" cssClass="span9" ></s:textfield>
							</div>
						</div>	
						
						
						<div class="control-group">
							<label class="control-label" for="DescSogg">Data emissione</label>
							<div class="controls">
								<s:textfield readonly="%{not utenteAmministratore}"  id="dataEmissione" title="gg/mm/aaaa" name="model.dataEmissione" cssClass="%{utenteAmministratore ? 'lbTextSmall span2 datepicker' : 'lbTextSmall span2'}"></s:textfield>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="DescSogg">Data trasmissione</label>
							<div class="controls">
								<s:textfield  readonly="%{not utenteAmministratore}" id="dataTrasmissione" title="gg/mm/aaaa" name="model.dataTrasmissione" cssClass="%{utenteAmministratore ? 'lbTextSmall span2 datepicker' : 'lbTextSmall span2'}"></s:textfield>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="DescSogg">Importo*</label>
							<div class="controls">
								<s:textfield    readonly="%{not utenteAmministratore}"   id="importo" name="model.importoFormattato" cssClass="lbTextSmall span2 soloNumeri decimale"></s:textfield>
							</div>
						</div>	
						
					<div class="control-group">      
				          <label class="control-label">Struttura Amministrativa</label>
				          <div class="controls">  
				          
				               
				      <s:if test="%{model.provvisorioDiCassaInAggiornamento.strutturaAmministrativoContabile != null}">
				          <div id="descr-sac"></div><br/>
				      </s:if>

				          	<s:hidden name="strutturaSelezionataSuPagina" value="%{model.provvisorioDiCassaInAggiornamento.strutturaAmministrativoContabile.uid}" />

				          <s:if test="not utenteAmministratore">
				            <ul id="strutturaAmministrativaAggiornamentoProvvisorio" class="ztree treeStruttAmm"></ul>
				          </s:if>
							
							<s:else>	
				            <div class="accordion span9" class="struttAmmAggProvvisorio" id="struttAmmAggProvvisorio">
				              <div class="accordion-group">
				                <div class="accordion-heading">    
				                  <a class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmmAggProvvisorio" href="#4b">
				                 Seleziona la Struttura amministrativa
				                  <i class="icon-spin icon-refresh spinner" id="spinnerStruttAmmAggiornamentoProvvisorio"></i></a>
				                </div>
				                <div id="4b" class="accordion-body collapse">
				                  <div class="accordion-inner" id="strutturaAmministrativaAggiornamentoProvvisorioDiv">
				                    <ul id="strutturaAmministrativaAggiornamentoProvvisorio" class="ztree treeStruttAmm"></ul>
				                  </div>
				                  <div class="accordion-inner" id="strutturaAmministrativaAggiornamentoProvvisorioWait">
				                    Attendere prego..
				                  </div>
				                  
				                </div>
				              </div>
				            </div>
</s:else>
				          </div>
				        </div>
					
					
					<span class="hide"
						data-is-amministratore='<s:property value="utenteAmministratore" />'
						data-is-decentrato='<s:property value="utenteDecentrato and provvisorioConSacUtente" />'
						data-is-lettore='<s:property value="utenteLettore" />'
						id="tipoUtente"></span>
					
			 		<s:if test="utenteAmministratore or (utenteDecentrato and provvisorioConSacUtente)">
						<div class="control-group">
							<label class="control-label" for="DescSogg">Data invio al Servizio (SAC)</label>
							<div class="controls">
								<s:textfield readonly="%{not utenteAmministratore}"  id="dataInvioServizio"
									title="gg/mm/aaaa" name="model.dataInvioServizio" 
									cssClass="%{utenteAmministratore ? 'lbTextSmall span2 datepicker' : 'lbTextSmall span2'}"></s:textfield>
							</div>
						</div>
					</s:if>
				        
			      
						<div class="control-group">
							<span class="control-label">Accettato</span>
							<div class="controls">														 
							 	<span class="radio inline">
							 		
							 		<s:if test="utenteLettore or (utenteDecentrato and not provvisorioConSacUtente)">
							 			<s:hidden name="model.accettatoStr" />
							 		</s:if>

                       				<s:radio  id="accettato" cssClass="flagDocumento" 
                       				 disabled="%{utenteLettore or (utenteDecentrato and not provvisorioConSacUtente)}"
                       					name="model.accettatoStr" list="#{ 'true': 'S&igrave;', 'false' : 'No', 'null' : 'Da definire' }"/>
								</span>
								
								
					 		<s:if test="tipoDocumentoProv eq 'Entrata' and (utenteDecentrato and provvisorioConSacUtente or utenteAmministratore)">
					 		<span class="hidex" id="dataPresaInCaricoServizioGrp">
								<span class="al">
									<label class="radio inline" for="DescSogg">Data presa in carico dal Servizio *</label>
								</span>
   							    <s:textfield readonly="%{not (tipoDocumentoProv eq 'Entrata' and (utenteDecentrato and provvisorioConSacUtente or utenteAmministratore))}" 
   							    	id="dataPresaInCaricoServizio" title="gg/mm/aaaa" name="model.dataPresaInCaricoServizio" 
   							    	cssClass="%{tipoDocumentoProv eq 'Entrata' and (utenteDecentrato and provvisorioConSacUtente or utenteAmministratore) ? 'lbTextSmall span2 datepicker' : 'lbTextSmall span2'}"></s:textfield>
							</span>
								
					 		<span class="hidex" id="dataRifiutoErrataAttribuzioneGrp">
								<span class="al">
									<label class="radio inline" for="DescSogg">Data rifiuto per errata attribuzione *</label>
								</span>	
								<s:textfield readonly="%{not (tipoDocumentoProv eq 'Entrata' and (utenteDecentrato and provvisorioConSacUtente or utenteAmministratore))}"  
									id="dataRifiutoErrataAttribuzione" title="gg/mm/aaaa" name="model.dataRifiutoErrataAttribuzione" 
									cssClass="%{tipoDocumentoProv eq 'Entrata' and (utenteDecentrato and provvisorioConSacUtente or utenteAmministratore) ? 'lbTextSmall span2 datepicker' : 'lbTextSmall span2'}"></s:textfield>
							</span>
							</s:if> 			
							</div>
						</div>
				        
				        
				        <div class="control-group">
							<label class="control-label" for="DescSogg">Note</label>
							<div class="controls">
								<s:textarea id="note" cssStyle="resize: none;" name="model.note" cssClass="span9" 
									readonly="%{utenteLettore or (utenteDecentrato and not provvisorioConSacUtente)}"  />
							</div>
						</div>
				        
				       </fieldset>         		            
  
					</div>
				</div>
				<br/> <br/> 
				<p>           
	            <s:include value="/jsp/include/indietro.jsp" /> 
	            

					<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
					<s:hidden id="struttAmmOriginale" name="teSupport.struttAmmOriginale"/>
				
		<s:if test="not utenteLettore and (not utenteDecentrato or provvisorioConSacUtente)">
	            
				<s:submit name="annulla" value="annulla" method="annullaAggiornaProvvisorio" cssClass="btn btn-secondary" />

				<button type="button"  id="pulsanteAggiornaProvvisorio" name="salva" value="Salva"  class="btn btn-primary pull-right" >Salva</button>

		</s:if>
				
				<!-- 
				 <a class="accordion-toggle btn btn-primary pull-right" id="pulsanteAggiornaProvvisorio" data-toggle="collapse" data-parent="#aggiornaProvvisorioDivPrincipale">
			       <i class="icon-search icon"></i>&nbsp;Salva&nbsp;<span class="icon"></span>
			     </a> 
			      -->
				
				</p>       				
  	    	</s:form>
    	</div>
	</div>	 
</div>

<script type="text/javascript">
$(document).on("ztree:init", function(ztree) {

	var setSac = function() {
		 if ($("span.button.chk.radio_true_full").length) {
			 var par = $("span.button.chk.radio_false_part").siblings('a').attr('title');
			 
 	 		 var sac = $("span.button.chk.radio_true_full").siblings('a').attr('title');

 	 		 if (par) 
 	 	 		 sac = par.replace(/\s+.+$/, '') + ' - ' + sac;
 	 		 
 			 $('#descr-sac').text(sac);
 	 	 }
	};
	

 	if ($("#strutturaAmministrativaAggiornamentoProvvisorio li").length) {
		 var ztree = $.fn.zTree.getZTreeObj("strutturaAmministrativaAggiornamentoProvvisorio");
		 ztree.expandAll(true);
		 
 	<s:if test="utenteAmministratore">
	 	 ztree.expandAll(false);
		 setSac();
	</s:if>
 	<s:else>
 		 setSac();
	 	 $('#strutturaAmministrativaAggiornamentoProvvisorio').remove();
	</s:else>
 	 	 
 	}
});

$(document).ready(function() {

 	<s:if test="not utenteAmministratore">
		$('#dataEmissione').datepicker('remove');
		$('#dataTrasmissione').datepicker('remove');
	</s:if>

	
	$("#pulsanteAggiornaProvvisorio").click(function(e) {
		//e.preventDefault();
		var treeObj;
		var struttammId;
		if($('#struttAmmAggProvvisorio').length != 0){
			treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaAggiornamentoProvvisorio");
			if (treeObj != null) {
				var selectedNode = treeObj.getCheckedNodes(true);
				selectedNode.forEach(function(currentNode) {
					//strutturaAmministrativaParam = "&struttAmmSelezionata=" + currentNode.uid;
					struttammId = currentNode.uid
				});
			}
			
			/* $.ajax({
				url: '<s:url method="primaDiAggiornaProvvisorioDiCassa"/>',
				type: 'POST',
				data: strutturaAmministrativaParam,
			    success: function(data)  {
				}
			}); */
				$('input[name="strutturaSelezionataSuPagina"]').val(struttammId? struttammId : "");
		}
		
		$('#aggiornaProvvisorioCassa')
		.append('<input type="hidden" name="method:aggiornaProvvisorioDiCassa" value="" class="btn" >')
		.submit();
		
	});	

	
});

</script>

<script type="text/javascript" src="${jspath}provvisorio/aggiornaProvvisorioCassa.js" charset="utf-8"></script>


<s:include value="/jsp/include/footer.jsp" />

