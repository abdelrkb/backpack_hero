package sae.main;


import fr.umlv.zen5.KeyboardKey;
import sae.enemy.RatWolf;
import sae.equipment.weapon.MeleeWeapon;
import sae.map.Map;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	//	var weapon = new MeleeWeapon("Épée",4,3,125,"rarity");
	//	System.out.println(weapon.itemGetDamage());
	//	System.out.println(weapon.itemGetManaCost());
		var enemy = new RatWolf();
		System.out.println(enemy);
		
		var map = new Map();
		System.out.println(map);

	}

}
 
