package com.mm.penrose.interactive;

import org.jetbrains.annotations.NotNull;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * Note: Bezier curves may be used to create parabolas:
 * http://math.stackexchange.com/questions/335226/convert-segment-of-parabola-to-quadratic-bezier-curve
 * <p>
 * Be sure to check the correction in the comment above the formula.
 * <p>
 * To rotate the parabola by 30 degrees, we get this formula:<br>
 * (k == sqrt(3)/2, k<sup>2</sup> = 3/4)<br>
 * (M == tan alpha = tan(36 degrees).<br>
 * 3M/4x<sup>2</sup> - KMxy + M/4y<sup>2</sup> -x/2 -Ky = 0<br>
 * <p>
 * (Ref: http://www.sinclair.edu/centers/mathlab/pub/findyourcourse/worksheets/Calculus/2ndDegreeEquation.pdf)
 * <p>
 * This becomes M((Kx)<sup>2</sup> - kxy + y<sup>2</sup>/4) -x/2 = 0<br>
 * We need to factor out the polynomial.<br>
 * <p>
 * x = (-b +- sqrt(b<sup>2</sup> - 4ac))/2a<br>
 * (k &plusmn; sqrt(k<sup>2</sup> - K))/2K<br>
 * Define B = sqrt(3 - 2sqrt(3))/2<br>
 * <p>
 * Roots are K+B, K-B<br>
 * <p>
 * <strong>Bezier Representation</strong><br>
 * <p>You can do this in two steps, first convert the parabola segment to a quadratic Bezier curve (with a single 
 * control point), then convert it to a cubic Bezier curve (with two control points).
 * <p>Let f(x)=Ax<sup>2</sup>+Bx+C be the parabola and let x<sub>1</sub> and x<sub>2</sub> be the edges of the segment on 
 * which the parabola is defined.</p>
 * <p>The derivative is f\u2032(x)=2Ax+B</p>
 * <p>
 * Then P<sub>1</sub>=(x<sub>1</sub>,f(x<sub>1</sub>)) and P<sub>2</sub>=(x<sub>2</sub>,f(x<sub>2</sub>)) 
 * are the Bezier curve start and end points, and 
 * <br>
 *   C=((x<sub>1</sub>+x<sub>2</sub>)/2, (f(x<sub>1</sub>)+f\u2032(x<sub>1</sub>)(x<sub>1</sub>+x<sub>2</sub>)/2)) is the 
 * control point for the quadratic Bezier curve.
 * 
 * <pre> <!-- Meant to be seen in JavaDocs. Don't mess with the spacing.) -->
 *        x<sub>1</sub> + x<sub>2</sub>                 x<sub>2</sub> - x<sub>1</sub>
 *   C =  -------, f(x<sub>1</sub>) + f'(x<sub>1</sub>) -------
 *           2                       2
 * </pre>
// * <html>
// * :<math>
// *   C = \tfrac{x_1 + x_2}{2}, f(x_1) + f'(x_1)\tfrac{x_2-x_1}{2}
// * </math>
 * </html>
 * <p>Now you can convert this quadratic Bezier curve to a cubic Bezier curve by defining two new control points.
 * <br>(This method does not use that last step. I don't know what the advantage is, if any. Speed is not an issue.)
 * 
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 4/16/14
 * <p>Time: 7:53 PM
 *
 * @author Miguel Mu\u00f1oz
 */
//@javax.annotation.ParametersAreNonnullByDefault
@SuppressWarnings({"UseOfSystemOutOrSystemErr", "HardCodedStringLiteral", "HardcodedLineSeparator", "MagicNumber", "StringConcatenation", "WeakerAccess"})
public final class PenroseMastersInteractive extends JPanel implements Pageable, Printable {

	// PRINT_SCALE is just for printing the coordinates of the points.
	private static final double PRINT_SCALE = 12.0;
	
	@NotNull
	private final Canvas mCanvas;
	private final boolean isLegalSize;
	
	private static Deque<PNumeric2.BezierPath> basicPaths;
	private static PNumeric2 penroseNumeric;
	private static final BoundedRangeModel rangeModel = new DefaultBoundedRangeModel(91, 0, 0, 150);

	/**
	 * Print master shapes.
	 * <pre>
	 * Usage:
	 *   java PenroseMasters      ! Use default value of 6 degrees
	 *   java PenroseMasters 7    ! Use 7 degrees
	 *   java PenroseMasters 6 n  ! use 6 degrees, don't draw the origin or axes.
	 * </pre>
	 * 
	 * @param args user parameters
	 */
	public static void main(String[] args) {
		double theta = 9.0705284575; // degrees
		boolean drawOrigin = true;

		if (args.length > 0) {
			try {
				theta = Double.parseDouble(args[0]);
			} catch (NumberFormatException e) {
				System.out.printf("Error reading value `%s`: %s. Using default value of %3.1f%n", args[0], e.getMessage(), theta);
			}
			
			if (args.length > 1) {
				drawOrigin = false;
			}
		}
//		System.out.printf("Rotation of %3.1f degrees%n", theta);
		makeDataFromAngle(theta);

//		System.out.println("Petal:");
		final ClosedShape petalShape = makeBezierPetal(basicPaths);
		Shape petalShapes = petalShape.getPath();
		JFrame frame = new JFrame(String.format("Penrose Petal: %3.1f degrees", theta));
		PenroseMastersInteractive pm = new PenroseMastersInteractive(petalShapes, 150, 0, false, drawOrigin, false, petalSupplier);
		frame.add(pm);
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		
		pm.installToolbar(frame);
		frame.setVisible(true);
		assert petalShapes != null;
		System.out.println("\nPetal:");
		printDataPoints(petalShapes);

//		System.out.println("\nLeaf:");

		final ClosedShape leafShape = makeBezierLeaf(basicPaths);
		Shape leafShapes = leafShape.getPath();
		frame = new JFrame(String.format("Penrose Leaf: %3.1f degrees", theta));
		pm = new PenroseMastersInteractive(leafShapes, 200, 120, true, drawOrigin, false, leafSupplier);
		frame.add(pm);
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		
		pm.installToolbar(frame);
		frame.setVisible(true);
		assert leafShapes != null;
		System.out.println("\nLeaf:");
		printDataPoints(leafShapes);
	}
	
	private static void makeDataFromAngle(double angleDegrees) {
		penroseNumeric = new PNumeric2(angleDegrees);
		basicPaths = makeBasicPaths();
		rangeModel.setValue((int) Math.round(angleDegrees * 10));
	}

	private static final Supplier<Shape> petalSupplier = () -> makeBezierPetal(basicPaths).getPath();
	private static final Supplier<Shape> leafSupplier = () -> makeBezierLeaf(basicPaths).getPath();

	private void installToolbar(@NotNull JFrame pFrame) {
		JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
		toolBar.setFloatable(false);
		JButton printButton = new JButton("Print");
		toolBar.add(printButton);
		printButton.addActionListener(e -> doPrint());
		pFrame.getContentPane().add(toolBar, BorderLayout.PAGE_START);
	}
	
	@NotNull
	private static Deque<PNumeric2.BezierPath> makeBasicPaths() {
		PNumeric2 pb = penroseNumeric;

		PNumeric2.BezierPath rightPath = pb.makeRightPath();
		PNumeric2.BezierPath leftPath = pb.makeLeftPath();
		
		return new LinkedList<>(Arrays.asList(rightPath, leftPath));
	}
	
	@NotNull
	static ClosedShape makeBezierPetal(Deque<PNumeric2.BezierPath> basicPaths) {
//		QList<PNumeric2.BezierPath> basicShapes = makeBasicPaths();
		PNumeric2.BezierPath rightPath = basicPaths.getFirst();
		PNumeric2.BezierPath leftPath = basicPaths.getLast(); // Only 2 shapes in the list anyway
		return makeBezierPetal(leftPath, rightPath);
	}

	@NotNull
	static ClosedShape makeBezierPetal(PNumeric2.BezierPath leftPath, PNumeric2.BezierPath rightPath) {

		TileSegment segmentOne = TileSegment.buildFromPath(rightPath.getForwardPath(), rightPath.getStart(), rightPath.getEnd());
		TileSegment segmentTwo = TileSegment.buildFromPath(rightPath.reverse().getForwardPath(), rightPath.getEnd(), rightPath.getStart());
		TileSegment segmentThree = TileSegment.buildFromPath(leftPath.getForwardPath(), leftPath.getStart(), leftPath.getEnd());
		TileSegment segmentFour = TileSegment.buildFromPath(leftPath.reverse().getForwardPath(), leftPath.getEnd(), leftPath.getStart());
//		segmentOne.printCurrentShape("one");
//		segmentTwo.printCurrentShape("two");
//		segmentThree.printCurrentShape("three");
//		segmentFour.printCurrentShape("four");

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
//		System.out.printf("%n%nPetal dimensions: %15.12f x %15.12f%n", width * PRINT_SCALE, length * PRINT_SCALE);
		AffineTransform printScale = getPrintScale();
//		System.out.printf("printScale: %s%n", printScale);
		Shape petalShape = segmentOne.getCurrentShape();
//		printShape("Petal Bezier Points", printScale.createTransformedShape(petalShape));
		
		return new ClosedShape(petalShape, segmentOne.getLength()); // All segments are the same length
	}

	@NotNull
	static ClosedShape makeBezierLeaf(Deque<PNumeric2.BezierPath> basicPaths) {
//		QList<PNumeric2.BezierPath> basicShapes = makeBasicPaths();
		PNumeric2.BezierPath rightPath = basicPaths.getFirst();
		PNumeric2.BezierPath leftPath = basicPaths.getLast(); // Only 2 shapes in the list anyway
		return makeBezierLeaf(leftPath, rightPath);
	}
	
	@NotNull
		static ClosedShape makeBezierLeaf(PNumeric2.BezierPath leftPath, PNumeric2.BezierPath rightPath) {

		TileSegment segmentOne = TileSegment.buildFromPath(rightPath.getForwardPath(), rightPath.getStart(), rightPath.getEnd());
		TileSegment segmentTwo = TileSegment.buildFromPath(leftPath.getForwardPath(), leftPath.getStart(), leftPath.getEnd());
		TileSegment segmentThree = TileSegment.buildFromPath(rightPath.reverse().getForwardPath(), rightPath.getStart(), rightPath.getEnd());
		TileSegment segmentFour = TileSegment.buildFromPath(leftPath.reverse().getForwardPath(), leftPath.getStart(), leftPath.getEnd());

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
//		System.out.printf("%n%nLeaf dimensions: %15.12f x %15.12f%n", width*PRINT_SCALE, length*PRINT_SCALE);
		AffineTransform printScale = getPrintScale();
		Shape leafShape = segmentOne.getCurrentShape();
//		printShape("Leaf Bezier Point", printScale.createTransformedShape(leafShape));
		
		return new ClosedShape(leafShape, segmentOne.getLength()); // All segments are the same length
	}
	
	private static AffineTransform getPrintScale() {
		return AffineTransform.getScaleInstance(PRINT_SCALE, PRINT_SCALE);
	}

	@NotNull
	private static Point2D getEndPoint(TileSegment segment) {
		return segment.getEndPoint();
	}

	public static void printDataPoints(Shape shape) {
		String pairFormat = "(%15.12f, %15.12f)";
		String onePair = "%11s:" + pairFormat + "%n";
		//noinspection MagicCharacter
		final char SPACE = ' ';
		double[] data = new double[6];
		for (PathIterator iterator = shape.getPathIterator(null); !iterator.isDone(); iterator.next()) {
			int type = iterator.currentSegment(data);
			switch (type) {
				case PathIterator.SEG_CLOSE:
					System.out.printf("SEG_CLOSE:%n");
					break;
				case PathIterator.SEG_CUBICTO:
					String threePair = "%11s:" + pairFormat + SPACE + pairFormat + SPACE + pairFormat + "%n";
					System.out.printf(threePair, "SEG_CUBICTO", data[0], data[1], data[2], data[3], data[4], data[5]);
					break;
				case PathIterator.SEG_LINETO:
					System.out.printf(onePair, "SEG_LINETO", data[0], data[1]);
					break;
				case PathIterator.SEG_MOVETO:
					System.out.printf(onePair, "SEG_MOVETO", data[0], data[1]);
					break;
				case PathIterator.SEG_QUADTO:
					String twoPair = "%11s:" + pairFormat + SPACE + pairFormat + "%n";
					System.out.printf(twoPair, "SEG_QUADTO", data[0], data[1], data[2], data[3]);
					break;
				default:
					throw new IllegalStateException("Unknown PathIterator Type: " + type);
			}
		}
	}

	@NotNull
	private static Shape makeShape(PathIterator pathIterator) {
		Path2D path = new Path2D.Double();
		path.append(pathIterator, false);
		return path;
	}

	@NotNull
	private static Path2D makePathFromList(Deque<Point2D> pointList) {
		Path2D path = new Path2D.Double();
		Iterator<Point2D> iterator = pointList.iterator();
		Point2D start = iterator.next();
		path.moveTo(start.getX(), start.getY());
		while (iterator.hasNext()) {
			Point2D point = iterator.next();
			path.lineTo(point.getX(), point.getY());
		}
		return path;
	}

	@NotNull
	private static <T> Deque<T> invert(Collection<T> list) {
		Deque<T> inverted = new LinkedList<>();
		for (T t: list) {
			inverted.addFirst(t);
		}
		return inverted;
	}

	@NotNull
	private Shape allShapes;

	/**
	 * Create a new PenroseMasters, using the specified shapes, to be drawn at the specified point, on the specified size
	 * of paper
	 * @param shapes The two shapes to draw.
	 * @param xDelta The x-offset, for drawing
	 * @param yDelta The y-offset, for drawing
	 * @param isLegalSize true for legal size paper, false otherwise.
	 */
	private PenroseMastersInteractive(@NotNull final Shape shapes, final int xDelta, final int yDelta, final boolean isLegalSize, boolean drawOrigin, boolean isForPrinting, Supplier<Shape> shapeSource) {
		super(new BorderLayout());
		this.isLegalSize = isLegalSize;
		
		allShapes = shapes;

		mCanvas = new Canvas() {
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
				g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

				Dimension size = new Dimension((105*72)/10, 8*72); // Size is 10.5 by 8 inches, measured in points.
				g.translate((size.width / 2) + xDelta, (size.height / 20) + yDelta);
//				System.out.println("Scale: " + scale);
				int scale = 6*72; // 6 inches, in points. This is half the length of a petal.
				AffineTransform scaleTransform = AffineTransform.getScaleInstance(scale, scale);

				Stroke stroke = new BasicStroke((0.0720f)); // one mill thick.
				g2.setStroke(stroke);
				if (drawOrigin) {
					drawOrigin(g2);
				}
				g2.setColor(Color.blue);


//				System.out.printf("Best Angle: %6.3f%n", bestAngle);

				Shape pLeftCurve = allShapes;
//				for (Shape pLeftCurve : allShapes) {
				//					Shape pRightCurve = shapes[ii+1];
				final Shape leftCurve = scaleTransform.createTransformedShape(pLeftCurve);
				Rectangle2D bounds2D = leftCurve.getBounds2D();
				Point2D center = new Point2D.Double(bounds2D.getCenterX(), bounds2D.getCenterY());
				AffineTransform transform = AffineTransform.getTranslateInstance(center.getX(), center.getY());

				double bestAngle = Math.toDegrees(StrictMath.atan(8.5 / 14.0)); // ~31 degrees
				transform.rotate(Math.toRadians(bestAngle));
					transform.translate(-center.getX(), -center.getY());
					
					Shape rotatedShape = transform.createTransformedShape(leftCurve);
					Rectangle2D finalBounds = rotatedShape.getBounds2D();
					
					if (isForPrinting) {
						g2.draw(rotatedShape);
					} else {
						g2.fill(rotatedShape);
					}

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
//				}
			}

			public void drawOrigin(final Graphics2D g2) {
				Color savedColor = g2.getColor();
				g2.setColor(Color.red);
				int originSize = 10;
//				int originSize = originSize /2;
//				g2.drawLine(originSize, 0, -originSize, 0);
//				g2.drawLine(0, originSize, 0, -originSize);
				
				for (int ii=0; ii<10; ++ii) {
					int x = 100*ii;
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
		
		if (!isForPrinting) {
			addSlider(shapeSource);
			
		}
	}
	
	private void addSlider(final Supplier<Shape> shapeSource) {
		JSlider slider = new JSlider(rangeModel);
		rangeModel.setValue(91);
		JTextField valueField = new JTextField(4);
		valueField.setEditable(false);
		slider.addChangeListener(e -> {
			int value = rangeModel.getValue();
			double theta = value/10.0;
			makeDataFromAngle(theta);
			allShapes = shapeSource.get();
			mCanvas.revalidate();
			mCanvas.repaint();
			showValue(valueField, theta);
		});
		
		JPanel sliderPanel = new JPanel(new BorderLayout());
		sliderPanel.add(slider, BorderLayout.CENTER);
		sliderPanel.add(valueField, BorderLayout.LINE_END);
		showValue(valueField, 9.0705284575);
		add(sliderPanel, BorderLayout.PAGE_END);
	}

	private void showValue(final JTextField valueField, final double theta) {
		valueField.setText(String.format("%4.1f", theta));
	}

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
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
		// This prints out coordinates in points.
		System.out.printf("Page:  %8.3f x %8.3f%nPrint: %8.3f x %8.3f%n", pageFormat.getWidth(), pageFormat.getHeight(), pageFormat.getImageableWidth(), pageFormat.getImageableHeight());
		if (pageFormat.getOrientation() != PageFormat.LANDSCAPE) {
			System.out.println("Wrong orientation: " + pageFormat.getOrientation());
			System.out.println("Should be " + PageFormat.LANDSCAPE);
			return NO_SUCH_PAGE;
		}
		if (pageIndex == 0) {
			mCanvas.print(graphics);
			return PAGE_EXISTS;
		} else {
			return NO_SUCH_PAGE;
		}
	}

	public static class TileSegment {

		private double length;
		@NotNull
		private Shape shape;
		
		@NotNull
		public static TileSegment buildFromPoints(Deque<Point2D> points) {
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

			AffineTransform transform = getRotationTransform(start, end);
			Shape shape = makeShape(path.getPathIterator(transform));
			return new TileSegment(path, start.distance(end), shape);
		}

		protected TileSegment(@SuppressWarnings("UnusedParameters") Path2D path, double length, @NotNull Shape shape) {
			this.length = length;
			this.shape = shape;
		}
		
		public void printCurrentShape(String title) {
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
			return AffineTransform.getRotateInstance(StrictMath.atan(tanAngle));
		}

		public double getLength() {
			return length;
		}

		@NotNull
		protected Shape getShape() { return shape;}

		// needs override for bezier
		@NotNull
		public TileSegment reverse() {
			AffineTransform flip = AffineTransform.getRotateInstance(Math.PI);
			Point2D delta = getVector();
//			System.out.printf("Flipping vector: (%7.4f, %7.4f)%n", delta.getX(), delta.getY());
			flip.translate(-delta.getX(), -delta.getY());
			Shape flippedCopy = flip.createTransformedShape(shape);
			Deque<Point2D> points = getCurrentPoints(flippedCopy);
			Deque<Point2D> revisedList = invert(points);
			return TileSegment.buildFromPoints(revisedList);
		}

		@NotNull
		public Point2D getEndPoint() {
			return getCurrentPoints(shape).getLast();
		}

		@NotNull
		private static Deque<Point2D> getCurrentPoints(Shape shape) {Deque<Point2D> points = new LinkedList<>();
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
			Deque<Point2D> points = getCurrentPoints(shape);
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
			((Path2D)shape).append(newSegment.getCurrentShape(), true);
			
			// Calculate a new length
			Deque<Point2D> currentPoints = TileSegment.getCurrentPoints(getCurrentShape());
			Point2D first = currentPoints.getFirst();
			Point2D last = currentPoints.getLast();
			length = first.distance(last);
		}
		
		public TileSegment rotateAndJoin(double angle, TileSegment newSegment) {
			newSegment.rotateDegrees(angle);
			AffineTransform transform = new AffineTransform();
			Point2D vector = getVector();
			transform.translate(vector.getX(), vector.getY());
			newSegment.transform(transform);
			join(newSegment);
			return newSegment;
		}
	}

	public static void printShape(String title, Shape shape) {
		PathIterator itr = shape.getPathIterator(null);
		System.out.println("\n\nTile Segment " + title);
		Deque<Point2D> ptList = new LinkedList<>();
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
					ptList.add(new Point2D.Double(ps[0], ps[1]));
					break;
				case PathIterator.SEG_CUBICTO: // not actually used here, but this may be useful elsewhere
					System.out.printf("cub: (%15.12f %15.12f) ((%15.12f, %15.12f) %n", ps[0], ps[1], ps[2], ps[3]);
					break;
				case PathIterator.SEG_CLOSE:
					System.out.println("close");
					break;
				default:
					throw new AssertionError("Missing option: " + type);
			}
			itr.next();
		}

		if (ptList.size() == 4) {
			Iterator<Point2D> ptItr = ptList.iterator();
			Point2D pt1 = ptItr.next();
			Point2D pt2 = ptItr.next();
			double segmentLength = pt1.distance(pt2);
			double cross1 = pt1.distance(ptItr.next());
			double cross2 = pt2.distance(ptItr.next());
			System.out.printf("CrossDistances:%n  %15.12f%n  %15.12f%n", cross1, cross2);
			System.out.printf("Segment: %15.12f%n", segmentLength);
		}
	}

	@SuppressWarnings("HardcodedFileSeparator")
	public static String asSvg(int width, Shape leafShape, int leafTop, Shape petalShape, int petalTop) {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
						"<svg\n" +
						"    xmlns=\"http://www.w3.org/2000/svg\"\n" +
						"    height=\"%d\" width=\"%d\">\n\n", width, width));
		String indent = "";
		appendShape(indent, builder, width, leafShape, leafTop, "leaf");
		appendShape(indent, builder, width, petalShape, petalTop, "petal");
		builder.append("</svg>\n");
		return builder.toString();
	}

	private static void appendShape(String priorIndent, StringBuilder builder, int width, Shape shape, int top, String name) {
		String indent = priorIndent + "  ";
		builder.append(indent).append(String.format(
				"<g transform=\"translate(%s, %s)\">\n", width / 2, top));
		appendMainShape(indent, builder, shape, name, false);
		appendMainShape(indent, builder, shape, name+"-ghost", true);
		builder.append(indent).append("</g>\n");
	}

	private static void appendMainShape(String priorIndent, StringBuilder builder, Shape shape, String name, boolean isGhost) {
		String indent = priorIndent + "  ";
		if (isGhost) {
			//noinspection MagicCharacter
			builder.append('\n').append(indent).append("<g style=\"fill:none;stroke:#000000;stroke-width:0.025\">");
		} else {
			builder.append(indent).append("<g style=\"fill:#4444FF\">\n");
		}
		appendShapeContents(indent, builder, shape, name, isGhost);
		builder.append(indent).append("\"/>\n");
		builder.append(indent).append("</g>\n");
	}

	private static void appendShapeContents(String priorIndent, StringBuilder builder, Shape shape, String name, boolean isGhost) {
		String indent = priorIndent + "  ";
		builder.append(indent).append(String.format("<path id=\"%s\" d=\"\n", name));
		builder.append(indent).append("M   0.000000000000  0.000000000000\n");
		PathIterator itr = shape.getPathIterator(null);
		while (!itr.isDone()) {
			double[] ps = new double[6];
			int type = itr.currentSegment(ps);
			switch (type) {
				case PathIterator.SEG_QUADTO:
					if (isGhost) {
						builder.append(indent).append(String.format("L %16.12f %16.12f%n", ps[2], ps[3]));
					} else {
						builder.append(indent).append(String.format("Q %16.12f %16.12f %16.12f %16.12f%n", ps[0], ps[1], ps[2], ps[3]));
					}
					break;
				case PathIterator.SEG_LINETO:
				case PathIterator.SEG_MOVETO:
				case PathIterator.SEG_CUBICTO: // not actually used here, but this may be useful elsewhere
				case PathIterator.SEG_CLOSE:
					break;
				default:
					throw new AssertionError("Missing option: " + type);
			}
			itr.next();
		}
	}

	static final class ClosedShape {
		private final Shape path;
		private final double segmentLength;
		
		private ClosedShape(Shape shape, double length) {
			path = shape;
			segmentLength = length;
		}

		public Shape getPath() {
			return path;
		}

		public double getSegmentLength() {
			return segmentLength;
		}
	}

	/**
	 * Find the angle in radians between lines ba and bc, where a, vertex, and b are three points.
	 * <p>
	 * Notes on Angles
	 * </p> <p>
	 * What is the angle at B of two lines BA and BC?
   * </p> <p>
   * Approach 1: Get both angles from horizontal, and subtract. <br>
   * Angles are BAh and BCh <br>
   * Angle BAh: sin(BAh) = Ay-By / ABd where ABd is the distance from A to B <br>
   * Angle BCh: sin(BCh) = Cy-By / BCd <br>
   * </p> <p>
   * Approach 2: Law of cosines: let a, vertex, &amp; b be the lengths of the sides opposite points A, B, &amp; C <br> 
   * b<sup>2</sup> = a<sup>2</sup> + vertex<sup>2</sup> -2bc cos C <br>
   * So, cos C = (a<sup>2</sup> + vertex<sup>2</sup> - b<sup>2</sup>)/2bc <br>
	 * </p>
	 * @param a point A
	 * @param vertex The vertex between VA and VB
	 * @param b point B
	 * @return The angle in radians between VA and VB
	 */
	public static double angle(Point2D.Double a, Point2D.Double vertex, Point2D.Double b) {
		double bSideSq = vertex.distanceSq(a);
		double aSideSq = vertex.distanceSq(b);
		double vSideSq = b.distanceSq(a);
		double bLen = Math.sqrt(bSideSq);
		double aLen = Math.sqrt(aSideSq);
		double cosineBeta = ((aSideSq + bSideSq) - vSideSq) / (2 * bLen * aLen);
		return StrictMath.acos(cosineBeta);
	}

	// This was clever, but ultimately unnecessary. There's a way to do this totally with Deque
//	public interface QList<T> extends Deque<T>, List<T> { }
//
//	/**
//	 * This gives us an interface for a LinkedList that gives us the API of both List and Deque. This way,
//	 * I can avoid declaring my variables as LinkedList, and declare them as QList, which is an interface, but I can
//	 * still call Deque.getFirst() as well as List.get(T) and List.sublist().
//	 * @param <T>
//	 */
//	@SuppressWarnings({"ClassExtendsConcreteCollection", "CloneableClassWithoutClone"})
//	public static class LinkedQueue<T> extends LinkedList<T> implements QList<T> {
//		public LinkedQueue() {
//			super();
//		}
//
//		public LinkedQueue(Collection<? extends T> c) {
//			super(c);
//		}
//	}
}
