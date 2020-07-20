package org.ckCoder.models;

import java.io.File;
import java.util.Set;

public class Book extends AbstractEntity {

    private String title;
    private String description;
    private Category category;
    private Double price;
    private String type;
    private String fileName;
    private String imgName;
    private Integer nbVue;
    private Integer editionYear;
    private Integer valeurNominal;
    private Integer valeurCritique;
    private Set<AbstractEntity> authors;
    private Boolean availability;

    private File imgfile;
    private File bookfile;

    private byte[] imgBinary;
    private byte[] bookBinary;

    public Book() {
        availability = true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public Integer getNbVue() {
        return nbVue;
    }

    public void setNbVue(Integer nbVue) {
        this.nbVue = nbVue;
    }

    public Integer getEditionYear() {
        return editionYear;
    }

    public void setEditionYear(Integer editionYear) {
        this.editionYear = editionYear;
    }

    public Integer getValeurNominal() {
        return valeurNominal;
    }

    public void setValeurNominal(Integer valeurNominal) {
        this.valeurNominal = valeurNominal;
    }

    public Integer getValeurCritique() {
        return valeurCritique;
    }

    public void setValeurCritique(Integer valeurCritique) {
        this.valeurCritique = valeurCritique;
    }

    public Set<AbstractEntity> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AbstractEntity> authors) {
        this.authors = authors;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public File getImgfile() {
        return imgfile;
    }

    public void setImgfile(File imgfile) {
        this.imgfile = imgfile;
    }

    public File getBookfile() {
        return bookfile;
    }

    public void setBookfile(File bookfile) {
        this.bookfile = bookfile;
    }

    public byte[] getImgBinary() {
        return imgBinary;
    }

    public void setImgBinary(byte[] imgBinary) {
        this.imgBinary = imgBinary;
    }

    public byte[] getBookBinary() {
        return bookBinary;
    }

    public void setBookBinary(byte[] bookBinary) {
        this.bookBinary = bookBinary;
    }
}
