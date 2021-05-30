package com.oracolo.findmycar.service;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.verify;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.oracolo.findmycar.dao.VehicleDao;
import com.oracolo.findmycar.entities.Vehicle;
import com.oracolo.findmycar.rest.VehicleRest;
import com.oracolo.findmycar.rest.dto.NewVehicleDto;
import com.oracolo.findmycar.rest.dto.VehicleDto;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.response.Response;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class VehicleIntegrationTest {

	private static final String OWNER_A = "owner_a";
	private static final String BLE_HARDWARE_A = "mac_address_a";
	private static final String VEHICLE_NAME_A="vehicle_name_a";
	private static final String BLE_HARDWARE_B = "mac_address_b";
	private static final String VEHICLE_NAME_B="vehicle_name_b";




	@Test
	@DisplayName("Should get vehicle by owner")
	void shouldGetVehicleByOwnerTest() {
		NewVehicleDto newVehicleDto = new NewVehicleDto();
		newVehicleDto.owner = OWNER_A;
		newVehicleDto.vehicleName = VEHICLE_NAME_A;
		newVehicleDto.bleHardware = BLE_HARDWARE_A;
		given().body(newVehicleDto).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);
		Response response = given().when().queryParam("owner", OWNER_A).get("tracks/vehicles");
		Assertions.assertEquals(200, response.getStatusCode());
		JsonArray jsonArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		Assertions.assertFalse(jsonArray.isEmpty());
		VehicleDto vehicleDto = Json.decodeValue(jsonArray.getJsonObject(0).encode(), VehicleDto.class);
		Assertions.assertEquals(OWNER_A, vehicleDto.getOwner());
		Assertions.assertTrue(vehicleDto.isFavorite());

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
	void vehicleAShouldBeFavorite(){
		String owner = "owner";

		String vehicleNameC = "vehicle_name_c";
		String bleHardwareC = "ble_hardware_c";
		NewVehicleDto vehicleDtoC= new NewVehicleDto();
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
		Assertions.assertTrue(vehiclesArray.size()>=2);
		List<VehicleDto> vehicleDtos = vehiclesArray.stream().map(JsonObject::mapFrom).map(json->Json.decodeValue(json.encode(),VehicleDto.class)).collect(
				Collectors.toUnmodifiableList());
		long numberOfFavoriteVehicle = vehicleDtos.stream().filter(VehicleDto::isFavorite).count();
		Assertions.assertEquals(numberOfFavoriteVehicle,1);

	}





}