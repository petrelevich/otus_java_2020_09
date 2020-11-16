package ru.otus.factorymethod2;

import ru.otus.factorymethod.Configuration;
import ru.otus.factorymethod.ConfigurationFile;

class ConfigurationFactoryFile extends ConfigurationFactory  {
  @Override
  Configuration buildConfiguration() {
    return new ConfigurationFile();
  }
}
