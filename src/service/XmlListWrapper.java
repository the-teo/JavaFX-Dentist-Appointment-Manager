package service;

import java.util.ArrayList;
import java.util.List;

public class XmlListWrapper<T> {
    private List<T> list = new ArrayList<>();

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
