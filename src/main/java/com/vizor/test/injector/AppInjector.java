package com.vizor.test.injector;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.vizor.test.controller.ImageController;
import com.vizor.test.repository.ImageRepository;
import com.vizor.test.repository.impl.ImageRepositoryImpl;
import com.vizor.test.service.ImageService;
import com.vizor.test.service.impl.ImageServiceImpl;
import com.vizor.test.util.Cash;

public class AppInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(ImageController.class);
        bind(ImageService.class).to(ImageServiceImpl.class).in(Singleton.class);
        bind(ImageRepository.class).to(ImageRepositoryImpl.class).in(Singleton.class);
        bind(Cash.class).in(Singleton.class);
    }
}
