package commons;

import static io.restassured.RestAssured.given;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Assertions;

import com.oracolo.findmycar.rest.dto.NewVehicleDto;
import com.oracolo.findmycar.rest.dto.PositionDto;
import com.oracolo.findmycar.rest.dto.UpdateVehicleDto;
import com.oracolo.findmycar.rest.dto.VehicleAssociationDto;
import com.oracolo.findmycar.rest.dto.VehicleDto;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.restassured.response.Response;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class BaseVehicleTest {

	protected static NewVehicleDto createNewVehicleDto(String owner, String bleHardware, String bleHumanName, boolean isFavorite,
			String vehicleName) {
		NewVehicleDto newVehicleDto = new NewVehicleDto();
		newVehicleDto.vehicleName = vehicleName;
		newVehicleDto.bleHardware = bleHardware;
		newVehicleDto.isFavorite = isFavorite;
		newVehicleDto.owner = owner;
		newVehicleDto.bleHumanName = bleHumanName;
		return newVehicleDto;
	}

	protected static NewVehicleDto createNewVehicleDto(String owner, String bleHardware, boolean isFavorite, String vehicleName) {
		NewVehicleDto newVehicleDto = new NewVehicleDto();
		newVehicleDto.vehicleName = vehicleName;
		newVehicleDto.bleHardware = bleHardware;
		newVehicleDto.isFavorite = isFavorite;
		newVehicleDto.owner = owner;
		return newVehicleDto;
	}

	protected static UpdateVehicleDto createUpdateVehicleDto(Integer id, String vehicleName, boolean isFavorite) {
		UpdateVehicleDto updateVehicleDto = new UpdateVehicleDto();
		updateVehicleDto.id = id;
		updateVehicleDto.vehicleName = vehicleName;
		updateVehicleDto.isFavorite = isFavorite;
		return updateVehicleDto;
	}

	/**
	 * Creates the VehicleDto by posting a new vehicle, and then getting newly created vehicle
	 * @param owner
	 * @param vehicleName
	 * @param ble
	 * @param isFavorite
	 * @return
	 */
	protected static VehicleDto createAndGetVehicle(String owner, String vehicleName, String ble, boolean isFavorite) {
		NewVehicleDto newVehicleDto = new NewVehicleDto();
		newVehicleDto.owner = owner;
		newVehicleDto.vehicleName = vehicleName;
		newVehicleDto.bleHardware = ble;
		newVehicleDto.isFavorite = isFavorite;
		given().body(newVehicleDto).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);
		Response response = given().when().queryParam("owner", owner).get("tracks/vehicles");
		Assertions.assertEquals(200, response.getStatusCode());
		JsonArray jsonArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		Assertions.assertFalse(jsonArray.isEmpty());
		VehicleDto vehicleDto = Json.decodeValue(jsonArray.getJsonObject(0).encode(), VehicleDto.class);
		Assertions.assertEquals(newVehicleDto.owner, vehicleDto.getOwner());

		Response responseVehicle = given().when().queryParam("owner", newVehicleDto.owner).get("tracks/vehicles");

		Assertions.assertEquals(200, responseVehicle.getStatusCode());
		JsonArray vehiclesArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(responseVehicle.body().asByteArray())));
		List<VehicleDto> vehicleDtos = vehiclesArray.stream().map(JsonObject::mapFrom).map(
				json -> Json.decodeValue(json.encode(), VehicleDto.class)).collect(Collectors.toUnmodifiableList());
		Assertions.assertEquals(1, vehicleDtos.size());
		VehicleDto responseDto = vehicleDtos.get(0);
		return responseDto;
	}

	protected static PositionDto getLastPositionDto(Integer vehicleId) {
		Response response = given().when().with().get("tracks/vehicles/" + vehicleId + "/positions/last");
		Assertions.assertEquals(HttpResponseStatus.OK.code(), response.statusCode());
		PositionDto positionDto = response.as(PositionDto.class);
		Assertions.assertNotNull(positionDto.latitude);
		Assertions.assertNotNull(positionDto.longitude);
		Assertions.assertNotNull(positionDto.timestamp);
		Assertions.assertNotNull(positionDto.timezone);
		Assertions.assertNotNull(positionDto.userId);
		return positionDto;
	}

	protected static PositionDto createDto(Long chatId, String timestamp, String timezone, String latitude, String longitude, String userId){
		PositionDto positionDto = new PositionDto();
		positionDto.latitude = latitude;
		positionDto.longitude=longitude;
		positionDto.timestamp= timestamp;
		positionDto.timezone = timezone;
		positionDto.userId = userId;
		positionDto.chatId = chatId;
		return positionDto;
	}

//	/**
//	 *
//	 * @param owner
//	 * @param ble
//	 * @param isFavorite
//	 * @param vehicleName
//	 * @return
//	 */
//	protected static VehicleDto postNewVehicle(String owner, String ble, boolean isFavorite, String vehicleName){
//		NewVehicleDto newVehicleDto = createNewVehicleDto(owner, ble, isFavorite, vehicleName);
//		given().body(newVehicleDto).contentType(MediaType.APPLICATION_JSON).post("tracks/vehicles").then().assertThat().statusCode(204);
//
//		Response response = given().when().queryParam("owner", owner).get("tracks/vehicles");
//		Assertions.assertEquals(200, response.statusCode());
//
//		JsonArray vehiclesArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
//		List<VehicleDto> vehicleDtos = vehiclesArray.stream().map(JsonObject::mapFrom).map(
//				json -> Json.decodeValue(json.encode(), VehicleDto.class)).collect(Collectors.toUnmodifiableList());
//		Assertions.assertEquals(1, vehicleDtos.size());
//		VehicleDto responseDto = Assertions.assertDoesNotThrow(() -> vehicleDtos.stream().findFirst().orElseThrow());
//		return responseDto;
//	}
	protected static VehicleDto getVehicleDto(String owner){
		Response response = given().when().queryParam("owner", owner).get("tracks/vehicles");
		Assertions.assertEquals(200, response.getStatusCode());
		JsonArray jsonArray = Assertions.assertDoesNotThrow(() -> new JsonArray(Buffer.buffer(response.body().asByteArray())));
		Assertions.assertFalse(jsonArray.isEmpty());
		VehicleDto vehicleDto = Json.decodeValue(jsonArray.getJsonObject(0).encode(), VehicleDto.class);
		return vehicleDto;
	}
	protected static void createNewVehicleAssociation(String userId, Integer vehicleId,boolean isFavorite){
		VehicleAssociationDto vehicleAssociationDto = new VehicleAssociationDto();
		vehicleAssociationDto.vehicleId = vehicleId;
		vehicleAssociationDto.isFavorite=isFavorite;
		vehicleAssociationDto.userId = userId;
		given().body(vehicleAssociationDto).contentType(MediaType.APPLICATION_JSON)
				.post("tracks/vehicles/"+vehicleId+"/associations").then()
				.assertThat().statusCode(204);
	}
}
