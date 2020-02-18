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
                    
 
  <hr />
<div class="container-fluid-banner">

  <a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->

<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">    
      <s:form id="%{labels.FORM}" action="%{labels.FORM}.do" method="post" class="form-horizontal">
       	<h3><s:property value="%{labels.TITOLO}"/></h3>
       	<div id="MyWizard" class="wizard">
          <ul class="steps">
            <li data-target="#step1" class="active"><span class="badge">1</span><s:property value="%{labels.STEP1}"/><span class="chevron"></span></li>
            <li data-target="#step2"><span class="badge">2</span>Classificazioni<span class="chevron"></span></li>
            <li data-target="#step3"><span class="badge">3</span><s:property value="%{labels.STEP3}"/><span class="chevron"></span></li>
          </ul>
        </div>
       
        <s:include value="/jsp/include/actionMessagesErrors.jsp" />
       
        <div class="step-content">
          <div class="step-pane active" id="step1">    
            
            <div id="refreshHeaderCapitolo">
            	<s:include value="/jsp/movgest/include/headerCapitolo.jsp"/>
            </div>
            <s:include value="/jsp/movgest/include/capitolo.jsp" />                                         
            <s:include value="/jsp/movgest/include/provvedimento.jsp" />
            <div id="refreshHeaderSoggetto">
            	<s:include value="/jsp/movgest/include/headerSoggetto.jsp"/>
            </div>
            <s:include value="/jsp/movgest/include/soggetto.jsp" />              
                <!--     TABELLE       RIEPILOGO   con azioni -->              
            <s:include value="/jsp/movgest/include/datiEntita.jsp" />
            
            <!-- Vincolo -->
            <s:if test="oggettoDaPopolareImpegno()"> 
             	<a id="ancoraVincoli"></a>
             	<s:include value="/jsp/movgest/include/tabVincolo.jsp" /> 
            </s:if> 
            <!-- fine Vincolo -->
             
            <!-- qua c'era l'accertamento collegato -->
            <!-- Modal --> 
            <s:include value="/jsp/movgest/include/modal.jsp" /> 
           
            <s:include value="/jsp/movgest/include/modalAccVincoli.jsp" /> 
            
            <!--modale progetto -->
			<s:include value="/jsp/movgest/include/modalProgettoCronop.jsp"/>	
			<!--/modale progetto -->
            
			<s:include value="/jsp/include/modalCronop.jsp"/>	
            
            
            <!-- Fine Modal -->
            <br/> <br/>    
              <!-- questi pulsanti servono se siamo in pluriennale -->           
           
           
            <p>           
            
           	 <s:include value="/jsp/include/indietro.jsp" /> 
           
           
            <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
           			<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
					
					<%-- CR-2023 da eliminare 
					<s:hidden id="ricaricaAlberoContoEconomico" name="teSupport.ricaricaAlberoContoEconomico"/> 
					--%>
					
					<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
					<s:hidden id="ricaricaSiopeSpesa" name="teSupport.ricaricaSiopeSpesa"/>
		   <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->

             
            <s:hidden id="impegnoRiaccGiaRicercata" name="impegnoRiaccGiaRicercata"></s:hidden>
         
             <s:hidden id="doveMiTrovo" name="doveMiTrovo" value="Inserimento "></s:hidden>
             <s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden>
             
             
           <!-- ANNO BILANCIO CORRENTE -->
           <s:hidden id="annoEsercizio" name="sessionHandler.annoEsercizio"/>
           
           <s:hidden id="bilPrecInPredispConsuntivo" name="bilancioPrecedenteInPredisposizioneConsuntivo"/>
           <s:hidden id="bilAttualeInPredispConsuntivo" name="bilancioAttualeInPredisposizioneConsuntivo"/>
           
           <s:hidden id="hiddenPerPrenotazioneLiquidabile" name="step1Model.hiddenPerPrenotazioneLiquidabile" />
           
            <s:submit method="annulla" value="annulla" cssClass="btn btn-secondary" id="annullaBtn"></s:submit>
                    
             <s:submit name="prosegui" id="prosegui"  value="prosegui" method="prosegui" cssClass="btn btn-primary pull-right" />
             	
           <%--  <s:hidden id="checkProsegui" name="step1Model.capitolo.numCapitolo"></s:hidden>	 --%>
          
        	<a id="linkMsgDatipersi" href="#msgControlloProsegui" data-toggle="modal" style="display:none;"></a>
             
             </p> 
                   
          </div>
        </div>
        
      </s:form>
    </div>
  </div>	 
</div>	


<script type="text/javascript">

		var tipoDebitoSiopeVar = $("input[name='step1Model.tipoDebitoSiope']");
		<s:include value="/jsp/include/toggleAssenzaCig.jsp" />


	<s:if test="step1Model.checkproseguiRiacc || step1Model.checkproseguiOrigin">
		$("#linkMsgDatipersi").click();
	</s:if>
	


	<!-- apre il tab vincoli -->
	<s:if test="step1Model.apriTabVincoli">
		$("#hrefTabVincolo").click();
	</s:if>
	<!-- apre il form di inserimento vincolo -->
	<s:if test="step1Model.inserisciVincoloBtn">
		$("#inserisciVincoloBtn").click();
	</s:if>
	
	<s:if test="step1Model.portaAdAltezzaVincoli">
		spostaLAncora('ancoraVincoli');
	</s:if>
	
		
</script>
 
<script src="/siacfinapp/js/local/movgest/inserisceImpegno.js" type="text/javascript"></script>
<script src="/siacfinapp/js/local/include/modalRicercaCronop.js" type="text/javascript"></script>

   
<s:include value="/jsp/include/footer.jsp" />
 