package org.usfirst.frc.team4183.robot;


import java.util.HashSet;
import java.util.Set;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

// Pretty much everything in this class is static. 
// I just want to say that I find all-static classes to be bad style
// so I'm not crazy about this implementation.
// I wish I had made it a full-fledged singleton and forced everyone
// to access it as OI.instance().xxx (or Robot.oi.xxx). 
// It does save a little bit of typing when accessing the buttons
// (i.e. just OI.btnShoot instead of OI.instance().btnShoot or Robot.oi.btnShoot). 
public class OI {

	private static PhysicalController driverController = new PhysicalController( new Joystick(0));
	private static PhysicalController operatorController = new PhysicalController( new Joystick(1));
	

	//****************************
	// BUTTON DEFINITIONS
	//****************************
	
	public static LogicalButton btnToggleFrontCameraView = driverController.bCross;
	public static LogicalButton btnSelectFrontCam = driverController.bPovUp;
	public static LogicalButton btnSelectRearCam = driverController.bPovDown;
  
	// Drive
	public static LogicalButton btnLowSensitiveDrive = driverController.bR1;  
	public static LogicalButton btnInvertAxis = driverController.bR2;
	public static LogicalButton btnAlignLock = driverController.bL1;
	public static LogicalButton btnDriveLock = driverController.bL2;	
	public static LogicalButton btnAlignAssist = driverController.bTriangle;
	
	
	// Gear Handler 
	public static LogicalButton btnWaitForGear = operatorController.bCross;
	public static LogicalButton btnWaitForBalls = operatorController.bCircle;
	public static LogicalButton btnSpitGearA = operatorController.bL2;
	public static LogicalButton btnSpitGearB = operatorController.bTriangle;
	
	// Ball Manip 
	public static LogicalButton btnOpenHopper = operatorController.bPovLeft;
	public static LogicalButton btnCloseHopper = operatorController.bPovRight;
	public static LogicalButton btnIntakeOn = operatorController.bL1;
	public static LogicalButton btnShooterStart = operatorController.bR1;
	public static LogicalButton btnShoot = operatorController.bR2;
	public static LogicalButton btnUnjam = operatorController.bSquare;
	
	// Multi-subsystem (Gear & Ball)
	public static LogicalButton btnIdle = operatorController.bTrackpad;
	
	
	//****************************
	// AXIS DEFINITIONS
	//****************************
	public static LogicalAxis axisForward = driverController.aLeftY;
	public static LogicalAxis axisTurn = driverController.aRightX;
	public static LogicalAxis axisClimb = operatorController.aRightY;

	
	//****************************
	// Permanent SoftButtons (used for inter-SM communications)
	//****************************
	public static LogicalButton sbtnShake = new SoftButton();

	
	/**
	 * If your Command needs rising or falling edge detect on a button,
	 * use this method to get a ButtonEvent for that purpose.
	 * In initialize(), get your ButtonEvent: OI.ButtonEvent btnShoot = OI.getBtnEvt( OI.btnShoot).
	 * In isFinished(), test the ButtonEvent: btnShoot.onPressed() or btnShoot.onReleased().
	 * 
	 * @param btn The Logical button 
	 * @return The ButtonEvent that wraps the button
	 */
	public static ButtonEvent getBtnEvt( LogicalButton btn) { return new ButtonEvent(btn); }

		
	// Singleton method; use OI.instance() to get the OI instance.
	// Should do this in Robot.robotInit().
	public static OI instance() {
		if(inst == null)
			inst = new OI();
		return inst;		
	}
	private static OI inst;	
	private OI() {}
	
	
	// Represents all the physical buttons & axis on one controller.
	// Lotta ugly typing in here but fortunately no need to change anything. 
	private static class PhysicalController {

		@SuppressWarnings("unused")
		private final LogicalButton 
			bSquare, bCross, bCircle,  bTriangle, 
			bL1, bR1, bL2, bR2,
			bShare, bOptions, bLstick, bRstick,
			bPS4, bTrackpad,
			bPovUp, bPovRight, bPovDown, bPovLeft;
				
		@SuppressWarnings("unused")
		private final LogicalAxis 
			aLeftX, aLeftY, aRightX, aRightY, aL2, aR2;
		
		private PhysicalController( Joystick controller) {
			bSquare = new FlexButton(new PhysicalButton(controller, PS4Constants.SQUARE.getValue()));
			bCross = new FlexButton(new PhysicalButton(controller, PS4Constants.CROSS.getValue()));
			bCircle = new FlexButton(new PhysicalButton(controller, PS4Constants.CIRCLE.getValue()));
			bTriangle = new FlexButton(new PhysicalButton(controller, PS4Constants.TRIANGLE.getValue()));
			bL1 = new FlexButton(new PhysicalButton(controller, PS4Constants.L1.getValue()));
			bR1 = new FlexButton(new PhysicalButton(controller, PS4Constants.R1.getValue()));
			bL2 = new FlexButton(new PhysicalButton(controller, PS4Constants.L2.getValue()));
			bR2 = new FlexButton(new PhysicalButton(controller, PS4Constants.R2.getValue()));
			bShare = new FlexButton(new PhysicalButton(controller, PS4Constants.SHARE.getValue()));
			bOptions = new FlexButton(new PhysicalButton(controller, PS4Constants.OPTIONS.getValue()));
			bLstick = new FlexButton(new PhysicalButton(controller, PS4Constants.L_STICK.getValue()));
			bRstick = new FlexButton(new PhysicalButton(controller, PS4Constants.R_STICK.getValue()));
			bPS4 = new FlexButton(new PhysicalButton(controller, PS4Constants.PS4.getValue()));
			bTrackpad = new FlexButton(new PhysicalButton(controller, PS4Constants.TRACKPAD.getValue()));
			bPovUp = new FlexButton(new PhysicalPovButton(controller, PhysicalPovButton.POV_BUTTON.UP));
			bPovRight = new FlexButton(new PhysicalPovButton(controller, PhysicalPovButton.POV_BUTTON.RIGHT));
			bPovDown = new FlexButton(new PhysicalPovButton(controller, PhysicalPovButton.POV_BUTTON.DOWN));
			bPovLeft = new FlexButton(new PhysicalPovButton(controller, PhysicalPovButton.POV_BUTTON.LEFT));

			aLeftX = new FlexAxis(new PhysicalAxis( controller, PS4Constants.LEFT_STICK_X.getValue(), false));
			aLeftY = new FlexAxis(new PhysicalAxis( controller, PS4Constants.LEFT_STICK_Y.getValue(), true));
			aRightX = new FlexAxis(new PhysicalAxis( controller, PS4Constants.RIGHT_STICK_X.getValue(), false));
			aRightY = new FlexAxis(new PhysicalAxis( controller, PS4Constants.RIGHT_STICK_Y.getValue(), true));
			aL2 = new FlexAxis(new PhysicalAxis( controller, PS4Constants.L2_AXIS.getValue(), false));
			aR2 = new FlexAxis(new PhysicalAxis( controller, PS4Constants.R2_AXIS.getValue(), false));
		}
	}
	
	// Flexor is a thing that behaves differently depending on Robot Mode,
	// and so needs to be informed of mode changes.
	private static abstract class Flexor {
		private Flexor() {
			flexorSet.add(this);
		}
		protected abstract void setDisabled();
		protected abstract void setTeleop();
		protected abstract void setAuto();
	}
	
	// Each Flexor adds itself to this Set so we can notify them appropriately
	private static Set<Flexor> flexorSet = new HashSet<>();
	
	public void setDisabled() {
		for( Flexor f : flexorSet)
			f.setDisabled();				
	}
	
	public void setTeleop() {
		for( Flexor f : flexorSet)
			f.setTeleop();		
	}
	
	public void setAuto() {
		for( Flexor f : flexorSet)
			f.setAuto();		
	}

	
	// Represents a generic button
	public static interface LogicalButton {
		public boolean get();
		public default void push() {}
		public default void release() {}
		public default void hit() { hit(300); }
		public default void hit( long msecs) {}  // "hit" means push, then release later
	}
	
	
	// FlexButton is a facade for NadaButton, PhysicalButton, or SoftButton
	// depending on mode Disabled, Teleop, Autonomous, respectively.
	private static class FlexButton extends Flexor implements LogicalButton {
		private LogicalButton teleopButton;
		private LogicalButton activeButton;
		
		private FlexButton( LogicalButton teleopButton) {
			this.teleopButton = teleopButton;
			activeButton = new NadaButton();
		}
		
		@Override
		protected void setDisabled() { activeButton = new NadaButton(); }
		@Override
		protected void setTeleop() { activeButton = teleopButton; }
		@Override
		protected void setAuto() { activeButton = new SoftButton(); }
		
		@Override
		public boolean get() { return activeButton.get(); }
		@Override
		public void push() { activeButton.push(); }
		@Override
		public void release() { activeButton.release(); }
		@Override
		public void hit( long msecs) { activeButton.hit( msecs); }
	}
	
	// NadaButton does nothing
	private static class NadaButton implements LogicalButton {
		@Override
		public boolean get() { return false; }
	}
	
	// A LogicalButton operated by software (for Autonomous)
	private static class SoftButton implements LogicalButton {
		volatile boolean state = false;
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
		private PhysicalButton( Joystick controller, int btnNum) { 
			btn = new JoystickButton( controller, btnNum); 
		}		
		private PhysicalButton( Button btn) {
			this.btn = btn;
		}
		@Override
		public boolean get() { return btn.get(); }
		
	}
	
	// Allows you to use a controller axis as a button.
	// Not used yet but could come in handy someday I guess.
	@SuppressWarnings("unused")
	private static class PhysicalAxisButton implements LogicalButton {
		private PhysicalAxis physAxis;
		private PhysicalAxisButton( Joystick controller, int axisNum, boolean invert) {
			physAxis = new PhysicalAxis( controller, axisNum, invert);
		}
		@Override
		public boolean get() { return physAxis.get() > 0.5; }
	}
	
	// Allows you to use a POV button as a button
	private static class PhysicalPovButton implements LogicalButton {		
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

		 // Returns true if the button is currently pressed
		public boolean isPressed() { return m_button.get(); }

		 // Returns true only once when the button is pressed
		 // (rising edge detect)
		public boolean onPressed() {	
			boolean rtn = false;
			boolean isPressedNow = isPressed();
			if( isPressedNow && !m_wasPressed)
				rtn = true;
			m_wasPressed = isPressedNow;
			return rtn;
		}

		 // Returns true only once when the button is released
		 // (falling edge detect)
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

	// FlexAxis is a facade for NadaAxis, PhysicalAxis, or SoftAxis
	// according to mode Disabled, Teleop, Autonomous respectively.
	private static class FlexAxis extends Flexor implements LogicalAxis {
		private LogicalAxis teleopAxis;
		private LogicalAxis activeAxis;

		private FlexAxis( LogicalAxis teleopAxis) {
			this.teleopAxis = teleopAxis;
			activeAxis = new NadaAxis();
		}
		
		@Override
		protected void setDisabled() { activeAxis = new NadaAxis(); }
		@Override
		protected void setTeleop() { activeAxis = teleopAxis; }
		@Override
		protected void setAuto() { activeAxis = new SoftAxis(); }
		
		@Override
		public double get() { return activeAxis.get(); }
		@Override
		public void set( double value) { activeAxis.set(value); }		
	}
	
	// NadaAxis does nothing
	private static class NadaAxis implements LogicalAxis {
		@Override
		public double get() { return 0.0; }
	}
	
	// An Axis operated by software (for Autonomous)
	private static class SoftAxis implements LogicalAxis {
		volatile double value = 0;
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

}



