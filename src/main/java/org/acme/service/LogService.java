package org.acme.service;

import javax.enterprise.context.RequestScoped;

@RequestScoped // To make it work for requests bigger than 10KB, use "@ApplicationScoped" here
public class LogService {

    public void log(final String entity) {
        System.out.println("Entity size: " + entity.length());
    }
}
