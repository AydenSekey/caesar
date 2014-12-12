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
package jscratch.dictionnaires;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nxtim.type.TypeElement;
import nxtim.instruction.Variable;

/**
 * Cette classe représente un dictionnaire de variables.
 */
public final class DicoVariables {
	/**
	 * Les différentes variables.
	 */
	private Map<String, Variable> dictionnaire;

	/**
	 * Crée un nouveau dictionnaire de variables.
	 */
	public DicoVariables() {
		dictionnaire = new HashMap<String, Variable>();
	}

	/**
	 * Ajouter la variable dans le dictionnaire.
	 * 
	 * @param v la variable à ajouter
	 */
	public void ajouter(final Variable v) {
		if (!v.getNom().isEmpty() && !dictionnaire.containsKey(v.getNom())) {
			dictionnaire.put(v.getNom(), v);
		}
	}

	/**
	 * Supprimer la variable portant le nom.
	 *
	 * @param nom le nom de la variable
	 */
	public void supprimer(final String nom) {
		dictionnaire.remove(nom);
	}

	/**
	 * Récupère toutes les variables du type voulu.
	 *
	 * @param type le type de variable désiré
	 * @return les variables du type désiré
	 */
	public List<Variable> getVariablesDeType(final TypeElement type) {
		ArrayList<Variable> res = new ArrayList<Variable>();
		for (Variable v : dictionnaire.values()) {
			if (v.getType() == type) {
				res.add(v);
			}
		}
		return res;
	}

	/**
	 * Récupère toutes les variables du dictionnaire.
	 *
	 * @return les variables du dictionnaire
	 */
	public Variable[] getLesVariables() {
		return (Variable[]) dictionnaire.values().toArray(new Variable[0]);
	}
	
	/**
	 * Test l'existance d'une variable dans le dictionnaire.
	 * 
	 * @param varName le nom de la variable.
	 * @return <code>true</code> si la variable existe dans le dictionnaire, sinon <code>false</code>.
	 */
	public boolean exist(String varName) {
		return dictionnaire.containsKey(varName);
	}
	
	/**
	 * Accède à une variable du dictionnaire.
	 * 
	 * @param varName le nom de la variable.
	 * @return la variable ou <code>null</code> si aucune variable ne correspond.
	 */
	public Variable getVariable(String varName) {
		return dictionnaire.get(varName);
	}
	
	/**
	 * Vérifie si le dictionnaire est vide.
	 * 
	 * @return <code>true</code> si le dictionnaire est vide, <code>false</code> s'il possède au moins une variable.
	 */
	public boolean isEmpty() {
		return dictionnaire.isEmpty();
	}
	
	/**
	 * Supprime toutes les variables du dictionnaire.
	 */
	public void clear() {
		dictionnaire.clear();
	}
}