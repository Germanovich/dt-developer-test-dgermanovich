package com.vizor.test.service.impl;

import com.vizor.test.model.Image;
import com.vizor.test.repository.ImageRepository;
import com.vizor.test.resource.ConfigurationManager;
import com.vizor.test.service.ImageService;
import com.vizor.test.util.Cash;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageServiceImpl implements ImageService {

    private static final Logger LOGGER = LogManager.getLogger();
    @Inject
    private ImageRepository imageRepository;
    @Inject
    private Cash cash;

    @Override
    public void addImage(File file) {
        String name = file.getName().split("\\.", 2)[0];
        try {
            imageRepository.addImage(name, ImageIO.read(file));
        } catch (IOException e) {
            LOGGER.warn("Can't read file", e);
        }
    }

    @Override
    public Integer getNumberOfPages() {
        Integer rows = getRows(cash.getSearchText());
        Integer numberOfPages = rows / Integer.parseInt(ConfigurationManager.getProperty("app.recordsPerPage"));
        if (rows < Integer.parseInt(ConfigurationManager.getProperty("app.recordsPerPage"))) {
            return ++numberOfPages;
        }
        if (rows % numberOfPages > 0) {
            numberOfPages++;
        }
        return numberOfPages;
    }

    @Override
    public List<Image> getImageList(Integer currentPage) {
        return imageRepository.getList(currentPage);
    }

    @Override
    public List<Image> getImageListByName(Integer currentPage, String name) {
        return imageRepository.getListByName(currentPage, name);
    }

    private Integer getRows(String name) {
        Integer rows;
        String searchText = cash.getSearchText();
        if (searchText == null || searchText.isEmpty()) {
            rows = imageRepository.getNumberOfRows();
        } else {
            rows = imageRepository.getNumberOfRowsByName(name);
        }
        if (rows == 0) {
            return 1;
        }
        return rows;
    }
}
