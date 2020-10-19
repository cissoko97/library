package org.ckCoder.models.jasper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceClient {
    private String cmd_create;
    private Double cmd_totalprice;
    private String user_name;
    private Double book_price;
    private String book_title;
    private String person_createdAt;
}
