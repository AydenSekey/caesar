package Vue.Interface;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class Fenetre extends JFrame {
    private Menu menu;
    private JSplitPane split;
    private JPanel glassPane;
    private static Fenetre instance = new Fenetre();
    private Dimension ecran;
    
    public Fenetre() {
	this.setTitle("JScratch");
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	
	this.addComponentListener(new ComponentAdapter() {

	    @Override
	    public void componentResized(ComponentEvent e) {
		split.setDividerLocation(0.75);
	    }
	});
	
	ecran = Toolkit.getDefaultToolkit().getScreenSize();
	this.setMinimumSize(new Dimension(((int)ecran.getWidth()*2/3), ((int)ecran.getHeight()*2/3)));
	
	this.menu = new Menu();
	this.setJMenuBar(this.menu);
	
	
	this.split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rootPaneCheckingEnabled, ZoneUtilisateur.getInstance(), PanelCodeConsole.getInstance());
	this.setContentPane(this.split);
	
	
	//Gestion du GlassPane
	this.glassPane = new JPanel();
        this.setGlassPane(this.glassPane);
        this.glassPane.setLayout(null);
        this.glassPane.setOpaque(false);
        this.glassPane.setVisible(true);
	
	this.setVisible(true);
	this.split.setDividerLocation(0.75);
    }
    
    public JPanel getGlassPane() {
	return this.glassPane;
    }
    
    public Menu getMenu() {
	return this.menu;
    }
    
    public static Fenetre getInstance() {
	return instance;
    }
}