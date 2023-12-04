<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
	       
 <div id="refreshPulsantiSalva" style="display: inline">
    
        <p>
<%--               <s:submit name="indietro" value="indietro" method="indietro" cssClass="btn btn-secondary" />   --%>
            <a class="btn btn-secondary" href="">annulla</a>
    
             	
   	<span class="pull-right buttonPluriennale">
     	<!--task-131 <s:submit method="inseriscisalvaProsegui" value="Salva e Prosegui" cssClass="btn btn-primary confermaPluri freezePagina"  /> &nbsp; -->
     	<!--task-131 <s:submit method="salvaPopup" value="Salva per riaccertamento" cssClass="btn btn-primary confermaPluri freezePagina"  />-->
    	<s:submit action="inserisciMovSpesaImporto_inseriscisalvaProsegui" value="Salva e Prosegui" cssClass="btn btn-primary confermaPluri freezePagina"  /> &nbsp;
     	<s:submit action="inserisciMovSpesaImporto_salvaPopup" value="Salva per riaccertamento" cssClass="btn btn-primary confermaPluri freezePagina"  />
    	
    
    </span> 
    <span class="pull-right singleButton">
    		
			<s:if test="salvaDaSDFANormale()">
				<div class="btn btn-primary pull-right">
					<a id="linkSalvaConConfermaDaSdfANormale" href="#msgConfermaSDFDiNuovoDisp" data-toggle="modal" class="linkConfermaSDFANormale">
					salva</a> 
				</div>
			</s:if>
			<s:else>
				<!-- task-131 <s:submit method="salva" value="Salva" cssClass="btn btn-primary freezePagina" ></s:submit> -->
				<s:submit action="inserisciMovSpesaImporto_salva" value="Salva" cssClass="btn btn-primary freezePagina" ></s:submit>
				
			</s:else>
    		
      </span>
 
 	<a id="linkMsgControlloProsegui" href="#msgControlloSiProsegui" data-toggle="modal" style="display:none;"></a>
     </p>
 
 </div>
