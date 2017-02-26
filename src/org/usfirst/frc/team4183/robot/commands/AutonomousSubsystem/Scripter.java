package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Scripter extends Command {
	
	// At this distance, getting close
	private final double PRETTY_CLOSE_FT = 2.0;
	
	// Exit tolerance;
	// THIS MUST BE LARGER than "DEAD_ZONE_FT" in DriveStraight,
	// or you can get stuck in an infinite state loop.
	private final double ALLOWABLE_ERR_FT = 1.5/12.0;
	
	// If there seems to be some systematic error while approaching the target using vision, 
	// can try to correct it here.
	// A positive value will push the overall path to the left (viewed from behind),
	// == CCW (viewed from above).
	private final double YAW_FUDGE_DEG = 0.0;
	
	// These values written by "MeasureGear"
	static double measuredDistance;		// feet
	static double measuredYaw;          // degrees, gives Robot pose in target C.S.; +val means Robot sitting CCW (viewed from top)
	
	private int pc = 0;
	private final boolean debug = false;
	
	// Test program (does nothing useful)
	String[][] script = {
			{"",			"BranchOnLocation Loc1 Loc2 Loc3" },
			{"Loc1",		"Delay 1000" },
			{"",			"Goto Head" },
			{"Loc2",		"Delay 2000" },
			{"",			"Goto Head" },
			{"Loc3",		"Delay 3000" },
			{"Head",		"DriveStraight 1.0" },
			{"",			"TurnBy 15" },
			{"",			"EnableVisionGear" },
			{"",			"Delay 1000" },
			{"",			"DeliverGear" },
			{"",			"Delay 1000" },
			{"",			"End" }			
	};
	
	// To see the Scripter instruction set documentation, 
	// scroll down to the switch() in executeNextInstruction()
	
	/*
	// "Real" program (almost) - I'm sure it will need tweaking
	// FIXME get the right vals in the dead-reckoning
	String[][] script = {
			{"", 		"BranchOnLocation Loc1 Loc2 Loc3" },  // Goto label 1,2,3 based on operator position
			{"Loc1", 	"DriveStraight 8.0" },   // Feet
			{"", 		"TurnBy -45.0" },        // Degrees, + is CCW from top (RHR Z-axis up)
			{"",		"Goto Vis" },
			{"Loc2",	"DriveStraight 6.0" },
			{"",		"Goto Vis" },
			{"Loc3",	"DriveStraight 8.0" },
			{"",		"TurnBy 45.0" },
			{"Vis", 	"EnableVisionGear" },
			{"", 		"Delay 500" },			// Msec
			{"Look", 	"MeasureGear" },		// Collects distance & yaw measures, puts estimates into measuredDistance, measuredYaw
			{"", 		"BranchOnDistance Fini Close Far" },  // |d|<ALLOWABLE_ERR_FT->1st label; else d<PRETTY_CLOSE_FT->2nd label; else->3rd label
			{"Far", 	"YawCorrect" },     		// TurnBy -measuredYaw
			{"", 		"DistanceCorrect 0.5" },	// DriveStraight (param)*measuredDistance
			{"", 		"Goto Look" },
			{"Close", 	"YawCorrect" },
			{"", 		"DistanceCorrect 1.0" },
			{"", 		"Goto Look" },
			{"Fini", 	"YawCorrect" },
			{"", 		"DeliverGear" },			// Spits the gear
			{"",		"Delay 200" },
			{"", 		"DriveStraight -1.0" },
			{"", 		"End" }						// MUST finish in End state
	};
	*/
	
    public Scripter() {
    	// No "requires" - this one stands apart - it's a Meta-State.
    	// This is start()-ed from Robot.autonomousInit().
    }

    protected void initialize() {
    	pc = 0;
    }

    protected void execute() {
    	
    	// When Auto subsystem is Idle, we give it something to do!
    	if( "Idle".equals(Robot.autonomousSubsystem.getCurrentCommand().getName()))
    	    executeNextInstruction();   	
    }

    protected boolean isFinished() {
        return false;
    }
 
    
    private void executeNextInstruction() {
    	
    	if( pc >= script.length) {
    		System.err.println( "Scripter.execute: pc is out of bounds (did you forget End in script?)");
    		return;
    	}
    	
      	String instruction = script[pc++][1];
      	
      	if(debug)
      		System.out.format( "Scripter.execute %s\n", instruction);
      		
    	String[] tokens = instruction.split(" +");
    	switch( tokens[0]) {
    	
    	// These are the legal Instruction Opcodes
    	// For each case in switch, a following comment documents Opcode's parameters if any
    	
    	case "Goto":  // label
    		doGoto(tokens[1]);
    		break;

    	case "Delay":  // msecs
    		delay( Long.parseLong(tokens[1]));
    		break;
    		
    	case "BranchOnLocation":  // lbl_1 lbl_2 lbl_3 (refers to operator location)
    		branchOnLocation( tokens[1], tokens[2], tokens[3]);
    		break;
    		
    	case "TurnBy": // yaw (degrees, + is CCW from top)
    		turnBy( Double.parseDouble(tokens[1]));
    		break;
    		
    	case "DriveStraight":  // distance (feet)
    		driveStraight( Double.parseDouble(tokens[1]));
    		break;
    	
    	case "EnableVisionGear":
    		enableVisionGear();
    		break;
    		
    	case "MeasureGear":  // (Sets measuredYaw and measuredDistance from Vision samples)
    		measureGear();
    		break;

    	case "BranchOnDistance":  // lblFini lblClose lblFar
    		branchOnDistance( tokens[1], tokens[2], tokens[3]);
    		break;

    	case "YawCorrect":  // (Turns by -measuredYaw)
    		yawCorrect();
    		break;
    		
    	case "DistanceCorrect":  // fract  (drives forward fract*measuredDistance)
    		distanceCorrect( Double.parseDouble(tokens[1]));
    		break;
    		
    	case "DeliverGear":  // (Spits the gear)
    		deliverGear();
    		break;
    	
    	case "End":  // (Stops all, does not exit - must be last instruction)
    		endState();
    		break;
    		
    	default:
    		throw new IllegalArgumentException( 
    			String.format("Scripter: unknown instruction: %s\n", instruction));
    	}    	
    }
    
    private void doGoto( String label) {
    	if(debug)
    		System.out.format( "Scripter.doGoto %s\n", label);
    	
    	for( int i = 0 ; i < script.length ; i++)
    		if( script[i][0].equals(label)) {
    			pc = i;
    			return;
    		}
    	
    	throw new IllegalArgumentException(
    		String.format("Scripter.doGoto: Label %s not found\n", label));
    }

    private void delay( long msecs) {
    	if(debug)
    		System.out.format("Scripter.delay %d\n", msecs);
    	(new Delay( msecs)).start();
    }
 
    private void branchOnLocation( String lbl1, String lbl2, String lbl3) {  	
    	if(debug)
    		System.out.format( "Scripter.branchOnLocation %s %s %s\n", lbl1, lbl2, lbl3);
    	
    	int location = DriverStation.getInstance().getLocation();
    	switch( location) {
    	case 1:
    		doGoto( lbl1);
    		break;
    	case 2:
    		doGoto( lbl2);
    		break;
    	case 3:
    		doGoto( lbl3);
    		break;
    	default:
    		throw new IllegalArgumentException(
    			String.format( "Scripter.branchOnLocation: unknown location %d\n", location));
    	}
    }
     
    private void turnBy( double yaw) {
    	if(debug)
    		System.out.format("Scripter.turnBy %f\n", yaw);
    	(new TurnBy( yaw)).start();
    }
    
    private void driveStraight( double dist) {
    	if(debug)
    		System.out.format( "Scripter.driveStraight %f\n", dist);
    	(new DriveStraight( dist)).start();
    }

    private void enableVisionGear() {
    	if(debug)
    		System.out.println("Scripter.enableVisionGear");
    	Robot.visionSubsystem.setGearMode();
    }
    
    private void measureGear() {
    	if(debug)
    		System.out.println("Scripter.measureGear");
    	(new MeasureGear()).start();
    }

    private void branchOnDistance( String lblFini, String lblClose, String lblFar) {  
    	if(debug)
    		System.out.format("Scripter.branchOnDistance %f %s %s %s\n", measuredDistance, lblFini, lblClose, lblFar);
 
    	if( Math.abs(measuredDistance) < ALLOWABLE_ERR_FT )
    		doGoto(lblFini);
    	else if( measuredDistance < PRETTY_CLOSE_FT)
    		doGoto(lblClose);
    	else
    		doGoto(lblFar);	
    }
   
    private void yawCorrect() {
    	if(debug)
    		System.out.format("Scripter.yawCorrect %f\n", measuredYaw);
    	(new TurnBy( -measuredYaw + YAW_FUDGE_DEG)).start();
    }
    
    private void distanceCorrect( double fract) {
    	if(debug)
    		System.out.format( "Scripter.distanceCorrect %f\n", measuredDistance*fract);
    	(new DriveStraight( measuredDistance*fract)).start();
    }
    
    private void deliverGear() {
    	if(debug)
    		System.out.println("Scripter.deliverGear");
    	OI.btnSpitGearA.hit();
    	OI.btnSpitGearB.hit();
    }
    
    private void endState() {
    	if(debug)
    		System.out.println("Scripter.endState");
    	(new End()).start();
    }    
}
