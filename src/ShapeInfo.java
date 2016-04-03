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
}
