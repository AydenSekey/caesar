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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import jscratch.controleur.sim.RobotController;
import jscratch.controleur.sim.Simulator;
import jscratch.exceptions.InstructionNonGereeException;
import jscratch.helpers.ErreurHelper;
import jscratch.modeles.sim.MotorPort;
import jscratch.vue.arborescence.ArborescenceTools;
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
import nxtim.instruction.NonLogique;
import nxtim.instruction.TempsCourant;
import nxtim.instruction.InstructionWhile;
import nxtim.instruction.Moteur;
import nxtim.operateur.Operateur;
import nxtim.instruction.RotationMoteur;
import nxtim.instruction.ValeurCapteur;
import nxtim.instruction.Variable;
import nxtim.instruction.VariableCapteur;
import nxtim.instruction.VariableModifiable;
import nxtim.instruction.VisiteurElementProg;

/**
 * @since 1.0
 * @version 1.0
 */
public final class Interpreteur implements Runnable, ObservableInterpreteur, VisiteurElementProg {

	/**
	 * Pile de double sauvegardant les valeurs des Instructions
	 */
	private Deque<Double> pile;

	private RobotController robot;

	/**
	 * Tableau d'observateurs
	 */
	private List<ObserverInterpreteur> listObserver;
	private boolean stop = false;
	private boolean wait = false;
	private boolean run = true;
	private double timeSleep;
	private double timeInterrupt;
	private double timeWait;
	private boolean sleep;
	private boolean sleepwait;

	public Interpreteur(Simulator simulator) {
		this.pile = new ArrayDeque<Double>();
		this.robot = simulator.getRobotController();
		this.listObserver = new ArrayList<ObserverInterpreteur>();
		this.listObserver.add(simulator);
		this.timeSleep = 0;
		this.timeInterrupt = 0;
		this.timeWait = 0;
		this.sleep = false;
		this.sleepwait = false;
	}

	@Override
	public void run() {
		this.run = true;
		this.stop = false;
		this.wait = false;
		this.sleep = false;
		this.sleepwait = false;

		//System.out.println("Demarrage de l'interpreteur ; stop="+stop);

		while(!stop){
			for(Instruction l : ArborescenceTools.getInstance().trouveTaches()){
				l.accepte(this);
			}
			stop=true;
		}
		this.notifyObserver("End", 0, null);

		//System.out.println("Arret de l'interpreteur ; stop =" + stop);
	}

	public synchronized void waitThread() {
		this.notify();
		this.wait = true;
		timeInterrupt = System.nanoTime();
		if(sleep){
			sleepwait = true;
			sleep = false;
		}
	}

	public synchronized void notifyThread() {
		this.notify();
		this.wait = false;
	}

	public synchronized void sleepThread(){
		if(sleepwait){
			double time = timeWait - ((timeInterrupt/1000000000.0-timeSleep/1000000000.0)*1000);
			try {
				timeWait = time;
				timeSleep = System.nanoTime();
				sleepwait = false;
				this.wait((long) time);
			} catch (InterruptedException e) {
				if(run){
					ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
				}
			}
		}
	}

	public synchronized void stopThread() {
		this.notify();
		this.run = false;
		this.stop = true;
		this.wait = false;
		this.sleep = false;
		this.sleepwait = false;
	}

	private synchronized void testWait(){
		try {
			if(wait){
				this.wait();
			}
		}
		catch (InterruptedException e) {
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	@Override
	public void addObserver(ObserverInterpreteur o) {
		 listObserver.add(o);
	}

	@Override
	public void deleteObserver(ObserverInterpreteur o) {
		listObserver.remove(o);
	}

	@Override
	public void notifyObserver() {
		for(ObserverInterpreteur o : listObserver){
			o.update(this);
		}
	}

	@Override
	public void notifyObserver(String type, int vitesse, List<MotorPort> ports) {
		for(ObserverInterpreteur o : listObserver){
			o.update(type,vitesse,ports);
		}
	}

	@Override
	public void visiter(InstructionDeclaration inst) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				Variable var = inst.getMembre();
				/*if (var != null) {
					//TODO mettre la variable dans la table des variables
				}*/
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	@Override
	public void visiter(InstructionDeclarationAffectation inst) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				Variable var = inst.getMembre();
				if (var != null) {
					Expression exp = inst.getMembreDroit();
					if (exp != null) {
						exp.accepte(this);
						var.setValeur("" + pile.pop());
					}
				}
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	@Override
	public void visiter(InstructionIf inst) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				Expression cond = inst.getCondition();
				if (cond != null) {
					cond.accepte(this);
					if (pile.pop() == 1) {
						for (Instruction is : inst.getEnfants()) {
							is.accepte(this);
						}
					}
				}
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	@Override
	public void visiter(InstructionIfElse inst) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				Expression cond = inst.getCondition();
				if (cond != null) {
					cond.accepte(this);
					if (pile.pop() == 1) {
						for (Instruction is : inst.getEnfantsIf()) {
							is.accepte(this);
						}
					} else {
						for (Instruction is : inst.getEnfantsElse()) {
							is.accepte(this);
						}
					}
				}
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	/**
	 * execute un While et effectue son contenu tant que la condition est respectée
	 *
	 */
	@Override
	public void visiter(InstructionWhile inst) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				Expression cond = inst.getCondition();

				if (cond != null) {
					cond.accepte(this);
					while (pile.pop() == 1) {
						for (Instruction is : inst.getEnfants()) {
							is.accepte(this);
						}
						cond.accepte(this);
					}
				}
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	@Override
	public void visiter(InstructionDoWhile inst) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				Expression cond = inst.getCondition();
				if (cond != null) {
					do {
						for (Instruction is : inst.getEnfants()) {
							is.accepte(this);
						}
						cond.accepte(this);
					} while (pile.pop() == 1);
				}
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	@Override
	public void visiter(InstructionTache inst) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				List<Instruction> list = inst.getEnfants();
				for (Instruction i : list) {
					i.accepte(this);
				}
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	@Override
	public void visiter(TempsCourant inst) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				pile.push(robot.getCurrentTime());
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	@Override
	public void visiter(InstructionAttente inst) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				Expression ex = inst.getExpression();
				if (ex != null) {
					ex.accepte(this);
				}

				double d = pile.pop();
				timeWait = d;
				try {
					timeSleep = System.nanoTime();
					synchronized (this) {
						sleep = true;
						this.wait((long) d);
					}
				} catch (InterruptedException e) {
					System.out.println("WARNING : interpreteur reveillé - sleep interrompu - Erreur temporelle possible");
				}
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	/**
	 * envoie une commande avec l'ordre d'avancer
	 *
	 */
	@Override
	public void visiter(InstructionMoteurMov inst) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				Expression ex = inst.getExpression();
				if (ex != null) {
					ex.accepte(this);
				}
				Moteur moteur = inst.getMoteur();
				double i = pile.pop();
				this.notifyObserver("Forward", (int) i, moteurToMotorPort(moteur));
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	/**
	 * envoie une commande avec l'ordre d'arreter
	 *
	 */
	@Override
	public void visiter(InstructionMoteurOff inst) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				Moteur moteur = inst.getMoteur();
				this.notifyObserver("Stop", 0, moteurToMotorPort(moteur));
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	/**
	 * execute un For
	 *
	 * @param inst
	 */
	@Override
	public void visiter(InstructionFor inst) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				Expression cond = inst.getCondition();
				Affectation init = inst.getInitialisation();
				Expression iter = inst.getIteration();
				if (init != null) {
					if (cond != null) {
						if (iter != null) {
							init.accepte(this);
							cond.accepte(this);
							while (pile.pop() == 1) {
								for (Instruction is : inst.getEnfants()) {
									is.accepte(this);
								}
								iter.accepte(this);
								cond.accepte(this);
							}
						}
					}
				}
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	/**
	 * visite un Repeat et effectue un certain nombre de fois les memes taches
	 *
	 */
	@Override
	public void visiter(InstructionRepeat inst) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				Expression exp = inst.getExpression();
				double cpt = 0;
				if (exp != null) {
					exp.accepte(this);
					cpt = pile.pop();

					for (int i = 0; i < cpt; i++) {
						for (Instruction is : inst.getEnfants()) {
							is.accepte(this);
						}
					}
				}
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	/**
	 * visite la variable et ajoute la valeur dans la pile
	 */
	@Override
	public void visiter(Variable var) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				if (!"".equals(var.getValeur())) {
					pile.push(Double.parseDouble(var.getValeur()));
				} else {
					pile.push((double) 0);
				}
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	@Override
	public void visiter(Affectation affec) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				Expression md = affec.getMembreDroit();
				VariableModifiable mg = (VariableModifiable) affec.getMembreGauche();

				double d = 0;

				if (md != null) {
					md.accepte(this);
					d = pile.pop();
				}
				if (mg != null) {
					mg.setValeur("" + d);
					if (!affec.isInstruction()) {
						mg.accepte(this);
					}
				}
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	@Override
	public void visiter(ExpressionComplexe expr) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				Operateur opt = expr.getOperateur();
				double d = 0;
				double g = 0;
				Expression mda = expr.getMembreDroit();
				Expression mdg = expr.getMembreGauche();

				if (mda != null) {
					mda.accepte(this);
					d = pile.pop();

				}

				if (mdg != null) {
					mdg.accepte(this);
					g = pile.pop();
				}

				switch (opt) {
					case ADDITION:
						pile.push(g + d);
						break;
					case SOUSTRACTION:
						pile.push(g - d);
						break;
					case MULTIPLICATION:
						pile.push(g * d);
						break;
					case DIVISION:
						pile.push(g / d);
						break;
					case SUPERIEUR:
						if (g > d) {
							pile.push((double) 1);
						} else {
							pile.push((double) 0);
						}
						break;
					case INFERIEUR:
						if (g < d) {
							pile.push((double) 1);
						} else {
							pile.push((double) 0);
						}
						break;
					case EGALITE:
						if (g == d) {
							pile.push((double) 1);
						} else {
							pile.push((double) 0);
						}
						break;
					case SUPEGAL:
						if (g >= d) {
							pile.push((double) 1);
						} else {
							pile.push((double) 0);
						}
						break;
					case INFEGAL:
						if (g <= d) {
							pile.push((double) 1);
						} else {
							pile.push((double) 0);
						}
						break;
					case ET:
						if (g == 0) {
							pile.push((double) 0);
						} else if (d == 0) {
							pile.push((double) 0);
						} else {
							pile.push((double) 1);
						}
						break;
					case OU:
						if (g == 1) {
							pile.push((double) 1);
						} else if (d == 1) {
							pile.push((double) 1);
						} else {
							pile.push((double) 0);
						}
						break;
					default:
						break;
				}
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	@Override
	public void visiter(InstructionDeclarationCapteur arg0) {
		// TODO interprétation
	}

	@Override
	public void visiter(VariableCapteur arg0) { }

	@Override
	public void visiter(InstructionMoteurRotate arg0) {
		// TODO interprétation
	}

	/**
	 * Convertion d'un moteur nxtim en une liste de ports des moteurs
	 * @param moteur La valeur moteur nxtim
	 * @return la liste des ports des moteurs
	 */
	private List<MotorPort> moteurToMotorPort(Moteur moteur) {
		List<MotorPort> ports = new LinkedList<MotorPort>();

		switch (moteur) {
			case A:
				ports.add(MotorPort.OUT_A);
				break;
			case B:
				ports.add(MotorPort.OUT_B);
				break;
			case C:
				ports.add(MotorPort.OUT_C);
				break;
			case AB:
				ports.add(MotorPort.OUT_A);
				ports.add(MotorPort.OUT_B);
				break;
			case AC:
				ports.add(MotorPort.OUT_A);
				ports.add(MotorPort.OUT_C);
				break;
			case BC:
				ports.add(MotorPort.OUT_B);
				ports.add(MotorPort.OUT_C);
				break;
			default:
				break;
		}

		return ports;
	}

	@Override
	public void visiter(InstructionIncrementation inst) {
		try{
			if(run){
				this.testWait();
				this.sleepThread();
				Variable var = inst.getVariable();

				double d = Double.parseDouble(var.getValeur());

				if(inst.isPositive()){
					d++;
				}
				else{
					d--;
				}

				var.setValeur(String.valueOf(d));
			}
		}
		catch(Exception e){
			if(run){
				ErreurHelper.afficherSansSortie(new InstructionNonGereeException("L'instruction n'est pas gérée par le simulateur"));
			}
		}
	}

	@Override
	public void visiter(NonLogique nonLog) {
		Expression cond = nonLog.getCondition();
		assert cond != null;// interpréteur supposer recevoir un code correct.

		cond.accepte(this);
		double d = pile.pop();
		if(d == (double) 0)
			pile.push((double) 1);
		else
			pile.push((double) 0);
	}

	@Override
	public void visiter(ValeurCapteur valCapteur) {	}

	@Override
	public void visiter(RotationMoteur rotMoteur) {
		// TODO interprétation
	}

	@Override
	public void visiter(InstructionRAZRotationMoteur razMoteur) {
		// TODO interprétation
	}

	@Override
	public void visiter(InstructionConfigCapteurs confCapt) {
		// TODO interprétation
	}

}