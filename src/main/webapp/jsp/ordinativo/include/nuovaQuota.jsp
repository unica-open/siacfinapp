<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<h4>Liquidazione <i>(risultato della ricerca)</i></h4>
		<fieldset>
		
<!-- 		#attr.listaLiquidOrdinativiId.attoAmministrativoLiquidazione.tipoAtto.codice  TIPO PROVVEDIMENTO -->
		
		<display:table name="gestioneOrdinativoStep2Model.listaLiquidazioni" requestURI="gestioneOrdinativoPagamentoStep2.do" keepStatus="true" 
             				   pagesize="10" class="table table-hover tab_left" summary="riepilogo indirizzo" uid="listaLiquidOrdinativiId" >
             
              	<display:column>
    						<s:radio list="%{#attr.listaLiquidOrdinativiId.idLiquidazione}" name="gestioneOrdinativoStep2Model.radioIdLiquidazione" cssClass="liquidazioneOrdinativo" theme="displaytag"></s:radio>
     				</display:column>
             		<display:column title="N. Liq.">
             				<s:property	value="%{#attr.listaLiquidOrdinativiId.annoLiquidazione}" />/<s:property value="%{#attr.listaLiquidOrdinativiId.numeroLiquidazione.intValue()}" />
             		</display:column>
		                <display:column title="Descrizione Liquidazione" property="descrizioneLiquidazione"/>
		                <display:column title="Impegno">
			                
			                <s:if test="rigaImpegno(#attr.listaLiquidOrdinativiId).equals('subImpegno')">
			                	<s:property	value="%{#attr.listaLiquidOrdinativiId.impegno.annoMovimento}" />/<s:property value="%{#attr.listaLiquidOrdinativiId.impegno.numero.intValue()}" />-<s:property	value="%{#attr.listaLiquidOrdinativiId.subImpegno.numero.intValue()}" /> 
			                </s:if>
			                <s:elseif test="rigaImpegno(#attr.listaLiquidOrdinativiId).equals('elencoSub')">
			                	<s:property	value="%{#attr.listaLiquidOrdinativiId.impegno.annoMovimento}" />/<s:property value="%{#attr.listaLiquidOrdinativiId.impegno.numero.intValue()}" />-<s:property	value="%{#attr.listaLiquidOrdinativiId.impegno.elencoSubImpegni[0].numero.intValue()}" /> 
			                </s:elseif>
			                <s:elseif test="rigaImpegno(#attr.listaLiquidOrdinativiId).equals('impegno')">
			                	<s:property	value="%{#attr.listaLiquidOrdinativiId.impegno.annoMovimento}" />/<s:property value="%{#attr.listaLiquidOrdinativiId.impegno.numero.intValue()}" /> 
			                </s:elseif>
		                </display:column>
		                <display:column title="Mutuo" property="numeroMutuo" />
		                
		                <display:column title="Provvedimento">
		                	<s:property	value="%{#attr.listaLiquidOrdinativiId.attoAmministrativoLiquidazione.anno}" />/<s:property	value="%{#attr.listaLiquidOrdinativiId.attoAmministrativoLiquidazione.numero}" />/
		                	<s:property value="%{#attr.listaLiquidOrdinativiId.attoAmministrativoLiquidazione.strutturaAmmContabile.codice}"/>/<s:property value="%{#attr.listaLiquidOrdinativiId.attoAmministrativoLiquidazione.tipoAtto.codice}"/>
		                </display:column>
		                
		                
		                
		                <display:column title="Siope Tipo debito">
			                <s:property value="%{#attr.listaLiquidOrdinativiId.siopeTipoDebito.descrizione}"/>
						</display:column>
		                
		                <display:column title="Cig o motivazione assenza">
			                <s:if test="%{#attr.listaLiquidOrdinativiId.cig != null && #attr.listaLiquidOrdinativiId.cig != ''}">
									<s:property value="%{#attr.listaLiquidOrdinativiId.cig}"/>
							</s:if>
							<s:else>
								<s:property value="%{#attr.listaLiquidOrdinativiId.siopeAssenzaMotivazione.descrizione}"/>
							</s:else>
		                </display:column>
		                
		                <display:column  title="Importo Liquidazione" property="importoLiquidazione" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
		                <display:column  title="Disponibile a Pagare" property="disponibilitaPagare" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
             
             </display:table>
						
			 <s:hidden id="toggleLiquidazioneAperto" name="toggleLiquidazioneAperto" />
														
						</fieldset>	
							
						<h4>Quota</h4>
						<fieldset class="form-horizontal">
						
							<div class="control-group">
								<label class="control-label" for="DescQU">Descrizione *</label>
								<div class="controls">    
								  
								  <s:textfield id="descrizioneQuota" cssClass="lbTextSmall span9" name="gestioneOrdinativoStep2Model.descrizioneQuota" maxlength="500"/> 
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label" for="impQU">Importo *</label>
								<div class="controls">                         
								 
								  <s:textfield id="impQU" cssClass="lbTextSmall span2 soloNumeri decimale" onkeypress="return checkItNumbersCommaAndDotOnly(event)" name="gestioneOrdinativoStep2Model.importoQuotaFormattato" maxlength="15"/> 
								  <span class="al">
									<label class="radio inline" for="dataQU">Data esecuzione pagamento</label>
								  </span>

								   <s:textfield id="dataQU" cssClass="lbTextSmall span2 datepicker" name="gestioneOrdinativoStep2Model.dataEsecuzionePagamento" maxlength="10"/>
								 </div>
							</div>
							
						</fieldset>
						
						
<script type="text/javascript">
	
		$(document).ready(function() {
			
// 			// deselect radio sedi
// 			var radioChecked = $('.idSedeCreditore').is(':checked');

// 			$('.idSedeCreditore').click(function() {
// 			    radioChecked = !radioChecked;
// 			    $(this).attr('checked', radioChecked);
			   
// 			    if(!radioChecked){
// 			    	//$(".idSedeCreditore").val(null);
// 			    	//alert("valore radio "+$(".idSedeCreditore").val());
// 			    	idSedeCreditoreChecked = false;
// 			    	ricaricaSediModPag();
			    	
// 			    } else {
// 			    	idSedeCreditoreChecked = true;
// 			    }
// 			});
			

	
		});
		
		
		function ricaricaTE () {
			var selectiondata = this.value;
	        $.ajax({
				url: '<s:url method="ricaricaTEByIdLiquidazione"></s:url>',
				type: "POST",
				data: $(".liquidazioneOrdinativo").serialize(), 
			    success: function(data)  {
			    	$("#refreshTE").html(data);
				}
			});
		}

		
		$('.liquidazioneOrdinativo').change(function() {
			ricaricaTE();

	    });
	</script>			
					