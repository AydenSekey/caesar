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
package jscratch.vue.arborescence;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import javax.swing.SwingUtilities;

import jscratch.dictionnaires.DicoTraces;
import nxtim.instruction.Categorie;
import jscratch.dictionnaires.DicoWidgetsCategories;
import jscratch.exceptions.ComposantIntrouvableException;
import jscratch.exceptions.NonClonableException;
import jscratch.helpers.ErreurHelper;
import jscratch.traces.fabriques.FabriqueTrace;
import jscratch.traduction.LanceurTraduction;
import jscratch.vue.arborescence.actions.Action;
import jscratch.vue.arborescence.actions.TypeAction;
import jscratch.vue.categories.boutons.BoutonCategorie;
import jscratch.vue.ginterface.principales.GUI;
import jscratch.vue.ginterface.principales.panels.GlassPane;
import jscratch.vue.ginterface.principales.panels.PanelCodeGraphique;
import jscratch.vue.ginterface.principales.panels.PanelTypeWidget;
import jscratch.vue.ginterface.principales.panels.PanelWidget;
import jscratch.vue.widgets.IWidget;
import jscratch.vue.widgets.Widget;
import jscratch.vue.widgets.WidgetCompose;
import jscratch.vue.widgets.modeles.ModeleWidget;
import jscratch.vue.widgets.modeles.TypeModeleWidget;
import jscratch.vue.widgets.modeles.zones.ChampTexte;
import jscratch.vue.widgets.modeles.zones.Zone;

/**
 * Classe implémentant le design pattern Singleton permettant de gérer le
 * système de drag & drop.
 *
 * @since 1.0
 * @version 1.0
 */
public final class DragAndDropTools extends Observable {

	/**
	 * List de widgets contenant les widgets en cours de draggage.
	 */
	private List<Widget> composantsDrague;
	/**
	 * Instance statique unique de la Classe.
	 */
	private static DragAndDropTools instance = new DragAndDropTools();

	private boolean deplacement = false;
	private int empAvant;

	/**
	 * Constructeur privé de la classe initialisant la liste de widgets à vide.
	 */
	private DragAndDropTools() {
		this.composantsDrague = new LinkedList<Widget>();
		this.addObserver(GUI.getGlassPane());
		this.addObserver(GUI.getPanelCodeGraphique());
		this.addObserver(GUI.getPanelWidget());
	}

	/**
	 * Méthode statique permettant de récupérer l'instance unique de la classe.
	 *
	 * @return L'instance unique de la classe.
	 */
	public static DragAndDropTools getInstance() {
		return instance;
	}

	/**
	 * Méthode appelée lors du clic sur un widget. Cette méthode va déplacer le
	 * widget cliqué ainsi que tous les widgets attachés sous lui sur le
	 * GlassPane et enlever ces derniers du PanelCodeGraphique.
	 *
	 * @param comp Le composant cliqué
	 * @param ptClick Le point où a eu lieu le clique au sein du widget
	 */
	public void clickWidget(Widget comp, Point ptClick) {
		comp.setPtClick(ptClick);
		ArborescenceTools arbo = ArborescenceTools.getInstance();
		Action act = FusionTools.checkSurvolWidgetV2(comp);
		empAvant = act.getVal();
		deplacement = comp.isDraggable();

		// Est-ce que l'on a cliqué sur un widget de la liste des widgets ?
		if (!deplacement) { // Oui
			clickWidgetOfListWidgets(comp);
		} else { // Non, il s'agit d'un widget dans la zone d'édition
			clickWidgetOfEditor(comp);
		}

		// Déplacer l'ensemble des composants à dragguer sur le GlassPane
		for (Widget w : composantsDrague) {
			passerSurAutrePanel(w, GUI.getGlassPane());
		}
		// Puis affiché le GlassPane
		GUI.getGlassPane().setVisible(true);

		/* 
		 * Calculer la position sur le GlassPane des composants draggués correspondante à celle sur leur ancien containeur
		 * de manière à ce qu'elle reste la même par rapport à la souris (et donc à l'écran).
		 */
		Point p = GUI.getGlassPane().getMousePosition();
		p.x -= ptClick.x;
		p.y -= ptClick.y;
		
		dragGroupeWidget(composantsDrague, p);

		arbo.updateWidgets();

		GUI.getPanelCodeGraphique().updateSize(arbo.getArborescence());
		this.setChanged();
		this.notifyObservers();
	}
	
	/**
	 * Traitement lors du clic sur un widget de la liste des widgets
	 * 
	 * @param comp le composant sur lequel on a cliqué.
	 */
	private void clickWidgetOfListWidgets(Widget comp) {
		comp.setDraggable(true);
		Widget compNouv;

		// Si c'est la tache main, on supprime le widget du panel
		if (comp.getType() == TypeModeleWidget.TACHE) {
			PanelTypeWidget.getInstance().supprimerTachePrincipale();
		}
		else { // sinon on remplace par un nouveau widget
			try {
				PanelWidget pw = GUI.getPanelWidget();
				// Le nouveau est un clone du compsosant draggué
				compNouv = pw.getFabrique().cloner(comp);
				int ind = pw.getIndex(comp);
				pw.supprimerWidget(comp);
				pw.ajouterWidget(compNouv, ind);
				DicoWidgetsCategories.getInstance().remplacerWidgetDansCategorie(GUI.getPanelTypeWidget().getCurrentCategorie(), comp, compNouv);
			} catch (NonClonableException ex) {
				ErreurHelper.afficher(ex);
			}
		}
		composantsDrague = new LinkedList<Widget>();
		composantsDrague.add(comp);
	}
	
	/**
	 * Traitement lors du clic sur un widget de la zone d'édition.
	 * 
	 * @param comp le composant sur lequel on a cliqué.
	 */
	private void clickWidgetOfEditor(Widget comp) {
		ArborescenceTools arbo = ArborescenceTools.getInstance();
		try {
			//recuperation et detachement des widgets dragués
			composantsDrague = new LinkedList<Widget>(arbo.getSuivants(comp, true));

			//supression des widgets dans l'arborescence
			arbo.supprimerWidgets(composantsDrague);

			if ((comp != null) && (comp.parent() != null) && (!comp.parent().isRacine())) {
				((Widget) comp.parent()).applyChangeModele();
			}

			comp.defParent(null);

		} catch (ComposantIntrouvableException ex) {
			ErreurHelper.afficher(ex);
		}	
	}

	/**
	 * Méthode récursive permettant de passer un widget d'un conteneur à
	 * l'autre. Si le widget passé en paramètre est de type complexe, cette
	 * méthode est de nouveau appelée pour chacun de ses fils.
	 *
	 * @param wi Le widget à passer sur un autre Panel
	 * @param destination Le panel de destination du widget
	 */
	public void passerSurAutrePanel(Widget wi, Container destination) {
		if (wi.isComplexe()) {
			for (List<Widget> lw : ((WidgetCompose) wi).getMapZone().values()) {
				for (Widget w : lw) {
					passerSurAutrePanel(w, destination);
				}
			}
		}
		Point p = wi.getLocationOnScreen();
		SwingUtilities.convertPointFromScreen(p, destination);
		wi.setLocation(p);
		wi.getParent().remove(wi);
		destination.add(wi);
	}

	/**
	 * Méthode permettant de déplacer un groupe de widgets selon un point
	 * particulier.
	 *
	 * @param lst La liste de widgets à déplacer
	 * @param p Le Point à partir duquel il faut placer le widget
	 */
	public void dragGroupeWidget(List<Widget> lst, Point p) {
		for (Widget w : lst) {
			w.setLocation(p.x, p.y);
			if (w.isComplexe()) {
				WidgetCompose wComp = (WidgetCompose) w;
				wComp.notifyChange();
			}
			p.y += w.getHeight() - ModeleWidget.OFFSET;
		}
	}

	/**
	 * Méthode appelée lors du drag de la souris. Cette méthode va déplacer sur
	 * le GlassPane les widgets en cours de drag.
	 *
	 * @param comp Le widget où a eu lieu l'événement souris d'origine
	 */
	public void dragWidget(Widget comp) {
		if (comp.isDraggable()) {
			Point ptClick = comp.getPtClick();
			Point pMouse = MouseInfo.getPointerInfo().getLocation();
			Point diff = new Point((int)(GUI.getZoneUtilisateur().getLocationOnScreen().getX() - GUI.getFenetre().getViewport().getLocationOnScreen().getX() + 5),(int)(GUI.getZoneUtilisateur().getLocationOnScreen().getY() - GUI.getFenetre().getViewport().getLocationOnScreen().getY() + 5));
			Rectangle recZoneUtil = new Rectangle((int)(GUI.getZoneUtilisateur().getLocationOnScreen().getX() - diff.getX()), (int)(GUI.getZoneUtilisateur().getLocationOnScreen().getY() - diff.getY()), GUI.getZoneUtilisateur().getWidth(), GUI.getZoneUtilisateur().getHeight());
			Rectangle boundsGroup = groupeWidgetBounds(composantsDrague, 0, null);
			if (boundsGroup == null) {
				boundsGroup = new Rectangle();
			}

			Rectangle recWid = new Rectangle(new Point((int) (pMouse.x - ptClick.getX() - diff.getX()), (int) (pMouse.y - ptClick.getY()- diff.getY())), new Dimension((int) boundsGroup.getWidth(), (int) boundsGroup.getHeight()));
			recZoneUtil.setBounds(recZoneUtil.getBounds().x, recZoneUtil.getBounds().y, recZoneUtil.getBounds().width, recZoneUtil.getBounds().height);

			if (!recZoneUtil.contains(recWid)) {
				boolean noX = false;
				if (recWid.getMinX() <= recZoneUtil.getMinX()) {
					// A gauche
					pMouse.x = (int) recZoneUtil.getMinX();
					pMouse.y -= ptClick.y + diff.getY();
					noX = true;
				} else if (recWid.getMaxX() > recZoneUtil.getMaxX()) {
					// A droite
					pMouse.x = (int) recZoneUtil.getMaxX() - recWid.width;
					pMouse.y -= ptClick.y + diff.getY();
					noX = true;
				}

				if (recWid.getMinY()<= recZoneUtil.getMinY()) {
					// En haut
					pMouse.y = (int) recZoneUtil.getMinY();
					if (!noX) {
						pMouse.x -= ptClick.x + diff.getX();
					}
				} else if (recWid.getMaxY() >= recZoneUtil.getMaxY()) {
					// En bas
					pMouse.y = (int) recZoneUtil.getMaxY() - recWid.height;
					if (!noX) {
						pMouse.x -= ptClick.x + diff.getX();
					}
				}
			} else {
				pMouse.x -= ptClick.x + diff.getX();
				pMouse.y -= ptClick.y + diff.getY();
			}
			pMouse.x -= GUI.getFenetre().getLocation().getX();
			pMouse.y -= GUI.getFenetre().getLocation().getY();
			dragGroupeWidget(composantsDrague, pMouse);

			this.setChanged();
			this.notifyObservers();
			int decal = (int) (Widget.TAUX_TRANSFERT_PANEL * comp.getWidth());
			int inter = (int) (boundsGroup.getMaxX() - GUI.getPanelCodeGraphique().getScroll().getBounds().getMinX());
			GlassPane g = GUI.getGlassPane();
			if (inter < decal) {
				Point ptDel = new Point(comp.getLocation());
				ptDel.translate(-20, -20);
				g.setDeleteIconPosition(ptDel);
			} else {
				g.setDeleteIconPosition(null);
			}
			FusionTools.dessinerIndicateursFusion(comp);

		}
	}

	/**
	 * Méthode appelée lors du relâchement de la souris après avoir dragué des
	 * composants.
	 */
	public void dropWidget() {


		Widget comp = composantsDrague.get(0);
		Action a = FusionTools.checkSurvolWidgetV2(comp);
		PanelCodeGraphique p = GUI.getPanelCodeGraphique();
		GlassPane g = GUI.getGlassPane();
		Rectangle r = (Rectangle) comp.getBounds().clone();

		Point pt = comp.getLocationOnScreen();

		int decal = (int) (Widget.TAUX_TRANSFERT_PANEL * comp.getWidth());
		int inter = (int) (r.getMaxX() - p.getScroll().getBounds().getMinX());

		ArborescenceTools arbo = ArborescenceTools.getInstance();
		boolean complexe = false;
		Widget compSurvole = null;
		try {
			if (inter >= decal) {
				p.add(comp);

				SwingUtilities.convertPointFromScreen(pt, p);
				if (inter < comp.getWidth()) {
					pt.x += (comp.getWidth() - inter) + (GUI.getZoneUtilisateur().getLocationOnScreen().getX() - GUI.getFenetre().getViewport().getLocationOnScreen().getX() + 5);

				}
				compSurvole = a.getComp();

				if (a.getVal() == TypeAction.INTERNE.toInt()) {
					Zone z = compSurvole.getModele().getLesZonesSaisies().get(a.getZoneIndex());
					DicoTraces.getInstance().ajouterTrace(FabriqueTrace.creerTraceWidgetModification(comp, z, ((ChampTexte) z).getValeur(), composantsDrague.get(0)));
					((ChampTexte) z).setWidgetContenu(composantsDrague.get(0));


				} else {
					switch (a.getVal()) {
						case 1:
							//Au dessus du compSurvole
							Widget w1 = (Widget) (a.getComp());
							arbo.ajouterWidgets(composantsDrague, compSurvole, false);
							if(!deplacement){
								DicoTraces.getInstance().ajouterTrace(FabriqueTrace.creerTraceWidgetAjout(comp, w1, a.getVal()));
							}else{
								DicoTraces.getInstance().ajouterTrace(FabriqueTrace.creerTraceWidgetDeplacement(comp, w1, a.getComp(),empAvant,a.getVal()));
							}
							break;

						case 0:
							//En dessous du compSurvole
							Widget w0 = (Widget) (a.getComp());
							arbo.ajouterWidgets(composantsDrague, compSurvole, true);
							if(!deplacement){
								DicoTraces.getInstance().ajouterTrace(FabriqueTrace.creerTraceWidgetAjout(comp, w0, a.getVal()));
							}else{
								DicoTraces.getInstance().ajouterTrace(FabriqueTrace.creerTraceWidgetDeplacement(comp, w0, a.getComp(),empAvant,a.getVal()));
							}
							break;

						case -1:
							//Aucun survol
							Widget w11 = (Widget) (a.getComp());
							arbo.ajouterWidgets(composantsDrague, compSurvole, true);
							if(!deplacement)
								DicoTraces.getInstance().ajouterTrace(FabriqueTrace.creerTraceWidgetAjout(comp, w11, a.getVal()));
							else
								DicoTraces.getInstance().ajouterTrace(FabriqueTrace.creerTraceWidgetDeplacement(comp, w11, a.getComp(),empAvant,a.getVal()));

							break;

						case 2:
							//Survol d'une zone d'accroche
							WidgetCompose wComp = (WidgetCompose) (a.getComp());
							List<Widget> lst = wComp.getMapZone().get(a.getRect());
							lst.addAll(composantsDrague);
							if(!deplacement)
								DicoTraces.getInstance().ajouterTrace(FabriqueTrace.creerTraceWidgetAjout(comp, wComp, a.getVal()));
							else
								DicoTraces.getInstance().ajouterTrace(FabriqueTrace.creerTraceWidgetDeplacement(comp, wComp, a.getComp(),empAvant,a.getVal()));

							break;

					}

					for (Widget w : composantsDrague) {

						passerSurAutrePanel(w, p);

						if (compSurvole == null) {
							w.defParent((IWidget) p);//gestion du parent suivant element survole

						} else {
							//Survol d'une zone d'accroche
							if (a.getVal() == 2) {
								complexe = true;
								w.defParent((WidgetCompose) compSurvole);
							} else {
								w.defParent(compSurvole.parent());
							}
						}
					}

					if (compSurvole != null && compSurvole.isComplexe()) {
						//((WidgetCompose) compSurvole).applyChangeModele();
						if (a.getVal() == 2) {
							((WidgetCompose) compSurvole).applyChangeModele();

						} else {
							if (compSurvole.parent() != null && !compSurvole.parent().isRacine()) {
								((WidgetCompose) compSurvole.parent()).applyChangeModele();

							}
						}
					} else if (compSurvole != null && !compSurvole.isComplexe() && compSurvole.parent() != null && !compSurvole.parent().isRacine()) {
						((WidgetCompose) compSurvole.parent()).applyChangeModele();

					}
				}

			} else {
				arbo.supprimerWidgets(composantsDrague);
				for (Widget w : composantsDrague) {
					deleteWidgetsFromGlassPane(w);
				}
			}
			switch (a.getVal()) {
				case 1:
					//Au dessus du compSurvole
					Rectangle bnds = groupeWidgetBounds(arbo.getListe(comp), composantsDrague.size(), null);
					if (bnds == null) {
						bnds = new Rectangle();
					}
					pt = bnds.getLocation();
					for (Widget w : composantsDrague) {
						pt.y -= (w.getHeight() - ModeleWidget.OFFSET);
					}
					break;

				case 0:
					//En dessous du compSurvole
					pt = arbo.getListe(comp).get(0).getLocation();
					break;

			}
			if (complexe) {
				dragGroupeWidget(arbo.getSuivants((Widget) (comp.parent()), true), ((Widget) comp.parent()).getLocation());
			} else {
				dragGroupeWidget(arbo.getListe(comp), pt);
			}
			composantsDrague.clear();

		} catch (ComposantIntrouvableException ex) {
			ErreurHelper.afficher(ex);
		}
		arbo.updateWidgets();


		p.updateSize(arbo.getArborescence());

		this.setChanged();
		this.notifyObservers();

		g.setPointLigneSurEcran(null);
		g.setRectFusion(null);
		g.setDeleteIconPosition(null);

		if (compSurvole != null) {
			compSurvole.getModele().applyChangeModele();
		}

		// Suppression de la tache principale de la liste des widgets
		if (comp.getType() == TypeModeleWidget.TACHE &&
				GUI.getPanelTypeWidget().getCurrentCategorie() == Categorie.STRUCTURES) {
			for (BoutonCategorie bc : PanelTypeWidget.getInstance().getLesCategories()) {
				if (bc.getCategorie() == Categorie.STRUCTURES) {

					GUI.getPanelWidget().setLesWidgets(bc.getNbColonnes());
					break;
				}
			}
		}

		LanceurTraduction.getInstance().lancerTraduction();

	}

	/**
	 * Méthode récursive permettant de supprimer un widget du GlassPane. Si le
	 * widget passé en paramètre est de type complexe, cette méthode est de
	 * nouveau appelée pour chacun de ses fils.
	 *
	 * @param comp le widget à supprimer
	 */
	private void deleteWidgetsFromGlassPane(Widget comp) {
		if (comp.isComplexe()) {
			for (List<Widget> lw : ((WidgetCompose) comp).getMapZone().values()) {
				for (Widget w : lw) {
					deleteWidgetsFromGlassPane(w);
				}
			}
		}

		if (comp.getType() == TypeModeleWidget.TACHE) {
			((WidgetCompose) comp).clean();
			PanelTypeWidget.getInstance().ajouterTachePrincipale();
		}

		GUI.getGlassPane().remove(comp);
		DicoTraces.getInstance().ajouterTrace(FabriqueTrace.creerTraceWidgetSuppression(comp));
	}

	/**
	 * Méthode permettant de calculer les Dimensions e position d'un groupe de
	 * widgets.
	 *
	 * @param lst Le groupe de widgets pour lesquels on veut recupérer les
	 * dimensions et positions
	 * @param index L'index à partir duquel on veut commencer à calculer les
	 * dimensions et positions du groupe
	 * @param rect Le rectangle utilisé pendant les appels récursifs
	 * @return Le Rectangle regroupant les positions et dimensions du goupe de
	 * widgets passé en paramètre ou null si la listede widgets est vide
	 */
	public static Rectangle groupeWidgetBounds(List<Widget> lst, int index, Rectangle rect) {
		for (int i = index; i < lst.size(); i++) {
			Widget w = lst.get(i);
			if (w.isComplexe()) {
				WidgetCompose comp = (WidgetCompose) w;
				for (List<Widget> l : comp.getMapZone().values()) {
					rect = groupeWidgetBounds(l, 0, rect);
				}
			}
			if (rect == null) {
				rect = w.getBounds();
			} else {
				rect = rect.union(w.getBounds());
			}
		}
		return rect;
	}
}
