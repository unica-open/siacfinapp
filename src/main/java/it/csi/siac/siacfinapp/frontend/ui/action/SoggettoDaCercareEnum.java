/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.action;

//per indicare a quale modal di ricerca soggetto 
//fare riferimento nei (rari) scenari
//in cui nella stessa action si debbano cercare piu' di un soggetto
public enum SoggettoDaCercareEnum {
	
	UNO(1), DUE(2);
	
    private final int value;

    private SoggettoDaCercareEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static int getValue(SoggettoDaCercareEnum se) {
        return se.getValue();
    }

}; 