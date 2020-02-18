<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
  <%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

<%-- Inclusione head e CSS NUOVO --%>
<s:include value="/jsp/include/head.jsp" />

<%-- Inclusione JavaScript NUOVO --%>
<s:include value="/jsp/include/javascript.jsp" />
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
    <div class="span12 ">
      <div class="contentPage">
	  <form method="get" action="#">
		

	<s:include value="/jsp/include/actionMessagesErrors.jsp" />
		<h3>Elenco mutui</h3>		
		<s:if test="resultSize!=0">
			<h4><span class="num_result"><s:property value="resultSize"/> </span> Risultati trovati</h4>
		</s:if>
		
				
         <display:table name="elencoMutui"  
	                 class="table table-hover tab_left" 
	                 summary="riepilogo mutui"
	                 pagesize="10"
	                 partialList="true" size="resultSize"
	                 requestURI="elencoMutui.do"
					 uid="ricercaMutuiID" keepStatus="true" clearStatus="${clearPagina}" >
					 
					 <display:column title="Nr. mutuo">
					 	<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="<s:property value="%{#attr.ricercaMutuiID.descrizioneMutuo}"/>">
					      <s:property value="%{#attr.ricercaMutuiID.codiceMutuo}"/>
					    </a>
					 </display:column>
					 <display:column title="Istituto mutuante">
					 	<s:property value="%{#attr.ricercaMutuiID.soggettoMutuo.codiceSoggetto}" />&nbsp;<s:property value="%{#attr.ricercaMutuiID.soggettoMutuo.denominazione}" />
					 </display:column>
					 <display:column title="Tipo mutuo" property="classificatoreTipoMutuo.descrizione" />
					 <%-- <display:column title="Data inizio" property="dataInizioValidita" format="{0,date,dd/MM/yyyy}"/> --%>
					 <display:column title="Data inizio" property="dataInizioMutuo" format="{0,date,dd/MM/yyyy}"/>
					 <display:column title="Stato">
					 	<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="${attr.ricercaMutuiID.classificatoreStatoOperativoMutuo.descrizione}">
					      <s:property value="%{#attr.ricercaMutuiID.classificatoreStatoOperativoMutuo.codice}"/>
					    </a>
					 </display:column>
					 <display:column title="Importo attuale" property="importoAttualeMutuo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
					 <display:column>
					 <s:url id="aggiornaMutuoUrl" action="gestioneMutuo.do" escapeAmp="false">
			        	<s:param name="codiceMutuo" value="%{#attr.ricercaMutuiID.codiceMutuo}" />
	                 </s:url>
	                 <s:url id="vociDiMutuoUrl" action="elencoVociDiMutuo.do" escapeAmp="false">
			        	<s:param name="codiceMutuo" value="%{#attr.ricercaMutuiID.codiceMutuo}" />
	                 </s:url>
					 	<div class="btn-group"> 
							<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni <span class="caret"> </span></button> 
									<ul class="dropdown-menu pull-right"> 
										
										<s:if test="isAbilitatoAggiorna(#attr.ricercaMutuiID.classificatoreStatoOperativoMutuo.codice)">
											<li><a href="<s:property value="vociDiMutuoUrl"/>" class="aggiornaButton">voci mutuo</a></li>
										</s:if>
										
										<s:if test="isAbilitatoAggiorna(#attr.ricercaMutuiID.classificatoreStatoOperativoMutuo.codice)">
									    	<li><a href="<s:property value="aggiornaMutuoUrl"/>" class="aggiornaButton">aggiorna</a></li>
									    </s:if> 
									    
				                  		<s:url id="consultaUrl" action="consultaMutuo.do" escapeAmp="false">
							        		<s:param name="numero" value="%{#attr.ricercaMutuiID.codiceMutuo}" />
					                    </s:url>
					                    <li><a href="<s:property value="%{consultaUrl}"/>">consulta</a></li>
					                    
										<s:if test="isAbilitatoAnnulla(#attr.ricercaMutuiID.classificatoreStatoOperativoMutuo.codice)">								 
											
											<li><a id="linkAnnulla_<s:property value="%{#attr.ricercaMutuiID.codiceMutuo}"/>_<s:property value="%{#attr.ricercaMutuiID.codiceMutuo}"/>" href="#msgAnnullaMutuo" data-toggle="modal" class="linkAnnullaMutuo">annulla</a>
										</s:if>	
									</ul> 
 						 </div> 
					 </display:column>
          </display:table>							 
		

			<s:if test="isInseribile()">
				<p><a class="btn"  href="gestioneMutuo.do">inserisci mutuo</a></p>  
			</s:if>	
			
			
			<s:hidden id="codiceMutuoDaPassare" name="codiceMutuoAnnullato"/>
			
			<!-- Modal -->
				 <!-- Modal  annulla Mutuo -->
            <div id="msgAnnullaMutuo" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                  <p><strong>Elemento selezionato:<s:textfield id="codiceMutuoDaAnnullare" name="codiceMutuoDaAnnullare" disabled="true"/> </strong></p>
                  <p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" action="annullaMutuo"/>
              </div>
            </div>  
            <!--/modale annulla Mutuo -->
            
			<!-- Fine Modal -->
			<div class="Border_line"></div>
			<p> <s:include value="/jsp/include/indietro.jsp" />   </p>  	                                 
      </form>
      </div>	
    </div>	
  </div>	 
</div>	
<script type="text/javascript">
	$(document).ready(function() {
		
		$(".linkAnnullaMutuo").click(function() {
			var supportId = $(this).attr("id").split("_");
			
			if (supportId != null && supportId.length > 0) {
				$("#codiceMutuoDaAnnullare").val(supportId[1]);
				$("#codiceMutuoDaPassare").val(supportId[2]);
				
			}
		});
		
	});
</script>
<s:include value="/jsp/include/footer.jsp" />
