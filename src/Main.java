import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        StringBuilder logs = new StringBuilder();

        String[] directoriesNames = {"src", "res", "savegames", "temp"};
        for(String name : directoriesNames) {
            if (new File("C://Games", name).mkdir()) {
                logs.append("Directory ").append(name).append(" is created ").append("in directory C://Games").append("\n");
            } else {
                logs.append("Something went wrong with ").append(name);
            }
        }
        String[] srcDirectoriesNames = {"main", "test"};
        for(String name : srcDirectoriesNames) {
            if (new File("C://Games/src", name).mkdir()) {
                logs.append("Directory ").append(name).append(" is created ").append("in directory C://Games/src").append("\n");
            } else {
                logs.append("Something went wrong with ").append(name);

            }
        }
        String[] resDirectoriesNames = {"drawables", "vectors", "icons"};
        for(String name : resDirectoriesNames) {
            if(new File("C://Games/res", name).mkdir()) {
                logs.append("Directory ").append(name).append(" is created ").append("in directory C://Games/res").append("\n");
            } else {
                logs.append("Something went wrong with ").append(name);
            }
        }
        File main = new File("C://Games/src", "Main.java");
        try {
            if(main.createNewFile()) {
                logs.append("File Main.java is created in directory C://Games/src" + "\n");
            } else {
                logs.append("Something went wrong with file Main.java");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File utils = new File("C://Games/src", "Utils.java");
        try {
            if(utils.createNewFile()) {
                logs.append("File Utils.java is created in directory C://Games/src" + "\n");
            } else {
                logs.append("Something went wrong with file Utils.java");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File temp = new File("C://Games/temp", "temp.txt");
        try {
            if(temp.createNewFile()) {
                logs.append("File temp.txt is created in directory C://Games/temp" + "\n");
            } else {
                logs.append("Something went wrong with file temp.txt");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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