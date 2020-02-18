<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>


<%-- Inclusione head e CSS NUOVO --%>
<s:include value="/jsp/include/head.jsp" />
  

</head>

<body>
  
  <s:include value="/jsp/include/header.jsp" />
  
     <%-- Inclusione JavaScript NUOVO --%>
    <s:include value="/jsp/include/javascript.jsp" />	
  	<s:include value="/jsp/include/javascriptTree.jsp" />
  
    
  <!-- NAVIGAZIONE
  <p class="nascosto"><a name="A-sommario" title="A-sommario"></a></p>     
  <ul id="sommario" class="nascosto">
    <li><a href="#A-contenuti">Salta ai contenuti</a></li>
  </ul>
   /NAVIGAZIONE -->
  <hr />
<div class="container-fluid-banner">


  <a name="A-contenuti" title="A-contenuti"></a>
</div>

<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">
    	<s:form class="form-horizontal" id="%{labels.FORM}" action="%{labels.FORM}.do" method="post" >
  
          <div id="msgControlloMovColl" class="modal hide fade" tabindex="-1" role="dialog"  aria-hidden="true">
			<div class="modal-body">
				<div class="alert alert-warning">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
    				<p><strong>Attenzione!</strong></p>
                   <s:iterator value="actionWarnings">
		     	  		<s:property/><br>
		   		   </s:iterator>
				</div>
			</div>
			<div class="modal-footer">
                <!-- <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button> -->
				<s:submit id="annullaMovCollBtn" name="btnAnnullaMovColl" method="annulla" value="no, annulla" cssClass="btn btn-secondary" />
				<s:if test="isProseguiStep1()">
                	<s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, prosegui" cssClass="btn btn-primary" method="siProsegui"/>
                </s:if>
                <s:else>
                	<s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, salva" cssClass="btn btn-primary" method="siSalva"/>
                </s:else>
			</div>
		</div>
      	 
  
  
  
      	 <h3>Aggiorna <s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
      	 
      	 
      	<%--  <s:if test="%{successStep1}">
				<div class="alert alert-success margin-medium">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					L'operazione &egrave; stata completata con successo
				</div>   
			</s:if>  --%>
      	   
      	 <s:include value="/jsp/include/actionMessagesErrors.jsp" />
		 <s:include value="/jsp/movgest/include/tabAggImpegnoPerStep.jsp" />
		 <s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
			
        <div id="MyWizard" class="wizard">
          <ul class="steps">
            <li data-target="#step1" class="active"><span class="badge ">1</span><s:property value="%{labels.STEP1}"/><span class="chevron"></span></li>
            <li data-target="#step2" ><span class="badge">2</span>Classificazioni<span class="chevron"></span></li>
            <!--li data-target="#step3"><span class="badge">3</span>Impegni pluriennali<span class="chevron"></span></li-->
          </ul>
        </div>
        <div class="step-content">
          <div class="step-pane active" id="step1">

			
                		
			 <h4>
				  Capitolo <a class="tooltip-test" title="Visualizza dettagli" href="#capitoloTab" data-toggle="modal"><i class="icon-info-sign">&nbsp;<span class="nascosto">Visualizza dettagli</span></i></a></dt>
				  <s:property value="step1Model.capitolo.anno"/>/<s:property value="step1Model.capitolo.numCapitolo"/>/<s:property value="step1Model.capitolo.articolo"/>/<s:property value="step1Model.capitolo.ueb"/>  - <s:property value="step1Model.capitolo.descrizione"/>  -  <s:property value="step1Model.capitolo.codiceStrutturaAmministrativa"/> - tipo Finanziamento: <s:property value="step1Model.capitolo.tipoFinanziamento"/></dd>              				
			</h4>	
	            
			<s:include value="/jsp/movgest/include/provvedimento.jsp" />
               
            <s:if test="oggettoDaPopolareImpegno()">    
	            <!-- CR 1965 Parere finanziario -->   
	            <h4>Parere finanziario</h4>
				<div class="control-group">
						<label class="control-label">Parere finanziario</label>
						<div class="controls">
							<s:checkbox id="parereFinanziario" name="step1Model.parereFinanziario" disabled="!abilitaModificaParereFinanziario()" />
						</div>
						
						<s:if test="step1Model.parereFinanziarioDataModifica != null">
							 <span class="al">
	        					Data modifica:  <s:property value="%{step1Model.parereFinanziarioDataModifica}"/>
	        					-
	        					Login modifica:  <s:property value="step1Model.parereFinanziarioLoginOperazione"/>
	      					</span>
      					</s:if>
      					
      					<!-- 
      					<s:if test="isImpegnoSdf()">   
	      					<span class="guidata">
	      						<label class="control-label" style="text-decoration: underline;">ATTENZIONE Impegno Senza Disponibilita' Fondi !</label>
	      					</span>
      					</s:if>
      					 -->
						
			     </div>  
            </s:if>
               
            <div id="refreshHeaderSoggetto">
            	<s:include value="/jsp/movgest/include/headerSoggetto.jsp"/>
            </div>
<%--             <s:include value="/jsp/movgest/include/soggettoAggiorna.jsp" />  --%>
				 <s:include value="/jsp/movgest/include/soggetto.jsp" />
				 
            <s:include value="/jsp/movgest/include/datiEntita_aggimp.jsp" />
            
             <!-- Vincolo -->
            <s:if test="oggettoDaPopolareImpegno()"> 
             	<a id="ancoraVincoli"></a>
             	<s:include value="/jsp/movgest/include/tabVincolo.jsp" /> 
            </s:if> 
            <!-- fine Vincolo -->
            
            <s:include value="/jsp/movgest/include/modal.jsp" />
            
            <s:include value="/jsp/movgest/include/modalSalvaSdf.jsp"/>
                                   
                                   
            <s:include value="/jsp/movgest/include/modalAccVincoli.jsp" />  
            
            <!--modale progetto -->
			<s:include value="/jsp/movgest/include/modalProgettoCronop.jsp"/>	
			<!--/modale progetto -->
                        
                        
			<s:include value="/jsp/include/modalCronop.jsp"/>	
                                   
            <br/> <br/>                                         
            <p>

			<s:if test="%{!flagIndietro}">
				<s:include value="/jsp/include/indietro.jsp" />
			</s:if>
			<s:else>
				<s:submit name="indietro" value="indietro" method="indietro" cssClass="btn btn-secondary" />
			</s:else>

			<s:hidden id="doveMiTrovo" name="doveMiTrovo" value="Aggiornamento "></s:hidden>
	        
	        <s:hidden id="hiddenPerPrenotazioneLiquidabile" name="step1Model.hiddenPerPrenotazioneLiquidabile" />
	               
	        <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
	        <s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
			
			<%-- CR-2023 da eliminare 
			<s:hidden id="ricaricaAlberoContoEconomico" name="teSupport.ricaricaAlberoContoEconomico"/> 
			--%>
			
			<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
			<s:hidden id="ricaricaSiopeSpesa" name="teSupport.ricaricaSiopeSpesa"/>
			<!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->

			<s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden>

			<%-- <s:if test="!isAbilitatoGestisciImpegnoDecentratoP()"> --%>
				<s:submit method="annulla" value="annulla" cssClass="btn btn-secondary" ></s:submit>
			<%-- </s:if> --%>
			
            <span class="pull-right">
            
            	<%-- <s:if test="!isAbilitatoGestisciImpegnoDecentratoP()"> --%>
	            	<s:submit name="ripeti" value="ripeti" method="ripeti" cssClass="btn btn-primary"  />
	            	<s:submit name="prosegui" value="prosegui" method="prosegui" cssClass="btn btn-primary"  />
		            
					<!-- con disabilitaTabModificheAggiornamento == true &&  abilitaBottneSalvaDecentrato == false il bottone non si abilita! -->
					
					<%-- SIAC-7320 viene disaccoppiata la gestione delle modifiche dall'azione decentrata
					<s:if test="disabilitaTabModificheAggiornamento()">
							
						<s:if test="isAbilitaBottneSalvaDecentrato()"> 
							<s:submit name="salva"  value="salva" id="salvaId" method="salva" cssClass="btn btn-primary freezePagina" />
						</s:if>	
						
					</s:if> --%>
					<s:if test="salvaDaNormaleASDF()"> 
						<div class="btn btn-primary pull-right">
							<a id="linkSalvaConConferma" href="#msgConfermaSDF" data-toggle="modal" class="pull-right">
							salva</a> 
						</div>
					</s:if>
					<s:elseif test="salvaDaSDFANormale()">
						<div class="btn btn-primary pull-right">
							<a id="linkSalvaConConfermaDaSdfANormale" href="#msgConfermaSDFDiNuovoDisp" data-toggle="modal" class="pull-right">
							salva</a> 
						</div>
					</s:elseif>
					<s:elseif test="isAbilitaBottneSalvaDecentrato()">
							<s:submit name="salva"  value="salva" id="salvaId" method="salva" cssClass="btn btn-primary freezePagina" />
					</s:elseif>
					
            </span>
			<a id="linkMsgControlloMovColl" href="#msgControlloMovColl" data-toggle="modal" style="display:none;"></a>
			<a id="linkVisualizzaModaleConfermaModificaProvvedimento" href="#modalSalvaModificaProvvedimento" data-toggle="modal" style="display:none;"></a>
			<a id="linkVisualizzaModaleConfermaProseguiModificaProvvedimento" href="#modalProseguiModificaProvvedimento" data-toggle="modal" style="display:none;"></a>
			
			<!-- RM 29/09/2017-->
            <!-- MODALE CONFERMA PROSEGUI MODIFICA VINCOLI  -->
			<a id="linkVisualizzaModaleConfermaSalvaModificaVincoli" href="#modalConfermaSalvaModificaVincoli" data-toggle="modal" style="display:none;"></a>
			<a id="linkVisualizzaModaleConfermaProseguiModificaVincoli" href="#modalConfermaProseguiModificaVincoli" data-toggle="modal" style="display:none;"></a>
			<!-- FINE -->
			
			<!-- DODICESIMI: -->
			<a id="linkVisualizzaModaleConfermaSalvaConBypassDodicesimi" href="#modalSalvaConBypassDodicesimi" data-toggle="modal" style="display:none;"></a>
            
            <a id="linkmsgPrimaNota" href="#msgPrimaNota" data-toggle="modal" style="display:none;"></a>
            
            </p>
            
          </div>
        </div>
        
        
		<!-- SIAC-5333 -->
		<s:include value="/jsp/movgest/include/modalValidaPrimaNota.jsp"/>
        <!-- SIAC-5333 -->
        
        
        <!-- SIAC-6993 -->
					<s:url method="consultaModificheProvvedimento"
						var="consultaModificheProvvedimento" />
					<s:url method="consultaModificheProvvedimentoSub"
						var="consultaModificheProvvedimentoSub" />

					<s:hidden id="consultaModificheProvvedimento"
						value="%{consultaModificheProvvedimento}" />
					<s:hidden id="consultaModificheProvvedimentoSub"
						value="%{consultaModificheProvvedimentoSub}" />
        <!-- SIAC-6993 -->

	  </s:form>
    </div>
  </div>	 
</div>	
   
  <script type="text/javascript">

	$(document).ready(function() {
		
		// SIAC-6103
		if ( $("#parereFinanziario").is(':checked') ) {
			$("#parereFinanziario").attr('readonly',true);
			$("#parereFinanziario").on( "click", function() {
				return false;
			});
		}
		
		var bloccoPrenotatoLiquidabile = $("#bloccoPrenotatoLiquidabile");
		var prenotazioneNo = $("#prenotazioneNo");
		var prenotazioneSi = $("#prenotazioneSi");

		/*	var radioPluriennaleNo = $("#radioPluriennaleNo");
		var radioPluriennaleSi = $("#radioPluriennaleSi");
		var numeroPluriennaliLabel = $("#numeroPluriennaliLabel");
		var numeroPluriennali = $("#numeroPluriennali");
	*/	var bloccoRiaccertato = $("#bloccoRiaccertato");
	    var riaccertatoNo = $("#riaccertatoNo");
		var riaccertatoSi = $("#riaccertatoSi");
		var annoImpRiacc = $("#annoImpRiacc");
		var numImpRiacc = $("#numImpRiacc");
		
	/*	if (radioPluriennaleNo.is(':checked')) {
			numeroPluriennaliLabel.hide();
			numeroPluriennali.hide();
		}
		if (radioPluriennaleSi.is(':checked')) {
			numeroPluriennaliLabel.show();
			numeroPluriennali.show();
		}
	*/	if (riaccertatoNo.is(':checked')) {
// 			annoImpRiacc.hide();
// 			numImpRiacc.hide();
			bloccoRiaccertato.hide();
		}
		if (riaccertatoSi.is(':checked')) {
// 			annoImpRiacc.show();
// 			numImpRiacc.show();
			bloccoRiaccertato.show();
		}
		
		if (prenotazioneNo.is(':checked')) {
			bloccoPrenotatoLiquidabile.hide();
		}
		if (prenotazioneSi.is(':checked')) {	
			bloccoPrenotatoLiquidabile.show();
		} 
		
		$("#ricercaGuidataCapitolo").click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaRicercaCapitolo");
			var selectedNode = treeObj.getCheckedNodes(true);
			var strutturaAmministrativaSelezionata = {};
			strutturaAmministrativaSelezionata.codice = "";
			if (selectedNode != null) {
				selectedNode.forEach(function(currentNode) {
				    strutturaAmministrativaSelezionata = currentNode;
				});
			}
			$.ajax({
				url: '<s:url method="ricercaCapitolo"/>',
				type: 'POST',
				data: $(".parametroRicercaCapitolo").serialize() + "&strutturaAmministrativaSelezionata=" + strutturaAmministrativaSelezionata.codice,
				success: function(data)  {
					$("#gestioneRisultatoRicercaCapitoli").html(data);
				}
			});
		});	
		
		$("#ricercaGuidataSoggetto").click(function() {
			$.ajax({
				url: '<s:url method="ricercaSoggetto"/>',
				type: 'POST',
				data: $(".parametroRicercaSoggetto").serialize(),
			    success: function(data)  {
				    $("#gestioneRisultatoRicercaSoggetti").html(data);
				}
			});
		});	
		
		$("#linkCompilazioneGuidataCapitolo").click(function(){
			$("#capitoloRicerca").val($("#capitolo").val());
			$("#articoloRicerca").val($("#articolo").val());
			$("#uebRicerca").val($("#ueb").val());
		});
		
       $("#linkCompilazioneGuidataProvvedimento").click(function(){

      		var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaOrdinativoIncasso");
      			var strutturaAmministrativaParam = "";
      			if (treeObj != null) {
      				var selectedNode = treeObj.getCheckedNodes(true);
      				selectedNode.forEach(function(currentNode) {
      					strutturaAmministrativaParam = currentNode;
      				});
      			}
      		
      		
      			initRicercaGuidataProvvedimentoConStruttura(
      				$("#annoProvvedimento").val(), 
      				$("#numeroProvvedimento").val(),
      				$("#listaTipiProvvedimenti").val(),
      				strutturaAmministrativaParam
      			);
      	});
       
       
       $("#linkInserisciProvvedimento").click(function(){

   		var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaInserimentoProvvedimento");
   			var strutturaAmministrativaParam = "";
   			if (treeObj != null) {
   				var selectedNode = treeObj.getCheckedNodes(true);
   				selectedNode.forEach(function(currentNode) {
   					strutturaAmministrativaParam = currentNode;
   				});
   			}
   			
   			$.ajax({
   				url: '<s:url method="clearInserimentoProvvedimento"/>',
   				success: function(data)  {
   					$("#gestioneEsitoInserimentoProvvedimento").html(data);
   				}
   			});

   		});
				
		$("#linkCompilazioneGuidataSoggetto").click(function(){
			initRicercaGuidataSoggetto(
				$("#codCreditore").val(),
				$("#listaClasseSoggetto").val()
			);
		});
		
		 $("#linkCompilazioneGuidataProgetto").click(function(){
		    	initRicercaGuidataProgetto($("#progetto").val());
			});
		
		$("#progetto").change(function(){
			var cod = $("#progetto").val();
			//Carico i dati in tabella "Modalita' di pagamento"		
			$.ajax({
				url: '<s:url method="codiceProgettoChanged"></s:url>',
				type: "GET",
				data: $(".hiddenGestoreToggle").serialize() + "&id=" + cod, 
			    success: function(data)  {
			    	$("#refreshDescrizioneProgetto").html(data);
				}
			});			
		});
		
		$("#codCreditore").blur(function(){
			$("#listaClasseSoggetto").val(-1);
		});
		
		$("#listaClasseSoggetto").change(function(){
			$("#codCreditore").val("");
			$.ajax({
				url: '<s:url method="listaClasseSoggettoChanged"/>',
				success: function(data)  {
				    $("#refreshHeaderSoggetto").html(data);
				}
			});
		});
		
		$("#cercaCapitoloSubmit").click(function(){
			$("#capitolo").attr("disabled", true);
			$("#articolo").attr("disabled", true);
			$("#ueb").attr("disabled", true);
		});
		
	/*	radioPluriennaleNo.change(function(){
			numeroPluriennaliLabel.hide();
			numeroPluriennali.hide();
		});
		
		radioPluriennaleSi.change(function(){
			numeroPluriennaliLabel.show();
			numeroPluriennali.show();
		});
	*/	
		riaccertatoNo.change(function(){
// 			annoImpRiacc.hide();
// 			numImpRiacc.hide();
			bloccoRiaccertato.hide();
		});
		
		riaccertatoSi.change(function(){
// 			annoImpRiacc.show();
// 			numImpRiacc.show();
			bloccoRiaccertato.show();
		});
		
		prenotazioneNo.change(function(){
			bloccoPrenotatoLiquidabile.hide();
		});
		
		prenotazioneSi.change(function(){
			bloccoPrenotatoLiquidabile.show();
		});
		
		
		var tipoDebitoSiopeVar = $("input[name='step1Model.tipoDebitoSiope']");
		<s:include value="/jsp/include/toggleAssenzaCig.jsp" />	
		
	});

	<s:if test="%{!richiediConfermaRedirezioneContabilitaGenerale}">
		
		<s:if test="isShowPopUpMovColl()">
			$("#linkMsgControlloMovColl").click();
		</s:if>
		
		<s:if test="isShowModaleConfermaModificaProvvedimento()">
			$("#linkVisualizzaModaleConfermaModificaProvvedimento").click();
		</s:if>
		
		<s:if test="isShowModaleConfermaProseguiModificaProvvedimento()">
			$("#linkVisualizzaModaleConfermaProseguiModificaProvvedimento").click();
		</s:if>
		
		/* **********************************************************************  */
		/* CR vitelli su aggiorna vincoli anche senza disponibilita' sul capitolo */
		<s:if test="isShowModaleConfermaSalvaModificaVincoli()">
			$("#linkVisualizzaModaleConfermaSalvaModificaVincoli").click();
		</s:if>
		<s:if test="isShowModaleConfermaProseguiModificaVincoli()">
			$("#linkVisualizzaModaleConfermaProseguiModificaVincoli").click();
		</s:if>
		/*  ************** fine *************************************************** */
		
		<s:if test="isShowModaleConfermaSalvaConBypassDodicesimi()">
			$("#linkVisualizzaModaleConfermaSalvaConBypassDodicesimi").click();
		</s:if>
		
		<!-- apre il form di inserimento vincolo -->
		<!-- apre il tab vincoli -->
		<s:if test="step1Model.apriTabVincoli">
			$("#hrefTabVincolo").click();
		</s:if>
		<!-- apre il form di inserimento vincolo -->
		<s:if test="step1Model.inserisciVincoloBtn">
			$("#inserisciVincoloBtn").click();
		</s:if>
	
	</s:if>
	<s:else>
	
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
	
	</s:else>
	
	
	<s:if test="step1Model.portaAdAltezzaVincoli">
		spostaLAncora('ancoraVincoli');
	</s:if>
	
	function impostaValorePrenotatoLiquidabile(){
		cbObj = document.getElementById("prenotatoLiquidabileCheckBox");
        var valore = cbObj.checked;
        $("#hiddenPerPrenotazioneLiquidabile").val(valore);
	}
	
	
</script>  
 
<script src="/siacfinapp/js/local/movgest/aggiornaImpegno.js" type="text/javascript"></script>
<script src="/siacfinapp/js/local/include/modalRicercaCronop.js" type="text/javascript"></script>

 
   
<s:include value="/jsp/include/footer.jsp" />
 