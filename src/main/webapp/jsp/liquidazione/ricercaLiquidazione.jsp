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
  
<div class="container-fluid-banner">


<a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->




<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 contentPage">
    
			<s:form method="post" action="ricercaLiquidazione.do" id="ricercaLiquidazione">
				<s:include value="/jsp/include/actionMessagesErrors.jsp" />
			
			
				<h3>Ricerca liquidazione</h3>
					
					<div class="step-content">	
					<div class="step-pane active" id="step1"> 
					<p class="margin-medium"><s:submit id="cerca" name="cerca" value="cerca" method="ricercaLiquidazioni" cssClass="btn btn-primary pull-right" /> </p>						
					<br>
					<h4 class="step-pane">Liquidazione</h4>
					
					<fieldset class="form-horizontal">
						<div class="control-group">
							<label class="control-label" for="anno">Anno *</label>
							<div class="controls">
								 <s:textfield id="annoEsercizio" onkeyup="return checkItNumbersOnly(event)" name="model.annoLiquidazione" cssClass="span1" />
								<span class="al">
									<label for="Numero" class="radio inline">Numero</label>
								</span>
								<s:textfield id="numeroLiquidazione" onkeyup="return checkItNumbersOnly(event)" name="model.numeroLiquidazioneString" cssClass="lbTextSmall span1"/>
							</div>
						</div>
						
						<div class="control-group">
							<div class="control-label">Escludi annullati</div>
							<div class="controls">
			   						<s:checkbox id="escludiAnnullatiCheckBox" name="model.escludiAnnullati" onclick="impostaValoreEscludiAnnullati()"/>  		   
							</div>
						</div>
						
						<h4 class="step-pane">Mutuo</h4>
						<div class="control-group">
							<label class="control-label" for="mutuo">Numero mutuo</label>
							<div class="controls">
								<s:textfield id="numeroMutuo" onkeyup="return checkItNumbersOnly(event)" name="model.numeroMutuoString" cssClass="lbTextSmall span1"/>
							</div>
						</div>								
					</fieldset>
			
			<s:include value="/jsp/liquidazione/include/impegnoric.jsp" />	
			<div id="refreshHeaderCapitolo">
            	<s:include value="/jsp/liquidazione/include/headerCapitoloLiquidazione.jsp"/>
            </div>
            <s:include value="/jsp/liquidazione/include/capitoloLiquidazione.jsp" />
            <s:include value="/jsp/liquidazione/include/provvedimentoLiquidazione.jsp" />   
            <!--  memorizza l'uid della sac selezionata nel tree dei provvedimenti -->
            <s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden> 
             
            <div id="refreshHeaderSoggetto">
            	<s:include value="/jsp/liquidazione/include/headerSoggettoLiquidazione.jsp"/>
            </div>
            <s:include value="/jsp/liquidazione/include/soggettoLiquidazione.jsp" />  
			<!-- Modal -->
                     
            <s:include value="/jsp/include/modalProvvedimenti.jsp" />
            <s:include value="/jsp/include/modalSoggetto.jsp" />
            <s:include value="/jsp/include/modalCapitolo.jsp" />
            
             <div id="capitoloTab" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="capitoloTabLabel" aria-hidden="true">
              		<div class="modal-header">
		              <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
		              <h3 id="myModalLabel2">Dettagli del capitolo</h3>
		              </div>
		              <div class="modal-body">
		               
					   <dl class="dl-horizontal">
					     <dt>Numero</dt>
					      <dd><s:property value="capitolo.anno"/> / <s:property value="capitolo.numCapitolo"/> / <s:property value="capitolo.articolo"/> / <s:property value="capitolo.ueb"/> - <s:property value="capitolo.descrizione" /> - <s:property value="capitolo.descrizioneStrutturaAmministrativa" />&nbsp;</dd>
					      <dt>Tipo finanziamento</dt>
					      <dd><s:property value="capitolo.tipoFinanziamento" />&nbsp;</dd>
					      <dt>Piano dei conti finanziario</dt>
					      <dd><s:property value="capitolo.descrizionePdcFinanziario" default=" "/>&nbsp;</dd>
					    </dl>
						<table class="table table-hover table-bordered">
					      <tr>
					        <th>&nbsp;</th>
					        <s:iterator value="datoPerVisualizza.importiCapitolo">
								<th scope="col" class="text-center"><s:property value="annoCompetenza" /></th>
							</s:iterator>
					      </tr>
					      <tr>
					        <th>Stanziamento</th>
					        <s:iterator value="datoPerVisualizza.importiCapitolo">
								<td><s:property value="getText('struts.money.format', {competenza})" /></td>
							</s:iterator>       
					      </tr>
					      <tr>
					        <th>Disponibile</th>

								<td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloUG[0].disponibilitaImpegnareAnno1})" /></td>
								<td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloUG[0].disponibilitaImpegnareAnno2})" /></td>
							    <td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloUG[0].disponibilitaImpegnareAnno3})" /></td>
					      </tr>
					    </table>
							                
		                
		
		              </div>
		            </div>  
	        <s:include value="/jsp/include/modalImpegno.jsp" />
                 
            <!-- Fine Modal -->
            <br/> <br/> 
            
            <p>           
            
           <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
           			<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
					<%-- CR 2023 si elimina il conto economico
					<s:hidden id="ricaricaAlberoContoEconomico" name="teSupport.ricaricaAlberoContoEconomico"/> 
					--%>
					<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
					<s:hidden id="ricaricaSiopeSpesa" name="teSupport.ricaricaSiopeSpesa"/>
		   <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
		   
		    <s:hidden id="hiddenPerEscludiAnnullati" name="model.hiddenPerEscludiAnnullati" />
            
            <s:include value="/jsp/include/indietro.jsp" /> 
            
			<s:submit name="annulla" value="annulla" method="annullaRicercaLiquidazioni" cssClass="btn btn-secondary" />
			<s:submit id="cerca2" name="cerca2" value="cerca" method="ricercaLiquidazioni" cssClass="btn btn-primary pull-right" />
				</p>     
  

      </s:form>
    </div>
</div>	 
</div>	


<script src="/siacfinapp/js/local/liquidazione/ricercaLiquidazione.js" type="text/javascript"></script>



<s:include value="/jsp/include/footer.jsp" />

