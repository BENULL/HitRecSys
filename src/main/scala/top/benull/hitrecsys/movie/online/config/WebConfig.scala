package top.benull.hitrecsys.movie.online.config

/**
 * descriptions:
 *
 * author: benull
 * date: 2021 - 09 - 04 17:27
 */
import java.util.List

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.{EnableWebMvc, WebMvcConfigurer}

@Configuration
@EnableWebMvc
class WebConfig extends WebMvcConfigurer {

  override def configureMessageConverters(converters: List[HttpMessageConverter[_]]): Unit = {
    val conv = new MappingJackson2HttpMessageConverter();
    val mapper = JsonMapper.builder()
      .addModule(DefaultScalaModule)
      .build();
    conv.setObjectMapper(mapper);
    converters.add(conv);
  }
}
