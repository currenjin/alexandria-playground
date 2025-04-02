package domain.car.attribute;

public enum KmPerLiter {
	SONATA(10),
	AVANTE(15),
	K5(13),
	;

	private final int kmPerLiter;

	KmPerLiter(int kmPerLiter) {
		this.kmPerLiter = kmPerLiter;
	}

	public int getKmPerLiter() {
		return kmPerLiter;
	}
}
