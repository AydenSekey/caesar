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
package jscratch.vue.widgets;

import jscratch.helpers.FontHelper;
import jscratch.vue.widgets.modeles.ModeleWidget;
import jscratch.vue.widgets.modeles.zones.Zone;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import jscratch.vue.widgets.modeles.TypeModeleWidget;
import nxtim.instruction.IElementProgramme;

import org.jdom2.Attribute;
import org.jdom2.Element;

import jscratch.vue.draganddrop.DragAndDropTools;
import jscratch.vue.ginterface.principales.GUI;
import jscratch.vue.widgets.modeles.ExpressionArithmeticWidget;
import jscratch.vue.widgets.modeles.ExpressionLogicalWidget;
import jscratch.vue.widgets.modeles.IncrementationWidget;
import jscratch.vue.widgets.modeles.VariableWidget;
import nxtim.instruction.Condition;
import nxtim.instruction.InstructionIncrementation;
import nxtim.instruction.Operation;

/**
 * Classe représentant graphiquement un widget.
 */
public class Widget extends JComponent {

	/**
	 * Ratio à partir duquel, si on dépose un widget à cheval entre le
	 * <code>PanelInstruction</code> et le
	 * <code>PanelCodeGraphique</code>,
	 * on peut choisir soit de déplacer un widget pour qu'il soit entièrement sur le PanelCodeGraphique, ou bien le supprimer.
	 */
	public static final float TAUX_TRANSFERT_PANEL = (float) 0.6;
	/**
	 * Point au sein du widget en cours de draggage où a eu lieu le click d'origine.
	 */
	private Point ptClick;
	/**
	 * Valeur booléenne permettant de savoir si un widget peut être dragué ou non.
	 */
	private boolean draggable = false;
	/**
	 * Valeur booléenne permettant de savoir si un widget est en train d'être dragué ou non
	 */
	private boolean dragging = false;
	/**
	 * Modèle du widget.
	 */
	private ModeleWidget modele;
	/**
	 * Police de caractères utilisée par le widget.
	 */
	private Font font;
	/**
	 * Le parent du Widget.<br/>
	 * Il peut être soit un composant complexe, soit le PanelCodeGraphique, soit
	 * <code>null</code> quand le Widget est en cours de draggage.
	 */
	private IWidget parent;

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(this.modele.getCouleur());
		g2d.fillPolygon(this.modele.getForme());
		g2d.setColor(Color.BLACK);
		g2d.drawPolygon(this.modele.getForme());
		g2d.setColor(Color.WHITE);
		g2d.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		for (Point p : this.modele.getMessage().keySet()) {
			if (this.modele.isConditionHaute()) {
				g2d.drawString(this.modele.getMessage().get(p), p.x, p.y);
			} else {
				g2d.drawString(this.modele.getMessage().get(p), p.x, this.getHeight() - p.y);
			}
		}
	}

	/**
	 * Constructeur du Widget initialisant toutes les propriétés du Widget.
	 *
	 * @param modele le modèle de structure à appliquer au Widget
	 */
	public Widget(final ModeleWidget modele) {
		this.font = FontHelper.getWidgetFont();
		this.modele = modele;
		this.setFont(this.font);
		this.setFocusable(true);
		this.setOpaque(true);
		this.setForme(true);

		initListeners();

		for (Zone z : this.modele.getLesZonesSaisies()) {
			this.add((JComponent) z);
		}

		this.parent = null;
	}

	/**
	 * Méthode permettant de définir la forme du widget et de calculer ses
	 * nouvelles dimensions et localisations.
	 *
	 * @param setLocation Permet de choisir si il faut également redéfinir la localisation du widget
	 */
	public void setForme(final boolean setLocation) {
		int maxX = 0;
		for (Integer i : this.modele.getForme().xpoints) {
			maxX = Math.max(maxX, i);
		}

		int maxY = 0;
		for (Integer i : this.modele.getForme().ypoints) {
			maxY = Math.max(maxY, i);
		}

		if (setLocation) {
			this.setBounds(0, 0, maxX + 1, maxY + 1);
		} else {
			this.setSize(maxX + 1, maxY + 1);
		}
		this.setPreferredSize(new Dimension(maxX, maxY));
	}

	/**
	 * Méthode d'initialisation des listeners du widget (click, drag et drop).
	 */
	public void initListeners() {
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					DragAndDropTools.getInstance().clickWidget((Widget) e.getComponent(), e.getPoint());
				}
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					DragAndDropTools.getInstance().dropWidget();
				}
			}
		});

		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(final MouseEvent e) {
				DragAndDropTools.getInstance().dragWidget((Widget) e.getComponent());
			}
		});
	}

	/**
	 * Permet de récupérer le modèle du widget.
	 *
	 * @return le modèle du Widget
	 */
	public ModeleWidget getModele() {
		return this.modele;
	}

	/**
	 * Permet de savoir si le widget est draggable.
	 *
	 * @return <code>true</code> si le composant est draggable, sinon <code>false</code>
	 */
	public boolean isDraggable() {
		return this.draggable;
	}

	/**
	 * Définie si un composant est draggable.
	 *
	 * @param draggable la valeur définissant si le composant sera draggable ou non
	 */
	public void setDraggable(final boolean draggable) {
		this.draggable = draggable;
	}

	/**
	 * Définie si un composant est en train dragué.
	 *
	 * @param dragging la valeur définissant si le composant est en cours de draggage.
	 */
	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

	
	/**
	 * Permet de savoir si le widget est dragué.
	 *
	 * @return <code>true</code> si le composant est dragué, sinon <code>false</code>
	 */
	public boolean isDragging() {
		return dragging;
	}

	/**
	 * Méthode permettant de définir le point où a eu lieu le click au sein du widget.
	 *
	 * @param ptClick le point de click sur le widget
	 */
	public void setPtClick(final Point ptClick) {
		this.ptClick = ptClick;
	}

	/**
	 * Méthode permettant de récupérer le point où a eu lieu le click sur le widget.
	 *
	 * @return le point où a eu lieu le click au sein du widget
	 */
	public Point getPtClick() {
		return this.ptClick;
	}

	/**
	 * Méthode permettant de savoir si un composant est complexe ou non.
	 *
	 * @return <code>true</code> si le composant est complexe, <code>false</code> dans le cas contraire
	 */
	public boolean isComplexe() {
		return false;
	}

	/**
	 * Méthode permettant de récupérer le parent du composant au sein de l'arborescence.
	 *
	 * @return le parent du Widget au sein de l'arborescence
	 */
	public IWidget parent() {
		return parent;
	}

	/**
	 * Méthode permettant de définir le parent du Widget.<br/>
	 * Ce parent peut être soit :
	 * <ul>
	 * <li>le PanelCodeGraphique</li>
	 * <li>un composant Complexe</li>
	 * <li><code>null</code> si le composant est un cours de draggage</li>
	 * </ul>
	 *
	 * @param parent le nouveau parent du Widget
	 */
	public void defParent(final IWidget parent) {
		this.parent = parent;
	}

	/**
	 * Méthode permettant de récupérer le type du Widget au sein de son modèle.
	 *
	 * @return le type du modèle du Widget
	 */
	public TypeModeleWidget getType() {
		return this.modele.getType();
	}

	/**
	 * Méthode permettant de récupérer l'élément de programme du widget au sein de son modèle.
	 *
	 * @return l'élément de programme du modèle du widget
	 */
	public IElementProgramme getElementProgramme() {
		return this.modele.getElementProgramme();
	}

	/**
	 * Met à jour l'arborescence des instructions.
	 */
	public void applyChangeModele() {
	}

	/**
	 * Permet d'avoir comment le widget doit se sérialiser.
	 */
	public Element toXml() {
		Element widget = new Element("widget");
		widget.setAttribute(new Attribute("classe", this.modele.getClass().getSimpleName()));
		widget.setAttribute("categorie", this.modele.getCategorie().toString());

		// Si le widget est une variable, ajout du nom de la variable
		if (VariableWidget.class.getSimpleName().equals(this.modele.getClass().getSimpleName())) {
			widget.setAttribute("supplement", ((VariableWidget) this.getModele()).getNomVariable());
		}

		// Si le widget est une expression arithmtique, ajout de l'operateur
		if (ExpressionArithmeticWidget.class.getSimpleName().equals(this.modele.getClass().getSimpleName())) {
			widget.setAttribute("supplement", ((Operation) this.getModele().getElementProgramme()).getOperateur().toString());
		}

		// Si le widget est une expression logique, ajout de l'opérateur
		if (ExpressionLogicalWidget.class.getSimpleName().equals(this.modele.getClass().getSimpleName())) {
			widget.setAttribute("supplement", ((Condition) this.getModele().getElementProgramme()).getOperateur().toString());
		}

		if (IncrementationWidget.class.getSimpleName().equals(this.modele.getClass().getSimpleName())) {
			widget.setAttribute("supplement", ((InstructionIncrementation) this.getModele().getElementProgramme()).getOperateur().toString());
		}

		// Si le widget est placé sur le PanelCodeGraphique, ajout des coordonnées. Sinon, les oordonnées seront recalculées automatiquement.
		if (this.parent == GUI.getPanelCodeGraphique()) {
			Element coordonnees = new Element("coordonnees");
			coordonnees.setAttribute(new Attribute("x", String.valueOf(this.getLocation().x)));
			coordonnees.setAttribute(new Attribute("y", String.valueOf(this.getLocation().y)));
			widget.addContent(coordonnees);
		}

		// Gestion des zones du widget (valeurs, variables, ...)
		if (this.modele.getLesZonesSaisies().size() > 0) {
			Element attribut = new Element("attributs");

			int i = 0;
			for (Zone z : this.modele.getLesZonesSaisies()) {
				Element zone = z.toXml();
				zone.setAttribute(new Attribute("id", String.valueOf(i)));
				i++;
				attribut.addContent(zone);
			}
			widget.addContent(attribut);
		}

		return widget;
	}
}
