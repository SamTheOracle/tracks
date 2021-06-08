package commons;

import java.util.Collections;
import java.util.Set;

import io.quarkus.test.junit.QuarkusTestProfile;

public class VehicleTestProfile implements QuarkusTestProfile {
	private static final String VEHICLE_TEST_PROFILE = "vehicle_profile";

	@Override
	public Set<String> tags() {
		return Collections.singleton(VEHICLE_TEST_PROFILE);
	}
}
