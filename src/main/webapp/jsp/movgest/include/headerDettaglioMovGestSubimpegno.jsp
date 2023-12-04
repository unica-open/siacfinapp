<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<dl class="dl-horizontal">
  <dt>Capitolo <a class="tooltip-test" title="Visualizza dettagli" href="#capitoloTab" data-toggle="modal"><i class="icon-info-sign">&nbsp;<span class="nascosto">Visualizza dettagli</span></i></a></dt>
  <dd><s:property value="step1Model.capitolo.anno"/>/<s:property value="step1Model.capitolo.numCapitolo"/>/<s:property value="step1Model.capitolo.ueb"/>  - <s:property value="step1Model.capitolo.descrizione"/>  -  <s:property value="step1Model.capitolo.codiceStrutturaAmministrativa"/> - tipo Finanziamento: <s:property value="step1Model.capitolo.tipoFinanziamento"/></dd>              
  <dt>Provvedimento</dt>
  <dd><s:property value="step1ModelSubimpegno.provvedimento.annoProvvedimento"/>/<s:property value="step1ModelSubimpegno.provvedimento.numeroProvvedimento"/> - <s:property value="step1ModelSubimpegno.provvedimento.tipoProvvedimento"/> - <s:property value="step1ModelSubimpegno.provvedimento.oggetto"/> - <s:property value="step1ModelSubimpegno.provvedimento.CodiceStrutturaAmministrativa"/> - <s:property value="step1ModelSubimpegno.provvedimento.strutturaAmministrativa"/> -  Stato: <s:property value="step1ModelSubimpegno.provvedimento.stato"/></dd>
  <dt>Soggetto</dt>
  <s:if test="isEsistenzaSoggettoOClasse()">
  <dd><s:property value="step1ModelSubimpegno.soggetto.codCreditore"/> - <s:property value="step1ModelSubimpegno.soggetto.denominazione"/> - CF: <s:property value="step1ModelSubimpegno.soggetto.codfisc"/> - PIVA : <s:property value="step1ModelSubimpegno.soggetto.piva"/> </dd></s:if><s:else>  <dd> Classe <s:property value="step1ModelSubimpegno.soggetto.classe"/></dd> </s:else>      
</dl>