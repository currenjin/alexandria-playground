package domain.car.model;

import domain.car.Car;
import domain.car.attribute.CarName;
import domain.car.attribute.KmPerLiter;

public class Sonata implements Car {
	public Sonata(int distance) {
		this.name = CarName.SONATA.getName();
		this.distance = distance;
		this.kmPerLiter = KmPerLiter.SONATA.getKmPerLiter();
	}

	private final String name;
	private final int distance;
	private final int kmPerLiter;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getFuelEfficiency() {
		return this.distance / this.kmPerLiter;
	}
}
