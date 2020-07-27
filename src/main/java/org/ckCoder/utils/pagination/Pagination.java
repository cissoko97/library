package org.ckCoder.utils.pagination;

import java.util.Collection;

public class Pagination<T>{
    Ressource<T> ressource;

    public Pagination(Collection<T> valeur, int totalElement, int totalPage) {
        ressource = new Ressource<T>();
        ressource.resource = valeur;
        ressource.info.totalPage = totalPage;
        ressource.info.totalElement = totalElement;
    }

    public Ressource<T> getRessource() {
        return ressource;
    }
}
