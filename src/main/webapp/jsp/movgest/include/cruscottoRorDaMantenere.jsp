<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<h5>
    Mantenimento a Residuo
</h5>
<ul class="htmlelt">                                 
    <li>
      <dfn>Residuo eventualmente da mantenere:</dfn>
      <dl><s:property value="residuoEventualeDaMantenere" /></dl>
      <!--da fixare con importo corretto-->
    </li>
</ul>

<s:if test="isInInserimento()">

    <div class="control-group">
    
        <div class="control-group">
            <label class="control-label" for="motivo">Modifica motivo :  </label>
            <label class="control-label" for="motivo"><b><s:property value="%{descrizioneMotivoRorMantenere}"/></b></label>
            <s:checkbox name="checkDaMantenere" disabled="disabledDaManterenere" fieldValue="true" style="margin-left: 10px;" id="daMantenereValue"/>
        </div>
    
        <div class="control-group">
            <div class="controls" style="margin-bottom: 10px;">
                
                <s:select list="motiviRorMantenimento" id="listaSintesiMotiviDaMantenere" headerKey="" 
                        headerValue=""  listKey="key" listValue="sintesi"  cssClass="span3" name="rorDaMantenere.valoreSintesi" disabled="disabledDaManterenere"/>
                <s:textarea type="text"  name="rorDaMantenere.descrizione" disabled="disabledDaManterenere"  id="descrizioneMotivoRorDaMantenere" class="span5" style="resize: none; height: 30px;"/>
                <s:hidden id="motivoMantenere" name="rorDaMantenere.idMotivo" value="%{mantenere.id}"/>
            </div> 
        </div> 

   
    </div>

       
</s:if>
<!--Aggiornamento -->
<s:else>
    <div class="control-group">
 


        
        <s:if test="getDaMantenerePresente()">
            <div class="control-group">
                <label class="control-label" for="motivo">Modifica motivo :  </label>
                <label class="control-label" for="motivo"><b><s:property value="%{descrizioneMotivoRorMantenere}"/></b></label>
                <!-- parlarne con filippo -->
                <s:checkbox id="checkRor" name="checkDaMantenere" fieldValue="true" value="daMantenerePresente" style="margin-left: 10px;"/>
            </div>

            <s:iterator value="listaModificheRor"  status="incr" >
                <s:if test='%{listaModificheRor[#incr.index].tipoModificaMovimentoGestione.equals("RORM")}'>
                    <div class="control-group">
                        <div class="controls" style="margin-bottom: 10px;">
                        
                            <s:select list="motiviRorMantenimento" id="listaSintesiMotiviDaMantenere" headerKey="" 
                        headerValue=""  listKey="key" listValue="sintesi"  cssClass="span3" disabled="disabledDaManterenere"/>
                            <s:textarea type="text"  name="listaModificheRor[%{#incr.index}].descrizioneModificaMovimentoGestione" value="%{descrizioneModificaMovimentoGestione}"  id="descrizioneMotivoRorDaMantenere" class="span5" style="resize: none; height: 30px;"/>
                            <a class="tooltip-test" href="#" data-original-title="Per eliminare la modifica, cancella descrizione e importo">
                                <i class="icon-info-sign">&nbsp;
                                    <span class="nascosto">Per eliminare la modifica, cancella descrizione o togli check</span>
                                </i>
                            </a>
                            <s:hidden name="rorDaMantenere.idMotivo" value="%{mantenere.id}"/>
                            <s:hidden name="listaModificheRor[%{#incr.index}].uid" value="%{uid}"/>
                            <s:hidden name="listaModificheRor[%{#incr.index}].importoOld" value="%{importoOld}"/>
                            <s:hidden name="listaModificheRor[%{#incr.index}].tipoModificaMovimentoGestione" value="%{tipoModificaMovimentoGestione}"/>
                        </div> 
                    </div> 
                </s:if>
            </s:iterator>
        </s:if>
        <s:else>
            <div class="control-group">
                <div class="control-group">
                    <label class="control-label" for="motivo">Modifica motivo :  </label>
                    <label class="control-label" for="motivo"><b><s:property value="%{descrizioneMotivoRorMantenere}"/></b></label>
                    <s:checkbox name="checkDaMantenere" fieldValue="true" style="margin-left: 10px;" id="daMantenereValue" disabled="disabledDaManterenere"/>
                </div>
                <div class="controls" style="margin-bottom: 10px;">
                    <s:select list="motiviRorMantenimento" id="listaSintesiMotiviDaMantenere" headerKey="" 
                        headerValue=""  listKey="key" listValue="sintesi"  cssClass="span3" name="rorDaMantenere.valoreSintesi" disabled="disabledDaManterenere"/>
                    <s:textarea type="text"  name="rorDaMantenere.descrizione" value="%{descrizione}" disabled="disabledDaManterenere"  id="descrizioneMotivoRorDaMantenere" class="span5" style="resize: none; height: 30px;"/>
                    <s:hidden name="rorDaMantenere.idMotivo" value="%{rorDaMantenere.idMotivo}"/>
                </div> 
            </div>
        </s:else>
   
    </div>
</s:else>
<%-- SIAC-8268 --%>
<s:hidden name="disabledDaManterenere" value="%{disabledDaManterenere}"/>
<%-- SIAC-8268 --%>
   