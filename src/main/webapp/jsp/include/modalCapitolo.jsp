<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<div id="guidaCap" class="modal hide fade" tabindex="-1" role="dialog"
	aria-labelledby="guidaCapLabel" aria-hidden="true">
	<div class="modal-body">

		<h4>Seleziona il capitolo</h4>
		<fieldset class="form-horizontal">
			<div id="campiRicerca" class="accordion-body collapse in">
				<div class="control-group">
					<label class="control-label" for="anno">Anno</label>
					<!--label class="radio inline" for="annoM">Anno</label-->
					<div class="controls">
						<s:textfield id="anno"
							cssClass="lbTextSmall span2 parametroRicercaCapitolo"
							name="capitoloRicerca.anno" disabled="true" />
						<span class="al"> <label class="radio inline"
							for="capitoloRicerca">Capitolo *</label>
						</span>
						<s:textfield id="capitoloRicerca"
							cssClass="lbTextSmall span2 parametroRicercaCapitolo"
							name="capitoloRicerca.numCapitolo" />
						<span class="al"> <label class="radio inline"
							for="articoloRicerca">Articolo *</label>
						</span>
						<s:textfield id="articoloRicerca"
							cssClass="lbTextSmall span2 parametroRicercaCapitolo"
							name="capitoloRicerca.articolo" />
						
						<s:if test ="visualizzaUEB()">
							<span class="al"> <label class="radio inline"
								for="uebRicerca">UEB</label>
							</span>
							<s:textfield id="uebRicerca"
								cssClass="lbTextSmall span2 parametroRicercaCapitolo"
								name="capitoloRicerca.ueb" />
						</s:if>
					</div>
				</div>
<!--   SEMBRA CHE AL MOMENTO VOGLIANO CHE IL PDC DEBBA ESSERE COMMENTATO

				<div class="control-group">
					<label class="control-label"><abbr title="Piano dei Conti">P.d.C.</abbr>
						finanziario <a class="tooltip-test"
						title="selezionare prima il macroaggregato" href="#"><i
							class="icon-info-sign">&nbsp;<span class="nascosto">selezionare
									prima il macroaggregato</span></i></a></label>
					<div class="controls">
						<div class="accordion span11" class="pianoConti">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a class="accordion-toggle" data-toggle="collapse"
										data-parent="#pianoConti" href="#pc"> Seleziona Piano dei
										conti</a>
								</div>
								<div id="pc" class="accordion-body collapse">
									<div class="accordion-inner">
										<ul id="elementiPdcModalCapitolo" class="ztree"></ul>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				-->
				<div class="control-group">
					<label class="control-label">Struttura Amministrativa</label>
					<div class="controls">
						<div class="accordion span11" class="struttAmm">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a class="accordion-toggle" data-toggle="collapse"
										data-parent="#struttAmm" href="#3"> Seleziona la Struttura
										amministrativa <i class="icon-spin icon-refresh spinner" id="spinnerStruttAmmRicercaCapitolo"></i></a>
								</div>
								<div id="3" class="accordion-body collapse">
								
								     <!-- ALBERO VISUALIZZATO -->
									<div class="accordion-inner" id="strutturaAmministrativaRicercaCapitoloDiv">
										<ul id="strutturaAmministrativaRicercaCapitolo"
											class="ztree treeStruttAmm"></ul>
									</div>
									 <!-- ALBERO IN ATTESA -->
									<div class="accordion-inner" id="strutturaAmministrativaRicercaCapitoloWait">
										 Attendere prego..
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">Tipo finanziamento</label>
					<div class="controls">
						<s:select list="listaTipoFinanziamento"
							id="listaTipoFinanziamento"
							cssClass="parametroRicercaCapitolo"
							headerKey="" headerValue=""
							name="capitoloRicerca.tipoFinanziamentoSelezionato"
							listKey="codice" listValue="descrizione" />

						<!--a class="btn btn-primary pull-right" href="#"><i class="icon-search icon"></i> cerca</a-->
					</div>
				</div>
			</div>

		</fieldset>


		<a class="accordion-toggle btn btn-primary pull-right" id="ricercaGuidataCapitolo" data-toggle="collapse" data-parent="#guidaCap" href="#campiRicerca"> 
		  <i class="icon-search icon"></i>&nbsp;cerca&nbsp;<span class="icon"></span>
		</a>

		<!-- SIAC-7349 -->
		<s:if test="componenteBilancioCapitoloAttivo">
		 <div id="gestioneRisultatoRicercaCapitoli" style="clear:both; padding-top:3px;">
		 		<s:include value="/jsp/movgest/include/risultatoRicercaElencoCapitoliComponentiBilancio.jsp" />
			
		</div>

		<div id="gestioneVisualizzaCapitoloComponentiBilancio">
			<s:include value="/jsp/movgest/include/visualizzaCapitoloComponentiBilancio.jsp" />
		</div>
		 </s:if>
		 <s:else>
		 <div id="gestioneRisultatoRicercaCapitoli" style="clear:both; padding-top:3px;">
			<s:include value="/jsp/movgest/include/risultatoRicercaElencoCapitoli.jsp" />
		</div>

		<div id="gestioneVisualizzaCapitolo">
			<s:include value="/jsp/movgest/include/visualizzaCapitolo.jsp" />
		</div>
		 </s:else>
		

	</div>
	
	
	
	
	<div class="modal-footer">
		<s:if test="componenteBilancioCapitoloAttivoRicerca">
<!-- 			SIAC-7739-7743 - inserimento id  -->
			<!-- task-131 <s:submit  name="cerca" value="conferma" id="cercaCapitoloGeneric" method="selezionaCapitolo" cssClass="btn btn-primary pull-right" />-->
			<s:submit  name="cerca" value="conferma" id="cercaCapitoloGeneric" action="%{#selezionaCapitoloAction}" cssClass="btn btn-primary pull-right" />
		
		</s:if>
		<s:else>
			<!--task-131 <s:submit id="cercaCapitoloSubmit" name="cerca" value="conferma" method="selezionaCapitolo" cssClass="btn btn-primary pull-right" /> -->
			<s:submit id="cercaCapitoloSubmit" name="cerca" value="conferma" action="%{#selezionaCapitoloAction}" cssClass="btn btn-primary pull-right" />
		</s:else>
		
		
		
	</div>

</div>



<script type="text/javascript">
  
    function initRicercaGuidataCapitolo (capitolo, articolo, ueb) {
    	$("#capitoloRicerca").val(capitolo);
		$("#articoloRicerca").val(articolo);
		$("#uebRicerca").val(ueb);
		$("#campiRicerca").attr("class", "accordion-body collapse in");
		$("#campiRicerca").attr("style", "height: auto");
		// al caricamento sparisce il dettaglio jira-932
		$("#visDett").hide();
		var url='';
		//CR - 1839	
		if(capitolo!='' && articolo!=''){
			// task-131 url = '<s:url method="ricercaCapitolo"/>';
			url = '<s:url action="%{#ricercaCapitoloAction}"/>';
			$.ajax({
				url: url,
				type: 'POST',
				data: $(".parametroRicercaCapitolo").serialize(),
				success: function(data)  {
				    $("#gestioneRisultatoRicercaCapitoli").html(data);
				}
			});
		}else	{
			
			//task-131 url = '<s:url method="pulisciRicercaCapitolo"/>';
			url = '<s:url action="%{#pulisciRicercaCapitoloAction}"/>';
			$.ajax({
				url: url,
				success: function(data)  {
				    $("#gestioneRisultatoRicercaCapitoli").html(data);
				}
			});
		}
		
		/*	
		$.ajax({
			//url: '<s:url method="pulisciRicercaCapitolo"/>',
			url: url,
			success: function(data)  {
			    $("#gestioneRisultatoRicercaCapitoli").html(data);
			}
		});*/
	}

	$(document).ready(function() {

		$("#ricercaGuidataCapitolo").click(function() {
			var treeObj = $.fn.zTree.getZTreeObj("strutturaAmministrativaRicercaCapitolo");
			var selectedNode = treeObj.getCheckedNodes(true);
			var strutturaAmministrativaSelezionata = {};
			strutturaAmministrativaSelezionata.codice = "";
			strutturaAmministrativaSelezionata.tipo = "";
			if (selectedNode != null) {
				selectedNode.forEach(function(currentNode) {
				    strutturaAmministrativaSelezionata = currentNode;
				});
			}
			/*Commentato per ricerca PDC su modalCapitolo troppo lenta
			var treeObjPdc = $.fn.zTree.getZTreeObj("elementiPdcModalCapitolo");
			var selectedNodePdc = treeObjPdc.getCheckedNodes(true); */
			var pdcSelezionato = {};
			pdcSelezionato.codice = "";
			/* if (selectedNodePdc != null) {
				selectedNodePdc.forEach(function(currentNodePdc) {
					pdcSelezionato = currentNodePdc;
				});
			} */
			// al click di ricerca sparisce il dettaglio jira-932
			$("#visDett").hide();
			$.ajax({
				//task-131 url: '<s:url method="ricercaCapitolo"/>',
				url: '<s:url action="%{#ricercaCapitoloAction}"/>',
				type: 'POST',
				data: $(".parametroRicercaCapitolo").serialize() + "&strutturaAmministrativaSelezionata=" + strutturaAmministrativaSelezionata.codice + "&tipoStrutturaAmministrativaSelezionata=" + strutturaAmministrativaSelezionata.tipo + "&pdcSelezionato=" + pdcSelezionato.codice,
				success: function(data)  {
					$("#gestioneRisultatoRicercaCapitoli").html(data);
				}
			});
		});	



		
		//SIAC-7349
		$("#cercaCapitoloSubmit").click(function() {
			var isComponenteBilancioAttivo = "${componenteBilancioCapitoloAttivo}";
			if(typeof isComponenteBilancioAttivo !=='undefined' && isComponenteBilancioAttivo == 'true'){
				if(typeof $("#listaComponenteBilancioRicerca") ==='undefined' || $("#listaComponenteBilancioRicerca").val()==''){
					$("#errorComponente").addClass( "alert alert-error" )
					$("#errorComponente").html('Componente obbligatoria<br>');
					//SIAC-7739-7743
					$("#capitolo").val($("#capitoloRicerca").val());
					$("#articolo").val($("#articoloRicerca").val());
					var $options = $("#listaComponenteBilancioRicerca > option").clone();
					$('#listaComponenteBilancio').html('');
					$('#listaComponenteBilancio').append($options);
					$('.modal-body').animate({ scrollTop: $(document).height() }, 2500);				
					return false;
				}

			}
		});

		
		
	});
	
</script>
