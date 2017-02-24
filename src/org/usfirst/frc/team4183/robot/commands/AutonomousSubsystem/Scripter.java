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
	
	
	static double measuredDistance;
	static double measuredYaw;
	
	private int pc = 0;
	private final boolean debug = true;
	
	// Test program
	String[][] script = {
			{"",			"BranchOnLocation Loc1 Loc2 Loc3" },
			{"Loc1",		"Delay 1000" },
			{"",			"Goto Head" },
			{"Loc2",		"Delay 2000" },
			{"",			"Goto Head" },
			{"Loc3",		"Delay 3000" },
			{"Head",		"DriveStraight 1.0" },
			{"",			"TurnBy 10.0" },
			{"",			"EnableVisionGear" },
			{"",			"Delay 1000" },
			{"",			"DeliverGear" },
			{"",			"Delay 1000" },
			{"",			"End" }			
	};
	
	/*
	// "Real" program (almost) - I'm sure it will need tweaking
	String[][] script = {
			{"", 		"BranchOnLocation Loc1 Loc2 Loc3" }, 
			{"Loc1", 	"DriveStraight 8.0" },  // FIXME get the right vals in the dead-reckoning
			{"", 		"TurnBy -45.0" },
			{"",		"Goto Vis" },
			{"Loc2",	"DriveStraight 6.0" },
			{"",		"Goto Vis" },
			{"Loc3",	"DriveStraight 8.0" },
			{"",		"TurnBy 45.0" },
			{"Vis", 	"EnableVisionGear" },
			{"", 		"Delay 500" },
			{"Meas", 	"MeasureGear" },
			{"", 		"BranchOnDistance Fini Close Far" },
			{"Far", 	"YawCorrect" },
			{"", 		"DistanceCorrect 0.5" },
			{"", 		"Goto Meas" },
			{"Close", 	"YawCorrect" },
			{"", 		"DistanceCorrect 1.0" },
			{"", 		"Goto Meas" },
			{"Fini", 	"YawCorrect" },
			{"", 		"DeliverGear" },
			{"",		"Delay 200" },
			{"", 		"DriveStraight -1.0" },
			{"", 		"End" }
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
    	// For each opcode, a following comment lists parameters if any
    	
    	case "Goto":  // label
    		doGoto(tokens[1]);
    		break;
    		
    	case "BranchOnDistance":  // lblFini lblClose lblFar
    		branchOnDistance( tokens[1], tokens[2], tokens[3]);
    		break;
    	
    	case "BranchOnLocation":  // lbl_1 lbl_2 lbl_3 
    		branchOnLocation( tokens[1], tokens[2], tokens[3]);
    		break;
    		
    	case "Delay":  // msecs
    		delay( Long.parseLong(tokens[1]));
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
    
    private void branchOnDistance( String fini, String close, String far) {  
    	if(debug)
    		System.out.format("Scripter.branchOnDistance %f %s %s %s\n", measuredDistance, fini, close, far);
 
    	if( Math.abs(measuredDistance) < ALLOWABLE_ERR_FT )
    		doGoto(fini);
    	else if( measuredDistance < PRETTY_CLOSE_FT)
    		doGoto(close);
    	else
    		doGoto(far);	
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
    
    private void delay( long msecs) {
    	if(debug)
    		System.out.format("Scripter.delay %d\n", msecs);
    	(new Delay( msecs)).start();
    }
    
    private void turnBy( double yaw) {
    	System.out.format("Scripter.turnBy %f\n", yaw);
    	(new TurnBy( yaw)).start();
    }
    
    private void driveStraight( double dist) {
    	System.out.format( "Scripter.driveStraight %f\n", dist);
    	(new DriveStraight( dist)).start();
    }

    private void enableVisionGear() {
    	System.out.println("Scripter.enableVisionGear");
    	Robot.visionSubsystem.setGearMode();
    }
    
    private void measureGear() {
    	System.out.println("Scripter.measureGear");
    	(new MeasureGear()).start();
    }
     
    private void yawCorrect() {
    	System.out.format("Scripter.yawCorrect %f\n", measuredYaw);
    	(new TurnBy( -measuredYaw + YAW_FUDGE_DEG)).start();
    }
    
    private void distanceCorrect( double fract) {
    	System.out.format( "Scripter.distanceCorrect %f\n", measuredDistance*fract);
    	(new DriveStraight( measuredDistance*fract)).start();
    }
    
    private void deliverGear() {
    	System.out.println("Scripter.deliverGear");
    	OI.btnSpitGearA.push();
    	OI.btnSpitGearB.push();
    }
    
    private void endState() {
    	System.out.println("Scripter.endState");
    	(new End()).start();
    }    
}
