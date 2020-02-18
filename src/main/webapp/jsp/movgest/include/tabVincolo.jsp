<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<div class="step-pane active" id="vincoliImpegno">
	<div class="accordion" >
		<div class="accordion-group">
			
			<div class="accordion-heading" id="tabVincolo">
				<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#vincoliImpegno" href="#vincoliImpegnoTAB" id="hrefTabVincolo" >
					Vincoli impegno accertamenti: <span class="infoVincoli">Totale <s:property value="getText('struts.money.format', {step1Model.totaleImportoVincoli})" default="0,00"/> </span> <span class="infoVincoli">Da collegare <s:property value="getText('struts.money.format', {step1Model.totaleImportoDaCollegare})" default="0,00" /></span>    <span class="icon">&nbsp;</span></a>
				</a>
			</div>
			<!-- totaleImportoVincoli, totaleImportoDaCollegare;	 -->
			<div id="vincoliImpegnoTAB" class="accordion-body collapse">
				<div class="accordion-inner">

					<fieldset class="form-horizontal">	
					
					
					<display:table name="step1Model.listaVincoliImpegno" 
								   class="table table-hover tab_left" 
			                 	   summary="riepilogo vincoli"
			                       pagesize="10"
			                       requestURI=""
							       uid="vincoliID">
							       
							<display:column title="Accertamento o Avanzo">
								<s:if test="%{#attr.vincoliID.accertamento != null}">
									<s:property  value="%{#attr.vincoliID.accertamento.annoMovimento}" />/<s:property  value="%{#attr.vincoliID.accertamento.numero.intValue()}" />		       
								</s:if>
								<s:elseif test="%{#attr.vincoliID.avanzoVincolo != null}">
									<s:property  value="%{#attr.vincoliID.avanzoVincolo.tipoAvanzovincolo.codice}" />
								</s:elseif>
							</display:column>  
							<display:column title="Descrizione"> 
								<s:if test="%{#attr.vincoliID.accertamento != null}">
									<s:property  value="%{#attr.vincoliID.accertamento.descrizione}" />		       
								</s:if>
								<s:elseif test="%{#attr.vincoliID.avanzoVincolo != null}">
									<s:property  value="%{#attr.vincoliID.avanzoVincolo.tipoAvanzovincolo.descrizione}" />
								</s:elseif>
							</display:column>
							<display:column title="Capitolo">
								<s:if test="%{#attr.vincoliID.accertamento != null}">
									<s:property  value="%{#attr.vincoliID.accertamento.capitoloEntrataGestione.annoCapitolo}" />/
									<s:property  value="%{#attr.vincoliID.accertamento.capitoloEntrataGestione.numeroCapitolo}" />/
									<s:property  value="%{#attr.vincoliID.accertamento.capitoloEntrataGestione.numeroArticolo}" />/
									<s:property  value="%{#attr.vincoliID.accertamento.capitoloEntrataGestione.numeroUEB}" />
								</s:if>
							</display:column>
							
							
							<display:column title="Importo accertato" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro">
								<s:if test="%{#attr.vincoliID.accertamento != null}">
									<s:property  value="%{#attr.vincoliID.accertamento.importoAttuale}" />		       
								</s:if>
								<s:elseif test="%{#attr.vincoliID.avanzoVincolo != null}">
									<s:property  value="%{#attr.vincoliID.avanzoVincolo.avavImportoMassimale}" />
								</s:elseif>
							</display:column>
											 
							<display:column title="Utilizzabile" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro">
								<s:if test="%{#attr.vincoliID.accertamento != null}">
									<s:property  value="%{#attr.vincoliID.accertamento.disponibilitaUtilizzare}" />		       
								</s:if>
								<s:elseif test="%{#attr.vincoliID.avanzoVincolo != null}">
									<s:property  value="%{#attr.vincoliID.avanzoVincolo.disponibileAvanzovincolo}" />
								</s:elseif>				
							</display:column>	
											
							<display:column title="Importo Vincolo" property="importo"     
											decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
											
							<display:column title="" class="tab_Right">
							    
									<div class="btn-group">
										<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
										<ul class="dropdown-menu pull-right">
										
										<s:if test="isAbilitatoInserimentoVincolo()">
											<s:if test="%{#attr.vincoliID.accertamento != null}">
												<li><a id="linkAggiorna_<s:property value="%{#attr.vincoliID.accertamento.annoMovimento}"/>_<s:property value="%{#attr.vincoliID.accertamento.numero.intValue()}"/>" href="#aggVincolo" data-toggle="modal" class="linkAggiornaVincolo">aggiorna</a>
												<li><a id="linkElimina_<s:property value="%{#attr.vincoliID.accertamento.annoMovimento}"/>_<s:property value="%{#attr.vincoliID.accertamento.numero.intValue()}"/>" href="#msgElimina" data-toggle="modal" class="linkEliminaVincolo">elimina</a>   
											</s:if>
											<s:elseif test="%{#attr.vincoliID.avanzoVincolo != null}">
												<li><a id="linkAvanzoAggiorna_<s:property value="%{#attr.vincoliID.avanzoVincolo.uid}"/>" href="#aggAvanzoVincolo" data-toggle="modal" class="linkAvanzoAggiornaVincolo">aggiorna</a>
												<li><a id="linkAvanzoElimina_<s:property value="%{#attr.vincoliID.avanzoVincolo.uid}"/>" href="#msgAvanzoElimina" data-toggle="modal" class="linkAvanzoEliminaVincolo">elimina</a>
											</s:elseif>
										</s:if>
										
											
										</ul>
									</div>
								
							</display:column>
					</display:table>
						       
					
					<s:if test="isAbilitatoInserimentoVincolo()">
						<p><a class="btn btn-secondary" data-toggle="collapse" data-target="#NewVincolo" id="inserisciVincoloBtn" >inserisci vincolo</a></p>
					</s:if>
					
		
												
					<!-- accordion vincolo-->
						<div id="NewVincolo" class="collapse">
							<div class="accordion_info">
							
								<fieldset class="form-horizontal">
								
									<div class="control-group">
				                      <label class="control-label" for="radioTipoVincolo">Tipo vincolo: </label>
				                      <div class="controls">
				                        <div class="radio inline" style="width: 617px;">
				                        	<s:radio id="radioTipoVincolo" name="step1Model.tipoVincolo" cssClass="flagTipoVincolo" list="step1Model.sceltaAccertamentoAvanzoList"/>  
										</div>   
				                      </div>
				                    </div>
								
								
								
								<div id="bloccoAccertamento">
								
								    <s:if test="step1Model.accertamentoPerVincolo==null">
							        	<h4 class="step-pane">Accertamento: </h4>
								    </s:if>        
									<s:else>
										<h4 class="step-pane">Accertamento: ${step1Model.accertamentoPerVincolo.annoMovimento} / ${step1Model.accertamentoPerVincolo.numero.intValue()} -
										                                    ${step1Model.accertamentoPerVincolo.descrizione} - 
										                                    <s:property value="getText('struts.money.format', {step1Model.accertamentoPerVincolo.importoAttuale})" /> -
										                                    ${step1Model.accertamentoPerVincolo.capitoloEntrataGestione.annoCapitolo} /
										                                    ${step1Model.accertamentoPerVincolo.capitoloEntrataGestione.numeroCapitolo} / 
										                                    ${step1Model.accertamentoPerVincolo.capitoloEntrataGestione.numeroArticolo} /
										                                     ${step1Model.accertamentoPerVincolo.capitoloEntrataGestione.numeroUEB}
										                                      </h4>
									</s:else>
										
  
											<div class="control-group">
												<label for="annoAcc" class="control-label">Anno *</label>
												<div class="controls">
												    <s:if test="step1Model.accertamentoPerVincolo==null"> 
												    	<s:textfield id="annoAccertamentoVincolo" name="step1Model.annoAccertamentoVincolo" title="anno" 
												        	         maxlength="4" onkeyup="return checkItNumbersOnly(event)" cssClass="span1">
												    	</s:textfield>
												    </s:if>
												    <s:else>
												   		 <s:textfield id="annoAccertamentoVincolo" name="step1Model.annoAccertamentoVincolo" title="anno" readonly="true"
												        	           maxlength="4" onkeyup="return checkItNumbersOnly(event)" cssClass="span1">
												    	</s:textfield>
												    </s:else>  
													<span class="al">
														<label class="radio inline">Numero *</label>
													</span>
													<s:if test="step1Model.accertamentoPerVincolo==null"> 
														<s:textfield id="numeroAccertamentoVincolo" name="step1Model.numeroAccertamentoVincolo" title="numero"  
														             maxlength="10" onkeyup="return checkItNumbersOnly(event)" cssClass="span2">
														</s:textfield>  
											        </s:if>
											        <s:else>
											        	<s:textfield id="numeroAccertamentoVincolo" name="step1Model.numeroAccertamentoVincolo" title="numero" readonly="true" 
														             maxlength="10" onkeyup="return checkItNumbersOnly(event)" cssClass="span2">
														</s:textfield>  
											        </s:else>
										
													<span class="radio guidata"><a href="#guidaAccertamento" data-toggle="modal" id="accRicercaGuidata" class="btn btn-primary">compilazione guidata</a></span>
	
												</div>
											</div>
											
											<div class="control-group">
												<label class="control-label">Importo vincolo *</label>
												<div class="controls">
												    <s:textfield id="importoVincoloFormattato" name="step1Model.importoVincoloFormattato" maxlength="14" cssClass="span3 soloNumeri decimale" ></s:textfield>    
													<label class="radio inline"><b>Disponibile a utilizzare: <s:property value="getText('struts.money.format', {step1Model.accertamentoPerVincolo.disponibilitaUtilizzare})" /> </b></label>	
												</div>
											</div>
									</div>
									
									
									<div id="bloccoAvanzovincolo">
								
										    <s:if test="step1Model.accertamentoPerVincolo==null">
									        	<h4 class="step-pane">FPV / Avanzo: </h4>
										    </s:if>        
											<s:else>
												<h4 class="step-pane">FPV / Avanzo: TODO DATI VINCOLO</h4>
											</s:else>

											<div class="control-group">
												<span class="al">
										      		<label class="radio inline" for="listaAvanzovincoloId">FPV / Avanzo *</label>
										     	</span>
										     	<s:if test="null!=listaAvanzovincolo">
											      	<s:select list="listaAvanzovincolo" id="listaAvanzovincoloId"  name="step1Model.avanzoVincoloSelezionato" cssClass="span5"  
											       	 	       listKey="uid" listValue="tipoAvanzovincolo.codice" />
										       	</s:if> 
										    </div> 	
									       	
									       	<div class="control-group">
												<label class="control-label">Importo vincolo *</label>
												<div class="controls">
												    <s:textfield id="importoAvanzoVincoloFormattato" name="step1Model.importoAvanzoVincoloFormattato" maxlength="14" cssClass="span3 soloNumeri decimale" ></s:textfield>    
													<s:include value="/jsp/movgest/include/dettaglioImportoResiduoAvanzoSelezionato.jsp" />
												</div>
											</div>
											
									</div>

								</fieldset>
								  
								<p> 
									<s:submit name="annullaVincolo" id="annullaVincolo" data-toggle="collapse" data-target="#NewVincolo"  
									          value="annulla" method="annullaValoriVincolo"  cssClass="btn btn-secondary" />
									          
									<s:submit name="aggiungiVincolo" id="aggiungiVincolo"  value="aggiungi vincolo" 
											  method="aggiungiVincolo" cssClass="btn btn-primary pull-right" />
								</p>
							</div>
						</div>
					<!-- /accordion accordion vincolo -->
											

					</fieldset >
            
            		<s:hidden id="annoAccDaPassare" name="annoAccDaPassare"/>
            		<s:hidden id="numeroAccDaPassare" name="numeroAccDaPassare"/>
            		
            		<s:hidden id="uidAvanzoVincoloDaPassare" name="uidAvanzoVincoloDaPassare"/>
            		
            
            		</div>
				</div>
			
			</div>
		</div>
	</div>		
</div>
		  
</div>

<script type="text/javascript">
	$(document).ready(function() {
		
		impostaCampiDaFlagTipoVincolo();
		
		$(".linkEliminaVincolo").click(function() {
				var supportId = $(this).attr("id").split("_");
				
				if (supportId != null && supportId.length > 0) {
					$("#annoAccDaPassare").val(supportId[1]);
					$("#numeroAccDaPassare").val(supportId[2]);
				}
		});

		$(".linkAvanzoEliminaVincolo").click(function() {
			var supportId = $(this).attr("id").split("_");
			
			if (supportId != null && supportId.length > 0) {
				$("#uidAvanzoVincoloDaPassare").val(supportId[1]);
			}
		});
		
		
		
		
		$(".linkAggiornaVincolo").click(function() {
			var supportId = $(this).attr("id").split("_");
			$.ajax({
				url: '<s:url method="dettaglioAggiornaVincolo"/>',
				type: 'POST',
				data: 'annoAccPerAggiorna=' + supportId[1] +'&numeroAccPerAggiorna=' + supportId[2],
			    success: function(data)  {
				    $("#divDettaglioAggiornaVincolo").html(data);
				}
			});
		});	
		
		$(".linkAvanzoAggiornaVincolo").click(function() {
			var supportId = $(this).attr("id").split("_");
			$.ajax({
				url: '<s:url method="dettaglioAvanzoAggiornaVincolo"/>',
				type: 'POST',
				data: 'uidAvanzoVincoloPerAggiorna=' + supportId[1],
			    success: function(data)  {
				    $("#divDettaglioAvanzoAggiornaVincolo").html(data);
				}
			});
		});	
		
		
		
		$("#linkAggiornaImpegnoConVincolo").click(function() {
			$.ajax({
				url: '<s:url method="dettaglioAggiornaImportoConVincoli"/>',
				type: 'POST',
			    success: function(data)  {
				    $("#divDettaglioAggiornaImportoConVincolo").html(data);
				}
			});
		});
		
		
		$("#accRicercaGuidata").click(function(){
			$("#annoAccRice").val($("#annoAccertamentoVincolo").val());
			$("#numAccRice").val($("#numeroAccertamentoVincolo").val());
			
			// pulisco i campi di ricerca di capitolo
			$("#capitoloRice").val("");
			$("#articoloRice").val("");
			$("#UBERice").val("");
			
			$("#campiRicerca").attr("class", "accordion-body collapse in");
			$("#campiRicerca").attr("style", "height: auto");
			$.ajax({
				url: '<s:url method="pulisciRicercaAccPerVincoli"/>',
			    success: function(data)  {
				    $("#gestioneRisultatoRicercaAcc").html(data);
				}
			});
		});
		
		
		
		$("#ricercaGuidataAccPerVincoli").click(function() {
			$.ajax({
				url: '<s:url method="ricercaAccertamentoPerVincoli"/>',
				type: 'POST',
				data: $(".parametroRicercaAccertamento").serialize(),
				success: function(data)  {
					$("#gestioneRisultatoRicercaAcc").html(data);
				}
			});
		});	
		
		
	    $('.flagTipoVincolo').change(function(){
	    	impostaCampiDaFlagTipoVincolo();
	     });
	    
	    $("#listaAvanzovincoloId").change(function() {
	    	refreshImportoResiduoAvanzo();
		});
		
		
	}); 
	
	
	function impostaCampiDaFlagTipoVincolo(){
  	  var radioSec = $('.flagTipoVincolo:checked').val();
	  if(radioSec == 'Accertamento'){
		  $('#bloccoAccertamento').show();
		  $('#bloccoAvanzovincolo').hide();
	  } 
	   if(radioSec == 'FPV / Avanzo') {
		  refreshVersoAvanzovincolo(refreshImportoResiduoAvanzo);
	  }
    }
	
	function refreshVersoAvanzovincolo(callback){
		$('#bloccoAvanzovincolo').show();
		$('#bloccoAccertamento').hide();
		callback();
	}
	
	function refreshImportoResiduoAvanzo(){
		var idAvanzo = $("#listaAvanzovincoloId").val();
		$.ajax({
			url: '<s:url method="dettaglioImportoResiduoAvanzoSelezionato"></s:url>',
			type: "GET",
			data: $(".hiddenGestoreToggle").serialize() + "&idAvanzo=" + idAvanzo, 
		    success: function(data)  {
		    	$("#divImportoResiduoAvanzoSelezionato").html(data);
			}
		});	
    }
	
</script>
         
         
<!-- modal aggiorna vincolo -->
<div id="aggVincolo" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="aggVincoloLabel" aria-hidden="true">

	<div id="divDettaglioAggiornaVincolo"></div>  

</div> 

<div id="aggAvanzoVincolo" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="aggAvanzoVincoloLabel" aria-hidden="true">

	<div id="divDettaglioAvanzoAggiornaVincolo"></div>  

</div> 

<!-- end  -->


<!-- modal aggiorna importo -->
<div id="aggImportoPerVincolo" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="aggImportoPerVincoloLabel" aria-hidden="true">

	<div id="divDettaglioAggiornaImportoConVincolo"></div>  

</div>	
<!-- fine  modal aggiorna importo  -->	


 <!-- Modal conferma elimina vincolo -->
 
<div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
	<div class="modal-body">
		<div class="alert alert-error">
		  <button type="button" class="close" data-dismiss="alert">&times;</button>
		  <p><strong>Attenzione!</strong></p>
		  <p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
		<s:submit id="submitBtnEliminaVincolo" name="btnEliminaVinc" value="si, prosegui" cssClass="btn btn-primary"  method="eliminaVincolo"/>
	</div>
</div>  

<div id="msgAvanzoElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
	<div class="modal-body">
		<div class="alert alert-error">
		  <button type="button" class="close" data-dismiss="alert">&times;</button>
		  <p><strong>Attenzione!</strong></p>
		  <p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
		<s:submit id="submitBtnEliminaVincolo" name="btnEliminaVinc" value="si, prosegui" cssClass="btn btn-primary"  method="eliminaAvanzoVincolo"/>
	</div>
</div> 

<!--/modale elimina -->