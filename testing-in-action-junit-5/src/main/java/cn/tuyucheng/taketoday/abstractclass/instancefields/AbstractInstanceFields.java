package cn.tuyucheng.taketoday.abstractclass.instancefields;

public abstract class AbstractInstanceFields {
    protected int count;
    private boolean active = false;

    public abstract int abstractFunc();

    public String testFunc() {
        String response;
        if (count > 5)
            response = "Overflow";
        else
            response = active ? "Added" : "Blocked";
        return response;
    }
}