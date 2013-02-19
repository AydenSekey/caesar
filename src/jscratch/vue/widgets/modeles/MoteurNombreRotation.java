package jscratch.vue.widgets.modeles;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import jscratch.vue.widgets.modeles.zones.ListeDeroulante;
import nxtim.instruction.Moteur;

/**
 * @since 1.0
 * @version 1.0
 */
public class MoteurNombreRotation extends ModeleWidget {
	private ListeDeroulante<Moteur> l;
	
	private int largeur;
	
	private final int LARG_EXTREMITE;
	
	/**
	 * Constructeur par défaut <code>MoteurNombreRotation</code>.
	 */
	public MoteurNombreRotation() {
		// TODO : faire element programme
		this.setElementProgramme(null);
		this.attachableBas = false;
		this.attachableHaut = false;
		this.imbricable = false;
		this.attachableInterne = true;
		this.setType(TypeModeleWidget.MOTEURNOMBREROTATION);
		
		String messageString = "Nombre de rotation";
		
		Font font = new Font("TimesRoman ", Font.PLAIN, 12);
		FontMetrics metrics = new FontMetrics(font) {
		};
		
		Rectangle2D bounds = metrics.getStringBounds(messageString, null);
		
		this.LARG_EXTREMITE = 5;
		
		message.put(new Point(3, 15), messageString);

		l = new ListeDeroulante<Moteur>(Moteur.values(), this);
		l.setBounds((int)(bounds.getX() + bounds.getWidth()) + LARG_EXTREMITE * 2, 3, 35, 15);
		this.getLesZonesSaisies().add(l);
		
		this.largeur = (int) bounds.getWidth() + LARG_EXTREMITE + l.getWidth();

		int tabX[] = {0, 5, this.LARG_EXTREMITE + largeur, largeur + 10, largeur + 10, this.LARG_EXTREMITE + largeur, 5, 0};
		int tabY[] = {5, 0, 0, 5, 15, 20, 20, 15};

		this.setTabX(tabX);
		this.setTabY(tabY);

		this.setForme(new Polygon(this.getTabX(), this.getTabY(), this.getTabX().length));

		this.setTailleX();
		this.setTailleY();
	}
}