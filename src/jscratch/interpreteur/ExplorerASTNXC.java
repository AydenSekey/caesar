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
package jscratch.interpreteur;

import jscratch.interpreteur.parser.NXCParser;
import org.antlr.runtime.tree.Tree;

/**
 * Explorateur d'un arbre syntaxique d'ANTLR pour un code NXC.
 */
public class ExplorerASTNXC {
    private final Object mutexMonteur;
    private MonteurProgramme monteur;

    public ExplorerASTNXC(MonteurProgramme monteur) {
        mutexMonteur = new Object();
        this.monteur = monteur;
    }

    public void explore(Tree antlrTree) {
        synchronized(mutexMonteur) {
            monteur.reset();
            exploration(antlrTree);
        }
    }

    private void exploration(Tree tree) {
        int nbChild = tree.getChildCount();
        switch(tree.getType()) {
			/* Traitements préfixes */
            case NXCParser.ROOT:
                for(int i = 0; i < nbChild; i++) {
                    exploration(tree.getChild(i));
                }
                System.out.println("Fin (ROOT atteint)");
                break;
            case NXCParser.TASK_DEFINITION:
                traitementTache(tree, nbChild);
                break;
			case NXCParser.VAR_DECLARATION:
				traitementDeclaration(tree, nbChild);
				break;
			case NXCParser.WHILE:
				if(tree.getChildCount() > 0) {
					/* n'est pas un do while */
					traitementWhile(tree);
				}
				break;
			case NXCParser.DO:
				traitementDoWhile(tree);
				break;
			case NXCParser.IF:
				if(nbChild == 2) {//if seul
					traitementIf(tree);
				}
				else {//if avec else
					traitementIfElse(tree);
				}
				break;
			case NXCParser.FOR:
				traitementFor(tree);
				break;
			case NXCParser.FUNC_CALL:
				traitementAppelFonction(tree, nbChild);
				break;
            default:
				//Exploration des fils
                for(int i = 0; i < nbChild; i++) {
                    exploration(tree.getChild(i));
                }
				/* traitements postfixes */
				switch(tree.getType()) {
					case NXCParser.DECIMAL_LITERAL:
						System.out.println("constante int " + tree.getText());
						monteur.constante(tree.getText(), "int");
						break;
					case NXCParser.STRING_LITERAL:
						System.out.println("constante string " + tree.getText());
						monteur.constante(tree.getText(), "string");
						break;
					case NXCParser.CHARACTER_LITERAL:
						System.out.println("constante char " + tree.getText());
						monteur.constante(tree.getText(), "char");
						break;
					case NXCParser.FLOAT_LITERAL:
						System.out.println("constante double " + tree.getText());
						monteur.constante(tree.getText(), "double");
						break;
					case NXCParser.PLUS:
					case NXCParser.MINUS:
					case NXCParser.STAR:
					case NXCParser.DIVIDE:
						System.out.println("operation " + tree.getText());
						monteur.operation(tree.getText());
						break;
					case NXCParser.GREATERTHAN:
					case NXCParser.GREATEREQUAL:
					case NXCParser.LESSTHAN:
					case NXCParser.LESSEQUAL:
					case NXCParser.EQUAL:
						System.out.println("condition " + tree.getText());
						monteur.condition(tree.getText());
						break;
					case NXCParser.ASSIGN:
						System.out.println("affectation");
						monteur.affectation();
						break;
					case NXCParser.IDENTIFIER:
						System.out.println("Variable " + tree.getText());
						monteur.variable(tree.getText());
						break;
					case NXCParser.ARGUMENTS:
						System.out.println(nbChild + " arguments");
						break;
					case NXCParser.STATEMENTS:
						System.out.println(nbChild + " instructions");
						break;
					case NXCParser.INITIALIZER:
						break;
					default:
						System.out.println(tree.getText() + " : " + tree.getType());
						break;
				}
                break;
        }
    }

	/* Gère l'exploration d'une tâche */
    private void traitementTache(Tree tree, int nbChild) {
        if(nbChild == 3) {
            String nom = tree.getChild(1).getText();
			int nbElemCorps = tree.getChild(2).getChildCount();//STATEMENTS
			for(int i = 0; i < nbElemCorps; i++) {
				exploration(tree.getChild(2).getChild(i));
			}
			System.out.println("Tâche " + nom + " (" + nbElemCorps + ")");
			monteur.tache(nom, nbElemCorps);
        }
    }
	
	/* Gère l'exploration d'une déclaration de variable */
	private void traitementDeclaration(Tree tree, int nbChild) {
		if(nbChild >= 2) {
			String type = convertTypeVar(tree.getChild(0).getText());
			String var = tree.getChild(1).getText();
			if(nbChild == 2) {
				System.out.println("Déclaration de " + type + " " + var);
				monteur.declaration(type, var);
			}
			else if(nbChild == 3) {// Declaration + initialisation
				exploration(tree.getChild(2));
				System.out.println("Déclaration initialisation de " + type + " " + var);
				monteur.declarationInitialisation(type, var);
			}
		}
	}
	
	/* Gère l'exploration d'un for */
	private void traitementFor(Tree tree) {
		int nbSousChild;
		//DO
		nbSousChild = tree.getChild(0).getChildCount();
		for(int i = 0; i < nbSousChild; i++) {
			exploration(tree.getChild(0).getChild(i));
		}
		//WHILE
		nbSousChild = tree.getChild(1).getChildCount();
		for(int i = 0; i < nbSousChild; i++) {
			exploration(tree.getChild(1).getChild(i));
		}
		//REPEAT
		nbSousChild = tree.getChild(2).getChildCount();
		for(int i = 0; i < nbSousChild; i++) {
			exploration(tree.getChild(2).getChild(i));
		}
		//STATEMENTS
		int nbStatements = tree.getChild(3).getChildCount();
		for(int i = 0; i < nbStatements; i++) {
			exploration(tree.getChild(3).getChild(i));
		}
        System.out.println("for (" + nbStatements + ")");
	}
	
	/* Gère l'exploration d'un appel de fonction */
	private void traitementAppelFonction(Tree tree, int nbChild) {
		String nom = tree.getChild(0).getText();
		int nbArgs = tree.getChild(1).getChildCount();//ARGS
		for(int i = 0; i < nbArgs; i++) {
			exploration(tree.getChild(1).getChild(i));
		}
		System.out.println("fonction " + nom + " (args " + nbArgs + ")");
		monteur.fonctionAppel(nom, nbArgs);
	}
	
	/* Gère l'exploration d'un while */
	private void traitementWhile(Tree tree) {
		exploration(tree.getChild(0));//Condition
		int nbInsts = tree.getChild(1).getChildCount();
		for(int i = 0; i < nbInsts; i++) {//STATEMENTS
			exploration(tree.getChild(1).getChild(i));
		}
		System.out.println("while (" + nbInsts + ")");
		monteur.bwhile(nbInsts);
	}
	
	/* Gère l'exploration d'un do-while */
	private void traitementDoWhile(Tree tree) {
		//tree.getChild(0) = while
		//condition
		exploration(tree.getChild(1));
		//STATEMENTS
		int nbInst = tree.getChild(2).getChildCount();
		for(int i = 0; i < nbInst; i++) {
			exploration(tree.getChild(2).getChild(i));
		}
		System.out.println("dowhile (" + nbInst + ")");
		monteur.bdoWhile(nbInst);
	}
	
	/* Gère l'exploration d'un if seul */
	private void traitementIf(Tree tree) {
		exploration(tree.getChild(0));//Condition
		int nbInsts = tree.getChild(1).getChildCount();
		for(int i = 0; i < nbInsts; i++) {//STATEMENTS
			exploration(tree.getChild(1).getChild(i));
		}
		System.out.println("if (" + nbInsts + ")");
		monteur.si(nbInsts);
	}
	
	/* Gère l'exploration d'un if suivi d'un else */
	private void traitementIfElse(Tree tree) {
		//if
		exploration(tree.getChild(0));//Condition
		int nbInsts = tree.getChild(1).getChildCount();
		for(int i = 0; i < nbInsts; i++) {//STATEMENTS
			exploration(tree.getChild(1).getChild(i));
		}

		Tree childElse0 = tree.getChild(2).getChild(0);
		int nbInst2 = childElse0.getChildCount();
		switch(childElse0.getType()) {
			case NXCParser.STATEMENTS://else
				for(int i = 0; i < nbInst2; i++) {
					exploration(childElse0.getChild(i));
				}
				break;
			default://else if
				nbInst2 = tree.getChild(2).getChildCount();
				exploration(childElse0);
				break;
		}
		System.out.println("ifelse (" + nbInsts + ", " + nbInst2 + ")");
		monteur.siSinon(nbInsts, nbInst2);
	}
	
	/* Convertie le nom d'un type de l'arbre généré par le parser en un nom plus lisible */
	private String convertTypeVar(String typeFromAST) {
		if(typeFromAST.equals("T_S16") || typeFromAST.equals("T_U16")) {
			return "int";
		}
		else if(typeFromAST.equals("T_S32") || typeFromAST.equals("T_U32")) {
			return "long";
		}
		else if(typeFromAST.equals("T_FLOAT")) {
			return "float";
		}
		else if(typeFromAST.equals("T_STRING")) {
			return "string";
		}
		return "";
	}
}
