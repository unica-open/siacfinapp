<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>



<div id="refreshtable">	

<h4 class="step-pane">Elenco documenti trovati</h4>   

		<display:table name="listaRicercaSubDocumentoSpesa" class="table table-hover tab_left" summary="lista subDoc" uid="subdocumentotab" >
		       <display:column>
		      		<s:radio value="radioSubDocumentoSelezionato" cssClass="linkRadioSubDocumentoSelezionato" id="checkSubDocumento" list="%{#attr.subdocumentotab.uid}" name="radioSubDocumentoSelezionato" theme="displaytag"></s:radio>
		       </display:column>
	
			<display:column title="Documento">
				<s:property value="%{#attr.subdocumentotab.documento.anno}"/>/<s:property value="%{#attr.subdocumentotab.documento.tipoDocumento.codice}"/>
				/<s:property value="%{#attr.subdocumentotab.documento.numero}"/>
			</display:column>	 
			
			<display:column title="Data" property="documento.dataEmissione" format="{0,date,dd/MM/yyyy}"/>	 
			
			<display:column title="Stato">
				<a href="#" data-trigger="hover" rel="popover" title="Stato" data-content="<s:property value="%{#attr.subdocumentotab.documento.statoOperativoDocumento.descrizione}"/>">
					<s:property value="%{#attr.subdocumentotab.documento.statoOperativoDocumento.codice}"/>
				</a>
			</display:column>
			
			
			<display:column title="Soggetto"><s:property value="%{#attr.subdocumentotab.documento.soggetto.codiceSoggetto}"/>-<s:property value="%{#attr.subdocumentotab.documento.soggetto.denominazione}"/></display:column>
			
			<display:column title="Quota" property="numero"></display:column>
			
			<display:column title="Impegno" >
				 <s:if test="%{#attr.subdocumentotab.subImpegno == null}">
	   						<s:property value="%{#attr.subdocumentotab.impegno.annoMovimento}"/>/<s:property value="%{#attr.subdocumentotab.impegno.numero.intValue()}"/>
						 </s:if>
				  <s:else>
				 	<s:property value="%{#attr.subdocumentotab.impegno.annoMovimento}"/>/<s:property value="%{#attr.subdocumentotab.impegno.numero.intValue()}"/>/<s:property value="%{#attr.subdocumentotab.subImpegno.numero.intValue()}"/>
				 </s:else>
			</display:column>
			
			<display:column title="Importo" property="importo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
	
		</display:table>
		
</div>			