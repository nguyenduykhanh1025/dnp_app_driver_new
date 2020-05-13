package vn.com.irtech.eport.framework.config;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import static com.google.code.kaptcha.Constants.*;

/**
 * Verification code configuration
 * 
 * @author admin
 */
@Configuration
public class CaptchaConfig
{
    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean()
    {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // Whether there is a border, the default is true, we can set yes, no
        properties.setProperty(KAPTCHA_BORDER, "yes");
        // Verification code text character color The default is Color.BLACK
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        // Captcha image width defaults to 200
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        // Verification code image height is 50 by default
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
        // Captcha text character size default is 40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "38");
        // KAPTCHA_SESSION_KEY
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCode");
        // Verification code text character length default is 5
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        // Verification code text font style The default is new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        // Picture Style Watermark com.google.code.kaptcha.impl.WaterRipple Fisheye com.google.code.kaptcha.impl.FishEyeGimpy Shadow com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha getKaptchaBeanMath()
    {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // Whether there is a border, the default is true, we can set yes, no
        properties.setProperty(KAPTCHA_BORDER, "yes");
        // Border color Default is Color.BLACK
        properties.setProperty(KAPTCHA_BORDER_COLOR, "105,179,90");
        // Verification code text character color The default is Color.BLACK
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        // Captcha image width defaults to 200
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        // Verification code image height is 50 by default
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
        // Captcha text character size default is 40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "35");
        // KAPTCHA_SESSION_KEY
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");
        // Captcha text generator
        properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, "vn.com.irtech.eport.framework.config.KaptchaTextCreator");
        // Verification code text character spacing The default is 2
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");
        // Verification code text character length default is 5
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");
        // Verification code text font style The default is new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        // Verification code noise color The default is Color.BLACK
        properties.setProperty(KAPTCHA_NOISE_COLOR, "white");
        // Interference implementation class
        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        // Picture Style Watermark com.google.code.kaptcha.impl.WaterRipple Fisheye com.google.code.kaptcha.impl.FishEyeGimpy Shadow com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
