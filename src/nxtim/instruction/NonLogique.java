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

import nxtim.type.exception.NXTIMBadTypeElementException;
import nxtim.type.TypeElement;

/**
 * Expression logique de négation.
 * 
 * @since 1.0
 */
public class NonLogique implements Expression {

	private Expression condition;
	
	/**
	 * Créer une expression de négation vide.
	 */
	public NonLogique() {
		condition = null;
	}

	/**
	 * Créé une expresssion de négation d'une condition binaire.
	 * 
	 * @param c la condition binaire sur laquelle faire la négation.
	 */
	public NonLogique(Condition c) {
		condition = c;
	}
	
	/**
	 * Crée une expression de négation d'une expression booléenne.
	 * 
	 * @param expr l'expression sur laquelle faire la négation
	 * @throws NXTIMBadTypeElementException si l'expression n'est pas booléenne.
	 */
	public NonLogique(Expression expr) {
		if(expr == null || expr.isBooleenne()) {
			condition = expr;
		}
		else {
			throw new NXTIMBadTypeElementException(expr.getType(), "Utilisation d'une expression non booléenne dans un non logique !");
		}
	}
	
	/**
	 * Accède à l'expression de la condition sur laquelle porte la négation.
	 * 
	 * @return l'expression de la condition.
	 */
	public Expression getCondition() {
		return condition;
	}
	
	/**
	 * Modifie la condition de l'expresssion de négation.
	 * 
	 * @param c une condition binaire sur laquelle faire la négation.
	 */
	public void setCondition(Condition c) {
		condition = c;
	}
	
	/**
	 * Modifie la condition de l'expression de négation.
	 * 
	 * @param v une variable booléenne.
	 * @throws NXTIMBadTypeElementException si la variable n'est pas booléenne
	 */
	public void setCondition(Variable v) {
		if(v == null || v.isBooleenne()) {
			condition = v;
		} else {
			throw new NXTIMBadTypeElementException(v.getType(), "Utilisation d'une variable non booléenne dans un non logique !");
		}
	}

	/**
	 * Modifie la condition de l'expression de négation.
	 * 
	 * @param expr une expression booléenne.
	 * @throws NXTIMBadTypeElementException si l'expression n'est pas booléenne
	 */
	public void setCondition(Expression expr) {
		assert expr != this;
		if(expr == null || expr.isBooleenne()) {
			condition = expr;
		} else {
			throw new NXTIMBadTypeElementException(expr.getType(), "Utilisation d'une expression non booléenne dans un non logique !");
		}
	}
	
	/**
	 * Modifie la condition de l'expresssion de négation par une autre expression de négation.
	 * Cette méthode existe car la majorité des langages permettent de faire un non logique
	 * sur un autre non logique bien que le résultat est le même que si les deux négations étaient absentes.
	 * 
	 * @param c une expression de négation sur laquelle faire la négation.
	 * @deprecated Utiliser setCondition(Expression expr).
	 */
	public void setCondition(NonLogique c) {
		assert c != this;
		condition = c;
	}
	
	@Override
	public void accepte(VisiteurElementProg visiteur) {
		visiteur.visiter(this);
	}

	@Override
	public Categorie getCategorie() {
		return Categorie.EXPRESSIONS;
	}

	@Override
	public TypeElement getType() {
		return TypeElement.BOOL;
	}

	@Override
	public boolean isBooleenne() {
		return true;
	}
}
