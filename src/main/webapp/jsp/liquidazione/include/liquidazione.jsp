<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

			<h4>Liquidazione
				<s:if test="%{step != null && step == 'INSLIQSTEP3'}"> 
				 : <s:property value="annoLiquidazioneScheda"/> / <s:property value="numeroLiquidazioneScheda.intValue()"/><s:if test="%{descrizioneLiquidazioneScheda != null && descrizioneLiquidazioneScheda != ''}"> - </s:if><s:property value="descrizioneLiquidazioneScheda"/><s:if test="%{statoLiquidazioneScheda != null && statoLiquidazioneScheda != ''}"> - </s:if><s:property value="statoLiquidazioneScheda"/>       
				</s:if>
			</h4> 
			
            <fieldset class="form-horizontal">
       
       			<s:if test="%{step != null && ( step == 'INSLIQSTEP2' || ( step='INSLIQSTEP3' && (statoLiquidazioneScheda == 'VALIDO' || statoLiquidazioneScheda == 'PROVVISORIO') )  ) }"> 
					<div class="control-group">
						<label class="control-label" for="descr">Descrizione </label>
						<div class="controls">	
						
							<s:if test="presenzaOrdinativiPerLaLiquidazione">
				        		<!-- DISABILITATO -->
				        		<s:textfield id="descrizioneLiquidazione" cssClass="lbTextSmall span9" name="descrizioneLiquidazione" maxlength="500" disabled="true"></s:textfield>
				        	</s:if>
				        	<s:else>
				        		<!-- ABILITATO -->
				        		<s:textfield id="descrizioneLiquidazione" cssClass="lbTextSmall span9" name="descrizioneLiquidazione" maxlength="500"></s:textfield>
				        	</s:else>
						  
						</div>
					</div>
	  			</s:if> 
	  			
				<div class="control-group">
					<label class="control-label" for="descr">Stato </label>
					<div class="controls">    
					  <s:textfield id="descrizioneLiquidazione" cssClass="lbTextSmall span2" name="provvedimento.stato" disabled="true"></s:textfield>	              
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="importo">Importo *</label>
					<div class="controls">
						<s:if test="lockImportoLiquidazione">    
					 		<s:textfield id="importoLiquidazione" cssClass="lbTextSmall span2 soloNumeri decimale" name="importoLiquidazione" disabled="true"></s:textfield>	
					  	</s:if>
					  	<s:else>
					  		<s:textfield id="importoLiquidazione" cssClass="lbTextSmall span2 soloNumeri decimale" name="importoLiquidazione" onkeypress="return checkItNumbersCommaAndDotOnly(event)"></s:textfield>
					  	</s:else>
						<div class="radio inline collapse_alert"><span class="icon-chevron-right icon-red alRight"></span>Disponibile a liquidare  <s:property value="disponibilita"></s:property></div>
					</div>
				</div>
				
				 <div class="control-group">
				    <span class="control-label">Tipo debito SIOPE</span>
				    <div class="controls">    
				      <s:radio id="tipoDebitoSiope" name="tipoDebitoSiope" cssClass="flagResidenza" list="scelteTipoDebitoSiope"
				      disabled="!isAbilitatoAggiornamentoCampiSiopePlus()"></s:radio> 
				    </div>
				 </div>
				
				  <div class="control-group">
						<label class="control-label" for="cig"><abbr title="codice identificativo gara">CIG</abbr></label>
						<div class="controls">
							<s:if test="presenzaOrdinativiPerLaLiquidazione || liquidazioneConAzioneImpegnoDecentrato">
			        			<!-- DISABILITATO -->
			        			<s:textfield id="cig" cssClass="lbTextSmall span3" name="cig" 
			        			disabled="true"></s:textfield>
				        	</s:if>
				        	<s:else>
				        		<!-- ABILITATO -->
						        <s:textfield id="cig" cssClass="lbTextSmall span3 cig" name="cig" 
						        disabled="!isAbilitatoAggiornamentoCampiSiopePlus()"></s:textfield>		
						    </s:else>
							
							<span id="bloccoMotivazioneAssenzaCig">
								<span class="al">
						      		<label class="radio inline" for="listaTipiImpegno">Motivazione assenza del CIG</label>
						     	</span>
						     	<s:if test="null!=listaMotivazioniAssenzaCig">
							      	<s:select list="listaMotivazioniAssenzaCig" headerKey="" 
					          		   headerValue="" id="listaMotivazioniAssenzaCigId"  name="motivazioneAssenzaCig" cssClass="span5"  
							       	 	       listKey="codice" listValue="descrizione" disabled="!isAbilitatoAggiornamentoCampiSiopePlus()" />
						       	</s:if> 
					       	</span>
							
						</div>
				  </div>
				  
				  
				  <div class="control-group">
						<label class="control-label" for="cup"><abbr title="Centro Unificato Prenotazioni">CUP</abbr></label>
						<div class="controls">
							<s:if test="presenzaOrdinativiPerLaLiquidazione || liquidazioneConAzioneImpegnoDecentrato">
				        		<!-- DISABILITATO -->
				        		<s:textfield id="cup" cssClass="lbTextSmall span3" name="cup" disabled="true"></s:textfield>
				        	</s:if>
				        	<s:else>
				        		<!-- ABILITATO -->
				        		<s:textfield id="cup" cssClass="lbTextSmall span3 cup" name="cup"></s:textfield>
				        	</s:else>
						</div>
				  </div>
				  
				  
				  
				  
				  
			    
				<div class="control-group">
					<label class="control-label" for="distinta">Distinta</label>
					<div class="controls">
					
							<s:if test="presenzaOrdinativiPerLaLiquidazione">
				        		<!-- DISABILITATO -->
				        		<s:select list="listDistinta" id="listDistinta"
								   name="distinta" cssClass="span4"  
							       headerKey="" headerValue="" 
				       	 	       listKey="codice" listValue="codice+' - '+descrizione" disabled="true"/>
				        	</s:if>
				        	<s:else>
				        		<!-- ABILITATO -->
				        		<s:select list="listDistinta" id="listDistinta"
								   name="distinta" cssClass="span4"  
							       headerKey="" headerValue="" 
				       	 	       listKey="codice" listValue="codice+' - '+descrizione" />
				        	</s:else>
					  
					  <span class="alRight">
						<label class="radio inline" for="ctTesoriere">Conto del tesoriere </label>
					  </span>
					  		<s:if test="presenzaOrdinativiPerLaLiquidazione">
				        		<!-- DISABILITATO -->
				        		<s:if test="listContoTesoreria.size() == 1">
	 		  						<s:select list="listContoTesoreria" id="listContoTesoreria"
								   name="contoTesoriere" cssClass="span4"  
				       	 	       listKey="codice" listValue="codice+' - '+descrizione" disabled="true"/> 
			 		  			 </s:if>
			 		  			 <s:else>										  
							 		<s:select list="listContoTesoreria" id="listContoTesoreria"
									   name="contoTesoriere" cssClass="span4"  
								       headerKey="" headerValue="" 
					       	 	       listKey="codice" listValue="codice+' - '+descrizione" disabled="true"/> 
						       	</s:else>
				        	</s:if>
				        	<s:else>
				        		<!-- ABILITATO -->
				        		<s:if test="listContoTesoreria.size() == 1">
	 		  						<s:select list="listContoTesoreria" id="listContoTesoreria"
								   name="contoTesoriere" cssClass="span4"  
				       	 	       listKey="codice" listValue="codice+' - '+descrizione" /> 
			 		  			 </s:if>
			 		  			 <s:else>										  
							 		<s:select list="listContoTesoreria" id="listContoTesoreria"
									   name="contoTesoriere" cssClass="span4"  
								       headerKey="" headerValue="" 
					       	 	       listKey="codice" listValue="codice+' - '+descrizione" /> 
						       	</s:else>
				        	</s:else>
					</div>
				</div>
				
				<div class="control-group">
					<span class="control-label">Emissione Ordinativo *</span>
					<div class="controls">
					<label class="radio inline" for="tipoConvalidaList"></label>
						<s:if test="lockTipoConvalida || presenzaOrdinativiPerLaLiquidazione">
							<!-- DISABILITATO -->
					 		<s:radio list="tipoConvalidaList" id="tipoConvalidaList" 
							 name="tipoConvalida" disabled="true"
							 listKey="codice" listValue="descrizione"/>			
					  	</s:if>
					  	<s:else>
					  		<!-- ABILITATO -->
					  		<s:radio list="tipoConvalidaList" id="tipoConvalidaList" 
							 name="tipoConvalida"
							 listKey="codice" listValue="descrizione"/>		
					  	</s:else>
					
					</div>			
				</div>
				
            </fieldset> 
            