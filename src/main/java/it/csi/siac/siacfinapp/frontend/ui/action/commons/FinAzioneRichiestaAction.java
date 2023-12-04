/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action.commons;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccommonapp.action.AzioneRichiestaAction;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class FinAzioneRichiestaAction extends AzioneRichiestaAction {
	private static final long serialVersionUID = 1L;

}
