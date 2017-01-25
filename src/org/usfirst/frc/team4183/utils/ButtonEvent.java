package org.usfirst.frc.team4183.utils;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * Wrap a joystick button in this class,
 * then use it to get the current state of the button,
 * or to detect presses & releases
 * 
 * @author twilson
 */
public class ButtonEvent {
	
	private final Button m_button;
	boolean m_wasPressed;
	
	/**
	 * Constructor
	 * @param button The Button to wrap
	 */
	public ButtonEvent( Button button) {
		m_button = button;
		m_wasPressed = isPressed();
	}

	/**
	 * Constructor
	 * @param joystick The HID, i.e a Joystick
	 * @param buttonNum The raw button number on the joystick
	 */
	public ButtonEvent( GenericHID joystick, int buttonNum) {
		m_button = new JoystickButton( joystick, buttonNum);
		m_wasPressed = isPressed();
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
