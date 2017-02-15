package org.usfirst.frc.team4183.robot.subsystems;

import org.usfirst.frc.team4183.robot.commands.VisionSubsystem.Idle;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class VisionSubsystem extends Subsystem
{
	// Strings to place into BucketVision NetworkTable
	public static final String GEAR_LIFT_MODE = "gearLift";
	public static final String BOILER_MODE = "Boiler";	
	public static final String FRONT_CAM= "frontCam";
	public static final String REAR_CAM = "rearCam";
	
	public static final String FRONT_CAM_MODE = "FrontCamMode";
	public static final String CURRENT_CAM = "CurrentCam";
	
	public static final String GEAR_LIFT_DATA = "GearLiftData";
	public static final String BOILER_DATA = "BoilerData";
	
	private static String currentCam = FRONT_CAM;
	private static String currentFrontCamMode = GEAR_LIFT_MODE;

	private static NetworkTable bvtable;
	
	class TargetData
	{
		public double confidenceFactor;
		public double distance_m;
		public double angle_deg;
		
		TargetData()
		{
			confidenceFactor = 0.0;
			distance_m = 0.0;
			angle_deg = 0.0;
		}
	}
	
	private static TargetData gearLiftData;
	private static TargetData boilerData;


	public VisionSubsystem()
	{
		// Set up defaults 
		bvtable = NetworkTable.getTable("BucketVision");
		bvtable.putString(FRONT_CAM_MODE, currentFrontCamMode);
		bvtable.putString(CURRENT_CAM, currentCam);
		
		gearLiftData = new TargetData();
		bvtable.putValue(GEAR_LIFT_DATA, gearLiftData);
		
		boilerData = new TargetData();
		bvtable.putValue(BOILER_DATA, boilerData);
		
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
		currentFrontCamMode = GEAR_LIFT_MODE;
		bvtable.putString(FRONT_CAM_MODE, currentFrontCamMode);
	}
	
	public boolean isGearMode()
	{
		return (currentFrontCamMode.equals(GEAR_LIFT_MODE));
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
	
	public boolean isGearLiftPresent()
	{
		bvtable.getValue(GEAR_LIFT_DATA, gearLiftData);
		
		return (gearLiftData.confidenceFactor >= 0.5);
	}
	
	public boolean isBoilerPresent()
	{
		bvtable.getValue(BOILER_DATA, boilerData);
		
		return (boilerData.confidenceFactor >= 0.5);
	}
	
	@Override
	protected void initDefaultCommand() 
	{
		setDefaultCommand( new Idle());
	}

}
