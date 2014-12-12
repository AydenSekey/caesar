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

import jscratch.vue.ginterface.principales.ApplicationUI;
import jscratch.vue.widgets.Widget;
import jscratch.vue.widgets.modeles.zones.ChampTexte;
import jscratch.vue.widgets.modeles.zones.Zone;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JComponent;
import jscratch.parametrages.properties.VariableProperties;
import nxtim.instruction.Affectation;
import nxtim.instruction.Condition;
import nxtim.instruction.IElementProgramme;
import nxtim.instruction.InstructionFor;
import nxtim.instruction.InstructionIncrementation;
import nxtim.instruction.NonLogique;
import nxtim.instruction.Operateur;
import nxtim.type.TypeElement;
import nxtim.instruction.Variable;
import nxtim.instruction.VariableConstante;
import nxtim.instruction.VariableModifiable;

/**
 * Classe héritant de ModeleWidget et implémentant <code>Serializable</code> modélisant la forme d'un widget de type For.
 */
public class ForWidget extends ModeleWidget {

	private ChampTexte lv, ff, fp;
    
	/**
     * Constructeur du modele définissant les différents paramètres du For.
     */
    public ForWidget() {
        int[] tX = {0, 5,/**/ 30, 35, 45, 50,/**/ 130, 135,/**/ 135, 130,/**/ 55, 50, 40, 35, /**/ 10, 5,/**/ 5, 10,/**/ 35, 40, 50, 55,/**/ 130, 135, /**/ 135, 130,/**/ 50, 45, 35, 30,/**/ 5, 0};
        int[] tY = {5, 0,/**/ 0, 5, 5, 0,/**/ 0, 5,/**/ 20, 25,/**/ 25, 30, 30, 25, /**/ 25, 30,/**/ 35, 40,/**/ 40, 45, 45, 40,/**/ 40, 45, /**/ 50, 55,/**/ 55, 60, 60, 55,/**/ 55, 50};

        this.setTabX(tX);
        this.setTabY(tY);
        this.setTailleX();
        this.setTailleY();
        this.setType(TypeModeleWidget.FOR);

        message.put(new Point(5, 17), "Pour");
        message.put(new Point(105, 17), "pas :");

        this.setElementProgramme(new InstructionFor());
        this.setForme(new Polygon(this.getTabX(), this.getTabY(), this.getTabX().length));
        this.zonesAccroches.add(VariableProperties.ZONE_ACCROCHE_PAR_DEFAULT);

        //variable
		int widthChamp = 40;
        ff = new ChampTexte(widthChamp, this);
        ff.ajouterTypeWidgetAccepte(TypeModeleWidget.EXPRESSION_LOGIQUE);
        ff.ajouterTypeWidgetAccepte(TypeModeleWidget.NEGATION);
		ff.supprimerTexte();
        ff.setBounds(60, 3, widthChamp, 20);
        this.getLesZonesSaisies().add(ff);
		
        //pas
		widthChamp = 20;
        fp = new ChampTexte(widthChamp, this);
		fp.ajouterTypeWidgetAccepte(TypeModeleWidget.EXPRESSION_INC);
		fp.supprimerTexte();
        fp.setBounds(138, 3, widthChamp, 20);
        this.getLesZonesSaisies().add(fp);

        this.decalageX(30);
    }
    
    @Override
	public void applyChangeModele(){
		Widget contentCondition = ff.getContentWidget();
		Widget contentPas = fp.getContentWidget();
		InstructionFor forIns = (InstructionFor) getElementProgramme();
		
		// On met à jour l'elementProgramme si les éléments existent
		if (contentCondition != null) {			
				IElementProgramme expr = contentCondition.getElementProgramme();
				if(expr instanceof Condition) {
					forIns.setCondition((Condition) expr);
				} else if(expr instanceof NonLogique) {
					forIns.setCondition((NonLogique) expr);
				} else {
					forIns.setCondition((Variable) expr);
				}
		}
		else {
			forIns.setCondition((Condition) null);
		}
			
		if (contentPas != null) {
			InstructionIncrementation affPas  = (InstructionIncrementation) contentPas.getElementProgramme();
			forIns.setIteration(affPas);
		}
		else {
			forIns.setIteration((InstructionIncrementation)null);
		}
	}

    @Override
    public void decalageX(final int a) {
        int i;
        for (i = 6; i < 10; i++) {
            this.getForme().xpoints[i] = this.getForme().xpoints[i] + a;
        }
        for (i = 22; i < 26; i++) {
            this.getForme().xpoints[i] = this.getForme().xpoints[i] + a;
        }
        this.setForme(this.getForme());
        this.setTailleX();
    }
	
    @Override
    public void decalageY(final int b, final Rectangle r) {
        int i;
        for (i = 16; i < tabY.length; i++) {
            this.getForme().ypoints[i] = this.getForme().ypoints[i] + b;
        }
        this.setForme(this.getForme());
        this.setTailleY();
    }
	
    @Override
    public void initListeners() {
        //TODO : faire fonctionner mise à jour FOR
		for (Zone z : this.getLesZonesSaisies()) {
            ((JComponent) z).addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(final FocusEvent arg0) {
                    VariableModifiable v = (VariableModifiable) ApplicationUI.getInstance().getDicoVariables().getLesVariables()[Integer.parseInt(((Zone) getLesZonesSaisies().get(0)).getValeur())];
                    String deb = ((Zone) getLesZonesSaisies().get(1)).getValeur();
                    Operateur o = Operateur.comparaisonA()[Integer.parseInt(((Zone) getLesZonesSaisies().get(2)).getValeur())];
                    String fin = ((Zone) getLesZonesSaisies().get(3)).getValeur();

                    setCondition(v, o, fin);
                    setIteration(v);
                    setInitialization(v, deb);
                }
            });
        }

    }

    /**
     * Méthode permettant de définir la condition d'arrêt de la boucle.
     *
     * @param v La variable de la condition
     * @param o L'opérateur de la condition
     * @param fin La valeur e la condition d'arrêt
     */
    public void setCondition(final Variable v, final Operateur o, final String fin) {
        Condition cond = new Condition(o, v, new VariableConstante(TypeElement.INT, "", fin));
        ((InstructionFor) getElementProgramme()).setCondition(cond);
    }

    /**
     * Méthode de définition de l'initialisation de la boucle.
     *
     * @param v La variable la boucle
     * @param deb La valeur de début
     */
    public void setInitialization(final Variable v, final String deb) {
        Affectation aff = new Affectation(false);
        aff.setMembreGauche(v);
        aff.setMembreDroit(new VariableConstante(TypeElement.INT, "", deb));
        ((InstructionFor) getElementProgramme()).setInitialisation(aff);
    }

    /**
     * Méthode de définition de la valeur d'augmentation après une itération.
     *
     * @param v La variable concernée
     */
    public void setIteration(final VariableModifiable v) {
        InstructionIncrementation aff = (InstructionIncrementation)getElementProgramme();
		aff.setVariable(v);
        ((InstructionFor) getElementProgramme()).setIteration(aff);
    }
}
