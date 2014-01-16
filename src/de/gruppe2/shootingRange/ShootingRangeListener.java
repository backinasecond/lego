package de.gruppe2.shootingRange;

public interface ShootingRangeListener {
	public void shootSuccess();
	public void shootFail();
	public void shootInvalidAngle();
	public void error(String message);
}
