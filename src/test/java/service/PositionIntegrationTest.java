package service;

import static io.restassured.RestAssured.given;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.oracolo.findmycar.Role;
import com.oracolo.findmycar.rest.dto.ErrorDto;
import com.oracolo.findmycar.rest.dto.PositionDto;
import com.oracolo.findmycar.rest.dto.VehicleDto;

import commons.BaseVehicleTest;
import commons.PositionTestProfile;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import io.restassured.response.Response;

@QuarkusTest
@TestProfile(PositionTestProfile.class)
class PositionIntegrationTest extends BaseVehicleTest {
	private static final String OWNER_A = "owner_a";
	private static final String BLE_HARDWARE_A = "mac_address_a";
	private static final String VEHICLE_NAME_A = "vehicle_name_a";
	private static final String OWNER_B = "owner_b";
	private static final String BLE_HARDWARE_B = "mac_address_b";
	private static final String VEHICLE_NAME_B = "vehicle_name_b";
	private static final String OWNER_C = "owner_c";
	private static final String BLE_HARDWARE_C = "mac_address_c";
	private static final String VEHICLE_NAME_C = "vehicle_name_c";
	private static final String OWNER_D = "owner_d";
	private static final String BLE_HARDWARE_D = "mac_address_d";
	private static final String VEHICLE_NAME_D = "vehicle_name_d";
	private static final String OWNER_E = "owner_e";
	private static final String BLE_HARDWARE_E = "mac_address_e";
	private static final String VEHICLE_NAME_E = "vehicle_name_e";
	private static final String OWNER_F = "owner_f";
	private static final String BLE_HARDWARE_F = "mac_address_f";
	private static final String VEHICLE_NAME_F = "vehicle_name_f";
	private static final String OWNER_G = "owner_g";
	private static final String BLE_HARDWARE_G = "ble_hardware_g";
	private static final String VEHICLE_NAME_G = "vehicle_name_g";
	private static final String OWNER_H = "owner_h";
	private static final String VEHICLE_NAME_H = "vehicle_name_h";
	private static final String BLE_HARDWARE_H = "ble_hardware_h";

	@Test
	@DisplayName("Should post position correctly")
	@TestSecurity(user = "user", roles = Role.USER)
	@OidcSecurity(claims = { @Claim(key = "sub", value = OWNER_A), @Claim(key = "email", value = "gzano93@gmail.com") })
	void shouldPostPositionCorrectly() {
		VehicleDto vehicleDto = createAndGetVehicle(OWNER_A, VEHICLE_NAME_A, BLE_HARDWARE_A, false);

		PositionDto positionDto = createDto(1234512312L, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), "Europe/Rome", "latitude",
				"longitude", OWNER_A);

		given().when().with().body(positionDto).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/" + vehicleDto.getId() + "/positions").then().assertThat().statusCode(204);

		PositionDto returnedPositionDto = getLastPositionDto(vehicleDto.getId());

		Assertions.assertEquals(positionDto.latitude, returnedPositionDto.latitude);
		Assertions.assertEquals(positionDto.longitude, returnedPositionDto.longitude);
		Assertions.assertEquals(positionDto.timestamp, returnedPositionDto.timestamp);
		Assertions.assertEquals(positionDto.timezone, returnedPositionDto.timezone);
		Assertions.assertEquals(positionDto.userId, returnedPositionDto.userId);
	}

	@Test
	@DisplayName("Should throw forbidden with timezone is not the timestamp timezone")
	@TestSecurity(user = "user", roles = Role.USER)
	@OidcSecurity(claims = { @Claim(key = "sub", value = OWNER_B), @Claim(key = "email", value = "gzano93@gmail.com") })
	void shouldThrowForbiddenWhenTimezoneIsNotTheTimestampTimezone() {
		VehicleDto vehicleDto = createAndGetVehicle(OWNER_B, VEHICLE_NAME_B, BLE_HARDWARE_B, false);

		PositionDto positionDto = createDto(1234512312L, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), "Europe/London",
				"latitude", "longitude", "owner_1");

		given().when().with().body(positionDto).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/" + vehicleDto.getId() + "/positions").then().assertThat().statusCode(HttpResponseStatus.FORBIDDEN.code());

	}

	@Test
	@DisplayName("Should get most recent position")
	@TestSecurity(user = "user", roles = Role.USER)
	@OidcSecurity(claims = { @Claim(key = "sub", value = OWNER_D), @Claim(key = "email", value = "gzano93@gmail.com") })
	void shouldGetMostRecentLastPosition() {
		VehicleDto vehicleDto = createAndGetVehicle(OWNER_D, VEHICLE_NAME_D, BLE_HARDWARE_D, false);

		PositionDto positionDto1 = createDto(123445L, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), "Europe/Rome", "latitude_1",
				"longitude_2", OWNER_D);

		given().when().with().body(positionDto1).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/" + vehicleDto.getId() + "/positions").then().assertThat().statusCode(
				HttpResponseStatus.NO_CONTENT.code());

		PositionDto positionDto2 = createDto(123445L, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), "Europe/Rome",
				"latitude_2", "longitude_2", OWNER_D);

		given().when().with().body(positionDto2).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/" + vehicleDto.getId() + "/positions").then().assertThat().statusCode(
				HttpResponseStatus.NO_CONTENT.code());

		PositionDto mostRecentDto = getLastPositionDto(vehicleDto.getId());
		Assertions.assertEquals("latitude_2",mostRecentDto.latitude);
		Assertions.assertEquals("longitude_2",mostRecentDto.longitude);

	}

	@Test
	@DisplayName("Should throw forbidden exception when any dto value is null")
	@TestSecurity(user = "user", roles = Role.USER)
	@OidcSecurity(claims = { @Claim(key = "sub", value = OWNER_E), @Claim(key = "email", value = "gzano93@gmail.com") })
	void shouldThrowForbiddenWhenAnyDtoValueIsNull() {
		PositionDto positionDto1 = createDto(null, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), "Europe/Rome", "latitude",
				"longitude", OWNER_E);
		given().when().with().body(positionDto1).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/101289709/positions").then().assertThat().statusCode(HttpResponseStatus.FORBIDDEN.code());
		PositionDto positionDto2 = createDto(1234L, null, "Europe/Rome", "latitude", "longitude", "user");
		given().when().with().body(positionDto2).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/101289709/positions").then().assertThat().statusCode(HttpResponseStatus.FORBIDDEN.code());
		PositionDto positionDto3 = createDto(1234L, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), null, "latitude", "longitude",
				"user");
		given().when().with().body(positionDto3).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/101289709/positions").then().assertThat().statusCode(HttpResponseStatus.FORBIDDEN.code());
		PositionDto positionDto4 = createDto(12345L, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), "Europe/Rome", null,
				"longitude", "user");
		given().when().with().body(positionDto4).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/101289709/positions").then().assertThat().statusCode(HttpResponseStatus.FORBIDDEN.code());
		PositionDto positionDto5 = createDto(1233L, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), "Europe/Rome", "latitude", null,
				"user");
		given().when().with().body(positionDto5).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/101289709/positions").then().assertThat().statusCode(HttpResponseStatus.FORBIDDEN.code());
		PositionDto positionDto6 = createDto(123L, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), "Europe/Rome", "latitude",
				"longitude", null);
		given().when().with().body(positionDto6).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/101289709/positions").then().assertThat().statusCode(HttpResponseStatus.FORBIDDEN.code());

	}

}