package net.myspring.basic.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by liuj on 2016/12/6.
 */
public class FileUtils extends org.apache.commons.io.FileUtils{

    public static void createFile(String filePath,List<String> fileContent) {
        File file = new File(filePath);
        File path = new File(file.getParent());
        path.mkdirs();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath)))  {
            for(String line:fileContent) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mkdirs(String filePath) {
        File file = new File(filePath);
        File path = new File(file.getParent());
        path.mkdirs();
    }
}
