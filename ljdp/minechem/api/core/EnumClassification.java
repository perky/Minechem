package ljdp.minechem.api.core;

import ljdp.minechem.common.utils.MinechemHelper;

public enum EnumClassification {
	nonmetal	("Non-metal"),
	inertGas	("Inert gas"),
	halogen		("Halogen"),
	alkaliMetal	("Alkali metal"), 
	alkalineEarthMetal	("Alkaline earth metal"),
	semimetallic		("Semimetallic"),
	otherMetal			("Other metal"),
	transitionMetal		("Transition metal"),
	lanthanide			("Lanthanide"),
	actinide	("Actinide"),
	solid		(MinechemHelper.getLocalString("element.property.solid")),
	gas			(MinechemHelper.getLocalString("element.property.gas")),
	liquid		(MinechemHelper.getLocalString("element.property.liquid"));
	
	
	private final String descriptiveName;
	EnumClassification(String descriptiveName) {
		this.descriptiveName = descriptiveName;
	}
	
	public String descriptiveName() {
		return descriptiveName;
	}
}
