/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.exception;

public class ConfigurationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ConfigurationException() {
		//costruttore semplice
	}

	public ConfigurationException(String message, Throwable cause) {
		//costruttore con message e cause
		super(message, cause);
	}

	public ConfigurationException(String message) {
		//costruttore con message
		super(message);
	}

	public ConfigurationException(Throwable cause) {
		//costruttore con cause
		super(cause);
	}
}
