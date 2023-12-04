<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<!-- SIAC-7349 Inizio  SR190 CM 20/05/2020 - Aggiunta per visualizzare la consulta dal pulsante associazioni -->
<s:include value="/jsp/movgest/include/modalCollegataSpesa.jsp" />
<!-- SIAC-7349 Fine SR190 CM 20/05/2020 -->

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
    <div class="control-group">
        <div class="control-group">
            <label class="control-label" for="motivo">Modifica motivo :  </label>
            <label class="control-label" for="motivo"><b><s:property value="%{descrizioneMotivoReimp}"/></b></label>
        </div>
        <!--ITERO LA LISTA DELLE REIMPUTAZIONI INIZIALIZZATA PER L'INSERIMENTO-->
        <div class="control-group">                            
            <s:iterator value="listaReimputazioneTriennio"  status="incr" >
                <div class="controls" style="margin-bottom: 10px;">
                    <s:textfield type="text" maxlength="4"  name="listaReimputazioneTriennio[%{#incr.index}].anno" value="%{anno}"  id="annoReimp%{#incr.index}" class="span1 soloNumeri"/>&nbsp;
                    <s:textfield type="text" maxlength="30"  name="listaReimputazioneTriennio[%{#incr.index}].importo" value="%{importo}"  id="inputReimp%{#incr.index}" onblur="checkNullToEmptyString(event)" placeholder="0.00" class="span2 soloNumeri decimale"/>&nbsp;
                    <s:select list="motiviRorReimputazione" id="listaSintesiMotiviReimputazione%{#incr.index}" headerKey="" 
                            headerValue=""  listKey="key" listValue="sintesi"  cssClass="span3" name="listaReimputazioneTriennio[%{#incr.index}].valoreSintesi" />
                    <s:textarea type="text"  name="listaReimputazioneTriennio[%{#incr.index}].descrizione" value="%{descrizione}"  id="descrizioneMotivoReimputazione%{#incr.index}" class="span5" style="resize: none; height: 30px;"/>

	                <!-- CONTABILIA-260 - Bug fix per test 24.2, 24.3 - SIAC-7349 Inizio CM 07/07/2020 -->
	                <s:if test='%{model.step1Model.numeroSubImpegno == null || model.step1Model.numeroSubImpegno.equals("")}'>
		                <s:if test='%{listaModificheRor[#incr.index].uid!=0}'>
	                  		<a id="linkAssociazioneSpesa_<s:property value='%{#incr.index}' />_<s:property value='%{uid}' />" href="#associaSpesa" data-toggle="modal" class="btn btn-primary linkAssociazioneSpesaListaCollegataImpegno">Associazioni</a>
						</s:if>
						<s:else>
							<a id="linkAssociazioneSpesa_<s:property value='%{#incr.index}' />_<s:property value='%{uid}' />" href="#" data-toggle="modal" class="btn btn-primary" disabled="true">Associazioni</a>
						</s:else>
	                </s:if>
		            <!-- CONTABILIA-260 - Bug fix per test 24.2, 24.3 - SIAC-7349 Fine CM 07/07/2020 -->

                    <s:hidden name="listaReimputazioneTriennio[%{#incr.index}].index" value="%{#incr.index}"/>
                    <s:hidden id= "motivo%{#incr.index}" name="listaReimputazioneTriennio[%{#incr.index}].idMotivo" value="%{reimputazione.id}"/>
                </div>
                <s:if test="%{#incr.index==0}">
                    <div class="controls" style="margin-top:10px; margin-bottom:10px;">
                        <input type="checkbox" id="spalmaDescrizione" style="margin-left:0px;" name="spalmaDescrizione" value="si"> Inserisci la stessa motivazione per tutte le annualità
                    </div>
                </s:if>
                <s:if test="%{#incr.index==2}">
                    <div class="controls">
                        <!--task-131 <s:submit onclick="overlayForWait()" cssClass="btn btn-primary" style="margin-bottom: 10px;" value="Aggiungi annualità successiva" method="aggiungiAnnoReimputazione"/>-->
                        <s:submit onclick="overlayForWait()" cssClass="btn btn-primary" style="margin-bottom: 10px;" value="Aggiungi annualità successiva" action="aggiornaModificaMovimentoRor_aggiungiAnnoReimputazione"/> 
                    </div>
                </s:if>
            </s:iterator>   
        </div> 
        
        
        <s:include value="/jsp/movgest/include/cruscottoRorCancellazione.jsp" /> 
        <s:include value="/jsp/movgest/include/cruscottoRorDaMantenere.jsp" />    
          
        <s:if test='%{model.step1Model.numeroSubImpegno != null && !model.step1Model.numeroSubImpegno.equals("") }'>
            <s:if test="isAbilitaPropagaDaSub()">
                <div class="control-group">
                    <label class="control-label" for="motivo" style="margin-right: 10px;"> Propaga modifiche ad impegno padre  :  </label>
                   <s:checkbox id="propagaSelected" name="propagaSelected" />
                </div>
            </s:if>             
        </s:if>
        <!-- task-131 <s:submit onclick="overlayForWait()" cssClass="btn btn-primary pull-right" value="salva" method="salva"/> -->
        <s:submit onclick="overlayForWait()" cssClass="btn btn-primary pull-right" value="salva" action="aggiornaModificaMovimentoRor_salva"/>
        <s:include value="/jsp/include/indietro.jsp" />       
        <!-- task-131 <s:submit name="annulla" value="annulla" method="annulla" onclick="overlayForWait()" cssClass="btn btn-secondary" /> -->
        <s:submit name="annulla" value="annulla" action="aggiornaModificaMovimentoRor_annulla" onclick="overlayForWait()" cssClass="btn btn-secondary" />
        <s:hidden name="tipoImpegno" value="%{tipoImpegno}"></s:hidden>  
        <s:hidden name="subSelected" value="%{subSelected}"></s:hidden>
        <s:hidden name="numeroSubImpegno" value="%{numeroSubImpegno}"></s:hidden>          
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
        
        <!-- SIAC-7349 Inizio SR190 CM 20/05/2020 -->
        <s:hidden id="inImpegnoValore" value="%{inImpegno}"/> 
        <!-- SIAC-7349 Fine  SR190 CM 20/05/2020 -->
       
        <div class="control-group">

            <s:iterator value="listaModificheRor"  status="incr" >

                <s:if test='%{listaModificheRor[#incr.index].tipoModificaMovimentoGestione.equals("REIMP")}'>
                    <div class="controls" style="margin-bottom: 10px;">
                        <s:textfield type="text" maxlength="4"  name="listaModificheRor[%{#incr.index}].annoReimputazione" value="%{annoReimputazione}"  id="annoReimp%{#incr.index}" class="span1 soloNumeri" readonly="%{listaModificheRor[#incr.index].uid!=0}"/>&nbsp;
                      <%-- SIAC-7773  FL 18/09/2020 importi allargati --%>
                        <s:textfield type="text"  maxlength="30" name="listaModificheRor[%{#incr.index}].importoOld" value="%{importoOld}"  id="inputReimp%{#incr.index}" onblur="checkNullToEmptyString(event)" class="span2 soloNumeri decimale"/>&nbsp;
                        <!--PER ABILITTARE LABEL ANNO-->

                        
                        <s:if test="%{listaModificheRor[#incr.index].uid==0}">
                            <s:select list="motiviRorReimputazione" id="listaSintesiMotiviReimputazione%{#incr.index}" headerKey="" 
                                headerValue=""  listKey="key" listValue="sintesi"  cssClass="span3" name="listaReimputazioneTriennio[%{#incr.index}].valoreSintesi" />
                        </s:if>
                        <s:else>
                            <s:select list="motiviRorReimputazione" id="listaSintesiMotiviReimputazione%{#incr.index}" headerKey="" 
                                headerValue=""  listKey="key" listValue="sintesi"  cssClass="span4" />

                        </s:else>
                        
                        <s:textarea type="text"  name="listaModificheRor[%{#incr.index}].descrizioneModificaMovimentoGestione" value="%{descrizioneModificaMovimentoGestione}"  id="descrizioneMotivoReimputazione%{#incr.index}" class="span5" style="resize: none; height: 30px;"/>
                        <s:if test="%{listaModificheRor[#incr.index].uid!=0}">
                            <a class="tooltip-test" href="#" data-original-title="Per eliminare la modifica, cancella descrizione e importo">
                                <i class="icon-info-sign">&nbsp;
                                    <span class="nascosto">Per eliminare la modifica, cancella descrizione e importo</span>
                                </i>
                            </a>
                        </s:if>
                        
                        <!-- SIAC-7349 Inizio SR190 CM 20/05/2020 -->
                        <!-- CONTABILIA-260 - Bug fix per test 24.14 - SIAC-7349 Inizio CM 07/07/2020 -->
    				 	<s:if test='%{model.step1Model.numeroSubImpegno == null || model.step1Model.numeroSubImpegno.equals("")}'>
			                <s:if test='%{listaModificheRor[#incr.index].uid!=0}'>
		                  		<a id="linkAssociazioneSpesa_<s:property value='%{#incr.index}' />_<s:property value='%{uid}' />" href="#associaSpesa" data-toggle="modal" class="btn btn-primary linkAssociazioneSpesaListaCollegataImpegno">Associazioni</a>
							</s:if>
							<s:else>
								<a id="linkAssociazioneSpesa_<s:property value='%{#incr.index}' />_<s:property value='%{uid}' />" href="#" data-toggle="modal" class="btn btn-primary" disabled="true">Associazioni</a>
							</s:else>
	               		</s:if>
	               		<!-- CONTABILIA-260 - Bug fix per test 24.14 - SIAC-7349 Fine CM 07/07/2020 -->
                        <!-- SIAC-7349 Fine  SR190 CM 20/05/2020-->
                        
                        <s:hidden name="listaModificheRor[%{#incr.index}].index" value="%{#incr.index}"/>
                        <s:hidden name="listaModificheRor[%{#incr.index}].idMotivo" value="%{reimputazione.id}"/>
                        <s:hidden name="listaModificheRor[%{#incr.index}].uid" value="%{uid}"/>
                        <s:hidden name="listaModificheRor[%{#incr.index}].tipoModificaMovimentoGestione" value="%{tipoModificaMovimentoGestione}"/>
                    </div>
                    
                </s:if>    
            </s:iterator>
            
            <div class="controls">
                <!--task-131 <s:submit onclick="overlayForWait()" cssClass="btn btn-primary" style="margin-bottom: 10px;" value="Aggiungi annualità successiva" method="aggiungiAnnoReimputazione"/> -->
                <s:submit onclick="overlayForWait()" cssClass="btn btn-primary" style="margin-bottom: 10px;" value="Aggiungi annualita successiva" action="aggiornaModificaMovimentoRor_aggiungiAnnoReimputazione"/>
            </div>
                               
        </div> 
        
        <s:include value="/jsp/movgest/include/cruscottoRorCancellazione.jsp" /> 
        <s:include value="/jsp/movgest/include/cruscottoRorDaMantenere.jsp" />
        <s:if test='%{model.step1Model.numeroSubImpegno != null && !model.step1Model.numeroSubImpegno.equals("") }'>
            <s:if test="isAbilitaPropagaDaSub()">
                <div class="control-group">
                    <label class="control-label" for="motivo" style="margin-right: 10px;"> Propaga modifiche ad impegno padre  :  </label> 
                   <s:checkbox id="propagaSelected" name="propagaSelected" />
                   <a class="tooltip-test" href="#" data-original-title="Verranno propagate all'impegno padre solo le nuove modifiche">
                    <i class="icon-info-sign">&nbsp;
                        <span class="nascosto">Verranno propagate solo le nuove modiche</span>
                    </i>
                    </a>
                </div>
            </s:if>             
        </s:if> 


        <!--task-131 <s:submit cssClass="btn btn-primary pull-right" value="salva" method="aggiornaModifiche" onclick="overlayForWait()"/> -->
        <s:submit cssClass="btn btn-primary pull-right" value="salva" action="aggiornaModificaMovimentoRor_aggiornaModifiche" onclick="overlayForWait()"/>
        <s:include value="/jsp/include/indietro.jsp" />
        <!-- task-131 <s:submit name="annulla" value="annulla" onclick="overlayForWait()" method="annulla" cssClass="btn btn-secondary" />-->
        <s:submit name="annulla" value="annulla" onclick="overlayForWait()" action="aggiornaModificaMovimentoRor_annulla" cssClass="btn btn-secondary" />
        <s:hidden name="tipoImpegno" value="%{tipoImpegno}"></s:hidden>
        <s:hidden name="subSelected" value="%{subSelected}"></s:hidden>
        <s:hidden name="numeroSubImpegno" value="%{numeroSubImpegno}"></s:hidden>  
    </div>    

</s:else>


   