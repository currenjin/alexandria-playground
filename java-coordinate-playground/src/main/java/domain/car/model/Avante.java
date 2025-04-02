package domain.car.model;

import domain.car.Car;
import domain.car.attribute.CarName;
import domain.car.attribute.KmPerLiter;

public class Avante implements Car {
	public Avante(int distance) {
		this.name = CarName.AVANTE.getName();
		this.distance = distance;
		this.kmPerLiter = KmPerLiter.AVANTE.getKmPerLiter();
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
