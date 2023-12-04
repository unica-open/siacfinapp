<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<div class="control-group"> 
		
		    
		
			<label class="control-label">
				<a href="#" data-trigger="hover" rel="popover" title="" 
				   data-original-title="Avviso richiesto" data-content="${gestioneOrdinativoStep1Model.avvisoFrase}">Avviso</a>
		    </label>						
			<div class="controls">
			
			<s:if test="oggettoDaPopolarePagamento()"> 
				<!-- ordinativo pagamento -->
				<s:if test="null!=gestioneOrdinativoStep1Model.listaAvviso">
					<s:if test="gestioneOrdinativoStep1Model.listaAvviso.size() == 1">
						<s:select list="gestioneOrdinativoStep1Model.listaAvviso" id="listaAvvisi" 
									   name="gestioneOrdinativoStep1Model.ordinativo.tipoAvviso.codice" cssClass="span3" 
					           		   listKey="codice" listValue="codice+' - '+descrizione" />
					</s:if>
					<s:else>
						<s:select list="gestioneOrdinativoStep1Model.listaAvviso" id="listaAvvisi" headerKey="" 
				          		   headerValue="" name="gestioneOrdinativoStep1Model.ordinativo.tipoAvviso.codice" cssClass="span3" 
				           		   listKey="codice" listValue="codice+' - '+descrizione" />
			        </s:else>   		   
		        </s:if>   		    
	        </s:if>
	        <s:else>
	           <!-- ordinativo incasso -->
	        
	           <s:if test="gestioneOrdinativoStep1Model.avvisoFrase==null || gestioneOrdinativoStep1Model.avvisoFrase==''">
	                <s:if test="null!=gestioneOrdinativoStep1Model.listaAvviso">
						<s:if test="gestioneOrdinativoStep1Model.listaAvviso.size() == 1">
							<s:select list="gestioneOrdinativoStep1Model.listaAvviso" id="listaAvvisi" 
										   name="gestioneOrdinativoStep1Model.ordinativo.tipoAvviso.codice" cssClass="span3" 
						           		   listKey="codice" listValue="codice+' - '+descrizione" />
						</s:if>
						<s:else>
							<s:select list="gestioneOrdinativoStep1Model.listaAvviso" id="listaAvvisi" headerKey="" 
					          		   headerValue="" name="gestioneOrdinativoStep1Model.ordinativo.tipoAvviso.codice" cssClass="span3" 
					           		   listKey="codice" listValue="codice+' - '+descrizione" />
				        </s:else>   		   
		        	</s:if>   		    
	           </s:if>
	           <s:else>
	           			<s:if test="gestioneOrdinativoStep1Model.avvisoFrase=='SI'">
		                    <s:if test="null!=gestioneOrdinativoStep1Model.listaAvviso">
								<s:if test="gestioneOrdinativoStep1Model.listaAvviso.size() == 1">
									<s:select list="gestioneOrdinativoStep1Model.listaAvviso" id="listaAvvisi" 
												   name="gestioneOrdinativoStep1Model.ordinativo.tipoAvviso.codice" cssClass="span3" 
								           		   listKey="codice" listValue="codice+' - '+descrizione" />
								</s:if>
								<s:else>
									<s:select list="gestioneOrdinativoStep1Model.listaAvviso" id="listaAvvisi" headerKey="" 
							          		   headerValue="" name="gestioneOrdinativoStep1Model.ordinativo.tipoAvviso.codice" cssClass="span3" 
							           		   listKey="codice" listValue="codice+' - '+descrizione" />
						        </s:else>   		   
					        </s:if>   	
		                </s:if>
	                    <s:else>
	                       <!-- disabilito select -->
	                       <s:if test="null!=gestioneOrdinativoStep1Model.listaAvviso">
								<s:if test="gestioneOrdinativoStep1Model.listaAvviso.size() == 1">
									<s:select list="gestioneOrdinativoStep1Model.listaAvviso" id="listaAvvisi" 
												   name="gestioneOrdinativoStep1Model.ordinativo.tipoAvviso.codice" cssClass="span3" 
								           		   listKey="codice" listValue="codice+' - '+descrizione" disabled="true" />
								</s:if>
								<s:else>
									<s:select list="gestioneOrdinativoStep1Model.listaAvviso" id="listaAvvisi" headerKey="" 
							          		   headerValue="" name="gestioneOrdinativoStep1Model.ordinativo.tipoAvviso.codice" cssClass="span3" 
							           		   listKey="codice" listValue="codice+' - '+descrizione"  disabled="true" />
						        </s:else>   		   
					        </s:if>   	
	                       
	                       
	                    </s:else>
	           </s:else>
	           
	        
	        </s:else>
	
			</div>
		</div>
	<script type="text/javascript">

		$(document).ready(function() {	
			$("a[rel=popover]").popover();
		});
	
	</script>  	