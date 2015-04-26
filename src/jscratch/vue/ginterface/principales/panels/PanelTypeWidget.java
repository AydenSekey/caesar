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
package jscratch.vue.ginterface.principales.panels;

import jscratch.vue.categories.boutons.BoutonCategorie;
import jscratch.vue.categories.boutons.BoutonCategorieStructure;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import jscratch.dictionnaires.DicoBoutonsCategories;
import jscratch.parametrages.properties.VariableProperties;
import nxtim.instruction.Categorie;

/**
 * Panel où sont entreposés les boutons des différentes catégories disponibles.
 *
 * @since 1.0
 * @version 1.0
 */
public final class PanelTypeWidget extends JPanel {

	/**
	 * SINGLETON.
	 *
	 * @since 1.0
	 */
	private static final PanelTypeWidget instance = new PanelTypeWidget();
	private List<BoutonCategorie> lesCategories;

	private PanelTypeWidget() {
		this.setBorder(BorderFactory.createTitledBorder("Catégories"));

		this.lesCategories = new ArrayList<BoutonCategorie>();
		this.lesCategories.addAll(DicoBoutonsCategories.getInstance().getBoutons());

		this.setLayout(null);
		int i = 0, x = 10, y = 26;
		do {
			BoutonCategorie b = this.lesCategories.get(i);
			b.setLocation(x, y);
			this.add(b);
			if (i % 2 == 0) {
				x += 10 + b.getWidth();
			} else {
				x = 10;
				y += b.getHeight() + 10;
			}
			i++;
		} while (i < this.lesCategories.size());

		int nb = this.lesCategories.size() / 2 + this.lesCategories.size() % 2;
		nb = 30 + nb * (VariableProperties.TAILLE_CATEGORIE_Y + 10);

		this.setPreferredSize(new Dimension(VariableProperties.X_MAX_INSTRUCTION, nb));
	}

	/**
	 * SINGLETON.
	 *
	 * @since 1.0
	 *
	 * @return L'instance unique de PanelWidget.
	 */
	public static PanelTypeWidget getInstance() {
		return instance;
	}

	/**
	 * @since 1.0
	 *
	 * @return Les boutons des différentes catégories.
	 */
	public List<BoutonCategorie> getLesCategories() {
		return this.lesCategories;
	}

	/**
	 * Permet de connaître la catégorie active.
	 * 
	 * @return la categorie sélectionnée ou <code>null</code> si aucune n'est sélectionnée.
	 */
	public Categorie getCurrentCategorie() {
		for (BoutonCategorie bc : this.getLesCategories()) {
			if (bc.isActive()) {
				return bc.getCategorie();
			}
		}
		return null;
	}
	
	/**
	 * Supression de la tache principale
	 */
	public void supprimerTachePrincipale() {
		BoutonCategorieStructure bc = (BoutonCategorieStructure) getBoutonCategorie(Categorie.STRUCTURES);
		if(bc != null)
			bc.supprimerTachePrincipale();
	}
	
	/**
	 * Ajout de la tache principale
	 */
	public void ajouterTachePrincipale() {
		BoutonCategorieStructure bc = (BoutonCategorieStructure) getBoutonCategorie(Categorie.STRUCTURES);
		if(bc != null)
			bc.ajouterTachePrincipale();
	}
	
	/**
	 * Accède au bouton d'une catégorie.
	 * 
	 * @param categorie la catégorie
	 * @return le bouton de la catégorie ou <code>null</code> si aucun bouton n'existe pour la catégorie.
	 */
	public BoutonCategorie getBoutonCategorie(Categorie categorie) {
		BoutonCategorie bouton = null;
		for(BoutonCategorie bc : lesCategories) {
			if(bc.getCategorie() == categorie) {
				bouton = bc;
				break;
			}
		}
		return bouton;
	}
}