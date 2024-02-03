package sae.enemy;

import sae.equipment.Equipment;

public class LivingShadow implements Enemy {
	private int lifePoint;
	private int shieldPoint;
	private final int attackDamage;
	private final int shieldProtection;
	
	public LivingShadow() {
		this.lifePoint = 50;
		this.shieldPoint = 2;
		this.attackDamage = 5;
		this.shieldProtection = 3;
	}
	
	
	//----------Accesseur----------------
	public int getLifePoint() {return lifePoint;}
	public int getAttackDamage() {return attackDamage;}
	public int getShieldProtection() {return shieldProtection;}
	public int getShieldPoint() {return shieldPoint;}

	
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
	@Override
	public String toString() {
		return "LivingShadow";
}
	}
