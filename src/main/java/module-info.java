module it.liquorice.kollapsed {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires kotlin.stdlib;

    requires atlantafx.base;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fluentui;
    requires org.slf4j;

    requires kotlinx.serialization.core;
    requires kotlinx.serialization.json;

    requires kotlinx.coroutines.core;
    requires kotlinx.coroutines.javafx;

    opens it.liquorice.kollapsed to javafx.fxml;
    exports it.liquorice.kollapsed;
}