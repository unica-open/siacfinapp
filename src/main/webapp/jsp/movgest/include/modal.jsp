<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags" %>

<!-- Modal -->
	<!--modale capitolo -->
	<s:include value="/jsp/include/modalCapitolo.jsp" />
	<!--/modale capitolo -->
					  
	<!--modale soggetto -->
	<s:include value="/jsp/include/modalSoggetto.jsp"/>	
	<!--/modale soggetto -->
 
 
	<!--modale provvedimento -->
	<s:include value="/jsp/include/modalProvvedimenti.jsp" />
	<!--/modale provvedimento -->
	
	<s:include value="/jsp/include/modalInserisciProvvedimento.jsp" />
	
	<!--modale consulta modifiche provvedimento-->
	<div id="modConsultaModificheProvvedimento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaProvLabel" aria-hidden="true">
	    <div class="modal-body">
	      
	        
 		<s:include value="/jsp/movgest/include/consultaModificheProvvedimento.jsp" />
	         
	              
	    
	    </div>   
  
	</div>
   <!--/modale modifiche -->
	
	
	
   
    <!--modale consulta modifiche -->
	<div id="consultaSubi" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaProvLabel" aria-hidden="true">
    <div class="modal-body">
      <h4>Consulta modifiche</h4>	
      <p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p>			             
      <fieldset class="form-horizontal">        		 
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
                  <a class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmm" href="#3c">
                  Seleziona la Struttura amministrativa</a>
                </div>
                <div id="3c" class="accordion-body collapse">
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
            <a class="btn btn-primary" href="#"><i class="icon-search icon"></i> cerca</a> <span class="nascosto"> | </span><!--<a class="btn" href="#">inserisci</a>             -->
          </div>
        </div>
        <!-- Modal -->
        <!--div id="struttAmm3" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
          <div class="modal-header">
            
            <a href="#struttAmm" role="button" class="close" data-toggle="modal"></a> 
            <h3 id="myModalLabel2">Struttura Amministrativa Responsabile</h3>
          </div>
          <div class="modal-body">
            <ul id="treeStruttAmm3" class="ztree"></ul>
          </div>
        
        </div-->    
      </fieldset>
      <h4>Elenco subimpegni trovati</h4>   
      <table class="table table-hover tab_centered">
        <thead>
          <tr>
            <th scope="col" colspan="2">Anno<!--/Articolo/<abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr> --></th>
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
            <!--<td>ggggggg</td> -->
          </tr>
          <tr>
            <td> <input type="radio" name="file_33" id="file_33c" /></td>									  
            <td>2013</td>
            <td>456</td>
            <td>determina</td>
            <td>oggetto determina 2</td>
            <td>dir 02</td>
            <td>provvisorio</td>
            <!--<td>ggggggg</td> -->
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
    <!--a class="btn" href="#">seleziona</a-->  
    </div>   
  <div class="modal-footer">
  <!-- <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button> -->
  <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">conferma</button>
  </div>
</div>
   <!--/modale modifiche -->
   
   
   <!--modale consulta modifiche -->
  <div id="consultaMod" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaProvLabel" aria-hidden="true">
    <div class="modal-body">
      <h4>Consulta modifiche</h4>	
      <p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p>			             
      <fieldset class="form-horizontal">        		 
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
                  <a class="accordion-toggle" data-toggle="collapse" data-parent="#struttAmm" href="#3d">
                  Seleziona la Struttura amministrativa</a>
                </div>
                <div id="3d" class="accordion-body collapse">
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
            <a class="btn btn-primary" href="#"><i class="icon-search icon"></i> cerca</a> <span class="nascosto"> | </span><!--<a class="btn" href="#">inserisci</a>             -->
          </div>
        </div>
        <!-- Modal -->
        <!--div id="struttAmm3" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
          <div class="modal-header">
            
            <a href="#struttAmm" role="button" class="close" data-toggle="modal"></a> 
            <h3 id="myModalLabel2">Struttura Amministrativa Responsabile</h3>
          </div>
          <div class="modal-body">
            <ul id="treeStruttAmm3" class="ztree"></ul>
          </div>
        
        </div-->    
      </fieldset>
      <h4>Elenco modifiche trovate</h4>   
      <table class="table table-hover tab_centered">
        <thead>
          <tr>
            <th scope="col" colspan="2">Anno<!--/Articolo/<abbr title="Unit&agrave; Elementare Bilancio">UEB</abbr> --></th>
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
            <!--<td>ggggggg</td> -->
          </tr>
          <tr>
            <td> <input type="radio" name="file_33" id="file_33c" /></td>									  
            <td>2013</td>
            <td>456</td>
            <td>determina</td>
            <td>oggetto determina 2</td>
            <td>dir 02</td>
            <td>provvisorio</td>
            <!--<td>ggggggg</td> -->
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
    <!--a class="btn" href="#">seleziona</a-->  
    </div>   
  <div class="modal-footer">
  <!-- <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button> -->
  <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">conferma</button>
  </div>
</div>
   <!--/modale modifiche -->
   
   
   
   <!--modale consulta mutui -->
  <div id="consultaMut" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaProvLabel" aria-hidden="true">
    <div class="modal-body">
      <h4>Consulta mutui</h4>	
      <p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p>			             
      <fieldset class="form-horizontal">        		 
        <div class="control-group">
          <label class="control-label" for="numero">Numero mutuo</label>
          <div class="controls">   
            <input id="numero" class="lbTextSmall span2" type="text" value="" name="numero" />
            <span class="al">
              <label class="radio inline" for="numreg">Numero registrazione</label>
            </span>
            <input id="numreg" class="lbTextSmall span2" type="text" value="" name="numreg"/>      
          </div>
        </div>   
      </fieldset>
      <h4>Provvedimento</h4>
      <!--p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto </p-->
      <fieldset class="form-horizontal">
        <div class="control-group">
          <label class="control-label" for="anno">Anno *</label>
          <div class="controls">   
            <input id="anno" class="lbTextSmall span2" type="text" value="" name="anno" />
            <span class="al">
              <label class="radio inline" for="numero">Numero *</label>
            </span>
            <input id="numero" class="lbTextSmall span2" type="text" value="" name="numero"/>      
            <span class="al">
              <label class="radio inline" for="Stato"></label>
            </span>
            <span class="radio guidata">Se non possiedi tutti i dati richiesti, passa alla <a href="#guidaProv" data-toggle="modal" class="btn btn-primary">complilazione guidata</a></span>
          </div>
        </div>
        <!-- Modal -->
      </fieldset>
      <h4>Istituto mutuante </h4>
      <fieldset class="form-horizontal">
        <div class="control-group">
          <label class="control-label" for="Codice">Codice</label>
          <div class="controls">
            <input id="Codice" name="Codice" class="span2" type="text"/>
            <span class="al">
              <label class="radio inline" for="classe">Classe</label>
            </span>
            <select name="classe" id="classe" class="span5"><option>xxxxx</option></select>
            <span class="radio guidata">Se non possiedi tutti i dati richiesti, passa alla <a href="#guidaSogMutuo" data-toggle="modal" class="btn btn-primary">complilazione guidata</a></span>
          </div>
        </div>
      </fieldset>
      <h4>Elenco mutui trovati</h4>   
      <table class="table table-hover tab_centered">
        <thead>
          <tr>
            <th scope="col">Sel.</th>
            <th scope="col">Nr. mutuo</th>
            <th scope="col">Tipo mutuo</th>
            <th scope="col">Data inizio</th>
            <th scope="col">Descrizione mutuo</th>
            <th scope="col">Stato</th>
            <th scope="col"><p class="pagination-right">Importo iniziale</p></th>
            <th scope="col"><p class="pagination-right">Importo attuale</p></th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td> <input type="radio" name="file_33" id="file_33d" /></td>									  
            <td>00837</td>
            <td>Riscossione completa</td>
            <td>01/01/2013</td>
            <td>Descrizione mutuo 1...</td>
            <td>D</td>
            <td><p class="pagination-right">200.000</p></td>
            <td><p class="pagination-right">125.000</p></td>
          </tr>
          <tr>
            <td> <input type="radio" name="file_33" id="file_33c" /></td>									  
            <td>00713</td>
            <td>Avanz. lavori</td>
            <td>01/01/2012</td>
            <td>Descrizione mutuo 2...</td>
            <td>A</td>
            <td><p class="pagination-right">150.000</p></td>
            <td><p class="pagination-right">93.000</p></td>
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
    <!--a class="btn" href="#">seleziona</a-->  
    </div>   
  <div class="modal-footer">
  <!-- <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button> -->
  <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">conferma</button>
  </div>
</div>
   <!--/modale mutui -->
   
   
   
   <!-- Modal -->
	<s:include value="/jsp/include/modalElimina.jsp" />

            <!-- Modal -->
            <div id="msgAnnulla" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                  <p><strong>Elemento selezionato:<s:textfield cssStyle="width:30px;" id="annoImpegnoImpegnoDaAnnullare" name="annoMovimento" disabled="true" maxlength="20"/>&nbsp;/&nbsp;<s:textfield cssStyle="width:30px;" id="numeroImpegnoDaAnnullare" name="numeroImpegno" disabled="true"/> </strong></p>
                  <p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="annullaImpegno"/>
              </div>
            </div>  
            <!--/modale annulla -->
            
            <!-- Modal annulla accertamento -->
            <div id="msgAnnullaAccertamento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                  <p><strong>Elemento selezionato:<s:textfield id="annoAccertamentoAccertamentoDaAnnullare" name="annoMovimento" disabled="true" cssStyle="width:30px;" />&nbsp;/&nbsp;<s:textfield cssStyle="width:30px;" id="numeroAccertamentoDaAnnullare" name="numeroAccertamento" disabled="true"/> </strong></p>
                  <p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="annullaAccertamento"/>
              </div>
            </div>  
            <!--/modale annulla -->
            
            
            
            <!-- Modal  annulla sub impegni -->
            <div id="msgAnnullaSubImp" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                  <p><strong>Elemento selezionato:<s:textfield id="numeroSubDaAnnullare" name="numeroSubDaAnnullare" disabled="true"/> </strong></p>
                  <p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="annullaSubImpegno"/>
              </div>
            </div>  
            <!--/modale annulla -->
            
            
             <!-- Modal  annulla sub accertamenti -->
            <div id="msgAnnullaSubAcc" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                  <p><strong>Elemento selezionato:<s:textfield id="numeroSubAccDaAnnullare" name="numeroSubDaAnnullare" disabled="true"/> </strong></p>
                  <p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="annullaSubAccertamento"/>
              </div>
            </div>  
            <!--/modale annulla -->
            
            
             <!-- Modal  annulla Movimento Spesa -->
            <div id="msgAnnullaModMov" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                  <p><strong>Elemento selezionato:<s:textfield id="numeroMovDaAnnullare" name="numeroMovDaAnnullare" disabled="true"/> </strong></p>
                  <p>Stai per annullare l'elemento selezionato, questo cambier&agrave; lo stato dell'elemento: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="annullaMovGestSpesa"/>
              </div>
            </div>  
            <!--/modale annulla -->
            
            
             <!-- Modal  Elimina sub impegni -->
            <div id="msgEliminaSubImp" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                  <p><strong>Elemento selezionato:<s:textfield id="numeroSubDaEliminare" name="numeroSubDaEliminare" disabled="true"/> </strong></p>
                  <p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="eliminaSubImpegno"/>
              </div>
            </div>  
            <!--/modale annulla -->
            
            
             <!-- Modal  Elimina sub impegni -->
            <div id="msgEliminaSubAcc" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                  <p><strong>Elemento selezionato:<s:textfield id="numeroSubAccDaEliminare" name="numeroSubDaEliminare" disabled="true"/> </strong></p>
                  <p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnAggiornamentoStato" value="si, prosegui" cssClass="btn btn-primary" method="eliminaSubAccertamento"/>
              </div>
            </div>  
            <!--/modale annulla -->
             
            
            
            <!--  RAFFAELA - NUOVA MODALE PER LA CONFERMA DI MODIFICA DEL PROVVEDIMENTO SUL SALVA IMPEGNI/ACCERTAMENTI/SUB DEFINITI E DEFINITIVI NON LIQUIDABILI -->
            
	            <div id="modalSalvaModificaProvvedimento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
	              <div class="modal-body">
	                <div class="alert alert-error">
	                  <button type="button" class="close" data-dismiss="alert">&times;</button>
	                  <p><strong>Attenzione!</strong></p>
	                  <p><s:property value="step1Model.alertDiConfermaModificaProvvedimento"/><br>
	                  <s:property value="step1Model.alertMovimentoCollegatoAOrdinativiOLiquidazioni"/></p>
	                </div>
	              </div>
	              <div class="modal-footer">
	                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
	                <s:submit id="submitBtn" name="btnSalvaModificaProvvedimento" value="si, salva" cssClass="btn btn-primary" method="siSalva"/>
	              </div>
	            </div>  
	            
            <!--/modale conferma modifica provvedimento -->
            
            
            <!--  RAFFAELA - NUOVA MODALE PER LA CONFERMA DI MODIFICA DEL PROVVEDIMENTO SUL PROSEGUI IMPEGNI/ACCERTAMENTI/SUB DEFINITI E DEFINITIVI NON LIQUIDABILI -->
                <div id="modalProseguiModificaProvvedimento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
	              <div class="modal-body">
	                <div class="alert alert-error">
	                  <button type="button" class="close" data-dismiss="alert">&times;</button>
	                  <p><strong>Attenzione!</strong></p>
	                  <p><s:property value="step1Model.alertDiConfermaModificaProvvedimento"/><br>
	                  <s:property value="step1Model.alertMovimentoCollegatoAOrdinativiOLiquidazioni"/></p>
	                </div>
	              </div>
	              <div class="modal-footer">
	                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
	                <s:submit id="submitBtn" name="btnProseguiModificaProvvedimento" value="si, prosegui" cssClass="btn btn-primary" method="siProsegui"/>
	              </div>
	            </div>  
	            
            <!--/modale conferma modifica provvedimento -->
            
            
            <!-- RM 29/09/2017-->
            <!-- MODALE CONFERMA PROSEGUI MODIFICA VINCOLI  -->
            <div id="modalConfermaSalvaModificaVincoli" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                  <p><s:property value="step1Model.messaggioDiConfermaModificaVincoli"/></p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnSalvaModificaVincoli" value="si, salva" cssClass="btn btn-primary" method="salvaDaModaleConfermaSalvaVincoli"/>
              </div>
            </div>  
	            
            <!-- MODALE CONFERMA SALVA MODIFICA VINCOLI  -->
            <div id="modalConfermaProseguiModificaVincoli" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                  <p><s:property value="step1Model.messaggioDiConfermaModificaVincoli"/></p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtn" name="btnProseguiModificaVincoli" value="si, prosegui" cssClass="btn btn-primary" method="proseguiDaModaleConfermaSalvaVincoli"/>
              </div>
            </div>     
            
            <!-- FINE -->
            
            
                        
            <!-- modale dati persi -->
            <div id="msgDatipersi" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgDatipersi" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                  <p>I dati non salvati andranno persi: sei sicuro di voler proseguire?</p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, prosegui" cssClass="btn btn-primary" method="gestisciForward"/>
              </div>
            </div>  
            <!-- /modale dati persi -->    
            
            
                     <!-- modale controllo prosegui -->
            <div id="msgControlloProsegui" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgDatipersi" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-error">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                  <s:actionerror/>
                  <p></p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, prosegui" cssClass="btn btn-primary" method="forzaProsegui"/>
              </div>
            </div>  
            <!-- /modale  controllo prosegui -->  
            
                       <!-- modale controllo prosegui pluriennali accertamento -->
            <div id="msgControlloProseguiPlurAcc" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgDatipersi" aria-hidden="true">
              <div class="modal-body">
                <div class="alert alert-warning">
                  <button type="button" class="close" data-dismiss="alert">&times;</button>
                  <p><strong>Attenzione!</strong></p>
                   <s:iterator value="actionWarnings">
		     	  		<s:property/><br>
		   		   </s:iterator>
                  <p></p>
                </div>
              </div>
              <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
                <s:submit id="submitBtnForward" name="btnSubmitBtnForward" value="si, prosegui" cssClass="btn btn-primary" method="forzaSalvaPluriennaleAccertamento"/>
              </div>
            </div>  
            <!-- /modale  controllo prosegui -->  
            
            
           <!--  CLAUDIO - NUOVA MODALE PER LA CONFERMA DI INSERIMENTO IMPEGNO CON BYPASS CONTROLLO SU DISP DODICESIMI -->
            
	            <div id="modalSalvaConBypassDodicesimi" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAnnullaLabel" aria-hidden="true">
	              <div class="modal-body">
	                <div class="alert alert-warning">
	                  <button type="button" class="close" data-dismiss="alert">&times;</button>
	                  <p><strong>Attenzione!</strong></p>
	                  <p>Disponibilita' insufficiente: superato limite dei dodicesimi<br>
	                  Salvare ugualmente ?</p>
	                </div>
	              </div>
	              <div class="modal-footer">
	                <button class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</button>
	                <s:submit id="submitBtn" name="btnSalvaConByPassDodicesimi" value="si, salva" cssClass="btn btn-primary" method="salvaConByPassDodicesimi"/>
	              </div>
	            </div>  
	            
            <!--/modale conferma inserimento impegno con bypass dodicesimi -->
            
            
            
            
            
	<div id="consultazioneSub" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="consulta" aria-hidden="true">
		<div id="divDettaglioSubPopUp"></div>  
	</div>  
	
	<div id="consultazioneMod" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="consulta" aria-hidden="true">
		<div id="divDettaglioModPopUp"></div>
	</div> 
  
<div id="capitoloTab" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="capitoloTabLabel" aria-hidden="true">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
    <h3 id="myModalLabel2">Dettagli del capitolo</h3>
  </div>
  <div class="modal-body">
    <dl class="dl-horizontal">
      <dt>Numero</dt>
      <dd><s:property value="step1Model.capitolo.anno"/> / <s:property value="step1Model.capitolo.numCapitolo"/> / <s:property value="step1Model.capitolo.articolo"/> / <s:property value="step1Model.capitolo.ueb"/> - <s:property value="step1Model.capitolo.descrizione" /> - <s:property value="step1Model.capitolo.descrizioneStrutturaAmministrativa" />&nbsp;</dd>
      <dt>Tipo finanziamento</dt>
      <dd><s:property value="step1Model.capitolo.tipoFinanziamento" />&nbsp;</dd>
      <dt>Piano dei conti finanziario</dt>
      <dd><s:property value="step1Model.capitolo.descrizionePdcFinanziario" default=" "/>&nbsp;</dd>
    </dl>
    <table class="table table-hover table-bordered">
      <tr>
        <th>&nbsp;</th>
        <s:iterator value="datoPerVisualizza.importiCapitolo">
			<th scope="col" class="text-center"><s:property value="annoCompetenza" /></th>
		</s:iterator>
      </tr>
      <tr>
        <th>Stanziamento</th>
        <s:iterator value="datoPerVisualizza.importiCapitolo">
			<td><s:property value="getText('struts.money.format', {competenza})" /></td>
		</s:iterator>       
      </tr>
      <tr>
        <th>Disponibile</th>
        <s:if test="oggettoDaPopolareImpegno()||oggettoDaPopolareSubimpegno()">
	        <td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloUG[0].disponibilitaImpegnareAnno1})" /></td>
	        <td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloUG[0].disponibilitaImpegnareAnno2})" /></td>
	        <td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloUG[0].disponibilitaImpegnareAnno3})" /></td>
        </s:if>
        <s:else>
	        <td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloEG[0].disponibilitaAccertareAnno1})" /></td>
	        <td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloEG[0].disponibilitaAccertareAnno2})" /></td>
	        <td><s:property value="getText('struts.money.format', {datoPerVisualizza.importiCapitoloEG[0].disponibilitaAccertareAnno3})" /></td>
        </s:else>
        <%-- <s:iterator value="datoPerVisualizza.importiCapitoloUG">
			<td><s:property value="getText('struts.money.format', {importiCapitoloUG})" /></td>
		</s:iterator>      --%>   
      </tr>
    </table>
  </div>
  </div>      