package Repositories;

import Domain.Nota;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.time.LocalDate;

public class NotaRepo extends AbstractRepository<String, Nota> {
    public NotaRepo(String fileName, String tagname) {
        super(fileName, tagname);
    }


    @Override
    public Nota parseline(Element e) {
        String id = e.getAttribute("id");

        Node n1 = e.getElementsByTagName("val").item(0);
        int not = Integer.parseInt(n1.getTextContent());

        Node n2 = e.getElementsByTagName("data").item(0);
        String[] d = n2.getTextContent().split("-");
        LocalDate data = LocalDate.of(Integer.parseInt(d[0]), Integer.parseInt(d[1]), Integer.parseInt(d[2]));

        Node n3 = e.getElementsByTagName("profesor").item(0);
        String prof = n3.getTextContent();

        Node n4=e.getElementsByTagName("feedback").item(0);
        String feed=n4.getTextContent();

        Nota asig = new Nota(not, data, prof,feed);
        asig.setId(id);
        return asig;
    }


    private static Node createNotaElement(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    @Override
    public Node toxml(Document doc, Nota entity) {

        Element nota = doc.createElement("nota");

        nota.setAttribute("id",entity.getId());
        nota.appendChild(createNotaElement(doc,"val",Integer.toString(entity.getNota())));
        nota.appendChild(createNotaElement(doc,"data",entity.getData().toString()));
        nota.appendChild(createNotaElement(doc,"profesor",entity.getProfesor()));
        nota.appendChild(createNotaElement(doc,"feedback",entity.getFeedback()));
        return nota;
    }

    public String tofile(Nota nota) {
        return nota.getId() + ";" + nota.getNota() + ";" + nota.getData() + ";" + nota.getProfesor();
    }
}
