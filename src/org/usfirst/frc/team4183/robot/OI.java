package org.usfirst.frc.team4183.robot;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	public enum WhoIsDriving {
		DRIVER_A, DRIVER_B;  // Add more as needed
	}

	// Factory method...
	// In Robot, use this to create the correct OI subclass based on who's driving.
	public static OI oiFactory( WhoIsDriving driver) {

		switch( driver) {
		case DRIVER_A:
			return new OI_DriverA();

		case DRIVER_B:
			return new OI_DriverB();
			
		// Add more cases to handle each enum value
		}

		//  Must not get here
		return null;
	}


	// These are what Commands should use, ie: OI.btnLogicalName.onPressed().
	// The physical buttons are mapped to these logical buttons
	// in the constructor for the driver-specific OI subclass, e.g. OI_driverA.
	// FIXME complete this list, using actual meaningful logical names;
	// the ones here are just examples.
	public static ButtonEvent btnShoot; 
	public static ButtonEvent btnGrab;
	public static ButtonEvent burstIntoFlame;
	// etc for up to 14 buttons on each controller (might be fewer)

	// These are what Commands should use, ie: OI.axisFunctionName.get(). 
	// The physical axis are mapped to these logical axis
	// in the constructor for the driver-specific OK subclass, e.g. OI_driverA. 
	// FIXME complete this list, using actual meaningful logical names;
	// the ones here are just examples.
	public static Axis axisForward;
	public static Axis axisTurn;
	public static Axis axisSomeOtherThing;
	// etc for up to 6 axis on each controller (might be fewer)

	// End of public interface


	
	
	// Make it so nobody can instantiate class OI directly - forced to use factory method
	protected OI() {}

	// Joysticks for the 2 controllers
	protected static final Joystick controller0 = new Joystick(0);
	protected static final Joystick controller1 = new Joystick(1);


	// Create ButtonEvent for each physical button on each controller (14 for each controller)
	// This list is ugly but need never be touched again.
	// These are protected because NOT meant to be used externally;
	// only used by Driver-specific subclass to map physical buttons to logical buttons.
	// 14 on controller 0
	protected static final ButtonEvent b0Square = new ButtonEvent( controller0, PS4Constants.SQUARE.getValue());
	protected static final ButtonEvent  b0Cross = new ButtonEvent( controller0, PS4Constants.CROSS.getValue());
	protected static final ButtonEvent b0Circle = new ButtonEvent( controller0, PS4Constants.CIRCLE.getValue());
	protected static final ButtonEvent b0Triangle = new ButtonEvent(controller0, PS4Constants.TRIANGLE.getValue());
	protected static final ButtonEvent b0L1 = new ButtonEvent(controller0, PS4Constants.L1.getValue());
	protected static final ButtonEvent b0R1 = new ButtonEvent(controller0, PS4Constants.R1.getValue());
	protected static final ButtonEvent b0L2 = new ButtonEvent(controller0, PS4Constants.L2.getValue());
	protected static final ButtonEvent b0R2 = new ButtonEvent(controller0, PS4Constants.R2.getValue());
	protected static final ButtonEvent b0Share = new ButtonEvent(controller0, PS4Constants.SHARE.getValue());
	protected static final ButtonEvent b0Options = new ButtonEvent(controller0, PS4Constants.OPTIONS.getValue());
	protected static final ButtonEvent b0Lstick = new ButtonEvent(controller0, PS4Constants.L_STICK.getValue());
	protected static final ButtonEvent b0Rstick = new ButtonEvent(controller0, PS4Constants.R_STICK.getValue());
	protected static final ButtonEvent b0PS4 = new ButtonEvent(controller0, PS4Constants.PS4.getValue());
	protected static final ButtonEvent b0Trackpad = new ButtonEvent(controller0, PS4Constants.TRACKPAD.getValue());
	// 14 more for controller 1
	protected static final ButtonEvent b1Square = new ButtonEvent(controller1, PS4Constants.SQUARE.getValue());
	protected static final ButtonEvent b1Cross = new ButtonEvent(controller1, PS4Constants.CROSS.getValue());
	protected static final ButtonEvent b1Circle = new ButtonEvent(controller1, PS4Constants.CIRCLE.getValue());
	protected static final ButtonEvent b1Triangle = new ButtonEvent(controller1, PS4Constants.TRIANGLE.getValue());
	protected static final ButtonEvent b1L1 = new ButtonEvent(controller1, PS4Constants.L1.getValue());
	protected static final ButtonEvent b1R1 = new ButtonEvent(controller1, PS4Constants.R1.getValue());
	protected static final ButtonEvent b1L2 = new ButtonEvent(controller1, PS4Constants.L2.getValue());
	protected static final ButtonEvent b1R2 = new ButtonEvent(controller1, PS4Constants.R2.getValue());
	protected static final ButtonEvent b1Share = new ButtonEvent(controller1, PS4Constants.SHARE.getValue());
	protected static final ButtonEvent b1Options = new ButtonEvent(controller1, PS4Constants.OPTIONS.getValue());
	protected static final ButtonEvent b1Lstick = new ButtonEvent(controller1, PS4Constants.L_STICK.getValue());
	protected static final ButtonEvent b1Rstick = new ButtonEvent(controller1, PS4Constants.R_STICK.getValue());
	protected static final ButtonEvent b1PS4 = new ButtonEvent(controller1, PS4Constants.PS4.getValue());
	protected static final ButtonEvent b1Trackpad = new ButtonEvent(controller1, PS4Constants.TRACKPAD.getValue());


	// Create Axis for each physical axis on each controller.
	// This list is ugly but need never be touched again. 
	// These are protected because NOT meant to be used externally;
	// only used by Driver-specific subclass to map these physical axis to logical axis.
	// 6 on controller 0
	protected static final Axis a0LeftX = new Axis( controller0, PS4Constants.LEFT_STICK_X.getValue(), false);
	protected static final Axis a0LeftY = new Axis( controller0, PS4Constants.LEFT_STICK_Y.getValue(), true);
	protected static final Axis a0RightX = new Axis( controller0, PS4Constants.RIGHT_STICK_X.getValue(), false);
	protected static final Axis a0RightY = new Axis( controller0, PS4Constants.RIGHT_STICK_Y.getValue(), true);
	protected static final Axis a0L2 = new Axis( controller0, PS4Constants.L2_AXIS.getValue(), false);
	protected static final Axis a0R2 = new Axis( controller0, PS4Constants.R2_AXIS.getValue(), false);
	// 6 more for controller 1
	protected static final Axis a1LeftX = new Axis( controller1, PS4Constants.LEFT_STICK_X.getValue(), false);
	protected static final Axis a1LeftY = new Axis( controller1, PS4Constants.LEFT_STICK_Y.getValue(), true);
	protected static final Axis a1RightX = new Axis( controller1, PS4Constants.RIGHT_STICK_X.getValue(), false);
	protected static final Axis a1RightY = new Axis( controller1, PS4Constants.RIGHT_STICK_Y.getValue(), true);
	protected static final Axis a1L2 = new Axis( controller1, PS4Constants.L2_AXIS.getValue(), false);
	protected static final Axis a1R2 = new Axis( controller1, PS4Constants.R2_AXIS.getValue(), false);



	// Inner classes - defined & used only in here


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


	// Left these comments in for convenience...maybe delete them later??

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

class OI_DriverBase extends OI {
	
	OI_DriverBase() {
		
		// Do the default mappings for all buttons & axis.
		// This will save typing in the subclasses, 'cause hopefully all you'll have to do there
		// is make a few changes for the specific Driver. 

		// Default mapping for the buttons.
		// Each line has form: btnLogical = buttonPhysical.
		// FIXME These are just examples, actual logical names & correct mapping.
		btnShoot = b0Square;  // e.g. SQUARE button on cntrl 0 performs "shoot" function
		btnGrab = b0L2;       // and L2 button on cntrl 0 performs "grab" function
		burstIntoFlame = b0Cross;  // Wouldn't hit this one if I were you.
		// etc for all logical buttons that are used...
		// every one that's used must get set or there's going to be null ref exceptions!

		// Default mapping for the axis.
		// Each line has form: axisLogical = axisPhysical.
		// FIXME These are just examples, finish this with actual logical names & correct mapping.
		axisForward = a0LeftY;
		axisTurn = a0LeftX;	
		axisSomeOtherThing =  a0RightY;
		// etc for all logical axis that are used...
		// every one that's used must get set or there's going to be null ref exceptions!
	}
	
}

// Need to define one of these OI subclasses for each driver
class OI_DriverA extends OI_DriverBase {

	OI_DriverA() {

		// Remap any buttons that DIFFER from default mapping for Driver A.
		// Each line has form: btnLogical = buttonPhysical
		// These are just examples.
		btnShoot = b0Cross;  // e.g. CROSS button on cntrl 0 performs "shoot" function
		// etc for all logical buttons that are different from defaults

		// Remap any axis that DIFFER from default mapping for Driver A.
		// Each line has form: axisLogical = axisPhysical
		// These are just examples.
		axisForward = a0RightY;  // Forward & turn on Right stick for this driver
		axisTurn = a0RightX;
		// etc for all logical axis that are different from defaults
	}	
}

class OI_DriverB extends OI_DriverBase {
	
	OI_DriverB() {
		// FIXME empty for now, so defaults will apply
	}
}




