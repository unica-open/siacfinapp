<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<div class="step-pane active" id="ordinativiCollegati"><!-- vincoliImpegno -->
	<div class="accordion" >
		<div class="accordion-group">
			
			<div class="accordion-heading" id="tabOrdinatviCollegati">
				<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#ordinativiCollegati" href="#ordinativiCollegatiTAB" id="hrefTabOrdinativiCollegati" >
					Ordinativi Collegati: <span class="infoVincoli">Totale <s:property value="getText('struts.money.format', {gestioneOrdinativoStep1Model.totaleImportoCollegati})" default="0,00"/> </span> 
					<span class="icon">&nbsp;</span></a>
				</a>
			</div>
			
			<div id="ordinativiCollegatiTAB" class="accordion-body collapse">
				<div class="accordion-inner">

					<fieldset class="form-horizontal">	
					
					
					<display:table name="gestioneOrdinativoStep1Model.ordinativo.elencoOrdinativiCollegati" 
								   class="table table-hover tab_left" 
			                 	   summary="riepilogo ordinativi di incasso collegati"
			                       pagesize="10"
			                       requestURI=""
							       uid="ordinativoIncassoID">
							       
							<display:column title="Ordinativo di Incasso">
								<s:property  value="%{#attr.ordinativoIncassoID.anno}" />/<s:property  value="%{#attr.ordinativoIncassoID.numero}" />		       
							</display:column>  
							 
							<display:column title="Soggetto">
								<s:property  value="%{#attr.ordinativoIncassoID.soggetto.codiceSoggetto}" />-
								<s:property  value="%{#attr.ordinativoIncassoID.soggetto.denominazione}" />
							</display:column>
							
							<display:column title="Descrizione">
								<s:property  value="%{#attr.ordinativoIncassoID.descrizione}" />		       
							</display:column>
							
							<display:column title="Stato">
								<s:property value="%{#attr.ordinativoIncassoID.statoOperativoOrdinativo}"/>
							</display:column>
														
							<display:column title="Capitolo">
								<s:property  value="%{#attr.ordinativoIncassoID.capitoloEntrataGestione.annoCapitolo}" />/
								<s:property  value="%{#attr.ordinativoIncassoID.capitoloEntrataGestione.numeroCapitolo}" />/
								<s:property  value="%{#attr.ordinativoIncassoID.capitoloEntrataGestione.numeroArticolo}" />/
								<s:property  value="%{#attr.ordinativoIncassoID.capitoloEntrataGestione.numeroUEB}" />
							</display:column>
											
							<display:column title="Importo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro">
								 <s:property value="%{#attr.ordinativoIncassoID.importoOrdinativo}"/>     
							</display:column>
									
											
							<display:column title="" class="tab_Right">
							    
							    <s:if test="isAbilitatoEliminaCollegamentoOrdinativo(#attr.ordinativoIncassoID.uid)">
									<div class="btn-group">
										<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
										<ul class="dropdown-menu pull-right">
											
											<li><a id="linkElimina_<s:property value="%{#attr.ordinativoIncassoID.anno}"/>_<s:property value="%{#attr.ordinativoIncassoID.numero.intValue()}"/>" href="#msgElimina" data-toggle="modal" class="linkElimina">elimina</a>
											</li>
											
										</ul>
									</div>
								</s:if>
								<s:else>
								&nbsp;
								</s:else>
								
							</display:column>
							
					</display:table>
						       
					
					
					<p>
						<s:if test="gestioneOrdinativoStep1Model.abilitatoACollegareOrdinativiNuovi">
							<a class="btn btn-secondary" data-toggle="collapse" data-target="#NewOrdinativoIncasso" id="inserisciOrdinativoIncassoBtn" >inserisci ordinativo di incasso</a>
						</s:if>
						<s:else>
							<a class="btn btn-secondary" >inserisci ordinativo di incasso</a>
						</s:else>
					</p>
		
												
					<!-- accordion vincolo-->
						<div id="NewOrdinativoIncasso" class="collapse">
							<div class="accordion_info">
								
								<fieldset class="form-horizontal">
								   <h4 class="step-pane">Ordinativo: </h4>
									
  
										<div class="control-group">
											<label for="numeroOrdinativoDaCollegare" class="control-label">Numero *</label>
											<div class="controls">
											   
												
										        	<s:textfield id="numeroOrdinativoDaCollegare" name="gestioneOrdinativoStep1Model.numeroOrdinativoDaCollegare" title="numero" readonly="%{abilitatoACollegareOrdinativiNuovi}"
													             maxlength="10" onkeyup="return checkItNumbersOnly(event)" cssClass="span2">
													           
													</s:textfield>  
										       

											</div>
										</div>
										

								</fieldset>
								  
								<p> 
									<s:submit name="aggiungiOrdinativoDaCollegare" id="aggiungiOrdinativoDaCollegare"  value="collega" 
											  method="aggiungiOrdinativoDaCollegare" cssClass="btn btn-primary pull-right" />
											  
									  <a id="linkMsgAggiungiQuietanzato" href="#msgAggiungiQuietanzato" style="display: none;" data-toggle="modal"></a>
								</p>
								
							</div>
						</div>
					<!-- /accordion accordion vincolo -->
											

					</fieldset >
            
            		<s:hidden id="annoOrdinativoDaPassare" name="annoOrdinativoDaPassare"/>
            		<s:hidden id="numeroOrdinativoDaPassare" name="numeroOrdinativoDaPassare"/>
            		<!-- SIAC-6138 -->
            		<s:hidden id="saltaControlloSoggetto" name="saltaControlloSoggetto"/>
            		<s:hidden id="chiediConfermaCollegamentoOrdinativo" name="chiediConfermaCollegamentoOrdinativo"/>
            
            		</div>
				</div>
			
			</div>
		</div>
	</div>		

	<div id="msgAggiungiQuietanzato" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgPrimaNota" aria-hidden="true">
	      <div class="modal-body">
	          
             <div class="alert alert-warning">
		       <button type="button" class="close" data-dismiss="alert">&times;</button>
		       <p><strong>Attenzione!</strong></p>
		       
		       <p>L'ordinativo di incasso scelto e' gia' Quietanzato: si desidera proseguire con il collegamento?</p>
		     </div>
	        
	      </div>
	      <div class="modal-footer">
	        <button class="btn" data-dismiss="modal" aria-hidden="true">no</button>
	        <s:submit id="aggiungiConfermaId" cssClass="btn btn-primary pull-right freezePagina"
       		 method="confermaAggiungiOrdinativoDaCollegare" value="si" name="si" data-dismiss="modal" />
	      </div>
	</div>
	
	 <div id="msgSoggetto" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgSoggetto" aria-hidden="true">
        <div class="modal-body">
          <div class="alert alert-warning">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <p> ATTENZIONE! L'ordinativo di incasso scelto ha soggetto diverso da quello del pagamento: si desidera proseguire con il collegamento?</p>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn" id="negaSoggettoOrdinativo" data-dismiss="modal" aria-hidden="true">No</button>
          <button class="btn btn-primary" id="confermaSoggettoOrdinativo" data-dismiss="modal" aria-hidden="true">S&igrave;</button>
        </div>
      </div>	 


<script type="text/javascript">

	<s:if test="gestioneOrdinativoStep1Model.checkWarningDaCollegareQuietanziato">
		$("#linkMsgAggiungiQuietanzato").click();
	</s:if><s:elseif test="%{chiediConfermaCollegamentoOrdinativo}">
		$('#msgSoggetto').modal('show');	
		$('#confermaSoggettoOrdinativo').on('click', function(){
			$('#saltaControlloSoggetto').val(true);			
			$('#aggiungiOrdinativoDaCollegare').click();
		});
		$('#negaSoggettoOrdinativo').on('click', function(){
			$('#chiediConfermaCollegamentoOrdinativo').val(false);
		});	
	</s:elseif>

	$(document).ready(function() {
		
				
		$(".linkElimina").click(function() {
				var supportId = $(this).attr("id").split("_");
				$('#chiediConfermaCollegamentoOrdinativo').val(false);
				if (supportId != null && supportId.length > 0) {
					$("#annoOrdinativoDaPassare").val(supportId[1]);
					$("#numeroOrdinativoDaPassare").val(supportId[2]);
				}
		});
		
		

		
	}); 
</script>
         
	


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
		<s:submit id="submitBtnEliminaOrdinativoCollegato" name="btnEliminaOrdinativoCollegato" value="si, prosegui" cssClass="btn btn-primary"  method="eliminaOrdinativoCollegato"/>
	</div>
</div>  
<!--/modale elimina -->