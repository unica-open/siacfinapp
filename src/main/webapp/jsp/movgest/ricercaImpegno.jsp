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
    <div class="span12 ">  
      <div class="contentPage"> 
        <s:form method="post" action="ricercaImpegno.do" id="ricercaImpegno">
            <s:include value="/jsp/include/actionMessagesErrors.jsp" />
          
          <h3>Ricerca Impegno</h3>
          <p>&Egrave; necessario inserire almeno un criterio di ricerca.</p>  
         
         <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
         
					<s:hidden id="idPianoDeiContiCapitolo" name="teSupport.pianoDeiConti.uid"/>
					<s:hidden id="codicePianoDeiContiCapitolo" name="teSupport.pianoDeiConti.codice"/>
					<s:hidden id="descrizionePianoDeiContiCapitolo" name="teSupport.pianoDeiConti.descrizione"/>
					<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
					<%-- CR-2023 da eliminare 
					<s:hidden id="ricaricaAlberoContoEconomico" name="teSupport.ricaricaAlberoContoEconomico"/> 
					--%>
					<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
					<s:hidden id="ricaricaSiopeSpesa" name="teSupport.ricaricaSiopeSpesa"/>

					<s:hidden id="daRicerca"
						name="step1Model.daRicerca" />				
         <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
          
          <div class="step-content">
         <div class="step-pane active" id="step1"> <br>
          <p>
          	<s:submit name="cerca" value="cerca" method="ricercaImpegni" cssClass="btn btn-primary pull-right" />
          </p><br>
           <h4>Impegno</h4>
          
          <fieldset class="form-horizontal">  
            <div class="control-group">
              <label class="control-label" for="annoEsercizio">Anno</label>
              <div class="controls">  
                <s:textfield id="annoMovimento" onkeyup="return checkItNumbersOnly(event)" name="model.ricercaModel.annoMovimento" cssClass="span1" />
                <span class="al">
                  <label class="radio inline" onkeyup="return checkItNumbersOnly(event)" for="model.ricercaModel.numeroImpegno">Numero </label>
                </span>
                <s:textfield id="numeroImpegno" onkeyup="return checkItNumbersOnly(event)" name="model.ricercaModel.numeroImpegno" cssClass="lbTextSmall span1"/> 
                <span class="al">
                  <label class="radio inline" for="stato">Stato</label>
                </span>
                <s:select list="listaStatoOperativoMovgest" id="listaStatoOperativoMovgest"  headerKey="" 
          		   headerValue="" name="model.ricercaModel.idStatoOperativoMovgest" cssClass="span2"  
       	 	       listKey="codice" listValue="descrizione" /> 
              </div>
            </div>
            
            <div class="control-group">
				<div class="control-label">Escludi annullati</div>
				<div class="controls">
   						<s:checkbox id="escludiAnnullatiCheckBox" name="model.ricercaModel.escludiAnnullati" onclick="impostaValoreEscludiAnnullati()"/>  		   
				</div>
			</div>
            
	      <%-- <div class="control-group">
	      <label for="pdc" class="control-label"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario </label>
          <div class="controls">
            
           <s:property value="teSupport.pianoDeiConti.descrizione"/>&nbsp;&nbsp;&nbsp;<a href="#myModal" role="button" class="btn btn-primary" data-toggle="modal">seleziona il Piano dei Conti</a>
            
            <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
                <h3 id="myModalLabel">Piano dei Conti</h3>
              </div> --%>
<!--               <div class="modal-body"> -->
<!--                 <ul id="elementoPdcRicercaImpegno" class="ztree"></ul> -->
<!--               </div> -->
              
              
               <!-- ALBERO VISUALIZZATO -->
				<!-- <div class="modal-body" id="elementoPdcRicercaImpegnoDiv">
					<ul id="elementoPdcRicercaImpegno" class="ztree"></ul>
				</div> -->
				
				 <!-- ALBERO IN ATTESA -->
				<%-- <div class="modal-body" id="elementoPdcRicercaImpegnoWait">
					
					    Attendere prego..
	
					
				</div>
              
              
              <div class="modal-footer">
              		<s:submit value="conferma" name="conferma" method="confermaPdc" cssClass="btn btn-primary" data-dismiss="modal" aria-hidden="true"></s:submit>
              </div>              
            </div>
            
          </div>
        </div> --%>
            <div class="control-group">
             <label class="control-label" for="piano">Competenza</label>
             	<div class="controls">					        
             	
                	<div class="radio inline">
			    		<s:radio id="competenze" cssClass="flagSesso" name="model.ricercaModel.competenze" list="competenzeList"/>
                	</div>
             				                                     
             </div>
            </div>
            <div class="control-group">
              <label class="control-label" for="cup">CUP</label>
              <div class="controls">					
              	<s:textfield name="model.ricercaModel.cup" id="cup" cssClass="lbTextSmall span2" />        
                <span class="cig">
                <label class="radio inline" for="cig">CIG</label>  
                </span>
                <s:textfield name="model.ricercaModel.cig" id="cig" cssClass="lbTextSmall span2" />      
                
                <!--  
                <span class="cig">
                	<label class="radio inline" for="progetto">Progetto</label>  
                </span>
                <s:textfield name="model.ricercaModel.progetto" id="progetto" cssClass="lbTextSmall span2" />    
                -->
                
                                  
              </div>
            </div>
            
            <div class="control-group">
    			<span class="control-label">Da riaccertamento</span>
    			<div class="controls">    
  				<!--CHIEDERE COME GESTIRE QUESTI RADIO  -->
      				<s:radio id="riaccertato" name="step1Model.riaccertato" cssClass="flagResidenza pb-1" list="step1Model.daRiaccertamento" onclick="changeRiacc();"></s:radio> 

      				<!-- CAMPI VISIBILI SE RADIOBUTTON SI=CHECKED -->       
      				<span class="riaccVisible">
      					&nbsp; <s:textfield onkeyup="return checkItNumbersOnly(event)" id="annoImpRiacc" name="model.ricercaModel.annoImpRiacc" cssClass="span1 " title="Anno" />&nbsp;
      					<s:textfield onkeyup="return checkItNumbersOnly(event)" id="numImpRiacc" cssClass="lbTextSmall span2 " title="Numero" name="model.ricercaModel.numeroImpRiacc"/>
     			 	</span>
     		 	<!-- FINE CAMPI VISIBILI -->      
   			 </div>
  			</div>
  			
            <div class="control-group">
            	<label class="control-label" >Impegno di origine</label>
              	<div class="controls">	
              	<s:textfield id="annoImpOrigine" onkeyup="return checkItNumbersOnly(event)" name="model.ricercaModel.annoImpOrigine" maxLength="4" cssClass="span1" title="Anno"/>	
              	<s:textfield id="numeroImpOrigine" onkeyup="return checkItNumbersOnly(event)" name="model.ricercaModel.numeroImpOrigine" cssClass="lbTextSmall span2" title="Numero"/>	
              				        
              	</div>
            </div>
            
            <!-- PROGETTO -->
   			<s:include value="/jsp/movgest/include/progetto.jsp" />
            
            
            <div id="refreshHeaderCapitolo">
            	<s:include value="/jsp/movgest/include/headerCapitolo.jsp"/>
            </div>
            <s:include value="/jsp/movgest/include/capitolo.jsp" />
            <s:include value="/jsp/movgest/include/provvedimento.jsp" />
              
            <s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden> 
            
            <s:hidden id="hiddenPerEscludiAnnullati" name="model.ricercaModel.hiddenPerEscludiAnnullati" />
            
            <div id="refreshHeaderSoggetto">
            	<s:include value="/jsp/movgest/include/headerSoggetto.jsp"/>
            </div>
            <s:include value="/jsp/movgest/include/soggetto.jsp" />  
			
			<!-- Modal -->
             <s:include value="/jsp/movgest/include/modal.jsp" /> 
             
            <!--modale progetto -->
			<s:include value="/jsp/movgest/include/modalProgettoCronop.jsp"/>	
			<!--/modale progetto -->
             
            <!-- Fine Modal -->
            
            
            
            <br/> <br/> 
            
            <p>           
            
            <s:include value="/jsp/include/indietro.jsp" /> 

             <s:submit name="annulla" value="annulla" method="annulla" cssClass="btn btn-secondary" />
             <s:submit id="cerca" name="cerca" value="cerca" method="ricercaImpegni" cssClass="btn btn-primary pull-right" />
             
             </p> 
            
                  
          </fieldset>  
       	  </div>
       	  </div>
           
        </s:form>
      </div>	
    </div>	
  </div>	 
</div>	


<script src="/siacfinapp/js/local/movgest/ricercaImpegno.js" type="text/javascript"></script>

<script>

  function changeRiacc(){
		var riaccertatoNo = $("#riaccertatoNo");
		var riaccertatoSi = $("#riaccertatoSi");
		var annoImpRiacc = $("#annoImpRiacc");
		var numImpRiacc = $("#numImpRiacc");
		var riaccVisible = $('.riaccVisible');
		if (riaccertatoNo.is(':checked')) {
			riaccVisible.hide();
			annoImpRiacc.hide();
			numImpRiacc.hide();
		}
		if (riaccertatoSi.is(':checked')) {
			riaccVisible.show();
			annoImpRiacc.show();
			numImpRiacc.show();
		}
	}

  function impostaValoreEscludiAnnullati(){
		cbObj = document.getElementById("escludiAnnullatiCheckBox");
	  var valore = cbObj.checked;
	  $("#hiddenPerEscludiAnnullati").val(valore);
	}

</script>


<s:include value="/jsp/include/footer.jsp" />