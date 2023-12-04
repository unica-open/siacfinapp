<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<!-- abilita se OP-COM-gestisciCFG -->
<s:if test="abilitatoGestioneTE()">

<!----------------------------- HIDDEN PER GESTIONE ALBERI ----------------------------------->
<s:hidden id="idMacroaggregatoCapitolo" name="teSupport.idMacroAggregato"/>
<s:hidden id="idPianoDeiContiCapitolo" name="teSupport.pianoDeiConti.uid"/>
<s:hidden id="codiceClassificatorePdc" name="teSupport.pianoDeiConti.tipoClassificatore.codice"/>
<s:hidden id="idPianoDeiContiCapitoloPadrePerAggiorna" name="teSupport.idPianoDeiContiPadrePerAggiorna"/>
<s:hidden id="codicePianoDeiContiCapitolo" name="teSupport.pianoDeiConti.codice"/>
<s:hidden id="descrizionePianoDeiContiCapitolo" name="teSupport.pianoDeiConti.descrizione"/>
<%-- CR 2023 si elimina il conto economico
<s:hidden id="idContoEconomicoTransazioneEconomica" name="teSupport.contoEconomico.uid"/>
<s:hidden id="codiceContoEconomicoTransazioneEconomica" name="teSupport.contoEconomico.codice"/>
<s:hidden id="descrizioneContoEconomicoTransazioneEconomica" name="teSupport.contoEconomico.descrizione"/> 
<s:hidden id="ricaricaAlberoContoEconomico" name="teSupport.ricaricaAlberoContoEconomico"/>
--%>
<s:hidden id="idSiopeSpesaTransazioneEconomica" name="teSupport.siopeSpesa.uid"/>
<s:hidden id="codiceSiopeSpesaTransazioneEconomica" name="teSupport.siopeSpesa.codice"/>
<s:hidden id="descrizioneSiopeSpesaTransazioneEconomica" name="teSupport.siopeSpesa.descrizione"/>
<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>



<s:hidden id="hiddenDatiUscitaImpegno" name="teSupport.datiUscitaImpegno" />
<s:hidden id="ricaricaStrutturaAmministrativa" name="teSupport.ricaricaStrutturaAmministrativa"/>
<s:hidden id="struttAmmOriginale" name="teSupport.struttAmmOriginale"/>
<s:hidden id="ricaricaSiopeSpesa" name="teSupport.ricaricaSiopeSpesa"/>
<!-------------------------------------------------------------------------------------------->

<fieldset class="form-horizontal">
	<s:if test="transazioneElementareAttiva()">
		<div class="accordion" id="soggetto1">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a class="accordion-toggle" data-toggle="collapse"
						data-parent="#soggetto1" href="#1">Transazione elementare<span
						class="icon">&nbsp;</span></a>
				</div>
				<div id="1" class="accordion-body collapse in">
					<div class="accordion-inner">
						<!-- MISSIONE E PROGRAMMA -->
						<s:if test="missioneProgrammaAttivi()">
							<div class="control-group">
								<label for="nomeautore" class="control-label">Missione</label>
								<div class="controls">
									<s:if test="teSupport.listaMissione!=null">
										<s:select list="teSupport.listaMissione" id="listaMissione"
											disabled="true" name="teSupport.missioneSelezionata" cssClass="span10"
											listKey="codice" listValue="descrizione" />
										<s:hidden name="teSupport.missioneSelezionata"/>
									</s:if>
								</div>
							</div>
							<div class="control-group">
								<label for="soggetto5" class="control-label">Programma</label>
								
								<div class="controls">
									<s:select list="teSupport.listaProgramma" id="listaProgramma"
										disabled="true" name="teSupport.programmaSelezionato" cssClass="span10"
										listKey="codice" listValue="descrizione"
										headerKey="" headerValue="" />
									<s:hidden name="teSupport.programmaSelezionato"/>
								</div>
								
								
							</div>
						</s:if>
						<!-- PIANO DEI CONTI ECONOMICO -->
						<div class="control-group">
							<label for="pdc" class="control-label"><abbr
								title="Piano dei Conti">P.d.C.</abbr> finanziario *</label>
							<div class="controls">
<%-- 								<s:property value="teSupport.pianoDeiConti.codice" /> --%>
<!-- 								- -->
								<s:property value="teSupport.pianoDeiConti.descrizione" />
								&nbsp;&nbsp;&nbsp; 
								<!-- liquidazione  -->							
								<s:if test="teSupport.oggettoAbilitaTE.equals('LIQUIDAZIONE') || teSupport.oggettoAbilitaTE.equals('ORDINATIVO_PAGAMENTO') || teSupport.oggettoAbilitaTE.equals('ORDINATIVO_INCASSO')">
									<%-- non si deve vedere il btn --%>
								</s:if>	
								<s:elseif test="teSupport.oggettoAbilitaTE.equals('IMPEGNO') || teSupport.oggettoAbilitaTE.equals('ACCERTAMENTO')">
									  <%-- SIAC-5288 POTREI ESSERE IN AGGIORNA IMPEGNO O ACCERTAMENTO POTENDO MODIFICARE SOLO GSA: --%>
									  <s:if test="isAbilitatoAggiornamentoGenerico()">
									  		 <a  href="#myModal" role="button"
											class="btn btn-primary" data-toggle="modal">seleziona il
											Piano dei Conti <i class="icon-spin icon-refresh spinner" id="spinnerElementoPianoDeiContiTE"></i></a>
									  </s:if>
								</s:elseif>
								<s:else>
								    <a  href="#myModal" role="button"
										class="btn btn-primary" data-toggle="modal">seleziona il
										Piano dei Conti <i class="icon-spin icon-refresh spinner" id="spinnerElementoPianoDeiContiTE"></i></a>
								</s:else>
								<div id="myModal" class="modal hide fade" tabindex="-1"
									role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
									
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" 
												aria-hidden="true" onclick="resetLastTreeNode()">x</button>
											<h3 id="myModalLabel">Piano dei Conti</h3>
										</div>
									
									 <!-- ALBERO VISUALIZZATO -->
									<div class="modal-body" id="elementiPdcTransazioneElementareDiv">
										<ul id="elementiPdcTransazioneElementare" class="ztree"></ul>
									</div>
									
									 <!-- ALBERO IN ATTESA -->
									<div class="modal-body" id="elementiPdcTransazioneElementareWait">
										
										    Attendere prego..
<!-- 											<img alt="Attendere prego.." src="/css/wait.gif" > -->
										
									</div>
									
									<div class="modal-footer">
										<!--task-131 <s:submit value="conferma" name="conferma" method="confermaPdc" cssClass="btn btn-primary" aria-hidden="true"></s:submit>-->
										<!-- task-159 -->
										<s:submit value="conferma" name="conferma" action="%{#confermaPdcAction}" cssClass="btn btn-primary" aria-hidden="true"></s:submit>
									</div>
								</div>
							</div>
						</div>
						
						
					<%-- 					
					CR 2023 si elimina il conto economico
						<!-- CONTO ECONOMICO -->
						<div class="control-group">
							<label for="contoEconomicoLabel" class="control-label">Conto economico 
					    	 </label>
							<div class="controls">
								<s:property value="teSupport.contoEconomico.codice" />
								-
								<s:property value="teSupport.contoEconomico.descrizione" />
								&nbsp;&nbsp;&nbsp; 
								
								<s:if test="teSupport.oggettoAbilitaTE.equals('ORDINATIVO_PAGAMENTO')">
									<!-- non si deve vedere il btn -->
								</s:if>	
								<s:else>
									<a href="#myModalContoEconomico"
										role="button" class="btn btn-primary" data-toggle="modal">seleziona
										il Conto Economico <i class="icon-spin icon-refresh spinner" id="spinnerElementoContoEconomicoTE"></i></a>
								</s:else>
								<div id="myModalContoEconomico" class="modal hide fade"
									tabindex="-1" role="dialog"
									aria-labelledby="myModalContoEconomicoLabel" aria-hidden="true">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-hidden="true" onclick="resetLastTreeNode()">x</button>
										<h3 id="myModalContoEconomicoLabel">Conto Economico</h3>
									</div>
									<!-- ALBERO VISUALIZZATO -->
									<div class="modal-body" id="contoEconomicoTransazioneElementareDiv">
									
										<ul id="contoEconomicoTransazioneElementare" class="ztree"></ul>
									</div>
									<!-- ALBERO IN ATTESA -->
									<div class="modal-body" id="contoEconomicoTransazioneElementareWait">
									     Attendere prego..
									</div>
									
									<div class="modal-footer">
										<s:submit value="conferma" name="confermaContoEconomico" id="btnConfermaContoEconomico"
											method="confermaContoEconomico" cssClass="btn btn-primary"
											data-dismiss="modal" aria-hidden="true"></s:submit>
									</div>
								</div>
							</div>
						</div> 
						--%>
						
						<!-- COFOG -->
						<s:if test="cofogAttivo()">
							<div class="control-group">
								
										
								<s:if test="teSupport.oggettoAbilitaTE.equals('LIQUIDAZIONE') || teSupport.oggettoAbilitaTE.equals('ORDINATIVO_PAGAMENTO')">
									<label for="Titolo" class="control-label">Cofog</label>
									<div class="controls">
									
										<s:select list="teSupport.listaCofog" id="listaCofog" headerKey=""
											headerValue="" name="teSupport.cofogSelezionato" cssClass="span10"
											listKey="codice" listValue="%{codice + ' - ' + descrizione}" disabled="true" />
											
										<s:hidden value="teSupport.cofogSelezionato"></s:hidden>	
									</div>
								</s:if>
								<s:else>
									<label for="Titolo" class="control-label">Cofog *</label>
									<div class="controls">
									   <s:select list="teSupport.listaCofog" id="listaCofog" headerKey=""
												headerValue="" name="teSupport.cofogSelezionato" cssClass="span10"
												listKey="codice" listValue="%{codice + ' - ' + descrizione}"
												  disabled="disabilitaCampoTePerGestisciMovP() || !isAbilitatoAggiornamentoGenerico()" />
																						
									</div>			
								</s:else>
							</div>
						</s:if>
						<!-- TRANSAZIONE EUROPEA -->
						<s:if test="datiUscitaImpegno()">
						    <s:if test="teSupport.oggettoAbilitaTE.equals('ORDINATIVO_PAGAMENTO') || presenzaOrdinativiPerLaLiquidazione || liquidazioneConAzioneImpegnoDecentrato">
						        <!--  DISABILITO -->
								<div class="control-group">
									<label for="Macroaggregato" class="control-label">Codifica
										Transazione Europea</label>
									<div class="controls">
										<s:select list="teSupport.listaTransazioneEuropeaSpesa" headerKey=""
											headerValue="" id="listaTransEuropea" disabled="true"
											name="teSupport.transazioneEuropeaSelezionato" cssClass="span10"
											listKey="codice" listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
								<s:hidden value="teSupport.listaTransazioneEuropeaSpesa" />
							</s:if>
							<s:else>
							 	<!--  ABILITO -->
								<div class="control-group">
									<label for="Macroaggregato" class="control-label">Codifica
										Transazione Europea *</label>
									<div class="controls">
										<s:select list="teSupport.listaTransazioneEuropeaSpesa" headerKey=""
											headerValue="" id="listaTransEuropea" 
											disabled="disabilitaCampoTePerGestisciMovP() || !isAbilitatoAggiornamentoGenerico()" 
											name="teSupport.transazioneEuropeaSelezionato" cssClass="span10"
											listKey="codice" listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
							</s:else>
						</s:if>
						<s:else>
						    <!-- accertamento -->
						    
						    <s:if test="teSupport.oggettoAbilitaTE.equals('ORDINATIVO_INCASSO')">
						    	<!-- disabilito -->
						    	<!-- incasso -->
								<div class="control-group">
									<label for="Macroaggregato" class="control-label">Codifica
										Transazione Europea *</label>
									<div class="controls">
										<s:select list="teSupport.listaTransazioneEuropeaEntrata" headerKey=""
											headerValue="" id="listaTransEuropea" disabled = "true"
											name="teSupport.transazioneEuropeaSelezionato" cssClass="span10"
											listKey="codice" listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
								<s:hidden value="teSupport.transazioneEuropeaSelezionato"/>
							</s:if>
							<s:else>
							    <!-- abilito -->
								<div class="control-group">
									<label for="Macroaggregato" class="control-label">Codifica
										Transazione Europea *</label>
									<div class="controls">
										<s:select list="teSupport.listaTransazioneEuropeaEntrata" headerKey=""
											headerValue="" id="listaTransEuropea" 
											disabled="disabilitaCampoTePerGestisciMovP() || !isAbilitatoAggiornamentoGenerico()" 
											name="teSupport.transazioneEuropeaSelezionato" cssClass="span10"
											listKey="codice" listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
							
							</s:else>
						
						</s:else>
						
						<!-- SIOPE OLD -->
						<!-- <div class="control-group">
							<label class="control-label">SIOPE</label>
							<div class="controls">
								<s:property value="teSupport.siopeSpesa.descrizione" />
								&nbsp;&nbsp;&nbsp; 
								 
								<s:if test="teSupport.oggettoAbilitaTE.equals('LIQUIDAZIONE') || teSupport.oggettoAbilitaTE.equals('ORDINATIVO_PAGAMENTO') || teSupport.oggettoAbilitaTE.equals('ORDINATIVO_INCASSO')">
								 -->
									<!-- non si deve vedere il btn -->
								<!--</s:if>
								<s:else>	
									<a href="#myModalSiopeSpesa" role="button"
										class="btn btn-primary" data-toggle="modal">seleziona SIOPE <i class="icon-spin icon-refresh spinner" id="spinnerElementoSiopeTE"></i></a>
								</s:else>
									
								<div id="myModalSiopeSpesa" class="modal hide fade"
									tabindex="-1" role="dialog"
									aria-labelledby="myModalSiopeSpesaLabel" aria-hidden="true">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"
											aria-hidden="true" onclick="resetLastTreeNode()">x</button>
										<h3 id="myModalSiopeSpesaLabel">SIOPE</h3>
									</div>
									-->
									
									<!-- ALBERO VISUALIZZATO -->
									<!--<div class="modal-body" id="siopeTransazioneElementareDiv">
										<ul id="siopeTransazioneElementare" class="ztree"></ul>
									</div>
									-->
									<!-- ALBERO IN ATTESA -->
									<!--<div class="modal-body" id="siopeTransazioneElementareWait">
										Attendere prego..
									</div>
									<div class="modal-footer">
										<s:submit value="conferma" name="confermaSiope"
											method="confermaSiope" cssClass="btn btn-primary"
											data-dismiss="modal" aria-hidden="true"></s:submit>
									</div>
								</div>
							</div>
						</div> -->
						
						
						
						<!-- SIOPE NUOVO -->
						
						<s:if test="presenzaOrdinativiPerLaLiquidazione || liquidazioneConAzioneImpegnoDecentrato">
						        <!--  DISABILITO -->
						        <div class="control-group" >
								      <label class="control-label" for="siopenew">SIOPE *</label>
								      
								      <div class="controls">
								      
									       <s:textfield id="siopenew" name="teSupport.siopeSpesaCod" cssClass="lbTextSmall span3" disabled="true"></s:textfield>
									       
									       <s:include value="/jsp/movgest/include/labelSiope.jsp" />
								       
						    	 	  </div>
						    	 </div>
						</s:if>
						<s:elseif test="teSupport.oggettoAbilitaTE.equals('IMPEGNO') || teSupport.oggettoAbilitaTE.equals('ACCERTAMENTO')">
							<%-- SIAC-5288  IMPEGNO E ACCERTAMENTO disabled dipende da isAbilitatoAggiornamentoGenerico--%>
								<div class="control-group" >
								      <label class="control-label" for="siopenew">SIOPE *</label>
								      
								      <div class="controls">
								      
									       <s:textfield id="siopenew" name="teSupport.siopeSpesaCod" cssClass="lbTextSmall span3" 
									       disabled="!isAbilitatoAggiornamentoGenerico()"></s:textfield>
									       
									       <s:include value="/jsp/movgest/include/labelSiope.jsp" />
								       
						    	 	  </div>
								 </div>
						</s:elseif>
						<s:else>
								<!--  ABILITATO -->
								<div class="control-group" >
								      <label class="control-label" for="siopenew">SIOPE *</label>
								      
								      <div class="controls">
								      
									       <s:textfield id="siopenew" name="teSupport.siopeSpesaCod" cssClass="lbTextSmall span3" ></s:textfield>
									       
									       <s:include value="/jsp/movgest/include/labelSiope.jsp" />
								       
						    	 	  </div>
								 </div>
						</s:else>
						
						
						<!-- CUP -->
						<s:if test="cupAttivo()">
							<div class="control-group">
								<label for="CUP" class="control-label">CUP</label>
								<div class="controls">
									<s:textfield name="teSupport.cup" id="cup" readonly="true" />
								</div>
							</div>
						</s:if>
						
						<!-- RICORRENTE -->
						<s:if test="datiUscitaImpegno()">
							<div class="control-group">
								
								<s:if test="teSupport.oggettoAbilitaTE.equals('LIQUIDAZIONE') || teSupport.oggettoAbilitaTE.equals('ORDINATIVO_PAGAMENTO')">
									<label for="Ricorrente" class="control-label">Ricorrente</label>								
									<div class="controls">
										<s:select list="teSupport.listaRicorrenteSpesa" id="listaRicorrenteSpesa"
											headerKey="" headerValue="" name="teSupport.ricorrenteSpesaSelezionato"
											cssClass="span10" listKey="codice"
											listValue="%{codice + ' - ' + descrizione}" disabled="true"/>
											
										<s:hidden value="teSupport.ricorrenteSpesaSelezionato"></s:hidden>	
									</div>
								</s:if>
								<s:else>
								    <label for="Ricorrente" class="control-label">Ricorrente *</label>
									<div class="controls">
										<s:select list="teSupport.listaRicorrenteSpesa" id="listaRicorrenteSpesa"
											headerKey="" headerValue="" name="teSupport.ricorrenteSpesaSelezionato"
											cssClass="span10" listKey="codice" 
											disabled="disabilitaCampoTePerGestisciMovP() || !isAbilitatoAggiornamentoGenerico()" 
											listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</s:else>								
							</div>
						</s:if>
						<s:else>
							<div class="control-group">
							    <s:if test="teSupport.oggettoAbilitaTE.equals('ORDINATIVO_INCASSO')">
							    
	
										<label for="Ricorrente" class="control-label">Ricorrente *</label>
										<div class="controls">
											<s:select list="teSupport.listaRicorrenteEntrata" id="listaRicorrenteEntrata"
												headerKey="" headerValue="" name="teSupport.ricorrenteEntrataSelezionato"
												cssClass="span10" listKey="codice" disabled="true"
												listValue="%{codice + ' - ' + descrizione}" />
									</div>
									<s:hidden value="teSupport.ricorrenteEntrataSelezionat" />
							    
							    </s:if>
							    <s:else>
										<label for="Ricorrente" class="control-label">Ricorrente *</label>
										<div class="controls">
											<s:select list="teSupport.listaRicorrenteEntrata" id="listaRicorrenteEntrata"
												headerKey="" headerValue="" name="teSupport.ricorrenteEntrataSelezionato"
												cssClass="span10" listKey="codice" 
												disabled="disabilitaCampoTePerGestisciMovP() || !isAbilitatoAggiornamentoGenerico()" 
												listValue="%{codice + ' - ' + descrizione}" />
									</div>
								
							    </s:else>
							</div>
						</s:else>
						
						<!-- PERIMETRO SANITARIO -->
						<s:if test="datiUscitaImpegno()">
						    
							<s:if test="teSupport.oggettoAbilitaTE.equals('ORDINATIVO_PAGAMENTO') || presenzaOrdinativiPerLaLiquidazione || liquidazioneConAzioneImpegnoDecentrato">
							    <!-- DISABILITO -->
								<div class="control-group">
									<label for="ASL" class="control-label">Capitoli perimetro
										sanitario</label>
										<s:if test="ObbligatorioPerimetroSanitario()">*</s:if>
									<div class="controls">
										<s:select list="teSupport.listaPerimetroSanitarioSpesa"
											id="listaPerimetroSanitarioSpesa" disabled="true"
											name="teSupport.perimetroSanitarioSpesaSelezionato" cssClass="span10"
											headerKey="" headerValue="" listKey="codice"
											listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
								<s:hidden value="teSupport.perimetroSanitarioSpesaSelezionato"/>
								
							</s:if>
							<s:else>
								
								  	<!-- abilito -->
								  	<div class="control-group">
											<label for="ASL" class="control-label">Capitoli perimetro
												sanitario</label>
												<s:if test="obbligatorioPerimetroSanitario">*</s:if>
											<div class="controls">
												<s:select list="teSupport.listaPerimetroSanitarioSpesa"
													id="listaPerimetroSanitarioSpesa" 
													disabled="disabilitaCampoTePerGestisciMovP() || !isAbilitatoAggiornamentoGenerico()" 
													name="teSupport.perimetroSanitarioSpesaSelezionato" cssClass="span10"
													headerKey="" headerValue="" listKey="codice"
													listValue="%{codice + ' - ' + descrizione}" />
											</div>
										</div>
								  	
							</s:else>	
						</s:if>
						<s:else>
							<s:if test="teSupport.oggettoAbilitaTE.equals('ORDINATIVO_INCASSO')">
							
							   <div class="control-group">
									<label for="ASL" class="control-label">Capitoli perimetro
										sanitario *</label>
									<div class="controls">
										<s:select list="teSupport.listaPerimetroSanitarioEntrata"
											id="listaPerimetroSanitarioEntrata" disabled="true"
											name="teSupport.perimetroSanitarioEntrataSelezionato" cssClass="span10"
											headerKey="" headerValue="" listKey="codice"
											listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
								<s:hidden value="teSupport.perimetroSanitarioEntrataSelezionato" />
							
							</s:if>
							<s:else>
								<div class="control-group">
									<label for="ASL" class="control-label">Capitoli perimetro
										sanitario *</label>
									<div class="controls">
										<s:select list="teSupport.listaPerimetroSanitarioEntrata"
											id="listaPerimetroSanitarioEntrata" 
											disabled="disabilitaCampoTePerGestisciMovP() || !isAbilitatoAggiornamentoGenerico()" 
											name="teSupport.perimetroSanitarioEntrataSelezionato" cssClass="span10"
											headerKey="" headerValue="" listKey="codice"
											listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
							</s:else>
							
						</s:else>
						<!-- PROGRAMMA POLITICO REGIONALE UNITARIO -->
						<s:if test="programmaPoliticoRegionaleUnitarioAttivo()">
							<s:if test="teSupport.oggettoAbilitaTE.equals('ORDINATIVO_PAGAMENTO') || presenzaOrdinativiPerLaLiquidazione || liquidazioneConAzioneImpegnoDecentrato">
								<div class="control-group">
									<label for="Unitaria" class="control-label">Programma
										Pol. Reg. Unitaria</label>
									<div class="controls">
										<s:select list="teSupport.listaPoliticheRegionaliUnitarie"
											id="listaPoliticheRegionaliUnitarie" disabled="true"
											name="teSupport.politicaRegionaleSelezionato" cssClass="span10"
											headerKey="" headerValue="" listKey="codice"
											listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
								<s:hidden value="teSupport.politicaRegionaleSelezionato"/>
							</s:if>
							<s:else>
								<div class="control-group">
										<label for="Unitaria" class="control-label">Programma
											Pol. Reg. Unitaria</label>
										<div class="controls">
											<s:select list="teSupport.listaPoliticheRegionaliUnitarie"
												id="listaPoliticheRegionaliUnitarie" 
												disabled="disabilitaCampoTePerGestisciMovP() || !isAbilitatoAggiornamentoGenerico()" 
												name="teSupport.politicaRegionaleSelezionato" cssClass="span10"
												headerKey="" headerValue="" listKey="codice"
												listValue="%{codice + ' - ' + descrizione}" />
										</div>
									</div>
							</s:else>
						</s:if>
					</div>
					<!-- PAOLO PROVA <s:property value="teSupport.oggettoAbilitaTE"/> -->
				</div>
			</div>
		</div>
	</s:if>
</s:if>	
	<s:if test="altriClassificatoriAttivi()">
		<!-- ALTRI CLASSIFICATORI -->
		<div class="accordion" id="soggetto2">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a class="accordion-toggle collapsed" data-toggle="collapse"
						data-parent="#soggetto2" href="#2">Altri classificatori<span
						class="icon">&nbsp;</span></a>
				</div>
				<div id="2" class="accordion-body collapse">
					<div class="accordion-inner">
					<!--  CLASSIFICATORI IMPEGNO E LIQUIDAZIONE -->
					<s:if test="classificatoriImpegnoLiquidazione()">
							<s:if
								test="teSupport.listaClassificatoriGen11!=null && teSupport.listaClassificatoriGen11.size()>0">
								<div class="control-group">
									<s:iterator value="teSupport.listaClassificatoriGen11" status="statGen11">
										<s:if test="#statGen11.first == true">
											<label for="allegati" class="control-label"><s:property
													value="tipoClassificatore.descrizione" /></label>
										</s:if>
									</s:iterator>
									<div class="controls">
										<s:select list="teSupport.listaClassificatoriGen11"
											id="listaClassificatoriGen11" name="teSupport.classGenSelezionato1"
											headerKey="" headerValue="" listKey="codice"
											listValue="descrizione" cssClass="span9" />
									</div>
								</div>
							</s:if>
							
							<s:if
								test="teSupport.listaClassificatoriGen12!=null && teSupport.listaClassificatoriGen12.size()>0">
								<div class="control-group">
									<s:iterator value="teSupport.listaClassificatoriGen12" status="statGen12">
										<s:if test="#statGen12.first == true">
											<label for="allegati" class="control-label"><s:property
													value="tipoClassificatore.descrizione" /></label>
										</s:if>
									</s:iterator>
									<div class="controls">
										<s:select list="teSupport.listaClassificatoriGen12"
											id="listaClassificatoriGen12" name="teSupport.classGenSelezionato2"
											headerKey="" headerValue="" listKey="codice"
											listValue="descrizione" cssClass="span9" />
									</div>
								</div>
							</s:if>
							
							<s:if
								test="teSupport.listaClassificatoriGen13!=null && teSupport.listaClassificatoriGen13.size()>0">
								<div class="control-group">
									<s:iterator value="teSupport.listaClassificatoriGen13" status="statGen13">
										<s:if test="#statGen13.first == true">
											<label for="allegati" class="control-label"><s:property
													value="tipoClassificatore.descrizione" /></label>
										</s:if>
									</s:iterator>
									<div class="controls input-append">
										<s:select list="teSupport.listaClassificatoriGen13"
											id="listaClassificatoriGen13" name="teSupport.classGenSelezionato3"
											headerKey="" headerValue="" listKey="codice"
											listValue="descrizione" cssClass="span9" />
									</div>
								</div>
							</s:if>
							
							<s:if
								test="teSupport.listaClassificatoriGen14!=null && teSupport.listaClassificatoriGen14.size()>0">
								<div class="control-group">
									<s:iterator value="teSupport.listaClassificatoriGen14" status="statGen14">
										<s:if test="#statGen14.first == true">
											<label for="allegati" class="control-label"><s:property
													value="tipoClassificatore.descrizione" /></label>
										</s:if>
									</s:iterator>
									<div class="controls input-append">
										<s:select list="teSupport.listaClassificatoriGen14"
											id="listaClassificatoriGen14" name="teSupport.classGenSelezionato4"
											headerKey="" headerValue="" listKey="codice"
											listValue="descrizione" cssClass="span9" />
									</div>
								</div>
							</s:if>
							
							<s:if
								test="teSupport.listaClassificatoriGen15!=null && teSupport.listaClassificatoriGen15.size()>0">
								<div class="control-group">
									<s:iterator value="teSupport.listaClassificatoriGen15" status="statGen15">
										<s:if test="#statGen15.first == true">
											<label for="allegati" class="control-label"><s:property
													value="tipoClassificatore.descrizione" /></label>
										</s:if>
									</s:iterator>
									<div class="controls input-append">
										<s:select list="teSupport.listaClassificatoriGen15"
											id="listaClassificatoriGen15" name="teSupport.classGenSelezionato5"
											headerKey="" headerValue="" listKey="codice"
											listValue="descrizione" cssClass="span9" />
									</div>
								</div>
							</s:if>
						
						</s:if>
						  
						<s:else>
						    <!--  ACCERTAMENTO -->
							<s:if test="teSupport.listaClassificatoriGen16!=null && teSupport.listaClassificatoriGen16.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen16" status="statGen16">
									<s:if test="#statGen16.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls">
									<s:select list="teSupport.listaClassificatoriGen16"
										id="listaClassificatoriGen16" name="teSupport.classGenSelezionato6"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
					
						<s:if test="teSupport.listaClassificatoriGen17!=null && teSupport.listaClassificatoriGen17.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen17" status="statGen17">
									<s:if test="#statGen17.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls">
									<s:select list="teSupport.listaClassificatoriGen17"
										id="listaClassificatoriGen17" name="teSupport.classGenSelezionato7"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
						
						<s:if
							test="teSupport.listaClassificatoriGen18!=null && teSupport.listaClassificatoriGen18.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen18" status="statGen18">
									<s:if test="#statGen18.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls input-append">
									<s:select list="teSupport.listaClassificatoriGen18"
										id="listaClassificatoriGen18" name="teSupport.classGenSelezionato8"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
						
						<s:if
							test="teSupport.listaClassificatoriGen19!=null && teSupport.listaClassificatoriGen19.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen19" status="statGen19">
									<s:if test="#statGen19.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls input-append">
									<s:select list="teSupport.listaClassificatoriGen19"
										id="listaClassificatoriGen19" name="teSupport.classGenSelezionato9"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
						
						<s:if
							test="teSupport.listaClassificatoriGen20!=null && teSupport.listaClassificatoriGen20.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen20" status="statGen20">
									<s:if test="#statGen20.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls input-append">
									<s:select list="teSupport.listaClassificatoriGen20"
										id="listaClassificatoriGen20" name="teSupport.classGenSelezionato10"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
						
						
						</s:else>
					</div>
					
				</div>
			</div>
		</div>
	</s:if>
</fieldset>


<script type="text/javascript">

$("#siopenew").change(function(){
	var cod = $("#siopenew").val();
	//Carico i dati in tabella "Modalita' di pagamento"		
	$.ajax({
		//task-131 url: '<s:url method="codiceSiopeChanged"></s:url>',
		url: '<s:url action="%{#codiceSiopeChangedAction}"></s:url>',
		type: "GET",
		data: $(".hiddenGestoreToggle").serialize() + "&id=" + cod, 
	    success: function(data)  {
	    	$("#refreshDescrizioneSiope").html(data);
		}
	});			
});

</script>
