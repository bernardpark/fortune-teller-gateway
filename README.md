# Fortune Teller MicroServices - Fortune UI

## Overview
This repository is a microservice of the larger [Fortune Teller Application](https://github.com/msathe-tech/fortune-teller) guided as a workshop. This application is a gateway instance that routes all your traffic to the correct application components.

## Spring Cloud Gateway
This application will act as a Spring Cloud Gateway instance that routes traffice to either the `fortune-teller-ui` or the `fortune-teller-service`. For example, visiting the gateway's endpoint in a browser will redirect you to the ui's endpoint, and adding an `/api` path to the gateway's endpoint will redirect to the service's endpoint.

## Spring Cloud Configuration
You should also notice the spring-cloud-services-starter-config-client dependency. This allows your application to bind to the Pivotal Cloud Config Server service to consumer external configuration.

### Coding Exercise 1 - Make your Application a Configuration Client
Let's have this application consume the URLs of its underlying applications through a remote configuration server. We will be using the Pivotal Cloud Config service to consume from a Github repository. To enable your application as a Configuration Client, annotate the `Application.java` class with `@EnableDiscoveryClient`.

**Application.java**

```
@SpringBootApplication
@EnableDiscoveryClient
public class Application {
...
```
Save your file.

### Coding Exercise 2 - Consume an External Configuration
Now that we can consume external configuration, let's do so in a properties class. In your `GatewayProperties.java` class, add the `@ConfigurationProperties` annotation while referring to prefix `url`. This will tell your class to consume configuration as a Configuration Client application, and apply the prefix `url` as default. You should notice that your configuration repository follows this property prefix. In particular, notice the `url.apiURL` and `url.uiURL` properties. These are the same as the class variables that are provided for you.

Also make sure you add the `@RefreshScope` annotation. This will allow you to refresh this bean with updated configuration by posting to the Spring Actuator refresh endpoint.

**GatewayProperties.java**

```
@ConfigurationProperties(prefix = "service")
@RefreshScope
public class GatewayProperties {
...
```

Save your file.

### Coding Exercise 3 - Create Routes
Take a look at the `RouteConfiguration.java` class. Here, you want to create routes to redirect traffic to the correct [Fortune UI](https://github.com/bernardpark/fortune-teller-ui) and [Fortune API](https://github.com/bernardpark/fortune-teller-api) components. Notice that as the single point of entry, this gateway will only provide access to the applications that should be consumed directly by the user.

Begin by making this class a Spring Configuration Bean, by annotating the class with `@Configuration`. Also add the `@EnableConfigurationProperties` annotation with reference to the `GatewayProperties.class` we just wrote. 

**RouteConfiguration.java**

```
@ConfigurationProperties(prefix = "service")
@RefreshScope
public class RouteConfiguration {
...
```

Now, autowire the `GatewayProperties` bean and create a new method, `customRouteLocator(RouteLocatorBuilder builder)` to return a custom configuration bean, annotated with `@Bean`. Have this new method return routes generated with the `RouteLocatorBuilder` so that all traffic to the gateway ending in `"$GATEWAY_URL/"` to be redirected to the [Fortune UI](https://github.com/bernardpark/fortune-teller-ui) and `"$GATEWAY_URL/api"` to the [Fortune API](https://github.com/bernardpark/fortune-teller-api). Below is a sample of the code to add.

**RouteConfiguration.java**

```
...
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("fortune_teller_api", r -> r
						.path("/api/**")
						.filters(f -> f.rewritePath("/api/(?<path>.*)", "/$\\{path}"))
						.uri(gatewayProperties.getApiURL()))
				.route("fortune_teller_ui", r -> r
						.path("/**")
						.uri(gatewayProperties.getUiURL()))
				.build();
	}
...
```

Save your file.

## Deploying the Application
1. Build your applications with Maven

```
mvn clean package
```

1. Create the necessary services on Pivotal Cloud Foundry. For this application, we will need a Config Server and a RabbitMQ instance (RabbitMQ will act as a service bus so that `/actuator/refresh` posts can be cascaded to all applications with `@RefreshScope` beans. If you don't already have a Service Registry, create that too.

One thing to note is that the Config Server service needs to be created with parameters to consume a backend git repository. You can do so by adding a JSON text with the `-c` flag.

```
# Repeat for all required services

# View available services
cf marketplace
# View service details
cf marketplace -s $SERVICE_NAME
# Create the service (config server)
cf create-service $SERVICE_NAME $SERVICE_PLAN $YOUR_SERVICE_NAME -c '{"git": { "uri": "https://github.com/$GITHUB/$REMOTE_CONFIG_REPO", "label": "$BRANCH" } }'
# Create the service (all others)
cf create-service $SERVICE_NAME $SERVICE_PLAN $YOUR_SERVICE_NAME
```
1. Draft your `manifest.yml` in the root directory. Note that the variables, enclosed in double parentheses (()), will contain the key of each variable. We will create the variable file shortly.

```
---
applications:
- name: ((app_name))
  memory: 1024M
  path: ./target/fortune-teller-gateway-0.0.1-SNAPSHOT.jar
  instances: 1
  services:
  - ((config_server))
  - ((service_registry))
  - ((cloud_bus))
  env:
    TRUST_CERTS: ((cf_trust_certs))
```

1. Draft your `vars.yml` file in the root directory. Notice that the keys to all variables are referenced in the `manifest.yml` file we just created. You will also need to know your PCF API endpoint. You can find this by visiting Apps Manager -> Tools -> `Login to the CLI` box, or by running the command `cf api | head -1 | cut -c 25-`.

```
app_name: $YOUR_APP_NAME
config_server: $YOUR_CONFIG_SERVICE_NAME
service_registry: $YOUR_SERVICE_REGISTRY_NAME
cloud_bus: $YOUR_CLOUD_BUS_NAME
cf_trust_certs: $YOUR_PCF_API_ENDPOINT
```

1. Push your application.

```
cf push
```

Examine the manifest.yml file to review the application deployment configurations and service bindings.

## Test the application

### Test redirection to the UI
1. Visit `https://$YOUR_GATEWAY_ENDPOINT`
1. Notice the redirect to `https://$YOUR_UI_ENDPOINT`

### Test redirection to the service
1. Visit `https://$YOUR_GATEWAY_ENDPOINT/api/random`
1. Notice the redirect to `https://$YOUR_SERVICE_ENDPOINT/api/random`

## Return to Workshop Respository
[Fortune Teller Workshop](https://github.com/msathe-tech/fortune-teller#lab5-implement-a-gateway)

## Authors
* **Bernard Park** - [Github](https://github.com/bernardpark)
* **Madhav Sathe** - [Github](https://github.com/msathe-tech)
