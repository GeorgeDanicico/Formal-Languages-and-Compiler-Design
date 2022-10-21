package model;

import java.util.ArrayList;
import java.util.LinkedList;

public class SymbolTable {
    private ArrayList<LinkedList<String>> hashTable;
    private int size;

    public SymbolTable(int size) {
        this.size = size;
        this.hashTable = new ArrayList<>();
        for(int i=0;i<size;++i) this.hashTable.add(new LinkedList<>());
    }

    public int size() {
        return size;
    }

    private int hash(String key) {
        int sum = 0;
        for(int i = 0; i < key.length(); i++) {
            sum += key.charAt(i);
        }
        return sum % size;
    }

    public boolean add(String key){

        int hashValue = hash(key);

        if(!hashTable.get(hashValue).contains(key)){
            hashTable.get(hashValue).add(key);
            return true;
        }
        return false;
    }

    public boolean contains(String key){
        int hashValue = hash(key);

        return hashTable.get(hashValue).contains(key);
    }

    public Pair<Integer, Integer> position(String key){
        if (this.contains(key)){
            int positionBasedOnHash = this.hash(key);
            int listPosition = -1;

            if (hashTable.get(positionBasedOnHash).contains(key)) {
                listPosition = hashTable.get(positionBasedOnHash).indexOf(key);
            }

            return new Pair<>(positionBasedOnHash, listPosition);
        }
        return new Pair<>(-1, -1);
    }

    public boolean remove(String key){
        int hashValue = hash(key);

        if(hashTable.get(hashValue).contains(key)){
            hashTable.get(hashValue).remove(key);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();

        for (int i = 0; i < size; i++) {
            string.append(i).append(": {");

            hashTable.get(i).forEach(a -> string.append(a).append(", "));

            string.append("}\n");
        }
        return string.toString();
    }
}