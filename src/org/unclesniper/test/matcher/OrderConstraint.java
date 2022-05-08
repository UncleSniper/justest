package org.unclesniper.test.matcher;

public enum OrderConstraint {

	EQUAL("=", "equal to"),
	UNEQUAL("<>", "unequal to"),
	LESS("<", "less than"),
	LESS_EQUAL("<=", "less than or equal to"),
	GREATER(">", "greater than"),
	GREATER_EQUAL(">=", "greater than or equal to");

	private final String symbol;

	private final String humanSymbol;

	private OrderConstraint(String symbol, String humanSymbol) {
		this.symbol = symbol;
		this.humanSymbol = humanSymbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getHumanSymbol() {
		return humanSymbol;
	}

	public OrderConstraint flip() {
		switch(this) {
			case EQUAL:
				return UNEQUAL;
			case UNEQUAL:
				return EQUAL;
			case LESS:
				return GREATER_EQUAL;
			case LESS_EQUAL:
				return GREATER;
			case GREATER:
				return LESS_EQUAL;
			case GREATER_EQUAL:
				return LESS;
			default:
				throw new Error("Unrecognized OrderConstraint: " + name());
		}
	}

	public boolean isSatisfiedBy(int actualRelation) {
		switch(this) {
			case EQUAL:
				return actualRelation == 0;
			case UNEQUAL:
				return actualRelation != 0;
			case LESS:
				return actualRelation < 0;
			case LESS_EQUAL:
				return actualRelation <= 0;
			case GREATER:
				return actualRelation > 0;
			case GREATER_EQUAL:
				return actualRelation >= 0;
			default:
				throw new Error("Unrecognized OrderConstraint: " + name());
		}
	}

}
