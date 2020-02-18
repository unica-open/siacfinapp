<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="r" uri="http://www.csi.it/taglibs/remincl-1.0"%>
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
				<s:include value="/jsp/include/actionMessagesErrors.jsp" />
				<s:form id="consultaMutuo" method="post" cssClass="form-horizontal">

<!-- ************************************************************************************* -->
					<h3>Mutuo <s:property value="mutuo.codiceMutuo" /></h3>
					
					<ul class="nav nav-tabs">
					<s:if test="activeTab == 'mutuo'">
						<li class="active"><a href="#mutuo" data-toggle="tab">Mutuo</a></li>
						<li><a href="#voci" data-toggle="tab">Voci di mutuo</a></li>
					</s:if>
					<s:else>
						<li><a href="#mutuo" data-toggle="tab">Mutuo</a></li>
						<li class="active"><a href="#voci" data-toggle="tab">Voci di mutuo</a></li>
					</s:else>
					</ul>
					
					<div class="tab-content">
<!-- ************************************************************************************* -->
					<s:if test="activeTab == 'mutuo'">
						<div class="tab-pane active" id="mutuo">
					</s:if>
					<s:else>
						<div class="tab-pane" id="mutuo">
					</s:else>
							<h4>Utente Inserimento; <s:property value="mutuo.loginCreazione" />  Data inserimento: <s:property value="%{mutuo.dataCreazione}" /> - Utente ultima modifica: <s:property value="mutuo.loginModifica" /> <span class="alLeft">Data ultima modifica: <s:property value="%{mutuo.dataModifica}" /></span></h4>
							<div class="boxOrSpan2">
								<div class="boxOrInLeft">
									<p>Provvedimento</p>
									<ul class="htmlelt">
										<li>
											<dfn>Anno</dfn> 
											<dl><s:property value="mutuo.attoAmministrativoMutuo.anno" /></dl>
										</li>
										<li>
											<dfn>Numero</dfn> 
											<dl><s:property value="mutuo.attoAmministrativoMutuo.numero" /> </dl>
										</li>
										<li>
											<dfn>Tipo</dfn> 
											<dl><s:property value="mutuo.attoAmministrativoMutuo.tipoAtto.descrizione" /></dl>
										</li>
									</ul>
									<p>Istituto mutuante</p>
									<ul class="htmlelt">
										<li>
											<dfn>Codice</dfn> 
											<dl><s:property value="mutuo.soggettoMutuo.codiceSoggetto" /></dl>
										</li>
										<li>
											<dfn>Codice Fiscale</dfn> 
											<dl><s:property value="mutuo.soggettoMutuo.codiceFiscale" /></dl>
										</li>
										<li>
											<dfn>Partita IVA</dfn> 
											<dl><s:property value="mutuo.soggettoMutuo.partitaIva" /></dl>
										</li>
										<li>
											<dfn>Denominazione</dfn> 
											<dl><s:property value="mutuo.soggettoMutuo.denominazione" /></dl>
										</li>
										<li>
											<dfn>Classificatore</dfn> 
											<dl><s:property value="mutuo.soggettoMutuo.elencoClassString" /></dl>
										</li>
									</ul>					
								</div>
			
								<div class="boxOrInRight">
									<p>Dati mutuo</p>
									
									<ul class="htmlelt">
										<li>
											<dfn>Tipo Mutuo</dfn> 
											<dl><s:property value="mutuo.descrizioneTipoMutuo" /></dl>
										</li>
										<li>
											<dfn>Stato Mutuo</dfn> 
											<dl><s:property value="mutuo.statoOperativoMutuo" /></dl>
										</li>
										<li>
											<dfn>Numero Registrazione</dfn> 
											<dl><s:property value="mutuo.numeroRegistrazioneMutuo" /></dl>
										</li>
										<li>
											<dfn>Descrizione</dfn> 
											<dl><s:property value="mutuo.descrizioneMutuo" /></dl>
										</li>
										<li>
											<dfn>Importo</dfn> 
											<dl><s:property value="getText('struts.money.format', {mutuo.importoAttualeMutuo})" /></dl>
										</li>
										<li>
											<dfn>Durata (anni)</dfn> 
											<dl><s:property value="mutuo.durataMutuo" /></dl>
										</li>
										<li>
											<dfn>Data inizio</dfn> 
											<dl><s:property value="%{mutuo.dataInizioMutuo}" /></dl>
										</li>
										<li>
											<dfn>Data fine</dfn> 
											<dl><s:property value="%{mutuo.dataFineMutuo}" /></dl>
										</li>
										<li>
											<dfn>Note</dfn> 
											<dl><s:property value="mutuo.noteMutuo" /></dl>
										</li>
									</ul>
			
								</div>				
							
							</div>
						</div>
<!-- ************************************************************************************* -->
					<s:if test="activeTab == 'voci'">
						<div class="tab-pane active" id="voci">
					</s:if>
					<s:else>
						<div class="tab-pane" id="voci">
					</s:else>
							<h4>
								Importo attuale: <s:property value="getText('struts.money.format', {mutuo.importoAttualeMutuo})" /> - 
								Totale impegnato: <s:property value="getText('struts.money.format', {mutuo.totaleVociMutuo.importoAttualeVoceMutuo})" /> -
								Disponibile mutuo:  <s:property value="getText('struts.money.format', {mutuo.disponibileMutuo})" />
							</h4>
							<h4>Codice Ist. Mutuante: <s:property value="mutuo.soggettoMutuo.codiceSoggetto" /> - <s:property value="mutuo.soggettoMutuo.denominazione" /></h4>

							<display:table htmlId="listaVoci" name="mutuo.listaVociMutuo" class="table table-hover tab_centered" summary="riepilogo" uid="voceID" pagesize="5"
							 	requestURI="consultaMutuo.do"
							>

								<display:column title="Capitolo / Articolo" headerClass="tabRow sBold" class="tabRow tab_Right">
									<s:property value="%{#attr.voceID.impegno.capitoloUscitaGestione.annoCapitolo}" /> / <s:property value="%{#attr.voceID.impegno.capitoloUscitaGestione.numeroCapitolo}" /> / <s:property value="%{#attr.voceID.impegno.capitoloUscitaGestione.numeroArticolo}" /> / <s:property value="%{#attr.voceID.impegno.capitoloUscitaGestione.numeroUEB}" />
								</display:column>
								<display:column title="Impegno - Sub" headerClass="tabRow sBold" class="tabRow tab_Right">
									<s:property value="%{#attr.voceID.impegno.annoMovimento}" /> / <s:property value="%{#attr.voceID.impegno.numero.intValue()}" />
								<s:if test="%{(#attr.voceID.impegno.elencoSubImpegni != null) && (#attr.voceID.impegno.elencoSubImpegni.size > 0) }">
									- <s:property value="%{#attr.voceID.impegno.elencoSubImpegni[0].numero.intValue()}" />
								</s:if>	
								</display:column>
								<display:column title="Provvedimento" headerClass="tabRow sBold" class="tabRow tab_Right">
									<s:property value="%{#attr.voceID.impegno.attoAmministrativo.anno}" /> / <s:property value="%{#attr.voceID.impegno.attoAmministrativo.numero}" />
								</display:column>
								<display:column title="Importo" headerClass="tabRow sBold" class="tabRow tab_Right" >
								<s:if test="%{#attr.voceID.subImpegno != null}">
									<s:property value="getText('struts.money.format', {#attr.voceID.subImpegno.importoAttuale})" default="-" />
								</s:if>
								<s:else>
									<s:property value="getText('struts.money.format', {#attr.voceID.impegno.importoAttuale})" default="-" />
								</s:else>
								</display:column>
								<display:column title="Disponibile modifiche" headerClass="tabRow sBold borderRight" class="borderRight">
									<s:property value="getText('struts.money.format', {#attr.voceID.importoDisponibileModificheImpegno})" default="-" />
								</display:column>
								
								<display:column title="Tipo voce" property="origineVoceMutuo" headerClass="tabRow sBold" class="tabRow tab_Right" />
								<display:column title="Importo" headerClass="tabRow sBold" class="tabRow tab_Right">
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

								<display:footer> 
									<tr> 
										<th>Totali</th>
										<th>&nbsp;</th>
										<th>&nbsp;</th>
										<th class="tab_Right" style="text-align:center"><s:property value="getText('struts.money.format', {mutuo.totaleVociMutuo.impegno.importoAttuale})" default="-" /></th>
										<th class="tab_Right borderRight" style="text-align:center"><s:property value="getText('struts.money.format', {mutuo.totaleVociMutuo.importoDisponibileModificheImpegno})" default="-" /></th>
										
										<th>&nbsp;</th>
										<th class="tab_Right" style="text-align:center"><s:property value="getText('struts.money.format', {mutuo.totaleVociMutuo.importoAttualeVoceMutuo})" default="-" /></th>
										<th class="tab_Right" style="text-align:center"><s:property value="getText('struts.money.format', {mutuo.totaleVociMutuo.importoVariazioniEconomia})" default="-" /></th>
										<th class="tab_Right" style="text-align:center"><s:property value="getText('struts.money.format', {mutuo.totaleVociMutuo.importoVariazioniRiduzione})" default="-" /></th>
										<th class="tab_Right" style="text-align:center"><s:property value="getText('struts.money.format', {mutuo.totaleVociMutuo.importoVariazioniStorno})" default="-" /></th>
										<th class="tab_Right" style="text-align:center"><s:property value="getText('struts.money.format', {mutuo.totaleVociMutuo.importoVariazioniResiduo})" default="-" /></th>
									<tr> 
								</display:footer>								
							</display:table>

						</div>
					</div>
					
<!-- ************************************************************************************* -->

					<p><s:include value="/jsp/include/indietro.jsp" /></p>
					
				</s:form>
			</div>
		</div>
	</div>
	
<script type="text/javascript">

	$(document).ready(function() {
		$('#listaVoci > thead').prepend('<tr><th colspan="5" class="borderRight">Impegno</th><th colspan="6">Voce Mutuo</th><tr>');
	});
	
</script>  

<s:include value="/jsp/include/footer.jsp" />