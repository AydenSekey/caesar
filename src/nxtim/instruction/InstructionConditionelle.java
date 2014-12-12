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

import nxtim.exception.NXTIMBadTypeElementException;
import nxtim.type.TypeElement;

/**
 * Concept abstrait regroupant toutes les instructions possédant une condition.
 */
public abstract class InstructionConditionelle extends InstructionStructure {

	/**
	 * La condition.
	 */
	private Expression condition;

	/**
	 * Crée une instruction conditionnelle sans initialisé la condition.<br/>
	 * La condition à pour valeur
	 * <code>null</code>.
	 */
	public InstructionConditionelle() {
		super();
		condition = null;
	}

	/**
	 * Crée une instruction conditionnelle avec une condition binaire.
	 *
	 * @param condition La condition associée à l'instruction.
	 */
	public InstructionConditionelle(final Condition condition) {
		super();
		this.condition = condition;
	}
	
	/**
	 * Crée une instruction conditionnelle avec une négation comme condition.
	 * 
	 * @param condition la négation à utiliser comme condition
	 */
	public InstructionConditionelle(final NonLogique condition) {
		super();
		this.condition = condition;
	}
	
	/**
	 * Crée une instruction conditionnelle avec une variable booléenne comme condition.
	 * 
	 * @param condition la variable booléenne
	 */
	public InstructionConditionelle(final Variable condition) {
		super();
		assert condition != null;// théoriquement ce constructeur n'est jamais appelé avec l'argument null
		
		if(condition.isBooleenne()) {
			this.condition = condition;
		}
		else {
			throw new NXTIMBadTypeElementException(condition.getType(), "Variable non booléenne utilisée comme condition à une expression conditionelle.");
		}
	}

	@Override
	public final Categorie getCategorie() {
		return Categorie.STRUCTURES;
	}

	/**
	 * Accède à la condition.
	 *
	 * @return la condition.
	 */
	public Expression getCondition() {
		return condition;
	}

	/**
	 * Modifie la condition.
	 *
	 * @param cond la nouvelle condition.
	 */
	public void setCondition(final Condition cond) {
		condition = cond;
	}
	
	/**
	 * Modifie la condition par une négation.
	 *
	 * @param cond une négation.
	 */
	public void setCondition(final NonLogique cond) {
		condition = cond;
	}
	
	/**
	 * Modifie la condition par une variable.
	 * 
	 * @param cond une variable booléenne.
	 */
	public void setCondition(final Variable cond) {
		if(cond == null || cond.isBooleenne()) {
			condition = cond;
		}
		else {
			throw new NXTIMBadTypeElementException(cond.getType(), "Variable non booléenne utilisée comme condition à une expression conditionelle.");
		}
	}
	
	/**
	 * Supprime la condition.
	 * La condition a pour valeur
	 * <code>null</code>.
	 */
	public void rmCondition() {
		condition = null;
	}
}
