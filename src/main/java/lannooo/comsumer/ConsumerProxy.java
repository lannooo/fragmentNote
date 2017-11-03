package lannooo.comsumer;

import lannooo.data.Record;

import java.util.ArrayList;
import java.util.List;

public class ConsumerProxy implements Consumer{

    public static ConsumerProxy instance;
    static {
        instance = new ConsumerProxy();
    }
    private List<Consumer> consumers;

    public ConsumerProxy(){
        consumers = new ArrayList<>();
    }

    public static ConsumerProxy getInstance(){
        return instance;
    }

    public ConsumerProxy registerConsumer(Consumer consumer){
        this.consumers.add(consumer);
        return this;
    }

    @Override
    public boolean accept(final Record record) {
        for(Consumer consumer: consumers){
            consumer.accept(record);
        }
        return true;
    }
}
