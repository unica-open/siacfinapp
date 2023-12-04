<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>


<fieldset class="form-horizontal">

	<s:if test="isEnteAbilitatoGestioneGsa()">
		<div class="accordion" id="sanita">
			<div class="accordion-group">
				<div class="accordion-heading">
					<a class="accordion-toggle collapsed" data-toggle="collapse"
						data-parent="#sanita" href="#3">Gestione sanit&agrave;<span
						class="icon">&nbsp;</span></a>
				</div>
				<div id="3" class="accordion-body collapse">
					<div class="accordion-inner">

						<!-- Rilevante Co.Ge. GSA -->
						<div class="control-group">
							<label for="rilevanteGsa" class="control-label">Rilevante Co.Ge. GSA</label>
							<div class="controls">
							
								 <s:if test="oggettoDaPopolareImpegno()"> 
								 		<s:radio name="step1Model.flagAttivaGsa" id="radioAttivaGsa"
									 cssClass="flagResidenza" list="step1Model.listflagAttivaGsa" disabled="!isAbilitatoAggiornaImpegnoGSA()"></s:radio>
								 </s:if>
								 <s:else>
								 	<s:radio name="step1Model.flagAttivaGsa" id="radioAttivaGsa"
									 cssClass="flagResidenza" list="step1Model.listflagAttivaGsa" disabled="!isAbilitatoAggiornaAccertamentoGSA()"></s:radio>
								 </s:else>
									
							</div>
						</div>

					</div>
					
				</div>
			</div>
		</div>
	</s:if>
</fieldset>