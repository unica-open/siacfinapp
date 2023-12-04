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
					<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#modPAG" href="#modTAB">
						Modalit&agrave; pagamento *
						
                         <s:if test="gestioneOrdinativoStep1Model.modpagamentoSelezionata!=null">
							: <s:property value="gestioneOrdinativoStep1Model.modpagamentoSelezionata.modalitaAccreditoSoggetto.codice"/> - <s:property value="gestioneOrdinativoStep1Model.modpagamentoSelezionata.modalitaAccreditoSoggetto.descrizione"/> 
						 </s:if>
						<span class="datiPagamento"></span><span class="icon">&nbsp;</span></a>
					</a>
				</div>
			
				<div id="modTAB" class="accordion-body collapse">
					<div class="accordion-inner">
						<fieldset class="form-horizontal">	
							<display:table name="gestioneOrdinativoStep1Model.listaModalitaPagamentoVisualizza" class="table table-hover tab_left" summary="riepilogo modalita pagamento" uid="modalitaPagamentoSupport">
							        <display:column>
							       		<s:radio list="%{#attr.modalitaPagamentoSupport.uid}" cssClass="idModCreditore" name="gestioneOrdinativoStep1Model.radioModPagSelezionato" theme="displaytag"></s:radio>
							        </display:column>
							    <display:column title="Numero d'ordine" property="codiceModalitaPagamento"/>
							    <display:column title="Modalit&agrave;">
							    	<%-- <a href="#" data-trigger="hover" rel="popover" title="${attr.modalitaPagamentoSupport.modalitaAccreditoSoggetto.codice}&nbsp;-&nbsp;${attr.modalitaPagamentoSupport.modalitaAccreditoSoggetto.descrizione}" data-content="${attr.modalitaPagamentoSupport.descrizioneFormattata}">${attr.modalitaPagamentoSupport.descrizioneModalitaPagamento}</a> --%>
							    	<a href="#" data-trigger="hover" rel="popover" 
							    	title="${attr.modalitaPagamentoSupport.modalitaAccreditoSoggetto.codice}&nbsp;-&nbsp;${attr.modalitaPagamentoSupport.modalitaAccreditoSoggetto.descrizione}" 
							    	data-content="${attr.modalitaPagamentoSupport.descrizioneFormattata}">${attr.modalitaPagamentoSupport.descrizioneInfo.descrizioneArricchita}</a>
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
	
	<script type="text/javascript">	
		$('.idModCreditore').change(function() {
	        $.ajax({
	        	//task-131url: '<s:url method="resede"></s:url>',
				url: '<s:url action="%{#resedeAction}"/>',				   
				type: "POST",
				data: $(".idModCreditore").serialize(), 
			    success: function(data)  {
			    	$("#refreshSediSecondarie").html(data);
			        $.ajax({
						//task-131 url: '<s:url method="caricaTitoloModPag"></s:url>',
						url: '<s:url action="%{#caricaTitoloModPagAction}"/>',				   
						type: "POST",
						data: $(".idModCreditore").serialize(), 
					    success: function(data)  {
					    	$("#refreshModPagamento").html(data);
					    	
						}
					});
				}
			});
	    });
		
		$("[rel=popover]").popover({html: true});
		
	</script>
	
	
<!-- 	$('input[type=radio][name=radioModPagSelezionato]').change(function() { -->
<!-- 	        var selectiondata = this.value; -->
<!-- 	        $.ajax({ -->
<%-- 				url: '<s:url method="resede"></s:url>', --%>
<!-- 				type: "GET", -->
<!-- 				data: { selection: selectiondata }, -->
<!-- 			    success: function(data)  { -->
<!-- 			    	$("#refreshSediSecondarie").html(data); -->
<!-- 				} -->
<!-- 			}); -->
<!-- 	    }); -->