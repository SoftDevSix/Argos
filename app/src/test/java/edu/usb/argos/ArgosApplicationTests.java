package edu.usb.argos;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
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
	void testMainMethod() {
		try {
			ArgosApplication.main(new String[] {});
		} catch (Exception e) {
			fail("Main method threw an exception: " + e.getMessage());
		}
	}

	@Test
	void testSimpleSum() {
		int result = 1 + 1;
		assertEquals(2, result, "1 + 1 should equal 2");
	}
}
