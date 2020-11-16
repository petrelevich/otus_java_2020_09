package ru.otus.factorymethod2;

import ru.otus.factorymethod.Configuration;
import ru.otus.factorymethod.ConfigurationDB;

class ConfigurationFactoryDB extends ConfigurationFactory {
  @Override
  Configuration buildConfiguration() {
    return new ConfigurationDB();
  }
}
