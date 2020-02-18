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
      <s:form id="mainForm" method="post" action="elencoMovimentoSpesa.do" cssClass="form-horizontal">
					
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
						<%-- Messaggio di WARNING --%>
						<div class="alert alert-success">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<ul>
								<s:actionmessage />
							</ul>
						</div>
					</s:if>
					<s:if test="%{successInsert}">
						<div class="alert alert-success margin-medium">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							L'operazione &egrave; stata completata con successo
						</div>   
					</s:if> 


			<div class="step-pane active" id="step1">

<!--  	<h3>Impegno ${model.movimentoSpesaModel.impegno.annoMovimento}/${model.movimentoSpesaModel.impegno.numero} - ${model.movimentoSpesaModel.impegno.descrizione} -  <s:property value="getText('struts.money.format', {movimentoSpesaModel.impegno.importoAttuale})"/></h3>	-->  
            	
            	<h3><s:include value="/jsp/movgest/include/titoloImpegno.jsp" /></h3>
            	
            	<s:include value="/jsp/movgest/include/tabModificaSpesa.jsp" />
		 		<s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
			
<display:table name="model.movimentoSpesaModel.listaModifiche" class="table table-striped table-bordered table-hover" summary="riepilogo Moficica spesa" pagesize="10" requestURI="elencoMovimentoSpesa.do"  uid="ricercaImpegnoID" >
	<display:column title="Numero" property="numeroModificaMovimentoGestione" />
	<display:column title="Sub" property="numeroSubImpegno" />
	<display:column title="Descrizione" property="descrizioneModificaMovimentoGestione" />
	<display:column title="Motivo" property="tipoModificaMovimentoGestione" />
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
	
	<!-- SIAC-6929 -->
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
			
			<!-- SIAC-6929 -->
                 <s:if test="%{#attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria != true}">
					<li><a href="aggiornaMovSpesa.do?movimentoSpesaId=${ricercaImpegnoID.uid}">aggiorna</a></li>
				</s:if>
            </s:if>
                 <li><a id="linkConsulta_<s:property value="%{#attr.ricercaImpegnoID_rowNum-1}"/>" href="#consultazioneMod" class="consultaModPopUp"  data-toggle="modal">consulta</a></li>

                 
                 <s:if test="isAnnullaMovimento(#attr.ricercaImpegnoID.uid)">
                 	<!-- SIAC-6929 -->
                 	<s:if test="%{#attr.ricercaImpegnoID.attoAmministrativo.bloccoRagioneria != true}">
                 	<li><a id="linkAnnulla_<s:property value="%{#attr.ricercaImpegnoID.uid}"/>_<s:property value="%{#attr.ricercaImpegnoID.numeroModificaMovimentoGestione}"/>" href="#msgAnnullaModMov" data-toggle="modal" class="linkAnnullaMod">annulla</a></li>
                 	</s:if>
                 </s:if>
                 
<!--             	<li><a href="#msgElimina" data-toggle="modal">elimina</a></li> -->
            </ul>
		</div>
	</display:column>
</display:table>

		<s:if test="%{model.impegnoInAggiornamento.descrizioneStatoOperativoMovimentoGestioneSpesa != 'PROVVISORIO' && model.impegnoInAggiornamento.descrizioneStatoOperativoMovimentoGestioneSpesa != 'ANNULLATO'}">

		
		
            <p style="margin-top:40px">                    
             <!--a class="btn"  href="FIN-insModifiche.shtml">inserisci modifica</a-->
              <s:a action="inserisciMovSpesaImporto" cssClass="btn">inserisci modifica importo</s:a>
            
            <%-- <s:if test="!isAbilitatoGestisciImpegnoDecentratoP()"> --%>
	            <s:if test="%{model.listaSubimpegni!=null}">
	              <s:a action="inserisciMovSpesaSoggetto" cssClass="btn">
	              <s:param name="aperturaIniziale" value="true"/>inserisci modifica soggetto</s:a>
	            </s:if>
            <%-- </s:if>    --%>
            </p>    
            
        </s:if>     
            
            <!-- HIDDEN -->
            <s:hidden id="uidModDaAnnullare"    name="uidModDaAnnullare" />
            
            
            
              <!-- Modal -->
             <!--#include virtual="include/modal.html" --> 
                <!-- Fine Modal -->  
                <s:include value="/jsp/movgest/include/modal.jsp" />    
                <!-- Jira - 1585 -->                   
<!--          <p class="marginLarge">  -->
<%--   	   		<s:submit name="indietro" value="indietro" method="indietro" cssClass="btn btn-secondary" /> --%>
<!--   		</p>  -->
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
				url: '<s:url method="dettaglioModPopUp"/>',
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
			}
		});
		
	});
</script>
