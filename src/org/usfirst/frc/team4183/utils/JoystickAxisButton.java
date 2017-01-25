package org.usfirst.frc.team4183.utils;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;

public class JoystickAxisButton extends Button {
	private Joystick joystick;
	private int axisNum;
	private double sign;
	
	public JoystickAxisButton(Joystick joystick, int axisNum, boolean positive) {
		this.joystick = joystick;
		this.axisNum = axisNum;
		sign = positive ? 1.0 : -1.0;
	}
	
	@Override
	public boolean get() {
		return joystick.getRawAxis(axisNum)*sign > 0.75;
	}

}
