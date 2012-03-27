package Vue.Widget.modele;

import Modeles.TypeWidget;
import Vue.Widget.modele.zones.ChampTexte;
import Vue.Widget.modele.zones.Zone;
import instruction.*;
import java.awt.Polygon;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JComponent;

public class WaitWidget extends ModeleWidget {

	
	
	public WaitWidget() {
		super();
		int tX[] = {0, 5, 30, 35, 45, 50, 130, 135, 135, 130, 50, 45, 35, 30, 5, 0};
		int tY[] = {5, 0, 0, 5, 5, 0, 0, 5, 20, 25, 25, 30, 30, 25, 25, 20};
		
		
		this.setTabX(tX);
		this.setTabY(tY);
		this.setTailleX();
		this.setTailleY();
		this.setType(TypeWidget.WAIT);
		this.setMessage("Wait");
		this.setElementProgramme(new InstructionAttente());
		this.setForme(new Polygon(this.getTabX(), this.getTabY(), this.getTabX().length));

		ChampTexte f = new ChampTexte();
		f.setBounds(110, 3, 20, 20);
		f.setText("0");
		this.getLesZonesSaisies().add(f);
		
		this.setInstructionValeur(f.getText());
		
		initListeners();
	}

	@Override
	public void decalageXout(int x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decalageXin(int x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decalageYout(int x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decalageYin(int x) {
		// TODO Auto-generated method stub
		
	}
	
	public void initListeners() {
		((JComponent) this.getLesZonesSaisies().get(0)).addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent arg0) {
				setInstructionValeur(((Zone) getLesZonesSaisies().get(0)).getValeur());
			}
		});
	}

	private void setInstructionValeur(String nom) {
		((InstructionAttente) getElementProgramme()).setExpression(new VariableConstante(TypeVariable.INT, "", nom));
	}
}
