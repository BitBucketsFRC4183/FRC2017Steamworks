
package org.usfirst.frc.team4183.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import java.util.HashSet;
import java.util.Set;

import org.usfirst.frc.team4183.robot.subsystems.AutonomousSubsystem;
// Subsystems
import org.usfirst.frc.team4183.robot.subsystems.BallManipSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.ClimbSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.GearHandlerSubsystem;


// Non-subsystem (i.e., non-commandable) controls
import org.usfirst.frc.team4183.robot.LightingControl;
import org.usfirst.frc.team4183.robot.BucketVision;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	// Commands should use the runMode variable
	// to determine the current running mode of the Robot.
	public enum RunMode { DISABLED, AUTO, TELEOP, TEST };
	public static RunMode runMode = RunMode.DISABLED;
	
	
	public static BallManipSubsystem ballManipSubsystem;
	public static ClimbSubsystem climbSubsystem;
	public static DriveSubsystem driveSubsystem;
	public static GearHandlerSubsystem gearHandlerSubsystem;
	public static AutonomousSubsystem autonomousSubsystem;
	
	public static OI oi;
	public static SendableChooser<String> Alliances;
	
	public static LightingControl lightingControl;	
	public static NavxIMU imu;
	
	public static BucketVision vision;
		
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
		climbSubsystem = new ClimbSubsystem();
		driveSubsystem = new DriveSubsystem();
		gearHandlerSubsystem = new GearHandlerSubsystem();
		autonomousSubsystem = new AutonomousSubsystem();
		
		// Construct Compressor
		compressor = new Compressor(RobotMap.PNEUMATICS_CONTROL_MODULE_ID);		
		
		// Construct LightingControl, IMU, and vision
		lightingControl = new LightingControl(); 		
		imu = new NavxIMU();
		vision  = new BucketVision();
		

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
		Scheduler.getInstance().run();
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
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		runMode = RunMode.TELEOP;
		
		// Set up OI with specific driver & operator mappings.
		// Must do this before clearing out scheduler, see note in autonomousInit().
		// TODO use real names
		// TODO get this info from SmartDashboard
		oi.teleopInit( OI.Driver.JOE, OI.Operator.BILL);

		// Clear out the scheduler
		// Will result in only Default Commands (Idle-s) running.
		// Do this last to be sure that Idle-s see correct info when starting.
		Scheduler.getInstance().removeAll();		
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	
	@Override
	public void testInit() {
		runMode = RunMode.TEST;

		// Disable LiveWindow & re-enable Scheduler
		LiveWindow.setEnabled(false);
		
		// Set up OI mode for this test
		// (as always, do this before clearing out Scheduler!)
		switch( dbgOImapChooser.getSelected()) {
		case Auto:
			oi.autonomousInit();
			break;
		case Teleop:
			oi.teleopInit();
			break;
		}

		// Clear out the scheduler
		Scheduler.getInstance().removeAll();
				
		// Get Command-to-test class name
		String cmdName = SmartDashboard.getString("CmdToTest", "");

		// Attempt to instantiate & start that Command
		stateUnderTest = null;
		if (!cmdName.equals("")) {
			
			// Get selected Subsystem
			Subsystem subsys = dbgSubsysChooser.getSelected();
			if (subsys != null) {

				// Commands for given Subsystem are in package
				// "org.usfirst.frc.team4183.robot.commands.Subsystem"
				String subsysName = subsys.getName();
				String fullName = "org.usfirst.frc.team4183.robot.commands." + subsysName + "." + cmdName;
				System.out.format("Attempt to test Command: %s...", fullName);

				// Note, for this to work, the Command must have a 
				// *public* no-arg constructor
				try {
					Command cmd = (Command) Class.forName(fullName).newInstance();
					cmd.start();
					stateUnderTest = cmd;
					System.out.println("Success!");
					
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| IllegalArgumentException e) {
					System.out.println("Sorry, could not test that command");
				}
			}
		}
	}

	
	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		// LiveWindow.run(); -- Not this!!
		Scheduler.getInstance().run();  // This!!
		
	}
	
	
	
	// State-testing-mode code follows.
	// It works but so far hasn't been all that useful.
	// The Subsystem and Scheduler displays are very handy though.
	
	private Set<Subsystem> subSystems = new HashSet<>();
	private SendableChooser<Subsystem> dbgSubsysChooser;
	private enum DbgOIMode {Teleop, Auto};
	private SendableChooser<DbgOIMode> dbgOImapChooser;
	Command stateUnderTest;

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

		// Chooser to select a Subsystem
		dbgSubsysChooser = new SendableChooser<Subsystem>();
		for (Subsystem subsys : subSystems)
			dbgSubsysChooser.addObject(subsys.getName(), subsys);
		SmartDashboard.putData("Debug", dbgSubsysChooser);
		
		// Chooser to select the OI mapping in test mode
		dbgOImapChooser = new SendableChooser<DbgOIMode>();
		dbgOImapChooser.addObject("Teleop", DbgOIMode.Teleop);
		dbgOImapChooser.addObject("Auto", DbgOIMode.Auto);
		SmartDashboard.putData("OI Mode", dbgOImapChooser);
		
		// Text field to enter Command to test
		SmartDashboard.putString("CmdToTest", "");
	}


	public void stateChange( Command fromState, Command toState) {
		
		if( !(runMode == RunMode.TEST)) {
			// Normal operation, just do the transition as ordered
			toState.start();
			return;
		}
		
		if( stateUnderTest == null) {
			// In test mode, but no state-under-test;
			// disallow all transitions
			Scheduler.getInstance().removeAll();
			return;
		}
			
		// Modify fromState to be the top-level CommandGroup containing fromState
		// (for simple stand-alone Command, fromState is unchanged)
		// TODO test this with CommandGroup
		while( fromState.getGroup() != null)
			fromState = fromState.getGroup();
		// Same for toState
		while( toState.getGroup() != null)
			toState = toState.getGroup();
		
		if( fromState == stateUnderTest && toState != stateUnderTest) {
			// This is the transition that terminates state-test-mode
			stateUnderTest = null;
			Scheduler.getInstance().removeAll();
		}
		else {
			// Other transitions are OK though
			toState.start();
		}	
	}	
}
