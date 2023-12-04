<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

<s:include value="/jsp/include/head.jsp" />
<s:include value="/jsp/include/javascript.jsp" />
<s:include value="/jsp/include/javascriptTree.jsp" />
</head>
<body>
	<s:include value="/jsp/include/header.jsp" />
	<hr />
	<div class="container-fluid-banner">
		<a name="A-contenuti" title="A-contenuti"></a>
	</div>
	
	<s:if test="oggettoDaPopolareSubimpegno()">
		<s:set var="gestisciForwardAction" value="%{'gestioneSubimpegnoStep2_gestisciForward'}" />	                     					
		<s:set var="selezionaProvvedimentoAction" value="%{'gestioneSubimpegnoStep2_selezionaProvvedimento'}" />       
		<s:set var="clearRicercaProvvedimentoAction" value="%{'gestioneSubimpegnoStep2_clearRicercaProvvedimento'}" />	          
	    <s:set var="ricercaProvvedimentoAction" value="%{'gestioneSubimpegnoStep2_ricercaProvvedimento'}" />	          
	    <s:set var="eliminaAction" value="%{'gestioneSubimpegnoStep2_elimina'}" />	
	    <s:set var="codiceSiopeChangedAction" value="%{'gestioneSubimpegnoStep2_codiceSiopeChanged'}" />
		<s:set var="confermaPdcAction" value="%{'gestioneSubimpegnoStep2_confermaPdc'}" />	
		
		<s:set var="siSalvaAction" value="%{'gestioneSubimpegnoStep2_siSalva'}" />	 
		<s:set var="siProseguiAction" value="%{'gestioneSubimpegnoStep2_siProsegui'}" />
		<s:set var="annullaSubImpegnoAction" value="%{'gestioneSubimpegnoStep2_annullaSubImpegno'}" />	 
		<s:set var="annullaSubAccertamentoAction" value="%{'gestioneSubimpegnoStep2_annullaSubAccertamento'}" />	 
		<s:set var="annullaMovGestSpesaAction" value="%{'gestioneSubimpegnoStep2_annullaMovGestSpesa'}" />	
		<s:set var="eliminaSubImpegnoAction" value="%{'gestioneSubimpegnoStep2_eliminaSubImpegno'}" />	 
		<s:set var="eliminaSubAccertamentoAction" value="%{'gestioneSubimpegnoStep2_eliminaSubAccertamento'}" />
		<s:set var="forzaProseguiAction" value="%{'gestioneSubimpegnoStep2_forzaProsegui'}" />	          
		<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'gestioneSubimpegnoStep2_forzaSalvaPluriennaleAccertamento'}" />	          
		<s:set var="salvaConByPassDodicesimiAction" value="%{'gestioneSubimpegnoStep2_salvaConByPassDodicesimi'}" />	          						          
	</s:if>
	<s:else>
		<s:set var="gestisciForwardAction" value="%{'gestioneSubaccertamentoStep2_gestisciForward'}" />	                     						
	    <s:set var="selezionaProvvedimentoAction" value="%{'gestioneSubaccertamentoStep2_selezionaProvvedimento'}" />       
		<s:set var="clearRicercaProvvedimentoAction" value="%{'gestioneSubaccertamentoStep2_clearRicercaProvvedimento'}" />	          
	    <s:set var="ricercaProvvedimentoAction" value="%{'gestioneSubaccertamentoStep2_ricercaProvvedimento'}" />	          
	    <s:set var="eliminaAction" value="%{'gestioneSubaccertamentoStep2_elimina'}" />	
	    <s:set var="codiceSiopeChangedAction" value="%{'gestioneSubaccertamentoStep2_codiceSiopeChanged'}" />
		<s:set var="confermaPdcAction" value="%{'gestioneSubaccertamentoStep2_confermaPdc'}" />		  
				
		<s:set var="siSalvaAction" value="%{'gestioneSubaccertamentoStep2_siSalva'}" />	 
		<s:set var="siProseguiAction" value="%{'gestioneSubaccertamentoStep2_siProsegui'}" />
		<s:set var="annullaSubImpegnoAction" value="%{'gestioneSubaccertamentoStep2_annullaSubImpegno'}" />	 
		<s:set var="annullaSubAccertamentoAction" value="%{'gestioneSubaccertamentoStep2_annullaSubAccertamento'}" />	 
		<s:set var="annullaMovGestSpesaAction" value="%{'gestioneSubaccertamentoStep2_annullaMovGestSpesa'}" />	
		<s:set var="eliminaSubImpegnoAction" value="%{'gestioneSubaccertamentoStep2_eliminaSubImpegno'}" />	 
		<s:set var="eliminaSubAccertamentoAction" value="%{'gestioneSubaccertamentoStep2_eliminaSubAccertamento'}" />
		<s:set var="forzaProseguiAction" value="%{'gestioneSubaccertamentoStep2_forzaProsegui'}" />	          
		<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'gestioneSubaccertamentoStep2_forzaSalvaPluriennaleAccertamento'}" />	          
		<s:set var="salvaConByPassDodicesimiAction" value="%{'gestioneSubaccertamentoStep2_salvaConByPassDodicesimi'}" />
	</s:else>
	            			

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form id="%{labels.FORM}" action="%{labels.FORM}" method="post">
	<!-- 		<h3>
					
					<s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/>
					<s:property value="step1Model.annoImpegno" />
					/
					<s:property value="step1Model.numeroImpegno" />
					-
					<s:property value="step1Model.oggettoImpegno" />
					-
					<s:property value="step1Model.importoFormattato" />
				</h3>	-->
				
				<h3><s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
				
					<s:include value="/jsp/include/actionMessagesErrors.jsp" />
					<s:include value="/jsp/movgest/include/tabsSubimpegno.jsp" />
					
					<s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
					<h4><s:property value="%{labels.OGGETTO_GENERICO}"/> - Totale: <s:property value="getText('struts.money.format', {totaleSubImpegno})"/> - Disponibile a
						<s:property value="%{labels.OGGETTO_GENERICO_VERBO}"/>: <s:property value="getText('struts.money.format', {disponibilitaSubImpegnare})"/></h4>
					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1" class="active"><span class="badge">1</span><s:property value="%{labels.STEP1}"/><span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge">2</span>Classificazioni<span
								class="chevron"></span></li>
							<!--li data-target="#step3"><span class="badge">3</span>Impegni pluriennali<span class="chevron"></span></li-->
							<!--<li data-target="#step4"><span class="badge">4</span>Salva<span class="chevron"></span></li> -->
						</ul>
					</div>
					<div class="step-content">
						<div class="step-pane active" id="step2">
							<s:include
								value="/jsp/movgest/include/headerDettaglioMovGestSubimpegno.jsp" />
						</div>
						<s:include value="/jsp/include/transazioneElementare.jsp" />
					</div>
					<p class="margin-large">

						<!-- Modal -->
						
						<s:include value="/jsp/movgest/include/modal.jsp" />

						<s:include value="/jsp/include/indietro.jsp" />

<!-- 					<a class="btn btn-secondary" href="">annulla</a>  -->
						<s:if test="oggettoDaPopolareSubimpegno()">
							<!-- task-131 <s:submit name="annulla" value="annulla" method="annulla"	cssClass="btn btn-secondary" /> -->
							<s:submit name="annulla" value="annulla" action="gestioneSubimpegnoStep2_annulla"	cssClass="btn btn-secondary" /><!-- task-207 -->
							
							<span class="pull-right"> <!-- <a class="btn btn-primary" href="FIN-InsImpegnoStep3.shtml">conferma e prosegui</a> -->
								<!-- task-131 <s:submit name="salva" value="salva" method="salva" id="salvaId" cssClass="btn btn-primary freezePagina" /> -->
								<s:submit name="salva" value="salva" action="gestioneSubimpegnoStep2_salva" id="salvaId" cssClass="btn btn-primary freezePagina" />
							</span>
						</s:if>		
						<s:else>
							<!-- task-131 <s:submit name="annulla" value="annulla" method="annulla"	cssClass="btn btn-secondary" /> -->
							<s:submit name="annulla" value="annulla" action="gestioneSubaccertamentoStep2_annulla"	cssClass="btn btn-secondary" /> <!-- task-207 -->
							
							<span class="pull-right"> <!-- <a class="btn btn-primary" href="FIN-InsImpegnoStep3.shtml">conferma e prosegui</a> -->
								<!-- task-131 <s:submit name="salva" value="salva" method="salva" id="salvaId" cssClass="btn btn-primary freezePagina" /> -->
								<s:submit name="salva" value="salva" action="gestioneSubaccertamentoStep2_salva" id="salvaId" cssClass="btn btn-primary freezePagina" />
							</span>
						</s:else>						
					</p>
					<a id="linkmsgPrimaNota" href="#msgPrimaNota" data-toggle="modal" style="display:none;"></a>
					<s:if test="%{richiediConfermaRedirezioneContabilitaGenerale}">
			           <div id="msgPrimaNota" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgPrimaNota" aria-hidden="true">
			             <div class="modal-body">
			               <div class="alert alert-warning">
			                 <button type="button" class="close" data-dismiss="alert">&times;</button>
			                 <p>
			                 	<s:property value="messaggioProsecuzioneSuContabilitaGenerale" escapeHtml="false"/>
			                 </p>
			               </div>
			             </div>
			             <div class="modal-footer">
			               <button class="btn" id="inserisciPrimaNotaProvvisoria" data-dismiss="modal" aria-hidden="true">valida in seguito</button>
			               <button class="btn" id="validaPrimaNota" data-dismiss="modal" aria-hidden="true">valida ora</button>
			             </div>
			           </div>
					</s:if>
					<s:hidden id="HIDDEN_saltaInserimentoPrimaNota" name="saltaInserimentoPrimaNota"/>
					<s:hidden id="HIDDEN_richiediConfermaUtente" name="richiediConfermaUtente"/>
		            <!-- SIAC-5333 --> 
		            <!-- SIAC-5943 passaggio a definitivo -->
		            <s:hidden id="HIDDEN_passaggioADefinitivo" name="passaggioAStatoDefinitivo"/>
				</s:form>
			</div>
		</div>
	</div>
	<script type="text/javascript">

		$(document).ready(function() {
			
			<s:if test="%{richiediConfermaRedirezioneContabilitaGenerale}">
				<s:if test="oggettoDaPopolareSubimpegno()">
					$('#linkmsgPrimaNota').click();
				 	$('#inserisciPrimaNotaProvvisoria').on('click', function(){
						$('#HIDDEN_saltaInserimentoPrimaNota').val(false);
						$('#HIDDEN_richiediConfermaUtente').val(true);
						
						$('form')
						//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
						.append('<input type="hidden" name="action:gestioneSubimpegnoStep2_salva" value="" class="btn" >')
						.submit();
					});
				 	$('#validaPrimaNota').on('click', function(){
						$('#HIDDEN_saltaInserimentoPrimaNota').val(true);
						$('#HIDDEN_richiediConfermaUtente').val(true);
						$('form')
						//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
						.append('<input type="hidden" name="action:gestioneSubimpegnoStep2_salva" value="" class="btn" >')
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
						.append('<input type="hidden" name="action:gestioneSubaccertamentoStep2_salva" value="" class="btn" >')
						.submit();
					});
				 	$('#validaPrimaNota').on('click', function(){
						$('#HIDDEN_saltaInserimentoPrimaNota').val(true);
						$('#HIDDEN_richiediConfermaUtente').val(true);
						$('form')
						//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
						.append('<input type="hidden" name="action:gestioneSubaccertamentoStep2_salva" value="" class="btn" >')				
						.submit(); 
						});
			</s:else>
			</s:if>

		 });  
	</script>
	<s:include value="/jsp/include/footer.jsp" />