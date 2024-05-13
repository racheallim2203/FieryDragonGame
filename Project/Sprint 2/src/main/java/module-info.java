module com.example.fit3077 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fit3077 to javafx.fxml;
    exports com.example.fit3077;
    exports com.example.fit3077.cards;
    opens com.example.fit3077.cards to javafx.fxml;
}