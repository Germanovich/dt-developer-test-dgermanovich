package com.vizor.test.ui;

import com.vizor.test.controller.ImageController;
import com.vizor.test.model.Image;
import com.vizor.test.resource.ConfigurationManager;
import com.vizor.test.util.Cash;
import com.vizor.test.util.ImageManagerHelper;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GalleryFrame extends JFrame {

    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    private static ImageController imageController;
    private static Cash cash;
    private final Integer width;
    private final Integer height;
    private final JTextField searchTextField;
    private final JLabel page;
    private final JButton searchButton;
    private final JButton addButton;
    private final JButton nextPageButton;
    private final JButton previousPageButton;
    private JPanel topPanel;
    private JPanel middlePanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JLabel loadingPanel;
    private boolean loaded = false;

    public GalleryFrame(String title, Integer width, Integer height, ImageController imageController, Cash cash) {
        super(title);
        GalleryFrame.imageController = imageController;
        GalleryFrame.cash = cash;
        this.width = width;
        this.height = height;
        searchTextField = new JTextField(15);
        searchButton = new JButton("Search");
        addButton = new JButton("Add");
        page = new JLabel(cash.getPageNumber().toString()
                .concat("\\")
                .concat(imageController.getNumberOfPages().toString()));
        nextPageButton = new JButton("Next");
        previousPageButton = new JButton("Previous");

        this.getRootPane().setBorder(BorderFactory.createMatteBorder(4,
                4,
                4,
                4,
                Color.DARK_GRAY));

        initLoadingPanel();
        initTopPanel();
        initMiddlePanel();
        initCenterPanel();
        initBottomPanel();

        initContainer();
        initActionListener();
    }

    private void initLoadingPanel() {
        loadingPanel = new JLabel();
        loadingPanel.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("view/loading.gif"))));
        loadingPanel.setVisible(false);
    }

    private void initTopPanel() {
        topPanel = new JPanel();
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(loadingPanel);
        topPanel.add(searchTextField);
        topPanel.add(searchButton);
    }

    private void initMiddlePanel() {
        middlePanel = new JPanel();
        middlePanel.setBackground(Color.DARK_GRAY);
        middlePanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 4, 4));

        JPanel gridPanel = new JPanel();
        gridPanel.setBackground(Color.DARK_GRAY);
        gridPanel.setLayout(new GridLayout(4, 1, 5, 5));
        gridPanel.add(addButton);
        middlePanel.add(gridPanel);
    }

    private void initCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setBackground(Color.DARK_GRAY);
        centerPanel.setLayout(new GridLayout(5, 5, 4, 4));
        List<Image> images = imageController.getImageList();
        initImageList(images);
    }

    private void initBottomPanel() {
        bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        bottomPanel.add(previousPageButton, BorderLayout.CENTER);
        bottomPanel.add(page, BorderLayout.CENTER);
        bottomPanel.add(nextPageButton, BorderLayout.CENTER);
    }

    private void initContainer() {
        Container container = this.getContentPane();
        container.setLayout(new BorderLayout(8, 6));
        container.setBackground(Color.DARK_GRAY);

        container.add(new JPanel());
        container.add(topPanel, BorderLayout.NORTH);
        container.add(middlePanel, BorderLayout.WEST);
        container.add(centerPanel);
        container.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void initActionListener() {
        previousPageButton.addActionListener(e -> {
            Integer numberOfPages = imageController.getNumberOfPages();
            Integer pageNumber = cash.getPageNumber();
            if (pageNumber > 1) {
                cash.setPageNumber(--pageNumber);
                page.setText(pageNumber.toString()
                        .concat("\\")
                        .concat(numberOfPages.toString()));
                centerPanel.removeAll();
                initImageList(imageController.getImageList());
            }
        });

        nextPageButton.addActionListener(e -> {
            Integer numberOfPages = imageController.getNumberOfPages();
            Integer pageNumber = cash.getPageNumber();
            if (pageNumber < numberOfPages) {
                cash.setPageNumber(++pageNumber);
                page.setText(pageNumber.toString()
                        .concat("\\")
                        .concat(numberOfPages.toString()));
                centerPanel.removeAll();
                initImageList(imageController.getImageList());
            }
        });

        searchButton.addActionListener(e -> {
            cash.setPageNumber(Integer.parseInt(ConfigurationManager.getProperty("app.startingPageNumber")));
            String searchName = searchTextField.getText();
            cash.setSearchText(searchName);
            List<Image> images = imageController.getImageList();
            page.setText(cash.getPageNumber().toString()
                    .concat("\\")
                    .concat(imageController.getNumberOfPages().toString()));
            centerPanel.removeAll();
            centerPanel.revalidate();
            initImageList(images);
        });

        addButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("PNG images", "png"));
            int ret = fileChooser.showDialog(null, "Save image");
            if (ret == JFileChooser.APPROVE_OPTION) {
                imageController.addImage(fileChooser.getSelectedFile());
            }
            centerPanel.revalidate();
        });
    }

    private void initImageList(List<Image> images) {
        if (!loaded) {
            loaded = true;
            loadingPanel.setVisible(true);
            lockAllButtons();
            THREAD_POOL.submit(() -> {
                for (Image image : images) {
                    initImage(image);
                }
                fillEmptySpaces(images);
                unlockAllButtons();
                loadingPanel.setVisible(false);
                centerPanel.revalidate();
                loaded = false;
            });
        }
    }

    private void initImage(Image image) {
        JButton button = new JButton();
        button.setIcon(new ImageIcon(ImageManagerHelper.getThumbnailImage(image.getBufferedImage())));
        button.addActionListener(e -> {
            JFrame frame = new JFrame(image.getName());
            JScrollPane scrollPane = new JScrollPane(new JLabel(new ImageIcon(image.getBufferedImage())));
            frame.add(scrollPane);
            frame.setMinimumSize(new Dimension(width, height));
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(2, 2));
        panel.add(button, BorderLayout.CENTER);
        panel.add(new JLabel(image.getName()), BorderLayout.SOUTH);
        centerPanel.add(panel);
    }

    private void fillEmptySpaces(List<Image> images) {
        int recordsPerPage = Integer.parseInt(ConfigurationManager.getProperty("app.recordsPerPage"));
        if (images.size() != recordsPerPage) {
            for (int number = images.size(); number < recordsPerPage; number++) {
                JPanel panel = new JPanel();
                panel.setBackground(centerPanel.getBackground());
                centerPanel.add(panel);
            }
        }
    }

    private void lockAllButtons() {
        searchButton.setEnabled(false);
        addButton.setEnabled(false);
        nextPageButton.setEnabled(false);
        previousPageButton.setEnabled(false);
    }

    private void unlockAllButtons() {
        searchButton.setEnabled(true);
        addButton.setEnabled(true);
        nextPageButton.setEnabled(true);
        previousPageButton.setEnabled(true);
    }
}
