package org.usfirst.frc.team4183.robot.subsystems;

import org.usfirst.frc.team4183.robot.commands.VisionSubsystem.Idle;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class VisionSubsystem extends Subsystem
{
	// Strings to place into BucketVision NetworkTable
	public static final String GEAR_MODE = "gear";
	public static final String BOILER_MODE = "boiler";	
	public static final String FRONT_CAM= "frontCam";
	public static final String REAR_CAM = "rearCam";
	
	public static final String FRONT_CAM_MODE = "FrontCamMode";
	public static final String CURRENT_CAM = "CurrentCam";
	
	private static String currentCam = FRONT_CAM;
	private static String currentFrontCamMode = GEAR_MODE;

	public static NetworkTable bvtable;

	public VisionSubsystem()
	{
		// Set up defaults 
		bvtable = NetworkTable.getTable("BucketVision");
		bvtable.putString(FRONT_CAM_MODE, currentFrontCamMode);
		bvtable.putString(CURRENT_CAM, currentCam);
		
	}
	
	public void setFrontCam()
	{
		currentCam = FRONT_CAM;
		bvtable.putString(CURRENT_CAM, currentCam);
	}

	public void setRearCam()
	{
		currentCam = REAR_CAM;
		bvtable.putString(CURRENT_CAM, currentCam);
	}
	
	public void setGearMode()
	{
		currentFrontCamMode = GEAR_MODE;
		bvtable.putString(FRONT_CAM_MODE, currentFrontCamMode);
	}
	
	public boolean isGearMode()
	{
		return (currentFrontCamMode.equals(GEAR_MODE));
	}
	
	public void setBoilerMode()
	{
		currentFrontCamMode = BOILER_MODE;
		bvtable.putString(FRONT_CAM_MODE, currentFrontCamMode);
	}

	public boolean isBoilerMode()
	{
		return (currentFrontCamMode.equals(BOILER_MODE));
	}

	@Override
	protected void initDefaultCommand() 
	{
		setDefaultCommand( new Idle());
	}

}
