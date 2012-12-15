package ljdp.minechem.common;

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
	solid		("Solid"),
	gas			("Gaseous"),
	liquid		("Liquid"),
	stable					("Stable"),
	hardlyRadioactive		("Hardly radioactive"),
	slightlyRadioactive		("Slightly radioactive"),
	radioactive				("Radioactive"),
	highlyRadioactive		("Highly radioactive"),
	extremelyRadioactive	("Extremely radioactive!");
	
	private final String descriptiveName;
	EnumClassification(String descriptiveName) {
		this.descriptiveName = descriptiveName;
	}
	
	public String descriptiveName() {
		return descriptiveName;
	}
}
