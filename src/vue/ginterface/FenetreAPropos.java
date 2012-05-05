package vue.ginterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import modeles.Erreur;
/**
 * Fenêtre pour afficher les infirmations de l'application.
 * 
 * @since 1.0
 * @version 1.0
 */
public class FenetreAPropos extends JFrame implements MouseListener {

	private JScrollPane textPane = null;
	private JTextPane text = null;
	private int tW = 300, tH = 350;
	private Timer t;

	public FenetreAPropos() {
		this.setTitle("A propos");
		this.setSize(400, 370);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.setLocationRelativeTo(null);

		this.text = new JTextPane();
		this.text.getCaret().setVisible(false);
		this.text.setSize(tW, tH);
		this.text.setContentType("text/html");
		this.text.setEditable(false);

		try {
			this.text.setPage(this.getClass().getClassLoader().getResource("APropos.html"));
		} catch (Exception e) {
			Erreur.afficher(e);
		}

		this.textPane = new JScrollPane(this.text);
		this.textPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		this.textPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.textPane.setBounds(55, 0, tW, tH);

		final JViewport vp = textPane.getViewport();
		vp.setViewPosition(new Point(0, 0));

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				t = new Timer(40, new ActionListenerTextTimer(vp, tH, 1800));//24 images par secondes
				t.start();
			}
		});

		this.text.addMouseListener(this);
		this.textPane.addMouseListener(this);

		this.setLayout(new BorderLayout());
		this.add(this.textPane, BorderLayout.CENTER);
		this.textPane.setBorder(null);

		this.setVisible(true);
	}
	/**
	 * Permet de gérer le défilement vertical du texte.
	 * 
	 * @since 1.0
	 */
	private class ActionListenerTextTimer implements ActionListener {

		private int i, posMax;
		JViewport vp;

		ActionListenerTextTimer(JViewport vp, int height, int posMax) {
			super();
			this.vp = vp;
			i = 1;
			this.posMax = posMax;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if (i <= posMax) {
				vp.setViewPosition(new Point(0, i));
				i++;
			} else {
				i = 1;
			}
		}
	}
	@Override
	public void dispose() {
		super.dispose();
		t.stop();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		this.dispose();
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
}