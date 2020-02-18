<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>
<%@taglib prefix="s" uri="/struts-tags"%>


<s:set var="initializedStringMPS" value="false" />

<s:if test='%{#this.iban != null && !"".equals(#this.iban)}'>
	iban: <s:property value="#this.iban" />
	<s:set var="initializedStringMPS" value="true" />
</s:if>
<s:if test='%{#this.bic != null && !"".equals(#this.bic)}'>
	<s:if test="%{#initializedStringMPS == true}"> - </s:if>bic: <s:property value="#this.bic" />
	<s:set var="initializedStringMPS" value="true" />
</s:if>
<s:if test='%{#this.contoCorrente != null && !"".equals(#this.contoCorrente)}'>
	<s:if test="%{#initializedStringMPS == true}"> - </s:if>conto: <s:property value="#this.contoCorrente" />
	<s:set var="initializedStringMPS" value="true" />
</s:if>
<s:if test='%{#this.intestazioneConto != null && !"".equals(#this.intestazioneConto)}'>
	<s:if test="%{#initializedStringMPS == true}"> - </s:if>intestato a: <s:property value="#this.intestazioneConto" />
	<s:set var="initializedStringMPS" value="true" />
</s:if>
<s:if test='%{#this.soggettoQuietanzante != null && !"".equals(#this.soggettoQuietanzante)}'>
	<s:if test="%{#initializedStringMPS == true}"> - </s:if>quietanzante: <s:property value="#this.soggettoQuietanzante" />
	<s:set var="initializedStringMPS" value="true" />
</s:if>
<s:if test='%{#this.codiceFiscaleQuietanzante != null && !"".equals(#this.codiceFiscaleQuietanzante)}'>
	<s:if test="%{#initializedStringMPS == true}"> - </s:if>CF: <s:property value="#this.codiceFiscaleQuietanzante" />
	<s:set var="initializedStringMPS" value="true" />
</s:if>
<s:if test='%{#this.dataNascitaQuietanzante != null && !"".equals(#this.dataNascitaQuietanzante)}'>
	<s:if test="%{#initializedStringMPS == true}"> - </s:if>nato il <s:property value="#this.dataNascitaQuietanzante" /> a <s:property value="#this.luogoNascitaQuietanzante" />, <s:property value="#this.statoNascitaQuietanzante" />
</s:if>

<s:if test="%{#this.modalitaPagamentoSoggettoCessione2 != null && #this.modalitaPagamentoSoggettoCessione2.uid != 0}">
	<s:if test='%{#this.modalitaPagamentoSoggettoCessione2.iban != null && !"".equals(#this.modalitaPagamentoSoggettoCessione2.iban)}'>
		iban: <s:property value="#this.modalitaPagamentoSoggettoCessione2.iban" />
		<s:set var="initializedStringMPS" value="true" />
	</s:if>
	<s:if test='%{#this.modalitaPagamentoSoggettoCessione2.bic != null && !"".equals(#this.modalitaPagamentoSoggettoCessione2.bic)}'>
		<s:if test="%{#initializedStringMPS == true}"> - </s:if>bic: <s:property value="#this.modalitaPagamentoSoggettoCessione2.bic" />
		<s:set var="initializedStringMPS" value="true" />
	</s:if>
	<s:if test='%{#this.modalitaPagamentoSoggettoCessione2.contoCorrente != null && !"".equals(#this.modalitaPagamentoSoggettoCessione2.contoCorrente)}'>
		<s:if test="%{#initializedStringMPS == true}"> - </s:if>conto: <s:property value="#this.modalitaPagamentoSoggettoCessione2.contoCorrente" />
		<s:set var="initializedStringMPS" value="true" />
	</s:if>
	<s:if test='%{#this.modalitaPagamentoSoggettoCessione2.intestazioneConto != null && !"".equals(#this.modalitaPagamentoSoggettoCessione2.intestazioneConto)}'>
		<s:if test="%{#initializedStringMPS == true}"> - </s:if>intestato a: <s:property value="#this.modalitaPagamentoSoggettoCessione2.intestazioneConto" />
		<s:set var="initializedStringMPS" value="true" />
	</s:if>
	<s:if test='%{#this.modalitaPagamentoSoggettoCessione2.soggettoQuietanzante != null && !"".equals(#this.modalitaPagamentoSoggettoCessione2.soggettoQuietanzante)}'>
		<s:if test="%{#initializedStringMPS == true}"> - </s:if>quietanzante: <s:property value="#this.modalitaPagamentoSoggettoCessione2.soggettoQuietanzante" />
		<s:set var="initializedStringMPS" value="true" />
	</s:if>
	<s:if test='%{#this.modalitaPagamentoSoggettoCessione2.codiceFiscaleQuietanzante != null && !"".equals(#this.modalitaPagamentoSoggettoCessione2.codiceFiscaleQuietanzante)}'>
		<s:if test="%{#initializedStringMPS == true}"> - </s:if>CF: <s:property value="#this.modalitaPagamentoSoggettoCessione2.codiceFiscaleQuietanzante" />
		<s:set var="initializedStringMPS" value="true" />
	</s:if>
	<s:if test='%{#this.modalitaPagamentoSoggettoCessione2.dataNascitaQuietanzante != null && !"".equals(#this.modalitaPagamentoSoggettoCessione2.dataNascitaQuietanzante)}'>
		<s:if test="%{#initializedStringMPS == true}"> - </s:if>nato il <s:property value="#this.modalitaPagamentoSoggettoCessione2.dataNascitaQuietanzante" /> a <s:property value="#this.modalitaPagamentoSoggettoCessione2.luogoNascitaQuietanzante" />, <s:property value="#this.modalitaPagamentoSoggettoCessione2.statoNascitaQuietanzante" />
	</s:if>
</s:if>
<%--s:property value="%{#this.modalitaAccreditoSoggetto.codice}" /> - <s:property value="%{#this.modalitaAccreditoSoggetto.descrizione}" /--%>
