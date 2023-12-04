<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<s:if test="radioCodiceCapitolo != null">
	<div id="visDett" class="collapse margin-large">
		<div class="accordion_info">	
			<div id="divTabellaStanziamenti">
	<!--GESC014 TABELLA PER PROVA -->
	<h4>Stanziamenti</h4>  	
	<table  class="table  table-bordered" id="idStanziamentiTabellaPrev">
		<thead>
			<tr class="row-slim-bottom">
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th scope="col" colspan="2" class="text-center">${annoStanziamentoCapitoloInizio - 1}</th>
				<th scope="col" colspan="2" class="text-center">Residui ${annoStanziamentoCapitoloInizio}</th>
				<th scope="col" colspan="2" class="text-center">${annoStanziamentoCapitoloInizio + 0}</th>
				<th scope="col" colspan="2" class="text-center">${annoStanziamentoCapitoloInizio + 1}</th>
				<th scope="col" colspan="2" class="text-center">${annoStanziamentoCapitoloInizio + 2}</th>
				<th scope="col" colspan="2" class="text-center"> > ${annoStanziamentoCapitoloInizio + 2}</th>
			</tr>
			<tr class="row-slim-top">
					<th>&nbsp;</th>
					<th>&nbsp;</th>
				<th scope="col" class="text-center">Iniziale</th>
				<th scope="col" class="text-center">Finale</th>
				<th scope="col" class="text-center">Iniziale</th>
				<th scope="col" class="text-center">Finale</th>
				<th scope="col" class="text-center">Iniziale</th>
				<th scope="col" class="text-center">Finale</th>
				<th scope="col" class="text-center">Iniziale</th>
				<th scope="col" class="text-center">Finale</th>
				<th scope="col" class="text-center">Iniziale</th>
				<th scope="col" class="text-center">Finale</th>
				<th scope="col" class="text-center">Iniziale</th>
				<th scope="col" class="text-center">Finale</th>
			</tr>
		</thead>
		
		<tr  class="componentiRowFirst">
			<th rowspan = "2" class="stanziamenti-titoli"><a id="competenzaTotalePrev" href="#idStanziamentiTabellaPrev"  class="disabled" >Competenza</a></th>
			<td class="text-center">Stanziamento</td>
			<td class="text-right"><s:property value="importiEx.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnnoPrec.importo" /></td>
			<!--SIAC 6881-->
			<td class="text-right"><s:property value="importiResidui.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioResidui.importo" 		   /></td>
			<!-- -->
			<td class="text-right"><s:property value="importiCapitoloUscita0.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnno0.importo" /></td>									
			<td class="text-right"><s:property value="importiCapitoloUscita1.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnno1.importo" /></td>
			
			<td class="text-right"><s:property value="importiCapitoloUscita2.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnno2.importo" /></td>
			<!--SIAC 6881-->
			<td class="text-right"><s:property value="importiAnniSuccessivi.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnniSucc.importo"/></td>
			<!-- -->
		</tr>
	
	
		<tr class="componentiRowOther">
			<td class="text-center">Impegnato</td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="competenzaImpegnato.dettaglioAnnoPrec.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="competenzaImpegnato.dettaglioResidui.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="competenzaImpegnato.dettaglioAnno0.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="competenzaImpegnato.dettaglioAnno1.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="competenzaImpegnato.dettaglioAnno2.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="competenzaImpegnato.dettaglioAnniSucc.importo" /></td>
		</tr>
		
		
			
		<tbody id="componentiCompetenzaPrev">
	
				<s:iterator value="importiComponentiCapitolo" var="cc" status="componentiCapitolo">
					
					
					
					
					<s:if test="#cc.tipoDettaglioComponenteImportiCapitolo.descrizione=='Stanziamento'">
						<tr  class="componentiCompetenzaRow previsione-default-stanziamento" >
					</s:if>
					<s:else>
						<tr  class="componentiCompetenzaRow" >
					</s:else>												
						
						<td id="description-component" class="componentiCompetenzaRow previsione-default-stanziamento" rowspan="1">
<!-- SIAC-7349 - GS - 28/07/2020 - INIZIO -Non mostrare la descrizione della componente se la riga non è stanziamento
  						<s:property value="#cc.tipoComponenteImportiCapitolo.descrizione"/>   

-->
							<s:if test="#cc.tipoDettaglioComponenteImportiCapitolo.descrizione=='Stanziamento'">
                             	<s:property value="#cc.tipoComponenteImportiCapitolo.descrizione"/>
                            </s:if>
						</td> 
<!-- SIAC-7349 - GS - 28/07/2020 - FINE --> 
							
						<td class="text-center" id="type-component">
							<s:property value="#cc.tipoDettaglioComponenteImportiCapitolo.descrizione"/>
						</td>
						<td class="text-right"></td>
						 <td class="text-right"> 
							<s:property value="#cc.dettaglioAnnoPrec.importo"/>
						</td>
						<td class="text-right"></td>
						<td class="text-right">
							<s:property value="#cc.dettaglioResidui.importo"/>
						</td>
						<td class="text-right"></td>
						<td class="text-right">
							<s:property value="#cc.dettaglioAnno0.importo"/>
						</td>
						<td class="text-right"></td>
						<td class="text-right">
							<s:property value="#cc.dettaglioAnno1.importo"/>
						</td>
						<td class="text-right"></td>
						<td class="text-right">
							<s:property value="#cc.dettaglioAnno2.importo"/>
						</td>
						<td class="text-right"></td>
						<td class="text-right">
							<s:property value="#cc.dettaglioAnniSucc.importo"/>
						</td>
					</tr>
				</s:iterator>
			</tbody>
			<tr class="componentiRowFirst">
				<th rowspan="2" class="stanziamenti-titoli">Residuo</th>
				<td class="text-center">Presunti</td>	
				<td class="text-right" scope="row"><s:property value="importiEx.stanziamentoResiduoIniziale" /></td>
				<td class="text-right" scope="row"><s:property value="residuiPresunti.dettaglioAnnoPrec.importo" /></td>
				<!--SIAC 6881-->
				<td class="text-right" scope="row"><s:property value="importiCapitoloUscita0.stanziamentoResiduoIniziale" />
					</td>
				<td class="text-right"><s:property value="residuiPresunti.dettaglioResidui.importo" /></td>
				<!---->
				<td class="text-right" scope="row"><s:property value="importiCapitoloUscita2.stanziamentoResiduoIniziale" /></td>
				<td class="text-right"><s:property value="residuiPresunti.dettaglioAnno0.importo" /></td>
				
				<td class="text-right"><s:property value="importiCapitoloUscita2.stanziamentoResiduoIniziale" /></td>
				<td class="text-right"><s:property value="residuiPresunti.dettaglioAnno1.importo" /></td>
				<td class="text-right"><s:property value="importiCapitoloUscita2.stanziamentoResiduoIniziale" /></td>
				<td class="text-right"><s:property value="residuiPresunti.dettaglioAnno2.importo" /></td>
				<!--SIAC -6881-->
				<td class="text-right"><s:property value="importiAnniSuccessivi.stanziamentoResiduoIniziale" /></td>
				<td class="text-right"><s:property value="residuiPresunti.dettaglioAnniSucc.importo" /></td>
				<!---->
			</tr>
			<tr class="componentiRowOther">
				<td class="text-center">Effettivi</td>
				<td class="text-right" scope="row"><s:property value="importiEx.residuoEffettivoIniziale" /></td>
				<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnnoPrec.importo" /></td>
				<td class="text-right"><s:property value="importiResidui.residuoEffettivoIniziale" /></td>
				<td class="text-right"><s:property value="residuiEffettivi.dettaglioResidui.importo" /></td>
				<td class="text-right" scope="row"><s:property value="importiCapitoloUscita0.residuoEffettivoIniziale" /></td>
				<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnno0.importo" /></td>
				<td class="text-right"><s:property value="importiCapitoloUscita1.residuoEffettivoIniziale" /></td>
				<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnno1.importo" /></td>
				<td class="text-right"><s:property value="importiCapitoloUscita2.residuoEffettivoIniziale" /></td>
				<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnno2.importo" /></td>
				<td class="text-right"><s:property value="importiAnniSuccessivi.residuoEffettivoIniziale" /></td>
				<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnniSucc.importo" /></td>
				</tr>
			<tr class="componentiRowFirst">
				<th rowspan="2" class="stanziamenti-titoli">Cassa</th>
				<td class="text-center">Stanziamento</td>
	
				<td class="text-right" scope="row"><s:property value="importiEx.stanziamentoCassaIniziale" /></td>
				<td class="text-right" scope="row"><s:property value="cassaStanziato.dettaglioAnnoPrec.importo" /></td>
				<!--SIAC 6881-->
				<td class="text-right"><s:property value="importiResidui.stanziamentoCassaIniziale" /></td>
				<td class="text-right"><s:property value="cassaStanziato.dettaglioResidui.importo" /></td>
				<!---->
				<td class="text-right" scope="row"><s:property value="importiCapitoloUscita0.stanziamentoCassaIniziale" /></td>
				<td class="text-right"><s:property value="cassaStanziato.dettaglioAnno0.importo" /></td>
				
				<td class="text-right" scope="row"><s:property value="cassaStanziato.dettaglioAnno1.importo" /></td>
				<td class="text-right"><s:property value="cassaStanziato.dettaglioAnno1.importo" /></td>
				<td class="text-right" scope="row"></td>
				<td class="text-right"></td>
				<!-- SIAC 6881-->
				<td class="text-right"></td>
				<td class="text-right"></td>
				<!---->
			</tr>
			<tr class="componentiRowOther">
				<td class="text-center">Pagato</td>
				<td class="text-right" scope="row"><s:property value="importiEx.pagatoCassaIniziale" /></td><!-- da rivedere dato -->
				<td class="text-right"><s:property value="cassaPagato.dettaglioAnnoPrec.importo" /></td>
				<td class="text-right"><s:property value="importiResidui.pagatoCassaIniziale" /></td>
				<td class="text-right"><s:property value="cassaPagato.dettaglioResidui.importo" /></td>
				<td class="text-right" scope="row"><s:property value="cassaPagato.dettaglioResidui.importo" /></td>
				<td class="text-right"><s:property value="cassaPagato.dettaglioAnno0.importo" /></td>
				<td class="text-right" scope="row"><s:property value="importiCapitoloUscita1.pagatoCassaIniziale" /></td>
				<td class="text-right"><s:property value="cassaPagato.dettaglioAnno1.importo" /></td>
				<td class="text-right" scope="row"></td>
				<td class="text-right"></td>
				<td class="text-right"></td>
				<td class="text-right"></td>
			</tr>									
	</table>
</div>
		</div>
	</div>
</s:if>


<script type="text/javascript">
  
!function($) {
    


    var listTypeComponent=[];
    var rows = $("tbody#componentiCompetenzaPrev").find("tr");
    


    function getDescriptions(){
        var rows = $("tbody#componentiCompetenzaPrev").find("tr");
        for (var index = 0; index < rows.length; index++) {
            var typeComponent = {};
            var tableCell = {};
            var item = rows[index];
            var description = ($(item).find('td:first-child').text());
            var type = ($(item).find('td:eq(1)').text());
            typeComponent[description.trim()]=type.trim();
            tableCell[item] = typeComponent;
            listTypeComponent.push(tableCell);
        }
        getRowSpan();
    }

    function getValuesFromObject(item){
        var values;
        for(var k in item){
            values = item[k];
        }
        return values;
    }
    function getKeyFromObject(item){
        var key;
        for(var k in item){
            key = k;
        }
        return key;
    }

    

    function getRowSpan(){
        var i=0;
        while(i<listTypeComponent.length){
            
            if(i==listTypeComponent.length){
                break;
            }
            
            //oggettoni contenenti i td
            var obj=listTypeComponent[i]; 
            var obj2 = listTypeComponent[i+1];
            //oggetti contententi descrizione - tipo
            var keyObj1=getKeyFromObject(obj); //td
            var keyObj2=getKeyFromObject(obj2) //td
            var valuesObj1 = getValuesFromObject(obj); //{}
            var valuesObj2 = getValuesFromObject(obj2);//{}
            
            //confronto le chiavi e i valori (descr - type)
            var key1 = getKeyFromObject(valuesObj1) //fresco1
            var key2 = getKeyFromObject(valuesObj2) //fresco1
            var value1 = getValuesFromObject(valuesObj1);
            var value2 = getValuesFromObject(valuesObj2);
            
            if(key1===key2 && value1!==value2){
                $(rows[i]).find('.componenti-competenza').attr("rowspan", "2");
                $(rows[i+1]).find('.componenti-competenza').remove();
                i+=2;
            }else if(key1!==key2){
                i+=1;
            }else{
                i+=1;
            }
            

        }
    }


    

    $(function() {
      
        
        
        $("#componentiCompetenzaPrev").hide();

         $("#competenzaTotalePrev").click(function(){
             $("#componentiCompetenzaPrev").slideToggle();
         });

       
         getDescriptions();

    });
}(jQuery);
	
</script>




