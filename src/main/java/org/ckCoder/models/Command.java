package org.ckCoder.models;

import java.util.Set;

public class Command extends AbstractEntity {

    private Double TotalPrice;
    private User user;
    private Boolean accepted;
    private Set<Line> lines;

    public Command() {
        accepted = false;
    }

    public Double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        TotalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Line> getLines() {
        return lines;
    }

    public void setLines(Set<Line> lines) {
        this.lines = lines;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }
}
