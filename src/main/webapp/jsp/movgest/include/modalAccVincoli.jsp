<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<div id="guidaAccertamento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaAccertamentoLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true"data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4 class="nostep-pane">Seleziona accertamento</h4>					             
	</div>
	
    <div class="modal-body">        
		<fieldset class="form-horizontal">        		 
        <div id="campiRicerca" class="accordion-body collapse in">
			<%-- commentato su richiesta di anna, 25.02.2015  
				<div class="control-group">
				<label class="control-label"><span class="pull-left"><b>Capitolo:</b></span>Anno</label>
				<div class="controls">    
				<!-- numeroCapitolo, numeroArticolo, ueb, annoAccertamento, numeroAccertamento;
				accertamentoRicerca.annoCapitolo -->
				    <s:textfield id="annoCap" cssClass="span2 soloNumeri parametroRicercaAccertamento" readonly="true" name="step1Model.accertamentoRicerca.annoCapitolo" />
<!-- 					<input id="annoCap" class="span2 " type="text" value="2014" name="annoCap" disabled="disabled" > -->
					<span class="al">
						<label class="radio inline">Capitolo</label>
					</span>
					<s:textfield id="capitoloRice" cssClass="span2 parametroRicercaAccertamento" name="step1Model.accertamentoRicerca.numeroCapitolo" />
<!-- 					<input id="capitolo" class="span2" type="text" value="" name="capitolo" >       -->
					<span class="al">
						<label class="radio inline">Articolo</label>
					</span>
					
					<s:textfield id="articoloRice" cssClass="span2 parametroRicercaAccertamento" name="step1Model.accertamentoRicerca.numeroArticolo" />
<!-- 					<input id="articolo" class="span2" type="text" value="" name="articolo" > -->
					<span class="al">
						<label class="radio inline">UEB</label>
					</span>
					<s:textfield id="UBERice" cssClass="span2 parametroRicercaAccertamento" name="step1Model.accertamentoRicerca.ueb" />
<!-- 					<input id="UEB" class="span2" type="text" value="" name="UEB"/> -->
				</div>
			</div> --%>
  
			<div class="control-group">
				<label class="control-label"><span class="pull-left"><b>Accertamento:</b></span>Anno *</label>
				<div class="controls">
				
				    <s:textfield id="annoAccRice"  cssClass="lbTextSmall span2 parametroRicercaAccertamento" name="step1Model.accertamentoRicerca.annoAccertamento"/> 
<!-- 					<input id="AnnoAcc" class="lbTextSmall span2" type="text" value="" name="AnnoAcc" > -->
					<span class="al">
						<label class="radio inline">Numero *</label>
					</span>
					
					<s:textfield id="numAccRice" cssClass="lbTextSmall span2 parametroRicercaAccertamento" name="step1Model.accertamentoRicerca.numeroAccertamento" />
<!-- 					<input id="numAcc" class="lbTextSmall span2" type="text" value="" name="numAcc" >   -->
					<span class="al">
						<a class="btn btn-primary pull-right" id="ricercaGuidataAccPerVincoli" data-toggle="collapse" data-parent="guidaAccertamento" href="#campiRicerca">
						   <i class="icon-search icon"></i>&nbsp;cerca
						</a>  
					</span>
					
					<!-- 
					
					<a class="accordion-toggle btn btn-primary pull-right" id="ricercaGuidataCapitolo" data-toggle="collapse" data-parent="#guidaCap" href="#campiRicerca"> 
		 			 <i class="icon-search icon"></i>&nbsp;cerca&nbsp;<span class="icon"></span>
					</a>
					-->
							
				</div>
			</div>
        </div><!-- fine campiRicerca -->
        
        <div id="gestioneRisultatoRicercaAcc">
			<s:include value="/jsp/movgest/include/risultatoRicercaElencoAcc.jsp" />
		</div>
        
        
			
		</fieldset>
		
	</div> 
	
	<div class="modal-footer">
		<s:submit id="confermaAccVincoli" name="confermaAcc" value="conferma" data-dismiss="modal"
			  	  method="selezionaAccPerVincolo" cssClass="btn btn-primary" aria-hidden="true" />
<!-- 		<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">conferma</button> -->
	</div>
</div>