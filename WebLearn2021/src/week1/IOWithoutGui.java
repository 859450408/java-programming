package week1;

import javafx.stage.FileChooser;

import java.io.*;
import java.util.Scanner;

public class IOWithoutGui {
    private PrintWriter pw = null;
    private Scanner sc = null;

    public IOWithoutGui() {
    }

    /*
    写文本文件最常用的类是PrintWriter，PrintWriter类中有一个常用的println(msg)方法，向输出流中输出一行字符，并
    自动添加一个行结束符:\n。注意，在使用PrintWriter类时，其虽然可以将文件名作为参数来写文件，但这种用法不能提供追加模式的，所以一般
    是和FileOutputStream联合使用，例如：

    new PrintWriter(new FileOutputStream("temp.txt", true));

    如果还需要指定文本的编码，并且是追加模式，可以这样联合使用：

new PrintWriter(
    new OutputStreamWriter(
        new FileOutputStream("temp.txt",true),  "UTF-8"));

     */
    public void appendTest(File file, String msg){
        try {
            pw = new PrintWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file, true),"utf-8"));
            pw.println(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pw.close();
        }
    }

    public String loadTest(File file){
        StringBuilder sb = new StringBuilder();
        try {
            //读和写的编码要注意保持一致
            sc = new Scanner(file,"utf-8");
            while (sc.hasNext()) {
                sb.append(sc.nextLine() + "\n"); //补上行读取的行末尾回车
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
        return sb.toString();
    }

}
