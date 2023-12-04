<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<p>Capitolo</p>
	                              <ul  class="htmlelt">
	                              
	                              <li>
	                                 <dfn>Capitolo</dfn>
									<dl><s:property value="movimento.capitolo.anno" /> / <s:property value="movimento.capitolo.numCapitolo" /> / <s:property value="movimento.capitolo.articolo" /> / <s:property value="movimento.capitolo.ueb" />&nbsp;</dl>
							   	  </li>
							   	  <li>
	                                <dl></dl>
									<dl><s:property value="movimento.capitolo.descrizione" />&nbsp;</dl>
							   	  </li>
							   	  <li>
	                                <dfn>Tipo finanziamento</dfn>
									<dl><s:property value="movimento.capitolo.tipoFinanziamento" />&nbsp;</dl>
							   	  </li>
							   	  <!-- SIAC-7349 gestione componenti su impegni -->
									<s:if test="movimento.impegno"> 
										<li> 
											<dfn>Componente</dfn>
											<dl><s:property value="%{movimento.descrizioneComponente}" />&nbsp;</dl>
										</li>
									</s:if>
                             		<!--  DISPONIBILITA -->
	                             
	                             <!--  
	                              
	                              <li>
	                                <dl></dl>
									<dl><b><i>Stanziamento - Disponibile</i></b></dl>
							   	  </li>
	                              
	                              <s:iterator value="movimento.capitolo.importiCapitolo" status="itStatus" >
	                               <li>
	                               	 <dfn>Competenza <s:property value="annoCompetenza" /></dfn>
	                               	 <dl><s:property value="getText('struts.money.format', {competenza})" />  -  
	                               	 
	                               	 <s:if test="oggettoDaPopolareImpegno()">
		                               	 <s:if test="#itStatus.count==1">
		                               		 <s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno1})" />
		                               	 </s:if>
		                               	 <s:elseif test="#itStatus.count==2">
		                               		 <s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno2})" />
		                               	 </s:elseif>
		                               	 <s:elseif test="#itStatus.count==3">
		                               		 <s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno3})" />
		                               	 </s:elseif>
	                               	 </s:if>
	                               	 <s:else>
		                               	 <s:if test="#itStatus.count==1">
		                               		 <s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno1})" />
		                               	 </s:if>
		                               	 <s:elseif test="#itStatus.count==2">
		                               		 <s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno2})" />
		                               	 </s:elseif>
		                               	 <s:elseif test="#itStatus.count==3">
		                               		 <s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno3})" />
		                               	 </s:elseif>
	                               	 </s:else>
	                               	 
	                               	 </dl>
	                               </li>
	                              
	                              </s:iterator>
	                              
	                              -->
	                              </ul>
	                              
	                              
	                                  <table>
									      <tr>
									        	<th>&nbsp;</th>
												<th scope="col" class="text-center">Stanziamento</th>
												<th scope="col" class="text-center">Disponibile</th>
									      </tr>
									      
									      <s:iterator value="movimento.capitolo.importiCapitolo" status="itStatus" >
									      
									      <tr>
									        <th>Competenza <s:property value="annoCompetenza" /></th>
									        
									        <td><s:property value="getText('struts.money.format', {competenza})" /></td>
									        
									        <td>
									        	<s:if test="oggettoDaPopolareImpegno()">
					                               	 <s:if test="#itStatus.count==1">
					                               		 <s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno1})" />
					                               	 </s:if>
					                               	 <s:elseif test="#itStatus.count==2">
					                               		 <s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno2})" />
					                               	 </s:elseif>
					                               	 <s:elseif test="#itStatus.count==3">
					                               		 <s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloUG[0].disponibilitaImpegnareAnno3})" />
					                               	 </s:elseif>
				                               	 </s:if>
				                               	 <s:else>
					                               	 <!-- accertamento -->
					                               	 <s:if test="#itStatus.count==1">
					                               		 <s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno1})" />
					                               	 </s:if>
					                               	 <s:elseif test="#itStatus.count==2">
					                               		 <s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno2})" />
					                               	 </s:elseif>
					                               	 <s:elseif test="#itStatus.count==3">
					                               		 <s:property value="getText('struts.money.format', {movimento.capitolo.importiCapitoloEG[0].disponibilitaAccertareAnno3})" />
					                               	 </s:elseif>
				                               	 </s:else>
	                               	 	   </td>
									              
									      </tr>
									      
									      </s:iterator>
									      
									    </table>
	                              
	                              
	                              
	                              
	                              
