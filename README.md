
<div style="display:flex; flex-direction:row; justify-content:center; align-items:center; align-content:center; justify-items:center; width:100%;">
    <img src="readme_assets/logo.svg" alt="Logo" style="height:100%; width:100%">
</div>

## About the project

**Showly** is a tool to function as a projector in places where these do not exist.

This is a java package that can extract slides from a PowerPoint presentation and display them in serveral web browsers in a LAN network.

A server device executes Showly, being connected in a LAN network. This creates a web server
that serves a web page with a simple web app that displays the slides.

## Built with

- Java 11
- [Maven](https://maven.apache.org/index.html)
- [Javalin](https://javalin.io/)
- [Apache POI](https://poi.apache.org/)
- [Handlebars.java](https://github.com/jknack/handlebars.java)

## Usage

For adding Showly to your project as a dependency you can add the following to your pom.xml if you are using Maven:

```xml
<dependency>
  <groupId>io.github.r0land013</groupId>
  <artifactId>showly</artifactId>
  <version>0.2.0</version>
</dependency>
```

If you are using Gradle, add this to your build.gradle:

```groovy
dependencies {
    implementation 'io.github.r0land013:showly:0.2.0'
}
```

For now, Showly support Power Point binary and xml formats. You can use Showly like this:

```Java
import io.github.r0land013.showly.Showly;
import io.github.r0land013.showly.ShowlyConfig;
import io.github.r0land013.showly.slides.Slide;


// Create a Showly instance with port and slide file path
ShowlyConfig config = new ShowlyConfig(8080, "slides.pptx");
Showly showly = new Showly(config);

// Extract slides and start web server
List<Silde> slides = showly.show();

// Stop server when done
showly.stop();
``` 

The Showly server will run on port `8080` and extract slides from slides.pptx.
To view slides, client devices can open a browser and go to:

`http://{Your IP address}:8080`

But if you want to test locally you can open this in your browser:

`http://localhost:8080`

## Contribuiting

If you wish to contribute with some ideas and bug fixes open an issue describing it.
If you have some great changes to add, please, open a Pull Request.

### Running Tests
Tests are written with JUnit. To run:

```bash
mvn test
```

## License

This project is under [MIT LICENSE](/LICENSE).

## Contact

You can find my contact info [here](https://r0land013.github.io/).