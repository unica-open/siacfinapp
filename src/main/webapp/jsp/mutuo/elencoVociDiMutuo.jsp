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
</head>
<body>
	<s:include value="/jsp/include/header.jsp" />
	<hr />
	<div class="container-fluid-banner">
		<a name="A-contenuti" title="A-contenuti"></a>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form id="elencoVociDiMutuo" action="elencoVociDiMutuo.do" method="post">
					<s:include value="/jsp/include/actionMessagesErrors.jsp" />
					<h3>Mutuo <s:property value="mutuoSelezionato.codiceMutuo" /> - <s:property value="mutuoSelezionato.soggettoMutuo.codiceSoggetto" /> - <s:property value="mutuoSelezionato.soggettoMutuo.denominazione" /></h3>
					<h4>
						Importo attuale: <s:property value="getText('struts.money.format', {mutuoSelezionato.importoAttualeMutuo})" /> - 
						 <span class="alLeft">Totale
							impegnato: <s:property value="getText('struts.money.format', {mutuoSelezionato.totaleVociMutuo.importoAttualeVoceMutuo})" />
						 </span> - 
						 <span class="alLeft">Disponibile mutuo:
							<s:property value="getText('struts.money.format', {mutuoSelezionato.disponibileMutuo})" />
						  </span>
					</h4>
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<h4>Elenco voci di mutuo</h4>
							
							<display:table htmlId="listaVoci" name="mutuoSelezionato.listaVociMutuo" class="table table-hover tab_centered" 
							summary="riepilogo" uid="voceID" varTotals="totali" pagesize="5"
							 	requestURI="elencoVociDiMutuo.do" keepStatus="${status}" clearStatus="${status}">

								<display:column title="Capitolo / Articolo" headerClass="tabRow sBold" class="tabRow tab_Right">
									<s:property value="%{#attr.voceID.impegno.capitoloUscitaGestione.annoCapitolo}" /> / <s:property value="%{#attr.voceID.impegno.capitoloUscitaGestione.numeroCapitolo}" /> / <s:property value="%{#attr.voceID.impegno.capitoloUscitaGestione.numeroArticolo}" /> / <s:property value="%{#attr.voceID.impegno.capitoloUscitaGestione.numeroUEB}" />
								</display:column>
								<display:column title="Impegno - Sub" headerClass="tabRow sBold" class="tabRow tab_Right">
									<s:property value="%{#attr.voceID.impegno.annoMovimento}" /> / <s:property value="%{#attr.voceID.impegno.numero.intValue()}" />
								<s:if test="%{#attr.voceID.impegno.elencoSubImpegni != null}">
									- <s:property value="%{#attr.voceID.impegno.elencoSubImpegni.get(0).numero.intValue()}" />
								</s:if>	
								</display:column>
								<display:column title="Provvedimento" headerClass="tabRow sBold" class="tabRow tab_Right">
									<s:property value="%{#attr.voceID.impegno.attoAmministrativo.anno}" /> / <s:property value="%{#attr.voceID.impegno.attoAmministrativo.numero}" />
								</display:column>
								
								<display:column title="Importo"  headerClass="tabRow sBold" class="tabRow tab_Right" total="true">
									<s:if test="%{#attr.voceID.impegno.elencoSubImpegni != null}">
										<s:property value="getText('struts.money.format', {#attr.voceID.impegno.elencoSubImpegni.get(0).importoAttuale})" default="-" />
									</s:if>
									<s:else>
										<s:property value="getText('struts.money.format', {#attr.voceID.impegno.importoAttuale})" default="-" />
									</s:else>
								</display:column>
								<display:column title="Disponibile modifiche" headerClass="tabRow sBold borderRight" class="borderRight">
										<s:property value="getText('struts.money.format', {#attr.voceID.importoDisponibileModificheImpegno})" default="-" />
								</display:column>
								
								<display:column title="Tipo voce" property="origineVoceMutuo" headerClass="tabRow sBold" class="tabRow tab_Right" />
								<display:column title="Importo"  headerClass="tabRow sBold" class="tabRow tab_Right">
									<s:property value="getText('struts.money.format', {#attr.voceID.importoAttualeVoceMutuo})" default="-" />
								</display:column>
								<display:column title="Economia" headerClass="tabRow sBold" class="tabRow tab_Right">
									<s:property value="getText('struts.money.format', {#attr.voceID.importoVariazioniEconomia})" default="-" />
								</display:column>
								<display:column title="Riduzione" headerClass="tabRow sBold" class="tabRow tab_Right">
									<s:property value="getText('struts.money.format', {#attr.voceID.importoVariazioniRiduzione})" default="-" />
								</display:column>
								<display:column title="Storno" headerClass="tabRow sBold" class="tabRow tab_Right">
									<s:property value="getText('struts.money.format', {#attr.voceID.importoVariazioniStorno})" default="-" />
								</display:column>
								<display:column title="Residuo" headerClass="tabRow sBold" class="tabRow tab_Right">
									<s:property value="getText('struts.money.format', {#attr.voceID.importoVariazioniResiduo})" default="-" />
								</display:column>
								<display:column title="" headerClass="tabRow sBold" class="tabRow tab_Right" >
									<div class="btn-group">
												<button class="btn dropdown-toggle" id="act_btn2" data-toggle="dropdown">Azioni <span class="caret"></span>
												</button>
												<ul class="dropdown-menu pull-right">
												
												    <s:if test="isAbilitato()">
													<li><a id="linkEconomia_<s:property value="%{#attr.voceID.numeroVoceMutuo}"/>" href="#ModEconomia" class="linkEconomiaVoceMutuo" data-toggle="modal" >inserisci
															economia</a></li>
													</s:if>		
													<s:if test="isAbilitato()">
													<li><a id="linkRiduzione_<s:property value="%{#attr.voceID.numeroVoceMutuo}"/>" href="#ModRiduzione" class="linkRiduzioneVoceMutuo" data-toggle="modal">riduzione
															voce</a></li>
													</s:if>	
													<s:if test="isAbilitato()">	
													
														<s:url id="stornoUrl" action="stornoVoceMutuoStep1.do" escapeAmp="false">
											        		<s:param name="numeroVoceMutuo" value="%{#attr.voceID.numeroVoceMutuo}" />
								                    	</s:url>
														<li><a href="<s:property value="stornoUrl"/>"">storno voce</a></li>
														
													</s:if>
													<s:if test="isAbilitato()">		
													<li><a id="linkRettifica_<s:property value="%{#attr.voceID.numeroVoceMutuo}"/>" href="#ModRettVoci"  class="linkRettificaVoceMutuo"data-toggle="modal">rettifica
															voce</a></li>
													</s:if>		
													<s:if test="isAbilitato()">		
													<li> <a id="linkElimina_<s:property value="%{#attr.voceID.numeroVoceMutuo}"/>_<s:property value="%{#attr.voceID.numeroVoceMutuo}"/>" href="#msgEliminaVoceMutuo" 
													      data-toggle="modal" class="linkEliminaVoceMutuo" >elimina voce</a>
													</li>
													</s:if>
												</ul>
											</div>
								</display:column>

								<display:footer> 
									<tr> 
										<th class="tab_Right">Totali</th>
										<th class="tab_Right">&nbsp;</th>
										<th class="tab_Right">&nbsp;</th>
<%-- 										<th class="tab_Right"><s:property value="getText('struts.money.format', {#attr.totali.column4})" /></th> --%>
<!-- 										<th class="borderRight">&nbsp;</th> -->
										<th class="tab_Right"  style="text-align: center;" ><s:property value="getText('struts.money.format', {mutuoSelezionato.totaleVociMutuo.impegno.importoAttuale})" default="-" /></th>
										<th class="tab_Right borderRight"  style="text-align: center;" ><s:property value="getText('struts.money.format', {mutuoSelezionato.totaleVociMutuo.importoDisponibileModificheImpegno})" default="-" /></th>
										
										
										<th class="tab_Right">&nbsp; &nbsp;</th>
										<th class="tab_Right" style="text-align: center;" ><s:property value="getText('struts.money.format', {mutuoSelezionato.totaleVociMutuo.importoAttualeVoceMutuo})" /></th>
										<th class="tab_Right" style="text-align: center;" ><s:property value="getText('struts.money.format', {mutuoSelezionato.totaleVociMutuo.importoVariazioniEconomia})" default="-" /></th>
										<th class="tab_Right" style="text-align: center;" ><s:property value="getText('struts.money.format', {mutuoSelezionato.totaleVociMutuo.importoVariazioniRiduzione})" default="-" /></th>
										<th class="tab_Right" style="text-align: center;" ><s:property value="getText('struts.money.format', {mutuoSelezionato.totaleVociMutuo.importoVariazioniStorno})" default="-" /></th>
										<th class="tab_Right" style="text-align: center;" ><s:property value="getText('struts.money.format', {mutuoSelezionato.totaleVociMutuo.importoVariazioniResiduo})" default="-" /></th>
										<th class="tab_Right">&nbsp;</th>
									<tr> 
								</display:footer>								
							</display:table>
							
							<s:hidden id="numeroVoceMutuoDaPassare" name="numeroVoceMutuoEliminato"/>
							<s:hidden id="numeroVoceMutuoSelezionato" name="numeroVoceMutuoSelezionato"/>
							
							<p>
								<s:submit name="idInserisciVoceDiMutuo" value="inserisci nuove voci" method="inserisciVoceDiMutuo" cssClass="btn bnt-secondary" disabled="disabilitaPerDisponibilita()"/>
							</p>
						</div>
					</div>
					<s:include value="/jsp/include/modalMutuo.jsp" />
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
<script type="text/javascript">

	$(document).ready(function() {
		$('#listaVoci > thead').prepend('<tr><th colspan="5" class="borderRight">Impegno</th><th colspan="7">Voce Mutuo</th><tr>');		
	});
		
	$(".linkEliminaVoceMutuo").click(function() {
		var supportId = $(this).attr("id").split("_");
		
		if (supportId != null && supportId.length > 0) {
			$("#numeroVoceMutuoDaEliminare").val(supportId[1]);
			$("#numeroVoceMutuoDaPassare").val(supportId[2]);
			
		}
	});
	$(".linkEconomiaVoceMutuo").click(function() {
		var supportId = $(this).attr("id").split("_");
		$('#importoEconomia').val('');
		if (supportId != null && supportId.length > 0) {
			$("#numeroVoceMutuoSelezionato").val(supportId[1]);
			
		}
	});
	$(".linkRiduzioneVoceMutuo").click(function() {
		var supportId = $(this).attr("id").split("_");
		$('#importoRiduzione').val('');
		if (supportId != null && supportId.length > 0) {
			$("#numeroVoceMutuoSelezionato").val(supportId[1]);
			
		}
	});
	$(".linkRettificaVoceMutuo").click(function() {
		var supportId = $(this).attr("id").split("_");
		$('#importoRettifica').val('');
		if (supportId != null && supportId.length > 0) {
			$("#numeroVoceMutuoSelezionato").val(supportId[1]);
			
		}
	});
	
	$("#submitBtnEconomia").click(function(){
		var importoDigit = $("#importoEconomia").val();
		impostaDatiPreInserimentoModifica(importoDigit);		
	});
	
	$("#submitBtnRiduzione").click(function(){
		var importoDigit = $("#importoRiduzione").val();
		impostaDatiPreInserimentoModifica(importoDigit);
	});
	
	$("#submitBtnRettifica").click(function(){
		var importoDigit = $("#importoRettifica").val();
		impostaDatiPreInserimentoModifica(importoDigit);
	});
	
	 function impostaDatiPreInserimentoModifica(importoDigit){
		 
		 var numVoceMutuoSel = $("#numeroVoceMutuoSelezionato").val();
			
			//Carico i dati in tabella "Modalita' di pagamento"		
			$.ajax({
				url: '<s:url method="preInserisciModifica"></s:url>',
				type: "GET",
				data: $(".hiddenGestoreToggle").serialize() + "&numVoceMutuoSel=" + numVoceMutuoSel + "&importoDigit=" + importoDigit, 
			    success: function(data)  {
			    	//$("#refreshDescrizioneProgetto").html(data);
				}
			});	
		 
	 }
	
	
</script>  
<s:include value="/jsp/include/footer.jsp" />