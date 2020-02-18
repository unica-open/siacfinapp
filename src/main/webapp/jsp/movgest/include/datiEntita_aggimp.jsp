<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


<h4><s:property value="%{labels.STEP1}"/></h4>
<fieldset class="form-horizontal margin-large">
  <div class="control-group">
  	<s:if test="oggettoDaPopolareImpegno()"><label class="control-label" for="anno">Tipo *</label></s:if>
    	<div class="controls">   
      	<s:if test="oggettoDaPopolareImpegno()">
      
   <!-- 	<label class="radio inline" for="listaTipiImpegno">Tipo *</label> -->
     	
      		<s:select list="listaTipiImpegno" id="listaTipiImpegno" name="step1Model.tipoImpegno" cssClass="span9"  
       	 	       listKey="codice" listValue="descrizione" disabled="!isAbilitatoAggiornamentoGenerico()"/>
        </s:if>
     </div>   
  </div>
      
      
 <div class="control-group">
  <label class="control-label" for="stato">Stato</label>
  <div class="controls">
	<s:textfield id="stato" name="step1Model.descrizioneStatoOperativoMovimento" cssClass="lbTextSmall span3" disabled="true"></s:textfield>
  </div>
 </div>
      	
      
  <div class="control-group">
    <label class="control-label" for="descrizione">Descrizione </label>
    <div class="controls">
      <s:textfield id="descrImpegno" name="step1Model.oggettoImpegno" cssClass="span9" maxlength="500" disabled="!isAbilitatoAggiornamentoGenerico()"></s:textfield>     
    </div>
  </div>

  <!-- SOLO IN INSERIMENTO -->
  <!--div class="control-group">
    <label class="control-label" for="importo">Importo *</label>
    <div class="controls">
      <input id="importo" name="importo" class="span2 required" required="required" type="text"/>
      <span class="al">
        <label class="radio inline" for="scadenza">Scadenza *</label>
      </span>
      <input id="scadenza" class="lbTextSmall span2 required" type="text" value="" name="scadenza"/>
    </div>
  </div-->  
  <!-- FINE SOLO IN INSERIMENTO -->

 <!-- SOLO IN AGGIORNAMENTO -->
  <div class="control-group">
    <label class="control-label" for="importo">Importo *</label>
    <div class="controls">
	    <s:if test="%{!abilitaModificaImporto || !isAbilitatoAggiornamentoGenerico() }">
      		<s:textfield id="importoFormattato" name="step1Model.importoFormattato" cssClass="span2 soloNumeri decimale" disabled="true" ></s:textfield>
      	</s:if>
        <s:else>
        	 
        	<s:if test="step1Model.listaVincoliImpegno!=null && step1Model.listaVincoliImpegno.size()>0">
        	    <!-- in aggiorna impegno in presenza di vincoli -->
        		<s:textfield id="importoFormattato" name="step1Model.importoFormattato"  cssClass="span2 soloNumeri decimale" readonly="true" ></s:textfield>
        		<a href="#aggImportoPerVincolo" data-toggle="modal" id="linkAggiornaImpegnoConVincolo"><i style="font-size: 20px" class="icon-edit"></i></a>
        	</s:if>
        	<s:else>
        	    <!-- in aggiorna impegno o accertamento e non presenza di vincoli -->
        		<s:textfield id="importoFormattato" name="step1Model.importoFormattato"  cssClass="span2 soloNumeri decimale" ></s:textfield>
        	</s:else>
        </s:else>
	     
      <span class="al">
        	<label class="radio inline" for="importoImpegno">Importo iniziale  <s:property value="getText('struts.money.format', {step1Model.importoImpegnoIniziale})"/>
      </span>
    </div>
  </div> 
  
  <!-- IMPORTO UTILIZZABILE SOLO PER ACCERTAMENTI: -->
  <s:if test="!oggettoDaPopolareImpegno()"> 
  
  	<div class="control-group">
    <label class="control-label" for="importo">Per vincoli *</label>
    <div class="controls">
	    <s:if test="%{!abilitaModificaImportoUtilizzabile}">
      		<s:textfield id="importoUtilizzabileFormattato" name="step1Model.importoUtilizzabileFormattato" cssClass="span2 soloNumeri decimale" disabled="true" ></s:textfield>
      	</s:if>
        <s:else>
        	<s:textfield id="importoUtilizzabileFormattato" name="step1Model.importoUtilizzabileFormattato"  cssClass="span2 soloNumeri decimale" ></s:textfield>
        </s:else>
	     
      <span class="al">
        	<label class="radio inline" for="importoImpegno">di cui non vincolato  <s:property value="getText('struts.money.format', {step1Model.disponibilitaUtilizzare})"/>
      </span>
    </div>
  </div> 
  
  </s:if>
  
  
   
  <div class="control-group">
    <label class="control-label" for="scadenza">Scadenza </label>
    <div class="controls">      
	 <s:textfield id="scadenza" title="gg/mm/aaaa" name="step1Model.scadenza" cssClass="lbTextSmall span2 datepicker" disabled="!isAbilitatoAggiornamentoGenerico()"></s:textfield>
	 
	     <s:if test="oggettoDaPopolareImpegno()"> 
	      <s:if test="isVisibileFlagFrazionabile()">
	     	 <span class="radio guidata">
	    	     <s:radio id="frazionabile" name="step1Model.frazionabile" cssClass="flagResidenza" list="step1Model.scelteFrazionabile"
	    	      disabled="!isModificabileFlagFrazionabile() || !isAbilitatoAggiornamentoGenerico()"></s:radio> 
	    	 </span>
	      </s:if>
   		 </s:if>
	 
    </div>
  </div>
  <!-- FINE SOLO IN AGGIORNAMENTO -->  
  
  
  <!-- PROGETTO -->
 	<div class="control-group">
	<label class="control-label" for="progetto">Progetto</label>
 	<div class="controls">   
    
 	     <s:textfield id="progetto" name="step1Model.progetto" cssClass="lbTextSmall span3"
 	       disabled="!abilitaModificaProgetto() || !isAbilitatoAggiornamentoGenerico()"></s:textfield>
 	       
      <s:if test="oggettoDaPopolareImpegno()">    
 	       
	       <span class="al">
        	<label class="radio inline" for="scadenza">Cronoprogramma </label>
      	   </span>
      	   
	       <s:textfield id="cronoprogramma" name="step1Model.cronoprogramma" cssClass="lbTextSmall span4" readonly="true"></s:textfield>
      	   <s:hidden id="idCronoprogramma" name="step1Model.idCronoprogramma" />
	       
 	       
 	       
	       <span class="hide">
	       <s:include value="/jsp/movgest/include/labelProgetto.jsp" />
	       </span>
	     </s:if>
	     <s:else>
	       <s:include value="/jsp/movgest/include/labelProgetto.jsp" />
	     </s:else>
	       
 	     <s:if test="abilitaModificaProgetto() && isAbilitatoAggiornamentoGenerico()">
 	     	 <span class="radio guidata"><a id="linkCompilazioneGuidataProgetto" href="#guidaProg" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
 	     </s:if>
 
     </div>
     
     
     


<!-- <br/> -->
	  	       
<!--   	  <div class="control-group"> -->
<%-- 	    <span class="control-label">Attivit&agrave; prevista </span> --%>
<!-- 	    <div class="controls">     -->
<%-- 	       <s:textfield id="attivitaPrevista" name="step1Model.attivitaPrevista" cssClass="lbTextSmall span4" readonly="true"  ></s:textfield> --%>
<%--       	   <s:hidden id="idSpesaCronoprogramma" name="step1Model.idSpesaCronoprogramma" /> --%>
<!-- 	    </div> -->
<!-- 	  </div> -->
	       
	       <s:hidden id="idSpesaCronoprogramma" name="step1Model.idSpesaCronoprogramma" />
	
     
     
   </div>
  
  <!-- se impegno allora si vede il CIG e i dati del CUP -->
  <s:if test="oggettoDaPopolareImpegno()">
  
  	
  	  <div class="control-group">
	    <span class="control-label">Tipo debito SIOPE</span>
	    <div class="controls">    
	      <s:radio id="tipoDebitoSiope" name="step1Model.tipoDebitoSiope" cssClass="flagResidenza"
	       list="step1Model.scelteTipoDebitoSiope" disabled="!isAbilitatoAggiornamentoCampiSiopePlus()"></s:radio> 
	    </div>
	  </div>
	  
	  <div class="control-group">
			<label class="control-label" for="cig"><abbr title="codice identificativo gara">CIG</abbr></label>
			<div class="controls">
				<s:textfield cssClass="lbTextSmall span2 cig" id="cig" name="step1Model.cig" maxlength="10"
				 disabled="!isAbilitatoAggiornamentoCampiSiopePlus()"></s:textfield>
				
				<span id="bloccoMotivazioneAssenzaCig">
					<span class="al">
			      		<label class="radio inline" for="listaTipiImpegno">Motivazione assenza del CIG</label>
			     	</span>
			     	<s:if test="null!=listaMotivazioniAssenzaCig">
				      	<s:select list="listaMotivazioniAssenzaCig" headerKey="" 
		          		   headerValue="" id="listaMotivazioniAssenzaCigId"  name="step1Model.motivazioneAssenzaCig" cssClass="span5"  
				       	 	       listKey="codice" listValue="descrizione" disabled="!isAbilitatoAggiornamentoCampiSiopePlus()" />
			       	</s:if>
		       	</span> 
				
			</div>
	  </div>
	  
 	  <div class="control-group">
			<label class="control-label" for="cup"><abbr title="Centro Unificato Prenotazioni">CUP</abbr></label>
			<div class="controls">
				<s:textfield cssClass="lbTextSmall span2 cup" id="cup" name="step1Model.cup" maxlength="15" 
				disabled="!isAbilitatoAggiornamentoGenerico()"></s:textfield>
			</div>
	  </div>
	  
  </s:if>

  <div class="control-group">
    <span class="control-label">Da riaccertamento</span>
    <div class="controls">    
      <s:if test="%{flagValido}">
        <s:radio id="riaccertato" name="step1Model.riaccertato" cssClass="flagResidenza" list="step1Model.daRiaccertamento" onclick="check(this.value)" disabled="true"></s:radio>
      </s:if>
      <s:else>
        <s:radio id="riaccertato" name="step1Model.riaccertato" cssClass="flagResidenza" list="step1Model.daRiaccertamento" onclick="check(this.value)"
        disabled="!isAbilitatoAggiornamentoGenerico()"></s:radio>
      </s:else>   
	  <!-- CAMPI VISIBILI SE RADIOBUTTON SI=CHECKED -->       
      <span class="riaccVisible" id="bloccoRiaccertato">
      	<s:if test="%{flagValido}">
      		&nbsp; <s:textfield id="annoImpRiacc" name="step1Model.annoImpRiacc" onkeyup="return checkItNumbersOnly(event)" maxlength="4" cssClass="span1 " title="Anno" disabled="true"/>&nbsp;
      		<s:textfield id="numImpRiacc" cssClass="lbTextSmall span2 " onkeyup="return checkItNumbersOnly(event)" title="Numero" name="step1Model.numImpRiacc" disabled="true"/>
      	</s:if>
      	<s:else>
      		&nbsp; <s:textfield id="annoImpRiacc" name="step1Model.annoImpRiacc" onkeyup="return checkItNumbersOnly(event)"
      		 maxlength="4" cssClass="span1 " title="Anno" disabled="!isAbilitatoAggiornamentoGenerico()"/>&nbsp;
      		<s:textfield id="numImpRiacc" cssClass="lbTextSmall span2 " onkeyup="return checkItNumbersOnly(event)"
      		 title="Numero" name="step1Model.numImpRiacc" disabled="!isAbilitatoAggiornamentoGenerico()"/>
      	</s:else>	
      </span>
      <!-- FINE CAMPI VISIBILI -->      
    </div>
  </div>
  
  
  <s:if test="oggettoDaPopolareImpegno()">
  
  	  <div class="control-group">
	    <span class="control-label">Prenotazione</span>
	    <div class="controls">    
	      <s:radio id="prenotazione" name="step1Model.prenotazione" cssClass="flagResidenza" list="step1Model.daPrenotazione"
	     	 disabled="!isAbilitatoAggiornamentoGenerico()"></s:radio> 
	       <s:if test="isAbilitatoGestisciImpegno()"> 
	       	 <span class="liqPerPrenotazioneVisibile" id="bloccoPrenotatoLiquidabile">
		     	<label class="radio inline" for="prenotatoLiquidabileCheckBox">Liquidabile</label>
		     	<s:checkbox id="prenotatoLiquidabileCheckBox" name="step1Model.prenotazioneLiquidabile"
		     	 onclick="impostaValorePrenotatoLiquidabile()" disabled="!isAbilitatoAggiornamentoGenerico()"/>
		      </span>
	       </s:if>
	    </div>
	  </div>
  
  
  <div class="control-group">
    <span class="control-label">Soggetto a DURC</span>
	    <div class="controls">    
	      <s:radio id="soggettoDurc" name="step1Model.soggettoDurc" cssClass="flagResidenza" list="#{ 'Si': 'S&igrave;', 'No' : 'No' }"></s:radio> 
	    </div>
  </div>
  
</s:if>
  	  
  
  
  <div class="control-group">
    <div class="control-label"><s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/> di origine</div>
    <div class="controls">
<%--       <s:if test="%{flagValido}">
	     	 <s:textfield id="annoImpOrigine" name="step1Model.annoImpOrigine" title="Anno" onkeyup="return checkItNumbersOnly(event)" cssClass="span1" disabled="true"/>&nbsp;
    	  	 <s:textfield id="numImpOrigine" cssClass="lbTextSmall span2" onkeyup="return checkItNumbersOnly(event)" title="Numero"name="step1Model.numImpOrigine" disabled="true"/>
      </s:if>
      <s:else>
 --%> 	     <s:textfield id="annoImpOrigine" name="step1Model.annoImpOrigine" title="Anno" onkeyup="return checkItNumbersOnly(event)" 
 				cssClass="span1" disabled="!isAbilitatoAggiornamentoGenerico()"/>&nbsp;
    	     <s:textfield id="numImpOrigine" cssClass="lbTextSmall span2" onkeyup="return checkItNumbersOnly(event)" title="Numero" 
    	        name="step1Model.numImpOrigine" disabled="!isAbilitatoAggiornamentoGenerico()"/>
<%--  </s:else> --%>	  
    </div>
  </div>
  <!-- BLOCCO VISIBILE SOLO IN INSERIMENTO-->
  <!--div class="control-group">
    <span class="control-label">Impegno pluriennale</span>
    <div class="controls">
      <label class="radio inline">
      <input type="radio" name="pluriennale" id="pluriennale1" value="option1">
      si
      </label>
      <label class="radio inline">
      <input type="radio" name="pluriennale" id="pluriennale2" value="option2" checked="checked">
      no
      </label>
      <span class="al">
        <label class="radio inline" for="Anni">Per anni</label>
      </span>
      <input id="Anni" class="lbTextSmall span2" type="text" value="" name="Anni"/>
    </div>
  </div-->
  <!-- FINE BLOCCO VISIBILE SOLO IN INSERIMENTO-->
  
  
    <!-- GESTIONE NUOVO FLAG PER ACCERTAMENTI (FlagFattura)  -->
  <s:if test="!oggettoDaPopolareImpegno()"> 
       <div class="control-group">
		    <span class="control-label">&Egrave; prevista fattura</span>
		    <div class="controls">      
		      <s:radio name="step1Model.flagFattura" id="radioPrevistaFattura" cssClass="flagResidenza" list="#{ 'Si': 'S&igrave;', 'No' : 'No' }" 
		      disabled="disableFlagPrevistaFattura()"></s:radio> 
		    </div>
		</div>
		
       <div class="control-group">
		    <span class="control-label">&Egrave; previsto corrispettivo</span>  
		    <div class="controls">      
		      <s:radio name="step1Model.flagCorrispettivo" id="radioPrevistoCorrispettivo" cssClass="flagResidenza" list="#{ 'Si': 'S&igrave;', 'No' : 'No' }" 
		      disabled="disableFlagPrevistoCorrispettivo()"></s:radio>
		      <s:if test="not step1Model.descrizioneStatoOperativoMovimento eq 'PROVVISORIO'"><s:hidden name="step1Model.flagCorrispettivo"/></s:if> 
		    </div>
		</div>
  </s:if>
  
  <s:if test="oggettoDaPopolareImpegno()"> 
 	   <div class="control-group">
	    <span class="control-label">Impegno di cassa economale</span>
	    <div class="controls">    
	      <s:radio id="riaccertato" name="step1Model.cassaEconomale" cssClass="flagResidenza" list="step1Model.diCassaEconomale"
	      	disabled="!isAbilitatoAggiornamentoGenerico()"></s:radio> 
	    </div>
	  </div>
 </s:if> 
  
  <!-- GESTIONE NUOVO FLAG PER ATTIVA GSA (FlagAttivaGsa)  -->
<%--   <s:if test="isEnteAbilitatoGestioneGsa()"> --%>
<!-- 	  <div class="control-group"> -->
<%-- 	    <span class="control-label">Rilevante Co.Ge. GSA</span> --%>
<!-- 	    <div class="controls">       -->
<%-- 	      <s:radio name="step1Model.flagAttivaGsa" id="radioAttivaGsa" cssClass="flagResidenza" list="step1Model.listflagAttivaGsa"></s:radio>  --%>
<!-- 	    </div> -->
<!-- 	  </div> -->
<%--   </s:if> --%>
  
</fieldset>