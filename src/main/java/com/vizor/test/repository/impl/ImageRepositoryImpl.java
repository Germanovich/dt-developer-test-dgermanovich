package com.vizor.test.repository.impl;

import com.vizor.test.exception.ApplicationException;
import com.vizor.test.exception.BusinessException;
import com.vizor.test.model.Image;
import com.vizor.test.model.builder.ImageBuilder;
import com.vizor.test.repository.ImageRepository;
import com.vizor.test.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageRepositoryImpl implements ImageRepository {

    private static final String PATH = ConfigurationManager.getProperty("images.local");
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void addImage(String name, BufferedImage image) {
        try {
            File file = new File(PATH.concat("\\").concat(name).concat(".png"));
            File[] files = new File(PATH).listFiles(path -> {
                String imageName = path.getName();
                return path.isFile() &&
                        imageName.equals(name.concat(".png")) &&
                        imageName.endsWith(".png");
            });
            if (files != null && files.length != 0) {
                throw new BusinessException("Image with the same name already exists");
            }
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            throw new ApplicationException("Error", e);
        }
        LOGGER.info("Image saved");
    }

    @Override
    public Integer getNumberOfRows() {
        File[] files = new File(PATH)
                .listFiles(path -> {
                    String imageName = path.getName().toLowerCase();
                    return path.isFile() && imageName.endsWith(".png");
                });
        return files == null ? 0 : files.length;
    }

    @Override
    public Integer getNumberOfRowsByName(String name) {
        File[] files = new File(PATH).listFiles(path -> {
            String imageName = path.getName().toLowerCase();
            return path.isFile() &&
                    imageName.split("\\.", 2)[0].contains(name) &&
                    imageName.endsWith(".png");
        });
        return files == null ? 0 : files.length;
    }

    @Override
    public List<Image> getList(Integer currentPage) {
        File[] files = new File(PATH).listFiles(path -> {
            String imageName = path.getName().toLowerCase();
            return path.isFile() && imageName.endsWith(".png");
        });
        LOGGER.info("Received list");
        return initImageList(files, currentPage);
    }

    @Override
    public List<Image> getListByName(Integer currentPage, String name) {
        File[] files = new File(PATH).listFiles(path -> {
            String imageName = path.getName().toLowerCase();
            return path.isFile() &&
                    imageName.split("\\.", 2)[0].contains(name) &&
                    imageName.endsWith(".png");
        });
        LOGGER.info("Received list by name");
        return initImageList(files, currentPage);
    }

    private List<Image> initImageList(File[] files, Integer currentPage) {
        List<Image> images = new ArrayList<>();
        if (files == null) {
            return images;
        }
        int recordsPerPage = Integer.parseInt(ConfigurationManager.getProperty("app.recordsPerPage"));
        int count = currentPage > 1 ? (currentPage - 1) * recordsPerPage : 0;
        while (count < recordsPerPage * currentPage && count < files.length) {
            try {
                String name = files[count].getName().split("\\.", 2)[0];
                images.add(new ImageBuilder().setName(name)
                        .setBufferedImage(ImageIO.read(files[count]))
                        .build());
            } catch (IOException e) {
                throw new ApplicationException("Error while uploading images", e);
            }
            count++;
        }
        return images;
    }
}
