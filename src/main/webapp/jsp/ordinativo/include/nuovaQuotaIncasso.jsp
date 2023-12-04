<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<h4>Accertamento <i>(risultato della ricerca)</i></h4>
		<fieldset>
		
<!-- 		#attr.listaLiquidOrdinativiId.attoAmministrativoLiquidazione.tipoAtto.codice  TIPO PROVVEDIMENTO -->
		
		<display:table name="gestioneOrdinativoStep2Model.listaAccertamento" requestURI="gestioneOrdinativoIncassoStep2.do" keepStatus="true"  partialList="true" size="gestioneOrdinativoStep2Model.resultSize"
             				   pagesize="10" class="table table-hover tab_left" summary="riepilogo indirizzo" uid="listaAccertamentoOrdinativoId" >
             
             
             		<display:column> 
    						<s:radio  list="%{#attr.listaAccertamentoOrdinativoId.uid}" name="gestioneOrdinativoStep2Model.radioIdAccertamento" cssClass="accertamentoOrdinativo" theme="displaytag"></s:radio>
     				</display:column>
     				
     				
     				<display:column title="Accertamento">
     				<s:if test="isSubAccertamento(#attr.listaAccertamentoOrdinativoId.numeroAccertamentoPadre)">
     				     
     							<s:property	value="%{#attr.listaAccertamentoOrdinativoId.annoAccertamentoPadre}" />/<s:property	value="%{#attr.listaAccertamentoOrdinativoId.numeroAccertamentoPadre.intValue()}"/>/<s:property	value="%{#attr.listaAccertamentoOrdinativoId.numero.intValue()}" />
     					
      				</s:if> 
     				<s:else> 
     					
      							<s:property	value="%{#attr.listaAccertamentoOrdinativoId.annoMovimento}" />/<s:property	value="%{#attr.listaAccertamentoOrdinativoId.numero.intValue()}" /> 
     					
     				
     				</s:else>
     				</display:column>
     				<display:column title="descrizione" property="descrizione"/>
     				
     				<display:column title="Provvedimento">
     					<s:property	value="%{#attr.listaAccertamentoOrdinativoId.attoAmministrativo.anno}" />/<s:property	value="%{#attr.listaAccertamentoOrdinativoId.attoAmministrativo.numero}" />/
     					<s:property value="%{#attr.listaAccertamentoOrdinativoId.attoAmministrativo.strutturaAmmContabile.codice}"/>/<s:property value="%{#attr.listaAccertamentoOrdinativoId.attoAmministrativo.tipoAtto.codice}"/>
     				</display:column>
             
                    <display:column title="Importo accertamento" property="importoAttuale" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
                    <display:column title="Disponibile a incassare" property="disponibilitaIncassare" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
                    
<%--               	<display:column> --%>
<%--     						<s:radio list="%{#attr.listaLiquidOrdinativiId.idLiquidazione}" name="gestioneOrdinativoStep2Model.radioIdLiquidazione" cssClass="liquidazioneOrdinativo" theme="displaytag"></s:radio> --%>
<%--      				</display:column> --%>
<%--              		<display:column title="N. Liq."> --%>
<%--              				<s:property	value="%{#attr.listaLiquidOrdinativiId.annoLiquidazione}" />/<s:property value="%{#attr.listaLiquidOrdinativiId.numeroLiquidazione.intValue()}" /> --%>
<%--              		</display:column> --%>
<%-- 		                <display:column title="Descrizione Liquidazione" property="descrizioneLiquidazione"/> --%>
<%-- 		                <display:column title="Impegno"> --%>
<%-- 		                	<s:property	value="%{#attr.listaLiquidOrdinativiId.impegno.annoMovimento}" />/<s:property value="%{#attr.listaLiquidOrdinativiId.impegno.numero.intValue()}" />  --%>
<%-- 		                </display:column> --%>
		                
<%-- 		                <display:column title="Provvedimento"> --%>
<%-- 		                	<s:property	value="%{#attr.listaLiquidOrdinativiId.attoAmministrativoLiquidazione.anno}" />/<s:property	value="%{#attr.listaLiquidOrdinativiId.attoAmministrativoLiquidazione.numero}" /> --%>
<%-- 		                </display:column> --%>
<%-- 		                <display:column class="tab_Right" title="Importo Liquidazione" property="importoLiquidazione" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" /> --%>
<%-- 		                <display:column class="tab_Right" title="Disponibile a Pagare" property="disponibilitaPagare" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" /> --%>
             
             </display:table>
						
			 <s:hidden id="toggleLiquidazioneAperto" name="toggleLiquidazioneAperto" />
							
							
							<s:if test="abilitaNuovoAccertamento()">
							<!-- abilito btn se presente azione ins-Liq-Man ...  --> 
								<p>
									<!--
									    probabilmente il pulsante non serve a nulla
									    <a class="btn btn-secondary">seleziona liquidazione</a>  
									    
									 -->
									<span class="pull-right"><a class="btn btn-primary" href="nuovoAccertamentoOrdinativo.do">nuovo accertamento</a></span>
								</p>
							 </s:if>
							
						</fieldset>	
							
						<h4>Quota</h4>
						<fieldset class="form-horizontal">
						
							<div class="control-group">
								<label class="control-label" for="DescQU">Descrizione quota ordinativo*</label>
								<div class="controls">    
								  
								  <s:textfield id="descrizioneQuota" cssClass="lbTextSmall span9" name="gestioneOrdinativoStep2Model.descrizioneQuota" maxlength="500"/> 
								</div>
							</div>
							
							<div class="control-group">
								<label class="control-label" for="impQU">Importo *</label>
								<div class="controls">                         
								 
								  <s:textfield id="impQU" cssClass="lbTextSmall span2 soloNumeri decimale" onkeypress="return checkItNumbersCommaAndDotOnly(event)" name="gestioneOrdinativoStep2Model.importoQuotaFormattato" maxlength="15"/> 
								  <span class="al">
									<label class="radio inline" for="dataQU">Data scadenza quota ordinativo</label>
								  </span>

								   <s:textfield id="dataQU" cssClass="lbTextSmall span2 datepicker" name="gestioneOrdinativoStep2Model.dataEsecuzionePagamento" maxlength="10"/>
								 </div>
							</div>
							
						</fieldset>
						
						
<script type="text/javascript">
	
		$(document).ready(function() {
			

	
		});
		
		
		function ricaricaTE () {
			var selectiondata = this.value;
	        $.ajax({
				// task-131 url: '<s:url method="ricaricaTEByIdAccertamento"></s:url>',
				url: '<s:url action="%{#ricaricaTEByIdAccertamentoAction}"/>',				
				type: "POST",
				data: $(".accertamentoOrdinativo").serialize(), 
			    success: function(data)  {
			    	$("#refreshTE").html(data);
			    	
			    	setCheck();			
			    	$("#level").bind("change", setCheck);
			    	$("#all").bind("change", setCheck);
				}
			});
		}

		
		$('.accertamentoOrdinativo').change(function() {
			ricaricaTE();

	    });
	</script>			
					