package me.mjakubowski;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileUtils;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.util.ModelPrinter;
import org.topbraid.shacl.validation.ValidationUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Validator {
    public static String validate(String shaclgraph, String datagraph) {
        Model dataModel = parseTurtle(datagraph);
        Model shaclModel = parseTurtle(shaclgraph);

        Resource report = ValidationUtil.validateModel(dataModel, shaclModel, true);
        Model reportModel = report.getModel();
        reportModel.setNsPrefix("sh", "http://www.w3.org/ns/shacl#");
        return ModelPrinter.get().print(report.getModel());
    }

    private static Model parseTurtle(String turtle) {
        Model m = JenaUtil.createMemoryModel();
        InputStream s = stringToInputStream(turtle);
        m.read(s, "urn:dummy", FileUtils.langTurtle);
        return m;
    }

    private static InputStream stringToInputStream(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }
}
