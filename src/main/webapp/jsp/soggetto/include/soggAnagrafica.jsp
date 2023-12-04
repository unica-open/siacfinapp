<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
 <!--#include virtual="sogg_anagrafica.html" -->
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
  <h4>Natura e codici soggetto</h4>
        <fieldset class="form-horizontal">
         	<div class="control-group">
                <label class="control-label" for="ente">Tipo soggetto * </label>
				 <div class="controls">
                	 <!--<select id="ente" name="ente" class="span3"><option>&nbsp;</option><option>pubblico e privato</option><option>x1</option><option>x2</option><option>x3</option><option>x4</option></select>
                	 -->
                	 <s:if test="null!=listaTipoSoggetto">
                	   <s:select list="listaTipoSoggetto" id="listaTipoSoggetto"  headerKey="" 
  	                		   headerValue="" name="dettaglioSoggetto.tipoSoggetto.soggettoTipoCode" cssClass="span3"  
                	 	       listKey="codice" listValue="descrizione" />
                	 </s:if>	        
 				
				<span class="al">
				<label class="radio inline" for="den">Natura giuridica</label>
                <!--<select id="den" name="den" class="span3"><option>&nbsp;</option><option>xxxx</option><option>x1</option><option>x2</option><option>x3</option><option>x4</option></select>
                -->
                <s:if test="null!=listaNaturaGiuridica">
                <s:select list="listaNaturaGiuridica" id="listaNaturaGiuridica"  headerKey=""  
 	                		 	  headerValue="" name="dettaglioSoggetto.naturaGiuridicaSoggetto.soggettoTipoCode"  cssClass="span3"  
                  	 	      	 listKey="codice" listValue="descrizione"/>
                 </s:if> 	 	      	   
				</span>
				
				<span class="al">
				<label class="radio inline" for="den">Codice PA/ PRIVATI</label>
                <s:if test="null!=listaCanalePA">
                <s:select list="listaCanalePA" id="listaCanalePA"  headerKey=""  
 	                		 	  headerValue="" name="dettaglioSoggetto.canalePA"  cssClass="span3"  
                  	 	      	 listKey="codice" listValue="descrizione"/>
                 </s:if> 	 	      	   
				</span>
				
				</div>
          </div>
			
			<!--
			<div class="control-group">
                <label class="control-label" for="den">Natura giuridica</label>
                <div class="controls">
                <select id="den" name="den" class="span8"><option>&nbsp;</option><option>xxxx</option><option>x1</option><option>x2</option><option>x3</option><option>x4</option></select>
                	
              </div>
          </div>
		  -->
           <div class="control-group">
                <label class="control-label" for="codfisc">Codice Fiscale</label>
                <div class="controls">
                	<!-- <input id="codfisc" name="codfisc" class="span3 required" type="text"/> -->
                	<s:textfield id="codiceFiscale" name="dettaglioSoggetto.codiceFiscale" 
                              cssClass="span3 required" maxlength="16" /> 
 								
				<span class="al">
				<label class="radio inline" for="iva">Partita IVA</label>
                <!-- <input id="iva" name="iva" class="span3 required" type="text"/> -->
                <s:textfield id="partitaIva" name="dettaglioSoggetto.partitaIva" 
				            cssClass="span3 required" maxlength="11" /> 
				</span>
             
			  
			  
			  </div>
          </div>
		  
		  
		  <!--
            <div class="control-group">
                <label class="control-label" for="iva">Partita IVA</label>
                 <div class="controls">
                 	<input id="iva" name="iva" class="span8 required" type="text"/>
              </div>
          </div>
		  
		  -->
		  
           <div class="control-group">
                <span class="control-label">Residente estero</span>
                 <div class="controls">
                  <div class="radio inline">
                 	 <s:radio id="flagResidenza" cssClass="flagResidenza" name="dettaglioSoggetto.residenteEsteroStringa" list="radioResidenza"  />
                  </div>	 
				
				
				<span class="al">
				<label class="radio inline" for="estero">Codice fiscale estero</label>
	                <!-- <input id="estero" name="estero" class="span3" type="text"/> -->
	                <s:textfield id="codiceFiscaleEstero" name="dettaglioSoggetto.codiceFiscaleEstero" cssClass="span3" />
				</span>
				<%-- SIAC-7150 text toUpperCase --%>
				<span class="al">
						<label class="radio inline" for="codDestinatario">Codice destinatario/ IPA</label>
						<s:textfield id="codDestinatario" name="dettaglioSoggetto.codDestinatario" cssClass="span2" maxlength="7" style="text-transform: uppercase"/>
				</span>
				<span class="al">
					<label class="radio inline" for="emailPec">email PEC</label>
					<s:textfield id="emailPec" name="dettaglioSoggetto.emailPec" cssClass="span2" maxlength="256"/>
				</span>	 
              </div>
          </div>
		  
		  <!--
           <div class="control-group">
                <label class="control-label" for="estero">Codice fiscale estero</label>
                 <div class="controls">
                 	<input id="estero" name="estero" class="span8" type="text"/>
              </div>
          </div>
           -->
           
           
           
           
           
           
           

    <div id="dati-durc" class="hide">
		  
           <div class="control-group">
                <label class="control-label" for="codfisc">Data fine validit&agrave; DURC</label>
                <div class="controls">

                   <s:textfield id="dataFineValiditaDurc" name="dettaglioSoggetto.dataFineValiditaDurc" readonly="true"
                              cssClass="datepicker" maxlength="16" /> 
			  </div>
          </div>


		<s:hidden id="tipoFonteDurc" name="dettaglioSoggetto.tipoFonteDurc"/>

		<div class="control-group hide" id="fonteDurcClassifIdDiv">

			<label class="control-label">Fonte DURC</label>
			<div class="controls">
                  <s:textfield id="descrizioneFonteDurc" name="dettaglioSoggetto.descrizioneFonteDurc" readonly="true"
                         cssClass="span7 hide" /> 
			
				<div class="accordion span8 struttAmm hide" id="accordionStrutturaAmministrativaContabileAttoAmministrativo">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a class="accordion-toggle collapsed" id="accordionPadreStrutturaAmministrativa" href="#collapseStrutturaAmministrativaContabileAttoAmministrativo"
								data-toggle="collapse" data-parent="#accordionStrutturaAmministrativaContabileAttoAmministrativo">
								<span id="SPAN_StrutturaAmministrativoContabile">Seleziona la Struttura amministrativa</span>
							</a>
						</div>
						<div id="collapseStrutturaAmministrativaContabileAttoAmministrativo" class="accordion-body collapse">
							<div class="accordion-inner">
								<ul id="treeStruttAmm" class="ztree treeStruttAmm"></ul>

								<span class="hide" id="btnStrutturaAmministrativoContabile">
									<button type="button" id="confermaStrutturaAmministrativoContabile" 
										class="btn btn-primary pull-right" data-toggle="collapse" data-target="#collapseStrutturaAmministrativaContabileAttoAmministrativo">Conferma</button>
									<button type="button" class="btn btn-secondary pull-right"
											id="deselezionaStrutturaAmministrativoContabile">
										Deseleziona
									</button>
								</span>

							</div>
						</div>
					</div>
				</div>

				<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="dettaglioSoggetto.fonteDurcClassifId" />
			</div>

		</div>

		  
  
           <div class="control-group hide" id="noteDurcDiv">
                <label class="control-label" for="codfisc">Note DURC</label>
                <div class="controls">
                   <s:textfield id="noteDurc" name="dettaglioSoggetto.noteDurc" readonly="true"
                              cssClass="span7 "  /> 
				</span>
			  
			  </div>
          </div>

			<div class="control-group">
			<label class="control-label" for=IstitutoDiCredito id="istitutoDiCred">Istituto di credito</label>
			<div class="controls">
                 <s:checkbox id="flagIstitutoDiCredito"  name="dettaglioSoggetto.flagIstitutoDiCredito" /> 
			</div>
		</div> 
    </div>
           
           
           
           
           
           
           
           
       </fieldset>
              
    