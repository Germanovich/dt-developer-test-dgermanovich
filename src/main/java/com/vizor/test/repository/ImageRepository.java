package com.vizor.test.repository;

import com.vizor.test.model.Image;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * The interface ImageRepository.
 */
public interface ImageRepository {

    /**
     * Add image.
     *
     * @param name  the name
     * @param image the image
     */
    void addImage(String name, BufferedImage image);

    /**
     * Gets number of rows.
     *
     * @return the number of rows
     */
    Integer getNumberOfRows();

    /**
     * Gets number of rows by name.
     *
     * @param name the name
     * @return the number of rows by name
     */
    Integer getNumberOfRowsByName(String name);

    /**
     * Gets list.
     *
     * @param currentPage the current page
     * @return the list
     */
    List<Image> getList(Integer currentPage);

    /**
     * Gets list by name.
     *
     * @param currentPage the current page
     * @param name        the name
     * @return the list by name
     */
    List<Image> getListByName(Integer currentPage, String name);
}
