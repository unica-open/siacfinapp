<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>


<%-- QUESTO E' IL BLOCCO A SCOMPARSA (collapse) per il collega impegno dentro a regolarizzazioneCarta.jsp --%>

<div id="collImpegno" class="collapse">
	<div class="accordion_info">

		<fieldset class="form-horizontal" id="insRegolarizzaTAB">   
			
			<s:include value="/jsp/carta/include/impegnoCarta.jsp" />
			
			<div class="control-group">
				<label class="control-label">Importo *</label>
				<div class="controls">
				<s:textfield id="importoImpegno" name="importoImpegno" onkeypress="return checkItNumbersCommaAndDotOnly(event)" maxlength="10" cssClass="span2 soloNumeri decimale" ></s:textfield>
				</div>
			</div>
			
			<div class="Border_line"></div>
			<p> 
				<!-- task-131 <s:submit cssClass="btn btn-primary pull-right" method="confermaCollegaImpegno" value="Conferma" name="Conferma" />-->
				<s:submit cssClass="btn btn-primary pull-right" action="regolazioneCarta_confermaCollegaImpegno" value="Conferma" name="Conferma" />
			</p>
		</fieldset>
		
	</div>
</div>