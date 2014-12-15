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
package jscratch.interpreteur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import nxtim.instruction.Affectation;
import nxtim.instruction.Condition;
import nxtim.instruction.Expression;
import nxtim.instruction.IElementProgramme;
import nxtim.instruction.Instruction;
import nxtim.instruction.InstructionAttente;
import nxtim.instruction.InstructionDeclaration;
import nxtim.instruction.InstructionDoWhile;
import nxtim.instruction.InstructionIf;
import nxtim.instruction.InstructionIfElse;
import nxtim.instruction.InstructionMoteurMov;
import nxtim.instruction.InstructionMoteurOff;
import nxtim.instruction.InstructionTache;
import nxtim.instruction.InstructionWhile;
import nxtim.operateur.Operateur;
import nxtim.instruction.Operation;
import nxtim.type.TypeElement;
import nxtim.instruction.Variable;
import nxtim.instruction.VariableConstante;
import nxtim.instruction.VariableModifiable;
import nxtim.instruction.Moteur;
import jscratch.dictionnaires.DicoVariables;
import nxtim.instruction.InstructionDeclarationAffectation;

/**
 * Constructeur de modèle NXTIM d'un programme NXC.
 * Fonctionne en postfixe uniquement.
 * 
 * @since 1.0
 */
public class MonteurNXTIM implements MonteurProgramme{

	private Pile<IElementProgramme> p;
	private DicoVariables dico;

	/**
	 * Créé un monteur de modèle NXTIM d'un programme NXC.
	 */
	public MonteurNXTIM(){
		p = new Pile<IElementProgramme>();
		dico = new DicoVariables();
		/* Ajout des constantes Moteur */
		dico.ajouter(new VariableConstante(TypeElement.INT, "OUT_A", null));
		dico.ajouter(new VariableConstante(TypeElement.INT, "OUT_B", null));
		dico.ajouter(new VariableConstante(TypeElement.INT, "OUT_C", null));
	}
	
	@Override
	public void declaration(String type, String var){
		TypeElement typeVar = typeConvert(type);
		VariableModifiable vm = null;
		if(!isInteger(var,10)) {
			vm = new VariableModifiable(typeVar,var,null);
		}
		dico.ajouter(vm);
		InstructionDeclaration inst = new InstructionDeclaration();
		inst.setMembre(vm);
		p.empile(inst);
	}
	
	@Override
	public void declarationInitialisation(String type, String var) {
		/* Récupération de l'expression d'initialisation */
		Expression exp = (Expression) p.depile();
		/* Création du modèle NXTIM */
		TypeElement typeVar = typeConvert(type);
		VariableModifiable vm = new VariableModifiable(typeVar,var,null);
		dico.ajouter(vm);
		InstructionDeclarationAffectation inst = new InstructionDeclarationAffectation();
		inst.setMembre(vm);
		inst.setMembreDroit(exp);
		/* Stockage du résultat */
		p.empile(inst);
	}

	@Override
	public void affectation(){
		/* Récupération des membres de l'affectation */
		IElementProgramme ie = p.depile();
		IElementProgramme ie2 = p.depile();
		/* Si correct, stockage du résultat */
		if(ie instanceof Expression && ie2 instanceof VariableModifiable) {
			p.empile(new Affectation((VariableModifiable) ie2, (Expression) ie, true));
		}

	}

	@Override
	public void tache(String nom,int nbIns){
		ArrayList<IElementProgramme> lIE= new ArrayList<IElementProgramme>() ;
		/* Récupération des éléments du corps */
		for(int i = 1; i<= nbIns ; i++)
			lIE.add(p.depile());
		Collections.reverse(lIE);
		/* Création du modèle NXTIM correspondant */
		InstructionTache iTache = new InstructionTache(nom);
		Iterator<IElementProgramme> i = lIE.iterator();
		while(i.hasNext()) {
			iTache.insererFin((Instruction) i.next());
		}
		/* Stockage du résulat */
		p.empile(iTache);
	}

	@Override
	public void condition(String nom){
		/* Récupération des deux membres de la condition */
		ArrayList<IElementProgramme> lEL = new ArrayList<IElementProgramme>();
		for(int i = 1; i<= 2; i++) {
			lEL.add(p.depile());
		}
		Collections.reverse(lEL);
		/* Création du modèle NXTIM correspondant */
		Condition cond = new Condition(convOperateur(nom), (Expression)lEL.get(0), (Expression)lEL.get(1));
		/* Stockage du résultat */
		p.empile(cond);
	}

	@Override
	public void bwhile(int nbArg){
		ArrayList<IElementProgramme> lEL = new ArrayList<IElementProgramme>();
		/* Récupération des éléments du corps */
		for(int i = 1; i<= nbArg; i++) {
			lEL.add(p.depile());
		}
		/* Récupération de la condition */
		Condition cond = (Condition) p.depile();
		/* Construction du modèle NXTIM correspondant */
		Collections.reverse(lEL);
		InstructionWhile insWhile = new InstructionWhile(cond);
		for(IElementProgramme ie : lEL) {
			insWhile.insererFin((Instruction)ie);
		}
		/* Stocker le résultat */
		p.empile(insWhile);
	}
	
	@Override
	public void bdoWhile(int nbInst) {
		ArrayList<IElementProgramme> instrucs = new ArrayList<IElementProgramme>();
		/* Récupération des éléments du corps */
		for(int i = 0; i < nbInst; i++) {
			instrucs.add(p.depile());
		}
		/* Récupération de la condition */
		Condition cond = (Condition) p.depile();
		/* Construction du modèle NXTIM correspondant */
		Collections.reverse(instrucs);
		InstructionDoWhile inst = new InstructionDoWhile(cond);
		for(IElementProgramme ie : instrucs) {
			inst.insererFin((Instruction) ie);
		}
		/* Stocker le résultat */
		p.empile(inst);
	}

	@Override
	public void si(int nbArg){
		/* Récupération des éléments du corps */
		ArrayList<IElementProgramme> lEL = new ArrayList<IElementProgramme>();
		for(int i = 1; i<= nbArg; i++) {
			lEL.add(p.depile());
		}
		/* Récupération de la condition */
		Condition cond = (Condition) p.depile();
		/* Construction du modèle NXTIM correspondant */
		Collections.reverse(lEL);
		InstructionIf insIf = new InstructionIf(cond);
		for(IElementProgramme ie : lEL) {
			insIf.insererFin((Instruction)ie);
		}
		/* Stockage du résultat */
		p.empile(insIf);
	}

	@Override
	public void siSinon(int nbArg1, int nbArg2) {

		ArrayList<IElementProgramme> lELIf = new ArrayList<IElementProgramme>();
		ArrayList<IElementProgramme> lELElse = new ArrayList<IElementProgramme>();
		/* Récupération des membres du else */
		for(int i = 1; i<= nbArg2; i++) {
			lELIf.add(p.depile());
		}
		Collections.reverse(lELIf);
		/* Récupération des membres du if */
		for(int i = 1; i<= nbArg1; i++) {
			lELElse.add(p.depile());
		}
		Collections.reverse(lELElse);
		/* Récupération de la condition */
		Condition cond = (Condition) p.depile();
		/* Création du modèle NXTIM correspondant */
		InstructionIfElse insIfElse = new InstructionIfElse(cond);
		for(IElementProgramme ie : lELIf) {
			insIfElse.insererFinElse((Instruction)ie);
		}
		for(IElementProgramme ie : lELElse) {
			insIfElse.insererFinIf((Instruction)ie);
		}
		/* Stokage du résultat */
		p.empile(insIfElse);

	}

	@Override
	public void variable(String var){

		VariableModifiable vm = null;
		boolean trouve = false;
		/* Recherche de la variable dans le dictionnaire de variable */
		for(Variable variable : dico.getLesVariables()){
			if(variable.getNom().equals(var)){
				/* La variable est dans le dictionnaire de variables */
				p.empile(variable);
				trouve = true;
				break;
			}
		}
		
		if(!trouve) {
			/* La variable n'est pas dans le dictionnaire de variables */
			System.err.println("WARNING : Erreur sémantique : la variable '" + var + "' n'a pas été déclarée ! ");
			/* En créé une sans la mettre dans le dico */
			vm = new VariableModifiable(null, var, null);
				p.empile(vm);
		}
	}

	@Override
	public void constante(String valeur, String type){
		TypeElement typeVar = typeConvert(type);
		/* Création du modèle NXTIM correspondant et stockage du résultat */
		p.empile( new VariableConstante(typeVar, "", valeur));
	}

	@Override
	public void fonctionAppel(String nom, int nbArg){
		Instruction ins = null;
		Moteur moteur = null;
		ArrayList<IElementProgramme> lEL = new ArrayList<IElementProgramme>();
		/* Récupération des arguments */
		for(int i = 1; i<= nbArg; i++) {
			lEL.add(p.depile());
		}
		Collections.reverse(lEL);
		/* Construction du modèle NXTIM correspondant */
		if(nom.equals("OnFwd")){
			moteur = chercheMoteur(((Variable)lEL.get(0)).getNom());
			ins = new InstructionMoteurMov(moteur,(Expression)lEL.get(1));
		}else if(nom.equals("OnRev")){
			moteur = chercheMoteur(((Variable)lEL.get(0)).getNom());
			ins = new InstructionMoteurMov(moteur,(Expression)lEL.get(1),true);
		}else if(nom.equals("Off")){
			moteur = chercheMoteur(((Variable)lEL.get(0)).getNom());
			ins = new InstructionMoteurOff();	
			((InstructionMoteurOff) ins).setMoteur(moteur);
		}else if(nom.equals("Wait")){
			ins = new InstructionAttente();
			((InstructionAttente)ins).setExpression((Expression)lEL.get(0));
		}
		/* Stockage du résulat */
		p.empile(ins);
	}

	private Moteur chercheMoteur(String iElementProgramme){
		if(iElementProgramme.equals("OUT_A")) {
			return Moteur.A;
		}
		else if(iElementProgramme.equals("OUT_B")) {
			return Moteur.B;
		}
		else {
			return Moteur.C;
		}
	}
	
	@Override
	public void operation(String op){
		/* Récupération des 2 memnres de l'opération */
		IElementProgramme ie = p.depile();
		IElementProgramme ie2 = p.depile();
		/* Construction du modèle NXTIM correspondant */
		Operation oper = new Operation(convOperateur(op),(Variable)null,(Variable) null);
		oper.setMembreDroit((Expression)ie);
		oper.setMembreGauche((Expression) ie2);
		/* Stockage du résultat */
		p.empile(oper);
	}

	private Operateur convOperateur(String nom){

		if(nom.equals("+")) {
			return Operateur.ADDITION;
		}
		else if (nom.equals("-")) {
			return Operateur.SOUSTRACTION;
		}
		else if (nom.equals("*")) {
			return Operateur.MULTIPLICATION;
		}
		else if (nom.equals("/")) {
			return Operateur.DIVISION;
		}
		else if (nom.equals("<=")) {
			return Operateur.INFEGAL;
		}
		else if (nom.equals(">=")) {
			return Operateur.SUPEGAL;
		}
		else if (nom.equals(">")) {
			return Operateur.SUPERIEUR;
		}
		else if (nom.equals("<")) {
			return Operateur.INFERIEUR;
		}
		else if (nom.equals("OU")) {
			return Operateur.OU;
		}
		else if (nom.equals("ET")) {
			return Operateur.ET;
		}
		return Operateur.EGALITE;
	}

	private TypeElement typeConvert(String type){
		if(type.equals("int")) {
			return TypeElement.INT;
		}
		else if(type.equals("float")) {
			return TypeElement.FLOAT;
		}
		else if(type.equals("boolean")) {
			return TypeElement.BOOL;
		}
		else if(type.equals("short")) {
			return TypeElement.SHORT;
		}
		else if(type.equals("long")) {
			return TypeElement.LONG;
		}
		else if(type.equals("list")) {
			return TypeElement.LIST;
		}
		return TypeElement.STRING;
	}

	public static boolean isInteger(String s, int radix) {
		if(s.isEmpty()) {
			return false;
		}
		for(int i = 0; i < s.length(); i++) {
			if(i == 0 && s.charAt(i) == '-') {
				if(s.length() == 1) {
					return false;
				}
				else {
					continue;
				}
			}
			if(Character.digit(s.charAt(i),radix) < 0) {
				return false;
			}
		}
		return true;
	}

	public IElementProgramme getModele(){
		return (IElementProgramme) p.sommet();
	}

	@Override
	public void reset() {
		p.nettoyer();
	}
}
