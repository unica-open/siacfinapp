<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<!--modale impegno -->
	<div id="guidaImpegno" class="modal hide fade" tabindex="-1">		
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" data-dismiss="modal">&times;</button>
				<h4 class="nostep-pane">Seleziona impegno</h4>
			</div>
			<div id="refreshPopupModal">			
				<s:include value="/jsp/carta/include/modalImpegnoContentCarta.jsp" />				
			</div>
	</div>
<!-- end modal -->


<!-- modal capitolo al click della I -->
<div id="capitoloTab" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="capitoloTabLabel" aria-hidden="true">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
    <h3 id="myModalLabel2">Dettagli del capitolo</h3>
  </div>
  <div class="modal-body">
    <dl class="dl-horizontal">
      <dt>Numero</dt>
      <dd><s:property value="capitolo.numCapitolo"/> / <s:property value="capitolo.articolo"/> / <s:property value="capitolo.ueb"/> - <s:property value="capitolo.descrizione" /> - <s:property value="capitolo.codiceStrutturaAmministrativa"/> - <s:property value="capitolo.descrizioneStrutturaAmministrativa" />&nbsp;</dd>
      <dt>Tipo finanziamento</dt>
      <dd><s:property value="capitolo.tipoFinanziamento" />&nbsp;</dd>
      <dt>Piano dei conti finanziario</dt>
      <dd><s:property value="capitolo.descrizionePdcFinanziario" default=" "/>&nbsp;</dd>
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
        
	        <td><s:property value="getText('struts.money.format', {capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno1})" /></td>
	        <td><s:property value="getText('struts.money.format', {capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno2})" /></td>
	        <td><s:property value="getText('struts.money.format', {capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno3})" /></td>
      	
<%--       		<td><s:property value="getText('struts.money.format', {capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno1})" /></td> --%>
<%-- 	        <td><s:property value="getText('struts.money.format', {capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno2})" /></td> --%>
<%-- 	        <td><s:property value="getText('struts.money.format', {capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno3})" /></td> --%>
       
      </tr>
    </table>
  </div>
  </div>      
  