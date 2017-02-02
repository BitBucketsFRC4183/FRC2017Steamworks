
package org.usfirst.frc.team4183.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.HashSet;
import java.util.Set;

import org.usfirst.frc.team4183.robot.subsystems.BallManipSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.ClimbSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4183.robot.subsystems.GearHandlerSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final BallManipSubsystem ballManipSubsystem = new BallManipSubsystem();
	public static final ClimbSubsystem climbSubsystem = new ClimbSubsystem();
	public static final DriveSubsystem driveSubsystem = new DriveSubsystem();
	public static final GearHandlerSubsystem gearHandlerSubsystem = new GearHandlerSubsystem();

	public static OI oi;

	
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
		oi = OI.instance();
				
		// Add all subsystems for debugging
		addSubsystemToDebug(ballManipSubsystem);
		addSubsystemToDebug(climbSubsystem);
		addSubsystemToDebug(driveSubsystem);
		addSubsystemToDebug(gearHandlerSubsystem);		
		showDebugInfo();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		// Clear out the scheduler
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
		// Clear out the scheduler
		Scheduler.getInstance().removeAll();
		
		// Re-create OI with specific driver & operator
		// This is just a placeholder
		// TODO use real names
		// TODO get this info from SmartDashboard
		oi.mapDriverOperator( OI.Driver.JOE, OI.Operator.BILL);

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
		// Disable LiveWindow & re-enable Scheduler
		LiveWindow.setEnabled(false);
		
		// Clear out the scheduler
		Scheduler.getInstance().removeAll();
				
		// Get Command-to-test class name
		String cmdName = SmartDashboard.getString("CmdToTest", "");

		// Attempt to instantiate & start that Command
		if (!cmdName.equals("")) {
			Subsystem subsys = dbgChooser.getSelected();
			if (subsys != null) {

				// We require that Commands for given Subsystem are in package
				// "org.usfirst.frc.team4183.robot.commands.Subsystem"
				String subsysName = subsys.getName();
				String fullName = "org.usfirst.frc.team4183.robot.commands." + subsysName + "." + cmdName;
				System.out.format("Attempt to test Command: %s...", fullName);

				// Note, for this to work, the Command must have a no-arg
				// constructor
				try {
					Command cmd = (Command) Class.forName(fullName).newInstance();
					cmd.start();
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
		// LiveWindow.run(); -- NOPE!!
		Scheduler.getInstance().run();
	}
	
	
	
	// State-testing-mode code follows
	// This is work-in-progress - tjw
	
	private Set<Subsystem> subSystems = new HashSet<>();
	private SendableChooser<Subsystem> dbgChooser;

	// Put debug info on SmartDashboard
	private void showDebugInfo() {

		// Show the scheduler
		SmartDashboard.putData(Scheduler.getInstance());

		// Show the Subsystems
		for (Subsystem subsys : subSystems)
			SmartDashboard.putData(subsys);

		// Chooser to select a Subsystem
		dbgChooser = new SendableChooser<Subsystem>();
		for (Subsystem subsys : subSystems)
			dbgChooser.addObject(subsys.getName(), subsys);
		SmartDashboard.putData("Debug", dbgChooser);

		
		// Text field to type in Command to test
		SmartDashboard.putString("CmdToTest", "");
	}

	public void addSubsystemToDebug(Subsystem subsys) {
		subSystems.add(subsys);
	}

	public void stateChange( Command fromState, Command toState) {

		// TODO this works OK for simple cases
		// but needs more work for CommandGroups and probably Auto mode
		
		if( !isTest())
			toState.start();
		else 			
			Scheduler.getInstance().removeAll();
	}	
}
