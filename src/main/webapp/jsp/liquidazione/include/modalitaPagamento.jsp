<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

	<div class="step-pane active" id="modPAG">
		<div class="accordion" >
			<div class="accordion-group">
				<div class="accordion-heading">
					<a class="accordion-toggle" data-toggle="collapse" data-parent="#modPAG" href="#modTAB">
						Modalit&agrave; pagamento <span class="datiPagamento"></span><span class="icon">&nbsp;</span></a>
					</a>
				</div>
			
				<div id="modTAB" class="accordion-body collapse in">
					<div class="accordion-inner">
						<fieldset class="form-horizontal">	
							<display:table name="listaModalitaPagamentoSoggetto" class="table table-hover tab_left" summary="riepilogo modalita pagamento" uid="modalitaPagamentoSupport">
							       
							       <s:if test="presenzaOrdinativiPerLaLiquidazione">
							       		<!-- DISABILITATO -->
								        <display:column>
								       		<s:radio list="%{#attr.modalitaPagamentoSupport.uid}" name="radioModPagSelezionato" theme="displaytag" disabled="true"></s:radio>
								        </display:column>
							       </s:if>
							       <s:else>
							     	    <!-- ABILITATO -->
							       		<display:column>
							       			<s:radio list="%{#attr.modalitaPagamentoSupport.uid}" name="radioModPagSelezionato" theme="displaytag"></s:radio>
							        	</display:column>
							       </s:else>
							   
							    <display:column title="Numero d'ordine" property="codiceModalitaPagamento"/>
							    <display:column title="Modalit&agrave;">
							    	<%-- <a href="#" data-trigger="hover" rel="popover" data-container="#modTAB" 
							    	title="${attr.modalitaPagamentoSupport.modalitaAccreditoSoggetto.codice}&nbsp;-&nbsp;${attr.modalitaPagamentoSupport.modalitaAccreditoSoggetto.descrizione}" 
							    	data-content="${attr.modalitaPagamentoSupport.descrizioneForPopOver}">${attr.modalitaPagamentoSupport.modalitaAccreditoSoggetto.codice}&nbsp;-&nbsp;${attr.modalitaPagamentoSupport.modalitaAccreditoSoggetto.descrizione}</a> --%>
							   
							   <a href="#" data-trigger="hover" rel="popover" data-container="#modTAB" 
							    	title="${attr.modalitaPagamentoSupport.modalitaAccreditoSoggetto.codice}&nbsp;-&nbsp;${attr.modalitaPagamentoSupport.modalitaAccreditoSoggetto.descrizione}" 
							    	data-content="${attr.modalitaPagamentoSupport.descrizioneForPopOver}">${attr.modalitaPagamentoSupport.descrizioneInfo.descrizioneArricchita}</a>
							    </display:column>
							  	<display:column title="<abbr title='progressivo'>Associato a</abbr>" property="associatoA"/>
							  	<display:column title="Stato" property="descrizioneStatoModalitaPagamento" />
							  </display:table>
													 
						</fieldset >						
					</div>
				</div>
			</div>
		</div>
	</div>	
	
	<style type="text/css"> 
   		.popoverlarge {width:300px;}
  		#modTAB .popover {
    		width: 300px;
		}
  	</style>
  	
	<script type="text/javascript">	
		$('input[type=radio][name=radioModPagSelezionato]').change(function() {
	        var selectiondata = this.value;
	        $.ajax({
				//task-131 url: '<s:url method="resede"></s:url>',
				<!--task-155-->
				url: '<s:url  action="inserisciLiquidazioneStep2_resede"></s:url>',
				type: "GET",
				data: { selection: selectiondata },
			    success: function(data)  {
			    	$("#refreshSediSecondarie").html(data);
				}
			});
	    });

		
		$("[rel=popover]").popover({html: true});
		 
	</script>