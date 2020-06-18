module me.mjakubowski {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.jena.core;
    requires shacl;
    requires org.apache.jena.shacl;
    requires org.apache.jena.arq;

    opens me.mjakubowski to javafx.fxml;
    exports me.mjakubowski;
}