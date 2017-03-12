package org.usfirst.frc.team4183.robot.commands.AutonomousSubsystem;

import org.usfirst.frc.team4183.robot.OI;
import org.usfirst.frc.team4183.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Scripter extends Command {
	
		
	// These values written by "MeasureGear"
	static double measuredDistance_inch;    // inch
	static double measuredYaw_deg;          // gives Robot pose in target C.S.; +val means Robot sitting CCW (viewed from top)
	
	private int pc = 0;
	private final boolean debug = false;
	private final int position;
	private final String color = "Red";
	
	// To see the Scripter instruction set documentation, 
	// scroll down to the switch() in executeNextInstruction()
	
	// Dead reckoning numbers are assuming: 
	// positions 1 & 3 start points are 7' left & right of center line respectively,
	// position 2 start point is on center line (directly facing gear peg)
	// 
//	private String[][] script = {
//			{"",		"DriveStraight 12.0"},
//			{"", 		"TurnBy -90.0"},
//			{"",		"DriveStraight 12.0"},
//			{"", 		"TurnBy -90.0"},
//			{"", 		"DriveStraight 12.0"},
//			{"", 		"TurnBy -90.0"},
//			{"", 		"DriveStraight 12.0"},
//			{"", 		"TurnBy 90.0"},
//			{"", 		"DriveStraight 12.0"},
//			{"", 		"TurnBy 90.0"},
//			{"", 		"DriveStraight 12.0"},
//			{"", 		"TurnBy 90.0"},
//			{"", 		"DriveStraight 12.0"},
//			{"", 		"TurnBy 90.0"},
//			{"", 		"DriveStraight 12.0"},
//			{"", 		"TurnBy -90.0"},
//			{"", 		"End"}
//	};
	
	private String[][] script = {
			{"", 		"BranchOnLocation Loc1 Loc2 Loc3" },  // Goto label 1,2,3 based on operator position
			{"Loc1", 	"DriveStraight 82.2" },  // Inch
			{"", 		"TurnBy -60.0" },        // Degrees, + is CCW from top (RHR Z-axis up)
			{"",		"Goto Vis" },
			{"Loc2",	"DriveStraight 26.0" },
			{"",		"Goto Vis" },
			{"Loc3",	"DriveStraight 82.2" },
			{"",		"TurnBy 60.0" },
			{"Vis", 	"EnableVisionGear" },   // S.B. ~4' from airship wall, looking straight at it
			{"", 		"MeasureGear" },		// Collect distance & yaw measures, put estimates into measuredDistance, measuredYaw
			{"", 		"YawCorrect" },     		// TurnBy -measuredYaw
			{"", 		"DistanceCorrect 21.0" },	// Stop short by this much
			{"", 		"MeasureGear" },
			{"", 		"YawCorrect" },
			{"", 		"DistanceCorrect 15.0" },	
			{"", 		"DeliverGear" },			// Spit the gear
			{"",        "BranchOnLocation Loc1B Loc2B Loc3B"},
			{"Loc1B",   "BranchOnColor Blue1 Red1"},
			{"Loc2B",   "DriveStraight -12.0"},//58.3"
			{"",        "Goto End"},
			{"Loc3B",   "BranchOnColor Blue3 Red3"},//149.3 deg Red
			{"Red1",    "DriveStraight -12.0"},
			{"",        "Goto End"},
			{"Blue1",   "StartShooter"},
			{"",   		"DriveStraight -70.2"},
			{"",        "TurnBy -149.3"},
			{"",        "Goto Shoot"},
			{"Red3",    "StartShooter"},
			{"",    	"DriveStraight -70.2"},
			{"",        "TurnBy 149.3"},
			{"",        "Goto Shoot"},
			{"Blue3",   "DriveStraight -12.0"},
			{"",        "Goto End"},
			{"Shoot",   "Shoot"},
			{"End", 	"End" }						// MUST finish in End state
	};
	
	
	
	// Test small moves to make sure MIN_DRIVEs big enough.
	// e.g. TurnBy 5, DriveStraight 3.
	// Test big moves to make sure it behaves & settles.
	// e.g. TurnBy 60, DriveStraight 48.
	/*
	private String[][] script = {
		{"", "TurnBy 60" },
		{"", "End" }    // MUST finish with End!
	};
	*/
	
	/*
	// Test just the dead-reckoning part of Gear program
	String[][] script = {
			{"", 		"BranchOnLocation Loc1 Loc2 Loc3" }, 
			{"Loc1", 	"DriveStraight 84" },   
			{"", 		"TurnBy -60.0" }, 
			{"",		"Goto Vis" },
			{"Loc2",	"DriveStraight 29" },
			{"",		"Goto Vis" },
			{"Loc3",	"DriveStraight 84" },
			{"",		"TurnBy 60.0" },
			{"Vis", 	"EnableVisionGear" },
			{"",		"Delay 500" },
			{"",		"MeasureGear" },
			{"", 		"End" }
	 };	 
	 */
	
	/*
	// Test just the Vision part of Gear program
	String[][] script = {
			{"Vis", 	"EnableVisionGear" },   // S.B. ~4' from airship wall (~3' from drop point), looking straight at it
			{"", 		"Delay 500" },			
			{"", 		"MeasureGear" },	
			{"", 		"YawCorrect" },  
			{"", 		"DistanceCorrect 24.0" },	
			{"", 		"MeasureGear" },
			{"", 		"YawCorrect" },
			{"", 		"DistanceCorrect 15.0" },
			{"", 		"DeliverGear" },
			{"",		"Delay 200" },
			{"", 		"DriveStraight -12.0" }, 
			{"", 		"End" }					
	};
	*/
	

    public Scripter( int position) {
    	// No "requires" - this one stands apart - it's a Meta-State.
    	// This is start()-ed from Robot.autonomousInit().
    	this.position = position;
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
    		
    	case "BranchOnColor":
    		branchOnColor(tokens[1], tokens[2]);
    		break;
    		
    	case "TurnBy": // yaw (degrees, + is CCW from top)
    		turnBy( Double.parseDouble(tokens[1]));
    		break;
    		
    	case "DriveStraight":  // distance (inches)
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
    		
    	case "StartShooter":
    		startShooter();
    		break;
    		
    	case "Shoot":
    		shoot();
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
    	
    	switch( position) {
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
    			String.format( "Scripter.branchOnLocation: unknown location %d\n", position));
    	}
    }
    
    private void branchOnColor(String lblB, String lblR) {
    	if(debug)
    		System.out.format("Scripter.branchOnColor %s %s %s\n", lblB, lblR);
    	
    	if(Robot.visionSubsystem.isBlueAlliance()) {
    		doGoto(lblB);
    	}
    	else {
    		doGoto(lblR);
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
    		System.out.format("Scripter.yawCorrect %f\n", measuredYaw_deg);
    	(new TurnBy( -measuredYaw_deg)).start();
    }
    
    private void distanceCorrect( double dRemain) {
    	if(debug)
    		System.out.format( "Scripter.distanceCorrect %f\n", measuredDistance_inch - dRemain);
    	(new DriveStraight( measuredDistance_inch - dRemain)).start();
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
    
    private void startShooter() {
    	OI.btnShooterStart.hit();
    }
    
    private void shoot() {
    	/// TODO: The following technique was used because
    	/// there was some trouble trying to get a logical button
    	/// press to induce a button event at the ball subsystem state machine
    	/// which was looking for an onPressed event (i.e., OI.btnShoot.hit() or press() did not work)
    	/// Despite this, the technique works and we were able to shoot balls in auto
    	long timeInit = System.currentTimeMillis();
    	while(System.currentTimeMillis() - timeInit < 4000) {
    		Robot.ballManipSubsystem.setConveyerOn();
    	}
    }
}
