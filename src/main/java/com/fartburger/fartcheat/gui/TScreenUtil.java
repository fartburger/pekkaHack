package com.fartburger.fartcheat.gui;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public interface TScreenUtil {


    public default void openup() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("http://www.example.com"));
    }
}
