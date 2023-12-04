<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

	<!-- SIAC-7349 Inizio  SR190 FL 15/04/2020 -->
	<s:include value="/jsp/movgest/include/modalCollegataSpesa.jsp" />
	<!-- SIAC-7349 Fine SR190 FL 15/04/2020 -->
	
<h5>
    Reimputazione Pluriennio
</h5>
<ul class="htmlelt">                                 
    <li>
      <dfn>Importo massimo differibile:</dfn>
      <dl><s:property value="model.importoMassimoDifferibile" /></dl>
    </li>
</ul>


<s:if test="isInInserimento()">
    <s:hidden id="inInserimentoValore" value="%{inInserimento}"/> 
    <s:hidden id="annoEsercizio" name="sessionHandler.annoEsercizio"/>
    <div class="control-group">
        <div class="control-group">
            <label class="control-label" for="motivo">Modifica motivo :  </label>
            <label class="control-label" for="motivo"><b><s:property value="%{descrizioneMotivoReimp}"/></b></label>
        </div>
        <!--ITERO LA LISTA DELLE REIMPUTAZIONI INIZIALIZZATA PER L'INSERIMENTO-->
        <div class="control-group">                            
            <s:iterator value="listaReimputazioneTriennio"  status="incr" >
                <div class="controls" style="margin-bottom: 10px;">
                    <s:textfield type="text" maxlength="4" name="listaReimputazioneTriennio[%{#incr.index}].anno" value="%{anno}"  id="annoReimp%{#incr.index}" class="span1 soloNumeri"/>&nbsp;
                    <%-- SIAC-7773  FL 18/09/2020 importi allargati --%>
                    <s:textfield type="text" maxlength="30"  name="listaReimputazioneTriennio[%{#incr.index}].importo" onblur="checkNullToEmptyString(event)" value="%{importo}"  id="inputReimp%{#incr.index}" placeholder="0.00" class="span2 soloNumeri decimale"/>&nbsp;
                    <s:select list="motiviRorReimputazione" id="listaSintesiMotiviReimputazione%{#incr.index}" headerKey="" 
                            headerValue=""  listKey="key" listValue="sintesi"  cssClass="span3" name="listaReimputazioneTriennio[%{#incr.index}].valoreSintesi" />
                    <s:textarea type="text"  name="listaReimputazioneTriennio[%{#incr.index}].descrizione" value="%{descrizione}"  id="descrizioneMotivoReimputazione%{#incr.index}" class="span5" style="resize: none; height: 30px;"/>

                    <s:hidden name="listaReimputazioneTriennio[%{#incr.index}].index" value="%{#incr.index}"/>
                    <s:hidden id= "motivo%{#incr.index}" name="listaReimputazioneTriennio[%{#incr.index}].idMotivo" value="%{reimputazione.id}"/>
                    <!-- SIAC-7349 Inizio  SR190 FL 15/04/2020 -->
                    <!-- CONTABILIA-260 - Bug fix per test 23.30 - SIAC-7349 Inizio CM 07/07/2020 -->
	               	<s:if test='%{model.step1Model.numeroSubImpegno == null || model.step1Model.numeroSubImpegno.equals("")}'> 
	               		<a id="linkAssociazioneSpesa_<s:property value='%{#incr.index}' />" href="#associaSpesa" data-toggle="modal" class="btn btn-primary linkAssociazioneSpesa">Associazioni</a>
					</s:if>
	               	<!-- SIAC-8826 lista associazioni visualizzabile anche su dettaglio se Ë flaggato propaga su accertamento -->
	               	<s:else>               			
	               		<span id="divAssociazioni_<s:property value='#incr.index' />" style="display: none"> 
	               			<a id="linkAssociazioneSpesa_<s:property value='%{#incr.index}' />" href="#associaSpesa" data-toggle="modal" class="btn btn-primary linkAssociazioneSpesa">Associazioni</a>
						</span>
					</s:else>
		            <!-- CONTABILIA-260 - Bug fix per test 23.30 - SIAC-7349 Fine CM 07/07/2020 -->
                    <!-- SIAC-7349 Fine  SR190 FL 15/04/2020 -->
                </div>
                <s:if test="%{#incr.index==0}">
                    <div class="controls" style="margin-top:10px; margin-bottom:10px;">
                        <input type="checkbox" id="spalmaDescrizione" style="margin-left:0px;" name="spalmaDescrizione" value="si"> Inserisci la stessa motivazione per tutte le annualit√†
                    </div>
                </s:if>
                <s:if test="%{#incr.index==2}">
                    <div class="controls">
                        <!--task-131 <s:submit onclick="overlayForWait()" cssClass="btn btn-primary" style="margin-bottom: 10px;" value="Aggiungi annualita† successiva" method="aggiungiAnnoReimputazione"/> -->
                        <s:submit onclick="overlayForWait()" cssClass="btn btn-primary" style="margin-bottom: 10px;" value="Aggiungi annualita† successiva" action="aggiornaModificaAccRor_aggiungiAnnoReimputazione"/>
                    </div>
                </s:if>
                

            </s:iterator>   
        </div> 
        
        
        <s:include value="/jsp/movgest/include/cruscottoRorCancellazioneAcc.jsp" /> 
        <s:include value="/jsp/movgest/include/cruscottoRorDaMantenereAcc.jsp" />    
          
        <s:if test='%{model.step1Model.numeroSubImpegno != null && !model.step1Model.numeroSubImpegno.equals("") }'>
            <s:if test="isAbilitaPropagaDaSub()">
                <div class="control-group">
                    <label class="control-label" for="motivo" style="margin-right: 10px;"> Propaga modifiche ad accertamento padre  :  </label>
                   <s:checkbox id="propagaSelected" name="propagaSelected" onchange="show_divAssociazioni()"/>
                </div>
            </s:if>             
        </s:if>
        <!--task-131 <s:submit onclick="overlayForWait()" cssClass="btn btn-primary pull-right" value="salva" method="salva"/>-->
        <s:submit onclick="overlayForWait()" cssClass="btn btn-primary pull-right" value="salva" action="aggiornaModificaAccRor_salva"/>
        <s:include value="/jsp/include/indietro.jsp" />       
        <!--task-131 <s:submit name="annulla" value="annulla" method="annulla" onclick="overlayForWait()" cssClass="btn btn-secondary" />-->
        <s:submit name="annulla" value="annulla" action="aggiornaModificaAccRor_annulla" onclick="overlayForWait()" cssClass="btn btn-secondary" />
        <s:hidden name="tipoImpegno" value="%{tipoImpegno}"></s:hidden>  
        <s:hidden name="subSelected" value="%{subSelected}"></s:hidden>
        <s:hidden name="numeroSubAccertamento" value="%{numeroSubAccertamento}"></s:hidden>          
    </div>    
</s:if>

<!-- parte di aggiornamento-->
<s:else>
    <div class="control-group">   
        <div class="control-group">
            <label class="control-label" for="motivo">Modifica motivo :  </label>
            <label class="control-label" for="motivo"><b><s:property value="%{descrizioneMotivoReimp}"/></b></label>
        </div>
        <s:hidden id="inInserimentoValore" value="%{inInserimento}"/> <!--per disattivare i controlli javascript-->
    
        <div class="control-group">

            <s:iterator value="listaModificheRor"  status="incr" >

                <s:if test='%{listaModificheRor[#incr.index].tipoModificaMovimentoGestione.equals("REIMP")}'>
                    <div class="controls" style="margin-bottom: 10px;">
                        <s:textfield type="text" maxlength="4" name="listaModificheRor[%{#incr.index}].annoReimputazione" value="%{annoReimputazione}"  id="annoReimp%{#incr.index}" class="span1 soloNumeri" readonly="%{listaModificheRor[#incr.index].uid!=0}"/>&nbsp;
                        <s:textfield type="text" maxlength="30"  name="listaModificheRor[%{#incr.index}].importoOld" value="%{importoOld}"  id="inputReimp%{#incr.index}" onblur="checkNullToEmptyString(event)" class="span2 soloNumeri decimale"/>&nbsp;

                        <s:if test="%{listaModificheRor[#incr.index].uid==0}">
                            <s:select list="motiviRorReimputazione" id="listaSintesiMotiviReimputazione%{#incr.index}" headerKey="" 
                                headerValue=""  listKey="key" listValue="sintesi"  cssClass="span3" name="listaModificheRor[%{#incr.index}].valoreSintesi" />
                        </s:if>
                        <s:else> 
                            <s:select list="motiviRorReimputazione" id="listaSintesiMotiviReimputazione%{#incr.index}" headerKey="" 
                                headerValue=""  listKey="key" listValue="sintesi"  cssClass="span3" />
                        </s:else>
                        
                        <s:textarea type="text"  name="listaModificheRor[%{#incr.index}].descrizioneModificaMovimentoGestione" value="%{descrizioneModificaMovimentoGestione}"  id="descrizioneMotivoReimputazione%{#incr.index}" class="span5" style="resize: none; height: 30px;"/>
                        <s:if test="%{listaModificheRor[#incr.index].uid!=0}">
                            <a class="tooltip-test" href="#" data-original-title="Per eliminare la modifica, cancella descrizione e importo">
                                <i class="icon-info-sign">&nbsp;
                                    <span class="nascosto">Per eliminare la modifica, cancella descrizione e importo</span>
                                </i>
                            </a>
                        </s:if>
                                
                        <!-- SIAC-7349 Inizio  SR190 FL 15/04/2020 -->
	                    <!-- CONTABILIA-260 - Bug fix per test sulla aggiorna accertamento ror derivante dalla 23.30 - SIAC-7349 Inizio CM 07/07/2020 -->
		               	<s:if test='%{model.step1Model.numeroSubImpegno == null || model.step1Model.numeroSubImpegno.equals("")}'>
						    <a id="linkAssociazioneSpesa_<s:property value='#incr.index' />" href="#associaSpesa" data-toggle="modal" class="btn btn-primary linkAssociazioneSpesa">Associazioni</a>
						</s:if>
						<!-- SIAC-8826 lista associazioni visualizzabile anche su dettaglio se Ë flaggato propaga su accertamento -->  	
	               		<s:else> 
	               			<s:if test="%{(listaModificheSpeseCollegata != null) && (listaModificheSpeseCollegata.size > 0) }">
	               				 <a id="linkAssociazioneSpesa_<s:property value='#incr.index' />" href="#associaSpesa" data-toggle="modal" class="btn btn-primary linkAssociazioneSpesa">Associazioni</a>
							</s:if>
	              	 		<s:else>
	              	 			<span id="divAssociazioni_<s:property value='#incr.index' />" style="display: none"> 
	               					<a id="linkAssociazioneSpesa_<s:property value='%{#incr.index}' />" href="#associaSpesa" data-toggle="modal" class="btn btn-primary linkAssociazioneSpesa">Associazioni</a>
								</span>
							</s:else>
						</s:else>
						<!-- CONTABILIA-260 - Bug fix per test sulla aggiorna accertamento ror derivante dalla 23.30 - SIAC-7349 Fine CM 07/07/2020 -->
                    	<!-- SIAC-7349 Fine  SR190 FL 15/04/2020 -->
                        
                        <s:hidden name="listaModificheRor[%{#incr.index}].index" value="%{#incr.index}"/>
                        <s:hidden name="listaModificheRor[%{#incr.index}].idMotivo" value="%{reimputazione.id}"/>
                        <s:hidden name="listaModificheRor[%{#incr.index}].uid" value="%{uid}"/>
                        <s:hidden name="listaModificheRor[%{#incr.index}].tipoModificaMovimentoGestione" value="%{tipoModificaMovimentoGestione}"/>
                    </div>
                    
                </s:if>    
            </s:iterator>
            
            <div class="controls">
                <!--task-131 <s:submit onclick="overlayForWait()" cssClass="btn btn-primary" style="margin-bottom: 10px;" value="Aggiungi annualita† successiva" method="aggiungiAnnoReimputazione"/>-->
                <s:submit onclick="overlayForWait()" cssClass="btn btn-primary" style="margin-bottom: 10px;" value="Aggiungi annualita successiva" action="aggiornaModificaAccRor_aggiungiAnnoReimputazione"/>
            </div>
                               
        </div> 
        
        <s:include value="/jsp/movgest/include/cruscottoRorCancellazioneAcc.jsp" /> 
        <s:include value="/jsp/movgest/include/cruscottoRorDaMantenereAcc.jsp" />
        <s:if test='%{model.step1Model.numeroSubImpegno != null && !model.step1Model.numeroSubImpegno.equals("") }'>
            <s:if test="isAbilitaPropagaDaSub()">
                <div class="control-group">
                    <label class="control-label" for="motivo" style="margin-right: 10px;"> Propaga modifiche ad accertamento padre  :  </label> 
                   <s:checkbox id="propagaSelected" name="propagaSelected" onchange="show_divAssociazioni()" />
                   <a class="tooltip-test" href="#" data-original-title="Verranno propagate all'impegno padre solo le nuove modifiche">
                    <i class="icon-info-sign">&nbsp;
                        <span class="nascosto">Verranno propagate solo le nuove modiche</span>
                    </i>
                    </a>
                </div>
            </s:if>             
        </s:if> 


        <!--task-131 <s:submit cssClass="btn btn-primary pull-right" value="salva" method="aggiornaModifiche" onclick="overlayForWait()"/>-->
        <s:submit cssClass="btn btn-primary pull-right" value="salva" action="aggiornaModificaAccRor_aggiornaModifiche" onclick="overlayForWait()"/>
        
        <s:include value="/jsp/include/indietro.jsp" />
        <!--task-131 <s:submit name="annulla" value="annulla" onclick="overlayForWait()" method="annulla" cssClass="btn btn-secondary" />-->
        <s:submit name="annulla" value="annulla" onclick="overlayForWait()" action="aggiornaModificaAccRor_annulla" cssClass="btn btn-secondary" />
        <s:hidden name="tipoImpegno" value="%{tipoImpegno}"></s:hidden>
        <s:hidden name="subSelected" value="%{subSelected}"></s:hidden>
        <s:hidden name="numeroSubAccertamento" value="%{numeroSubAccertamento}"></s:hidden>  
    </div>    
    

</s:else>


   