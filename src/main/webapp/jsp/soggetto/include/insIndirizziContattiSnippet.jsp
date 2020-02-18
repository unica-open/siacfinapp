<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<div class="accordion_info"> 
       <fieldset class="form-horizontal">

                       
                       
              
  				 <s:hidden id="idIndirizzo" name="idIndirizzo" />  
  
                                                  
             <div class="control-group">
                <label class="control-label" for="tipoind">Tipo indirizzo*</label>
                <div class="controls">
                 <!--  <select id="tipoind" name="tipoind"><option>sede principale</option><option>sede legale</option><option>sede avviso pagamenti</option><option>sede per incassi</option></select>
                 -->
                 <s:if test="null!=listaTipoIndirizzoSede">
                  <s:select list="listaTipoIndirizzoSede" id="tipoSede" name="indirizzo.tipoIndirizzo"  
 	                	          listKey="codice" listValue="descrizione"/>
 	             </s:if> 
 	            	           
 	             </div>   
 	             	          
            </div>

			
	        <div class="control-group">
                <label class="control-label" for="Ricorrente55">Principale</label>
                 <div class="controls">
                 <!-- 
                 <input id="Ricorrente2" type="checkbox" name="Ricorrente55"/>
                 -->
                 <s:checkbox id="ckPrincipale" name="indirizzo.checkPrincipale"/>
                 
                 <span class="al">
				 <label class="radio inline" for="Avviso">Avviso</label>
			     </span>
			     <s:checkbox id="ckAvviso" name="indirizzo.checkAvviso"/>
				 <!-- <input id="Avviso" type="checkbox" name="Avviso"/> -->
				 
			</div></div>
			
			
			<div class="control-group">
                <label class="control-label" for="Stato2">Stato\Comune *</label>
                <div class="controls">
                <!-- 	<select id="Stato2" name="Stato2" class="span3"><option>&nbsp;</option><option>italia</option><option>estero</option></select>
                -->
                 <s:if test="isNazioniPresenti()">
                 <s:select list="nazioni" id="idNazione" name="indirizzo.stato"   cssClass="span3"
 	                	          listKey="codice" listValue="descrizione"/>
 	                	          
 	                	              				 <s:hidden id="nomeNazione" name="indirizzo.nomeNazione" />  
 	                	          
 	             </s:if>   	           

			    <span class="al">
				<!-- <label class="radio inline" for="comune"> Comune</label> -->
				</span>
				<!-- <input id="comune" name="comune" class="lbTextSmall span3" type="text" placeholder="comune"/> Provincia: xxxx -->
				 <s:textfield id="comune" name="indirizzo.comune" title="comune" cssClass="lbTextSmall span3"   /> 
				 <s:hidden id="comuneId" name="indirizzo.idComune" />  

			    				
			</div>
		    </div>    


             <div class="control-group">
                <label class="control-label" for="sedimiList">Indirizzo *</label>
                <div class="controls">
				<!-- <input id="Sedime" name="Sedime" class="lbTextSmall span1" type="text" placeholder="sedime" required="required"/> -->
				 <s:textfield id="sedimiList" name="indirizzo.sedime" cssClass="lbTextSmall span1" title="sedime"  />
            
			    <span class="al">
				<!-- <label class="radio inline" for="nomevia">Nome via</label> -->
				</span>
				<!-- <input id="nomevia" name="nomevia" class="lbTextSmall span4" type="text" placeholder="nome via" required="required"/> -->
				<s:textfield id="nomeVia" name="indirizzo.nomeVia" cssClass="lbTextSmall span4" title="nome via"  />

				<span class="al">
				<!-- <label class="radio inline" for="numciv">Numero civico</label> -->
				</span>
				<!--  <input id="numciv" name="numciv"  class="lbTextSmall span1" type="text" placeholder="n. civico" required="required"/> -->
				<s:textfield id="numeroCivico" name="indirizzo.numeroCivico" cssClass="lbTextSmall span1" maxlength="7" title="n. civico"   />  
				
				<span class="al">
				<!-- <label class="radio inline" for="cap"><acronym title="codice di avviamento postale">\</acronym></label> -->
				</span>
				<!-- <input id="cap" name="cap" class="span1" type="text" placeholder="C.A.P." required="required"/>-->
				<s:textfield id="cap" name="indirizzo.cap"
				 			 onkeyup="return checkItNumbersOnly(event)" 
				             cssClass="span1" title="C.A.P."  maxlength="5" />   
            </div>
            </div>



		

	</fieldset>  
               <p>
                  <a class="btn" href="#"  id="annullaInserimentoIndirizzo" >annulla inserimento</a> 
                        		  
                  <s:submit id="salvaIndirizzo"  name="salvaIndirizzo" value="carica" method="salvaIndirizzo" 
                        		  cssClass="btn" data-target="#insInd" data-toggle="collapse" />   
                 
               </p>

                        </div>
                        
<script type="text/javascript">
$(document).ready(function() {
	var inserIndirizzo = $("#insInd");
	autocompleteCitta();
	autocompleteSedimi();
	$("#annullaInserimentoIndirizzo").click(function() {
		$.ajax({
			url: '<s:url method="pulisciIndirizzi"/>',
			    success: function(data)  {
				inserIndirizzo.html(data);
			}
		});
	});	

	$("#idNazione").change(function() {
		$("#nomeNazione").val($("#idNazione option:selected").text());
	});

});


$(document).ready(function() {
	var inserIndirizzo = $("#insInd");
	autocompleteCitta();
	autocompleteSedimi();
	$("#annullaInserimentoIndirizzo").click(function() {
		$.ajax({
			url: '<s:url method="pulisciIndirizzi"/>',
			    success: function(data)  {
				inserIndirizzo.html(data);
			}
		});
	});	
});


</script>                          