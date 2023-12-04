<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="s" uri="/struts-tags"%>
<div class="accordion_info">
	<fieldset class="form-horizontal">

		<div class="control-group">
			<label class="control-label" for="Denominazione">Denominazione
				*</label>
			<div class="controls">
				<s:textfield id="nuovaSedeSecondaria.denominazione"
					name="nuovaSedeSecondaria.denominazione" type="text" cssClass="span4"/>
				<s:hidden id="uid" name="nuovaSedeSecondaria.uid"/>
				<s:hidden id="idSedeSecondaria" name="idSedeSecondaria"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="Stato2">Stato\Comune</label>
			<div class="controls">
			    <s:if test="null!=nazioni">
					<s:select list="nazioni" id="idNazione"
						name="nuovaSedeSecondaria.indirizzoSoggettoPrincipale.codiceNazione"
						cssClass="span3" listKey="codice" listValue="descrizione" />
				</s:if>	
				<span class="al"></span>
				<s:textfield id="comune"
					name="nuovaSedeSecondaria.indirizzoSoggettoPrincipale.comune"
					title="comune" cssClass="lbTextSmall span3" />
				<s:hidden id="comuneId"
					name="nuovaSedeSecondaria.indirizzoSoggettoPrincipale.idComune" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label" for="Sedime">Indirizzo</label>
			<div class="controls">
				<s:textfield id="sedimiList"
					name="nuovaSedeSecondaria.indirizzoSoggettoPrincipale.sedime"
					cssClass="lbTextSmall span1" title="sedime" />

				<span class="al"></span>
				<s:textfield id="nomeVia"
					name="nuovaSedeSecondaria.indirizzoSoggettoPrincipale.denominazione"
					cssClass="lbTextSmall span4" title="nome via" />

				<span class="al"></span>
				<s:textfield id="numeroCivico"
					name="nuovaSedeSecondaria.indirizzoSoggettoPrincipale.numeroCivico"
					cssClass="lbTextSmall span1" maxlength="7" title="n. civico" />

				<span class="al"></span>
				<s:textfield id="cap"
					name="nuovaSedeSecondaria.indirizzoSoggettoPrincipale.cap"
					onkeyup="return checkItNumbersOnly(event)" cssClass="span1"
					title="C.A.P." maxlength="5" />
					
					<!--  da scommentare per la risoluzione della jira 1013 -->
					
				<label class="radio inline">
					<s:checkbox id="idAvvisoIndirizzo" name="nuovaSedeSecondaria.indirizzoSoggettoPrincipale.checkAvviso" />
					&nbsp;Avviso
				</label>	
					
			</div>
		</div>
	</fieldset>
</div>

<div class="accordion_info">

	<fieldset class="form-horizontal">
		<div class="control-group">
			<label class="control-label" for="Numero">Numero telefono</label>
			<div class="controls">
				<s:textfield id="numeroTelefono" name="recapitoSS.numeroTelefono"
					onkeyup="return checkItNumbersOnly(event)" cssClass="span2" />

				<span class="al"> <label class="radio inline" for="Numero2">Numero
						cellulare</label>
				</span>
				<s:textfield id="numeroCellulare" name="recapitoSS.numeroCellulare"
					onkeyup="return checkItNumbersOnly(event)" cssClass="span2" />

				<span class="al"> <label class="radio inline" for="Numero3">Fax</label>
				</span>
				<s:textfield id="numeroFax" name="recapitoSS.numeroFax"
					onkeyup="return checkItNumbersOnly(event)" cssClass="span2" />
			</div>
		</div>




		<div class="control-group">
			<label class="control-label" for="PEC"><acronym
				title="posta elettronica certificata">PEC</acronym></label>
			<div class="controls">
				<s:textfield id="pec" name="recapitoSS.pec" cssClass="span2" />

				<span class="al"> </span>
				<div class="radio inline">
					<s:checkbox id="checkAvvisoPec" name="recapitoSS.checkAvvisoPec"
						 />
					&nbsp;Avviso
				</div>
				
				<span class="al">
						<label class="radio inline" for="codDestinatario">Codice destinatario/ IPA</label>
						<s:textfield id="codDestinatario" name="nuovaSedeSecondaria.codDestinatario" cssClass="span2" maxlength="7" style="text-transform: uppercase"/>
				</span>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label" for="mail">E-mail</label>
			<div class="controls">
				<s:textfield id="mail" name="recapitoSS.email" cssClass="span2" />
				<span class="al"> </span>
				<div class="radio inline">
					<s:checkbox id="checkAvvisoEmail"
						name="recapitoSS.checkAvvisoEmail" />
					&nbsp;Avviso
				</div>

			</div>
		</div>



		<div class="control-group">
			<label class="control-label" for="web">Sito web</label>
			<div class="controls">
				<s:textfield id="sitoWeb" name="recapitoSS.sitoWeb" cssClass="span3" />
			</div>
		</div>


		<div class="control-group">
			<label class="control-label" for="contatto">Nome contatto</label>
			<div class="controls">
				<s:textfield id="contatto" name="recapitoSS.contatto"
					cssClass="span3" />
			</div>
		</div>

	</fieldset>
	<p class="marginLarge">
		<a href="#" id="annullaInserimento" class="btn">annulla inserimento</a>
		<!-- task-131 <s:submit name="salvaSede" value="salva" method="salvaSede" id="salvaId" cssClass="btn" data-target="#insInd"/>-->
		<s:submit name="salvaSede" value="salva" action="aggiornaSediSecondarie_salvaSede" id="salvaId" cssClass="btn" data-target="#insInd"/>
	</p>

</div>

	
		
		