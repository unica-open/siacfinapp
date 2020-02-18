<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="clear"></div>
<br />
<div class="collapse" id="collapseDatiStruttura">
	<div class="step-content">
		<div class="step-pane active" id="step1">
			<h4 class="step-pane">Dati struttura</h4>
			<fieldset id="fieldsetCollapseDatiStruttura">
				<s:hidden name="segnoCollapse" id="segnoCollapse" />
				<div class="control-group">
					<label class="control-label">Conto</label>
					<div class="controls">
						<s:textfield id="codiceConto" name="conto.codice" cssClass="span3" />
						<span id="descrizioneConto"></span>
						<button type="button" class="btn btn-primary pull-right" id="pulsanteCompilazioneGuidataConto">compilazione guidata</button>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" >Segno Conto *</label>
					<div class="controls">
						<label class="radio inline">
							Dare <input type="radio" name="operazioneSegnoContoCollapse" value="DARE" data-maintain />
						</label>
						<label class="radio inline">
							Avere <input type="radio" name="operazioneSegnoContoCollapse" value="AVERE" data-maintain />
						</label>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">Importo *</label>
					<div class="controls">
						<s:textfield id="importoCollapse" name="importoCollapse" cssClass="span3 soloNumeri decimale required"/>
					</div>
				</div>
			</fieldset>
			<div class="Border_line"></div>
			<p>
				<button type="button" aria-controls="collapseInserimentoConto" aria-expanded="false" data-toggle="collapse" class="btn btn-secondary" data-target="#collapseDatiStruttura">annulla</button>
				<span class="pull-right">
					<button type="button" class="btn btn-primary" id="pulsanteSalvaInserimentoConto">salva inserimento</button>
				</span>
			</p>
		</div>
	</div>
</div>