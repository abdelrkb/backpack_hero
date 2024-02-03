package sae.equipment.weapon;

import java.util.Objects;

import sae.equipment.Equipment;

public class RangeWeapon implements Equipment{
		private final String name; //Nom de l'arme
		private final int damage;
		private final int energyCost; //cout en energie
		private final int price; //prix si achat dans boutique
		private final String rarity;
		private final int manaCost; // cout en mana
private final int id;
		
		public RangeWeapon(String name,int damage, int energyCost, int price, String rarity,int manaCost, int id) {
			Objects.requireNonNull(name);
			Objects.requireNonNull(rarity);


			if (energyCost < 0 || price < 0 || manaCost < 0) {
				throw new IllegalArgumentException("Cette valeur ne peut pas être négative");
			}
			this.name = name;
			this.damage = damage;
			this.energyCost = energyCost;
			this.price = price;
			this.rarity = rarity;
			this.manaCost = manaCost;
			this.id = id;
		}
		
		
		
		//---------------Accesseur------------------
		
		public int itemGetEnergyCost(){return energyCost;}
		
		public int itemGetPrice() {return price;}
		
		public int itemGetManaCost() {return manaCost;}
		
		
		public String itemGetRarity() {return rarity;}
		
		
		//----------------Methode add(interface)------------------
		public void add() {}
		public int itemGetDamage() {return damage;}

		@Override
		public int itemGetId() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int itemGetShield() {
			// TODO Auto-generated method stub
			return 0;
		}



		@Override
		public int itemGetHeal() {
			// TODO Auto-generated method stub
			return 0;
		};

}
