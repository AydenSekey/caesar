package jscratch.vue.widgets;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import jscratch.vue.widgets.modeles.TypeModeleWidget;
import jscratch.parametrages.Variables;
import jscratch.exceptions.ComposantIntrouvableException;
import nxtim.instruction.Instruction;
import nxtim.instruction.InstructionIfElse;
import nxtim.instruction.InstructionStructure;
import org.jdom2.Attribute;
import org.jdom2.Element;
import jscratch.vue.widgets.modeles.ModeleWidget;

/**
 * Classe héritant de Widget et implémentant IWidget permettant de représenter un Widget de type Complexe.
 */
public class WidgetCompose extends Widget implements IWidget {

	/**
	 * HashMap stockant les zones d'accroches du composant complexe et les widgets qui y sont insérés.
	 */
	private LinkedHashMap<YComparableRectangle, List<Widget>> mapZone;

	/**
	 * Méthode redéfinie en phase de test afin laisser apparaître les zones d'accroches.
	 *
	 * @param g l'objet <code>Graphics</code> de l'appel
	 */
	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
	}

	/**
	 * Constructeur du Widget Compose faisant appel au constructeur de sa classe mère (
	 * <code>Widget</code>).
	 *
	 * @param comp le modèle du widget à créer
	 */
	public WidgetCompose(final ModeleWidget comp) {
		super(comp);
		this.mapZone = new LinkedHashMap<YComparableRectangle, List<Widget>>();
		for (YComparableRectangle r : comp.getZonesAccroches()) {
			this.mapZone.put(r, new LinkedList<Widget>());
		}
	}

	/**
	 * Méthode permettant de supprimer un widget au sein d'une zone d'accroche.
	 *
	 * @param cle la clé de la HashMap où supprimer le widget
	 * @param widget le widget à supprimer de la zone
	 * @return un booléen attestant de la suppression ou non du widget au sein de la HashMap.
	 */
	public boolean supprimerWidget(final YComparableRectangle cle, final Widget widget) {
		return this.mapZone.get(cle).remove(widget);
	}

	/**
	 * Méthode retournant la HashMap représentant les zones d'accroche du composant.
	 *
	 * @return la HashMap représentant les zones d'accroches du composant
	 */
	public HashMap<YComparableRectangle, List<Widget>> getMapZone() {
		return this.mapZone;
	}

	/*
	 * public HashMap<Rectangle,List<Widget>> getComposition() { return
	 * this.mapZone.; }
	 */
	/**
	 * Méthode permettant de rechercher, au sein de toutes les zones d'accroches
	 * du composant, un composant en particulier et ainsi retourner la liste de Widgets de la zone d'accroche à laquelle ce composant appartient.
	 *
	 * @param comp le composant à rechercher au sein des zones d'accroche du composant complexe
	 * @return la liste de widgets de la zone d'accroche qui contient comp
	 * @throws ComposantIntrouvableException si le Widget n'est pas trouvé dans aucune des zones d'accroches du composant complexe
	 */
	public List<Widget> getWidgetsAssocies(final Widget comp) throws ComposantIntrouvableException {
		List<Widget> l = null;
		boolean trouve = false;
		for (List<Widget> lst : this.mapZone.values()) {
			if (lst.contains(comp)) {
				trouve = true;
				l = lst;
				break;
			}
			if (trouve) {
				break;
			}
		}
		if (!trouve) {
			throw new ComposantIntrouvableException();
		}
		return l;
	}

	@Override
	public boolean isComplexe() {
		return true;
	}

	@Override
	public boolean isRacine() {
		return false;
	}

	/**
	 * Méthode permettant de recalculer les positions et dimensions de chaque zones d'accroches du composant complexe.
	 */
	public void notifyChange() {
		HashMap<YComparableRectangle, YComparableRectangle> mapRect = new HashMap<YComparableRectangle, YComparableRectangle>();
		HashMap<YComparableRectangle, Integer> mapDecal = new HashMap<YComparableRectangle, Integer>();
		for (YComparableRectangle r : mapZone.keySet()) {
			int decalY = 0;
			Rectangle maxBounds = null;

			//Redimensionnement les zones d'accroches
			for (Widget w : mapZone.get(r)) {

				w.setLocation((int) (this.getLocation().getX() + r.getX()), (int) (this.getLocation().getY() + r.getY() + decalY));
				//Widget parent = (Widget)w.parent();
				if (w.isComplexe()) {
					((WidgetCompose) w).notifyChange();
				}
				if (maxBounds == null) {
					maxBounds = new Rectangle(w.getBounds());
					decalY += w.getBounds().height - ModeleWidget.OFFSET;
				} else {
					maxBounds = maxBounds.union(w.getBounds());
					decalY += w.getBounds().height - ModeleWidget.OFFSET;
				}
			}

			if (maxBounds == null) {
				maxBounds = new Rectangle();
			}
			maxBounds.height -= ModeleWidget.OFFSET;
			maxBounds.height = Math.max(maxBounds.height, Variables.ECART_PAR_DEFAULT);
			int diff = maxBounds.height - r.height;

			//On stocke le décalage qu'on voudra appliquer sur les zones d'accroche du composant
			this.getModele().decalageY(diff, r);
			decaleZonesEnDessousDe(r.y, diff, mapDecal);

			YComparableRectangle bnds = new YComparableRectangle(r);
			bnds.height = maxBounds.height;
			//On stocke les nouvelles bounds des zone ou il y a des changements
			mapRect.put(r, bnds);
			this.setForme(false);
		}

		//Une fois sorti de la boucle ...
		// ...On decale les nouvelles bounds qu'on a stocké plus haut si il y a besoin
		for (YComparableRectangle r : mapDecal.keySet()) {
			if (mapRect.get(r) != null) {
				YComparableRectangle rectDecal = new YComparableRectangle(mapRect.get(r));
				rectDecal.y += mapDecal.get(r);
				mapRect.put(r, rectDecal);
			}
		}
		// Et enfin on finit par attribuer a chaque zone ses nouvelles bounds
		for (YComparableRectangle r : mapRect.keySet()) {
			this.mapZone.put(mapRect.get(r), this.mapZone.remove(r));
		}

		//Remise dans l'ordre de la Hashmap des zones d'accroche
		if (mapZone.keySet().size() > 1) {
			LinkedList<YComparableRectangle> collRect = new LinkedList<YComparableRectangle>(mapZone.keySet());
			Collections.sort(collRect);
			LinkedHashMap<YComparableRectangle, List<Widget>> newMap = new LinkedHashMap<YComparableRectangle, List<Widget>>();
			for (YComparableRectangle rect : collRect) {
				newMap.put(rect, mapZone.get(rect));
			}
			this.mapZone = newMap;
		}
	}

	/**
	 * Méthode permettant de décaler toutes les zones d'accroches situées en dessous d'un certaine zone d'accroche.
	 *
	 * @param y la position en Y de la zone d'accroche qui a été agrandie
	 * @param diff la valeur de laquelle il faut décaler chaque zone d'accroche en dessous de y
	 * @param map la Hashmap dans laquelle il faut rechercher les zones à décaler
	 */
	private void decaleZonesEnDessousDe(int y, int diff, HashMap<YComparableRectangle, Integer> map) {
		for (YComparableRectangle r : this.mapZone.keySet()) {
			if (r.y > y) {
				map.put(r, diff);
			}
		}
	}

	@Override
	public void applyChangeModele() {
		this.getModele().applyChangeModele();
		// Tester le type du modele widget courant
		if (this.getModele().getType() == TypeModeleWidget.IFELSE) {
			// Cas du if...else
			InstructionIfElse structInst = (InstructionIfElse) this.getModele().getElementProgramme();
			// Récupérer clé mapZone du if et du else clé du if <=> rectangle supérieur clé du else <=> rectangle inféreur
			// Suppression des instructions du if et du else dans l'arbre des instruction
			// Récupérer les instructions des widgets du if les ajouté à l'arbre des instructions
			List<Widget> widgets = recupeAllWidgetCorps(1);
			structInst.removeEnfantsIf();
			for (Widget widget : widgets) {
				Instruction inst = (Instruction) widget.getElementProgramme();
				structInst.insererFinIf(inst);
			}
			// Récupérer les instructions des widgets du else les ajouté à l'arbre des instructions
			widgets = recupeAllWidgetCorps(0);
			structInst.removeEnfantsElse();
			for (Widget widget : widgets) {
				Instruction inst = (Instruction) widget.getElementProgramme();
				structInst.insererFinElse(inst);
			}

		} else {
			// Cas des instructions structures autre que IfElse
			this.applyChangeStructInst();
		}
	}

	/**
	 * Méthode permettant de récupérer tous les widgets d'une zone d'accroche.
	 *
	 * @param i l'index de la zone à récupérer
	 * @return la liste des widgets situés à l'index i
	 */
	private List<Widget> recupeAllWidgetCorps(int i) {
		// Récupérer la clé du corps
		Set<YComparableRectangle> keys = this.mapZone.keySet();
		// Récupérer les widgets du contenue
		Object[] rects = keys.toArray();
		List<Widget> widgets = this.mapZone.get(rects[i]);
		return widgets;
	}

	/**
	 * Méthode permettant d'appliquer les changements à la structure du composant.
	 */
	private void applyChangeStructInst() {
		// Cas de la tâche
		InstructionStructure structInst = (InstructionStructure) this.getModele().getElementProgramme();
		// Récupération des widgets fils
		List<Widget> widgets = recupeAllWidgetCorps(0);
		// Suppression des enfants de la tâche
		structInst.removeEnfants();
		// Ajout de tous les enfants nouveaux et anciens
		for (Widget widget : widgets) {
			Instruction inst = (Instruction) widget.getElementProgramme();
			structInst.insererFin(inst);
		}
	}

	@Override
	public Element toXml() {
		Element widget = super.toXml();
		widget.setName("widgetcompose");

		// Gestion des widgets internes
		int i = 0;
		for (Rectangle zone : this.mapZone.keySet()) {
			Element accroche = new Element("accroche");
			accroche.setAttribute(new Attribute("id", String.valueOf(i)));
			i++;

			for (Widget widgetInterne : this.mapZone.get(zone)) {
				accroche.addContent(widgetInterne.toXml());
			}
			widget.addContent(accroche);
		}
		return widget;
	}
}