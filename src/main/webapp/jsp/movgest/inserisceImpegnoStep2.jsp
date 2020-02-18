<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%-- Inclusione head e CSS NUOVO --%>
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
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form id="%{labels.FORM}" action="%{labels.FORM}.do" method="post">
					<h3>
						<s:property value="%{labels.TITOLO}" />
					</h3>
					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1" class="complete"><span
								class="badge badge-success">1</span>
							<s:property value="%{labels.STEP1}" /><span class="chevron"></span></li>
							<li data-target="#step2" class="active"><span class="badge">2</span>Classificazioni<span
								class="chevron"></span></li>
							<li data-target="#step3"><span class="badge">3</span>
							<s:property value="%{labels.STEP3}" /><span class="chevron"></span></li>
						</ul>
					</div>
					<s:include value="/jsp/include/actionMessagesErrors.jsp" />
					<div class="step-content">
						<div class="step-pane active" id="step2">
							<!-- dettaglio con capitolo/provvedimento/soggetto -->
							<s:include value="/jsp/movgest/include/headerDettaglioMovGest.jsp" />
						</div>
						<s:include value="/jsp/include/transazioneElementare.jsp" />
						
						<s:include value="/jsp/include/gestioneSanita.jsp" />
						
					</div>
			</div>
			<!-- questi pulsanti servono se non e' pluriennale -->
			<!--p class="margin-large"><a class="btn btn-secondary" href="javascript:history.go(-1)">indietro</a>   <a class="btn btn-secondary" href="">annulla</a>    <a class="btn btn-primary pull-right" href="FIN-AggImpegno.shtml">salva</a> </p-->
			<!-- questi pulsanti servono se e' pluriennale -->
			<p class="margin-large">

				<!-- Modal -->
				<s:include value="/jsp/movgest/include/modal.jsp" />
				
				<s:include value="/jsp/movgest/include/modalSalvaSdf.jsp"/>
				
				 <s:include value="/jsp/movgest/include/modalConfermaScritturaGenPerMovimentoPluriennale.jsp"/>
				

				<s:include value="/jsp/include/indietroSubmit.jsp" />
				
				

<!-- 				<a class="btn btn-secondary" href="">annulla</a>  -->
			    <s:submit method="annulla" value="annulla" cssClass="btn btn-secondary"></s:submit>
				<span class="pull-right"> <!--         <a class="btn btn-primary" href="FIN-InsImpegnoStep3.shtml">conferma e prosegui</a> -->



					<s:if test="model.step1Model.pluriennale==null || model.step1Model.pluriennale=='No'">
						<!-- sono in pluriennale -->
						<s:if test="salvaConSDF()"> 
							<!-- devo salvare SDF -->
							<div class="btn btn-primary">
							<a id="linkSalvaConConferma" href="#msgConfermaSDF" data-toggle="modal" class="linkConfermaSDF">
							salva</a> 
						</div>
						</s:if>
						<s:else>
							<!-- non devo salvare sdf -->
							<s:submit name="salva"  value="salva" id="salvaId" method="salva" cssClass="btn btn-primary freezePagina" />
						</s:else>
	
					</s:if> 
					<s:else>
						<!-- posso andare avanti -->
						<s:submit name="prosegui" value="conferma e prosegui" id="proseguiId" method="prosegui" cssClass="btn btn-primary freezePagina" />
					</s:else>

				</span>
				
				<!-- DODICESIMI: -->
				<a id="linkVisualizzaModaleConfermaSalvaConBypassDodicesimi" href="#modalSalvaConBypassDodicesimi" data-toggle="modal" style="display:none;"></a>
				<a id="linkVisualizzaModaleConfermaPrimeNoteStep2" href="#msgConfermaPrimeNoteStep2" data-toggle="modal" style="display:none;"></a>
				<a id="linkmsgPrimaNota" href="#msgPrimaNota" data-toggle="modal" style="display:none;"></a>
			</p>
			
			<!-- SIAC-5333 -->
			<s:include value="/jsp/movgest/include/modalValidaPrimaNota.jsp"/>
            <!-- SIAC-5333 --> 

			</s:form>
		</div>
	</div>
	</div>
	
	 <script type="text/javascript">
	 
		 <s:if test="isShowModaleConfermaSalvaConBypassDodicesimi()">
			$("#linkVisualizzaModaleConfermaSalvaConBypassDodicesimi").click();
		</s:if><s:elseif test="%{richiediConfermaRedirezioneContabilitaGenerale}">
			$('#linkmsgPrimaNota').click();
		 	$('#inserisciPrimaNotaProvvisoria').on('click', function(){
				$('#HIDDEN_saltaInserimentoPrimaNota').val(false);
				$('#HIDDEN_richiediConfermaUtente').val(true);
				
				$('form')
				.append('<input type="hidden" name="method:salva" value="" class="btn" >')
				.submit();
			});
			$('#validaPrimaNota').on('click', function(){
				$('#HIDDEN_saltaInserimentoPrimaNota').val(true);
				$('#HIDDEN_richiediConfermaUtente').val(true);
				$('form')
				.append('<input type="hidden" name="method:salva" value="" class="btn" >')
				.submit(); 
				});
		</s:elseif>
		 
		
		<s:if test="isShowModaleConfermaAnnoDiCompetenzaInCorso()">
			$("#linkVisualizzaModaleConfermaPrimeNoteStep2").click();
		</s:if>
		
	 </script>  
	

	<s:include value="/jsp/include/footer.jsp" />