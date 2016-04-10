import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
/*
 * 실질적으로 그림을 그리는 패널
 * 마우스 클릭/해제/드래그 때의 리스너를 등록해놓았다.
 * 1. PaintPanel 클래스 구성 : ShapeInfo의 shape 객체로 도형 정보 등록 
 * 						/ 실질적인 그림 그리기 패널의 paint() 메소드 구현
 * 2. save / open / saveAs 메소드 구현 : 열기, 저장, 다른 이름으로 저장
 * 3. drawTriangle/Pentagon/Hexagon/Star 메소드 구현으로 특수도형 그리기 구현
 * 4. init() 메소드로 그림판 새로 시작하는 메소드 구현
 * 5. 그리기 모드에 대응하는 상수들 선언 :
 * 		PENCIL BRUSH DASH ERASE LINE REC OVAL ROUNDREC TRI PENTA HEXA STAR
 * */
public class PaintPanel extends JPanel {
	private BufferedImage b1 = new BufferedImage(1000, 800, BufferedImage.TYPE_3BYTE_BGR);
	private BufferedImage b2 = new BufferedImage(1000, 800, BufferedImage.TYPE_3BYTE_BGR);
	private Graphics g1 = b1.getGraphics(); // 실제 그려지는 영역
	String thisPath = null;
	ShapeInfo shape = new ShapeInfo();
	int drawM = 1;	//그리기 모드	// 이거 또는 shape내의 type필드 둘 중 하나 선택하고 지우기 *******
	/* 그리기 모드에 대응하는 상수들 */
	final static int PENCIL = 1;	//자유 곡선
	final static int BRUSH = 2;		//붓
	final static int DASH = 3;		//점선
	final static int ERASE = 4;		//지우기
	final static int LINE = 5;		//직선
	final static int REC = 6;		//네모
	final static int OVAL = 7;		//원(타원)
	final static int ROUNDREC = 8;	//둥근 네모
	final static int TRI = 9;		//삼각형
	final static int PENTA = 10;	//오각형
	final static int HEXA = 11;		//육각형
	final static int STAR = 12;		//별

	public PaintPanel (String path) {
		setBackground(Color.WHITE);
		g1.fillRect(0, 0, 1000, 800);
		g1.drawImage(new ImageIcon(path).getImage(), 0, 0, null);
		/* 기본 선 굵기 설정 */
		shape.setStroke(1);
		addMouseListener( new PaintListener());
		addMouseMotionListener( new PaintListener());
	}
	
	class PaintListener implements MouseListener, MouseMotionListener {
		public void mousePressed(MouseEvent e) {
			/* 첫 시작점 shape 객체 내 포인트 벡터에 저장 
			 * (이 상태에서는 포인트 벡터 내에 아무 포인트도 없는 상태) */
			shape.add(e.getPoint());
			((Graphics2D)g1).setStroke(shape.getStroke());	// 선 굵기 설정
			g1.setColor(shape.getColor());					// 선 색 / 채우기 색 설정
		}
		/* 마우스 클릭이 해제됐을 때의 이벤트
		 * 도형 그리기는 이 때 실질적으로 패널에 그려짐 (g1 사용) */
		public void mouseReleased(MouseEvent e) {
			// 포인트 shape 객체 내 벡터에 저장 (끝점 저장)
			shape.add(e.getPoint());
			/* 포인트 shape 객체 내 벡터에 저장되어 있는 시작점과 끝점 받아오기 */
			Point sp = shape.get().firstElement();	// 시작점
			Point ep = shape.get().lastElement();	// 끝점
			Rectangle rect = shape.getRect(sp, ep);
			switch (drawM)
			{
				case ERASE :
					/* 지우개는 굵기의 최저값이 10이도록 설정 */
					if (shape.getIntStroke() < 10)
						((Graphics2D) g1).setStroke(new BasicStroke(10));
					g1.setColor(Color.WHITE);
				case PENCIL :
					sp = shape.point.size() > 1 ? shape.point.get(shape.point.size() - 2) : shape.point.firstElement();
					g1.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
					break;
				case BRUSH :
					sp = shape.point.size() > 1 ? shape.point.get(shape.point.size() - 2) : shape.point.firstElement();
					((Graphics2D) g1).setStroke(new BasicStroke(shape.getIntStroke(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
					g1.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
					break;
				case DASH :
					float[] dash = new float[] { 10, 10, 10, 10 };
					((Graphics2D) g1).setStroke(new BasicStroke(shape.getIntStroke(), 0, BasicStroke.JOIN_MITER, 1.0f, dash, 0));
				case LINE : 
					g1.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
					break;
				case REC :	
					if (shape.fill == false)
						g1.drawRect(rect.x, rect.y, rect.width, rect.height);
					else
						g1.fillRect(rect.x, rect.y, rect.width, rect.height);
					break;
				case OVAL :
					if (shape.fill == false)
						g1.drawOval(rect.x, rect.y, rect.width, rect.height);
					else
						g1.fillOval(rect.x, rect.y, rect.width, rect.height);
					break;
				case ROUNDREC :
					if (shape.fill == false)
						g1.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);	//뒤에 두 인자는 둥근 정도 수치
					else
						g1.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);
					break;
				case TRI :
					drawTriangle(g1, sp, ep, rect, shape.fill);
					break;
				case PENTA :
					drawPentagon(g1, sp, ep, rect, shape.fill);
					break;
				case HEXA :
					drawHexagon(g1, sp, ep, rect, shape.fill);
					break;
				case STAR :
					drawStar(g1, sp, ep, rect, shape.fill);
					break;

			}
			repaint();
			/* 저장된 포인트 모두 삭제 */
			shape.get().clear();
		}
		
		/* 마우스 클릭 후 드래그 할 때, 임시로 보여지는 도형의 이벤트 구현 
		 * 단, PENCIL, BRUSH, ERASE는 드래그할 때도 실제로 그려짐 : g1 그래픽 객체 사용
		 * 나머지 도형들은 임시로만 보여지도록 지역 객체로 g2 그래픽 객체에 그림 */
		public void mouseDragged(MouseEvent e) {
			shape.add(e.getPoint());
			Point sp = shape.get().firstElement();
			Point ep = shape.get().lastElement();
			/* 그리기 모드에 따라 다른 상황으로
			 * 자유곡선, 지우기는 드래그도 그대로 그림판에 입력
			 * 나머지 기능은 드래그 과정은 그냥 보여주기만 (실질적 입력이 아님 - 지역 객체 g2 사용)
			 */
			if (drawM == PENCIL || drawM == BRUSH || drawM == ERASE )	// 자유곡선, 지우개일 때
			{
				((Graphics2D) g1).setStroke(shape.stroke);
				if (drawM == ERASE)
				{
					g1.setColor(Color.WHITE);
					/* 지우개는 굵기의 최저값이 10이도록 설정 */
					if (shape.getIntStroke() < 10)
						((Graphics2D) g1).setStroke(new BasicStroke(10));
				}
				sp = shape.point.size() > 1 ? shape.point.get(shape.point.size() - 2) : shape.point.firstElement();
				if (drawM == BRUSH)
				{
					((Graphics2D) g1).setStroke(new BasicStroke(shape.getIntStroke(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
					g1.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
				}
				else
				{
					g1.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
				}
				repaint();
			}
			else
			{
				Graphics g2 = getGraphics();				//임시 그래픽 객체 받아오기
				Rectangle rect = shape.getRect(sp, ep);		//시작점 끝점 기준으로 사각형 받아오기 (도형 그리기에 사용됨)
				setStrokeType(g2, shape.getStrokeType());	//임시 객체, g1 객체 선 유형 설정하기
				g2.setColor(shape.getColor());				//임시 객체 선 색 설정하기
				switch (drawM)
				{
					case DASH : 
						float[] dash = new float[] { 10, 10, 10, 10 };
						((Graphics2D) g2).setStroke(new BasicStroke(shape.getIntStroke(), 0, BasicStroke.JOIN_MITER, 1.0f, dash, 0));
					case LINE : 
						g2.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
						break;
					case REC :
						if (shape.fill == false)
							g2.drawRect(rect.x, rect.y, rect.width, rect.height);
						else
							g2.fillRect(rect.x, rect.y, rect.width, rect.height);
						break;
					case OVAL :
						if (shape.fill == false)
							g2.drawOval(rect.x, rect.y, rect.width, rect.height);
						else
							g2.fillOval(rect.x, rect.y, rect.width, rect.height);
						break;
					case ROUNDREC :
						if (shape.fill == false)
							g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);	//뒤에 두 인자는 둥근 정도 수치
						else
							g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);
						break;
					case TRI : 
						drawTriangle(g2, sp, ep, rect, shape.fill);
						break;
					case PENTA :
						drawPentagon(g2, sp, ep, rect, shape.fill);
						break;
					case HEXA :
						drawHexagon(g2, sp, ep, rect, shape.fill);
						break;
					case STAR :
						drawStar(g2, sp, ep, rect, shape.fill);
						break;
				}
				/* g2 객체로 그린 것 매번 지워주는 부분 : 부모를 불러와 repaint를 해서 방금 그린 것을 없애줌 */
				getParent().repaint();
			}
		}
		/* 사용하지 않는 메소드들 : 사용하지 않아도 implements 했기 때문에 빈 상태로 오버라이드 */
		public void mouseMoved(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
	/* 선 유형 설정해주는 메소드 */
	public void setStrokeType(Graphics g, int type)
	{
		switch (type)
		{
			case 1 : // 실선 : 아무것도 안해줌
				((Graphics2D) g).setStroke(shape.getStroke());
				((Graphics2D) g1).setStroke(shape.getStroke());
				break;	
			case 2 : // 점선 : 선 유형 바꿔주기
				float[] dash = new float[] { 10, 10, 10, 10 };
				((Graphics2D) g).setStroke(new BasicStroke(shape.getIntStroke(), 0, BasicStroke.JOIN_MITER, 1.0f, dash, 0));
				((Graphics2D) g1).setStroke(new BasicStroke(shape.getIntStroke(), 0, BasicStroke.JOIN_MITER, 1.0f, dash, 0));
				break;
		}
	}
	/* TRI : triangle : 삼각형 그리는 메소드 */
	public void drawTriangle(Graphics g, Point sp, Point ep, Rectangle rect, boolean fill) {
		int r = rect.width;
		int[] x = new int[3];
		int[] y = new int[3];
		x[0] = (sp.x + ep.x)/2;
		y[0] = sp.y;
		x[1] = sp.x;
		y[1] = ep.y;
		x[2] = ep.x;
		y[2] = ep.y;
		/* 도형 채우기 여부 */
		if (fill == false)
			g.drawPolygon(x, y, x.length);
		else
			g.fillPolygon(x, y, x.length);
	}
	/* PENTA : pentagon : 오각형 그리는 메소드 */
	public void drawPentagon(Graphics g, Point sp, Point ep, Rectangle rect, boolean fill) {
		int r = rect.width;
		int[] x = {sp.x, ((ep.x - (sp.x+ep.x)/2)/2+ep.x+sp.x)/2, (ep.x - (sp.x + ep.x)/2)/2+ep.x, ep.x, ((sp.x+ep.x)/2-sp.x)/2+sp.x };
		int[] y = {sp.y, sp.y - (ep.y-sp.y)/2, sp.y, ep.y, ep.y};
		/* 기준 점을 드래그하는 점과 점 사이의 중간점으로 설정 */
		int standX = (sp.x + ep.x)/2;
		int standY = (sp.y + ep.y)/2;
		//g.translate(standX, standY);
		/* 도형 채우기 여부 */
		if (fill == false)
			g.drawPolygon(x, y, x.length);
		else
			g.fillPolygon(x, y, x.length);
		//g.translate(-standX, -standY);
	}
	/* HEXA : hexagon : 육각형 그리는 메소드 */
	public void drawHexagon(Graphics g, Point sp, Point ep, Rectangle rect, boolean fill) {
		int r = rect.width;
		int[] x = new int[6];
		int[] y = new int[6];
		boolean updown = false;
		/* 1. 각 포인트의 y값 좌표가 반전될 때 처리 : 드래그 시작점보다 끝점 y좌표값이 더 작을 때 */
		if (sp.y > ep.y)
			updown = true;
		else
			updown = false;
		x[0] = (sp.x + ep.x)/2;
		y[0] = sp.y;
		x[1] = sp.x;
		/* 위 주석 1번과 같은 경우, 시작점 y좌표에서 빼주는 식으로 변경. y[2], y[4], y[5] 모두 유사하게 처리 */
		y[1] = updown==false?sp.y + Math.abs(ep.y - sp.y)/4:sp.y - Math.abs(ep.y - sp.y)/4;
		x[2] = sp.x;
		y[2] = updown==false?sp.y + Math.abs(ep.y - sp.y)/4*3:sp.y - Math.abs(ep.y - sp.y)/4*3;
		x[3] = (sp.x + ep.x)/2;
		y[3] = ep.y;
		x[4] = ep.x;
		y[4] = updown==false?sp.y + Math.abs(ep.y - sp.y)/4*3:sp.y - Math.abs(ep.y - sp.y)/4*3;
		x[5] = ep.x;
		y[5] = updown==false?sp.y + Math.abs(ep.y - sp.y)/4:sp.y - Math.abs(ep.y - sp.y)/4;
		/* 도형 채우기 여부 */
		if (fill == false)
			g.drawPolygon(x, y, x.length);
		else
			g.fillPolygon(x, y, x.length);
	}
	/* STAR : star : 별 모양 그리는 메소드 */
	public void drawStar(Graphics g, Point sp, Point ep, Rectangle rect, boolean fill) {
		int r = rect.width;
		int[] x = new int[10];
		int[] y = new int[10];
		int cx = r;
		int cy = rect.height;
		int r1 = Math.min(cx, cy) - 10;
		int r2 = Math.min(cx, cy) / 2;
		for (int i = 0; i < 10; i++)
		{
			x[i] = cx + (int)(r1 * Math.cos(i * Math.PI / 5 - Math.PI / 2));
			y[i] = cy + (int)(r1 * Math.sin(i * Math.PI / 5 - Math.PI / 2));
			i++;
			x[i] = cx + (int)(r2 * Math.cos((i+1) * Math.PI / 5 - Math.PI / 2));
			y[i] = cy + (int)(r2 * Math.sin((i+1) * Math.PI / 5 - Math.PI / 2));
		}
		/* 기준 점을 드래그하는 점과 점 사이의 중간점으로 설정 */
		g.translate(sp.x, sp.y);
		g.drawPolygon(x, y, x.length);
		g.translate(-sp.x, -sp.y);
	}
	
	
	/* 그림판 새로 시작하기 */
	public void init(String path) {
		/* 변경 사항 저장할 건지 물어보기 */
		//TODO 
		g1.setColor(Color.WHITE);
		g1.fillRect(0, 0, 1000, 800);
		g1.drawImage(new ImageIcon(path).getImage(), 0, 0, null);
		repaint();
	}
	/* 열기 다이얼로그 출력하고 파일 이름 불러오는 메소드 */
	public String open() {
		/* JFileChooser 객체 생성 */
		JFileChooser chooser = new JFileChooser();
		/* 파일 필터 객체 생성 : png, jpg */
		FileNameExtensionFilter filter1 = new FileNameExtensionFilter("PNG(*.png)", "png");
		FileNameExtensionFilter filter2 = new FileNameExtensionFilter("JPG(*.jpg)", "jpg");
		/* chooser에 파일 필터 설정 */
		chooser.setFileFilter(filter1);
		chooser.setFileFilter(filter2);
		/* 열기 다이얼로그 출력 */
		int ret = chooser.showOpenDialog(null);
		/* 파일 선택 안할 시 경고창 띄우기 */
		if (ret != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다", "경고", JOptionPane.WARNING_MESSAGE);
			return null;
		}
		/* 사용자가 선택한 파일 이름 알아내기 */
		String pathName = chooser.getSelectedFile().getPath();
		String fileName = chooser.getSelectedFile().getName();
		/* 열기 선택한 파일을 가져와 그림 패널 초기화 */
		init(pathName);
		/* 파일 이름으로 타이틀 설정 */
		return fileName;
		
	}
	/* 저장 다이얼로그 출력하고 파일 이름 불러오는 메소드 */
	public String saveAs() {
		/* JFileChooser 객체 생성 */
		JFileChooser chooser = new JFileChooser();
		/* 파일 필터 객체 생성 : png, jpg */
		FileNameExtensionFilter filter1 = new FileNameExtensionFilter("PNG(*.png)", "png");
		FileNameExtensionFilter filter2 = new FileNameExtensionFilter("JPG(*.jpg)", "jpg");
		/* chooser에 파일 필터 설정 */
		chooser.setFileFilter(filter1);
		chooser.setFileFilter(filter2);
		/* 열기 다이얼로그 출력 */
		int ret = chooser.showSaveDialog(null);
		/* 닫기 버튼 입력 시 그냥 닫기 */
		if (ret == JFileChooser.CANCEL_OPTION) {
			return null;
		}

		File file = chooser.getSelectedFile();
		String fileName = file.getName();
		if (fileName.contains(".jpg") || fileName.contains(".JPG") || fileName.contains(".png") || fileName.contains(".PNG"))
			;
		else
			/* 아무 확장자도 입력안 할 때, 기본 확장자는 .png로 자동 설정 */
			file = new File(chooser.getSelectedFile() + ".png");
		/* 이미 존재하는 파일일 경우 */
		if (file.isFile())
		{
			JLabel message = new JLabel();
			message.setText("이미 존재하는 파일입니다. 바꾸시겠습니까?");
			/* 바꾸지 않겠다고 클릭하면 종료 */
			if (!(JOptionPane.showConfirmDialog(null, message, "확인", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION))
				return null;
		}
		
		/* 실질적인 파일 저장 : ImageIO.write을 사용해 버퍼이미지 b1을 파일형태로 저장 */
		try {
			/* 선택된 파일 유형에 따라 저장을 달리함 */
			if (chooser.getFileFilter().equals(filter1))
				ImageIO.write(b1, "png", file);
			else
				ImageIO.write(b1, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		thisPath = file.getPath();
		return file.getName();
	}
	public String save() {
		/* 제목없음.png의 상태. 아직 저장한 적이 없는 파일일 경우, '다음이름으로 저장'하는 단계로 보낸다 */
		if (thisPath == null)
		{
			return saveAs();
		}
		/* filechooser 부를 것 없이, 그 파일에 그대로 저장 */
		File file = new File(thisPath);
		String fileName = file.getName();
		
		/* 실질적인 파일 저장 : ImageIO.write을 사용해 버퍼이미지 b1을 파일형태로 저장 */
		try {
			/* 선택된 파일 유형에 따라 저장을 달리함 */
			if (fileName.contains(".png") || fileName.contains(".PNG"))
				ImageIO.write(b1, "png", file);
			else
				ImageIO.write(b1, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
	public void cancel() {
		getParent().repaint();
	}
	/* repaint() 호출 시 실행되는 메소드 */
	public void paint(Graphics g) {
		g.drawImage(b1, 0, 0, this);
	}
}
