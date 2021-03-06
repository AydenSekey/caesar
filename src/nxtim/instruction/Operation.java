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
import nxtim.type.DefautTypePromotionStrategie;
import nxtim.type.TypeElement;
import nxtim.type.TypePromotionStrategie;

/**
 * Expression arithmétique.
 */
public class Operation extends ExpressionComplexe {
	private TypePromotionStrategie typePromotionStrategie;

	/**
	 * Créé une opération à partir de deux autres.
	 *
	 * @param operation l'opérateur de l'opération
	 * @param membreGauche le membre de gauche
	 * @param membreDroit le membre de droit
	 * @throws NXTIMBadOperateurException Si l'opérateur fourni n'est pas un opérateur arithmétique
	 */
	public Operation(final Operateur operation, final Operation membreGauche, final Operation membreDroit) {
		super(operation, membreGauche, membreDroit);
		if (!Operateur.isArithmetique(operation)) {
			throw new NXTIMBadOperateurException(operation, "Opérateur non arithméthique dans Operation.");
		}
		typePromotionStrategie = new DefautTypePromotionStrategie();
	}

	/**
	 * Créé une opération sur deux variables.
	 *
	 * @param operation l'opérateur de l'opération
	 * @param membreGauche le membre de gauche
	 * @param membreDroit le membre de droite
	 * @throws NXTIMBadOperateurException Si l'opérateur fourni n'est pas un opérateur arithmétique
	 */
	public Operation(final Operateur operation, final Variable membreGauche, final Variable membreDroit) {
		super(operation, membreGauche, membreDroit);
		if (!Operateur.isArithmetique(operation)) {
			throw new NXTIMBadOperateurException(operation, "Opérateur non arithméthique dans Operation.");
		}
		typePromotionStrategie = new DefautTypePromotionStrategie();
	}

	/**
	 * Créé une opération à partir d'une variable et d'une autre opération.
	 *
	 * @param operation l'opérateur de l'opération
	 * @param membreGauche le membre de gauche
	 * @param membreDroit le membre de droite
	 * @throws NXTIMBadOperateurException Si l'opérateur fourni n'est pas un opérateur arithmétique
	 */
	public Operation(final Operateur operation, final Operation membreGauche, final Variable membreDroit) {
		super(operation, membreGauche, membreDroit);
		if (!Operateur.isArithmetique(operation)) {
			throw new NXTIMBadOperateurException(operation, "Opérateur non arithméthique dans Operation.");
		}
		typePromotionStrategie = new DefautTypePromotionStrategie();
	}

	/**
	 * Créé une opération à partir d'une variable et d'une autre opération.
	 *
	 * @param operation l'opérateur de l'opération
	 * @param membreGauche le membre gauche
	 * @param membreDroit le membre de droite
	 * @throws NXTIMBadOperateurException Si l'opérateur fourni n'est pas un opérateur arithmétique
	 */
	public Operation(final Operateur operation, final Variable membreGauche, final Operation membreDroit) {
		super(operation, membreGauche, membreDroit);
		if (!Operateur.isArithmetique(operation)) {
			throw new NXTIMBadOperateurException(operation, "Opérateur non arithméthique dans Operation.");
		}
		typePromotionStrategie = new DefautTypePromotionStrategie();
	}

	@Override
	public void accepte(VisiteurElementProg v) {
		v.visiter(this);
	}

	@Override
	public Categorie getCategorie() {
		return Categorie.EXPRESSIONS;
	}

	@Override
	public boolean isBooleenne() {
		return false;
	}

	@Override
	public TypeElement getType() {
		if(getMembreGauche() == null || getMembreDroit() == null)
			return null;
		return choixDuType(getMembreGauche().getType(), getMembreDroit().getType());
	}
	
	/**
	 * Modifie la stratégie de promotion de type.<br />
	 * Cette stratégie est utilisée pour la détermination du type de l'expression arithmétique. Par défaut elle est de type {@link DefautTypePromotionStrategie}.
	 * @param strategie la nouvelle stratégie de promotion de type. Ne doit pas être <code>null</code>.
	 * @throws NullPointerException si <code>null</code> est fourni en paramètre.
	 */
	public void setTypePromotionStrategie(TypePromotionStrategie strategie) {
		if(strategie != null) {
			typePromotionStrategie = strategie;
		} else {
			throw new NullPointerException("la stratégie de promotion de type ne doit par être null.");
		}
	}
	
	/**
	 * Donne le type acceptant deux autres.
	 * 
	 * @param e1 le premier type
	 * @param e2 le second type
	 * @return le type minimal pouvant recevoir les deux autres ou null s'il n'y en a pas.
	 */
	private TypeElement choixDuType(TypeElement e1, TypeElement e2) {
		if(typePromotionStrategie.isPromouvableEn(e1, e2)) {
			return e2;
		} else if(typePromotionStrategie.isPromouvableEn(e2, e1)) {
			return e1;
		}
		return null;
	}
}