package edu.usb.argos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class ArgosApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		assertNotNull(context, "The application context should have loaded.");
	}

	@Test
	void main() {
		ArgosApplication.main(new String[] {});
	}

	@Test
	void testSimpleSum() {
		int result = 1 + 1;
		assertEquals(2, result, "1 + 1 should equal 2");
	}
}
