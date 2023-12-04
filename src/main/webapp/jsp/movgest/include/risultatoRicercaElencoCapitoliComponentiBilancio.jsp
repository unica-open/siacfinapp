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
    
    
   
     <s:hidden id="azioneRicerca" name="componenteBilancioCapitoloAttivoRicerca"></s:hidden>
    <s:if test="!visualizzaListaCapitoliCompatta()">
    	 <!-- SELECT COMPONENTE BILANCIO SIAC-7349-->
    	 <div class="control-group" style="display:none" id="selectComponentiBilancio">
					<label class="control-label">Seleziona Componente</label>
					<div class="controls">
					<select id="listaComponenteBilancioRicerca" name="componenteBilancioUid" class="parametroRicercaCapitolo"  >
					</select>
					</div>
		 </div>
    	 <span class="pull-right"><a id="visualizzaDettaglioCapitolo" class="btn" style="display:none">visualizza dettaglio</a> </span>  
    	 <a id="visualizzaDettaglioCapitoloHidden" data-toggle="collapse" data-target="#visDett" style="display:none"></a>    
    </s:if>
    
    <div id="errorComponente" style="clear:both;"></div>
    
    
</s:if>
<script type="text/javascript">

	$(document).ready(function() {
		$("#visualizzaDettaglioCapitolo").click(function() {
			var overlay = $('.modal-body').overlay({usePosition: true});
			overlay.overlay('show');
			$("#gestioneVisualizzaCapitoloComponentiBilancio").hide();
			$.ajax({
				//task-131 url: '<s:url method="visualizzaDettaglioCapitolo"/>',
				url: '<s:url action="%{#visualizzaDettaglioCapitoloAction}"/>',
				type: 'POST',
				data: $(".radioCodiceCapitolo").serialize(),
				success: function(data) {
					$("#gestioneVisualizzaCapitoloComponentiBilancio").show();
					$("#gestioneVisualizzaCapitoloComponentiBilancio").html(data);
					$("#visualizzaDettaglioCapitoloHidden").click();
					$('.modal-body').animate({ scrollTop: $(document).height() }, 2500);
				}
			}).always(overlay.overlay.bind(overlay, 'hide'));
		});	

		$(".rowTableComponentiSelected").remove();
		$(".radioCodiceCapitolo").change(function() {
			
			var row_index = $(this).parent().index();
			var overlay = $('#ricercaCapitoloID').overlay({usePosition: true});
			overlay.overlay('show');
			var oggettoDaInviare = {};
			oggettoDaInviare.radioCodiceCapitolo =$(".radioCodiceCapitolo").val();
			if($("#azioneRicerca").val() == 'true'){
				oggettoDaInviare.azione = 'RICERCA';
				}else{
					oggettoDaInviare.azione = 'INSERISCI';
					}
			//SIAC-7739-7743
			$("#cercaCapitoloSubmit").prop("disabled",true);
			$("#cercaCapitoloGeneric").prop("disabled",true);
			
			$.ajax({
				url: 'ajax/getComponentiBilancioCapitoloAjax.do',   
				type: 'GET',
				data: oggettoDaInviare,
				//data: $(".radioCodiceCapitolo").serialize(),
				success: function(data) {
					$("#visualizzaDettaglioCapitolo").css('display', 'inline-block');
					$("#selectComponentiBilancio").css('display', 'inline-block');
					$(".rowTableComponentiSelected").remove();
					$("#ricercaCapitoloID > tbody > tr").eq(row_index).after("<tr class='rowTableComponentiSelected' ><td></td><td></td><td style='background-color:#d9d9d9'>Componente</td><td></td><td></td><td></td><td></td><td></td></tr>");
					//$('#listaComponenteBilancioRicerca').html('<option></option>');
					//DISABILITARE SE UN SOLO ELEMENTO E NON DA RICERCA
					if($("#azioneRicerca").val() == 'true'){
						$('#listaComponenteBilancioRicerca').html('<option></option>');
					}else{
						$('#listaComponenteBilancioRicerca').html('');
						if(data.listaComponentiBilancioCompleta.length != 1){
							$('#listaComponenteBilancioRicerca').html('<option></option>');
						}
					}
					//***** INSERIEMENTO ****/
					$.each(data.listaComponentiBilancio, function (i, item) {
						//Select componenti
// 						if($("#azioneRicerca").val() != 'true'){
// 						    $('#listaComponenteBilancioRicerca').append($('<option>', { 
// 						        value: item.uidComponente,
// 						        text : item.tipoComponenteImportiCapitolo.descrizione 
// 						    }));
// 						}
					   //Tabella
					   $("#ricercaCapitoloID > tbody > tr").eq(row_index+1+i).after("<tr class='rowTableComponentiSelected'><td></td><td></td><td>"+item.tipoComponenteImportiCapitolo.descrizione+"</td><td>"+item.dettaglioAnno0.disponibilita+"</td><td>"+item.dettaglioAnno1.disponibilita+"</td><td>"+item.dettaglioAnno2.disponibilita+"</td><td></td><td></td></tr>");
						   
					});
					/*****RICERCA E INSERIMENTO COMBO ******/
						$.each(data.listaComponentiBilancioCompleta, function (i, item) {
							//Select componenti
							    $('#listaComponenteBilancioRicerca').append($('<option>', { 
							        value: item.uidComponente,
							        text : item.tipoComponenteImportiCapitolo.descrizione 
							    }));
						     
						});
					/******* DISABILITIAMO LA SELECT SE ELEMENTO E' UNICO SOLO PER INSERIMENTO****/ 
					 if($("#azioneRicerca").val() != 'true'){
						 if(data.listaComponentiBilancioCompleta.length == 1){
								$('#listaComponenteBilancioRicerca').attr('readonly',true);
								$('#listaComponenteBilancioRicerca').attr("style", "pointer-events: none;");
							}else{
								$('#listaComponenteBilancioRicerca').attr('readonly',false);
								$('#listaComponenteBilancioRicerca').attr("style", "pointer-events: display;");
							}
					 }
					//SIAC-7739-7743
					$("#cercaCapitoloSubmit").prop("disabled",false);
					$("#cercaCapitoloGeneric").prop("disabled",false);
					
				}
			}).always(overlay.overlay.bind(overlay, 'hide'));


	        
			
		});
		$("a[rel=popover]").popover();
	});
	
</script> 