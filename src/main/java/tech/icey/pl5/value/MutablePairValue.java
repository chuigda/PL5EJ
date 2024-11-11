package tech.icey.pl5.value;

public final class MutablePairValue {
    private Object car;
    private Object cdr;

    public MutablePairValue(Object car, Object cdr) {
        this.car = car;
        this.cdr = cdr;
    }

    public Object car() {
        return car;
    }

    public Object cdr() {
        return cdr;
    }

    public void setCar(Object car) {
        this.car = car;
    }

    public void setCdr(Object cdr) {
        this.cdr = cdr;
    }
}
