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
                    
  <!-- NAVIGAZIONE 
  <p class="nascosto"><a name="A-sommario" title="A-sommario"></a></p>     
  <ul id="sommario" class="nascosto">
    <li><a href="#A-contenuti">Salta ai contenuti</a></li>
  </ul>
  /NAVIGAZIONE -->
  <hr />
<div class="container-fluid-banner">

  <a name="A-contenuti" title="A-contenuti"></a>
</div>
<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">    
    
     <s:form id="%{labels.FORM}" action="%{labels.FORM}" method="post">
     	
  		<s:if test="oggettoDaPopolarePagamento()">
  			<s:set var="dettaglioConsultaQuotaAction" value="%{'gestioneOrdinativoPagamentoStep2_dettaglioConsultaQuota'}" />
		    <s:set var="dettaglioAggiornaQuotaAction" value="%{'gestioneOrdinativoPagamentoStep2_dettaglioAggiornaQuota'}" />
		    <s:set var="annullaInserimentoQuotaAction" value="%{'gestioneOrdinativoPagamentoStep2_annullaInserimentoQuota'}" />	   		           
			<!-- per contenitoreOrdPag.jsp -->
			<s:set var="inserisciQuotaAction" value="%{'gestioneOrdinativoPagamentoStep2_inserisciQuota'}" />	 
			<s:set var="ricaricaTEByIdLiquidazioneAction" value="%{'gestioneOrdinativoPagamentoStep2_ricaricaTEByIdLiquidazione'}" />	 
			
			<!-- per modalOrdinativo.jsp -->
			<s:set var="eliminaQuotaOrdinativoAction" value="%{'gestioneOrdinativoPagamentoStep2_eliminaQuotaOrdinativo'}" />
			<s:set var="eliminaProvvisorioAction" value="%{'gestioneOrdinativoPagamentoStep2_eliminaProvvisorio'}" />
			<s:set var="forzaInserisciQuotaAccertamentoAction" value="%{'gestioneOrdinativoPagamentoStep2_forzaInserisciQuotaAccertamento'}" />
			<s:set var="forzaAggiornaQuotaAccertamentoAction" value="%{'gestioneOrdinativoPagamentoStep2_forzaAggiornaQuotaAccertamento'}" />				  		           
		</s:if>
  		<s:else>
  			<s:set var="dettaglioConsultaQuotaAction" value="%{'gestioneOrdinativoIncassoStep2_dettaglioConsultaQuota'}" />
		    <s:set var="dettaglioAggiornaQuotaAction" value="%{'gestioneOrdinativoIncassoStep2_dettaglioAggiornaQuota'}" />
		    <s:set var="annullaInserimentoQuotaAction" value="%{'gestioneOrdinativoIncassoStep2_annullaInserimentoQuota'}" />	   		           		
  			<!-- per contenitoreOrdInc.jsp -->
  			<s:set var="inserisciQuotaAction" value="%{'gestioneOrdinativoIncassoStep2_inserisciQuota'}" />
  			<s:set var="ricaricaTEByIdAccertamentoAction" value="%{'gestioneOrdinativoIncassoStep2_ricaricaTEByIdAccertamento'}" />	 
			
  			<!-- per modalOrdinativo.jsp -->
			<s:set var="eliminaQuotaOrdinativoAction" value="%{'gestioneOrdinativoIncassoStep2_eliminaQuotaOrdinativo'}" />
			<s:set var="eliminaProvvisorioAction" value="%{'gestioneOrdinativoIncassoStep2_eliminaProvvisorio'}" />
			<s:set var="forzaInserisciQuotaAccertamentoAction" value="%{'gestioneOrdinativoIncassoStep2_forzaInserisciQuotaAccertamento'}" />
			<s:set var="forzaAggiornaQuotaAccertamentoAction" value="%{'gestioneOrdinativoIncassoStep2_forzaAggiornaQuotaAccertamento'}" />
				
  		</s:else> 
  		
		<s:include value="/jsp/include/actionMessagesErrors.jsp" />
		<s:if test="(sonoInAggiornamento() || sonoInAggiornamentoIncasso())"><h3>Ordinativo <s:property value="gestioneOrdinativoStep1Model.ordinativo.numero"/>  del <s:property value="%{gestioneOrdinativoStep1Model.ordinativo.dataEmissione}"/> -  Stato <s:property value="gestioneOrdinativoStep1Model.ordinativo.statoOperativoOrdinativo"/> dal <s:property value="%{gestioneOrdinativoStep1Model.ordinativo.dataInizioValidita}"/> </h3></s:if>
		<s:else><h3>Inserimento quote ordinativo</h3></s:else>
       	
        <div id="MyWizard" class="wizard">
			<ul class="steps" >
				<li data-target="#step1" class="complete"><span class="badge badge-success">1</span>Dati ordinativo<span class="chevron"></span></li>
				<li data-target="#step2" class="active"><span class="badge">2</span>Quote ordinativo<span class="chevron"></span></li>
				<s:if test="isCampoAbilitatoInAggiornamento('TAB_PROVVISORI')">
					<li data-target="#step3" class="complete"><span class="badge">3</span>Provvisori di cassa<span class="chevron"></span></li>
				</s:if>
			</ul>
        </div>
        
        <s:hidden name="attivaBtnSalva" />
		
        <div class="step-content"> 
          <div class="step-pane active" id="step2">
          
		<s:include value="/jsp/ordinativo/include/headerDettaglioOrdinativo.jsp" />

		<s:if test="oggettoDaPopolarePagamento()"> 
   			 <!--  ORDINATIVO PAGAMENTO -->
			
			<display:table name="gestioneOrdinativoStep2Model.listaSubOrdinativiPagamenti"  requestURI="gestioneOrdinativoPagamentoStep2.do"
		               pagesize="10" class="table table-hover tab_left"  summary="riepilogo indirizzo" uid="subOrdinativiPagId" >
					<display:column title="Quota" property="numero" />		
					<display:column title="A. liq" property="liquidazione.annoLiquidazione" />	
					<display:column title="N. liq" property="liquidazione.numeroLiquidazione" />
					<display:column title="Descrizione quota" property="descrizione" />
					
					<!-- CR 1911: aggiungere le informazioni sul documento della quota -->
					<display:column title="Documento" >
										
						
						<s:property value="%{#attr.subOrdinativiPagId.subDocumentoSpesa.documento.anno}" /> / 
						<s:property value="%{#attr.subOrdinativiPagId.subDocumentoSpesa.documento.descrizione}" />/ 
						<s:property value="%{#attr.subOrdinativiPagId.subDocumentoSpesa.documento.numero}" /> 
						 del  <s:property value="%{#attr.subOrdinativiPagId.subDocumentoSpesa.documento.dataEmissione}" /> - quota: <s:property value="%{#attr.quotaID.subDocumentoSpesa.numero}" />
										
					</display:column>
					
					<display:column title="Impegno">
<!-- 					 <a href="#" data-trigger="hover" rel="popover" title="Oggetto"  -->
<%-- 						data-content="<s:property value="%{#attr.subOrdinativiPagId.liquidazione.impegno.descrizione}"/>"> --%>
<%-- 						<s:property value="%{#attr.subOrdinativiPagId.liquidazione.impegno.annoMovimento}" />/<s:property value="%{#attr.subOrdinativiPagId.liquidazione.impegno.numero.intValue()}" /></a>  --%>
 	
					  <a href="#" data-trigger="hover" rel="popover" title="Oggetto"
					  	 data-content="<s:property value="%{#attr.subOrdinativiPagId.liquidazione.impegno.descrizione}"/>">
					  	 
	 					  <s:if test="rigaImpegno(#attr.subOrdinativiPagId.liquidazione).equals('subImpegno')">
	 					  		
				          		<s:property	value="%{#attr.subOrdinativiPagId.liquidazione.impegno.annoMovimento}" />/<s:property value="%{#attr.subOrdinativiPagId.liquidazione.impegno.numero.intValue()}" />-<s:property	value="%{#attr.subOrdinativiPagId.liquidazione.subImpegno.numero.intValue()}" /> 
				          </s:if>
				          <s:elseif test="rigaImpegno(#attr.subOrdinativiPagId.liquidazione).equals('elencoSub')">
				                	<s:property	value="%{#attr.subOrdinativiPagId.liquidazione.impegno.annoMovimento}" />/<s:property value="%{#attr.subOrdinativiPagId.liquidazione.impegno.numero.intValue()}" />-<s:property	value="%{#attr.subOrdinativiPagId.liquidazione.impegno.elencoSubImpegni[0].numero.intValue()}" /> 
				          </s:elseif>
				          <s:elseif test="rigaImpegno(#attr.subOrdinativiPagId.liquidazione).equals('impegno')">
				               	<s:property	value="%{#attr.subOrdinativiPagId.liquidazione.impegno.annoMovimento}" />/<s:property value="%{#attr.subOrdinativiPagId.liquidazione.impegno.numero.intValue()}" /> 
				          </s:elseif>
				      </a>    
					</display:column>
										
					<display:column class="pagination-right" title="Importo quota" property="importoAttuale" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
					
					
					<display:column>
						
							<div class="btn-group">
								
							  <button class="btn dropdown-toggle" data-toggle="dropdown" >Azioni <span class="caret"></span></button>
							  
							  <ul class="dropdown-menu pull-right">
								
								<li><a id="linkAggiorna_<s:property value="%{#attr.subOrdinativiPagId.numero}"/>" href="#aggOrdinativo2" data-toggle="modal" class="linkAggiornaQuota">aggiorna</a></li>
								
								<s:if test="!sonoInAggiornamento()">                 
									<li><a id="linkElimina_<s:property value="%{#attr.subOrdinativiPagId.numero}"/>" href="#msgElimina" data-toggle="modal" class="linkEliminaQuota">elimina</a></li>
								</s:if>
							  </ul>
							</div>
						
					</display:column>	
					
					<display:footer>
						<tr class="newline">
							<th scope="col" colspan="7">Totale ordinativo</th>
							<th scope="col"><s:property value="getText('struts.money.format', {gestioneOrdinativoStep2Model.sommatoriaQuote})"/>&nbsp;</th>
							<th scope="col">&nbsp;</th>
                		</tr>
					</display:footer>
					
						
                     
		 	</display:table>	
		 
			<s:hidden id="numeroQuotaDaPassare" name="numeroQuotaEliminato"/>

		</s:if>
		<s:else>
			<!--  ORDINATIVO INCASSO -->
			<display:table name="gestioneOrdinativoStep2Model.listaSubOrdinativiIncasso"  requestURI="gestioneOrdinativoIncassoStep2.do"
			               pagesize="10" class="table table-hover tab_left" summary="riepilogo indirizzo" uid="subOrdinativiIncassoId" >
			              
			        <display:column title="N. Quota" property="numero" />	
		            <display:column title="Accertamento">
		          	   
		               <s:if test="rigaAccertamento(#attr.subOrdinativiIncassoId.accertamento)">
		                   <!-- accertamento -->
		                   <s:property value="%{#attr.subOrdinativiIncassoId.accertamento.annoMovimento}" />/<s:property value="%{#attr.subOrdinativiIncassoId.accertamento.numero.intValue()}" />
		               </s:if>
		               <s:else>
		                    <!-- subaccertamento -->
		                     <s:property value="%{#attr.subOrdinativiIncassoId.accertamento.annoAccertamentoPadre}" />/<s:property value="%{#attr.subOrdinativiIncassoId.accertamento.numeroAccertamentoPadre.intValue()}" /> - <s:property value="%{#attr.subOrdinativiIncassoId.accertamento.numero.intValue()}" />
		               </s:else>
		            
		            </display:column>   
			            
		            
		            <display:column title="Descrizione">
		           		 <s:property value="%{#attr.subOrdinativiIncassoId.descrizione}" />
		            </display:column>	
		            <display:column title="Data scadenza" property="dataScadenza" format="{0,date,dd/MM/yyyy}" />
		            <display:column title="Importo" property="importoAttuale" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />		      
			        
			        <display:column>
							
					<div class="btn-group">
					
						<button class="btn dropdown-toggle" data-toggle="dropdown" >Azioni <span class="caret"></span></button>
						
						<ul class="dropdown-menu pull-right">
		
							
							<li><a id="linkAggiorna_<s:property value="%{#attr.subOrdinativiIncassoId.numero}"/>" href="#aggOrdinativo2" data-toggle="modal" class="linkAggiornaQuota">aggiorna</a></li>
							
							
						    <s:if test="!sonoInAggiornamentoIncasso()">
						        <!-- se non in aggiorna non deve esserci il tasto elimina -->
								<li><a id="linkElimina_<s:property value="%{#attr.subOrdinativiIncassoId.numero}"/>" href="#msgElimina" data-toggle="modal" class="linkEliminaQuota">elimina</a> </li>
							</s:if>	
							
							<li><a id="linkConsulta_<s:property value="%{#attr.subOrdinativiIncassoId.numero}"/>" href="#consultaOrd2" data-toggle="modal" class="linkConsultaQuota">consulta</a> </li>
						</ul>
					</div>
							
						</display:column>	
			        
			        
			        <display:footer>
							<tr class="newline">
								<th scope="col" colspan="4">Totale ordinativo</th>
								<th scope="col"><s:property value="getText('struts.money.format', {gestioneOrdinativoStep2Model.sommatoriaQuote})"/>&nbsp;</th>
								<th scope="col">&nbsp;</th>
	                		</tr>
					</display:footer>       
			</display:table>               
			<a id="linkMsgDatipersi" href="#msgControlloProseguiOrdInc" data-toggle="modal" style="display:none;"></a>         
			 
			<s:hidden id="numeroQuotaDaPassare" name="numeroQuotaEliminato"/>
		
		</s:else>
		 
		 <s:if test="oggettoDaPopolarePagamento()"> 
		 
		            <!--  -->
		            <!--  ORDINATIVO PAGAMENTO -->
		            <!--  --> 
		            <s:include value="/jsp/ordinativo/include/contenitoreOrdPag.jsp" />
		 
		 </s:if>
		 <s:else>
		 		    <!--  -->
		            <!--  ORDINATIVO INCASSO -->
		            <!--  --> 
		            <s:include value="/jsp/ordinativo/include/contenitoreOrdInc.jsp" />
		 </s:else>
	
	
		<!--#include virtual="include/modal.html" --> 
				<s:include value="/jsp/ordinativo/include/modalOrdinativo.jsp" />
	
	
		</div> 
	    </div>
		
		</div> <!-- messo qui div chiuso perche' la display table aggiungi un div e probabilemente rimane aperto, causando una scorretta visualizzazione dei bottoni -->
		<!-- INIZIO BOTTONI  -->
		
		<p class="margin-medium"> 
			<s:include value="/jsp/include/indietroSubmit.jsp" />
			
				<s:if test="oggettoDaPopolarePagamento()">				
					
					<s:if test="attivaPulsanteSalva()">
						<!-- task-131 <s:submit id="saveBtn" cssClass="btn btn-primary freezePagina pull-right" method="salva" value="salva" name="salva" /> -->
						<s:submit id="saveBtn" cssClass="btn btn-primary freezePagina pull-right" action="gestioneOrdinativoPagamentoStep2_salva" value="salva" name="salva" />	
					</s:if> 
					<s:else>						
						<s:if test="presenzaQuote()">
		                    <!--  attivo btn solo se sono presenti delle quote -->			
							<!-- task-131 <s:submit id="proseguiStep" cssClass="btn btn-primary pull-right" method="proseguiStep" value="prosegui" name="prosegui" /> -->
							<s:submit id="proseguiStep" cssClass="btn btn-primary pull-right" action="gestioneOrdinativoPagamentoStep2_proseguiStep" value="prosegui" name="prosegui" />					
					   	</s:if>					   						   	
						<s:if test="sonoInAggiornamento()">
						  	<!-- task-131 <s:submit id="aggiornaOrdinativo" cssClass="btn btn-primary pull-right" method="aggiornaOrdinativo" value="salva" name="salva" /> -->
						  	<s:submit id="aggiornaOrdinativo" cssClass="btn btn-primary pull-right" action="gestioneOrdinativoPagamentoStep2_aggiornaOrdinativo" value="salva" name="salva" />		
						</s:if>
					</s:else>
				</s:if>	
				<s:else>
					<s:if test="attivaPulsanteProsegui()">
						<!-- task-131 <s:submit id="proseguiBtn" cssClass="btn btn-primary pull-right" method="prosegui" value="prosegui" name="prosegui" /> -->
						<s:submit id="proseguiBtn" cssClass="btn btn-primary pull-right" action="gestioneOrdinativoIncassoStep2_prosegui" value="prosegui" name="prosegui" />	
					</s:if>
					<s:else>
						<s:if test="controlloQuote()">
							<!-- task-131 <s:submit id="proseguiBtn" cssClass="btn btn-primary pull-right" method="prosegui" value="prosegui" name="prosegui" /> -->
							<s:submit id="proseguiBtn" cssClass="btn btn-primary pull-right" action="gestioneOrdinativoIncassoStep2_prosegui" value="prosegui" name="prosegui" />	
						</s:if>
						
						<s:if test="sonoInAggiornamentoIncasso()">
							<!-- task-131 <s:submit id="saveBtn" cssClass="btn btn-primary freezePagina pull-right" method="aggiornaOrdinativoIncasso" value="salva" name="salva" /> -->
							<s:submit id="saveBtn" cssClass="btn btn-primary freezePagina pull-right" action="gestioneOrdinativoIncassoStep2_aggiornaOrdinativoIncasso" value="salva" name="salva" />
						</s:if>
						<s:else>
							<!-- task-131 <s:submit id="saveBtn" cssClass="btn btn-primary freezePagina pull-right" method="salva" value="salva" name="salva" /> -->
							<s:submit id="saveBtn" cssClass="btn btn-primary freezePagina pull-right" action="gestioneOrdinativoIncassoStep2_salva" value="salva" name="salva" />
						</s:else>	
					</s:else>
				</s:else>	
		</p> 
		
		
		
      </s:form>
    </div>
  </div>	 
</div>	

<script type="text/javascript">

$(document).ready(function() {
	
	var insQuota = $("#insQuotaBtn");
	var toggleLiquidazioneAperto = $("#toggleLiquidazioneAperto");
	//console.log("toggleLiquidazioneAperto --> "+toggleLiquidazioneAperto.val());
	if (toggleLiquidazioneAperto.val() == "true") {
		insQuota.click();
	}	
	
	$(".linkEliminaQuota").click(function() {
			var supportId = $(this).attr("id").split("_");
			
			if (supportId != null && supportId.length > 0) {
				$("#numeroQuotaDaPassare").val(supportId[1]);
			}
	});
	
	$(".linkConsultaQuota").click(function() {
		var supportId = $(this).attr("id").split("_");
		$.ajax({
			//task-131 url: '<s:url method="dettaglioConsultaQuota"/>',
			url: '<s:url action="%{#dettaglioConsultaQuotaAction}"/>',
		    type: 'POST',
			data: 'numeroPerDettaglio=' + supportId[1],
		    success: function(data)  {
			    $("#divDettaglioConsultaQuotaOrdinativo").html(data);
			}
		});
	});	
	
	$(".linkAggiornaQuota").click(function() {
		var supportId = $(this).attr("id").split("_");
		$.ajax({
			//task-131 url: '<s:url method="dettaglioAggiornaQuota"/>',
			url: '<s:url action="%{#dettaglioAggiornaQuotaAction}"/>',
		    type: 'POST',
			data: 'numeroPerDettaglio=' + supportId[1],
		    success: function(data)  {
			    $("#divDettaglioAggiornaQuotaOrdinativo").html(data);
			}
		});
	});	
	
	var tipoDebitoSiopeVar = $("input[name='gestioneOrdinativoStep2Model.tipoDebitoSiope']");
	<s:include value="/jsp/include/toggleAssenzaCig.jsp" />	
	
}); 
 
 
	<s:if test="oggettoDaPopolarePagamento()">
		<!-- ORDINATIVO PAGAMENTO -->
		<s:if test="gestioneOrdinativoStep2Model.listaLiquidazioni!=null && gestioneOrdinativoStep2Model.listaLiquidazioni.size()>0">
			$("#insQuotaBtn").click();
		</s:if>
	</s:if>
	<s:else>
		<!-- ORDINATIVO INCASSO -->
		<s:if test="gestioneOrdinativoStep2Model.listaAccertamento!=null && gestioneOrdinativoStep2Model.listaAccertamento.size()>0">
			$("#insQuotaBtn").click();
		</s:if>
		<s:if test="gestioneOrdinativoStep2Model.checkProseguiQuoteIncasso">
			$("#linkMsgDatipersi").click();
		</s:if>
		<s:if test="gestioneOrdinativoStep2Model.checkAggiornaQuoteIncasso">
			$("#linkMsgDatipersi").click();
		</s:if>
	</s:else>
 
   function pulisciCampiQuota(){
	   $("#descrizioneQuota").val("");
	   $("#impQU").val("");
	   $("#dataQU").val("");
	   var radioChecked = $('.liquidazioneOrdinativo').is(':checked');
	   
	   if(radioChecked){
		   $('.liquidazioneOrdinativo').attr('checked',false);
	   }
	   
	   // questo toglie il radio di accertamento
	   var radioCheckedAccertamento = $('.accertamentoOrdinativo').is(':checked');
	  
	   if(radioCheckedAccertamento){
		   $('.accertamentoOrdinativo').attr('checked',false);
	   }
	   	 
	   
       $.ajax({
			//task-131 url: '<s:url method="annullaInserimentoQuota"></s:url>',
			url: '<s:url action="%{#annullaInserimentoQuotaAction}"/>',  
			type: "POST",
			data: $("#gestioneOrdinativoIncassoStep2").serialize(), 
		    success: function(data)  {
		    	$("#refreshTE").html(data);
			}
		});
	   
   }
 
</script>
<s:include value="/jsp/include/footer.jsp" />