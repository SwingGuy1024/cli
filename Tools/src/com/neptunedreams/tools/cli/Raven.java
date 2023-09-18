package com.neptunedreams.tools.cli;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>For this to work, Java needs to be enabled as accessible in System Settings.<br>
 * See <a href="https://stackoverflow.com/questions/53103394/robot-mousemove-does-not-work-at-all-in-mac-os-x/76879702#76879702">Stack Overflow on Robot and Mac</a></p>
 * <p>Created by IntelliJ IDEA.
 * <p>Date: 8/6/23
 * <p>Time: 4:13 PM
 *
 * @author Miguel Mu–oz
 */
public final class Raven extends JPanel {

  private final JButton button = new JButton("Exit");
  private final AtomicLong lastMoveTime = new AtomicLong(System.currentTimeMillis());
  private final AtomicBoolean moveDetectionOn = new AtomicBoolean(true);
  public static final long delay = 120_000;
  public static final Robot robot = makeRobot();
  public static final long start = System.currentTimeMillis();
  private boolean processMouseEvent = true;
  private static final JLabel lastMovement = new JLabel("");
  private int yDelta = 0;

  public static void main(String[] args) {
    JFrame frame = new JFrame("Raven");
    frame.setLocationByPlatform(true);
    frame.setSize(500, 300);
    final Raven raven = new Raven();
    frame.add(raven, BorderLayout.CENTER);
    frame.add(lastMovement, BorderLayout.PAGE_END);
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    WindowListener windowListener = new WindowAdapter() {
      @Override
      public void windowOpened(WindowEvent e) {
        try {
          raven.startRobot();
        } catch (AWTException ex) {
          //noinspection ProhibitedExceptionThrown
          throw new RuntimeException(ex);
        }
        frame.removeWindowListener(this);
      }
    };
    frame.addWindowListener(windowListener);
    frame.setVisible(true);
  }
  
  private static Robot makeRobot() {
    try {
      return new Robot();
    } catch (AWTException e) {
      throw new IllegalStateException("Failed to create robot", e);
    }
  }

  private Raven() {
    super(new FlowLayout());
    ActionListener actionListener = e -> {
      Toolkit.getDefaultToolkit().beep();
      try {
        Thread.sleep(1000L);
      } catch (InterruptedException ex) { throw new IllegalStateException(ex); }
      System.exit(0);
    };
    button.addActionListener(actionListener);
    add(button);

    AWTEventListener awtEventListener = event -> {
      // Calling Robot.moveMouse() sends two mouse events for each movement, so we skip every other event.
      processMouseEvent = !processMouseEvent;
      final boolean detectionOn = moveDetectionOn.get();
//      System.out.printf("Movement. (%b)%n", detectionOn); // NON-NLS
      if (processMouseEvent) {
        if (detectionOn ) {
          lastMoveTime.set(System.currentTimeMillis());
          lastMovement.setText(String.valueOf((lastMoveTime.get() - start)/1000L));
        } else {
          System.out.printf("Turning Movement on from %s%n", moveDetectionOn); // NON-NLS
          moveDetectionOn.set(true);
        }
      }
      System.out.println();
    };
    Toolkit.getDefaultToolkit().addAWTEventListener(awtEventListener, AWTEvent.MOUSE_MOTION_EVENT_MASK);
  }

  private void startRobot() throws AWTException {
//    System.out.println("STARTING Robot");
    Runnable runner = () -> {
      yDelta = button.getHeight() / 2;
      //noinspection InfiniteLoopStatement
      while (true) {
        //noinspection CatchMayIgnoreException
        try {
          moveRight();
          sleep60();

          moveLeft();
          sleep60();
        } catch (InterruptedException | InvocationTargetException e) { }
      }
    };
    Thread robotThread = new Thread(runner, "Robot");
    robotThread.setDaemon(true);
    robotThread.start();
  }

  private void moveRight() throws InterruptedException, InvocationTargetException {
    final Point buttonLocationAfter = button.getLocationOnScreen();
    System.out.println("Moving Right\n");
    moveMouse(buttonLocationAfter.x + button.getWidth() + 5, buttonLocationAfter.y + yDelta);
  }

  private void moveLeft() throws InterruptedException, InvocationTargetException {
    final Point buttonLocationBefore = button.getLocationOnScreen();
    System.out.println("Moving Left\n");
    moveMouse(buttonLocationBefore.x - 5, buttonLocationBefore.y + yDelta);
  }

  private void moveMouse(int x, int y) throws InterruptedException, InvocationTargetException {
    long now = System.currentTimeMillis();
    final long sinceLastMove = now - lastMoveTime.get();
    System.out.print("Since last move: " + sinceLastMove);
    if (sinceLastMove > delay) {
      System.out.print(" Moving...");
      execute( () -> {
        moveDetectionOn.set(false);
        System.out.printf(" R.movement: %s%n", moveDetectionOn); // NON-NLS
        robot.mouseMove(x, y);
      });
    }
    System.out.println();
    System.out.println();
  }

  private static void sleep60() throws InterruptedException {
//    System.out.println("Sleeping...");
//    try {
      Thread.sleep(delay);
//    } catch (InterruptedException e) { }
  }
  
  private void execute(Runnable runner) throws InterruptedException, InvocationTargetException {
    SwingUtilities.invokeAndWait(runner);
  }
}
