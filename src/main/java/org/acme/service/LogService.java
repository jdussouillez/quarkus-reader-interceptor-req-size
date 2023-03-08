package org.acme.service;

import java.io.File;
import javax.enterprise.context.RequestScoped;

@RequestScoped // To make it work for requests bigger than 10KB, use "@ApplicationScoped" here
public class LogService {

    public void log(final File entity) {
        System.out.println("Entity size: " + entity.length());
    }
}
