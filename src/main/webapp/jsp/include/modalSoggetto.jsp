<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<div id="guidaSog" class="modal hide fade" tabindex="-1" role="dialog"
	aria-labelledby="guidaSogLabel" aria-hidden="true">
	<div class="modal-body">
		<h4>Seleziona soggetto</h4>
		<!--     <p> E' possibile associare all'impegno un soggetto o una classe di soggetti</p> -->
		<fieldset class="form-horizontal">
			<div id="campiRicercaSog" class="accordion-body collapse in">
				<div class="control-group">
					<label class="control-label" for="codCreditoreRicerca">Codice</label>
					<div class="controls">
						<s:textfield id="codCreditoreRicerca"
							name="soggettoRicerca.codCreditore"
							cssClass="span3 parametroRicercaSoggetto" type="text" onkeyup="return checkItNumbersOnly(event)"  />
						<label class="radio inline" for="codfiscRicerca">Codice
							Fiscale</label>
						<s:textfield id="codfiscRicerca"
							name="soggettoRicerca.codfisc"
							cssClass="span4 parametroRicercaSoggetto" type="text" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="pivaRicerca">Partita IVA</label>
					<div class="controls">
						<s:textfield id="pivaRicerca"
							name="soggettoRicerca.piva"
							cssClass="span3 parametroRicercaSoggetto"  />
						<label class="radio inline" for="denominazione">Denominazione</label>
						<s:textfield name="soggettoRicerca.denominazione"
							id="denominazione" cssClass="span4 parametroRicercaSoggetto" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="listaClasseSoggettoRicerca">Classificatore</label>
					<div class="controls">
					
					 <s:if test="not listaClasseSoggetto.empty" >
						<s:select id="listaClasseSoggettoRicerca"
							list="listaClasseSoggetto"
							name="soggettoRicerca.classe"
							cssClass="span5 parametroRicercaSoggetto"
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
				<!-- <div class="control-group">
      <label for="FPV" class="control-label"><abbr title="Fondo Pluriennale Vincolato">FPV</abbr></label>
      <div class="controls">
      <input id="FPV" type="checkbox" value="option1"/>  
      </div>
      </div> -->
			</div>
			
		</fieldset>

		<a class="accordion-toggle btn btn-primary pull-right" id="ricercaGuidataSoggetto" data-toggle="collapse" data-parent="#guidaCap" href="#campiRicercaSog">
		  <i class="icon-search icon"></i>&nbsp;cerca&nbsp;<span class="icon"> </span>
		</a>

		<div id="gestioneRisultatoRicercaSoggetti" style="clear:both; padding-top:3px;">
			<s:include value="/jsp/movgest/include/risultatoRicercaElencoSoggetti.jsp" />
		</div>

	</div>
	
	<div class="modal-footer">
		<!-- <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button> -->
		<!-- task-131 <s:submit cssClass="btn btn-primary" aria-hidden="true" value="Conferma" method="selezionaSoggetto"></s:submit> -->
		<s:submit cssClass="btn btn-primary" aria-hidden="true" value="Conferma" action="%{#selezionaSoggettoAction}"></s:submit>

	</div>
</div>

<script type="text/javascript">
  
    function initRicercaGuidataSoggetto (codice, classe) {
    	$("#codCreditoreRicerca").val(codice);
		$("#listaClasseSoggettoRicerca").val(classe);
		$("#campiRicercaSog").attr("class", "accordion-body collapse in");
		$("#campiRicercaSog").attr("style", "height: auto");
		$("#codfiscRicerca").val("");
		$("#pivaRicerca").val("");
		$("#denominazione").val("");
		$.ajax({
			//task-131 url: '<s:url method="pulisciRicercaSoggetto"/>',
			url: '<s:url action="%{#pulisciRicercaSoggettoAction}"/>',
		    success: function(data)  {
			    $("#gestioneRisultatoRicercaSoggetti").html(data);
			}
		});
	}

	$(document).ready(function() {
		$("#ricercaGuidataSoggetto").click(function() {
			$.ajax({
				//task-131 url: '<s:url method="ricercaSoggetto"/>',
				url: '<s:url action="%{#ricercaSoggettoAction}"/>',
				type: 'POST',
				data: $(".parametroRicercaSoggetto").serialize(),
			    success: function(data)  {
				    $("#gestioneRisultatoRicercaSoggetti").html(data);
				}
			});
		});	
	});
	
</script>