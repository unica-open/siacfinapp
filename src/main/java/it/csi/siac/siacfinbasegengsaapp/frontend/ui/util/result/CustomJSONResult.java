/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinbasegengsaapp.frontend.ui.util.result;

import org.apache.struts2.json.JSONResult;

/**
 * Result for working with JSON data. Gives sensible defaults for some of the properties.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2014
 *
 */
public class CustomJSONResult extends JSONResult {
	
	/** For serialization purpose */
	private static final long serialVersionUID = 5007655031236727633L;

	/** Empty default constructor */
	public CustomJSONResult() {
		super();
		setIgnoreHierarchy(false);
		setEnumAsBean(true);
		setExcludeNullProperties(true);
	}
	
}
