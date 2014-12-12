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
 * Cette instruction définit une incrémentation ou une décrémentation
 * 
 * @since 1.0
 * @version 1.0
 */
public class InstructionIncrementation implements Instruction, Expression {

	/**
	 * L'expression à incrementer.
	 */
	private VariableModifiable variable;
	
	/**
	 * <code>true</code> si l'incrémentation est avant (++x).
	 */
	private boolean avant;
	
	/**
	 * <code>true</code> si l'incrémentation est positive (++).
	 */
	private boolean positive;
	
	/**
	 * Définie si cette instruction est utilisé comme instruction ou expression.
	 */
	private boolean instruction;
	
	/**
	 * Créé une instruction d'incrémentation du type x++.
	 * 
	 * @param var la variable à incrémenter.
	 * @param positive <code>true</code> si l'incrémentation est positive, <code>false</code> sinon
	 */
	public InstructionIncrementation(final VariableModifiable var, final boolean positive) {
		this(var, positive, false, true);
	}
	
	/**
	 * Créé une instruction d'incrémentation.
	 * 
	 * @param var la variable à incrémenter
	 * @param positive <code>true</code> si l'incrémentation est positive, <code>false</code> sinon
	 * @param avant <code>true</code> si l'opérateur d'incrémentation est avant (++x), false sinon (x++)
	 */
	public InstructionIncrementation(final VariableModifiable var, final boolean positive, final boolean avant) {
		this(var, positive, avant, true);
	}
	
	/**
	 * Créé une incrémentation.
	 * 
	 * @param var la variable à incrémenter.
	 * @param positive <code>true</code> si l'incrémentation est positive, <code>false</code> sinon
	 * @param avant <code>true</code> si l'opérateur d'incrémentation est avant (++x), false sinon (x++)
	 * @param isInstruction <code>true</code> si l'incrémentation doit être vu comme une instruction, <code>false</code> si elle doit être vu comme une expression.
	 */
	public InstructionIncrementation(final VariableModifiable var, final boolean positive, final boolean avant, final boolean isInstruction) {
		if(var != null && var.isBooleenne()) {
			throw new NXTIMBadTypeElementException(TypeElement.BOOL, "Incrémentation d'une variable booléenne !");
		}
		this.variable = var;
		this.positive = positive;
		this.avant = avant;
		this.instruction = isInstruction;
	}

	/**
	 * Permet de modifier la variable.
	 * 
	 * @param variable la nouvelle variable
	 */
	public void setVariable(final VariableModifiable variable) {
		if(variable != null && variable.isBooleenne()) {
			throw new NXTIMBadTypeElementException(TypeElement.BOOL, "Incrémentation d'une variable booléenne !");
		}
		this.variable = variable;
	}
	
	/**
	 * Permet de récupérer l'expression.
	 * 
	 * @return la variable
	 */
	public Variable getVariable() {
		return this.variable;
	}

	/**
	 * Permet de savoir si l'opérateur d'incrementation est avant ou après.
	 * 
	 * @return <code>true</code> si l'opérateur d'incrémentation est avant, <code>false</code> sinon
	 */
	public boolean isAvant() {
		return this.avant;
	}
	
	/**
	 * Permet de savoir si l'incrementation est positive ou négative
	 * 
	 * @return <code>true</code> si l'incrémentation est positive, <code>false</code> sinon
	 * @deprecated utiliser getOperateur().
	 */
	public boolean isPositive() {
		return this.positive;
	}

	@Override
	public Categorie getCategorie() {
		return Categorie.EXPRESSIONS;
	}

	@Override
	public void accepte(VisiteurElementProg visiteur) {
		visiteur.visiter(this);
	}

	/**
	 * Donne l'opérateur d'incrémentation.
	 * 
	 * @return <code>Operateur.INCREMENTATION_PLUS</code> pour une incrémentation positive, <code>Operateur.INCREMENTATION_MOINS</code> dans le cas inverse.
	 */
	public Object getOperateur() {
		if (positive) {
			return Operateur.INCREMENTATION_PLUS;
		}
		else {
			return Operateur.INCREMENTATION_MOINS;
		}
	}
	
	/**
	 * Donne l'utilisation qu'est faite de l'incrémentation (instruction ou expression).
	 * 
	 * @return <code>true</code> si utilisée comme instruction, <code>false</code> sinon.
	 */
	public boolean isInstruction() {
		return instruction;
	}
	
	/**
	 * Modifie le type d'utilisation de l'incrémentation.
	 * 
	 * @param b <code>true</code> pour une utilisation en tant qu'instruction, <code>false</code> en tant qu'expression.
	 */
	public void setIsInstruction(boolean b) {
		instruction = b;
	}

	@Override
	public TypeElement getType() {
		return variable.getType();
	}

	@Override
	public boolean isBooleenne() {
		return false;
	}
}