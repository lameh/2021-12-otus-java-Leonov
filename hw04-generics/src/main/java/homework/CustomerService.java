package homework;

import java.util.*;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    private final TreeMap<Customer, String> customerStringMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));


    public Map.Entry<Customer, String> getSmallest() {
//        Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        final Map.Entry<Customer, String> firstEntry = customerStringMap.firstEntry();
        final Customer key = firstEntry.getKey();
        return Map.entry(
                new Customer(key.getId(), key.getName(), key.getScores()),
                firstEntry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> result = customerStringMap.higherEntry(customer);
        if (result == null) {
            Iterator<Map.Entry<Customer, String>> iterator = customerStringMap.entrySet().iterator();
            while (iterator.hasNext()) {
                if (customer.equals(iterator.next().getKey())) {
                    return iterator.hasNext() ? iterator.next() : null;
                }
            }
        }
        return result;
    }

    public void add(Customer customer, String data) {
        this.customerStringMap.put(customer, data);
    }
}
