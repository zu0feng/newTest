package factory;

import java.util.Calendar;

import parkable.Car;
import parkable.Motor;
import parkable.Parkable;
import parkable.Plane;

public class ParkableFactory implements ParkableFactoryInterface {

	@Override
	public Parkable create(String type, String plate, int width, String[] extraRegistrationInfo) throws Exception {
		if (type.equals("car")) {
			Car c = new Car(plate, width);
			c.registerDriver(extraRegistrationInfo[0]);
			return c;
		} else if (type.equals("motor")) {
			Motor m = new Motor(plate, width);
			m.registerDriver(extraRegistrationInfo[0]);
			return m;
		} else if (type.equals("plane")) {
			Plane p = new Plane(plate, width);
			Calendar c = Calendar.getInstance();
			//����extraRegistrationInfo[1]�ַ�������c��ֵ
			p.registerAirport(extraRegistrationInfo[0], c);
			return p;
		}
		else
			throw new Exception("Illegal type");
	}

}
