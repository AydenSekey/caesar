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
package jscratch.vue.widgets.modeles;

import jscratch.vue.widgets.modeles.zones.Zone;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import nxtim.instruction.IElementProgramme;
import jscratch.vue.widgets.modeles.zones.ChampTexte;
import nxtim.instruction.Categorie;

/**
 * Classe abstraite permet de définir une structure commune pour chaque widget.
 * Cette classe implemente Serializable.
 */
public abstract class ModeleWidget implements Serializable {
    
	/**
     * Constante donnant la hauteur du "décrochement" des widgets.
     */
    public static final int OFFSET = 5;

	/**
     * Liste des abscisses pour chaque point composant la forme du widget.
     */
    protected int[] tabX;
    
	/**
     * Liste des ordonnées pour chaque point composant la forme du widget.
     */
    protected int[] tabY;
    
	/**
     * La couleur de fond du widget.
     */
    private Color couleur;
    
	/**
     * La forme du widget.
     */
    private Polygon forme;
   
	/**
     * Hashmap stockant les messages affichés sur le widget et les points où ils
     * doivent se positionner.
     */
    protected HashMap<Point, String> message;
    
	/**
     * Le type du widget choisit parmis l'enumeration TypeWidget.
     */
    private TypeModeleWidget type;
    
	/**
     * La largeur du widget.
     */
    private int tailleX;
    
	/**
     * La hauteur du widget.
     */
    private int tailleY;
    
	/**
     * Booléen permettant de définir s'il faut afficher les conditions d'arrêt
     * sur le haut ou le bas du widget (différence entre While et DoWhile).
     */
    private boolean conditionHaute;
    
	/**
     * Booleen permettant de définir si un widget peut avoir un widget accroché
     * à lui en haut.
     */
    protected boolean attachableHaut;
    
	/**
     * Booléen permettant de définir si un widget peut avoir un widget accroché
     * à lui en bas.
     */
    protected boolean attachableBas;
    
	/**
     * Booléen permettant de définir si un peut etre utilisé au sein d'un autre
     * widget en tant que valeur
     */
    protected boolean attachableInterne;
    
	/**
     * Booléen permettant de définir si un widget peut être placé dans un widget
     * complexe.
     */
    protected boolean imbricable;
    
	/**
     * Liste des zones où pourront être imbriqués d'autres widgets.
     */
    protected List<Rectangle> zonesAccroches;
    
	/**
     * Élément du programme de traduction.
     */
    private IElementProgramme elementProgramme;
    
	/**
     * Liste des zones de saisies du widget.
     */
    private List<Zone> lesZonesSaisies;
	
	/**
	 * Catégorie du widget.
	 */
	private Categorie categorie;

    /**
     * Constructeur initialisant différentes valeurs du modèle.
     */
    public ModeleWidget() {
        this.conditionHaute = true;
        this.attachableBas = true;
        this.attachableHaut = true;
        this.attachableInterne = false;
        this.imbricable = true;
        this.zonesAccroches = new LinkedList<Rectangle>();
        this.lesZonesSaisies = new LinkedList<Zone>();
        this.message = new HashMap<Point, String>();
    }

    /**
     * Méthode permettant d'initialiser les listeners du modèle. Par défaut cette methode ne fait rien
     */
    public void initListeners() { }

    /**
     * Méthode permettant de récupérer l'élement de programme du modèle.
     *
     * @return L'élément de programme du modèle.
     */
    public IElementProgramme getElementProgramme() {
        return this.elementProgramme;
    }

    /**
     * Permet de définir l'élément de programme du widget.
     *
     * @param element Le nouvel élément de programe du modèle
     */
    public void setElementProgramme(IElementProgramme element) {
        this.elementProgramme = element;
    }

    /**
     * Méthode permettant de récupérer les zones de saise du modèle.
     *
     * @return La liste de zones de saisies du modèle.
     */
    public List<Zone> getLesZonesSaisies() {
        return lesZonesSaisies;
    }

	/**
	 * Récupère la catégorie du widget.
	 * 
	 * @return la catégorie 
	 */
	public Categorie getCategorie() {
		return categorie;
	}

	/**
	 * Définit la catégorie du widget.
	 * 
	 * @param categorie la catégorie
	 */
	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

    /**
     * Méthode permettant de définir les zones de saisie du programme.
     *
     * @param lesZonesSaisies Les nouvelles zones de saisie du programme
     */
    public void setLesZonesSaisies(List<Zone> lesZonesSaisies) {
        this.lesZonesSaisies = lesZonesSaisies;
    }

    /**
     * Permet de connaître la position de la condition au sein du modèle.
     *
     * @return true si la condition doit être affichée en haut, false dans le
     * cas contraire.
     */
    public boolean isConditionHaute() {
        return conditionHaute;
    }

    /**
     * Définie si la condition de la boucle doit être affichée en haut ou non.
     *
     * @param conditionHaute true si la condition doit être affichée en haut,
     * false sinon
     */
    public void setConditionHaute(boolean conditionHaute) {
        this.conditionHaute = conditionHaute;
    }

    /**
     * Récupère les zones d'accroche du widget.
     *
     * @return La liste de zones d'accroche du widget.
     */
    public List<Rectangle> getZonesAccroches() {
        return zonesAccroches;
    }

    /**
     * Méthode permettant de récupérer un tableau d'entier représentant les
     * coordonnées des abscisses de la forme du widget.
     *
     * @return Le tableau d'entier représentant les coordonnées des abscisses de
     * la forme du widget.
     */
    public int[] getTabX() {
        return tabX;
    }

    /**
     * Méthode permettant de définir un tableau représentant les coordonnées des
     * abscisses de la forme du widget.
     *
     * @param tabX Le tableau représentant les coordonnées des abscisses de la
     * forme du widget
     */
    public void setTabX(int[] tabX) {
		this.tabX = Arrays.copyOf(tabX, tabX.length);
    }

    /**
     * Méthode permettant de récupérer un tableau d'entier représentant les
     * coordonnées des ordonnées de la forme du widget.
     *
     * @return Le tableau d'entier représentant les coordonnées des ordonnées de
     * la forme du widget.
     */
    public int[] getTabY() {
        return tabY;
    }

    /**
     * Méthode permettant de définir un tableau représentant les coordonnées des
     * ordonnées de la forme du widget.
     *
     * @param tabY Le tableau représentant les coordonnées des ordonnées de la
     * forme du widget.
     */
    public void setTabY(int[] tabY) {
		this.tabY = Arrays.copyOf(tabY, tabY.length);
    }

    /**
     * Accède à la couleur de fond du widget.
     *
     * @return Le couleur de fond du widget.
     */
    public Color getCouleur() {
        return couleur;
    }

    /**
     * Modifie la couleur de fond du le widget.
     *
     * @param couleur La nouvelle couleur de fond du widget
     */
    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    /**
     * Accède à la largeur du widget.
     *
     * @return La largeur du widget.
     */
    public int getTailleX() {
        return tailleX;
    }

    /**
     * Recalcule la largeur du widget.
     */
    public void setTailleX() {
        this.tailleX = this.getXMax() - this.getXMin();
    }

    /**
     * Accède à la hauteur du widget.
     *
     * @return la hauteur du widget.
     */
    public int getTailleY() {
        return tailleY;
    }

    /**
     * Recalcule la hauteur du widget.
     */
    public void setTailleY() {
        this.tailleY = this.getYMax() - this.getYMin();
    }

    /**
     * Récupère la plus petite abscisse possible du widget.
     *
     * @return la plus petite abscisse.
     */
    public int getXMin() {
        int min = this.tabX[0];
        for (Integer i : tabX) {
            min = Math.min(min, i);
        }
        return min;
    }

    /**
     * Récupére la plus grande abscisse possible du widget
     *
     * @return la plus grande abscisse.
     */
    public int getXMax() {
        int max = this.tabX[0];
        for (Integer i : tabX) {
            max = Math.max(max, i);
        }
        return max;
    }

    /**
     * Récupère la plus petite ordonnée possible du widget.
     *
     * @return la plus petite ordonnée.
     */
    public int getYMin() {
        int min = this.tabY[0];
        for (Integer i : tabY) {
            min = Math.min(min, i);
        }
        return min;
    }

    /**
     * Récupére la plus grande ordonnée possible du widget
     *
     * @return la plus grande ordonnée.
     */
    public int getYMax() {
        int max = this.tabY[0];
        for (Integer i : tabY) {
            max = Math.max(max, i);
        }
        return max;
    }

    /**
     * Récupère la forme du widget.
     *
     * @return La forme du widget.
     */
    public Polygon getForme() {
        return forme;
    }

    /**
     * Modifie la forme du widget.
     */
    public void setForme(Polygon forme) {
        this.forme = forme;
    }

    /**
     * Accède au type du widget.
     *
     * @return le type du widget.
     */
    public TypeModeleWidget getType() {
        return type;
    }

    /**
     * Modifie le type du widget.
     *
     * @param type le nouveau type.
     */
    public void setType(TypeModeleWidget type) {
        this.type = type;
    }

    /**
     * Permet de savoir si on peut attacher un widget en bas de ce widget ou
     * non.
     *
     * @return true si on peut attacher un widget au bas de ce widget, false
     * dans le cas contraire.
     */
    public boolean isAttachableBas() {
        return attachableBas;
    }

    /**
     * Permet de savoir si on peut attacher un widget en haut de ce widget ou
     * non.
     *
     * @return true si on peut attacher un widget au haut de ce widget, false
     * dans le cas contraire.
     */
    public boolean isAttachableHaut() {
        return attachableHaut;
    }

    /**
     * Permet de savoir si on peut utiliser ce widget pour l'utiliser en guise de valeur
     * en le droppant dans un ChampTexte
     *
     * @return true si on peut utiliser ce widget pour l'utiliser en guise de valeur 
     * (cas du widget variable), false dans le cas contraire.
     * 
     * @see ChampTexte
     */
    public boolean isAttachableInterne() {
        return attachableInterne;
    }

    /**
     * Permet de savoir si on peut attacher ce widget dans un widget composé
     * (dans une zone d'accroche).
     *
     * @return true si on peut attacher ce widget dans un widget composé, false
     * dans le cas contraire.
     */
    public boolean isImbricable() {
        return imbricable;
    }

    /**
     * Méthode permettant d'augmenter ou diminuer la largeur du composant.
     *
	 * Un comportement de base a été définit est ce comportement doit être redefinit pour des widgets plus complexes
	 * 
     * @param x La valeur de l'agrandissement à appliquer
     */

    public void decalageX(int x){
		int i;
		for (i = 6; i < 10; i++) {
			this.getForme().xpoints[i] = this.getForme().xpoints[i] + x;
		}
		this.setForme(this.getForme());
		this.setTailleX();
	}

    /**
     * Méthode permettant d'augmenter ou diminuer la hauteur du composant
	 * 
	 * Si la méthode n'a pas été redéfinie dans les classes concrètes filles, aucune action n'est effectuée
     *
     * @param x La valeur de l'agrandissement à appliquer
     * @param r Le rectangle à augmenter également
     */
    public void decalageY(int x,Rectangle r){}

	public void decalerComposantsSuivantsX(int positionX, int decalage){
		for(Zone z : this.lesZonesSaisies){
			int zonePosX = z.getPositionX();
			if(zonePosX > positionX){
				z.setPositionX(zonePosX + decalage);
			}
		}
		Iterator<Point> i = message.keySet().iterator();
		HashMap<Point,String> mapTemp = new LinkedHashMap<Point, String>();
		while(i.hasNext()){
			Point pt = i.next();
			int messPosX = (int) pt.getX();
			if(messPosX > positionX){
				Point pt2 = new Point();
				pt2.setLocation(messPosX + decalage,pt.getY());
				mapTemp.put(pt2, message.get(pt));
				i.remove();
			}
		}
		i = mapTemp.keySet().iterator();
		while(i.hasNext()){
			Point pt = i.next();
			message.put(pt, mapTemp.get(pt));
		}
	}

	public void decalerComposantsSuivantsY(int positionY, int decalage){
		for(Zone z : this.lesZonesSaisies){
			int zonePosY = z.getPositionY();
			if(zonePosY > positionY){
				z.setPositionY(zonePosY + decalage);
			}
		}
		Iterator<Point> i = message.keySet().iterator();
		HashMap<Point,String> mapTemp = new LinkedHashMap<Point, String>();
		while(i.hasNext()){
			Point pt = i.next();
			int messPosY = (int) pt.getY();
			if(messPosY > positionY){
				Point pt2 = new Point();
				pt2.setLocation(pt.getX(),messPosY + decalage);
				mapTemp.put(pt2, message.get(pt));
				i.remove();
			}
		}
		i = mapTemp.keySet().iterator();
		while(i.hasNext()){
			Point pt = i.next();
			message.put(pt, mapTemp.get(pt));
		}
	}

    /**
     * Méthode permettant de récupérer la liste des messages du widget.
     *
     * @return la liste des messages de widgets et leurs coordonnées.
     */
    public HashMap<Point, String> getMessage() {
        return this.message;
    }
    
    /**
     * Mettre à jour le modèle
     */
    public void applyChangeModele(){ }
    	
    	
    
}
