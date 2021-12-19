package com.javalearn.first;
/*
*  File file = new File( "C:/java_projects/tmp/io.txt"); -- создаем обьект класса file и присаеваем путь к файлу
*  file.createNewFile(); -- создаем сам файл
*  PrintWriter pw = new PrintWriter(file); -- созадем объект класса PrintWriter с параметром File.
*  pw.println --пишем в файл
*  pw.close(); закрываем pwpw
*
*
* util.Scanner, util.ArrayList
* java.nio.channels.FileChannel, java.util.Scanner, java.io.File, java.util.Collections, java.util.HashMap
* */
import javax.management.ObjectName;
import java.io.*;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

public class first {
    public static void main(String[] args) {
/*        FileInputStream fis = null;
        int num = 0;
        BufferedReader br = null;
        FileReader fr = null;

        try {
            File file = new File( "C:/java_projects/tmp/io.txt");
            if(!file.exists())
                file.createNewFile();
            PrintWriter pw = new PrintWriter(file);
            for(int i=0; i<10; i++){
                pw.println( i );
            }
            pw.close();

            fis = new FileInputStream("C:/java_projects/tmp/io.txt");
            fr = new FileReader("C:/java_projects/tmp/io.txt");

            while ((num = fr.read()) != -1){
                System.out.print((char)num);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }*/

/*       public static String formatDT( Date date, String fmt ) {
            if( date == null ) {
                Date date = new Date();
            }
            SimpleDateFormat sfd = new SimpleDateFormat( fmt );
            return sfd.format( date );
        }

        String formatDT( String fmt ) {
            SimpleDateFormat sfd = new SimpleDateFormat( fmt );
            return sfd.format( new Date() );
        }*/

        // String pattern = "MM/dd/yyyy HH:mm:ss";

/*        DateFormat df = new SimpleDateFormat(pattern);
     //   Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);
        System.out.println("today : " + today);
        System.out.println("todayAsString : " + todayAsString);*/

        try {
            // Форматмы даты в классе DateFormat
            String patternDate = "yyyyMMdd";
            DateFormat df = new SimpleDateFormat(patternDate);

//            String patternFirstDate = "yyyyMM01";
//            DateFormat dfFirst = new SimpleDateFormat(patternFirstDate);

            String patternYear = "yyyy";
            DateFormat dfYear = new SimpleDateFormat(patternYear);

            String patternMonth = "MM";
            DateFormat dfMonth = new SimpleDateFormat(patternMonth);

//            String patternDay = "dd";
//            DateFormat dfDay = new SimpleDateFormat(patternDay);



            Calendar cDateFrom = new GregorianCalendar(2019, 12 - 1, 14);
            Date dateFrom = cDateFrom.getTime();
            String repDate1 = df.format(dateFrom);
            System.out.println("DATE_FROM : " + repDate1);


            Calendar cDateTo = new GregorianCalendar(2021, 10 - 1, 31);
            Date dateTo = cDateTo.getTime();
            String repDate2 = df.format(dateTo);
            System.out.println("DATE_TO : " + repDate2);

            System.out.println(dateFrom.compareTo(dateTo) <= 0);

            Calendar c1 = new GregorianCalendar();
            Calendar c2 = new GregorianCalendar();


////////////////////////////////////////////////////////////////////////

            while (cDateFrom.compareTo(cDateTo) <= 0) //
            {

                c1 = cDateFrom;
                c1.set(cDateFrom.DAY_OF_MONTH, 1);
                Date firstDate = c1.getTime();
                String repDate11 = df.format(firstDate);
                System.out.println(repDate11);

                c2 = c1;
                //c2.setTime(dateFrom.getTime());
                c2.add(Calendar.MONTH, 1);
                c2.add(Calendar.DAY_OF_MONTH, -1);
                Date secondDate = c2.getTime();
                String repDate22 = df.format(secondDate);
                System.out.println(repDate22);

                Date cdate = c2.getTime();
                String pyear = dfYear.format(cdate); //formatDT(cdate, "yyyy");
                String pmonth = dfMonth.format(cdate); //formatDT(cdate, "MM");
                System.out.println(pyear);
                System.out.println(pmonth);
                int pyearInt = Integer.parseInt(pyear);
                String scheme = "1";
                //int pyear1 = 2018;

                //System.out.println(pyearInt == 2018);

               if (pyearInt == 2018 || pyearInt == 2019) {
                    scheme = "lg190701kvs";
                } else if (pyearInt == 2020 || pyearInt == 2021) {
                    scheme = "PAN";
                }
                ;

                System.out.println(scheme);

/*                outFile = "/DATA/ftp/lg190701kvs/MY_UPLOAD/TXT/MY_UPLOAD.TXT";
                outFile = "'" + outFile + "'";
                System.out.println(outFile);
                sql_runner3("q1tower", "/DATA/ftp/lg190701kvs/MY_UPLOAD/OUTPUT.MY_UPLOAD_tower.sql " + repDate11 + " " + repDate22 + " " + scheme + " " + outFile, 3, lpar_timeout);
                esystem("/DATA/Gremlin/Client.sh q1tower.db.it.loc:2006 run /DATA/ftp/lg190701kvs/MY_UPLOAD/output_my_upload.bsh");

                inFile = "/DATA/ftp/lg190701kvs/MY_UPLOAD/LOAD_tmp";
                for (file:
                     listFullPath(inFile, "*.*")) {
                    sql_runner3("lpar_1", "/DATA/ftp/lg190701kvs/MY_UPLOAD/LOAD.MY_UPLOAD_lpar_1.sql " + file, 3, lpar_timeout);
                    remove(file);
                }
*/
                System.out.println("///////////  FINISH ITERATION ////////////");

                cDateFrom.add(Calendar.MONTH, 1);

            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
}


 //   }
//}