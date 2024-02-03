package sae.hero;

import sae.enemy.Enemy;
import sae.equipment.Protection;

public class Hero {
	private int lifePoint;
	private int protectionPoint;
	private int energyPoint;
	private int manaPoint; 
	
	public Hero() {
		this.lifePoint = 40;
		this.protectionPoint = 5;
		this.energyPoint =9;
		this.manaPoint = 3;
	}
	
	public void receiveDamage(Enemy enemy) {
		if (protectionPoint >= enemy.getAttackDamage()) {
			protectionPoint -= enemy.getAttackDamage();
		}
		else if (protectionPoint > 0 && protectionPoint < enemy.getAttackDamage()){
			lifePoint -= enemy.getAttackDamage() - protectionPoint;
			protectionPoint = 0;
		}
		else {
			lifePoint -= enemy.getAttackDamage();

		}
	}
	
	public void decreaseEP(int x) {
		energyPoint -= x;
	}
	
	public void receiveProtection(int protection) {
		protectionPoint += protection;
	}
	
	public void receiveLife(int life) {
		lifePoint += life;
	}
	
	public int getLife() {
		return lifePoint;
	}
	
	public String getPV() {
		return lifePoint + "";	} 
	
	public int getProtection() {
		return protectionPoint;
	}
}

