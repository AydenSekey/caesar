package Vue.Interface;

import Vue.Tools.Variables;
import Vue.Widget.Widget;
import Vue.Widget.FabriqueInstructions;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class PanelWidget extends JPanel {

	private static PanelWidget instance = new PanelWidget();
	private FabriqueInstructions fabrique;
	private List<Widget> lesWidgets;

	private PanelWidget() {
		this.lesWidgets = new LinkedList<Widget>();
		this.setBorder(BorderFactory.createTitledBorder("Les ***"));

		this.fabrique = new FabriqueInstructions();
		this.setLayout(null);

		this.setMinimumSize(new Dimension(Variables.X_MAX_INSTRUCTION, 600));
	}

	public static PanelWidget getInstance() {
		return instance;
	}

	public FabriqueInstructions getFabrique() {
		return this.fabrique;
	}
	
	public void setLesWidgets(List<Widget> l) {
		this.removeAll();
		this.lesWidgets = l;
		placerWidgets();
		this.repaint();
	}

	private void placerWidgets() {
		int i = 1;
		int y = 30;
		
		for (Widget w : this.lesWidgets) {
			w.setDraggable(false);

			int x = Variables.X_MAX_INSTRUCTION / 2 - w.getWidth() / 2;
			
			w.setLocation(x, y);
			y += w.getHeight() + 20;

			this.add(w);
			i++;
		}
	}
}
