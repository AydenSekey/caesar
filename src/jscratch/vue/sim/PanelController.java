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
package jscratch.vue.sim;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import de.javasoft.swing.ButtonBar;
import de.javasoft.swing.SimpleDropDownButton;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import jscratch.controleur.sim.Simulator;
import jscratch.dictionnaires.DicoTraces;
import jscratch.helpers.ImagesHelper;
import jscratch.helpers.LangueHelper;
import jscratch.interpreteur.GestionSimulation;
import jscratch.parametrages.langue.VariableLangue;
import jscratch.traces.fabriques.FabriqueTrace;

/**
 * Panel permettant de controller l'execution du simulateur
 */
public class PanelController extends JPanel {

	private ButtonBar buttonBar;
	private AbstractButton bExec;
	private AbstractButton bPause;
	private AbstractButton bStop;
	private SimpleDropDownButton bDebug;
	private JMenuItem m1;
	private JMenuItem m2;
	private boolean pause = false;
	private Simulator simulator;
	private GestionSimulation simulation;
	private PanelSimulator panelSimulator;

	/**
	 * Constructeur par défaut de <code>PanelController</code>.
	 *
	 * @param simulator  le simulateur
	 * @param panelSimulator le panel du simulateur
	 */
	public PanelController(Simulator simulator, PanelSimulator panelSimulator) {
		this.simulator = simulator;
		this.simulation = new GestionSimulation(simulator);
		this.panelSimulator = panelSimulator;

		setLayout(new FlowLayout(10, 1, 4));

		ImageIcon iconPlay = (ImageIcon) ImagesHelper.getIcon("control.png");
		ImageIcon iconPause = (ImageIcon) ImagesHelper.getIcon("control-pause.png");
		ImageIcon iconStop = (ImageIcon) ImagesHelper.getIcon("control-stop-square.png");
		ImageIcon iconDebug = (ImageIcon) ImagesHelper.getIcon("bug.png");

		buttonBar = new ButtonBar();

		bExec = createButton(LangueHelper.getInstance().get(VariableLangue.SIM_BUT_EXEC), iconPlay, true, false);
		bPause = createButton(LangueHelper.getInstance().get(VariableLangue.SIM_BUT_PAUSE), iconPause, false, false);
		bStop = createButton(LangueHelper.getInstance().get(VariableLangue.SIM_BUT_STOP), iconStop, false, false);
		bExec.setActionCommand("ExécutionSimulator");
		bPause.setActionCommand("PauseSimulator");
		bStop.setActionCommand("StopSimulator");

		bDebug = new SimpleDropDownButton(LangueHelper.getInstance().get(VariableLangue.SIM_BUT_DEB));
		bDebug.setIcon(iconDebug);

		JPopupMenu popupMenu = bDebug.getPopupMenu();

		m1 = new JMenuItem(LangueHelper.getInstance().get(VariableLangue.SIM_BUT_DEB_SEN));
		m2 = new JMenuItem(LangueHelper.getInstance().get(VariableLangue.SIM_BUT_DEB_COL));
		m1.setActionCommand("PointsCapteurs");
		m2.setActionCommand("PointsCollisions");
		popupMenu.add(m1);
		popupMenu.add(m2);

		buttonBar.add(bExec);
		buttonBar.add(bPause);
		buttonBar.add(bStop);
		buttonBar.add(bDebug);
		this.add(buttonBar);

		Listener listener = new Listener();

		bExec.addActionListener(listener);
		bPause.addActionListener(listener);
		bStop.addActionListener(listener);
		m1.addActionListener(listener);
		m2.addActionListener(listener);
	}

	/**
	 * Créer un bouton pour la buttonBar.
	 *
	 * @param text le texte du bouton
	 * @param icon l'icône du bouton
	 * @param enabled <code>true</code> pour que le bouton soit activé.
	 * @param toggleButton <code>true</code> pour créer un bouton de type toggle.
	 * @return le bouton créé.
	 */
	private AbstractButton createButton(String text, ImageIcon icon, boolean enabled, boolean toggleButton) {
		AbstractButton b = null;
		if (toggleButton) {
			b = new JToggleButton(text) {
				@Override
				public Insets getMargin() {
					if (super.getMargin() != null) {
						return new Insets(super.getMargin().top, 10, super.getMargin().bottom, 10);
					}
					return null;
				}
			};
		} else {
			b = new JButton(text) {
				@Override
				public Insets getMargin() {
					if (super.getMargin() != null) {
						return new Insets(super.getMargin().top, 10, super.getMargin().bottom, 10);
					}
					return null;
				}
			};
		}
		b.setEnabled(enabled);
		Insets margin = b.getMargin();
		b.setMargin(new Insets(margin.top, 10, margin.bottom, 10));
		b.setIcon(icon);
		return b;
	}

	/**
	 * Classe pour la gestion des appuis sur les boutons.
	 */
	private class Listener implements ActionListener,ObserverPanelController {

		@Override
		public void actionPerformed(ActionEvent e) {
			DicoTraces.getInstance().ajouterTrace(FabriqueTrace.creerTraceBoutonsSimulateur(((JButton)e.getSource()).getText()));

			if ("ExécutionSimulator".equals(e.getActionCommand())) {
				this.start();
				this.startThread();
			}
			else if ("PauseSimulator".equals(e.getActionCommand())) {
				this.pause();
			}
			else if ("StopSimulator".equals(e.getActionCommand())) {
				this.stop();
				simulation.stopThread();
			}
			else if ("PointsCapteurs".equals(e.getActionCommand())) {
				panelSimulator.getRobotRenderer().setCapteurs(!panelSimulator.getRobotRenderer().getCapteurs());
			}
			else if ("PointsCollisions".equals(e.getActionCommand())) {
				panelSimulator.getRobotRenderer().setCollisions(!panelSimulator.getRobotRenderer().getCollisions());
			}
		}

		/**
		 * Gere le debut de la simulation
		 */
		private void startThread(){
			simulator.addObserver(this); // Pour mettre les boutons en état stop
			simulation.startThread(); // Lance la simulation
		}

		/**
		 * Gere l'etat des boutons lors du debut de la simulation
		 */
		private void start(){
			bPause.setEnabled(true);
			bStop.setEnabled(true);
			bExec.setEnabled(false);
		}

		/**
		 * Gere l'etat des boutons lors du debut de la simulation
		 * Gere l'état de la simulation
		 */
		private void pause(){
			if (!pause) {
				pause = true;
				bPause.setText("Relance");
				simulation.waitThread(); // Mise en attente de la simulation
			} else {
				pause = false;
				bPause.setText("Pause");
				simulation.notifyThread(); // Re-démarrage de la simulation
			}
		}

		/**
		 * Gere l'etat des boutons lors du debut de la simulation
		 */
		private void stop(){
			bPause.setEnabled(false);
			bStop.setEnabled(false);
			bExec.setEnabled(true);
			pause = false;
			bPause.setText("Pause");
		}

		@Override
		public void update(ObservableSimulator o) {
			this.stop();
		}
	}
}
