package com.softdevsix.argos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ArgosApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testSimpleSum() {
		int result = 1 + 1;
		assertEquals(2, result, "1 + 1 should equal 2");
	}
}
