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
<%--corpo pagina--%>
<%--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> --%>


<s:set var="gestisciForwardAction" value="%{'elencoMovimentoSpesaAcc_gestisciForward'}" />			    		 	
<s:set var="siSalvaAction" value="%{'elencoMovimentoSpesaAcc_siSalva'}" />	 
<s:set var="siProseguiAction" value="%{'elencoMovimentoSpesaAcc_siProsegui'}" />	
<s:set var="annullaSubImpegnoAction" value="%{'elencoMovimentoSpesaAcc_annullaSubImpegno'}" />	 
<s:set var="annullaSubAccertamentoAction" value="%{'elencoMovimentoSpesaAcc_annullaSubAccertamento'}" />	 
<s:set var="annullaMovGestSpesaAction" value="%{'elencoMovimentoSpesaAcc_annullaMovGestSpesa'}" />	 
<s:set var="eliminaSubImpegnoAction" value="%{'elencoMovimentoSpesaAcc_eliminaSubImpegno'}" />	 
<s:set var="eliminaSubAccertamentoAction" value="%{'elencoMovimentoSpesaAcc_eliminaSubAccertamento'}" />
<s:set var="forzaProseguiAction" value="%{'elencoMovimentoSpesaAcc_forzaProsegui'}" />	          
<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'elencoMovimentoSpesaAcc_forzaSalvaPluriennaleAccertamento'}" />	          
<s:set var="salvaConByPassDodicesimiAction" value="%{'elencoMovimentoSpesaAcc_salvaConByPassDodicesimi'}" />	          
	
<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">  
      <%-- SIAC-7952 rimuovo .do dalla action --%> 
      <s:form id="mainForm" method="post" action="elencoMovimentoSpesaAcc" cssClass="form-horizontal">
					
		<%-- SIAC-7630 --%>
		<s:include value="/jsp/include/actionMessagesErrors.jsp" />
<%-- 	<s:if test="hasActionErrors()">
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
			Messaggio di WARNING
			<div class="alert alert-success">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<ul>
					<s:actionmessage />
				</ul>
			</div>
		</s:if> --%>
		<s:if test="%{successInsert}">
			<div class="alert alert-success margin-medium">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				L'operazione &egrave; stata completata con successo
			</div>   
		</s:if> 


			<div class="step-pane active" id="step1">

<%-- 	 	<h3>Accertamento ${model.movimentoSpesaModel.accertamento.annoMovimento}/${model.movimentoSpesaModel.accertamento.numero} - ${model.movimentoSpesaModel.accertamento.descrizione} -  <s:property value="getText('struts.money.format', {movimentoSpesaModel.accertamento.importoAttuale})"/></h3>  --%>
            	
            	<h3><s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
            	
            	<s:include value="/jsp/movgest/include/tabModificaSpesa.jsp" />
            	<s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
			
<display:table name="model.movimentoSpesaModel.listaModificheEntrata" class="table table-striped table-bordered table-hover" summary="riepilogo Moficica spesa" pagesize="10" requestURI="elencoMovimentoSpesaAcc.do" uid="ricercaImpegnoID">
	<display:column title="Numero" property="numeroModificaMovimentoGestione" />
	<display:column title="Sub" property="numeroSubAccertamento" />
	<%-- SIAC-8506 setto una lunghezza massima per la descrizione per non sfondare la pagina --%>
	<display:column title="Descrizione" maxLength="80" >
		<s:if test="%{#attr.ricercaImpegnoID.isRiepilogoAutomatiche()==false}">
			<s:property value="%{#attr.ricercaImpegnoID.descrizioneModificaMovimentoGestione}" />
		</s:if>
		<s:else>
			<s:property value="%{#attr.ricercaImpegnoID.descrizioneModificaMovimentoGestione}" /> - 
			<s:property value="%{#attr.ricercaImpegnoID.numeroModificheAutomatiche}" />
		</s:else>
	</display:column>
	
	
	<display:column title="Motivo" property="tipoModificaMovimentoGestione" />
	<display:column title="Stato" property="codiceStatoOperativoModificaMovimentoGestione" />
	<display:column title="Provvedimento">
	
		<s:if test="%{#attr.ricercaImpegnoID.isRiepilogoAutomatiche()==false}">
		
			<a href="#" data-trigger="hover" rel="popover" title="Oggetto" 
			data-content="<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.oggetto}"/>">
				<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.anno}"/>/
				<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.numero}"/>/
				<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.strutturaAmmContabile.codice}"/>/
				<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.tipoAtto.codice}"/>(
				<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.statoOperativo.toString().charAt(0)}"/>)
			</a>
		
		</s:if>
		
	</display:column>
	<%-- siac 6292--%>
	<display:column title="Blocco Rag.">
		<s:if test="%{#attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria==false}">
			No
		</s:if>
		<s:elseif test="%{#attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria==true}">
			Si
		</s:elseif>
		<s:else >
			N/A
		</s:else>
	</display:column>
	<display:column title="Creditore">
		<s:if test="%{#attr.ricercaImpegnoID.classeSoggettoNewMovimentoGestione!=null}">
			<s:property value="%{#attr.ricercaImpegnoID.classeSoggettoNewMovimentoGestione.codice}" /> - <s:property value="%{#attr.ricercaImpegnoID.classeSoggettoNewMovimentoGestione.descrizione}" />
		</s:if>	
		<s:else>
			<s:property value="%{#attr.ricercaImpegnoID.soggettoNewMovimentoGestione.codiceSoggetto}" />
		</s:else>
	</display:column>
	
	<display:column title="Importo" property="importoOld" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
	<display:column title="">
		<s:if test="%{#attr.ricercaImpegnoID.isRiepilogoAutomatiche()==false}">
		
			<div class="btn-group">
				<button class="btn dropdown-toggle" data-toggle="dropdown">
					Azioni<span class="caret"></span>
				</button>
				<ul class="dropdown-menu pull-right">
				<s:if test="isAggiornaMovimento(#attr.ricercaImpegnoID.uid)">
					<s:if test="%{#attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria!=true}">
	                	<li><a href="aggiornaMovSpesaAcc.do?movimentoSpesaId=${ricercaImpegnoID.uid}">aggiorna</a></li>
					</s:if>
				</s:if>      
	            <li><a id="linkConsulta_<s:property value="%{#attr.ricercaImpegnoID_rowNum-1}"/>" href="#consultazioneMod" class="consultaModPopUp"  data-toggle="modal">consulta</a></li>
	
	                 
	             <s:if test="isAnnullaMovimento(#attr.ricercaImpegnoID.uid)">
					<s:if test="%{#attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria!=true}">
	                	<li>	<!-- SIAC-7349 Inizio  SR180 FL 02/04/2020  -->	
						  <a id="linkAnnulla_<s:property value="%{#attr.ricercaImpegnoID.uid}"/>_<s:property value="%{#attr.ricercaImpegnoID.numeroModificaMovimentoGestione}"/>_<s:property value="%{#attr.ricercaImpegnoID.listaModificheMovimentoGestioneSpesaCollegata.size}"/>" href="#msgAnnullaModMov" data-toggle="modal" class="linkAnnullaMod">annulla</a>
						</li>
					</s:if> 
				</s:if>     
	                 
	<%--             	<li><a href="#msgElimina" data-toggle="modal">elimina</a></li> --%>
	            </ul>
			</div>
		</s:if>
	</display:column>
</display:table>
	
		<s:if test="%{model.accertamentoInAggiornamento.descrizioneStatoOperativoMovimentoGestioneEntrata != 'PROVVISORIO' && model.accertamentoInAggiornamento.descrizioneStatoOperativoMovimentoGestioneEntrata != 'ANNULLATO'}">
		   <s:if test="!isAbilitatoAccertamentoRORdecentratoEFaseBilancioPredisposizioneConsuntivo()">
		    <p style="margin-top:40px">                    
             <!--a class="btn"  href="FIN-insModifiche.shtml">inserisci modifica</a-->
              <s:a action="inserisciModificaMovimentoSpesaAccImporto" cssClass="btn">inserisci modifica importo</s:a>
              
              <s:if test="%{model.listaSubaccertamenti!=null}">
              	<s:a action="inserisciModificaMovimentoSpesaAccSoggetto" cssClass="btn">
              	<s:param name="aperturaIniziale" value="true"/>inserisci modifica soggetto</s:a>
              </s:if> 
               
            </p>   
		  </s:if> 
		</s:if>
 

             <s:hidden id="uidModDaAnnullare"    name="uidModDaAnnullare" />
            
              <%-- Modal --%>
             <%--#include virtual="include/modal.html" --%> 
                <%-- Fine Modal --%>  
                <s:set var="selezionaProvvedimentoAction" value="%{'elencoMovimentoSpesaAcc_selezionaProvvedimento'}" />
                <s:set var="clearRicercaProvvedimentoAction" value="%{'elencoMovimentoSpesaAcc_clearRicercaProvvedimento'}" />	          
       			<s:set var="ricercaProvvedimentoAction" value="%{'elencoMovimentoSpesaAcc_ricercaProvvedimento'}" />	          
       			<s:set var="eliminaAction" value="%{'elencoMovimentoSpesaAcc_elimina'}" />	  
                <s:set var="selezionaProvvedimentoInseritoAction" value="%{'elencoMovimentoSpesaAcc_selezionaProvvedimentoInserito'}" />	
				<s:set var="inserisciProvvedimentoAction" value="%{'elencoMovimentoSpesaAcc_inserisciProvvedimento'}" />	
	
                <s:include value="/jsp/movgest/include/modal.jsp" />   
                <%-- Jira - 1584 --%>                     
<%--          <p class="marginLarge">  --%>
<%--   	   		<s:submit name="indietro" value="indietro" method="indietro" cssClass="btn btn-secondary" /> --%>
<%--   		</p>  --%>
  		</div>        
      </s:form>
    </div>
  </div>	 
</div>	

<s:include value="/jsp/include/footer.jsp" />

<script type="text/javascript">
	$(document).ready(function() {
		
		$(".consultaModPopUp").click(function() {
			var supportId = $(this).attr("id").split("_");
				
			$.ajax({
				//task-131 url: '<s:url method="dettaglioModPopUp"/>',
				url: '<s:url action="elencoMovimentoSpesaAcc_dettaglioModPopUp"/>',
				type: 'POST',
				data: 'uidPerDettaglioModAccertamento=' + supportId[1],
			    success: function(data)  {
				    $("#divDettaglioModPopUp").html(data);
				}
			});
		});	
		
		
		//annulla
		$(".linkAnnullaMod").click(function() {
			
			var supportId = $(this).attr("id").split("_");
			if (supportId != null && supportId.length > 0) {
				
				$("#uidModDaAnnullare").val(supportId[1]);
				$("#numeroMovDaAnnullare").val(supportId[2]);
				//SIAC-7349 Inizio  SR180 FL 15/04/2020
				$("#numeroMovCollegatiDaAnnullare").val(supportId[3]);
				//$("#tipoMovimento").val("acc");
				//SIAC-7349 Fine  SR180 FL 15/04/2020

				//CONTABILIA-260 - Bug fix per test 22.1 SIAC-7349 Inizio SR180 CM 09/07/2020 Aggiunto per gestire la presenza del messaggio sul numero dei movCollegati nelle modifiche di accertamento
				$("#numeroMovCollegatiDaAnnullare1").val(supportId[3]);
				$("#numeroMovCollegatiDaAnnullare2").val(supportId[3]);
				$("#numeroMovCollegatiDaAnnullare1").attr("disabled","disabled");
				$("#numeroMovCollegatiDaAnnullare1P").show();
				//CONTABILIA-260 - Bug fix per test 22.1 SIAC-7349 Fine SR180 CM 09/07/2020
				//SIAC-8611
				$("#numeroMovDaAnnullare2").val(supportId[2]);
				
			}
		});
		
	});
</script>