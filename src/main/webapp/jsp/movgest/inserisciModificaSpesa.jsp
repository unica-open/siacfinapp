<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>


    <%-- Inclusione head e CSS NUOVO --%>
    <s:include value="/jsp/include/head.jsp" />
    
    <%-- Inclusione JavaScript NUOVO --%>
    <s:include value="/jsp/include/javascript.jsp" />	
   	<s:include value="/jsp/include/javascriptTree.jsp" />
  </head>

  <body>
  	
  <s:include value="/jsp/include/header.jsp" />
  
<div class="container-fluid-banner">



<a name="A-contenuti" title="A-contenuti"></a>
</div>
<!--corpo pagina-->
<!--<p><a href="cruscotto.shtml" target="iframe_a">W3Schools.com</a></p>
<iframe src="siac_iframe.htm" name="iframe_a"width="98%" height="600px" frameborder="0"></iframe> -->


<div class="container-fluid">
  <div class="row-fluid">
    <div class="span12 contentPage">
       <h3>Impegno 2013/12300 - Acquisto scrivanie per Anagrafe - 100.000,00</h3>  
       	<s:include value="/jsp/movgest/include/tabAggImpegno.jsp" />
       	<s:set var="gestisciForwardAction" value="%{'inserisciMovSpesaImporto_gestisciForward'}" />
		<s:include value="/jsp/include/javascriptCheckModificheTabs.jsp" />
			
      <form class="form-horizontal">     
        <div class="alert alert-error">
          <button type="button" class="close" data-dismiss="alert">&times;</button>
          <strong>Attenzione!!</strong><br /> Messaggio di feedback negativo
        </div>
        <div class="alert alert-success">
          <button type="button" class="close" data-dismiss="alert">&times;</button>
          <strong>Attenzione!!</strong><br /> xxxxxxx
        </div>  
        <h4>Inserimento modifica </h4> 
        <div class="step-content">
          <div class="step-pane active" id="step1">
           <fieldset class="form-horizontal margin-large">
                    <div class="control-group">
                      <!--label class="control-label" for="subimpegno">Subimpegno</label-->
                      <label class="control-label" for="subimpegno">Abbina a</label>
                      <div class="controls">
                        <label class="radio inline">
                        <input type="radio" name="optAbbina" id="optionsRadios1" value="impegno" checked>
                        Impegno
                        </label>
                        <label class="radio inline">
                        <input type="radio" name="optAbbina" id="optionsRadios2" value="subimpegno">
                        Subimpegno
                        </label>         
                        <span class="campiSubimpegno">
                          <select name="subimpegnoNum" class="span1" id="subimpegnoNum"><option> </option><option>xx</option><option>aaa</option></select>&nbsp; 
                          <label class="checkbox inline" for="anche"><input name="anche" type="checkbox" value="" id="anche"> Anche impegno</label>
                        </span>
                      </div>
                    </div>
                    <div class="control-group datiVisibili1">              
                      <div class="controls">              
                        <dl class="dl-horizontal">
                          <dt>Importo</dt>
                          <dd>Minimo XXXX  - Massimo XXXX</dd>        
                        </dl>             
                      </div>
                    </div>
                    <div class="control-group datiVisibili2">
                      <div class="controls">             
                        <dl class="dl-horizontal">
                          <dt>Codice subimpegno</dt>
                          <dd>aaa</dd>
                          <dt>Importo</dt>
                          <dd>Minimo XXXX  - Massimo XXXX</dd>
                        </dl>             
                      </div>
                    </div>       
                    <div class="control-group">
                      <label class="control-label" for="Descrizione">Descrizione *</label>
                      <div class="controls">
                        <input id="Descrizione" name="Descrizione" class="span9" required="required" type="text"/>
                      </div>
                    </div>
                    
                    <div class="control-group">
                      <label class="control-label" for="motivo">Motivo </label>
                      <div class="controls">
                        <select id="motivo" name="motivo" class="span9"> <option></option><option value="">zzzzzz</option> <option id="pluriennale">xxxxxx</option></select>
                      </div>
                    </div>
                    <div class="control-group anniPluriennale">
                      <label class="control-label" for="motivo">Anni di pluriennale</label>
                      <div class="controls">
                        <input id="annipluri" name="annipluri" class="span1" type="text"/>
                      </div>
                    </div>
                    <div class="control-group">
                      <label class="control-label" for="importo">Importo </label>
                      <div class="controls">
                        <input id="importo" name="importo" class="span2"  type="text"/>
                      </div>
                    </div>
                  </fieldset>
                  <!--p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p-->			          
                  <!--#include virtual="include/provvedimento_modifiche.html" -->                  
                  <!--#include virtual="include/creditore.html" -->
            <!--#include virtual="include/modal.html" --> 
            <!-- Fine Modal --> 
                                   
            <br/> <br/>                                         
            <p>
<%--                 <s:submit name="indietro" value="indietro" method="indietro" cssClass="btn btn-secondary" />    --%>
               <a class="btn btn-secondary" href="">annulla</a>   <span class="pull-right buttonPluriennale"><a class="btn btn-primary confermaPluri" rel="FIN-insModificheStep2.shtml">Prosegui</a>&nbsp;<a class="btn btn-primary confermaPluri" rel="FIN-Modifiche.shtml">Salva</a></span> <span class="pull-right singleButton"><a class="btn btn-primary" href="FIN-Modifiche.shtml">Salva</a></span></p>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>




 <script type="text/javascript">
     $(document).ready(function() {
      $('.buttonPluriennale').hide();
      $('.anniPluriennale').hide();
      $('#motivo').change(function(){
        var selectOpt=$('#motivo').children(":selected").attr("id");
        if(selectOpt=='pluriennale'){              
          $('.buttonPluriennale').show();
          $('.anniPluriennale').show();
          $('.singleButton').hide();
        }else{
          $('.buttonPluriennale').hide();
          $('.anniPluriennale').hide();
          $('.singleButton').show();
        }
      });

 
 
        $(document).on("click", ".confermaPluri", function(e) {  
            //alert($(this).attr('rel'));
            var link=$(this).attr('rel');
            var campoAnnoP=$('#annipluri').val();
           // alert(campoAnnoP);
            if(campoAnnoP==''){
              bootbox.dialog({ 
                className:'dialogAlert',
                title: "Attenzione!",
                message: "Non hai specificato gli anni di pluriennale: sei sicuro di voler proseguire?",  
                buttons: {
                  success: {
                    label: "Conferma",
                    className: "btn-primary",
                    callback: function() {
                       window.location.href=link;                    
                    }
                  },
                  main: {
                  label: "Chiudi",
                  className: "btn-primary",
                    callback: function() {
                   
                    }
                  }
                }
              });
            }else{
             window.location.href=link;
            }
          });
 
 
 });
</script>


<s:include value="/jsp/include/footer.jsp" />