package org.usfirst.frc.team4183.robot.commands;

import edu.wpi.first.wpilibj.GenericHID;

/**
 * Wrap a joystick button in this class,
 * then use it to get the current state of the button,
 * or to detect presses & releases
 * 
 * @author twilson
 */
public class ButtonEvent {
	
	  private final GenericHID m_joystick;
	  private final int m_buttonNum;
	  
	  boolean m_wasPressed;
	
	/**
	 * Constructor
	 * @param joystick The HID, i.e. from new Joystick(0)
	 * @param buttonNum The raw button number on the joystick
	 */
	ButtonEvent( GenericHID joystick, int buttonNum) {
		m_joystick = joystick;
		m_buttonNum = buttonNum;		
		m_wasPressed = isPressed();
	}
		
	/**
	 * Returns true if the button is currently pressed
	 */
	public boolean isPressed() {
	    return m_joystick.getRawButton(m_buttonNum);
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
