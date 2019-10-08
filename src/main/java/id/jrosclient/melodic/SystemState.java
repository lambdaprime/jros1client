package id.jrosclient.melodic;

import java.util.List;

public class SystemState extends Response {

    List<Publisher> publishers;
    List<SubScriber> subscribers;
    List<Service> services;
}
