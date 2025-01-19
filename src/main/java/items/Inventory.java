package items;

import containers.LinkedList;


/**
 * An Inventory is composed of n slots. Each slot may store only
 * one type of item--specified by *slots*.
 * <p>
 * Once all slots are filled, no additional Item types may be
 * stored. Individual slots may contain any number of the same
 * Item--if the Item is stackable.
 */
public class Inventory
{
    /**
     * This is the Default Inventory size.
     */
    public static final int DEFAULT_SIZE = 10;

    /**
     * Individual item slots--each ItemStack occupies one slot.
     */
    private LinkedList<ItemStack> slots;

    /**
     * Total number of distinct Item types that can be stored.
     */
    private int capacity;


    /**
     * This is utility function that takes two ItemStacks and adds the
     * number of items in the right-hand side stack to the left-hand side stack.
     *
     * @param lhs stack whose size will be increased
     * @param rhs stack whose size we need to examine
     */
    public static void mergeStacks(ItemStack lhs, ItemStack rhs)
    {
        lhs.addItems(rhs.size());
    }

    

    /**
     * Default to an inventory with 10 slots.
     */
    public Inventory()
    {
        this(DEFAULT_SIZE);
    }

    /**
     * Create an inventory with n slots.
     *
     * @param desiredCapacity size of the new Inventory
     */
    public Inventory(int desiredCapacity)
    {
        this.slots    = new LinkedList<ItemStack>();
        this.capacity = desiredCapacity;
    }

    /**
     * Determine the number of slots currently in use.
     */
    public int utilizedSlots()
    {
        return this.slots.currentSize;
    }

    /**
     * Determine the number of empty (unused) slots.
     */
    public int emptySlots()
    {
        return this.totalSlots() - this.utilizedSlots();
    }

    /**
     * Retrieve the capacity (number of distinct types of items) that this
     * inventory can store.
     */
    public int totalSlots()
    {
        return this.capacity;
    }

    /**
     * Determine if the inventory is considered full.
     *
     * @return true if the current size is equal to capacity
     */
    public boolean isFull()
    {
        if (this.utilizedSlots() == this.totalSlots()) { 
            return true;  
        }
        return false;
    }

    /**
     * Determine if the inventory is empty.
     *
     * @return true if current size is zero
     */
    public boolean isEmpty()
    {
        return this.slots.currentSize == 0;
    }

    /**
     * Search through all slots (Nodes in the LinkedList) and look for a
     * matching ItemStack.
     *
     * @param key stack for which the search is being conducted
     *
     * @return matching stack if one was found and `null` otherwise
     */
    public ItemStack findMatchingItemStack(ItemStack key)
    {
        LinkedList.Node current = this.slots.head;

        while (current != null) {
            ItemStack currentItemStack = (ItemStack) current.data; // cast to itemstack so current is still able to use .next
            if (currentItemStack.getItem().equals(key.getItem())) { // checking if the item type is the same
                return currentItemStack;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * This is the standard Linked List append operation from Review 01
     *
     * @param toAdd data that we want to store in a Node and add to the list
     */
    public void addItemStackNoCheck(ItemStack toAdd)
    {
        LinkedList.Node<ItemStack> newNode = new LinkedList.Node<>(toAdd); // noCheck means we are not using findMatchingItemStackatchingItemStack

        // if adding the first node
        if (this.slots.head == null) {
            this.slots.head = newNode;
            this.slots.tail = newNode;
            this.slots.currentSize = 1;
        }

        // linking the newNode to the end of an existing list
        else {
            this.slots.tail.next = newNode;
            this.slots.tail = this.slots.tail.next;
            ++this.slots.currentSize;
        }
    }

    /**
     * Add one or more items to the inventory list.
     *
     * @param stack new stack of items to add
     *
     * @return true if *stack* was added and false otherwise
     */
    public boolean addItems(ItemStack stack)
    {
        ItemStack match = this.findMatchingItemStack(stack);

        // if a match was found
        if (match != null) {
            // If the Item is stackable, add it to the ItemStack
            if (match.permitsStacking()) {
                mergeStacks(match, stack);

                return true;
            }
        }

        if (this.slots.currentSize < capacity) {
            this.addItemStackNoCheck(stack);
            return true;
        }

        return false;
    }

    /**
     * *Print* a Summary of the Inventory and all Items contained within.
     */
    @Override
    public String toString()
    {
        String summaryLine = String.format(
            " -Used %d of %d slots%n", this.slots.currentSize, this.capacity
        );

        StringBuilder strBld = new StringBuilder();
        strBld.append(summaryLine);

        LinkedList.Node<ItemStack> it = this.slots.head;

        while (it != null) {
            String itemLine = String.format("  %s%n", it.data);
            strBld.append(itemLine);

            it = it.next;
        }

        return strBld.toString();
    }
}
