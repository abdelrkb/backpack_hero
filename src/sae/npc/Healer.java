package sae.npc;
import java.util.Random;
import sae.hero.Hero;

public class Healer {
	
public Healer() {};
 
	public int healAmount(Hero hero) {
	    int minHealAmount = 5;
	    int maxHealAmount = 10;

	    Random random = new Random();
	    int healAmount = random.nextInt(maxHealAmount - minHealAmount + 1) + minHealAmount;

	    int currentHealth = hero.getLife();
	    int remainingHeal = 40 - currentHealth;

	    if (healAmount > remainingHeal) {
	        healAmount = remainingHeal; // Limiter la guérison pour ne pas dépasser la vie maximale
	    }
	    return healAmount;
	}
	
	public void giveHeal(Hero hero) {
		int healAmount = healAmount(hero);
		hero.receiveLife(healAmount);
	}
	
}
