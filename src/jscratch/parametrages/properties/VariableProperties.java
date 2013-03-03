package jscratch.parametrages.properties;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import jscratch.helpers.PropertiesHelper;

/**
 * Classe regroupant différentes variables utilisées par le programme
 *
 * @since 1.0
 * @version 1.0
 */
public final class VariableProperties {

	/**
	 * Taille en X des boutons des catégories.
	 * 
	 * @see properties
	 */
	public static final int TAILLE_CATEGORIE_X = Integer.parseInt(PropertiesHelper.getInstance().get("taille.categorie.x"));
	
	/**
	 * Taille en Y des boutons des catégories.
	 * 
	 * @see properties
	 */
	public static final int TAILLE_CATEGORIE_Y = Integer.parseInt(PropertiesHelper.getInstance().get("taille.categorie.y"));

	/**
	 * Taille en X du PanelInstruction.
	 */
	public static final int X_MAX_INSTRUCTION = TAILLE_CATEGORIE_X * 2 + 33;
	/**
	 * Rectangles pour la zone d'accroche des Widgets Compose
	 */
	public static final Rectangle ZONE_ACCROCHE_PAR_DEFAULT = new Rectangle(5, 25, 130, 16);
	public static final Rectangle ZONE_ACCROCHE_ELSE = new Rectangle(5, 55, 130, 16);
	public static final Rectangle ZONE_ACCROCHE_DOWHILE = new Rectangle(5, 15, 130, 16);
	
	// Gestion des couleurs des catégories
	public static final Color GRIS_INACTIF = new Color(90, 111, 116);
	public static final Color GRIS_ACTIF = new Color(55, 69, 72);
	public static final Color GRIS_SURVOLE = new Color(147, 167, 172);
	
	/**
	 * Taille en Y de la marge pour la fusion de deux widgets.
	 *
	 * @see properties
	 */
	public static final int MARGE_AIMENT = Integer.parseInt(PropertiesHelper.getInstance().get("marge.aiment"));
	
	/**
	 * Epaisseur du trait lors de la fusion.
	 *
	 * @see propertie
	 */
	public static final int LINE_STROKE_VALUE = Integer.parseInt(PropertiesHelper.getInstance().get("linestroke.value"));
	
	/**
	 * Indice (en %) pour la conservation du widget dans le PanelCodeGraphique.
	 *
	 * @see properties
	 */
	public static final float TAUX_TRANSFERT_PANEL = Float.parseFloat(PropertiesHelper.getInstance().get("taux.transfert"));
	
	/**
	 * Espacement WidgetCompose vide
	 *
	 * @see properties
	 */
	public static final int ECART_PAR_DEFAULT = Integer.parseInt(PropertiesHelper.getInstance().get("ecart.defaut"));
	
	/**
	 * Chemin d'accès au compilateur NXC
	 *
	 * @see properties
	 */
	public static final String CHEMIN_ACCES_NBC = PropertiesHelper.getInstance().get("user.chemin.compilateur").replace("\\", File.separator).replace("/", File.separator);
	
	/**
	 * Affichage du nombre de widget dans les boutons catégorie.
	 * 
	 * @see properties
	 */
	public static final boolean AFFICHAGE_NOMBRE_WIDGET = Boolean.valueOf(PropertiesHelper.getInstance().get("user.categorie.afficher.nombrewidget"));
	
	/**
	 * Couleur des categories.
	 * 
	 * @see properties
	 */
	public static final Color COULEUR_STRUCTURES = new Color(Integer.parseInt(PropertiesHelper.getInstance().get("user.couleur.Structures"))),
								COULEUR_MOTEURS = new Color(Integer.parseInt(PropertiesHelper.getInstance().get("user.couleur.Moteurs"))),
								COULEUR_EXPRESSIONS = new Color(Integer.parseInt(PropertiesHelper.getInstance().get("user.couleur.Expressions"))),
								COULEUR_CAPTEURS = new Color(Integer.parseInt(PropertiesHelper.getInstance().get("user.couleur.Capteurs"))),
								COULEUR_TEMPS = new Color(Integer.parseInt(PropertiesHelper.getInstance().get("user.couleur.Temps"))),
								COULEUR_VARIABLES = new Color(Integer.parseInt(PropertiesHelper.getInstance().get("user.couleur.Variables")));
}