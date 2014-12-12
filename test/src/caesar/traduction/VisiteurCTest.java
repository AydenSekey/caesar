package caesar.traduction;

import static org.junit.Assert.*;
import jscratch.traduction.VisiteurC;
import nxtim.instruction.InstructionDeclaration;
import nxtim.type.TypeElement;
import nxtim.instruction.Variable;
import nxtim.instruction.VariableConstante;
import nxtim.instruction.VariableModifiable;

import org.junit.Test;

public class VisiteurCTest {

	@Test
	public void testTraduireVariableModifiable() {
		final VisiteurC visiteur = new VisiteurC();
		final String nom = "booleen";
		final Variable var = new VariableModifiable(TypeElement.BOOL, nom);
		var.accepte(visiteur);
		assertEquals("Traduction d'une variable modifiable incorrecte.", nom, visiteur.getTraduction());
	}

	@Test
	public void testTraduireInstructionDeclaration() {
		final VisiteurC visiteur = new VisiteurC();
		final InstructionDeclaration declaration = new InstructionDeclaration();
		
		declaration.accepte(visiteur);
		assertEquals("Traduction d'une déclaration sans variable incorrecte.", ";\n", visiteur.getTraduction());
		
		visiteur.reset();
		declaration.setMembre(new VariableModifiable(TypeElement.FLOAT, "f"));
		declaration.accepte(visiteur);
		assertEquals("Traduction d'une déclaration incorrecte.", "float f;\n", visiteur.getTraduction());
	}
	
	@Test
	public void testTraduireVariableConstante() {
		final VisiteurC visiteur = new VisiteurC();
		final String valeur = "2.1";
		final String nom = "var";
		Variable var = new VariableConstante(TypeElement.DOUBLE, valeur);
		var.accepte(visiteur);
		assertEquals("Traduction d'une valeur incorrecte.", valeur, visiteur.getTraduction());
		
		visiteur.reset();
		var = new VariableConstante(TypeElement.DOUBLE, nom, valeur);
		var.accepte(visiteur);
		assertEquals("Traduction d'une variable constante incorrecte.", nom, visiteur.getTraduction());
	}
}
