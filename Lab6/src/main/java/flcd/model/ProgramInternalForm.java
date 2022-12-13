package flcd.model;

import flcd.common.Pair;
import flcd.common.Triplet;
import java.util.ArrayList;
import java.util.List;

public class ProgramInternalForm {
    private List<Triplet<String, Integer, Pair<Integer, Integer>>> programInternalForm;

    public ProgramInternalForm() {
        programInternalForm = new ArrayList<>();
    }

    public void add(String token, Integer tokenCode, Pair<Integer, Integer> symbolTablePosition) {
        programInternalForm.add(new Triplet(token, tokenCode, symbolTablePosition));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        int index = 0;
        for (var triplet : programInternalForm) {
            stringBuilder.append("Token: " + triplet.getFirst() + "; Token Code: " + triplet.getSecond() + "; "
                + "Symbol Table Position: " + triplet.getThird() + ";\n");
                    index ++;
        }

        return stringBuilder.toString();
    }

}
