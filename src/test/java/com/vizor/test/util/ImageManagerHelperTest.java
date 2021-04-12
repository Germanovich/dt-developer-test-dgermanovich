package com.vizor.test.util;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.awt.image.BufferedImage;

public class ImageManagerHelperTest {

    private BufferedImage image;

    @BeforeClass
    public void setUp() {
        image = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_RGB);
    }

    @AfterClass
    public void tearDown() {
        image = null;
    }

    @Test
    void ImageManagerHelper_addImage() {
        BufferedImage thumbnailImage = ImageManagerHelper.getThumbnailImage(image);
        Assert.assertEquals(thumbnailImage.getHeight(), 140);
        Assert.assertEquals(thumbnailImage.getWidth(), 300);
    }
}