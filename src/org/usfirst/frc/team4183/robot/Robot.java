
package org.usfirst.frc.team4183.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.usfirst.frc.team4183.robot.subsystems.AutonomousSubsystem;
// Subsystems
import org.usfirst.frc.team4183.robot.subsystems.BallManipSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.VisionSubsystem;
import org.usfirst.frc.team4183.utils.Stopwatch;
import org.usfirst.frc.team4183.robot.subsystems.ClimbSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.GearHandlerSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.HopperSubsystem;
// Non-subsystem (i.e., non-commandable) controls
import org.usfirst.frc.team4183.robot.LightingControl;

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
	
	public static OI oi;
	public static SendableChooser<String> Alliances;
	
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
	
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
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
				
		// Start pressurizing the tanks
		compressor.setClosedLoopControl(true);
		
		// Initialize Network Tables and Sendable Chooser
		Alliances = new SendableChooser<String>();
		Alliances.addDefault("Red", "red");
		Alliances.addObject("Blue", "blue");		
		SmartDashboard.putData("Alliances", Alliances);
						
		// Add all subsystems for debugging
		addSubsystemToDebug(ballManipSubsystem);
		addSubsystemToDebug(hopperSubsystem);
		addSubsystemToDebug(climbSubsystem);
		addSubsystemToDebug(driveSubsystem);
		addSubsystemToDebug(gearHandlerSubsystem);
		addSubsystemToDebug(autonomousSubsystem);		
		showDebugInfo();		
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		runMode = RunMode.DISABLED;
		
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

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
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
	}

	/**
	 * This function is called periodically during autonomous
	 */
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

	/**
	 * This function is called periodically during operator control
	 */

	List<Double> runTimeList = new ArrayList<>();
	
	@Override
	public void teleopPeriodic() {			
		runWatch.start();
		Scheduler.getInstance().run();	
		runWatch.stop();
	}

	/**
	 * This function is called periodically during test mode
	 */
	
	@Override
	public void testInit() {
		runMode = RunMode.TEST;
	}

	
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();		
	}
	
	
	@Override
	public void robotPeriodic() {
		loopWatch.stop();
		loopWatch.start();
	}

	private Stopwatch runWatch = 
			new Stopwatch( "Run", 
			(name, max, min, avg) -> SmartDashboard.putNumber( "MaxRun", max) );
	private Stopwatch loopWatch = 
			new Stopwatch( "Loop", 
			(name, max, min, avg) -> SmartDashboard.putNumber( "MaxLoop", max) );

	
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
