/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinapp.frontend.ui.util;


import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccorser.model.Azione;
import it.csi.siac.siaccorser.model.AzioneRichiesta;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.ParametroAzioneRichiesta;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

public class FakeObjectFactory {

	private static int seq = 0;

	private static int getSequenceNextVal() {
		return ++seq;
	}

	public static Ente getEnte() {

		Ente ente = new Ente();

		ente.setUid(1);

		return ente;
	}

	public static AzioneRichiesta getAzioneRichiesta() {

		AzioneRichiesta ar = new AzioneRichiesta();

		List<ParametroAzioneRichiesta> parametri = new ArrayList<ParametroAzioneRichiesta>();

		ParametroAzioneRichiesta p = new ParametroAzioneRichiesta();

		p.setNome("annoEsercizio");
		p.setValore("2012");

		parametri.add(p);

		ar.setParametri(parametri);

		ar.setAzione(new Azione());

		return ar;
	}

	public static Richiedente getRichiedente() {
		Richiedente r = new Richiedente();

		return r;
	}
/*
	public static RicercaSinteticaCapitoloUscitaPrevisioneResponse getRicercaSinteticaCapitoloUscitaPrevisioneResponse() {

		RicercaSinteticaCapitoloUscitaPrevisioneResponse x = new RicercaSinteticaCapitoloUscitaPrevisioneResponse();


		ListaPaginata<CapitoloUscitaPrevisione> l = new ListaPaginataImpl<CapitoloUscitaPrevisione>();

		for (int i = 0; i < 3; i++)
			l.add(getCapitoloUscitaPrevisione());

		x.setListaCapitoloUscitaPrevisione(l);

		return x;
	}

	protected static CapitoloUscitaPrevisione getCapitoloUscitaPrevisione() {
		CapitoloUscitaPrevisione c = new CapitoloUscitaPrevisione();

		c.setNumeroCapitolo(getSequenceNextVal());
		c.setDescrizione("descrizione " + getSequenceNextVal());

		return c;
	}

	public static List<Missione> getListaMissione() {

		List<Missione> ltc = new ArrayList<Missione>();

		for (int i = 0; i < 5; i++) {

			Missione missione = new Missione(StringUtils.leftPad(
					String.valueOf(i), 2, '0'), "Missione uid " + i);

			missione.setUid(getSequenceNextVal());

			ltc.add(missione);
		}

		return ltc;
	}

	public static List<TitoloSpesa> getListaTitoloSpesa() {

		List<TitoloSpesa> ltc = new ArrayList<TitoloSpesa>();

		for (int i = 0; i < 5; i++) {

			TitoloSpesa titoloSpesa = new TitoloSpesa(StringUtils.leftPad(
					String.valueOf(i), 2, '0'), "Titolo Spesa uid " + i);

			titoloSpesa.setUid(getSequenceNextVal());

			ltc.add(titoloSpesa);
		}

		return ltc;
	}

	public static List<ClassificazioneCofog> getListaCofog() {
		List<ClassificazioneCofog> ltc = new ArrayList<ClassificazioneCofog>();

		for (int i = 0; i < 5; i++) {

			ClassificazioneCofog classificazioneCofog = new ClassificazioneCofog(
					StringUtils.leftPad(String.valueOf(i), 2, '0'),
					"Classificazione Cofog uid " + i);

			classificazioneCofog.setUid(getSequenceNextVal());

			ltc.add(classificazioneCofog);
		}

		return ltc;
	}

	public static List<Programma> getListaProgramma(int uidMissione) {

		List<Programma> l = new ArrayList<Programma>();

		for (int i = 0; i < 5; i++) {

			Programma programma = new Programma(StringUtils.leftPad(
					String.valueOf(i), 2, '0'), "Programma " + i
					+ " Missione uid " + uidMissione);

			programma.setUid(getSequenceNextVal());

			l.add(programma);
		}

		return l;
	}

	public static List<Macroaggregato> getListaMacroaggregato(int uidTitoloSpesa) {

		List<Macroaggregato> l = new ArrayList<Macroaggregato>();

		for (int i = 0; i < 5; i++) {

			Macroaggregato m = new Macroaggregato(StringUtils.leftPad(
					String.valueOf(i), 2, '0'), "Macroaggregato " + i
					+ " Titolo spesa uid " + uidTitoloSpesa);

			m.setUid(getSequenceNextVal());

			l.add(m);
		}

		return l;
	}

	public static List<TipoFondo> getListaTipoFondo() {

		List<TipoFondo> ltc = new ArrayList<TipoFondo>();

		for (int i = 0; i < 5; i++) {

			TipoFondo tf = new TipoFondo(StringUtils.leftPad(
					String.valueOf(i), 2, '0'), "Tipo Fondo uid " + i);

			tf.setUid(getSequenceNextVal());

			ltc.add(tf);
		}

		return ltc;
	}
	
	public static List<TipoFinanziamento> getListaTipoFinanziamento() {

		List<TipoFinanziamento> ltc = new ArrayList<TipoFinanziamento>();

		for (int i = 0; i < 5; i++) {

			TipoFinanziamento tf = new TipoFinanziamento(StringUtils.leftPad(
					String.valueOf(i), 2, '0'), "Tipo Finanziamento uid " + i);

			tf.setUid(getSequenceNextVal());

			ltc.add(tf);
		}

		return ltc;
	}
	
	public static List<ElementoPianoDeiConti> getListaPianoDeiConti(
			int uidMacroaggregato) {
		List<ElementoPianoDeiConti> l = new ArrayList<ElementoPianoDeiConti>();

		l.add(getPianoDeiConti(3));

		l.add(getPianoDeiConti(2));

		return l;
	}

	private static ElementoPianoDeiConti getPianoDeiConti(int level) {
		int n = getSequenceNextVal();

		ElementoPianoDeiConti x = new ElementoPianoDeiConti(String.valueOf(n),
				"Piano dei conti n. " + n);
		
		x.setUid(n);

		if (level > 0) {
			x.getElemPdc().add(getPianoDeiConti(level - 1));
			x.getElemPdc().add(getPianoDeiConti(level - 1));
			x.getElemPdc().add(getPianoDeiConti(level - 1));
		}

		return x;
	}
	
	*/

	public static List<StrutturaAmministrativoContabile> getListaStrutturaAmministrativoContabile() {
		
		List<StrutturaAmministrativoContabile> l = new ArrayList<StrutturaAmministrativoContabile>();

		l.add(getStrutturaAmministrativoContabile(2));

		l.add(getStrutturaAmministrativoContabile(1));

		return l;
	}

	private static StrutturaAmministrativoContabile getStrutturaAmministrativoContabile(
			int level) {
		int n = getSequenceNextVal();

		StrutturaAmministrativoContabile x = new StrutturaAmministrativoContabile(String.valueOf(n),
				"StrutturaAmministrativoContabile n. " + n);
		
		x.setUid(n);

		if (level > 0) {
			x.getSubStrutture().add(getStrutturaAmministrativoContabile(level - 1));
			x.getSubStrutture().add(getStrutturaAmministrativoContabile(level - 1));
			x.getSubStrutture().add(getStrutturaAmministrativoContabile(level - 1));
		}

		return x;
	}

}
