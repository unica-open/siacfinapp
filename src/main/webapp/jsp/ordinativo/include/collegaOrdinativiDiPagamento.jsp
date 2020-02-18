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
			                 	   summary="riepilogo ordinativi di pagamento collegati"
			                       pagesize="10"
			                       requestURI=""
							       uid="ordinativoPagamentoID">
							       
							<display:column title="Ordinativo di Pagamento">
								<s:property  value="%{#attr.ordinativoPagamentoID.anno}" />/<s:property  value="%{#attr.ordinativoPagamentoID.numero}" />		       
							</display:column>  
							 
							<display:column title="Soggetto">
								<s:property  value="%{#attr.ordinativoPagamentoID.soggetto.codiceSoggetto}" />-
								<s:property  value="%{#attr.ordinativoPagamentoID.soggetto.denominazione}" />
							</display:column>
							
							<display:column title="Descrizione">
								<s:property  value="%{#attr.ordinativoPagamentoID.descrizione}" />		       
							</display:column>
							
							<display:column title="Stato">
								<s:property value="%{#attr.ordinativoPagamentoID.statoOperativoOrdinativo}"/>
							</display:column>
														
							<display:column title="Capitolo">
								<s:property  value="%{#attr.ordinativoPagamentoID.capitoloUscitaGestione.annoCapitolo}" />/
								<s:property  value="%{#attr.ordinativoPagamentoID.capitoloUscitaGestione.numeroCapitolo}" />/
								<s:property  value="%{#attr.ordinativoPagamentoID.capitoloUscitaGestione.numeroArticolo}" />/
								<s:property  value="%{#attr.ordinativoPagamentoID.capitoloUscitaGestione.numeroUEB}" />
							</display:column>
											
							<display:column title="Importo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro">
								 <s:property value="%{#attr.ordinativoPagamentoID.importoOrdinativo}"/>     
							</display:column>
									
											
							<display:column title="" class="tab_Right">
							    
							    <s:if test="isAbilitatoEliminaCollegamentoOrdinativoPagamento(#attr.ordinativoPagamentoID.uid)">
									<div class="btn-group">
										<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
										<ul class="dropdown-menu pull-right">
											
											<li><a id="linkElimina_<s:property value="%{#attr.ordinativoPagamentoID.anno}"/>_<s:property value="%{#attr.ordinativoPagamentoID.numero.intValue()}"/>" href="#msgElimina" data-toggle="modal" class="linkElimina">elimina</a>
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
							<a class="btn btn-secondary" data-toggle="collapse" data-target="#NewOrdinativoPagamento" id="inserisciOrdinativoPagamentoBtn" >inserisci ordinativo di pagamento</a>
						</s:if>
						<s:else>
							<a class="btn btn-secondary" >inserisci ordinativo di pagamento</a>
						</s:else>
					</p>
		
												
					<!-- accordion vincolo-->
						<div id="NewOrdinativoPagamento" class="collapse">
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
								</p>
								
							</div>
						</div>
					<!-- /accordion accordion vincolo -->
											

					</fieldset >
            
            		<s:hidden id="annoOrdinativoDaPassare" name="annoOrdinativoDaPassare"/>
            		<s:hidden id="numeroOrdinativoDaPassare" name="numeroOrdinativoDaPassare"/>
            		
            
            		</div>
				</div>
			
			</div>
		</div>
	</div>		
</div>
		  
</div>

<script type="text/javascript">
	$(document).ready(function() {
		
				
		$(".linkElimina").click(function() {
				var supportId = $(this).attr("id").split("_");
				
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