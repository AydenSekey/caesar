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
package jscratch.vue.widgets.fabrique;

import java.util.List;
import jscratch.dictionnaires.DicoCouleursCategories;
import jscratch.exceptions.NonChargeableException;
import jscratch.exceptions.NonClonableException;
import jscratch.vue.ginterface.principales.ApplicationUI;
import jscratch.vue.widgets.Widget;
import jscratch.vue.widgets.WidgetCompose;
import jscratch.vue.widgets.modeles.CapteurWidget;
import jscratch.vue.widgets.modeles.ConfigurationCapteursWidget;
import jscratch.vue.widgets.modeles.DeclarationVariableWidget;
import nxtim.instruction.Condition;
import nxtim.instruction.NonLogique;
import nxtim.operateur.Operateur;
import nxtim.instruction.Operation;
import nxtim.instruction.VariableModifiable;
import jscratch.vue.widgets.modeles.DoWhileWidget;

import jscratch.vue.widgets.modeles.ExpressionArithmeticWidget;
import jscratch.vue.widgets.modeles.ExpressionLogicalWidget;
import jscratch.vue.widgets.modeles.ForWidget;
import jscratch.vue.widgets.modeles.IfElseWidget;
import jscratch.vue.widgets.modeles.IfWidget;
import jscratch.vue.widgets.modeles.IncrementationWidget;
import jscratch.vue.widgets.modeles.MoteurMarcheWidget;
import jscratch.vue.widgets.modeles.MoteurNombreRotationWidget;
import jscratch.vue.widgets.modeles.MoteurOffWidget;
import jscratch.vue.widgets.modeles.MoteurRAZWidget;
import jscratch.vue.widgets.modeles.NonLogiqueWidget;
import jscratch.vue.widgets.modeles.RepeatWidget;
import jscratch.vue.widgets.modeles.TacheWidget;
import jscratch.vue.widgets.modeles.TempsCourantWidget;
import jscratch.vue.widgets.modeles.TypeModeleWidget;
import jscratch.vue.widgets.modeles.WaitWidget;
import jscratch.vue.widgets.modeles.VariableWidget;
import jscratch.vue.widgets.modeles.VariableSetValueWidget;
import jscratch.vue.widgets.modeles.WhileWidget;
import nxtim.instruction.Categorie;
import nxtim.instruction.InstructionIncrementation;
import nxtim.instruction.Variable;

/**
 * Classe implémentant le design pattern Factory permettant la création de tous les types de widgets.
 *
 * @since 1.0
 * @version 1.0
 */
public class FabriqueWidgets {

	/**
	 * Méthode permettant de créer un widget de type " Moteur Marche ".
	 *
	 * @return un widget de type " Moteur Marche"
	 */
	public Widget creerWidgetMoteurMarche() {
		return new Widget(new MoteurMarcheWidget());
	}

	/**
	 * Méthode permettant de créer un widget de type "Arrêt Moteur".
	 *
	 * @return un widget de type "Arrêt Moteur"
	 */
	public Widget creerWidgetMoteurOff() {
		return new Widget(new MoteurOffWidget());
	}

	/**
	 * Méthode permettant de créer un widget de type "Nombre de rotation".
	 *
	 * @return un widget de type "Nombre de rotation"
	 */
	public Widget creerWidgetMoteurNombreRotation() {
		return new Widget(new MoteurNombreRotationWidget());
	}

	/**
	 * Méthode permettant de créer un widget de type "Moteur RAZ".
	 *
	 * @return un widget de type "Nombre de rotation"
	 */
	public Widget creerWidgetMoteurRAZ() {
		return new Widget(new MoteurRAZWidget());
	}

	/**
	 * Méthode permettant de créer un widget complexe de type "Boucle Pour".
	 *
	 * @return un widget complexe de type "Boucle Pour"
	 */
	public Widget creerWidgetFor() {
		return new WidgetCompose(new ForWidget());
	}

	/**
	 * Méthode permettant de créer un widget complexe de type "Boucle Tant Que".
	 *
	 * @return un widget complexe de type "Boucle Tant que"
	 */
	public Widget creerWidgetWhile() {
		return new WidgetCompose(new WhileWidget());
	}

	/**
	 * Méthode permettant de créer un widget complexe de type "Boucle Faire Tant Que".
	 *
	 * @return un widget complexe de type "Boucle Faire Tant que"
	 */
	public Widget creerWidgetDoWhile() {
		return new WidgetCompose(new DoWhileWidget());
	}

	/**
	 * Méthode permettant de créer un widget complexe de type "Répéter".
	 *
	 * @return un widget complexe de type "Répéter"
	 */
	public Widget creerWidgetRepeat() {
		return new WidgetCompose(new RepeatWidget());
	}

	/**
	 * Méthode permettant de créer un widget complexe de type "Structure Si".
	 *
	 * @return un widget complexe de type "Structure Si"
	 */
	public Widget creerWidgetIf() {
		return new WidgetCompose(new IfWidget());
	}

	/**
	 * Méthode permettant de créer un widget complexe de type "Structure Si Sinon".
	 *
	 * @return un widget complexe de type "Structure Si Sinon"
	 */
	public Widget creerWidgetIfElse() {
		return new WidgetCompose(new IfElseWidget());
	}

	/**
	 * Méthode permettant de créer un widget de type "Attente".
	 *
	 * @return un widget de type "Attente"
	 */
	public Widget creerWidgetWait() {
		return new Widget(new WaitWidget());
	}

	/**
	 * Méthode permettant de créer un widget de type "Variable".
	 *
	 * @return un widget de type "Variable"
	 */
	public Widget creerWidgetVariable(VariableModifiable variableModifiable) {
		return new Widget(new VariableWidget(variableModifiable));
	}

	/**
	 * Méthode permettant de créer un widget de type "VariableSetValue".
	 *
	 * @return un widget de type "VariableSetValue"
	 */
	public Widget creerWidgetVariableSetValue() {
		return new Widget(new VariableSetValueWidget());
	}

	/**
	 * Permet de créer un widget de type déclaration de variable.
	 *
	 * @since 1.0
	 *
	 * @return le widget
	 */
	public Widget creerWidgetDeclarerVariable() {
		return new Widget(new DeclarationVariableWidget());
	}

	/**
	 * Méthode permettant de créer un widget de type "Temps Courant".
	 *
	 * @return un widget de type "Temps Courant"
	 */
	public Widget creerWidgetTempsCourant() {
		return new Widget(new TempsCourantWidget());
	}

	/**
	 * Méthode permettant de créer un widget complexe de type "Tâche".
	 *
	 * @return un widget complexe de type "Tâche"
	 */
	public Widget creerWidgetTache() {
		return new WidgetCompose(new TacheWidget());
	}

	/**
	 * Méthode permettant de créer un widget complexe de type "Expression arithmétique".
	 *
	 * @return un widget complexe de type "expression arithmétique"
	 */
	public Widget creerWidgetExpressionArithmetic(Operateur op) {
		return new Widget(new ExpressionArithmeticWidget(op));
	}

	/**
	 * Méthode permettant de créer un widget complexe de type "Expression logique".
	 *
	 * @return un widget complexe de type "expression logique"
	 */
	public Widget creerWidgetExpressionLogical(Operateur op) {
		return new Widget(new ExpressionLogicalWidget(op));
	}

	/**
	 * Méthode permettant de créer un widget complexe de type "NonLogique".
	 *
	 * @return un widget complexe de type "non logique"
	 */
	public Widget creerWidgetNonLogique() {
		return new Widget(new NonLogiqueWidget());
	}

	/**
	 * Méthode permettant de créer un widget d'incrémentation (var++ ou var--).
	 *
	 * @param op l'opérateur
	 * @return un widget d'incrémentation
	 */
	public Widget creerWidgetIncrementation(Operateur op) {
		return new Widget(new IncrementationWidget(op, false));
	}

	/**
	 * Crée un widget de récupération de la valeur d'un capteur.
	 *
	 * @return un widget de type "Capteur"
	 */
	public Widget creerWidgetCapteur() {
		return new Widget(new CapteurWidget());
	}

	/**
	 * Crée un widget de configuration des capteurs.
	 *
	 * @return un widget de type "Capteur"
	 */
	public Widget creerWidgetConfigurationCapteur() {
		return new Widget(new ConfigurationCapteursWidget());
	}

	/**
	 * Méthode permettant de créer un widget d'incrémentation.
	 *
	 * @param op l'opérateur
	 * @param isAvant <code>true</code> si l'operateur est avant
	 * @return un widget d'incrémentation
	 * @deprecated les widget d'incrementation avant ne sont pas gérés dans les sauvegardes
	 */
	public Widget creerWidgetIncrementation(Operateur op, final boolean isAvant) {
		return new Widget(new IncrementationWidget(op, isAvant));
	}

	/**
	 * Méthode permettant de créér une copie d'un widget.
	 *
	 * @param comp le widget à cloner
	 * @return la copie du widget passé en paramètre
	 * @throws NonClonableException Si on essaye de cloner un widget qui n'est pas clonable
	 */
	public Widget cloner(final Widget comp) throws NonClonableException {
		Widget w = null;
		TypeModeleWidget type = comp.getModele().getType();

		switch(type) {
			case IF:
				w = creerWidgetIf();
				break;
			case IFELSE:
				w = creerWidgetIfElse();
				break;
			case TACHE:
				w = creerWidgetTache();
				break;
			case WHILE:
				w = creerWidgetWhile();
				break;
			case DOWHILE:
				w = creerWidgetDoWhile();
				break;
			case MOTEURMARCHE:
				w = creerWidgetMoteurMarche();
				break;
			case MOTEURNOMBREROTATION:
				w = creerWidgetMoteurNombreRotation();
				break;
			case MOTEURRAZ:
				w = creerWidgetMoteurRAZ();
				break;
			case MOTEUROFF:
				w = creerWidgetMoteurOff();
				break;
			case WAIT:
				w = creerWidgetWait();
				break;
			case VARIABLE:
				w = creerWidgetVariable((VariableModifiable) comp.getModele().getElementProgramme());
				break;
			case SETVALUEVARIABLE:
				w = creerWidgetVariableSetValue();
				break;
			case DECLARERVARIABLE:
				w = creerWidgetDeclarerVariable();
				break;
			case TEMPSCOURANT:
				w = creerWidgetTempsCourant();
				break;
			case REPEAT:
				w = creerWidgetRepeat();
				break;
			case FOR:
				w = creerWidgetFor();
				break;
			case EXPRESSION_ARITHMETIQUE:
				Operation op = (Operation) comp.getModele().getElementProgramme();
				w = creerWidgetExpressionArithmetic(op.getOperateur());
				break;
			case EXPRESSION_LOGIQUE:
				Condition con = (Condition) comp.getModele().getElementProgramme();
				w = creerWidgetExpressionLogical(con.getOperateur());
				break;
			case NEGATION:
				NonLogique nl = (NonLogique) comp.getModele().getElementProgramme();
				w = creerWidgetNonLogique();
				break;
			case EXPRESSION_INC:
				InstructionIncrementation ins = (InstructionIncrementation) comp.getModele().getElementProgramme();
				if (ins.isPositive()) {
					w = creerWidgetIncrementation(Operateur.INCREMENTATION_PLUS);
				}
				else {
					w = creerWidgetIncrementation(Operateur.INCREMENTATION_MOINS);
				}
				break;
			case CAPTEUR:
				w = creerWidgetCapteur();
				break;
			case CAPTEURCONFIG:
				w = creerWidgetConfigurationCapteur();
				break;
			default:
				throw new NonClonableException("Ajouter le type de widget \"" + comp.getType() + "\"dans la méthode clone");
		}

		w.getModele().setCouleur(comp.getModele().getCouleur());
		w.getModele().setCategorie(comp.getModele().getCategorie());
		w.setBounds(comp.getBounds());

		return w;
	}

	/**
	 * Méthode permettant de créér un widget à partir d'informations données.
	 *
	 * @param nomClasse le nom de la classe du widget
	 * @param categorie la catégorie du widget
	 * @param supplement la variable attachée au widget variable, <code>null</code> si pas un widget variable
	 * @return le widget
	 * @throws NonChargeableException Si on essaye de charger un widget qui n'est pas pris en compte
	 */
	public Widget creerWidget(final String nomClasse, final String categorie, final String supplement) throws NonChargeableException {
		Widget w = null;
		if ("IfWidget".equals(nomClasse)) {
			w = creerWidgetIf();
		} else if ("IfElseWidget".equals(nomClasse)) {
			w = creerWidgetIfElse();
		} else if ("TacheWidget".equals(nomClasse)) {
			w = creerWidgetTache();
		} else if ("WhileWidget".equals(nomClasse)) {
			w = creerWidgetWhile();
		} else if ("DoWhileWidget".equals(nomClasse)) {
			w = creerWidgetDoWhile();
		} else if ("MoteurMarcheWidget".equals(nomClasse)) {
			w = creerWidgetMoteurMarche();
		} else if ("MoteurNombreRotationWidget".equals(nomClasse)) {
			w = creerWidgetMoteurNombreRotation();
		} else if ("MoteurOffWidget".equals(nomClasse)) {
			w = creerWidgetMoteurOff();
		} else if ("WaitWidget".equals(nomClasse)) {
			w = creerWidgetWait();
		} else if ("VariableSetValueWidget".equals(nomClasse)) {
			w = creerWidgetVariableSetValue();
		} else if ("TempsCourantWidget".equals(nomClasse)) {
			w = creerWidgetTempsCourant();
		} else if ("RepeatWidget".equals(nomClasse)) {
			w = creerWidgetRepeat();
		} else if ("ForWidget".equals(nomClasse)) {
			w = creerWidgetFor();
		} else if ("ExpressionArithmeticWidget".equals(nomClasse)) {
			List<Operateur> opes = Operateur.arithmetiques();
			for (Operateur o : opes) {
				if (o.toString().equals(supplement)) {
					w = creerWidgetExpressionArithmetic(o);
					break;
				}
			}
		} else if ("ExpressionLogicalWidget".equals(nomClasse)) {
			List<Operateur> opes = Operateur.logiques();
			for (Operateur o : opes) {
				if (o.toString().equals(supplement)) {
					w = creerWidgetExpressionLogical(o);
					break;
				}
			}
		} else if ("VariableWidget".equals(nomClasse)) {
			Variable[] vars = ApplicationUI.getInstance().getDicoVariables().getLesVariables();
			for (Variable v : vars) {
				if (v.getNom().equals(supplement)) {
					w = creerWidgetVariable((VariableModifiable)v);
					break;
				}
			}
		} else if ("IncrementationWidget".equals(nomClasse)) {
			List<Operateur> opes = Operateur.incrementations();
			for (Operateur o : opes) {
				if (o.toString().equals(supplement)) {
					w = creerWidgetIncrementation(o);
					break;
				}
			}
		} else if("DeclarationVariableWidget".equals(nomClasse)) {
			w = creerWidgetDeclarerVariable();
		} else if("MoteurRAZWidget".equals(nomClasse)) {
			w = creerWidgetMoteurRAZ();
		} else if ("ConfigurationCapteursWidget".equals(nomClasse)) {
			w = creerWidgetConfigurationCapteur();
		} else if("CapteurWidget".equals(nomClasse)) {
			w = creerWidgetCapteur();
		} else {
			throw new NonChargeableException("Impossible de charger le widget " + nomClasse);
		}

		// On remet la couleur du widget suivant sa catégorie
		for (Categorie c : Categorie.values()) {
			if (c.toString().equals(categorie)) {
				w.getModele().setCategorie(c);
				w.getModele().setCouleur(DicoCouleursCategories.getInstance().getCouleur(c));
				break;
			}
		}

		return w;
	}
}