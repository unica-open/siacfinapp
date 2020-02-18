<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<h4>Dati <s:property value="%{labels.OGGETTO_GENERICO}" /></h4>
<fieldset class="form-horizontal margin-large">
	<div class="control-group">
		<label class="control-label" for="descrizioneSubimpegno">Descrizione </label>
		<div class="controls">
			<s:textfield id="descrizioneSubimpegno" name="step1ModelSubimpegno.oggettoImpegno" cssClass="span5" maxlength="500"></s:textfield>  
			<span class="al"> <label class="radio inline" for="statoSubimpegno">Stato</label>
			</span><s:textfield id="statoSubimpegno" name="step1ModelSubimpegno.stato" cssClass="lbTextSmall span2" disabled="true"></s:textfield>
		</div>
	</div>
    <!-- GESTIONE AJAX CON IMPORTO INIZIALe -->
	<div class="control-group" id ="gestioneAggiornaContemporaneo">
			  <s:include value="/jsp/movgest/include/aggiornaImportoIniziale.jsp" /> 
	</div>

    <!-- con Impegno vengo gestiti anche Progetto, CIG e CUP --> 
	<s:if test="oggettoDaPopolareSubimpegno()"> 
	
		<div class="control-group">
		    <span class="control-label">Tipo debito SIOPE</span>
		    <div class="controls">    
		      <s:radio id="tipoDebitoSiope" name="step1ModelSubimpegno.tipoDebitoSiope" cssClass="flagResidenza"
		       list="step1ModelSubimpegno.scelteTipoDebitoSiope" disabled="!isAbilitatoAggiornamentoCampiSiopePlus()"></s:radio> 
		    </div>
		</div>
	
	    <div class="control-group">
			<label class="control-label" for="cig"><abbr title="codice identificativo gara">CIG</abbr></label>
			<div class="controls">
				<s:textfield cssClass="lbTextSmall span2 cig" id="cig" name="step1ModelSubimpegno.cig" 
				maxlength="10" disabled="!isAbilitatoAggiornamentoCampiSiopePlus()"/>   
				
				<span id="bloccoMotivazioneAssenzaCig">
					<span class="al">
			      		<label class="radio inline" for="listaTipiImpegno">Motivazione assenza del CIG</label>
			     	</span>
			     	<s:if test="null!=listaMotivazioniAssenzaCig">
				      	<s:select list="listaMotivazioniAssenzaCig" headerKey="" 
		          		   headerValue="" id="listaMotivazioniAssenzaCigId"  name="step1ModelSubimpegno.motivazioneAssenzaCig" cssClass="span5"  
				       	 	       listKey="codice" listValue="descrizione" disabled="!isAbilitatoAggiornamentoCampiSiopePlus()" />
			       	</s:if> 
		       	</span>
				
			</div>
	     </div>
		
		
		<div class="control-group">
			<label class="control-label" for="cup"><abbr title="Centro Unificato Prenotazioni">CUP</abbr></label>
			<div class="controls">
				<s:textfield cssClass="lbTextSmall span2 cup" id="CUPSubimpegno" name="step1ModelSubimpegno.cup" maxlength="15"/>
			</div>
	    </div>
		
		
	</s:if>

</fieldset>
