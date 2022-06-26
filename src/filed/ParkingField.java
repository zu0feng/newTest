package exercise_5_4.field;

import java.util.Map;

import exercise_5_4.strategy.ParkingStrategy;
import exercise_5_4.visitor.ParkingVisitor;

/**
 * A mutable ADT
 */
public interface ParkingField extends Iterable<String> {
	
	/**
	 * ����һ���µ�ͣ����
	 * 
	 * @param nos ��ͣ��λ�ı�ţ���Ϊ��Ȼ���������ظ�
	 * @param widths ��ͣ��λ�Ŀ�ȣ�������Ԫ������=nos��Ԫ������ ��>=5 
	 * @return һ��ͣ�������󣬰�����widths.length����λ������λ�Ŀ����nos����Ӧ����һ�£��Ҹ���λ�Ͼ�δ��ͣ��
	 * @throws ���Υ��nos��widths���Ϸ�
	 */
	public static ParkingField create(int[] nos, int[] widths) throws Exception {
		return new ConcreteParkingField(nos, widths);
	}
	
	/**
	 * ����һ���µ�ͣ����
	 * 
	 * @param lots key�ǳ�λ��ţ���Ȼ������value�ǳ�λ��ȣ���Ȼ������lots����>=5��
	 * @return һ��ͣ�������󣬰�����lots.size()����λ������λ�ı��������lots�е�KVһ�£��Ҹ���λ�Ͼ�δ��ͣ��
	 * @throws ���lots���Ϸ�
	 */
	public static ParkingField create(Map<Integer, Integer> lots) throws Exception {
		return new ConcreteParkingField(lots);
	}
	
	/**
	   * ��ĳ��ͣ��λ��ͣ��
	   * ���ƺ�Ϊplate�ĳ�����֮ǰûͣ�ڳ�����ִ�к�ͣ���˳�λ��Ϊnum�ĳ�λ�ϣ��ó�λ��ȴ��ڳ����
	   * ������λ��״̬����
	 * 
	 * @param plate  Ҫͣ�����ĳ������ƺţ�not null
	 * @param width  ���Ŀ�ȣ���Ȼ��
	 * @param num 	  ָ����ͣ��λ��ţ���Ȼ��
	 * @throws ���plate���Ѿ�ͣ�ڸ�ͣ����������num��λ�ѱ�������ռ�ã�����num��λ��Ȳ�����width������num�����ǺϷ���λ
	 */
	public void parking(String type, String plate, int width, int num, String[] extraRegistrationInfo) throws Exception;

	/**
	   * ��ͣ����ͣ�����Զ��������ͣ��λ
	   * ���ƺ�Ϊplate�ĳ�����֮ǰûͣ�ڳ�����ִ�к�ͣ����һ��֮ǰ���еĳ�λ�ϣ��ó�λ��ȴ��ڳ����
	   * ������λ��״̬����
	 * 
	 * @param plate Ҫͣ�����ĳ������ƺ�
 	 * @param width  ���Ŀ�ȣ���Ȼ��
 	 * @throws ���plate���Ѿ�ͣ�ڸ�ͣ���������߸ó����Ѿ�û�г���width�Ŀ��г�λ
	 */
	public void parking(String type, String plate, int width, String[] extraRegistrationInfo);

	/**
	   * ������ʻ��ͣ������plate��ԭ��ռ�õĳ�λ�ճ����ˣ����������ͣ���ķ��ã���Сʱ10Ԫ�������Сʱ����Сʱ���㣩
	 * 
	 * @param plate ��ʻ��ĳ�����not null
	 * @return ����ͣ���ķ��ã���ȷ����õ���
	 * @throws plate����û��ͣ�ڱ�����
	 */
	public double depart(String plate) throws Exception;

	/**
	   * ���ص�ǰͣ����ÿ����λ��״̬���գ���ĳ��ռ�ã�
	 * 
	 * @return KeyΪͣ��λ�ı�ţ�ValueΪ�ó�λ�ϵĳ������ƺš����ͣ��λ���޳��������Ӧ��ValueΪ����
	 */
	public Map<Integer, String> status();
	
	/**
	 * ��ó������ܵĳ�λ��Ŀ
	 * 
	 * @return �������ܵĳ�λ��Ŀ
	 */
	public int getNumberOfLots();
	
	/**
	 * ���������Ƿ����ָ����λ�źͿ�ȵĳ�λ
	 * 
	 * @return true������ڣ�false���������
	 */
	public boolean isLotInParkingField(int num, int width);
	
	/**
	 * ����Ŀǰ�Ƿ�Ϊ�գ�û�г�ͣ��
	 * 
	 * @return true ����գ�false������
	 */
	public boolean isEmpty();

	/**
	 * ����ĳ��λ���
	 *
	 * @param num ��λ��
	 * @return �ó�λ���
	 * @throws ���num�ǷǷ���λ
	 */
	int getLotWidth(int num) throws Exception;
	
	/**
	 * An extension point of external visitor operations
	 * 
	 * @param pv a visitor object
	 * @return 
	 */
	public double accept(ParkingVisitor pv);
	
	public void setParkingStrategy(ParkingStrategy ps);

}
