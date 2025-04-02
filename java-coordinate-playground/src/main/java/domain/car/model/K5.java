package domain.car.model;

import domain.car.Car;
import domain.car.attribute.CarName;
import domain.car.attribute.KmPerLiter;

public class K5 implements Car {
	public K5(int distance) {
		this.name = CarName.K5.getName();
		this.distance = distance;
		this.kmPerLiter = KmPerLiter.K5.getKmPerLiter();
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
