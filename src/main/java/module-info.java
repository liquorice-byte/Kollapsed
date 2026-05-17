module it.liquorice.kollapsed {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;

    requires atlantafx.base;

    opens it.liquorice.kollapsed to javafx.fxml;
    exports it.liquorice.kollapsed;
}