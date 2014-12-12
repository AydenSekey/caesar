/*
Copyright (C) Université du Maine (2013) 

contributeurs : Adrien Duroy, Bastien Andru, Quentin Gosselin, Guillaume Delorme,
 Nicolas Detan, Zubair Parwany, Houda Chouket, Bastien Aubry,
 Vincent Besnard, Ivan Melnychenko, Gwendal Martin

ad.duroy@gmail.com

Ce fichier est une partie du logiciel CAESAR.

CAESAR est un programme informatique servant à construire un programme
pour un robot NXT et à effectuer une simulation de l'exécution de ce dernier. 

CAESAR est régi par la licence CeCILL soumise au droit français et
respectant les principes de diffusion des logiciels libres. Vous pouvez
utiliser, modifier et/ou redistribuer ce programme sous les conditions
de la licence CeCILL telle que diffusée par le CEA, le CNRS et l'INRIA 
sur le site "http://www.cecill.info".

En contrepartie de l'accessibilité au code source et des droits de copie,
de modification et de redistribution accordés par cette licence, il n'est
offert aux utilisateurs qu'une garantie limitée.  Pour les mêmes raisons,
seule une responsabilité restreinte pèse sur l'auteur du programme,  le
titulaire des droits patrimoniaux et les concédants successifs.

A cet égard  l'attention de l'utilisateur est attirée sur les risques
associés au chargement,  à l'utilisation,  à la modification et/ou au
développement et à la reproduction du logiciel par l'utilisateur étant 
donné sa spécificité de logiciel libre, qui peut le rendre complexe à 
manipuler et qui le réserve donc à des développeurs et des professionnels
avertis possédant  des  connaissances  informatiques approfondies.  Les
utilisateurs sont donc invités à charger  et  tester  l'adéquation  du
logiciel à leurs besoins dans des conditions permettant d'assurer la
sécurité de leurs systèmes et ou de leurs données et, plus généralement, 
à l'utiliser et l'exploiter dans les mêmes conditions de sécurité. 

Le fait que vous puissiez accéder à cet en-tête signifie que vous avez 
pris connaissance de la licence CeCILL, et que vous en avez accepté les
termes.
 */
package jscratch.vue.ginterface.principales;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JTextField;
import jscratch.dictionnaires.DicoWidgetsCategories;
import nxtim.type.TypeElement;
import nxtim.instruction.Variable;
import nxtim.instruction.VariableModifiable;

public final class FenetreAjoutVariable extends JFrame {

	private JComboBox typeVariable;
	private JTextField nomVariable;
	private JButton boutonValider, boutonAnnuler;
	
	/**
	 * Le constructeur par défaut de <code>FenetreAjoutVariable</code>.
	 */
	public FenetreAjoutVariable() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(null);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		
		Box boxV = Box.createVerticalBox();
		Box boxH1 = Box.createHorizontalBox();
		Box boxH2 = Box.createHorizontalBox();

		this.typeVariable = new JComboBox(TypeElement.values());
		this.nomVariable = new JFormattedTextField(new RegexFormatter("[A-z]+"));
		this.boutonValider = new JButton("Valider");
		this.boutonAnnuler = new JButton("Annuler");

		this.setContentPane(boxV);
		boxV.add(Box.createVerticalStrut(10));
		boxV.add(boxH1);
		boxV.add(Box.createVerticalStrut(15));
		boxV.add(boxH2);
		boxV.add(Box.createVerticalStrut(10));

		boxH1.add(Box.createHorizontalStrut(10));
		boxH1.add(this.typeVariable);
		boxH1.add(Box.createHorizontalStrut(10));
		boxH1.add(this.nomVariable);
		boxH1.add(Box.createHorizontalStrut(10));

		boxH2.add(Box.createHorizontalStrut(10));
		boxH2.add(this.boutonValider);
		boxH2.add(Box.createHorizontalStrut(10));
		boxH2.add(this.boutonAnnuler);
		boxH2.add(Box.createHorizontalStrut(10));

		this.pack();
		this.setLocationRelativeTo(GUI.getPanelWidget());
		this.setVisible(true);
		this.nomVariable.requestFocus();
		
		boutonValider.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (!nomVariable.getText().isEmpty()) {
					Variable var = new VariableModifiable((TypeElement) typeVariable.getSelectedItem(), nomVariable.getText(), "0");
					
					// Ajout dans le dictionnaire
					ApplicationUI.getInstance().getDicoVariables().ajouter(var);
					
					// Mise à jour des widgets liés au variables
					DicoWidgetsCategories.getInstance().updateWidgetsVariables();
					
					// Mise à jour du panel widget
					GUI.getPanelWidget().setLesWidgets(1);
					
					dispose();
				}
			}
		});

		boutonAnnuler.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
		
		this.getRootPane().setDefaultButton(this.boutonValider);
		boutonValider.setDefaultCapable(true);
	}
}