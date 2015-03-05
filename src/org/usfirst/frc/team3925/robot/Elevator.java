package org.usfirst.frc.team3925.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {
	
	Latches latches;
	
	private static final double MAX_ELEVATOR_HEIGHT = 14;
	private static final double TOLERANCE = 0.25;
	private static final double SPEED = .5d;
	private static final double TOTE_HEIGHT = 13.500001;
	private static double elevatorEncoderOffset = -4;
	private static final int STATE_IDLE = 0;
	private static final int STATE_LIFT_INIT = 1;
	private static final int STATE_LIFT_WAIT_UP = 2;
	private static final int STATE_LIFT_WAIT_DOWN = 3;
	private static final int STATE_LOWER_INIT = 9;
	private static final int STATE_LOWER_WAIT_UP = 10;
	private static final int STATE_LOWER_WAIT_DOWN = 11;
	private static final int STATE_RESET = 15;
		
	double targetHeight = 0;
	int state = 0;
	
	Victor leftElevatorMotor;
	Victor rightElevatorMotor;
	Encoder elevatorEncoder;
	DigitalInput limitSwitch;
	
	public Elevator(int leftelevatormotor, int rightelevatormotor, int encoderportA, int encoderportB, int limitswitchport, Latches latches) {
		leftElevatorMotor = new Victor(leftelevatormotor);
		rightElevatorMotor = new Victor(rightelevatormotor);
		elevatorEncoder = new Encoder(encoderportA, encoderportB);
		limitSwitch = new DigitalInput(limitswitchport);
		//inches per revolution
		elevatorEncoder.setDistancePerPulse(4.25/2048);
		this.latches = latches;
	}
	
	private void updateHeight(double height) {
		double current = getCurrentHeight();
		if (Math.abs(height-current) > TOLERANCE) {
			if (current > height) {
				setElevatorSpeed(-SPEED);
			}else {
				setElevatorSpeed(SPEED);
			}
		}else {
			setElevatorSpeed(0);
		}
	}
	
	public double getCurrentHeight() {
		return elevatorEncoder.getDistance() + elevatorEncoderOffset;
	}
	
	public void setElevatorSpeed(double speed) {
		if (getCurrentHeight() >= MAX_ELEVATOR_HEIGHT && speed >= 0) {
			speed = 0;
		}
		speed = -speed; // motors on the robot are inverted
		leftElevatorMotor.set(speed);
		rightElevatorMotor.set(speed);
	}
	
	public void elevatorRun() {
		if (state == STATE_IDLE) {
			targetHeight = getCurrentHeight();
		}else if (state == STATE_LIFT_INIT) {
			targetHeight = TOTE_HEIGHT;
			state++;
		}else if (state == STATE_LIFT_WAIT_UP) {
			if (Math.abs(getCurrentHeight()-targetHeight) < TOLERANCE) {
				targetHeight = 0;
				state++;
			}
		}else if (state == STATE_LIFT_WAIT_DOWN) {
			if (Math.abs(getCurrentHeight()-targetHeight) < TOLERANCE) {
				state = STATE_IDLE;
			}
		}else if (state == STATE_LOWER_INIT) {
			targetHeight = TOTE_HEIGHT;
			state++;
		}else if (state == STATE_LOWER_WAIT_DOWN) {
			if (Math.abs(getCurrentHeight()-targetHeight) < TOLERANCE) {
				latches.setLatches(true);
				targetHeight = 0;
				state++;
			}
		}else if (state == STATE_LOWER_WAIT_UP) {
			if (Math.abs(getCurrentHeight()-targetHeight) < TOLERANCE) {
				latches.setLatches(false);
				state = STATE_IDLE;
			}
		}else if (state == STATE_RESET) {
			if (Math.abs(getCurrentHeight()-targetHeight) < TOLERANCE) {
				targetHeight = 0;
				elevatorEncoderOffset = 0;
				state = STATE_IDLE;
			}
			if (limitSwitch.get()) {
				elevatorEncoder.reset();
				targetHeight = 0;
				state = STATE_IDLE;
			}
		}
		SmartDashboard.putNumber("elevator state", state);
		updateHeight(targetHeight);
	}
	
	public void zeroElevator() {
		targetHeight = -MAX_ELEVATOR_HEIGHT*2;
    	state = STATE_RESET;
	}
	
	public void liftStack() {
		state = STATE_LIFT_INIT;
	}
	
	public void lowerStack() {
		state = STATE_LOWER_INIT;
	}
	
}
