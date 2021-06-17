package service;

import static io.restassured.RestAssured.given;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.oracolo.findmycar.rest.dto.ErrorDto;
import com.oracolo.findmycar.rest.dto.PositionDto;
import com.oracolo.findmycar.rest.dto.VehicleDto;

import commons.BaseVehicleTest;
import commons.PositionTestProfile;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.response.Response;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestProfile(PositionTestProfile.class)
class PositionIntegrationTest extends BaseVehicleTest {

	@Test
	@DisplayName("Should post position correctly")
	void shouldGetVehicleByOwnerTest() {
		VehicleDto vehicleDto = createAndGetVehicle("owner", "name", "ble", false);

		PositionDto positionDto = createDto(1234512312L, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), "Europe/Rome", "latitude",
				"longitude", "owner");

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
	void shouldThrowForbiddenWhenTimezoneIsNotTheTimestampTimezone() {
		VehicleDto vehicleDto = createAndGetVehicle("owner_1", "name_1", "ble_1", false);

		PositionDto positionDto = createDto(1234512312L, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), "Europe/London",
				"latitude", "longitude", "owner_1");

		given().when().with().body(positionDto).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/" + vehicleDto.getId() + "/positions").then().assertThat().statusCode(HttpResponseStatus.FORBIDDEN.code());

	}

	@Test
	@DisplayName("Should throw forbidden when user with no association post position")
	void shouldThrowForbiddenWhenUserWithNoAssociationPostPosition() {
		VehicleDto vehicleDto = createAndGetVehicle("owner_2", "name_2", "ble_2", false);

		PositionDto positionDto = createDto(123445L, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), "Europe/Rome", "latitude",
				"longitude", "user_klasdp");

		Response response = given().when().with().body(positionDto).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/" + vehicleDto.getId() + "/positions");
		Assertions.assertEquals(HttpResponseStatus.FORBIDDEN.code(), response.getStatusCode());
		String messageFromRest = response.as(ErrorDto.class).message;
		String correctMessage = "User with id " + positionDto.userId + " has no association on vehicle " + vehicleDto.getId();
		Assertions.assertEquals(correctMessage, messageFromRest);

	}

	@Test
	@DisplayName("Should get most recent position")
	void shouldGetMostRecentLastPosition() {
		String owner = "owner_3";
		VehicleDto vehicleDto = createAndGetVehicle(owner, "name_2", "ble_3", false);

		PositionDto positionDto1 = createDto(123445L, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), "Europe/Rome", "latitude_1",
				"longitude_2", owner);

		given().when().with().body(positionDto1).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/" + vehicleDto.getId() + "/positions").then().assertThat().statusCode(
				HttpResponseStatus.NO_CONTENT.code());

		PositionDto positionDto2 = createDto(123445L, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), "Europe/Rome",
				"latitude_2", "longitude_2", owner);

		given().when().with().body(positionDto2).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/" + vehicleDto.getId() + "/positions").then().assertThat().statusCode(
				HttpResponseStatus.NO_CONTENT.code());

		PositionDto mostRecentDto = getLastPositionDto(vehicleDto.getId());
		Assertions.assertEquals("latitude_2",mostRecentDto.latitude);
		Assertions.assertEquals("longitude_2",mostRecentDto.longitude);

	}

	@Test
	@DisplayName("Should throw forbidden exception when any dto value is null")
	void shouldThrowForbiddenWhenAnyDtoValueIsNull() {
		PositionDto positionDto1 = createDto(null, ZonedDateTime.now(ZoneId.of("Europe/Rome")).toString(), "Europe/Rome", "latitude",
				"longitude", "user");
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