package com.vizor.test.controller;

import com.vizor.test.model.Image;
import com.vizor.test.service.ImageService;
import com.vizor.test.util.Cash;

import javax.inject.Inject;
import java.io.File;
import java.util.List;

public class ImageController {

    @Inject
    private ImageService imageService;
    @Inject
    private Cash cash;

    /**
     * Add image.
     *
     * @param file the file
     */
    public void addImage(File file) {
        imageService.addImage(file);
    }

    /**
     * Gets number of pages.
     *
     * @return the number of pages
     */
    public Integer getNumberOfPages() {
        return imageService.getNumberOfPages();
    }

    /**
     * Gets image list.
     *
     * @return the image list
     */
    public List<Image> getImageList() {
        String searchName = cash.getSearchText();
        if (searchName != null && !searchName.isEmpty()) {
            return imageService.getImageListByName(cash.getPageNumber(), searchName);
        }
        return imageService.getImageList(cash.getPageNumber());
    }
}
