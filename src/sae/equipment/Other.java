package sae.equipment;

import java.util.Objects;

public class Other implements Equipment{
	private final String name;
	private final int energyCost;
	private final int giveHeal;
	private final int price;
	private final int id;

	
	public Other(String name,int energyCost, int giveHeal, int price, int id) {
		Objects.requireNonNull(name);
		
		this.name = name;
		this.energyCost = energyCost;
		this.giveHeal = giveHeal;
		this.price = price;
		this.id = id;
	}


	@Override
	public void add() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int itemGetDamage() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int itemGetId() {
		// TODO Auto-generated method stub
		return id;
	}

	public int itemGetHeal() {
		return giveHeal;
	}

	@Override
	public int itemGetEnergyCost() {
		// TODO Auto-generated method stub
		return energyCost;
	}


	@Override
	public int itemGetShield() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int itemGetPrice() {
		return price;
	}
	

}
