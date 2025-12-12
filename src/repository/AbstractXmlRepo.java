package repository;

import domain.Id;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import service.XmlListWrapper;

import java.io.File;
import java.io.FileReader;

public abstract class AbstractXmlRepo<ID, T extends Id<ID>> extends FilteredRepo<ID, T> {

    protected final String fileName;
    protected final Class<T> entityClass;

    public AbstractXmlRepo(String fileName, Class<T> entityClass) {
        this.fileName = fileName;
        this.entityClass = entityClass;
        readFromFile();
    }

    protected abstract Class<? extends XmlListWrapper<T>> getWrapperClass();

    protected void readFromFile() {
        try (FileReader reader = new FileReader(fileName)) {
            JAXBContext context = JAXBContext.newInstance(getWrapperClass(), entityClass);
            Unmarshaller um = context.createUnmarshaller();

            XmlListWrapper<T> wrapper = (XmlListWrapper<T>) um.unmarshal(reader);
            if (wrapper != null && wrapper.getList() != null) {
                wrapper.getList().forEach(super::save);
            }
        } catch (Exception e) {
            System.out.println("Could not read XML file. Starting empty: " + e.getMessage());
        }
    }

    protected void writeToFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(getWrapperClass(), entityClass);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            XmlListWrapper<T> wrapper = getWrapperClass().getDeclaredConstructor().newInstance();
            wrapper.setList(super.findAll());

            m.marshal(wrapper, new File(fileName));
        } catch (Exception e) {
            System.err.println("Error writing XML: " + e.getMessage());
        }
    }

    @Override
    public T save(T entity) {
        T result = super.save(entity);
        writeToFile();
        return result;
    }

    @Override
    public boolean deleteById(ID id) {
        boolean deleted = super.deleteById(id);
        if (deleted) {
            writeToFile();
        }
        return deleted;
    }
}