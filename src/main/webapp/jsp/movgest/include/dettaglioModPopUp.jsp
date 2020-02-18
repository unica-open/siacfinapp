<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="display" uri="/display-tags"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
	<h4>Modifica <s:property value="modificaDettaglio.numero" /> -<s:if test="modificaDettaglio.descSub == null">Associato all'<s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/></s:if><s:else>Associato al Sub <s:property value="%{labels.OGGETTO_GENERICO_PADRE}"/></s:else>  - <s:property value="modificaDettaglio.descrizione" /></h4>
</div>
<div class="modal-body">
	<fieldset class="form-horizontal">
		<p>Data inserimento: <s:property value="%{modificaDettaglio.dataInserimento}" /> - Data ultima modifica: <s:property value="%{modificaDettaglio.dataModifica}" /></p>
		<dl class="dl-horizontal">
			<dt>Inserito</dt>
			<dd>il <s:property value="%{modificaDettaglio.dataInserimento}" /> da <s:property value="modificaDettaglio.utenteCreazione" />&nbsp;</dd>
			<dt>Aggiornato</dt>
			<dd>il <s:property value="%{modificaDettaglio.dataModifica}" /> da <s:property value="modificaDettaglio.utenteModifica" />&nbsp;</dd>
			<dt>Stato</dt>
			<dd><s:property value="modificaDettaglio.statoOperativo" /> dal <s:property value="%{modificaDettaglio.dataStatoOperativo}" />&nbsp;</dd>
			<dt>Dati <s:property value="modificaDettaglio.descMovimentoLower" />o</dt>
			<dd><s:property value="modificaDettaglio.descMain" />&nbsp;</dd>
		<s:if test="modificaDettaglio.descSub != null">
			<dt>Dati sub<s:property value="modificaDettaglio.descMovimentoLower" />o</dt>
			<dd><s:property value="modificaDettaglio.descSub" />&nbsp;</dd>
		</s:if>
			<dt>Provvedimento</dt>
			<dd>
			<s:if test="modificaDettaglio.provvedimento.tipo != null">
				<s:property value="modificaDettaglio.provvedimento.anno" /> / <s:property value="modificaDettaglio.provvedimento.numero" /> - <s:property value="modificaDettaglio.provvedimento.tipo" /> - <s:property value="modificaDettaglio.provvedimento.oggetto" /> - <s:property value="modificaDettaglio.provvedimento.struttura" /> - Stato: <s:property value="modificaDettaglio.provvedimento.stato" /> 
				- Blocco: di operativit&agrave; contabile del provvedimento:&nbsp;
				<!--  - <s:if test="modificaDettaglio.provvedimento.bloccoRagioneria=='true'">Si</s:if>
							<s:if test="modificaDettaglio.provvedimento.bloccoRagioneria=='false'">No</s:if>
							<s:if test="modificaDettaglio.provvedimento.bloccoRagioneria=='null'">N/A</s:if> &nbsp; -->
			</s:if>
			&nbsp;
			</dd>
			<dt>Motivo</dt>
			<dd><s:property value="modificaDettaglio.motivo" />&nbsp;</dd>
			
		<s:if test="modificaDettaglio.importo != null">
			<dt>Importo modifica</dt>
			<dd><s:property value="getText('struts.money.format', {modificaDettaglio.importo})" /></dd>
		</s:if>	
		<s:else>	
		<s:if test="modificaDettaglio.soggettoAttuale.codice != null">
			<dt>Soggetto attuale</dt>
			<dd>
				<s:property value="modificaDettaglio.soggettoAttuale.codice" /> - <s:property value="modificaDettaglio.soggettoAttuale.denominazione" />
			<s:if test="modificaDettaglio.soggettoAttuale.codiceFiscale != null"> - CF: <s:property value="modificaDettaglio.soggettoAttuale.codiceFiscale" /> </s:if>
			<s:if test="modificaDettaglio.soggettoAttuale.partitaIva != null"> - PIVA: <s:property value="modificaDettaglio.soggettoAttuale.partitaIva" /> </s:if> 
			&nbsp;
			</dd>
		</s:if>
		<s:elseif test="modificaDettaglio.soggettoAttuale.classeSoggettoCodice != null">
			<dt>Classe soggetto attuale</dt>
			<dd><s:property value="modificaDettaglio.soggettoAttuale.classeSoggettoCodice" /> - <s:property value="modificaDettaglio.soggettoAttuale.classeSoggettoDescrizione" />&nbsp;</dd>
		</s:elseif> 
		<s:if test="modificaDettaglio.soggettoPrec.codice != null">
			<dt>Soggetto precedente</dt>
			<dd>
				<s:property value="modificaDettaglio.soggettoPrec.codice" /> - <s:property value="modificaDettaglio.soggettoPrec.denominazione" />
			<s:if test="modificaDettaglio.soggettoPrec.codiceFiscale != null"> - CF: <s:property value="modificaDettaglio.soggettoPrec.codiceFiscale" /> </s:if>
			<s:if test="modificaDettaglio.soggettoPrec.partitaIva != null"> - PIVA: <s:property value="modificaDettaglio.soggettoPrec.partitaIva" /> </s:if> 
			&nbsp;
			</dd>
		</s:if>
		<s:elseif test="modificaDettaglio.soggettoPrec.classeSoggettoCodice != null">
			<dt>Classe soggetto precedente</dt>
			<dd><s:property value="modificaDettaglio.soggettoPrec.classeSoggettoCodice" /> - <s:property value="modificaDettaglio.soggettoPrec.classeSoggettoDescrizione" />&nbsp;</dd>
		</s:elseif>
		<s:else>
			<dt>Soggetto precedente</dt>
			<dd>&nbsp;</dd>
		</s:else>
		</s:else>
		
		<!-- SOLO PER MODIFICHE DI IMPORTO: -->
		<s:if test="modificaDettaglio.importo != null">
			<dt>Reimputazione</dt>
			<dd><s:property value="modificaDettaglio.reimputazione" /></dd>
			<s:if test="modificaDettaglio.annoReimputazione != null">
				<dt>Anno reimputazione</dt>
				<dd><s:property value="modificaDettaglio.annoReimputazione" /></dd>
			</s:if>
		</s:if>	
	
		</dl>
	</fieldset>
</div>
<div class="modal-footer">
	<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
</div>