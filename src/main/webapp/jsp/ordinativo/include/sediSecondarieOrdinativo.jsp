<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>
	
	<div class="step-pane active" id="sediSEC">
		<div class="accordion" >
			<div class="accordion-group">
				<div class="accordion-heading">
					<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#sediSEC" href="#sediTAB">
						Sedi secondarie

<%-- 	   					<s:property value="gestioneOrdinativoStep1Model.sedeSelezionata.denominazione"/>  --%>
	   
						<span class="icon">&nbsp;</span></a>
					</a>
				</div>
			
				<div id="sediTAB" class="accordion-body collapse">
					<div class="accordion-inner">
						<fieldset class="form-horizontal">					
							<display:table name="gestioneOrdinativoStep1Model.listaSediSecondarie" class="table table-hover tab_left" summary="riepilogo indirizzo" uid="sediSec" >
							        <display:column>
							       		<s:radio id="checkSedi" list="%{#attr.sediSec.uid}" cssClass="idSedeCreditore" name="gestioneOrdinativoStep1Model.radioSediSecondarieSoggettoSelezionato" theme="displaytag"></s:radio>
							        </display:column>
							  	<display:column title="Denominazione" property="denominazione" />
							  	<display:column title="Indirizzo"><s:property value="%{#attr.sediSec.indirizzoSoggettoPrincipale.sedime}"/> <s:property value="%{#attr.sediSec.indirizzoSoggettoPrincipale.denominazione}"/> <s:property value="%{#attr.sediSec.indirizzoSoggettoPrincipale.numeroCivico}" /> <s:property value="%{#attr.sediSec.indirizzoSoggettoPrincipale.cap}" /></display:column>
							  	<display:column title="Comune" property="indirizzoSoggettoPrincipale.comune" />
							  	<display:column title="Stato" property="descrizioneStatoOperativoSedeSecondaria" />
							  </display:table>						
						</fieldset >
						
					</div>
				</div>
			</div>
		</div>
	</div>	
			
	<script type="text/javascript">
		var idSedeCreditoreChecked = true;
		$(document).ready(function() {
			
			// deselect radio sedi
			var radioChecked = $('.idSedeCreditore').is(':checked');

			var caricaTitolo = false;
			
			$('.idSedeCreditore').click(function() {
			    radioChecked = !radioChecked;
			    $(this).attr('checked', radioChecked);
			   
			    if(!radioChecked){
			    	//$(".idSedeCreditore").val(null);
			    	//alert("valore radio "+$(".idSedeCreditore").val());
			    	idSedeCreditoreChecked = false;
			    	caricaTitolo = false;
// 			    	alert("sede deselezionata");
                    ricaricaSediModPag(caricaTitolo);
			    	
			    } else {
			    	
			    	idSedeCreditoreChecked = true;
			    	caricaTitolo = true;
// 			    	alert("passo di qui "+idSedeCreditoreChecked);
			    	ricaricaSediModPag(caricaTitolo);
			    }
			});
			
			$('.idSedeCreditore').change(function() {
// 				alert("cambio sede");
// 				caricaTitolo
				caricaTitolo = true;
				ricaricaSediModPag(caricaTitolo);

		    });
	
		});
		
		
		function ricaricaSediModPag (caricaTitolo) {
			var selectiondata = this.value;
			var data;
			if (idSedeCreditoreChecked) {
				data = $(".idSedeCreditore").serialize() + "&idSedeCreditoreChecked=true";
			} else {
				data = "idSedeCreditoreChecked=false";
			}
			
			
			if(caricaTitolo){
			
		        $.ajax({
					url: '<s:url method="remodpagamento"></s:url>',
					type: "POST",
					data: data, 
				    success: function(data)  {
				    	$("#refreshModPagamento").html(data);
// 				    	$.ajax({
// 								url: '<s:url method="caricaTitoloSedi"></s:url>',
// 								type: "POST",
// 								data: data, 
// 							    success: function(data)  {
// 							    	$("#refreshSediSecondarie").html(data);
							    	
// 								}
// 							});
					}
				});
	        
			}else{
				// non carica il titolo
				 $.ajax({
						url: '<s:url method="remodpagamento"></s:url>',
						type: "POST",
						data: data, 
					    success: function(data)  {
					    	$("#refreshModPagamento").html(data);
					    	
						}
					});
				
			}
	        
		}

		
		
	</script>