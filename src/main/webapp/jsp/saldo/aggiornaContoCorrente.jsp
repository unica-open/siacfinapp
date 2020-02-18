<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>

<%-- Inclusione head e CSS NUOVO --%>
<s:include value="/jsp/include/head.jsp" />

<%-- Inclusione JavaScript NUOVO --%>
<s:include value="/jsp/include/javascript.jsp" />
</head>
<body>
	<s:include value="/jsp/include/header.jsp" />
	<hr />
	<div class="container-fluid-banner">
		<a name="A-contenuti" title="A-contenuti"></a>
	</div>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
			
       			 <s:form  method="post" cssClass="form-horizontal" id="form">
       			 
			<s:include value="/jsp/include/actionMessagesErrors.jsp" />
					
					
					<h3>Aggiorna conto corrente</h3>


					
					
					<div class="step-content">
			<div class="step-pane active" id="step1">
			
			<fieldset class="form-horizontal margin-large">



				<div class="control-group">
					<label class="control-label" for="anno">Anno</label>

					<div class="controls">
						<s:property value="vociContoCorrente.anno"/>
					</div>
				</div>


				<div class="control-group">
					<label for="conto" class="control-label">Conto corrente</label>
					<div class="controls">
						<s:hidden name="vociContoCorrente.idClassifConto"/>
						<s:property value="vociContoCorrente.contoCorrente"/>
					</div>
				</div>

				
				<div class="control-group">
					<label class="control-label" for="anno">Saldo iniziale *</label>

					<div class="controls">
					<s:textfield id="saldoIniziale"  name="vociContoCorrente.saldoIniziale" cssClass="span3 importo"  />
					</div>
				</div>
	
	
	          <p class="margin-medium">
			  
 			  <s:submit cssClass="btn btn-primary pull-right" method="aggiornaSaldo" value="aggiorna" />  

		  </p>
		  
						<br/>
				<br/>
	
  <s:if test="vociContoCorrente.saldoIniziale != null">
				
	
	<h4>Inserimento addebito</h4>
	
					<div class="control-group">
					<div class="controls">
						<span class="al">
							<label for="data" class="radio inline">Data</label>
					<input type="text" name="addebitoContoCorrente.data" value="" id="data" 
							class="lbTextSmall span2 datepicker" placeholder="gg/mm/aaaa" tabindex="-1">	</span>
						
						<span class="al">
							<label for="spesa" class="radio inline">Spese</label>
						<input type="text" name="addebitoContoCorrente.importoSpesa" value="" id="spesa" 
							class="lbTextSmall span2 importo" tabindex="-1"></span>
						
						<span class="al">
							<label for="prelievo" class="radio inline">Prelievo</label>
					<input type="text" name="addebitoContoCorrente.importoPrelievo" value="" id="prelievo" 
							class="lbTextSmall span2 importo"   tabindex="-1">	</span>
						
			<s:hidden name="addebitoContoCorrente.idSaldo" value="%{vociContoCorrente.uid}"/>
						
					</div>
				</div>
	
	          <p class="margin-medium">
			  
 			  <s:submit cssClass="btn btn-primary pull-right" method="inserisciAddebito" value="inserisci" />  

		  </p>
		  
		   <br/>
				<br/>
	
	</s:if>
		  <s:if test="not vociContoCorrente.elencoAddebiti.empty">
				
	
		<h4>Aggiorna addebiti</h4>
	
	<table class="table table-hover tab_left"  summary="...." >
				<thead>
					<tr>
						<th>Data</th>
						<th>Spesa</th>
						<th>Prelievo</th>
						<th>&nbsp;</th>
					</tr>
				</thead>
				<tbody>
				
			<s:iterator value="vociContoCorrente.elencoAddebiti"  status="st" var="a">			
				
	<tr>
		<td><input type="text" name="vociContoCorrente.elencoAddebiti[<s:property value="#st.index" />].data" 
							value='<s:property value="vociContoCorrente.elencoAddebiti[#st.index].data" />' id="data_<s:property value="#st.index" />" 
							class="lbTextSmall span4 datepicker" placeholder="gg/mm/aaaa" tabindex="-1"></td>
							
		<td><input type="text" name="vociContoCorrente.elencoAddebiti[<s:property value="#st.index" />].importoSpesa" 
							 value='<s:property value="vociContoCorrente.elencoAddebiti[#st.index].importoSpesa" />' 
							class="lbTextSmall span4 importo"   tabindex="-1"></td>
							
		<td><input type="text" name="vociContoCorrente.elencoAddebiti[<s:property value="#st.index" />].importoPrelievo" 
							 value='<s:property value="vociContoCorrente.elencoAddebiti[#st.index].importoPrelievo" />' 
							class="lbTextSmall span4 importo"  tabindex="-1">
					
								<s:hidden name="vociContoCorrente.elencoAddebiti[%{#st.index}].uid"  />
							
								<s:hidden name="vociContoCorrente.elencoAddebiti[%{#st.index}].idSaldo" value="%{vociContoCorrente.uid}"/>
							
							</td>
							
		<td class="elimina tab_Right">	<a  class="btn" onclick="return confirm('Eliminare addebito del <s:property value="vociContoCorrente.elencoAddebiti[#st.index].data" />?')" href="<s:url 
				action="aggiornaContoCorrente" method="eliminaAddebito"><s:param name="pos" >
					<s:property value="#st.index" /></s:param>
		</s:url>">elimina<i class="icon-trash marginLeft1"></i></a> </td>							
		
	</tr>
				</s:iterator>
				

				</tbody>
				<tfoot>
				</tfoot>

			</table>
		
		          <p class="margin-medium">
			  
 			  <s:submit cssClass="btn btn-primary pull-right" method="aggiornaAddebiti" value="aggiorna" />  

		  </p>
		  
		<br>&nbsp;<br/>
			
	</s:if>
				
			</fieldset>  

		</div>
		</div>
			
          <p class="margin-medium">
			  <a class="btn btn-secondary" href="ricercaContoCorrente.do">indietro</a>   
			  

		  </p>
		  
		  
				</s:form>
			</div>
		</div>
	</div>
	
<script type="text/javascript">
$(document).ready(function() {

	$('#form').on('keyup keypress', function(e) {
		  var keyCode = e.keyCode || e.which;
		  if (keyCode === 13) { 
		    e.preventDefault();
		    return false;
		  }
	});

	





	var selected = false;
	
	$('input.importo').keypress(function(e) 
	{
		if (e.which < 32)
			return true;

	    var t = [$(this).val().slice(0, this.selectionStart), e.key, $(this).val().slice(this.selectionEnd)].join('');

	    if (t.match(/^([1-9]\d*|0)(\,|\,\d{0,2})?$/) != null) {
	     // $(this).val(t);
	      selected = false;
	      return true;
	    }

	    return false;

	  }).focus(function(e) {
	    $(this).val($(this).val().replace(/\./g, ''));
	    
	  }).blur(function(e) {
	    $(this).val($(this).val().replace(/\./g, ''));
	    
	    if ($(this).val().match(/^([1-9]\d*|0)(\,|\,\d{0,2})?$/) == null)
	      $(this).val('')
	    else
	      $(this).val($(this).val().replace(/[^\d\,]/g, '').replace(/^(\d+)\,?$/, '$1,00').replace(/(\,\d)$/, '$10').replace(/\B(?=(\d{3})+(?!\d))/g, '.'));
	      
	  }).select(function(e) {
	    selected = true;
	  });
});
	
</script>  

<s:include value="/jsp/include/footer.jsp" />