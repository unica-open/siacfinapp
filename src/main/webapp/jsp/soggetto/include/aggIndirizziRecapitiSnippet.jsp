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
                <label class="control-label" for="tipoind">Tipo indirizzo</label>
                <div class="controls">
<%--                 <select id="tipoind" name="tipoind"><option>sede principale</option><option>sede legale</option><option>sede avviso pagamenti</option><option>sede per incassi</option></select>
--%>
                  
                  <s:if test="listaTipoIndirizzoSede">
					 <s:select list="listaTipoIndirizzoSede" id="tipoSede" name="indirizzoSoggetto.idTipoIndirizzo"  
 	                	          listKey="codice" listValue="descrizione"/> 
 	              </s:if>   	          
 	              
</div> 
            </div>

			
	        <div class="control-group">
                <label class="control-label" for="Ricorrente55">Principale</label>
                 <div class="controls">
<!--                  <input id="Ricorrente2" type="checkbox" name="Ricorrente55"/> -->
 					 <s:checkbox id="ckPrincipale" name="indirizzoSoggetto.checkPrincipale"/>
                 <span class="al">
				 <label class="radio inline" for="Avviso">Avviso</label>
			     </span>
			     <s:checkbox id="ckAvviso" name="indirizzoSoggetto.checkAvviso"/>
<!-- 				 <input id="Avviso" type="checkbox" name="Avviso"/> -->
			</div></div>
			
			
			<div class="control-group">
                <label class="control-label" for="Stato2">Stato\Comune *</label>
                <div class="controls">
<%--                 	<select id="Stato2" name="Stato2" class="span3"><option>&nbsp;</option><option>italia</option><option>estero</option></select> --%>
				<s:if test="nazioni">
					 <s:select list="nazioni" id="idNazione" name="indirizzoSoggetto.codiceNazione"   cssClass="span3"
					 	                	          listKey="codice" listValue="descrizione"/>
				</s:if>	 	                	           
			    <span class="al">
				<!-- <label class="radio inline" for="comune"> Comune</label> -->
				</span>
<!-- 				<input id="comune" name="comune" class="lbTextSmall span3" type="text" placeholder="comune"/> Provincia: xxxx -->
				  <s:textfield id="comune" name="indirizzoSoggetto.comune" title="comune" cssClass="lbTextSmall span3"   /> 
				 <s:hidden id="comuneId" name="indirizzoSoggetto.idComune" />  

			    				
			</div>
		    </div>    


             <div class="control-group">
                <label class="control-label" for="Sedime">Indirizzo *</label>
                <div class="controls">
<!-- 				<input id="Sedime" name="Sedime" class="lbTextSmall span1" type="text" placeholder="sedime" /> -->
				<s:textfield id="sedimiList" name="indirizzoSoggetto.sedime" cssClass="lbTextSmall span1"  title="sedime" />
            
			    <span class="al">
				<!-- <label class="radio inline" for="nomevia">Nome via</label> -->
				</span>
<!-- 				<input id="nomevia" name="nomevia" class="lbTextSmall span4" type="text" placeholder="nome via" /> -->
				<s:textfield id="nomeVia" name="indirizzoSoggetto.denominazione" cssClass="lbTextSmall span4" title="nome via" />

				<span class="al">
				<!-- <label class="radio inline" for="numciv">Numero civico</label> -->
				</span>
<!-- 				<input id="numciv" name="numciv"  class="lbTextSmall span1" type="text" placeholder="n. civico" /> -->
				<s:textfield id="numciv" name="indirizzoSoggetto.numeroCivico" cssClass="lbTextSmall span1" maxlength="7"  title="n. civico" />
				
				<span class="al">
				<!-- <label class="radio inline" for="cap"><acronym title="codice di avviamento postale">\</acronym></label> -->
				</span>
<!-- 				<input id="cap" name="cap" class="span1" type="text" placeholder="C.A.P." />  -->
					<s:textfield id="cap" name="indirizzoSoggetto.cap" onkeyup="return checkItNumbersOnly(event)" 
					              cssClass="span1"  title="C.A.P" maxlength="5" />
            </div>
            </div>

			
        
                
          
                     </fieldset>  
                     <p>
                     	<p class="marginLarge">
                      		<a class="btn"  data-toggle="collapse" 
		     			 		data-target="#insInd"  href="#" id="annullaInserimentoIndirizzo">annulla inserimento</a>
                     	</p> 
<!--                       <a class="btn" href="#"  data-toggle="collapse" data-target="#insInd">salva</a>   -->
                        <!-- task-131 <s:submit name="salvaIndirizzo" value="carica" method="salvaIndirizzo"  cssClass="btn" /> -->
                        <s:submit name="salvaIndirizzo" value="carica"  action="%{#salvaIndirizzoAction}"  cssClass="btn" />                         
                     </p>

                        </div>                      
<script type="text/javascript">
$(document).ready(function() {
	var inserIndirizzo = $("#insInd");
	autocompleteCitta();
	autocompleteSedimi();
	$("#annullaInserimentoIndirizzo").click(function() {
		$.ajax({
			//task-131 url: '<s:url method="pulisciIndirizzi"/>',
			url: '<s:url action="%{#pulisciIndirizziAction}"/>',
			    success: function(data)  {
				inserIndirizzo.html(data);
				copiaNazioneInComune();
			}
		});
		
	});	
});
</script>        