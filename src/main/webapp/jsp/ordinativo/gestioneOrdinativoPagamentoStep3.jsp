<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>



<%-- Inclusione head e CSS NUOVO --%>
<s:include value="/jsp/include/head.jsp" />

<%-- Inclusione JavaScript NUOVO --%>
<s:include value="/jsp/include/javascript.jsp" />
<s:include value="/jsp/include/javascriptTree.jsp" />
</head>

<body>

	<s:include value="/jsp/include/header.jsp" />

	<!-- NAVIGAZIONE 
  <p class="nascosto"><a name="A-sommario" title="A-sommario"></a></p>     
  <ul id="sommario" class="nascosto">
    <li><a href="#A-contenuti">Salta ai contenuti</a></li>
  </ul>
  /NAVIGAZIONE -->
	<hr />
	<div class="container-fluid-banner">
		<a name="A-contenuti" title="A-contenuti"></a>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">

				<s:form id="%{labels.FORM}" action="%{labels.FORM}" method="post">
					<s:if test="oggettoDaPopolarePagamento()">
						<s:set var="oggetto" value="%{'Pagamento'}" />
						<s:set var="selezionaProvvisorioAction" value="%{'gestioneOrdinativoPagamentoStep3_selezionaProvvisorio'}" />
		    			<s:set var="aggiornaBottonieraAction" value="%{'gestioneOrdinativoPagamentoStep3_aggiornaBottoniera'}" />
		    			<s:set var="dettaglioAggiornaCopertureAction" value="%{'gestioneOrdinativoPagamentoStep3_dettaglioAggiornaCoperture'}" />	   		           
						<!-- per aggiornaBottoniera.jsp -->
						<s:set var="pulisciProvvisorioAction" value="%{'gestioneOrdinativoPagamentoStep3_pulisciProvvisorio'}" />	   		           
						<s:set var="inserisciProvvisorioAction" value="%{'gestioneOrdinativoPagamentoStep3_inserisciProvvisorio'}" />	   		           	
						<!-- per modalOrdinativo.jsp -->
						<s:set var="eliminaQuotaOrdinativoAction" value="%{'gestioneOrdinativoPagamentoStep3_eliminaQuotaOrdinativo'}" />
						<s:set var="eliminaProvvisorioAction" value="%{'gestioneOrdinativoPagamentoStep3_eliminaProvvisorio'}" />
						<s:set var="forzaInserisciQuotaAccertamentoAction" value="%{'gestioneOrdinativoPagamentoStep3_forzaInserisciQuotaAccertamento'}" />
						<s:set var="forzaAggiornaQuotaAccertamentoAction" value="%{'gestioneOrdinativoPagamentoStep3_forzaAggiornaQuotaAccertamento'}" />				
					</s:if>
					<s:else>
						<s:set var="oggetto" value="%{'Incasso'}" />
						<s:set var="selezionaProvvisorioAction" value="%{'gestioneOrdinativoIncassoStep3_selezionaProvvisorio'}" />
		    			<s:set var="aggiornaBottonieraAction" value="%{'gestioneOrdinativoIncassoStep3_aggiornaBottoniera'}" />
		    			<s:set var="dettaglioAggiornaCopertureAction" value="%{'gestioneOrdinativoIncassoStep3_dettaglioAggiornaCoperture'}" />	   		           
						<!-- per aggiornaBottoniera.jsp -->
						<s:set var="pulisciProvvisorioAction" value="%{'gestioneOrdinativoIncassoStep3_pulisciProvvisorio'}" />	   		           
						<s:set var="inserisciProvvisorioAction" value="%{'gestioneOrdinativoIncassoStep3_inserisciProvvisorio'}" />
						<!-- per modalOrdinativo.jsp -->
						<s:set var="eliminaQuotaOrdinativoAction" value="%{'gestioneOrdinativoIncassoStep3_eliminaQuotaOrdinativo'}" />
						<s:set var="eliminaProvvisorioAction" value="%{'gestioneOrdinativoIncassoStep3_eliminaProvvisorio'}" />
						<s:set var="forzaInserisciQuotaAccertamentoAction" value="%{'gestioneOrdinativoIncassoStep3_forzaInserisciQuotaAccertamento'}" />
						<s:set var="forzaAggiornaQuotaAccertamentoAction" value="%{'gestioneOrdinativoIncassoStep3_forzaAggiornaQuotaAccertamento'}" />
					</s:else>

					<a id="ancorettaInsProvErrori"></a>
					<s:include value="/jsp/include/actionMessagesErrors.jsp" />
					<s:if test="sonoInAggiornamento()">
						<h3>
							Ordinativo <s:property	value="gestioneOrdinativoStep1Model.ordinativo.numero" />
							del <s:property	value="%{gestioneOrdinativoStep1Model.ordinativo.dataEmissione}" />
							- Stato <s:property	value="gestioneOrdinativoStep1Model.ordinativo.statoOperativoOrdinativo" />
							dal <s:property	value="%{gestioneOrdinativoStep1Model.ordinativo.dataInizioValidita}" />
						</h3>
					</s:if>
					<s:else>
						<h3>Provvisori di cassa a copertura</h3>
					</s:else>

					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1" class="complete"><span class="badge badge-success">1</span>Dati ordinativo<span class="chevron"></span></li>
							<li data-target="#step2" class="complete"><span class="badge badge-success">2</span>Quote ordinativo<span class="chevron"></span></li>
							<s:if test="attivaVideataProvCassa()">
								<li data-target="#step3" class="active"><span class="badge">3</span>Provvisori di cassa<span class="chevron"></span></li>
							</s:if>
						</ul>
					</div>
					<!-- insProvvisorio -->
					<s:hidden id="toggleRicercaProvvAperto" name="toggleRicercaProvvAperto" />
					<div class="step-content">
						<div class="step-pane active" id="step2">
							<s:include value="/jsp/ordinativo/include/headerDettaglioOrdinativo.jsp" />
							<a id="ancorettaInsProv"></a>
							<h4>Coperture</h4>

							<s:if test="oggettoDaPopolarePagamento()">
								<display:table
									name="gestioneOrdinativoStep3Model.listaCoperture"
									class="table tab_left table-hover" summary="riepilogo provv"
									requestURI="gestioneOrdinativoPagamentoStep3.do"
									uid="tabCopertureID" pagesize="5">

									<display:column title="Anno" property="anno" />
									<display:column title="N. Provvisorio" property="numero" />
									<display:column title="Causale" property="causale" />
									<display:column title="Importo regolarizzato" property="importo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
									<display:column>
										<div class="btn-group">
											<button class="btn dropdown-toggle" data-toggle="dropdown"> Azioni <span class="caret"></span>	</button>
											<ul class="dropdown-menu pull-right">
												<li><a id="linkAggiorna_<s:property value="%{#attr.tabCopertureID.idProvvisorioDiCassa}"/>"
													href="#aggProvvisorioCassa" data-toggle="modal"
													class="linkAggiornaCopertura">aggiorna</a>
												<li><a id="linkElimina_<s:property value="%{#attr.tabCopertureID.idProvvisorioDiCassa}"/>"
													href="#msgEliminaProvvisorio" data-toggle="modal"
													class="linkEliminaProvvisorio">elimina</a>
											</ul>
										</div>
									</display:column>
									<display:column></display:column>
									<display:footer>
										<tr class="borderBottomLight">
											<th></th>
											<th scope="col" colspan="2"></th>
											<th scope="col">Totale Ordinativo</th>
											<th scope="col" class="tab_Right"><s:property value="getText('struts.money.format', {model.gestioneOrdinativoStep3Model.totalizzatorePagato})" />&nbsp;</th>
											<th scope="col"></th>
										</tr>
										<tr class="borderBottomLight">
											<th></th>
											<th scope="col" colspan="2"></th>
											<th scope="col">Totale su Ordinativo</th>
											<th scope="col" class="tab_Right"><s:property value="getText('struts.money.format', {model.gestioneOrdinativoStep3Model.totalizzatoreOrdinativo})" />&nbsp;</th>
											<th scope="col" class="tab_Right"></th>
										</tr>
										<tr class="borderBottomLight">
											<th></th>
											<th scope="col" colspan="2"></th>
											<th scope="col">Da Collegare</th>
											<th scope="col" class="tab_Right"><s:property value="getText('struts.money.format', {model.gestioneOrdinativoStep3Model.totalizzatoreDaCollegare})" />&nbsp;</th>
											<th scope="col" class="tab_Right"></th>
										</tr>
									</display:footer>
								</display:table>
							</s:if>
							<s:else>
								<!-- task-131 gestioneOrdinativoIncassoStep3.do -->
								<display:table
									name="gestioneOrdinativoStep3Model.listaCoperture"
									class="table tab_left table-hover" summary="riepilogo provv"
									requestURI="gestioneOrdinativoIncassoStep3.do"
									uid="tabCopertureID" pagesize="5">

									<display:column title="Anno" property="anno" />
									<display:column title="N. Provvisorio" property="numero" />
									<display:column title="Causale" property="causale" />
									<display:column title="Importo regolarizzato" property="importo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
									<display:column>
										<div class="btn-group">
											<button class="btn dropdown-toggle" data-toggle="dropdown"> Azioni <span class="caret"></span> </button>
											<ul class="dropdown-menu pull-right">
												<li><a id="linkAggiorna_<s:property value="%{#attr.tabCopertureID.idProvvisorioDiCassa}"/>"
													href="#aggProvvisorioCassa" data-toggle="modal"
													class="linkAggiornaCopertura">aggiorna</a>
												<li><a id="linkElimina_<s:property value="%{#attr.tabCopertureID.idProvvisorioDiCassa}"/>"
													href="#msgEliminaProvvisorio" data-toggle="modal"
													class="linkEliminaProvvisorio">elimina</a>
											</ul>
										</div>
									</display:column>
									<display:column></display:column>
									<display:footer>
										<tr class="borderBottomLight">
											<th></th>
											<th scope="col" colspan="2"></th>
											<th scope="col">Totale Ordinativo</th>
											<th scope="col" class="tab_Right"><s:property value="getText('struts.money.format', {model.gestioneOrdinativoStep3Model.totalizzatorePagato})" />&nbsp;</th>
											<th scope="col"></th>
										</tr>
										<tr class="borderBottomLight">
											<th></th>
											<th scope="col" colspan="2"></th>
											<th scope="col">Totale su Ordinativo</th>
											<th scope="col" class="tab_Right"><s:property value="getText('struts.money.format', {model.gestioneOrdinativoStep3Model.totalizzatoreOrdinativo})" />&nbsp;</th>
											<th scope="col" class="tab_Right"></th>
										</tr>
										<tr class="borderBottomLight">
											<th></th>
											<th scope="col" colspan="2"></th>
											<th scope="col">Da Collegare</th>
											<th scope="col" class="tab_Right"><s:property value="getText('struts.money.format', {model.gestioneOrdinativoStep3Model.totalizzatoreDaCollegare})" />&nbsp;</th>
											<th scope="col" class="tab_Right"></th>
										</tr>
									</display:footer>
								</display:table>
							</s:else>

							<p>
								<a id="puntoQui" class="btn" data-toggle="collapse" data-target="#insProvvisorioTable">inserisci provvisorio</a>
							</p>

							<div id="insProvvisorioTable" class="collapse">
								<div class="accordion_info">
									<div class="step-pane active" id="step1">
										<p>
											<a class="btn btn-primary pull-right" href="#RicProvvisorio" data-toggle="modal">cerca provvisorio</a>
										</p>
										<br> <a id="ancoretta"></a>
										<h4>
											Elenco Provvisori <i>(risultato della ricerca)</i>
										</h4>
										<fieldset>
											<s:if test="oggettoDaPopolarePagamento()">
												<!-- ORDINATIVO PAGAMENTO -->
												<!-- task-131 gestioneOrdinativoPagamentoStep3.do  -->
												<display:table	name="gestioneOrdinativoStep3Model.listaProvvisori"
													class="table tab_left table-hover"
													summary="riepilogo provv"
													requestURI="gestioneOrdinativoPagamentoStep3.do"
													uid="tabElProvvOrdinativiID" pagesize="10"
													keepStatus="true" clearStatus="${clearStatus}">

													<display:column>
														<s:radio id="radioSediProvvisori"
															list="%{#attr.tabElProvvOrdinativiID.idProvvisorioDiCassa}"
															name="uidProvvDaRicerca" cssClass="radioProvvCassa"
															theme="displaytag">
														</s:radio>

													</display:column>
													<display:column title="Anno" property="anno" />
													<display:column title="Numero Provvisorio" property="numero" />
													<display:column title="Data" property="dataEmissione" format="{0,date,dd/MM/yyyy}" />
													<display:column title="Causale" property="causale" />
													<display:column title="Soggetto Creditore" property="denominazioneSoggetto" />
													<display:column title="Importo">
														<s:property value="%{#attr.tabElProvvOrdinativiID.importo}" />
													</display:column>
													<display:column title="Importo da Regolarizzare">
														<s:property	value="%{#attr.tabElProvvOrdinativiID.importoDaRegolarizzare}" />
													</display:column>

												</display:table>
											</s:if>
											<s:else>
												<!-- ORDINATIVO INCASSO -->
												<!-- task-131 gestioneOrdinativoIncassoStep3.do  -->
												<display:table
													name="gestioneOrdinativoStep3Model.listaProvvisori"
													class="table tab_left table-hover"
													summary="riepilogo provv"
													requestURI="gestioneOrdinativoIncassoStep3.do"
													uid="tabElProvvOrdinativiID" pagesize="10"
													keepStatus="true" clearStatus="${clearStatus}">

													<display:column>
														<s:radio id="radioSediProvvisori"
															list="%{#attr.tabElProvvOrdinativiID.idProvvisorioDiCassa}"
															name="uidProvvDaRicerca" cssClass="radioProvvCassa"
															theme="displaytag">
														</s:radio>

													</display:column>
													<display:column title="Anno" property="anno" />
													<display:column title="Numero Provvisorio"
														property="numero" />
													<display:column title="Data" property="dataEmissione" format="{0,date,dd/MM/yyyy}" />
													<display:column title="Causale" property="causale" />
													<display:column title="Soggetto Creditore" property="denominazioneSoggetto" />
													<display:column title="Importo">
														<s:property value="%{#attr.tabElProvvOrdinativiID.importo}" />
													</display:column>
													<display:column title="Importo da Regolarizzare">
														<s:property value="%{#attr.tabElProvvOrdinativiID.importoDaRegolarizzare}" />
													</display:column>
												</display:table>
											</s:else>

											<span class="btnSelezionaVisible"> <!-- <p> -->
												<%-- <s:submit name="seleziona_provvisorio" value="seleziona provvisorio" method="selezionaProvvisorio" cssClass="btn btn-secondary" /> --%>
												<!--  </p> -->
											</span>
										</fieldset>

										<!--  REGOLA PROVVISORIO -->
										<a id="ancorettaProv"></a>
										<div id="aggiornaRegolaProvv">
											<s:include
												value="/jsp/ordinativo/include/regolaProvvisorio.jsp" />
										</div>

										<div class="Border_line"></div>

									</div>

									<!-- aggiorna bottoni ajax -->
									<div id="aggiornaBottoniera">
										<s:include
											value="/jsp/ordinativo/include/aggiornaBottoniera.jsp" />
									</div>

								</div>
							</div>

						</div>
					</div>


					<!--modale consulta RicProvvisorio -->
					<div id="RicProvvisorio" class="modal hide fade" tabindex="-1"
						role="dialog" aria-labelledby="RicProvvisorioLabel"
						aria-hidden="true">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal"
								aria-hidden="true">&times;</button>
							<h4 class="nostep-pane">Ricerca provvisori</h4>
						</div>
						<div class="modal-body">

							<fieldset class="form-horizontal">
								<div class="control-group">
									<label class="control-label" for="DaAnno">Da</label>
									<div class="controls">
										<span class="al"> <label class="radio inline"
											for="DaAnno">Anno </label>
										</span>
										<s:textfield name="ricercaProvvisorioModel.annoProvvisorioDa"
											id="annoDa" cssClass="lbTextSmall span2 soloNumeri"
											maxlength="4" />

										<span class="al"> <label class="radio inline"
											for="DaNum">Numero </label>
										</span>
										<s:textfield
											name="ricercaProvvisorioModel.numeroProvvisorioDa"
											id="numeroDa" cssClass="lbTextSmall span2 soloNumeri"
											maxlength="15" />

									</div>
								</div>

								<div class="control-group">
									<label class="control-label" for="AAnno">A</label>
									<div class="controls">
										<span class="al"> <label class="radio inline"
											for="AAnno">Anno </label>
										</span>
										<s:textfield name="ricercaProvvisorioModel.annoProvvisorioA"
											id="annoA" cssClass="lbTextSmall span2 soloNumeri"
											maxlength="4" />

										<span class="al"> <label class="radio inline"
											for="ANum">Numero </label>
										</span>
										<s:textfield name="ricercaProvvisorioModel.numeroProvvisorioA"
											id="numeroA" cssClass="lbTextSmall span2 soloNumeri"
											maxlength="15" />



									</div>
								</div>

								<div class="control-group">
									<label class="control-label" for="Emissione">Data emissione</label>
									<div class="controls">
										<span class="alLeft"> <label class="radio inline" for="EmissioneDa">Da </label>
										</span>
										<s:textfield name="ricercaProvvisorioModel.dataInizio" id="dataEmissioneDa" cssClass="lbTextSmall span2" maxlength="10" />

										<span class="marginLeft4"></span> <span class="al"> <label
											class="radio inline" for="EmissioneA">A </label>
										</span>
										<s:textfield name="ricercaProvvisorioModel.dataFine" id="dataEmissioneA" cssClass="lbTextSmall span2" maxlength="10" />

										<%-- 					<span class="alLeft"> --%>
										<!-- 						<a class="btn btn-primary "><i class="icon-search icon"></i>cerca&nbsp;</a>   -->
										<%-- 					</span>	 --%>
									</div>
								</div>

								<div class="control-group">
									<label class="control-label" for="Importo">Importo</label>
									<div class="controls">
										<span class="alLeft"> <label class="radio inline" for="importoDa">Da </label>
										</span>
										<s:textfield name="ricercaProvvisorioModel.importoDa"
											id="importoDa" cssClass="span2 soloNumeri decimale"
											onkeypress="return checkItNumbersCommaAndDotOnly(event)"
											maxlength="12" />

										<span class="marginLeft4"></span> <span class="al"> <label
											class="radio inline" for="ImportoA">A </label>
										</span>
										<s:textfield name="ricercaProvvisorioModel.importoA" id="importoA" cssClass="span2 soloNumeri decimale"
											onkeypress="return checkItNumbersCommaAndDotOnly(event)" />

									</div>
								</div>

								<!-- SIAC-6356 -->

								<div class="control-group">
									<label class="control-label" for="distinta">Conto
										evidenza</label>
									<div class="controls">
										<s:select
											list="gestioneOrdinativoStep3Model.listaContoTesoriere"
											id="listacontoTesoriere" headerKey="" headerValue=""
											name="ricercaProvvisorioModel.contoTesoriere"
											cssClass="span4" listKey="codice"
											listValue="codice + ' - ' + descrizione" />
									</div>
								</div>

								<div class="control-group">
									<label class="control-label" for="DescCaus">Descrizione causale</label>
									<div class="controls">
										<s:textfield rows="2" cols="15" id="DescCaus" name="ricercaProvvisorioModel.descCausale" cssClass="span9"></s:textfield>
									</div>
								</div>


								<div class="control-group">
									<label class="control-label" for="DescSogg">Descrizione
										soggetto</label>
									<div class="controls">
										<s:textfield rows="2" cols="15" id="DescSogg" name="ricercaProvvisorioModel.denominazioneSoggetto" cssClass="span9"></s:textfield>
									</div>
								</div>
								<!-- SIAC-6356 -->

							</fieldset>
						</div>
						<div class="modal-footer">
							<!-- task-131 <s:submit id="cercaProvvisorioSubmit" name="cerca" value="conferma"	method="cercaProvvisorio" cssClass="btn btn-primary" /> -->
							<s:submit id="cercaProvvisorioSubmit" name="cerca" value="conferma" action="gestioneOrdinativo%{#oggetto}Step3_cercaProvvisorio" cssClass="btn btn-primary" />

						</div>
					</div>
					<!--/modale ricerca provvisorio -->
					<s:include value="/jsp/ordinativo/include/modalOrdinativo.jsp" />
					<!--#include virtual="include/modal.html" -->
			</div>
			<!-- messo qui div chiuso perche' la display table aggiungi un div e probabilemente rimane aperto, causando una scorretta visualizzazione dei bottoni -->
			<!-- INIZIO BOTTONI  -->
			<p class="margin-medium">
				<s:include value="/jsp/include/indietro.jsp" />
				<span class="pull-right"> 
					<s:if	test="!sonoInAggiornamentoOR()">
						<!-- task-131 <s:submit id="salvaProvv" name="salva" value="salva" method="salvaProvvisori" cssClass="btn btn-primary freezePagina" /> -->
						<s:submit id="salvaProvv" name="salva" value="salva" action="gestioneOrdinativo%{#oggetto}Step3_salvaProvvisori" cssClass="btn btn-primary freezePagina" />					
					</s:if> 
					<s:else>
						<s:if test="sonoInAggiornamento()">
							<!-- task-131 <s:submit id="aggiornaProv" name="salva" value="salva" method="aggiornaOrdinativo" cssClass="btn btn-primary freezePagina" /> -->
							<s:submit id="aggiornaProv" name="salva" value="salva" action="gestioneOrdinativoPagamentoStep3_aggiornaOrdinativo" cssClass="btn btn-primary freezePagina" />
						</s:if>
						<s:elseif test="sonoInAggiornamentoIncasso()">
							<!-- task-131 <s:submit id="aggiornaProv" name="salva" value="salva" method="aggiornaOrdinativoIncasso" cssClass="btn btn-primary freezePagina" /> -->
							<s:submit id="aggiornaProv" name="salva" value="salva" action="gestioneOrdinativoIncassoStep3_aggiornaOrdinativoIncasso" cssClass="btn btn-primary freezePagina" />
						</s:elseif>
					</s:else> <!-- <a class="btn btn-primary" href="FIN-AggOrdinativoStep1.shtml">salva</a> -->

				</span>
			</p>
			<s:hidden id="ancoraVisualizza" name="ancoraVisualizza" />
			<s:hidden id="ancoraVisualizzaInserisciProv" name="ancoraVisualizzaInserisciProv" />
			<s:hidden id="ancoraVisualizzaInserisciProvErrori" name="ancoraVisualizzaInserisciProvErrori" />
			<s:hidden id="idCoperturaDaEliminare" name="idCoperturaDaEliminare" />
			</s:form>
		</div>
	</div>
	</div>
	<script type="text/javascript">
	
	
	$(document).ready(function() {
		var insProvvisorio = $("#puntoQui");
		var toggleRicercaProvvAperto = $("#toggleRicercaProvvAperto");
		if (toggleRicercaProvvAperto.val() == "true") {
			insProvvisorio.click();
		} 

		if($("#ancoraVisualizza").val() == 'true')
		{
			// metodo in generic.js che dato id ancora ti sposta con animazione
			spostaLAncora('ancoretta');
		}
				
		if($("#ancoraVisualizzaInserisciProv").val() == 'true')
		{
			// metodo in generic.js che dato id ancora ti sposta con animazione
			spostaLAncora('ancorettaInsProv');
		}
				
		if($("#ancoraVisualizzaInserisciProvErrori").val() == 'true')
		{
			// metodo in generic.js che dato id ancora ti sposta con animazione
			spostaLAncora('ancorettaInsProvErrori');
		}
		
		$(".radioProvvCassa").change(function(){
			$.ajax({
				// task-131 url: '<s:url method="selezionaProvvisorio"/>',
				url: '<s:url action="%{#selezionaProvvisorioAction}"/>',
				type: 'POST',
				data: 'uidProvvDaRicerca=' + $(this).val(),
			    success: function(data)  {
				    $("#aggiornaRegolaProvv").html(data);
				    spostaLAncora('ancorettaProv');
				    // paolo
				    $.ajax({
						//task-131 url: '<s:url method="aggiornaBottoniera"></s:url>',
						url: '<s:url action="%{#aggiornaBottonieraAction}"/>',
						type: "GET",
					    success: function(data)  {
					    	$("#aggiornaBottoniera").html(data);
					    	
						}
					}); 
				    // paolo
				}
			});
		});
		
		
		$(".linkAggiornaCopertura").click(function() {
			var supportId = $(this).attr("id").split("_");
			$.ajax({
				//task-131 url: '<s:url method="dettaglioAggiornaCoperture"/>',
				url: '<s:url action="%{#dettaglioAggiornaCopertureAction}"/>',
				type: 'POST',
				data: 'numeroPerDettaglioCopertura=' + supportId[1],
			    success: function(data)  {
				    $("#divDettaglioAggiornaProvvisorioCassa").html(data);
				}
			});
		});	
		
		$(".linkEliminaProvvisorio").click(function() {
			var supportId = $(this).attr("id").split("_");
			if (supportId != null && supportId.length > 0) {
				$("#idCoperturaDaEliminare").val(supportId[1]);
			}
		});
		

		
		
	});
	
	
	<s:if test="oggettoDaPopolarePagamento()">
		<!-- ORDINATIVO PAGAMENTO onReady specializzata -->
		$(document).ready(function() {

			// nella popUp di ricercaProvv copia annoDa -> annoA
			$("#annoDa").blur(function() {
				$("#annoA").val( this.value);				
			});
			
			// nella popUp di ricercaProvv copia numeroDa -> numeroA
			$("#numeroDa").blur(function() {
				$("#numeroA").val( this.value);				
			});
			
			
			// nella popUp di ricercaProvv copia dataDa -> dataA
			$("#dataEmissioneDa").blur(function() {
				$("#dataEmissioneA").val( this.value);				
			});
			
		});
	</s:if>
	<s:else>
		<!-- ORDINATIVO INCASSO onReady specializzata -->
		$(document).ready(function() {
			
			// nella popUp di ricercaProvv copia annoDa -> annoA
			$("#annoDa").blur(function() {
				$("#annoA").val( this.value);				
			});
			
			// nella popUp di ricercaProvv copia numeroDa -> numeroA
			$("#numeroDa").blur(function() {
				$("#numeroA").val( this.value);				
			});
			
		});
	</s:else>

		
</script>
<s:include value="/jsp/include/footer.jsp" />