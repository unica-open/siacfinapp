<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>


<div id="associaSpesa" class="modal hide fade" tabindex="-1" role="dialog"
	aria-labelledby="guidaListaLabel" aria-hidden="true">
	<div class="modal-body">
		
		<h4>Associa Modifiche</h4>
		<fieldset class="form-horizontal">
			<div id="campiRicerca" class="accordion-body collapse in">
				<div class="control-group">
						<s:hidden id= "indexReimputazioneAnnualita" name="reimputazioneAnnualita.index" value="%{reimputazioneAnnualita.index}"/>		
						<s:hidden id= "motivoInes" name="reimputazioneAnnualita.idMotivo" value="%{reimputazioneAnnualita.idMotivo}"/>
						<!-- SIAC-7349 Inizio  SR190 CM 05/05/2020 aggiunto span, eliminato div e modificata class="span5" in class="span4" per allineamento in unica riga -->
						<span class="al"><label class="radio inline" for="annoAssocia">Anno</label>
							<s:textfield id="annoAssocia" cssClass="lbTextSmall span1 parametroRicercaCapitolo soloNumeri"	 name="reimputazioneAnnualita.anno"  />
						</span>
						
						<span class="al"> <label class="radio inline"for="importoAssocia">Importo</label>
							
							<s:textfield type="text" maxlength="30"  name="reimputazioneAnnualita.importo" onblur="checkNullToEmptyString(event)" id="importoAssocia" placeholder="0.00" class="span1 soloNumeri decimale"/>
						</span>
						
						<span class="al"> <label class="radio inline" for="descrzioneAssocia">Descrizione</label>
							<s:select list="motiviRorReimputazione" id="sintesiAssocia" headerKey="" headerValue=""  listKey="key" listValue="sintesi"  cssClass="span4" name="reimputazioneAnnualita.valoreSintesi" />
                  			<s:textarea type="text"  name="reimputazioneAnnualita.descrizione" value="%{descrizione}"  id="descrzioneAssocia" class="span4" style="resize: none; height: 30px;"/>
						</span>
						<!-- SIAC-7349 Fine  SR190 CM 05/05/2020 -->
				</div>
			</div>
		</fieldset>

		<br/>
 		<div id="gestioneListaCollegata" style="clear:both; padding-top:3px;">
			
		</div>
		
		<s:include value="/jsp/include/actionMessagesErrors.jsp" />
		
		<!-- SIAC-7349 Inizio SR190 CM 20/05/2020 -->
		<%-- Qui non serve l'uid perchè va mostrata la tabella per tutti i record. Solo che in alcuni casi sarà vuota, in altri piena --%>
		<%-- in caso aggiungere solo il controllo di uid != 0 --%>
		<s:if test="%{!isInInserimento() && isInImpegno()}">
			<s:include
				value="/jsp/movgest/include/listaCollegataSpesaImpegno.jsp" />
		</s:if>
		<s:else>
			<s:include value="/jsp/movgest/include/listaCollegataSpesa.jsp" />
		</s:else>
		<!-- SIAC-7349 Fine SR190 CM 20/05/2020 -->
	</div>
	
	<!-- SIAC-7349 Inizio SR190 CM 20/05/2020 -->
	<s:if test="%{!isInInserimento() && isInImpegno()}">
		<div class="modal-footer">
			<s:submit id="associaSpesaSubmitImpegno" value="chiudi" cssClass="btn btn-primary pull-right" data-dismiss="modal" />
		</div>
	</s:if>
	<s:else>
		<div class="modal-footer">
			<!--task-131 <s:submit id="associaSpesaSubmit" name="cerca" value="conferma" method="confermaSpeseCollegate" onclick="overlayForWait()" cssClass="btn btn-primary pull-right" /> -->
			<s:submit id="associaSpesaSubmit" name="cerca" value="conferma" action="%{#confermaSpeseCollegateAction}" onclick="overlayForWait()" cssClass="btn btn-primary pull-right" />
		</div>
	</s:else>
	<!-- SIAC-7349 Fine SR190 CM 20/05/2020 -->
</div>

<script type="text/javascript" src="${jspath}movgest/listaCollegataSpesa.js"></script>
