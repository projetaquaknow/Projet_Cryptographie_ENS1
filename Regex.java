
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PrigentC
 */
public class Regex {
    
    public static void main(String[] args) throws IOException {
        File file = new File("filesignature.txt");
        FileInputStream fis = new FileInputStream(file);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        String inputLine;
    
        StringBuffer sb = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        
        Pattern p1 = Pattern.compile("^(.*)==");
        Matcher m1 = p1.matcher(sb.toString());
        
        Pattern p2 = Pattern.compile("C=(.*)[A-Z]");
        Matcher m2 = p2.matcher(sb.toString());
        
        if(m1.find()&&m2.find()) {
            System.out.println(m1.group(1)+"==\n");
            System.out.println("C=" + m2.group(1));
        }
    }
}
