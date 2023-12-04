<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<!-- ALTRI CLASSIFICATORI -->
<s:if test="oggettoDaPopolarePagamento()">
		<div class="accordion" id="soggetto2">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a class="accordion-toggle collapsed" data-toggle="collapse"
						data-parent="#soggetto2" href="#2">Altri classificatori<span
						class="icon">&nbsp;</span></a>
				</div>
				<div id="2" class="accordion-body collapse">
					<div class="accordion-inner">
						<s:if
							test="teSupport.listaClassificatoriGen21!=null && teSupport.listaClassificatoriGen21.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen21" status="statGen11">
									<s:if test="#statGen11.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls">
									<s:select list="teSupport.listaClassificatoriGen21"
										id="listaClassificatoriGen11" name="teSupport.classGenSelezionato1"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
						
						<s:if
							test="teSupport.listaClassificatoriGen22!=null && teSupport.listaClassificatoriGen22.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen22" status="statGen12">
									<s:if test="#statGen12.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls">
									<s:select list="teSupport.listaClassificatoriGen22"
										id="listaClassificatoriGen22" name="teSupport.classGenSelezionato2"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
						
						<s:if
							test="teSupport.listaClassificatoriGen23!=null && teSupport.listaClassificatoriGen23.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen23" status="statGen13">
									<s:if test="#statGen13.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls input-append">
									<s:select list="teSupport.listaClassificatoriGen23"
										id="listaClassificatoriGen23" name="teSupport.classGenSelezionato3"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
						
						<s:if
							test="teSupport.listaClassificatoriGen24!=null && teSupport.listaClassificatoriGen24.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen24" status="statGen14">
									<s:if test="#statGen14.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls input-append">
									<s:select list="teSupport.listaClassificatoriGen24"
										id="listaClassificatoriGen24" name="teSupport.classGenSelezionato4"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
						
						<s:if
							test="teSupport.listaClassificatoriGen25!=null && teSupport.listaClassificatoriGen25.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen25" status="statGen15">
									<s:if test="#statGen15.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls input-append">
									<s:select list="teSupport.listaClassificatoriGen25"
										id="listaClassificatoriGen15" name="teSupport.classGenSelezionato5"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
						
						<s:if
							test="teSupport.listaClassificatoriStipendi!=null && teSupport.listaClassificatoriStipendi.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriStipendi" status="statSti">
									<s:if test="#statSti.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls input-append">
									<s:select list="teSupport.listaClassificatoriStipendi"
										id="listaClassificatoriStipendi" name="teSupport.uidClassStipendiSelezionato"
										headerKey="0" headerValue="" listKey="uid"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>	
						
					</div>
				</div>
			</div>
		</div>
</s:if>
	<s:else>
	
	
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
						<s:if
							test="teSupport.listaClassificatoriGen26!=null && teSupport.listaClassificatoriGen26.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen26" status="statGen11">
									<s:if test="#statGen11.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls">
									<s:select list="teSupport.listaClassificatoriGen26"
										id="listaClassificatoriGen11" name="teSupport.classGenSelezionato1"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
						
						<s:if
							test="teSupport.listaClassificatoriGen27!=null && teSupport.listaClassificatoriGen27.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen27" status="statGen12">
									<s:if test="#statGen12.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls">
									<s:select list="teSupport.listaClassificatoriGen27"
										id="listaClassificatoriGen12" name="teSupport.classGenSelezionato2"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
						
						<s:if
							test="teSupport.listaClassificatoriGen28!=null && teSupport.listaClassificatoriGen28.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen28" status="statGen13">
									<s:if test="#statGen13.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls input-append">
									<s:select list="teSupport.listaClassificatoriGen28"
										id="listaClassificatoriGen13" name="teSupport.classGenSelezionato3"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
						
						<s:if
							test="teSupport.listaClassificatoriGen29!=null && teSupport.listaClassificatoriGen29.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen29" status="statGen14">
									<s:if test="#statGen14.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls input-append">
									<s:select list="teSupport.listaClassificatoriGen29"
										id="listaClassificatoriGen14" name="teSupport.classGenSelezionato4"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
						
						<s:if
							test="teSupport.listaClassificatoriGen30!=null && teSupport.listaClassificatoriGen30.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriGen30" status="statGen15">
									<s:if test="#statGen15.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls input-append">
									<s:select list="teSupport.listaClassificatoriGen30"
										id="listaClassificatoriGen15" name="teSupport.classGenSelezionato5"
										headerKey="" headerValue="" listKey="codice"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>
						
						<s:if
							test="teSupport.listaClassificatoriStipendi!=null && teSupport.listaClassificatoriStipendi.size()>0">
							<div class="control-group">
								<s:iterator value="teSupport.listaClassificatoriStipendi" status="statSti">
									<s:if test="#statSti.first == true">
										<label for="allegati" class="control-label"><s:property
												value="tipoClassificatore.descrizione" /></label>
									</s:if>
								</s:iterator>
								<div class="controls input-append">
									<s:select list="teSupport.listaClassificatoriStipendi"
										id="listaClassificatoriStipendi" name="teSupport.uidClassStipendiSelezionato"
										headerKey="0" headerValue="" listKey="uid"
										listValue="descrizione" cssClass="span9" />
								</div>
							</div>
						</s:if>	
						
					</div>
					
					<div class="control-group">
						<label class="control-label" for="tipocaus">Tipo causale
						</label>
						<div class="controls">
							<s:if test="null!=gestioneOrdinativoStep1Model.causaleEntrataTendino.listaTipiCausale">
					 		  <s:select list="gestioneOrdinativoStep1Model.causaleEntrataTendino.listaTipiCausale" id="listaTipiCausale" headerKey="" headerValue=""   
					           		   name="gestioneOrdinativoStep1Model.causaleEntrataTendino.codiceTipoCausale" cssClass="span9" 
					           		   listKey="codice" listValue="codice+' - '+descrizione" disabled="!abilitaCausale()" />
							</s:if> 
				        </div>
					</div>
					 <div id="refreshTendinoCausali" style="display: inline">
						<s:include value="/jsp/ordinativo/include/tendinoCausali.jsp" />
					</div>
					
				</div>
			</div>
		</div>
	
	</s:else>
	
	