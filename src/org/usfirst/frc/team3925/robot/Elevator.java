//UPDATED ELVATOR CODE FOR NEW DESIGN WITHOUT LATCHES REFER TO "OldElevator.java" FOR OLD CODE WITH LATCHES

package org.usfirst.frc.team3925.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {
	
	//sets constants
	private static final double MAX_ELEVATOR_HEIGHT = 13.25;
	private static final double TOLERANCE = 0.1;
	private static final double UP_SPEED = -1d;
	private static final double DOWN_SPEED = 1d;
	private static final double TOTE_HEIGHT = 13.500001;
	private static double elevatorEncoderOffset = 0;
	private static final int STATE_IDLE = 0;
	private static final int STATE_LIFT_INIT = 1;
	private static final int STATE_LIFT_WAIT_DOWN = 2;
	private static final int STATE_LIFT_WAIT_UP = 3;
	private static final int STATE_LOWER_INIT = 9;
	private static final int STATE_LOWER_WAIT_DOWN = 10;
	private static final int STATE_RESET = 15;

	//vital variables
	double targetHeight = 0;
	int state = 0;
	
	Talon leftElevatorMotor;
	Talon rightElevatorMotor;
	Encoder elevatorEncoder;
	DigitalInput limitSwitch;
	
	public Elevator(int leftelevatormotor, int rightelevatormotor, int encoderportA, int encoderportB, int limitswitchport, Latches latche) {
		leftElevatorMotor = new Talon(leftelevatormotor);
		rightElevatorMotor = new Talon(rightelevatormotor);
		elevatorEncoder = new Encoder(encoderportA, encoderportB);
		limitSwitch = new DigitalInput(limitswitchport);
		//inches per revolution
		elevatorEncoder.setDistancePerPulse(4.25/2048);
	}
	
	//returns current elevator height (based on encoder position which should be zeroed befor calling this)
	public double getCurrentHeight() {
		return elevatorEncoder.getDistance() + elevatorEncoderOffset;
	}
	
	//sets the raw elevator speed, limits speed based on maximum height
	public void setElevatorSpeed(double speed, boolean doLimits) {
		if (doLimits) {
			if (getCurrentHeight() >= MAX_ELEVATOR_HEIGHT && speed > 0) {
				speed = 0;
			}else if (getCurrentHeight() <= 0 && speed < 0) {
				speed = 0;
			}
		}
		speed = -speed; // motors on the robot are inverted
		leftElevatorMotor.set(speed);
		rightElevatorMotor.set(speed);
	}
	
	//sets elevator speed based on target height, always constant, stops if it is at target within tolerance
	private void updateHeight(double height, boolean doLimits) {
		double current = getCurrentHeight();
		if (Math.abs(height-current) > TOLERANCE) {
			if (current > height) {
				setElevatorSpeed(UP_SPEED, doLimits);
			}else {
				setElevatorSpeed(DOWN_SPEED, doLimits);
			}
		}else {
			setElevatorSpeed(0, doLimits);
		}
	}
	
	//state machine that runs the elevator
	public void elevatorRun() {// no activity
		if (state == STATE_IDLE) {
			targetHeight = getCurrentHeight();
		}else if (state == STATE_LIFT_INIT) {// initializes lift sequence
			targetHeight = 0;
			state++;
		}else if (state == STATE_LIFT_WAIT_DOWN) {// runs during lift sequence
			if (Math.abs(getCurrentHeight()-targetHeight) < TOLERANCE) {
				targetHeight = TOTE_HEIGHT;
				state = STATE_LIFT_WAIT_UP;
			}
		}else if (state == STATE_LIFT_WAIT_UP) {
			if (Math.abs(getCurrentHeight()-targetHeight) < TOLERANCE) {
				state = STATE_IDLE;
			}
		}else if (state == STATE_LOWER_INIT) {// initializes lower sequence
			targetHeight = 0;
			state++;
		}else if (state == STATE_LOWER_WAIT_DOWN) {// runs during lower sequence
			if (Math.abs(getCurrentHeight()-targetHeight) < TOLERANCE) {
				state = STATE_IDLE;
			}
		}else if (state == STATE_RESET) {// resets the height to 0 (where 0 is really offsetted from the actual 0 by elevatorEncoderOffset)
			if (Math.abs(getCurrentHeight()-targetHeight) < TOLERANCE) {
				targetHeight = 0;
				elevatorEncoderOffset = 0;
				state = STATE_IDLE;
			}
			if (!limitSwitch.get()) {
				elevatorEncoder.reset();
				targetHeight = 0;
				state = STATE_IDLE;
			}
		}
		SmartDashboard.putNumber("elevator state", state);
		updateHeight(targetHeight, state != STATE_RESET);
	}
	
	//resets the height to 0
	public void zeroElevator() {
		targetHeight = -1.2 * MAX_ELEVATOR_HEIGHT;
    	state = STATE_RESET;
	}
	
	//lifts the elevator to tote height
	public void liftStack() {
		state = STATE_LIFT_INIT;
	}
	
	//lowers the elevator to 0
	public void lowerStack() {
		state = STATE_LOWER_INIT;
	}

	public void idle() {
		state = STATE_IDLE;
	}
	
}
