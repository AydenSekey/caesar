package nxtim;

import org.junit.Test;

import static org.junit.Assert.*;
import nxtim.exception.NXTIMBadOperateurException;
import nxtim.exception.NXTIMBadTypeElementException;
import nxtim.instruction.*;

public class TestNXTIM {
	
	@Test
	public void testNonLogiqueException() {
		boolean exception = false;
		try {
			new NonLogique(new VariableConstante(TypeElement.DOUBLE, "2"));
		} catch(NXTIMBadTypeElementException e) {
			exception = true;
			assertEquals("Information de l'exception incorrecte.", TypeElement.DOUBLE, e.getTypeElement());
		}
		assertTrue("Une NXTIMBadTypeElementException devrait être levée.", exception);
	}

	@Test
	public void testConditionExceptionConstruction() {
		boolean exception = false;
		try {
			Condition con = new Condition(Operateur.ADDITION, new VariableConstante(TypeElement.INT, "1"), new VariableConstante(TypeElement.INT, "0"));
		} catch (NXTIMBadOperateurException e) {
			assertEquals("Opérateur conditionnel incorrect.", Operateur.ADDITION, e.getOperateur());
			exception = true;
		}
		assertTrue("Une NXTIMBadOperateurException devrait être levée. (Constructeur avec 3 paramètres)", exception);
		
		exception = false;
		try {
			Condition con = new Condition(Operateur.MULTIPLICATION);
		} catch(NXTIMBadOperateurException e) {
			assertEquals("Opérateur conditionnel incorrect.", Operateur.MULTIPLICATION, e.getOperateur());
			exception = true;
		}
		assertTrue("Une NXTIMBadOperateurException devrait être levée. (Constructeur avec un paramètre)", exception);
	}
	
	@Test
	public void testConditionExceptionModification() {	
		final Variable membreDroit = new VariableConstante(TypeElement.BOOL, "0");
		Condition con = new Condition(Operateur.OU, new VariableConstante(TypeElement.BOOL, "1"), membreDroit) ;
		boolean exception = false;
		try {
			con.setMembreDroit(new VariableConstante(TypeElement.INT, "1"));
		} catch(NXTIMBadTypeElementException e) {
			exception = true;
			assertEquals("Information de l'exception incorrecte.", TypeElement.INT, e.getTypeElement());
		}
		assertTrue("Une NXTIMBadTypeElementException devrait être levée.", exception);
		assertTrue("L'ancien membre a été écrasé par à cause de l'élément incorrecte", membreDroit == con.getMembreDroit());
	}

	@Test
	public void testOperationExceptionConstruction() {
		boolean exception = false;
		try {
			Operation op = new Operation(Operateur.EGALITE, new VariableModifiable(TypeElement.INT, "var"), new VariableModifiable(TypeElement.INT, "var2"));
		} catch (NXTIMBadOperateurException e) {
			assertEquals("Opérateur incorrecte.", Operateur.EGALITE, e.getOperateur());
			exception = true;
		}
		assertTrue("Une NXTIMBadOperateurException devrait être levée.", exception);
	}
}
