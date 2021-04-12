package com.vizor.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vizor.test.controller.ImageController;
import com.vizor.test.exception.ApplicationException;
import com.vizor.test.exception.BusinessException;
import com.vizor.test.injector.AppInjector;
import com.vizor.test.ui.GalleryFrame;
import com.vizor.test.util.Cash;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Integer WIDTH = 1024;
    private static final Integer HEIGHT = 768;

    public void run() {
        Injector injector = Guice.createInjector(new AppInjector());
        try {
            JFrame frame = new GalleryFrame("DT Developer Test",
                    WIDTH,
                    HEIGHT,
                    injector.getInstance(ImageController.class),
                    injector.getInstance(Cash.class));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        } catch (ApplicationException e) {
            LOGGER.error(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, e.getMessage());
        } catch (BusinessException e) {
            LOGGER.warn(e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main()::run);
    }
}
