package com.javalearn.errorcatcher;
/*
<SCRIPTINFO>
<document>without any doc</document>
<description>Данные по ошибкам сервиса ErrCather</description>
<customer>Потапенко А.В. DN210481PAV1</customer>
<business>Департамент розробки засоб?в автоматизац?ї анал?тичних систем</business>
<developer>Потапенко А.В. DN210481PAV1</developer>
<misc>IT_REGLAMENT.ERROR_CATCHER_DATA_RP</misc>
</SCRIPTINFO>
*/
public class errorcathcer {




import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

    //  Вывод сообщения о текущем процессе
    private static void printMessage(String text) {
        // System.out.println(text); // Для стандартной java environment
        print(text);            // Для balabit
    }

    //  Копирование файла лога в рабочий каталог
//  source      - лог-файл с данными на Gremlin
//  destination - копия лог-файла с данными для обработки в рабочем каталоге
    private static void copyLogFile(File source, File destination) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;
        printMessage("Log-file exists. Start copying...");
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destinationChannel = new FileOutputStream(destination).getChannel();
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
        finally {
            sourceChannel.close();
            destinationChannel.close();
            printMessage("Log-file has copied!");
        }
    }

    //  Удаление копии лог-файла с данными после парсинга
//  destination - файл для обработки парсером, удаляется после завершения парсинга
    private static void deleteFile(File destination) {
        if(destination.delete()){
            printMessage("Log-file delete.");
        }else printMessage("File doesn't exist!");
    }

    //  Изменение формата даты из DD-MM-YYYY в YYYY-MM-DD.
//  inputFormat - запись дата/время в формате DD-MM-YYYY
    private static String dateConvert(String inputFormat) {
        String newFormat = new String();
        newFormat = inputFormat.substring(6, 10) + '-' +
                inputFormat.substring(3, 5)  + '-' +
                inputFormat.substring(0, 2)  + ' ' +
                inputFormat.substring(11,23);
        newFormat = newFormat.replace(',', '.');

        return newFormat; //запись дата/время в формате YYYY-MM-DD
    }

    //  Считаем количество строк в файле для определения макисмального размеров массива с параметрами ошибок
//  file - входящий файл для анализа
    private static int countLines(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
        int lineNumber = 0;

        while (lineNumberReader.readLine() != null){
            lineNumber++;
        }
        return lineNumber; // количество строк в файле
    }

    //  Поиск ошибок в лог-файле, а также их запись в массив
//  file - входящий файл для анализа
//  arr  - массив с данными про ошибки
    private static void errorSearch(File file, String[][] arr) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int errorsCount = 0;

        try {
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("[ERROR] [main.script]") &&
                        !line.contains("handlers.time") &&
                        !line.contains("sql_school") &&
                        !line.contains("ftp") &&
                        !line.contains("data_calc_package")) {

                    // Расчитываем индексы интервала о датавремени срабатывания ошибки на сервере
                    int startErrorDate = 0;
                    int stopErrorDate = line.indexOf("{")-1;

                    // Расчитываем индексы интервала с путем к bsh-файлу
                    String bshPathFile = new String();
                    bshPathFile = line.substring(line.indexOf("{"), line.indexOf("}")+1);
                    int startBshPath = bshPathFile.indexOf("{")+1;
                    int stopBshPath = bshPathFile.lastIndexOf("/")+1;

                    // Расчитываем индексы интервала с названием bsh-файла
                    int startBshFile = stopBshPath;
                    int stopBshFile = bshPathFile.indexOf(".bsh")+4;

                    // Расчитываем индексы интервала с названием sql-файла, при выполнении которого возникла ошибка
                    int stopSql = line.lastIndexOf(".sql")+4;
                    int startSql = line.substring(0, stopSql).lastIndexOf("/")+1;

                    // Записываем необходимую информацию в массив
                    arr[errorsCount][0] = dateConvert(line.substring(startErrorDate, stopErrorDate)); // Датавремя возникновения ошибки
                    arr[errorsCount][1] = bshPathFile.substring(startBshPath, stopBshPath);           // Путь к исполняемому bsh-файлу
                    arr[errorsCount][2] = bshPathFile.substring(startBshFile, stopBshFile);           // Наименование исполняемого bsh-файла
                    arr[errorsCount][3] = line.substring(startSql, stopSql);                          // Наименование исполняемого sql-файла

                    errorsCount++;
                }
            }
        }
        finally {
            scanner.close();
        }
        printMessage("There are "+Integer.toString(errorsCount)+" errors in log.");
        printMessage("Errors search stopped!");
    }

    //  Поиск макисмального индекса строки действительной ошибки в массиве с максимально-возможным количеством ошибок
//  cnt - максимальное количество строк в массиве с ошибками
//  arr - собственно сам массив с ошибками
    private static int countRealErrors(int cnt, String[][] arr) {
        int y = cnt-1;
        for(int x = 0; x <= y; x++) {
            if (arr[x][0] != null) {
                cnt = x;
            }
        }
        return cnt;
    }

    //  Вытаскиваем файл с максимальной датой модификации, если их несколько в каталоге
//  inputFiles - массив файлов .out в каталоге
    private static File lastModifiedFile(File[] inputFiles) {
        // Мэп со значением модификации файла как ключа и самим файлом, как значением
        //Map<Long, File> filesMap = new HashMap<Long, File>(); // jdk environment
        Map filesMap = new HashMap();  // balabit/beanshell environment
        for (File s : inputFiles) {
            filesMap.put(s.lastModified(), s);
        }

        //ArrayList<Long> datesAL = new ArrayList<>(filesMap.keySet()); // jdk environment
        ArrayList datesAL = new ArrayList(filesMap.keySet()); // balabit/beanshell environment
        datesAL.sort(Collections.reverseOrder()); // Сортировка по ключу по убыванию
        File fileOutput = filesMap.get(datesAL.get(0));

        return fileOutput;
    }

    //  Определение сервера запуска скрипта в файле .out
//  inputFile - файл.out из каталога output c информацией о ходе выполнения регламентного расчета
//  key       - ключевое слово, после которого идет упоминание сервера выполнения расчета
    private static String serverName(File inputFile, String key) throws FileNotFoundException {
        String result = "not found";
        Scanner scanner = new Scanner(inputFile);
        try {
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.contains(key)) {
                    String serverInfo = new String();
                    serverInfo = line.substring(line.indexOf(key), line.lastIndexOf(key)+25);
                    int startServerName = serverInfo.indexOf(key)+11;
                    int stopServerName = serverInfo.lastIndexOf(":")-1;
                    result = serverInfo.substring(startServerName, stopServerName);
                }
            }
        }finally {
            scanner.close();
        }
        return result;
    }

    //  Определение текста ошибки в файле .out
//  inputFile - файл.out из каталога output c информацией о ходе выполнения регламентного расчета
//  key       - массив ключевых слов, с которых начинается текст ошибок
    private static String errorText(File inputFile, String[] key) throws FileNotFoundException {
        String result = "";

        for(String s:key) {
            Scanner scanner = new Scanner(inputFile);
            try {
                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if(line.contains(s)) {
                        String errorInfo = new String();
                        errorInfo = line.substring(line.indexOf(s));
                        if(!result.contains(errorInfo)) {
                            result += errorInfo;
                        }
                    }
                }
            }finally {
                scanner.close();
            }
        }
        return result;
    }

    //  Поиск и обработка каталогов output, а также запись результатов про сервер отработки и саму ошибку в массив с ошибками
//  path     - путь к каталогам output
//  arr      - собственно сам массив с ошибками
//  iter     - номер строки в массиве с ошибками
//  key      - ключевое слово в файле.out после которого следует наименование сервера
//  mask     - маска файла .out в котором содержится информация о ходе выполнения расчета
//  keyError - массив ключевых слов, с которых начинается текст ошибок
    private static void outputExplorer(String path, String[][] arr, int iter, String key, String mask, String[] keyError) throws FileNotFoundException {
        String dateOutput = (arr[iter][0]).substring(0, 10).replace('-', '.'); // Каталог-дата отработки сервисов
        String bshOutput = (((arr[iter][1]+arr[iter][2])).replace('/', '!')); // Каталог c файлами .out конкретного bsh-сервиса

        File file = new File(path+dateOutput+"/"+bshOutput);
        File x = null;

        if(file.listFiles() != null) {
            // Вытягиваем файл .out с максимальной датой модификации чтобы избежать старых ошибок. Только новые ошибки.
            for (File s : file.listFiles()) {
                if (s.isFile()) {
                    if (s.getName().contains(mask)) {
                        x = lastModifiedFile(file.listFiles()); // Получаем файл с максимальным значением дата/время модификации
                    }
                }
            }
            arr[iter][4] = serverName(x,key);     // Получаем имя сервера и заливаем его в массив с ошибками
            arr[iter][5] = errorText(x,keyError); // Получаем текст ошибки
        }
    }

    //  Сохраняет данные массива с параметрами ошибок в файл для последующей заливки данной информации на витрину
//  arr -               собственно сам массив с ошибками
//  errorFilePathName - Путь и название файла с данными про ошибки
    private static void saveToFile(String[][] arr, String errorFilePathName) throws IOException {
        FileWriter errorFile = new FileWriter(errorFilePathName);
        String delimiter = "][";

        printMessage("Start export error data to file.");
        for (int x = 0; x < arr.length; x++) {
            if(arr[x][0] != null) {
                errorFile.write(arr[x][0]+           // Датавремя возникновения ошибки
                        delimiter+
                        arr[x][1]+           // Путь к исполняемому bsh-файлу
                        delimiter+
                        arr[x][2]+           // Наименование исполняемого bsh-файла
                        delimiter+
                        arr[x][3]+           // Наименование исполняемого sql-файла
                        delimiter+
                        arr[x][4]+           // Наименование сервера, на котором возникла ошибка
                        delimiter+
                        arr[x][5]+"\n");     // Текст ошибки
            }
        }
        errorFile.close();
        printMessage("Export error data to file finished!");
//        sendMailGeneral("anton.potapenko@privatbank.ua", "ErrCatcher!", " Ошибка в можеть быть перезапущена автоматически.\n\n" + ex);
    }




    main() {
        execDate = formatDT("yyyyMMdd");
        scriptsName = bsh.sysinfo.getScriptName();
        setStartExecutionTime(execDate, scriptsName);

        try{
            Date date = new Date();
            SimpleDateFormat currentDateTime = new SimpleDateFormat("yyyyMMdd HH:mm:ss.mmm");

            // Параметры на balabit
            String logSourceFilePath = "/DATA/Gremlin/log/"; // Путь к файлу лога на сервере
            String logSourceFileName = "GremlinLog.log"; // Название файла лога на сервере
            String logDestFilePath   = "/DATA/script/tmp/IT_REGLAMENT/ERROR_CATCHER_DATA_RP/"; // Путь к копии файла лога
            String logDestFileName   = "GremlinLog.log"; // Название копии файла лога
            String outputCatalog     = "/DATA/Gremlin/output/"; // Путь к каталогу с файлами output
            String errorFile         = "/DATA/script/tmp/IT_REGLAMENT/ERROR_CATCHER_DATA_RP/ERROR_CATCHER_DATA_RP.TXT"; // Путь и название файла с данными про ошибки
            String errorFilePath     = "/DATA/script/tmp/IT_REGLAMENT/ERROR_CATCHER_DATA_RP/"; // Путь к файлу с данными про ошибки
            String errorFileName     = "ERROR_CATCHER_DATA_RP.TXT";  // Название файла с данными про ошибки
            String keyServerName     = "start SQL"; // Ключевое слово в файле.out после которого следует наименование сервера
            String outputFileMask    = ".out"; // Маска файла .out в котором содержится информация о ходе выполнения расчета
            String[] errorTextKey    = {"Could not","Error!"}; // Ключевые слова, с которых начинается текст ошибок



            //-------------------------------------------------------------------------------------------------------
            // Этап 1. Копирование лог-файла Gremlin в рабочий каталог
            //-------------------------------------------------------------------------------------------------------

            File logFile = new File(logSourceFilePath,logSourceFileName); //Лог файл Gremlin на текущую дату
            File mylogFile = new File(logDestFilePath,logDestFileName);   //Лог файл в рабочем каталоге для обработки
            // Проверяем наличие файла на сервере, если есть - копируем в рабочий каталог
            if(logFile.exists()) {
                copyLogFile(logFile, mylogFile);
            }
            else {
                printMessage("File doesn't exist on server!");
            }

            //-------------------------------------------------------------------------------------------------------
            // Этап 2. Парсинг log-файла и его удаление после обработки
            //-------------------------------------------------------------------------------------------------------

            int countErrors = countLines(mylogFile);
            String[][] arrErrors = new String[countErrors][6]; // Динамический массив с параметрами ошибок.

            // Если копия файла лога есть - ищем ошибки
            if(mylogFile.exists()) {
                errorSearch(mylogFile,arrErrors);
                deleteFile(mylogFile);
            }
            else {
                printMessage("File doesn't exist on directory!");
            }

            //-------------------------------------------------------------------------------------------------------
            // Этап 3. Парсинг каталога output
            //-------------------------------------------------------------------------------------------------------

            countErrors = countRealErrors(countErrors,arrErrors); // Переопределяем параметр c количеством действительных ошибок

            if (countErrors != 0) {
                printMessage("Start search errors in output catalog.");
                // Итерация в output по ошибкам из log
                for (int x = 0; x <= countErrors; x++) {
                    outputExplorer(outputCatalog,arrErrors,x,keyServerName,outputFileMask,errorTextKey);
                }
                printMessage("Search errors in output catalog finished!");

                //-------------------------------------------------------------------------------------------------------
                // Этап 4. Заливка данных с ошибками в файл и последующая передача этих данных на витрину
                //-------------------------------------------------------------------------------------------------------

                saveToFile(arrErrors,errorFile); // Сохраняем массив с ошибками в файл

                // Заливаем файл на витрину
                for(file : listFullPath( errorFilePath, errorFileName ) ) {
                    sql_runner3("lpar_1", DS + "IT_REGLAMENT/calc/LOAD.IT_REGLAMENT.dn210481pav1.ERROR_CATCHER_DATA_RP_lpar_1.sql " + file,3,lpar_timeout);
                    remove(file);
                }
                printMessage("Data Mart updated!");
            }
            printMessage("That's all Folks!");
            sendMailGeneral("anton.potapenko@privatbank.ua", "normid_ins_dop.bsh", "Сервис normid_ins_dop.bsh можеть быть перезапущен автоматически. Ваш ErrCatcher\n\n" + ex);
        }
        catch( RuntimeException ex ){
            sendMailGeneral("anton.potapenko@privatbank.ua", "ERROR in ErrCatcher", "Ошибка при выполнении.\n\n" + ex);
            return false;
        }

        setFinishExecutionTime(execDate, scriptsName);
        return true;
    }

return main();

}
