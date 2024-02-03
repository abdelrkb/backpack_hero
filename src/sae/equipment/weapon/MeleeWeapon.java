package sae.equipment.weapon;

import java.util.Objects;

import sae.equipment.Equipment;

public class MeleeWeapon implements Equipment{
	private final String name; //Nom de l'arme
	private final int damage; //dommage de l'arme
	private final int energyCost; //cout en energie
	private final int price; //prix si achat dans boutique
	private final String rarity;
	private final String sideEffect;
	private final int manaCost; // cout en mana
	private final int size;
	private final int id;

	
	public MeleeWeapon(String name, int damage, int energyCost, int price, String rarity,String sideEffect,int manaCost, int size, int id) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(damage);
		Objects.requireNonNull(energyCost);
		Objects.requireNonNull(price);
		Objects.requireNonNull(rarity);


		if (damage < 0 || energyCost < 0 || price < 0 || manaCost < 0) {
			throw new IllegalArgumentException("Cette valeur ne peut pas être négative");
		}
		this.name = name;
		this.damage = damage;
		this.energyCost = energyCost;
		this.price = price;
		this.rarity = rarity;
		this.sideEffect = sideEffect;
		this.manaCost = manaCost;
		this.size = size;
		this.id = id;

	}
	
	//Constructeur si pas de sideEffect ou manacost
	public MeleeWeapon(String name, int damage, int energyCost, int price, String rarity, int size, int id) {
		this(name,damage,energyCost,price,rarity,"None",0, size, id);
	}
	
	//---------------Accesseur------------------
	public int itemGetDamage(){return damage;}
	
	public int itemGetEnergyCost(){return energyCost;}
	
	public int itemGetPrice() {return price;}
	
	public int itemGetManaCost() {return manaCost;}
	
	public String itemGetSideEffect() {return sideEffect;}
	
	public String itemGetRarity() {return rarity;}
	
	public int itemGetId() {return id;}
	
	//----------------Methode add(interface)------------------
	public void add() {}

	@Override
	public int itemGetShield() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int itemGetHeal() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
