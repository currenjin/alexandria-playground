package domain.rentcompany;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

import domain.car.model.Avante;
import domain.car.model.K5;
import domain.car.model.Sonata;

public class RentCompanyTest {
	private static final String NEWLINE = System.lineSeparator();

	@Test
	public void report() {
		RentCompany company = RentCompany.create();
		company.addCar(new Sonata(150));
		company.addCar(new K5(260));
		company.addCar(new Sonata(120));
		company.addCar(new Avante(300));
		company.addCar(new K5(390));

		String report = company.generateReport();
		assertThat(report).isEqualTo(
			"Sonata : 15리터" + NEWLINE +
				"K5 : 20리터" + NEWLINE +
				"Sonata : 12리터" + NEWLINE +
				"Avante : 20리터" + NEWLINE +
				"K5 : 30리터" + NEWLINE
		);
	}
}
