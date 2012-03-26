package Vue.Widget.modele;

import Modeles.TypeWidget;
import java.awt.Polygon;
import java.io.Serializable;

public class InstructionWidget extends ModeleWidget implements Serializable{

	public InstructionWidget() {
		super();
				
		int[] tX = {0, 5, 30, 35, 45, 50, 130, 135, 135, 130, 50, 45, 35, 30, 5, 0};
		int[] tY = {5, 0, 0, 5, 5, 0, 0, 5, 20, 25, 25, 30, 30, 25, 25, 20};
		
		this.setTabX(tX);
		this.setTabY(tY);
		this.setType(TypeWidget.INSTRUCTION);
		this.setMessage("Instruction");
		this.setForme(new Polygon(this.getTabX(), this.getTabY(), this.getTabX().length));
	}

	  public void decalageXout(int a) {
	        int i;
	        for (i = 6; i < 10; i++) {
	            this.getForme().xpoints[i] = this.getForme().xpoints[i] + a;
	        }
	        this.setForme(this.getForme());
	        this.setTailleX();
	    }

	    public void decalageXin(int a) {
	        int i;
	        for (i = 6; i < 10; i++) {
	            this.getForme().xpoints[i] = this.getForme().xpoints[i] - a;
	        }
	        this.setForme(this.getForme());
	        this.setTailleX();
	    }

	    public void decalageYout(int b) {
	        int i;
	        for (i = 8; i < 16; i++) {
	            this.getForme().ypoints[i] = this.getForme().ypoints[i] + b;
	        }
	        this.setForme(this.getForme());
	        this.setTailleY();
	    }

	    public void decalageYin(int b) {
	        int i;
	        for (i = 8; i < 16; i++) {
	            this.getForme().ypoints[i] = this.getForme().ypoints[i] - b;
	        }
	        this.setForme(this.getForme());
	        this.setTailleY();
	    }
}
