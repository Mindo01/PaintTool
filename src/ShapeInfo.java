import java.awt.*;
import java.util.Vector;

/* 
 * 도형에 대한 정보를 담고 있는 클래스
 * 1. 포인트 시작점/끝점 판별
 * 2. 도형 타입
 * 3. 선 색상
 * 4. 선 굵기
 * 5. 채우기 여부
 * */
public class ShapeInfo {
	Vector<Point> point = new Vector<Point>();
	int type;		//도형 타입 (자유, 선, 원, 네모, 둥근 네모 ...)
	Color color;	//선 색상
	Stroke stroke;	//선 굵기
	boolean fill;	//채우기 여부	true 채우기 / false 안채우기
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
