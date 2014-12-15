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

import nxtim.operateur.exception.NXTIMBadOperateurException;
import nxtim.operateur.Operateur;
import nxtim.type.TypeElement;

/**
 * Expression logique binaire.
 */
public class Condition extends ExpressionComplexe {

	/**
	 * Crée une condition.
	 *
	 * @param operateur l'opérateur logique à utiliser
	 * @param membreDroit le membre gauche de la condition
	 * @param membreGauche le membre droit de la condition
	 * @throws NXTIMBadOperateurException Si l'opérateur fourni n'est pas un opérateur logique.
	 */
	public Condition(Operateur operateur, Expression membreGauche, Expression membreDroit) {
		super(operateur, membreGauche, membreDroit);
		if (!Operateur.isLogique(operateur)) {
			throw new NXTIMBadOperateurException(operateur, "Opérateur non logique dans Condition.");
		}
	}

	/**
	 * Crée une condition.
	 *
	 * @param operateur l'opérateur logique à utiliser
	 * @throws NXTIMBadOperateurException Si l'opérateur fourni n'est pas un opérateur logique.
	 */
	public Condition(Operateur operateur) {
		super(operateur);
		if (!Operateur.isLogique(operateur)) {
			throw new NXTIMBadOperateurException(operateur, "Opérateur non logique dans Condition.");
		}
	}

	@Override
	public void accepte(VisiteurElementProg v) {
		v.visiter(this);
	}

	@Override
	public Categorie getCategorie() {
		// TODO Auto-generated method stub
		return null;
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
