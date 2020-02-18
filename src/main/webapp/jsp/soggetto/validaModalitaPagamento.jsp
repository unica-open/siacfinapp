<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


    <%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />
    
    
    <%-- Inclusione JavaScript NUOVO --%>
   <s:include value="/jsp/include/javascript.jsp" />

</head>

<s:include value="/jsp/include/header.jsp" />

  <body>
  
 
<div class="container-fluid-banner">
<a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->

<!--   TABELLE       RIEPILOGO    -->

<div class="container-fluid">
<div class="row-fluid">
<div class="span12">


		<p></p>
		<%-- Messaggio di ERROR --%>
		<s:if test="hasActionErrors()">
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
					<s:actionerror/>
				</ul>
			</div>
		</s:if>
	
	
		<s:if test="hasActionMessages()">
			<%-- Messaggio di INFO --%>
			<div class="alert alert-success">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
					<s:actionmessage />
				</ul>
			</div>
		</s:if>





<div class="contentPage">    
<s:form name="validaModalitaPropostaForm" action="validaModalitaPagamento.do" method="post" class="form-horizontal">
<s:hidden name="mdpId" value="%{mdpId}" />
  <div>	
    <ul class="nav nav-tabs">
    <li class="active">
    <a href="#soggetto" data-toggle="tab">Modalit&agrave; pagamento</a>
    </li>
  <!-- <li><a href="../BIL-UPB/BIL_aggUPBUscPrev.shtml">U.P.B.</a></li> -->
 <!--   <li><a href="#sedi" data-toggle="tab">Sedi Secondarie</a></li>
    <li><a href="#modalita" data-toggle="tab">Modalit&agrave; pagamento</a></li> -->
    </ul>
    
<div class="tab-content">
<div class="tab-pane active" id="soggetto" style="height: 290px;">
   <div class="span6">
    <s:if test="modalitaPagamentoSoggettoToValidate != null">
    	<h4>Attualmente presente</h4>
    </s:if>
    <s:if test="modalitaPagamentoSoggettoToValidate == null">
    	<h4>Da Validare</h4>
    </s:if> 
          <dl class="dl-horizontal well">
				<dt>Tipo Accredito</dt>
	            
	            <s:if test="%{modalitaPagamentoSoggettoOut.modalitaAccreditoSoggetto.codice != null}">
   		            <dd>${modalitaPagamentoSoggettoOut.modalitaAccreditoSoggetto.codice} - ${modalitaPagamentoSoggettoOut.modalitaAccreditoSoggetto.descrizione} &nbsp;</dd>	
				</s:if>	 			
				<s:else>
					<s:if test="%{modalitaPagamentoSoggettoToValidate.modalitaAccreditoSoggetto.codice != null}">
	 					<dd>${modalitaPagamentoSoggettoToValidate.modalitaAccreditoSoggetto.codice} - ${modalitaPagamentoSoggettoToValidate.modalitaAccreditoSoggetto.descrizione} &nbsp;</dd>
	 				</s:if>
	 			</s:else>
	 				 				 			
	 				<dt>Stato modalit&agrave;</dt>
	 			<s:if test="%{modalitaPagamentoSoggettoOut.descrizioneStatoModalitaPagamento != null && modalitaPagamentoSoggettoOut.descrizioneStatoModalitaPagamento.trim() != ''}">
	            	<dd>${modalitaPagamentoSoggettoOut.descrizioneStatoModalitaPagamento} &nbsp;</dd>	            	   
	            </s:if>        
	            <s:else >
	            	<s:if test="%{modalitaPagamentoSoggettoToValidate.descrizioneStatoModalitaPagamento != null && modalitaPagamentoSoggettoToValidate.descrizioneStatoModalitaPagamento.trim() != ''}">
	            		<dd>${modalitaPagamentoSoggettoToValidate.descrizioneStatoModalitaPagamento} &nbsp;</dd>
	            	</s:if>	
	            </s:else>
	            
				<dt>Codice Soggetto</dt>
				<dd>${model.dettaglioSoggetto.codiceSoggetto} &nbsp;</dd>
				<dt>Codice Fiscale Soggetto</dt>
				<dd>${model.dettaglioSoggetto.codiceFiscale} &nbsp;</dd>
				
				<dt>Iban</dt>
				<s:if test="%{modalitaPagamentoSoggettoOut.iban != null && modalitaPagamentoSoggettoOut.iban.trim() != ''}">
	         		<dd>${modalitaPagamentoSoggettoOut.iban} &nbsp;</dd>
	         	</s:if>
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
	         	
          	    <dt>Conto Corrente</dt>
	         	<s:if test="%{modalitaPagamentoSoggettoOut.contoCorrente != null && modalitaPagamentoSoggettoOut.contoCorrente.trim() != ''}">
	        	 	<dd>${modalitaPagamentoSoggettoOut.contoCorrente} &nbsp;</dd>
	 		 	</s:if>
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
	 		 	
	 		 	<!-- nuovi campi : intestazione conto -->
	 		 	
	          	    <dt>Intestazione Conto</dt>
	 		 	<s:if test="%{modalitaPagamentoSoggettoOut.intestazioneConto != null && modalitaPagamentoSoggettoOut.intestazioneConto.trim() != ''}">
	        	 	<dd>${modalitaPagamentoSoggettoOut.intestazioneConto} &nbsp;</dd>
	 		 	</s:if>
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
	 		 	
	 		 	<!-- fine - nuovi campi -->
	 		 	
	 		 		<dt>Bic</dt>
	 		 	<s:if test="%{modalitaPagamentoSoggettoOut.bic != null && modalitaPagamentoSoggettoOut.bic.trim() != ''}">
	         		<dd>${modalitaPagamentoSoggettoOut.bic} &nbsp;</dd>
	            </s:if>
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
	            
		            <dt>Quietanzante</dt>
	            <s:if test="%{modalitaPagamentoSoggettoOut.soggettoQuietanzante != null && modalitaPagamentoSoggettoOut.soggettoQuietanzante.trim() != ''}">
		   			<dd>${modalitaPagamentoSoggettoOut.soggettoQuietanzante} &nbsp;</dd>
		   		</s:if>	
		 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
		   		
					<dt>Codice Fiscale Quietanzante</dt>
		   		<s:if test="%{modalitaPagamentoSoggettoOut.codiceFiscaleQuietanzante != null && modalitaPagamentoSoggettoOut.codiceFiscaleQuietanzante.trim() != ''}">
			        <dd>${modalitaPagamentoSoggettoOut.codiceFiscaleQuietanzante} &nbsp;</dd>
			    </s:if>   
			    	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
			    
			    <!-- nuovi campi : data e luogo di nascita -->
			   
					<dt>Data Nascita Quietanzante</dt>
			    <s:if test="%{modalitaPagamentoSoggettoOut.dataNascitaQuietanzante != null && modalitaPagamentoSoggettoOut.dataNascitaQuietanzante.trim() != ''}">
			        <dd>${modalitaPagamentoSoggettoOut.dataNascitaQuietanzante} &nbsp;</dd>
			    </s:if> 
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
			    
					<dt>Luogo Nascita Quietanzante</dt>
			    <s:if test="%{modalitaPagamentoSoggettoOut.luogoNascitaQuietanzante != null}">
			        <dd>${modalitaPagamentoSoggettoOut.luogoNascitaQuietanzante} &nbsp;</dd>
			    </s:if> 
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
			    
			    <!-- fine - nuovi campi -->
			     
		  			<dt>Note</dt>
			    <s:if test="%{modalitaPagamentoSoggettoOut.note != null && modalitaPagamentoSoggettoOut.note.trim() != ''}">
				    <dd>${modalitaPagamentoSoggettoOut.note} &nbsp;</dd> 
				</s:if> 
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
				
		            <dt>Data scadenza</dt>
				<s:if test="%{modalitaPagamentoSoggettoOut.dataFineValidita != null}">
					<dd>
						<s:date name="modalitaPagamentoSoggettoOut.dataFineValidita" format="dd/MM/yyyy"/>  &nbsp;
					</dd>
         		</s:if>
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
				
		            <dt>Per stipendi</dt>
					<dd>
	         			<s:if test="modalitaPagamentoSoggettoOut.perStipendi">S&igrave;</s:if><s:else>No</s:else>
					</dd>
         </dl>       
	</div>
    
    
    <s:if test="%{modalitaPagamentoSoggettoToValidate != null}">
    <div class="span6"> 
    <h4>Da validare</h4>
         <dl class="dl-horizontal well">
				<dt>Tipo Accredito</dt>
	            <dd>${modalitaPagamentoSoggettoToValidate.modalitaAccreditoSoggetto.codice} - ${modalitaPagamentoSoggettoToValidate.modalitaAccreditoSoggetto.descrizione} &nbsp;</dd>         
				
	 				<dt>Stato modalit&agrave;</dt>
	 			<s:if test="%{modalitaPagamentoSoggettoOut.descrizioneStatoModalitaPagamento != null && modalitaPagamentoSoggettoOut.descrizioneStatoModalitaPagamento.trim() != ''}">
	            	<dd>${modalitaPagamentoSoggettoOut.descrizioneStatoModalitaPagamento} &nbsp;</dd>	            	   
	            </s:if>        
	            <s:else >
	            	<s:if test="%{modalitaPagamentoSoggettoToValidate.descrizioneStatoModalitaPagamento != null && modalitaPagamentoSoggettoToValidate.descrizioneStatoModalitaPagamento.trim() != ''}">
	            		<dd>${modalitaPagamentoSoggettoToValidate.descrizioneStatoModalitaPagamento} &nbsp;</dd>
	            	</s:if>	
	            </s:else>
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
				
				<dt>Codice Soggetto</dt>
				<dd>${model.dettaglioSoggetto.codiceSoggetto} &nbsp;</dd>
				<dt>Codice Fiscale Soggetto</dt>
				<dd>${model.dettaglioSoggetto.codiceFiscale} &nbsp;</dd>
				
									<dt>Iban</dt>
				<s:if test="%{modalitaPagamentoSoggettoToValidate.iban != null && modalitaPagamentoSoggettoToValidate.iban.trim() != ''}">
					<s:if test="%{modalitaPagamentoSoggettoToValidate.iban != modalitaPagamentoSoggettoOut.iban}">
						<dd style="background-color: yellow;">
					</s:if>
	         		<s:else>
	         			<dd>
	         		</s:else>
	         			${modalitaPagamentoSoggettoToValidate.iban} &nbsp;
	         		</dd>
	         	</s:if>
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
	         	
	          	    <dt>Conto Corrente</dt>
	         	<s:if test="%{modalitaPagamentoSoggettoToValidate.contoCorrente != null && modalitaPagamentoSoggettoToValidate.contoCorrente.trim() != ''}">
	        	 	<s:if test="%{modalitaPagamentoSoggettoToValidate.contoCorrente != modalitaPagamentoSoggettoOut.contoCorrente}">
						<dd style="background-color: yellow;">
					</s:if>
	         		<s:else>
	         			<dd>
	         		</s:else>
	        	 		${modalitaPagamentoSoggettoToValidate.contoCorrente} &nbsp;
	        	 	</dd>
	 		 	</s:if>
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
	 		 	
	 		 	<!-- nuovi campi : intestazione conto -->
	          	    <dt>Intestazione Conto</dt>
	 		 	<s:if test="%{modalitaPagamentoSoggettoToValidate.intestazioneConto != null && modalitaPagamentoSoggettoToValidate.intestazioneConto.trim() != ''}">
	        	 	<s:if test="%{modalitaPagamentoSoggettoToValidate.intestazioneConto != modalitaPagamentoSoggettoOut.intestazioneConto}">
						<dd style="background-color: yellow;">
					</s:if>
	         		<s:else>
	         			<dd>
	         		</s:else>
	        	 		${modalitaPagamentoSoggettoToValidate.intestazioneConto} &nbsp;
	        	 	</dd>
	 		 	</s:if>
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
	 		 	<!-- fine - nuovi campi -->
	 		 	
	 		 	
	 		 		<dt>Bic</dt>
	 		 	<s:if test="%{modalitaPagamentoSoggettoToValidate.bic != null && modalitaPagamentoSoggettoToValidate.bic.trim() != ''}">
	         		<s:if test="%{modalitaPagamentoSoggettoToValidate.bic != modalitaPagamentoSoggettoOut.bic}">
						<dd style="background-color: yellow;">
					</s:if>
	         		<s:else>
	         			<dd>
	         		</s:else>
	         			${modalitaPagamentoSoggettoToValidate.bic} &nbsp;
	         		</dd>
	            </s:if>
		 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
	            
		            <dt>Quietanzante</dt>
	            <s:if test="%{modalitaPagamentoSoggettoToValidate.soggettoQuietanzante != null && modalitaPagamentoSoggettoToValidate.soggettoQuietanzante.trim() != ''}">
		   			<s:if test="%{modalitaPagamentoSoggettoToValidate.soggettoQuietanzante != modalitaPagamentoSoggettoOut.soggettoQuietanzante}">
						<dd style="background-color: yellow;">
					</s:if>
	         		<s:else>
	         			<dd>
	         		</s:else>
		   				${modalitaPagamentoSoggettoToValidate.soggettoQuietanzante} &nbsp;
		   			</dd>
		   		</s:if>	
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>

					<dt>Codice Fiscale Quietanzante</dt>
		   		<s:if test="%{modalitaPagamentoSoggettoToValidate.codiceFiscaleQuietanzante != null && modalitaPagamentoSoggettoToValidate.codiceFiscaleQuietanzante.trim() != ''}">
		   			<s:if test="%{modalitaPagamentoSoggettoToValidate.codiceFiscaleQuietanzante != modalitaPagamentoSoggettoOut.codiceFiscaleQuietanzante}">
						<dd style="background-color: yellow;">
					</s:if>
	         		<s:else>
	         			<dd>
	         		</s:else>
			        	${modalitaPagamentoSoggettoToValidate.codiceFiscaleQuietanzante} &nbsp;
			        </dd>
			    </s:if>    
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
			    
			    <!-- nuovi campi : data e luogo di nascita -->
			    
					<dt>Data Nascita Quietanzante</dt>
			    <s:if test="%{modalitaPagamentoSoggettoToValidate.dataNascitaQuietanzante != null && modalitaPagamentoSoggettoToValidate.dataNascitaQuietanzante.trim() != ''}">
		   			<s:if test="%{modalitaPagamentoSoggettoToValidate.dataNascitaQuietanzante != modalitaPagamentoSoggettoOut.dataNascitaQuietanzante}">
						<dd style="background-color: yellow;">
					</s:if>
	         		<s:else>
	         			<dd>
	         		</s:else>
			        	${modalitaPagamentoSoggettoToValidate.dataNascitaQuietanzante} &nbsp;
			        </dd>
			    </s:if>   
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
			    
					<dt>Luogo Nascita Quietanzante</dt>
			     <s:if test="%{modalitaPagamentoSoggettoToValidate.comuneNascita != null}">
		   			<s:if test="%{modalitaPagamentoSoggettoToValidate.comuneNascita.descrizione != modalitaPagamentoSoggettoOut.luogoNascitaQuietanzante}">
						<dd style="background-color: yellow;">
					</s:if>
	         		<s:else>
	         			<dd>
	         		</s:else>
			        	${modalitaPagamentoSoggettoToValidate.comuneNascita.descrizione} &nbsp;
			        </dd>
			    </s:if>   
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>
			    
			    <!-- fine - nuovi campi -->
			    
			    
			    
		  			<dt>Note</dt>
			    <s:if test="%{modalitaPagamentoSoggettoToValidate.note != null && modalitaPagamentoSoggettoToValidate.note.trim() != ''}">
		   			<s:if test="%{modalitaPagamentoSoggettoToValidate.note != modalitaPagamentoSoggettoOut.note}">
						<dd style="background-color: yellow;">
					</s:if>
	         		<s:else>
	         			<dd>
	         		</s:else>
				    	${modalitaPagamentoSoggettoToValidate.note} &nbsp;
				    </dd> 
				</s:if>    
	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>

		            <dt>Data scadenza</dt>
				<s:if test="%{modalitaPagamentoSoggettoToValidate.dataFineValidita != null}">
					<s:if test="%{modalitaPagamentoSoggettoToValidate.dataFineValidita != modalitaPagamentoSoggettoOut.dataFineValidita}">
						<dd style="background-color: yellow;">
					</s:if>
	         		<s:else>
	         			<dd>
	         		</s:else>
	         			<s:date name="modalitaPagamentoSoggettoToValidate.dataFineValidita" format="dd/MM/yyyy"/>  &nbsp;
					</dd>
         		</s:if>
 	 		 	<s:else>
	 		 		<dd>&nbsp;</dd>
	 		 	</s:else>

	            <dt>Per stipendi</dt>
				<s:if test="%{modalitaPagamentoSoggettoToValidate.perStipendi != modalitaPagamentoSoggettoOut.perStipendi}">
					<dd style="background-color: yellow;">
				</s:if>
         		<s:else>
         			<dd>
         		</s:else>
         			<s:if test="modalitaPagamentoSoggettoToValidate.perStipendi">S&igrave;</s:if><s:else>No</s:else>
				</dd>
         </dl>  
	</div>
	</s:if>
    
</div>


 
 <!-- Modal -->
<div id="msgRifiuta" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgRifiutaLabel" aria-hidden="true">
<div class="modal-body">
<div class="alert alert-error">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
    <p><strong>Attenzione!</strong></p>
    <p>Stai per rifiutare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
</div>
</div>
<div class="modal-footer">
	<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
	<s:submit method="rifiutaValidazione" name="rifiutaValidazione" value="si, prosegui" cssClass="btn btn-primary rifiuta-prosegui" />
</div>
</div>  
  <!--/modale Rifiuta -->  
  
<!-- Modal Valida -->
<div id="msgValida" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgValidaLabel" aria-hidden="true">
<div class="modal-body">
<div class="alert alert-error">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
    <p><strong>Attenzione!</strong></p>
    <p>Stai per validare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
</div>
</div>
<div class="modal-footer">
	<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
	<s:submit method="validaProposta" name="validaProposta" value="si, prosegui" cssClass="btn btn-primary valida-prosegui" />
</div>
</div>  
  <!--/modale Valida --> 
 
 
 
 
 </div>
 
 
 	<div style="margin-top:10px;">
 		<s:include value="/jsp/include/indietro.jsp" />
 		<span class="nascosto"> &nbsp;|&nbsp;</span>&nbsp;
 		<a href="#msgRifiuta" data-toggle="modal" class="btn btn-primary">rifiuta</a>
 		<span class="nascosto"> &nbsp;|&nbsp;</span> &nbsp;
 		<a href="#msgValida"  data-toggle="modal" class="btn btn-primary pull-right">valida</a>
 	</div>
 </div>

 
</s:form>
</div>	
</div>	
</div>
</div>	 	
<s:include value="/jsp/include/footer.jsp" />