<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>



<fieldset class="form-horizontal">
		<div class="accordion" id="accordionPlusId">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a class="accordion-toggle" data-toggle="collapse"
						data-parent="#accordionPlusId" href="#datiPlusId">Dati Siope+<span
						class="icon">&nbsp;</span></a>
				</div>
				
				<div id="datiPlusId" class="accordion-body collapse in">
					<div class="accordion-inner">
				
			  	    <div class="control-group">
					    <span class="control-label">Tipo debito SIOPE</span>
					    <div class="controls">    
					      <s:radio id="tipoDebitoSiope" name="gestioneOrdinativoStep2Model.tipoDebitoSiope"
					       cssClass="flagResidenza" list="gestioneOrdinativoStep2Model.scelteTipoDebitoSiope"
					       disabled="true"></s:radio> 
					    </div>
					 </div>
				  
					  <div class="control-group">
							<label class="control-label" for="cig"><abbr title="codice identificativo gara">CIG</abbr></label>
							<div class="controls">
								<s:textfield cssClass="lbTextSmall span2 cig" id="cig" name="gestioneOrdinativoStep2Model.cig"
								 maxlength="10" disabled="true"/>
								
								<span id="bloccoMotivazioneAssenzaCig">
									<span class="al">
							      		<label class="radio inline" for="listaTipiImpegno">Motivazione assenza del CIG</label>
							     	</span>
							     	<s:if test="null!=gestioneOrdinativoStep2Model.listaMotivazioniAssenzaCig">
								      	<s:select list="gestioneOrdinativoStep2Model.listaMotivazioniAssenzaCig" headerKey="" 
						          		   headerValue="" id="listaMotivazioniAssenzaCigId"  name="gestioneOrdinativoStep2Model.motivazioneAssenzaCig" cssClass="span5"  
								       	 	       listKey="codice" listValue="descrizione" disabled="true" />
							       	</s:if>
						       	</span> 
								
							</div>
					  </div>
				
				    </div>
				</div>
				
		 	</div>
		 </div>
</fieldset>


