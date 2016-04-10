import java.awt.*;
import java.util.Vector;

/* 
 * ������ ���� ������ ��� �ִ� Ŭ����
 * 1. ����Ʈ ������/���� �Ǻ�
 * 2. �� ����
 * 3. �� ���� (BasicStroke ��ü ����, stroke �� ������ ����)
 * 4. ä��� ����
 * */
public class ShapeInfo {
	
	Vector<Point> point = new Vector<Point>();
	Color color;		//�� ����
	int opacity;		//����
	BasicStroke stroke;	//�� ����
	/* ���� �, �� / ���찳 / ���� �� ���� ������ �� ���� �ֱ� 
	 * 0 : ���� �, ��
	 * 1 : ���찳
	 * 2 : ���� 
	 * */
	int[] strokeInt = new int[3];		//�� ���� ������ ��
	int strokeType;		//�� ����
	boolean fill;	//ä��� ����	true ä��� / false ��ä���
	public ShapeInfo () {
		color = Color.BLACK;
		fill = false;
		strokeInt[0] = 1;
		strokeInt[1] = 10;
		strokeInt[2] = 1;
		strokeType = 1;
		opacity = 255;
	}
	/* ���Ϳ� ����Ʈ ���� : add ������ ��ǥ ���� */
	public void add(Point point) {
		this.point.add(point);
	}
	/* ����Ʈ ���� �ҷ����� */
	public Vector<Point> get() {
		return point;
	}
	/* ����Ʈ ���� ���� �ҷ����� */
	public int getLength() {
		return point.size();
	}
	/* ���� ���� */
	public void setColor(Color color) {
		this.color = color;
	}
	/* ���� �ҷ����� */
	public Color getColor() {
		return color;
	}
	/* �� ���� ���� */
	public void setStroke(int size, int type) {
		this.strokeInt[getType(type)] = size;
	}
	/* �� ���� �ҷ����� */
	public BasicStroke getStroke(int type) {
		stroke = new BasicStroke(strokeInt[getType(type)]);
		return stroke;
	}
	/* �� ���� ������ �ҷ����� 
	 * type : drawM ��(�׸��� ��� ������)�� �Ű������� �޾ƿ���,
	 * 		�˾Ƽ� index������ ��ȯ�� �� ���� ���� �����ش� */
	public int getIntStroke(int type) {
		return strokeInt[getType(type)];
	}
	/* stroke ���� ������ �� ���� �����۵��� �ε��� ��ȯ */
	public int strokeIndex(int value)
	{
		int i;
		int[] strValue = {1, 5, 10, 25, 40, 60};
		for (i = 0; i < strValue.length; i++)
		{
			if (strValue[i] == value)
				break;
		}
		return i;
	}
	/* �� ���� ���� */
	public void setStrokeType(String type) {
		if (type.equals("�Ǽ�"))
			this.strokeType = 1;
		if (type.equals("����"))
			this.strokeType = 2;
	}
	/* �� ���� �ҷ����� */
	public int getStrokeType() {
		return strokeType;
	}
	/* ���� �����ϱ� */
	public void setOpacity(Color c, int op) {
		opacity = op;
		color = new Color(c.getRed(), c.getGreen(), c.getBlue(), op);
	}
	/* ������ ���� ������ �� �з��� ���� ��ȯ�ϱ�
	 * 0 : ���� �, ��
	 * 1 : ���찳
	 * 2 : ����, ����, ������  */
	public int getType(int value)
	{
		/* �׸��� ��忡 �����ϴ� ����� */
		final int PENCIL = 1;	//���� �
		final int BRUSH = 2;	//��
		final int DASH = 3;		//����
		final int ERASE = 4;	//�����
		final int LINE = 5;		//����
		final int REC = 6;		//�׸�
		final int OVAL = 7;		//��(Ÿ��)
		final int ROUNDREC = 8;	//�ձ� �׸�
		final int TRI = 9;		//�ﰢ��
		final int PENTA = 10;	//������
		final int HEXA = 11;	//������
		final int STAR = 12;	//��
		switch (value)
		{
			case PENCIL : case BRUSH :
				return 0;
			case ERASE :
				return 1;
			case DASH : case LINE :
			case REC : case OVAL : case ROUNDREC : case TRI : case PENTA : case HEXA : case STAR :
				return 2;
		}
		return 0;
	}
	/* �簢�� ��ü ��ȯ : p1�� p2�� ��ǥ ���̸� ���밪���� ��� �� ��ȯ 
	 * ��� ����? ���������� �巡��/Ŭ�������� ���� ��ǥ�� ���� ��, �� �׷����� ���� ó��
	 * */
	public Rectangle getRect(Point p1, Point p2) {
		Rectangle rect = null;
		int minX = Math.min(p1.x, p2.x);
		int maxX = Math.max(p1.x, p2.x);
		int minY = Math.min(p1.y, p2.y);
		int maxY = Math.max(p1.y, p2.y);
		rect = new Rectangle(minX, minY, maxX - minX, maxY - minY);
		return rect;
	}
}
