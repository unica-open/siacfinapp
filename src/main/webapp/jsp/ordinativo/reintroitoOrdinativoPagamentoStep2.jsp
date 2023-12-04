<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>


   
    <%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />

    <%-- Inclusione JavaScript NUOVO --%>
    <s:include value="/jsp/include/javascript.jsp" />	
  	<s:include value="/jsp/include/javascriptTree.jsp" />
  </head>
  
  <body>     
  
  <s:include value="/jsp/include/header.jsp" />
                    
  <hr />
<div class="container-fluid-banner">


  <a name="A-contenuti" title="A-contenuti"></a>
</div>


<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">  

      <s:form id="reintroitoOrdinativoPagamentoStep2" action="reintroitoOrdinativoPagamentoStep2" method="post" cssClass="form-horizontal">
		 <s:include value="/jsp/include/actionMessagesErrors.jsp" />
		 
		<s:hidden id="liquidazioneTrovata" name="liquidazioneTrovata"/>
		<s:hidden id="accertamentoTrovato" name="accertamentoTrovato"/>

		<h3>Reintroito ordinativo di pagamento</h3>
		
		<div id="MyWizard" class="wizard">
		  <ul class="steps">
				<li data-target="#step1" class="complete"><span class="badge">1</span>Step 1<span class="chevron"></span></li>
				<li data-target="#step2" class="active"><span class="badge">2</span>Step 2<span class="chevron"></span></li>
<%-- 				<li data-target="#step3" class="complete"><span class="badge">3</span>Step 3<span class="chevron"></span></li> --%>
			</ul>
		</div>

        <div class="step-content">
          <div class="step-pane active" id="step1">
           
          <!-- Ricerca ordinativo -->
       		<h4>Ordinativo <s:property value="reintroitoOrdinativoStep1Model.ordinativoDaReintroitare.numero"/>  del <s:property value="%{reintroitoOrdinativoStep1Model.ordinativoDaReintroitare.dataEmissione}"/> -  Stato <s:property value="reintroitoOrdinativoStep1Model.ordinativoDaReintroitare.statoOperativoOrdinativo"/> dal <s:property value="%{reintroitoOrdinativoStep1Model.ordinativoDaReintroitare.dataInizioValidita}"/> </h4>
        	
			<s:if test="isOrdinativoPresente()">
				<s:include value="/jsp/ordinativo/include/headerDettaglioOrdinativoReintroito.jsp" />
			</s:if>
          
          
          <table class="table table-hover tab_left">
          
	          <thead>
         			<tr class="newline">
         				<th scope="col">&nbsp;</th>
         				<th scope="col">&nbsp;</th>
         			    <th scope="col">Importo</th>
						<th scope="col" colspan="3" style="text-align:center;">Impegno di destinazione</th>
						<th scope="col">&nbsp;</th>
						<th scope="col" colspan="3" style="text-align:center;">Accertamento di destinazione</th>
					    <th scope="col">&nbsp;</th>
               		</tr>
	          </thead>
	          
	          <tbody>
	          
	          	<tr>
		          	<td width="14%">Ordinativo di pagamento</td>
		          	<td width="30%">Importo netto: <s:property value="reintroitoOrdinativoStep1Model.importoNetto" /></td>
		          	
		          	<td width="10%"><s:textfield id="idNettoImporto" name="reintroitoOrdinativoStep1Model.ordinativoDaReintroitare.importoOrdinativo" cssClass="span12" disabled="true"/></td>
<%-- 					<td width="10%"><s:property value="getText('struts.money.format', {reintroitoOrdinativoStep1Model.ordinativoDaReintroitare.importoOrdinativo})" cssClass="span12"/></td> --%>
		          
		          	<td width="5%"><s:textfield title="anno" id="idNettoAnnoImp" name="reintroitoOrdinativoStep2Model.netto.annoImpegno" cssClass="span12" disabled="true"/></td>
		          	<td width="8%"><s:textfield title="numero" id="idNettoNumeroImp" name="reintroitoOrdinativoStep2Model.netto.numeroImpegno" cssClass="span12" disabled="true"/></td>
	         	    <td width="5%"><s:textfield title="sub" id="idNettoNumeroSubImp" name="reintroitoOrdinativoStep2Model.netto.numeroSubImpegno" cssClass="span12" disabled="true"/></td>
		          	<td width="5%"><s:if test="!elaborazioneAvviata"><button data-compilazione-guidata="cgNettoImp" data-target="#guidaImpegno" title="Compilazione guidata" data-toggle="modal" class="btn btn-primary"><i class="icon-search icon" ></i></button></s:if></td>
		            <td width="5%"></td>
		            <td width="8%"></td>
		            <td width="5%"></td>
		            <td width="5%"></td>
		        </tr>
		        
		        <s:if test="ciSonoRitenute()">
		        
		        	<tr>
			          	<td width="14%">Ritenute</td>
			          	<td width="30%"></td>
			          	
			          	<td width="10%"><s:textfield id="idRitenuteImporto" name="reintroitoOrdinativoStep2Model.ritenute.importo" cssClass="span12" disabled="true"/></td>
<%-- 			            <td width="10%"><s:property value="getText('struts.money.format', {reintroitoOrdinativoStep2Model.ritenute.importo})" cssClass="span12"/></td> --%>
			          	
			          	<td width="5%"><s:textfield title="anno" id="idRitenuteAnnoImp" name="reintroitoOrdinativoStep2Model.ritenute.annoImpegno" cssClass="span12" maxlength="4" disabled="true"/></td>
			          	<td width="8%"><s:textfield title="numero"  id="idRitenuteNumeroImp" name="reintroitoOrdinativoStep2Model.ritenute.numeroImpegno" cssClass="span12" disabled="true"/></td>
		         	    <td width="5%"><s:textfield title="sub" id="idRitenuteNumeroSubImp" name="reintroitoOrdinativoStep2Model.ritenute.numeroSubImpegno" cssClass="span12" disabled="true"/></td>
			          	<td width="5%"><s:if test="!elaborazioneAvviata"><button data-compilazione-guidata="cgRitenuteImp" data-target="#guidaImpegno" title="Compilazione guidata" data-toggle="modal" class="btn btn-primary"><i class="icon-search icon"></i></button></s:if></td>
			            <td width="5%"><s:textfield title="anno" id="idRitenuteAnnoAcc" name="reintroitoOrdinativoStep2Model.ritenute.annoAccertamento" cssClass="span12" maxlength="4" disabled="true"/></td>
			          	<td width="8%"><s:textfield title="numero" id="idRitenuteNumeroAcc" name="reintroitoOrdinativoStep2Model.ritenute.numeroAccertamento" cssClass="span12" disabled="true"/></td>
		         	    <td width="5%"><s:textfield title="sub" id="idRitenuteNumeroSubAcc" name="reintroitoOrdinativoStep2Model.ritenute.numeroSubAccertamento" cssClass="span12" disabled="true"/></td>
			          	<td width="5%"><s:if test="!elaborazioneAvviata"><button data-compilazione-guidata-acc="cgRitenuteAcc" data-target="#guidaAccertamento" title="Compilazione guidata" data-toggle="modal" class="btn btn-primary"><i class="icon-search icon"></i></button></s:if></td>
		        	</tr>
		        
		        
		         	<s:iterator value="reintroitoOrdinativoStep2Model.listOrdInc" var="riga" status="stat">
			          <tr>
			          	<td width="14%"><s:property value="#riga.labelRigaOrdinativo" /></td>
			          	<td width="30%"><s:textfield value="%{#riga.descrizione}" name="reintroitoOrdinativoStep2Model.listOrdInc[%{#stat.index}].descrizione" cssClass="span12 saveOnChange" disabled="true"/></td>
			          	
			          	<td width="10%"><s:textfield value="%{#riga.importo}" name="reintroitoOrdinativoStep2Model.listOrdInc[%{#stat.index}].importo" cssClass="span12 saveOnChange" disabled="true"/></td>
<%-- 			            <td width="10%"><s:property value="getText('struts.money.format', {reintroitoOrdinativoStep2Model.listOrdInc[%{#stat.index}].importo})" cssClass="span12"/></td> --%>
			          	
			          	<td width="5%"><s:textfield  value="%{#riga.annoImpegno}" name="reintroitoOrdinativoStep2Model.listOrdInc[%{#stat.index}].annoImpegno" cssClass="span12" maxlength="4" title="anno" disabled="true"/></td>
			          	<td width="8%"><s:textfield value="%{#riga.numeroImpegno}" name="reintroitoOrdinativoStep2Model.listOrdInc[%{#stat.index}].numeroImpegno" cssClass="span12" title="numero" disabled="true"/></td>
		         	    <td width="5%"><s:textfield value="%{#riga.numeroSubImpegno}" name="reintroitoOrdinativoStep2Model.listOrdInc[%{#stat.index}].numeroSubImpegno" cssClass="span12" title="sub" disabled="true"/></td>
			          	<td width="5%"><s:if test="!elaborazioneAvviata"><button data-compilazione-guidata="imp_<s:property value="#stat.index"/>" data-target="#guidaImpegno" title="Compilazione guidata" data-toggle="modal" class="btn btn-primary"><i class="icon-search icon"></i></button></s:if></td>
			            <td width="5%"><s:textfield  value="%{#riga.annoAccertamento}" name="reintroitoOrdinativoStep2Model.listOrdInc[%{#stat.index}].annoAccertamento" cssClass="span12" maxlength="4" title="anno" disabled="true"/></td>
			          	<td width="8%"><s:textfield value="%{#riga.numeroAccertamento}" name="reintroitoOrdinativoStep2Model.listOrdInc[%{#stat.index}].numeroAccertamento" cssClass="span12" title="numero" disabled="true"/></td>
		         	    <td width="5%"><s:textfield  value="%{#riga.numeroSubAccertamento}" name="reintroitoOrdinativoStep2Model.listOrdInc[%{#stat.index}].numeroSubAccertamento" cssClass="span12"  title="sub" disabled="true"/></td>
			          	<td width="5%"><s:if test="!elaborazioneAvviata"><button data-compilazione-guidata-acc="acc_<s:property value="#stat.index"/>" data-target="#guidaAccertamento" title="Compilazione guidata" data-toggle="modal" class="btn btn-primary"><i class="icon-search icon"></i></button></s:if></td>
			       	  </tr>
		          
		          	</s:iterator>
		        
		        </s:if>
		        
	          
	          </tbody>
          
          </table>
          

		  <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
           			<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
					
					<%-- CR-2023 da eliminare 
					<s:hidden id="ricaricaAlberoContoEconomico" name="teSupport.ricaricaAlberoContoEconomico"/> --%>
					
					<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
					<s:hidden id="ricaricaSiopeSpesa" name="teSupport.ricaricaSiopeSpesa"/>
		   <!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
			
			<s:hidden id="strutturaSelezionataSuPagina" name="strutturaSelezionataSuPagina"></s:hidden>
			
			
			
            <!-- Modal -->
            
            <s:include value="/jsp/ordinativo/include/modalImpegnoReintroito.jsp" />
            <s:include value="/jsp/ordinativo/include/modalAccertamentoReintroito.jsp" /> 
            
            
<%--             <s:include value="/jsp/include/modalSoggetto.jsp" />	 --%>
<%--             <s:include value="/jsp/include/modalProvvedimenti.jsp" /> --%>
<%--             <s:include value="/jsp/include/modalCapitolo.jsp" /> --%>
<%--             <s:hidden id="strutturaDaInserimento"  name="strutturaDaInserimento"></s:hidden> --%>
            <!-- Fine Modal -->
            
            </div>
        </div> 
        
		<p class="margin-medium"> 
		<s:include value="/jsp/include/indietroSubmit.jsp" /> 
		<!-- task-131 <s:submit cssClass="btn btn-secondary" method="annullaStep2" value="annulla" name="annulla" /> -->
		<!-- task-131 <s:submit cssClass="btn btn-secondary" action="reintroitoOrdinativoPagamentoStep2_annullaStep2" value="annulla" name="annulla" /> -->
		<s:submit cssClass="btn btn-secondary" action="reintroitoOrdinativoPagamentoStep2_annullaStep2" value="annulla" name="annulla" />
		
        <!-- task-131 <s:submit id="eseguiReintroito" disabled="elaborazioneAvviata" cssClass="btn btn-primary pull-right freezePagina" method="preCheckCoerenzaSoggetti" value="esegui" name="esegui" /> -->
        <s:submit id="eseguiReintroito" disabled="elaborazioneAvviata" cssClass="btn btn-primary pull-right freezePagina" action="reintroitoOrdinativoPagamentoStep2_preCheckCoerenzaSoggetti" value="esegui" name="esegui" />
        
        <a id="linkMsgCheckSoggetti" href="#msgControlloSoggetti" style="display: none;" data-toggle="modal"></a>
        
        <a id="linkMsgCheckPianiDeiConti" href="#msgControlloPianiDeiConti" style="display: none;" data-toggle="modal"></a>
        
        
		<!-- task-131 <s:submit id="controllaDispCassa" cssClass="btn btn-secondary pull-right" method="controlloDispDiCassa" value="controllo disponibilita di cassa" name="controllo disponibilita di cassa" /> -->
		<s:submit id="controllaDispCassa" cssClass="btn btn-secondary pull-right" action="reintroitoOrdinativoPagamentoStep2_controlloDispDiCassa" value="controllo disponibilita di cassa" name="controllo disponibilita di cassa" />
		<!-- task-131 <s:submit id="controllaDispLiq" cssClass="btn btn-secondary pull-right" method="controlloDispLiquidare" value="controllo disponibilita a liquidare" name="controllo disponibilita a liquidare" /> -->
		<s:submit id="controllaDispLiq" cssClass="btn btn-secondary pull-right" action="reintroitoOrdinativoPagamentoStep2_controlloDispLiquidare" value="controllo disponibilita a liquidare" name="controllo disponibilita a liquidare" />	
		
		</p>  
		
		<div id="msgControlloSoggetti" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgPrimaNota" aria-hidden="true">
		      <div class="modal-body">
		          
		        <div style="padding: 0px 20px 0px 20px">
					<s:include value="/jsp/include/actionMessagesErrors.jsp" />
				</div>
		        
		        <p>Classi soggetti non coerenti, vuoi salvare comunque ?</p>
		        
		      </div>
		      <div class="modal-footer">
		        <button class="btn" data-dismiss="modal" aria-hidden="true">no</button>
		        <!-- task-131 <s:submit id="eseguiReintroito1" disabled="elaborazioneAvviata" cssClass="btn btn-primary pull-right freezePagina" method="preCheckCoerenzaPianoDeiConti" value="si" name="si" /> -->
		        <s:submit id="eseguiReintroito1" disabled="elaborazioneAvviata" cssClass="btn btn-primary pull-right freezePagina" action="reintroitoOrdinativoPagamentoStep2_preCheckCoerenzaPianoDeiConti" value="si" name="si" />
		      </div>
		</div>   
		
		<div id="msgControlloPianiDeiConti" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgPrimaNota" aria-hidden="true">
		      <div class="modal-body">
		          
		        <div style="padding: 0px 20px 0px 20px">
					<s:include value="/jsp/include/actionMessagesErrors.jsp" />
				</div>
		        
		        <p>Piano dei conti potenzialemente non coerente, vuoi salvare comunque ?</p>
		        
		      </div>
		      <div class="modal-footer">
		        <button class="btn" data-dismiss="modal" aria-hidden="true">no</button>
		        <!-- task-131 <s:submit id="eseguiReintroito2" disabled="elaborazioneAvviata" cssClass="btn btn-primary pull-right freezePagina" method="prosegui" value="si" name="si" /> -->
		        <s:submit id="eseguiReintroito2" disabled="elaborazioneAvviata" cssClass="btn btn-primary pull-right freezePagina" action="reintroitoOrdinativoPagamentoStep2_prosegui" value="si" name="si" />
		      </div>
		</div>      

      </s:form>
    </div>
  </div>	 
</div>	



<script type="text/javascript">

	<s:if test="reintroitoOrdinativoStep2Model.checkWarningSoggetti">
		$("#linkMsgCheckSoggetti").click();
	</s:if>
	
	<s:if test="reintroitoOrdinativoStep2Model.checkWarningPianoDeiConti">
		$("#linkMsgCheckPianiDeiConti").click();
	</s:if>
	
	$(document).ready(function() {
		
    	$('button[data-compilazione-guidata]').click(function(e) {
    		var $el = $(e.currentTarget);
    		var selezionato = $el.data('compilazioneGuidata');
    		console.log($el.data('compilazioneGuidata'));
    		
    		$.ajax({
                //task-131 url: '<s:url method="azzeraModaleImpegno"></s:url>',
                url: '<s:url action="reintroitoOrdinativoPagamentoStep2_azzeraModaleImpegno"></s:url>',
                type: "GET",
                data: {riferimentoRigaSelezionata: selezionato },
             	success: function(data){
                	$("#refreshImpegnoPopupModal").html(data);
                }
        	});
    		
    	});
    	
    	$('button[data-compilazione-guidata-acc]').click(function(e) {
    		var $el = $(e.currentTarget);
    		var selezionato = $el.data('compilazioneGuidataAcc');
    		console.log($el.data('compilazioneGuidataAcc'));
    		
    		$.ajax({
                //task-131 url: '<s:url method="azzeraModaleAccertamento"></s:url>',
                 url: '<s:url action="reintroitoOrdinativoPagamentoStep2_azzeraModaleAccertamento"></s:url>',
                type: "GET",
                data: {riferimentoRigaSelezionata: selezionato },
             	success: function(data){
                	$("#refreshAccertamentoPopupModal").html(data);
                }
        	});
    		
    	});
    	
    	$(".saveOnChange").change(function(e){
    		var $target = $(e.target);
    		var name = $target.attr('name');
    		var value = $target.val();
    		//Carico i dati in tabella "Modalita' di pagamento"		
    		$.ajax({
    			//task-131 url: '<s:url method="saveOnChangeElementoRigaOrdinaivi"></s:url>',
    			url: '<s:url action="reintroitoOrdinativoPagamentoStep2_saveOnChangeElementoRigaOrdinaivi"></s:url>',
    			type: "GET",
    			data: $(".hiddenGestoreToggle").serialize() + "&name=" + name + "&value=" + value, 
    		});			
    	});
    	
		
		
	});
</script>
	
<s:include value="/jsp/include/footer.jsp" />
