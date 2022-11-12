package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SymbolTable {
    private List<LinkedList<String>> hashTable;
    private int size;
    private Integer noOfElements;

    public SymbolTable(int size) {
        this.size = size;
        this.hashTable = new ArrayList<>();
        noOfElements = 0;

        for(int i = 0; i < size; i++)
            this.hashTable.add(new LinkedList<>());
    }

    public int size() {
        return size;
    }

    private void rehash() {
        int newSize = 2 * size;

        List<LinkedList<String>> newHashTable = new ArrayList<>();
        for(int i = 0; i < 2 * size; i++)
            newHashTable.add(new LinkedList<>());

        for (int i = 0; i < size; i++) {
            for (String key : hashTable.get(i)) {
                int hashValue = hash(key, newSize);
                newHashTable.get(hashValue).add(key);
            }
        }

        hashTable = newHashTable;
        this.size = newSize;
    }

    private int hash(String key, int size) {
        int sum = 0;
        for(int i = 0; i < key.length(); i++) {
            sum += key.charAt(i);
        }
        return sum % size;
    }

    public boolean add(String key){

        int hashValue = hash(key, this.size);
        noOfElements++;
        double loadFactor = (double) noOfElements / size;
        if (loadFactor > 0.75)
            rehash();

        if(!hashTable.get(hashValue).contains(key)){
            hashTable.get(hashValue).add(key);
            return true;
        }
        return false;
    }

    public boolean contains(String key){
        int hashValue = hash(key, this.size);

        return hashTable.get(hashValue).contains(key);
    }

    public Pair<Integer, Integer> position(String key){
        if (this.contains(key)){
            int positionBasedOnHash = this.hash(key, size);
            int listPosition = -1;

            if (hashTable.get(positionBasedOnHash).contains(key)) {
                listPosition = hashTable.get(positionBasedOnHash).indexOf(key);
            }

            return new Pair<>(positionBasedOnHash, listPosition);
        }
        return new Pair<>(-1, -1);
    }

    public boolean remove(String key){
        int hashValue = hash(key, size);

        if(hashTable.get(hashValue).contains(key)){
            hashTable.get(hashValue).remove(key);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Symbol Table: \n");

        for (int i = 0; i < size; i++) {
            string.append(i).append(": {");

            hashTable.get(i).forEach(a -> string.append(a).append(", "));

            string.append("}\n");
        }
        return string.toString();
    }
}