package net.minecraft.minechem;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.mod_Minechem;

public class Molecule {
	
	public ItemStack stack;
	public int atoms;
	public int elementId;
	public String name;
	public String elementName;
	public boolean isCompound;
	
	public static Molecule elementByFormula(String formula, int atoms) {
		String[][] elements = ItemTestTube.elements;
		for(int i = 0; i < elements.length; i++) {
			if(elements[i][0].equals(formula)) {
				return new Molecule(i, atoms);
			}
		}
		
		return null;
	}
	
	public static Molecule moleculeByFormula(String formula) {
		return new Molecule(0, 1, formula);
	}
	
	public static Molecule moleculeOrElementByFormula(String formula) {
		Pattern pattern = Pattern.compile("([A-Z][a-z]*)([0-9]*)");
		Matcher matcher = pattern.matcher(formula);
		if(isFormulaCompound(formula)) {
			return moleculeByFormula(formula);
		} else {
			while(matcher.find()) {
				String chemicalName = matcher.group(1);
				String atomsStr = matcher.group(2);
				int atoms = 1;
				if(!atomsStr.equals(""))
					atoms = Integer.valueOf(atomsStr);
				return elementByFormula(chemicalName, atoms);
			}
		}
		
		return null;
	}
	
	public static Molecule moleculeByItemStack(ItemStack itemstack) {
		if(itemstack == null)
			return null;
		
		NBTTagCompound tagCompound = itemstack.getTagCompound();
		int id = itemstack.getItemDamage();
		if(tagCompound != null) {
			String formula = tagCompound.getString("chemicalname");
			if(id == 0) {
				return moleculeByFormula(formula);
			} else {
				int atoms = tagCompound.getInteger("atoms");
				return elementByFormula(ItemTestTube.elements[id][0], atoms);
			}
		} else {
			return null;
		}
	}
	
	public static boolean isFormulaCompound(String formula) {
		Pattern pattern = Pattern.compile("([A-Z][a-z]*)([0-9]*)");
		Matcher matcher = pattern.matcher(formula);
		int count = 0;
		while(matcher.find()) {
			count++;
		}
		
		return count > 1 ? true : false;
	}
	
	public boolean isCompound() {
		return stack.getItemDamage() == 0 ? true : false;
	}
	
	public Molecule(int elementId, int atoms) {
		this.elementId = elementId;
		this.stack = new ItemStack(mod_Minechem.itemTesttube, 1, elementId);
		this.atoms = atoms;
		this.isCompound = false;
		if(atoms != -1)
		{
			int atomicNumber = this.stack.getItemDamage();
			String name = ItemTestTube.elements[atomicNumber][0];
			String number = Integer.toString(atoms);
			if(atoms == 1)
				number = "";
			
			int decayTime = -1;
			if(atomicNumber > 82)
				decayTime = (35-(atomicNumber-84)) * ItemTestTube.halfLifeTicks;
				//decayTime = (int)Math.log( 35 - ((atomicNumber + 0.99) - 85) ) * ItemTestTube.halfLifeTicks;
			
			this.name = name + number;
			this.elementName = name;
			NBTTagCompound tagCompound = new NBTTagCompound();
			tagCompound.setString("chemicalname", this.name);
			tagCompound.setInteger("atoms", atoms);
			tagCompound.setInteger("decaytime", decayTime);
			this.stack.setTagCompound(tagCompound);
		}
	}
	
	public Molecule(int elementId, int atoms, String chemicalname) {
		this.elementId = elementId;
		this.atoms = atoms;
		this.stack = new ItemStack(mod_Minechem.itemTesttube, 1, elementId);
		this.name = chemicalname;
		if(elementId == 0)
			this.isCompound = true;
		else
			this.isCompound = false;
		NBTTagCompound tagCompound = new NBTTagCompound();
		try {
			tagCompound.setString("fullChemicalName", mod_Minechem.findChemicalName(chemicalname));
		} catch(IOException e) {
			e.printStackTrace();
		}
		tagCompound.setString("chemicalname", chemicalname);
		tagCompound.setInteger("atoms", atoms);
		tagCompound.setInteger("decaytime", -1);
		this.stack.setTagCompound(tagCompound);
	}
	
	public Molecule(ItemStack itemstack)
	{
		if(itemstack == null)
			return;
		
		this.stack = itemstack;
		this.elementId = itemstack.getItemDamage();
		NBTTagCompound tagCompound = itemstack.getTagCompound();
		if(tagCompound != null) {
			this.name = tagCompound.getString("chemicalname");
			if(elementId == 0) {
				isCompound = true;
			} else {
				isCompound = false;
			}
		} else {
			
		}
		
		if(tagCompound != null)
		{
			this.atoms = tagCompound.getInteger("atoms");
			this.name = tagCompound.getString("chemicalname");
		} else {
			this.atoms = 1;
			this.name = ItemTestTube.getFormula(itemstack);
		}
		
		this.elementName = this.name.replaceAll("[0-9]", "");
		this.isCompound = false;
	}
	
	public void setAtoms(int amount) {
		this.atoms = amount;
		NBTTagCompound tagCompound = this.stack.getTagCompound();
		if(tagCompound != null)
		{
			tagCompound.setInteger("atoms", atoms);
			if(!isCompound) {
				name = elementName + atoms;
				tagCompound.setString("chemicalname", name);
			}
			this.stack.setTagCompound(tagCompound);
		}
	}
	
	public ItemStack decrAtoms(int amount){
		setAtoms( this.atoms - amount );
		if(this.atoms <= 0){
			return new ItemStack(mod_Minechem.itemTesttubeEmpty, 1);
		}
		
		return this.stack;
	}

}
