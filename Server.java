package java_final;

public class Server {
	public static void main(String[] args){
		WithWeb web_server = new WithWeb("127.0.0.1",2016, true);
		web_server.go();
	}

}
