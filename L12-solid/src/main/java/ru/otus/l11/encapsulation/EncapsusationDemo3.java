package ru.otus.l11.encapsulation;

// Какие здесь проблемы?

class BadDepartment {
  private Company company;

  public Company getCompany() {
    return this.company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }
}

class BadDepartmentDemo {

  public static void main(String[] args) {
    Company company = new Company();
    BadDepartment department = new BadDepartment();
    department.setCompany(company);
  }
}

// -- см. код ниже BetterDepartment















class BetterDepartment {
  private Company company; // Что еще можно добавить, улучшить?

  BetterDepartment(Company company) {
    if (company == null)
      throw new IllegalArgumentException("company cannot be null");

    this.company = company;
  }

  public Company getCompany() {
    return this.company;
  }

  // сеттера нет специально
}

class BetterDepartmentDemo {

  public static void main(String[] args) {
    Company company = new Company();
    BetterDepartment department = new BetterDepartment(null);
  }
}


