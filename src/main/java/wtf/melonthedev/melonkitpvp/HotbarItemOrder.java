package wtf.melonthedev.melonkitpvp;

public class HotbarItemOrder {


    public Type slot1;
    public Type slot2;
    public Type slot3;
    public Type slot4;
    public Type slot5;
    public Type slot6;
    public Type slot7;
    public Type slot8;
    public Type slot9;

    public HotbarItemOrder(Type... slots) {
        for (int i = 0; i < 9; i++) {
            if (i >= slots.length) continue;
            switch (i) {
                case 0 -> slot1 = slots[i];
                case 1 -> slot2 = slots[i];
                case 2 -> slot3 = slots[i];
                case 3 -> slot4 = slots[i];
                case 4 -> slot5 = slots[i];
                case 5 -> slot6 = slots[i];
                case 6 -> slot7 = slots[i];
                case 7 -> slot8 = slots[i];
                case 8 -> slot9 = slots[i];
            }
        }
    }

    public HotbarItemOrder setSlot(int i, Type slot) {
        switch (i) {
            case 0 -> slot1 = slot;
            case 1 -> slot2 = slot;
            case 2 -> slot3 = slot;
            case 3 -> slot4 = slot;
            case 4 -> slot5 = slot;
            case 5 -> slot6 = slot;
            case 6 -> slot7 = slot;
            case 7 -> slot8 = slot;
            case 8 -> slot9 = slot;
        }
        return this;
    }

    public HotbarItemOrder setSlot1(Type slot1) {
        this.slot1 = slot1;
        return this;
    }

    public HotbarItemOrder setSlot2(Type slot2) {
        this.slot2 = slot2;
        return this;
    }

    public HotbarItemOrder setSlot3(Type slot3) {
        this.slot3 = slot3;
        return this;
    }

    public HotbarItemOrder setSlot4(Type slot4) {
        this.slot4 = slot4;
        return this;
    }

    public HotbarItemOrder setSlot5(Type slot5) {
        this.slot5 = slot5;
        return this;
    }

    public HotbarItemOrder setSlot6(Type slot6) {
        this.slot6 = slot6;
        return this;
    }

    public HotbarItemOrder setSlot7(Type slot7) {
        this.slot7 = slot7;
        return this;
    }

    public HotbarItemOrder setSlot8(Type slot8) {
        this.slot8 = slot8;
        return this;
    }

    public HotbarItemOrder setSlot9(Type slot9) {
        this.slot9 = slot9;
        return this;
    }

    public int getSlot(Type type) {
        if (slot1 == type) return 0;
        if (slot2 == type) return 1;
        if (slot3 == type) return 2;
        if (slot4 == type) return 3;
        if (slot5 == type) return 4;
        if (slot6 == type) return 5;
        if (slot7 == type) return 6;
        if (slot8 == type) return 7;
        if (slot9 == type) return 8;
        return 18;
    }

    public enum Type {
        SWORD, BLOCKS, FOOD, GAPPELS, BUCKET, ROD, BOW, ARROWS, NONE, COBWEB, OTHERS, OTHERS2, OTHERS3
    }

}
