<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h3>Quota Numero: <s:property value="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.numeroQuota"/></h3>
							</div>
							<div class="modal-body">

								<dl class="dl-horizontal">
								    <dt>Descrizione</dt>
								    <dd><s:property value="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.descrizione"/>&nbsp;</dd>
								    <dt>Importo</dt>
								    <dd><s:property value="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.importoQuotaFormattato"/>&nbsp;</dd>
								    <dt>Data scadenza</dt>				    
								    <dd><s:property value="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.dataScadenzaSubOrd"/>&nbsp;</dd>
								    <dt>Accertamento</dt>
								    <dd><s:property value="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.accertamento"/>&nbsp;</dd>
								</dl>
							</div>
							<div class="modal-footer">
								<button class="btn" data-dismiss="modal" aria-hidden="true">chiudi</button>
							</div>
								    
<!-- <div class="modal-header"> -->
<!-- 	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> -->
<%-- 	<h4>Quota Numero: <s:property value="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.numeroQuota"/> </h4>		  --%>
<!-- </div> -->

<!-- <div class="modal-body"> -->
<!-- 	<fieldset class="form-horizontal"> -->
<!-- 		<div class="control-group"> -->
<!-- 			<label class="control-label" for="anno1">Descrizione</label> -->
<!-- 			<div class="controls"> -->
<%-- 			  <s:property value="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.descrizione"/>     --%>
<%-- <%-- 			  <s:textfield id="descPopUp" name="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.descrizione" cssClass="lbTextSmall span9"  />	   --%> 
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		<div class="control-group"> -->
<!-- 			<label class="control-label" for="anno1">Importo</label> -->
<!-- 			<div class="controls">    -->
<%-- 				  <s:property value="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.importoQuotaFormattato"/>     --%>
<%-- <%-- 			  <s:textfield id="impQuotaOrdinativo" cssClass="lbTextSmall span2 soloNumeri decimale" onkeyup="return checkItNumbersOnly(event)"  --%> 
<%-- <%-- 			               name="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.importoQuotaFormattato" maxlength="15" disabled="checkDisabilita()" />  --%> 
			  
<%-- 			  <span class="al"> --%>
<!-- 				<label class="radio inline" for="capitolo">Data esecuzione pagamento</label> -->
<%-- 			  </span> --%>
<%-- 			  <s:property value="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.dataEsecuzionePagamento"/>     --%>
			  
<%-- <%-- 			  <s:textfield id="dataQuota" cssClass="lbTextSmall span2 datepicker" maxlength="10" title="gg/mm/aaaa" --%> 
<%-- <%-- 			  			   name="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.dataEsecuzionePagamento" /> --%> 
			  
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</fieldset>     -->
      
<%--       <s:hidden id="numQuotaPopUp" name="gestioneOrdinativoStep2Model.dettaglioQuotaOrdinativoModel.numeroQuota"/>  --%>
       
<!-- </div> -->
<!-- <div class="modal-footer"> -->
<%-- 	<s:submit id="submitBtnAggOrdinativo" name="btnAggOrdinativo" value="conferma" cssClass="btn btn-primary" method="aggiornaQuotaOrdinativo"/> --%>
<!-- </div> -->
