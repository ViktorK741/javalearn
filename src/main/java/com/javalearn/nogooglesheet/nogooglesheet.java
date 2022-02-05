package com.javalearn.errorcatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/*
<SCRIPTINFO>
<document>without any doc</document>
<description>Данные по ошибкам сервиса ErrCather</description>
<customer>Потапенко А.В. DN210481PAV1</customer>
<business>Департамент розробки засобів автоматизації аналітичних систем</business>
<developer>Потапенко А.В. DN210481PAV1</developer>
<misc>IT_REGLAMENT.ERROR_CATCHER_DATA_RP</misc>
</SCRIPTINFO>
*/
public class nogooglesheet {


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

    public static int checkIsJar(File inputFile, String key) throws FileNotFoundException {
        int result = 0;
        Scanner scanner = new Scanner(inputFile);
        try {
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.contains(key)) {
                    result = 1;
                }
            }
        }finally {
            scanner.close();
        }
        return result;
    }

    public static void getFiles(String str, String mask, String key, String outputDataFile) throws IOException {
        File f = new File(str);
        int i = 1;

        try (FileWriter readyFile = new FileWriter(outputDataFile)) {
            for (File s : f.listFiles()) {
                if (s.isFile()) {
                    if(s.getName().contains(mask)) {
                        if(checkIsJar(s,key)==1) {
                            System.out.println(s.getName());
                            readyFile.write(Integer.toString(i)+"."+s.getPath()+"\n");
                            i++;
                        }
                    }
                } else if (s.isDirectory()) {
                    getFiles(s.getAbsolutePath(),mask,key,outputDataFile);
                }
            }
            readyFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    boolean main() {
        //execDate = formatDT("yyyyMMdd");
        //scriptsName = bsh.sysinfo.getScriptName();
        //setStartExecutionTime(execDate, scriptsName);

        try {
            Date date = new Date();
            SimpleDateFormat currentDateTime = new SimpleDateFormat("yyyyMMdd HH:mm:ss.mmm");

            String needPath = "/DATA/Gremlin/Scripts/";
            String needMask = ".bsh";
            String needKeyText = "DownloadGoogleSpreadsheet-1.01.jar";
            String outputDataFile = "/DATA/ftp/dn210481pav1/NoGoogleSheet/NO_GOOGLE_SHEET.TXT";

            getFiles(needPath,needMask,needKeyText,outputDataFile);
        }
        catch(RuntimeException | IOException ex ){
            System.out.println((//EmailGladush,
                    "anton.potapenko@privatbank.ua",
                    "ERROR in no_google_sheet.bsh Поиск скриптов, забирающих данные из гугл-таблиц",
                    "Ошибка при выполнении.\n\n" +
                            ex
            );
            return false;
        }
        //setFinishExecutionTime(execDate, scriptsName);
        return true;
    }
return main();
}
