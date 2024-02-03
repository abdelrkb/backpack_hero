package sae.equipment;

import java.util.Objects;

public class Gold implements Equipment{
	private int number;
	
	public Gold(int number) {
		Objects.requireNonNull(number);
		this.number = number;
	}
	
	public  void increaseGold(int nb) {
		number += nb;
	}
	public  void decreaseGold(int nb) {
		number -= nb;
	}
	//-------Accesseur----------
	public int getNumberOfGold() {return number;}
	
	
	public void add() {
	}
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
	public int itemGetPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int itemGetHeal() {
		// TODO Auto-generated method stub
		return 0;
	};

}
