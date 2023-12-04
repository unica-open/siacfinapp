/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.exception;


/**
 * Eccezione rilanciata in caso di errori generici  
 * 
 *
 */
public class ApplicationException extends Exception {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6192059243647355106L;

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(String message) {
		super(message);
	}
	
}
