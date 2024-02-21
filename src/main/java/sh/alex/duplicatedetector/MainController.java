package sh.alex.duplicatedetector;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController {

    @FXML
    private ListView<String> directoryListView;
    @FXML
    private ListView<String> extensionListView;
    @FXML
    private TextArea messageTextArea;

    @FXML
    private void addDirectory(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            directoryListView.getItems().add(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void removeDirectory(ActionEvent event) {
        int selectedIndex = directoryListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            directoryListView.getItems().remove(selectedIndex);
        }
    }

    @FXML
    private void addExtension(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Введите расширение файла:");
        dialog.setTitle("Добавить расширение");
        dialog.showAndWait().ifPresent(extension -> extensionListView.getItems().add(extension));
    }

    @FXML
    private void removeExtension(ActionEvent event) {
        int selectedIndex = extensionListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            extensionListView.getItems().remove(selectedIndex);
        }
    }

    @FXML
    private void processFiles(ActionEvent event) {
        messageTextArea.clear();
        Map<String, List<String>> filesByHash = new HashMap<>();
        List<String> directories = directoryListView.getItems();
        List<String> extensions = extensionListView.getItems();
        int duplicateCount = 0;

        for (String directory : directories) {
            File dir = new File(directory);
            if (dir.isDirectory()) {
                processDirectory(dir, extensions, filesByHash);
            }
        }

        // Находим дубликаты
        for (List<String> fileList : filesByHash.values()) {
            if (fileList.size() > 1) {
                messageTextArea.appendText("Дубликаты: " + String.join(" ", fileList) + "\n");
                duplicateCount++;
            }
        }

        messageTextArea.appendText("Обработка окончена. Дубликатов: " + duplicateCount);
    }

    private void processDirectory(File directory, List<String> extensions, Map<String, List<String>> filesByHash) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && hasExtension(file.getName(), extensions)) {
                    try {
                        String hash = calculateMD5(file);
                        if (!filesByHash.containsKey(hash)) {
                            filesByHash.put(hash, new ArrayList<>());
                        }
                        filesByHash.get(hash).add(file.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean hasExtension(String fileName, List<String> extensions) {
        for (String extension : extensions) {
            if (fileName.toLowerCase().endsWith("." + extension.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private String calculateMD5(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        String md5 = DigestUtils.md5Hex(fis);
        fis.close();
        return md5;
    }
}