package com.vizor.test.service;

import com.vizor.test.model.Image;

import java.io.File;
import java.util.List;

public interface ImageService {

    /**
     * Add image.
     *
     * @param file the file
     */
    void addImage(File file);

    /**
     * Gets number of pages.
     *
     * @return the number of pages
     */
    Integer getNumberOfPages();

    /**
     * Gets image list.
     *
     * @param currentPage the current page
     * @return the image list
     */
    List<Image> getImageList(Integer currentPage);

    /**
     * Gets image list by name.
     *
     * @param currentPage the current page
     * @param name        the name
     * @return the image list by name
     */
    List<Image> getImageListByName(Integer currentPage, String name);
}
