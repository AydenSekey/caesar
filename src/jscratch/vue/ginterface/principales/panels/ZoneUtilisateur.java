package jscratch.vue.ginterface.principales.panels;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import jscratch.helpers.PropertiesHelper;
import jscratch.vue.ginterface.principales.GUI;

/**
 * Cette zone correspond à la zone où l'utilisateur peut intéragir.<br />
 * Permet de regrouper les 2 panels :
 * <ul>
 * <li>PanelInstruction</li>
 * <li>PanelCodeGraphique</li>
 * </ul>
 *
 * @since 1.0
 * @version 1.0
 */
public final class ZoneUtilisateur extends JPanel {

	/**
	 * Instance unique de <code>ZoneUtilisateur</code>.
	 */
	private static ZoneUtilisateur instance = null;
	private JScrollPane scrollCodeGraphique;

	/**
	 * @since 1.0
	 */
	private ZoneUtilisateur() {
		this.setLayout(new BorderLayout());
		PanelCodeGraphique p = GUI.getPanelCodeGraphique();
		
		scrollCodeGraphique = new JScrollPane(p);
		scrollCodeGraphique.setBorder(null);
		p.setScrollPane(scrollCodeGraphique);		
		this.reset();
		this.add(scrollCodeGraphique, BorderLayout.CENTER);
	}

	/**
	 * @since 1.0
	 *
	 * @return L'unique instance de ZoneUtilisateur.
	 */
	public static ZoneUtilisateur getInstance() {
		if (instance == null) {
			instance = new ZoneUtilisateur();
		}
		return instance;
	}
	
	/**
	 * Permet de modifier l'objet en fonction des properties
	 */
	public void reset() {
		if (Boolean.valueOf(PropertiesHelper.getInstance().get("user.interface.afficher.categories"))) {
			this.add(GUI.getPanelInstruction(), BorderLayout.WEST);
		}
		else {
			this.remove(GUI.getPanelInstruction());
		}
	}
}