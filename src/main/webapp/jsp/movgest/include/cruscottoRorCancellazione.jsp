<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<h5>
    Cancellazione
</h5>
<ul class="htmlelt">                                 
    <li>
      <dfn>Importo massimo cancellabile:</dfn>
      <dl><s:property value="model.importoMassimoCancellabile" /></dl>
      <!--da fixare con importo corretto-->
    </li>
</ul>
<s:if test="isInInserimento()">
    <div class="control-group">
    
        <div class="control-group">
            <label class="control-label" for="motivo">Modifica motivo :  </label>
            <label class="control-label" for="motivo"><b><s:property value="%{descrizioneMotivoRorCancellazioneInesigibilita}"/></b></label>
        </div>
    
        <div class="control-group">
            <div class="controls" style="margin-bottom: 10px;">
                <s:textfield type="text" maxlength="30" onblur="checkNullToEmptyString(event)" name="rorCancellazioneInesigibilita.importo"  id="inputCancellazioneInesegibilita" placeholder="0.00" class="span2 soloNumeri decimale" />&nbsp;
                <s:select list="motiviRorCancellazione" id="listaSintesiMotiviCancellazioneInesegibilita" headerKey="" 
                        headerValue=""  listKey="key" listValue="sintesi"  cssClass="span4" name="rorCancellazioneInesigibilita.valoreSintesi" />
                <s:textarea type="text"  name="rorCancellazioneInesigibilita.descrizione"   id="descrizioneMotivoCancellazioneInesegibilita" class="span5" style="resize: none; height: 30px;"/>
                <s:hidden id= "motivoInes" name="rorCancellazioneInesigibilita.idMotivo" value="%{rorCancellazioneInesigibilita.idMotivo}"/>
            </div> 
        </div> 
    </div>
    <div class="control-group">
    
        <div class="control-group">
            <label class="control-label" for="motivo">Modifica motivo :  </label>
            <label class="control-label" for="motivo"><b><s:property value="%{descrizioneMotivoRorCancellazioneInsussistenza}"/></b></label>
        </div>
    
        <div class="control-group">
            <div class="controls" style="margin-bottom: 10px;">
                <s:textfield type="text" maxlength="30" onblur="checkNullToEmptyString(event)" name="rorCancellazioneInsussistenza.importo"  id="inputCancellazioneInsussistenza" placeholder="0.00" class="span2 soloNumeri decimale" />&nbsp;
                <s:select list="motiviRorCancellazione" id="listaSintesiMotiviCancellazioneInsussistenza" headerKey="" 
                        headerValue=""  listKey="key" listValue="sintesi"  cssClass="span4" name="rorCancellazioneInsussistenza.valoreSintesi"/>
                <s:textarea type="text"  name="rorCancellazioneInsussistenza.descrizione"   id="descrizioneMotivoCancellazioneInsussistenza" class="span5" style="resize: none; height: 30px;"/>
                <s:hidden id= "motivoInsus" name="rorCancellazioneInsussistenza.idMotivo" value="%{rorCancellazioneInsussistenza.idMotivo}"/>
            </div> 
        </div> 

   
    </div>
       
</s:if>
<!--aggiornamento -->
<s:else>
    <div class="control-group">
        <div class="control-group">
            <label class="control-label" for="motivo">Modifica motivo :  </label>
            <label class="control-label" for="motivo"><b><s:property value="%{descrizioneMotivoRorCancellazioneInesigibilita}"/></b></label>
        </div>
        <s:if test="getInesigibilitaRor()">
            <s:iterator value="listaModificheRor"  status="incr" >
                <s:if test='%{listaModificheRor[#incr.index].tipoModificaMovimentoGestione.equals("INEROR")}'>
                    <div class="control-group">
                        <div class="controls" style="margin-bottom: 10px;">
                           <%-- SIAC-7773  FL 18/09/2020 importi allargati --%>
                            <s:textfield type="text" maxlength="30" name="listaModificheRor[%{#incr.index}].importoOld" value="%{importoOld}" onblur="checkNullToEmptyString(event)"  id="inputCancellazioneInesegibilita" placeholder="0.00" class="span2 soloNumeri decimale" />&nbsp;
                            <s:select list="motiviRorCancellazione" id="listaSintesiMotiviCancellazioneInesegibilita" headerKey="" 
                        headerValue=""  listKey="key" listValue="sintesi"  cssClass="span4" name="rorCancellazioneInesigibilita.valoreSintesi"/>
                            <s:textarea type="text"  name="listaModificheRor[%{#incr.index}].descrizioneModificaMovimentoGestione" value="%{descrizioneModificaMovimentoGestione}"  id="descrizioneMotivoCancellazioneInesegibilita" class="span5" style="resize: none; height: 30px;"/>
                            <a class="tooltip-test" href="#" data-original-title="Per eliminare la modifica, cancella descrizione e importo">
                                <i class="icon-info-sign">&nbsp;
                                    <span class="nascosto">Per eliminare la modifica, cancella descrizione e importo</span>
                                </i>
                            </a>
                            
                            <s:hidden name="listaModificheRor[%{#incr.index}].uid" value="%{uid}"/>
                            <s:hidden name="listaModificheRor[%{#incr.index}].tipoModificaMovimentoGestione" value="%{tipoModificaMovimentoGestione}"/>
                        </div> 
                    </div> 
                </s:if>
            </s:iterator>
        </s:if>
        <s:else>
            <div class="controls" style="margin-bottom: 10px;">
                <s:textfield type="text" maxlength="30" onblur="checkNullToEmptyString(event)"  name="rorCancellazioneInesigibilita.importo"  id="inputCancellazioneInesegibilita" placeholder="0.00" class="span2 soloNumeri decimale" />&nbsp;
                <s:select list="motiviRorCancellazione" id="listaSintesiMotiviCancellazioneInesegibilita" headerKey="" 
                        headerValue=""  listKey="key" listValue="sintesi"  cssClass="span4" name="rorCancellazioneInesigibilita.valoreSintesi"/>
                <s:textarea type="text"  name="rorCancellazioneInesigibilita.descrizione" value="%{descrizione}"  id="descrizioneMotivoCancellazioneInesegibilita" class="span5" style="resize: none; height: 30px;"/>
                <s:hidden name="rorCancellazioneInesigibilita.idMotivo" value="%{rorCancellazioneInesigibilita.idMotivo}"/>
            </div>
        </s:else>
    </div>

    <div class="control-group">
    
        <div class="control-group">
            <label class="control-label" for="motivo">Modifica motivo :  </label>
            <label class="control-label" for="motivo"><b><s:property value="%{descrizioneMotivoRorCancellazioneInsussistenza}"/></b></label>
        </div>
        <s:if test="getInsussistenzaRor()">
            <s:iterator value="listaModificheRor"  status="incr" >
                <s:if test='%{listaModificheRor[#incr.index].tipoModificaMovimentoGestione.equals("INSROR")}'>
                    <div class="control-group">
                        <div class="controls" style="margin-bottom: 10px;">
                            <s:textfield type="text" maxlength="30" name="listaModificheRor[%{#incr.index}].importoOld" value="%{importoOld}" onblur="checkNullToEmptyString(event)" placeholder="0.00" class="span2 soloNumeri decimale" />&nbsp;
                            <s:select list="motiviRorCancellazione" id="listaSintesiMotiviCancellazioneInsussistenza" headerKey="" 
                        headerValue=""  listKey="key" listValue="sintesi"  cssClass="span4" />
                            <s:textarea type="text"  name="listaModificheRor[%{#incr.index}].descrizioneModificaMovimentoGestione" value="%{descrizioneModificaMovimentoGestione}"  id="descrizioneMotivoCancellazioneInsussistenza" class="span5" style="resize: none; height: 30px;"/>
                            <a class="tooltip-test" href="#" data-original-title="Per eliminare la modifica, cancella descrizione e importo">
                                <i class="icon-info-sign">&nbsp;
                                    <span class="nascosto">Per eliminare la modifica, cancella descrizione e importo</span>
                                </i>
                            </a>
                            <s:hidden name="listaModificheRor[%{#incr.index}].uid" value="%{uid}"/>
                            <s:hidden name="listaModificheRor[%{#incr.index}].tipoModificaMovimentoGestione" value="%{tipoModificaMovimentoGestione}"/>
                        </div> 
                    </div> 
                </s:if>
            </s:iterator>
        </s:if>
        <s:else>
            <div class="control-group">
                <div class="controls" style="margin-bottom: 10px;">
                    <s:textfield type="text" maxlength="30" name="rorCancellazioneInsussistenza.importo" onblur="checkNullToEmptyString(event)" id="inputCancellazioneInsussistenza" placeholder="0.00" class="span2 soloNumeri decimale" />&nbsp;
                    <s:select list="motiviRorCancellazione" id="listaSintesiMotiviCancellazioneInsussistenza" headerKey="" 
                            headerValue=""  listKey="key" listValue="sintesi"  cssClass="span4" name="rorCancellazioneInsussistenza.valoreSintesi"/>
                    <s:textarea type="text"  name="rorCancellazioneInsussistenza.descrizione" value="%{descrizione}"  id="descrizioneMotivoCancellazioneInsussistenza" class="span5" style="resize: none; height: 30px;"/>
                    <s:hidden name="rorCancellazioneInsussistenza.idMotivo" value="%{cancellazioneInsussistenza.idMotivo}"/>
                </div> 
            </div>
        </s:else>
   
    </div>
    
</s:else>

   