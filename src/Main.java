import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    static StringBuilder logs = new StringBuilder();
    public static void main(String[] args) {


        String[] directoriesNames = {"C://Games/src", "C://Games/res", "C://Games/savegames",
                "C://Games/temp", "C://Games/src/main", "C://Games/src/test",
                "C://Games/res/drawables", "C://Games/res/vectors", "C://Games/res/icons"};

        String[] fileNames = {"C://Games/src/Main.java", "C://Games/src/Utils", "C://Games/temp/temp.txt"};
        File main = new File("C://Games/src", "Main.java");
        createDirectory(directoriesNames);
        createFile(fileNames);

        GameProgress gp1 = new GameProgress(100,1,1,0.0);
        GameProgress gp2 = new GameProgress(40,1,3,100.0);
        GameProgress gp3 = new GameProgress(80,4,10,1000.25);
        saveGame("C://Games/savegames/savegame1.dat", gp1);
        saveGame("C://Games/savegames/savegame2.dat", gp2);
        saveGame("C://Games/savegames/savegame3.dat", gp3);
        String[] files = {"C://Games/savegames/savegame1.dat", "C://Games/savegames/savegame2.dat", "C://Games/savegames/savegame3.dat"};
        zipFiles("C://Games/savegames/archive.zip", files);
        File savegame1 = new File("C://Games/savegames", "savegame1.dat");
        File savegame2 = new File("C://Games/savegames", "savegame2.dat");
        File savegame3 = new File("C://Games/savegames", "savegame3.dat");
        if(savegame1.delete() && savegame2.delete() && savegame3.delete()){
            logs.append("All non-archieved files were deleted!");
            try(FileWriter fileWriter = new FileWriter("C://Games/temp/temp.txt")) {
                fileWriter.write(logs.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        openZip("C://Games/savegames/archive.zip", "C://Games/savegames/");
        System.out.println(openProgress("C://Games/savegames/savegame3.dat"));




    }
    public static void addLog(StringBuilder logs, String log) {
        logs.append(log);

    }
    public static void createDirectory(String[] directoriesNames) {
        for(String name : directoriesNames) {
            if (new File(name).mkdir()) {
                addLog(logs, "Directory " + name + " is created! \n");
            } else {
               addLog(logs, "Something went wrong with " + name);
            }
        }
    }
    public static void createFile(String[] filesNames) {
        for(String name : filesNames) {
            try {
                if(new File(name).createNewFile()) {
                    addLog(logs, "File " + name + " is created! \n");
                } else {
                    addLog(logs, "Something went wrong with " + name);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void saveGame(String path, GameProgress gp) {
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            objectOutputStream.writeObject(gp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void zipFiles(String path, String[] filesToZip){
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(path))) {
            for(int i = 0; i < filesToZip.length; i++) {
                FileInputStream fileInputStream = new FileInputStream(filesToZip[i]);
                ZipEntry entry = new ZipEntry("savegame" + (i + 1) + ".dat");
                zipOutputStream.putNextEntry(entry);
                byte[] buffer = new byte[fileInputStream.available()];
                fileInputStream.read(buffer);
                zipOutputStream.write(buffer);
                zipOutputStream.closeEntry();
                fileInputStream.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void openZip(String archiveLocation, String path) {
        try(ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(archiveLocation))) {
            ZipEntry entry;
            String name;
            while ((entry = zipInputStream.getNextEntry()) != null){
                name = entry.getName();
                FileOutputStream fileOutputStream = new FileOutputStream(path + name);
                for(int c = zipInputStream.read(); c != -1; c = zipInputStream.read()) {
                    fileOutputStream.write(c);
                }
                fileOutputStream.flush();
                zipInputStream.closeEntry();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static GameProgress openProgress(String path) {
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path))) {
            return (GameProgress) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}