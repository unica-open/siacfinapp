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
		  <s:form id="nuovaRigaDaMovimento" action="nuovaRigaDaMovimento" method="post">  
			<!--#include virtual="include/alertErrorSuccess.html" -->
			<s:include value="/jsp/include/actionMessagesErrors.jsp" />

			<!-- <h3>Carta 2014/23568 - Carta contabile per anticipo fatture</h3> -->
			<h3><s:property value="model.titoloStep"/></h3>
			
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
							<label class="control-label">Conto del tesoriere *</label>
							<div class="controls">
								<s:if test="null!=model.listaContoTesoriereRiga">
									<s:select list="model.listaContoTesoriereRiga" id="listacontoTesoreriaRiga" headerKey=""  
										headerValue="" name="model.contoTesoriereRiga" cssClass="span4" 
										listKey="codice" listValue="codice+' - '+descrizione" />
								</s:if> 
							</div>
						</div>
						</fieldset>
						
						<h4>Estremi pagamento</h4>
						<fieldset class="form-horizontal">
						
						<%-- SIAC-6076 tolgo data esecuzione
						<div class="control-group">
							<label class="control-label" >Data esecuzione *</label>
							<div class="controls">
								<s:textfield id="dataEsecuzioneRiga" title="gg/mm/aaaa" name="model.dataEsecuzioneRiga" cssClass="lbTextSmall span2 datepicker"></s:textfield>								
							</div>
						</div> --%>
						
						<div class="control-group">
							<label class="control-label">Importo *</label>
							<div class="controls">
								<!-- <input id="ImportoRigaCarta" name="ImportoRigaCarta" class="span2" type="text" value="" /> -->
								<s:textfield id="importoRiga" name="model.importoRiga" cssClass="span2 soloNumeri decimale" onkeypress="return checkItNumbersCommaAndDotOnly(event)"></s:textfield>
								
								<s:if test="model.codiceDivisa!=null">
									<s:if test="model.codiceDivisa!=''">	
										<span class="radio inline">Importo estero</span>
										<span class="radio inline alRight" id="valueEstero" name="valueEstero">${model.codiceDivisa}</span>
										<!-- <input id="ImportoRigaEstero" class="span2" type="text" value="" name="ImportoRigaEstero" /> -->
										<s:textfield id="importoEsteroRiga" name="model.importoEsteroRiga" cssClass="span2 soloNumeri decimale" onkeypress="return checkItNumbersCommaAndDotOnly(event)"></s:textfield>
									</s:if>
								</s:if>
								
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label">Descrizione *</label>
							<div class="controls">
								<!-- <textarea id="DescrizioneRigaMovimento" name="DescrizioneRigaMovimento" rows="1" cols="15" class="span9" type="text" value=""  ></textarea> -->
								<s:textfield rows="1" cols="15" id="descrizioneRiga" name="model.descrizioneRiga" cssClass="span9" ></s:textfield>
							</div>
						</div>
						
						<div class="control-group">
							<label class="control-label">Note</label>
							<div class="controls">
								<!-- <textarea id="NoteRigaMovimento" name="NoteRigaMovimento" rows="2" cols="15" class="span9" type="text" value="" ></textarea> -->
								<s:textfield rows="2" cols="15" id="noteRiga" name="model.noteRiga" cssClass="span9" ></s:textfield>
							</div>
						</div>
						
						<s:set var="resedeAction" value="%{'nuovaRigaDaMovimento_resede'}" />	          
						<s:set var="caricaTitoloModPagAction" value="%{'nuovaRigaDaMovimento_caricaTitoloModPag'}" />	          
						<s:set var="remodpagamentoAction" value="%{'nuovaRigaDaMovimento_remodpagamento'}" />	         
						
						<div class="accordion" id="soggetto2">
							<div class="accordion-group">
								<div class="accordion-heading">    
									<a class="accordion-toggle" data-toggle="collapse" data-parent="#soggetto2" href="#ImputazionContRigaMovimento">Imputazioni contabili<span class="icon">&nbsp;</span></a>
								</div>
								<div id="ImputazionContRigaMovimento" class="accordion-body collapse in">
									<div class="accordion-inner">              
										<!--#include virtual="include/impegno.html" -->										
										
										<s:include value="/jsp/carta/include/impegnoCarta.jsp" />
										
										
										<!--#include virtual="include/soggetto.html" -->
										<!--#include virtual="include/sediSecondarie.html" -->
										<!--#include virtual="include/modalitaPagamento.html" -->  
										
										<s:set var="ricercaSoggettoAction" value="%{'nuovaRigaDaMovimento_ricercaSoggetto'}"/> 
										<s:set var="selezionaSoggettoAction" value ="%{'nuovaRigaDaMovimento_selezionaSoggetto'}"/>
										
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

					</fieldset>  

				</div>
			</div>
			
			<s:set var="confermaImpegnoCartaAction" value="%{'nuovaRigaDaMovimento_confermaImpegnoCarta'}" />			
            <s:set var="pulisciRicercaImpegnoAction" value="%{'nuovaRigaDaMovimento_pulisciRicercaImpegno'}" />	          
            <s:set var="ricercaGuidataImpegnoAction" value="%{'nuovaRigaDaMovimento_ricercaGuidataImpegno'}" />	          
         
			<s:include value="/jsp/carta/include/modalImpegnoCarta.jsp" />
			  	
			<!--#include virtual="include/modal.html" -->  
			<p class="margin-medium">
				<!-- <a class="btn btn-secondary" href="javascript:history.go(-1)">indietro</a> -->
				<!-- task-131 <s:submit name="indietro" value="indietro" method="indietroRiga" cssClass="btn btn-secondary" /> -->
				<s:submit name="indietro" value="indietro" action="nuovaRigaDaMovimento_indietroRiga" cssClass="btn btn-secondary" />
				
				<!-- <a class="btn btn-secondary" href="#">annulla</a> --> 
				<!-- task-131 <s:submit name="annulla" value="annulla" method="annullaInserisciRiga" cssClass="btn btn-secondary" /> -->
				<s:submit name="annulla" value="annulla" action="nuovaRigaDaMovimento_annullaInserisciRiga" cssClass="btn btn-secondary" />
				<span class="pull-right">
					<!-- <a class="btn btn-primary" href="FIN-insCartaContabileStep2.shtml">inserisci riga</a> -->
					<!-- task-131 <s:submit cssClass="btn btn-primary pull-right" method="inserisciRiga" value="inserisci riga" name="inserisciRiga" /> -->
					<s:submit cssClass="btn btn-primary pull-right" action="nuovaRigaDaMovimento_inserisciRiga" value="inserisci riga" name="inserisciRiga" />
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
			$.ajax({
				//task-131 url: '<s:url method="ricercaSoggetto"></s:url>',
				url: '<s:url action="nuovaRigaDaMovimento_ricercaSoggetto"></s:url>',
				type: "GET",
				data: $(".hiddenGestoreToggle").serialize() + "&id=" + cod, 
			    success: function(data)  {
			    	$("#refreshModPagamento").html(data);
			    }
			});
			
		});
		
		$("#linkCompilazioneGuidataImpegno").click(function(){
			initRicercaGuidataImpegno(
					$("#annoImpegno").val(), 
					$("#numeroImpegno").val()
			);
		});
		
		$("#codCreditore").change(function(){
			var cod = $("#codCreditore").val();
			//Carico i dati in tabella "Modalita' di pagamento"		
			$.ajax({
				//task-131 url: '<s:url method="modpagamento"></s:url>',
				url: '<s:url action="nuovaRigaDaMovimento_modpagamento"></s:url>',
				type: "GET",
				data: $(".hiddenGestoreToggle").serialize() + "&id=" + cod, 
			    success: function(data)  {
			    	$("#refreshModPagamento").html(data);
			    	// $("#openSediSEC").click(); 
			    	 //Carico i dati in tabella "Sedi secondarie"
					$.ajax({
						//task-131 url: '<s:url method="sedisecondarie"></s:url>',
						url: '<s:url action="nuovaRigaDaMovimento_sedisecondarie"></s:url>',
						type: "GET",
						data: $(".hiddenGestoreToggle").serialize() + "&id=" + cod, 
					    success: function(data)  {
					    	$("#refreshSediSecondarie").html(data);
					    	
					    	
						}
					}); 
				}
			});			
		});	
		


	});
	</script>

<s:include value="/jsp/include/footer.jsp" />
