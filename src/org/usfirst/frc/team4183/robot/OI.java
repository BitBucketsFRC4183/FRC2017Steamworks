package org.usfirst.frc.team4183.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	// Singleton method; use OI.instance() to get the OI instance.
	// Should do this in Robot.robotInit().
	private static OI inst;
	public static OI instance() {
		if(inst == null)
			inst = new OI();
		return inst;		
	}
	
	// Call on entry to Autonomous Mode to set up the Soft Buttons
	public void autonomousInit() {
		mode = OIMode.SOFT;
	}
	
	
	public enum Driver {
		DEFAULT, JOE, SAM;  // TODO Put in actual names, add more as needed
	}
	
	public enum Operator {
		DEFAULT, BILL, MIKE;  // TODO Put in actual names, add more as needed
	}
	
	public void teleopInit() {
		mode = OIMode.HARD;
	}

	public void remapDriverOperator( Driver driver, Operator operator) {
		
		// Begin by setting defaults
		doDefaultMapping();
		
		// Override default mappings for particular driver.
		// Make sure to pass driverController!
		switch(driver) {
		case JOE:
			mapDriver_Joe( driverController);
			break;
		case SAM:
			// Currently no remapping for Sam
			break;
		case DEFAULT:
			break;
		}
		
		// Override default mappings for particular operator
		// Make sure to pass operatorController!
		switch(operator) {
		case BILL:
			mapOperator_Bill( operatorController);
			break;
		case MIKE:
			// Currently no remapping for Mike
			break;
		case DEFAULT:
			break;
		}
	}

	
	/**
	 * If your Command needs rising or falling edge detect on a button,
	 * use this method to get a ButtonEvent for that purpose.
	 * In initialize(), get your ButtonEvent: OI.ButtonEvent btnShoot = OI.getBtnEvt( OI.btnShoot).
	 * In isFinished(), test the ButtonEven: btnShoot.onPressed() or btnShoot.onReleased().
	 * 
	 * @param btn The Logical button 
	 * @return The ButtonEvent that wraps the button
	 */
	public static ButtonEvent getBtnEvt( LogicalButton btn) { return new ButtonEvent(btn); }

	
	//****************************
	// LOGICAL BUTTON DEFINITIONS
	//****************************

	public static LogicalButton btnToggleFrontCameraView;
	public static LogicalButton btnSelectFrontCam;
	public static LogicalButton btnSelectRearCam;
  
	public static LogicalButton btnLowSensitiveDrive;  
	public static LogicalButton btnInvertAxis;
	public static LogicalButton btnAlignLock;
	public static LogicalButton btnDriveLock;	
	public static LogicalButton btnAlignAssist;
	
	public static LogicalButton btnClimbControl;
	
	// Gear/ball-loading functions
	public static LogicalButton btnWaitForGear;
	public static LogicalButton btnWaitForBalls;
	public static LogicalButton btnSpitGearA;
	public static LogicalButton btnSpitGearB;
	
	// Ball handling functions
	public static LogicalButton btnOpenCloseHopper;
	public static LogicalButton btnIntakeOn;
	public static LogicalButton btnShooterStart;
	public static LogicalButton btnShoot;
	
	public static LogicalButton btnIdle;
	
	
	//****************************
	// LOGICAL AXIS DEFINITIONS
	//****************************
	public static LogicalAxis axisForward;
	public static LogicalAxis axisTurn;


	
	
	// End of public interface

	

	private PhysicalController driverController, operatorController;
	
	private enum OIMode { HARD, SOFT }
	private static OIMode mode = OIMode.HARD;

	// Private so nobody can instantiate class OI directly - 
	// forced to use instance().
	private OI() {
		driverController = new PhysicalController( new Joystick(0));
		operatorController = new PhysicalController( new Joystick(1));
				
		// Set default control mapping
		doDefaultMapping();		
	}
	
	// Person-specific mapping functions.
	// Override Defaults here.
	
	// Example: remap driver controller for Joe as driver
	// TODO: change name of method for real driver name,
	// and of course put in actual desired mapping
	private void mapDriver_Joe( PhysicalController controller) {
	}
	
	// Example: remap operator controller for Mike as operator
	// TODO: change name of method for real operator name,
	// and of course put in actual desired mapping
	private void mapOperator_Bill( PhysicalController controller) {
	}
	
	
	// Default mapping of Physical to Logical button, axis
	private void doDefaultMapping() {
		
		// Assign to EVERY logical button a physical button
		// Assign to EVERY logical axis a physical axis

		// ****************
		// DRIVER CONTROLS Logical <- Physical
		// ****************
		btnToggleFrontCameraView = driverController.bCross;
		btnSelectFrontCam = driverController.bPovUp;
		btnSelectRearCam = driverController.bPovDown;
		
		btnLowSensitiveDrive = driverController.bR1;
		btnInvertAxis = driverController.bR2;	
		btnAlignLock = driverController.bL1;
		btnDriveLock = driverController.bL2;		
		btnAlignAssist = driverController.bTriangle;

		
		// ****************
		// OPERATOR CONTROLS Logical <- Physical
		// ****************		
		btnClimbControl = operatorController.bShare;
		
		btnWaitForGear = operatorController.bCross;
		btnWaitForBalls = operatorController.bCircle;
		btnSpitGearA = operatorController.bL2;
		btnSpitGearB = operatorController.bTriangle;
		
		btnOpenCloseHopper = operatorController.bPovLeft;
		btnIntakeOn = operatorController.bL1;
		btnShooterStart = operatorController.bR1;
		btnShoot = operatorController.bR2;
		
		btnIdle = operatorController.bTrackpad;		// Big easy button to make selected operator subs idle
				
		
		axisForward = driverController.aLeftY;
		axisTurn = driverController.aRightX;		
	}

	// Represents the physical buttons & axis on one controller
	private static class PhysicalController {

		@SuppressWarnings("unused")
		private final LogicalButton 
			bSquare, bCross, bCircle,  bTriangle, 
			bL1, bR1, bL2, bR2,
			bShare, bOptions, bLstick, bRstick,
			bPS4, bTrackpad;
		
		@SuppressWarnings("unused")
		private final LogicalButton
			bPovUp, bPovRight, bPovDown, bPovLeft;

		@SuppressWarnings("unused")
		private final LogicalAxis 
			aLeftX, aLeftY, aRightX, aRightY, aL2, aR2;
		
		private PhysicalController( Joystick controller) {
			bSquare = new LogicalButton( new PhysicalButton(controller, PS4Constants.SQUARE.getValue()));
			bCross = new LogicalButton( new PhysicalButton(controller, PS4Constants.CROSS.getValue()));
			bCircle = new LogicalButton( new PhysicalButton(controller, PS4Constants.CIRCLE.getValue()));
			bTriangle = new LogicalButton( new PhysicalButton(controller, PS4Constants.TRIANGLE.getValue()));
			bL1 = new LogicalButton( new PhysicalButton(controller, PS4Constants.L1.getValue()));
			bR1 = new LogicalButton( new PhysicalButton(controller, PS4Constants.R1.getValue()));
			bL2 = new LogicalButton( new PhysicalButton(controller, PS4Constants.L2.getValue()));
			bR2 = new LogicalButton( new PhysicalButton(controller, PS4Constants.R2.getValue()));
			bShare = new LogicalButton( new PhysicalButton(controller, PS4Constants.SHARE.getValue()));
			bOptions = new LogicalButton( new PhysicalButton(controller, PS4Constants.OPTIONS.getValue()));
			bLstick = new LogicalButton( new PhysicalButton(controller, PS4Constants.L_STICK.getValue()));
			bRstick = new LogicalButton( new PhysicalButton(controller, PS4Constants.R_STICK.getValue()));
			bPS4 = new LogicalButton( new PhysicalButton(controller, PS4Constants.PS4.getValue()));
			bTrackpad = new LogicalButton( new PhysicalButton(controller, PS4Constants.TRACKPAD.getValue()));
			bPovUp = new LogicalButton( new PhysicalPovButton(controller, PhysicalPovButton.POV_BUTTON.UP));
			bPovRight = new LogicalButton( new PhysicalPovButton(controller, PhysicalPovButton.POV_BUTTON.RIGHT));
			bPovDown = new LogicalButton( new PhysicalPovButton(controller, PhysicalPovButton.POV_BUTTON.DOWN));
			bPovLeft = new LogicalButton( new PhysicalPovButton(controller, PhysicalPovButton.POV_BUTTON.LEFT));

			aLeftX = new LogicalAxis( new PhysicalAxis( controller, PS4Constants.LEFT_STICK_X.getValue(), false));
			aLeftY = new LogicalAxis( new PhysicalAxis( controller, PS4Constants.LEFT_STICK_Y.getValue(), true));
			aRightX = new LogicalAxis( new PhysicalAxis( controller, PS4Constants.RIGHT_STICK_X.getValue(), false));
			aRightY = new LogicalAxis( new PhysicalAxis( controller, PS4Constants.RIGHT_STICK_Y.getValue(), true));
			aL2 = new LogicalAxis( new PhysicalAxis( controller, PS4Constants.L2_AXIS.getValue(), false));
			aR2 = new LogicalAxis( new PhysicalAxis( controller, PS4Constants.R2_AXIS.getValue(), false));
		}
	}

	
	public static class LogicalButton {
		SoftButton sb;
		IPhysicalButton pb;
		
		private LogicalButton( IPhysicalButton pb) {
			this.pb = pb;
			sb = new SoftButton();
		}
		private LogicalButton() {
			this(null);
		}
		
		public boolean get() { 			
			if( pb != null)
				return pb.get() || sb.get();
			else
				return sb.get();			
		}
		
		public void push() { sb.push(); }
		public void release() { sb.release(); }
		public void hit() { sb.hit(300); }
		public void hit(long msecs) { sb.hit(msecs); }		
	}
	
	// A button that can be operated by software
	private static class SoftButton {
		volatile boolean state;
		public boolean get() { return state; }
		public void push() { state = true; }
		public void release() { state = false; }
		public void hit( long msecs) {
			// push() this, 
			// then start a thread to release() after a short while
			push();
			new Thread() {
				public void run() {
					try {
						Thread.sleep(msecs);
					} catch (InterruptedException e) {}
					release();
				}
			}.start();
		}
	}
	
	private static interface IPhysicalButton {
		public boolean get();
	}
	
	// A button on a controller
	private static class PhysicalButton implements IPhysicalButton {
		Button btn;
		private PhysicalButton( Joystick controller, int btnNum) { 
			btn = new JoystickButton( controller, btnNum); 
		}		
		private PhysicalButton( Button btn) {
			this.btn = btn;
		}
		@Override
		public boolean get() { return btn.get(); }
		
	}
	
	// Allows you to use a controller axis as a button
	// Not used yet
	@SuppressWarnings("unused")
	private static class PhysicalAxisButton implements IPhysicalButton {
		private PhysicalAxis physAxis;
		private PhysicalAxisButton( Joystick controller, int axisNum, boolean invert) {
			physAxis = new PhysicalAxis( controller, axisNum, invert);
		}
		@Override
		public boolean get() { return physAxis.get() > 0.5; }
	}
	
	// Allows you to use a POV button as a button
	private static class PhysicalPovButton implements IPhysicalButton {		
		private Joystick controller;
		private POV_BUTTON whichPov;
		
		private enum POV_BUTTON { 
			UP(0), RIGHT(90), DOWN(180), LEFT(270);
			private int value;
			private POV_BUTTON(int value) {
				this.value = value;
			}
			private int getValue() { return value; }
		}
		
		private PhysicalPovButton( Joystick controller, POV_BUTTON whichPov) {
			this.controller = controller;
			this.whichPov = whichPov;
		}
		
		@Override
		public boolean get() { return controller.getPOV() == whichPov.getValue(); }		
	}
	
	// Wraps a LogicalButton & makes it easy to catch rising/falling edges
	public static class ButtonEvent {

		private final LogicalButton m_button;
		private boolean m_wasPressed;

		private ButtonEvent( LogicalButton button) {
			m_button = button;
			m_wasPressed = isPressed();
		}

		/**
		 * Returns true if the button is currently pressed
		 */
		public boolean isPressed() { return m_button.get(); }

		/**
		 * Returns true only once when the button is pressed
		 * (rising edge detect)
		 */
		public boolean onPressed() {	
			boolean rtn = false;

			boolean isPressedNow = isPressed();
			if( isPressedNow && !m_wasPressed)
				rtn = true;

			m_wasPressed = isPressedNow;
			return rtn;
		}

		/**
		 * Returns true only once when the button is released
		 * (falling edge detect)
		 */
		public boolean onReleased() {
			boolean rtn = false;

			boolean isPressedNow = isPressed();
			if( !isPressedNow && m_wasPressed)
				rtn = true;

			m_wasPressed = isPressedNow;
			return rtn;		
		}
	}
	
	
	// Represents a generic Axis
	public static class LogicalAxis {
		private final SoftAxis sa;
		private final PhysicalAxis pa;
		
		LogicalAxis( PhysicalAxis pa) {
			this.pa = pa;
			this.sa = new SoftAxis();
		}
		LogicalAxis() {
			this(null);
		}
		
		public double get() {
			if( mode == OIMode.HARD && pa != null)
				return pa.get();
			else
				return sa.get();
		}
		public void set( double value) { sa.set( value); }
	}
	
	// An Axis that can be operated by software
	private static class SoftAxis {
		volatile double value = 0;
		public double get() { return value; }
		public void set( double value) { this.value = value; }
	}
	
	
	// An Axis on a controller
	private static class PhysicalAxis {
		Joystick controller;
		int axisNum;
		boolean invert;
		protected PhysicalAxis( Joystick controller, int axisNum, boolean invert) {
			this.controller = controller;
			this.axisNum = axisNum;
			this.invert = invert;
		}

		public double get() {
			return (invert ? -1.0 : 1.0 ) * controller.getRawAxis(axisNum);
		}
	}
}



