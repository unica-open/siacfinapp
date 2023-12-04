<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:hidden name="baseUrl" id="HIDDEN_baseUrl" />
				<s:hidden name="contiCausale" id="HIDDEN_contiCausale" />
			
				<s:include value="/jsp/include/actionMessagesErrors.jsp" />
				<h3><s:property value="campoTitoloPagina"/></h3>
				<h4>Inserimento prima nota integrata</h4>
				<h4><s:property value="intestazioneRichiesta" escapeHtml="false"/></h4>
				
				<div class="accordion" id="accordionMovimento">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle collapsed" data-toggle="collapse" data-parent="#accordionMovimento" href="#collapseMovimento">
								Movimento: consultazione abilitata da ricerca registro
							</a>
						</div>
						<div id="collapseMovimento" class="accordion-body collapse">
							<div class="accordion-inner">
								<fieldset class="form-horizontal">
								</fieldset>
							</div>
						</div>
					</div>
				</div>
				<s:form cssClass="form-horizontal" action="%{urlSalva}" novalidate="novalidate" id="formGestisciPrimaNotaintegrata">
					
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal" id="#fieldsetGestisciPNI">
								<h4 class="step-pane">Dati</h4>
								<div class="control-group">
									<label class="control-label">Descrizione *</label>
									<div class="controls">
										<s:textfield id="descrizionePrimaNota" name="primaNota.descrizione" cssClass="span9 required" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Data Registrazione *</label>
									<div class="controls">
										<s:textfield id="dataRegistrazionePrimaNota" name="primaNota.dataRegistrazione" cssClass="span2 datepicker required" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Causale *</label>
									<div class="controls">
										<s:select list="listaCausaleEP" id="uidCausaleEP" name="causaleEP.uid"
												cssClass="span6 selectCausaleEP" headerKey="" headerValue=""
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="%{aggiornamento}"/>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Note</label>
									<div class="controls">
										<input id="note" name="note" class="span9" type="text" value="" />
									</div>
								</div>
								<h4 class="nostep-pane">Da registrare(D-A): <span id="spanDaRegistrare"><s:property value="daRegistrare" /></span></h4>
								<h4 class="step-pane">Elenco scritture</h4>
								<table class="table table-hover tab_left" id="tabellaScritture">
									<thead>
										<tr>
											<th>Conto</th>
											<th>Descrizione</th>
											<th class="tab_Right">Dare</th>
											<th class="tab_Right">Avere</th>
											<th class="tab_Right span2">&nbsp;</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
									<tfoot>
										<tr>
											<th colspan="2">Totale</th>
											<th class="tab_Right" id="totaleDare"></th>
											<th class="tab_Right" id="totaleAvere"></th>
											<th class="tab_Right span2">&nbsp;</th>
										</tr>
									</tfoot>
								</table>
								<p>
									<button type="button" id="pulsanteInserimentoDati" class="btn btn-secondary">
										inserisci dati in elenco&nbsp;<!-- <i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteInserimentoDati"></i> -->
									</button>
								</p>
								<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/collapseDatiStruttura.jsp" />
							</fieldset>
						</div>
					</div>
					<p class="margin-large">
						<button id="redirezioneIndietro" type="button" class="btn">indietro</button>
						<s:if test = "%{primaNota == null || primaNota.uid == 0}">
							<s:submit value="salva" cssClass="btn btn-primary pull-right" />
						</s:if>
					</p>
				</s:form>
				<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaConto.jsp" />
				<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleEliminazioneConto.jsp" />
				<s:include value="/jsp/contabilitaGenerale/primaNotaIntegrata/include/modaleAggiornaConto.jsp" />
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}contabilitaGenerale/common.js"></script>
	<script type="text/javascript" src="${jspath}contabilitaGenerale/ricercaConto.js"></script>
	<script type="text/javascript" src="${jspath}contabilitaGenerale/primaNotaIntegrata/gestisci.movimento.reg.js"></script>

</body>
</html>