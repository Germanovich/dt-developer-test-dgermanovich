package com.vizor.test.model;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class Image {

    private String name;
    private BufferedImage bufferedImage;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets buffered image.
     *
     * @return the buffered image
     */
    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    /**
     * Sets buffered image.
     *
     * @param bufferedImage the buffered image
     */
    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Image)) {
            return false;
        }
        Image image = (Image) o;
        return getName().equals(image.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
