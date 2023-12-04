<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<h4>Gestione Dati Contabili</h4>
<fieldset class="form-horizontal" id="datiContabili">


    <dl class="dl-horizontal">
        <div class="boxOrSpan2">
            <div class="boxOrInLeft">
                <p>Impegno</p>
                <ul class="htmlelt">
                    <li>
                        <dfn>N.Impegno</dfn>
                        <dl>${model.step1Model.annoImpegno}/${model.step1Model.numeroImpegno}</dl>
                    </li>
                    <s:if test='%{model.step1Model.numeroSubImpegno != null && !model.step1Model.numeroSubImpegno.equals("") }'>
	                     <li>
	                        <dfn>N.Subimpegno</dfn>
	                        <dl>${model.step1Model.numeroSubImpegno}</dl>
	                    </li>
                     </s:if>
                    <li>
                        <dfn>Importo da riaccertare al 31/12</dfn>
                        <dl><s:number name="%{model.importoDaRiaccertare}" minimumFractionDigits="2"/></dl>
                    </li>
                    <li>
                        <dfn>Documenti non liquidati anno successivo</dfn>
                        <dl><s:number name="%{model.documentiNoLiqAnnoSuccessivo}" minimumFractionDigits="2"/></dl>
                    </li>
                    <li>
                        <dfn>Liquidato anno successivo</dfn>
                        <dl><s:number name="%{model.liquidatoAnnoSuccessivo}" minimumFractionDigits="2"/></dl>
                    </li>
                    <li>
                        <dfn>Importo Massimo da riaccertare</dfn>
                        <dl><s:number name="%{model.importoMassimoDaRiaccertare}" minimumFractionDigits="2"/></dl>
                    </li>
                    <li>
                        <dfn>Importo modifiche</dfn>
                        <dl><s:number name="%{model.importoModifiche}" minimumFractionDigits="2"/></dl>
                    </li>
                     
                </ul>
            </div>
        </div>
    </dl>
    



    <s:include value="/jsp/movgest/include/cruscottoReimputazione.jsp" />
    


</fieldset>

