package org.annpurna.wallet.cli.api.handler;

import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.models.*;

import io.swagger.models.auth.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class Bootstrap extends HttpServlet {
  @Override
  public void init(ServletConfig config) throws ServletException {
    Info info = new Info()
      .title("Swagger Server")
      .description("Annpurna - Cli Web Services.")
      .termsOfService("https://www.fssplibray.org/terms.html")
      .contact(new Contact()
        .email("atri.avneesh@gmail.com"))
      .license(new License()
        .name("Proprietary License")
        .url("https://www.annpurna.org"));

    ServletContext context = config.getServletContext();
    Swagger swagger = new Swagger().info(info);

    new SwaggerContextService().withServletConfig(config).updateSwagger(swagger);
  }
}
