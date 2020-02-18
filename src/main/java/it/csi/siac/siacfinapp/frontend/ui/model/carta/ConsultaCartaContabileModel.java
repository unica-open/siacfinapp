/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.model.carta;

import it.csi.siac.siacfinapp.frontend.ui.model.GenericPopupModel;
import it.csi.siac.siacfinser.model.carta.CartaContabile;


public class ConsultaCartaContabileModel extends GenericPopupModel {
	
	private static final long serialVersionUID = 1L;
	
	private CartaContabile cartaContabile;
	private String activeTab = "cartaContabile";
	
	private String intestazioneFirma1;
	private String intestazioneFirma2;

	/* **************************************************************************** */
	/*   Getter / Setter															*/
	/* **************************************************************************** */
	
	public CartaContabile getCartaContabile() {
		return cartaContabile;
	}

	public void setCartaContabile(CartaContabile cartaContabile) {
		this.cartaContabile = cartaContabile;
	}

	public String getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(String activeTab) {
		this.activeTab = activeTab;
	}

	public String getIntestazioneFirma1() {
		return intestazioneFirma1;
	}

	public void setIntestazioneFirma1(String intestazioneFirma1) {
		this.intestazioneFirma1 = intestazioneFirma1;
	}

	public String getIntestazioneFirma2() {
		return intestazioneFirma2;
	}

	public void setIntestazioneFirma2(String intestazioneFirma2) {
		this.intestazioneFirma2 = intestazioneFirma2;
	}
	
}
