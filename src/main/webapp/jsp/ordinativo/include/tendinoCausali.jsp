<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
	       

	  
	 <div class="control-group">
				<label class="control-label" for="caus">Causale
				</label>
				<div class="controls">
				  <s:if test="null!=gestioneOrdinativoStep1Model.causaleEntrataTendino.listaCausali">
			 		  <s:select list="gestioneOrdinativoStep1Model.causaleEntrataTendino.listaCausali" id="listaCausali" headerKey="" headerValue="" 
			           		   name="gestioneOrdinativoStep1Model.causaleEntrataTendino.codiceCausale" cssClass="span9" 
			           		   listKey="codice" listValue="codice+' - '+descrizione" disabled="!abilitaCausale()" />
		          </s:if> 	
		         </div>
	</div>
