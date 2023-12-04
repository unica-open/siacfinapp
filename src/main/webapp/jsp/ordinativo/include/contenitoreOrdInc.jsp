<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

<!--  ORDINATIVO INCASSO  -->

<div class="Border_line"></div>
	 <s:if test="!sonoInAggiornamentoIncasso()"> 
	 
				<fieldset class="form-horizontal">
						<div class="accordion" id="quotaIncasso1">
							<div class="accordion-group">
								<div class="accordion-heading">
									<a class="accordion-toggle" data-toggle="collapse"
										data-parent="#quotaIncasso1" href="#1_quota">inserisci quota<span
										class="icon">&nbsp;</span></a>
								</div>
								<div id="1_quota" class="accordion-body collapse in">
									<div class="accordion-inner">
										<div class="control-group">
																
				
											<s:include value="/jsp/ordinativo/include/nuovaQuotaIncasso.jsp" />
											
											<p>
												<a class="btn btn-secondary" onclick="pulisciCampiQuota()"  data-toggle="collapse" data-target="#insQuota" >annulla inserimento</a>    
												<span class="pull-right">
													<!-- task-131 <s:submit id="inserisciQuota" cssClass="btn btn-primary" method="inserisciQuota" value="inserisci quota" name="inserisci quota" /> -->
													<s:submit id="inserisciQuota" cssClass="btn btn-primary" action="%{#inserisciQuotaAction}" value="inserisci quota" name="inserisci quota" />	
												</span> 
											</p>
																
										</div>
									</div>
								</div>
							</div>
						</div>
				</fieldset>
	 
	 		
			<div id="refreshTE">     	

			<s:hidden id="idPianoDeiContiCapitolo" name="teSupport.pianoDeiConti.uid"/>
			<s:hidden id="ricaricaAlberoPianoDeiConti" name="teSupport.ricaricaAlberoPianoDeiConti"/>
			<s:include value="/jsp/include/transazioneElementare.jsp" />
				
			
			</div>
	</s:if>
