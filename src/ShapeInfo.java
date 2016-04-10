import java.awt.*;
import java.util.Vector;

/* 
 * 도형에 대한 정보를 담고 있는 클래스
 * 1. 포인트 시작점/끝점 판별
 * 2. 선 색상
 * 3. 선 굵기 (BasicStroke 객체 저장, stroke 값 정수로 저장)
 * 4. 채우기 여부
 * */
public class ShapeInfo {
	
	Vector<Point> point = new Vector<Point>();
	Color color;		//선 색상
	int opacity;		//투명도
	BasicStroke stroke;	//선 굵기
	int strokeInt;		//선 굵기 정수형 값
	int strokeType;		//선 유형
	boolean fill;	//채우기 여부	true 채우기 / false 안채우기
	public ShapeInfo () {
		color = Color.BLACK;
		fill = false;
		setStroke(1);
		strokeType = 1;
		opacity = 255;
	}
	/* 벡터에 포인트 저장 : add 시점의 좌표 저장 */
	public void add(Point point) {
		this.point.add(point);
	}
	/* 포인트 벡터 불러오기 */
	public Vector<Point> get() {
		return point;
	}
	/* 포인트 벡터 길이 불러오기 */
	public int getLength() {
		return point.size();
	}
	/* 색상 설정 */
	public void setColor(Color color) {
		this.color = color;
	}
	/* 색상 불러오기 */
	public Color getColor() {
		return color;
	}
	/* 선 굵기 설정 */
	public void setStroke(int size) {
		BasicStroke tmp = new BasicStroke(size);
		this.stroke = tmp;
		this.strokeInt = size;
	}
	/* 선 굵기 불러오기 */
	public BasicStroke getStroke() {
		return stroke;
	}
	/* 선 굵기 정수값 불러오기 */
	public int getIntStroke() {
		return strokeInt;
	}
	/* 선 유형 설정 */
	public void setStrokeType(String type) {
		if (type.equals("실선"))
			this.strokeType = 1;
		if (type.equals("점선"))
			this.strokeType = 2;
	}
	/* 선 유형 불러오기 */
	public int getStrokeType() {
		return strokeType;
	}
	/* 투명도 설정하기 */
	public void setOpacity(Color c, int op) {
		opacity = op;
		color = new Color(c.getRed(), c.getGreen(), c.getBlue(), op);
	}
	/* 사각형 객체 반환 : p1과 p2의 좌표 차이를 절대값으로 계산 후 반환 
	 * 어디에 쓰나? 시작점보다 드래그/클릭해제된 점의 좌표가 적을 때, 안 그려지는 오류 처리
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
