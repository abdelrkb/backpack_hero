package sae.equipment;

import java.util.Objects;

public class Protection implements Equipment{
	private final String name; //Nom de l'arme
	private final int shieldProtection; //point de protection de l'objet
	private final int energyCost; //cout en energie
	private final int price; //prix si achat dans boutique
	private final String rarity;
	private final String sideEffect;
	private final int manaCost;
	private int id; // cout en mana
	
	public Protection(String name, int shield, int energyCost, int price, String rarity, String sideEffect, int manaCost, int id) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(shield);
		Objects.requireNonNull(energyCost);
		Objects.requireNonNull(price);
		Objects.requireNonNull(rarity);
		if (shield < 0 || energyCost < 0 || price < 0 || manaCost < 0) {
			throw new IllegalArgumentException("Cette valeur ne peut pas être négative");
		}
		this.name = name;
		this.shieldProtection=shield;
		this.energyCost = energyCost;
		this.price = price;
		this.rarity = rarity;
		this.sideEffect = sideEffect;
		this.manaCost = manaCost;
		this.id = id;
	} 
	
	//Constructeur si pas de sideEffect ou manacost
	public Protection(String name, int shield, int energyCost, int price, String rarity, int id) {
		this(name, shield, energyCost, price, rarity, "None", 0, id);
	}
	
	//---------------Accesseur------------------
	public int itemGetShield(){return shieldProtection;}
	
	public int itemGetEnergyCost(){return energyCost;}
	
	public int itemGetPrice() {return price;}
	
	public int itemGetManaCost() {return manaCost;}
	
	public String itemGetSideEffect() {return sideEffect;}
	
	public String itemGetRarity() {return rarity;}
	
	public int itemGetId() {return id;}
	
	//----------------Methode add(interface)------------------
	public void add() {}
	public int itemGetDamage() {return 0;}

	@Override
	public int itemGetHeal() {
		// TODO Auto-generated method stub
		return 0;
	}

	

}
