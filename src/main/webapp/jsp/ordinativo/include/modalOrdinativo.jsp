<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

<div id="capitoloTabOrd" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="capitoloTabLabel" aria-hidden="true">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
    <h3 id="myModalLabel2">Dettagli del capitolo</h3>
  </div>
  <div class="modal-body">
    <dl class="dl-horizontal">
      <dt>Numero</dt>
      <dd><s:property value="gestioneOrdinativoStep1Model.capitolo.anno"/> / <s:property value="gestioneOrdinativoStep1Model.capitolo.numCapitolo"/> / <s:property value="gestioneOrdinativoStep1Model.capitolo.articolo"/> / <s:property value="gestioneOrdinativoStep1Model.capitolo.ueb"/> - <s:property value="gestioneOrdinativoStep1Model.capitolo.descrizione" /> - <s:property value="gestioneOrdinativoStep1Model.capitolo.codiceStrutturaAmministrativa"/> - <s:property value="gestioneOrdinativoStep1Model.capitolo.descrizioneStrutturaAmministrativa" />&nbsp;</dd>
      <dt>Tipo finanziamento</dt>
      <dd><s:property value="gestioneOrdinativoStep1Model.capitolo.tipoFinanziamento" />&nbsp;</dd>
      <dt>Piano dei conti finanziario</dt>
      <dd><s:property value="gestioneOrdinativoStep1Model.capitolo.descrizionePdcFinanziario" default=" "/>&nbsp;</dd>
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
	        <td><s:property value="getText('struts.money.format', {gestioneOrdinativoStep1Model.capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno1})" /></td>
	        <td><s:property value="getText('struts.money.format', {gestioneOrdinativoStep1Model.capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno2})" /></td>
	        <td><s:property value="getText('struts.money.format', {gestioneOrdinativoStep1Model.capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno3})" /></td>
      	</s:if>
      	<s:else>
      		<td><s:property value="getText('struts.money.format', {gestioneOrdinativoStep1Model.capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno1})" /></td>
	        <td><s:property value="getText('struts.money.format', {gestioneOrdinativoStep1Model.capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno2})" /></td>
	        <td><s:property value="getText('struts.money.format', {gestioneOrdinativoStep1Model.capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno3})" /></td>
        </s:else>
      </tr>
    </table>
  </div>
  </div>      
  
  
  <div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
	   <!--<div class="modal-header">
	   <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
	   <h3 id="myModalLabel">Attenzione</h3>
	   </div> -->
	   <div class="modal-body">
	     <div class="alert alert-error">
	       <button type="button" class="close" data-dismiss="alert">&times;</button>
	       <p><strong>Attenzione!</strong></p>
<%-- 	       <p><strong>Elmento selezionato:.....</strong></p> --%>
	       <p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
	     </div>
	   </div>
	   <div class="modal-footer">
	     <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
	     <s:submit id="submitBtnEliminaQuota" name="btnEconomia" value="si, prosegui" cssClass="btn btn-primary"  method="eliminaQuotaOrdinativo"/>
	     
	   </div>
 </div>
  
  <div id="msgEliminaProvvisorio" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
	   <!--<div class="modal-header">
	   <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
	   <h3 id="myModalLabel">Attenzione</h3>
	   </div> -->
	   <div class="modal-body">
	     <div class="alert alert-error">
	       <button type="button" class="close" data-dismiss="alert">&times;</button>
	       <p><strong>Attenzione!</strong></p>
<%-- 	       <p><strong>Elmento selezionato:.....</strong></p> --%>
	       <p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
	     </div>
	   </div>
	   <div class="modal-footer">
	     <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
	     <s:submit id="submitBtnEliminaProvvisorio" name="btnEconomia" value="si, prosegui" cssClass="btn btn-primary"  method="eliminaProvvisorio"/>
	     
	   </div>
 </div>
 
 
 
 <!--modale consultaOrd2 -->  
    <div id="consultaOrd2" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="consultaOrd2Label" aria-hidden="true">
    
    	<div id="divDettaglioConsultaQuotaOrdinativo"></div>  
    	
	</div>
<!--/modale  -->	


 <!--modale aggOrdinativo2 -->  
    <div id="aggOrdinativo2" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="aggOrdinativo2Label" aria-hidden="true">
    
    	<div id="divDettaglioAggiornaQuotaOrdinativo"></div>  
    	
	</div>
<!--/modale  -->	


<!--modale aggProvvisorioCassa-->  
    <div id="aggProvvisorioCassa" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="aggOrdinativo2Label" aria-hidden="true">
    
    	<div id="divDettaglioAggiornaProvvisorioCassa"></div>  
    	
	</div>
<!--/modale  -->	

                       <!-- modale controllo prosegui Quota INCASSO-->
            <div id="msgControlloProseguiOrdInc" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgDatipersi" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-warning">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                   <s:iterator value="actionWarnings">
		     	  		<s:property escapeHtml="false"/><br>
		   		   </s:iterator>
                  <p></p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:if test="gestioneOrdinativoStep2Model.checkProseguiQuoteIncasso">
                	<s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, prosegui" cssClass="btn btn-primary" method="forzaInserisciQuotaAccertamento"/>
                </s:if>
                <s:elseif test="gestioneOrdinativoStep2Model.checkAggiornaQuoteIncasso">
                	<s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, prosegui" cssClass="btn btn-primary" method="forzaAggiornaQuotaAccertamento"/>
                </s:elseif>
              </div>
            </div>  
            <!-- /modale  controllo prosegui Quota INCASSO-->  
  
<!-- modale dettaglio quote ordinativi-->
<div id="ModQuote" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="ModQuoteLabel" aria-hidden="true">
	<div id="divModQuote"></div>
</div>
<!-- /modale dettaglio quote ordinativi-->