package sae.enemy;

import sae.equipment.Equipment;

public interface Enemy {
	public int getLifePoint() ;
	public int getAttackDamage() ;
	public int getShieldProtection();
	public int getShieldPoint();
	public void receiveDamage(Equipment woodenSword);
	public void receiveProtection();
}
