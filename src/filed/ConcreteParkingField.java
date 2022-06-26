package exercise_5_4.field;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import exercise_5_4.parkable.Parkable;
import exercise_5_4.strategy.ParkingStrategy;
import exercise_5_4.strategy.RandomParkingStrategy;
import exercise_5_4.visitor.ParkingVisitor;

public class ConcreteParkingField implements ParkingField {

	// Rep
	private final List<Lot> lots = new ArrayList<>(); // һ�鳵λ
	private final Map<Lot, Parkable> status = new HashMap<>(); // ռ�����
	private final List<Record> records = new ArrayList<>(); // ͣ����¼
	private ParkingStrategy ps = new RandomParkingStrategy();

	// Rep invariants:
	// lots.size() >= 5;
	// lots.size() >= status.size();
	// status�е�ÿ��key����lots�г��֣�
	// status�е�values�в������ظ���
	// status�е�value����Car�Ŀ��С�ڵ�����Ӧ��key����λ���Ŀ�ȣ�
	// ��records�е�ÿ��Record�������record.getTimeOut()Ϊ�գ�
	// ��<record.getLot(),record.getCar()>�ض�������status��

	// Abstraction function:
	// ������һ��ͣ��������ͣ������lots.size()����λ
	// lots��ÿ��Ԫ��l������һ����λ�����źͿ����l.getNumber()��l.getWidth()
	// l��ͣ�ĳ���status.get(l)
	// �ó���������ͣ����¼��records�����е�Ԫ��r��ʾһ��ͣ����¼��������
	// ����r.getCar()��r.getTimeIn()ʱ��ͣ����r.getLot()��λ�ϣ���r.getTimeOut()�뿪������r.getFee()Ԫ

	private void checkRep() {

		assert lots.size() >= 5;
		assert lots.size() >= status.size();

		Set<Parkable> parkingCars = new HashSet<>();
		for (Lot lot : status.keySet()) {
			Parkable car = status.get(lot);
			assert lots.contains(lot);
			assert !parkingCars.contains(car);
			parkingCars.add(car);
			assert lot.getWidth() >= car.getWidth();
		}

		for (Record record : records) {
			if (record.getTimeOut() == null) {
				assert status.containsKey(record.getLot());
				assert status.get(record.getLot()).equals(record.getCar());
			}
		}
	}

	/**
	 * ����һ���µ�ͣ����
	 * 
	 * @param lots key�ǳ�λ��ţ���Ȼ������value�ǳ�λ��ȣ���Ȼ������lots����>=5��
	 * @return һ��ͣ�������󣬰�����lots.size()����λ������λ�ı��������lots�е�KVһ�£��Ҹ���λ�Ͼ�δ��ͣ��
	 * @throws ���lots���Ϸ�
	 */
	public ConcreteParkingField(Map<Integer, Integer> lotsInfo) throws Exception {

		// check pre-condition, fail fast
		if (lotsInfo.size() < 5)
			throw new Exception();

		for (Map.Entry<Integer, Integer> entry : lotsInfo.entrySet()) {
			int num = entry.getKey();
			int width = entry.getValue();

			// check pre-condition, fail fast
			if (width <= 0)
				throw new Exception();

			// work
			Lot lot = new Lot(num, width);
			this.lots.add(lot);
		}

		// check post-condition
		assert this.getNumberOfLots() == lotsInfo.size();
		assert this.status.size() == 0;

		// check RI
		checkRep();
	}

	public ConcreteParkingField(int[] nos, int[] widths) {
		// TODO Auto-generated method stub
		checkRep();
	}

	/**
	 * ��ĳ��ͣ��λ��ͣ�� ���ƺ�Ϊplate�ĳ�����֮ǰûͣ�ڳ�����ִ�к�ͣ���˳�λ��Ϊnum�ĳ�λ�ϣ��ó�λ��ȴ��ڳ���� ������λ��״̬����
	 * 
	 * @param plate Ҫͣ�����ĳ������ƺţ�not null
	 * @param width ���Ŀ�ȣ���Ȼ��
	 * @param num   ָ����ͣ��λ��ţ���Ȼ��
	 * @throws ���plate���Ѿ�ͣ�ڸ�ͣ����������num��λ�ѱ�������ռ�ã�����num��λ��Ȳ�����width������num�����ǺϷ���λ
	 */
	@Override
	public void parking(String type, String plate, int width, int num, String[] extraRegistrationInfo)
			throws Exception {

		Parkable parkable = Parkable.create(type, plate, width, extraRegistrationInfo);

		// plate���Ѿ�ͣ�ڸ�ͣ����
		if (status.containsValue(parkable))
			throw new Exception();

		// num��λ�ѱ�������ռ��
		for (Lot lot : status.keySet())
			if (lot.getNumber() == num)
				throw new Exception();

		boolean legal = false;
		for (Lot lot : lots) {
			if (lot.getNumber() == num) {
				// num��λ��Ȳ�����width
				if (lot.getWidth() < width)
					throw new Exception();
				legal = true;

				// ��ʽͣ��
				status.put(lot, parkable);
				records.add(new Record(parkable, lot));

				parkable.setState(parkable.getState().parking());
			}
		}
		// num�����ǺϷ���λ
		if (!legal)
			throw new Exception();

		checkRep();
	}

	@Override
	public void parking(String type, String plate, int width, String[] extraRegistrationInfo) {

		Parkable car;
		try {
			car = Parkable.create(type, plate, width, extraRegistrationInfo);
			Lot lot = this.ps.selectLot(lots, status, car);
			//������ɽ�carͣ��lot�ϵ��߼�
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		checkRep();
	}

	@Override
	public double depart(String plate) throws Exception {
		Lot lot = null;
		Record rec = null;
		Parkable car = null;

		Iterator<Record> iterator = records.iterator();
		while (iterator.hasNext()) {
			Record r = iterator.next();
			if (r.getCar().getPlate().equals(plate) && r.getTimeOut() == null) {
				lot = r.getLot();
				rec = r;
				car = r.getCar();
				break;
			}
		}

		status.remove(lot);
		rec.setTimeOut();

		double fee = rec.calcFee();

		car.setState(car.getState().depart());

		checkRep();

		return fee;
	}

	@Override
	public Map<Integer, String> status() {
		Map<Integer, String> st = new HashMap<>();
		for (Lot lot : lots) {
			Parkable c = status.get(lot);
			if (c == null)
				st.put(lot.getNumber(), "");
			else
				st.put(lot.getNumber(), status.get(lot).getPlate());
		}
		checkRep();
		return st;
	}

	@Override
	public int getNumberOfLots() {
		return lots.size();
	}

	@Override
	public boolean isLotInParkingField(int num, int width) {
		for (Lot lot : lots) {
			if (lot.getNumber() == num && lot.getWidth() == width)
				return true;
		}

		return false;
	}

	@Override
	public boolean isEmpty() {
		return status.isEmpty();
	}

	@Override
	public int getLotWidth(int num) throws Exception {
		for (Lot lot : lots) {
			if (lot.getNumber() == num)
				return lot.getWidth();
		}

		throw new Exception("No such lot " + num);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("The parking field has total number of lots: " + this.getNumberOfLots() + "\n");

		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(4);
		String ratio = numberFormat.format((double) this.status.size() / (double) this.getNumberOfLots() * 100);

		sb.append("Now " + ratio + "% lots are occupied" + "\n");
		for (Lot lot : lots) {
			if (status.get(lot) != null)
				sb.append("Lot " + lot.getNumber() + " (" + lot.getWidth() + "):\tCar " + status.get(lot).getPlate()
						+ "\n");
			else
				sb.append("Lot " + lot.getNumber() + " (" + lot.getWidth() + "):\tFree\n");

		}
		return sb.toString();
	}

	@Override
	public double accept(ParkingVisitor pv) {
		return pv.visit(this);
	}

	@Override
	public Iterator<String> iterator() {
		return new ParkingIterator(this.status);
	}

	@Override
	public void setParkingStrategy(ParkingStrategy ps) {
		this.ps = ps;
	}

}
