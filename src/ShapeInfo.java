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
	int strokeInt;		//�� ���� ������ ��
	int strokeType;		//�� ����
	boolean fill;	//ä��� ����	true ä��� / false ��ä���
	public ShapeInfo () {
		color = Color.BLACK;
		fill = false;
		setStroke(1);
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
	public void setStroke(int size) {
		BasicStroke tmp = new BasicStroke(size);
		this.stroke = tmp;
		this.strokeInt = size;
	}
	/* �� ���� �ҷ����� */
	public BasicStroke getStroke() {
		return stroke;
	}
	/* �� ���� ������ �ҷ����� */
	public int getIntStroke() {
		return strokeInt;
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
