package jscratch.vue.widgets.modeles;

import jscratch.vue.widgets.Widget;
import jscratch.vue.widgets.modeles.zones.ChampTexte;
import jscratch.vue.widgets.modeles.zones.Zone;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JComponent;
import java.awt.Rectangle;
import jscratch.parametrages.Variables;
import nxtim.instruction.Expression;
import nxtim.instruction.InstructionRepeat;
import nxtim.instruction.TypeVariable;
import nxtim.instruction.VariableConstante;

/**
 * Classe héritant de ModeleWidget et implémentant Seriliazable modélisant la forme d'un widget de type Repeat.
 * 
 * @since 1.0
 * @version 1.0
 */
public class RepeatWidget extends ModeleWidget {
	private ChampTexte f;
	public RepeatWidget() {
		int[] tX = {0, 5,/**/ 30, 35, 45, 50,/**/ 130, 135,/**/ 135, 130,/**/ 55, 50, 40, 35, /**/ 10, 5,/**/ 5, 10,/**/ 35, 40, 50, 55,/**/ 130, 135, /**/ 135, 130,/**/ 50, 45, 35, 30,/**/ 5, 0};
		int[] tY = {5, 0,/**/ 0, 5, 5, 0,/**/ 0, 5,/**/ 20, 25,/**/ 25, 30, 30, 25, /**/ 25, 30,/**/ 35, 40,/**/ 40, 45, 45, 40,/**/ 40, 45, /**/ 50, 55,/**/ 55, 60, 60, 55,/**/ 55, 50};

		this.setTabX(tX);
		this.setTabY(tY);
		this.setTailleX();
		this.setTailleY();
		this.setType(TypeModeleWidget.REPEAT);
		
		message.put(new Point(5, 17), "Répéter");
		message.put(new Point(85, 17), "fois");
		
		this.setElementProgramme(new InstructionRepeat());
		this.setForme(new Polygon(this.getTabX(), this.getTabY(), this.getTabX().length));
		this.zonesAccroches.add(Variables.ZONE_ACCROCHE_PAR_DEFAULT);


		int widthChamp = 20;
		f = new ChampTexte(widthChamp, this);
		f.ajouterTypeWidgetAccepte(TypeModeleWidget.VARIABLE);
		f.ajouterTypeWidgetAccepte(TypeModeleWidget.EXPRESSION_ARITHMETIQUE);
		f.setBounds(60, 3, widthChamp, 20);
		f.setValeur("0");
		this.getLesZonesSaisies().add(f);

		this.setInstructionValeur(f.getValeur());
		
		this.decalageX(-25);
		
		initListeners();
	}
	
	@Override
	public void applyChangeModele(){		
		Widget contentWidget = f.getContentWidget();
		InstructionRepeat repeatIns = (InstructionRepeat) getElementProgramme();
		
		// On met à jour la condition dans l'elementProgramme si elle existe
		if (contentWidget != null) {			
			Expression exp = (Expression) contentWidget.getElementProgramme();
			repeatIns.setExpression(exp);
		} else {
			repeatIns.setExpression(new VariableConstante(TypeVariable.INT, f.getValeur()));
		}
	}

	@Override
	public void decalageX(int a) {
        int i;
        for (i = 6; i < 10; i++) {
            this.getForme().xpoints[i] = this.getForme().xpoints[i] + a;
        }
        for (i = 22; i < 26; i++) {
            this.getForme().xpoints[i] = this.getForme().xpoints[i] + a;
        }
        this.setForme(this.getForme());
        this.setTailleX();
    }
	
	@Override
    public void decalageY(int b,Rectangle r) {
        int i;
        for (i = 16; i < tabY.length; i++) {
            this.getForme().ypoints[i] = this.getForme().ypoints[i] + b;
        }
        this.setForme(this.getForme());
        this.setTailleY();
    }
	
	@Override
	public void initListeners() {
		((JComponent) this.getLesZonesSaisies().get(0)).addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent arg0) {
				setInstructionValeur(((Zone) getLesZonesSaisies().get(0)).getValeur());
			}
		});
	}
	
	/**
	 * Méthode permettant de définir la valeur de l'instruction moteur
	 * 
	 * @param nom La valeur de l'instruction moteur
	 */
	private void setInstructionValeur(String nom) {
		((InstructionRepeat) getElementProgramme()).setExpression(new VariableConstante(TypeVariable.INT, "", nom));
	}
}