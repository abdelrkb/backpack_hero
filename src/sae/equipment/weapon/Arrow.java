package sae.equipment.weapon;

import java.util.Objects;

import sae.equipment.Equipment;

public class Arrow implements Equipment{
	private final String name;
	private final int damage;
	private final String sideEffect;
	
	public Arrow(String name, int damage, String sideEffect) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(damage);
		
		this.name = name;
		this.damage = damage;
		this.sideEffect=sideEffect;
	}
	
	public Arrow(String name, int damage) {
		this(name,damage,"None");
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
	public int itemGetPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int itemGetHeal() {
		// TODO Auto-generated method stub
		return 0;
	}
}
