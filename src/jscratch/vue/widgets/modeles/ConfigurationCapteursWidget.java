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
package jscratch.vue.widgets.modeles;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import jscratch.vue.itemlist.ItemCapteur;
import jscratch.vue.widgets.modeles.zones.ListeDeroulante;
import nxtim.instruction.Capteur;
import nxtim.instruction.CapteurSlot;
import nxtim.instruction.InstructionConfigCapteurs;

/**
 * @since 1.0
 * @version 1.0
 */
public class ConfigurationCapteursWidget extends ModeleWidget {

	private ListeDeroulante<ItemCapteur> l1, l2, l3, l4;
	
	/**
	 * Constructeur par défaut de <code>CapteursConfigurationWidget</code>.
	 */
	public ConfigurationCapteursWidget() {
		super();
		int tX[] = {0, 5, 30, 35, 45, 50, 380, 385, 385, 380, 50, 45, 35, 30, 5, 0};
		int tY[] = {5, 0, 0, 5, 5, 0, 0, 5, 20, 25, 25, 30, 30, 25, 25, 20};

		this.setTabX(tX);
		this.setTabY(tY);
		this.setTailleX();
		this.setTailleY();
		this.setType(TypeModeleWidget.CAPTEURCONFIG);
		message.put(new Point(5, 17), "Configuration");

		InstructionConfigCapteurs ins = new InstructionConfigCapteurs();
		ins.setCapteurAuSlot(CapteurSlot.A, Capteur.NONE);
		ins.setCapteurAuSlot(CapteurSlot.B, Capteur.NONE);
		ins.setCapteurAuSlot(CapteurSlot.C, Capteur.NONE);
		ins.setCapteurAuSlot(CapteurSlot.D, Capteur.NONE);
		
		this.setElementProgramme(ins);
		this.setForme(new Polygon(this.getTabX(), this.getTabY(), this.getTabX().length));

		int widthChamp = 70;
		final ItemCapteur[] itemsCapteur = ItemCapteur.itemsCapteur();
		l1 = new ListeDeroulante<ItemCapteur>(itemsCapteur, this);
		l1.setBounds(85, 3, widthChamp, 20);
		this.getLesZonesSaisies().add(l1);
		
		l2 = new ListeDeroulante<ItemCapteur>(itemsCapteur, this);
		l2.setBounds(160, 3, widthChamp, 20);
		this.getLesZonesSaisies().add(l2);
		
		l3 = new ListeDeroulante<ItemCapteur>(itemsCapteur, this);
		l3.setBounds(235, 3, widthChamp, 20);
		this.getLesZonesSaisies().add(l3);
		
		l4 = new ListeDeroulante<ItemCapteur>(itemsCapteur, this);
		l4.setBounds(310, 3, widthChamp, 20);
		this.getLesZonesSaisies().add(l4);
		
		initListeners();
	}

	@Override
	public void initListeners() {
		l1.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					InstructionConfigCapteurs ins = (InstructionConfigCapteurs)getElementProgramme();
					ItemCapteur item = (ItemCapteur)l1.getSelectedItem();
					ins.setCapteurAuSlot(CapteurSlot.A, item.getCapteur());
				}
			});
		l2.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					InstructionConfigCapteurs ins = (InstructionConfigCapteurs)getElementProgramme();
					ItemCapteur item = (ItemCapteur)l2.getSelectedItem();
					ins.setCapteurAuSlot(CapteurSlot.B, item.getCapteur());
				}
			});
		
		l3.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					InstructionConfigCapteurs ins = (InstructionConfigCapteurs)getElementProgramme();
					ItemCapteur item = (ItemCapteur)l3.getSelectedItem();
					ins.setCapteurAuSlot(CapteurSlot.C, item.getCapteur());
				}
			});
		
		l4.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					InstructionConfigCapteurs ins = (InstructionConfigCapteurs)getElementProgramme();
					ItemCapteur item = (ItemCapteur)l4.getSelectedItem();
					ins.setCapteurAuSlot(CapteurSlot.D, item.getCapteur());
				}
			});
	}
}