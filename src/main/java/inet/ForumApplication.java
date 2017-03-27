package inet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ForumApplication {

	@Bean
	public FilterRegistrationBean jwtFilterRegistration() {
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter());
		registrationBean.addUrlPatterns("/user/all");
		registrationBean.addUrlPatterns("/user/role/*");
		registrationBean.addUrlPatterns("/thread/*");
		registrationBean.addUrlPatterns("/post/*");

		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}
}
