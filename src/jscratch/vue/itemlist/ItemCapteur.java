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
package jscratch.vue.itemlist;

import jscratch.lang.CapteurLangueTraducteur;
import nxtim.instruction.Capteur;

/**
 * Class encapsulant une valeur de {@link Capteur}, utilisée pour afficher un texte dépendant de la langue de l'application dans une liste graphique.
 */
public final class ItemCapteur {
	private final Capteur capteur;
	
	/**
	 * Crée un tableau de {@link ItemCapteur} correspondant au tableau retourné par {@link Capteur#values()}.
	 * @return un nouveau tableau des items correpondant à chaque capteur.
	 */
	public static ItemCapteur[] itemsCapteur() {
		final Capteur[] capteurs = Capteur.values();
		ItemCapteur[] items = new ItemCapteur[capteurs.length];
		for(int i = 0; i < capteurs.length; i++) {
			items[i] = new ItemCapteur(capteurs[i]);
		}
		return items;
	}

	/**
	 * Crée l'item correspondant à un capteur
	 * @param capteur le capteur.
	 */
	public ItemCapteur(Capteur capteur) {
		this.capteur = capteur;
	}
	
	public Capteur getCapteur() {
		return capteur;
	}
	
	@Override
	public String toString() {
		return CapteurLangueTraducteur.traduire(capteur);
	}
}