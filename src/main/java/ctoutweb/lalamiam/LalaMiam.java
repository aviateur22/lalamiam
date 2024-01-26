package ctoutweb.lalamiam;

import ctoutweb.lalamiam.config.DatabaseSourceConfig;
import ctoutweb.lalamiam.helper.CalculateDetailCommandHelper;
import ctoutweb.lalamiam.helper.CommandServiceHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
