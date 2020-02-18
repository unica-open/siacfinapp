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
    <!-- PAOLO -->
<%--       <s:form id="gestioneOrdinativoPagamentoStep1" action="gestioneOrdinativoPagamentoStep1.do" method="post">   --%>
      <s:form id="%{labels.FORM}" action="%{labels.FORM}.do" method="post" cssClass="form-horizontal">
		 <s:include value="/jsp/include/actionMessagesErrors.jsp" />
		 
		<s:hidden id="liquidazioneTrovata" name="liquidazioneTrovata"/>
		<s:hidden id="accertamentoTrovato" name="accertamentoTrovato"/>

		<s:if test="(sonoInAggiornamento() || sonoInAggiornamentoIncasso())">
			<h3>Ordinativo <s:property value="gestioneOrdinativoStep1Model.ordinativo.numero"/>  del <s:property value="%{gestioneOrdinativoStep1Model.ordinativo.dataEmissione}"/> -  Stato <s:property value="gestioneOrdinativoStep1Model.ordinativo.statoOperativoOrdinativo"/> dal <s:property value="%{gestioneOrdinativoStep1Model.ordinativo.dataInizioValidita}"/> </h3>
		</s:if>
		<s:else><h3>Inserimento ordinativo</h3></s:else>
		<div id="MyWizard" class="wizard">
		  <ul class="steps">
				<li data-target="#step1" class="active"><span class="badge">1</span>Dati ordinativo<span class="chevron"></span></li>
				<li data-target="#step2" class="complete"><span class="badge">2</span>Quote ordinativo<span class="chevron"></span></li>
				<s:if test="isCampoAbilitatoInAggiornamento('TAB_PROVVISORI') && attivaVideataProvCassa()">
					<li data-target="#step3" class="complete"><span class="badge">3</span>Provvisori di cassa<span class="chevron"></span></li>
				</s:if>
			</ul>
		</div>

        <div class="step-content">
          <div class="step-pane active" id="step1">
           
          <!-- PAGAMENTO per ricerca liquidazione -->
          <s:if test="oggettoDaPopolarePagamento()"> 
          	<s:if test="!sonoInAggiornamento()">
	        	<h4>Liquidazione</h4>
				<div class="control-group">
					<label class="control-label" for="Codice">Anno </label>
					<div class="controls">
						<s:textfield id="annoLiquidazione" name="gestioneOrdinativoStep1Model.annoLiquidazione" cssClass="span1" maxlength="4" onkeyup="return checkItNumbersOnly(event)"/>
			            Numero 
				        <s:textfield id="numeroLiquidazione" name="gestioneOrdinativoStep1Model.numeroLiquidazione" cssClass="span2" onkeyup="return checkItNumbersOnly(event)"/>	
					 	<span id="searchLiq">
					 		<s:submit id="cercaLiquidazioneInsOrPag" cssClass="btn btn-primary" method="cercaLiquidazione" value="cerca" name="cerca" />
					 		<!-- <a class="btn btn-primary">cerca</a> -->
					 	</span>
					</div>
				</div>
			</s:if>
          </s:if>
          <s:else>
          <!-- INCASSO per ricerca accertamento -->
	          <s:if test="!sonoInAggiornamentoIncasso()">
	         	<h4>Accertamento</h4>
	            <!-- <fieldset class="form-horizontal"> -->
	              <div class="control-group">
	                <label class="control-label" for="Codice">Anno</label>
	                <div class="controls">
	                  <s:textfield id="annoAccertamento" name="gestioneOrdinativoStep1Model.annoAccertamento" cssClass="span1" maxlength="4" onkeyup="return checkItNumbersOnly(event)"/>
	                  Numero
		              <s:textfield id="numeroAccertamento" name="gestioneOrdinativoStep1Model.numeroAccertamento" cssClass="span2" onkeyup="return checkItNumbersOnly(event)"/>  
		              Numero Subaccertamento
		              <s:textfield id="numeroSubAcc" name="gestioneOrdinativoStep1Model.numeroSubAcc" cssClass="span1" maxlength="7" onkeyup="return checkItNumbersOnly(event)"/>  
	              	
		              <span id="searchLiq">
						<s:submit id="cercaAccertamentoInsOrPag" cssClass="btn btn-primary" method="cercaAccertamento" value="cerca" name="cerca" />
					  </span>	
		             </div>
	              </div>
	           <!--  </fieldset> -->
	          </s:if>
          </s:else>
          
          <div id="campiDisabled" class="campiDisabled">
	          <s:if test="!sonoInAggiornamentoOR()">    
	                <!-- <div id="refreshHeaderCapitoloOrdinativo"> -->
		            <s:include value="/jsp/ordinativo/include/headerCapitoloOrdinativo.jsp"/>
		            <!-- </div> -->
		            <s:include value="/jsp/ordinativo/include/capitoloOrdinativo.jsp" />
	           </s:if>
	           <s:else><s:include value="/jsp/ordinativo/include/headerDettaglioOrdinativo.jsp" /></s:else>
            
               
		       <s:include value="/jsp/ordinativo/include/headerSoggettoOrdinativo.jsp"/>		       	
			   <s:if test="!sonoInAggiornamentoOR()">
			       		<s:include value="/jsp/ordinativo/include/soggettoOrdinativo.jsp" />
			   </s:if> 
			   
			   <a id="openSediSEC" data-toggle="modal" data-target="#refreshSediSecondarie"></a>    	
		        
		        <div id="refreshSediSecondarie">
		        		<s:include value="/jsp/ordinativo/include/sediSecondarieOrdinativo.jsp" />	   
		        </div>	
		        	
			    <s:if test="oggettoDaPopolarePagamento()">
			    	<a id="openModPAG" data-toggle="modal" data-target="#refreshModPagamento"></a>	        	
			        <div id="refreshModPagamento">     	
		        			<s:include value="/jsp/ordinativo/include/modalitaPagamentoOrdinativo.jsp" />
			        </div>
			    </s:if>
		        	
		        	
		        <s:if test="!sonoInAggiornamento()">  
			           	<s:include value="/jsp/ordinativo/include/provvedimentoOrdinativo.jsp" /> 
		        </s:if>	  

			</div>	
			<s:include value="/jsp/ordinativo/include/datiEntitaOrdinativo.jsp" />
			
			<!-- Ordinativi collegati -->
			
			<s:if test="sonoInAggiornamento() && oggettoDaPopolarePagamento()">  
             	<a id="ancoraOrdinativiCollegati"></a>
             	<s:include value="/jsp/ordinativo/include/collegaOrdinativiDiIncasso.jsp" /> 
           
            </s:if> 
            
            <span class="perReintroitiVisible" id="bloccoOrdinativiPagamento">
	            <s:if test="!oggettoDaPopolarePagamento()">  
	             	<a id="ancoraOrdinativiCollegati"></a>
	             	<s:include value="/jsp/ordinativo/include/collegaOrdinativiDiPagamento.jsp" /> 
	            </s:if> 
            </span>
            
            
            <!-- Ordinativi collegati  -->
             
            
			
			<s:include value="/jsp/ordinativo/include/classifGenerici.jsp" />
           
           
          <!-- HIDDEN PER GESTIRE FLAG PER REINTROITI --> 
 		  <s:hidden id="hiddenFlagPerReintroiti" name="gestioneOrdinativoStep1Model.flagPerReintroiti" />


		  <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
           			<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
					
					<%-- CR-2023 da eliminare 
					<s:hidden id="ricaricaAlberoContoEconomico" name="teSupport.ricaricaAlberoContoEconomico"/> --%>
					
					<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
					<s:hidden id="ricaricaSiopeSpesa" name="teSupport.ricaricaSiopeSpesa"/>
		   <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
			
			<s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden>
			
            <!-- Modal -->
            <s:include value="/jsp/include/modalSoggetto.jsp" />	
            <s:include value="/jsp/include/modalProvvedimenti.jsp" />
            <s:include value="/jsp/include/modalCapitolo.jsp" />
            <s:hidden id="strutturaDaInserimento"  name="strutturaDaInserimento"></s:hidden>
            <!-- Fine Modal -->
            <s:if test="sonoInAggiornamento()">
				<s:hidden id="doveMiTrovo" name="doveMiTrovo" value="Inserimento "></s:hidden>
			</s:if>
			<s:else>
				<s:hidden id="doveMiTrovo" name="doveMiTrovo" value="Aggiornamento "></s:hidden>
			</s:else>
            </div>
        </div> 
        
       	<s:include value="/jsp/ordinativo/include/modalOrdinativo.jsp" />
       	
		<p class="margin-medium"> <s:include value="/jsp/include/indietro.jsp" />    
		<s:submit cssClass="btn btn-secondary" method="annullaStep1" value="annulla" name="annulla" />
        <s:submit id="proseguiInsOrdInc" cssClass="btn btn-primary pull-right" method="prosegui" value="prosegui" name="prosegui" />
		
		<s:if test="sonoInAggiornamento() && gestioneOrdinativoStep2Model.listaSubOrdinativiPagamenti.size() > 0">
			<s:submit  cssClass="btn btn-primary pull-right freezePagina" method="aggiornaOrdinativo" value="salva" name="salva" />
		</s:if>
		
		 
		<s:if test="sonoInAggiornamentoIncasso() && gestioneOrdinativoStep2Model.listaSubOrdinativiIncasso.size() > 0">
				<s:submit cssClass="btn btn-primary pull-right freezePagina" method="aggiornaOrdinativoIncasso" id="aggiornaOrdIncasso" value="salva" name="salva" ondblclick="this.disabled=true;"   />
				
		</s:if>
		</p>       

      </s:form>
    </div>
  </div>	 
</div>	




<script type="text/javascript">




<!-- READY COMUNE A PAGAMENTO E INCASSO -->
	$(document).ready(function() {




		$("#flagDaTrasmettere").click(function() {
			if (! $(this).attr('readonly')) {
				if (this.checked) {
					$("#HIDDEN_flagDaTrasmettere").remove();
				} else {
					$(this).after('<input type="hidden" name="gestioneOrdinativoStep1Model.ordinativo.flagDaTrasmettere" id="HIDDEN_flagDaTrasmettere" value="false" />');
				}
			}
		});





		
		$("#linkCompilazioneGuidataCapitolo").click(function(){
			initRicercaGuidataCapitolo(
					$("#capitolo").val(), 
					$("#articolo").val(),
					$("#ueb").val()
				
			);
		});
		
		
		$("#cercaCapitoloSubmit").click(function(){
			$("#capitolo").attr("disabled", true);
			$("#articolo").attr("disabled", true);
			$("#ueb").attr("disabled", true);
		});
		
		
		$("#linkCompilazioneGuidataSoggetto").click(function(){
			initRicercaGuidataSoggetto(
				$("#codCreditoreLiquidazione").val(),
				null
			);
		});
		
		
		  
	    $('#Note').keyup(function(){ 
	    			    	
	    	var text = $(this).val();
	    	
	        var size = text.length;  
	    			  
	        if(size > 500){  

	            var newText = text.substr(0, 500);  
	   
	            $(this).val(newText);  
	        }  		        
	   
	    });  
	    
		
		$("#listaNoteTesoriere").change(function() {
					    			
			$("#Note").val($("#listaNoteTesoriere option:selected").text());
			
		});
		
		$("#listaTipiCausale").change(function() {
			var selezionata = $("#listaTipiCausale").val();
			
			$.ajax({
				url: '<s:url method="tipoCausaleEntrataChanged"></s:url>',
				type: "GET",
				data: $(".hiddenGestoreToggle").serialize() + "&selezionata=" + selezionata
			}).then(function(data)  {
		    	$("#refreshTendinoCausali").html(data);
			});	
			
		});	
		
	});

</script>

<s:if test="oggettoDaPopolarePagamento()">

<!-- ORDINATIVO PAGAMENTO -->
<script type="text/javascript">
	
	$(document).ready(function() {
		
	<s:if test="sonoInAggiornamento()">
		$("#flagDaTrasmettere").attr('readonly', true).click(function(e){
			return false;
		});
	</s:if>
	<s:else>
		$("#flagDaTrasmettere").attr('checked', true);
	</s:else>

		
		$("#proseguiInsOrdInc").click(function() {
			
			var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaOrdinativoIncasso");
			var strutturaAmministrativaParam = "";
			if (treeObj != null) {
				var selectedNode = treeObj.getCheckedNodes(true);
				selectedNode.forEach(function(currentNode) {
					strutturaAmministrativaParam = currentNode.uid;
				});
			}
			
			//$("#strutturaDaInserimento").val(strutturaAmministrativaParam);
			//alert("strutturaAmministrativaParam: "+ strutturaAmministrativaParam);
			// Jira 1682, con il currentNode.uid si stettaca prima l'hidden sbagliato
			// hidden che non veniva poi usato lato action
			$("#strutturaSelezionataSuPagina").val(strutturaAmministrativaParam);

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
		
		
		
		
		$("#codCreditoreLiquidazione").change(function(){
			var cod = $("#codCreditoreLiquidazione").val();
			//Carico i dati in tabella "Modalità di pagamento"		
			$.ajax({
				url: '<s:url method="modpagamento"></s:url>',
				type: "GET",
				data: $(".hiddenGestoreToggle").serialize() + "&id=" + cod, 
			    success: function(data)  {
			    	$("#refreshModPagamento").html(data);
			    	// $("#openSediSEC").click(); 
			    	 //Carico i dati in tabella "Sedi secondarie"
					$.ajax({
						url: '<s:url method="sedisecondarie"></s:url>',
						type: "GET",
						data: $(".hiddenGestoreToggle").serialize() + "&id=" + cod, 
					    success: function(data)  {
					    	$("#refreshSediSecondarie").html(data);
					    	
					    	
					    	$.ajax({
								url: '<s:url method="aggiornaAvviso"></s:url>',
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
		

		if($("#liquidazioneTrovata").val()!=null && $("#liquidazioneTrovata").val()=='true'){
		 	   
			$(".guidata").addClass("disabledEN");
			var campiReadonly = document.getElementById("campiDisabled");
			var inputSet = campiReadonly.getElementsByTagName('input');
			
			for (var i = 0; i < inputSet.length; i++) {
			   document.getElementById(inputSet[i].id).setAttribute("disabled", "disabled");
			}
		}    
		
		
		if($("#idHiddenPassAlbero").val()!=null && $("#idHiddenPassAlbero").val()=='true'){
		 	   
		       if($("#idPassaggioAlbero").val()!=null && $("#idPassaggioAlbero").val()!=''){
		           
		    	   $("#testataStruttura").click();
		    	   
		    	   $("#lineaStruttura").click();
		    	   
		    	   
		       }
		}    

		
		
	});
	$(function() {
	    $('#descrizione').off('keyup')
	    .on('keyup', function(e) {
	        var val = $(this).val();
	        var max = 500;
	        
	        if(val.replace(/\n/g, "\r\n").length > max) {
	            $(this).val(val.replace(/\n/g, "\r\n").substr(0, max).replace(/\r\n/g, "\n"));
	            e.preventDefault();
				e.stopPropagation();
				// Apertura di un messaggio di warning
				bootbox.dialog({
				    message: 'Descrizione troppo lunga. Mantenuti i primi ' + max + ' caratteri',
				    title: 'Attenzione',
				    className: 'dialogWarn',
				    buttons: {
				        confirm: {
				            label: 'Ok',
				            className: 'btn-primary'
				        }
				    }
				});
	        }
	    })
	});
</script>
	
</s:if>
<s:else>
	<script type="text/javascript">
	   <!-- ORDINATIVO INCASSO -->
	   
		$(document).ready(function() {


		<s:if test="sonoInAggiornamentoIncasso()">
			$("#flagDaTrasmettere").attr('readonly', true).click(function(e){
				return false;
			});
		</s:if>
		<s:else>
			$("#flagDaTrasmettere").attr('checked', true);
		</s:else>
			
			
			
			var flagCoperturaVar = $("#flagCopertura");
			var flagPerReintroitoVar = $("#flagPerReintroiti");
			
			gestisciFlagCopertura();
			gestisciFlagPerReintroiti();
			
			flagCoperturaVar.change(function(){
				gestisciFlagCopertura();
			});
			
			flagPerReintroitoVar.change(function(){
				gestisciFlagPerReintroiti();
			});
			
			$("#proseguiInsOrdInc").click(function() {
				
				var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaOrdinativoIncasso");
				var strutturaAmministrativaParam = "";
				if (treeObj != null) {
					var selectedNode = treeObj.getCheckedNodes(true);
					selectedNode.forEach(function(currentNode) {
						strutturaAmministrativaParam = currentNode.uid;
					});
				}
				
				// Jira 1682, con il currentNode.uid si stettaca prima l'hidden sbagliato
				// hidden che non veniva poi usato lato action
				$("#strutturaSelezionataSuPagina").val(strutturaAmministrativaParam);

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
			
			
			
			$("#codCreditoreLiquidazione").change(function(){
				var cod = $("#codCreditoreLiquidazione").val();
				//Carico i dati in tabella "Modalità di pagamento"		
				$.ajax({
					url: '<s:url method="sediIncasso"></s:url>',
					type: "GET",
					data: $(".hiddenGestoreToggle").serialize() + "&id=" + cod, 
				    success: function(data)  {
				    	$("#refreshSediSecondarie").html(data);
				    	// $("#openSediSEC").click(); 
				    	 //Carico i dati in tabella "Sedi secondarie"
				    	$.ajax({
							url: '<s:url method="aggiornaAvviso"></s:url>',
							type: "GET",
						    success: function(data)  {
						    	$("#aggiornaAvviso").html(data);
						    	
							}
						}); 
					}
				});			
			});	
			
	
			
			if($("#accertamentoTrovato").val()!=null && $("#accertamentoTrovato").val()=='true'){
			 	   
				$(".guidata").addClass("disabledEN");
				
				
				if($("#linkCompilazioneGuidataProvvedimento").length > 0){
										
					$("#linkCompilazioneGuidataProvvedimento").parent().removeClass("disabledEN");
					$("#linkCompilazioneGuidataSoggetto").parent().removeClass("disabledEN");
										
				}
				
				var campiReadonly = document.getElementById("campiDisabled");
				var inputSet = campiReadonly.getElementsByTagName('input');
				
				for (var i = 0; i < inputSet.length; i++) {
					
					if(document.getElementById(inputSet[i].id).id != "codCreditoreLiquidazione"){
				  		 document.getElementById(inputSet[i].id).setAttribute("disabled", "disabled");
					}					
				}
			}    
			
			
			if($("#idHiddenPassAlbero").val()!=null && $("#idHiddenPassAlbero").val()=='true'){
			 	   
			       if($("#idPassaggioAlbero").val()!=null && $("#idPassaggioAlbero").val()!=''){
			           
			    	   $("#testataStruttura").click();
			    	   
			    	   $("#lineaStruttura").click();
			    	   
			    	   
			       }
			}    
			
			
			<!-- apre il tab vincoli -->
			<s:if test="gestioneOrdinativoStep1Model.apriTabOrdinativiCollegati">
				$("#hrefTabOrdinativiCollegati").click();
			</s:if>
			<!-- apre il form di inserimento vincolo -->
			/* <s:if test="step1Model.inserisciVincoloBtn">
				$("#inserisciVincoloBtn").click();
			</s:if> */
			
			<s:if test="gestioneOrdinativoStep1Model.portaAdAltezzaOrdinativiCollegati">
				spostaLAncora('ancoraOrdinativiCollegati');
			</s:if>
			
			
			
			
		});
		
	  function gestisciFlagCopertura(){
		var coperturaChecked = $("#flagCopertura").is(':checked');
		var bloccoPerReintroiti = $("#bloccoPerReintroiti");
		var bloccoOrdinativiPagamento = $("#bloccoOrdinativiPagamento");
		var flagReintroitiVar = $("#flagPerReintroiti");
		if(coperturaChecked == true){
			bloccoPerReintroiti.show();
		} else {
			flagReintroitiVar.prop('checked', false);
			bloccoPerReintroiti.hide();
			bloccoOrdinativiPagamento.hide();
		}
	 }
	  
	 function gestisciFlagPerReintroiti(){
		var reintroitiChecked = $("#flagPerReintroiti").is(':checked');
		var bloccoOrdinativiPagamento = $("#bloccoOrdinativiPagamento");
		if(reintroitiChecked == true){
			bloccoOrdinativiPagamento.show();
		} else {
			bloccoOrdinativiPagamento.hide();
		}
	 }
	 
	function impostaValoreFlagPerReintroiti(){
		cbObj = document.getElementById("flagPerReintroiti");
        var valore = cbObj.checked;
        $("#hiddenFlagPerReintroiti").val(valore);
	}
	
	// SIAC-6157
	
	$(function() {
	    $('#descrizione').off('keyup')
	    .on('keyup', function(e) {
	        var val = $(this).val();
	        var max = 500;
	        
	        if(val.replace(/\n/g, "\r\n").length > max) {
	            $(this).val(val.replace(/\n/g, "\r\n").substr(0, max).replace(/\r\n/g, "\n"));
	            e.preventDefault();
				e.stopPropagation();
				// Apertura di un messaggio di warning
				bootbox.dialog({
				    message: 'Descrizione troppo lunga. Mantenuti i primi ' + max + ' caratteri',
				    title: 'Attenzione',
				    className: 'dialogWarn',
				    buttons: {
				        confirm: {
				            label: 'Ok',
				            className: 'btn-primary'
				        }
				    }
				});
	        }
	    })
	});
		
	</script>	
	</s:else>
	

	
<s:include value="/jsp/include/footer.jsp" />
