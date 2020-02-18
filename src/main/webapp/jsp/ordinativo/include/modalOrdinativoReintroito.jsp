<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

<div id="capitoloTabOrdReintroito" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="capitoloTabLabel" aria-hidden="true">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
    <h3 id="myModalLabel2">Dettagli del capitolo</h3>
  </div>
  <div class="modal-body">
    <dl class="dl-horizontal">
      <dt>Numero</dt>
      <dd><s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.anno"/> / <s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.numCapitolo"/> / <s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.articolo"/> / <s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.ueb"/> - <s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.descrizione" /> - <s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.codiceStrutturaAmministrativa"/> - <s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.descrizioneStrutturaAmministrativa" />&nbsp;</dd>
      <dt>Tipo finanziamento</dt>
      <dd><s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.tipoFinanziamento" />&nbsp;</dd>
      <dt>Piano dei conti finanziario</dt>
      <dd><s:property value="reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.descrizionePdcFinanziario" default=" "/>&nbsp;</dd>
    </dl>
    <table class="table table-hover table-bordered">
      <tr>
        <th>&nbsp;</th>
        <s:iterator value="datoPerVisualizza.importiCapitolo">
			<th scope="col" class="text-center"><s:property value="annoCompetenza" /></th>
		</s:iterator>
      </tr>
      <tr>
        <th>Stanziamento</th>
        <s:iterator value="datoPerVisualizza.importiCapitolo">
			<td><s:property value="getText('struts.money.format', {competenza})" /></td>
		</s:iterator>       
      </tr>
      <tr>
         <th>Disponibile</th>
         <s:if test="oggettoDaPopolarePagamento()">
	        <td><s:property value="getText('struts.money.format', {reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.importiCapitoloUG[0].disponibilitaImpegnareAnno1})" /></td>
	        <td><s:property value="getText('struts.money.format', {reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.importiCapitoloUG[0].disponibilitaImpegnareAnno2})" /></td>
	        <td><s:property value="getText('struts.money.format', {reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.importiCapitoloUG[0].disponibilitaImpegnareAnno3})" /></td>
      	</s:if>
      	<s:else>
      		<td><s:property value="getText('struts.money.format', {reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.importiCapitoloEG[0].disponibilitaAccertareAnno1})" /></td>
	        <td><s:property value="getText('struts.money.format', {reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.importiCapitoloEG[0].disponibilitaAccertareAnno2})" /></td>
	        <td><s:property value="getText('struts.money.format', {reintroitoOrdinativoStep1Model.capitoloOrdinativoDaReintroitare.importiCapitoloEG[0].disponibilitaAccertareAnno3})" /></td>
        </s:else>
      </tr>
    </table>
  </div>
  </div>      
