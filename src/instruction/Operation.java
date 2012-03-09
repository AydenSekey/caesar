package instruction;

import traduction.VisiteurTraduction;

public class Operation extends ExpressionComplexe {

	/**
	 * Créé une opération à partir de deux autres.
	 * @param typeOperation
	 * @param membreGauche
	 * @param membreDroit
	 * @throws Exception
	 */
	public Operation(Operateur operation, Operation membreGauche, Operation membreDroit) throws Exception {
		super(operation, membreGauche, membreDroit);
		if(!Operateur.isArithmetique(operation))
			throw new Exception("Opérateur non arithméthique dans Operation.");
	}
	
	/**
	 * Créé une opération sur deux variables.
	 * @param typeOperation
	 * @param membreGauche
	 * @param membreDroit
	 */
	public Operation(Operateur operation, Variable membreGauche, Variable membreDroit) throws Exception
	{
		super(operation, membreGauche, membreDroit);
		if(!Operateur.isArithmetique(operation))
			throw new Exception("Opérateur non arithméthique dans Operation.");
	}
	/**
	 * Créé une opération à partir d'une variable et d'une autre opération.
	 * @param typeOperation
	 * @param membreGauche
	 * @param membreDroit
	 */
	public Operation(Operateur operation, Operation membreGauche, Variable membreDroit) throws Exception
	{
		super(operation, membreGauche, membreDroit);
		if(!Operateur.isArithmetique(operation))
			throw new Exception("Opérateur non arithméthique dans Operation.");
	}
	/**
	 *  Créé une opération à partir d'une variable et d'une autre opération.
	 * @param typeOperation
	 * @param membreGauche
	 * @param membreDroit
	 */
	public Operation(Operateur operation, Variable membreGauche, Operation membreDroit) throws Exception
	{
		super(operation, membreGauche, membreDroit);
		if(!Operateur.isArithmetique(operation))
			throw new Exception("Opérateur non arithméthique dans Operation.");
	}
	

	@Override
	public void accepte(VisiteurTraduction v) {
		v.visiter(this);
	}

}
