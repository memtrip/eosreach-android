import com.memtrip.eosreach.service.MainVerticle;
import io.vertx.core.Vertx;

public class Entry {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
    }
}
