package unit;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.ForbiddenException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.oracolo.findmycar.dao.VehicleDao;
import com.oracolo.findmycar.entities.Vehicle;
import com.oracolo.findmycar.entities.VehicleAssociation;

import com.oracolo.findmycar.service.VehicleAssociationService;
import com.oracolo.findmycar.service.VehicleService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
class VehicleServiceTest {

	private static final String OWNER_A = "owner_a";
	private static final String BLE_HARDWARE_A = "mac_address_a";
	private static final String VEHICLE_NAME_A="vehicle_name_a";
	private static final String OWNER_B = "owner_b";
	private static final String BLE_HARDWARE_B = "mac_address_b";
	private static final String VEHICLE_NAME_B="vehicle_name_b";

	@Inject
	VehicleService vehicleService;

	@InjectMock
	VehicleDao vehicleDao;

	@InjectMock
	VehicleAssociationService vehicleAssociationService;




	@Test
	@DisplayName("Throw when ble already exists")
	public void shouldThrowWhenBleAlreadyExists(){
		when(vehicleDao.getVehicleByBleHardware(BLE_HARDWARE_A)).thenReturn(Optional.of(new Vehicle()));
		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleName(VEHICLE_NAME_A);
		vehicle.setOwner(OWNER_A);
		vehicle.setBleHardwareMac(BLE_HARDWARE_A);
		Throwable throwable = Assertions.assertThrows(ForbiddenException.class,()->vehicleService.createVehicle(vehicle,false));
		Assertions.assertEquals(throwable.getClass(), ForbiddenException.class);
		Assertions.assertEquals("Cannot create a new vehicle with same ble hardware "+BLE_HARDWARE_A,throwable.getMessage());
	}

	@Test
	@DisplayName("Do not throw when ble does not already exists")
	public void shouldNotThrowWhenBleDoesNotAlreadyExists(){
		when(vehicleDao.getVehicleByBleHardware(BLE_HARDWARE_A)).thenReturn(Optional.empty());
		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleName(VEHICLE_NAME_A);
		vehicle.setOwner(OWNER_A);
		vehicle.setBleHardwareMac(BLE_HARDWARE_A);
		Assertions.assertDoesNotThrow(()->vehicleService.createVehicle(vehicle,false));
		ArgumentCaptor<Vehicle> vehicleArgumentCaptor = ArgumentCaptor.forClass(Vehicle.class);
		verify(vehicleDao).insert(vehicleArgumentCaptor.capture());
		Vehicle insertedVehicle = vehicleArgumentCaptor.getValue();
		Assertions.assertEquals(VEHICLE_NAME_A,insertedVehicle.getVehicleName());
		Assertions.assertEquals(OWNER_A,insertedVehicle.getOwner());
		Assertions.assertEquals(BLE_HARDWARE_A,insertedVehicle.getBleHardwareMac());
	}

	@Test
	@DisplayName("Should create vehicle association with favorite false")
	public void shouldCreateVehicleAssociationWithFavoriteAsFalse(){
		when(vehicleDao.getVehicleByBleHardware(BLE_HARDWARE_A)).thenReturn(Optional.empty());
		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleName(VEHICLE_NAME_A);
		vehicle.setOwner(OWNER_A);
		vehicle.setBleHardwareMac(BLE_HARDWARE_A);
		Assertions.assertDoesNotThrow(()->vehicleService.createVehicle(vehicle,false));
		ArgumentCaptor<VehicleAssociation> vehicleAssociationArgumentCaptor = ArgumentCaptor.forClass(VehicleAssociation.class);
		verify(vehicleAssociationService).insertAssociation(vehicleAssociationArgumentCaptor.capture());

		VehicleAssociation vehicleAssociation = vehicleAssociationArgumentCaptor.getValue();
		Assertions.assertFalse(vehicleAssociation.getFavorite());
	}

	@Test
	@DisplayName("If user is also owner should delete association and vehicle if no other association are present")
	public void deleteWithUserIsOwnerAndNoOtherUsers(){

	}
	@Test
	@DisplayName("Should delete association and change owner when owner not null")
	void deleteWithChangeOwner(){

	}

}
