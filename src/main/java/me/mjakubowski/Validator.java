package me.mjakubowski;

import org.apache.jena.graph.Graph;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RiotException;
import org.apache.jena.shacl.ShaclValidator;
import org.apache.jena.util.FileUtils;
import org.topbraid.jenax.util.JenaUtil;
import org.topbraid.shacl.util.ModelPrinter;
import org.topbraid.shacl.validation.ValidationUtil;
import org.apache.jena.shacl.Shapes;
import org.apache.jena.shacl.ValidationReport;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

public class Validator {
    public static String validate(String shaclgraph, String datagraph, String backend) throws DataParseException, ShapeParseException {
        Model dataModel;
        Model shaclModel;
        try {
            dataModel = parseTurtle(datagraph);
        } catch (RiotException e) {
            throw new DataParseException(e.getMessage());
        }

        try {
            shaclModel = parseTurtle(shaclgraph);
        } catch (RiotException e) {
            throw new ShapeParseException(e.getMessage());
        }

        Model reportModel;
        switch (backend) {
            case "TQ":
                reportModel = generateReportTQ(dataModel, shaclModel);
                break;
            case "JENA":
                reportModel = generateReportJena(dataModel, shaclModel);
                break;
            default:
                throw new RuntimeException("Illegal backend specified: use TQ or JENA");
        }

        reportModel.setNsPrefix("sh", "http://www.w3.org/ns/shacl#");
        StringWriter s = new StringWriter();
        RDFDataMgr.write(s, reportModel, Lang.TTL);
        return s.toString();
    }

    public static Model generateReportTQ(Model dataModel, Model shaclModel) {
        Resource report = ValidationUtil.validateModel(dataModel, shaclModel, true);
        return report.getModel();
    }

    public static Model generateReportJena(Model dataModel, Model shaclModel) {
        Shapes shapes = Shapes.parse(shaclModel);
        ValidationReport report = ShaclValidator.get().validate(shapes, dataModel.getGraph());
        return report.getModel();
    }

    private static Model parseTurtle(String turtle) {
        Model m = ModelFactory.createDefaultModel() ;
        InputStream s = stringToInputStream(turtle);
        m.read(s, "urn:dummy", "TURTLE");
        return m;
    }

    private static InputStream stringToInputStream(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }
}
