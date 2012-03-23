package Vue.Categories;

import Vue.Interface.PanelWidget;
import java.awt.Color;

public class BoutonCategorieInstruction extends BoutonCategorie {

	public BoutonCategorieInstruction() {
		super(new ModeleCategorie("Instructions", Color.BLUE));
		
		this.ajouterUnWidget(PanelWidget.getInstance().getFabrique().creerWidgetIf());
		this.ajouterUnWidget(PanelWidget.getInstance().getFabrique().creerWidgetWhile());
	}
}