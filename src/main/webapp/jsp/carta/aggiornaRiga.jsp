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
	<!-- NAVIGAZIONE -->
	<s:include value="/jsp/include/header.jsp" />  	
	<!-- /NAVIGAZIONE -->
	<hr />

<div class="container-fluid-banner">

	<a name="A-contenuti" title="A-contenuti"></a>
</div>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 contentPage">    
		<%-- SIAC-7952 rimuovo .do dalla action --%>  
		<s:form id="aggiornaRigaDaMovimento" action="aggiornaRigaDaMovimento" method="post">  
			<!--#include virtual="include/alertErrorSuccess.html" -->
			<s:include value="/jsp/include/actionMessagesErrors.jsp" />
			<!--<h3>Carta 2014/23568 - riga n. 9999</h3> -->
			<h3>Riga n. <s:property value="rigaId"/> - <s:property value="soggetto.denominazione" /></h3>
			
			<div id="MyWizard" class="wizard wizadRigaMovimento" style="display:none">
				<ul class="steps">
					<li data-target="#rigaContabileMov1" class="active"><span class="badge">1</span>Riga carta<span class="chevron"></span></li>
					<li data-target="#rigaContabileMov2"><span class="badge">2</span>Pagamento estero<span class="chevron"></span></li>
				</ul>
			</div>
			
			<div class="step-content">
				<div class="step-pane active" id="step1">
						
					<h4>Dati Riga</h4>
					<fieldset class="form-horizontal">
					
						<div class="control-group">
							<label class="control-label" for="ContoTesoriere">Conto del tesoriere *</label>
							<div class="controls">
								<s:if test="null!=model.listaContoTesoriereRiga">
									<s:select list="model.listaContoTesoriereRiga" id="listacontoTesoreriaRiga" headerKey=""  
										headerValue="" name="model.contoTesoriereRiga" cssClass="span4" 
										listKey="codice" listValue="codice+' - '+descrizione" disabled="disableAggiornaRiga()" />
								</s:if> 
							</div>
						</div>
					</fieldset>
						
					<h4>Estremi pagamento</h4>
					<fieldset class="form-horizontal">
						
						<%-- SIAC-6076 tolgo data esecuzione
						<div class="control-group">
							<label class="control-label" for="DataEsecuzioneRigaCarta">Data esecuzione </label>
								<div class="controls">
									<s:textfield id="dataEsecuzioneRiga" title="gg/mm/aaaa" name="model.dataEsecuzioneRiga" cssClass="lbTextSmall span2 datepicker" disabled="disableAggiornaRiga()"></s:textfield>
							</div>
						</div--%>
						
						<div class="control-group">
							<label class="control-label" for="ImportoRigaCarta">Importo *</label>
							<div class="controls">
								
								<s:if test="model.codiceDivisa!=null">
									<s:if test="model.codiceDivisa!=''">
										<s:textfield id="importoRiga" name="model.importoRiga" cssClass="span2 soloNumeri decimale" onkeypress="return checkItNumbersCommaAndDotOnly(event)" disabled="disableAggiornaImportoRiga()"></s:textfield>																
									</s:if>
									<s:else>
										<s:textfield id="importoRiga" name="model.importoRiga" cssClass="span2 soloNumeri decimale" onkeypress="return checkItNumbersCommaAndDotOnly(event)" disabled="disableAggiornaRiga()"></s:textfield>
									</s:else>
								</s:if>									
								<s:else>
									<s:textfield id="importoRiga" name="model.importoRiga" cssClass="span2 soloNumeri decimale" onkeypress="return checkItNumbersCommaAndDotOnly(event)" disabled="disableAggiornaRiga()"></s:textfield>
								</s:else>
								
								<s:if test="model.codiceDivisa!=null">
									<s:if test="model.codiceDivisa!=''">
										<span class="radio inline">Importo estero</span>
										<%-- <span class="radio inline alRight" name="ImportoRigaEstero">$</span> --%>
										<span class="radio inline alRight" id="valueEstero" name="valueEstero">${model.codiceDivisa}</span>
										<s:textfield id="importoEsteroRiga" name="model.importoEsteroRiga" cssClass="span2 soloNumeri decimale" onkeypress="return checkItNumbersCommaAndDotOnly(event)" disabled="disableAggiornaRiga()"></s:textfield>
									</s:if>
								</s:if>
								
								<div class="radio inline collapse_alert"><span class="icon-chevron-right icon-red alRight"></span>Da Regolarizzare <s:property value="model.importoDaRegolarizzare"/> </div>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="DescrizioneRigaCarta">Descrizione *</label>
							<div class="controls">
									<s:textfield rows="1" cols="15" id="descrizioneRiga" name="model.descrizioneRiga" cssClass="span9" disabled="disableAggiornaRiga()"></s:textfield>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label" for="NoteRigaCarta">Note </label>
							<div class="controls">
									<s:textfield rows="2" cols="15" id="noteRiga" name="model.noteRiga" cssClass="span9" disabled="disableAggiornaRiga()"></s:textfield>
							</div>
						</div>
						
						<s:set var="resedeAction" value="%{'aggiornaRigaDaMovimento_resede'}" />	          
						<s:set var="caricaTitoloModPagAction" value="%{'aggiornaRigaDaMovimento_caricaTitoloModPag'}" />	          
						<s:set var="remodpagamentoAction" value="%{'aggiornaRigaDaMovimento_remodpagamento'}" />	          
						
						<div class="accordion" id="soggetto2">
							<div class="accordion-group">
							  <div class="accordion-heading">    
								<a class="accordion-toggle" data-toggle="collapse" data-parent="#soggetto2" href="#ImputazionContRigaMovimento">
								 Imputazioni contabili<span class="icon">&nbsp;</span>
								</a>
							  </div>
							  <div id="ImputazionContRigaMovimento" class="accordion-body collapse in">
								<div class="accordion-inner">              
									<!--#include virtual="include/impegno.html" -->
									<%-- <h4>Soggetto: <span class="datiRIFSoggetto">000000/X/XX - .....</span></h4> --%>
									<!--#include virtual="include/sediSecondarie.html" -->
									<!--#include virtual="include/modalitaPagamento.html" -->
									
									<s:include value="/jsp/carta/include/impegnoCarta.jsp" />
									 
									<div id="refreshHeaderSoggetto">
								          	<s:include value="/jsp/carta/include/headerSoggettoCarta.jsp"/>		       	
									       	<s:include value="/jsp/carta/include/soggettoCarta.jsp" /> 
									       	<s:include value="/jsp/include/modalSoggetto.jsp" />									       	
									          
									        <a id="openSediSEC" data-toggle="modal" data-target="#refreshSediSecondarie"></a>    	
								        	<div id="refreshSediSecondarie">
								        		<s:include value="/jsp/carta/include/sediSecondarieCarta.jsp" />	   
								        	</div>									        									        
								        	
								        	<a id="openModPAG" data-toggle="modal" data-target="#refreshModPagamento"></a>	        	
								        	<div id="refreshModPagamento">     	
							        			<s:include value="/jsp/carta/include/modalitaPagamentoCarta.jsp" />
								        	</div>								        									       
							        	</div>    
								</div>
							  </div>
							</div>
						</div>
						
						<s:set var="confermaImpegnoCartaAction" value="%{'aggiornaRigaDaMovimento_confermaImpegnoCarta'}" />			
            			<s:set var="pulisciRicercaImpegnoAction" value="%{'aggiornaRigaDaMovimento_pulisciRicercaImpegno'}" />	          
            			<s:set var="ricercaGuidataImpegnoAction" value="%{'aggiornaRigaDaMovimento_ricercaGuidataImpegno'}" />	          
						<s:include value="/jsp/carta/include/modalImpegnoCarta.jsp" />
						
						<div class="accordion" id="accordionRigaCC">
							<div class="accordion-group">
							  
							  <div class="accordion-heading">    
								<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordionRigaCC" href="#accordionRigaCCTAB">
								 Documenti collegati<span class="icon">&nbsp;</span>
								</a>
							  </div>
							  
							  <div id="accordionRigaCCTAB" class="accordion-body collapse">
								<div class="accordion-inner">
								<div id="refreshDopoElimina">
									  <s:include value="/jsp/carta/include/elencoDocumentiDaRicerca.jsp"></s:include>
								</div> 		
										<p><a id="linkCompilazioneGuidataDocumento" class="btn btn-primary pull-right" href="#collegaDocumento" data-toggle="modal">collega documento</a></p>
										
								</div>
							  </div>
							  
							</div>
						</div>

					</fieldset>  

				</div>
			</div>
			  
		   <!--#include virtual="include/modal.html" -->
		   <s:set var="confermaCompGuidataDocumentoAction" value="%{'aggiornaRigaDaMovimento_confermaCompGuidataDocumento'}" />			            			 
		   <s:set var="pulisciRicercaDocumentoAction" value="%{'aggiornaRigaDaMovimento_pulisciRicercaDocumento'}" />	          
           <s:set var="ricercaGuidataDocumentoAction" value="%{'aggiornaRigaDaMovimento_ricercaGuidataDocumento'}" />	          
           <s:include value="/jsp/carta/include/modalRicercaDocumentoCarta.jsp" />
			
			<p class="margin-medium">
				<!-- <a class="btn btn-secondary" href="javascript:history.go(-1)">indietro</a> -->
				<!-- task-131 <s:submit name="indietro" value="indietro" method="indietroRiga" cssClass="btn btn-secondary" /> -->
				<s:submit name="indietro" value="indietro" action="aggiornaRigaDaMovimento_indietroRiga" cssClass="btn btn-secondary" />
				<!-- <a class="btn btn-secondary" href="#">annulla</a> --> 
				<!-- task-131 <s:submit name="annulla" value="annulla" method="annullaAggiornaRiga" cssClass="btn btn-secondary" />-->
				<s:submit name="annulla" value="annulla" action="aggiornaRigaDaMovimento_annullaAggiornaRiga" cssClass="btn btn-secondary" />
				
				<span class="pull-right">
					<!-- <a class="btn btn-primary" href="FIN-insCartaContabileStep2.shtml">aggiorna riga</a> -->
					<!-- task-131 <s:submit cssClass="btn btn-primary pull-right" method="aggiornaRiga" value="aggiorna riga" name="aggiornaRiga" /> -->
					<s:submit cssClass="btn btn-primary pull-right" action="aggiornaRigaDaMovimento_aggiornaRiga" value="aggiorna riga" name="aggiornaRiga" />
				</span>
			</p>       
			  
		</s:form>
		  
		  
		</div>
	</div>	 
</div>	

	<!-- CARTA CONTABILE -->
	<script type="text/javascript">
	
	$(document).ready(function() {		
		
		$("#linkCompilazioneGuidataSoggetto").click(function(){
			initRicercaGuidataSoggetto(
				$("#codCreditore").val(),
				null
			);
		});
		
		$("#linkCompilazioneGuidataImpegno").click(function(){
			initRicercaGuidataImpegno(
					$("#annoImpegno").val(), 
					$("#numeroImpegno").val()
			);
		});
		
		$("#linkCompilazioneGuidataDocumento").click(function(){
			initRicercaGuidataDocumento();
		});
		
		
		
		$("#codCreditore").change(function(){
			var cod = $("#codCreditore").val();
			//Carico i dati in tabella "Modalita' di pagamento"		
			$.ajax({
				//task-131 url: '<s:url method="modpagamento"></s:url>',
				url: '<s:url action="aggiornaRigaDaMovimento_modpagamento"></s:url>',
				type: "GET",
				data: $(".hiddenGestoreToggle").serialize() + "&id=" + cod, 
			    success: function(data)  {
			    	$("#refreshModPagamento").html(data);
			    	// $("#openSediSEC").click(); 
			    	 //Carico i dati in tabella "Sedi secondarie"
					$.ajax({
						//task-131 url: '<s:url method="sedisecondarie"></s:url>',
						url: '<s:url action="aggiornaRigaDaMovimento_sedisecondarie"></s:url>',
						type: "GET",
						data: $(".hiddenGestoreToggle").serialize() + "&id=" + cod, 
					    success: function(data)  {
					    	$("#refreshSediSecondarie").html(data);
					    	
					    	
					    	$.ajax({
					    		//task-131 url: '<s:url method="aggiornaAvviso"></s:url>',
					    		 url: '<s:url action="aggiornaRigaDaMovimento_aggiornaAvviso"></s:url>',
								type: "GET",
							    success: function(data)  {
							    	$("#aggiornaAvviso").html(data);
							    	
								}
							}); 
						}
					}); 
				}
			});			
		});	
		


	});
	
<s:if test="disableAggiornaRiga()">
	/*impegno*/
	$('#annoImpegno').attr('disabled', true);
	$('#numeroImpegno').attr('disabled', true);
	$('#numeroSub').attr('disabled', true);
	$('#compilazioneGuidataImpegno').attr('disabled', true);
	$('#compilazioneGuidataImpegno').removeAttr('href');
	
	/*soggetto*/
	$('#checkSedi').attr('disabled', true);
	$('#checkMdp').attr('disabled', true);
	$('#linkCompilazioneGuidataSoggetto').attr('disabled', true);
	$('#linkCompilazioneGuidataSoggetto').removeAttr('href');
</s:if>

<s:if test="disableNuovoDocumento()">
	$('#linkCompilazioneGuidataDocumento').attr('disabled', true);
	$('#linkCompilazioneGuidataDocumento').removeAttr('href');
</s:if>

	</script>

<s:include value="/jsp/include/footer.jsp" />
