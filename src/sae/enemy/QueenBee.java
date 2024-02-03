package sae.enemy;

import sae.equipment.Equipment;

public class QueenBee implements Enemy{
	private int lifePoint;
	private int shieldPoint;
	private final int attackDamage;
	private final int shieldProtection;
	
	public QueenBee() {
		this.lifePoint = 75;
		this.attackDamage = 7;
		this.shieldPoint = 0;
		this.shieldProtection = 2;
	}
	//----------Méthode----------------
	public void receiveDamage(Equipment equipment) {
		if (shieldPoint > equipment.itemGetDamage()) {
			shieldPoint -= equipment.itemGetDamage();
		}
		else if (shieldPoint > 0 && shieldPoint < equipment.itemGetDamage()) {
			lifePoint -= equipment.itemGetDamage() - shieldPoint;
			shieldPoint = 0;
		}
		else {
			lifePoint -= equipment.itemGetDamage();
		}
	}
	
	public void receiveProtection() {
		shieldPoint += shieldProtection;
	}
	//----------Accesseur----------------
	public int getLifePoint() {return lifePoint;}
	public int getAttackDamage() {return attackDamage;}
	public int getShieldProtection() {return shieldProtection;}
	public int getShieldPoint() {return shieldPoint;}

	
	@Override
	public String toString() {
		return "QueenBee";
	}
}
