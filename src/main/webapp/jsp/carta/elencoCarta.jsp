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
  
  <s:form id="mainForm" method="post" action="elencoCarta.do">
  <div class="container-fluid-banner">

	<a name="A-contenuti" title="A-contenuti"></a>
	</div>



<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12 contentPage"> 
		
			<form method="post" action="elencoCarta.do">  
				<s:include value="/jsp/include/actionMessagesErrors.jsp" />	   
				<h3>Risultati ricerca carta contabile</h3>
<!-- 				<h4>Dati di ricerca xxxxxxxxxxxxx</h4>  -->

				<fieldset class="form-horizontal">
				
					<h4 class="step-pane"><span class="num_result"><s:property value="resultSize" /> </span> Risultati trovati</h4>
<!-- 					ElencoCarteContabili -->

                    <display:table name="elencoCarteContabili" 
									class="table table-hover tab_left" 
									summary="riepilogo carte contabili" 
									pagesize="10" requestURI="elencoCarta.do" 
									partialList="true" size="resultSize"
									uid="ricercaCarteContID" keepStatus="true" clearStatus="${clearPagina}">
									
									
							<display:column title="Numero" property="numero" />	 
							<display:column title="Data scadenza" property="dataScadenza" format="{0,date,dd/MM/yyyy}"/>	 
							<display:column title="Oggetto" property="oggetto" />	
							<display:column title="Stato" property="statoOperativoCartaContabile" />
							<display:column title="Provvedimento">
								
									
								<s:property value="%{#attr.ricercaCarteContID.attoAmministrativo.anno}"/> 
								/ <s:property value="%{#attr.ricercaCarteContID.attoAmministrativo.numero}"/>
								/ <s:property value="%{#attr.ricercaCarteContID.attoAmministrativo.strutturaAmmContabile.codice}"/> 
								/ <s:property value="%{#attr.ricercaCarteContID.attoAmministrativo.tipoAtto.codice}"/> 
								
							</display:column>
							<display:column title="Importo" property="importo"  		
									decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
						
							<display:column title="">
							
							<div class="btn-group"> 
										<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
										<ul class="dropdown-menu pull-right">
											<s:url id="consultaUrl" action="consultaCartaContabile.do" escapeAmp="false">
								        		<s:param name="anno" value="%{#attr.ricercaCarteContID.bilancio.anno}" />
								        		<s:param name="numero" value="%{#attr.ricercaCarteContID.numero}" />			        				        	
						                    </s:url>
											<li><a href="<s:property value="consultaUrl"/>">consulta</a></li>
										<s:if test="abilitaTasto('annulla', #attr.ricercaCarteContID.statoOperativoCartaContabile)">
											<li><a id="linkAnnulla_<s:property value="%{#attr.ricercaCarteContID.uid}"/>_<s:property value="%{#attr.ricercaCarteContID.numero}"/>_<s:property value="%{#attr.ricercaCarteContID.numero}"/>" href="#msgAnnullaCarta" data-toggle="modal" class="linkAnnullaCarta">annulla</a></li>
										</s:if>
										
						    			<s:url id="aggiornaUrl" action="gestioneCartaStep1.do" escapeAmp="false">
			        						<s:param name="annoCarta"   value="%{#attr.ricercaCarteContID.bilancio.anno}" />
			        						<s:param name="numeroCarta" value="%{#attr.ricercaCarteContID.numero}" />
					                    </s:url>
										<s:if test="abilitaTasto('aggiorna', #attr.ricercaCarteContID.statoOperativoCartaContabile)">	
											<!-- <li><a href="FIN-aggCartaContabileStep1.shtml">aggiorna</a></li> -->
											<li><a href="<s:property value="aggiornaUrl"/>" class="aggiornaButton">aggiorna</a></li>
										</s:if>
										
										
										<s:url id="regolarizzaUrl" action="gestRigheCarta.do" escapeAmp="false">
			        						<s:param name="anno"   value="%{#attr.ricercaCarteContID.bilancio.anno}" />
			        						<s:param name="numero" value="%{#attr.ricercaCarteContID.numero}" />
	                    				</s:url>
	                   
										
										<s:if test="abilitaTasto('regolarizza', #attr.ricercaCarteContID.statoOperativoCartaContabile)">	
											<li><a href="<s:property value="regolarizzaUrl"/>">regolarizza</a></li>
										</s:if>
										
										
										
										<s:if test="abilitaTasto('completa', #attr.ricercaCarteContID.statoOperativoCartaContabile)">
											<!-- <li><a href="#msgCompleta" data-toggle="modal">completa</a></li> -->
											<li><a id="linkCompletaCarta_COMPLETATO_<s:property value="%{#attr.ricercaCarteContID.numero}"/>_<s:property value="%{#attr.ricercaCarteContID.statoOperativoCartaContabile}"/>" href="#msgModificaStatoCarta" data-toggle="modal" class="linkCompletaCarta">completa</a></li>
										</s:if>
										
										<s:if test="abilitaTasto('trasmetti', #attr.ricercaCarteContID.statoOperativoCartaContabile)">	
											<!-- <li><a href="#msgTrasmesso" data-toggle="modal">trasmetti</a></li> -->
											<li><a id="linkCompletaCarta_TRASMESSO_<s:property value="%{#attr.ricercaCarteContID.numero}"/>_<s:property value="%{#attr.ricercaCarteContID.statoOperativoCartaContabile}"/>" href="#msgModificaStatoCarta" data-toggle="modal" class="linkCompletaCarta">trasmetti</a></li>
										</s:if>
										
										<s:if test="abilitaTasto('ripAProv', #attr.ricercaCarteContID.statoOperativoCartaContabile)">	
											<!-- <li><a href="#statoProvvisorio" data-toggle="modal">riporta a provvisorio</a></li> -->
											<li><a id="linkCompletaCarta_PROVVISORIO_<s:property value="%{#attr.ricercaCarteContID.numero}"/>_<s:property value="%{#attr.ricercaCarteContID.statoOperativoCartaContabile}"/>" href="#msgModificaStatoCarta" data-toggle="modal" class="linkCompletaCarta">riporta a provvisorio</a></li>
										</s:if>
										
										<s:if test="abilitaTasto('ripACompl', #attr.ricercaCarteContID.statoOperativoCartaContabile)">	
											<!-- <li><a href="#statoCompletato" data-toggle="modal">riporta a completato</a></li> -->
											<li><a id="linkCompletaCarta_COMPLETATO_<s:property value="%{#attr.ricercaCarteContID.numero}"/>_<s:property value="%{#attr.ricercaCarteContID.statoOperativoCartaContabile}"/>" href="#msgModificaStatoCarta" data-toggle="modal" class="linkCompletaCarta">riporta a completato</a></li>
										</s:if>	
										
											
										<s:url id="stampaUrl" action="stampaCartaContabile.do" escapeAmp="false">
			        						<s:param name="annoCarta"   value="%{#attr.ricercaCarteContID.bilancio.anno}" />
			        						<s:param name="numeroCarta" value="%{#attr.ricercaCarteContID.numero}" />
					                    </s:url>
										<li><a href="<s:property value="stampaUrl"/>" class="aggiornaButton">stampa</a></li>
											
										</ul>
 									</div>    
							</display:column> 		
									
					</display:table>				



				
				</fieldset>



				<s:hidden id="uidCartaDaAnnullare" name="uidCartaDaAnnullare"/>
        		<s:hidden id="numeroCartaDaAnnullare" name="numeroCartaDaAnnullare"/>
        		
        		<s:hidden id="numeroCartaDaModificare" name="numeroCartaDaModificare"/>
				<s:hidden id="nuovoStatoCartaDaModificare" name="nuovoStatoCartaDaModificare"/>
        		
        		
        		
        		
        		<!-- Modal  annulla Carta -->
            <div id="msgAnnullaCarta" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                <button type="button" class="close" data-dismiss="modal">&times;</button>                  
                  <p><strong>Attenzione!</strong></p>
                  <p><strong>Elemento selezionato: <s:textfield id="numeroCartaDaAnnullare2" name="numeroCartaDaAnnullare" disabled="true"/></strong></p>
                  <p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="annullaCarta"/>
              </div>
            </div>  
            <!--/modale annulla Carta -->
            
            <!-- Modal  Trasmetti Carta -->
            <div id="msgTrasmesso" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
            	<div class="modal-body">
					<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
		
					<fieldset class="form-horizontal">  
							<p><strong>Attenzione!</strong></p>
							<p>La carta viene portata in stato trasmesso: si ricorda di stampare il modulo per la tesoreria ......</p>
							<p>Sei sicuro di voler proseguire?</p>
							<div class="Border_line"></div>
						<div class="control-group">
							<label class="control-label" >Numero registrazione</label>
								<div class="controls">
									<s:textfield id="numRegistrazioneCarta" name="numRegistrazioneCarta" cssClass="span4 soloNumeri" />
								</div>
						</div>	
					</fieldset>  
		
					</div>
	
 				</div>
  
  				<div class="modal-footer">
					<button class="btn" data-dismiss="modal" aria-hidden="true">annulla</button>
               		<s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="trasmettiCarta"/>
              	</div>
            </div>  
            <!--/modale Trasmetti Carta -->
            
        	<!-- Modal  cambia stato Carta -->
           <div id="msgModificaStatoCarta" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgModificaStatoCartaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                <button type="button" class="close" data-dismiss="modal">&times;</button>                  
                  <p><strong>Attenzione!</strong></p>
                  <p id="msgModificaStatoCartaLbl"></p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="cambiaStatoCarta"/>
              </div>
            </div>
            <!--/modale completa Carta -->



			 
				<div class="Border_line"></div>
				
							<s:include value="/jsp/include/indietro.jsp" />
				
			  
			</form>
		  
		</div>
	</div>	 
</div>	
</s:form>

<s:include value="/jsp/include/footer.jsp" />

<script type="text/javascript">
	$(document).ready(function() {
		var mainForm = $("#mainForm");
		$(".linkAnnullaCarta").click(function() {
			var supportId = $(this).attr("id").split("_");
			
			if (supportId != null && supportId.length > 0) {
				$("#uidCartaDaAnnullare").val(supportId[1]);
				$("#numeroCartaDaAnnullare").val(supportId[2]); //utilizzato solo per accertamento
				$("#numeroCartaDaAnnullare2").val(supportId[3]);
				
			}
		});
		
		$(".linkCompletaCarta").click(function() {
			var supportId = $(this).attr("id").split("_");
			
			if (supportId != null && supportId.length > 0) {
				$("#nuovoStatoCartaDaModificare").val(supportId[1]);
				$("#numeroCartaDaModificare").val(supportId[2]);
				$("#statoCartaDaModificare").val(supportId[3]);
				$("#msgModificaStatoCartaLbl").html('FIN_INF_0047 - Il movimento in stato '+supportId[3]+' verr&#224; salvato in stato '+supportId[1]+': confermi l&#39;operazione?');
				
			}
		});
		
	});
</script>
  