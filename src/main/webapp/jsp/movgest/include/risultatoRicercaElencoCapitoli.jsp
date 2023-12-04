<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:include value="/jsp/include/actionMessagesErrors.jsp" />
<s:if test="capitoloTrovato">
    <h4>Elenco Capitoli</h4> 
    
      
     <s:if test="!visualizzaListaCapitoliCompatta()">
      
<!-- 	    <div style="width: 100%; height: 300px; overflow-y: scroll;"> -->
		    <display:table name="listaRicercaCapitolo"  
		                 class="table table-striped table-bordered table-hover" 
		                 summary="riepilogo capitoli"
						 uid="ricercaCapitoloID">
		       <display:column>
		            <!-- se impegnabile aperto, altrimenti il radio si disabilita -->
		            <s:if test="%{#attr.ricercaCapitoloID.flagImpegnabile == true}">
		            	<s:radio list="%{#attr.ricercaCapitoloID.uid}" name="radioCodiceCapitolo" theme="displaytag" cssClass="radioCodiceCapitolo"></s:radio>
		            </s:if>
		       		<s:else>
		       			<s:if test="oggettoDaPopolareImpegno()"> 
		       				<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="Capitolo non impegnabile">X</a>
		       			</s:if>
		       			<s:else>
		       				<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="Capitolo non accertabile">X</a>
		       			</s:else>
		       		</s:else>
		        </display:column>
		        <display:column title="Capitolo">
		    		<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="${attr.ricercaCapitoloID.descrizione}">
		    			${attr.ricercaCapitoloID.numCapitolo}/${attr.ricercaCapitoloID.articolo}/${attr.ricercaCapitoloID.ueb}
		    		</a>
		        </display:column>
		        <display:column title="Classificazione" property="classificazione"/>
		        <display:column title="Disponibile ${attr.ricercaCapitoloID.importiCapitolo[0].annoCompetenza}" property="disponibileAnno1" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
		        <display:column title="Disponibile ${attr.ricercaCapitoloID.importiCapitolo[1].annoCompetenza}" property="disponibileAnno2" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
		        <display:column title="Disponibile ${attr.ricercaCapitoloID.importiCapitolo[2].annoCompetenza}" property="disponibileAnno3" decorator="it.csi.siac.siacfinapp.frontend.ui.util.displaytag.ConverterEuro"/>
		        <display:column title="Struttura Amm. Resp.">
		       		<a href="#" data-trigger="hover" data-placement="left" rel="popover" title="Struttura Amm. Resp. Desc." data-content="${attr.ricercaCapitoloID.descrizioneStrutturaAmministrativa}">
		    			${attr.ricercaCapitoloID.codiceStrutturaAmministrativa}
		    		</a>
		    	</display:column>
		        <display:column title="P.d.C. Finanziario">
		        	<a href="#" data-trigger="hover" rel="popover" data-placement="left" title="Descrizione P.d.C. Finanziario" data-content="${attr.ricercaCapitoloID.descrizionePdcFinanziario}">
		    			${attr.ricercaCapitoloID.codicePdcFinanziario}
		    		</a>
		    	</display:column>
			</display:table> 
		
<!-- 		</div>  -->
	
	
	</s:if>
	<s:else>
	<!-- TABELLA COMPATTA CON POCHE COLONNE MA TANTE RIGHE -->
			<div style="width: 100%; height: 300px; overflow-y: scroll;">
				<display:table name="listaRicercaCapitolo"  
		                 class="table table-striped table-bordered table-hover" 
		                 summary="riepilogo capitoli"
						 uid="ricercaCapitoloID">
				       <display:column>
				            <!-- se impegnabile aperto, altrimenti il radio si disabilita -->
				            <s:if test="%{#attr.ricercaCapitoloID.flagImpegnabile == true}">
				            	<s:radio list="%{#attr.ricercaCapitoloID.uid}" name="radioCodiceCapitolo" theme="displaytag" cssClass="radioCodiceCapitolo"></s:radio>
				            </s:if>
				       		<s:else>
				       			<s:if test="oggettoDaPopolareImpegno()"> 
				       				<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="Capitolo non impegnabile">X</a>
				       			</s:if>
				       			<s:else>
				       				<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="Capitolo non accertabile">X</a>
				       			</s:else>
				       		</s:else>
				        </display:column>
				        <display:column title="Capitolo">
				    		<a href="#" data-trigger="hover" rel="popover" title="Descrizione" data-content="${attr.ricercaCapitoloID.descrizione}">
				    			${attr.ricercaCapitoloID.numCapitolo}/${attr.ricercaCapitoloID.articolo}/${attr.ricercaCapitoloID.ueb}
				    		</a>
				        </display:column>
				        <display:column title="Struttura Amm. Resp.">
				       		<a href="#" data-trigger="hover" data-placement="left" rel="popover" title="Struttura Amm. Resp. Desc." data-content="${attr.ricercaCapitoloID.descrizioneStrutturaAmministrativa}">
				    			${attr.ricercaCapitoloID.codiceStrutturaAmministrativa}
				    		</a>
				    	</display:column>
				        <display:column title="P.d.C. Finanziario">
				        	<a href="#" data-trigger="hover" rel="popover" data-placement="left" title="Descrizione P.d.C. Finanziario" data-content="${attr.ricercaCapitoloID.descrizionePdcFinanziario}">
				    			${attr.ricercaCapitoloID.codicePdcFinanziario}
				    		</a>
				    	</display:column>
				</display:table> 
			</div>
	</s:else>
	
	
	       
    <s:if test="listaRicercaCapitolo != null && listaRicercaCapitolo.size() > 0">
    </s:if>
    
    <s:if test="!visualizzaListaCapitoliCompatta()">
    	<a id="visualizzaDettaglioCapitolo" class="btn" style="display:none">visualizza dettaglio</a>    
    	<a id="visualizzaDettaglioCapitoloHidden" data-toggle="collapse" data-target="#visDett" style="display:none"></a>    
    </s:if>
    
</s:if>
<script type="text/javascript">

	$(document).ready(function() {
		$("#visualizzaDettaglioCapitolo").click(function() {
			$.ajax({
				//task-131 url: '<s:url method="visualizzaDettaglioCapitolo"/>',
				url: '<s:url action="%{#visualizzaDettaglioCapitoloAction}"/>',
				type: 'POST',
				data: $(".radioCodiceCapitolo").serialize(),
				success: function(data) {
					$("#gestioneVisualizzaCapitolo").html(data);
					$("#visualizzaDettaglioCapitoloHidden").click();
				}
			});
		});	
		$(".radioCodiceCapitolo").change(function() {
			$("#visualizzaDettaglioCapitolo").css('display', 'inline-block');
		});
		$("a[rel=popover]").popover();
	});
	
</script> 