<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

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
    <p><a class="btn" href="#" id="annullaInserimentoContatto" >annulla inserimento</a>    
    	 <!-- task-131 <s:submit name="salvaRecapito" value="carica" method="salvaRecapito" cssClass="btn" /> -->
    	 <s:submit name="salvaRecapito" value="carica" action="%{#salvaRecapitoAction}" cssClass="btn" />  
      	<!--  <a class="btn" href="#"  data-toggle="collapse" data-target="#instelefono">salva</a> -->  
    </p>

    </div>   