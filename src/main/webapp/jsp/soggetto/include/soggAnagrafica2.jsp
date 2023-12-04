<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!--#include virtual="sogg_anagrafica2.html" -->	
  <h4>Dati soggetto</h4>
      
              <fieldset class="form-horizontal">
         	<div class="control-group">
                <label class="control-label" for="ente">Ragione sociale</label>
				 <div class="controls">
                 <!-- <input id="ente" name="Coentegnome" class="span6 required" type="text" placeholder="ragione sociale"/>
                 -->
                   <s:textfield id="denominazione" name="dettaglioSoggetto.denominazione" title="ragione sociale" 
                              cssClass="span6 required" />
                	<!-- <select id="ente" name="ente"><option>&nbsp;</option><option>pubblico e privato</option><option>x1</option><option>x2</option><option>x3</option><option>x4</option></select> -->
 				</div>
          </div>
			<div class="control-group">
                <label class="control-label" for="Cognome">Cognome e Nome</label>
                <div class="controls">
                	<!-- <input id="Cognome" name="Cognome" class="span2 required" type="text" placeholder="cognome"/> -->
                	<s:textfield id="cognome" name="dettaglioSoggetto.cognome" title="cognome" cssClass="span2 required" />
              	
				<span class="al">
				<!-- <input id="Nome" name="Nome" class="span2 required" type="text" placeholder="nome"/> -->
				<s:textfield id="nome" name="dettaglioSoggetto.nome" title="nome" cssClass="span2 required" />
				</span>

				<div  class="radio inline">
					<s:radio  id="sessoId" cssClass="flagSesso" name="dettaglioSoggetto.sessoStringa" list="radioSesso"  />
				</div>
				
			  </div>
          </div>
		  
		  <div class="control-group">
                <label class="control-label" for="Matricola">Matricola</label>
                <div class="controls">
                	<s:textfield id="matricola" name="dettaglioSoggetto.matricola"  maxlength="12" cssClass="min required" />
              </div>
          </div>
		  
          <div class="control-group">
                <label class="control-label" for="Nascita">Data di Nascita</label>
                <div class="controls">
                	<!-- <input id="Nascita" name="Nascita" class="min required" type="text"/>
                	value="%{dettaglioSoggetto.dataNascita}" -->
                	<s:textfield id="dataNascita" name="dataNascitaStringa"  maxlength="10" cssClass="min required" />
              </div>
          </div>
          <!-- <div class="control-group">

                <label class="control-label" for="Sesso">Sesso</label>
                <div class="controls">
                	 <select id="Sesso" name="Sesso"><option>&nbsp;</option><option>maschio</option><option>femmina</option></select>
              </div>
          </div> -->
		  
		    <div class="control-group">
                <label class="control-label" for="Stato2">Stato\Comune</label>
                <div class="controls">
                <!-- 
                	<select id="Stato2" name="Stato2" class="span3"><option>&nbsp;</option><option>italia</option><option>estero</option></select>
					-->
					 <s:select list="nazioni" id="idNazione" name="dettaglioSoggetto.comuneNascita.nazioneCode" headerKey="" headerValue=""  
 	                	          listKey="codice" listValue="descrizione"/> 
			    <span class="al">
				<!-- <label class="radio inline" for="comune"> Comune</label> -->
				</span>
				<s:textfield id="comune" name="dettaglioSoggetto.comuneNascita.descrizione" cssClass="lbTextSmall span3"/>
     			<s:hidden id="comuneId" name="dettaglioSoggetto.comuneNascita.uid"></s:hidden>    
				<!-- <input id="comune" name="comune" class="lbTextSmall span3" type="text" placeholder="comune"/> Provincia: xxxx -->

			    				
			</div>
		    </div>    


              <div class="control-group">
                <label class="control-label" for="Note">Note</label>
                 <div class="controls">
                 	<!-- <textarea rows="3" id="Note" class="span6"></textarea> -->
                 	 <s:textarea id="note" name="dettaglioSoggetto.note" rows="3" cssClass="span6" /> 
              </div>
          </div>
       </fieldset>
                
  