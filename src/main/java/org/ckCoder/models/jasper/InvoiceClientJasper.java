package org.ckCoder.models.jasper;

import lombok.Getter;
import lombok.Setter;
import org.ckCoder.models.Book;
import org.ckCoder.models.Command;
import org.ckCoder.models.Person;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class InvoiceClientJasper {
    private String date;
    private List<Book> bookList = new ArrayList<>();
    private List<Command> commands = new ArrayList<>();
    private List<Person> personList = new ArrayList<>();
}
