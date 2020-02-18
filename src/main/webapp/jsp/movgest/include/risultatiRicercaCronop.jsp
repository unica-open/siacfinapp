<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:include value="/jsp/include/actionMessagesErrors.jsp" />




<s:iterator value="listaRicercaCronop" var="cronop" status="st">

<div class="lista-cronop" id="parent-collapse-<s:property value="#st.index"/>">
<div class="accordion" id="accordion-<s:property value="#st.index"/>">
		<div class="accordion-group">
			<div class="accordion-heading">
				<div class="accordion-toggle collapsed"  data-parent="parent-collapse-<s:property value="#st.index"/>" 
					data-href="#collapse-<s:property value="#st.index"/>" id="href-collapse-<s:property value="#st.index"/>" >
		Progetto <s:property value="#cronop.progetto.codice"/> - <s:property value="#cronop.progetto.descrizione"/>  
		/ Ambito <s:property value="#cronop.progetto.tipoAmbito.codice"/> 
		/ Valore complessivo <s:property value="#cronop.progetto.valoreComplessivo"/> 
		/ Cronoprogramma	<s:property value="#cronop.codice"/> - <s:property value="#cronop.descrizione"/> 
   <span class="icon">&nbsp;</span></div>
				
			</div>
			<!-- totaleImportoVincoli, totaleImportoDaCollegare;	 -->
			<div id="collapse-<s:property value="#st.index"/>" class="accordion-body collapse hide" >
				<div class="accordion-inner">

					<table id="ricercaCronopID" summary="riepilogo progetti cronoprogramma" class="table table-striped table-bordered table-hover">
					<thead>
					<tr>
						<th></th>
						<th>Attivit&agrave; prevista</th>
						<th>Anno spesa</th>
						<th>Anno entrata</th>
						<th>Classificazione di bilancio</th>
						<th>Capitolo/Articolo</th>
						<th>Valore previsto</th>
					</tr></thead>
					<tbody>
				<s:iterator value="#cronop.capitoliUscita" var="spese" status="st-spese">
					<tr>
						<td>
							<input class="speseProgetti" type="radio" name="radioCodiceProgetto" id="radioCodiceProgetto"
							data-progetto="<s:property value="#cronop.progetto.codice"/>"
							data-cronoprogramma="<s:property value="#cronop.codice"/>"
							data-id-cronoprogramma="<s:property value="#cronop.uid"/>"
							data-id-spesa-cronoprogramma="<s:property value="#spese.uid"/>"
							data-importo="<s:property value="#spese.stanziamento"/>"
							data-cup="<s:property value="#cronop.progetto.cup"/>"
							data-attivita-prevista="<s:property value="#spese.descrizioneCapitolo"/>"
							data-capitolo="<s:property value="#spese.numeroCapitolo"/>"
							data-articolo="<s:property value="#spese.numeroArticolo"/>"
							data-annocompetenza="<s:property value="#spese.annoCompetenza"/>"
							>
						</td>
						<td><s:property value="#spese.descrizioneCapitolo"/></td>
						<td><s:property value="#spese.annoCompetenza"/></td>
						<td><s:property value="#spese.annoEntrata"/></td>
						<td><s:property value="#spese.missione.codice"/> - <s:property value="#spese.programma.codice"/> - <s:property value="#spese.titoloSpesa.codice"/> </td>
						<td><s:if test="#spese.numeroCapitolo != null"><s:property value="#spese.numeroCapitolo"/>/<s:property value="#spese.numeroArticolo"/></s:if></td>
						<td><s:property value="#spese.stanziamento"/></td>
					</tr>
				</s:iterator>	
					
				</tbody></table>
            
           		</div>
			</div>
		
		</div>
	</div>
</div>

</s:iterator>	



