<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


<display:table name="listaSubDocumentoSpesa" class="table table-hover tab_left" summary="lista subDoc" uid="subdocumentotablista" >
	   <display:column title="Documento"><s:property value="%{#attr.subdocumentotablista.documento.anno}"/>/<s:property value="%{#attr.documentotab.subdocumentotablista.documento.numero}"/>/<s:property value="%{#attr.subdocumentotablista.documento.numero}"/> </display:column>
	   <display:column title="Data" property="dataCreazione" format="{0,date,dd/MM/yyyy}"/>
	   <display:column title="Stato" ><s:property value="%{#attr.documentotab.documento.statoOperativoDocumento.codice}"/></display:column>
	   <display:column title="Quota" property="numero" />
	   <display:column title="Movimento" >
	   <s:if test="%{#attr.subdocumentotablista.subImpegno == null}">
	 	     <s:property value="%{#attr.subdocumentotablista.impegno.annoMovimento}"/>/<s:property value="%{#attr.subdocumentotablista.impegno.numero.intValue()}"/>
	   </s:if>
	   <s:else>
	 		<s:property value="%{#attr.subdocumentotablista.impegno.annoMovimento}"/>/<s:property value="%{#attr.subdocumentotablista.impegno.numero.intValue()}"/>/<s:property value="%{#attr.subdocumentotablista.subImpegno.numero.intValue()}"/>
	   </s:else>
	   </display:column>
	 
	   <display:column class="tab_Right" title="Importo" property="importo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
	   <display:column> 
	   
	   <s:if test="!disableCancellaDocumento(#attr.subdocumentotablista.uid)">
	   		<div class="btn btn-secondary"><a id="linkElimina_<s:property value="%{#attr.subdocumentotablista.uid}"/>" class="linkEliminaDocumentoDaLista" >elimina</a></div> 
	   </s:if>
	   <s:else>
	 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	   </s:else>
	   </display:column>
	   <display:footer>
			<tr> 
				<th>Totale</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th class="tab_Right"><s:property value="getText('struts.money.format', {sommaDocumentiCollegati})" /></th>
				<th>&nbsp;</th>
			</tr>  
	  </display:footer>	
   </display:table>
   
   <script type="text/javascript">
	
   $(document).ready(function() {		
   
	   $(".linkEliminaDocumentoDaLista").click(function() {
				var supportId = $(this).attr("id").split("_");
				if (supportId != null && supportId.length > 0) {
	// 				$("#idSoggettoDaAggiornare").val(supportId[1]);
					$.ajax({
						url: '<s:url method="eliminaDocDaTabella"></s:url>',
						type: "GET",
						data: {idDocDaEliminare: supportId[1] },
					    success: function(data)  {
					    	$("#refreshDopoElimina").html(data);
					    	
						}
					}); 
	
				}
			});
		
	});
	</script>