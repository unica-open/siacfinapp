<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>		  
            <h4>Liquidazione</h4> 
            <fieldset class="form-horizontal">
      
				<div class="control-group">
					<label class="control-label" for="descr">Descrizione *</label>
					<div class="controls"> 
					  <s:textarea cssClass="span9" rows="1" cols="15" id="descrizione" name="nuovaLiquidazioneModel.descrizioneLiquidazione" />                  
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="importo">Importo *</label>
					<div class="controls">    
					  
                      <s:textfield id="importoLiquidazione" name="nuovaLiquidazioneModel.importoLiquidazione" 
                                     onkeypress="return checkItNumbersCommaAndDotOnly(event)" cssClass="lbTextSmall span2 soloNumeri decimale" maxlength="15" />
					</div>
				</div>
				

				<div class="control-group">
					<label class="control-label" for="CIG"><abbr title="codice identificativo gara">CIG</abbr></label>
					<div class="controls">
					  
					  <s:textfield id="cig" cssClass="lbTextSmall span3 cig" name="nuovaLiquidazioneModel.cig" />
					  <span class="alRight">
					   <label class="radio inline" for="CUP"><abbr title="codice unico progetto">CUP</abbr></label>
					  </span>
					 <s:textfield id="cup" cssClass="lbTextSmall span3 cup" disabled="true" name="nuovaLiquidazioneModel.cup" />
					</div>
				  </div>

<!-- NON SERVONO QUESTE COMBO -->				
<!-- 				<div class="control-group"> -->
<!-- 					<label class="control-label" for="distinta">Distinta</label> -->
<!-- 					<div class="controls"> -->
<%-- 					 <s:if test="null!=gestioneOrdinativoStep1Model.listaDistinta"> --%>
<%-- 							  <s:select list="gestioneOrdinativoStep1Model.listaDistinta" id="listaDistinta" headerKey=""  --%>
<%-- 					           		   headerValue="" name="nuovaLiquidazioneModel.distinta" cssClass="span3"  --%>
<%-- 					           		   listKey="codice" listValue="codice+' - '+descrizione" />    --%>
<%-- 	      			 </s:if>   --%>
<%-- 					  <span class="alRight"> --%>
<!-- 						<label class="radio inline" for="ctTesoriere">Conto del tesoriere </label> -->
<%-- 					  </span> --%>
					  
<%-- 					  <s:if test="null!=gestioneOrdinativoStep1Model.listaContoTesoriere"> --%>
<%-- 				 		  <s:select list="gestioneOrdinativoStep1Model.listaContoTesoriere" id="listacontoTesoreria" headerKey=""   --%>
<%-- 				           		   headerValue="" name="nuovaLiquidazioneModel.contoTesoriere" cssClass="span4"  --%>
<%-- 				           		   listKey="codice" listValue="codice+' - '+descrizione" /> --%>
<%--           			  </s:if>  --%>
					  
<!-- 					</div> -->
<!-- 				</div> -->
				
            </fieldset> 
            