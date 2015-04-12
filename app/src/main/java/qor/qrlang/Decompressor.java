package qor.qrlang;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Decompressor {

    public static String baseVariables = "keyup = keydown = keyleft = keyright = keyspace = false; $A = $B = $C = $D = $E = $F = $G = $H = $I = $J = $K = $L = $M = $N = $O = $P = $Q = $R = $S = $T = $U = $V = $W = $X = $Y = $Z = $0 = $1 = $2 = $3 = $4 = $5 = $6 = $7 = $8 = $9 = 0;";

    private String[][] matches = {
            {";Android.drawbox(","DB"},
            {";Android.write(", "WR"},
            {"Android.key('up')", "keyup"},
            {"Android.key('down')", "keydown"},
            {";Android.log(", "LG"},
            {"new Array(", "AR"},
            {";draw = function(){", "DR"},
            {";update = function(){", "UP"},
            {"function(){", "FU"},
            {"\n\rif( ", "IF"},
            {"\n\relse", "EL"},
            {"\n\relse if(", "EI"},
            {";while(", "WH"},
            {";for", "FR"},
            {"keyup", "KU"},
            {"keydown", "KD"},
            {"keyleft", "KL"},
            {"keyright", "KR"},
            {"keyspace", "KS"},
            {"keyenter", "KE"},
            {"tiltup", "TU"},
            {"tiltdown", "TD"},
            {"tiltleft", "TL"},
            {"tiltright", "TR"},
            {">=", "GE"},
            {"<=", "LE"},
            {">", "GT"},
            {"<", "LT"},
            {"==", "EE"},
            {"||", "OR"},
            {"~", "TI"},
            {"!=", "NE"},
            {"!", "NO"},
            {"&&", "AN"},
            {"*=", "ME"},
            {"/=", "DE"},
            {"*", "MU"},
            {"/", "DI"},
            {"%", "MO"},
            {"\\", "ES"},
            {"(", "LB"},
            {")", "RB"},
            {"{\n", "*"},
            {"}\n", "."},
            {"[","LS"},
            {"]","RS"},
            {",", "/"},
            {"=","%"},
            {";\n", ":"}

    };


    //Decompresses given code to valid javascript code
    public String decompress(String code){
        code = code.replace("QS","'");

        ArrayList<String> quotes = getQuotes(code);
        code = removeQuotes(code);

        code = code.replaceAll("\\$([A-Z])(?=\\$)|\\d+(?=\\$)", "$0;");
        code = code.replaceAll("\\+\\+|--", "$0;");
        code = code.replaceAll("\\+;RB", "+RB");

        code = code.replaceAll("\\$[A-Z0-9]|[A-Z]{2}", " $0 ");

        code = replaceSpecialCharacters(code);
        code = replaceQuotes(code, quotes);

        return code;
    }

    //Replaces the quote back into the code
    private String replaceQuotes(String code, ArrayList<String> quotes) {
        for(int i = 0; i < quotes.size(); i++){
            String quote = quotes.get(i);
            code = code.replace("?" + (i+1), quote);
        }

        return code;
    }

    //Replaces all special strings with their corresponding matches
    private String replaceSpecialCharacters(String code){
        for(int i = 0; i < matches.length; i++){
            String[] pair = matches[matches.length - i - 1];
            code = code.replaceAll(Pattern.quote(pair[1]),pair[0]);
        }
        return code;
    }

    //Retrieves all quoted sectipns from the code
    private ArrayList<String> getQuotes(String code){
        ArrayList<String> quotes = new ArrayList<String>();

        Matcher matcher = Pattern.compile("'(.*?)'").matcher(code);
        while(matcher.find()) quotes.add(matcher.group());

        return quotes;
    }

    //Remove quoted sections from the code
    private String removeQuotes(String code){
        Matcher matcher = Pattern.compile("'(.*?)'").matcher(code);
        int i = 1;
        while(matcher.find()){
            code = code.replace(matcher.group(), "?" + i);
            i++;
        }

        return code;
    }

}
