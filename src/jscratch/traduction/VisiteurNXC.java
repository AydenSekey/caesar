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
package jscratch.traduction;

import nxtim.instruction.Capteur;
import nxtim.instruction.CapteurSlot;
import nxtim.instruction.Expression;
import nxtim.instruction.Instruction;
import nxtim.instruction.InstructionAttente;
import nxtim.instruction.InstructionConfigCapteurs;
import nxtim.instruction.InstructionDeclarationCapteur;
import nxtim.instruction.InstructionMoteurMov;
import nxtim.instruction.InstructionMoteurOff;
import nxtim.instruction.InstructionMoteurRotate;
import nxtim.instruction.InstructionRAZRotationMoteur;
import nxtim.instruction.InstructionRepeat;
import nxtim.instruction.InstructionTache;
import nxtim.instruction.Moteur;
import nxtim.instruction.RotationMoteur;
import nxtim.instruction.TempsCourant;
import nxtim.instruction.ValeurCapteur;
import nxtim.instruction.VariableCapteur;

/**
 * Traducteur d'instructions dans le langage NXC.
 * Cette classe implémente le design pattern Singleton.
 *
 * @since 1.0
 * @version 1.0
 */
public final class VisiteurNXC extends VisiteurC {

	private static VisiteurNXC instance;

	private VisiteurNXC() {
		super();
	}

	/**
	 * Donne l'instance unique de VisiteurNXC.
	 *
	 * @return l'instance de VisiteurNXC
	 */
	public static synchronized VisiteurNXC getInstance() {
		if (instance == null) {
			instance = new VisiteurNXC();
		}
		return instance;
	}

	private void ajouterNomMoteur(Moteur m) {
		if (m != null) {
			switch (m) {
				case A:
					traduction += "OUT_A";
					break;
				case B:
					traduction += "OUT_B";
					break;
				case C:
					traduction += "OUT_C";
					break;
				case AB:
					traduction += "OUT_AB";
					break;
				case AC:
					traduction += "OUT_AC";
					break;
				case BC:
					traduction += "OUT_BC";
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Ajoute le nom d'un slot de capteur dans la traduction.
	 * 
	 * @param c le slot. Si <code>null</code> rien n'est ajouté à la traduction.
	 */
	private void ajouterNomCapteurSlot(CapteurSlot c) {
		if(c != null) {
			switch (c) {
				case A:
					traduction += "IN_1";
					break;
				case B:
					traduction += "IN_2";
					break;
				case C:
					traduction += "IN_3";
					break;
				case D:
					traduction += "IN_4";
					break;
				default:
					break;
			}
		}
	}
	
	/**
	 * Ajoute le nom d'un capteur dans la traduction.
	 * 
	 * @param c le capteur. Si <code>null</code> rien n'est ajouté à la traduction.
	 */
	private void ajouterNomCapteur(Capteur c) {
		if(c != null) {
			switch(c) {
				case COLOR:
					traduction += "COLOR";
					break;
				case LIGHT:
					traduction += "LIGHT";
					break;
				case TOUCH:
					traduction += "TOUCH";
					break;
				case ULTRASONIC:
					traduction += "SONIC";
					break;
				case NONE:
					traduction += "NONE";
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void visiter(InstructionTache inst) {
		traduction += indent() + "task " + inst.getNom() + "(){\n";

		nivIndent++;
		for (Instruction is : inst.getEnfants()) {
			is.accepte(this);
		}
		nivIndent--;

		traduction += indent() + "}\n";
	}

	@Override
	public void visiter(InstructionAttente inst) {
		traduction += indent();
		traduction += "Wait(";
		Expression ex = inst.getExpression();
		if (ex != null) {
			ex.accepte(this);
		}
		traduction += ");\n";
	}

	@Override
	public void visiter(InstructionMoteurMov inst) {
		traduction += indent();

		if (inst.isReverse()) {
			traduction += "OnRev(";
		} else {
			traduction += "OnFwd(";
		}

		ajouterNomMoteur(inst.getMoteur());

		traduction += ", ";
		Expression ex = inst.getExpression();
		if (ex != null) {
			ex.accepte(this);
		}
		traduction += ");\n";
	}

	@Override
	public void visiter(InstructionMoteurOff inst) {
		traduction += indent();
		traduction += "Off(";

		ajouterNomMoteur(inst.getMoteur());

		traduction += ");\n";
	}

	@Override
	public void visiter(TempsCourant inst) {
		traduction += "CurrentTick()";
	}

	@Override
	public void visiter(InstructionRepeat inst) {
		traduction += indent();

		traduction += "repeat(";
		Expression ex = inst.getExpression();
		if (ex != null) {
			ex.accepte(this);
		}
		traduction += "){\n";

		nivIndent++;
		for (Instruction is : inst.getEnfants()) {
			is.accepte(this);
		}
		nivIndent--;

		traduction += indent() + "}\n";
	}

	@Override
	public void visiter(InstructionDeclarationCapteur instructionDeclarationCapteur) {
		traduction += indent();
		switch (instructionDeclarationCapteur.getCapteur()) {
			case TOUCH:
				traduction += "SetSensorTouch(";
				break;
			default:
				break;
		}
		ajouterNomCapteurSlot(instructionDeclarationCapteur.getCapteurSlot());
		traduction += ");\n";
	}

	@Override
	public void visiter(VariableCapteur variableCapteur) {
		traduction += "Sensor(";
		ajouterNomCapteurSlot(variableCapteur.getCapteurSlot());
		traduction += ")";
	}

	@Override
	public void visiter(InstructionMoteurRotate instructionMoteurRotate) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void visiter(ValeurCapteur valCapteur) {
		traduction += "GetSensorValue(";
		ajouterNomCapteurSlot(valCapteur.getSlot());
		traduction += ")";
	}
	
	@Override
	public void visiter(RotationMoteur rotMoteur) {
		traduction += "MotorRotationCount(";
		ajouterNomMoteur(rotMoteur.getMoteur());
		traduction += ")";
	}
	
	@Override
	public void visiter(InstructionRAZRotationMoteur razMoteur) {
		traduction += indent();
		traduction += "ResetMotorRotationCount(";
		ajouterNomMoteur(razMoteur.getMoteur());
		traduction += ");\n";
	}
	
	@Override
	public void visiter(InstructionConfigCapteurs confCapt) {
		traduction += indent();
		traduction += "DefineSensors(";
		Capteur capt = confCapt.getCapteurAuSlot(CapteurSlot.A);
		if(capt != null)
			ajouterNomCapteur(capt);
		else
			traduction += "NONE";
		traduction += ", ";
		capt = confCapt.getCapteurAuSlot(CapteurSlot.B);
		if(capt != null)
			ajouterNomCapteur(capt);
		else
			traduction += "NONE";
		traduction += ", ";
		capt = confCapt.getCapteurAuSlot(CapteurSlot.C);
		if(capt != null)
			ajouterNomCapteur(capt);
		else
			traduction += "NONE";
		traduction += ", ";
		capt = confCapt.getCapteurAuSlot(CapteurSlot.D);
		if(capt != null)
			ajouterNomCapteur(capt);
		else
			traduction += "NONE";
		traduction += ");\n";
	}
}
