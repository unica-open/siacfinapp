<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags" %>

<!--modale creditore liquidazione -->
<div id="guidaCreditore" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaCreditoreLabel" aria-hidden="true">
	<div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4 class="nostep-pane">Seleziona creditore</h4>	  
	</div>
	<div class="modal-body">
    
    <fieldset class="form-horizontal">
	<div id="campiRicercaSog" class="accordion-body collapse in">
      <div class="control-group">
        <label class="control-label" for="Codice">Codice</label>
        <div class="controls">
          <input id="Codice" name="Codice" class="span3" type="text"/>
          <label class="radio inline" for="codfisc">Codice Fiscale</label>  
          <input id="codfisc" name="codfisc" class="span4" type="text"/>
        </div>
      </div>
      <div class="control-group">
        <label class="control-label" for="iva">Partita IVA</label>
        <div class="controls">
          <input id="iva" name="iva" class="span3" type="text"/>  
          <label class="radio inline">Denominazione</label> <input type="text" name="optionsRadios" id="optionsRadios1" class="span4">
        </div>
      </div> 
	  
       <div class="control-group">
        <label class="control-label" for="classif">Classificatore</label>
        <div class="controls">
          <select id="classif" name="classif" class="span3">
			  <option>x0</option>
			  <option>x1</option>
			  <option>x2</option>
			  <option>x3</option>
			  <option>x4</option>
		  </select>      
          
        </div>
      </div>     	 
	 </div>
      <a class="accordion-toggle btn btn-primary pull-right" data-toggle="collapse" data-parent="#guidaCap" href="#campiRicercaSog"><i class="icon-search icon"></i>cerca&nbsp;<span class="icon"> </span></a>
    </fieldset>
    <h4>Elenco creditori</h4>     

    <table class="table table-hover tab_left">
      <thead>
        <tr>
          <th scope="col"></th>
		  <th scope="col" >Codice</th>
          <th scope="col">Codice fiscale</th>
          <th class="header headerSortDown">Partita IVA</th>
          <th scope="col">Denominazione</th>
          <th scope="col">Stato</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td> <input type="radio" name="file_3d" id="file_3" /></td>
          <td><label for="file_3">xxxxx</label></td>
          <td>xxxxxxxxx xxxx</td>
          <td>01010203</td>
          <td>rrrrrrr</td>
          <td>yyy yyyy</td>
        </tr>
        <tr>
          <td> <input type="radio" name="file_3" id="file_2" /></td>
          <td><label for="file_2">xxxxx</label></td>
          <td>xxxxxxxxx xxxx</td>
          <td>01010203</td>
          <td>rrrrrrr</td>
          <td>valido</td>
        </tr>
        <tr>
          <td> <input type="radio" name="file_3" id="file_1" /></td>
          <td><label for="file_1">xxxxx</label></td>
          <td>xxxxxxxxx xxxx</td>
          <td>01010203</td>
          <td>rrrrrrr</td>
          <td>yyy yyyy</td>
        </tr>
      </tbody>
    </table>      
    <div class="row pagination_conth">
      <div id="risultatiricerca_info" class="span4">1 - 10 di 28 risultati</div>   
      <div class="span8">                               
        <div id="paginazione" class="pagination pagination-right">  
          <ul>
            <li><a href="#">&laquo; inizio</a></li>
            <li><a href="#">&laquo; prec</a></li>
            <li class="disabled"><a href="#">1</a></li>
            <li class="active"><a href="#">2</a></li>
            <li><a href="#">3</a></li>
            <li><a href="#">4</a></li>
            <li><a href="#">5</a></li>   
            <li><a href="#">succ &raquo;</a></li>
            <li><a href="#"> fine &raquo;</a></li>
          </ul>

        </div>
      </div>  
    </div>
          
  </div>
  <div class="modal-footer">
    <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">conferma</button>
  </div>
</div>
<!--/modale  creditore liquidazione -->

<!--modale provvedimento -->
<div id="guidaProv" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaProvLabel" aria-hidden="true">
	<div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4 class="nostep-pane">Seleziona provvedimento</h4>
		<p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p>	
	</div>
	
	<div class="modal-body">		             
      <fieldset class="form-horizontal">        		 
        <div id="campiRicercaProv" class="accordion-body collapse in">
        <div class="control-group">
          <label class="control-label" for="anno">Anno *</label>
          <div class="controls">   
            <input id="anno" class="lbTextSmall span2" type="text" value="" name="anno" />
            <span class="al">
              <label class="radio inline" for="numero">Numero</label>
            </span>
            <input id="numero" class="lbTextSmall span1" type="text" value="" name="numero"/>      
            <span class="al">
              <label class="radio inline" for="tipo">Tipo</label>
            </span>
            <select id="tipo" class="lbTextSmall span2"><option> </option><option>Delibera</option><option>Determina</option><option>Movimento Interno</option></select>
          </div>
        </div>
        <div class="control-group">      
          <label class="control-label">Struttura Amministrativa</label>
          <div class="controls">                    
            <div class="accordion span9" class="struttAmm">
              <div class="accordion-group">
                <div class="accordion-heading">    
                  <a class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmm" href="#3b">
                  Seleziona la Struttura amministrativa</a>
                </div>
                <div id="3b" class="accordion-body collapse">
                  <div class="accordion-inner">
                   <ul class="ztree treeStruttAmm"></ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="control-group">
          <label class="control-label" for="Oggetto">Oggetto</label>
			<div class="controls">
				<input id="Oggetto" class="lbTextSmall span9" type="text" value="" name="Oggetto" />
			</div>
        </div>
        </div>
      <a class="accordion-toggle btn btn-primary pull-right" data-toggle="collapse" data-parent="#guidaProv" href="#campiRicercaProv"><i class="icon-search icon"></i>cerca&nbsp;<span class="icon"> </span></a>  
      </fieldset>
      <h4>Elenco provvedimenti trovati</h4>   
      <table class="table table-hover tab_left">
        <thead>
          <tr>
            <th scope="col"></th>
			<th scope="col">Anno</th>
            <th scope="col">Numero</th>
            <th scope="col">Tipo</th>
            <th scope="col">Oggetto</th>
            <th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt Amm Resp</abbr></th>
            <th scope="col">Stato</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td> <input type="radio" name="file_33" id="file_33d" /></td>									  
            <td>2013</td>
            <td>230</td>
            <td>determina</td>
            <td>oggetto determina 1</td>
            <td>dir 01</td>
            <td>provvisorio</td>
          </tr>
          <tr>
            <td> <input type="radio" name="file_33" id="file_33c" /></td>									  
            <td>2013</td>
            <td>456</td>
            <td>determina</td>
            <td>oggetto determina 2</td>
            <td>dir 02</td>
            <td>provvisorio</td>
          </tr>
        </tbody>
        <tfoot>
        </tfoot>
      </table> 
      <div class="row pagination_conth">
        <div id="risultatiricerca_info2" class="span5">1 - 10 di 28 risultati</div>   
        <div class="span7">                               
          <div id="paginazione2" class="pagination pagination-right">
            <ul>
              <li><a href="#">&laquo; inizio</a></li>
              <li><a href="#">&laquo; prec</a></li>
              <li class="disabled"><a href="#">1</a></li>
              <li class="active"><a href="#">2</a></li>
              <li><a href="#">3</a></li>
              <li><a href="#">4</a></li>
              <li><a href="#">5</a></li>
              <li><a href="#">succ &raquo;</a></li>
              <li><a href="#"> fine &raquo;</a></li>
            </ul>
          </div>
        </div>  
      </div>             
    </div>   
  <div class="modal-footer">
	<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">conferma</button>
  </div>
</div>
<!--/modale provvedimento -->
 
 <!--modale guidaCap -->
 <div id="guidaCap" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaCapLabel" aria-hidden="true">
	<div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4 class="nostep-pane">Seleziona il capitolo</h4>		 
	</div>
	
	<div class="modal-body">
	
    <fieldset class="form-horizontal">    
      <div id="campiRicerca" class="accordion-body collapse in">
        <div class="control-group">
          <label class="control-label" for="annoda2">Anno</label>
          <div class="controls">      
            <input id="annoM" class="lbTextSmall span2" type="text" value="XXXX" name="annoM" disabled="disabled" />
            <span class="al">
              <label class="radio inline" for="capM">Capitolo *</label>
            </span>
            <input id="capM" class="lbTextSmall span2" type="text" value="" name="capM"/>      
            <span class="al">
              <label class="radio inline" for="artM">Articolo *</label>
            </span>
            <input id="artM" class="lbTextSmall span2" type="text" value="" name="artM"/>
            <span class="al">
              <label class="radio inline" for="UEBM">UEB</label>
            </span>
            <input id="UEBM" class="lbTextSmall span2" type="text" value="" name="UEBM"/>
            
          </div>
        </div>
        
              <div class="control-group">
                  <label class="control-label"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario <a class="tooltip-test" title="selezionare prima il macroaggregato" href="#"><i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare prima il macroaggregato</span></i></a></label>
                  <div class="controls">                    
                    <div class="accordion span11" class="pianoConti">
                      <div class="accordion-group">
                        <div class="accordion-heading">    
                          <a class="accordion-toggle" data-toggle="collapse" data-parent="#pianoConti" href="#pc">
                          Seleziona Piano dei conti</a>
                        </div>
                        <div id="pc" class="accordion-body collapse">
                          <div class="accordion-inner">
                           <ul id="treePDC" class="ztree"></ul>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>   
              </div>
        <div class="control-group">
            <label class="control-label">Struttura Amministrativa</label>
            <div class="controls">                    
              <div class="accordion span11" class="struttAmm">
                <div class="accordion-group">
                  <div class="accordion-heading">    
                    <a class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmm" href="#3">
                    Seleziona la Struttura amministrativa</a>
                  </div>
                  <div id="3" class="accordion-body collapse">
                    <div class="accordion-inner">
                     <ul class="ztree treeStruttAmm"></ul>
                    </div>
                  </div>
                </div>
              </div>
            </div>   
        </div>
        <div class="control-group">
            <label class="control-label">Tipo finanziamento</label>
            <div class="controls">   
            <select name="strAmm" id="strAmm" class="span5"><option>xxxxx</option></select>  
            </div>   
        </div>
      </div>
     <a class="accordion-toggle btn btn-primary pull-right" data-toggle="collapse" data-parent="#guidaCap" href="#campiRicerca"><i class="icon-search icon"></i>cerca&nbsp;<span class="icon"> </span></a>
    </fieldset>
    <h4>Elenco Ueb capitolo 0000004/1</h4>   
    <table class="table table-hover tab_left">
      <thead>
        <tr>
          <th scope="col" ></th> 
			<th scope="col">Capitolo</th>		  
          <th scope="col">Classificazione</th>
          <th scope="col">Disponibile anno 1</th>
          <th scope="col">Disponibile anno 2</th>
          <th scope="col">Disponibile anno 3</th>
          <th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt Amm Resp</abbr></th>
          <th scope="col"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td> <input type="radio" name="file_3" id="file_3d" /></td>    
          <td><a href="#" data-trigger="hover" rel="popover" title="" data-original-title="Descrizione" data-content="descrizione del capitolo">0000004/1/1</a></td>    
          <td>01010203</td>
          <td>1000</td>
          <td>200</td>
          <td>1200</td>
          <td><a href="#" data-trigger="hover" rel="popover" data-placement="left" title="" data-original-title="Descrizione" data-content="descrizione della struttura">dir 01</a></td>
          <td><a href="#" data-trigger="hover" rel="popover" data-placement="left" title="" data-original-title="Voce" data-content="Altre imposte sostitutive n.a.c. riscosse in via ordinaria e attraverso altre forme">E.1.01.01.09.001</a></td>

        </tr>
        <tr>
          <td> <input type="radio" name="file_3" id="file_3c" /></td>    
          <td><a href="#" data-trigger="hover" rel="popover" title="" data-original-title="Descrizione" data-content="descrizione del capitolo">0000004/1/2</a></td>    
          <td>01010203</td>
          <td>1500</td>
          <td>400</td>
          <td>1900</td>
          <td><a href="#" data-trigger="hover" rel="popover" data-placement="left" title="" data-original-title="Descrizione" data-content="descrizione della struttura">dir 02</a></td>
          <td><a href="#" data-trigger="hover" rel="popover" data-placement="left" title="" data-original-title="Voce" data-content="Altre imposte sostitutive n.a.c. riscosse in via ordinaria e attraverso altre forme">E.1.01.01.09.001</a></td>
        </tr>
        <tr>
          <td> <input type="radio" name="file_3" id="file_3f" /></td>
          <td><a href="#" data-trigger="hover" rel="popover" title="" data-original-title="Descrizione" data-content="descrizione del capitolo">0000004/1/3</a></td>   
          <td>01010203</td>
          <td>2000</td>
          <td>600</td>
          <td>2600</td>
          <td><a href="#" data-trigger="hover" rel="popover" data-placement="left" title="" data-original-title="Descrizione" data-content="descrizione della struttura ">dir 03</a></td>
          <td><a href="#" data-trigger="hover" rel="popover" data-placement="left" title="" data-original-title="Voce" data-content="Altre imposte sostitutive n.a.c. riscosse in via ordinaria e attraverso altre forme">E.1.01.01.09.001</a></td>

        </tr>
      </tbody>
      <tfoot>
      </tfoot>    
    </table> 
    <div class="row pagination_conth">
      <div id="risultatiricerca_info" class="span4">1 - 10 di 28 risultati</div>   
      <div class="span8">                               
        <div id="paginazione" class="pagination pagination-right">
          <ul>
            <li><a href="#">&laquo; inizio</a></li>
            <li><a href="#">&laquo; prec</a></li>
            <li class="disabled"><a href="#">1</a></li>
            <li class="active"><a href="#">2</a></li>
            <li><a href="#">3</a></li>
            <li><a href="#">4</a></li>
            <li><a href="#">5</a></li>
            <li><a href="#">succ &raquo;</a></li>
            <li><a href="#"> fine &raquo;</a></li>    
          </ul>
        </div>    
      </div>  
    </div>                
    <a class="btn" href="#">seleziona</a>
    <a class="btn" data-toggle="collapse" data-target="#visDett">visualizza dettaglio</a>           
    <div id="visDett" class="collapse margin-large">
    <div class="accordion_info">
    <h4>UEB 0000004/1/3 - <strong>Tipo finanziamento:</strong> XXXXXX</h4>
    <table summary="riepilogo incarichi" class="table table-hover tab_centered">
      <thead>
        <tr>
          <th>Stanziamenti</th>
          <th>2013</th>
          <th>2014</th>
          <th>2015</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <th>Competenza</th>
          <td>xxxx</td>
          <td>xxxx</td>
          <td>xxxx </td>        
        </tr>
        <tr>
          <th>Residuo</th>
          <td>xxxx</td>
          <td>xxxxx</td>
          <td>xxxxxx</td>        
        </tr>
        <tr>
          <th>Cassa</th>
          <td>xxxx</td>
          <td>xxxxx</td>
          <td>xxxxxx</td>        
        </tr>
      </tbody>
    </table>
    </div>
    </div>        
  </div>
  <div class="modal-footer">
    <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">conferma</button>
  </div>
</div>  
 <!--/modale guidaCap -->

 <!--modale guidaSog --> 
 <div id="guidaSog" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaSogLabel" aria-hidden="true">
	<div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4 class="nostep-pane">Seleziona soggetto</h4>	  
		<p> E' possibile associare all'impegno un soggetto o una classe di soggetti</p>		 
	</div>
	<div class="modal-body">
    
    <fieldset class="form-horizontal">
      <div id="campiRicercaSog" class="accordion-body collapse in">
      <div class="control-group">
        <label class="control-label" for="Codice">Codice</label>
        <div class="controls">
          <input id="Codice" name="Codice" class="span3" type="text"/>
          <label class="radio inline" for="codfisc">Codice Fiscale</label>  
          <input id="codfisc" name="codfisc" class="span4" type="text"/>
        </div>
      </div>
      <div class="control-group">
        <label class="control-label" for="iva">Partita IVA</label>
        <div class="controls">
          <input id="iva" name="iva" class="span3" type="text"/>  
          <label class="radio inline">Denominazione</label> <input type="text" name="optionsRadios" id="optionsRadios1" class="span4">
        </div>
      </div> 
      <div class="control-group">
        <label class="control-label" for="classif">Classificatore</label>
        <div class="controls">
          <select id="classif" name="classif" class="span3">
			  <option>x0</option>
			  <option>x1</option>
			  <option>x2</option>
			  <option>x3</option>
			  <option>x4</option>
		  </select>      
          
        </div>
      </div>     	 
    
      </div>
      <a class="accordion-toggle btn btn-primary pull-right" data-toggle="collapse" data-parent="#guidaCap" href="#campiRicercaSog"><i class="icon-search icon"></i>cerca&nbsp;<span class="icon"> </span></a>
    </fieldset>
    <h4>Elenco soggetti</h4>     

    <table class="table table-hover tab_left">
      <thead>
        <tr>
            <th scope="col"></th>
			<th scope="col" >Codice</th>
			<th scope="col">Codice fiscale</th>
			<th class="header headerSortDown">Partita IVA</th>
			<th scope="col">Denominazione</th>
			<th scope="col">Stato</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td> <input type="radio" name="file_3d" id="file_3" /></td>
          <td><label for="file_3">xxxxx</label></td>
          <td>xxxxxxxxx xxxx</td>
          <td>01010203</td>
          <td>rrrrrrr</td>
          <td>yyy yyyy</td>
        </tr>
        <tr>
          <td> <input type="radio" name="file_3" id="file_2" /></td>
          <td><label for="file_2">xxxxx</label></td>
          <td>xxxxxxxxx xxxx</td>
          <td>01010203</td>
          <td>rrrrrrr</td>
          <td>valido</td>
        </tr>
        <tr>
          <td> <input type="radio" name="file_3" id="file_1" /></td>
          <td><label for="file_1">xxxxx</label></td>
          <td>xxxxxxxxx xxxx</td>
          <td>01010203</td>
          <td>rrrrrrr</td>
          <td>yyy yyyy</td>
        </tr>
      </tbody>
    </table>      
    <div class="row pagination_conth">
      <div id="risultatiricerca_info" class="span4">1 - 10 di 28 risultati</div>   
      <div class="span8">                               
        <div id="paginazione" class="pagination pagination-right">  
          <ul>
            <li><a href="#">&laquo; inizio</a></li>
            <li><a href="#">&laquo; prec</a></li>
            <li class="disabled"><a href="#">1</a></li>
            <li class="active"><a href="#">2</a></li>
            <li><a href="#">3</a></li>
            <li><a href="#">4</a></li>
            <li><a href="#">5</a></li>   
            <li><a href="#">succ &raquo;</a></li>
            <li><a href="#"> fine &raquo;</a></li>
          </ul>

        </div>
      </div>  
    </div>
          
  </div>
  <div class="modal-footer">
    <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">conferma</button>
  </div>
</div>
 <!--/modale guidaSog --> 

 <!-- Modal -->
	<div id="msgElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
            
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                  <p><strong>Elemento selezionato:.....</strong></p>
                  <p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <button class="btn btn-primary">si, prosegui</button>
              </div>
	</div>  
<!--/modale elimina -->

<!-- Modal -->
	<div id="msgAnnullaLiquidazione" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione GIUSTAAAA!</strong></p>
                  <p><strong>Elemento selezionato:<s:textfield id="codiceLiquidazioneDaAnnullare" name="codiceLiquidazioneDaAnnullare" disabled="true"/></strong></p>
                  <p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" action="annullaLiquidazione"/>
              </div>
	</div>  
	
	
<!--/modale annulla --> 

<!-- Modal capitoloTab -->
<div id="capitoloTab" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="capitoloTabLabel" aria-hidden="true">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
    <h4 id="myModalLabel2">Dettagli del capitolo</h4>
  </div>
  <div class="modal-body">
    <dl class="dl-horizontal">
      <dt>Numero</dt>
      <dd>cap - art - ueb</dd>
      <dt>Tipo finanziamento</dt>
      <dd>XXXXX</dd>
      <dt>Piano dei conti finanziario</dt>
      <dd>codice + descrizione</dd>
    </dl>
    <table class="table tab_left table-bordered">
      <tr>
        <th>&nbsp;</th>
        <th scope="col" class="tab_Right">2013</th>
        <th scope="col" class="tab_Right">2014</th>
        <th scope="col" class="tab_Right">2015</th>
      </tr>
      <tr>
        <th>Stanziamento</th>
        <td class="tab_Right">&nbsp;</td>
        <td class="tab_Right">&nbsp;</td>
        <td class="tab_Right">&nbsp;</td>       
      </tr>
      <tr>
        <th>Disponibile </th>
        <td class="tab_Right" scope="row" >&nbsp;</td>
        <td class="tab_Right">&nbsp;</td>
        <td class="tab_Right">&nbsp;</td>        
      </tr>
    </table>
  </div>
  </div>  
    
<!--/modale annulla -->    