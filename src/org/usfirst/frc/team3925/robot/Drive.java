package org.usfirst.frc.team3925.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;

public class Drive {
	
	RobotDrive drive;
	DoubleSolenoid shifter;
	Encoder leftEncoder;
	Encoder rightEncoder;
	
	public Drive(int leftmotorport, int rightmotorport, int pcmCanId, int solenoidportA, int solenoidportB, int leftencoderportA, int leftencoderportB, int rightencoderportA, int rightencoderportB) {
		drive = new RobotDrive(leftmotorport, rightmotorport);
		shifter = new DoubleSolenoid(pcmCanId, solenoidportA, solenoidportB);
		leftEncoder = new Encoder(leftencoderportA, leftencoderportB);
		rightEncoder = new Encoder(rightencoderportA, rightencoderportB);
	}
	
	public void drive(double fwd, double turn, boolean gear) {
		drive.arcadeDrive(fwd, turn);
		shifter.set(gear ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
	}
	
	public double getLeftEncoder() {
		return leftEncoder.getDistance();
	}
	
	public double getRightEncoder() {
		return rightEncoder.getDistance();
	}
	
	public void resetLeftEncoder() {
		leftEncoder.reset();
	}
	
	public void resetRightEncoder() {
		rightEncoder.reset();
	}
}
