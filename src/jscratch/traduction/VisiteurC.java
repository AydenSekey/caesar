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

import nxtim.instruction.Affectation;
import nxtim.instruction.Expression;
import nxtim.instruction.ExpressionComplexe;
import nxtim.instruction.Instruction;
import nxtim.instruction.InstructionAttente;
import nxtim.instruction.InstructionConfigCapteurs;
import nxtim.instruction.InstructionDeclaration;
import nxtim.instruction.InstructionDeclarationAffectation;
import nxtim.instruction.InstructionDeclarationCapteur;
import nxtim.instruction.InstructionDoWhile;
import nxtim.instruction.InstructionFor;
import nxtim.instruction.InstructionIf;
import nxtim.instruction.InstructionIfElse;
import nxtim.instruction.InstructionIncrementation;
import nxtim.instruction.InstructionMoteurMov;
import nxtim.instruction.InstructionMoteurOff;
import nxtim.instruction.InstructionMoteurRotate;
import nxtim.instruction.InstructionRAZRotationMoteur;
import nxtim.instruction.InstructionRepeat;
import nxtim.instruction.InstructionTache;
import nxtim.instruction.InstructionWhile;
import nxtim.instruction.NonLogique;
import nxtim.operateur.Operateur;
import nxtim.instruction.RotationMoteur;
import nxtim.instruction.TempsCourant;
import nxtim.instruction.ValeurCapteur;
import nxtim.instruction.Variable;
import nxtim.instruction.VariableCapteur;

public class VisiteurC extends VisiteurTraduction {

	@Override
	public void visiter(InstructionDeclaration instructionDeclaration) {
		traduction += indent();
		Variable var = instructionDeclaration.getMembre();
		if (var != null) {
			traduction += var.getType() + " ";
			var.accepte(this);
		}
		traduction += ";\n";
	}

	@Override
	public void visiter(InstructionDeclarationAffectation instructionDeclarationAffectation) {
		traduction += indent();
		Variable var = instructionDeclarationAffectation.getMembre();
		if (var != null) {
			traduction += var.getType() + " ";
			var.accepte(this);
		}
		traduction += " = ";
		Expression ex = instructionDeclarationAffectation.getMembreDroit();
		if (ex != null) {
			ex.accepte(this);
		}
		traduction += ";\n";
	}

	@Override
	public void visiter(InstructionIf instructionIf) {
		traduction += indent();
		traduction += "if(";
		Expression cond = instructionIf.getCondition();
		if (cond != null) {
			cond.accepte(this);
		}
		traduction += ") {\n";

		nivIndent++;
		for (Instruction is : instructionIf.getEnfants()) {
			is.accepte(this);
		}
		nivIndent--;

		traduction += indent() + "}\n";
	}

	@Override
	public void visiter(InstructionIfElse inst) {
		// If
		InstructionIf membre = inst.getMembreIf();
		if (membre != null) {
			membre.accepte(this);
		}
		// Else
		traduction += indent();
		traduction += "else {\n";

		nivIndent++;
		for (Instruction is : inst.getEnfantsElse()) {
			is.accepte(this);
		}
		nivIndent--;

		traduction += indent() + "}\n";
	}

	@Override
	public void visiter(InstructionWhile inst) {
		traduction += indent();

		traduction += "while(";
		Expression cond = inst.getCondition();
		if (cond != null) {
			cond.accepte(this);
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
	public void visiter(InstructionDoWhile inst) {
		traduction += indent();

		traduction += "do {\n";

		nivIndent++;
		for (Instruction ins : inst.getEnfants()) {
			ins.accepte(this);
		}
		nivIndent--;

		traduction += indent() + "} while(";
		Expression cond = inst.getCondition();
		if (cond != null) {
			cond.accepte(this);
		}
		traduction += ");\n";
	}

	@Override
	public void visiter(InstructionTache inst) {
		traduction += "/* Code C pour Tache inconnu ! */\n" + indent() + "{\n";

		nivIndent++;
		for (Instruction is : inst.getEnfants()) {
			is.accepte(this);
		}
		nivIndent--;
		traduction += indent() + "}\n";
	}

	@Override
	public void visiter(TempsCourant exp) {
		traduction += "clock()";
	}

	@Override
	public void visiter(InstructionAttente inst) {
		traduction += indent() + "/* Code C pour l'attente inconnu ! */\n";
	}

	@Override
	public void visiter(InstructionMoteurMov inst) {
		traduction += indent() + "/* Code C pour faire avancer un moteur inconnu ! */\n";
	}

	@Override
	public void visiter(InstructionMoteurOff inst) {
		traduction += indent() + "/* Code C pour faire reculer un moteur inconnu ! */\n";
	}

	@Override
	public void visiter(InstructionFor instructionFor) {
		traduction += indent();
		traduction += "for(";
		Expression ini = instructionFor.getInitialisation();
		if (ini != null) {
			ini.accepte(this);
		}
		traduction += "; ";
		Expression cond = instructionFor.getCondition();
		if (cond != null) {
			cond.accepte(this);
		}
		traduction += "; ";
		Expression iter = instructionFor.getIteration();
		if (iter != null) {
			iter.accepte(this);
		}
		traduction += ") {\n";

		nivIndent++;
		for (Instruction inst : instructionFor.getEnfants()) {
			inst.accepte(this);
		}
		nivIndent--;

		traduction += indent() + "}\n";
	}

	@Override
	public void visiter(InstructionRepeat instructionRepeat) {
		traduction += indent() + "int nb_rep;\n";
		traduction += indent() + "for(nb_rep = 0; nb_rep < ";
		Expression exp = instructionRepeat.getExpression();
		if (exp != null) {
			exp.accepte(this);
		}
		traduction += "; nb_rep++) {\n";

		nivIndent++;
		for (Instruction inst : instructionRepeat.getEnfants()) {
			inst.accepte(this);
		}
		nivIndent--;

		traduction += indent() + "}\n";
	}

	@Override
	public void visiter(Variable variable) {
		if (variable.isConstante() && variable.getNom().isEmpty()) {
			traduction += variable.getValeur();
		} else {
			traduction += variable.getNom();
		}
	}

	@Override
	public void visiter(Affectation affectation) {
		if (affectation.isInstruction()) {
			traduction += indent();
		}
		Expression membre = affectation.getMembreGauche();
		if (membre != null) {
			membre.accepte(this);
		}
		traduction += " = ";
		membre = affectation.getMembreDroit();
		if (membre != null) {
			membre.accepte(this);
		}
		if (affectation.isInstruction()) {
			super.traduction += ";\n";
		}
	}

	@Override
	public void visiter(ExpressionComplexe expr) {
		traduction += "(";
		Expression membre = expr.getMembreGauche();
		if (membre != null) {
			membre.accepte(this);
		}
		traduction += " " + expr.getOperateur() + " ";
		membre = expr.getMembreDroit();
		if (membre != null) {
			membre.accepte(this);
		}
		traduction += ")";
	}

	@Override
	public void visiter(InstructionDeclarationCapteur instructionDeclarationCapteur) {
		traduction += indent() + "/* Code C pour déclarer un capteur inconnu ! */\n";
	}

	@Override
	public void visiter(VariableCapteur variableCapteur) {
		traduction += indent() + "/* Code C pour une variable de capteur inconnu ! */\n";
	}

	@Override
	public void visiter(InstructionMoteurRotate instructionMoteurRotate) {
		traduction += indent() + "/* Code C pour rotation d'un moteur inconnu ! */\n";
	}

	@Override
	public void visiter(InstructionIncrementation instructionIncrementation) {
		String op = "++";
		if (instructionIncrementation.getOperateur() == Operateur.INCREMENTATION_MOINS) {
			op = "--";
		}
		
		if (instructionIncrementation.isAvant()) {
			traduction += op;
		}
		if (instructionIncrementation.getVariable() != null) {
			instructionIncrementation.getVariable().accepte(this);
		}
		if (!instructionIncrementation.isAvant()) {
			traduction += op;
		}
		if(instructionIncrementation.isInstruction()) {
			traduction += ";\n";
		}
	}

	@Override
	public void visiter(ValeurCapteur valCapteur) {
		traduction += "/* Code C pour obtenir la valeur d'un capteur inconnu ! */\n";
	}

	@Override
	public void visiter(RotationMoteur rotMoteur) {
		traduction += "/* Code C pour obtenir le nombre de rotations d'un moteur inconnu ! */\n";
	}

	@Override
	public void visiter(InstructionRAZRotationMoteur razMoteur) {
		traduction += "/* Code C pour RAZ du nombre de rotations d'un moteur inconnu ! */\n";
	}

	@Override
	public void visiter(InstructionConfigCapteurs confCapt) {
		traduction += indent() + "/* Code C pour la configuration des capteurs inconnu ! */\n";
	}

	@Override
	public void visiter(NonLogique nonLog) {
		traduction += "!";
		if(nonLog.getCondition() != null) {
			nonLog.getCondition().accepte(this);
		}
	}
}
