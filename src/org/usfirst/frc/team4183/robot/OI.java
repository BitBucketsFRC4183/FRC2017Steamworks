package org.usfirst.frc.team4183.robot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	public enum Driver {
		JOE, SAM;  // TODO Put in actual names, add more as needed
	}
	
	public enum Operator {
		BILL, MIKE;  // TODO Put in actual names, add more as needed
	}

	// Factory methods...
	// In Robot, use this to get the OI instance.
	public static OI oiFactory( Driver driver, Operator operator) {
		return new OI( driver, operator);
	}
	
	public static OI oiFactory() {
		return new OI();
	}

	// Access to Buttons for Commands:

	// If your Command needs rising or falling edge detect on a button,
	// use this method to get a ButtonEvent for that purpose.
	// In initialize(), get your ButtonEvent: OI.ButtonEvent btnShoot = OI.getBtnEvt( OI.btnShoot).
	// In isFinished(), test the ButtonEven: btnShoot.onPressed() or btnShoot.onReleased().
	public static ButtonEvent getBtnEvt( Button btn) { return new ButtonEvent(btn); }

	// If your Command doesn't require edge detect on the button,
	// then you can look at one of these directly,
	// in isFinished(): OI.btnShoot.get().
	// TODO complete this list, using actual meaningful logical names;
	// the ones here are just examples.
	public static Button btnShoot; 
	public static Button btnGrab;
	public static Button btnBurstIntoFlame;
	// etc for up to 14 buttons on each controller (might be fewer)


	// Access to Axis for Commands:
	// In execute(), OI.axisForward.get(). 
	// TODO complete this list, using actual meaningful logical names;
	// the ones here are just examples.
	public static Axis axisForward;
	public static Axis axisTurn;
	public static Axis axisSomeOtherThing;
	// etc for up to 6 axis on each controller (might be fewer)

	// End of public interface


	private PhysicalController driverController, operatorController;

	// Protected so nobody can instantiate class OI directly - 
	// forced to use public factory method.
	protected OI() {
		driverController = new PhysicalController( new Joystick(0));
		operatorController = new PhysicalController( new Joystick(1));
		
		// Default mapping
		doDefaultMapping();		
	}
	
	protected OI( Driver driver, Operator operator) {
		// Create, setup default mapping
		this();
		
		// Override default mappings for particular driver.
		// Make sure to pass driverController!
		switch(driver) {
		case JOE:
			map_driver_Joe( driverController);
			break;
		case SAM:
			// Currently no remapping for Sam
			break;
		}
		
		// Override default mappings for particular operator
		// Make sure to pass operatorController!
		switch(operator) {
		case BILL:
			map_operator_Bill( operatorController);
			break;
		case MIKE:
			// Currently no remapping for Mike
			break;
		}		
	}

	// Person-specific mapping functions.
	
	// Example: remap driver controller for Joe as driver
	// TODO: change name of method for real driver name,
	// and of course put in actual desired mapping
	private void map_driver_Joe( PhysicalController controller) {
		// Change from default: btnShoot <- Cross, btnGrab <- Square
		btnShoot = controller.bCross;
		btnGrab = controller.bSquare;
	}
	
	// Example: remap operator controller for Mike as operator
	// TODO: change name of method for real operator name,
	// and of course put in actual desired mapping
	private void map_operator_Bill( PhysicalController controller) {
		// Change from default: btnBurstIntoFlames <- Circle
		btnBurstIntoFlame = controller.bCircle;
	}
	
	
	
	private void doDefaultMapping() {
		
		// Assign to EVERY logical button a physical button
		// TODO finish this list w/real logical button names & real default mapping
		btnShoot = driverController.bSquare;
		btnGrab = driverController.bCross;
		btnBurstIntoFlame = operatorController.bSquare;
		
		// Assign to EVERY logical axis a physical axis
		// TODO finish this list w/real logical axis names & real mapping
		axisForward = driverController.aLeftY;
		axisTurn = driverController.aLeftX;
		axisSomeOtherThing = operatorController.aL2;		
	}


	// Inner classes - defined & used only in here

	// Represents the physical buttons & axis on one controller
	private static class PhysicalController {

		@SuppressWarnings("unused")
		protected final Button 
			bSquare, bCross, bCircle,  bTriangle, 
			bL1, bR1, bL2, bR2,
			bShare, bOptions, bLstick, bRstick,
			bPS4, bTrackpad;

		@SuppressWarnings("unused")
		protected final Axis 
			aLeftX, aLeftY, aRightX, aRightY, aL2, aR2;

		PhysicalController( Joystick controller) {
			bSquare = new JoystickButton(controller, PS4Constants.SQUARE.getValue());
			bCross = new JoystickButton(controller, PS4Constants.CROSS.getValue());
			bCircle = new JoystickButton(controller, PS4Constants.CIRCLE.getValue());
			bTriangle = new JoystickButton(controller, PS4Constants.TRIANGLE.getValue());
			bL1 = new JoystickButton(controller, PS4Constants.L1.getValue());
			bR1 = new JoystickButton(controller, PS4Constants.R1.getValue());
			bL2 = new JoystickButton(controller, PS4Constants.L2.getValue());
			bR2 = new JoystickButton(controller, PS4Constants.R2.getValue());
			bShare = new JoystickButton(controller, PS4Constants.SHARE.getValue());
			bOptions = new JoystickButton(controller, PS4Constants.OPTIONS.getValue());
			bLstick = new JoystickButton(controller, PS4Constants.L_STICK.getValue());
			bRstick = new JoystickButton(controller, PS4Constants.R_STICK.getValue());
			bPS4 = new JoystickButton(controller, PS4Constants.PS4.getValue());
			bTrackpad = new JoystickButton(controller, PS4Constants.TRACKPAD.getValue());

			aLeftX = new Axis( controller, PS4Constants.LEFT_STICK_X.getValue(), false);
			aLeftY = new Axis( controller, PS4Constants.LEFT_STICK_Y.getValue(), true);
			aRightX = new Axis( controller, PS4Constants.RIGHT_STICK_X.getValue(), false);
			aRightY = new Axis( controller, PS4Constants.RIGHT_STICK_Y.getValue(), true);
			aL2 = new Axis( controller, PS4Constants.L2_AXIS.getValue(), false);
			aR2 = new Axis( controller, PS4Constants.R2_AXIS.getValue(), false);		
		}
	}


	// Represents logical axis; get() returns the axis value (-1..+1).
	protected static class Axis {
		Joystick controller;
		int axisNum;
		boolean invert;
		protected Axis( Joystick controller, int axisNum, boolean invert) {
			this.controller = controller;
			this.axisNum = axisNum;
			this.invert = invert;
		}

		/**
		 * 
		 * @return Axis value, [-1..1] (except the L2 & R2, they run from 0 to 1). 
		 */
		public double get() {
			return (invert ? -1.0 : 1.0 ) * controller.getRawAxis(axisNum);
		}
	}


	// Represents logical button, in convenient event-ized form for Commands to use.
	public static class ButtonEvent {

		private final Button m_button;
		boolean m_wasPressed;

		protected ButtonEvent( Button button) {
			m_button = button;
			m_wasPressed = isPressed();
		}

		protected ButtonEvent( Joystick cntrl, int buttonNum) {
			this( new JoystickButton( cntrl, buttonNum));		
		}

		/**
		 * Returns true if the button is currently pressed
		 */
		public boolean isPressed() {
			return m_button.get();
		}

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

	// Can use this to wrap a RawAxis, then it can also be used as a Button
	public static class JoystickAxisButton extends Button {
		private final Axis axis;

		protected JoystickAxisButton(Axis axis) {
			this.axis = axis;
		}

		@Override 
		public boolean get() {
			return axis.get() > 0.75;
		}
	}


	// Left these Template comments in for convenience... TODO delete them later

	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
}



