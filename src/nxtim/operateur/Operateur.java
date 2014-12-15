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
package nxtim.operateur;

import java.util.LinkedList;
import java.util.List;

/**
 * Cette énumération regroupe les différents opérateurs disponibles pour les expressions.
 */
public enum Operateur {

	/**
	 * Les opérateurs d'incrémentation.
	 */
	INCREMENTATION_PLUS, INCREMENTATION_MOINS,
	/**
	 * Les opérateurs binaire.
	 */
	ADDITION, SOUSTRACTION, MULTIPLICATION, DIVISION,
	/**
	 * Les opérateurs logiques.
	 */
	ET, OU, SUPERIEUR, INFERIEUR, EGALITE, SUPEGAL, INFEGAL,
	/**
	 * Les opérateurs d'affectation.
	 */
	AFFECTATION;

	@Override
	public String toString() {
		switch (this) {
			case INCREMENTATION_PLUS:
				return "++";
			case INCREMENTATION_MOINS:
				return "--";
			case AFFECTATION:
				return "=";
			case ADDITION:
				return "+";
			case SOUSTRACTION:
				return "-";
			case MULTIPLICATION:
				return "*";
			case DIVISION:
				return "/";
			case ET:
				return "&&";
			case OU:
				return "||";
			case SUPERIEUR:
				return ">";
			case INFERIEUR:
				return "<";
			case EGALITE:
				return "==";
			case SUPEGAL:
				return ">=";
			case INFEGAL:
				return "<=";
			default:
				return null;
		}
	}

	/**
	 * Permet de savoir si un opérateur est arithmétique.
	 *
	 * @param o l'opérateur à tester
	 * @return <code>true</code> si l'opérateur est arithmétique, sinon <code>false</code>.
	 */
	public static boolean isIncrementation(final Operateur o) {
		switch (o) {
			case INCREMENTATION_PLUS:
				return true;
			case INCREMENTATION_MOINS:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Permet de savoir si un opérateur est arithmétique.
	 *
	 * @param o l'opérateur à tester
	 * @return <code>true</code> si l'opérateur est arithmétique, sinon <code>false</code>.
	 */
	public static boolean isArithmetique(final Operateur o) {
		switch (o) {
			case ADDITION:
				return true;
			case DIVISION:
				return true;
			case SOUSTRACTION:
				return true;
			case MULTIPLICATION:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Permet de savoir si un opérateur est logique.
	 *
	 * @param o l'opérateur à tester
	 * @return <code>true</code> si l'opérateur est logique, sinon <code>false</code>
	 */
	public static boolean isLogique(final Operateur o) {
		if (!Operateur.isArithmetique(o) && !Operateur.isIncrementation(o) && o != AFFECTATION) {
			return true;
		}
		return false;
	}
	
	/**
	 * Permet de savoir si un opérateur est un opérateur de logique booléenne (leurs opérandes doivent être des booléens).
	 * @param o l'opérateur à tester
	 * @return <code>true</code> si c'est un opérateur de logique booléenne, sinon <code>false</code>.
	 */
	public static boolean isLogiqueBooleenne(final Operateur o) {
		switch(o) {
			case ET:
			case OU:
				return true;
			default:
				return false;
		}
	}

	/**
	 * Donne la liste des opérateurs logiques sous forme d'une liste.
	 *
	 * @return la liste des opérateurs logiques.
	 */
	public static List<Operateur> incrementations() {
		List<Operateur> ops = new LinkedList<Operateur>();
		for (Operateur o : values()) {
			if (isIncrementation(o)) {
				ops.add(o);
			}
		}
		return ops;
	}
	
	/**
	 * Donne la liste des opérateurs logiques sous forme d'une liste.
	 *
	 * @return la liste des opérateurs logiques.
	 */
	public static List<Operateur> logiques() {
		List<Operateur> ops = new LinkedList<Operateur>();
		for (Operateur o : values()) {
			if (isLogique(o)) {
				ops.add(o);
			}
		}
		return ops;
	}

	/**
	 * Donne la liste des opérateurs logiques sous forme d'un tableau.
	 *
	 * @return la liste des opérateurs logiques
	 */
	public static Operateur[] logiquesA() {
		return (Operateur[]) logiques().toArray(new Operateur[0]);
	}

	/**
	 * Donne la liste des opérateurs de comparaison sous forme d'une liste.
	 *
	 * @return la liste des opérateurs de comparaison
	 */
	public static List<Operateur> comparaison() {
		List<Operateur> ops = new LinkedList<Operateur>();
		ops.add(INFERIEUR);
		ops.add(INFEGAL);
		ops.add(SUPERIEUR);
		ops.add(SUPEGAL);
		return ops;
	}

	/**
	 * Donne la liste des opérateurs de comparaison sous forme d'un tableau.
	 *
	 * @return la liste des opérateurs de comparaison
	 */
	public static Operateur[] comparaisonA() {
		return (Operateur[]) comparaison().toArray(new Operateur[0]);
	}

	/**
	 * Donne la liste des opérateurs arithmétiques.
	 *
	 * @return la liste des opérateurs arithmétiques
	 */
	public static List<Operateur> arithmetiques() {
		List<Operateur> ops = new LinkedList<Operateur>();
		ops.add(ADDITION);
		ops.add(DIVISION);
		ops.add(SOUSTRACTION);
		ops.add(MULTIPLICATION);
		return ops;
	}
}