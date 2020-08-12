package org.ckCoder.utils;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class UtilForArray {

    public static ImageView getImageView(int dimension, byte[] bytes) {
        InputStream file = new ByteArrayInputStream(bytes);
        Image image = new Image(file);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(dimension);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        return imageView;
    }

    public static void setwidthBtn(TableColumn colBtn) {
        colBtn.setMaxWidth(68);
        colBtn.setMinWidth(68);
    }

    public static boolean isIntegr(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isDouble(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static <K, V> List<V> createListFromMapEntries(Map<K, V> map){
        return map.values().stream().collect(Collectors.toList());
    }
}
