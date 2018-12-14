package com.music.structure;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    private static String txtSourceFile;
    private static String outRootDirectory;

    public static void main(String[] args) {
        createStructureWithArgs(args);
//        createStructureWithArgs(new String[]{"C:\\Users\\ali\\Favorites\\source\\HS1_10.00-13.00.txt" ,"C:\\Users\\ali\\Documents\\music"});
    }

    private static void createStructureWithArgs(String[] args) {
        if (args.length < 2 || args[0] == null || args[1] == null) {
            System.out.println("input path to source txt and path to RootDirectory ");
            return;
        }
        txtSourceFile = args[0];
        outRootDirectory = args[1];
        System.out.println("started work with " + txtSourceFile);
        ArrayList<String> paths = new ArrayList<String>();
        ArrayList<String> finalPaths = new ArrayList<String>();
        ArrayList<String> newPaths;

        try {
            readFile(paths);
            newPaths = createNewPath(paths);

            copyFilesToRootFolder(paths, newPaths, finalPaths);
            createPlayListTxt(finalPaths);
            System.out.println("DONE! =)");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void createPlayListTxt(ArrayList<String> paths) {
        BufferedWriter writer = null;
        try {
            final int index = txtSourceFile.lastIndexOf("\\");
            final String playlistName = txtSourceFile.substring(index + 1);
            File file = new File(outRootDirectory + "\\playlist\\");

            if (!file.exists()) {
                file.mkdirs();
            }
            File filePlaylist = new File(file, playlistName);
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePlaylist.getAbsolutePath()), "utf-8"));
            for (String line : paths) {
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
            writer.close();
            System.out.println("new playlist " + playlistName + " created in " + filePlaylist.getAbsolutePath());
        } catch (FileNotFoundException e) {
            System.out.println("createPlayListTxt FileNotFoundException" + e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("createPlayListTxt UnsupportedEncodingException" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("createPlayListTxt IOException" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                // ignore
            }
        }

    }

    private static void copyFilesToRootFolder(ArrayList<String> paths,
                                              ArrayList<String> newPaths,
                                              ArrayList<String> finalPaths) {
        for (int i = 0; i < paths.size(); i++) {
            File source = new File(paths.get(i));
            File dest = new File(newPaths.get(i));
            dest.mkdirs();
            try {
                final String finalPath = dest.toPath() + "\\" + source.getName();
                finalPaths.add(finalPath);
                Files.copy(source.toPath(), (new File(finalPath)).toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("copy " + source.toPath() + " to " + finalPath);
            } catch (IOException e) {
                System.out.println("copyFilesToRootFolder IOException" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static ArrayList<String> createNewPath(final ArrayList<String> paths) {
        ArrayList<String> newPathList = new ArrayList<>();
        for (String path : paths) {
            final String cuttedString = path.substring(3);
            final int index = cuttedString.lastIndexOf("\\");
            final String cuttedString2 = cuttedString.substring(0, index);
            newPathList.add(outRootDirectory + "\\" + cuttedString2);
        }
        return newPathList;
    }

    private static void readFile(ArrayList<String> sb) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(txtSourceFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException for " + txtSourceFile);
            e.printStackTrace();
        }
    }
}