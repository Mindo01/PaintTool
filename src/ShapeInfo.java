import java.awt.*;
import java.util.Vector;

/* 
 * ������ ���� ������ ��� �ִ� Ŭ����
 * 1. ����Ʈ ������/���� �Ǻ�
 * 2. ���� Ÿ��
 * 3. �� ����
 * 4. �� ����
 * 5. ä��� ����
 * */
public class ShapeInfo {
	Vector<Point> point = new Vector<Point>();
	int type;		//���� Ÿ�� (����, ��, ��, �׸�, �ձ� �׸� ...)
	Color color;	//�� ����
	Stroke stroke;	//�� ����
	boolean fill;	//ä��� ����	true ä��� / false ��ä���
	public ShapeInfo () {
		type = -1;
		color = Color.BLACK;
		fill = false;
	}
	
	public void add(Point point) {
		this.point.add(point);
	}
	public Vector<Point> get() {
		return point;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	public Color getColor() {
		return color;
	}
	
	public void setStroke(int size) {
		BasicStroke tmp = new BasicStroke(size);
		this.stroke = tmp;
	}
	public Stroke getStroke() {
		return stroke;
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
