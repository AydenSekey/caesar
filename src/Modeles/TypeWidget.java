package Modeles;

public enum TypeWidget {

	INSTRUCTION, IF, WHILE, NEGATION;

	public String toString() {
		switch (this) {
			case INSTRUCTION:
				return "Instruction";
			case NEGATION:
				return "Négation";
			case IF:
				return "If";
			case WHILE:
				return "While";
			default:
				return null;
		}
	}
}
