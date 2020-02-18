<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<div id="guidaSogDue" class="modal hide fade" tabindex="-1" role="dialog"
	aria-labelledby="guidaSogLabel" aria-hidden="true">
	<div class="modal-body">
		<h4>Seleziona soggetto</h4>
		<!--     <p> E' possibile associare all'impegno un soggetto o una classe di soggetti</p> -->
		<fieldset class="form-horizontal">
			<div id="campiRicercaSogDue" class="accordion-body collapse in">
				<div class="control-group">
					<label class="control-label" for="codCreditoreRicercaDue">Codice</label>
					<div class="controls">
						<s:textfield id="codCreditoreRicercaDue"
							name="soggettoRicercaDue.codCreditore"
							cssClass="span3 parametroRicercaSoggettoDue" type="text" onkeyup="return checkItNumbersOnly(event)"  />
						<label class="radio inline" for="codfiscRicercaDue">Codice
							Fiscale</label>
						<s:textfield id="codfiscRicercaDue"
							name="soggettoRicercaDue.codfisc"
							cssClass="span4 parametroRicercaSoggettoDue" type="text" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="pivaRicercaDue">Partita IVA</label>
					<div class="controls">
						<s:textfield id="pivaRicercaDue"
							name="soggettoRicercaDue.piva"
							cssClass="span3 parametroRicercaSoggettoDue"  />
						<label class="radio inline" for="denominazioneDue">Denominazione</label>
						<s:textfield name="soggettoRicercaDue.denominazione"
							id="denominazioneDue" cssClass="span4 parametroRicercaSoggettoDue" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="listaClasseSoggettoRicercaDue">Classificatore</label>
					<div class="controls">
					
					 <s:if test="not listaClasseSoggetto.empty" >
						<s:select id="listaClasseSoggettoRicercaDue"
							list="listaClasseSoggetto"
							name="soggettoRicercaDue.classe"
							cssClass="span5 parametroRicercaSoggettoDue"
							title="Scegli il tipo di classificazione" listKey="id"
							listValue="codice+' - '+descrizione" headerKey="-1"
							headerValue="Scegli il tipo di classificazione" />
					</s:if>
					<s:else>
	     				 <s:select list="#{}" id="listaClasseSoggetto" 
	          		   	cssClass="span5"  
	       	 	      /> 
	     			</s:else>	
							
						<!--a class="btn btn-primary" href="#"><i class="icon-search icon"></i> cerca</a-->
					</div>
				</div>
			</div>
			
		</fieldset>

		<a class="accordion-toggle btn btn-primary pull-right" id="ricercaGuidataSoggettoDue" data-toggle="collapse" data-parent="#guidaCap" href="#campiRicercaSogDue">
		  <i class="icon-search icon"></i>&nbsp;cerca&nbsp;<span class="icon"> </span>
		</a>

		<div id="gestioneRisultatoRicercaSoggettiDue" style="clear:both; padding-top:3px;">
			<s:include value="/jsp/movgest/include/risultatoRicercaElencoSoggetti.jsp" />
		</div>

	</div>
	
	<div class="modal-footer">
		<!-- <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button> -->
		<s:submit cssClass="btn btn-primary" data-dismiss="modal"
			aria-hidden="true" value="Conferma" method="selezionaSoggettoDue"></s:submit>

	</div>
</div>

<script type="text/javascript">
  
    function initRicercaGuidataSoggettoDue (codice, classe) {
    	$("#codCreditoreRicercaDue").val(codice);
		$("#listaClasseSoggettoRicercaDue").val(classe);
		$("#campiRicercaSogDue").attr("class", "accordion-body collapse in");
		$("#campiRicercaSogDue").attr("style", "height: auto");
		$("#codfiscRicercaDue").val("");
		$("#pivaRicercaDue").val("");
		$("#denominazioneDue").val("");
		$.ajax({
			url: '<s:url method="pulisciRicercaSoggetto"/>',
		    success: function(data)  {
			    $("#gestioneRisultatoRicercaSoggettiDue").html(data);
			}
		});
	}

	$(document).ready(function() {
		$("#ricercaGuidataSoggettoDue").click(function() {
			$.ajax({
				url: '<s:url method="ricercaSoggettoDue"/>',
				type: 'POST',
				data: $(".parametroRicercaSoggettoDue").serialize(),
			    success: function(data)  {
				    $("#gestioneRisultatoRicercaSoggettiDue").html(data);
				}
			});
		});	
	});
	
</script>