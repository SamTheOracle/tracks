package com.oracolo.findmycar.service;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.verify;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.oracolo.findmycar.entities.Vehicle;
import com.oracolo.findmycar.rest.VehicleRest;
import com.oracolo.findmycar.rest.dto.NewVehicleDto;
import com.oracolo.findmycar.rest.dto.VehicleDto;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.response.Response;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class VehicleIntegrationTest {

	private static final String OWNER = "test_owner";



	@BeforeEach
	void createVehicle() {
		NewVehicleDto vehicleDto = new NewVehicleDto();
		vehicleDto.vehicleName = "bel veicolo";
		vehicleDto.owner = OWNER;
		given().body(vehicleDto).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);
	}

	@Test
	@DisplayName("Should get vehicle by owner")
	void shouldGetVehicleByOwnerTest() {
		Response response = given().when().queryParam("owner", OWNER).get("tracks/vehicles");
		Assertions.assertEquals(200, response.getStatusCode());
		JsonArray jsonArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		Assertions.assertFalse(jsonArray.isEmpty());
		VehicleDto vehicleDto = Json.decodeValue(jsonArray.getJsonObject(0).encode(), VehicleDto.class);
		Assertions.assertEquals(OWNER, vehicleDto.getOwner());

	}
	@Test
	@DisplayName("Should get empty vehicle list by owner")
	void shouldGetEmptyVehicleListByOwnerTest() {
		Response response = given().when().queryParam("owner", "test").get("tracks/vehicles");
		Assertions.assertEquals(200, response.getStatusCode());
		JsonArray jsonArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		Assertions.assertTrue(jsonArray.isEmpty());
	}

}