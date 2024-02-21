module sh.alex.duplicatedetector {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.codec;


    opens sh.alex.duplicatedetector to javafx.fxml;
    exports sh.alex.duplicatedetector;
}