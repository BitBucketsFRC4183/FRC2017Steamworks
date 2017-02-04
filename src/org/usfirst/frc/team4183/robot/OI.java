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
		doAutonomousMapping();
	}
	
	public enum Driver {
		DEFAULT, JOE, SAM;  // TODO Put in actual names, add more as needed
	}
	
	public enum Operator {
		DEFAULT, BILL, MIKE;  // TODO Put in actual names, add more as needed
	}
	
	public void teleopInit( Driver driver, Operator operator) {
		
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

	public void teleopInit() {
		teleopInit(Driver.DEFAULT, Operator.DEFAULT);
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

	// If your Command doesn't require edge detect on the button,
	// then you can look at one of these directly,
	// in isFinished(): OI.btnShoot.get().
	// TODO complete this list, using the meaningful logical names.
	public static LogicalButton btnActivateDrive;
	public static LogicalButton btnClimbControl;
	public static LogicalButton btnWaitForGear;
	public static LogicalButton btnWaitForBalls;
	public static LogicalButton btnGearIdle;
	public static LogicalButton btnOpenGate;
	public static LogicalButton btnIntakeOn;
	public static LogicalButton btnShooterStart;
	public static LogicalButton btnBallIdle;
	// etc for up to 14 buttons on each controller (might be fewer)


	// Access to Axis for Commands:
	// In execute(), OI.axisForward.get(). 
	// TODO complete this list, using actual meaningful logical names.
	public static LogicalAxis axisForward;
	public static LogicalAxis axisTurn;
	// etc for up to 6 axis on each controller (might be fewer)


	// End of public interface

	

	private PhysicalController driverController, operatorController;

	// Private so nobody can instantiate class OI directly - 
	// forced to use instance().
	private OI() {
		driverController = new PhysicalController( new Joystick(0));
		operatorController = new PhysicalController( new Joystick(1));
		
		// Set default mapping
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
		btnClimbControl = controller.bShare;
	}
	
	
	// Mapping of Soft(ware) button/axis to Logical buttons & axis
	// TODO complete this
	private void doAutonomousMapping() {
		
		// Assign to EVERY logical button a soft button
		btnActivateDrive = new SoftButton();
		btnClimbControl = new SoftButton();
		btnWaitForGear = new SoftButton();
		btnWaitForBalls = new SoftButton();
		btnGearIdle = new SoftButton();
		btnOpenGate = new SoftButton();
		
		// Assign to EVERY logical axis a soft axis
		axisForward = new SoftAxis();
		axisTurn = new SoftAxis();
	}
	
	// Default mapping of Physical to Logical button, axis
	private void doDefaultMapping() {
		
		// Assign to EVERY logical button a physical button
		// TODO finish this list w/real logical button names & real default mapping
		// FIXME these mappings below NOT 1-to-1!
		btnActivateDrive = driverController.bCircle;
		btnClimbControl = operatorController.bShare;
		btnWaitForGear = operatorController.bCross;
		btnWaitForBalls = operatorController.bCircle;
		btnGearIdle = operatorController.bSquare;
		btnOpenGate = operatorController.bTriangle;
		btnIntakeOn = operatorController.bL1;
		btnShooterStart = operatorController.bR1;
		btnBallIdle = operatorController.bTrackpad;
		
		// Assign to EVERY logical axis a physical axis
		// TODO finish this list w/real logical axis names & real mapping
		axisForward = driverController.aLeftY;
		axisTurn = driverController.aLeftX;
	}

	// Represents the physical buttons & axis on one controller
	private static class PhysicalController {

		@SuppressWarnings("unused")
		private final PhysicalButton 
			bSquare, bCross, bCircle,  bTriangle, 
			bL1, bR1, bL2, bR2,
			bShare, bOptions, bLstick, bRstick,
			bPS4, bTrackpad;

		@SuppressWarnings("unused")
		private final PhysicalAxis 
			aLeftX, aLeftY, aRightX, aRightY, aL2, aR2;

		private PhysicalController( Joystick controller) {
			bSquare = new PhysicalButton(controller, PS4Constants.SQUARE.getValue());
			bCross = new PhysicalButton(controller, PS4Constants.CROSS.getValue());
			bCircle = new PhysicalButton(controller, PS4Constants.CIRCLE.getValue());
			bTriangle = new PhysicalButton(controller, PS4Constants.TRIANGLE.getValue());
			bL1 = new PhysicalButton(controller, PS4Constants.L1.getValue());
			bR1 = new PhysicalButton(controller, PS4Constants.R1.getValue());
			bL2 = new PhysicalButton(controller, PS4Constants.L2.getValue());
			bR2 = new PhysicalButton(controller, PS4Constants.R2.getValue());
			bShare = new PhysicalButton(controller, PS4Constants.SHARE.getValue());
			bOptions = new PhysicalButton(controller, PS4Constants.OPTIONS.getValue());
			bLstick = new PhysicalButton(controller, PS4Constants.L_STICK.getValue());
			bRstick = new PhysicalButton(controller, PS4Constants.R_STICK.getValue());
			bPS4 = new PhysicalButton(controller, PS4Constants.PS4.getValue());
			bTrackpad = new PhysicalButton(controller, PS4Constants.TRACKPAD.getValue());

			aLeftX = new PhysicalAxis( controller, PS4Constants.LEFT_STICK_X.getValue(), false);
			aLeftY = new PhysicalAxis( controller, PS4Constants.LEFT_STICK_Y.getValue(), true);
			aRightX = new PhysicalAxis( controller, PS4Constants.RIGHT_STICK_X.getValue(), false);
			aRightY = new PhysicalAxis( controller, PS4Constants.RIGHT_STICK_Y.getValue(), true);
			aL2 = new PhysicalAxis( controller, PS4Constants.L2_AXIS.getValue(), false);
			aR2 = new PhysicalAxis( controller, PS4Constants.R2_AXIS.getValue(), false);		
		}
	}

	// Represents a generic button
	public static interface LogicalButton {
		public boolean get();
		public default void push() {}
		public default void release() {}
		public default void hit() { hit(300); }
		public default void hit( long msecs) {}
	}
	
	// A button that can be operated by software
	private static class SoftButton implements LogicalButton {
		boolean state;
		@Override
		public boolean get() { return state; }
		@Override
		public void push() { state = true; }
		@Override
		public void release() { state = false; }
		@Override
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
	
	// A button on a controller
	private static class PhysicalButton implements LogicalButton {
		Button btn;
		PhysicalButton( Joystick controller, int btnNum) { 
			btn = new JoystickButton( controller, btnNum); 
		}		
		@Override
		public boolean get() { return btn.get(); }
	}
	
	
	// Wraps a LogicalButton & makes it easy to catch rising/falling edges
	public static class ButtonEvent {

		private final LogicalButton m_button;
		boolean m_wasPressed;

		protected ButtonEvent( LogicalButton button) {
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
	public static interface LogicalAxis {
		public double get();
		public default void set( double value) {}
	}
	
	// An Axis that can be operated by software
	private static class SoftAxis implements LogicalAxis {
		double value = 0;
		@Override
		public double get() { return value; }
		@Override
		public void set( double value) { this.value = value; }
	}
	
	
	// An Axis on a controller
	private static class PhysicalAxis implements LogicalAxis {
		Joystick controller;
		int axisNum;
		boolean invert;
		protected PhysicalAxis( Joystick controller, int axisNum, boolean invert) {
			this.controller = controller;
			this.axisNum = axisNum;
			this.invert = invert;
		}

		@Override
		public double get() {
			return (invert ? -1.0 : 1.0 ) * controller.getRawAxis(axisNum);
		}
	}


	// Can use this to wrap a LogicalAxis, then it can also be used as a Button
	// (not sure how useful?)
	public static class JoystickAxisButton extends Button {
		private final LogicalAxis axis;

		protected JoystickAxisButton(LogicalAxis axis) {
			this.axis = axis;
		}

		@Override 
		public boolean get() {
			return axis.get() > 0.5;
		}
	}
}



