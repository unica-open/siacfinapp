<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="display" uri="/display-tags"%>
<s:include value="/jsp/include/actionMessagesErrors.jsp" />
<display:table name="sessionScope.RISULTATI_RICERCA_IMPEGNI_SUB_PER_VOCI_MUTUO"
	class="table table-hover tab_left"
	summary="riepilogo impegni finanziabili da mutuo" pagesize="10" partialList="true" size="resultSize"
	requestURI="gestioneVoceDiMutuoStep2.do"
	uid="ricercaImpegniSubimpegniID">
	<display:column>
		<s:radio list="%{#attr.ricercaImpegniSubimpegniID.uid}"
			name="uidImpegnoSubimpegnoSelezionato" theme="displaytag"
			cssClass="paramVoceDiMutuo" disabled="%{#attr.ricercaImpegniSubimpegniID.giaAssociatoAdUnaVoce}" />
	</display:column>
	<display:column title="Anno" property="anno" />
	<display:column title="Numero">
		<a href="#" data-trigger="hover" rel="popover" title="Oggetto"
			data-content="<s:property value="%{#attr.ricercaImpegniSubimpegniID.descrizione}"/>">
				<s:if test="%{#attr.ricercaImpegniSubimpegniID.impegno}">
				${attr.ricercaImpegniSubimpegniID.numero}
				</s:if> 
				<s:else>
				${attr.ricercaImpegniSubimpegniID.numeroImpegnoPadre}/${attr.ricercaImpegniSubimpegniID.numero}
				</s:else>
		</a>		
	</display:column>
	<display:column title="Capitolo">
<%-- 	<s:if test="%{#attr.ricercaImpegniSubimpegniID.impegno}"> --%>
		<s:property
			value="%{#attr.ricercaImpegniSubimpegniID.capitolo.annoCapitolo}" />/<s:property
			value="%{#attr.ricercaImpegniSubimpegniID.capitolo.numeroCapitolo}" />/<s:property
			value="%{#attr.ricercaImpegniSubimpegniID.capitolo.numeroArticolo}" />/<s:property
			value="%{#attr.ricercaImpegniSubimpegniID.capitolo.numeroUEB}" />
<%-- 	</s:if>		 --%>
	</display:column>
	<display:column title="Stato" property="stato" />
	<display:column title="Provvedimento">
		<a href="#" data-trigger="hover" rel="popover" title="Oggetto"
			data-content="<s:property value="%{#attr.ricercaImpegniSubimpegniID.provvedimento.oggetto}"/>"><s:property
				value="%{#attr.ricercaImpegniSubimpegniID.provvedimento.anno}" />/<s:property
				value="%{#attr.ricercaImpegniSubimpegniID.provvedimento.numero}" /></a>
	</display:column>
	<display:column title="Importo" property="importo"
		decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
	<display:column title="Importo da finanziare"
		property="importoDaFinanziare"
		decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro" />
</display:table>
<span class="btnVisible">
	<p>
		<a class="btn"  data-toggle="collapse" data-target="#insVoce" id="selezioneVoceDiMutuo">seleziona</a>
	</p>
</span>
	
<div id="insVoce" class="collapse refreshAggiornaImportoVoceDiMutuo">
	<s:include value="/jsp/mutuo/include/gestioneImportoVoceDiMutuoStep2.jsp" />
</div>
<script type="text/javascript">

	$(document).ready(function() {
		$("#selezioneVoceDiMutuo").unbind().click(function() {
			$.ajax({
				url: '<s:url method="aggiornaImportoVoceDiMutuo"/>',
				type: 'POST',
				data: $(".paramVoceDiMutuo").serialize(),
			    success: function(data) {
			    	 $(".refreshAggiornaImportoVoceDiMutuo").html(data);
			    }
			});
		});
		

		$('.btnVisible').hide();
		var Statusbtn = true;
		var btnCheck = $('input[name=uidImpegnoSubimpegnoSelezionato]');
		var btnIn = $('a[name=btnInvisible]');
		$(btnCheck).click(function() {
			$('.btnVisible').show();
		});

	});
</script>