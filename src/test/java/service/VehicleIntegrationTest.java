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

import com.oracolo.findmycar.rest.dto.ErrorDto;
import com.oracolo.findmycar.rest.dto.NewVehicleDto;
import com.oracolo.findmycar.rest.dto.PositionDto;
import com.oracolo.findmycar.rest.dto.UpdateVehicleDto;
import com.oracolo.findmycar.rest.dto.VehicleDto;

import commons.BaseVehicleTest;
import commons.VehicleTestProfile;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.restassured.response.Response;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestProfile(VehicleTestProfile.class)
class VehicleIntegrationTest extends BaseVehicleTest {

	private static final String OWNER_A = "owner_a";
	private static final String BLE_HARDWARE_A = "mac_address_a";
	private static final String VEHICLE_NAME_A = "vehicle_name_a";


	@Test
	@DisplayName("Should get vehicle by owner")
	void shouldGetVehicleByOwnerTest() {
		NewVehicleDto newVehicleDto = new NewVehicleDto();
		newVehicleDto.owner = OWNER_A;
		newVehicleDto.vehicleName = VEHICLE_NAME_A;
		newVehicleDto.bleHardware = BLE_HARDWARE_A;
		newVehicleDto.isFavorite = true;
		given().body(newVehicleDto).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);
		Response response = given().when().queryParam("owner", OWNER_A).get("tracks/vehicles");
		Assertions.assertEquals(200, response.getStatusCode());
		JsonArray jsonArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		Assertions.assertFalse(jsonArray.isEmpty());
		VehicleDto vehicleDto = Json.decodeValue(jsonArray.getJsonObject(0).encode(), VehicleDto.class);
		Assertions.assertEquals(OWNER_A, vehicleDto.getOwner());

	}

	@Test
	@DisplayName("Should get empty vehicle list by owner")
	void shouldGetEmptyVehicleListByOwnerTest() {
		Response response = given().when().queryParam("owner", "test").get("tracks/vehicles");
		Assertions.assertEquals(200, response.getStatusCode());
		JsonArray jsonArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		Assertions.assertTrue(jsonArray.isEmpty());
	}

	@Test
	@DisplayName("Only one vehicle should be favorite")
	void vehicleAShouldBeFavorite() {
		String owner = "owner";

		String vehicleNameC = "vehicle_name_c";
		String bleHardwareC = "ble_hardware_c";
		NewVehicleDto vehicleDtoC = new NewVehicleDto();
		vehicleDtoC.owner = owner;
		vehicleDtoC.vehicleName = vehicleNameC;
		vehicleDtoC.bleHardware = bleHardwareC;
		vehicleDtoC.isFavorite = true;

		String vehicleNameB = "vehicle_name_b";
		String bleHardwareB = "ble_hardware_b";
		NewVehicleDto vehicleDtoB = new NewVehicleDto();
		vehicleDtoB.owner = owner;
		vehicleDtoB.vehicleName = vehicleNameB;
		vehicleDtoB.bleHardware = bleHardwareB;
		vehicleDtoB.isFavorite = false;

		given().body(vehicleDtoC).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);
		given().body(vehicleDtoB).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);

		Response response = given().when().queryParam("owner", owner).get("tracks/vehicles");
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
	public void shouldUpdateVehicleName() {
		String owner = "owner_to_update";

		String vehicleName = "vehicle_name_to_update";
		String bleHardware = "ble_hardware_to_update";
		NewVehicleDto vehicleDto = new NewVehicleDto();
		vehicleDto.owner = owner;
		vehicleDto.vehicleName = vehicleName;
		vehicleDto.bleHardware = bleHardware;
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
	public void shouldUpdateVehicleNameAndIsFavorite() {
		String owner = "update_vehicle_name_and_favorite_owner";
		String ble1 = "update_vehicle_name_is_favorite_ble_";
		String vehicleName1 = "vehicle_name_to_change";
		NewVehicleDto newVehicleDto1 = createNewVehicleDto(owner, ble1, true, vehicleName1);
		given().body(newVehicleDto1).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);

		String ble2 = "update_vehicle_name_is_favorite_ble_2";
		String vehicleName2 = "vehicle_name_to_change_2";
		NewVehicleDto newVehicleDto2 = createNewVehicleDto(owner, ble2, false, vehicleName2);
		given().body(newVehicleDto2).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);

		Response response = given().when().queryParam("owner", owner).get("tracks/vehicles");
		Assertions.assertEquals(200, response.statusCode());
		JsonArray vehiclesArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		List<VehicleDto> vehicleDtos = vehiclesArray.stream().map(JsonObject::mapFrom).map(
				json -> Json.decodeValue(json.encode(), VehicleDto.class)).collect(Collectors.toUnmodifiableList());
		Assertions.assertEquals(vehicleDtos.size(), 2);
		VehicleDto responseDto = Assertions.assertDoesNotThrow(
				() -> vehicleDtos.stream().filter(dto -> dto.getName().equals(vehicleName1)).findFirst().orElseThrow());

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
	public void shouldThrowForbiddenDuringPutVehicle() {

		UpdateVehicleDto updateVehicleDto = new UpdateVehicleDto();
		Response updateResponse = given().body(updateVehicleDto).contentType(MediaType.APPLICATION_JSON).put("tracks/vehicles/8789");
		Assertions.assertEquals(javax.ws.rs.core.Response.Status.FORBIDDEN.getStatusCode(), updateResponse.statusCode());
	}

	@Test
	@DisplayName("Should throw forbidden when deleting a vehicle with different owner")
	public void shouldThrowForbiddenWhenDeletingAVehicleWithDifferentOwner() {
		String owner = "delete_owner_forbidden";
		NewVehicleDto newVehicleDto = createNewVehicleDto(owner, "delete_ble_forbidden", false, "vehicle_name_delete_forbidden");
		given().body(newVehicleDto).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);

		Response response = given().when().queryParam("owner", owner).get("tracks/vehicles");
		Assertions.assertEquals(200, response.statusCode());

		JsonArray vehiclesArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		List<VehicleDto> vehicleDtos = vehiclesArray.stream().map(JsonObject::mapFrom).map(
				json -> Json.decodeValue(json.encode(), VehicleDto.class)).collect(Collectors.toUnmodifiableList());
		Assertions.assertEquals(1, vehicleDtos.size());
		VehicleDto responseDto = Assertions.assertDoesNotThrow(() -> vehicleDtos.stream().findFirst().orElseThrow());

		given().when().queryParam("owner", "different_owner").delete("tracks/vehicles/" + responseDto.getId()).then().assertThat().statusCode(403);
	}
	@Test
	@DisplayName("Should delete vehicle")
	public void shouldDeleteVehicle() {
		String owner = "delete_owner";
		System.out.println(ZonedDateTime.now());
		NewVehicleDto newVehicleDto = createNewVehicleDto(owner, "delete_ble", false, "vehicle_name_delete");
		given().body(newVehicleDto).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);

		Response response = given().when().queryParam("owner", owner).get("tracks/vehicles");
		Assertions.assertEquals(200, response.statusCode());

		JsonArray vehiclesArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		List<VehicleDto> vehicleDtos = vehiclesArray.stream().map(JsonObject::mapFrom).map(
				json -> Json.decodeValue(json.encode(), VehicleDto.class)).collect(Collectors.toUnmodifiableList());
		Assertions.assertEquals(1, vehicleDtos.size());
		VehicleDto responseDto = Assertions.assertDoesNotThrow(() -> vehicleDtos.stream().findFirst().orElseThrow());

		given().when().queryParam("owner", owner).delete("tracks/vehicles/" + responseDto.getId()).then().assertThat().statusCode(204);

		given().when().get("tracks/vehicles/"+responseDto.getId()).then().assertThat().statusCode(404);
	}

	@Test
	@DisplayName("Should delete all vehicle-related data after delete")
	void shouldDeleteAllVehicleRelatedData(){
		String owner = "owner_delete_all_data_ok";
		VehicleDto vehicleDto = createAndGetVehicle(owner,"vehicle_name","ble_delete_all_ok",false);
		PositionDto positionDto = new PositionDto();
		positionDto.userId = owner;
		positionDto.chatId=1234L;
		positionDto.timezone="Europe/Rome";
		positionDto.latitude="lat";
		positionDto.longitude="long";
		positionDto.timestamp = ZonedDateTime.now(ZoneId.of(positionDto.timezone)).toString();

		given().when().body(positionDto).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles/"+vehicleDto.getId()+"/positions").then().assertThat().statusCode(204);

		given().when().queryParam("owner", owner).delete("tracks/vehicles/" + vehicleDto.getId()).then().assertThat().statusCode(204);

		given().when().get("tracks/vehicles/"+vehicleDto.getId()+"/positions/last").then().assertThat().statusCode(404);

	}
	@Test
	@DisplayName("Should change owner when change_owner parameter is specified")
	void shouldChangeOwner(){
		String owner = "owner_delete_all_data";
		VehicleDto vehicleDto = createAndGetVehicle(owner,"vehicle_name","ble_delete_all",false);
		String newOwner = "new_owner_test";
		createNewVehicleAssociation(newOwner,vehicleDto.getId(),false);
		given().when().queryParam("owner", owner).queryParam("new_owner",newOwner).delete("tracks/vehicles/" + vehicleDto.getId()).then().assertThat().statusCode(204);

		VehicleDto changedOwnerVehicleDto = getVehicleDto(newOwner);
		Assertions.assertEquals(newOwner,changedOwnerVehicleDto.getOwner());


	}


}