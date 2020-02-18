<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!-- Modal elimina voce di mutuo -->
        <div id="msgEliminaVoceMutuo" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
<%--                   <p><strong>Elemento selezionato:<s:textfield id="numeroVoceMutuoDaEliminare" name="numeroVoceMutuoDaEliminare" disabled="true"/> </strong></p>
 --%>                  <p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary"  action="eliminaVoceMutuo"/>
              </div>
            </div>  
<!--/modale elimina -->

<!-- Modal ModEconomia --> 
<div id="ModEconomia" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="ModEconomiaLabel" aria-hidden="true">
    <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true"data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4>Economia - Importo attuale <s:property value="getText('struts.money.format', {mutuoSelezionato.importoAttualeMutuo})"/></h4>										             
	</div>
	
	<div class="modal-body">
		<div class="control-group">                   
			<label for="impoEco" class="control-label">Riduzione per economia</label>
			<div class="controls">
			<s:textfield id="importoEconomia" name="importoEconomia" onkeypress="return checkItNumbersCommaAndDotOnly(event)" cssClass="span2 soloNumeri decimale"/>
		
			</div>
		</div>  
    </div>  
	
	<div class="modal-footer">
	   <s:submit id="submitBtnEconomia" name="btnEconomia" value="conferma" cssClass="btn btn-primary" method="inserisciEconomiaVoceMutuo"/>
	   
	</div>
</div>
<!--end Modal -->
<!-- Modal ModRiduzione --> 
<div id="ModRiduzione" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="ModRiduzioneLabel" aria-hidden="true">
    <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true"data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4>Riduzione - Importo attuale <s:property value="getText('struts.money.format', {mutuoSelezionato.importoAttualeMutuo})"/></h4>											             
	</div>
	
	<div class="modal-body">
    	<div class="control-group">
			<label for="impoVoce" class="control-label">Importo riduzione</label>
			<div class="controls">
				<s:textfield id="importoRiduzione" name="importoRiduzione" onkeypress="return checkItNumbersCommaAndDotOnly(event)" cssClass="span2 soloNumeri decimale"/>
			</div>
		</div>

    </div>  
	
	<div class="modal-footer">
		  <s:submit id="submitBtnRiduzione" name="btnRiduzione" value="conferma" cssClass="btn btn-primary"  method="inserisciRiduzioneVoceMutuo"/>
	</div>
</div>
<!--end Modal -->
<!-- Modal ModRettVoci --> 
<div id="ModRettVoci" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="ModRettVociLabel" aria-hidden="true">
    <div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true"data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4>Rettifica voce - Importo attuale <s:property value="getText('struts.money.format', {mutuoSelezionato.importoAttualeMutuo})"/></h4>										             
	</div>
	
	<div class="modal-body">
		<div class="control-group">
			<label for="impoVoce" class="control-label">Riduzione per rettifica</label>
			<div class="controls">
				<s:textfield id="importoRettifica" name="importoRettifica" onkeypress="return checkItNumbersCommaAndDotAndMinusOnly(event)" cssClass="span2 soloNumeri decimale"/>
			</div>
		</div>
    </div>  
	
	<div class="modal-footer">
		  <s:submit id="submitBtnRettifica" name="btnRiduzione" value="conferma" cssClass="btn btn-primary"  method="inserisciRettificaVoceMutuo"/>
	</div>
</div>
<!--end Modal -->