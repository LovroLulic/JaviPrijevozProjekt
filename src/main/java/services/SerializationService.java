package services;
import java.io.*;


public class SerializationService {
    private static final String BACKUP_PATH="backup.bin";

    public static void kreirajBackup(BackupData backupData){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BACKUP_PATH))){
            oos.writeObject(backupData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static BackupData ucitajBackup(){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BACKUP_PATH))){
            return (BackupData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
