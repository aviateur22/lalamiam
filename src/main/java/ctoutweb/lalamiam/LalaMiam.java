package ctoutweb.lalamiam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LalaMiam {
  public static void main(String[] args) {

//    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//    context.register(CalculateDetailCommandHelper.class);
//    context.register(CommandServiceHelper.class);
//    context.register(DatabaseSourceConfig.class);
//    context.refresh();
  SpringApplication.run(LalaMiam.class, args);
  }
}
