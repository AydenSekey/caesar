/*
Copyright (C) Université du Maine (2013) 

contributeurs : Adrien Duroy, Bastien Andru, Quentin Gosselin, Guillaume Delorme,
 Nicolas Detan, Zubair Parwany, Houda Chouket, Bastien Aubry,
 Vincent Besnard, Ivan Melnychenko, Gwendal Martin

ad.duroy@gmail.com

Ce fichier est une partie du logiciel CAESAR.

CAESAR est un programme informatique servant à construire un programme
pour un robot NXT et à effectuer une simulation de l'exécution de ce dernier. 

CAESAR est régi par la licence CeCILL soumise au droit français et
respectant les principes de diffusion des logiciels libres. Vous pouvez
utiliser, modifier et/ou redistribuer ce programme sous les conditions
de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
sur le site "http://www.cecill.info".

En contrepartie de l'accessibilité au code source et des droits de copie,
de modification et de redistribution accordés par cette licence, il n'est
offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
seule une responsabilité restreinte pèse sur l'auteur du programme,  le
titulaire des droits patrimoniaux et les concédants successifs.

A cet égard  l'attention de l'utilisateur est attirée sur les risques
associés au chargement,  à l'utilisation,  à la modification et/ou au
développement et à la reproduction du logiciel par l'utilisateur étant 
donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
manipuler et qui le réserve donc à des développeurs et des professionnels
avertis possédant  des  connaissances  informatiques approfondies.  Les
utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
logiciel à leurs besoins dans des conditions permettant d'assurer la
sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 

Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
pris connaissance de la licence CeCILL, et que vous en avez accepté les
termes.
 */
package jscratch.vue.widgets.modeles;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import jscratch.vue.widgets.Widget;
import jscratch.vue.widgets.modeles.zones.ChampTexte;
import jscratch.vue.widgets.modeles.zones.ListeDeroulante;
import nxtim.instruction.Expression;
import nxtim.instruction.InstructionMoteurMov;
import nxtim.instruction.Moteur;
import nxtim.type.TypeElement;
import nxtim.instruction.VariableConstante;

/**
 * @since 1.0
 * @version 1.0
 */
public class MoteurMarcheWidget extends ModeleWidget {

	private ChampTexte f;
	private ListeDeroulante<Moteur> l;
	
	public MoteurMarcheWidget(){
		super();
		int tX[] = {0, 5, 30, 35, 45, 50, 183, 188, 188, 183, 50, 45, 35, 30, 5, 0};
		int tY[] = {5, 0, 0, 5, 5, 0, 0, 5, 20, 25, 25, 30, 30, 25, 25, 20};

		this.setTabX(tX);
		this.setTabY(tY);
		this.setTailleX();
		this.setTailleY();
		this.setType(TypeModeleWidget.MOTEURMARCHE);
		message.put(new Point(5, 17), "Moteur");
		message.put(new Point(94, 17), "puissance");

		this.setElementProgramme(new InstructionMoteurMov());
		this.setForme(new Polygon(this.getTabX(), this.getTabY(), this.getTabX().length));

		l = new ListeDeroulante<Moteur>(Moteur.values(), this);
		l.setBounds(55, 3, 35, 20);
		this.getLesZonesSaisies().add(l);

		int widthChamp = 40;
		f = new ChampTexte(widthChamp, this);

		f.ajouterTypeWidgetAccepte(TypeModeleWidget.VARIABLE);
		f.ajouterTypeWidgetAccepte(TypeModeleWidget.TEMPSCOURANT);
		f.ajouterTypeWidgetAccepte(TypeModeleWidget.CAPTEUR);
		f.ajouterTypeWidgetAccepte(TypeModeleWidget.EXPRESSION_ARITHMETIQUE);
		f.setBounds(155, 3, widthChamp, 20);
		f.setValeur("0");
		this.getLesZonesSaisies().add(f);

		setInstructionMoteur(l.getValeur());
		setInstructionValeur(f.getValeur());

		this.decalageX(11);

		initListeners();
	}
	
	@Override
	public void applyChangeModele(){
		Widget contentWidget = f.getContentWidget();
		InstructionMoteurMov moteurMarcheIns = ((InstructionMoteurMov) getElementProgramme());
		setInstructionMoteur(l.getValeur());
		
		// On met à jour la condition dans l'elementProgramme si elle existe
		if (contentWidget != null) {
			Expression exp = (Expression) contentWidget.getElementProgramme();
			moteurMarcheIns.setExpression(exp);
		} else {
			moteurMarcheIns.setExpression(new VariableConstante(TypeElement.INT, f.getValeur()));
		}
	}

	@Override
	public void decalageX(int a) {
		int i;
		for (i = 6; i < 10; i++) {
			this.getForme().xpoints[i] = this.getForme().xpoints[i] + a;
		}
		this.setForme(this.getForme());
		this.setTailleX();
	}

	@Override
	public void decalageY(int b, Rectangle r) {
		int i;
		for (i = 8; i < 16; i++) {
			this.getForme().ypoints[i] = this.getForme().ypoints[i] + b;
		}
		this.setForme(this.getForme());
		this.setTailleY();
	}

	@Override
	public void initListeners() {
		l.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				setInstructionMoteur(l.getValeur());
			}
		});
		f.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				setInstructionValeur(f.getValeur());
			}
		});
	}

	/**
	 * Méthode permettant de définir l'instruction moteur du widget.
	 *
	 * @param nom L'instruction Moteur à définir sur le modèle
	 */
	private void setInstructionMoteur(String nom) {
		((InstructionMoteurMov) getElementProgramme()).setMoteur(Moteur.values()[Integer.parseInt(nom)]);
	}

	/**
	 * Méthode permettant de définir la valeur de l'instruction moteur
	 *
	 * @param nom La valeur de l'instruction moteur
	 */
	private void setInstructionValeur(String nom) {
		((InstructionMoteurMov) getElementProgramme()).setReverse(false);
		((InstructionMoteurMov) getElementProgramme()).setExpression(new VariableConstante(TypeElement.INT, "", nom));
	}
}
