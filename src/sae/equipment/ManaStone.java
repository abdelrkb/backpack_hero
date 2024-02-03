package sae.equipment;

import java.util.Objects;

public class ManaStone {
	private final String name;
	private final String effect;
	
	public ManaStone(String name, String effect) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(effect);
		
		this.name = name;
		this.effect = effect;
	}
	
	
	//-------Accesseur---------
	public String getEffect() {return effect;}
	
	public void add() {}
}
