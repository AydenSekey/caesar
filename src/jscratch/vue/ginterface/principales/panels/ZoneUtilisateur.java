package jscratch.vue.ginterface.principales.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import jscratch.helpers.PropertiesHelper;

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
public class ZoneUtilisateur extends JPanel {

	/**
	 * Instance unique de <code>ZoneUtilisateur</code>.
	 */
	private static ZoneUtilisateur instance = new ZoneUtilisateur();
	private JScrollPane scrollCodeGraphique;

	/**
	 * @since 1.0
	 */
	private ZoneUtilisateur() {
		this.setLayout(new BorderLayout());

		PanelCodeGraphique p = PanelCodeGraphique.getInstance();
		
		scrollCodeGraphique = new JScrollPane(p);
		scrollCodeGraphique.setBorder(null);
		p.setScrollPane(scrollCodeGraphique);
		
		if (Boolean.valueOf(PropertiesHelper.getInstance().get("user.interface.afficher.categories"))) {
			this.add(PanelInstruction.getInstance(), BorderLayout.WEST);
		}
		this.add(scrollCodeGraphique, BorderLayout.CENTER);
	}

	/**
	 * @since 1.0
	 *
	 * @return L'unique instance de ZoneUtilisateur.
	 */
	public static ZoneUtilisateur getInstance() {
		return instance;
	}
}