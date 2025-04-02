package domain.car.attribute;

public enum CarName {
	SONATA("Sonata"),
	AVANTE("Avante"),
	K5("K5"),
	;

	private final String name;

	CarName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
