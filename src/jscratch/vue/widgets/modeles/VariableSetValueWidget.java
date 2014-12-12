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

import nxtim.instruction.Affectation;
import nxtim.instruction.Expression;
import nxtim.instruction.VariableModifiable;
import jscratch.vue.widgets.Widget;
import jscratch.vue.widgets.modeles.zones.ChampTexte;
import nxtim.type.TypeElement;
import nxtim.instruction.VariableConstante;

/**
 *
 * @since 1.0
 * @version 1.0
 */
public class VariableSetValueWidget extends ModeleWidget {
	private ChampTexte g,d;
	/**
	 * Constructeur du modèle définissant les différents paramètres du Variable.
	 */
	public VariableSetValueWidget() {
		int tX[] = {0, 5, 30, 35, 45, 50, 170, 175, 175, 170, 50, 45, 35, 30, 5, 0};
		int tY[] = {5, 0, 0, 5, 5, 0, 0, 5, 20, 25, 25, 30, 30, 25, 25, 20};

		this.setTabX(tX);
		this.setTabY(tY);
		this.setTailleX();
		this.setTailleY();
		this.setType(TypeModeleWidget.SETVALUEVARIABLE);

		message.put(new Point(5, 17), "Mettre");
		message.put(new Point(95, 17), "dans");

		this.setForme(new Polygon(this.getTabX(), this.getTabY(), this.getTabX().length));

		int widthChamp = 35;
		g = new ChampTexte(widthChamp, this);
		g.ajouterTypeWidgetAccepte(TypeModeleWidget.VARIABLE);
		g.ajouterTypeWidgetAccepte(TypeModeleWidget.TEMPSCOURANT);
		g.ajouterTypeWidgetAccepte(TypeModeleWidget.CAPTEUR);
		g.ajouterTypeWidgetAccepte(TypeModeleWidget.EXPRESSION_ARITHMETIQUE);
		g.ajouterTypeWidgetAccepte(TypeModeleWidget.MOTEURNOMBREROTATION);
		g.setBounds(55, 3, widthChamp, 20);
		g.setValeur("0");
		this.getLesZonesSaisies().add(g);

		widthChamp = 40;
		d = new ChampTexte(widthChamp, this);
		d.ajouterTypeWidgetAccepte(TypeModeleWidget.VARIABLE);
		d.setBounds(128, 3, widthChamp, 20);
		d.supprimerTexte();
		this.getLesZonesSaisies().add(d);
		
		this.setElementProgramme(new Affectation(true));
	}

	@Override
	public void applyChangeModele() {
		if (g != null && d != null) {
			Widget contentWidget = g.getContentWidget();
			Widget contentWidgetVar = d.getContentWidget();

			Affectation setValueIns = ((Affectation) getElementProgramme());

			// On met à jour le contenu dans l'elementProgramme
			if (contentWidget != null) {
				Expression expComp = (Expression) contentWidget.getElementProgramme();
				setValueIns.setMembreDroit(expComp);
			}
			else {
				setValueIns.setMembreDroit(new VariableConstante(TypeElement.INT, g.getValeur()));
			}
			if (contentWidgetVar != null) {
				VariableModifiable var  = (VariableModifiable) contentWidgetVar.getElementProgramme();
				setValueIns.setMembreGauche(var);
				setValueIns.setIsInstruction(true);
			}
			else {
				setValueIns.setMembreGauche(new VariableConstante(TypeElement.INT, d.getValeur()));
			}
		}
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

	/**
	 * Permet de récupérer le nom de la variable.
	 * 
	 * @return le nom de variable
	 */
	public String getNomVariable() {
		return ((VariableModifiable)this.getElementProgramme()).getNom();
	}
}
