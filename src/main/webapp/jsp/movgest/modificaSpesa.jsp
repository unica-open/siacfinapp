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
  
<s:set var="gestisciForwardAction" value="%{'elencoMovimentoSpesa_gestisciForward'}" />			    		 	
<s:set var="siSalvaAction" value="%{'elencoMovimentoSpesa_siSalva'}" />	 
<s:set var="siProseguiAction" value="%{'elencoMovimentoSpesa_siProsegui'}" />	
<s:set var="annullaSubImpegnoAction" value="%{'elencoMovimentoSpesa_annullaSubImpegno'}" />	 
<s:set var="annullaSubAccertamentoAction" value="%{'elencoMovimentoSpesa_annullaSubAccertamento'}" />	 
<s:set var="annullaMovGestSpesaAction" value="%{'elencoMovimentoSpesa_annullaMovGestSpesa'}" />	 
<s:set var="eliminaSubImpegnoAction" value="%{'elencoMovimentoSpesa_eliminaSubImpegno'}" />	 
<s:set var="eliminaSubAccertamentoAction" value="%{'elencoMovimentoSpesa_eliminaSubAccertamento'}" />
<s:set var="forzaProseguiAction" value="%{'elencoMovimentoSpesa_forzaProsegui'}" />	          
<s:set var="forzaSalvaPluriennaleAccertamentoAction" value="%{'elencoMovimentoSpesa_forzaSalvaPluriennaleAccertamento'}" />	          
<s:set var="salvaConByPassDodicesimiAction" value="%{'elencoMovimentoSpesa_salvaConByPassDodicesimi'}" />	          

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
      <s:form id="mainForm" method="post" action="elencoMovimentoSpesa" cssClass="form-horizontal">
					
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

<%--  	<h3>Impegno ${model.movimentoSpesaModel.impegno.annoMovimento}/${model.movimentoSpesaModel.impegno.numero} - ${model.movimentoSpesaModel.impegno.descrizione} -  <s:property value="getText('struts.money.format', {movimentoSpesaModel.impegno.importoAttuale})"/></h3>	--%>  
            	
            	<h3><s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
            	
            	<s:include value="/jsp/movgest/include/tabModificaSpesa.jsp" />
            	<s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
			
<display:table name="model.movimentoSpesaModel.listaModifiche" class="table table-striped table-bordered table-hover" summary="riepilogo Moficica spesa" pagesize="10" requestURI="elencoMovimentoSpesa.do"  uid="ricercaImpegnoID" >
	<display:column title="Numero" property="numeroModificaMovimentoGestione" />
	<display:column title="Sub" property="numeroSubImpegno" />
	<%-- SIAC-8506 setto una lunghezza massima per la descrizione per non sfondare la pagina --%>
	<display:column title="Descrizione" property="descrizioneModificaMovimentoGestione" maxLength="80" />
	<display:column title="Motivo" property="tipoModificaMovimentoGestione" />
	<!-- SIAC-8834 -->
	<display:column title="Impegno contestuale" >
		<s:if test="%{#attr.ricercaImpegnoID.impegno != null}">
			<s:property value="%{#attr.ricercaImpegnoID.impegno.annoMovimento}" />/
 			<s:property value="%{#attr.ricercaImpegnoID.impegno.numero.intValue()}" />
 		</s:if>
	</display:column>
	<display:column title="Stato" property="codiceStatoOperativoModificaMovimentoGestione" />
	<display:column title="Provvedimento">
		<a href="#" data-trigger="hover" rel="popover" title="Oggetto" data-content="<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.oggetto}"/>">
			<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.anno}"/>/
			<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.numero}"/>/
			<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.strutturaAmmContabile.codice}"/>/
			<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.tipoAtto.codice}"/>(
			<s:property value="%{#attr.ricercaImpegnoID.attoAmministrativo.statoOperativo.toString().charAt(0)}"/>)
		</a>
	</display:column>
		
	<display:column title="Creditore">
		<s:if test="%{#attr.ricercaImpegnoID.classeSoggettoNewMovimentoGestione!=null}">
			<s:property value="%{#attr.ricercaImpegnoID.classeSoggettoNewMovimentoGestione.codice}" /> - <s:property value="%{#attr.ricercaImpegnoID.classeSoggettoNewMovimentoGestione.descrizione}" />
		</s:if>	
		<s:else>
			<s:property value="%{#attr.ricercaImpegnoID.soggettoNewMovimentoGestione.codiceSoggetto}" />
		</s:else>
	</display:column>
	
	<%-- SIAC-6929 --%>
	<display:column title="Blocco Rag.">
		    <s:if test="%{#attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria == null }">
              	N/A
              </s:if>  
              <s:if test="%{#attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria == false}">
              	NO
              </s:if> 
              <s:if test="%{#attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria == true}">
              	SI
              </s:if> 
		</display:column>
		
	
	<display:column title="Importo" property="importoOld" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
	<display:column title="">
		<div class="btn-group">
			<button class="btn dropdown-toggle" data-toggle="dropdown">
				Azioni<span class="caret"></span>
			</button>
			<ul class="dropdown-menu pull-right">
			<s:if test="isAggiornaMovimento(#attr.ricercaImpegnoID.uid)">
			
			<%-- SIAC-6929 --%>
                 <s:if test="%{#attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria != true}">
						<li><a href="aggiornaMovSpesa.do?movimentoSpesaId=${ricercaImpegnoID.uid}">aggiorna</a></li>
				</s:if>
            </s:if>
                 <li><a id="linkConsulta_<s:property value="%{#attr.ricercaImpegnoID_rowNum-1}"/>" href="#consultazioneMod" class="consultaModPopUp"  data-toggle="modal">consulta</a></li>

                 
                 
                 <s:if test="isAnnullaMovimento(#attr.ricercaImpegnoID.uid)">
                 <!-- SIAC-7349 Inizio  SR180 FL 02/04/2020  Aggiunta condizione sul esistenza delle modifiche di accertamento collegate-->	
                 <s:if test="%{#attr.ricercaImpegnoID.listaModificheMovimentoGestioneSpesaCollegata == null || #attr.ricercaImpegnoID.listaModificheMovimentoGestioneSpesaCollegata.size() == 0}">  
                 	</s:if>
                 	<!-- SIAC-6929 -->
                 	<s:if test="%{#attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria != true}">
                 		<!-- SIAC-8611-->
                 		<s:if test="%{#attr.ricercaImpegnoID.numeroSubImpegno != null}">
                 			<li><a id="linkAnnulla_<s:property value="%{#attr.ricercaImpegnoID.uid}"/>_<s:property value="%{#attr.ricercaImpegnoID.numeroModificaMovimentoGestione}"/>_<s:property value="%{#attr.ricercaImpegnoID.listaModificheMovimentoGestioneSpesaCollegata.size}"/>_<s:property value="%{#attr.ricercaImpegnoID.numeroSubImpegno}"/>" href="#msgAnnullaModMov" data-toggle="modal" class="linkAnnullaMod">annulla</a></li>	
                 		</s:if>
                 		<s:else>
                 			<li><a id="linkAnnulla_<s:property value="%{#attr.ricercaImpegnoID.uid}"/>_<s:property value="%{#attr.ricercaImpegnoID.numeroModificaMovimentoGestione}"/>_<s:property value="%{#attr.ricercaImpegnoID.listaModificheMovimentoGestioneSpesaCollegata.size}"/>_0" href="#msgAnnullaModMov" data-toggle="modal" class="linkAnnullaMod">annulla</a></li>
                 		</s:else>		
                 	</s:if>
                 
                 </s:if>
<%--             	<li><a href="#msgElimina" data-toggle="modal">elimina</a></li> --%>
            </ul>
		</div>
	</display:column>
</display:table>

		<s:if test="%{model.impegnoInAggiornamento.descrizioneStatoOperativoMovimentoGestioneSpesa != 'PROVVISORIO' && model.impegnoInAggiornamento.descrizioneStatoOperativoMovimentoGestioneSpesa != 'ANNULLATO'}">

			<s:if test="!isAbilitatoImpegnoRORdecentratoEFaseBilancioPredisposizioneConsuntivo()">
			
	            <p style="margin-top:40px">                    
	             <%--a class="btn"  href="FIN-insModifiche.shtml">inserisci modifica</a--%>
	              <s:a action="inserisciMovSpesaImporto" cssClass="btn">inserisci modifica importo</s:a>
	            
	            <%-- <s:if test="!isAbilitatoGestisciImpegnoDecentratoP()"> --%>
		            <s:if test="%{model.listaSubimpegni!=null}">
		              <s:a action="inserisciMovSpesaSoggetto" cssClass="btn">
		              <s:param name="aperturaIniziale" value="true"/>inserisci modifica soggetto</s:a>
		            </s:if>
	            <%-- </s:if>    --%>
	            </p>    
            
       		</s:if>     
        </s:if>
            <%-- HIDDEN --%>
            <s:hidden id="uidModDaAnnullare"    name="uidModDaAnnullare" />
            <!-- SIAC-8611-->
            <s:hidden id="numeroSubImpegnoMod"    name="numeroSubImpegnoMod" />
            <s:hidden id="numeroMovDaAnnullare"    name="numeroMovDaAnnullare" />
            
            
            
              <%-- Modal --%>
             <%--#include virtual="include/modal.html" --%> 
                <%-- Fine Modal --%>  
               	<s:set var="selezionaProvvedimentoAction" value="%{'elencoMovimentoSpesa_selezionaProvvedimento'}" />
                <s:set var="clearRicercaProvvedimentoAction" value="%{'elencoMovimentoSpesa_clearRicercaProvvedimento'}" />	          
       			<s:set var="ricercaProvvedimentoAction" value="%{'elencoMovimentoSpesa_ricercaProvvedimento'}" />	
       			<s:set var="eliminaAction" value="%{'elencoMovimentoSpesa_elimina'}" />	  
                <s:set var="selezionaProvvedimentoInseritoAction" value="%{'elencoMovimentoSpesa_selezionaProvvedimentoInserito'}" />	
				<s:set var="inserisciProvvedimentoAction" value="%{'elencoMovimentoSpesa_inserisciProvvedimento'}" />	
	            
                <s:include value="/jsp/movgest/include/modal.jsp" />    
                <%-- Jira - 1585 --%>                   
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
				// task-131 url: '<s:url method="dettaglioModPopUp"/>',
				url: '<s:url action="elencoMovimentoSpesa_dettaglioModPopUp"/>',
				type: 'POST',
				data: 'uidPerDettaglioModImpegno=' + supportId[1],
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
				//SIAC-7349 Inizio SR180 CM 23/04/2020 Aggiunti/,modificati (id) per gestire la presenza del messaggio sul numero dei movCollegati
				$("#numeroMovCollegatiDaAnnullare1").val(supportId[3]);
				$("#numeroMovCollegatiDaAnnullare2").val(supportId[3]);

				var movCollegatiDiv = $('#movCollegati');
				if (movCollegatiDiv.length != 0) {
					$("#numeroMovCollegatiDaAnnullare1P").show();
				}
				//SIAC-7349 Fine SR180 CM 23/04/2020
				
				//$("#tipoMovimento").val("imp");
				var countMovCollegatiDaAnnullare = supportId[3];
				if(countMovCollegatiDaAnnullare!='' && countMovCollegatiDaAnnullare>0){
			 		$("div.movCollegati").each(function() {
 			 			$(this).children('span').hide();
 			 			//SIAC-7349 Inizio SR180 CM 22/04/2020
 			 			$("#span2MovCollegati").show();
						$("#idButtonMovCollegati").text("Ok, indietro");
						//SIAC-7349 Fine SR180 CM 22/04/2020
			 		});
				}else{
// 					$("#numeroMovCollegatiDaAnnullare").val(0);
					//SIAC-7349 Inizio SR180 CM 23/04/2020 Aggiunti/,modificati (id) per gestire la presenza del messaggio sul numero dei movCollegati
					$("#numeroMovCollegatiDaAnnullare1").val(0);
					$("#numeroMovCollegatiDaAnnullare2").val(0);
					//SIAC-7349 Fine SR180 CM 23/04/2020
				}
				//SIAC-8611
				$("#numeroSubImpegnoMod").val(supportId[4]);
				$("#numeroMovDaAnnullare2").val(supportId[2]);
				//SIAC-7349 Fine  SR180 FL 15/04/2020


				
			}
		});
		
	});
</script>
