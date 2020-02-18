<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


<s:if test="dettaglioSoggetto != null">
      <h4>Soggetto:&nbsp;<s:property value="dettaglioSoggetto.denominazione"/></h4>
      <p>
      <!-- gestione per salto su pagina sedi con soggetto e mdp bloccati -->
<%--            <s:url id="gestisciSediUrl" action="aggiornaSediSecondarie.do" escapeAmp="false"> --%>
<%--            			<s:param name="soggetto" value="dettaglioSoggetto.codiceSoggetto" /> --%>
<%--            			<s:param name="fromGestisciSede">true</s:param> --%>
<%-- 	        </s:url> --%>
	                
	              
<%--           <a class="btn btn-primary pull-right" href="<s:property value="%{gestisciSediUrl}"/>">Gestisci sedi secondarie</a> <br/> --%>
         
      </p>
      <s:if test="sediSecondarie!=null || sediSecondarie.size()>0">
         <display:table name="sediSecondarie" class="table table-hover" summary="riepilogo indirizzo" requestURI="elencoSoggetti.do" uid="sediSec" >
		  	<display:caption>Sedi secondarie</display:caption>
		  	<display:column title="Denominazione" property="denominazione" />
		  	<display:column title="Indirizzo"><s:property value="%{#attr.sediSec.indirizzoSoggettoPrincipale.sedime}"/> <s:property value="%{#attr.sediSec.indirizzoSoggettoPrincipale.denominazione}"/> <s:property value="%{#attr.sediSec.indirizzoSoggettoPrincipale.numeroCivico}" /> <s:property value="%{#attr.sediSec.indirizzoSoggettoPrincipale.cap}" /></display:column>
		  	<display:column title="Comune" property="indirizzoSoggettoPrincipale.comune" />
		  	<display:column title="Stato" property="descrizioneStatoOperativoSedeSecondaria" />
		  </display:table>
	  </s:if>
	  
      <!-- gestione per salto su pagina mdp con soggetto e sedi bloccate -->
<%--       <s:url id="gestisciMdpUrl" action="modalitaPagamentoSoggetto.do" escapeAmp="false"> --%>
<%--            			<s:param name="soggetto" value="dettaglioSoggetto.codiceSoggetto" /> --%>
<%--            			<s:param name="fromGestisciMdp">true</s:param> --%>
<%-- 	  </s:url> --%>
	                     
<%--       <a class="btn btn-primary pull-right" href="<s:property value="gestisciMdpUrl"/>">Gestisci modalit&agrave; di pagamento</a> --%>
      
	  <s:if test="modalitaPagamento!=null || modalitaPagamento.size()>0">
	      <display:table name="modalitaPagamento" class="table table-hover margin-large" summary="riepilogo modalita pagamento" requestURI="elencoSoggetti.do" uid="modalitaPagamentoSupport">
		    <display:caption>Modalit&agrave; pagamento</display:caption>
		   
		   <display:column title="Numero d'ordine" property="codiceModalitaPagamento" />
		   
		   <display:column title="Modalit&agrave;">
	    	  <a data-html="true" href="#" data-trigger="hover" rel="popover" data-container="#modTAB" 
			    	title="${modalitaPagamentoSupport.modalitaAccreditoSoggetto.codice}&nbsp;-&nbsp;${modalitaPagamentoSupport.modalitaAccreditoSoggetto.descrizione}" 
			    	data-content="${modalitaPagamentoSupport.descrizioneForPopOver}">${modalitaPagamentoSupport.descrizioneInfo.descrizioneArricchita}</a>
	    	</display:column>
	    	
	    	
		   
		    <%--  OLD 
		    <display:column title="Modalit&agrave;">
		    	<a href="#">${modalitaPagamentoSupport.modalitaAccreditoSoggetto.codice}&nbsp;-&nbsp;${modalitaPagamentoSupport.modalitaAccreditoSoggetto.descrizione}</a>
		    </display:column>
		    
		   
			<display:column title="Descrizione">
								<s:push value="%{#attr.modalitaPagamentoSupport}" >
									<s:include value="/jsp/include/descrizioneCompletaModalitaPagamento.jsp" />
								</s:push>
			</display:column>
			--%>
		    
		    
		  	<display:column title="<abbr title='progressivo'>Associato a</abbr>" property="associatoA"/>
		  	<display:column title="Stato" property="descrizioneStatoModalitaPagamento" />
		  </display:table>
	  </s:if>	  
</s:if>  