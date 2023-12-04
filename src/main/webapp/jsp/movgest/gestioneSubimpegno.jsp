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
	<hr />
	<div class="container-fluid-banner">
		<a name="A-contenuti" title="A-contenuti"></a>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form id="%{labels.FORM}" action="%{labels.FORM}" method="post">
		<!-- 		<h3><s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/> <s:property value="step1Model.annoImpegno"/>/<s:property value="step1Model.numeroImpegno"/> - <s:property value="step1Model.oggettoImpegno"/> -
					<s:property value="step1Model.importoFormattato"/></h3>	-->
					
					<h3><s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
					
					<s:include value="/jsp/include/actionMessagesErrors.jsp" />
					<s:include value="/jsp/movgest/include/tabsSubimpegno.jsp" />
					
					<s:if test="oggettoDaPopolareSubimpegno()">
						<s:set var="gestisciForwardAction" value="%{'gestioneSubimpegno_gestisciForward'}" />
						<s:set var="consultaModificheProvvedimentoAction" value="%{'gestioneSubimpegno_consultaModificheProvvedimento'}" />
	
						<s:set var="siSalvaAction" value="%{'gestioneSubimpegno_siSalva'}" />	 
						<s:set var="siProseguiAction" value="%{'gestioneSubimpegno_siProsegui'}" />
						<s:set var="annullaSubImpegnoAction" value="%{'gestioneSubimpegno_annullaSubImpegno'}" />	 
						<s:set var="annullaSubAccertamentoAction" value="%{'gestioneSubimpegno_annullaSubAccertamento'}" />	 
						<s:set var="annullaMovGestSpesaAction" value="%{'gestioneSubimpegno_annullaMovGestSpesa'}" />	
						<s:set var="eliminaSubImpegnoAction" value="%{'gestioneSubimpegno_eliminaSubImpegno'}" />	 
						<s:set var="eliminaSubAccertamentoAction" value="%{'gestioneSubimpegno_eliminaSubAccertamento'}" />
						<s:set var="forzaProseguiAction" value="%{'gestioneSubimpegno_forzaProsegui'}" />	          
						<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'gestioneSubimpegno_forzaSalvaPluriennaleAccertamento'}" />	          
						<s:set var="salvaConByPassDodicesimiAction" value="%{'gestioneSubimpegno_salvaConByPassDodicesimi'}" />	          				
						
						<!--  per soggetto -->
						<s:set var="selezionaSoggettoAction" value="%{'gestioneSubimpegno_selezionaSoggetto'}" />
						<s:set var="pulisciRicercaSoggettoAction" value="%{'gestioneSubimpegno_pulisciRicercaSoggetto'}" />	          
						<s:set var="ricercaSoggettoAction" value="%{'gestioneSubimpegno_ricercaSoggetto'}" />	    
						<s:set var="listaClasseSoggettoChangedAction" value="%{'gestioneSubimpegno_listaClasseSoggettoChanged'}" />	
		
					</s:if>
					<s:else>
						<s:set var="gestisciForwardAction" value="%{'gestioneSubaccertamento_gestisciForward'}" />
						<s:set var="consultaModificheProvvedimentoAction" value="%{'gestioneSubaccertamento_consultaModificheProvvedimento'}" />
						
						<s:set var="siSalvaAction" value="%{'gestioneSubaccertamento_siSalva'}" />	 
						<s:set var="siProseguiAction" value="%{'gestioneSubaccertamento_siProsegui'}" />
						<s:set var="annullaSubImpegnoAction" value="%{'gestioneSubaccertamento_annullaSubImpegno'}" />	 
						<s:set var="annullaSubAccertamentoAction" value="%{'gestioneSubaccertamento_annullaSubAccertamento'}" />	 
						<s:set var="annullaMovGestSpesaAction" value="%{'gestioneSubaccertamento_annullaMovGestSpesa'}" />	
						<s:set var="eliminaSubImpegnoAction" value="%{'gestioneSubaccertamento_eliminaSubImpegno'}" />	 
						<s:set var="eliminaSubAccertamentoAction" value="%{'gestioneSubaccertamento_eliminaSubAccertamento'}" />
						<s:set var="forzaProseguiAction" value="%{'gestioneSubaccertamento_forzaProsegui'}" />	          
						<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'gestioneSubaccertamento_forzaSalvaPluriennaleAccertamento'}" />	          
						<s:set var="salvaConByPassDodicesimiAction" value="%{'gestioneSubaccertamento_salvaConByPassDodicesimi'}" />	          
					
						<!--  per soggetto -->
						<s:set var="selezionaSoggettoAction" value="%{'gestioneSubaccertamento_selezionaSoggetto'}" />
						<s:set var="pulisciRicercaSoggettoAction" value="%{'gestioneSubaccertamento_pulisciRicercaSoggetto'}" />	          
						<s:set var="ricercaSoggettoAction" value="%{'gestioneSubaccertamento_ricercaSoggetto'}" />	    
						<s:set var="listaClasseSoggettoChangedAction" value="%{'gestioneSubaccertamento_listaClasseSoggettoChanged'}" />	
		
					</s:else>
						                     		 					
					<s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
					<h4><s:property value="%{labels.OGGETTO_GENERICO}"/> - Totale: <s:property value="getText('struts.money.format', {totaleSubImpegno})"/> - Disponibile a
						<s:property value="%{labels.OGGETTO_GENERICO_VERBO}"/>: <s:property value="getText('struts.money.format', {disponibilitaSubImpegnare})"/></h4>
					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1" class="active"><span class="badge">1</span><s:property value="%{labels.STEP1}"/><span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge">2</span>Classificazioni<span
								class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div class="step-pane active" id="step1">
						    
						    <h4><s:property value="%{labels.OGGETTO_GENERICO}"/>: <s:property value="subDettaglio.numero"/> - <s:property value="subDettaglio.descrizione"/> <s:if test="oggettoDaPopolareSubimpegno()">Disponibilit&agrave; a liquidare: <s:property value="subDettaglio.disponibilitaLiquidare"/></s:if></h4>
						   
							<s:include value="/jsp/movgest/include/provvedimentoSubimpegno.jsp" />  
				            <div id="refreshHeaderSoggetto">
				            	<s:include value="/jsp/movgest/include/headerSoggettoSubimpegno.jsp"/>
				            </div>
				            <s:include value="/jsp/movgest/include/soggettoSubimpegno.jsp" />              
				            <s:include value="/jsp/movgest/include/datiEntitaSubimpegno.jsp" />
				           
				            <s:if test="oggettoDaPopolareSubimpegno()">
					            <s:set var="selezionaProvvedimentoAction" value="%{'gestioneSubimpegno_selezionaProvvedimento'}" />
					            <s:set var="clearRicercaProvvedimentoAction" value="%{'gestioneSubimpegno_clearRicercaProvvedimento'}" />	          
	        					<s:set var="ricercaProvvedimentoAction" value="%{'gestioneSubimpegno_ricercaProvvedimento'}" />	          
	         					<s:set var="eliminaAction" value="%{'gestioneSubimpegno_elimina'}" />	  
	            				<s:set var="selezionaProvvedimentoInseritoAction" value="%{'gestioneSubimpegno_selezionaProvvedimentoInserito'}" />	
								<s:set var="inserisciProvvedimentoAction" value="%{'gestioneSubimpegno_inserisciProvvedimento'}" />	
								<s:set var="clearInserimentoProvvedimentoAction" value="%{'gestioneSubimpegno_clearInserimentoProvvedimento'}" />	            
								
								<s:set var="aggiornaContemporaneoAction" value="%{'gestioneSubimpegno_aggiornaContemporaneo'}" />	            
							</s:if>
							<s:else>
							    <s:set var="selezionaProvvedimentoAction" value="%{'gestioneSubaccertamento_selezionaProvvedimento'}" />
					            <s:set var="clearRicercaProvvedimentoAction" value="%{'gestioneSubaccertamento_clearRicercaProvvedimento'}" />	          
	        					<s:set var="ricercaProvvedimentoAction" value="%{'gestioneSubaccertamento_ricercaProvvedimento'}" />	          
	         					<s:set var="eliminaAction" value="%{'gestioneSubaccertamento_elimina'}" />	  
	            				<s:set var="selezionaProvvedimentoInseritoAction" value="%{'gestioneSubaccertamento_selezionaProvvedimentoInserito'}" />	
								<s:set var="inserisciProvvedimentoAction" value="%{'gestioneSubaccertamento_inserisciProvvedimento'}" />	
								<s:set var="clearInserimentoProvvedimentoAction" value="%{'gestioneSubaccertamento_clearInserimentoProvvedimento'}" />
							
								<s:set var="aggiornaContemporaneoAction" value="%{'gestioneSubaccertamento_aggiornaContemporaneo'}" />	            
							</s:else>		
				            <s:include value="/jsp/movgest/include/modal.jsp" /> 
				            <br/> <br/>    
							<p>
							<!-- Jira-1584 levare il tasto indietro -->
							<%--	<s:include value="/jsp/include/indietro.jsp" />  --%>
            					<s:if test="oggettoDaPopolareSubimpegno()">
						       		<!--task-131 <s:submit method="annulla" value="annulla" cssClass="btn btn-secondary" ></s:submit> -->
						       		<s:submit action="gestioneSubimpegno_annulla" value="annulla" cssClass="btn btn-secondary" ></s:submit> 
	            					<span class="pull-right">
	            						<!--task-131 <s:submit name="salva" value="salva" id="salvaId" method="salva" cssClass="btn btn-primary freezePagina" /> -->
	            						<s:submit name="salva" value="salva" id="salvaId" action="gestioneSubimpegno_salva" cssClass="btn btn-primary freezePagina" />
	            						<span class="nascosto">| </span>  
										<!--task-131 <s:submit name="prosegui" value="prosegui" method="prosegui" cssClass="btn btn-primary" />	-->
										<s:submit name="prosegui" value="prosegui" action="gestioneSubimpegno_prosegui" cssClass="btn btn-primary" />
									</span>
								</s:if>
								<s:else>
								<!--task-131 <s:submit method="annulla" value="annulla" cssClass="btn btn-secondary" ></s:submit> -->
						       		<s:submit action="gestioneSubaccertamento_annulla" value="annulla" cssClass="btn btn-secondary" ></s:submit> 
	            					<span class="pull-right">
	            						<!--task-131 <s:submit name="salva" value="salva" id="salvaId" method="salva" cssClass="btn btn-primary freezePagina" /> -->
	            						<s:submit name="salva" value="salva" id="salvaId" action="gestioneSubaccertamento_salva" cssClass="btn btn-primary freezePagina" />
	            						<span class="nascosto">| </span>  
										<!--task-131 <s:submit name="prosegui" value="prosegui" method="prosegui" cssClass="btn btn-primary" />	-->
										<s:submit name="prosegui" value="prosegui" action="gestioneSubaccertamento_prosegui" cssClass="btn btn-primary" />
									</span>
								</s:else>
							</p>
							<a id="linkVisualizzaModaleConfermaModificaProvvedimento" href="#modalSalvaModificaProvvedimento" data-toggle="modal" style="display:none;"></a>
							<a id="linkVisualizzaModaleConfermaProseguiModificaProvvedimento" href="#modalProseguiModificaProvvedimento" data-toggle="modal" style="display:none;"></a>
            				<a id="linkmsgPrimaNota" href="#msgPrimaNota" data-toggle="modal" style="display:none;"></a>
            				
						</div>
					</div>
					<!-- SIAC-5333 -->
					<s:include value="/jsp/movgest/include/modalValidaPrimaNota.jsp"/>
		           
		            <s:hidden id="HIDDEN_statoMovimentoGestione" name="statoMovimentoGestioneOld" />
		            
		             <!-- SIAC-5333 --> 
				</s:form>
			</div>
		</div>
	</div>
	
<script type="text/javascript">

	$(document).ready(function() {
		
		$("#linkCompilazioneGuidataProvvedimento").click(function(){
			initRicercaGuidataProvvedimento(
					$("#annoProvvedimento").val(), 
					$("#numeroProvvedimento").val(),
					$("#listaTipiProvvedimenti").val()
			);
		});
		
		$("#linkCompilazioneGuidataSoggettoSubimpegno").click(function(){
			initRicercaGuidataSoggetto(
				$("#codCreditoreSubimpegno").val(),
				null
			);
		});
		
		
		var tipoDebitoSiopeVar = $("input[name='step1ModelSubimpegno.tipoDebitoSiope']");
		<s:include value="/jsp/include/toggleAssenzaCig.jsp" />	
		
		
	});
	
	<s:if test="isShowModaleConfermaModificaProvvedimento()">
		$("#linkVisualizzaModaleConfermaModificaProvvedimento").click();
	</s:if>

	<s:elseif test="isShowModaleConfermaProseguiModificaProvvedimento()">
		$("#linkVisualizzaModaleConfermaProseguiModificaProvvedimento").click();
	</s:elseif>
	<s:elseif test="%{richiediConfermaRedirezioneContabilitaGenerale}">
		<s:if test="oggettoDaPopolareSubimpegno()">
			$('#linkmsgPrimaNota').click();
		 	$('#inserisciPrimaNotaProvvisoria').on('click', function(){
				$('#HIDDEN_saltaInserimentoPrimaNota').val(false);
				$('#HIDDEN_richiediConfermaUtente').val(true);
				
				$('form')
				//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
				.append('<input type="hidden" name="action:gestioneSubimpegno_salva" value="" class="btn" >')
				.submit();
			});
		 	$('#validaPrimaNota').on('click', function(){
				$('#HIDDEN_saltaInserimentoPrimaNota').val(true);
				$('#HIDDEN_richiediConfermaUtente').val(true);
				$('form')
				//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
				.append('<input type="hidden" name="action:gestioneSubimpegno_salva" value="" class="btn" >')
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
				.append('<input type="hidden" name="action:gestioneSubaccertamento_salva" value="" class="btn" >')
				.submit();
			});
		 	$('#validaPrimaNota').on('click', function(){
				$('#HIDDEN_saltaInserimentoPrimaNota').val(true);
				$('#HIDDEN_richiediConfermaUtente').val(true);
				$('form')
				//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
				.append('<input type="hidden" name="action:gestioneSubaccertamento_salva" value="" class="btn" >')
				.submit(); 
				});
		 </s:else>
	</s:elseif>
	
</script>  