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
						Sedi secondarie<span class="icon">&nbsp;</span></a>
					</a>
				</div>
			
				<div id="sediTAB" class="accordion-body collapse">
					<div class="accordion-inner">
						<fieldset class="form-horizontal">					
							<display:table name="listaSediSecondarieSoggetto" class="table table-hover tab_left" summary="riepilogo indirizzo" uid="sediSec" >
							       
							       <s:if test="presenzaOrdinativiPerLaLiquidazione">
							       		<!-- DISABILITATO -->
								        <display:column>
							       		<s:radio id="checkSedi" list="%{#attr.sediSec.uid}" name="radioSediSecondarieSoggettoSelezionato" theme="displaytag" disabled="true"></s:radio>
							        </display:column>
							       </s:if>
							       <s:else>
							     	    <!-- ABILITATO -->
							       		<display:column>
							       		<s:radio id="checkSedi" list="%{#attr.sediSec.uid}" name="radioSediSecondarieSoggettoSelezionato" theme="displaytag"></s:radio>
							        </display:column>
							       </s:else> 
							        
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

	<input type="hidden" value="<s:url method="remodpagamento" />" id="HIDDEN_url_remodpagamento" />
	<script src="${jspath}liquidazione/refreshModalitaPagamento.js" type="text/javascript"></script>
