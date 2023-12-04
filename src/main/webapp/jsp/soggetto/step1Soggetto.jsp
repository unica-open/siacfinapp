<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>


    <%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />
    
    
  </head>

  <body>
  
 
  
  
  <s:include value="/jsp/include/header.jsp" />
  

    <div class="container-fluid">
<div class="row-fluid">


    <div class="span12 contentPage">
    	<%-- SIAC-7952 rimuovo .do dalla action --%>
        <s:form id="inserisciSoggetto" action="inserisciSoggetto" method="post">
        <h3>Inserisci un nuovo soggetto</h3>
        
        
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
			<div class="alert alert-warning">
				<button type="button" class="close" data-dismiss="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul>
					<s:actionmessage/>
				</ul>
			</div>
		</s:if>
        
        <div id="MyWizard" class="wizard">
					<ul class="steps">
						<li data-target="#step1" class="active"><span class="badge">1</span>Anagrafica<span class="chevron"></span></li>
						<li data-target="#step2"><span class="badge">2</span>Recapiti<span class="chevron"></span></li>
						<li data-target="#step3"><span class="badge">3</span>Salva<span class="chevron"></span></li>
						<!--<li data-target="#step4"><span class="badge">4</span>Salva<span class="chevron"></span></li> -->
					</ul>
				</div>
				
		<div class="step-content">
					<div class="step-pane active" id="step1">		
				
				<!-- PAOLO -->
				<h4>Natura e codici soggetto</h4>
        <fieldset class="form-horizontal">
         	<div class="control-group">
                <label class="control-label" for="ente">Tipo soggetto * </label>
				 <div class="controls">
				 <s:if test="null!=listaTipoSoggetto">
                	<s:if test="!effettuataRicercaAnagrafica">
 				      <s:select list="listaTipoSoggetto" id="listaTipoSoggetto"  headerKey="" 
  	                		   headerValue="" name="idTipoSoggetto" cssClass="span3"  
                	 	       listKey="codice" listValue="descrizione" /> 
                	</s:if>
                	<s:else>
                 	 	<s:select list="listaTipoSoggetto" id="listaTipoSoggetto"  headerKey=""  
  	                		   headerValue="" name="idTipoSoggetto" cssClass="span3"  
                  	 	       listKey="codice" listValue="descrizione" disabled="true" />  
                	 	       
                	 	 <s:hidden name="idTipoSoggetto"></s:hidden>        
                	</s:else> 	 
                </s:if> 	      
				<span class="al">
				<label class="radio inline" for="den">Natura giuridica</label>
                <s:if test="null!=listaNaturaGiuridica">
	                <s:if test="!effettuataRicercaAnagrafica">
	                  		<s:select list="listaNaturaGiuridica" id="listaNaturaGiuridica"  headerKey=""  
	 	                		 	  headerValue="" name="idNaturaGiuridica"  cssClass="span3"  
	                  	 	      	 listKey="codice" listValue="descrizione"/>  
	                </s:if>
	                <s:else>
	                          <s:select list="listaNaturaGiuridica" id="listaNaturaGiuridica"  headerKey=""  disabled="true" 
		                		 	  headerValue="" name="idNaturaGiuridica"  cssClass="span3"  
	                  	 	      	 listKey="codice" listValue="descrizione"/>  
	                	 	      	 
	                		 <s:hidden name="idNaturaGiuridica"></s:hidden>       
	                </s:else>
                </s:if>
				</span>
				
				<span class="al">
				<label class="radio inline" for="den">Canale PA/ PRIVATI</label>
                <s:if test="null!=listaCanalePA">
                <s:select list="listaCanalePA" id="listaCanalePA"  headerKey=""  
 	                		 	  headerValue="" name="canalePA"  cssClass="span3"  
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
                <s:if test="!effettuataRicercaAnagrafica">
                   <s:textfield id="codiceFiscale" name="codiceFiscale" 
                              cssClass="span3 required" maxlength="16" /> 
                </s:if>
                <s:else>
               		 <s:textfield id="codiceFiscale" name="codiceFiscale" readonly="true" 
                              cssClass="span3 required" maxlength="16" /> 
                </s:else> 
                	
 								
				<span class="al">
				<label class="radio inline" for="iva">Partita IVA</label>
				 <s:if test="!effettuataRicercaAnagrafica">
					<s:textfield id="partitaIva" name="partitaIva" 
				                 cssClass="span3 required" maxlength="11" /> 
                </s:if>
                <s:else>
               		 <s:textfield id="partitaIva" name="partitaIva"  readonly="true"
				                  cssClass="span3 required" maxlength="11" /> 
                </s:else>
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
                
                 <!-- control -->
                <div class="controls">
                 <!-- 
                 	<label class="radio inline">
                 	 
                        <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked="checked">
                        si
                     </label>
                     <label class="radio inline">
                        <input type="radio" name="optionsRadios" id="optionsRadios2" value="option2">
                        no
                        
                     </label>
				    -->
				      
                 	 <div class="radio inline">
                 	  <s:if test="!effettuataRicercaAnagrafica">
                 	  	 <s:radio id="flagResidenza" cssClass="flagResidenza" name="flagResidenza" list="radioResidenza"  />
                      </s:if>
                      <s:else>
                      	<s:radio id="flagResidenza" cssClass="flagResidenza" disabled="true" name="flagResidenza" list="radioResidenza"  />
                      	<s:hidden name="flagResidenza"></s:hidden>
                      </s:else> 
                     </div>   
                    
				    
				 
				
				<span class="al">
				<label class="radio inline" for="estero">Codice fiscale estero</label>
				     <s:if test="!effettuataRicercaAnagrafica">
						<s:textfield id="codiceFiscaleEstero" name="codiceFiscaleEstero" cssClass="span3" />
                     </s:if>
                     <s:else>
                     	<s:textfield id="codiceFiscaleEstero" readonly="true" name="codiceFiscaleEstero" cssClass="span3" />
                     </s:else>
				</span>
					 
              </div>
          </div>
		  
		  <div class="control-group">
			<label class="control-label"	for="codDestinatario">Codice destinatario/ IPA</label>
			<div class="controls">
	
			<%-- SIAC-7150 text toUpperCase --%>
			 <s:textfield
					id="codDestinatario" name="codDestinatario" cssClass="span2"
					maxlength="7" style="text-transform: uppercase"/>

			 <span class="al">
			 <label class="radio inline" for="emailPec">email PEC</label>
			
			  <s:textfield id="emailPec" name="emailPec" cssClass="span2"
					maxlength="256" />
	
			</span>
			</div>
		</div> 
	
		<div class="control-group">
			<label class="control-label" for=IstitutoDiCredito id="istitutoDiCred">Istituto di credito</label>
			<div class="controls">
                 <s:checkbox id="flagIstitutoDiCredito"  name="flagIstitutoDiCredito" /> 
			</div>
		</div> 
	
    <div id="dati-durc" class="hide">
		  
           <div class="control-group">
                <label class="control-label" for="codfisc">Data fine validit&agrave; DURC</label>
                <div class="controls">

                   <s:textfield id="dataFineValiditaDurc" name="dataFineValiditaDurc" readonly="true"
                              cssClass="datepicker" maxlength="16" /> 
			  </div>
          </div>


		<s:hidden id="tipoFonteDurc" name="tipoFonteDurc" disabled="true"/>

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

				<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="fonteDurcClassifId" />
			</div>

		</div>

		  
  
           <div class="control-group hide" id="noteDurcDiv">
                <label class="control-label" for="codfisc">Note DURC</label>
                <div class="controls">
                   <s:textfield id="noteDurc" name="noteDurc" readonly="true"
                              cssClass="span7 "  /> 
				</span>
			  
			  </div>
          </div>
     </div>
		  		  
		
       </fieldset>
              
    
				<!-- fine PAOLO -->
				

      <!--#include virtual="sogg_anagrafica.html" -->
      
        <p> 
<%--     <a class="btn btn-link" href="<s:url method="pulisci"/>">annulla</a>  --%>
 		 <!--task-131 <s:submit name="annullaControlla" value="annulla" method="pulisci" cssClass="btn" /> -->
 		 <!--task-131 <s:submit name="controllaDati" value="controlla dati" method="controllaDati" cssClass="btn" /> -->
         <s:submit name="annullaControlla" value="annulla" action="inserisciSoggetto_pulisci" cssClass="btn" /> 
         <s:submit name="controllaDati" value="controlla dati" action="inserisciSoggetto_controllaDati" cssClass="btn" />  
        </p>
        
         <s:hidden name="parametroRicercaControllaDati"/>	
         
         
<%--    <h5>Risultati della ricerca nelle fonti anagrafiche per <s:property value="parametroRicercaControllaDati"/> </h5>   --%>
   																
<%--    <display:table name="soggetti"  --%>
<%-- 		         class="table tableHover" summary="riepilogo indirizzo" --%>
<%-- 		         requestURI="inserisciSoggetto.do" --%>
<%-- 				 uid="tabMovCollID"  > --%>
				          
				          
<%-- 		<display:column title="Sel." class="cen" > --%>
				
<%--   			<s:radio list="%{#attr.tabMovCollID.idSoggetto}"  --%>
<%--        			     name="idSoggetto"  --%>
<%--        			     theme="displaytag"  --%>
<%--        			     value="%{#attr.tabMovCollID.idSoggetto}" /> --%>
							       			                
<%-- 		</display:column>      --%>
	      
<%-- 		<display:column title="Fonte" scope="row" property="fonte" />	 --%>
<%-- 		<display:column title="Tipo"  scope="row" property="tipo" /> --%>
<%-- 		<display:column title="Codice Fiscale" scope="row" property="codiceFiscale" />	 --%>
<%-- 		<display:column title="Descrizione"  scope="row" property="descrizione" />	 --%>
<%-- 		<display:column title="Indirizzo" scope="row" property="indirizzo" />	 --%>
         
				          
<%--   </display:table>	 --%>
   
<!-- 	<p> -->
<!--         <a class="btn btn-link" href="<s:url method="pulisci"/>">annulla</a> -->
<!--     	<input class="btn" type="button" value="seleziona soggetto"/>   -->
<!--     </p>       		 -->
                                          
            
       <s:if test="effettuataRicercaAnagrafica">
             
             <!--  </div> Seconda parte della pagina -->
             
              <h4>Dati soggetto</h4>
      
              <fieldset class="form-horizontal">
         	<div class="control-group">
                <label class="control-label" for="ente">Ragione sociale</label>
				 <div class="controls">
				 <!-- 
                 <input id="ente" name="Coentegnome" class="span6 required" type="text" placeholder="ragione sociale"/>
                 -->
                 <s:textfield id="denominazione" name="denominazione" placeholder="ragione sociale" 
                              cssClass="span6 required" />
                	<!-- <select id="ente" name="ente"><option>&nbsp;</option><option>pubblico e privato</option><option>x1</option><option>x2</option><option>x3</option><option>x4</option></select> -->
 				</div>
          </div>
			<div class="control-group">
                <label class="control-label" for="Cognome">Cognome e Nome</label>
                <div class="controls">
                	<!-- <input id="Cognome" name="Cognome" class="span2 required" type="text" placeholder="cognome"/> -->
                	 <s:textfield id="cognome" name="cognome" cssClass="span2 required" />
              	
				<span class="al">
				<!-- 
				<input id="Nome" name="Nome" class="span2 required" type="text" placeholder="nome"/>
				-->
				<s:textfield id="nome" name="nome" cssClass="span2 required" />
				</span>

                
				<div  class="radio inline">
					<s:radio  id="sessoId" cssClass="flagSesso" name="flagSesso" list="radioSesso"  />
				</div>
				
               <!-- 
                <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked="checked"> Maschio
                </label>
                <label class="radio inline">
                <input type="radio" name="optionsRadios" id="optionsRadios2" value="option2">Femmina
                -->
                </label>
			  </div>
          </div>
		  
		  <!--
            <div class="control-group">
                <label class="control-label" for="Nome">Nome</label>
                 <div class="controls">
                 	<input id="Nome" name="Nome" class="span8 required" type="text"/>
              </div>
          </div>
		  -->
		  <!--
         <div class="control-group">

                <label class="control-label" for="Sesso">Sesso</label>
                <div class="controls">
                	 <select id="Sesso" name="Sesso" class="span8"><option>&nbsp;</option><option>maschio</option><option>femmina</option></select>
              </div>
          </div>
		  -->
		  
		   <div class="control-group">
                <label class="control-label" for="matricola">Matricola</label>
                <div class="controls">
                    <s:textfield id="matricola" name="matricola"  maxlength="12" cssClass="min required" />
              </div>
          </div> 
		  
            <div class="control-group">
                <label class="control-label" for="Nascita">Data di Nascita</label>
                <div class="controls">
                    <s:textfield id="dataNascita" name="dataNascita"  maxlength="10" cssClass="min required datepicker" />
                	<!-- <input id="Nascita" name="Nascita" class="min required" type="text"/> -->
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
<%--                  <s:if test="isNazioniPresenti()"> --%>
                  <s:if test="null!=nazioni">
 	                <s:select list="nazioni" id="idNazione" name="idNazione" headerKey="" headerValue=""
 	                	          listKey="codice" listValue="descrizione"/>
 	              </s:if>
 	                	           
			    <span class="al">
				<!-- <label class="radio inline" for="comune"> Comune</label> -->
				</span>
				<!-- 
				<input id="comune" name="comune" class="lbTextSmall span3" type="text" placeholder="comune"/> Provincia: xxxx
                -->
			    <s:textfield id="comune" name="comune" cssClass="lbTextSmall span3"/>
     			<s:hidden id="codiceIstatComune" name="codiceIstatComune"></s:hidden>    
			</div>
		    </div>    

<!--		  
           <div class="control-group">
                <label class="control-label" for="Stato">Stato</label>
                <div class="controls">
                	<select id="Stato" name="Stato" class="span8"><option>&nbsp;</option><option>italia</option><option>estero</option></select>
              </div></div>
           <div class="control-group">
                <label class="control-label" for="Comune">Comune</label>
                <div class="controls">
                	<input id="Comune" name="Comune" class="span8 required" type="text"/> Provincia: xxxx
              </div>
          </div>
		  -->
              <div class="control-group">
                <label class="control-label" for="Note">Note</label>
                 <div class="controls">
                    <s:textarea id="note" name="note" rows="3" cssClass="span6" /> 
                 	<!-- <textarea rows="3" id="Note" class="span6"></textarea> -->
              </div>
          </div>
       </fieldset>
    
                
      <h4>Classificazioni</h4>  
    <fieldset class="form-horizontal">
         	<div class="control-group">
                <label class="control-label" for="classif">Tipo classificazione</label>
				 <div class="controls">
				 <s:if test="null!=listaClasseSoggetto">
				    <s:select list="listaClasseSoggetto" id="idClasseSoggetto" name="idClasseSoggetto" 
 	                	      multiple="true" title="Scegli il tipo di classificazione"   
 	                	      listKey="codice" listValue="codice+' - '+descrizione" cssClass="multiSelectCustom"/> 
 	              </s:if>  	      
                	<!--  <select id="classif" name="classif" class="selectpicker" multiple title='Scegli il tipo di classificazione'><option>&nbsp;</option><option>x0</option><option>x1</option><option>x2</option><option>x3</option><option>x4</option></select>
                	-->
 				</div>
          </div>
          </fieldset>
		  
		  
		  
           <h4>Oneri Fiscali e ritenute</h4>  
    <fieldset class="form-horizontal">
         	<div class="control-group">
                <label class="control-label" for="Oneri">Oneri</label>
				 <div class="controls">
				 <s:if test="null!=listaTipoOnere">
					  <s:select list="listaTipoOnere" id="idTipoOnere" name="idTipoOnere" 
	 	                	      multiple="true" title="Scegli il tipo di onere"   
	 	                	      listKey="codice" listValue="codice+' - '+descrizione" cssClass="multiSelectCustom"/> 
				  </s:if>
                	 
 				</div>
          </div>
          </fieldset>
             
             <p>
             
               <s:include value="/jsp/include/indietro.jsp" />
             
<%--          	   <a class="btn btn-link" href="<s:url method="pulisci"/>">annulla</a> --%>
         	   <!-- task-131 <s:submit name="pulisciFondo" value="annulla" method="pulisci" cssClass="btn btn-link" /> -->
               <!-- task-131 <s:submit name="prosegui" value="prosegui" method="prosegui" cssClass="btn btn-primary pull-right" /> -->
               <s:submit name="pulisciFondo" value="annulla" action="inserisciSoggetto_pulisci" cssClass="btn btn-link" />
               <s:submit name="prosegui" value="prosegui" action="inserisciSoggetto_prosegui" cssClass="btn btn-primary pull-right" />
                
          </p>
             
             
  </s:if><!-- fine visualizzazione seconda parte -->      
             
             
             <!--  fine seconda parte della pagina -->
				</div>
      </s:form>
    </div>
</div>	 
</div>	

<!-- paolo -->
 </div>


<script type="text/javascript">
var elencoStruttureAmministrativoContabiliJson = 
	<s:property escapeHtml="false" value="elencoStruttureAmministrativoContabiliJson" />;
</script>	
	

    <%-- Inclusione JavaScript NUOVO --%>
   <s:include value="/jsp/include/javascript.jsp" />
   
   <script src="${jspath}soggetto/step1Soggetto.js" type="text/javascript"></script>
   
   
   
<s:include value="/jsp/include/footer.jsp" />