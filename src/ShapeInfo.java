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
	/* 자유 곡선, 붓 / 지우개 / 도형 에 대해 각각의 선 굵기 주기 
	 * 0 : 자유 곡선, 붓
	 * 1 : 지우개
	 * 2 : 도형 
	 * */
	int[] strokeInt = new int[3];		//선 굵기 정수형 값
	int strokeType;		//선 유형
	boolean fill;	//채우기 여부	true 채우기 / false 안채우기
	public ShapeInfo () {
		color = Color.BLACK;
		fill = false;
		strokeInt[0] = 1;
		strokeInt[1] = 10;
		strokeInt[2] = 1;
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
	public void setStroke(int size, int type) {
		this.strokeInt[getType(type)] = size;
	}
	/* 선 굵기 불러오기 */
	public BasicStroke getStroke(int type) {
		stroke = new BasicStroke(strokeInt[getType(type)]);
		return stroke;
	}
	/* 선 굵기 정수값 불러오기 
	 * type : drawM 값(그리기 모드 정수값)을 매개변수로 받아오고,
	 * 		알아서 index값으로 변환해 선 굵기 값을 보내준다 */
	public int getIntStroke(int type) {
		return strokeInt[getType(type)];
	}
	/* stroke 정수 값으로 선 굵기 아이템들의 인덱스 반환 */
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
	/* 도구에 따른 유형을 세 분류로 나눠 반환하기
	 * 0 : 자유 곡선, 붓
	 * 1 : 지우개
	 * 2 : 점선, 직선, 도형들  */
	public int getType(int value)
	{
		/* 그리기 모드에 대응하는 상수들 */
		final int PENCIL = 1;	//자유 곡선
		final int BRUSH = 2;	//붓
		final int DASH = 3;		//점선
		final int ERASE = 4;	//지우기
		final int LINE = 5;		//직선
		final int REC = 6;		//네모
		final int OVAL = 7;		//원(타원)
		final int ROUNDREC = 8;	//둥근 네모
		final int TRI = 9;		//삼각형
		final int PENTA = 10;	//오각형
		final int HEXA = 11;	//육각형
		final int STAR = 12;	//별
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
