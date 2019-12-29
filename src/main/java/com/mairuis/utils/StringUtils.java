package com.mairuis.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mairuis
 * @date 2019/12/8
 */
public class StringUtils {

    public static int editDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();
        if (n * m == 0)
            return n + m;
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i < n + 1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j < m + 1; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {
                int left = dp[i - 1][j] + 1;
                int down = dp[i][j - 1] + 1;
                int left_down = dp[i - 1][j - 1];
                if (word1.charAt(i - 1) != word2.charAt(j - 1))
                    left_down += 1;
                dp[i][j] = Math.min(left, Math.min(down, left_down));

            }
        }
        return dp[n][m];
    }

    public static int longestCommonString(String wa, String wb) {
        int max = 0;
        int[][] dp = new int[wa.length()][wb.length()];
        dp[0][0] = wa.charAt(0) == wb.charAt(0) ? 1 : 0;
        for (int i = 1; i < wa.length(); i += 1) {
            for (int j = 1; j < wb.length(); j += 1) {
                dp[i][j] = wa.charAt(i - 1) == wb.charAt(j - 1) ? dp[i - 1][j - 1] + 1 : 0;
                max = Math.max(max, dp[i][j]);
            }
        }
        return max;
    }

    public static Map<String, String> argsToMap(String[] args) {
        Map<String, String> map = new HashMap<>(args.length);
        for (String arg : args) {
            map.put(arg.split("=")[0], arg.split("=")[1]);
        }
        return map;
    }

    public static void main(String[] args) {
        System.out.println(longestCommonString("abbac", "cabbad"));
    }
}
