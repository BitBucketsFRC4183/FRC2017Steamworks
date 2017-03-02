package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Scripter extends Command {
	
		
	// These values written by "MeasureGear"
	static double measuredDistance;		// feet
	static double measuredYaw;          // degrees, gives Robot pose in target C.S.; +val means Robot sitting CCW (viewed from top)
	
	private int pc = 0;
	private final boolean debug = false;
	
	
	// To see the Scripter instruction set documentation, 
	// scroll down to the switch() in executeNextInstruction()
	
	// Dead reckoning numbers are assuming: 
	// positions 1 & 3 start points are 7' left & right of center line respectively,
	// position 2 start point is on center line (directly facing gear peg)
	// 
	private String[][] script = {
			{"", 		"BranchOnLocation Loc1 Loc2 Loc3" },  // Goto label 1,2,3 based on operator position
			{"Loc1", 	"DriveStraight 6.9" },   // Feet
			{"", 		"TurnBy -60.0" },        // Degrees, + is CCW from top (RHR Z-axis up)
			{"",		"Goto Vis" },
			{"Loc2",	"DriveStraight 2.4" },
			{"",		"Goto Vis" },
			{"Loc3",	"DriveStraight 6.9" },
			{"",		"TurnBy 60.0" },
			{"Vis", 	"EnableVisionGear" },   // S.B. ~4' from airship wall, looking straight at it
			{"", 		"Delay 500" },			
			{"", 		"MeasureGear" },		// Collect distance & yaw measures, put estimates into measuredDistance, measuredYaw
			{"", 		"YawCorrect" },     		// TurnBy -measuredYaw
			{"", 		"DistanceCorrect 2.0" },	// Stop short by 2 ft
			{"", 		"MeasureGear" },
			{"", 		"YawCorrect" },
			{"", 		"DistanceCorrect 1.0" },	// Stop short by 1 ft
			{"", 		"DeliverGear" },			// Spit the gear
			{"",		"Delay 200" },
			{"", 		"DriveStraight -1.0" },     // Back up
			{"", 		"End" }						// MUST finish in End state
	};
	
	/*
	// Test small moves (to see if MIN_DRIVEs big enough)
	private String[][] script = {
		{"", "TurnBy 5" },
		{"", "DriveStraight 0.25"},
		{"", "End" }    // MUST finish with End!
	};
	 */
	
	/*
	// Test just the dead-reckoning part of Gear program
	String[][] script = {
			{"", 		"BranchOnLocation Loc1 Loc2 Loc3" }, 
			{"Loc1", 	"DriveStraight 6.9" },   
			{"", 		"TurnBy -60.0" }, 
			{"",		"Goto Vis" },
			{"Loc2",	"DriveStraight 2.4" },
			{"",		"Goto Vis" },
			{"Loc3",	"DriveStraight 6.9" },
			{"",		"TurnBy 60.0" },
			{"Vis", 	"End" }
	 };	 
	 */
	
	/*
	// Test just the Vision part of Gear program
	String[][] script = {
			{"Vis", 	"EnableVisionGear" },   // S.B. ~4' from airship wall (~3' from drop point), looking straight at it
			{"", 		"Delay 500" },			
			{"", 		"MeasureGear" },	
			{"", 		"YawCorrect" },  
			{"", 		"DistanceCorrect 1.0" },	// DriveStraight measuredDistance - 1ft
			{"", 		"MeasureGear" },
			{"", 		"YawCorrect" },
			{"", 		"DistanceCorrect 0.0" },	// Drive full measureDistance
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

    	case "YawCorrect":  // (Turns by -measuredYaw)
    		yawCorrect();
    		break;
    		
    	case "DistanceCorrect":  // drives forward measuredDistance - param)
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
   
    private void yawCorrect() {
    	if(debug)
    		System.out.format("Scripter.yawCorrect %f\n", measuredYaw);
    	(new TurnBy( -measuredYaw)).start();
    }
    
    private void distanceCorrect( double dRemain) {
    	if(debug)
    		System.out.format( "Scripter.distanceCorrect %f\n", measuredDistance - dRemain);
    	(new DriveStraight( measuredDistance - dRemain)).start();
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
