<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>


    <%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />
    
    <%-- Inclusione JavaScript NUOVO --%>
    <s:include value="/jsp/include/javascript.jsp" />	
   	<s:include value="/jsp/include/javascriptTree.jsp" />
  </head>

  <body>
  	
  <s:include value="/jsp/include/header.jsp" />
  
  
  <style>
  #abbinas .checkboxLabel{
    margin-left: 25px;
    position: relative;
    top: -16px;
	}
  
  </style>

<s:set var="proseguiSalvaAction" value="%{'inserisciMovSpesaImporto_proseguiSalva'}" />			    
  
<s:set var="gestisciForwardAction" value="%{'inserisciMovSpesaImporto_gestisciForward'}" />			    
<s:set var="siSalvaAction" value="%{'inserisciMovSpesaImporto_siSalva'}" />	 
<s:set var="siProseguiAction" value="%{'inserisciMovSpesaImporto_siProsegui'}" />	
<s:set var="annullaSubImpegnoAction" value="%{'inserisciMovSpesaImporto_annullaSubImpegno'}" />	 
<s:set var="annullaSubAccertamentoAction" value="%{'inserisciMovSpesaImporto_annullaSubAccertamento'}" />	 
<s:set var="annullaMovGestSpesaAction" value="%{'inserisciMovSpesaImporto_annullaMovGestSpesa'}" />	 
<s:set var="eliminaSubImpegnoAction" value="%{'inserisciMovSpesaImporto_eliminaSubImpegno'}" />	 
<s:set var="eliminaSubAccertamentoAction" value="%{'inserisciMovSpesaImporto_eliminaSubAccertamento'}" />
<s:set var="forzaProseguiAction" value="%{'inserisciMovSpesaImporto_forzaProsegui'}" />	          
<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'inserisciMovSpesaImporto_forzaSalvaPluriennaleAccertamento'}" />	          
<s:set var="salvaConByPassDodicesimiAction" value="%{'inserisciMovSpesaImporto_salvaConByPassDodicesimi'}" />	          
  
<div class="container-fluid-banner">




<a name="A-contenuti" title="A-contenuti"></a>
</div>
<%--corpo pagina--%>
<%--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> --%>


<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">
      <%-- SIAC-7952 rimuovo .do dalla action --%>
      <s:form id="mainForm" method="post" action="inserisciMovSpesaImporto" cssClass="form-horizontal">
       
        <div id="msgControlloSiProsegui" class="modal hide fade" tabindex="-1" role="dialog"  aria-hidden="true">
			<div class="modal-body">
				<div class="alert alert-error">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
    				<p><strong>Attenzione!</strong></p>
    				<s:actionerror/>
				</div>
			</div>
			<div class="modal-footer">
				<%-- <button id="noPluri" class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button> --%>
				<!-- task-131 <s:submit id="annullaProseguiBtn" name="btnAnnullaProsegui" method="noProsegui" value="no" cssClass="btn btn-secondary" /> -->
				<s:submit id="annullaProseguiBtn" name="btnAnnullaProsegui" action="inserisciMovSpesaImporto_noProsegui" value="no" cssClass="btn btn-secondary" />
				<%-- <button class="btn" data-dismiss="modal" aria-hidden="true">si, prosegui</button> --%>
				<!-- task-131 <s:submit id="proseguiBtn" name="btnProsegui" method="siProsegui" value="si" cssClass="btn btn-primary" /> -->
				<s:submit id="proseguiBtn" name="btnProsegui" action="inserisciMovSpesaImporto_siProsegui" value="si" cssClass="btn btn-primary" />
			</div>
		</div>
       
       
<%--   	<h3>Impegno ${model.movimentoSpesaModel.impegno.annoMovimento}/${model.movimentoSpesaModel.impegno.numero} - ${model.movimentoSpesaModel.impegno.descrizione} - <s:property value="getText('struts.money.format', {movimentoSpesaModel.impegno.importoAttuale})"/></h3>	--%>

		<h3><s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
       
       	<s:include value="/jsp/movgest/include/tabModificaSpesa.jsp" />
       	<s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
			
        <%-- SIAC-7630 --%>
		<s:include value="/jsp/include/actionMessagesErrors.jsp" />   
<%-- 		<s:if test="hasActionErrors()">
		Messaggio di ERROR
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
					<ul>
						<s:actionerror />
					</ul>
			</div>
		</s:if>
		<s:if test="hasActionMessages()">
		Messaggi dalla response
			<div class="alert alert-success">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<ul>
					<s:actionmessage />
				</ul>
			</div>
		</s:if>        
		
		<s:if test="hasActionWarnings()">
			Messaggio di WARNING
			<div class="alert alert-warning">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
				   <s:iterator value="actionWarnings">
				       <s:property escapeHtml="false"/><br>
				   </s:iterator>
					
				</ul>
			</div>
		</s:if> --%>

        <h4>Inserimento modifica </h4> 
        <div class="step-content">
          <div class="step-pane active" id="step1">
           <fieldset class="form-horizontal margin-large">
                    <div class="control-group">
                      <%--label class="control-label" for="subimpegno">Subimpegno</label--%>
                      <label class="control-label" for="subimpegno">Abbina a</label>
                      <div class="controls">
                        <div class="radio inline" style="width: 617px;">
                        	<s:radio id="competenze" cssClass="flagCompetenze" name="tipoImpegno" list="tipoImpegnoModificaImportoList"/>  
						</div>   
                      </div>
                    </div>
                    <div id="subDiv" class="control-group">
                    	<label class="control-label" for="subimpegno">Seleziona Sub Impegno</label>
                  	  	<div class="controls">
							<s:select list="numeroSubImpegnoList" name="subSelected" id="numeroSubImpegnoList" headerKey="" headerValue="" onchange="javascript:document.location.href='inserisciMovSpesaImporto_caricaDatiSub.do?tipoImpegno=SubImpegno&subSelected='+this.value+'&abbinaChk='+abbina.checked" />
							<span id="abbinas" class="radio inline" style="width: 160px;" >
								<s:checkboxlist id="abbina" cssClass="flagAbbina" name="abbina" list="abbinaList"/>
							</span>                     
                   	  	</div>
                   	</div>
                    <div id="infosImp" class="control-group datiVisibili1">              
                      <div class="controls">              
                        <dl class="dl-horizontal">
                          <dt>Importo Impegno</dt>
                          <s:if test="!model.flagSuperioreTreAnni">
                         	 <%--<dd>Modifica Impegno compresa tra <s:property value="model.minImportoCalcolatoMod" /> e <s:property value="model.maxImportoCalcolatoMod" /></dd>--%>
							 <!--SIAC-7349--> 
							  <dd>Modifica Impegno compresa tra <s:property value="model.minImportoImpComp" /> e <s:property value="model.maxImportoImpComp" /> - <b>Disponibilit&agrave; complessiva capitolo:</b> <s:property value="model.maxImportoCalcolatoMod" /></dd>
						  </s:if>
                          <s:else>
							<%--<dd>Modifica Impegno non inferiore a <s:property value="model.minImportoCalcolatoMod" /></dd>--%>
							<!--SIAC-7349--> 
							<dd>Modifica Impegno non inferiore a <s:property value="model.minImportoImpComp" /></dd>
						  </s:else>
                                  
                        </dl>             
                      </div>
                    </div>
                    
                    <s:if test="model.subImpegnoSelectedMod">
                   		<div id="subInfo" class="control-group datiVisibili2">
                   	   		<div class="controls">             
                        		<dl class="dl-horizontal">
                          			<dt style="width:220px;">Codice Sub Impegno &nbsp;</dt>
                          			<dd style="margin-left:200px;"> <s:property value="model.numeroSubImpegnoMod" /></dd>
                          			<dt style="width:220px;">Importo Attuale Sub Impegno &nbsp;</dt>
                          			<dd style="margin-left:200px;"> <s:property value="model.importoAttualeSubImpegnoMod" /></dd>
                          			<dt>Importo</dt>
                          			<dd>Modifica Sub Impegno compresa tra <s:property value="model.minImportoSubCalcolatoMod" /> e <s:property value="model.maxImportoSubCalcolatoMod" /></dd>
                        		</dl>             
                      		</div>
                    	</div> 
                    </s:if>  
                    
                    <s:set var="consultaModificheProvvedimentoAction" value="%{'inserisciMovSpesaImporto_consultaModificheProvvedimento'}" />
					<s:set var="consultaModificheProvvedimentoSubAction" value="%{'inserisciMovSpesaImporto_consultaModificheProvvedimentoSub'}" />
                      
<%--                     <s:include value="/jsp/movgest/include/provvedimentoAggiorna.jsp" />  --%>
 						 <s:include value="/jsp/movgest/include/provvedimento.jsp" />
 					<%-- SIAC-7976 --%>
 					<s:if test="movimentoSpesaModel.abilitaGestioneAggiudicazione">
	                    <div class="control-group hide" id="modificaMotivoAggiudicazione">
	                      <label class="control-label" for="motivo">Modifica motivo *</label>
	                      <div class="controls">
	                      <%-- <select id="listaTipoMotivoAgg" name="idTipoMotivo" >
	                      	<option value=""></option>
							<s:iterator value="model.movimentoSpesaModel.listaTipoMotivoAggiudicazione" var="mot">
								<option value="<s:property value="#mot.codice"/>" <s:if test="%{ idTipoMotivo == #mot.codice}">selected</s:if> >
									<s:property value="#mot.descrizione"/>
								</option>
							</s:iterator>
							</select> --%>
	                        <s:select list="model.movimentoSpesaModel.listaTipoMotivoAggiudicazione" id="listaTipoMotivoAgg" headerKey="" 
	          		   				headerValue="" name="idTipoMotivo" disabled="true" listKey="codice" listValue="descrizione"  cssClass="span9"/>
	                      </div>
	                    </div>
 					</s:if>
 					<div class="control-group" id="modificaMotivoGenerico">
	                    
	                      <label class="control-label" for="motivo">Modifica motivo *</label>
	                      <div class="controls">
	                      <%-- <select id="listaTipoMotivo" name="idTipoMotivo" >
	                      	<option value=""></option>
							<s:iterator value="model.movimentoSpesaModel.listaTipoMotivo" var="mot">
								<option value="<s:property value="#mot.codice"/>" <s:if test="%{ idTipoMotivo == #mot.codice}">selected</s:if> >
									<s:property value="#mot.descrizione"/>
								</option>
							</s:iterator>
							</select> --%>
	                        <s:select list="model.movimentoSpesaModel.listaTipoMotivo" id="listaTipoMotivo" headerKey="" 
	          		   				headerValue="" name="idTipoMotivo" listKey="codice" listValue="descrizione"  cssClass="span9"/>
	                      </div>
	                    </div>
                    
                    <div class="control-group">
                      <label class="control-label" for="Descrizione">Modifica descrizione <span class="hide" id="modificaDescrizioneObbligatoria"> * </span></label>
                      <div class="controls">
                      	<%-- SIAC-8506 --%>
                        <s:textfield name="descrizione" id="descrizione" cssClass="span9" maxlength="500" />  
                      </div>
                    </div>
					<div class="control-group anniPluriennale">
                      <label class="control-label" for="motivo">Anni di pluriennale</label>
                      <div class="controls">
                      	<s:textfield id="anniPluriennali" onkeyup="return checkItNumbersOnly(event)" cssClass="span1" name="anniPluriennali" />
                      </div>
                    </div>
                    
                    <div class="control-group">
                      <label class="control-label" for="importo">Importo </label>
                      <div class="controls">
                      	<s:textfield name="importoImpegnoModImp" id="importoImpegnoModImp" cssClass="span2 soloNumeri decimale" /> 
                      			   <span id="imp">
                      			   				<s:if test="!model.flagSuperioreTreAnni">
													<%--Modifica compresa tra <s:property value="model.minImpMod" /> e <s:property value="model.maxImpMod" /> --%>
													<!--SIAC-7349-->
													Modifica compresa tra <s:property value="model.minImportoImpComp" /> e <s:property value="model.maxImportoImpComp" /> - <b>Disponibilit&agrave; complessiva capitolo:</b> <s:property value="model.maxImpMod" />  
												</s:if>
                      			                <s:else>
													<%--Modifica Impegno non inferiore a <s:property value="model.minImpMod" /> --%>
													<!--SIAC-7349-->
													Modifica Impegno non inferiore a <s:property value="model.minImportoImpComp" />
							           			</s:else>      
                      			   </span>           
								   <span id="sub">
								       Modifica compresa tra <s:property value="model.minSubMod" /> e <s:property value="model.maxSubMod" /> 
								   </span> 
								   <span id="anche">
							   		   Modifica compresa tra <s:property value="model.minAncheMod" /> e <s:property value="model.maxAncheMod" /> 
							   	</span>
							   	<span id="HIDDEN_importoRORM"></span>
                      </div>
                    </div>
                    
                    <div class="control-group">    
                      <label class="control-label" for="reimputazione">Reimputazione </label>
                      <div class="controls">
					      <s:radio id="radioReimputato" name="model.movimentoSpesaModel.reimputazione" cssClass="flagResidenza" list="model.movimentoSpesaModel.daReimputazione"></s:radio> 
					      <span class="riaccVisible" id="bloccoReimputato">
					      &nbsp; Anno reimputazione:
					      &nbsp; <s:textfield id="annoReimp" name="model.movimentoSpesaModel.annoReimputazione" onkeyup="return checkItNumbersOnly(event)" cssClass="span1 soloNumeri" />&nbsp;
					      &nbsp; Elaborato ROR - Reimp. in corso d&lsquo;anno
					      &nbsp; <s:radio id="radioElaboratoRor" disabled="true" name="model.movimentoSpesaModel.elaboratoRor" cssClass="flagResidenza" list="model.movimentoSpesaModel.daElaboratoRor"></s:radio>
				         </span>
                      </div>
                   </div>
                   <s:if test="%{movimentoSpesaModel.abilitaGestioneAggiudicazione}">
                   	<span class="nascosto" id="hidden_motivo_agg" ></span>	
	                   <div class="control-group">    
	                      <label class="control-label" for="flagAggiudicazione">Riduzione e contestuale impegno </label>
	                      <div class="controls">
	                      		<s:checkbox id="flagAggiudicazione" name="model.movimentoSpesaModel.aggiudicazione"/>
	                      </div>
	                   </div> 
	                   <div id="campiAggiudicazione" class="hide">
		                   <s:if test="%{model.movimentoSpesaModel.soggettoAggiudicazioneModel != null}">
		                   		<h4> <s:property value="model.movimentoSpesaModel.intestazioneSoggettoAggiudicazioneSelezionato"/></h4>
	                     	</s:if>   
		                   <div class="control-group">
		                   		<label class="control-label" for="codice">Codice</label>
		                      	<div class="controls">
		                      		<s:textfield id="codCreditore" name="model.movimentoSpesaModel.soggettoAggiudicazioneModel.codCreditore" cssClass="span2"/>
		      						<span class="al">
		         						<label class="radio inline" for="classe">Classe</label>
		         					</span>
		             				<s:select list="listaClasseSoggetto" id="listaClasseSoggetto"  headerKey="" 
		          		   				headerValue="" name="model.movimentoSpesaModel.soggettoAggiudicazioneModel.idClasse" cssClass="span5"  
		       	 	       				listKey="id" listValue="codice+' - '+descrizione" /> 
		       	 	       			<span class="al">
		         						<label class="radio inline" for="flagSenzaSoggetto">Senza soggetto</label>
		         					</span>
		             				<s:checkbox id="flagSenzaSoggetto" name="model.movimentoSpesaModel.flagAggiudicazioneSenzaSoggetto"/>
		       	 	       			<span class="radio guidata"><a id="linkCompilazioneGuidataSoggetto" href="#guidaSog" data-toggle="modal" class="btn btn-primary">compilazione guidata</a></span>
		                 		</div>
	                     	</div> 
	                     	<!-- SIAC-8184 -->
		                   <div class="control-group">
		                   		<label class="control-label" for="nuovaDescrizioneEventuale">Eventuale Nuova Descrizione impegno</label>
		                      	<div class="controls">
		                      		<%-- SIAC-8516 --%>
		                      		<s:textfield id="nuovaDescrizione" name="model.movimentoSpesaModel.nuovaDescrizioneEventualeImpegnoAggiudicazione" cssClass="span8" maxlength="500" />
		                 		</div>
	                     	</div> 
	                     	<!-- SIAC-8811 -->
	                     	<div class="control-group">
		                   		<label class="control-label" for="EventualeNuovoCUPImpegno">Eventuale Nuovo CUP Impegno</label>
		                      	<div class="controls">
		                      		<s:textfield id="EventualeNuovoCUPImpegno" name="model.movimentoSpesaModel.EventualeNuovoCUPImpegno" cssClass="span8" maxlength="500" />
		                 		</div>
	                     	</div> 
	                   	</div>   
                    </s:if>
                   
                    
                  </fieldset>
                  <!--p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p-->			          
                                   
            <!--#include virtual="include/creditore.html" -->
            <s:set var="selezionaProvvedimentoAction" value="%{'inserisciMovSpesaImporto_selezionaProvvedimento'}" />
            <s:set var="clearRicercaProvvedimentoAction" value="%{'inserisciMovSpesaImporto_clearRicercaProvvedimento'}" />	          
        	<s:set var="ricercaProvvedimentoAction" value="%{'inserisciMovSpesaImporto_ricercaProvvedimento'}" />	          
            <s:set var="eliminaAction" value="%{'inserisciMovSpesaImporto_elimina'}" />	  
            <s:set var="selezionaProvvedimentoInseritoAction" value="%{'inserisciMovSpesaImporto_selezionaProvvedimentoInserito'}" />	
			<s:set var="inserisciProvvedimentoAction" value="%{'inserisciMovSpesaImporto_inserisciProvvedimento'}" />	
			<s:set var="clearInserimentoProvvedimentoAction" value="%{'inserisciMovSpesaImporto_clearInserimentoProvvedimento'}" />
	            
			<!--  per soggetto -->
			<s:set var="selezionaSoggettoAction" value="%{'inserisciMovSpesaImporto_selezionaSoggetto'}" />
			<s:set var="pulisciRicercaSoggettoAction" value="%{'inserisciMovSpesaImporto_pulisciRicercaSoggetto'}" />	          
			<s:set var="ricercaSoggettoAction" value="%{'inserisciMovSpesaImporto_ricercaSoggetto'}" />	    
			<s:set var="listaClasseSoggettoChangedAction" value="%{'inserisciMovSpesaImporto_listaClasseSoggettoChanged'}" />
	           
            <s:include value="/jsp/movgest/include/modal.jsp" /> 
            
            <s:include value="/jsp/movgest/include/modalSalvaSdf.jsp" /> 
            
            <!-- Fine Modal --> 
                                   
            <br/> <br/>                                         
            
            <s:include value="/jsp/movgest/include/pulsantiSalvaModMovImportoUscita.jsp" />
            
         	<!-- DODICESIMI: -->
			<a id="linkVisualizzaModaleConfermaSalvaConBypassDodicesimi" href="#modalSalvaConBypassDodicesimi" data-toggle="modal" style="display:none;"></a>
              
              
          </div>
        </div>
      </s:form>
    </div>
  </div>	 
</div>	

<!-- task-131 <s:url method="importoModificaChanged" var="urlImporto"></s:url> -->
<s:url action="inserisciMovSpesaImporto_importoModificaChanged" var="urlImporto"></s:url>


<s:hidden id="HIDDEN_urlImporto" value="%{urlImporto}"/>
<script type="text/javascript" src="${jspath}movgest/inserisciModificaSpesaImporto.js"></script>

 <script type="text/javascript">
 
 
	<s:if test="step1Model.checkproseguiMovimentoSpesa">
		$("#linkMsgControlloProsegui").click();
	</s:if>
	
	<s:if test="isShowModaleConfermaSalvaConBypassDodicesimi()">
		$("#linkVisualizzaModaleConfermaSalvaConBypassDodicesimi").click();
	</s:if>

     
</script>
<s:include value="/jsp/include/footer.jsp" />