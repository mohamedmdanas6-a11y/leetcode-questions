

class Solution {
    public String firstPalindrome(String[] words) {
        for (String word : words) {
            StringBuilder sb = new StringBuilder(word);
            if (word.equals(sb.reverse().toString())) {
                return word;
            }
        }
        return "";
    }
}


