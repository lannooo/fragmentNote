package lannooo.comsumer;

import lannooo.data.Record;

public interface Consumer {
    public boolean accept(final Record record);
}
