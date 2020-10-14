package ru.otus;

public class MyMapInt {

    private final int size;
    private static final int STEP_LIMIT = 120;
    private final String[] entriesKey;
    private final int[] entriesValue;
    private final boolean[] entriesOccupied;

    public MyMapInt(int size) {
        this.size = size * 2;
        entriesKey = new String[this.size];
        entriesValue = new int[this.size];
        entriesOccupied = new boolean[this.size];
    }

    public void put(String key, int value) {
        var index = calcIndex(key);

        this.entriesKey[index] = key;
        this.entriesValue[index] = value;
        this.entriesOccupied[index] = true;
    }

    public int get(String key) {
        var step = 0;
        int index;
        do {
            index = getBaseIndex(key, step++);
        } while (!key.equals(entriesKey[index]));
        return entriesValue[index];
    }

    private int getBaseIndex(String key, int step) {
        if (step >= STEP_LIMIT) {
            throw new OperationTakesToManySteps("Can't execute operation for the key:" + key);
        }
        int hash = key.hashCode();
        return (((hash + hash * ++step) & 0x7fffffff) % this.size);
    }

    private int calcIndex(String key) {
        int step = 0;
        int index;
        do {
            index = getBaseIndex(key, step++);
        } while (entriesOccupied[index]);
        return index;
    }

    public static class OperationTakesToManySteps extends RuntimeException {
        public OperationTakesToManySteps(String msg) {
            super(msg);
        }
    }
}
