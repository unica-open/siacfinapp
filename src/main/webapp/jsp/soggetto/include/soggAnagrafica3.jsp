<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>


                
      <h4>Classificazioni</h4>  
    <fieldset class="form-horizontal">
         	<div class="control-group">
                <label class="control-label" for="classif">Tipo classificazione</label>
				 <div class="controls">
                	 <!--<select id="classif" name="classif" class="selectpicker" multiple title='Scegli il tipo di classificazione'><option>&nbsp;</option><option>x0</option><option>x1</option><option>x2</option><option>x3</option><option>x4</option></select>
                	 -->
                	  <s:if test="null!=listaClasseSoggetto">
                	  <s:select list="listaClasseSoggetto" id="idClasseSoggetto" name="dettaglioSoggetto.tipoClassificazioneSoggettoId" 
 	                	      multiple="true" title="Scegli il tipo di classificazione"   
 	                	      listKey="codice" listValue="codice+' - '+descrizione" cssClass="multiSelectCustom"/>
 	                  </s:if>	       
 				</div>
          </div>
          </fieldset>
		  
		  
		  
           <h4>Oneri Fiscali e ritenute</h4>  
    <fieldset class="form-horizontal">
         	<div class="control-group">
                <label class="control-label" for="Oneri">Oneri</label>
				 <div class="controls">
                	<!--  <select id="Oneri" name="Oneri" class="selectpicker" multiple title='Scegli il tipo di onere'><option>&nbsp;</option><option>x0</option><option>x1</option><option>x2</option><option>x3</option><option>x4</option></select>
                	-->
                	<s:if test="null!=listaClasseSoggetto">
                	 <s:select list="listaTipoOnere" id="idTipoOnere" name="dettaglioSoggetto.tipoOnereId" 
 	                	      multiple="true" title="Scegli il tipo di onere"   
 	                	      listKey="codice" listValue="codice+' - '+descrizione"  cssClass="multiSelectCustom"/>
 	                </s:if>	       
 				</div>
          </div>
          </fieldset>	