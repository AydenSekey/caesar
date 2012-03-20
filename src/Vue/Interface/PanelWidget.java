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
		this.setBorder(BorderFactory.createTitledBorder(""));

		this.fabrique = new FabriqueInstructions();
		this.setLayout(null);

		lesWidgets.add(fabrique.creerWidgetInstruction());
		lesWidgets.add(fabrique.creerWidgetIf());
		lesWidgets.add(fabrique.creerWidgetNegation());

		placerWidgets();

		this.setMinimumSize(new Dimension(Variables.xMax, 600));
	}

	public static PanelWidget getInstance() {
		return instance;
	}

	public FabriqueInstructions getFabrique() {
		return this.fabrique;
	}

	private void placerWidgets() {
		int i = 1;
		for (Widget w : this.lesWidgets) {
			w.setDraggable(false);

			int x = Variables.xMax / 2 - w.getWidth() / 2;
			int y = i * (w.getHeight() + 10);

			w.setLocation(x, y);
			this.add(w);
			i++;
		}
	}
}