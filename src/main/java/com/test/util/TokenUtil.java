package com.test.util;

import java.util.HashSet;
import java.util.Set;

public final class TokenUtil {

	
	public static int MAX_TOKENS_LENGTH = 5000;
	
	public static String INDICE_TITULO_AUTOR = "INDICE_TITULO_AUTOR";
	
	public static String tokenize(String phrase) {
		StringBuilder tokens = new StringBuilder();
		try {
			for (String word : phrase.split(" ")) {
				if (word.length() < 1) {
					continue;
				}
				int j = 1;
				while (true) {
					for (int i = 0; i < word.length() - j + 1; i++) {
						tokens.append(word.substring(i, i + j)).append(" ");
					}
					if (j == word.length()) {
						break;
					}
					j++;
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
		return tokens.toString();
	}
	
	
	
	public static String getSearchTokens(String text) {

        StringBuilder tokenized = new StringBuilder();
        Set tokens = new HashSet();
        String[] words = text.trim().split("\\s+");
        String token;
        Integer pos;

        for (String word : words) {
            if (tokens.size() > MAX_TOKENS_LENGTH) {
                break;
            }

            for (pos = 1; pos <= word.length(); pos++) {
                token = word.substring(0, pos);
                if (!tokens.contains(token)) {
                    tokens.add(token);
                    tokenized.append(token).append(" ");
                }
            }

        }

        return tokenized.toString();
	}
	
}
