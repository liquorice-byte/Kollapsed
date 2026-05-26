module it.liquorice.kollapsed {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires atlantafx.base;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fluentui;
    requires org.slf4j;

    requires kotlinx.serialization.core;
    requires kotlinx.serialization.json;

    opens it.liquorice.kollapsed to javafx.fxml;
    exports it.liquorice.kollapsed;
}