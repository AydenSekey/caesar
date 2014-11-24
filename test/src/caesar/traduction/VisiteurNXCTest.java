package caesar.traduction;

import static org.junit.Assert.*;
import jscratch.traduction.VisiteurNXC;
import nxtim.instruction.Capteur;
import nxtim.instruction.CapteurSlot;
import nxtim.instruction.InstructionConfigCapteurs;
import nxtim.instruction.InstructionDeclaration;
import nxtim.instruction.InstructionTache;
import nxtim.instruction.TypeElement;
import nxtim.instruction.VariableConstante;
import nxtim.instruction.VariableModifiable;

import org.junit.Test;

public class VisiteurNXCTest {

	@Test
	public void testTraduireTache() {
		InstructionTache tache = new InstructionTache("Tache");
		VisiteurNXC visiteur = VisiteurNXC.getInstance();
		visiteur.reset();
		tache.accepte(visiteur);
		assertEquals("Traduction d'un tâche vide incorrecte.", "task Tache(){\n}\n", visiteur.getTraduction());

		visiteur.reset();
		tache.insererDebut(new InstructionDeclaration(new VariableModifiable(TypeElement.INT, "i")));
		tache.accepte(visiteur);
		assertEquals("Traduction d'une tâche incorrecte.", "task Tache(){\n    int i;\n}\n", visiteur.getTraduction());
	}

	@Test
	public void testTraduireInstructionConfigCapteur() {
		VisiteurNXC visiteur = VisiteurNXC.getInstance();
		visiteur.reset();
		InstructionConfigCapteurs configCapteurs = new InstructionConfigCapteurs();
		configCapteurs.setCapteurAuSlot(CapteurSlot.A, Capteur.COLOR);
		configCapteurs.setCapteurAuSlot(CapteurSlot.B, Capteur.LIGHT);
		configCapteurs.setCapteurAuSlot(CapteurSlot.C, Capteur.TOUCH);
		configCapteurs.setCapteurAuSlot(CapteurSlot.D, Capteur.ULTRASONIC);
		configCapteurs.accepte(visiteur);
		assertEquals("Traduction d'un instruction de configuration de capteur incorrecte.", "DefineSensors(COLOR, LIGHT, TOUCH, SONIC);\n", visiteur.getTraduction());
		
		configCapteurs.setCapteurAuSlot(CapteurSlot.A, Capteur.NONE);
		visiteur.reset();
		configCapteurs.accepte(visiteur);
		assertEquals("Traduction du slot de capteur A sans capteur associé incorrecte.", "DefineSensors(NONE, LIGHT, TOUCH, SONIC);\n", visiteur.getTraduction());
	}
}
