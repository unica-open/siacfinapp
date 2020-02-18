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
  
  
<div class="container-fluid-banner">




<a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->


<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">
      
      <s:form id="mainForm" method="post" action="inserisciMovSpesaImporto.do" cssClass="form-horizontal">
       
        <div id="msgControlloSiProsegui" class="modal hide fade" tabindex="-1" role="dialog"  aria-hidden="true">
			<div class="modal-body">
				<div class="alert alert-error">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
    				<p><strong>Attenzione!</strong></p>
    				<s:actionerror/>
				</div>
			</div>
			<div class="modal-footer">
				<!-- <button id="noPluri" class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button> -->
				<s:submit id="annullaProseguiBtn" name="btnAnnullaProsegui" method="noProsegui" value="no" cssClass="btn btn-secondary" />
				<!-- <button class="btn" data-dismiss="modal" aria-hidden="true">si, prosegui</button> -->
				<s:submit id="proseguiBtn" name="btnProsegui" method="siProsegui" value="si" cssClass="btn btn-primary" />
			</div>
		</div>
       
       
<!--   	<h3>Impegno ${model.movimentoSpesaModel.impegno.annoMovimento}/${model.movimentoSpesaModel.impegno.numero} - ${model.movimentoSpesaModel.impegno.descrizione} - <s:property value="getText('struts.money.format', {movimentoSpesaModel.impegno.importoAttuale})"/></h3>	-->

		<h3><s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
       
       	<s:include value="/jsp/movgest/include/tabModificaSpesa.jsp" />
		<s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
			
           
		<s:if test="hasActionErrors()">
		<%-- Messaggio di ERROR --%>
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
					<ul>
						<s:actionerror />
					</ul>
			</div>
		</s:if>
		<s:if test="hasActionMessages()">
		<%-- Messaggi dalla response --%>
			<div class="alert alert-success">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<ul>
					<s:actionmessage />
				</ul>
			</div>
		</s:if>        
		
		<s:if test="hasActionWarnings()">
			<%-- Messaggio di WARNING --%>
			<div class="alert alert-warning">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
				   <s:iterator value="actionWarnings">
				       <s:property/><br>
				   </s:iterator>
					
				</ul>
			</div>
		</s:if>

        <h4>Inserimento modifica </h4> 
        <div class="step-content">
          <div class="step-pane active" id="step1">
           <fieldset class="form-horizontal margin-large">
                    <div class="control-group">
                      <!--label class="control-label" for="subimpegno">Subimpegno</label-->
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
							<s:select list="numeroSubImpegnoList" name="subSelected" id="numeroSubImpegnoList" headerKey="" headerValue="" onchange="javascript:document.location.href='inserisciMovSpesaImporto!caricaDatiSub.do?tipoImpegno=SubImpegno&subSelected='+this.value+'&abbinaChk='+abbina.checked" />
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
                         	 <dd>Modifica Impegno compresa tra <s:property value="model.minImportoCalcolatoMod" /> e <s:property value="model.maxImportoCalcolatoMod" /></dd>
                          </s:if>
                          <s:else>
                          	<dd>Modifica Impegno non inferiore a <s:property value="model.minImportoCalcolatoMod" /></dd>
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
<%--                     <s:include value="/jsp/movgest/include/provvedimentoAggiorna.jsp" />  --%>
 						 <s:include value="/jsp/movgest/include/provvedimento.jsp" />
                    <div class="control-group">
                      <label class="control-label" for="motivo">Modifica motivo *</label>
                      <div class="controls">

                        <s:select list="model.movimentoSpesaModel.listaTipoMotivo" id="listaTipoMotivo" headerKey="" 
          		   				headerValue="" name="idTipoMotivo" listKey="codice" listValue="descrizione"  cssClass="span9"/>
                      </div>
                    </div>
                    <div class="control-group">
                      <label class="control-label" for="Descrizione">Modifica descrizione <span class="hide" id="modificaDescrizioneObbligatoria"> * </span></label>
                      <div class="controls">
                        <s:textfield name="descrizione" id="descrizione" cssClass="span9" />  
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
                      			                	Modifica compresa tra <s:property value="model.minImpMod" /> e <s:property value="model.maxImpMod" /> 
                      			                </s:if>
                      			                <s:else>
								           		    Modifica Impegno non inferiore a <s:property value="model.minImpMod" /> 
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
				         </span>
                      </div>
                   </div> 
                    
                  </fieldset>
                  <!--p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p-->			          
                                   
                  <!--#include virtual="include/creditore.html" -->
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

<s:url method="importoModificaChanged" var="urlImporto"></s:url>

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