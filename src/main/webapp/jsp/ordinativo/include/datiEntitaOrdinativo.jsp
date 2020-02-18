<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


	<h4>Dati ordinativo</h4>
	<fieldset class="form-horizontal">
  
  
  	<div class="control-group">
		<label class="control-label" for="descr">Descrizione *</label>
		<div class="controls">
<%-- 		<s:textfield id="descrizione" name="gestioneOrdinativoStep1Model.ordinativo.descrizione" maxlength="250"></s:textfield> --%>
		<s:textarea id="descrizione" rows="1" cols="15"  cssClass="span9" name="gestioneOrdinativoStep1Model.ordinativo.descrizione" ></s:textarea>
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="bollo">Bollo *</label>
		<div class="controls">
		<s:if test="null!=gestioneOrdinativoStep1Model.listaBollo">
			<s:if test="gestioneOrdinativoStep1Model.listaBollo.size() == 1">
			<s:select list="gestioneOrdinativoStep1Model.listaBollo" id="listaBollo" 
						   name="gestioneOrdinativoStep1Model.ordinativo.codiceBollo.codice" cssClass="span3" 
		           		   listKey="codice" listValue="codice+' - '+descrizione" /> 
			</s:if> 
		    <s:else>
				<s:select list="gestioneOrdinativoStep1Model.listaBollo" id="listaBollo" headerKey="" 
		           		   headerValue="" name="gestioneOrdinativoStep1Model.ordinativo.codiceBollo.codice" cssClass="span3" 
		           		   listKey="codice" listValue="codice+' - '+descrizione" /> 
	        </s:else>
        </s:if>
		</div>
	</div>

   
   <s:if test="oggettoDaPopolarePagamento()">
		<div class="control-group">
			<label class="control-label">Commissioni *</label>
			<div class="controls">
			<s:if test="gestioneOrdinativoStep1Model.listaCommissioni!=null">
				<s:if test="gestioneOrdinativoStep1Model.listaCommissioni.size() == 1">
				 <s:select list="gestioneOrdinativoStep1Model.listaCommissioni" id="listaCommissioni"
				 	       name="gestioneOrdinativoStep1Model.ordinativo.commissioneDocumento.codice" cssClass="span9"   
	            		   listKey="codice" listValue="codice+' - '+descrizione" /> 
				</s:if>
				<s:else>
				  <s:select list="gestioneOrdinativoStep1Model.listaCommissioni" id="listaCommissioni" headerKey="" 
	          		   headerValue="" name="gestioneOrdinativoStep1Model.ordinativo.commissioneDocumento.codice" cssClass="span9"   
	            		   listKey="codice" listValue="codice+' - '+descrizione" /> 
	 			</s:else>        
	         </s:if>   		     
			</div>
		</div>
	</s:if>
	
	<div class="control-group">
		<label class="control-label" for="distinta">Distinta*</label>
		<div class="controls">
		
		 <s:if test="null!=gestioneOrdinativoStep1Model.listaDistinta">
		 	<s:if test="gestioneOrdinativoStep1Model.listaDistinta.size() == 1">
			 	 <s:select list="gestioneOrdinativoStep1Model.listaDistinta" id="listaDistinta"
			 	 		   name="gestioneOrdinativoStep1Model.ordinativo.distinta.codice" cssClass="span4" 
		           		   listKey="codice" listValue="codice+' - '+descrizione" />
		 	</s:if>
		 	<s:else>
			  <s:select list="gestioneOrdinativoStep1Model.listaDistinta" id="listaDistinta" headerKey="" 
	           		   headerValue="" name="gestioneOrdinativoStep1Model.ordinativo.distinta.codice" cssClass="span4" 
	           		   listKey="codice" listValue="codice+' - '+descrizione" />
	        </s:else>  		      
	      </s:if>      		   
 		  <span class="al"> 
			<label class="radio inline" for="ctTesoriere">Conto del tesoriere *</label>
 		  </span> 
 		  <s:if test="null!=gestioneOrdinativoStep1Model.listaContoTesoriere">
 		  	<s:if test="gestioneOrdinativoStep1Model.listaContoTesoriere.size() == 1">
	 		  	<s:select list="gestioneOrdinativoStep1Model.listaContoTesoriere" id="listacontoTesoreria" 
	 		  	           name="gestioneOrdinativoStep1Model.ordinativo.contoTesoreria.codice" cssClass="span4" 
		           		   listKey="codice" listValue="codice+' - '+descrizione" />
 		  	</s:if>
 		  	<s:else>
	 		  <s:select list="gestioneOrdinativoStep1Model.listaContoTesoriere" id="listacontoTesoreria" headerKey=""  
	           		   headerValue="" name="gestioneOrdinativoStep1Model.ordinativo.contoTesoreria.codice" cssClass="span4" 
	           		   listKey="codice" listValue="codice+' - '+descrizione" />
            </s:else>
          </s:if> 		      
		</div>
	</div>
	
	
	
	<div class="control-group">
		<label class="control-label" for="noteal">Note al tesoriere
			<a class="tooltip-test" title="Selezionando un elemento dalla lista viene valorizzato il campo 'Note al tesoriere' in automatico, campo Note che &egrave; comunque editabile.">
				<i class="icon-info-sign">&nbsp;</i>
			</a>
		</label>
		<div class="controls">
		  <s:if test="null!=gestioneOrdinativoStep1Model.listaNoteTesoriere">
		  	<s:if test="gestioneOrdinativoStep1Model.listaNoteTesoriere.size() == 1">
		  	  <s:select list="gestioneOrdinativoStep1Model.listaNoteTesoriere" id="listaNoteTesoriere" 
		  	           name="gestioneOrdinativoStep1Model.ordinativo.noteTesoriere.codice" cssClass="span9" 
	           		   listKey="codice" listValue="codice+' - '+descrizione" disabled="!isCampoAbilitatoInAggiornamento('TESORIERE')" />
		  	</s:if>
		  	<s:else>
	 		  <s:select list="gestioneOrdinativoStep1Model.listaNoteTesoriere" id="listaNoteTesoriere" headerKey=""  
	           		   headerValue="" name="gestioneOrdinativoStep1Model.ordinativo.noteTesoriere.codice" cssClass="span9" 
	           		   listKey="codice" listValue="codice+' - '+descrizione" disabled="!isCampoAbilitatoInAggiornamento('TESORIERE')" />
	        </s:else>   		   
          </s:if> 	
          </div>
	</div>
	
	
	<div class="control-group">
		<label class="control-label" for="note"></label>
		<div class="controls">
		<s:textarea rows="3" cols="15" id="Note" cssClass="span9" name="gestioneOrdinativoStep1Model.ordinativo.note"></s:textarea>
		</div>
	</div>
		
	<div id="aggiornaAvviso">

		<s:include value="/jsp/ordinativo/include/avvisoAggiorna.jsp" />
	</div>

	<div class="control-group">
		<label class="control-label" for="Allcart">Allegati cartacei</label>
		<div class="controls">
		 <s:checkbox id="flagAllegatoCartaceo" name="gestioneOrdinativoStep1Model.ordinativo.flagAllegatoCartaceo"/>
		  <span class="al">
		  
		    <s:if test="oggettoDaPopolarePagamento()">
		  
				<label class="radio inline" for="creditMulti">Creditori multipli (vedi allegato)</label>
			
			</s:if>
			<s:else>
				<label class="radio inline" for="creditMulti">Debitori multipli (vedi allegato)</label>
			</s:else>
			
		  </span>
		   <s:checkbox id="flagBeneficiMultiplo" name="gestioneOrdinativoStep1Model.ordinativo.flagBeneficiMultiplo" disabled="!isCampoAbilitatoInAggiornamento('BENEFICIARI_MULTIPLI')" />
		  
		  <s:if test="oggettoDaPopolarePagamento()">
		      <!--  ORDINATIVO PAGAMENTO -->
			  <s:if test="disabilitaCheckAggiornamento()">
				  <span class="al">
					<label class="radio inline" for="Acop">A copertura</label>
				  </span>
				  <s:if test="abilitaProvvisoriCassa()">
				  	<s:checkbox id="flagCopertura" name="gestioneOrdinativoStep1Model.ordinativo.flagCopertura" disabled="!isCampoAbilitatoInAggiornamento('A_COPERTURA')"/>
				  </s:if>
				  <s:else>
				 	 <s:checkbox id="flagCopertura" name="gestioneOrdinativoStep1Model.ordinativo.flagCopertura" disabled="true"/>
				  </s:else>
			  </s:if>
		  </s:if>
		  <s:else>
		  	 <!--  ORDINATIVO INCASSO -->
		     <s:if test="disabilitaCheckAggiornamento()">
				  <span class="al">
					<label class="radio inline" for="Acop">A copertura</label>
				  </span>
				  <s:if test="abilitaProvvisoriCassa()">
				  	<s:checkbox id="flagCopertura" name="gestioneOrdinativoStep1Model.ordinativo.flagCopertura" />
				  </s:if>
				  <s:else>
				 	 <s:checkbox id="flagCopertura" name="gestioneOrdinativoStep1Model.ordinativo.flagCopertura" disabled="true"/>
				  </s:else>
				  
				  <s:if test="cercatoAccertamentoConPianoContiV()">
				   <span class="perReintroitiVisible" id="bloccoPerReintroiti">
					 <span class="al">
						<label class="radio inline" for="flagPerReintroiti">Per reintroiti</label>
					 </span>
				   	 <s:checkbox id="flagPerReintroiti" name="gestioneOrdinativoStep1Model.flagPerReintroiti" disabled="sonoInAggiornamentoIncasso()"
				   	 onclick="impostaValoreFlagPerReintroiti()"/>
				   </span>
				  </s:if>
				  
			  </s:if>
		  </s:else>	



		  <span class="al">
				<label class="radio inline" for="creditMulti">Da trasmettere</label>
		  </span>
          <s:checkbox id="flagDaTrasmettere" name="gestioneOrdinativoStep1Model.ordinativo.flagDaTrasmettere"  fieldValue="true"  />



 
		</div>
	</div>
 


</fieldset>