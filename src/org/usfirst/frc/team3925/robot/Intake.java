package org.usfirst.frc.team3925.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

public class Intake {
	
	Victor leftArm;
	Victor rightArm;
	Talon rollers;
	
	public Intake(int leftarmport, int rightarmport, int rollerport) {
		leftArm = new Victor(leftarmport);
		rightArm = new Victor(rightarmport);
		rollers = new Talon(rollerport);
	}
	
	public void setSpeed(double speed) {
		leftArm.set(speed);
		rightArm.set(-speed);
		rollers.set(speed);
	}
	
}
