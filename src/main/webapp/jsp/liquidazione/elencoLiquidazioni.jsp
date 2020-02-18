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
<%--    <s:include value="/jsp/include/javascriptTree.jsp" /> --%>
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
			
				<s:form id="mainForm" method="post" action="elencoLiquidazioni.do">  

<s:include value="/jsp/include/actionMessagesErrors.jsp" />
		<h3>Elenco liquidazioni</h3>		
		<s:if test="resultSize!=0">
			<h4><span class="num_result"><s:property value="resultSize"/> </span> Risultati trovati</h4>
		</s:if>

<h3>Risultati di ricerca delle liquidazioni</h3>

<display:table name="elencoLiquidazioni" 
	class="table table-hover tab_left" 
							 keepStatus="true"
							 clearStatus="false"
	                 summary="riepilogo liquidazioni"
	                 pagesize="10"
	                 partialList="true" size="resultSize"
	                 requestURI="elencoLiquidazioni.do"
					 uid="ricercaLiquidazioniID">
	
		<display:column title="Anno" property="annoLiquidazione" />
		<display:column title="Numero" property="numeroLiquidazione" />	
		<display:column title="Creditore">	
			<s:property value="%{#attr.ricercaLiquidazioniID.soggettoLiquidazione.codiceSoggetto}" /> - <s:property value="%{#attr.ricercaLiquidazioniID.soggettoLiquidazione.denominazione}" /> 
		</display:column> 
		<display:column title="Descrizione" property="descrizioneLiquidazione" />	 
		<display:column title="Stato" property="codiceStatoOperativoLiquidazione"/>
		<display:column title="Numero Impegno" >
	        <s:if test="isSubImpegno(#attr.ricercaLiquidazioniID.subImpegno.numero)">
	        	<s:property value="%{#attr.ricercaLiquidazioniID.impegno.annoMovimento}" /> / <s:property value="%{#attr.ricercaLiquidazioniID.impegno.numero.intValue()}" /> / <s:property value="%{#attr.ricercaLiquidazioniID.subImpegno.numero.intValue()}" />
	        </s:if>
	        <s:else>
	        	<s:property value="%{#attr.ricercaLiquidazioniID.impegno.annoMovimento}" /> / <s:property value="%{#attr.ricercaLiquidazioniID.impegno.numero.intValue()}" />
	        </s:else>
	        <s:if test="isMaggioreDiZero(#attr.ricercaLiquidazioniID.numeroMutuo)">
	        	- <s:property value="%{#attr.ricercaLiquidazioniID.numeroMutuo}" /> 
	        </s:if>
		</display:column> 
		<display:column title="Capitolo" >
			<s:property value="%{#attr.ricercaLiquidazioniID.capitoloUscitaGestione.annoCapitolo}"/> / <s:property value="%{#attr.ricercaLiquidazioniID.capitoloUscitaGestione.numeroCapitolo}"/> / <s:property value="%{#attr.ricercaLiquidazioniID.capitoloUscitaGestione.numeroArticolo}"/> / <s:property value="%{#attr.ricercaLiquidazioniID.capitoloUscitaGestione.numeroUEB}"/> 
		</display:column>
		<display:column title="Provvedimento" >
			<a href="#" data-trigger="hover" rel="popover" title="Oggetto" data-content="<s:property value="%{#attr.ricercaLiquidazioniID.attoAmministrativoLiquidazione.oggetto}"/>">
				<s:property value="%{#attr.ricercaLiquidazioniID.attoAmministrativoLiquidazione.anno}"/> / 
				<s:property value="%{#attr.ricercaLiquidazioniID.attoAmministrativoLiquidazione.numero}"/>/
				<s:property value="%{#attr.ricercaLiquidazioniID.attoAmministrativoLiquidazione.strutturaAmmContabile.codice}"/>/
				<s:property value="%{#attr.ricercaLiquidazioniID.attoAmministrativoLiquidazione.tipoAtto.codice}"/>
			</a>
		</display:column>
		<display:column title="Importo" property="importoLiquidazione"  
                      decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>

		<display:column title="">
		<s:url id="aggiornaLiquidazioneUrl" action="inserisciLiquidazioneStep3Aggiorna.do" escapeAmp="false">
			<s:param name="anno" value="%{#attr.ricercaLiquidazioniID.annoLiquidazione}" />
			<s:param name="numero" value="%{#attr.ricercaLiquidazioniID.numeroLiquidazione}" />
	   	</s:url>
		    	<div class="btn-group">
		    		<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
		    		<ul class="dropdown-menu pull-right">
                  	 	<s:url id="consultaUrl" action="consultaLiquidazione.do" escapeAmp="false">
			        		<s:param name="anno" value="%{#attr.ricercaLiquidazioniID.annoLiquidazione}" />
			        		<s:param name="numero" value="%{#attr.ricercaLiquidazioniID.numeroLiquidazione}" />			        				        	
	                    </s:url>
	               		<li><a href="<s:property value="%{consultaUrl}"/>">consulta</a></li>
	               		
	               		<s:if test="isAbilitatoGestisciLiquidazione()">
		               		<s:if test="isAbilitatoAggiorna(#attr.ricercaLiquidazioniID.statoOperativoLiquidazione)">
								<li><a href="<s:property value="aggiornaLiquidazioneUrl"/>" class="aggiornaButton">aggiorna</a></li>
							</s:if> 
						</s:if> 
	               		
	               		
	               		<s:if test="isAbilitatoGestisciLiquidazione()">
							<s:if test="isAbilitatoAnnulla(#attr.ricercaLiquidazioniID.statoOperativoLiquidazione)">
								<li><a id="linkAnnulla_<s:property value="%{#attr.ricercaLiquidazioniID.annoLiquidazione}"/>_<s:property value="%{#attr.ricercaLiquidazioniID.numeroLiquidazione}"/>_<s:property value="%{#attr.ricercaLiquidazioniID.annoLiquidazione + '/' + #attr.ricercaLiquidazioniID.numeroLiquidazione}"/>" href="#msgAnnullaLiquidazione" data-toggle="modal" class="linkAnnullaLiquidazione">annulla</a>
							</s:if>
						</s:if>

		    		</ul>
				</div>
		 </display:column>
</display:table>
        
        <s:hidden id="annoLiquidazioneAnnullato" name="annoLiquidazioneAnnullato"/>
        <s:hidden id="numeroLiquidazioneAnnullato" name="numeroLiquidazioneAnnullato"/>
        
			<!-- Modal -->
				 <!-- Modal  annulla Liquidazione -->
            <div id="msgAnnullaLiquidazione" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                <button type="button" class="close" data-dismiss="modal">&times;</button>                  
                  <p><strong>Attenzione!</strong></p>
                  <p><strong>Elemento selezionato: <s:textfield id="liquidazioneDaAnnullare" name="liquidazioneDaAnnullare" disabled="true"/></strong></p>
                  <p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" action="annullaLiquidazione"/>
              </div>
            </div>  
            <!--/modale annulla Liquidazione -->
            
			<!-- Fine Modal -->
        
        <p>
		<s:if test="isAbilitatoInserisciLiquidazione() || isAbilitatoInserisciLiquidazioneMan()">
			<s:if test="isFaseBilancioAbilitata()">
				<s:submit name="inserisci" value="inserisci liquidazione" method="inserisciLiquidazione" cssClass="btn btn-secondary" />
			</s:if>	
		</s:if>	
		</p>
        
        <p class="marginLarge"> 
  	   		<s:include value="/jsp/include/indietro.jsp" />
  		</p>  	                                 
      </s:form>
      </div>	
    </div>	
  </div>	 
</div>	

<s:include value="/jsp/include/footer.jsp" />
<script type="text/javascript">
	$(document).ready(function() {
		var mainForm = $("#mainForm");
		$(".linkAnnullaLiquidazione").click(function() {
			var supportId = $(this).attr("id").split("_");
			if (supportId != null && supportId.length > 0) {
				$("#annoLiquidazioneAnnullato").val(supportId[1]);
				$("#numeroLiquidazioneAnnullato").val(supportId[2]);
				$("#liquidazioneDaAnnullare").val(supportId[3]);				
			}
		});
		
	});
	
</script>

			