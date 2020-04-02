package Repositories;

import Domain.Entity;
import Validator.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


    public abstract class AbstractRepository<ID, E extends Entity<ID>> implements CRUDRepository<ID, E> {
        private String fileName;
        private String tagname;
        private Map<ID, E> treeMap;


        AbstractRepository(String fileName, String tagname) {
            this.fileName = fileName;
            this.tagname=tagname;
            treeMap = new TreeMap<>();
            loadfromXMLfile(tagname);
        }


        public ID autoid()
        {
            ID ret = null;
            for(ID id: treeMap.keySet())
            {
                ret=id;
            }
            return ret;
        }

        public abstract E parseline(Element e);

        public abstract Node toxml(Document doc, E entity);

        @Override
        public E findOne(ID id) {
            if (id == null) throw new IllegalArgumentException("Id-ul este vid");
            E entity = treeMap.get(id);
            if (entity == null)
                throw new ValidationException("Nu exista entitatea");
            return entity;
        }

        @Override
        public Iterable<E> findAll() {
            return treeMap.values();
        }

        @Override
        public E save(E entity) {
            if (entity == null)
                throw new IllegalArgumentException("Entitate vida!");
            if (!treeMap.containsKey(entity.getId()))
                treeMap.put(entity.getId(), entity);
            else throw new ValidationException("ID-ul exista deja!");
            writetofile(tagname);
            return null;
        }

        @Override
        public E delete(ID id) {
            if (id == null)
                throw new IllegalArgumentException("ID-ul este vid!");
            if (treeMap.get(id) == null)
                throw new ValidationException("Entitatea nu exista!");
            E ent = treeMap.remove(id);
            writetofile(tagname);
            return ent;
        }

        @Override
        public E update(E entity) {
            if (entity == null)
                throw new IllegalArgumentException("Entitate vida");
            ID id = entity.getId();
            if (id == null)
                throw new ValidationException("Entitatea nu exista");
            E existingent = treeMap.get(id);
            if (existingent != null) {
                treeMap.put(id, entity);
                writetofile(tagname);
                return null;
            } else
                return entity;
        }


        private void loadfromXMLfile(String tagname)
        {
            try{
                File xmlFile=new File(fileName);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc=builder.parse(xmlFile);

                doc.getDocumentElement().normalize();
                String root=doc.getDocumentElement().getNodeName();

                NodeList nodeList=doc.getElementsByTagName(tagname);
                for(int i=0;i<nodeList.getLength();i++)
                {
                    Node node=nodeList.item(i);

                    if(node.getNodeType()== Node.ELEMENT_NODE)
                    {
                        Element elem=(Element) node;
                        E entity=parseline(elem);
                        save(entity);

                    }
                }

            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
            }
        }

        private void writetofile(String tagname)
        {
            try{
                DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
                DocumentBuilder db=factory.newDocumentBuilder();
                Document doc=db.newDocument();

                File myFile=new File(fileName);
                String oldroot=db.parse(myFile).getDocumentElement().getNodeName();

                Element root=doc.createElement(oldroot);
                doc.appendChild(root);

                for(E ent: findAll())
                {
                    root.appendChild(toxml(doc,ent));
                }
                TransformerFactory transformerFactory=TransformerFactory.newInstance();
                Transformer transformer=transformerFactory.newTransformer();

                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                DOMSource source = new DOMSource(doc);

                //    StreamResult console=new StreamResult(System.out);
                StreamResult file= new StreamResult(myFile);
                //    transformer.transform(source, console);
                transformer.transform(source, file);

            } catch (ParserConfigurationException | TransformerException e)  {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


