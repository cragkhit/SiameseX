package echotest;

public class EchoTest {
    private String lastEcho;

    public EchoTest()
    {
        lastEcho = "no echo";
    }

    public String echoString(String message)
    {
        lastEcho = message;
        return "Echo back to you -> " + lastEcho;
    }

    public String getLastMessage()
    {
        return lastEcho;
    }
}
