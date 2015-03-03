package org.usfirst.frc.team3925.robot;

import edu.wpi.first.wpilibj.Joystick;

public class ToggleButton {

	final Joystick stick;
	final int btn;
	
	private boolean lastBtn, state;
	
	public ToggleButton(Joystick stick, int btn) {
		this.stick = stick;
		this.btn = btn;
		lastBtn = false;
		state = false;
	}
	
	public void update() {
		boolean pressed = stick.getRawButton(btn);
		if (pressed != lastBtn && pressed) {
			state = !state;
		}
		lastBtn = pressed;
	}
	
	public void reset() {
		state = false;
	}
	
	public boolean get() {
		return state;
	}
	
}
