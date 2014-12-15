/*
Copyright (C) Université du Maine (2013) 

contributeurs : Adrien Duroy, Bastien Andru, Quentin Gosselin, Guillaume Delorme,
 Nicolas Detan, Zubair Parwany, Houda Chouket, Bastien Aubry,
 Vincent Besnard, Ivan Melnychenko

ad.duroy@gmail.com

Ce fichier est une partie de NXTIM.

NXTIM est une bibliothèque logiciel fournissant un modèle objet du code d'un programme
pour un robot NXT. 

NXTIM est régi par la licence CeCILL-C soumise au droit français et
respectant les principes de diffusion des logiciels libres. Vous pouvez
utiliser, modifier et/ou redistribuer ce programme sous les conditions
de la licence CeCILL-C telle que diffusée par le CEA, le CNRS et l'INRIA 
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
pris connaissance de la licence CeCILL-C, et que vous en avez accepté les
termes.
 */
package nxtim.instruction;

import nxtim.exception.NXTIMRuntimeException;
import nxtim.instruction.validation.ExpressionComplexeValidateur;
import nxtim.instruction.validation.OperandesExpComplexeValidateur;
import nxtim.operateur.Operateur;

/**
 * Expression composée de deux autres expressions avec lesquelles effectuer une opération.<br/>
 * Utilise le design pattern Composite.
 */
public abstract class ExpressionComplexe implements Expression {

	private Expression membreDroit;
	private Expression membreGauche;
	private Operateur operateur;
	private ExpressionComplexeValidateur validateur;

	/**
	 * Crée une expression vide sans validateur, le contenu devra être fourni plus tard.
	 */
	public ExpressionComplexe() {
		super();
	}

	/**
	 * Crée une expression complexe. <br>
	 * L'expression est associé à un validateur de type {@link OperandesExpComplexeValidateur}. La validation est effectuée à la fin de la construction lançant une {@link NXTIMRuntimeException} si elle échoue.
	 *
	 * @param operation l'opérateur de l'expression
	 * @param membreGauche l'expression à gauche de l'opérateur
	 * @param membreDroit l'expression à droite de l'opérateur
	 * @throws NXTIMRuntimeException si l'expression n'est pas valide.
	 */
	public ExpressionComplexe(final Operateur operation, final Expression membreGauche, final Expression membreDroit) {
		this(operation, membreGauche, membreDroit, new OperandesExpComplexeValidateur());
	}
	
	/**
	 * Crée une expression complexe. <br>
	 * L'expression est associé à un validateur de type {@link OperandesExpComplexeValidateur}. La validation est effectuée à la fin de la construction lançant une {@link NXTIMRuntimeException} si elle échoue.
	 * 
	 * @param operation l'opérateur de l'expression
	 * @param membreGauche l'expression à gauche de l'opérateur
	 * @param membreDroit l'expression à droite de l'opérateur
	 * @throws NXTIMRuntimeException si l'expression n'est pas valide.
	 */
	public ExpressionComplexe(final Operateur operation, final Expression membreGauche, final Expression membreDroit, final ExpressionComplexeValidateur validateur) {
		this.operateur = operation;
		this.membreGauche = membreGauche;
		this.membreDroit = membreDroit;
		this.validateur = validateur;
		valider();
	}

	/**
	 * Crée une expression complexe dont seul l'opérateur est défini, les membres devront être fournis plus tard.
	 * L'expression est associé à un validateur de type {@link OperandesExpComplexeValidateur}. La validation est effectuée à la fin de la construction lançant une {@link NXTIMRuntimeException} si elle échoue.
	 *
	 * @param operation l'opérateur de l'expression
	 */
	public ExpressionComplexe(final Operateur operation) {
		this.operateur = operation;
		validateur = new OperandesExpComplexeValidateur();
	}

	/**
	 * Modifie le membre droit. La validation est effectuée, si elle échoue la modification est annulée et une {@link NXTIMRuntimeException} est lancée.
	 *
	 * @param expression l'expression à mettre en membre droit
	 * @throws NXTIMRuntimeException si le nouveau membre n'est pas valide.
	 */
	public void setMembreDroit(final Expression expression) {
		Expression old = membreDroit;
		membreDroit = expression;
		try {
			valider();
		} catch (NXTIMRuntimeException e) {
			membreDroit = old;
			throw e;
		}
	}

	/**
	 * Modifie le membre gauche. La validation est effectuée, si elle échoue la modification est annulée et une {@link NXTIMRuntimeException} est lancée.
	 *
	 * @param expression l'expression à mettre en membre gauche
	 * @throws NXTIMRuntimeException si le nouveau membre n'est pas valide.
	 */
	public void setMembreGauche(final Expression expression) {
		Expression old = membreGauche;
		membreGauche = expression;
		try {
			valider();
		} catch (NXTIMRuntimeException e) {
			membreGauche = old;
			throw e;
		}
	}

	/**
	 * Accède au membre gauche.
	 *
	 * @return le membre gauche.
	 */
	public Expression getMembreGauche() {
		return this.membreGauche;
	}

	/**
	 * Accède au membre droit.
	 *
	 * @return le membre droit.
	 */
	public Expression getMembreDroit() {
		return this.membreDroit;
	}

	/**
	 * Accède à l'opérateur.
	 *
	 * @return l'opérateur.
	 */
	public Operateur getOperateur() {
		return this.operateur;
	}
	
	/**
	 * Accède au validateur de l'expression.
	 * @return le validateur.
	 */
	public ExpressionComplexeValidateur getValidateur() {
		return validateur;
	}

	/**
	 * Modifie le validateur de l'expression.
	 * @param validateur le nouveau validateur.
	 */
	public void setValidateur(ExpressionComplexeValidateur validateur) {
		this.validateur = validateur;
	}

	@Override
	public String toString() {
		return "(" + membreGauche + operateur + membreDroit + ")";
	}
	
	/*
	 * Valide la cohérence des attributs.
	 */
	protected void valider() throws NXTIMRuntimeException {
		if(validateur != null) {
			validateur.valider(this);
		}
	}
}
