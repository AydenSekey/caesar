package instruction;

import traduction.VisiteurTraduction;

public class InstructionMoteurMov extends InstructionMoteurCmd{

	protected boolean reverse;
	protected Expression exp;
	
	public InstructionMoteurMov ()
	{
		reverse = false;
	}
	
	@Override
	public Categorie getCategorie() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void accepte(VisiteurTraduction visiteur) {
		visiteur.visiter(this);
		
	}
	
	public boolean isReverse()
	{
		return reverse;
	}
	
	public String toString()
	{
		return "Moteur mov" ;
	}
	public void setExpression ( Expression exp){
		this.exp = exp;
	}
	
}
