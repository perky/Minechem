package ljdp.minechem.common.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class BoundedInventory implements IInventory {

    private final IInventory _inv;
    private final int _start;
    private final int _end;

    public BoundedInventory(IInventory inv, int start, int end) {
        if (inv == null) throw new IllegalArgumentException("inv: must not be null");
        if (start < 0 || start >= inv.getSizeInventory()) throw new IllegalArgumentException("start: out of bounds");
        if (end <= 0 || end > inv.getSizeInventory()) throw new IllegalArgumentException("end: out of bounds");
        if (start >= end) throw new IllegalArgumentException("start/end: overlap");

        _inv = inv;
        _start = start;
        _end = end;
    }

    @Override
    public int getSizeInventory() {
        return _end - _start;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return _inv.getStackInSlot(_start + slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return _inv.decrStackSize(_start + slot, amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return _inv.getStackInSlotOnClosing(_start + slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        _inv.setInventorySlotContents(_start + slot, stack);
    }

    @Override
    public String getInvName() {
        return _inv.getInvName();
    }

    @Override
    public int getInventoryStackLimit() {
        return _inv.getInventoryStackLimit();
    }

    @Override
    public void onInventoryChanged() {
        _inv.onInventoryChanged();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return _inv.isUseableByPlayer(player);
    }

    @Override
    public void openChest() {
        _inv.openChest();
    }

    @Override
    public void closeChest() {
        _inv.closeChest();
    }
}
