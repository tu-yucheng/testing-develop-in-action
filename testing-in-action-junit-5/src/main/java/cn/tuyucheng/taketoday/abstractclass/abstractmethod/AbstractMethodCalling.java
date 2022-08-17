package cn.tuyucheng.taketoday.abstractclass.abstractmethod;

public abstract class AbstractMethodCalling {

    public abstract String abstractFunc();

    public String defaultImpl() {
        String res = abstractFunc();
        return (res == null) ? "Default" : (res + " Default");
    }
}