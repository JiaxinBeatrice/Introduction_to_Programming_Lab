package java_final;

public class Client {
	public static void main(String[] args){
		WithWeb web_client = new WithWeb("127.0.0.1",2016, false);
		web_client.go();		
	}
}
