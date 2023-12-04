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
	
  </head>

  <body>
  
  
<!--   <p class="nascosto"><a name="A-sommario" title="A-sommario"></a></p> -->
     
<!--   <ul id="sommario" class="nascosto"> -->
<!--     <li><a href="#A-contenuti">Salta ai contenuti</a></li> -->
<!--   </ul> -->
 
<!--   <hr /> -->


    




<s:include value="/jsp/include/header.jsp" />

    <div class="container-fluid">
<div class="row-fluid">


    <div class="span12 contentPage">
    	 <%-- SIAC-7952 rimuovo .do dalla action --%>
         <s:form id="inserisciContatti"  action="inserisciContattiCec" method="post">
         
          <s:set var="salvaRecapitoAction" value="%{'inserisciContattiCec_salvaRecapito'}" />	          
		  <s:set var="salvaIndirizzoAction" value="%{'inserisciContattiCec_salvaIndirizzo'}" />	          
	      <s:set var="pulisciIndirizziAction" value="%{'inserisciContattiCec_pulisciIndirizzi'}" />	          
	    		   
        <h3>Inserisci un nuovo soggetto</h3>
        <!-- <p> Segui i passi indicati per inserire i dati dell'ente. 
        <br/>Non &egrave; necessario compilare tutti i campi, ma solo quelli obbligatori: xxx, xxxx e xxxxx.</p>
        -->
        
<%-- <s:include value="/jsp/include/actionMessagesErrors.jsp" /> --%>

 <s:if test="hasActionErrors()">
						<%-- Messaggio di ERROR --%>
						<div class="alert alert-error">
							<button type="button" class="close" data-dismiss="alert">&times;</button>
							<strong>Attenzione!!</strong><br>
							<ul>
								<s:actionerror/>
							</ul>
						</div>
					</s:if>
					
			<s:if test="hasActionMessages()">
				<%-- Messaggio di WARNING --%>
				<!-- <div class="alert alert-warning"> -->
				   <div class="alert alert-success">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<strong>Attenzione!!</strong><br>
					<ul>
					    <s:actionmessage/>
					</ul>
				</div>
			</s:if>
        
<s:if test="hasActionWarnings()">
	<%-- Messaggio di WARNING --%>
	<div class="alert alert-warning">
		<button type="button" class="close" data-dismiss="alert">&times;</button>
		<strong>Attenzione!!</strong><br>
		<ul>
		   <s:iterator value="actionWarnings">
		       <s:property  escapeHtml="false" /><br>
		   </s:iterator>
			
		</ul>
	</div>
</s:if>   

        
        <div id="MyWizard" class="wizard">
					<ul class="steps">
						<li data-target="#step1" class="complete"><span class="badge badge-success">1</span>Anagrafica<span class="chevron"></span></li>
						<li data-target="#step2" class="active"><span class="badge">2</span>Recapiti<span class="chevron"></span></li>
						<li data-target="#step3"><span class="badge">3</span>Salva<span class="chevron"></span></li>
<!--						<li data-target="#step4"><span class="badge">4</span>Salva<span class="chevron"></span></li> -->
					</ul>
				</div>
				<div class="step-content">
					<div class="step-pane active" id="step1">
                    
                    


         <!--#include virtual="sogg_contatti.html" -->		

<!--  INIZIO DATI -->


   <h4>Indirizzi</h4>
   
   <s:hidden name="pressedButton" id="pressedButton"></s:hidden>
   
      
              <!--  DISPLAY TAG -->
              
              <display:table name="sessionScope.INSERISCI_SOGGETTO_STEP2.indirizzi"  
                 class="table tableHover tab_left" summary="riepilogo indirizzo"
		         requestURI="inserisciContattiCec.do" 
				 uid="tabContattiID"  >
				 
				  <display:column title="Tipo"  property="tipoIndizzoDesc" />	
				 
				  <display:column title="Indirizzo"   property="indirizzoEsteso" />
				  <display:column title="Principale"  property="principale" />
				  <display:column title="Avviso"  property="avviso" />
				  <display:column title="" class="tab_Right">
				     
<%-- 				     <s:url id="updateUrl" action="inserisciContatti.do"> --%>
<%-- 				         <s:param name="idIndirizzo" value="%{#attr.tabContattiID.idIndirizzo}" /> --%>
<%--                      </s:url> --%>
                     
<%--                      <button class="btn"  onclick="document.getElementById('pressedButton').value='${updateUrl}'" > --%>
<%--                        <s:a href="%{updateUrl}"> --%>
				    	
<%-- 				       </s:a> --%>
				     
				     <s:param name="idIndirizzo" value="%{#attr.tabContattiID.idIndirizzo}" />
				     	
				 	
				 	<div class="btn-group">
			    	
	                <button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
			     	<ul class="dropdown-menu pull-right" id="ul_action">
		     			 
		     			 	<li><a data-id_indirizzo="<s:property value="#idIndirizzo" />"
		     			 		data-tipo_indirizzo="<s:property value="#attr.tabContattiID.tipoIndirizzo"/>"	
		     			 		data-principale="<s:property value="#attr.tabContattiID.principale"/>"	
		     			 		data-avviso="<s:property value="#attr.tabContattiID.avviso"/>"	
		     			 		data-stato="<s:property value="#attr.tabContattiID.stato"/>"	
		     			 		data-comune="<s:property value="#attr.tabContattiID.comune"/>"	
		     			 		data-id_comune="<s:property value="#attr.tabContattiID.idComune"/>"	
		     			 		data-sedime="<s:property value="#attr.tabContattiID.sedime"/>"	
		     			 		data-nome_via="<s:property value="#attr.tabContattiID.nomeVia"/>"	
		     			 		data-numero_civico="<s:property value="#attr.tabContattiID.numeroCivico"/>"	
		     			 		data-cap="<s:property value="#attr.tabContattiID.cap"/>"	
		     			 		class="aggiornaIndirizzo" 
		     			 		data-toggle="collapse" 
		     			 		data-target="#insInd">aggiorna</a></li>

                     		<li><a href="<s:url action="inserisciContattiCec.do">
				         <s:param name="idIndirizzo" value="#idIndirizzo" />
				         <s:param name="pressedButton">eliminaIndirizzo</s:param>
                     </s:url>" data-toggle="modal" class="linkEliminaSoggetto">elimina</a></li>                        	
                         
			     	</ul>
			   </div>
				 	
				 	
				 	
				 	
				 	
				 </display:column>
				  
				</display:table>	 
              


<p>
         <a class="btn"  data-toggle="collapse" data-target="#insInd">inserisci nuovi indirizzi</a>
        </p>  
                                           
	  <div id="insInd" class="collapse">
	  
	     <!-- INCLUDE DEGLI INDIRIZZI -->   
	     <s:include value="/jsp/soggetto/include/insIndirizziContattiSnippet.jsp" />
	                        
	  </div>
                
 <h4>Contatti</h4>
                 
                 <display:table name="sessionScope.INSERISCI_SOGGETTO_STEP2.recapiti"  class="table tableHover" summary="riepilogo recapiti"
		         requestURI="inserisciContattiCec.do"
				 uid="tabRecapitiID"  >
				 	 <display:column title="Tipo"  property="descrizioneModo" />
					 <display:column title="Dato"  property="descrizione" />
					 <display:column title="Avviso">
					 	<s:if test="%{#attr.tabRecapitiID.avviso == 'true'}">si</s:if>
					 </display:column>
					 <display:column title="" >
					    
					     <%-- task-131<s:url id="updateUrl" action="inserisciContattiCec.do"> --%>
					     <s:url var="updateUrl" action="inserisciContattiCec.do">
					         <s:param name="tipoRecapito" value="%{#attr.tabRecapitiID.descrizioneModo}" />
	                     </s:url>
	                     
	                     <button class="btn"  onclick="document.getElementById('pressedButton').value='${updateUrl}'" >
	                       <s:a href="%{updateUrl}">
					    	
					       </s:a>
					     
					     <i class="icon-trash"></i> elimina
					     </button>
					 </display:column>	

				</display:table>	 
                    

 <p>
         <a class="btn"  data-toggle="collapse" data-target="#instelefono">inserisci contatti</a>
</p>  
                        
                        <!-- INCLUDE DEI CONTATTI -->                     
<div id="instelefono" class="collapse">
  
      <s:include value="/jsp/soggetto/include/insContattiSnippet.jsp" />
           
 </div>
                        <!-- 
                        <div class="alert alert-success">
    		<button type="button" class="close" data-dismiss="alert">&times;</button>
   				
           

			I dati sono stati inseriti correttamente

    	</div>		         
    	-->       
  
<!-- Modal -->
<div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
<!--<div class="modal-header">
<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
<h3 id="myModalLabel">Attenzione</h3>
</div> -->
<div class="modal-body">
<div class="alert alert-error">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
          <p><strong>Attenzione!</strong></p>
          <p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
        </div>
</div>
<div class="modal-footer">
<button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
<button class="btn btn-primary">si, prosegui</button>
</div>
</div>  
<!--/modale elimina -->


   
<p> <s:include value="/jsp/include/indietro.jsp" />   
   <!-- task-131 <s:submit name="annulla" value="annulla" method="annullaIndirizziERecapiti" cssClass="btn" /> -->
   <!-- task-131 <s:submit name="salva" value="salva" method="salva" id="salvaId" cssClass="btn btn-primary pull-right" /> -->
   <s:submit name="annulla" value="annulla" action="inserisciContattiCec_annullaIndirizziERecapiti" cssClass="btn" />
   <s:submit name="salva" value="salva" action="inserisciContattiCec_salva" id="salvaId" cssClass="btn btn-primary pull-right" />
</p>
</div>
</div>
</div>
</s:form>
</div>
</div>	 
<!-- </div>	 -->

<script type="text/javascript">

$(document).ready(function() {
	initAnnullaInsContattoClick();
	
	 // blocca pagina su salva 
	 $('#salvaId').click(function() { 
	 	bloccaPagina('#salvaId');
	 }); 
	 
	 
	 
	$("a.aggiornaIndirizzo").click(function() 
	{ 	
		$("#idIndirizzo").val($(this).data('id_indirizzo'));
		$("#tipoSede").val($(this).data('tipo_indirizzo'));
	 	$("#ckPrincipale").attr("checked", true === $(this).data('principale'));
	 	$("#ckAvviso").attr("checked", true === $(this).data('avviso')); 	
		$("#idNazione").val($(this).data('stato'));
	 	$("#comune").val($(this).data('comune'));
	 	$("#comuneId").val($(this).data('id_comune'));
	 	$("#sedimiList").val($(this).data('sedime'));
	 	$("#nomeVia").val($(this).data('nome_via'));
	 	$("#numeroCivico").val($(this).data('numero_civico'));
	 	$("#cap").val($(this).data('cap'));
	 	
	 	$("#annullaInserimentoIndirizzo").html("annulla aggiornamento");
	 	$("#salvaIndirizzo").val("aggiorna");
	 	
	 	$("#pressedButton").val("aggiornaIndirizzo");
	 });
	
	
	
	
	$("#idNazione").change(function() {
		$("#comune").val($("option:selected", this).text()).trigger({type: 'keydown', keyCode: 1, which: 1, charCode: 1});
	});
	
});


function initAnnullaInsContattoClick() {
	var insContatti = $("#instelefono");
	$("#annullaInserimentoContatto").click(function() {
		$.ajax({
			//task-131 url: '<s:url method="pulisciContatto"/>',
			url: '<s:url action="inserisciContattiCec_pulisciContatto"/>',
			success: function(data)  {
			    insContatti.html(data);
			    initAnnullaInsContattoClick();
			}
		});
	});	
}

</script>
<s:include value="/jsp/include/footer.jsp" />