/*
Copyright (C) Université du Maine (2013) 

contributeurs : Adrien Duroy, Bastien Andru, Quentin Gosselin, Guillaume Delorme,
 Nicolas Detan, Zubair Parwany, Houda Chouket, Bastien Aubry,
 Vincent Besnard, Ivan Melnychenko

ad.duroy@gmail.com

Ce fichier est une partie de NXTIM.

NXTIM est une bibliothèque logiciel fournissant un modèle objet du code d'un programme
pour un robot NXT. 

NXTIM est régi par la licence CeCILL-C soumise au droit français et
respectant les principes de diffusion des logiciels libres. Vous pouvez
utiliser, modifier et/ou redistribuer ce programme sous les conditions
de la licence CeCILL-C telle que diffusée par le CEA, le CNRS et l'INRIA 
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
pris connaissance de la licence CeCILL-C, et que vous en avez accepté les
termes.
 */
package nxtim;

import static org.junit.Assert.*;
import nxtim.instruction.Affectation;
import nxtim.instruction.InstructionTache;
import nxtim.type.TypeElement;
import nxtim.instruction.VariableModifiable;

import org.junit.Test;

public class TestValidateur {

	@Test
	public void testValiderTacheCorrecte() {
		InstructionTache tache = new InstructionTache();
		boolean valide = Validateur.valider(tache);
		assertTrue("La tâche est censée être valide.", valide);
	}

	@Test
	public void testTacheInvalide() {
		Validateur validateur = new Validateur();
		InstructionTache tache = new InstructionTache("");
		tache.accepte(validateur);
		assertEquals("Nombre d'erreurs incorrect.", 1, validateur.nbErreurs());
		assertEquals("Erreurs incorrecte.", "Une tâche n'est pas nommée.", validateur.getIteratorErreurs().next());
	}
	
	@Test
	public void testReset() {
		Validateur validateur = new Validateur();
		InstructionTache tache = new InstructionTache("");
		tache.accepte(validateur);
		assertEquals("Nombre d'erreurs incorrect avant reset.", 1, validateur.nbErreurs());

		validateur.reset();
		assertEquals("Nombre d'erreurs incorrect avant reset.", 0, validateur.nbErreurs());
	}
	
	@Test
	public void testValide() {
		Validateur validateur = new Validateur();
		assertTrue("Le validateur n'a encore rien validé, il ne devrait pas y avoir d'erreurs.", validateur.valide());
		InstructionTache tache = new InstructionTache("");
		tache.accepte(validateur);
		assertFalse("Le validateur aurait dû détecter une erreur.", validateur.valide());
	}
	
	@Test
	public void testValiderAffectationCorrecte() {
		VariableModifiable var = new VariableModifiable(TypeElement.BOOL, "var");
		Affectation affectation = new Affectation(var, var, true);
		assertTrue("L'affectation devrait être valide.", Validateur.valider(affectation));
	}
}
