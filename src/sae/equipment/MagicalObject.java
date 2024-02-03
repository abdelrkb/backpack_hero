package sae.equipment;

import java.util.Objects;

public class MagicalObject implements Equipment{
	private final String name; //Nom de l'arme
	private final int price; //prix si achat dans boutique
	private final String rarity;
	private final String sideEffect;
	private final int manaCost; // cout en mana
	
	public MagicalObject(String name, int price, String rarity, String sideEffect, int manaCost) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(price);
		Objects.requireNonNull(rarity);
		Objects.requireNonNull(sideEffect);
		Objects.requireNonNull(manaCost);
		if (price < 0 || manaCost < 0) {
			throw new IllegalArgumentException("Cette valeur ne peut pas être négative");
		}
		this.name=name;
		this.price=price;
		this.rarity=rarity;
		this.sideEffect=sideEffect;
		this.manaCost = manaCost;
	}
	
	//---------------Accesseur------------------	
	public int itemGetPrice() {return price;}
	
	public int itemGetManaCost() {return manaCost;}
	
	public String itemGetSideEffect() {return sideEffect;}
	
	public String itemGetRarity() {return rarity;}
	
	
	//----------------Methode add(interface)------------------
	public void add() {}
	public int itemGetDamage() {return 0;}

	@Override
	public int itemGetId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int itemGetEnergyCost() {
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
