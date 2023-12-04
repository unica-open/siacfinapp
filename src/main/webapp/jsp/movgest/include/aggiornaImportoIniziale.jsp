<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>



<label class="control-label" for="importoImpegnoSubimpegno">Importo *</label>
		<div class="controls">
			<s:textfield id="importoImpegnoSubimpegno" name="step1ModelSubimpegno.importoFormattato"  cssClass="span2 soloNumeri decimale parametroAggiornaImporto" disabled="!campoImportoAbilitato()"></s:textfield>
			<!--  se sono in inserimento non devo visualizzare importo iniziale -->
			
			
			<s:if test="verificaInserimentoSub()">
			
			    &nbsp; Importo Iniziale <s:property value="subDettaglio.importoIniziale"/> &nbsp;
<%-- 				<s:if test="oggettoDaPopolareSubimpegno()"> --%>
				
<%-- 				    &nbsp; Importo Iniziale IMPEGNO <span class="aggiornaImporto"> <s:property  value="impegnoInAggiornamento.importoIniziale"/></span> --%>
<%-- 				</s:if>  --%>
<%-- 				<s:else> --%>
<%-- 					&nbsp; Importo Iniziale ACCERTAMENTO <span class="aggiornaImporto"><s:property value="accertamentoInAggiornamento.importoIniziale"/></span> --%>
<%-- 				</s:else> --%>
			</s:if>	
			
			
			<span class="al"> <label
				class="radio inline" for="scadenzaSubimpegno">Scadenza</label>
			</span> <s:textfield id="scadenzaSubimpegno" title="gg/mm/aaaa" name="step1ModelSubimpegno.scadenza" cssClass="lbTextSmall span2 datepicker"></s:textfield>
		</div>

<script type="text/javascript">

	$(document).ready(function() {
		
		$('#importoImpegnoSubimpegno').change(function(){
			
			
			$.ajax({
				//task-131 url: '<s:url method="aggiornaContemporaneo"/>',
				url: '<s:url action="%{#aggiornaContemporaneoAction}"/>',
				type: 'POST',
				data: $(".parametroAggiornaImporto").serialize(),
				success: function(data)  {
					$("#gestioneAggiornaContemporaneo").html(data);
				}
			});

// 			var statoProvv = $('#statoProvvHidden').val();
// 			if(statoProvv!=null && statoProvv!="undefined" && statoProvv=="PROVVISORIO"){
// 				$(".aggiornaImporto").html("<s:property  value="impegnoInAggiornamento.importoIniziale"/>")
// 			}
		});
		
		$(".soloNumeri").allowedChars({numeric: true});
		
		 /* Formattazione corretta dei campi numerici */
	    $(".decimale").gestioneDeiDecimali();
		
	});
</script>