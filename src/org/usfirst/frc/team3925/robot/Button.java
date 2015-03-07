package org.usfirst.frc.team3925.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Button {
	final Joystick stick;
	final int btn;
	
	private boolean lastBtn;
	
	public Button(Joystick stick, int btn) {
		this.stick = stick;
		this.btn = btn;
		lastBtn = false;
	}
	
	public boolean get() {
		boolean ret = false;
		boolean pressed = stick.getRawButton(btn);
		if (pressed != lastBtn && pressed) {
			ret = true;
		}
		lastBtn = pressed;
		return ret;
	}
	
}
