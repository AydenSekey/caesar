package Vue.Widget.modele;

import Modeles.TypeWidget;
import Vue.Widget.modele.zones.ChampTexte;
import Vue.Widget.modele.zones.Zone;
import instruction.IElementProgramme;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;

public abstract class ModeleWidget implements Serializable {

    public static final int OFFSET = 5;
    protected int[] tabX, tabY;
    private Color couleur;
    private Polygon forme;
    private String message;
    private TypeWidget type;
    private int tailleX, tailleY;
    private boolean conditionHaute;
    protected boolean attachableHaut, attachableBas;
    protected List<Rectangle> zonesAccroches;
	private IElementProgramme elementProgramme;
	private List<Zone> lesZonesSaisies;

	public ModeleWidget() {
        this.conditionHaute = true;
        this.attachableBas = true;
        this.attachableHaut = true;
        this.zonesAccroches = new LinkedList<Rectangle>();
		this.lesZonesSaisies = new LinkedList<Zone>();
    }

	public IElementProgramme getElementProgramme() {
		return elementProgramme;
	}

	public void setElementProgramme(IElementProgramme element) {
		this.elementProgramme = element;
	}

    public List<Zone> getLesZonesSaisies() {
		return lesZonesSaisies;
	}

	public void setLesZonesSaisies(List<Zone> lesZonesSaisies) {
		this.lesZonesSaisies = lesZonesSaisies;
	}

    public boolean isConditionHaute() {
        return conditionHaute;
    }

    public void setConditionHaute(boolean conditionHaute) {
        this.conditionHaute = conditionHaute;
    }

    public List<Rectangle> getZonesAccroches() {
        return zonesAccroches;
    }

    public int[] getTabX() {
        return tabX;
    }

    public void setTabX(int[] tabX) {
        this.tabX = tabX;
    }

    public int[] getTabY() {
        return tabY;
    }

    public void setTabY(int[] tabY) {
        this.tabY = tabY;
    }

    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    public int getTailleX() {
        return tailleX;
    }

    public void setTailleX() {
        this.tailleX = this.getXMax() - this.getXMin();
    }

    public int getTailleY() {
        return tailleY;
    }

    public void setTailleY() {
        this.tailleY = this.getYMax() - this.getYMin();
    }

    public int getXMin() {
        int max = this.tabX[0];
        for (Integer i : tabX) {
            max = Math.min(max, i);
        }
        return max;
    }

    public int getXMax() {
        int max = this.tabX[0];
        for (Integer i : tabX) {
            max = Math.max(max, i);
        }
        return max;
    }

    public int getYMin() {
        int max = this.tabY[0];
        for (Integer i : tabY) {
            max = Math.min(max, i);
        }
        return max;
    }

    public int getYMax() {
        int max = this.tabY[0];
        for (Integer i : tabY) {
            max = Math.max(max, i);
        }
        return max;
    }

    public Polygon getForme() {
        return forme;
    }

    public void setForme(Polygon forme) {
        this.forme = forme;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TypeWidget getType() {
        return type;
    }

    public void setType(TypeWidget type) {
        this.type = type;
    }

    public boolean isAttachableBas() {
        return attachableBas;
    }

    public boolean isAttachableHaut() {
        return attachableHaut;
    }

    public abstract void decalageXout(int x);

    public abstract void decalageXin(int x);

    public abstract void decalageYout(int x);

    public abstract void decalageYin(int x);
}