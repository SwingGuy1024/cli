package com.mm.penrose;

import org.jetbrains.annotations.NotNull;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static java.awt.Color.black;

/**
 * Note: Bezier curves may be used to create parabolas:
 * <a href="http://math.stackexchange.com/questions/335226/convert-segment-of-parabola-to-quadratic-bezier-curve">Parabola to B&eacute;zier Curve</a>
 * <p/>
 * Be sure to check the correction in the comment above the formula.
 * <p/>
 * To rotate the parabola by 30 degrees, we get this formula:<br>
 * (k == sqrt(3)/2, k^2 = 3/4)<br>
 * (M == tan alpha = tan(36 degrees).<br>
 * 3M/4x^2 - KMxy + M/4y^2 -x/2 -Ky = 0<br>
 * <p/>
 * (Ref: <a href="http://www.sinclair.edu/centers/mathlab/pub/findyourcourse/worksheets/Calculus/2ndDegreeEquation.pdf">2nd degree equation</a>)
 * <p/>
 * This becomes M((Kx)^2 - kxy + y^2/4) -x/2 = 0<br>
 * We need to factor out the polynomial.<br>
 * <p/>
 * x = (-b +- sqrt(b^2 - 4ac))/2a<br>
 * (k +- sqrt(k^2 - K))/2K<br>
 * Define B = sqrt(3 - 2sqrt(3))/2<br>
 * <p/>
 * Roots are K+B, K-B<br>
 * <p/>
 * <strong>Bezier Representation</strong><br>
 * <p/>You can do this in two steps, first convert the parabola segment to a quadratic Bezier curve (with a single 
 * control point), then convert it to a cubic Bezier curve (with two control points).
 * <p/>Let $f(x)=Ax^2+Bx+C$ be the parabola and let $x<sub>1</sub>$ and $x<sub>2</sub>$ be the edges of the segment on which the parabola is 
 * defined.
 * <p/>
 * Then $P<sub>1</sub>=(x<sub>1</sub>,f(x<sub>1</sub>))$ and $P<sub>2</sub>=(x<sub>2</sub>,f(x<sub>2</sub>))$ are the Bezier curve start and end points
 * and $C=(\frac{x<sub>1</sub>+x<sub>2</sub>}{2},f(x<sub>1</sub>)+f'(x<sub>1</sub>)\cdot \frac{x<sub>1</sub>+x<sub>2</sub>}{2})$ is the control point for the quadratic Bezier curve.
 * <p/>Now you can convert this quadratic Bezier curve to a cubic Bezier curve by define the two control points as:
 * $C<sub>1</sub>=\frac{2}{3}C+\frac{1}{3}P<sub>1</sub>$ and
 * $C<sub>2</sub>=\frac{2}{3}C+\frac{1}{3}P<sub>2</sub>$. 
 *
 * <p/>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 4/16/14
 * <p>Time: 7:53 PM
 *
 * @author Miguel Mu–oz
 */
//@javax.annotation.ParametersAreNonnullByDefault
@SuppressWarnings({"UseOfSystemOutOrSystemErr", "HardCodedStringLiteral", "HardcodedLineSeparator", "MagicNumber", "StringConcatenation"})
public final class PenroseMasters extends JPanel implements Pageable, Printable {

	// PRINT_SCALE is just for printing the coordinates of the points.
	private static final double PRINT_SCALE = 12.0;
	
//	private static boolean printDoublePage = false;

	private enum ShapeType { POINTS, BEZIER }

	@NotNull
	private final Canvas mCanvas;
	private final boolean isLegalSize;
	
//	private static ShapeType readShapeOption(String[] args) {
//		for (String s: args) {
//			if ("bezier".equals(s.toLowerCase())) {
//				return ShapeType.BEZIER;
//			}
//		}
//		return ShapeType.POINTS;
//	}
	
//	private static boolean readDoubleOption(String[] args) {
//		for (String s: args) {
//			if ("double".equals(s.toLowerCase())) {
//				return true;
//			}
//		}
//		return false;
//	}

	/**
	 * Print master shapes.
	 * <pre>
	 * Usage:
	 *   java PenroseMasters
	 *   java PenroseMasters bezier
	 *   java PenroseMasters double
	 *   java PenroseMasters bezier double
	 * </pre>
	 * 
	 * @param args user parameters
	 */
	public static void main(String[] args) {
//		if (args.length > 0) {
//			printDoublePage = true;
//		}
//		ShapeType shapeType = readShapeOption(args);
//		printDoublePage = readDoubleOption(args);
//		System.out.printf("Double: %b%n", printDoublePage);

		System.out.println("Petal:");
		ShapeType shapeType = ShapeType.BEZIER;
		@NotNull
		Shape[] petalShapes = makePetal(shapeType);
		JFrame frame = new JFrame("Penrose Petal");
		PenroseMasters pm = new PenroseMasters(petalShapes, 0, 0, false);
		frame.add(pm);
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		
		pm.installToolbar(frame);
		frame.setVisible(true);
		assert petalShapes[0] != null;
		printDataPoints(petalShapes[0]);

		System.out.println("\nLeaf:");
		
		Shape[] leafShapes = makeLeaf(shapeType);
		frame = new JFrame("Penrose Leaf");
		pm = new PenroseMasters(leafShapes, 150, 120, true);
		frame.add(pm);
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		
		pm.installToolbar(frame);
		frame.setVisible(true);
		assert leafShapes[0] != null;
		printDataPoints(leafShapes[0]);
		
		Shape[] bothLeafShapes = makeLeaf(shapeType);
		Shape leaf = bothLeafShapes[0];
		PathIterator iterator = leaf.getPathIterator(null);
		// Find the outer corners to rotate 180 degrees around the center.
		// First and third "Quad_to" will be the outer corners.
		int qCount = 0;
		double[] points = new double[6];
		Point2D p0 = new Point2D.Double(0.0, 0.0);
		Point2D p2 = new Point2D.Double();  // dummy value
		while (qCount < 4) {
			iterator.next();
			int segment = iterator.currentSegment(points);
			if (segment == PathIterator.SEG_QUADTO) {
				if (qCount == 3) {
					p0 = new Point2D.Double(points[2], points[3]);
				} else if (qCount == 1) {
					p2 = new Point2D.Double(points[2], points[3]);
				}
				qCount++;
			}
		}
		Point2D midPoint = new Point2D.Double((p0.getX() + p2.getX())/2.0, (p0.getY() + p2.getY())/2.0);
		AffineTransform toMdPt = AffineTransform.getTranslateInstance(-midPoint.getX(), -midPoint.getY());
		AffineTransform rotate = AffineTransform.getRotateInstance(Math.PI);
		AffineTransform toHome = AffineTransform.getTranslateInstance(midPoint.getX(), midPoint.getY());
		toHome.concatenate(rotate);
		toHome.concatenate(toMdPt);
//		leaf = toMdPt.createTransformedShape(leaf);
//		leaf = rotate.createTransformedShape(leaf);
		leaf = toHome.createTransformedShape(leaf);
		Shape[] bothLeaves = { leafShapes[0], leaf };
		
		frame = new JFrame("Leaf Symmetry");
		pm = new PenroseMasters(bothLeaves, 150, 120, true);
		frame.add(pm);
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		pm.installToolbar(frame);
		frame.setVisible(true);
	}

	private void installToolbar(@NotNull JFrame pFrame) {
		JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
		toolBar.setFloatable(false);
		JButton printButton = new JButton("Print");
		toolBar.add(printButton);
		printButton.addActionListener(e -> doPrint());
		pFrame.getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}
	
	@NotNull
	private static QList<PNumeric2.BezierPath> makeBasicPaths() {
		PNumeric2 pb = new PNumeric2();

		PNumeric2.BezierPath rightPath = pb.makeRightPath();
		PNumeric2.BezierPath leftPath = pb.makeLeftPath();
		
//		Path2D rightPath = pb.getRightPathForward();
//		Path2D leftPath = pb.getLeftPathForward();
//		Path2D rightPathRev = pb.getRightPathReverse();
//		Path2D leftPathRev = pb.getLeftPathReverse();
		
		return new LinkedQueue<>(Arrays.asList(rightPath, leftPath));
	}

	@NotNull
	private static QList<QList<Point2D>> makeBasicShapes() {
		PNumeric2 pb = new PNumeric2(); // pb is short for parabola. This has the numbers from a rotated parabola
		System.out.println("Numeric: " + pb.getClass());

		QList<Point2D> rightPointList = new LinkedQueue<>();
		QList<Point2D> dummyLeftPointList = new LinkedQueue<>();

		// Calculate the right side as a parabola
		final int segmentCount = 4;
		for (int ii = 0; ii <= segmentCount; ++ii) {

			
			// Rotated parabola:
			double x = -ii/(double)segmentCount;
			double y = pb.getYFromMappedX(x);
			rightPointList.add(new Point2D.Double(-x, y));
			y = pb.getYFromMappedX(-x);
			dummyLeftPointList.add(new Point2D.Double(x, y));
			
//			Point2D[] bezierPoints = pb.getBezier(0.0, 1.0);
		}
		
		for (Point2D pt: rightPointList) {
			System.out.printf("basicShape: (%7.4f, %7.4f)%n", pt.getX(), pt.getY());
		}
		
		// Calculate the left side as the arc of a circle
//		Point2D endPoint = rightPointList.getLast();
//		Point2D midPoint = new Point2D.Double(endPoint.getX()/2.0, endPoint.getY()/2.0);

		// We're looking for the point where the perpendicular bisector intersects the y axis.
		// That would be "b" in "y = mx + b". But the formula we have is "y-Y = m(x-X) where X and Y are
		// the coordinates of the mid point. So, solving for y, we get y = mx - mX + Y. 
		// This means b = Y - mX
		// Find where a line perpendicular to the midPoint intersects the Y axis:
//		double midRadiusSlope = -endPoint.getX()/endPoint.getY();
//		double b = midPoint.getY() - (midRadiusSlope * midPoint.getX());
//		double b = endPoint.getY() * 2.0;
		
//		// b is now also the radius of the circle.
//		// The circle formula is x^2 + (y-b)^2 = b^2
//		// So (y-b)^2 = b^2 - x^2;
//		// y = Math.sqrt(b^2 - x^2) + b
//		double maxAngle = 72;
//		double radiusSquared = b * b;
//		LinkedQueue<Point2D> leftPointList = new LinkedQueue<>();
//		for (int ii=0; ii<= segmentCount; ++ii) {
//			double theta = (maxAngle * ii) / segmentCount;
//
//			// x/r = sin(theta)
//			double x = - Math.sin(Math.toRadians(theta)) * b;
//			double y = b - Math.sqrt(radiusSquared - (x * x));
//			leftPointList.add(new Point2D.Double(x, y));
//		}
		
//		 listArray = Collection.toArray(rightPointList, leftPointList);
		QList<QList<Point2D>> resultList = new LinkedQueue<>();
		resultList.add(rightPointList);
		resultList.add(dummyLeftPointList);
		return resultList;
	}

//	QList<Path2D> basicPaths = makeBasicPaths;

	@NotNull
	private static Shape[] makePetal(ShapeType shapeType) {
		if (shapeType == ShapeType.BEZIER) {
			return makeBezierPetal();
		} else {
			QList<QList<Point2D>> basicShapes = makeBasicShapes();
			assert basicShapes.size() == 2;

			//noinspection ConstantConditions
			TileSegment segmentOne = TileSegment.buildFromPoints(basicShapes.get(0));
			TileSegment segmentTwo = segmentOne.reverse();
			//noinspection ConstantConditions
			TileSegment segmentThree = TileSegment.buildFromPoints(basicShapes.get(1));
			TileSegment segmentFour = segmentThree.reverse();

			segmentOne.rotateDegrees(36 - 90);
			Point2D p1 = getEndPoint(segmentOne);
			segmentOne.rotateAndJoin(90 - 36, segmentTwo);
//		print("CurrentShape:", segmentOne);
			Point2D p2 = getEndPoint(segmentOne);

			segmentOne.rotateAndJoin(36 - 270, segmentThree);
			Point2D p3 = getEndPoint(segmentOne);
			segmentOne.rotateAndJoin(270 - 36, segmentFour);
			Point2D p4 = getEndPoint(segmentOne);
			double width = p1.distance(p3);
			double length = p4.distance(p2);
			System.out.printf("Petal dimensions: %15.12f x %15.12f%n", width, length);
			return new Shape[]{segmentOne.getCurrentShape()};//, segmentFour.getCurrentShape()};
		}
	}

	@NotNull
	private static Shape[] makeLeaf(ShapeType shapeType) {
		if (shapeType == ShapeType.BEZIER) {
			return makeBezierLeaf();
		} else {
			QList<QList<Point2D>> basicShapes = makeBasicShapes();

			//noinspection ConstantConditions
			TileSegment segmentOne = TileSegment.buildFromPoints(basicShapes.get(0));
			//noinspection ConstantConditions
			TileSegment segmentTwo = TileSegment.buildFromPoints(basicShapes.get(1));
			TileSegment segmentThree = segmentOne.reverse();
			TileSegment segmentFour = segmentTwo.reverse();

			segmentOne.rotateDegrees(18 - 90);
			Point2D p1 = getEndPoint(segmentOne);
			segmentOne.rotateAndJoin(90 - 18, segmentTwo);
			// get End point to determine length:
			Point2D p2 = getEndPoint(segmentOne);
			segmentOne.rotateAndJoin(18 - 270, segmentFour);
			Point2D p3 = getEndPoint(segmentOne);
			segmentOne.rotateAndJoin(270 - 18, segmentThree);
			Point2D p4 = getEndPoint(segmentOne);
			double width = p1.distance(p3);
			double length = p4.distance(p2);
			System.out.printf("Leaf dimensions: %15.12f x %15.12f%n", width, length);
			return new Shape[]{segmentOne.getCurrentShape()};
		}
	}

	@NotNull
	private static Shape[] makeBezierPetal() {
		QList<PNumeric2.BezierPath> basicShapes = makeBasicPaths();
		@NotNull
		PNumeric2.BezierPath rightPath = basicShapes.get(0);
		@NotNull
		PNumeric2.BezierPath leftPath = basicShapes.get(1);

		@NotNull
		TileSegment segmentOne = TileSegment.buildFromPath(rightPath.getForwardPath(), rightPath.getStart(), rightPath.getEnd());
		@NotNull
		TileSegment segmentTwo = TileSegment.buildFromPath(rightPath.reverse().getForwardPath(), rightPath.getEnd(), rightPath.getStart());
		@NotNull
		TileSegment segmentThree = TileSegment.buildFromPath(leftPath.getForwardPath(), leftPath.getStart(), leftPath.getEnd());
		@NotNull
		TileSegment segmentFour = TileSegment.buildFromPath(leftPath.reverse().getForwardPath(), leftPath.getEnd(), leftPath.getStart());
		segmentOne.print("one");
		segmentTwo.print("two");
		segmentThree.print("three");
		segmentFour.print("four");

		segmentOne.rotateDegrees(36 - 90);
		Point2D p1 = getEndPoint(segmentOne);
		segmentOne.rotateAndJoin(90 - 36, segmentTwo);
//		print("CurrentShape:", segmentOne);
		Point2D p2 = getEndPoint(segmentOne);

		segmentOne.rotateAndJoin(36 - 270, segmentThree);
		Point2D p3 = getEndPoint(segmentOne);
		segmentOne.rotateAndJoin(270 - 36, segmentFour);
		Point2D p4 = getEndPoint(segmentOne);
		double width = p1.distance(p3);
		double length = p4.distance(p2);
		System.out.printf("%n%nPetal dimensions: %15.12f x %15.12f%n", width * PRINT_SCALE, length * PRINT_SCALE);
		AffineTransform printScale = getPrintScale();
		System.out.printf("printScale: %s%n", printScale);
		Shape petalShape = segmentOne.getCurrentShape();
		printShape("Petal Bezier Points", printScale.createTransformedShape(petalShape));
		return new Shape[]{petalShape};//, segmentFour.getCurrentShape()};
	}

	@NotNull
	private static Shape[] makeBezierLeaf() {
		QList<PNumeric2.BezierPath> basicShapes = makeBasicPaths();
		PNumeric2.BezierPath rightPath = basicShapes.get(0);
		PNumeric2.BezierPath leftPath = basicShapes.get(1);

		TileSegment segmentOne = TileSegment.buildFromPath(rightPath.getForwardPath(), rightPath.getStart(), rightPath.getEnd());
		TileSegment segmentTwo = TileSegment.buildFromPath(leftPath.getForwardPath(), leftPath.getStart(), leftPath.getEnd());
		TileSegment segmentThree = TileSegment.buildFromPath(rightPath.reverse().getForwardPath(), rightPath.getStart(), rightPath.getEnd());
		TileSegment segmentFour = TileSegment.buildFromPath(leftPath.reverse().getForwardPath(), leftPath.getStart(), leftPath.getEnd());
//		TileSegment segmentThree = segmentOne.reverse();
//		TileSegment segmentFour = segmentTwo.reverse();

		segmentOne.rotateDegrees(18 - 90);
		Point2D p1 = getEndPoint(segmentOne);
		segmentOne.rotateAndJoin(90 - 18, segmentTwo);
		// get End point to determine length:
		Point2D p2 = getEndPoint(segmentOne);
		segmentOne.rotateAndJoin(18 - 270, segmentFour);
		Point2D p3 = getEndPoint(segmentOne);
		segmentOne.rotateAndJoin(270 - 18, segmentThree);
		Point2D p4 = getEndPoint(segmentOne);
		double width = p1.distance(p3);
		double length = p4.distance(p2);
		System.out.printf("%n%nLeaf dimensions: %15.12f x %15.12f%n", width*PRINT_SCALE, length*PRINT_SCALE);
		AffineTransform printScale = getPrintScale();
		Shape leafShape = segmentOne.getCurrentShape();
		printShape("Leaf Bezier Point", printScale.createTransformedShape(leafShape));
		return new Shape[]{leafShape};
	}
	
	private static AffineTransform getPrintScale() {
		return AffineTransform.getScaleInstance(PRINT_SCALE, PRINT_SCALE);
	}

	@NotNull
	private static Point2D getEndPoint(TileSegment segment) {
		return segment.getEndPoint();
	}

	private static void printDataPoints(Shape shape) {
		String format = "(%15.12f, %15.12f)%n";
		System.out.printf(format, 0.0, 0.0);
		double[] data = new double[6];
		for (PathIterator iterator = shape.getPathIterator(null); !iterator.isDone(); iterator.next()) {
			int type = iterator.currentSegment(data);
			if (type == PathIterator.SEG_LINETO) {
				System.out.printf(format, data[0], data[1]);
			}
		}
	}

	@NotNull
	private static Shape makeShape(PathIterator pathIterator) {
		Path2D path = new Path2D.Double();
		path.append(pathIterator, false);
		return path;
	}

//	private static QList<Point2D> extractPoints(Shape shape) {

	@NotNull
	private static Path2D makePathFromList(List<Point2D> pointList) {
		Path2D path = new Path2D.Double();
		Point2D start = pointList.get(0);
		path.moveTo(start.getX(), start.getY());
		List<Point2D> subList = pointList.subList(1, pointList.size());
		for (Point2D point: subList) {
			path.lineTo(point.getX(), point.getY());
		}
//		path.moveTo(start.getX(), start.getY());
		return path;
	}

	@NotNull
	private static  <T> QList<T> makeInverted(QList<T> list) {
		ListIterator<T> iterator = list.listIterator(list.size());
		QList<T> inverted = new LinkedQueue<>();
		while (iterator.hasPrevious()) {
			inverted.add(iterator.previous());
		}
		return inverted;
	}

	private final Shape[] allShapes;
	
	private boolean repeat = false;
	
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	private PenroseMasters(@NotNull final Shape[] shapes, final int xDelta, final int yDelta, final boolean isLegalSize) {
		super(new BorderLayout());
		this.isLegalSize = isLegalSize;
		
		allShapes = shapes;

//		Shape pLeftCurve, final Shape pRightCurve) {
		mCanvas = new Canvas() {
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
				g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
				g2.setRenderingHint(RenderingHints.KEY_RESOLUTION_VARIANT, RenderingHints.VALUE_RESOLUTION_VARIANT_DPI_FIT);

				Dimension size = new Dimension((105*72)/10, 8*72); // Size is 10.5 by 8 inches, measured in points.
//				int scale = Math.min(size.width, size.height)/2;
//				if (printDoublePage) {
//					scale *= 1.5;
//					size.setSize(size.getWidth()*1.5, size.getHeight()*1.5);
//					g.translate((size.width /2)*145/160, size.height/50); // 25 works
//				} else {
				g2.translate((size.width / 2) + xDelta, (size.height / 20) + yDelta);
//				}
				int scale = 6*72; // 6 inches, in points. This is apparently half the width of a petal.
				System.out.println("Scale: " + scale);
				AffineTransform scaleTransform = AffineTransform.getScaleInstance(scale, scale);

				double bestAngle;
				if (isLegalSize) {
					bestAngle = Math.toDegrees(Math.atan(8.5/14.0)); // ~31 degrees
				} else {
					bestAngle = 36.0;
				}
				
				System.out.printf("Best Angle: %6.3f%n", bestAngle);
				drawOrigin(g2);
				Color shapeColor = black;

				// Always use the bounds of the first element, to ensure all shapes get the same translation.
				Rectangle2D bounds2D = allShapes[0].getBounds2D();
				for (Shape pLeftCurve : allShapes) {

					System.out.printf("Shape id %d from window %s%n", pLeftCurve.hashCode(), getWindowName(PenroseMasters.this)); // NON-NLS
					//					Shape pRightCurve = shapes[ii+1];
					final Shape leftCurve = scaleTransform.createTransformedShape(pLeftCurve);
//					final Shape rightCurve = scaleTransform.createTransformedShape(pRightCurve);
//				System.out.println("Shape of " + leftCurve.getClass());
					System.out.printf("Pre rotate size: %8.4f x %8.4f%n", bounds2D.getWidth(), bounds2D.getHeight());
					Point2D center = new Point2D.Double(bounds2D.getCenterX(), bounds2D.getCenterY());
					AffineTransform transform = AffineTransform.getTranslateInstance(center.getX(), center.getY());
					transform.rotate(Math.toRadians(bestAngle));
					transform.translate(-center.getX(), -center.getY());
					
					Shape rotatedShape = transform.createTransformedShape(leftCurve);
					Rectangle2D finalBounds = rotatedShape.getBounds2D();
					System.out.printf("Size: %6.4f x %6.4f%n", finalBounds.getWidth(), finalBounds.getHeight());
					System.out.printf("Bounds: %s%n", finalBounds); // NON-NLS

					Stroke stroke = new BasicStroke((0.0720f)); // one mill thick.
					g2.setStroke(stroke);
					g2.setColor(shapeColor);
					g2.draw(rotatedShape);
					shapeColor = Color.red;
					
//					g2.setColor(Color.red);
//					g2.draw(rightCurve);

//					// printing only:
//					Path2D leftPath = (Path2D) leftCurve;
//					PathIterator iterator = leftPath.getPathIterator(new AffineTransform());
//					System.out.println("");
//					double[] points = new double[6];
//					while (!iterator.isDone()) {
//						int type = iterator.currentSegment(points);
//						if (type != PathIterator.SEG_MOVETO) {
//							System.out.printf("[%15.12f, %15.12f]%n", points[0], points[1]);
//						}
//						iterator.next();
//					}
				}
			}

			@Override
			public void update(Graphics g) {
				paint(g);
			}

			public void drawOrigin(final Graphics2D g2) {
				Color savedColor = g2.getColor();
				g2.setColor(Color.red);
				int originSize = 10;
//				int originSize = originSize /2;
//				g2.drawLine(originSize, 0, -originSize, 0);
//				g2.drawLine(0, originSize, 0, -originSize);

				for (int ii = 0; ii < 10; ++ii) {
					int x = 100 * ii;
					g2.drawLine(x, originSize, x, -originSize);
					g2.drawLine(-x, originSize, -x, -originSize);
					//noinspection SuspiciousNameCombination,UnnecessaryLocalVariable
					int y = x;
					g2.drawLine(originSize, y, -originSize, y);
					g2.drawLine(originSize, -y, -originSize, -y);
				}

				g2.setColor(savedColor);
			}

		};
		mCanvas.setMinimumSize(new Dimension(100, 100));
		mCanvas.setPreferredSize(new Dimension(1400, 1000));
		
		int matte = 20;
		Border border = BorderFactory.createMatteBorder(matte, matte, matte, matte, getBackground());
		setBorder(border);

		add(mCanvas, BorderLayout.CENTER);
	}
	
	private static String getWindowName(JComponent component) {
		JFrame frame = (JFrame) component.getRootPane().getParent();
		return frame.getTitle();
	}

//		QList<Point2D> list = new LinkedQueue<>();
//		PathIterator iterator = shape.getPathIterator(null);
//		double[] points = new double[6];
//		while (!iterator.isDone()) {
//			iterator.currentSegment(points);
//			list.add(new Point2D.Double(points[0], points[1]));
//			iterator.next();
//		}
//		return list;
//	}
//		int tail = array.length;
//		int half = tail/2;
//		for (int ii=0; ii<half; ++ii) {
//			T swap = array[ii];
//			array[ii] = array[--tail];
//			array[tail] = swap;
//		}
//	}
//		QList<T> inverted = new LinkedQueue<>();
//		for (T t: list) {
//			inverted.addFirst(t);
//		}
//		return inverted;
//	}


	private void doPrint() {

		PrinterJob job = PrinterJob.getPrinterJob();
		
		// For some reason, this doesn't work...
		job.setPageable(this);
//		Rectangle bounds = allShapes[0].getBounds();
		// todo: I need to print both pages in a single call to doPrint(), so I can set them to the same scale.
//		int width = bounds.width;
		
		// I need to set the PageFormat here, instead.
		job.setPrintable(this, getPageFormat(0));
		boolean doPrint = job.printDialog();
		if (doPrint) {
			try {
				job.print();
			} catch (PrinterException pe) {
				pe.printStackTrace();
			}
		}
	}

	@Override // Pageable
	public int getNumberOfPages() {
		return 1;
	}

	private PageFormat pageFormat = null;
	
	@Override // Pageable
	public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
		if (pageFormat == null) {
			pageFormat = new PageFormat();
			Paper paper = pageFormat.getPaper();
			if (isLegalSize) {
				paper.setImageableArea(0.25 * 72, 0.25 * 72, 8.0 * 72, 13.5 * 72);
				paper.setSize(8.5*72, 14.0*72);
			} else {
				paper.setImageableArea(0.25 * 72, 0.25 * 72, 8.0 * 72, 10.5 * 72);
			}
			System.out.printf("Paper size: %4.1f x %4.1f%n", paper.getWidth()/72.0, paper.getHeight()/72.0);
			pageFormat.setPaper(paper);
			pageFormat.setOrientation(PageFormat.LANDSCAPE);
		}
		return pageFormat;
	}

	@Override // Pageable
	public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
		return this;
	}

	@Override // Printable
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
//				pageFormat.setOrientation(PageFormat.LANDSCAPE);
		// This prints out coordinates in points.
		System.out.printf("Page:  %8.3f x %8.3f%nPrint: %8.3f x %8.3f%n", pageFormat.getWidth(), pageFormat.getHeight(), pageFormat.getImageableWidth(), pageFormat.getImageableHeight());
		if (pageFormat.getOrientation() != PageFormat.LANDSCAPE) {
			System.out.println("Wrong orientation: " + pageFormat.getOrientation());
			System.out.println("Should be " + PageFormat.LANDSCAPE);
			return NO_SUCH_PAGE;
//			throw new PrinterException("Wrong orientation!");
		}
		if (pageIndex == 0) {
			mCanvas.print(graphics);
			return PAGE_EXISTS;
		} else {
			return NO_SUCH_PAGE;
		}
	}

//	public interface PenroseSegment {
//		double getLength();
//		PenroseSegment reverse();
//	}
	
	public static class TileSegment { //implements PenroseSegment {

//		private final Path2D path;
		private double length;
		@NotNull
		private Shape  shape;
		
		@NotNull
		public static TileSegment buildFromPoints(QList<Point2D> points) {
			Path2D path = makePathFromList(points);
			Point2D firstPoint = points.getFirst();
			Point2D lastPoint = points.getLast();
			double length = firstPoint.distance(lastPoint);

			AffineTransform transform = getRotationTransform(firstPoint, lastPoint);
			Shape shape = makeShape(path.getPathIterator(transform));
			return new TileSegment(path, length, shape);
		}

		@NotNull
		public static TileSegment buildFromPath(Path2D path, Point2D start, Point2D end) {
//			Path2D path = makePathFromList(points);

			AffineTransform transform = getRotationTransform(start, end);
			Shape shape = makeShape(path.getPathIterator(transform));
			return new TileSegment(path, start.distance(end), shape);
		}

		protected TileSegment(@SuppressWarnings("UnusedParameters") Path2D path, double length, @NotNull Shape shape) {
//			this.path = path;
			this.length = length;
			this.shape = shape;
		}
		
		public void print(String title) {
//			PathIterator itr = shape.getPathIterator(null);
			printShape(title, shape);
		}
		

		/**
		 * Returns the transform for the angle from the y axis.
		 * @param pFirstPoint The starting point
		 * @param pLastPoint The ending point
		 * @return An Affine Transform for rotation.
		 */
		protected static AffineTransform getRotationTransform(Point2D pFirstPoint, Point2D pLastPoint) {
			// tangent of angle from the Y axis
			double tanAngle = (pLastPoint.getX() - pFirstPoint.getX())/(pLastPoint.getY() - pFirstPoint.getY());
			return AffineTransform.getRotateInstance(Math.atan(tanAngle));
		}

//		@Override
		public double getLength() {
			return length;
		}

		@NotNull
		protected Shape getShape() { return shape;}

//		@Override
		
		// needs override for bezier
		@NotNull
		public TileSegment reverse() {
//			Shape copy = (Shape) new Path2D.Double(shape);
			AffineTransform flip = AffineTransform.getRotateInstance(Math.PI);
			Point2D delta = getVector();
			System.out.printf("Flipping vector: (%7.4f, %7.4f)%n", delta.getX(), delta.getY());
			flip.translate(-delta.getX(), -delta.getY());
			Shape flippedCopy = flip.createTransformedShape(shape);
			QList<Point2D> points = getCurrentPoints(flippedCopy);
//			QList<Point2D> points = getCurrentPoints(copy);
			QList<Point2D> revisedList = makeInverted(points);
//			Point2D firstPoint = revisedList.getFirst();
//			Point2D lastPoint = revisedList.getLast();
//			for (Point2D pt: revisedList) {
//				//noinspection ObjectToString
//				System.out.println("Reversed: " + pt);
//			}
			return TileSegment.buildFromPoints(revisedList);
		}

		@NotNull
		public Point2D getEndPoint() {
			System.out.printf("%nGetting end point for%n");
			print("");
			System.out.printf("End point: (%s)%n", PNumeric2.toFormat("%7.4f", getCurrentPoints(shape).getLast()));
			return getCurrentPoints(shape).getLast();
		}

		@NotNull
		private static QList<Point2D> getCurrentPoints(Shape shape) {QList<Point2D> points = new LinkedQueue<>();
			PathIterator iterator = shape.getPathIterator(new AffineTransform());
			double[] data = new double[6];
			while (!iterator.isDone()) {
				int type = iterator.currentSegment(data);
				points.add(new Point2D.Double(data[0], data[1]));
				if (type == PathIterator.SEG_QUADTO) {
					points.add(new Point2D.Double(data[2], data[3]));
				}
				iterator.next();
			}
			return points;
		}

		@NotNull
		public Point2D getVector() {
			QList<Point2D> points = getCurrentPoints(shape);
			Point2D firstPoint = points.getFirst();
			Point2D lastPoint = points.getLast();
			
			return new Point2D.Double(lastPoint.getX() - firstPoint.getX(), lastPoint.getY() - firstPoint.getY());
		}
		
		public void transform(AffineTransform transform) {
			shape = makeShape(shape.getPathIterator(transform));
		}
		
		public void rotate(double radians) {
			transform(AffineTransform.getRotateInstance(radians));
		}
		
		public void rotateDegrees(double degrees) {
			rotate(Math.toRadians(degrees));
		}

		@NotNull
		public Shape getCurrentShape() { return shape; }

		/**
		 * Adds the specified segment to the current shape. If the first point of the new segment matches the last
		 * point of the current segment, it gets skipped.
		 * @param newSegment The segment to add
		 *                   
		 * TODO: Rewrite the join code to not use points. It should only use Path Iterators.
		 */
		protected void join(TileSegment newSegment) { // needs override for bezier
			((Path2D)shape).append(newSegment.getCurrentShape(), false);
			
			// Calculate a new length
			QList<Point2D> currentPoints = TileSegment.getCurrentPoints(getCurrentShape());
			Point2D first = currentPoints.getFirst();
			Point2D last = currentPoints.getLast();
			length = first.distance(last);
		}
		
		public void rotateAndJoin(double angle, TileSegment newSegment) {
			newSegment.rotateDegrees(angle);
			AffineTransform transform = new AffineTransform();
			Point2D vector = getVector();
			transform.translate(vector.getX(), vector.getY());
			newSegment.transform(transform);
			join(newSegment);
		}
	}

	public static void printShape(String title, Shape shape) {
		PathIterator itr = shape.getPathIterator(null);
		System.out.println("\n\nTile Segment " + title);
		while (!itr.isDone()) {
			double[] ps = new double[6];
			int type = itr.currentSegment(ps);
			switch (type) {
				case PathIterator.SEG_QUADTO:
					System.out.printf("Ctr: (%15.12f, %15.12f, 0.0)%nP2:  (%15.12f, %15.12f, 0.0)%n", ps[0], ps[1], ps[2], ps[3]);
					break;
				case PathIterator.SEG_LINETO:
				case PathIterator.SEG_MOVETO:
					String txt = (type == PathIterator.SEG_LINETO) ? "lin" : "mov";
					System.out.printf("%s: (%15.12f, %15.12f, 0.0)%n", txt, ps[0], ps[1]);
					break;
				case PathIterator.SEG_CLOSE:
					System.out.println("close");
					break;
				default:
					throw new AssertionError("Missing option: " + type);
			}
			itr.next();
		}
		
	}

	public interface QList<T> extends Deque<T>, List<T> { }

	/**
	 * This gives us an interface for a LinkedList that gives us the API of both List and Deque. This way,
	 * I can avoid declaring my variables as LinkedList, and declare them as QList, which is an interface.
	 * @param <T>
	 */
	@SuppressWarnings({"ClassExtendsConcreteCollection", "CloneableClassWithoutClone"})
	public static class LinkedQueue<T> extends LinkedList<T> implements QList<T> {
		public LinkedQueue() {
			super();
		}

		public LinkedQueue(Collection<? extends T> c) {
			super(c);
		}
	}
}
