<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


<dl class="dl-horizontal">
  <dt>Capitolo <a class="tooltip-test" title="Visualizza dettagli" href="#capitoloTabOrdReintroito" data-toggle="modal"><i class="icon-info-sign">&nbsp;<span class="nascosto">Visualizza dettagli</span></i></a></dt>
  <dd><s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.anno"/>/<s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.numCapitolo"/>/<s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.articolo"/>/<s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.ueb"/>  - <s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.descrizione"/>  -  <s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.codiceStrutturaAmministrativa"/> <s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.descrizioneStrutturaAmministrativa"/> - tipo Finanziamento: <s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.tipoFinanziamento"/></dd>              
  <dt>Provvedimento</dt>
  <dd><s:property value="reintroitoOrdinativoStep1Model.provvedimentoOrdinativoDaReintroitare.annoProvvedimento"/>/<s:property value="reintroitoOrdinativoStep1Model.provvedimentoOrdinativoDaReintroitare.numeroProvvedimento"/> - <s:property value="reintroitoOrdinativoStep1Model.provvedimentoOrdinativoDaReintroitare.tipoProvvedimento"/> - <s:property value="reintroitoOrdinativoStep1Model.provvedimentoOrdinativoDaReintroitare.oggetto"/> - <s:property value="reintroitoOrdinativoStep1Model.provvedimentoOrdinativoDaReintroitare.codiceStrutturaAmministrativa"/> - <s:property value="reintroitoOrdinativoStep1Model.provvedimentoOrdinativoDaReintroitare.strutturaAmministrativa"/> - Stato: <s:property value="reintroitoOrdinativoStep1Model.provvedimentoOrdinativoDaReintroitare.stato"/></dd>
  <dt>Soggetto</dt>
  <dd><s:property value="reintroitoOrdinativoStep1Model.soggettoOrdinativoDaReintroitare.codCreditore"/> - <s:property value="reintroitoOrdinativoStep1Model.soggettoOrdinativoDaReintroitare.denominazione"/> - CF: <s:property value="reintroitoOrdinativoStep1Model.soggettoOrdinativoDaReintroitare.codfisc"/>  <s:if test="reintroitoOrdinativoStep1Model.soggettoOrdinativoDaReintroitare.piva != null">- PIVA : <s:property value="reintroitoOrdinativoStep1Model.soggettoOrdinativoDaReintroitare.piva"/></s:if> </dd>
  
  <dt>Tipo debito siope</dt>
  <dd><s:property value="reintroitoOrdinativoStep1Model.ordinativoDaReintroitare.siopeTipoDebito.descrizione"/>&nbsp;</dd>
  
  <s:if test ="isDocumentiCollegatiPresenti()">
  
  	<s:iterator value="reintroitoOrdinativoStep1Model.documentiCollegati" var="docIt" status="statDoc">
		<dt>Documento</dt>
	    <dd>N. <s:property value="#docIt.numeroDocumento"/> del <s:property value="#docIt.dataEmissioneDocumento"/></dd>
    </s:iterator>
  
    <dt>Importo</dt>
    <dd><s:property value="reintroitoOrdinativoStep1Model.importoDocumentiCollegati"/>&nbsp;</dd>

  </s:if>
  
  <dt>Modalit&agrave; di pagamento</dt>
  <dd><s:property value="reintroitoOrdinativoStep1Model.descrArrModPag"/>&nbsp;</dd>	
</dl>