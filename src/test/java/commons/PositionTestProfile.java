package commons;

import java.util.Collections;
import java.util.Set;

import io.quarkus.test.junit.QuarkusTestProfile;

public class PositionTestProfile implements QuarkusTestProfile {
	private static final String POSITION_TEST_PROFILE = "position_profile";

	@Override
	public Set<String> tags() {
		return Collections.singleton(POSITION_TEST_PROFILE);
	}
}
