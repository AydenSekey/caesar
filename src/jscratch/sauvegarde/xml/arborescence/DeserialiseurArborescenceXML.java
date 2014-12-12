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
package jscratch.sauvegarde.xml.arborescence;

import java.util.LinkedList;
import java.util.List;
import jscratch.dictionnaires.DicoWidgetsCategories;
import jscratch.exceptions.NonChargeableException;
import jscratch.vue.ginterface.principales.ApplicationUI;
import jscratch.vue.ginterface.principales.GUI;
import jscratch.vue.widgets.IWidget;
import jscratch.vue.widgets.Widget;
import jscratch.vue.widgets.WidgetCompose;
import jscratch.vue.widgets.fabrique.FabriqueWidgets;
import jscratch.vue.widgets.modeles.zones.ChampTexte;
import jscratch.vue.widgets.modeles.zones.Zone;
import nxtim.type.TypeElement;
import nxtim.instruction.Variable;
import nxtim.instruction.VariableModifiable;
import org.jdom2.Document;
import org.jdom2.Element;

/**
 * @since 1.0
 * @version 1.0
 */
public final class DeserialiseurArborescenceXML {

	/**
	 * Default constructor of <code>ChargeurXML</code>.
	 *
	 * @since 1.0
	 */
	private DeserialiseurArborescenceXML() { }
	
	/**
	 * Permet de charger l'arborescence et le dictionnaire à partir d'un document XML.
	 *
	 * @since 1.0
	 * 
	 * @param docXml le document XML à parser
	 * @return L'arborescence à injecter dans le programme
	 * @throws NonChargeableException Si un des composants n'est pas chargeable (non définit dans la fabrique)
	 */
	public static List<List<Widget>> load(final Document docXml) throws NonChargeableException {
		List<List<Widget>> arbo = new LinkedList<List<Widget>>();
		
		// Chargement du dictionnaire
		Element dictionnaire = docXml.getRootElement().getChild("dictionnaire");
		for (Element variable : dictionnaire.getChildren()) {
			Variable var = new VariableModifiable(TypeElement.fromString(variable.getAttributeValue("type")), variable.getAttributeValue("nom"), "");
			ApplicationUI.getInstance().getDicoVariables().ajouter(var);
		}
		DicoWidgetsCategories.getInstance().updateWidgetsVariables();
		
		// Chargement de l'arborescence
		List<Element> groupesXml = docXml.getRootElement().getChild("arborescence").getChildren("groupe");
		
		for (Element groupe : groupesXml) {
			List l = new LinkedList<Widget>();
			for (Element widgetXml : groupe.getChildren()) {
				Widget w = deserializeWidget(widgetXml, GUI.getPanelCodeGraphique());
				l.add(w);
			}
			arbo.add(l);
		}
		return arbo;
	}
	
	/**
	 * Permet de désérialiser un widget.
	 *
	 * @since 1.0
	 * 
	 * @param widget le widget a désérialiser
	 * @param parent le parent de ce widget
	 * @return le widget désérialisé
	 * @throws NonChargeableException lévéé si le widget ne peut pas être créé
	 */
	private static Widget deserializeWidget(Element widget, IWidget parent) throws NonChargeableException {
		String classe = widget.getAttributeValue("classe");
		String categorie = widget.getAttributeValue("categorie");
		String supplement = widget.getAttributeValue("supplement");
		Element coordonneesXml = widget.getChild("coordonnees");
		
		List<Element> zonesXml = null;
		if (widget.getChild("attributs") != null) {
			zonesXml = widget.getChild("attributs").getChildren();
		}
		
		List<Element> accrochesXml = widget.getChildren("accroche");

		// Récupération d'un objet Widget correspondant à la classe
		FabriqueWidgets fabrique = new FabriqueWidgets();
		
		Widget w = fabrique.creerWidget(classe, categorie, supplement);
		w.setDraggable(true);
		w.defParent(parent);
				
		// Remplissage des coordonnées
		if (coordonneesXml != null) {
			w.setLocation(Integer.parseInt(coordonneesXml.getAttributeValue("x")), Integer.parseInt(coordonneesXml.getAttributeValue("y")));
		}
	
		// Remplissage des zones
		List<Zone> lesZones = w.getModele().getLesZonesSaisies();
		for (int i = 0; i < lesZones.size(); i++) {
			Element zoneXml = zonesXml.get(i);
			Zone zone = lesZones.get(i);
			if (!Boolean.parseBoolean(zoneXml.getAttributeValue("isWidget"))) {
				zone.setValeur(zoneXml.getAttributeValue("valeur"));
			}
			else {
				// Il y a forcement qu'un widget dans une zone
				Element widgetsDansZoneXml = zoneXml.getChildren().get(0);
				Widget widgetDansZone = deserializeWidget(widgetsDansZoneXml, null);
				((ChampTexte)zone).setWidgetContenu(widgetDansZone);
			}
		}

		// Remplissage des zones d'accroche
		if (w.isComplexe()) {
			WidgetCompose wComp = (WidgetCompose) w;
			int i = 0;
			for (List<Widget> accroche : wComp.getMapZone().values()) {
				Element accrocheXml = accrochesXml.get(i);
				for (Element widgetXml : accrocheXml.getChildren()) {
					Widget wid = deserializeWidget(widgetXml, wComp);
					accroche.add(wid);
				}
				i++;
			}
			wComp.notifyChange();
		}

		w.applyChangeModele();
		return w;
	}
}