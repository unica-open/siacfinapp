<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@ taglib uri="/struts-tags" prefix="s" %>



   <h4>Indirizzi</h4>
   <s:hidden name="pressedButton" id="pressedButton"></s:hidden>
   
<%--       <s:property value="stack('pippo')"/> --%>
      
      <display:table name="dettaglioSoggetto.indirizzi"  
                 class="table tableHover" summary="riepilogo indirizzo"
	         requestURI="aggiornaRecapiti.do" 
			 uid="tabAggiornaIndirizziID"  >
			  <display:column title="Tipo" property="idTipoIndirizzoDesc"/>
<!-- 			  getSedime() + " " + getDenominazione() + " " + getNumeroCivico() + ", " + getComune() + " " + getCap() -->
			  <display:column title="Indirizzo">
			     <s:property value="%{#attr.tabAggiornaIndirizziID.sedime}"/>
			     <s:property value="%{#attr.tabAggiornaIndirizziID.denominazione}"/>
			     <s:property value="%{#attr.tabAggiornaIndirizziID.numeroCivico}"/>
			     <s:property value="%{#attr.tabAggiornaIndirizziID.comune}"/>
			     <s:property value="%{#attr.tabAggiornaIndirizziID.cap}"/>
			  </display:column>
			  <display:column title="Principale">
			  		<s:if test="%{#attr.tabAggiornaIndirizziID.principale == 'true'}">si</s:if>
			  </display:column>
			  <display:column title="Avviso" >
			  	<s:if test="%{#attr.tabAggiornaIndirizziID.avviso == 'true'}">si</s:if>
			  </display:column>
			  <display:column title="" > 
			  
			  
			  
			  
<%-- 			  <s:if test="%{#attr.tabAggiornaIndirizziID.indirizzoIdProvvisorio != null && #attr.tabAggiornaIndirizziID.indirizzoIdProvvisorio != ''}"> --%>
<%-- 			  		<s:url id="updateUrl" action="aggiornaRecapiti.do">  --%>
<%-- 			  		   <s:param name="idIndirizzoProvvisorio" value="%{#attr.tabAggiornaIndirizziID.indirizzoIdProvvisorio}" /> --%>
<%-- 			        </s:url>   --%>
<%-- 			   </s:if> --%>
<%-- 			   <s:else> --%>
<%-- 			  		  <s:url id="updateUrl" action="aggiornaRecapiti.do">  --%>
<%-- 			   		      <s:param name="idIndirizzo" value="%{#attr.tabAggiornaIndirizziID.indirizzoId}" /> --%>
<%-- 			   		  </s:url>   --%>
<%-- 			   </s:else> --%>
			   
			   
			   	<div class="btn-group">
			    	
	                <button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
			     	<ul class="dropdown-menu pull-right" id="ul_action">
		     			 
		     			 	<li><a data-indirizzo_id="<s:property value="#attr.tabAggiornaIndirizziID.indirizzoId" />"
		     			 		data-indirizzo_id_provvisorio="<s:property value="#attr.tabAggiornaIndirizziID.indirizzoIdProvvisorio"/>"	
		     			 		data-tipo_indirizzo="<s:property value="#attr.tabAggiornaIndirizziID.idTipoIndirizzo"/>"	
		     			 		data-principale="<s:property value="#attr.tabAggiornaIndirizziID.principale"/>"	
		     			 		data-avviso="<s:property value="#attr.tabAggiornaIndirizziID.avviso"/>"	
		     			 		data-codice_nazione="<s:property value="#attr.tabAggiornaIndirizziID.codiceNazione"/>"	
		     			 		data-comune="<s:property value="#attr.tabAggiornaIndirizziID.comune"/>"	
		     			 		data-id_comune="<s:property value="#attr.tabAggiornaIndirizziID.idComune"/>"	
		     			 		data-sedime="<s:property value="#attr.tabAggiornaIndirizziID.sedime"/>"	
		     			 		data-denominazione="<s:property value="#attr.tabAggiornaIndirizziID.denominazione"/>"	
		     			 		data-numero_civico="<s:property value="#attr.tabAggiornaIndirizziID.numeroCivico"/>"	
		     			 		data-cap="<s:property value="#attr.tabAggiornaIndirizziID.cap"/>"	
		     			 		class="aggiornaIndirizzo" 
		     			 		data-toggle="collapse" 
		     			 		data-target="#insInd">aggiorna</a></li>

	    <s:set var="aggiornaRecapitiUrl" value="%{'aggiornaRecapiti'}"/>          
						

			  		<li><a href="<s:url action="%{#aggiornaRecapitiUrl}">
				        	 <s:param name="idIndirizzoProvvisorio" value="#attr.tabAggiornaIndirizziID.indirizzoIdProvvisorio" />
			   		         <s:param name="idIndirizzo" value="#attr.tabAggiornaIndirizziID.indirizzoId" />
				         <s:param name="pressedButton">eliminaIndirizzo</s:param>
                     </s:url>" data-toggle="modal" class="linkEliminaSoggetto">elimina</a></li>      
                         
			     	</ul>
			   </div>
			   
			   
			   
<%-- 			   		<button class="btn"  onclick="document.getElementById('pressedButton').value='${updateUrl}'">  --%>
<%--                        <s:a href="%{updateUrl}">  --%>
			    	
<%--  			      		 </s:a>  --%>
<!--  			       		<i class="icon-trash"></i> elimina -->
<!-- 				     </button>	  -->
			</display:column>	     	
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
<%-- 			  <s:url id="updateUrl" action="aggiornaRecapiti.do">  --%>
<%-- 			         <s:if test="%{#attr.tabAggiornaIndirizziID.indirizzoIdProvvisorio == null}"> --%>
<%-- 		        		 <s:param name="idIndirizzoProvvisiorio" value="%{#attr.tabAggiornaIndirizziID.indirizzoIdProvvisorio}" /> --%>
 			              
<%--  			         </s:if> --%>
<%--  			         <s:else> --%>
<%--  			            <s:param name="idIndirizzo" value="%{#attr.tabAggiornaIndirizziID.indirizzoId}" /> --%>
<%--  			         </s:else>     --%>
<%--                      </s:url>  --%>
                    
<%--                      <button class="btn"  onclick="document.getElementById('pressedButton').value='${updateUrl}'">  --%>
<%--                        <s:a href="%{updateUrl}">  --%>
			    	
<%--  			      		 </s:a>  --%>
<!--  			       		<i class="icon-trash"></i> elimina -->
<!-- 				     </button>	 	 -->
<%-- 			  </display:column> --%>
 
			
			</display:table>	                                  




<p>
         <a class="btn"  data-toggle="collapse" data-target="#insInd">inserisci nuovi indirizzi</a>
        </p>  
                                           
<div id="insInd" class="collapse">
 <!-- INCLUDE DEGLI INDIRIZZI -->   
	     <s:include value="/jsp/soggetto/include/aggIndirizziRecapitiSnippet.jsp" />
	    </div>
<!--                       <div class="alert alert-success"> -->
<!--     		<button type="button" class="close" data-dismiss="alert">&times;</button> -->
<!--  			I dati sono stati inseriti correttamente -->
<!--     	    </div> -->
 
 		<h4>Contatti</h4>
                <display:table name="dettaglioSoggetto.contatti" class="table tableHover" summary="riepilogo recapiti"
		         requestURI="aggiornaRecapiti.do" uid="tabAggiornaRecapitiID"  >
				 	 <display:column title="Tipo" property="descrizioneModo" />
					 <display:column title="Dato" property="descrizione" />
					 <display:column title="Avviso">
 					 	<s:if test="controlloAvviso(#attr.tabAggiornaRecapitiID.avviso)">si</s:if>
					 </display:column>
					 <display:column title="" >
					    <!-- qui va messo il pezzo mancante di if ed else -->
					    <s:if test="%{#attr.tabAggiornaRecapitiID.uidProvvisiorio != null && #attr.tabAggiornaRecapitiID.uidProvvisiorio != ''}">
					    	<s:url var="updateUrl" action="aggiornaRecapiti.do">
					     		<s:param name="uid" value="%{#attr.tabAggiornaRecapitiID.uidProvvisiorio}" />
	                     	</s:url>
					    </s:if>
					    <s:else>
					    	<s:url var="updateUrl" action="aggiornaRecapiti.do">
					     		<s:param name="uid" value="%{#attr.tabAggiornaRecapitiID.uid}" />
	                     	</s:url>
					    </s:else>
	                     <button class="btn" onclick="document.getElementById('pressedButton').value='${updateUrl}'" >
	                       <s:a href="%{updateUrl}">
					    	
					       </s:a>
					     
					     <i class="icon-trash"></i> elimina
					     </button>
					 </display:column>	

				</display:table>	
                    

 <p>
         <a class="btn"  data-toggle="collapse" data-target="#instelefono">inserisci contatti</a>
</p>  
                                           
<div id="instelefono" class="collapse">
 <div class="accordion_info"> 
       <fieldset class="form-horizontal">


	<div class="control-group">
			<label class="control-label" for="contatto">Nome contatto</label>
			<div class="controls">
				<s:textfield id="contatto" name="recapito.contatto"
					cssClass="span3" />
			</div>
		</div>		
			
			
			
			
	 <div class="control-group">
     <label class="control-label" for="Numero">Numero telefono</label>
     <div class="controls">
              <!-- <input id="Numero" name="Numero" class="span2" type="text"/> -->
              <s:textfield id="numeroTelefono" name="recapito.numeroTelefono" onkeyup="return checkItNumbersOnly(event)"
                           cssClass="span2"  />

			    <span class="al">
				<label class="radio inline" for="Numero2">Numero cellulare</label>
				</span>
				<!-- <input id="Numero2" name="Numero2" class="span2" type="text"/> -->
				<s:textfield id="numeroCellulare" name="recapito.numeroCellulare" 
				             onkeyup="return checkItNumbersOnly(event)" cssClass="span2"   />

			    <span class="al">
				<label class="radio inline" for="Numero3">Fax</label>
				</span>
				<!-- 
				<input id="Numero3" name="Numero3" class="span2" type="text"/>
				-->
				 <s:textfield id="numeroFax" name="recapito.numeroFax" 
				              onkeyup="return checkItNumbersOnly(event)" cssClass="span2" />
	 </div>
    </div>
	   



<div class="control-group">
    <label class="control-label" for="PEC"><acronym title="posta elettronica certificata">PEC</acronym></label>
    <div class="controls">
    <!-- 
	 <input id="PEC" name="PEC" class="span2" type="text"/>
	 -->
	 <s:textfield id="pec" name="recapito.pec" cssClass="span2" />
	 
	 <!--<label class="checkbox"><input type="checkbox"> Avviso</label> -->
     <span class="al">
     <!-- 
	 <label class="radio inline">
	      <input type="checkbox"> Avviso</label> -->
	  <div class="radio inline">
	   <s:checkbox id="checkAvvisoPec" name="recapito.checkAvvisoPec" value="si"/>&nbsp;Avviso
	   </div>      
	 </span>
	 <!--
	 <span class="al">
	 <label class="radio inline" for="mail">E-mail</label>
     <input id="mail" name="mail" class="span2" type="text"/>
	 <span class="al">
	 <label class="radio inline"> <input type="checkbox"> Avviso</label>
	 </span>
	 </span>
	 -->
	</div>
</div>         


<div class="control-group">
    <label class="control-label" for="mail">E-mail</label>
    <div class="controls">
    <!-- 
	 <input id="mail" name="mail" class="span2" type="text"/>
	 -->
	  <s:textfield id="mail" name="recapito.email"  cssClass="span2"  />
	 <!--<label class="checkbox"><input type="checkbox"> Avviso</label> -->
     <span class="al">
     <!-- 
	 <label class="radio inline"> <input type="checkbox"> Avviso</label>
	 -->
	  <div class="radio inline">
            	    <s:checkbox id="checkAvvisoEmail" name="recapito.checkAvvisoEmail" value="si"/>&nbsp;Avviso
		<!-- <input type="checkbox"> -->		 
		</div>
	 
	 </span>
	
	</div>
</div>         



<div class="control-group">
   <label class="control-label" for="web">Sito web</label>
   <div class="controls">
         <!--  <input id="web" name="web" class="span3" type="text"/> -->
         <s:textfield id="sitoWeb" name="recapito.sitoWeb" cssClass="span3" />
     </div>
</div>                  
                                               
                
                

                     </fieldset>  
                     <p>
                         <p class="marginLarge">
                         <!-- task-131 <a class="btn" href="<s:url method="pulisciContatto"/>"> -->
                     		 <a class="btn" href="<s:url action="%{#pulisciContattoAction}"/>">annulla inserimento</a>
                     	 </p>	         
                     	 <!-- task-131 <s:submit name="salvaRecapito" value="carica" method="salvaRecapito" cssClass="btn"/> -->  
                     	 <s:submit name="salvaRecapito" value="carica" action="%{#salvaRecapitoAction}" cssClass="btn"/>
                       <!--  <a class="btn" href="#"  data-toggle="collapse" data-target="#instelefono">salva</a> -->  
                     </p>

                        </div>     </div>
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
