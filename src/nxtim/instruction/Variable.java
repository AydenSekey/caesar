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

import nxtim.type.TypeElement;

/**
 * Concept abstrait regroupant les variables.
 */
public abstract class Variable implements Expression {

	/**
	 * Le type de la variable.
	 */
	private TypeElement type;
	/**
	 * Le nom de la variable.
	 */
	private String nom;
	/**
	 * La valeur de la variable.
	 */
	private String valeur;

	/**
	 * Crée une variable sans valeur.<br/>
	 * La valeur est initialisée à
	 * <code>null</code>
	 *
	 * @param type le type de la variable
	 * @param nom le nom de la variable
	 */
	public Variable(final TypeElement type, final String nom) {
		this(type, nom, "");
	}

	/**
	 * Construit une variable.
	 *
	 * @param type le type de la variable
	 * @param nom le nom de la variable
	 * @param valeur la valeur initiale de la variable. Si la variable est de type INT ou DOUBLE, seule la partie entière est conservée.
	 */
	public Variable(final TypeElement type, final String nom, final String valeur) {
		this.type = type;
		this.nom = nom;
		this.valeur = valeur;
		castValeur();
	}

	@Override
	public void accepte(VisiteurElementProg v) {
		v.visiter(this);
	}

	/**
	 * Permet de savoir si la variable est constante.
	 *
	 * @return <code>true</code> si la variable est constante, sinon <code>false</code>
	 */
	public abstract boolean isConstante();

	/**
	 * Accède au type de la variable.
	 *
	 * @return le type de la variable
	 */
	@Override
	public TypeElement getType() {
		return type;
	}

	/**
	 * Modifie le type de la variable.
	 *
	 * @param type le nouveau type
	 */
	public void setType(final TypeElement type) {
		this.type = type;
	}

	/**
	 * Accède à la valeur de la variable.
	 *
	 * @return la valeur
	 */
	public String getValeur() {
		return valeur;
	}

	/**
	 * Modifie la valeur de la variable.
	 *
	 * @param valeur la nouvelle valeur. Si la variable est de type INT ou DOUBLE, seule la partie entière est conservée.
	 */
	public void setValeur(final String valeur) {
		this.valeur = valeur;
		castValeur();
	}
	
	/**
	 * Effectue (si possible) un cast de la valeur pour les variables de type INT et LONG.
	 */
	private void castValeur() {
		/* Si la valeur n'est pas vide */
		if(valeur != null && !valeur.isEmpty()) {
			switch(type) {
				case INT:
				case LONG:
					/* La variable est de type entier */
					try {
						double d = Double.parseDouble(valeur);
						/* Cast en entier */
						valeur = "" + (long) d;
					} catch(NumberFormatException e) {}
					break;
				default:
					break;
			}
		}
	}

	@Override
	public String toString() {
		return this.getNom();
	}

	/**
	 * Donne le nom de la variable ou sa valeur si la variable n'a pas de nom.
	 *
	 * @return le nom de la variable
	 */
	public String getNom() {
		if (nom == null || "".equals(nom)) {
			return valeur;
		}
		return nom;
	}
	
	@Override
	public Categorie getCategorie() {
		return Categorie.VARIABLES;
	}
	
	@Override
	public boolean isBooleenne() {
		return type == TypeElement.BOOL?true:false;
	}
}