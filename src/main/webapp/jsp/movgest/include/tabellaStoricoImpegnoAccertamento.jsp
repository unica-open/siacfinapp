<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

          <display:table name="sessionScope.RISULTATI_RICERCA_STORICO_IMPEGNI_ACCERTAMENTI" 
		class="table tab_left table-hover" 
		summary="riepilogo storico" 
		pagesize="10" partialList="true" size="resultSize" 
		requestURI="gestisciStoricoImpegnoAccertamentoStep2.do" uid="tabellaStoricoImpegniAccertamento" 
		keepStatus="${status}" clearStatus="${status}">


		<display:column title="Anno impegno">
			<s:property value="%{#attr.tabellaStoricoImpegniAccertamento.impegno.annoMovimento}"/>
		</display:column>
		<%-- <display:column title="Anno impegno" property="annoMovimento" /> --%>
		<display:column title="Numero impegno">
			<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="<s:property value="%{#attr.ricercaImpegnoID.descrizione}"/>">
				<s:property value="%{#attr.tabellaStoricoImpegniAccertamento.impegno.numero.intValue()}"/>
			</a>
		</display:column>
		<display:column title="Numero subimpegno">
			<s:if test="%{#attr.tabellaStoricoImpegniAccertamento.subImpegno != null}">
				<s:property value="%{#attr.tabellaStoricoImpegniAccertamento.subImpegno.numero.intValue()}"/>
			</s:if>
		</display:column>
		
		<display:column title="Anno Accertamento">
			<s:property value="%{#attr.tabellaStoricoImpegniAccertamento.accertamento.annoMovimento}"/>
		</display:column>
		
		<display:column title="Numero accertamento">
			<s:property value="%{#attr.tabellaStoricoImpegniAccertamento.accertamento.numero.intValue()}"/>
		</display:column>
		<display:column title="Numero subaccertamento">
			<s:property value="%{#attr.tabellaStoricoImpegniAccertamento.subAccertamento.numero.intValue()}"/>
		</display:column>

		<s:if test="abilitaModificheStorico">
			<display:column title="" class="tab_Right">
			
				<div class="btn-group">
		    		<button class="btn dropdown-toggle" data-toggle="dropdown">Azioni<span class="caret"></span></button>
		    		<ul class="dropdown-menu pull-right" id="ul_action">
		    			<li><a href="#msgAnnulla" class="aggiornaStorico" 
									data-anno="<s:property value="%{#attr.tabellaStoricoImpegniAccertamento.accertamento.annoMovimento}"/>"
									data-numero="<s:property value="%{#attr.tabellaStoricoImpegniAccertamento.accertamento.numero.intValue()}"/>"
									data-numero-sub="<s:property value="%{#attr.tabellaStoricoImpegniAccertamento.subAccertamento.numero.intValue()}"/>"
									data-uid="<s:property value="%{#attr.tabellaStoricoImpegniAccertamento.uid}"/>"
									>aggiorna</a>
						</li>
						<li>
							<a id="linkElimina_<s:property value="%{#attr.tabellaStoricoImpegniAccertamento.uid}"/>_<s:property value="%{#attr.tabellaStoricoImpegniAccertamento.accertamento.numero.intValue()}"/>_<s:property value="%{#attr.tabellaStoricoImpegniAccertamento.accertamento.numero.intValue()}"/>" href="#msgElimina" data-toggle="modal" class="linkElimina">elimina</a>
						</li>
		    		</ul>
				</div>
		</display:column>
	</s:if>
</display:table>