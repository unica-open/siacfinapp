<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<h4><s:property value="%{labels.STEP1}"/></h4>
<fieldset class="form-horizontal">
	<div class="control-group">
    	<label class="control-label" for="annoImpegno">Anno *</label>
    	<div class="controls">
      		<s:textfield id="annoImpegno" name="step1Model.annoImpegno" maxlength="4" onkeyup="return checkItNumbersOnly(event)" cssClass="span1" disabled="disabilitatoSePredispConsul()"></s:textfield>      
      		<s:if test="oggettoDaPopolareImpegno()">
      			<span class="al">
      				<label class="radio inline" for="listaTipiImpegno">Tipo *</label>
     			</span>
	     		<s:if test="null!=listaTipiImpegno">
		      		<s:select list="listaTipiImpegno" id="listaTipiImpegno"  name="step1Model.tipoImpegno" cssClass="span5" listKey="codice" listValue="descrizione" />
	       		</s:if> 	       
      		</s:if>
      		<span class="al">
        		<label class="radio inline" for="stato">Stato</label>
      		</span>
      		<s:textfield id="stato" name="step1Model.stato" cssClass="lbTextSmall span2" disabled="true"></s:textfield>
    	</div>
  	</div>
  	<span id="bloccoNumero">
		<div class="control-group">
	    	<label class="control-label" for="oggettoImpegno">Numero *</label>
	    	<div class="controls">
	    		<s:textfield id="oggettoImpegno" name="step1Model.numeroImpegno" cssClass="span2" onkeyup="return checkItNumbersOnly(event)" maxlength="16"></s:textfield>       
			</div>
	  	</div>	  	
  	</span>
  	<div class="control-group">
    	<label class="control-label" for="descrImpegno">Descrizione</label>
    	<div class="controls">
     		<s:textfield id="descrImpegno" name="step1Model.oggettoImpegno" cssClass="span9" maxlength="500"></s:textfield>       
    	</div>
  	</div>
  	<div class="control-group">
    	<label class="control-label" for="importoImpegno">Importo *</label>
    	<div class="controls">
    		<!-- blocco l'importo se ci sono dei vincoli -->
    	  	<s:textfield id="importoImpegno" name="step1Model.importoFormattato" cssClass="span2 soloNumeri decimale" ></s:textfield>
      		<span class="al">
        		<label class="radio inline" for="scadenza">Scadenza </label>
      		</span>
      		<s:textfield id="scadenza" title="gg/mm/aaaa" name="step1Model.scadenza" maxlength="10" cssClass="lbTextSmall span2 datepicker"></s:textfield>
      	   	<s:if test="oggettoDaPopolareImpegno()"> 
         		<s:if test="isVisibileFlagFrazionabile()"> 
		   	    	<span class="radio guidata">
		    	   		<s:radio id="frazionabile" name="step1Model.frazionabile" cssClass="flagResidenza" list="step1Model.scelteFrazionabile"></s:radio> 
		    		</span>
	    		</s:if>
   		 	</s:if>
    	</div>
  	</div> 
    <!-- PROGETTO -->
   	<s:include value="/jsp/movgest/include/progetto.jsp" />
  	<!-- se impegno allora si vede il CIG e il CUP -->
  	<s:if test="oggettoDaPopolareImpegno()"> 
  		<div class="control-group">
	    	<span class="control-label">Tipo debito SIOPE</span>
	    	<div class="controls">    
	      		<s:radio id="tipoDebitoSiope" name="step1Model.tipoDebitoSiope" cssClass="flagResidenza" list="step1Model.scelteTipoDebitoSiope"></s:radio> 
	    	</div>
	  	</div>
    	<div class="control-group">
			<label class="control-label" for="cig"><abbr title="codice identificativo gara">CIG</abbr></label>
			<div class="controls">
				<s:textfield cssClass="lbTextSmall span2 cig" id="cig" name="step1Model.cig" maxlength="10" />
				<span id="bloccoMotivazioneAssenzaCig">
					<span class="al">
			      		<label class="radio inline" for="listaTipiImpegno">Motivazione assenza del CIG</label>
			     	</span>
			     	<s:if test="null!=listaMotivazioniAssenzaCig">
				      	<s:select list="listaMotivazioniAssenzaCig" headerKey="" headerValue="" id="listaMotivazioniAssenzaCigId"  name="step1Model.motivazioneAssenzaCig" cssClass="span5" listKey="codice" listValue="descrizione" />
			       	</s:if> 
		       	</span>
			</div>
		</div>
	  	<div class="control-group">
			<label class="control-label" for="cup"><abbr title="Centro Unificato Prenotazioni">CUP</abbr></label>
			<div class="controls">
				<s:textfield cssClass="lbTextSmall span2 cup" id="cup" name="step1Model.cup" maxlength="15" />
			</div>
	  	</div>
	</s:if>
  	<!-- SIAC-6997 -->
  	<div class="control-group">      
  		<label class="control-label">Struttura Competente *</label> 
    	<div class="controls">
    		<s:hidden name="step1Model.strutturaSelezionataCompetente" id="strutturaSelezionataCompetente" />
    		
    		<s:hidden name="step1Model.strutturaSelezionataCompetenteDesc" id="step1Model.strutturaSelezionataCompetenteDesc" />
    		
        	<s:hidden id="struttAmmOriginaleCompetente" name="%{step1Model.strutturaSelezionataCompetente}"/>
        	<div class="accordion span9" class="struttAmmCompetente">
        		<div class="accordion-group">
        			<div class="accordion-heading">    
        				<a id="lineaStruttura" class="accordion-toggle" data-parent="#struttAmmCompetente" data-toggle="collapse" href="#4n">
				        <s:if test="(step1Model.strutturaSelezionataCompetente!='' && step1Model.strutturaSelezionataCompetente != null) && (step1Model.strutturaSelezionataCompetenteDesc!='' && step1Model.strutturaSelezionataCompetenteDesc != null )" >
							<s:property value="step1Model.strutturaSelezionataCompetenteDesc"/>
						</s:if>
				        <s:else>Seleziona la Struttura Competente</s:else>
		        			<i class="icon-spin icon-refresh spinner" id="spinnerStrutturaAmministrativaCompetente"></i>
						</a>
                	</div>
        			<div id="4n" class="accordion-body collapse">
                		<div class="accordion-inner" id="strutturaAmministrativaCompetenteDiv">
                   			<ul id="strutturaAmministrativaCompetente" class="ztree treeStruttAmm"></ul>
                 		</div>
                 		<div class="accordion-inner" id="strutturaAmministrativaCompetenteWait">Attendere prego..</div>
               		</div>
            	</div>
  			</div>
    	</div>
  	</div>
  	<s:if test="!oggettoDaPopolareImpegno()"> 
  		<div class="control-group">
			<label class="control-label" for="cup">Codice verbale</label>
			<div class="controls">
				<s:textfield cssClass="lbTextSmall span2" id="codiceVerbale" name="step1Model.codiceVerbale"/>
			</div>
	  	</div>
  	</s:if>
    <div class="control-group">
  		<table class="span10 ">
  			<tr>
  				<td class="span4 ">
  					<span class="control-label">Da riaccertamento</span>
  					<div class="controls">    
			      		<s:radio id="riaccertato" name="step1Model.riaccertato" cssClass="flagResidenza" list="step1Model.daRiaccertamento"></s:radio> 
			    	</div>
  				</td>
  				<td rowspan="3" valign="middle" width="90%" class="span8 ">
  					<span class="riaccVisible" id="bloccoRiaccertato">
			   			&nbsp; <s:textfield id="annoImpRiacc" name="step1Model.annoImpRiacc" onkeyup="return checkItNumbersOnly(event)" cssClass="span2 " title="Anno" />&nbsp;
			   			<s:textfield id="numImpRiacc" cssClass="lbTextSmall span4 " onkeyup="return checkItNumbersOnly(event)" title="Numero" name="step1Model.numImpRiacc"/>
			   		</span>
  				</td>			
  			</tr>
  			<tr><td>&nbsp;</td></tr>
  			<!-- inizio SIAC-6997 -->
  			<tr>
  				<td class="span4 ">
  					<span class="control-label">Da reimputazione in corso d&rsquo;anno</span>
  					<div class="controls">
			      		<s:radio id="reanno" name="step1Model.reanno" cssClass="flagResidenza" list="step1Model.daReanno"></s:radio>
			    	</div>
  				</td>
  			</tr>
  		</table>
  	</div>	
  	<s:if test="oggettoDaPopolareImpegno()">  
	   	<div class="control-group">
	    	<span class="control-label">Prenotazione</span>
	    	<div class="controls">    
	      		<s:radio id="prenotazione" name="step1Model.prenotazione" cssClass="flagResidenza" list="step1Model.daPrenotazione"></s:radio> 
	      		<s:if test="isAbilitatoGestisciImpegno()"> 
	      	 		<span class="liqPerPrenotazioneVisibile" id="bloccoPrenotatoLiquidabile">
		     			<label class="radio inline" for="prenotatoLiquidabileCheckBox">Liquidabile</label>
		     			<s:checkbox id="prenotatoLiquidabileCheckBox" name="step1Model.prenotazioneLiquidabile" />
		      		</span>
	      		</s:if> 
	    	</div>
	  	</div>
	  	<div class="control-group">
	    	<span class="control-label">Soggetto a DURC</span>
		   	<div class="controls">    
		   		<s:radio id="soggettoDurc" name="step1Model.soggettoDurc" cssClass="flagResidenza" list="#{'Si':'Si','No':'No'}"></s:radio> 
		   	</div>
	  	</div>
	</s:if>  
  	<div class="control-group">
    	<div class="control-label"><s:property value="%{labels.OGGETTO_GENERICO}"/> di origine</div>
    	<div class="controls">
      		<s:textfield id="annoImpOrigine" name="step1Model.annoImpOrigine" title="Anno" maxlength="4" onkeyup="return checkItNumbersOnly(event)" cssClass="span1" />&nbsp;
      		<s:textfield id="numImpOrigine" cssClass="lbTextSmall span2" onkeyup="return checkItNumbersOnly(event)" title="Numero"name="step1Model.numImpOrigine"/>
    	</div>
  	</div>
  	<s:if test="oggettoDaPopolareImpegno()"> 
  		<s:if test="isImpegnoPlurAbilitato()">
        	<!--  se OP_SPE_gestisciImpegnoPluriennale allora faccio vedere i bottoni -->
		  	<div class="control-group">
		    	<span class="control-label"><s:property value="%{labels.OGGETTO_GENERICO}"/> pluriennale</span>
		    	<div class="controls">      
		      		<s:radio name="step1Model.pluriennale" id="radioPluriennale" cssClass="flagResidenza" list="step1Model.implPluriennale"></s:radio> 
			   		<span style="display: inline;" class="vis" id="bloccoPluriennali">
			      		<span class="al">
			        		<label id="numeroPluriennaliLabel" class="radio inline" for="Anni">Per anni</label>
			      		</span>
			       		<s:textfield id="numeroPluriennali" cssClass="lbTextSmall span2" onkeyup="return checkItNumbersOnly(event)" name="step1Model.numeroPluriennali"/>
			   		</span>
		    	</div>
		  	</div>
	  	</s:if>
	   	<div class="control-group">
	    	<span class="control-label">Impegno di cassa economale</span>
	    	<div class="controls">    
	      		<s:radio id="impegnoCassaEconomale" name="step1Model.cassaEconomale" cssClass="flagResidenza" list="step1Model.diCassaEconomale"></s:radio> 
	    	</div>
	  	</div>
	</s:if>
  	<s:else>
  		<s:if test="isAccertamentoPlurAbilitato()">
	 		<!--  se OP_ENT_gestisciAccertamentoPluriennale allora faccio vedere i bottoni -->
	  		<div class="control-group">
		    	<span class="control-label"><s:property value="%{labels.OGGETTO_GENERICO}"/> pluriennale</span>
		    	<div class="controls">      
		      		<s:radio name="step1Model.pluriennale" id="radioPluriennale" cssClass="flagResidenza" list="step1Model.implPluriennale"></s:radio> 
			   		<span style="display: inline;" class="vis" id="bloccoPluriennali">
			      		<span class="al">
			        		<label id="numeroPluriennaliLabel" class="radio inline" for="Anni">Per anni</label>
			      		</span>
			       		<s:textfield id="numeroPluriennali" cssClass="lbTextSmall span2" onkeyup="return checkItNumbersOnly(event)" name="step1Model.numeroPluriennali"/>
			   		</span>
		    	</div>
		  	</div>
	  	</s:if>
  	</s:else>
  	<!-- GESTIONE NUOVO FLAG PER ACCERTAMENTI (FlagFattura)  -->
  	<s:if test="!oggettoDaPopolareImpegno()"> 
    	<div class="control-group">
			<span class="control-label">E' prevista Fattura</span>
		    <div class="controls">      
		    	<s:radio name="step1Model.flagFattura" id="radioPrevistaFattura" cssClass="flagResidenza" list="#{ 'Si': 'S&igrave;', 'No' : 'No' }"></s:radio> 
		    </div>
		</div>
		<div class="control-group">
			<span class="control-label">&Egrave; previsto corrispettivo</span>
		    <div class="controls">      
		    	<s:radio name="step1Model.flagCorrispettivo" id="radioPrevistoCorrispettivo" cssClass="flagResidenza" list="#{ 'Si': 'S&igrave;', 'No' : 'No' }"></s:radio>
		    </div>
		</div>
	</s:if>
</fieldset>