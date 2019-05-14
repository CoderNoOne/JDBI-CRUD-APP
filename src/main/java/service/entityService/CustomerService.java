package service.entityService;

import lombok.RequiredArgsConstructor;
import model.Customer;
import repository.impl.CustomerRepository;
import validators.impl.CustomerValidator;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;

  private Customer createCustomer(String name, String surname, int age, String email) {
    return Customer.builder().name(name).surname(surname).age(age).email(email).build();
  }

  public boolean addCustomer(String name, String surname, int age, String email) {
    Customer customer = createCustomer(name, surname, age, email);
    boolean isValid = new CustomerValidator().validateEntity(customer);

    if (isValid) {
      customerRepository.add(customer);
    }
    return isValid;
  }

  public void deleteCustomer(final Integer id) {
    customerRepository.delete(id);
  }

  public void showAllCustomers() {
    customerRepository.findAll().forEach(System.out::println);
  }

  public Optional<Customer> findCustomerById(final Integer id) {
    return customerRepository.findById(id);
  }

  private void updateCustomer(Customer customer) {
    customerRepository.update(customer);
  }

  public boolean updateCustomerDetail(Integer id, String name, String surname, Integer age, String email) {

    Customer customer = Customer.builder().id(id).name(name).surname(surname).age(age).email(email).build();

    boolean isValid = new CustomerValidator().validateEntity(customer);

    if (isValid) {
      updateCustomer(customer);
    }
    return isValid;
  }

}
