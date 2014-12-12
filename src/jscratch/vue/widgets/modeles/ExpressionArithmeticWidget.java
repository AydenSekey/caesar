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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;

import jscratch.helpers.FontHelper;
import jscratch.vue.widgets.Widget;
import jscratch.vue.widgets.modeles.zones.ChampTexte;
import nxtim.instruction.Expression;
import nxtim.instruction.Operateur;
import nxtim.instruction.Operation;
import nxtim.type.TypeElement;
import nxtim.instruction.Variable;
import nxtim.instruction.VariableConstante;

/**
 * Classe heritant de ModeleWidget et implementant Seriliazable modelisant la
 * forme d'un widget de type Expression arithmetique.
 *
 * @since 1.0
 * @version 1.0
 */
public class ExpressionArithmeticWidget extends ModeleWidget {

	private int largeur;
	
	private final int LARG_EXTREMITE;
	
	private ChampTexte f, l;
	
	/**
	 * Constructeur du modele definissant les differents parametres du ExpressionSum .
	 */
	public ExpressionArithmeticWidget(Operateur op) {

		this.setElementProgramme(new Operation(op, (Variable) null, (Variable) null));
		this.attachableBas = false;
		this.attachableHaut = false;
		this.imbricable = false;
		this.attachableInterne = true;
		this.setType(TypeModeleWidget.EXPRESSION_ARITHMETIQUE);

		//graphisme
		this.LARG_EXTREMITE = 5;
		final int LARG_CHAMP = 14;


		l = new ChampTexte(LARG_CHAMP, this);
		l.setBounds(10, 3, LARG_CHAMP, 14);
		l.ajouterTypeWidgetAccepte(TypeModeleWidget.VARIABLE);
		l.ajouterTypeWidgetAccepte(TypeModeleWidget.TEMPSCOURANT);
		l.ajouterTypeWidgetAccepte(TypeModeleWidget.CAPTEUR);
		l.ajouterTypeWidgetAccepte(TypeModeleWidget.EXPRESSION_ARITHMETIQUE);
		l.ajouterTypeWidgetAccepte(TypeModeleWidget.MOTEURNOMBREROTATION);
		this.getLesZonesSaisies().add(l);

		message.put(new Point(29, 15), op.toString());

		Font font = FontHelper.getWidgetFont();
		FontMetrics metrics = new FontMetrics(font) {
		};
		Rectangle2D bounds = metrics.getStringBounds(op.toString(), null);

		f = new ChampTexte(LARG_CHAMP, this);
		f.ajouterTypeWidgetAccepte(TypeModeleWidget.VARIABLE);
		f.ajouterTypeWidgetAccepte(TypeModeleWidget.TEMPSCOURANT);
		f.ajouterTypeWidgetAccepte(TypeModeleWidget.CAPTEUR);
		f.ajouterTypeWidgetAccepte(TypeModeleWidget.EXPRESSION_ARITHMETIQUE);
		f.ajouterTypeWidgetAccepte(TypeModeleWidget.MOTEURNOMBREROTATION);
		f.setBounds(29 + (int) bounds.getWidth() + 5, 3, LARG_CHAMP, 14);
		this.getLesZonesSaisies().add(f);

		this.largeur = LARG_CHAMP * 2 + 24 + (int) bounds.getWidth();
		int tabX[] = {0, 5, this.LARG_EXTREMITE + largeur, largeur + 10, largeur + 10, this.LARG_EXTREMITE + largeur, 5, 0};
		int tabY[] = {5, 0, 0, 5, 15, 20, 20, 15};

		this.setTabX(tabX);
		this.setTabY(tabY);

		this.setForme(new Polygon(this.getTabX(), this.getTabY(), this.getTabX().length));

		this.setTailleX();
		this.setTailleY();
	}
	
	@Override
	public void applyChangeModele() {
		Operation expArith = ((Operation) getElementProgramme());
		
		Widget widgetG = l.getContentWidget();
		if (widgetG != null) {
			Expression varMemGauche = (Expression) widgetG.getElementProgramme();
			expArith.setMembreGauche(varMemGauche);
		} else {
			String val = l.getValeur();
			if(val.isEmpty()) {
				expArith.setMembreGauche(null);
			}
			else {
				expArith.setMembreGauche(new VariableConstante(TypeElement.INT, val));
			}
		}
		
		Widget widgetD = f.getContentWidget();
		if (widgetD != null) {
			Expression varMemDroite = (Expression) widgetD.getElementProgramme();
			expArith.setMembreDroit(varMemDroite);
		} else {
			String val = f.getValeur();
			if(val.isEmpty()) {
				expArith.setMembreDroit(null);
			}
			else {
				expArith.setMembreDroit(new VariableConstante(TypeElement.INT, val));
			}
		}
	}

	@Override
	public void decalageX(int x) {
		int i;
		for (i = 2; i < this.getTabX().length - 2; i++) {
			this.getForme().xpoints[i] = this.getForme().xpoints[i] + x;
		}
		this.setForme(this.getForme());
		this.setTailleX();
	}
}
