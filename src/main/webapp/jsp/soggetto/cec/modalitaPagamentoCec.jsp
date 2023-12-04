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
<div class="span12 ">

<div class="contentPage">  
   <%-- SIAC-7952 rimuovo .do dalla action --%>
   <s:form method="post" action="modalitaPagamentoSoggettoCec">
   <s:include value="/jsp/include/actionMessagesErrors.jsp" />

						<ul class="nav nav-tabs">
						
							<s:hidden name="fromGestisciSede"/> 
							<s:hidden name="soggetto"/> 
					 	
			
							<li>
							<%-- task-131 <s:a action="aggiornaSoggettoCec" method="doExecute">--%>
							<s:a action="aggiornaSoggettoCec_doExecute">Soggetto</s:a></li>
							<li class="active"><a href="#">Modalit&agrave; pagamento</a></li>
						
						</ul>


		<h3>Codice Soggetto: <s:property value="dettaglioSoggetto.codiceSoggetto" /> 
		-  <s:property value="dettaglioSoggetto.denominazione" /> 
		 (<s:property value="dettaglioSoggetto.statoOperativo" /> dal <s:property value="%{dettaglioSoggetto.dataValidita}" />) </h3>     

						<h4>Modalit&agrave; di Pagamento</h4>
	   <%int indice = 0; %>
      <display:table name="model.dettaglioSoggetto.modalitaPagamentoList" 
                     class="table tab_left table-hover" summary="riepilogo modalita pagamento" 
                     requestURI="modalitaPagamentoSoggettoCec.do" uid="modPag">
                     
        <display:column title="Codice" property="codiceModalitaPagamento" />             
	    <display:column title="Modalita&grave;">
	    	${modPag.modalitaAccreditoSoggetto.codice}&nbsp;-&nbsp;${modPag.modalitaAccreditoSoggetto.descrizione}    	   	
	    </display:column>
	    
		<display:column title="Descrizione">
								<s:push value="%{#attr.modPag}" >
									<s:include value="/jsp/include/descrizioneCompletaModalitaPagamento.jsp" />
								</s:push>
		</display:column>

	    
	  	<display:column title="<abbr title='progressivo'>Associato a</abbr>" property="associatoA"/>
	  	<display:column title="Stato" property="descrizioneStatoModalitaPagamento" />
	  	<display:column title="" class="tab_Right">
       <div class="btn-group">
       	<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>         
        	<ul class="dropdown-menu pull-right" id="ul_action_<s:property value="%{#attr.ricercaSoggettoID.codiceSoggetto}"/>">         
          	<s:if test="isAbilitato(1, #attr.modPag.uid)">
          		<li><a href="#consultaModPag_<%=indice%>" data-toggle="modal">consulta</a></li>
          	</s:if>
          	<s:if test="isAbilitato(2, #attr.modPag.uid)">
          		<li><a href="modalitaPagamentoSoggettoCec_aggiornaMDP.do?aggiornaCodiceSoggetto=${modPag.uid}" data-toggle="modal">aggiorna</a></li>
          	</s:if>    
          	<s:if test="isAbilitato(3, #attr.modPag.uid)">
          		<li><a href="#msgAnnulla" onclick="$('.annulla-prosegui').attr('href', 'modalitaPagamentoSoggettoCec_annullaModalitaDiPagamento.do?aggiornaCodiceSoggetto=' + ${modPag.uid})"  data-toggle="modal">annulla</a></li>
          	</s:if>
          	<s:if test="isAbilitato(6, #attr.modPag.uid)">
          		<li><a href="#msgElimina" onclick="$('.elimina-prosegui').attr('href', 'modalitaPagamentoSoggettoCec_eliminaMdp.do?aggiornaCodiceSoggetto=' + ${modPag.uid})"  data-toggle="modal">elimina</a></li>
          	</s:if>
          
           <%indice++; %>                
         </ul>
      </div>
   </display:column>
	  </display:table>

<!-- Start Sezione Inserimento -->

<s:if test="%{azioneAggiorna == false}">
<p style="margin-bottom:10px;" class="margin-medium">
	<a class="btn" href="modalitaPagamentoSoggettoCec_panelInsertPayment.do" data-toggle="collapse" data-target="#inseriscipag">inserisci nuove modalit&agrave;</a>
</p>
</s:if>


<s:if test="%{inserisciModPag}">
 <div id="inseriscipag">
 <a id="ancoretta"></a>
    <div class="accordion_info"> 
       <fieldset class="form-horizontal">
       <s:form name="handlePaymentType" action="modalitaPagamentoSoggettoCec" method="post">
           <div class="control-group">
	           	<label class="control-label" for="tipoind">Associato a *</label>
				<div class="controls">
				<s:if test="null!=modelWeb.associataList">
					<s:select list="modelWeb.associataList" id="tipoind" name="modelWeb.tipoind"/>
	            </s:if>	
		       	</div>
			</div>
		
         	<div class="control-group">
            	<label class="control-label" for="tipoind">Tipo accredito *</label>
				<div class="controls">
				    <s:if test="null!=listaTipoAccredito">
						<s:select  list="listaTipoAccredito" id="idAccreditoTipoSelected" name="modelWeb.idAccreditoTipoSelected" listKey="id" listValue="%{codice + ' - ' + descrizione}" headerKey="-1" headerValue="Scegli il tipo di accredito" title="Scegli il tipo di accredito" onchange="handlePayment();" />
					</s:if> 
				</div>
			</div>
			<style type="text/css">
				.hideButton{display:none !important;}
			</style>
			<!-- task-131 <s:submit id="submitPaymentType" name="handle_payment_type" value="seleziona tipologia" method="handleTypePayment" cssClass="hideButton" /> -->
			<s:submit id="submitPaymentType" name="handle_payment_type" value="seleziona tipologia" action="modalitaPagamentoSoggettoCec_handleTypePayment" cssClass="hideButton" />	
			
			<script type="text/javascript">
				function handlePayment(){
					//todo se la select value e' scegli etc etc non fa nulla
					if($('#idAccreditoTipoSelected').val() != '-1'){
						$('#submitPaymentType').click();
					}
					
				}
			
			</script>
			</s:form>
   		</fieldset>                     
  	</div>
</div>	
</s:if>

<s:hidden id="ancoraVisualizza" name="ancoraVisualizza"/>

<!--<div class="btn-group">
             <button class="btn dropdown-toggle" data-toggle="dropdown">inserisci nuove modalit&agrave;<span class="caret"></span></button>
           <ul class="dropdown-menu pull-right">
               <li><a href="#inscontocorr" data-toggle="collapse" data-target="#inscontocorr">conto corrente</a></li>
             <li><a href="#inscontanti" data-toggle="collapse" data-target="#inscontanti">contanti</a></li>
             <li><a href="#inscessione" data-toggle="collapse" data-target="#inscessione">cessione credito</a></li>
            
           </ul>
         </div>
 --><!-- <p>
         <a class="btn"  data-toggle="collapse" data-target="#insInd">inserisci nuove modalit&agrave;</a>
        </p>  --> 
             
<s:if test="%{contoCorrenteVisible}">  
<s:form name="insertModPagContoCorrente" action="modalitaPagamentoSoggettoCec" method="post"> 
<s:hidden name="tipoAccreditoPage" id="tipoAccreditoPage" value="tipoAccreditoPage" />                                    
<div id="inscontocorr">
<a id="ancorettaMdp"></a>
	<div class="accordion_info"> 
    	<fieldset class="form-horizontal">
    	<s:if test="%{modelWeb.codeMdpSelected != 'CBI'}">
			<div class="control-group">
			
            	<label class="control-label" for="iban">IBAN </label>
                <div class="controls">
                	<s:textfield id="ibanToInsert" name="modalitaPagamentoSoggettoToInsert.iban" cssClass="span3 required" />
                </div>
			</div>
		</s:if> 	
			
			<s:if test="%{modelWeb.codeMdpSelected != 'CCP' && modelWeb.codeMdpSelected != 'CBI' }">
			<div class="control-group">
	                <label class="control-label" for="bic">BIC </label>
	                <div class="controls">
	                	<s:textfield id="bicToInsert" name="modalitaPagamentoSoggettoToInsert.bic" cssClass="span3 required" />
	                </div>
            </div> 
            </s:if>   
                                                    
            <div class="control-group">
                <label class="control-label" for="conto">Numero conto</label>
                <div class="controls">
                	<s:textfield id="ncontoCorrenteToInsert" name="modalitaPagamentoSoggettoToInsert.contoCorrente" cssClass="span3 required" />
                </div>
            </div>   
            
  		<div class="control-group">
				<label class="control-label" for="denominazioneBanca">Denominazione banca</label>
				<div class="controls">
					<s:textfield id="ndenominazioneBancaToInsert"
						name="modalitaPagamentoSoggettoToInsert.denominazioneBanca"
						cssClass="span3 required" />
				</div>
			</div>
            
            
            
            <div class="control-group">
                <label class="control-label" for="intconto">Intestazione conto</label>
                <div class="controls">
                	<s:textfield id="nIntestazioneToInsert" name="modalitaPagamentoSoggettoToInsert.intestazioneConto" maxlength="499" cssClass="span3 required" />
                </div>
            </div>   
                    
            <div class="control-group">
                <label class="control-label" for="scadenza">Data cessazione</label>
                <div class="controls">
                	<s:textfield id="scadenzaToInsert" name="dataScadenzaStringa" maxlength="10" cssClass="span3 datepicker"/>
                </div>
            </div> 
            <div class="control-group">
    	  		<label class="control-label" for="notee">Note</label>
                <div class="controls"> 
                	<s:textarea id="notaToInsert" name="modalitaPagamentoSoggettoToInsert.note" cssClass="span3" />
                </div>
            </div>
            	       
  		</fieldset>                     
  		<p>
  			<a class="btn" href="modalitaPagamentoSoggettoCec_annullaInserimento.do">annulla inserimento</a>
  			<!-- task-131 <s:submit name="inserisciModPagCorrente" value="Salva" id="salvaId" cssClass="btn" method="inserisciModPagContoCorrente" /> -->
  			<s:submit name="inserisciModPagCorrente" value="Salva" id="salvaId" cssClass="btn" action="modalitaPagamentoSoggettoCec_inserisciModPagContoCorrente" />
  		</p> 
  	</div>
</div>
</s:form> 
</s:if>   











<s:if test="%{genericoVisibile}">  
<s:form name="insertModPagGenerico" action="modalitaPagamentoSoggettoCec" method="post"> 
<s:hidden name="tipoAccreditoPage" id="tipoAccreditoPage" value="tipoAccreditoPage" />                                    
<div id="ins-generico">
<a id="ancorettaMdp"></a>
	<div class="accordion_info"> 
    	<fieldset class="form-horizontal">
    
                                                    
                 
            <div class="control-group">
                <label class="control-label" for="scadenza">Data cessazione</label>
                <div class="controls">
                	<s:textfield id="scadenzaToInsert" name="dataScadenzaStringa" maxlength="10" cssClass="span3 datepicker"/>
                </div>
            </div> 
            <div class="control-group">
    	  		<label class="control-label" for="notee">Note</label>
                <div class="controls"> 
                	<s:textarea id="notaToInsert" name="modalitaPagamentoSoggettoToInsert.note" cssClass="span3" />
                </div>
            </div>
            	       
  		</fieldset>                     
  		<p>
  			<a class="btn" href="modalitaPagamentoSoggettoCec_annullaInserimento.do">annulla inserimento</a>
  			<s:submit name="inserisciModPagGenerico" value="Salva" id="salvaId" cssClass="btn" action="modalitaPagamentoSoggettoCec_inserisciModPagGenerico" />
  		</p> 
  	</div>
</div>
</s:form> 
</s:if>  

















				<s:if test="not hasActionErrors()">
<s:hidden id="ancoraMdpVisualizza" name="ancoraMdpVisualizza"/>
</s:if>
              
<s:if test="%{contanteVisible}">
<s:form name="inserisciModPagContante" action="modalitaPagamentoSoggettoCec" method="post">
<s:hidden name="tipoAccreditoPage" id="tipoAccreditoPage" value="tipoAccreditoPage" />
<div id="inscontante">
<a id="ancorettaMdp"></a>
	<div class="accordion_info"> 
    	<fieldset class="form-horizontal">     
        	<div class="control-group">
            	<label class="control-label" for="nomevia">Codice fiscale Quietanzante</label>
                <div class="controls">
                	<s:textfield id="codFiscale" name="modalitaPagamentoSoggettoToInsert.codiceFiscaleQuietanzante" cssClass="span3" />
               	</div>
		    </div>
                                                    
            <div class="control-group">
            	<label class="control-label" for="conto">Quietanzante</label>
                <div class="controls">
                	<s:textfield id="quietanzante" name="modalitaPagamentoSoggettoToInsert.soggettoQuietanzante" cssClass="span3"/>
                </div>
            </div>
             
             <div class="control-group">
            	<label class="control-label" for="dataNascita">Data di nascita quietanzante</label>
                <div class="controls">
                	<s:textfield id="dataNascitaToInsert" name="modalitaPagamentoSoggettoToInsert.dataNascitaQuietanzante" maxlength="10" cssClass="span3"  />
                </div>
            </div>  
            
            <!--  aggiunta luogo nascita -->
            
            <div class="control-group">
                <label class="control-label" for="Stato2">Luogo nascita quietanzante</label>
                <div class="controls">
                
					<s:select list="nazioni" id="idNazione" headerKey="" headerValue="" name="modalitaPagamentoSoggettoToInsert.comuneNascita.nazioneCode"  
 	                	          listKey="codice" listValue="descrizione"/> 
					
			    <span class="al">
				</span>
				<s:textfield id="comune" name="modalitaPagamentoSoggettoToInsert.comuneNascita.descrizione" cssClass="lbTextSmall span3"/>
     			<s:hidden id="comuneId" name="modalitaPagamentoSoggettoToInsert.comuneNascita.uid"></s:hidden>   

			    				
			</div>
		    </div>    
            
            
             
            <div class="control-group">
            	<label class="control-label" for="scadenza">Data cessazione</label>
                <div class="controls">
                	<s:textfield id="scadenzaToInsert" name="dataScadenzaStringa" maxlength="10" cssClass="span3 datepicker"  />
                </div>
            </div> 
            
			<div class="control-group">
                <label class="control-label" for="notee">Note</label>
                <div class="controls"> 
                	<s:textarea id="notee" name="modalitaPagamentoSoggettoToInsert.note" cssClass="span3"/>
                </div>
            </div>           
		</fieldset>                     
  		<p>
  			<a class="btn" href="modalitaPagamentoSoggettoCec_annullaInserimento.do">annulla inserimento</a>    
  			<!-- task-131 <s:submit cssClass="btn" value="Salva" id="salvaId" method="inserisciModPagContanti" name="salvaContante" /> -->
  			<s:submit cssClass="btn" value="Salva" id="salvaId" action="modalitaPagamentoSoggettoCec_inserisciModPagContanti" name="salvaContante" /> 
  		</p> 
  	</div>
</div>
</s:form>
</s:if> 			  

        
<!--- per la gestione del credito si visualizzera' solo questo-->   

<s:if test="%{cessioneVisible}">              
<div id="inscessione">
<a id="ancorettaMdp"></a>
	<div class="accordion_info"> 
		<h5>Cerca Soggetto</h5>
		<s:form id="ricercaSoggettoModPag" action="modalitaPagamentoSoggettoCec" method="post">
        <fieldset class="form-horizontal">
        	<div class="control-group">
            	<label class="control-label" for="Codice">Codice</label>
                <div class="controls">
              		<s:textfield id="codice" name="modelWeb.codice" cssClass="span4" />
                	<label for="den" class="radio inline">Denominazione</label>
               		<s:textfield id="denominazione" name="modelWeb.denominazione" cssClass="span6 required"/>
              	</div>
          	</div>
            <div class="control-group">
            	<label class="control-label" for="iva">Partita IVA</label>
                <div class="controls">
                	<s:textfield id="partitaIva" name="modelWeb.partitaIva" cssClass="span4 required" />
                	<label  class="radio inline" for="codfisc">Codice Fiscale</label>
                	<s:textfield id="codiceFiscale" name="modelWeb.codiceFiscale" cssClass="span6 required" />
              	</div>
          	</div>
		</fieldset>
 		<p>
 			<a class="btn btn-link" href="modalitaPagamentoSoggettoCec_annullaInserimento.do">annulla</a>    
 			 <!--task-131 <s:submit name="cerca" value="cerca" method="ricercaSoggettoModPag" cssClass="btn" /> -->
 			 <s:submit name="cerca" value="cerca" action="modalitaPagamentoSoggettoCec_ricercaSoggettoModPag" cssClass="btn" />
 		</p>
   		</s:form>
  	
	<s:if test="%{cessioneSearchVisible}"> 
		<s:form id="selezioneSoggettoCessioneForm" action="modalitaPagamentoSoggettoCec" method="post">
		<s:hidden name="tipoAccreditoPage" id="tipoAccreditoPage" value="tipoAccreditoPage" />  
   		<h5>Risultati della ricerca </h5>     
   		<a id="ancorettaMdp"></a>
    	<table class="table table-hover" summary="riepilogo soggetti" >
			<thead>
				<tr>
					<th scope="col">Codice</th>
					<th scope="col">Cod. Fiscale</th>
					<th scope="col">Partita IVA</th>
					<th scope="col">Denominazione</th>
					<th scope="col">Stato</th>
				</tr>
			</thead>
			<tbody>
			<s:iterator value="modelWeb.soggettiRicercati" var="soggettoRicercato">
				<tr>
					<td scope="row">
						<label for="${soggettoRicercato.codiceSoggetto}">
							<input value="${soggettoRicercato.codiceSoggetto}" type="radio" name="codiceSoggettoRicercatoCessione" id="${soggettoRicercato.codiceSoggetto}" /> ${soggettoRicercato.codiceSoggetto}
						</label>
					</td>
					<td>${soggettoRicercato.codiceFiscale}</td>
					<td>${soggettoRicercato.partitaIva}</td>
					<td>${soggettoRicercato.denominazione}</td>
					<td>${soggettoRicercato.statoOperativo}</td>
				</tr>
			</s:iterator>
			</tbody>
		</table>  
                                 
        <p>
        	<a class="btn btn-link" href="modalitaPagamentoSoggettoCec_annullaInserimento.do">annulla</a>    
        	<!-- task-131 <s:submit name="seleziona_soggetto" value="seleziona soggetto" method="ricercaSoggettoModPag_step2" cssClass="btn" /> -->
        	<s:submit name="seleziona_soggetto" value="seleziona soggetto" action="modalitaPagamentoSoggettoCec_ricercaSoggettoModPag_step2" cssClass="btn" /> 
        </p> 
    	</s:form>                     
    </s:if>
     
    <s:if test="%{cessioneSearchStep2}">    
    <s:form name="inserisciModPagCessione" action="modalitaPagamentoSoggettoCec" method="post">
    <s:hidden name="tipoAccreditoPage" id="tipoAccreditoPage" value="tipoAccreditoPage" />
        <h5>Soggetto selezionato: ${modelWeb.dettaglioSoggettoCessione.denominazione} &nbsp; - &nbsp; ${modelWeb.dettaglioSoggettoCessione.codiceFiscale}</h5>
        <a id="ancorettaMdp"></a>
        <fieldset class="form-horizontal">   
                                   
      		<div class="control-group">
            	<label class="control-label" for="pagamento">Modalit&agrave; di pagamento</label>
                <div class="controls">
                	<s:select  list="modelWeb.modalitaPagamentoCessione" id="modPagCessione" name="modalitaPagamentoSoggettoToInsert.uid" title="Scegli la modalita di pagamento" listKey="uid" listValue="modalitaAccreditoSoggetto.descrizione" /> 
                </div>
            </div>
            <div class="control-group">
            	<label class="control-label" for="notee">Note</label>
                <div class="controls"> 
                	<s:textarea id="notee" name="modalitaPagamentoSoggettoToInsert.note" cssClass="span3"/>
                </div>
            </div>
            <div class="control-group">
            	<label class="control-label" for="scadenza">Data cessazione</label>
                <div class="controls">
                	<s:textfield id="scadenzaToInsert" name="dataScadenzaStringa" maxlength="10" cssClass="span3 datepicker"  />
                </div>
            </div> 
               <!--   <div class="control-group">
                <label class="control-label" for="pagamento">Modalit&agrave; pagamento</label>
                <div class="controls">
                <select id="pagamento" name="pagamento"><option>&nbsp;</option>
                                                      <option>xxx</option>
                  </select></div>
            </div> -->
                    
         </fieldset>  
         
         <p>
         	<a class="btn" href="modalitaPagamentoSoggettoCec_annullaInserimento.do">annulla inserimento</a>  
         	<!--task-131 <s:submit cssClass="btn" name="salvaCessione" id="salvaId" value="Salva" method="inserisciModPagCessione" /> -->
			<s:submit cssClass="btn" name="salvaCessione" id="salvaId" value="Salva" action="modalitaPagamentoSoggettoCec_inserisciModPagCessione" />
        </p>
		</s:form>
		</s:if>
	</div>
</div>
</s:if>

<s:if test="%{successInserimento}">
<div class="alert alert-success margin-medium">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
	L'operazione e' stata completata con successo
</div>   
</s:if>

<s:if test="%{aggiornaSuccessVisible}">
<div class="alert alert-success margin-medium">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
	L'operazione e' stata completata con successo
</div>   
</s:if>

<!-- End Sezione Inserimento -->
 <!-- Start Modal Window Consulta Mod Pagamento -->  
 <%int secondoIndice = 0; %>       
      <s:iterator value="model.dettaglioSoggetto.modalitaPagamentoList" var="pagamento">
     	 <div id="consultaModPag_<%=secondoIndice %>" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="consPag" aria-hidden="true">
 			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h3>Modalit&agrave; di pagamento: ${pagamento.modalitaAccreditoSoggetto.codice} &nbsp;-&nbsp;${pagamento.modalitaAccreditoSoggetto.descrizione} </h3>
			</div>
			
			<!-- START MDP NORMALE -->
			<s:if test="%{#pagamento.inModifica == false}">
			<div class="modal-body">
         		<dl class="dl-horizontal">
           
           		<s:if test="%{#pagamento.tipoAccredito != null}">
					<dt style="width: 180px;">Tipo Accredito</dt>
	            	<dd style="margin-left: 200px;">${pagamento.tipoAccredito}</dd>
	            </s:if>	
	            	
	            <s:if test="%{#pagamento.inModifica}">
	            	<dt style="width: 180px;">Stato modalit&agrave;</dt>
	             	<dd style="margin-left: 200px;">VALIDO</dd> 
	            </s:if>
	            <s:else>
	           		<s:if test="%{#pagamento.descrizioneStatoModalitaPagamento != null && #pagamento.descrizioneStatoModalitaPagamento.trim() != ''}">
	 					<dt style="width: 180px;">Stato modalit&agrave;</dt>
	             		<dd style="margin-left: 200px;">${pagamento.descrizioneStatoModalitaPagamento}</dd>    
	            	</s:if> 
	            </s:else>
	            
	            <s:if test="%{#pagamento.inModifica}">		<!-- non entra nemmeno -->
		        	<dt  style="width: 180px;">In Modifica</dt>
		            <dd style="margin-left: 200px;">Si</dd>
		            <s:if test="%{#pagamento.loginModifica != null && #pagamento.loginModifica.trim() != ''}">    
		            	<dt style="width: 180px;">Utente Modifica</dt>	       
		                <dd style="margin-left: 200px;">${pagamento.loginModifica}</dd>
		            </s:if>
		            <s:if test="%{#pagamento.dataModifica != null}">    
		                <dt style="width: 180px;">Data Modifica</dt>	                	        
		                <dd style="margin-left: 200px;">
		               		<s:date name="%{#pagamento.dataModifica}" format="dd/MM/yyyy"/> 
		        	   	</dd>	                	              
		            </s:if>
		          	<s:if test="%{#pagamento.loginUltimaModifica != null && #pagamento.loginUltimaModifica.trim() != ''}">    
	                	<dt style="width: 180px;">Utente Ultimo Aggiornamento</dt>	        
	                	<dd style="margin-left: 200px;">${pagamento.loginUltimaModifica}</dd>
	            	</s:if>
	             	<s:if test="%{#pagamento.dataUltimaModifica != null}">    
	                	<dt style="width: 180px;">Data Ultimo Aggiornamento</dt>	                	        
	                	<dd style="margin-left: 200px;">
	               	 		<s:date name="%{#pagamento.dataUltimaModifica}" format="dd/MM/yyyy"/> 
	               	 	</dd>	                	              
	            	</s:if>
	            </s:if>
	            <s:else>
		            	<s:if test="%{#pagamento.loginUltimaModifica != null && #pagamento.loginUltimaModifica.trim() != ''}">    
			                	<dt style="width: 180px;">Utente Ultimo Aggiornamento</dt>	        
			                	<dd style="margin-left: 200px;">${pagamento.loginUltimaModifica}</dd>
			            </s:if>
		            	<s:else>
			            	<s:if test="%{#pagamento.loginModifica != null && #pagamento.loginModifica.trim() != ''}">    
		                		<dt style="width: 180px;">Utente Ultimo Aggiornamento</dt>	        
		                		<dd style="margin-left: 200px;">${pagamento.loginModifica}</dd>
		            		</s:if>	
			            </s:else>
		            	
		            	<s:if test="%{#pagamento.dataUltimaModifica != null}">    
		                	<dt style="width: 180px;">Data Ultimo Aggiornamento</dt>	                	        
		                	<dd style="margin-left: 200px;">
		               	 		<s:date name="%{#pagamento.dataUltimaModifica}" format="dd/MM/yyyy"/> 
		               	 	</dd>	                	              
		            	</s:if>		            			             	
			        	<s:else>    			            
			             	<s:if test="%{#pagamento.dataModifica != null}">    
			                	<dt style="width: 180px;">Data Ultimo Aggiornamento</dt>	                	        
			                	<dd style="margin-left: 200px;">
			               	 		<s:date name="%{#pagamento.dataModifica}" format="dd/MM/yyyy"/> 
			               	 	</dd>	                	              
		            		</s:if>
		            	</s:else>	            
	            </s:else>
	            
	            <s:if test="%{#attr.dettaglioSoggetto.codiceSoggetto != null && #attr.dettaglioSoggetto.codiceSoggetto.trim() != ''}">        
	                <dt style="width: 180px;">Codice Soggetto</dt>
	                <dd style="margin-left: 200px;">${model.dettaglioSoggetto.codiceSoggetto}</dd>	                
	            </s:if>
	            <s:if test="%{#attr.dettaglioSoggetto.codiceFiscale != null && #attr.dettaglioSoggetto.codiceFiscale.trim() != ''}">    
	                <dt style="width: 180px;">Codice Fiscale</dt>
	                <dd style="margin-left: 200px;">${model.dettaglioSoggetto.codiceFiscale }</dd>
	            </s:if>
	            <s:if test="%{#attr.dettaglioSoggetto.partitaIva != null && #attr.dettaglioSoggetto.partitaIva.trim() != ''}">    
	                <dt style="width: 180px;">P.IVA</dt>
	                <dd style="margin-left: 200px;">${model.dettaglioSoggetto.partitaIva}</dd>
	            </s:if>
	            <s:if test="%{#attr.dettaglioSoggetto.denominazione != null && #attr.dettaglioSoggetto.denominazione.trim() != ''}">    
	                <dt style="width: 180px;">Denominazione</dt>
	                <dd style="margin-left: 200px;">${model.dettaglioSoggetto.denominazione}</dd>
	            </s:if>
	             	
  				<s:if test="%{#pagamento.iban != null && #pagamento.iban.trim() != ''}">
				  	<dt style="width: 180px;">Iban</dt>
	         		<dd style="margin-left: 200px;">${pagamento.iban}</dd>
	         	</s:if>
	         	<s:if test="%{#pagamento.bic != null && #pagamento.bic.trim() != ''}">	
	                <dt style="width: 180px;">Bic</dt>
	                <dd style="margin-left: 200px;">${pagamento.bic}</dd>
	            </s:if>
	            <s:if test="%{#pagamento.contoCorrente != null && #pagamento.contoCorrente.trim() != ''}">    
	                <dt style="width: 180px;">Conto Corrente</dt>
	                <dd style="margin-left: 200px;">${pagamento.contoCorrente}</dd>
	            </s:if>
	            
	             <s:if test="%{#pagamento.intestazioneConto != null && #pagamento.intestazioneConto.trim() != ''}">    
	                <dt style="width: 180px;">Intestazione Conto</dt>
	                <dd style="margin-left: 200px;">${pagamento.intestazioneConto}</dd>
	            </s:if>
	            
	            <s:if test="%{#pagamento.soggettoQuietanzante != null && #pagamento.soggettoQuietanzante.trim() != ''}">
	                
	                <dt style="width: 180px;">Quietanzante</dt>
	                <dd style="margin-left: 200px;">${pagamento.soggettoQuietanzante}</dd>
	            </s:if>
	            <s:if test="%{#pagamento.codiceFiscaleQuietanzante != null && #pagamento.codiceFiscaleQuietanzante.trim() != ''}">    
	                <dt style="width: 180px;">Codice Fiscale Quietanzante</dt>
	                <dd style="margin-left: 200px;">${pagamento.codiceFiscaleQuietanzante}</dd>
	            </s:if>

                <!-- nuovi campi -->  
                <s:if test="%{#pagamento.comuneNascita != null}">
	                
	                <dt style="width: 180px;">Comune Nascita quietanzante</dt>
	                <dd style="margin-left: 200px;">${pagamento.comuneNascita.descrizione}</dd>
	            </s:if>
	            <s:if test="%{#pagamento.dataNascitaQuietanzante != null}">
	                
	                <dt style="width: 180px;">Data Nascita quietanzante</dt>
	                <dd style="margin-left: 200px;">${pagamento.dataNascitaQuietanzante}</dd>
	            </s:if>
	            

				<!-- / nuovi campi -->  


	            <!--   
	                <dt>ModalitÃ  Collegata</dt>
	                <dd></dd>
	             -->
		        <s:if test="%{#pagamento.cessioneCodSoggetto != null && #pagamento.cessioneCodSoggetto.trim() != ''}">
	            	<dt style="width: 180px;">Codice Soggetto Ricevente</dt>
	            	<dd style="margin-left: 200px;">${pagamento.cessioneCodSoggetto} 
													- ${pagamento.soggettoCessione.denominazione}</dd>
	            </s:if>
	            <s:if test="%{#pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.codice != null && #pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.codice.trim() != ''}">
	            	<dt style="width: 180px;">Tipo Accredito Ricevente</dt>
	            	<dd style="margin-left: 200px;">${pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.codice} - ${pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.descrizione}</dd>
	            </s:if>       
	            <s:if test="%{#pagamento.note != null && #pagamento.note.trim() != ''}">    
	                <dt style="width: 180px;">Note</dt>
	                <dd style="margin-left: 200px;">${pagamento.note}</dd>
	            </s:if>	 
	            <s:if test="%{#pagamento.dataFineValidita != null}">    
	                <dt style="width: 180px;">Data cessazione</dt>
	                <dd style="margin-left: 200px;">
	              	  <s:date name="%{#pagamento.dataFineValidita}" format="dd/MM/yyyy"/>	                	            	                
	                </dd>
	            </s:if>
	   
	            <s:if test="%{#pagamento.loginCreazione != null && #pagamento.loginCreazione.trim() != ''}">    
	                <dt style="width: 180px;">Utente Inserimento</dt>	        
	                <dd style="margin-left: 200px;">${pagamento.loginCreazione}</dd>
	            </s:if>
				 <s:if test="%{#pagamento.dataCreazione != null}">    
	                <dt style="width: 180px;">Data Inserimento</dt>	        
	                <dd style="margin-left: 200px;">
	            		<s:date name="%{#pagamento.dataCreazione}" format="dd/MM/yyyy"/>    
	                </dd>
	            </s:if>
	            
            	</dl>       
			</div>
			
			
			
			</s:if>
			<!-- END MDP NORMALE -->
			
			
			<!-- START MDP MODIFICA -->
			<s:if test="%{#pagamento.inModifica}">
			<div class="modal-body">
         		<dl class="dl-horizontal">
           
           		<s:if test="%{#pagamento.tipoAccredito != null}">
					<dt style="width: 200px;">Tipo Accredito</dt>
	            	<dd style="margin-left: 225px;">${pagamento.tipoAccredito}</dd>
	            </s:if>	
	            	
	            <s:if test="%{#pagamento.inModifica}">
	            	<dt style="width: 200px;">Stato modalit&agrave;</dt>
	             	<dd style="margin-left: 225px;">VALIDO</dd> 
	            </s:if>
	            <s:if test="%{#pagamento.loginUltimaModifica != null && #pagamento.loginUltimaModifica.trim() != ''}">	    
	            	<dt style="width: 200px;">Utente Ultimo Aggiornamento</dt>	        
	                <dd style="margin-left: 225px;">${pagamento.loginUltimaModifica}</dd>
	            </s:if>
	            <s:if test="%{#pagamento.dataUltimaModifica != null}">    
	            	<dt style="width: 200px;">Data Ultimo Aggiornamento</dt>				                	        
	                <dd style="margin-left: 225px;">
	               	 	<s:date name="%{#pagamento.dataUltimaModifica}" format="dd/MM/yyyy"/> 
	               	 </dd>	                	              
	            </s:if>
	            <dt style="width: 200px;">In Modifica</dt>
	            <dd style="margin-left: 225px;">Si</dd> 
	            <s:else>
	           		<s:if test="%{#pagamento.descrizioneStatoModalitaPagamento != null && #pagamento.descrizioneStatoModalitaPagamento.trim() != ''}">
	 					<dt style="width: 200px;">Stato modalit&agrave;</dt>
	             		<dd style="margin-left: 225px;">${pagamento.descrizioneStatoModalitaPagamento}</dd>    
	            	</s:if> 
	            </s:else>

	           	<s:if test="%{#pagamento.loginModifica != null && #pagamento.loginModifica.trim() != ''}">    
	            	<dt style="width: 200px;">Utente Modifica</dt>	        
	                <dd style="margin-left: 225px;">${pagamento.loginModifica}</dd>
	            </s:if>
	            <s:if test="%{#pagamento.dataModifica != null}">    
	               <dt style="width: 200px;">Data Modifica</dt>	                	        
	               <dd style="margin-left: 225px;">
	               		<s:date name="%{#pagamento.dataModifica}" format="dd/MM/yyyy"/> 
	               </dd>	                	              
	            </s:if>
	            
	            <s:if test="%{#attr.dettaglioSoggetto.codiceSoggetto != null && #attr.dettaglioSoggetto.codiceSoggetto.trim() != ''}">        
	                <dt style="width: 200px;">Codice Soggetto</dt>
	                <dd style="margin-left: 225px;">${model.dettaglioSoggetto.codiceSoggetto}</dd>	                
	            </s:if>
	            
	            <s:if test="%{#attr.dettaglioSoggetto.codiceFiscale != null && #attr.dettaglioSoggetto.codiceFiscale.trim() != ''}">    
	                <dt style="width: 200px;">Codice Fiscale</dt>
	                <dd style="margin-left: 225px;">${model.dettaglioSoggetto.codiceFiscale }</dd>
	            </s:if>
	            <s:if test="%{#attr.dettaglioSoggetto.partitaIva != null && #attr.dettaglioSoggetto.partitaIva.trim() != ''}">    
	                <dt style="width: 200px;">P.IVA</dt>
	                <dd style="margin-left: 225px;">${model.dettaglioSoggetto.partitaIva}</dd>
	            </s:if>
	            <s:if test="%{#attr.dettaglioSoggetto.denominazione != null && #attr.dettaglioSoggetto.denominazione.trim() != ''}">    
	                <dt style="width: 200px;">Denominazione</dt>
	                <dd style="margin-left: 225px;">${model.dettaglioSoggetto.denominazione}</dd>
	            </s:if>
	            
	            <s:if test="isDecentrato()">
	            	<s:if test="%{#pagamento.iban != null && #pagamento.iban.trim() != ''}">
	            		<dt style="width: 200px;">Iban</dt>	        
               	 		<dd style="margin-left: 225px;">${pagamento.iban}</dd>
	            	</s:if>
	            </s:if>
	            <s:else>
	            	<s:if test="%{#pagamento.modalitaOriginale.iban != null && #pagamento.modalitaOriginale.iban.trim() != ''}">
	            		<dt style="width: 200px;">Iban</dt>
	         			<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.iban}</dd>
	            	</s:if>
	            </s:else> 
	            
	            
	            <s:if test="isDecentrato()">
	            	<s:if test="%{#pagamento.bic != null && #pagamento.bic.trim() != ''}">
	            		<dt style="width: 200px;">Bic</dt>	        
               	 		<dd style="margin-left: 225px;">${pagamento.bic}</dd>
	            	</s:if>
	            </s:if>
	            <s:else>
	            	<s:if test="%{#pagamento.modalitaOriginale.bic != null && #pagamento.modalitaOriginale.bic.trim() != ''}">	
	                	<dt style="width: 200px;">Bic</dt>
	                	<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.bic}</dd>
	            	</s:if>
	            </s:else> 	
  				 
  				<s:if test="isDecentrato()">
  				 	<s:if test="%{#pagamento.contoCorrente != null && #pagamento.contoCorrente.trim() != ''}">
  				 		<dt style="width: 200px;">Conto Corrente</dt>	        
               	 		<dd style="margin-left: 225px;">${pagamento.contoCorrente}</dd>
  				 	</s:if>
  				</s:if>
  				<s:else>
  					<s:if test="%{#pagamento.modalitaOriginale.contoCorrente != null && #pagamento.modalitaOriginale.contoCorrente.trim() != ''}">
  						<dt style="width: 200px;">Numero di Conto</dt>
	                	<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.contoCorrente}</dd>    
  					</s:if>
  				</s:else> 
  				
  				<!-- nuovi campi INTESTAZIONE CONTO -->
  				<s:if test="isDecentrato()">
  				 	<s:if test="%{#pagamento.intestazioneConto != null && #pagamento.intestazioneConto.trim() != ''}">
  				 		<dt style="width: 200px;">Intestazione Conto</dt>	        
               	 		<dd style="margin-left: 225px;">${pagamento.intestazioneConto}</dd>
  				 	</s:if>
  				</s:if>
  				<s:else>
  					<s:if test="%{#pagamento.modalitaOriginale.intestazioneConto != null && #pagamento.modalitaOriginale.intestazioneConto.trim() != ''}">
  						<dt style="width: 200px;">Intestazione Conto</dt>
	                	<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.intestazioneConto}</dd>    
  					</s:if>
  				</s:else> 
  				
  				<!-- fine - nuovi campi -->

				<s:if test="isDecentrato()">
					<s:if test="%{#pagamento.soggettoQuietanzante != null && #pagamento.soggettoQuietanzante.trim() != ''}">
						<dt style="width: 200px;">Soggetto Quietanzante</dt>
	                	<dd style="margin-left: 225px;">${pagamento.soggettoQuietanzante}</dd>
					</s:if>
				</s:if>
				<s:else>
					<s:if test="%{#pagamento.modalitaOriginale.soggettoQuietanzante != null && #pagamento.modalitaOriginale.soggettoQuietanzante.trim() != ''}">
						<dt style="width: 200px;">Soggetto Quietanzante</dt>
	                	<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.soggettoQuietanzante}</dd>
					</s:if>
				</s:else>
	            
	            <s:if test="isDecentrato()">
	            	<s:if test="%{#pagamento.codiceFiscaleQuietanzante != null && #pagamento.codiceFiscaleQuietanzante.trim() != ''}">
	            		<dt style="width: 200px;">Codice Fiscale Quietanzante</dt>	        
               	 		<dd style="margin-left: 225px;">${pagamento.codiceFiscaleQuietanzante}</dd>
	            	</s:if>
	            </s:if>
	            <s:else>
	            	<s:if test="%{#pagamento.modalitaOriginale.codiceFiscaleQuietanzante != null && #pagamento.modalitaOriginale.codiceFiscaleQuietanzante.trim() != ''}">    
	            		<dt style="width: 200px;">Codice Fiscale Quietanzante</dt>
	                	<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.codiceFiscaleQuietanzante}</dd>
	            	</s:if>
	            </s:else>
	            
	            
	            <!-- nuovi campi: data e luogo di nascita-->
	            
	             <s:if test="isDecentrato()">
	            	<s:if test="%{#pagamento.dataNascitaQuietanzante != null && #pagamento.dataNascitaQuietanzante.trim() != ''}">
	            		<dt style="width: 200px;">Data Nascita Quietanzante</dt>	        
               	 		<dd style="margin-left: 225px;">${pagamento.dataNascitaQuietanzante}</dd>
	            	</s:if>
	            </s:if>
	            <s:else>
	            	<s:if test="%{#pagamento.modalitaOriginale.dataNascitaQuietanzante != null && #pagamento.modalitaOriginale.dataNascitaQuietanzante.trim() != ''}">    
	            		<dt style="width: 200px;">Data Nascita Quietanzante</dt>
	                	<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.dataNascitaQuietanzante}</dd>
	            	</s:if>
	            </s:else>
	            
	            <s:if test="isDecentrato()">
	            	<s:if test="%{#pagamento.comuneNascita != null}">
	            		<dt style="width: 200px;">Luogo Nascita Quietanzante</dt>	        
               	 		<dd style="margin-left: 225px;">${pagamento.comuneNascita.descrizione}</dd>
	            	</s:if>
	            </s:if>
	            <s:else>
	            	<s:if test="%{#pagamento.modalitaOriginale.comuneNascita != null}">    
	            		<dt style="width: 200px;">Luogo Nascita Quietanzante</dt>
	                	<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.comuneNascita.descrizione}</dd>
	            	</s:if>
	            </s:else>
	            
	            
	            
	            <!-- fine - nuovi campi -->
 
	            <!--   
	                <dt>ModalitÃ  Collegata</dt>
	                <dd></dd>
	             -->
		        <s:if test="%{#pagamento.cessioneCodSoggetto != null && #pagamento.cessioneCodSoggetto.trim() != ''}">
	            	<dt style="width: 200px;">Codice Soggetto Ricevente</dt>
	            	<dd style="margin-left: 225px;">${pagamento.cessioneCodSoggetto} 
													- ${pagamento.soggettoCessione.denominazione}</dd>
	            </s:if>
	            <s:if test="%{#pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.codice != null && #pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.codice.trim() != ''}">
	            	<dt style="width: 200px;">Tipo Accredito Ricevente</dt>
	            	<dd style="margin-left: 225px;">${pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.codice} - ${pagamento.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.descrizione}</dd>
	            </s:if>   
	                
	                
	            <s:if test="isDecentrato()">  
	            	<s:if test="%{#pagamento.note != null && #pagamento.note.trim() != ''}">
	            		<dt style="width: 200px;">Note</dt>	        
               	 		<dd style="margin-left: 225px;">${pagamento.note}</dd>
	            	</s:if>
	            </s:if>
	            <s:else>
	            	<s:if test="%{#pagamento.modalitaOriginale.note != null && #pagamento.modalitaOriginale.note.trim() != ''}">    
	            		 <dt style="width: 200px;">Note</dt>
	                	<dd style="margin-left: 225px;">${pagamento.modalitaOriginale.note}</dd>
	            	</s:if>
	            </s:else> 
	                
	
				<s:if test="isDecentrato()">  
					<s:if test="%{#pagamento.dataFineValidita != null}">
						<dt style="width: 200px;">Data cessazione</dt>
	                	<dd style="margin-left: 225px;">
	              	  		<s:date name="%{#pagamento.dataFineValidita}" format="dd/MM/yyyy"/>	                	            	                
	                	</dd>
					</s:if>
				</s:if>
				<s:else>
					<s:if test="%{#pagamento.modalitaOriginale.dataFineValidita != null}">    
	                	<dt style="width: 200px;">Data cessazione</dt>
	                	<dd style="margin-left: 225px;">
	              	  		<s:date name="%{#pagamento.modalitaOriginale.dataFineValidita}" format="dd/MM/yyyy"/>	                	            	                
	                	</dd>
	            	</s:if>
				</s:else>
	   
	            <s:if test="%{#pagamento.loginCreazione != null && #pagamento.loginCreazione.trim() != ''}">    
	                <dt style="width: 200px;">Utente Inserimento</dt>	        
	                <dd style="margin-left: 225px;">${pagamento.loginCreazione}</dd>
	            </s:if>
				 <s:if test="%{#pagamento.dataCreazione != null}">    
	                <dt style="width: 200px;">Data Inserimento</dt>	        
	                <dd style="margin-left: 225px;">
	            		<s:date name="%{#pagamento.dataCreazione}" format="dd/MM/yyyy"/>    
	                </dd>
	            </s:if>
	            


	            
            	</dl>  
            	     
			</div>
			
			
			
			
			
			</s:if>
			<!-- END MDP MODIFICA -->
			
			
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">chiudi</button>
			</div>
		</div> 
		<%secondoIndice++; %>
      </s:iterator>
 <!-- End Modal Window Consulta Mod Pagamento -->      
      
 <!-- START SEZIONE AGGIORNA -->     
<s:if test="%{aggiornaContoCorrenteVisible}">  
<s:form name="aggiornaModPagContoCorrente" action="modalitaPagamentoSoggettoCec" method="post"> 
<s:hidden name="aggiornaTipoAccredito" id="aggiornaTipoAccredito" value="aggiornaTipoAccredito" />                                    
<div id="inscontocorr">
<a id="ancorettaMdp"></a>
	<div class="accordion_info"> 
    	<fieldset class="form-horizontal">
    	<s:if test="%{aggiornaTipoAccredito != 'CBI'}">
			<div class="control-group">
			
            	<label class="control-label" for="iban">IBAN </label>
                <div class="controls">
                	<s:textfield id="ibanToInsert" name="soggettoDaAggiornare.iban" cssClass="span3 required" />
                </div>
			</div>
		</s:if> 	
			
			<s:if test="%{aggiornaTipoAccredito != 'CCP' && aggiornaTipoAccredito != 'CBI' }">
			<div class="control-group">
	                <label class="control-label" for="bic">BIC </label>
	                <div class="controls">
	                	<s:textfield id="bicToInsert" name="soggettoDaAggiornare.bic" cssClass="span3 required" />
	                </div>
            </div> 
            </s:if>   
                                                    
            <div class="control-group">
                <label class="control-label" for="conto">Numero conto</label>
                <div class="controls">
                	<s:textfield id="ncontoCorrenteToInsert" name="soggettoDaAggiornare.contoCorrente" cssClass="span3 required" />
                </div>
            </div>     
 
 
			<div class="control-group">
				<label class="control-label" for="denominazioneBanca">Denominazione banca</label>
				<div class="controls">
					<s:textfield id="ndenominazioneBancaToInsert"
						name="soggettoDaAggiornare.denominazioneBanca"
						cssClass="span3 required" />
				</div>
			</div>

            
            <div class="control-group">
                <label class="control-label" for="intestazioneConto">Intestazione conto</label>
                <div class="controls">
                	<s:textfield id="nIntestazioneContoCorrenteToInsert" name="soggettoDaAggiornare.intestazioneConto" cssClass="span3 required" />
                </div>
            </div>    
                  
            <div class="control-group">
                <label class="control-label" for="scadenza">Data cessazione</label>
                <div class="controls">
                	<s:textfield id="scadenzaToInsert" name="dataScadenzaStringa" maxlength="10" cssClass="span3 datepicker"  />
                </div>
            </div> 
            <div class="control-group">
    	  		<label class="control-label" for="notee">Note</label>
                <div class="controls"> 
                	<s:textarea id="notaToInsert" name="soggettoDaAggiornare.note" cssClass="span3" />
                </div>
            </div>
            	       
  		</fieldset>                     
  		<p>
  			<a class="btn" href="modalitaPagamentoSoggettoCec_annullaInserimento.do">annulla inserimento</a>
  			<!-- task-131 <s:submit name="aggiornaModPagCorrente" value="Salva" id="aggiornaId" cssClass="btn" method="aggiornaModPagContoCorrente" /> -->
  			<s:submit name="aggiornaModPagCorrente" value="Salva" id="aggiornaId" cssClass="btn" action="modalitaPagamentoSoggettoCec_aggiornaModPagContoCorrente" />
  		</p> 
  	</div>
</div>
</s:form> 
</s:if>  
 
 
 <s:if test="%{aggiornaGenericoVisible}">  
<s:form name="aggiornaModPagGenerico" action="modalitaPagamentoSoggettoCec" method="post"> 
<s:hidden name="aggiornaTipoAccredito" id="aggiornaTipoAccredito" value="aggiornaTipoAccredito" />                                    
<div id="agg-generico">
<a id="ancorettaMdp"></a>
	<div class="accordion_info"> 
    	<fieldset class="form-horizontal">
    	 
                  
            <div class="control-group">
                <label class="control-label" for="scadenza">Data cessazione</label>
                <div class="controls">
                	<s:textfield id="scadenzaToInsert" name="dataScadenzaStringa" maxlength="10" cssClass="span3 datepicker"  />
                </div>
            </div> 
            <div class="control-group">
    	  		<label class="control-label" for="notee">Note</label>
                <div class="controls"> 
                	<s:textarea id="notaToInsert" name="soggettoDaAggiornare.note" cssClass="span3" />
                </div>
            </div>
            	       
  		</fieldset>                     
  		<p>
  			<a class="btn" href="modalitaPagamentoSoggettoCec_annullaInserimento.do">annulla inserimento</a>
  			<!--task-131 <s:submit name="aggiornaGenerico" value="Salva" id="aggiornaId" cssClass="btn" method="aggiornaGenerico" />-->
  			<s:submit name="aggiornaGenerico" value="Salva" id="aggiornaId" cssClass="btn" action="modalitaPagamentoSoggettoCec_aggiornaGenerico" />
  		</p> 
  	</div>
</div>
</s:form> 
</s:if>  
 
 
 
 
 
<s:if test="%{aggiornaContantiVisible}">
<s:form name="aggiornaModPagContante" action="modalitaPagamentoSoggettoCec" method="post">
<s:hidden name="aggiornaTipoAccredito" id="aggiornaTipoAccredito" value="aggiornaTipoAccredito" />
<div id="inscontante">
<a id="ancorettaMdp"></a>
	<div class="accordion_info"> 
    	<fieldset class="form-horizontal">     
        	<div class="control-group">
            	<label class="control-label" for="nomevia">Codice fiscale Quietanzante</label>
                <div class="controls">
                	<s:textfield id="codFiscale" name="soggettoDaAggiornare.codiceFiscaleQuietanzante" cssClass="span3" />
               	</div>
		    </div>
                                                    
            <div class="control-group">
            	<label class="control-label" for="conto">Quietanzante</label>
                <div class="controls">
                	<s:textfield id="quietanzante" name="soggettoDaAggiornare.soggettoQuietanzante" cssClass="span3"/>
                </div>
            </div>
            
             <div class="control-group">
            	<label class="control-label" for="scadenza">Data di nascita quietanzante</label>
                <div class="controls">
                	<s:textfield id="nascitaToInsert" name="soggettoDaAggiornare.dataNascitaQuietanzante" maxlength="10" cssClass="span3"  />
                </div>
            </div> 
            
            <div class="control-group">
                <label class="control-label" for="luogoNascita">Luogo nascita quietanzante</label>
                <div class="controls">
	                
					<s:select list="nazioni" id="idNazione" headerKey="" headerValue="" name="soggettoDaAggiornare.comuneNascita.nazioneCode"  
	 	                      listKey="codice" listValue="descrizione"/> 
						
				    <span class="al">
					</span>
					<s:textfield id="comune" name="soggettoDaAggiornare.comuneNascita.descrizione" cssClass="lbTextSmall span3"/>
	     			<s:hidden id="comuneId" name="soggettoDaAggiornare.comuneNascita.uid"></s:hidden>   
				</div>
		    </div>    
             
            <div class="control-group">
            	<label class="control-label" for="scadenza">Data cessazione</label>
                <div class="controls">
                	<s:textfield id="scadenzaToInsert" name="dataScadenzaStringa" maxlength="10" cssClass="span3 datepicker"  />
                </div>
            </div> 
            
			<div class="control-group">
                <label class="control-label" for="notee">Note</label>
                <div class="controls"> 
                	<s:textarea id="notee" name="soggettoDaAggiornare.note" cssClass="span3"/>
                </div>
            </div>           
		</fieldset>                     
  		<p>
  			<a class="btn" href="modalitaPagamentoSoggettoCec_annullaInserimento.do">annulla inserimento</a>    
  			<!-- task-131 <s:submit cssClass="btn" value="Salva" id="aggiornaId" method="aggiornaContante" name="aggiornaContante" /> -->
  			<s:submit cssClass="btn" value="Salva" id="aggiornaId" action="modalitaPagamentoSoggettoCec_aggiornaContante" name="aggiornaContante" /> 
  		</p> 
  	</div>
</div>
</s:form>
</s:if> 	


<s:if test="%{aggiornaCessioneSearch}">              
<div id="inscessione">
<a id="ancorettaMdp"></a>
	<div class="accordion_info"> 
		<h5>Cerca Soggetto</h5>
		<s:form id="aggiornaricercaSoggettoModPag" action="modalitaPagamentoSoggettoCec" method="post">
		<s:hidden name="aggiornaTipoAccredito" id="aggiornaTipoAccredito" value="aggiornaTipoAccredito" />  
        <fieldset class="form-horizontal">
        	<div class="control-group">
            	<label class="control-label" for="Codice">Codice</label>
                <div class="controls">
              		<s:textfield id="codice" name="modelWeb.codiceAggiorna" cssClass="span4" />
                	<label for="den" class="radio inline">Denominazione</label>
               		<s:textfield id="denominazione" name="modelWeb.denominazioneAggiorna" cssClass="span6 required"/>
              	</div>
          	</div>
            <div class="control-group">
            	<label class="control-label" for="iva">Partita IVA</label>
                <div class="controls">
                	<s:textfield id="partitaIva" name="modelWeb.partitaIvaAggiorna" cssClass="span4 required" />
                	<label  class="radio inline" for="codfisc">Codice Fiscale</label>
                	<s:textfield id="codiceFiscale" name="modelWeb.codiceFiscaleAggiorna" cssClass="span6 required" />
              	</div>
          	</div>
		</fieldset>
 		<p>
 			<a class="btn btn-link" href="modalitaPagamentoSoggettoCec_annullaInserimento.do">annulla</a>    
 			 <!-- task-131 <s:submit name="cerca" value="cerca" method="ricercaSoggettoModPagAggiorna" cssClass="btn" /> -->
 			 <s:submit name="cerca" value="cerca" action="modalitaPagamentoSoggettoCec_ricercaSoggettoModPagAggiorna" cssClass="btn" /> 
 		</p>
   		</s:form>
  	
	<s:if test="%{aggiornaCessioneStep2}"> 
		<s:form id="selezioneSoggettoCessioneAggiornaForm" action="modalitaPagamentoSoggettoCec" method="post">
		<s:hidden name="aggiornaTipoAccredito" id="aggiornaTipoAccredito" value="aggiornaTipoAccredito" />  
   		<h5>Risultati della ricerca </h5>     
    	<table class="table table-hover" summary="riepilogo soggetti" >
			<thead>
				<tr>
					<th scope="col">Codice</th>
					<th scope="col">Cod. Fiscale</th>
					<th scope="col">Partita IVA</th>
					<th scope="col">Denominazione</th>
					<th scope="col">Stato</th>
				</tr>
			</thead>
			<tbody>
			<s:iterator value="modelWeb.soggettiRicercatiAggiorna" var="soggettoRicercatoAggiorna">
				<tr>
					<td scope="row">
						<label for="${soggettoRicercatoAggiorna.codiceSoggetto}">
							<input value="${soggettoRicercatoAggiorna.codiceSoggetto}" type="radio" name="codiceSoggettoRicercatoCessioneAggiorna" id="${soggettoRicercatoAggiorna.codiceSoggetto}" /> ${soggettoRicercatoAggiorna.codiceSoggetto}
						</label>
					</td>
					<td>${soggettoRicercatoAggiorna.codiceFiscale}</td>
					<td>${soggettoRicercatoAggiorna.partitaIva}</td>
					<td>${soggettoRicercatoAggiorna.denominazione}</td>
					<td>${soggettoRicercatoAggiorna.statoOperativo}</td>
				</tr>
			</s:iterator>
			</tbody>
		</table>  
                                 
        <p>
        	<a class="btn btn-link" href="modalitaPagamentoSoggettoCec_annullaInserimento.do">annulla</a>    
        	<!-- task-131 <s:submit name="seleziona_soggetto" value="seleziona soggetto" method="ricercaSoggettoModPag_step2Aggiorna" cssClass="btn" /> -->
        	<s:submit name="seleziona_soggetto" value="seleziona soggetto" action="modalitaPagamentoSoggettoCec_ricercaSoggettoModPag_step2Aggiorna" cssClass="btn" /> 
        </p> 
    	</s:form>                     
    </s:if>
     
    <s:if test="%{aggiornaCessioneStep3}">    
    <s:form name="inserisciModPagCessione" action="modalitaPagamentoSoggettoCec" method="post">
    <s:hidden name="aggiornaTipoAccredito" id="aggiornaTipoAccredito" value="aggiornaTipoAccredito" />
        <h5>Soggetto selezionato: ${modelWeb.dettaglioSoggettoCessioneAggiorna.denominazione} &nbsp; - &nbsp; ${modelWeb.dettaglioSoggettoCessioneAggiorna.codiceFiscale}</h5>
		<a id="ancorettaMdp"></a>
        <fieldset class="form-horizontal">   
                                   
      		<div class="control-group">
            	<label class="control-label" for="pagamento">Modalit&agrave; di pagamento</label>
                <div class="controls">
                	<s:select  list="modelWeb.modalitaPagamentoCessioneAggiorna" id="modPagCessione" name="soggettoDaAggiornare.uid" title="Scegli la modalita di pagamento" listKey="uid" listValue="modalitaAccreditoSoggetto.descrizione" /> 
                </div>
            </div>
            <div class="control-group">
            	<label class="control-label" for="notee">Note</label>
                <div class="controls"> 
                	<s:textarea id="notee" name="soggettoDaAggiornare.note" cssClass="span3"/>
                </div>
            </div>
            <div class="control-group">
            	<label class="control-label" for="scadenza">Data cessazione</label>
                <div class="controls">
														<s:textfield id="scadenzaToInsert"
															name="dataScadenzaStringa" maxlength="10"
															cssClass="span3 datepicker" />
													</div>
            </div> 
               <!--   <div class="control-group">
                <label class="control-label" for="pagamento">Modalit&agrave; pagamento</label>
                <div class="controls">
                <select id="pagamento" name="pagamento"><option>&nbsp;</option>
                                                      <option>xxx</option>
                  </select></div>
            </div> -->
                    
         </fieldset>  
         
         <p>
         	<a class="btn" href="modalitaPagamentoSoggettoCec_annullaInserimento.do">annulla inserimento</a>  
         	<!-- task-131 <s:submit cssClass="btn" name="salvaCessione" id="aggiornaId" value="" method="aggiornaModPagCessione" /> -->
         	<s:submit cssClass="btn" name="salvaCessione" id="aggiornaId" value="" action="modalitaPagamentoSoggettoCec_aggiornaModPagCessione" />  
         </p>
		</s:form>
		</s:if>
	</div>
</div>
</s:if>		

    <s:if test="%{aggiornaCessioneStep3}">    
    <s:form name="inserisciModPagCessione" action="modalitaPagamentoSoggettoCec" method="post">
    <s:hidden name="aggiornaTipoAccredito" id="aggiornaTipoAccredito" value="aggiornaTipoAccredito" />
    <h5>Codice Soggetto Cessione: ${soggettoDaAggiornare.cessioneCodSoggetto} - ${soggettoDaAggiornare.soggettoCessione.denominazione}</h5>
    
    <fieldset class="form-horizontal">   
		    <div class="control-group">
            	<label style="width: auto;" class="control-label" for="pagamento">Modalit&agrave; di pagamento : ${soggettoDaAggiornare.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.codice} - ${soggettoDaAggiornare.modalitaPagamentoSoggettoCessione2.modalitaAccreditoSoggetto.descrizione}  </label>
            </div>
            <div class="control-group">
            	<label class="control-label" for="notee">Note</label>
                <div class="controls"> 
                	<s:textarea id="notee" name="soggettoDaAggiornare.note" cssClass="span3"/>
                </div>
            </div>
            <div class="control-group">
            	<label class="control-label" for="scadenza">Data cessazione</label>
                <div class="controls">
                	<s:textfield id="scadenzaToInsert" name="dataScadenzaStringa" maxlength="10" cssClass="span3 datepicker"  />
                </div>
            </div> 
               <!--   <div class="control-group">
                <label class="control-label" for="pagamento">Modalit&agrave; pagamento</label>
                <div class="controls">
                <select id="pagamento" name="pagamento"><option>&nbsp;</option>
                                                      <option>xxx</option>
                  </select></div>
            </div> -->
                    
         </fieldset>  
         
         <p>
         	<a class="btn" href="modalitaPagamentoSoggettoCec_annullaInserimento.do">annulla inserimento</a>  
         	<!-- task-131 <s:submit cssClass="btn" name="salvaCessione" id="aggiornaId" value="Salva" method="aggiornaModPagCessione" /> -->
         	<s:submit cssClass="btn" name="salvaCessione" id="aggiornaId" value="Salva" action="modalitaPagamentoSoggettoCec_aggiornaModPagCessione" />  
         </p>
		</s:form>
		</s:if>        
      
 <!-- END SEZIONE AGGIORNA -->      
 <!-- Modal -->
<div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">


<div class="modal-body">
<div class="alert alert-error">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
	<p><strong>Attenzione!</strong></p>
	<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
</div>
</div>
<div class="modal-footer">
	<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
	<a href="#" class="btn btn-primary elimina-prosegui">si, prosegui</a>
</div>
</div>  
<!--/modale elimina -->
<!-- Modal -->
<div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
<div class="modal-body">
<div class="alert alert-error">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
    <p><strong>Attenzione!</strong></p>
    <p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
</div>
</div>
<div class="modal-footer">
	<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
	<a href="#" class="btn btn-primary annulla-prosegui">si, prosegui</a>
</div>
</div>  
  <!--/modale annulla -->
<!-- Modal -->
<div id="msgBlocca" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgBloccaLabel" aria-hidden="true">
<div class="modal-body">
<div class="alert alert-error">
	<button type="button" class="close" data-dismiss="alert">&times;</button>
    <p><strong>Attenzione!</strong></p>
    <p>Stai per bloccare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
</div>
</div>
<div class="modal-footer">
	<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
	<a href="modalitaPagamentoSoggettoCec_bloccaModalitaDiPagamento.do?aggiornaCodiceSoggetto=" class="btn btn-primary blocca-prosegui">si, prosegui</a>
</div>
</div>  
  <!--/modale blocca -->  
  	                                 
</s:form>
</div>	
</div>	
</div>	 
</div>	
<s:include value="/jsp/include/footer.jsp" />

<script type="text/javascript">
	$(document).ready(function() {
		
		// blocca pagina su salva 
		$('#salvaId').click(function() {
			bloccaPagina('#salvaId');
		});
		
		// blocca pagina su aggiorna 
		$('#aggiornaId').click(function() {
			bloccaPagina('#aggiornaId');
		});

		if($("#ancoraVisualizza").val() == 'true')
		{
			// metodo in generic.js che dato id ancora ti sposta con animazione
			spostaLAncora('ancoretta');
		}
		
		if($("#ancoraMdpVisualizza").val() == 'true')
		{
			// metodo in generic.js che dato id ancora ti sposta con animazione
			spostaLAncora('ancorettaMdp');
		}				
	
			
	});
</script>	