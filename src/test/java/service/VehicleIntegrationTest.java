package service;

import static io.restassured.RestAssured.given;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.oracolo.findmycar.Role;
import com.oracolo.findmycar.rest.dto.ErrorDto;
import com.oracolo.findmycar.rest.dto.NewVehicleDto;
import com.oracolo.findmycar.rest.dto.PositionDto;
import com.oracolo.findmycar.rest.dto.UpdateVehicleDto;
import com.oracolo.findmycar.rest.dto.VehicleDto;

import commons.BaseVehicleTest;
import commons.VehicleTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import io.restassured.response.Response;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@QuarkusTest
@TestProfile(VehicleTestProfile.class)
class VehicleIntegrationTest extends BaseVehicleTest {

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
	@DisplayName("Should get vehicle by owner")
	@TestSecurity(user = "user", roles = Role.USER)
	@OidcSecurity(claims = { @Claim(key = "sub", value = OWNER_A), @Claim(key = "email", value = "gzano93@gmail.com") })
	void shouldGetVehicleByOwnerTest() {
		NewVehicleDto newVehicleDto = new NewVehicleDto();
		newVehicleDto.vehicleName = VEHICLE_NAME_A;
		newVehicleDto.bleHardware = BLE_HARDWARE_A;
		newVehicleDto.isFavorite = true;
		given().body(newVehicleDto).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);
		Response response = given().when().get("tracks/vehicles");
		Assertions.assertEquals(200, response.getStatusCode());
		JsonArray jsonArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		Assertions.assertFalse(jsonArray.isEmpty());
		VehicleDto vehicleDto = Json.decodeValue(jsonArray.getJsonObject(0).encode(), VehicleDto.class);
		Assertions.assertEquals(OWNER_A, vehicleDto.getOwner());

	}

	@Test
	@DisplayName("Should get empty vehicle list by owner")
	@TestSecurity(user = "user", roles = Role.USER)
	@OidcSecurity(claims = { @Claim(key = "sub", value = OWNER_B), @Claim(key = "email", value = "gzano93@gmail.com") })
	void shouldGetEmptyVehicleListByOwnerTest() {
		Response response = given().when().get("tracks/vehicles");
		Assertions.assertEquals(200, response.getStatusCode());
		JsonArray jsonArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		Assertions.assertTrue(jsonArray.isEmpty());
	}

	@Test
	@DisplayName("Only one vehicle should be favorite")
	@TestSecurity(user = "user", roles = Role.USER)
	@OidcSecurity(claims = { @Claim(key = "sub", value = OWNER_C), @Claim(key = "email", value = "gzano93@gmail.com") })
	void vehicleAShouldBeFavorite() {

		NewVehicleDto vehicleDtoC = new NewVehicleDto();
		vehicleDtoC.vehicleName = VEHICLE_NAME_C;
		vehicleDtoC.bleHardware = BLE_HARDWARE_C;
		vehicleDtoC.isFavorite = true;

		NewVehicleDto vehicleDtoB = new NewVehicleDto();
		vehicleDtoB.vehicleName = VEHICLE_NAME_B;
		vehicleDtoB.bleHardware = BLE_HARDWARE_B;
		vehicleDtoB.isFavorite = false;

		given().body(vehicleDtoC).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);
		given().body(vehicleDtoB).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);

		Response response = given().when().get("tracks/vehicles");
		Assertions.assertEquals(200, response.getStatusCode());
		JsonArray vehiclesArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		Assertions.assertTrue(vehiclesArray.size() >= 2);
		List<VehicleDto> vehicleDtos = vehiclesArray.stream().map(JsonObject::mapFrom).map(
				json -> Json.decodeValue(json.encode(), VehicleDto.class)).collect(Collectors.toUnmodifiableList());
		long numberOfFavoriteVehicle = vehicleDtos.stream().filter(VehicleDto::isFavorite).count();
		Assertions.assertEquals(numberOfFavoriteVehicle, 1);
		VehicleDto favoriteDto = Assertions.assertDoesNotThrow(
				() -> vehicleDtos.stream().filter(VehicleDto::isFavorite).findFirst().orElseThrow());
		Assertions.assertEquals(favoriteDto.getName(), vehicleDtoC.vehicleName);

	}

	@Test
	@DisplayName("Should update only vehicle name")
	@TestSecurity(user = "user", roles = Role.USER)
	@OidcSecurity(claims = { @Claim(key = "sub", value = OWNER_D), @Claim(key = "email", value = "gzano93@gmail.com") })
	public void shouldUpdateVehicleName() {
		String owner = "owner_to_update";


		NewVehicleDto vehicleDto = new NewVehicleDto();
		vehicleDto.vehicleName = VEHICLE_NAME_D;
		vehicleDto.bleHardware = BLE_HARDWARE_D;
		vehicleDto.isFavorite = true;

		given().body(vehicleDto).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);

		Response response = given().when().queryParam("owner", owner).get("tracks/vehicles");

		Assertions.assertEquals(200, response.getStatusCode());
		JsonArray vehiclesArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		List<VehicleDto> vehicleDtos = vehiclesArray.stream().map(JsonObject::mapFrom).map(
				json -> Json.decodeValue(json.encode(), VehicleDto.class)).collect(Collectors.toUnmodifiableList());
		Assertions.assertEquals(vehicleDtos.size(), 1);
		VehicleDto responseDto = vehicleDtos.get(0);
		UpdateVehicleDto updateVehicleDto = new UpdateVehicleDto();
		updateVehicleDto.vehicleName = "new_vehicle_name";
		updateVehicleDto.id = responseDto.getId();

		Response updateResponse = given().body(updateVehicleDto).contentType(MediaType.APPLICATION_JSON).put(
				"tracks/vehicles/" + responseDto.getId());
		Assertions.assertEquals(204, updateResponse.statusCode());

		Response updatedDtoResponse = given().get("tracks/vehicles/" + responseDto.getId());
		Assertions.assertEquals(200, updatedDtoResponse.statusCode());
		VehicleDto updatedDto = Json.decodeValue(new String(updatedDtoResponse.body().asByteArray()), VehicleDto.class);
		Assertions.assertEquals("new_vehicle_name", updatedDto.getName());
	}

	@Test
	@DisplayName("Should update vehicle name and is favorite")
	@TestSecurity(user = "user", roles = Role.USER)
	@OidcSecurity(claims = { @Claim(key = "sub", value = OWNER_E), @Claim(key = "email", value = "gzano93@gmail.com") })
	public void shouldUpdateVehicleNameAndIsFavorite() {

		NewVehicleDto newVehicleDto1 = createNewVehicleDto(OWNER_E, BLE_HARDWARE_E, true, VEHICLE_NAME_E);
		given().body(newVehicleDto1).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);

		String ble2 = "update_vehicle_name_is_favorite_ble_2";
		String vehicleName2 = "vehicle_name_to_change_2";
		NewVehicleDto newVehicleDto2 = createNewVehicleDto(OWNER_E, ble2, false, vehicleName2);
		given().body(newVehicleDto2).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);

		Response response = given().when().get("tracks/vehicles");
		Assertions.assertEquals(200, response.statusCode());
		JsonArray vehiclesArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		List<VehicleDto> vehicleDtos = vehiclesArray.stream().map(JsonObject::mapFrom).map(
				json -> Json.decodeValue(json.encode(), VehicleDto.class)).collect(Collectors.toUnmodifiableList());
		Assertions.assertEquals(vehicleDtos.size(), 2);
		VehicleDto responseDto = Assertions.assertDoesNotThrow(
				() -> vehicleDtos.stream().filter(dto -> dto.getName().equals(VEHICLE_NAME_E)).findFirst().orElseThrow());

		String newVehicleName = "this is a new name";

		UpdateVehicleDto updateVehicleDto = createUpdateVehicleDto(responseDto.getId(), newVehicleName, false);

		Response updateResponse = given().body(updateVehicleDto).contentType(MediaType.APPLICATION_JSON).put(
				"tracks/vehicles/" + responseDto.getId());
		Assertions.assertEquals(204, updateResponse.statusCode());

		Response updatedDtoResponse = given().get("tracks/vehicles/" + responseDto.getId());
		Assertions.assertEquals(200, updatedDtoResponse.statusCode());
		VehicleDto updatedVehicleDto = updatedDtoResponse.as(VehicleDto.class);

		Assertions.assertFalse(updatedVehicleDto.isFavorite());
		Assertions.assertEquals(newVehicleName, updatedVehicleDto.getName());

		VehicleDto responseDto2 = Assertions.assertDoesNotThrow(
				() -> vehicleDtos.stream().filter(dto -> dto.getName().equals(vehicleName2)).findFirst().orElseThrow());
		Assertions.assertFalse(responseDto2.isFavorite());
		Assertions.assertNotEquals(newVehicleName, responseDto2.getName());

	}

	@Test
	@DisplayName("Should throw Forbidden when dto id not equal to path id")
	@TestSecurity(user = "user", roles = Role.USER)
	@OidcSecurity(claims = { @Claim(key = "sub", value = OWNER_A), @Claim(key = "email", value = "gzano93@gmail.com") })
	public void shouldThrowForbiddenWhenDtoIdNotEqualToPathId() {
		UpdateVehicleDto updateVehicleDto = new UpdateVehicleDto();
		updateVehicleDto.id = 12;
		Response updateResponse = given().body(updateVehicleDto).contentType(MediaType.APPLICATION_JSON).put("tracks/vehicles/8789");
		Assertions.assertEquals(javax.ws.rs.core.Response.Status.FORBIDDEN.getStatusCode(), updateResponse.statusCode());
		ErrorDto errorDto = Assertions.assertDoesNotThrow(() -> updateResponse.as(ErrorDto.class));
		String error = errorDto.error;
		Assertions.assertEquals(ForbiddenException.class.getSimpleName(), error);
		String message = errorDto.message;
		Assertions.assertEquals("Path parameter 8789 is not equal to dto vehicle id 12", message);
	}

	@Test
	@DisplayName("Should throw Forbidden if id is null in put request")
	@TestSecurity(user = "user", roles = Role.USER)
	@OidcSecurity(claims = { @Claim(key = "sub", value = OWNER_G ), @Claim(key = "email", value = "gzano93@gmail.com") })
	public void shouldThrowForbiddenDuringPutVehicle() {

		UpdateVehicleDto updateVehicleDto = new UpdateVehicleDto();
		Response updateResponse = given().body(updateVehicleDto).contentType(MediaType.APPLICATION_JSON).put("tracks/vehicles/8789");
		Assertions.assertEquals(javax.ws.rs.core.Response.Status.FORBIDDEN.getStatusCode(), updateResponse.statusCode());
	}

	@Test
	@DisplayName("Should delete vehicle")
	@TestSecurity(user = "user", roles = Role.USER)
	@OidcSecurity(claims = { @Claim(key = "sub", value = OWNER_G ), @Claim(key = "email", value = "gzano93@gmail.com") })
	public void shouldDeleteVehicle() {

		VehicleDto responseDto = createAndGetVehicle(OWNER_G, VEHICLE_NAME_G, BLE_HARDWARE_G, false);



		given().when().delete("tracks/vehicles/" + responseDto.getId()).then().assertThat().statusCode(204);

		given().when().get("tracks/vehicles/" + responseDto.getId()).then().assertThat().statusCode(404);
	}

	@Test
	@DisplayName("Should delete all vehicle-related data after delete")
	@TestSecurity(user = "user", roles = Role.USER)
	@OidcSecurity(claims = { @Claim(key = "sub", value = OWNER_H), @Claim(key = "email", value = "gzano93@gmail.com") })
	void shouldDeleteAllVehicleRelatedData() {
		VehicleDto vehicleDto = createAndGetVehicle(OWNER_H, VEHICLE_NAME_H, BLE_HARDWARE_H, false);
		PositionDto positionDto = new PositionDto();
		positionDto.chatId = 1234L;
		positionDto.timezone = "Europe/Rome";
		positionDto.latitude = "lat";
		positionDto.longitude = "long";
		positionDto.timestamp = ZonedDateTime.now(ZoneId.of(positionDto.timezone)).toString();

		given().when().body(positionDto).contentType(MediaType.APPLICATION_JSON).post(
				"tracks/vehicles/" + vehicleDto.getId() + "/positions").then().assertThat().statusCode(204);

		given().when().delete("tracks/vehicles/" + vehicleDto.getId()).then().assertThat().statusCode(204);

		given().when().get("tracks/vehicles/" + vehicleDto.getId() + "/positions/last").then().assertThat().statusCode(404);

	}

}