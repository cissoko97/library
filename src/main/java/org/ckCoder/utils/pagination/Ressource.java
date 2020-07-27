package org.ckCoder.utils.pagination;

import java.util.ArrayList;
import java.util.Collection;

public class Ressource<T> {
    public Collection<T> resource = new ArrayList<>();
    public Info info;
    Ressource(){
        info = new Info();
    }
}
