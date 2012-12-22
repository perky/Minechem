package ljdp.minechem.api.core;

import ljdp.minechem.api.util.Constants;
import ljdp.minechem.common.utils.MinechemHelper;


public enum EnumRadioactivity {
	stable					(MinechemHelper.getLocalString("element.property.stable"), 0, 0),
	hardlyRadioactive		(MinechemHelper.getLocalString("element.property.veryLowRadiation"), 	Constants.TICKS_PER_DAY,	1),
	slightlyRadioactive		(MinechemHelper.getLocalString("element.property.lowRadiation"), 		Constants.TICKS_PER_HOUR,	2),
	radioactive				(MinechemHelper.getLocalString("element.property.mediumRadiation"), 	Constants.TICKS_PER_MINUTE * 10,	4),
	highlyRadioactive		(MinechemHelper.getLocalString("element.property.highRadiation"), 		Constants.TICKS_PER_SECOND * 30,	8),
	extremelyRadioactive	(MinechemHelper.getLocalString("element.property.veryHighRadiation"), 	Constants.TICKS_PER_SECOND * 10,	16);
	
	private String descriptiveName;
	private int life;
	private int damage;
	private EnumRadioactivity(String descriptiveName, int life, int damage) {
		this.descriptiveName = descriptiveName;
		this.life = life;
		this.damage = damage;
	}
	
	public String getDescriptiveName() {
		return this.descriptiveName;
	}
	
	public int getLife() {
		return this.life;
	}
	
	public int getDamage() {
		return this.damage;
	}
}
