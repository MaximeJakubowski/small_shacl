module me.mjakubowski {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.jena.core;
    requires shacl;

    opens me.mjakubowski to javafx.fxml;
    exports me.mjakubowski;
}