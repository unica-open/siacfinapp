<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>


<!--#include virtual="../../ris/servizi/siac/include/head.html" -->
<!--
=======================================================================
											*****************
											fine inclusione HEAD
											*****************
=======================================================================
-->
<s:include value="/jsp/include/head.jsp" />
<s:include value="/jsp/include/javascript.jsp" />	
<s:include value="/jsp/include/javascriptTree.jsp" />
</head>

<%-- Inclusione head e CSS NUOVO --%>

 <s:if test="oggettoDaPopolareImpegno()">
	<s:set var="oggetto" value="%{'Impegno'}" />
	<s:set var="proseguiSalvaAction" value="%{'aggiornaImpegnoStep2_proseguiSalva'}" />	
	  	    
	<s:set var="gestisciForwardAction" value="%{'aggiornaImpegnoStep2_gestisciForward'}" />	  
	<s:set var="siSalvaAction" value="%{'aggiornaImpegnoStep2_siSalva'}" />	 
	<s:set var="siProseguiAction" value="%{'aggiornaImpegnoStep2_siProsegui'}" />	
	<s:set var="annullaSubImpegnoAction" value="%{'aggiornaImpegnoStep2_annullaSubImpegno'}" />	 
	<s:set var="annullaSubAccertamentoAction" value="%{'aggiornaImpegnoStep2_annullaSubAccertamento'}" />	 
	<s:set var="annullaMovGestSpesaAction" value="%{'aggiornaImpegnoStep2_annullaMovGestSpesa'}" />	 
	<s:set var="eliminaSubImpegnoAction" value="%{'aggiornaImpegnoStep2_eliminaSubImpegno'}" />	 
	<s:set var="eliminaSubAccertamentoAction" value="%{'aggiornaImpegnoStep2_eliminaSubAccertamento'}" />
	<s:set var="forzaProseguiAction" value="%{'aggiornaImpegnoStep2_forzaProsegui'}" />	          
	<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'aggiornaImpegnoStep2_forzaSalvaPluriennaleAccertamento'}" />	          
	<s:set var="salvaConByPassDodicesimiAction" value="%{'aggiornaImpegnoStep2_salvaConByPassDodicesimi'}" />	          
	
	<s:set var="codiceSiopeChangedAction" value="%{'aggiornaImpegnoStep2_codiceSiopeChanged'}" />
    <s:set var="confermaPdcAction" value="%{'aggiornaImpegnoStep2_confermaPdc'}" />
</s:if>
<s:else>
	<s:set var="oggetto" value="%{'Accertamento'}" />
	<s:set var="proseguiSalvaAction" value="%{'aggiornaAccertamentoStep2_proseguiSalva'}" />	
	
	<s:set var="gestisciForwardAction" value="%{'aggiornaAccertamentoStep2_gestisciForward'}" />	
	<s:set var="siSalvaAction" value="%{'aggiornaAccertamentoStep2_siSalva'}" />	 
	<s:set var="siProseguiAction" value="%{'aggiornaAccertamentoStep2_siProsegui'}" />	
	<s:set var="annullaSubImpegnoAction" value="%{'aggiornaAccertamentoStep2_annullaSubImpegno'}" />	 
	<s:set var="annullaSubAccertamentoAction" value="%{'aggiornaAccertamentoStep2_annullaSubAccertamento'}" />	 
	<s:set var="annullaMovGestSpesaAction" value="%{'aggiornaAccertamentoStep2_annullaMovGestSpesa'}" />	 
	<s:set var="eliminaSubImpegnoAction" value="%{'aggiornaAccertamentoStep2_eliminaSubImpegno'}" />	 
	<s:set var="eliminaSubAccertamentoAction" value="%{'aggiornaAccertamentoStep2_eliminaSubAccertamento'}" />
	<s:set var="forzaProseguiAction" value="%{'aggiornaAccertamentoStep2_forzaProsegui'}" />	          
	<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'aggiornaAccertamentoStep2_forzaSalvaPluriennaleAccertamento'}" />	          
	<s:set var="salvaConByPassDodicesimiAction" value="%{'aggiornaAccertamentoStep2_salvaConByPassDodicesimi'}" />	          
	
	<s:set var="codiceSiopeChangedAction" value="%{'aggiornaAccertamentoStep2_codiceSiopeChanged'}" />
    <s:set var="confermaPdcAction" value="%{'aggiornaAccertamentoStep2_confermaPdc'}" />
</s:else> 

<body>

	<s:include value="/jsp/include/header.jsp" />


	<!-- NAVIGAZIONE
  <p class="nascosto"><a name="A-sommario" title="A-sommario"></a></p>
     
  <ul id="sommario" class="nascosto">
    <li><a href="#A-contenuti">Salta ai contenuti</a></li>
  </ul>
  <!-- /NAVIGAZIONE -->
	<hr />
	<div class="container-fluid-banner">
		<!-- 
=======================================================================
											*****************
											inclusione Banner portale
											*****************
=======================================================================
-->


		<!--#include virtual="../../ris/servizi/siac/include/portalheader.html" -->

		<!--
=======================================================================
											*****************
											fine ////////inclusione Banner portale
											*****************
=======================================================================
-->
		<!--#include virtual="../navbarLogin.html" -->
		<!--
=======================================================================
											*****************
											inclusione Banner applicativo
											*****************
=======================================================================
-->
		<!--#include virtual="../../ris/servizi/siac/include/applicationHeader.html" -->
		<!--
=======================================================================
											*****************
											fine //////inclusione Banner applicativo
											*****************
=======================================================================
-->
		<!--
=======================================================================
											*****************
											inclusione login
											*****************
=======================================================================
-->
		<!--
=======================================================================
											*****************
											fine //////inclusione login
											*****************
=======================================================================
-->
		<a name="A-contenuti" title="A-contenuti"></a>
	</div>

	<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">    
       <s:form id="%{labels.FORM}" action="%{labels.FORM}" method="post">
       
    
	      
		<!-- 	<h3><s:property value="%{labels.TITOLO}"/> ${model.step1Model.annoImpegno}/ ${model.step1Model.numeroImpegno} - ${model.step1Model.oggettoImpegno} - <s:property value="getText('struts.money.format', {step1Model.importoImpegno})"/></h3>	-->

		<!-- Questa e' la modale per la conferma dell'operazione -->
		<div id="msgControlloMovColl" class="modal hide fade" tabindex="-1" role="dialog"  aria-hidden="true">
			<div class="modal-body">
				<div class="alert alert-warning">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
    				<p><strong>Attenzione!</strong></p>
                   <s:iterator value="actionWarnings">
		     	  		<s:property escapeHtml="false"/><br>
		   		   </s:iterator>
				</div>
			</div>
			<div class="modal-footer">
                <!-- <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button> -->
				<!-- task-131 <s:submit id="annullaMovCollBtn" name="btnAnnullaMovColl" method="annulla" value="no, annulla" cssClass="btn btn-secondary" /> -->
				<s:submit id="annullaMovCollBtn" name="btnAnnullaMovColl" action="aggiorna%{#oggetto}Step2_annulla" value="no, annulla" cssClass="btn btn-secondary" />
				
				
				<s:if test="oggettoDaPopolareImpegno()"> 
	            	<s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, salva" action="aggiorna%{#oggetto}Step2_siSalva" cssClass="btn btn-primary" />										 
				</s:if>
				<s:else> 
					<s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, salva" action="aggiorna%{#oggetto}Step2_siSalva" cssClass="btn btn-primary" />	
				</s:else>
				
			</div>
		</div>

				<h3>Aggiorna <s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
					
					<s:if test="%{successStep2}">
						<div class="alert alert-success margin-medium">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							L'operazione &egrave; stata completata con successo
						</div>   
					</s:if>

					<s:include value="/jsp/include/actionMessagesErrors.jsp" />
					<s:include value="/jsp/movgest/include/tabAggImpegnoPerStep.jsp" />
					
					<s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />

					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1" class="complete"><span
								class="badge badge-success">1</span><s:property value="%{labels.STEP1}"/><span
								class="chevron"></span></li>
							<li data-target="#step2" class="active"><span class="badge">2</span>Classificazioni<span
								class="chevron"></span></li>
							<!--li data-target="#step3"><span class="badge">3</span>Impegni pluriennali<span class="chevron"></span></li-->
							<!--						<li data-target="#step4"><span class="badge">4</span>Salva<span class="chevron"></span></li> -->
						</ul>
					</div>

					<div class="step-content">
						<div class="step-pane active" id="step2">
							<!-- dettaglio con capitolo/provvedimento/soggetto -->
							<s:include
								value="/jsp/movgest/include/headerDettaglioMovGest.jsp" />
						</div>
						
						
						<s:include value="/jsp/include/transazioneElementare.jsp" />
						
						<s:include value="/jsp/include/gestioneSanita.jsp" />
						
					</div>
			</div>
			<!-- questi pulsanti servono se non Ã¨ pluriennale -->
			<!-- questi pulsanti servono se Ã¨ pluriennale -->
			<!--p class="margin-large"><input class="btn" type="button" value="indietro"/>   <a class="btn btn-link" href="">annulla</a>    <a class="btn btn-primary pull-right" href="FIN-AggImpegno3.shtml">conferma e prosegui</a>  </p-->
			<br />
			<!--  <p><a class="btn btn-secondary" href="javascript:history.go(-1)">indietro</a>   <a class="btn btn-secondary" href="">annulla</a><span class="pull-right"><a class="btn btn-primary" href="FIN-Subimpegno.shtml">Salva</a><span class="nascosto"> | </span> <!--a class="btn btn-primary" href="FIN-AggImpegno3.shtml">Prosegui</a></span-->
			
					
            <s:include value="/jsp/movgest/include/modal.jsp" />
			
			<s:include value="/jsp/movgest/include/modalSalvaSdf.jsp"/>
			
			
			<p>
			<%-- <s:include value="/jsp/include/indietroSubmit.jsp" /> --%>
			<!-- task-131 <s:submit name="indietro" value="indietro" method="indietroStep2" cssClass="btn btn-secondary" /> -->
			<s:submit name="indietro" value="indietro" action="aggiorna%{#oggetto}Step2_indietroStep2" cssClass="btn btn-secondary" />
			
			<a class="btn btn-secondary" href="">annulla</a> 
			
			
			<%-- SIAC-7320 viene disaccoppiata la gestione delle modifiche dall'azione decentrata
			<s:if test="disabilitaTabModificheAggiornamento()"> 
				<s:if test="isAbilitaBottneSalvaDecentrato()">
					 <s:submit name="salva"  value="salva" id="salvaId" method="salva" cssClass="btn btn-primary freezePagina" />
				</s:if>
			</s:if> --%>
			<s:if test="salvaDaNormaleASDF()"> 
				<div class="btn btn-primary pull-right">
					<a id="linkSalvaConConferma" href="#msgConfermaSDF" data-toggle="modal" class="linkConfermaSDF">
					salva</a> 
				</div>
			</s:if>
			<s:elseif test="salvaDaSDFANormale()">
				<div class="btn btn-primary pull-right">
					<a id="linkSalvaConConfermaDaSdfANormale" href="#msgConfermaSDFDiNuovoDisp" data-toggle="modal" class="linkConfermaSDFANormale">
					salva</a> 
				</div>
			</s:elseif>
					<%-- <s:elseif test="!isAbilitaBottneSalvaDecentrato()">
							<!-- Nn si visualizza nulla! -->
					</s:elseif> --%>
			<s:elseif test="isAbilitaBottneSalvaDecentrato()">
				<!-- task-131 <s:submit name="salva"  value="salva" id="salvaId" method="salva" cssClass="btn btn-primary freezePagina" /> -->
				<s:submit name="salva"  value="salva" id="salvaId" action="aggiorna%{#oggetto}Step2_salva" cssClass="btn btn-primary freezePagina" />
			</s:elseif>
			
			<a id="linkMsgControlloMovColl" href="#msgControlloMovColl" data-toggle="modal" style="display:none;"></a>
			
			<!-- DODICESIMI: -->
			<a id="linkVisualizzaModaleConfermaSalvaConBypassDodicesimi" href="#modalSalvaConBypassDodicesimi" data-toggle="modal" style="display:none;"></a>
            
            <a id="linkmsgPrimaNota" href="#msgPrimaNota" data-toggle="modal" style="display:none;"></a>
            
            
			</p>
			
		  
			<!-- SIAC-5333 -->
			<s:include value="/jsp/movgest/include/modalValidaPrimaNota.jsp"/>
            <!-- SIAC-5333 --> 
	 	  
		  
			</s:form>
		</div>
	</div>
	</div>
	<!-- 
=======================================================================
											*****************
											inclusione footer portale
											*****************
=======================================================================
-->

	<!-- Footer
    ================================================== -->
	<!--#include virtual="../../ris/servizi/siac/include/portalFooter.html" -->



	<!--#include virtual="../../ris/servizi/siac/include/javascript.html" -->
	<!--
=======================================================================
											*****************
											fine //////inclusione footer portale
											*****************
=======================================================================
-->



<script type="text/javascript">

	<s:if test="%{!richiediConfermaRedirezioneContabilitaGenerale}">

		<s:if test="isShowPopUpMovColl()">
			$("#linkMsgControlloMovColl").click();
		</s:if>
		 <s:if test="isShowModaleConfermaSalvaConBypassDodicesimi()">
			$("#linkVisualizzaModaleConfermaSalvaConBypassDodicesimi").click();
		</s:if>
		
	</s:if>
	<s:else>
		<s:if test="oggettoDaPopolareImpegno()">
			$('#linkmsgPrimaNota').click();
		 	$('#inserisciPrimaNotaProvvisoria').on('click', function(){
				$('#HIDDEN_saltaInserimentoPrimaNota').val(false);
				$('#HIDDEN_richiediConfermaUtente').val(true);
				
				$('form')
				//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
				.append('<input type="hidden" name="action:aggiornaImpegnoStep2_salva" value="" class="btn" >')
				.submit();
			});
			$('#validaPrimaNota').on('click', function(){
				$('#HIDDEN_saltaInserimentoPrimaNota').val(true);
				$('#HIDDEN_richiediConfermaUtente').val(true);
				$('form')
				//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
				.append('<input type="hidden" name="action:aggiornaImpegnoStep2_salva" value="" class="btn" >')
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
			.append('<input type="hidden" name="action:aggiornaAccertamentoStep2_salva" value="" class="btn" >')
			.submit();
		});
		$('#validaPrimaNota').on('click', function(){
			$('#HIDDEN_saltaInserimentoPrimaNota').val(true);
			$('#HIDDEN_richiediConfermaUtente').val(true);
			$('form')
			//task-131 .append('<input type="hidden" name="method:salva" value="" class="btn" >')
			.append('<input type="hidden" name="action:aggiornaAccertamentoStep2_salva" value="" class="btn" >')
			.submit(); 
			});
		</s:else>
	</s:else>
		
</script>  

	<s:include value="/jsp/include/footer.jsp" />