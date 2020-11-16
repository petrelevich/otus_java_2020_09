package ru.otus.factorymethod2;

import ru.otus.factorymethod.Configuration;

public class DemoFactoryMethod {
  public static void main(String[] args) {
    // Пример:
    // У нас есть какая-то конфигурация
    // и мы хотим читать ее их разных мест (БД, файл и тд)

   // из файла
    //   каким-то образом получаем фабрику (как в предыдущем примере - простая фабрика в виде статического метода)
    ConfigurationFactory factory1 = ConfigurationFactory.getConfigurationFactory("file");
    // получаем конфигурацию
    Configuration config1 = factory1.buildConfiguration();
    System.out.println(config1.params());

    // из БД
    ConfigurationFactory factory2 = ConfigurationFactory.getConfigurationFactory("db");
    Configuration config2 = factory2.buildConfiguration();
    System.out.println(config2.params());

    // или еще откуда-то ...
    // ...
  }
}
