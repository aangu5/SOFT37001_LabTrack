package org.ordep.labtrack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.ordep.labtrack.controller.APIController;
import org.ordep.labtrack.controller.WebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LabTrackApplicationTests {

	@Autowired
	private WebController webController;
	@Autowired
	private APIController apiController;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(webController);
		Assertions.assertNotNull(apiController);
	}

}
