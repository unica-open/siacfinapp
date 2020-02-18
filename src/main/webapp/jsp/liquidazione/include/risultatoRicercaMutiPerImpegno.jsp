<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<div class="accordion" id="mutui">
	<div class="accordion-group">
		<div class="accordion-heading">
			<a class="accordion-toggle collapsed" data-toggle="collapse"
				data-parent="#mutui" href="#mutuiTAB"> Mutuo<span class="icon">&nbsp;</span></a>
		</div>
		<div id="mutuiTAB" class="accordion-body collapse">
			<div class="accordion-inner">						
				<display:table name="listaVociMutuo" class="table table-hover tab_left" summary="lista vocimutuo" uid="mutuotab" >
				       <display:column>
				      		<s:radio id="mutuotab" list="%{#attr.mutuotab.uid}" name="radioVoceMutuoSelezionata" theme="displaytag"></s:radio>
				       </display:column>
				 	<display:column title="Numero Mutuo" property="numeroMutuo" />
				 	<display:column title="Descrizione" property="descrizioneMutuo"/>
				 	<display:column title="Istitutuo Mutuante" property="istitutoMutuante.denominazione" />
				 	<display:column class="tab_Right" title="Importo Voce" property="importoAttualeVoceMutuo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
				 	<display:column class="tab_Right" title="Disponibilit&agrave; a liquidare" property="importoDisponibileLiquidareVoceMutuo" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
				</display:table>						 
			</div>
		</div>
	</div>
</div>