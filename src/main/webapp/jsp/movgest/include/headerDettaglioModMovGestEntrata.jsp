<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<dl class="dl-horizontal">
  <dt>Capitolo <a class="tooltip-test" title="Visualizza dettagli" href="#capitoloTab" data-toggle="modal"><i class="icon-info-sign">&nbsp;<span class="nascosto">Visualizza dettagli</span></i></a></dt>
  <dd><s:property value="accertamentoInAggiornamento.capitoloEntrataGestione.annoCapitolo"/>/<s:property value="accertamentoInAggiornamento.capitoloEntrataGestione.numeroCapitolo"/>/<s:property value="accertamentoInAggiornamento.capitoloEntrataGestione.numeroUEB"/>  - <s:property value="accertamentoInAggiornamento.capitoloEntrataGestione.descrizione"/>  -  <s:property value="impegnoInAggiornamento.capitoloEntrataGestione.strutturaAmministrativaContabile.codice"/> - tipo Finanziamento: <s:property value="accertamentoInAggiornamento.capitoloEntrataGestione.tipoFinanziamento.codice"/></dd>              
  <dt>Provvedimento</dt>
  <dd><s:property value="step1Model.provvedimento.annoProvvedimento"/>/<s:property value="step1Model.provvedimento.numeroProvvedimento"/> - <s:property value="step1Model.provvedimento.tipoProvvedimento"/> - <s:property value="step1Model.provvedimento.oggetto"/> - <s:property value="step1Model.provvedimento.CodiceStrutturaAmministrativa"/> - <s:property value="step1Model.provvedimento.strutturaAmministrativa"/> -  Stato: <s:property value="step1Model.provvedimento.stato"/></dd>
  <dt>Soggetto</dt>
  <s:if test="isEsistenzaSoggettoOClasse()">
  <dd><s:property value="step1Model.soggetto.codCreditore"/> - <s:property value="step1Model.soggetto.denominazione"/> - CF: <s:property value="step1Model.soggetto.codfisc"/> - PIVA : <s:property value="step1Model.soggetto.piva"/></dd></s:if><s:else>  <dd> Classe <s:property value="step1Model.soggetto.classe"/></dd></s:else>        
</dl>