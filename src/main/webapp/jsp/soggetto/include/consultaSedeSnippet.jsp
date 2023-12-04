<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

							<div class="modal-header">
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
								<h3>Sede secondaria</h3>
							</div>
							<div class="modal-body">

								<dl class="dl-horizontal">
								    <dt>Stato Sede</dt>
								    <dd><s:property value="selectedSede.descrizioneStatoOperativoSedeSecondaria" />&nbsp;</dd>
								    <dt>Utente Ultimo Aggiornamento</dt>
								    <dd><s:property value="selectedSede.utenteModifica" />&nbsp;</dd>
								    <dt>Data Ultimo Aggiornamento</dt>
								    <dd><s:property value="%{selectedSede.dataModifica}" />&nbsp;</dd>
								    <s:if test="checkSedeInModifica()">
								    <dt>In Modifica</dt>
								    <dd>Si</dd>
								    <dt>Utente Modifica</dt>
								    <dd><s:property value="selectedSedeMod.utenteCreazione" />&nbsp;</dd>
								    <dt>Data Modifica</dt>
								    <dd><s:property value="%{selectedSedeMod.dataCreazione}" />&nbsp;</dd>
								    </s:if>
									<dt>Denominazione</dt>
									<dd><s:property value="selectedSede.denominazione" />&nbsp;</dd>
									<dt>Indirizzo</dt>
									<dd>
										<s:property value="selectedSede.indirizzoSoggettoPrincipale.sedime" />
										<s:property value="selectedSede.indirizzoSoggettoPrincipale.denominazione" />
										<s:property value="selectedSede.indirizzoSoggettoPrincipale.numeroCivico" />&nbsp;
									</dd>
									<dt>Comune</dt>
									<dd>
										<s:property value="selectedSede.indirizzoSoggettoPrincipale.comune" />&nbsp;
									</dd>

									<dt>
										<acronym title="codice di avviamento postale">C.A.P.</acronym>
									</dt>
									<dd>
										<s:property value="selectedSede.indirizzoSoggettoPrincipale.cap" default="-" />&nbsp;
									</dd>

									<dt>Telefono</dt>
									<dd>
										<s:property value="getContattoStr('telefono')" default="-" />&nbsp;
									</dd>
									
									<dt>Cellulare</dt>
									<dd>
										<s:property value="getContattoStr('cellulare')" default="-" />&nbsp;
									</dd>

									<dt>Fax</dt>
									<dd>
										<s:property value="getContattoStr('fax')" default="-" />&nbsp;
									</dd>

									<dt>Sito web</dt>
									<dd>
										<s:property value="getContattoStr('sito')" default="-" />&nbsp;
									</dd>

									<!--	<dt>Ricorrente</dt>
	        <dd>xxxx     Percentuale %</dd> -->

									<dt>Nome contatto</dt>
									<dd>
										<s:property value="getContattoStr('soggetto')" default="-" />&nbsp;
									</dd>

									<dt>E-mail</dt>
									<dd>
										<s:property value="getContattoStr('email')" default="-" />&nbsp;
									</dd>

									<dt>
										<acronym title="posta elettronica certificata">PEC</acronym>
									</dt>
									<dd>
										<s:property value="getContattoStr('PEC')" default="-" />&nbsp;
									</dd>
									

									<dt>Codice destinatario / IPA</dt>
									<dd>
										<s:property value="selectedSede.codDestinatario" default="-" />&nbsp;
									</dd>

									
									<dt>Utente Inserimento</dt>
								    <dd><s:property value="selectedSede.utenteCreazione" />&nbsp;</dd>
								    <dt>Data Inserimento</dt>
								    <dd><s:property value="%{selectedSede.dataCreazione}" />&nbsp;</dd>

								</dl>
							</div>
							<div class="modal-footer">
								<button class="btn" data-dismiss="modal" aria-hidden="true">chiudi</button>
							</div>
