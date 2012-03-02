package instruction;

public class InstructionIfElse extends InstructionConditionelle {
	
	private InstructionIf instIf;
		
	@Override
	public void accepte(VisiteurTraduction visiteur) {
		visiteur.visiter(this);
	}

	
}
