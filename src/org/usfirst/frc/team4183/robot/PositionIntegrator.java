package org.usfirst.frc.team4183.robot;

import org.usfirst.frc.team4183.robot.subsystems.DriveSubsystem;

public class PositionIntegrator implements Runnable
{

	NavxIMU imu;
	DriveSubsystem driveSubsystem;
	double heading;
	double posX;
	double posY;
	
	public PositionIntegrator(NavxIMU imu, DriveSubsystem driveSubsystem) {
		this.imu = imu;
		this.driveSubsystem = driveSubsystem;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void setPos(double posX, double posY, double heading) {
		this.heading = heading;
		this.posX = posX;
		this.posY = posY;
	}
	
	public double getX() {
		return posX;
	}
	
	public double getY() {
		return posY;
	}
	
	public double getHeading() {
		return heading;
	}
	

}
