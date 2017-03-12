
package org.usfirst.frc.team4183.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.HashSet;
import java.util.Set;

import org.usfirst.frc.team4183.robot.subsystems.AutonomousSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.BallManipSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.VisionSubsystem;
import org.usfirst.frc.team4183.utils.DoEveryN;
import org.usfirst.frc.team4183.utils.Stopwatch;
import org.usfirst.frc.team4183.robot.subsystems.ClimbSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.GearHandlerSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.HopperSubsystem;
import org.usfirst.frc.team4183.robot.LightingControl;
import org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem.Scripter;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	// Use this runMode variable to determine the 
	// current running mode of the Robot.
	public enum RunMode { DISABLED, AUTO, TELEOP, TEST };
	public static RunMode runMode = RunMode.DISABLED;
	
	
	public static BallManipSubsystem ballManipSubsystem;
	public static HopperSubsystem hopperSubsystem;
	public static ClimbSubsystem climbSubsystem;
	public static DriveSubsystem driveSubsystem;
	public static GearHandlerSubsystem gearHandlerSubsystem;
	public static AutonomousSubsystem autonomousSubsystem;
	public static VisionSubsystem visionSubsystem;
	
	public SendableChooser<Integer> positionChooser;
	
	public static OI oi;
	
	public static LightingControl lightingControl;	
	public static NavxIMU imu;
	
		
	private Compressor compressor;
		
	// Anybody needing the (singleton) Robot instance 
	// can get it by doing Robot.instance().
	// Bit of a hack but WPILib leaves me no other way.
	private static Robot robotInstance;
	public static Robot instance() { return robotInstance; }
	
	public Robot() {
		robotInstance = this;
	}
	
	
	@Override
	public void robotInit() {

		// Construct the OI
		// Do this first in case any Subsystems need to look at it
		oi = OI.instance();
		
		// Construct the Subsystems
		ballManipSubsystem = new BallManipSubsystem();
		hopperSubsystem = new HopperSubsystem();
		climbSubsystem = new ClimbSubsystem();
		driveSubsystem = new DriveSubsystem();
		gearHandlerSubsystem = new GearHandlerSubsystem();
		autonomousSubsystem = new AutonomousSubsystem();
		visionSubsystem  = new VisionSubsystem();
		
		// Construct Compressor
		compressor = new Compressor(RobotMap.PNEUMATICS_CONTROL_MODULE_ID);		
		
		// Construct LightingControl, IMU, and vision
		lightingControl = new LightingControl(); 		
		imu = new NavxIMU();
			
		// Construction is complete
		
		// Make the SD autonomous-mode position chooser
		positionChooser = new SendableChooser<Integer>();
		positionChooser.addDefault( "None", 0);
		positionChooser.addObject( "Left", 1);
		positionChooser.addObject( "Center", 2);
		positionChooser.addObject( "Right", 3);
		SmartDashboard.putData( "AutoChooser", positionChooser);
		
				
		// Start pressurizing the tanks (when Robot enabled)
		compressor.setClosedLoopControl(true);
		
						
		// Add all subsystems for debugging
		addSubsystemToDebug(ballManipSubsystem);
		addSubsystemToDebug(hopperSubsystem);
		addSubsystemToDebug(climbSubsystem);
		addSubsystemToDebug(driveSubsystem);
		addSubsystemToDebug(gearHandlerSubsystem);
		addSubsystemToDebug(autonomousSubsystem);		
		showDebugInfo();		
	}


	@Override
	public void disabledInit() {
		runMode = RunMode.DISABLED;

		// Set up OI for teleop mode - in case there are any buttons
		// that should work while disabled (unlikely but possible).
		// NOTE: Must do this BEFORE clearing out scheduler!
		// Clearing out scheduler causes Default Commands (Idle-s)
		// to start, and we want those to see the new OI mappings.
		oi.teleopInit();
		
		// Clear out the scheduler
		// Will result in only Default Commands (Idle-s) running.
		// Do this last to be sure that Idle-s see correct info when starting.
		Scheduler.getInstance().removeAll();
	}

	@Override
	public void disabledPeriodic() {
		runWatch.start();
		Scheduler.getInstance().run();
		runWatch.stop();
	}


	@Override
	public void autonomousInit() {
		runMode = RunMode.AUTO;
		
		// Set up OI for autonomous mode
		// NOTE: Must do this BEFORE clearing out scheduler!
		// Clearing out scheduler causes Default Commands (Idle-s)
		// to start, and we want those to see the new OI mappings.
		oi.autonomousInit();
				
		// Clear out the scheduler
		// Will result in only Default Commands (Idle-s) running.
		// Do this last to be sure that Idle-s see correct info when starting.
		Scheduler.getInstance().removeAll();
		
		// Start the Autonomous-mode Scripter
		int position = positionChooser.getSelected();
		if( position != 0)
			(new Scripter( positionChooser.getSelected())).start();
	}

	@Override
	public void autonomousPeriodic() {
		runWatch.start();
		Scheduler.getInstance().run();
		runWatch.stop();
	}
	

	@Override
	public void teleopInit() {
		runMode = RunMode.TELEOP;

		// Set up OI for teleop mode
		// NOTE: Must do this BEFORE clearing out scheduler!
		// Clearing out scheduler causes Default Commands (Idle-s)
		// to start, and we want those to see the new OI mappings.
		oi.teleopInit();

		// Clear out the scheduler
		// Will result in only Default Commands (Idle-s) running.
		// Do this last to be sure that Idle-s see correct info when starting.
		Scheduler.getInstance().removeAll();
	}


	@Override
	public void teleopPeriodic() {			
		runWatch.start();
		Scheduler.getInstance().run();	
		runWatch.stop();
	}


	
	@Override
	public void testInit() {
		runMode = RunMode.TEST;
	}

	
	@Override
	public void testPeriodic() {
		LiveWindow.run();		
	}
	
	
	// Called periodically all the time (regardless of mode)
	@Override
	public void robotPeriodic() {
		loopWatch.stop();
		loopWatch.start();
		
		periodicSDdebugLoop.update();
	}
	

	// Some ancillary debugging stuff below here
	
	private Stopwatch runWatch = 
			new Stopwatch( "Run", 
			(name, max, min, avg) -> SmartDashboard.putNumber( "MaxRun", max) );
	private Stopwatch loopWatch = 
			new Stopwatch( "Loop", 
			(name, max, min, avg) -> SmartDashboard.putNumber( "MaxLoop", max) );

	private DoEveryN periodicSDdebugLoop = 
			new DoEveryN( 10, () -> putPeriodicSDdebug());
	
	
	private void putPeriodicSDdebug() {
		
		SmartDashboard.putString( "IMU_Yaw", 
				String.format("%.1f", imu.getYawDeg()));
		SmartDashboard.putString( "IMU_Yawrate", 
				String.format("%.1f", imu.getYawRateDps()));
		SmartDashboard.putString( "Left_Position", 
				String.format("%.1f", driveSubsystem.getLeftPosition_inch()));
		SmartDashboard.putString( "Right_Position", 
				String.format("%.1f", driveSubsystem.getRightPosition_inch()));
		SmartDashboard.putString("Fwd_Velocity",
				String.format("%.1f", driveSubsystem.getFwdVelocity_ips()));
		SmartDashboard.putString( "Fwd_Current", 
				String.format( "%.1f", driveSubsystem.getFwdCurrent()));
		SmartDashboard.putString( "VisGearYaw",
				String.format("%.1f", visionSubsystem.getGearAngle_deg()));
		SmartDashboard.putString( "VisGearDist", 
				String.format("%.1f", visionSubsystem.getGearDistance_inch()));
	}
	
	
	private Set<Subsystem> subSystems = new HashSet<>();

	// Add Subsystem to the test set
	public void addSubsystemToDebug(Subsystem subsys) {
		subSystems.add(subsys);
	}

	// Put debug info on SmartDashboard
	private void showDebugInfo() {

		// Show the Scheduler
		SmartDashboard.putData(Scheduler.getInstance());

		// Show the Subsystems
		for (Subsystem subsys : subSystems)
			SmartDashboard.putData(subsys);
	}	
}
