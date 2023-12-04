/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.exception;

///SIAC-7349 GM 01/07/2020
public class ImportoDeltaException extends Exception {
	
	private static final long serialVersionUID = 6192059243647355106L;
	
	public ImportoDeltaException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImportoDeltaException(String message) {
		super(message);
	}
}
