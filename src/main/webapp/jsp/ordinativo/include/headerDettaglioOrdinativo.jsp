<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


<dl class="dl-horizontal">
  <dt>Capitolo <a class="tooltip-test" title="Visualizza dettagli" href="#capitoloTabOrd" data-toggle="modal"><i class="icon-info-sign">&nbsp;<span class="nascosto">Visualizza dettagli</span></i></a></dt>
  <dd><s:property value="gestioneOrdinativoStep1Model.capitolo.anno"/>/<s:property value="gestioneOrdinativoStep1Model.capitolo.numCapitolo"/>/<s:property value="gestioneOrdinativoStep1Model.capitolo.articolo"/>/<s:property value="gestioneOrdinativoStep1Model.capitolo.ueb"/>  - <s:property value="gestioneOrdinativoStep1Model.capitolo.descrizione"/>  -  <s:property value="gestioneOrdinativoStep1Model.capitolo.codiceStrutturaAmministrativa"/> <s:property value="gestioneOrdinativoStep1Model.capitolo.descrizioneStrutturaAmministrativa"/> - tipo Finanziamento: <s:property value="gestioneOrdinativoStep1Model.capitolo.tipoFinanziamento"/></dd>              
  <dt>Provvedimento</dt>
  <dd><s:property value="gestioneOrdinativoStep1Model.provvedimento.annoProvvedimento"/>/<s:property value="gestioneOrdinativoStep1Model.provvedimento.numeroProvvedimento"/> - <s:property value="gestioneOrdinativoStep1Model.provvedimento.tipoProvvedimento"/> - <s:property value="gestioneOrdinativoStep1Model.provvedimento.oggetto"/> - <s:property value="gestioneOrdinativoStep1Model.provvedimento.codiceStrutturaAmministrativa"/> - <s:property value="gestioneOrdinativoStep1Model.provvedimento.strutturaAmministrativa"/> - Stato: <s:property value="gestioneOrdinativoStep1Model.provvedimento.stato"/></dd>
  <dt>Soggetto</dt>
 <%--  <s:if test="isEsistenzaSoggettoOClasse()"> --%>
  <dd><s:property value="gestioneOrdinativoStep1Model.soggetto.codCreditore"/> - <s:property value="gestioneOrdinativoStep1Model.soggetto.denominazione"/> - CF: <s:property value="gestioneOrdinativoStep1Model.soggetto.codfisc"/>  <s:if test="gestioneOrdinativoStep1Model.soggetto.piva != null">- PIVA : <s:property value="gestioneOrdinativoStep1Model.soggetto.piva"/></s:if> </dd><%--</s:if> <s:else>  <dd> Classe <s:property value="gestioneOrdinativoStep1Model.soggetto.classe"/></dd></s:else>  --%>       
<!--   <dt>Transazione elementare</dt> -->
<%--   <dd><s:property value="gestioneOrdinativoStep1Model.transazione"/></dd> --%>
</dl>