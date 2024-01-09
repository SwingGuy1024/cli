package com.mm.penrose.interactive;

import org.jetbrains.annotations.NotNull;

//import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.function.DoubleFunction;

/**
 * This class is to solve the problem of fitting a rotated parabola to a penrose triangle. A "penrose triangle" is the
 * triangle with the biggest angle that can be made from two segments of the larger penrose rhombus.
 * <p>
 * A rotated parabola is described as follows:
 * <p>
 * Suppose theta is the angle (clockwise) by which we rotate the parabola around the origin. The parabola is of
 * the form y = x^2
 * <p>
 * Let S = sin(theta) <br>
 * Let C = cos(theta)
 * <p>
 * The parameterized formula is :
 * <p>
 * x = (t + alpha CS t^2 + S^2t)/C <br>
 * y = alpha C t^2 - St
 * <p>
 * We solve this numerically.
 * We want to find y, X, and deltaX such that:
 * <p>
 * B = f(X) <br>
 * y = F(X-deltaX) <br>
 * y = F(X+deltaX) <br>
 * alpha = tan(36 degrees) <br>
 * where <br>
 * (y-f(x))/(deltaX) = alpha
 * <p>
 * Prior to rotation, the formula are
 * x = t
 * y = alpha * t * t
 * 
 * Transformations:
 * <p>
 * x' = xC + yS <br>
 * y' = yC - xS <br>
 *   Where C = cos(theta) and S = sin(theta)
 * Starting with: <br>
 *   x = t <br>
 *   y = alpha t^2 <br>
 *     
 * We get x' = t C + alpha sin^2 theta
 * -----------
 * XPrime and YPrime are in the drawing plane
 * X and Y are in the un-rotated coordinate system.
 * 
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 7/7/14
 * <p>Time: 3:15 PM
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings({"HardCodedStringLiteral", "MagicNumber", "FieldCanBeLocal"})
//@ParametersAreNonnullByDefault
class PNumeric2 {
	// Alpha is really an arbitrary number, which determines the size of the parabola. I settled on this value because
	// I was also using it for another constant which specified the smaller angle of the bigger rhombus.
	private static final double alpha = StrictMath.tan(Math.toRadians(36));

	@NotNull
	private AffineTransform rawTransform; // = AffineTransform.getScaleInstance(1.0, 1.0);

	/**
	 * Returns the point at x, alpha x<sup>2</sup>, where alpha is the tangent of 36\u00b0 This is the small angle of the 
	 * leaf shape, and half the small angle of the petal shape. (Actually, the angles of the rhombuses on which the
	 * shapes are based.)
	 * @param x double value
	 * @return The specified point
	 */
	private static Point2D getPoint(double x) {
		return new Point2D.Double(x, alpha * x * x);
	}

	@NotNull
	private Point2D getPPrime(double x, AffineTransform transform) {
		assert !Double.isNaN(x);
		Point2D pt = getPoint(x);
		transform.transform(pt, pt);
		return pt;
	}
	

//	public static void main(String[] args) {
//		
//		JFrame frame = new JFrame("PenroseNumeric");
//		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//		double theta = 5.0; //	angle of rotation, degrees 
//
//
//		if (args.length > 0) {
//			try {
//				theta = Double.valueOf(args[0]);
//			} catch (NumberFormatException e) {
//				System.out.printf("Error reading value `%s`: %s. Using default value of %3.1f%n", args[0], e.getMessage(), theta);
//			}
//		}
//		final PNumeric2 penroseNumeric = new PNumeric2(theta);
//		Canvas canvas = new Canvas() {
//			@Override
//			public void paint(Graphics g) {
//				Graphics2D g2 = (Graphics2D) g;
//				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//				g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
//				g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//				AffineTransform savedTransform = g2.getTransform();
//
//				Dimension size = new Dimension((105 * 72) / 10, 8 * 72);
//				g.translate(size.width / 2, size.height / 10);
//				double scale = 300;
//				((Graphics2D) g).scale(scale, scale);
//
//				PathFactory factory = new BezierPathFactory();
//				Path2D t0Path = factory.makePath(penroseNumeric.tr0);
//
//				Path2D t1Path = factory.makePath(penroseNumeric.tr1, penroseNumeric.xStart, penroseNumeric.xEnd);
//
//				Point2D theMidPoint = penroseNumeric.midPoint;
//
//				Path2D sPath1 = factory.makePath(penroseNumeric.rawTransform, penroseNumeric.xStart, penroseNumeric.xMid);
//				Path2D sPath2 = factory.makePath(penroseNumeric.rawTransform, penroseNumeric.xMid, penroseNumeric.xEnd);
//
//				g2.setStroke(new BasicStroke((float)(1.0/scale)));
//				g2.setColor(Color.ORANGE);
//				g2.draw(t0Path);
//				g2.setColor(Color.red);
//				g2.draw(t1Path);
//
//				g2.setColor(Color.green);
//				g2.draw(sPath1);
//				g2.setColor(Color.blue);
//				g2.draw(sPath2);
//
//				// Draw the axes
//				g2.setColor(Color.lightGray);
//				g2.drawLine(-1, 0, 1, 0);
//				g2.drawLine(0, 0, 0, 1);
//
//				// draw the mid Point
//				double dst = 0.025;
//				Point2D loD1 = new Point2D.Double(theMidPoint.getX() - dst, theMidPoint.getY() - dst);
//				Point2D hiD1 = new Point2D.Double(theMidPoint.getX() + dst, theMidPoint.getY() + dst);
//				Point2D loD2 = new Point2D.Double(theMidPoint.getX() - dst, theMidPoint.getY() + dst);
//				Point2D hiD2 = new Point2D.Double(theMidPoint.getX() + dst, theMidPoint.getY() - dst);
//
//				Line2D line1 = new Line2D.Double(loD1, hiD1);
//				Line2D line2 = new Line2D.Double(loD2, hiD2);
//				g2.setColor(Color.gray);
//				g2.draw(line1);
//				g2.draw(line2);
//
//				g2.setTransform(savedTransform);
//			}
//
//			@Override
//			public void update(Graphics g) {
//				paint(g);
//			}
//		};
//		canvas.setMinimumSize(new Dimension(100, 100));
//		canvas.setPreferredSize(new Dimension(1400, 1000));
//		frame.add(canvas);
//		frame.pack();
//		frame.setVisible(true);
//
//		penroseNumeric.makeRightPath();
//		penroseNumeric.makeLeftPath();
//	}
	
//	public interface PathFactory {
//		Path2D makePath(AffineTransform transform);
//		Path2D makePath(AffineTransform transform, double xStart, double xEnd);
//	}

//	@SuppressWarnings("UnusedDeclaration")
//	private static class PathFactoryImpl implements PathFactory {
//		@Override
//		public Path2D makePath(AffineTransform transform) {
//			return makePath(transform, -1.0, 1.0);
//		}
//		
//		@Override
//		@NotNull
//		public Path2D makePath(AffineTransform transform, double xStart, double xEnd) {
//			Point2D start = getPoint(xStart);
//			transform.transform(start, start);
//			Path2D path = new Path2D.Double();
//			path.moveTo(start.getX(), start.getY());
//			int intervals = 16;
//			double deltaX = xEnd - xStart;
//			for (int ii = 1; ii <= intervals; ++ii) {
//				double x = ((deltaX * ii) / intervals) + xStart;
//				Point2D pt = getPoint(x);
//				transform.transform(pt, pt);
//				path.lineTo(pt.getX(), pt.getY());
//			}
//			return path;
//		}
//	}

////	@SuppressWarnings("UnusedDeclaration")
//	private static class BezierPathFactory implements PathFactory {
//		@Override
//		public Path2D makePath(AffineTransform transform) {
//			return makePath(transform, -1.0, 1.0);
//		}
//
//		@Override
//		@NotNull
//		public Path2D makePath(AffineTransform transform, double xStart, double xEnd) {
//			BezierPoints points = new BezierPoints(xStart, xEnd, alpha, 0, 0, transform);
//			Point2D start = new Point2D.Double();
//			transform.transform(points.getP1(), start);
//
//			Path2D path = new Path2D.Double();
//			path.moveTo(start.getX(), start.getY());
//
//			Point2D ctl = new Point2D.Double();
//			transform.transform(points.getCtl(), ctl);
//
//			Point2D end = new Point2D.Double();
//			transform.transform(points.getP2(), end);
//
//			path.quadTo(ctl.getX(), ctl.getY(), end.getX(), end.getY());
//			return path;
//		}
//	}
	

	@NotNull
	private final AffineTransform tr0;
	@NotNull
	private final AffineTransform tr1;
	private final double xStart;
	private final double xMid;
	private final double xEnd;
//	private final double length;
	@NotNull
	private final Point2D midPoint;

	/**
	 * Create an object that calculates the coordinates of the Bezier-curve control points. These values depend on how
	 * far the axis of the parabola is rotated from the short axis of the larger rhombus. This value is measured in 
	 * degrees
	 * @param theta Rotation angle, in degrees.
	 */
	PNumeric2(double theta) {
		rawTransform = AffineTransform.getRotateInstance(Math.toRadians(theta));
		final DoubleFunction<Double> findYPrime = x -> getPPrime(x, rawTransform).getY();

		/*
		 * Finds the value of x on the other side of the Y axis that produces
		 * the same yPrime that this x produces.
		 */
		final DoubleFunction<Double> findMatchingX = x -> {
			double leftYPrime = getPPrime(x, rawTransform).getY();
			return interpolate(findYPrime, leftYPrime, -x);
		};

		double rhombusAngle = StrictMath.tan(Math.toRadians(36.0));
		DoubleFunction<Double> fRatio = x -> ratio(x, findMatchingX, rawTransform);
		final double x1 = interpolate(fRatio, rhombusAngle, 1.0);
		final double x2 = findMatchingX.apply(x1);
		Point2D pp1 = getPPrime(x1, rawTransform);
		Point2D pp2 = getPPrime(x2, rawTransform);

		double midPrime = (pp2.getX() + pp1.getX()) / 2.0;
		double mid = reverseX(midPrime, midPrime, rawTransform);
		midPoint = getPPrime(mid, rawTransform);

		// Calculate the translation.
		assert fuzzyEquals(midPrime, midPoint.getX()) : String.format("Mid Mismatch: %30.27f != %30.27f", midPrime, midPoint.getX());

		tr0 = rawTransform;
		rawTransform = AffineTransform.getTranslateInstance(-midPoint.getX(), -midPoint.getY());
		rawTransform.concatenate(tr0);

		pp1 = getPPrime(x1, rawTransform );
		pp2 = getPPrime(x2, rawTransform);
		double xp1 = pp1.getX();
		double xp2 = pp2.getX();
		double scale = Math.abs(2 / (xp2 - xp1));

		tr1 = rawTransform;
		rawTransform = AffineTransform.getScaleInstance(scale, scale);
		rawTransform.concatenate(tr1);
		rawTransform = tr1;

		pp1 = getPPrime(x1, rawTransform);
		pp2 = getPPrime(x2, rawTransform);
		xp1 = pp1.getX();
		xp2 = pp2.getX();
		xStart = x1;
		xEnd = x2;
		xMid = reverseX((xp1 + xp2)/2.0, 0.0, rawTransform);
//		length = 0;
	}

//	@NotNull
//	static String toFormat(String fpFormat, Point2D point) {
//		String format = '(' + fpFormat + ", " + fpFormat + ')';
//		return String.format(format, point.getX(), point.getY());
//	}
	
//	/**
//	 * Gets the value of y from a "mapped" value of x. This class takes x values in the range of (-1, 1) and
//	 * maps them to values from x1 to x2, then determines the Y value for the mapped X value. Then it "unMaps" the y 
//	 * value before returning it.
//	 * <p>
//	 * TODO: Rewrite this to use a scaled and rotated transform. Verify that it returns the same results. 
//	 * TODO  Then use the transform to return transformed bezier control points. (YMultiplier = 1/XMultiplier,
//	 * TODO  so this looks feasible.
//	 * @param xPrime A number in the range from -1 to 1;
//	 * @return The value of y, after mapping the input to a range matching the x1 and x2 members of this instance.
//	 */
//	double getYFromMappedX(double xPrime) {
//		double x = reverseX(xPrime, xPrime, rawTransform);
//		return getPPrime(x, rawTransform).getY();
//	}
//
	private double ratio(double x1, DoubleFunction<Double> f, AffineTransform transform) {
		double x2 = f.apply(x1);
		Point2D pp1 = getPPrime(x1, transform);
		Point2D pp2 = getPPrime(x2, transform);
		double yP = pp1.getY();
		double yP2 = pp2.getY();
		assert fuzzyEquals(pp1.getY(), pp2.getY()) : String.format("Mismatch: %23.20f != %23.20f [%23.20f] for x (%6.3f - %6.3f)", yP, yP2, yP/yP2, x1, x2);
		
		double xPMid = (pp1.getX() + pp2.getX()) / 2;
		
		Point2D pPMid = getPPrime(reverseX(xPMid, xPMid, transform), transform);
		double deltaY = pp2.getY() - pPMid.getY();
		return Math.abs(deltaY/(pp2.getX()-xPMid));
	}
	
	private static boolean fuzzyEquals(double a, double b) {
		final double delta = 0.0000000000001;
		if (a == 0.0) {
			return Math.abs(b) < delta;
		}
		if (b == 0.0) {
			return Math.abs(a) < delta;
		}
		double ratio = a / b;

		final double lowR = 1.0-delta;
		final double highR = 1.0+delta;
		return (ratio > lowR) && (ratio <= highR);
	}
	
	private double reverseX(double expected, double startingX, AffineTransform transform) {
		DoubleFunction<Double> f = x -> getPPrime(x, transform).getX();
		return interpolate(f, expected, startingX);
	}

	/**
	 * Returns the best value for X that returns the value expectedY after passing X to the function
	 * specified in f. In essence, this returns the inverse function of X.
	 * <p>
	 * In tests, this converges in 5 to 7 iterations every time.
	 * @param f The function to reverse.
	 * @param expectedY The value of Y that you are expecting.
	 * @param startingX A reasonable starting point for the X you are looking for.
	 * @return A value for x such that f(x) returns expectedY
	 */
	@SuppressWarnings("FloatingPointEquality")
	public static double interpolate(DoubleFunction<Double> f, double expectedY, double startingX) {
//		System.out.println("Interpolate: t = [" + expectedY + ']');
		double guess = startingX;
		double previousX = guess + 0.1;
		double previousY = f.apply(previousX);
		double prev2X = previousX;
		while ((guess != previousX) && (guess != prev2X)) {
			double newY = f.apply(guess);
			if ((newY == expectedY) || (newY == previousY) || fuzzyEquals(newY, expectedY)) {
				break;
			}
			double slope = (newY - previousY) / (guess - previousX);
			double nextGuess = ((expectedY - newY) / slope) + guess;
			// ---
			prev2X = previousX;
			previousX = guess;
			previousY = newY;
			guess = nextGuess;
		}
		return guess;
	}
	
//	@FunctionalInterface
//	public interface Function {
//		double apply(double x);
//	}
//
	// Formulas taken from 
	// http://math.stackexchange.com/questions/335226/convert-segment-of-parabola-to-quadratic-bezier-curve
	public static class BezierPoints {
		@NotNull
		private final Point2D p1;
		@NotNull
		private final Point2D p2;
		@NotNull
		private final Point2D ctl;
		
		private final double length;

		/**
		 * Generate Bezier end points and a control point for a bezier parabola segment from x1 to x2, with formula:
		 * <pre>    y = AX<sup>2</sup> + Bx + C</pre>
		 *
		 * @param x1 Starting x of segment
		 * @param x2 Ending x of segment
		 * @param A  A from parabola formula
		 * @param B  B from parabola formula
		 * @param C  C from parabola formula
		 */
		BezierPoints(double x1, double x2, double A, double B, double C, AffineTransform t) {
			p1 = new Point2D.Double(x1, f(x1, A, B, C));
			p2 = new Point2D.Double(x2, f(x2, A, B, C));
			ctl = new Point2D.Double((x1 + x2) / 2, f(x1, A, B, C) + (fPrime(x1, A, B) * ((x2 - x1) / 2)));
			t.transform(p1, p1);
			t.transform(p2, p2);
			t.transform(ctl, ctl);
			length = p1.distance(p2);
		}

		private BezierPoints(@NotNull Point2D p1, @NotNull Point2D ctl, @NotNull Point2D p2) {
			this.p1 = p1;
			this.ctl = ctl;
			this.p2 = p2;
			length = p1.distance(p2);
		}

		private double f(double x, double A, double B, double C) { return (A * x * x) + (B * x) + C; }

		private double fPrime(double x, double A, double B) { return (2 * A * x) + B; }

		@NotNull
		Point2D getP1() { return p1; }

		@NotNull
		Point2D getP2() { return p2; }

		@NotNull
		Point2D getCtl() { return ctl; }

		public double getLength() { return length; }

		@NotNull
		BezierPoints reverse() {
			AffineTransform flip = AffineTransform.getRotateInstance(Math.PI);
			Point2D delta = getVector(getP2(), getP1());
			flip.translate(delta.getX(), delta.getY());

			Point2D pp1 = new Point2D.Double();
			flip.transform(getP2(), pp1);
			Point2D pp2 = new Point2D.Double();
			flip.transform(getP1(), pp2);
			Point2D ct = new Point2D.Double();
			flip.transform(getCtl(), ct);

			return new BezierPoints(pp1, ct, pp2);
		}
	}

	BezierPath makeLeftPath() {
		BezierPoints points = new BezierPoints(xMid, xEnd, alpha, 0, 0, rawTransform);
		return new BezierPath(points);
	}
	
	BezierPath makeRightPath() {
		BezierPoints points = new BezierPoints(xMid, xStart, alpha, 0.0, 0.0, rawTransform);
		return new BezierPath(points);
	}
	
	public static class BezierPath {
		@NotNull
		private final BezierPoints points;
		@NotNull
		private final Path2D forwardPath;
		private final double length;
		@NotNull
		private final Point2D start;
		@NotNull
		private final Point2D ctl;
		@NotNull
		private final Point2D end;
		
		BezierPath(BezierPoints rawPoints) {
			points = rawPoints;
			start = rawPoints.getP1();
			
			end = rawPoints.getP2();

			ctl = rawPoints.getCtl();

			forwardPath = new Path2D.Double();
			forwardPath.moveTo(start.getX(), start.getY());
			forwardPath.quadTo(ctl.getX(), ctl.getY(), end.getX(), end.getY());
			
			length = start.distance(end);
		}
		
		@NotNull
		Path2D getForwardPath() { return forwardPath; }

		public double getLength() {
			return length;
		}

		@NotNull
		Point2D getStart() { return start; }

		@NotNull
		Point2D getEnd() { return end; }
		
		@NotNull
		Point2D getCtl() { return ctl; }

		@NotNull
		BezierPath reverse() {
			return new BezierPath(points.reverse());
		}
	}

	private static Point2D getVector(Point2D start, Point2D end) {
		return new Point2D.Double(end.getX() - start.getX(), end.getY() - start.getY());
	}
}
