package com.github.pius.pichats.service.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TokenGenerator {

    public static String generateCode() {
        int length = Constants.TOKEN_LENGTH;
        return generateCodeString(length);
    }

    public static String generateCode(int length) {
        return generateCodeString(length);
    }

    private static String generateCodeString(int length) {
        String codeCharacters = "1234567890abcdefghijklmnopqrstuvwxyz";
        //Get random indices
        List<Integer> randomIndices = getRandomIndices(length, codeCharacters.length() - 1);

        StringBuilder code = new StringBuilder();

        //Generate code based on the random number from the codeCharacters
        for (int i = 0; i < length; i++) {
            code.append(codeCharacters.charAt(randomIndices.get(i)));
        }

        return code.toString();
    }


    private static List<Integer> getRandomIndices(int length, int range) {
        List<Integer> randomIndices = new ArrayList<>(length);
        Random random = new Random();

        //Populates the randomIndices with random integers between 0 to 61
        for (int i = 0; i < length; i++) {
            randomIndices.add(i, random.nextInt(range));
        }

        return randomIndices;
    }
}
